/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanguine.webpos.printing;

import static com.lowagie.text.pdf.PdfFileSpecification.url;

import java.awt.Dimension;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.webpos.bean.clsPOSBillDtl;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsDatabaseConnection;
import com.sanguine.webpos.util.clsPOSUtilityController;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
@Controller

public class clsJasperFormat9ForBill
{

	
	@Autowired
	clsPOSMasterService objMasterService;

	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	clsPrintingUtility objPrintingUtility;
	String strBillPrinterPort = "";
	
	@Autowired
	clsGlobalFunctions objGlobalFunctions;
    
	@Autowired
	clsPOSUtilityController objUtility;
	
	@Autowired
	clsDatabaseConnection  objDatabseCon;
	
    private SimpleDateFormat ddMMyyyyAMPMDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
    private DecimalFormat stdDecimalFormat = new DecimalFormat("######.##");
    private final String dashedLineFor40Chars = "  --------------------------------------";

    /**
     *
     * @param billNo
     * @param reprint
     * @param formName
     * @param transType
     * @param billDate
     * @param posCode
     * @param viewORprint
     */
    public void funGenerateBill(String billNo, String reprint,String transType, String posCode, String billDate , String clientCode, String strServerBillPrinterName, boolean isOriginal,HttpServletResponse response)
    {
	HashMap hm = new HashMap();
	billDate = billDate.split(" ")[0];

	DecimalFormat decimalFormat = new DecimalFormat("#.###");
	DecimalFormat decimalFormat0Decimal = new DecimalFormat("0");
	String Linefor5 = "  --------------------------------------";
	try
	{
	   // String deliveryCharge="0";
		clsSetupHdModel   objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
	    final DecimalFormat gDecimalFormat = objUtility.funGetGlobalDecimalFormatter(objSetupHdModel.getDblNoOfDecimalPlace());;
		final String gDecimalFormatString = objUtility.funGetGlobalDecimalFormatString(objSetupHdModel.getDblNoOfDecimalPlace());;
        String formName="Settlement";
		PreparedStatement pst=null;
        Connection  con=objDatabseCon.funOpenPOSCon("mysql");
	    String user = "";
        String billType = " ";

	    String billhd = "tblbillhd";
	    String billdtl = "tblbilldtl";
	    String billModifierdtl = "tblbillmodifierdtl";
	    String billSettlementdtl = "tblbillsettlementdtl";
	    String billtaxdtl = "tblbilltaxdtl";
	    String billDscFrom = "tblbilldiscdtl";
	    String billPromoDtl = "tblbillpromotiondtl";
	    String billComplDtl = "tblbillcomplementrydtl";

         String billSeriesNoFromFun=billNo;
	    String hdBillNo = billNo;
	    String dtlBillNos = "";
	    ArrayList<String> arrListBillNos = new ArrayList<String>();

	    arrListBillNos.add(hdBillNo);

	    String sqlBillNos = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos,a.dblGrandTotal "
		    + "from tblbillseriesbilldtl a "
		    + "where a.strHdBillNo='" + hdBillNo + "' "
		    + "and date(a.dteBillDate)='" + billDate + "' ";
	    ResultSet rsDtlBillNos = objDatabseCon.funGetResultSet(sqlBillNos);
	    if (rsDtlBillNos.next())
	    {
		dtlBillNos = rsDtlBillNos.getString(4);
	    }
	    rsDtlBillNos.close();
	    String[] arrDtlBills = dtlBillNos.split(",");
	    for (int i = 0; i < arrDtlBills.length; i++)
	    {
		if (arrDtlBills[i].trim().length() > 0)
		{
		    arrListBillNos.add(arrDtlBills[i]);
		}
	    }

	    final String ORDER = "FLH";
	    Comparator<String> billNoSorting = new Comparator<String>()
	    {
		@Override
		public int compare(String o1, String o2)
		{
		    return ORDER.indexOf(o1.charAt(0)) - ORDER.indexOf(o2.charAt(0));
		}
	    };

	    Collections.sort(arrListBillNos, billNoSorting);

	    /**
	     * Solution 2
	     */
	    HashMap headerJasperParameters = new HashMap();
	    HashMap bodyJasperParameters = new HashMap();
	    HashMap footerJasperParameters = new HashMap();
            
            String sqlUser=" select a.strKOTUser from "+billdtl+" a where a.strBillNo ='"+billNo+"'"
		         + " order by a.dteBillDate asc "
		         + " limit 1 ";
	    ResultSet rsUserData = objDatabseCon.funGetResultSet(sqlUser);
	    if (rsUserData.next())
	    {
		headerJasperParameters.put("username", rsUserData.getString(1));
		
	    }
	    
	    List<clsPOSBillDtl> listOfFooterDtl = new ArrayList<>();
	    String sqlFooter = "select a.strBillFooter,a.strBillFooterStatus "
		    + "from tblsetup a  "
		    + "where (a.strPOSCode='" + posCode + "' or a.strPOSCode='All') ";
	    ResultSet rsFooter = objDatabseCon.funGetResultSet(sqlFooter);
	    if (rsFooter.next())
	    {
		clsPOSBillDtl objBillDtl = new clsPOSBillDtl();
		objBillDtl.setStrItemName(rsFooter.getString(1));
		listOfFooterDtl.add(objBillDtl);
	    }
	    rsFooter.close();
	    footerJasperParameters.put("listOfFooterDtl", listOfFooterDtl);

	    String manualBillNo = "";
	    String sqlHeaderData = "select a.strBillNo,a.dteBillDate,a.strPOSCode,a.strSettelmentMode"
		    + ",a.dblSubTotal,a.dblDiscountAmt,a.dblTaxAmt,a.dblGrandTotal,a.strUserEdited,a.strManualBillNo  "
		    + "from " + billhd + " a "
		    + "where a.strBillNo='" + hdBillNo + "' "
		    + "and date(a.dteBillDate)='" + billDate + "' ";
	    ResultSet rsHeaderData = objDatabseCon.funGetResultSet(sqlHeaderData);
	    if (rsHeaderData.next())
	    {
		headerJasperParameters.put("user", rsHeaderData.getString(9));
		manualBillNo = rsHeaderData.getString(10);
	    }
	    rsHeaderData.close();

	    headerJasperParameters.put("BillType", billType);

	    headerJasperParameters.put("decimalFormaterForDoubleValue", gDecimalFormatString);
	    headerJasperParameters.put("decimalFormaterForIntegerValue", "0");

	    bodyJasperParameters.put("decimalFormaterForDoubleValue", gDecimalFormatString);
	    bodyJasperParameters.put("decimalFormaterForIntegerValue", "0");

	    footerJasperParameters.put("decimalFormaterForDoubleValue", gDecimalFormatString);
	    footerJasperParameters.put("decimalFormaterForIntegerValue", "0");


	    String customerCode = "";
	    boolean flag_DirectBiller = false;

	    if (clientCode.equals("117.001"))
	    {
		if (posCode.equals("P01"))
		{
		    headerJasperParameters.put("posWiseHeading", "THE PREM'S HOTEL");
		}
		else if (posCode.equals("P02"))
		{
		    headerJasperParameters.put("posWiseHeading", "SWIG");
		}
	    }

	    if (clientCode.equals("239.001"))
	    {
		headerJasperParameters.put("posWiseHeading", "URBO");
	    }

	    boolean isReprint = false;
	    if(isOriginal)
	    {
		headerJasperParameters.put("duplicate", "[ORIGINAL]");
	    }
	  
	    if ("reprint".equalsIgnoreCase(reprint))
	    {
		isReprint = true;
		headerJasperParameters.put("duplicate", "[DUPLICATE]");
	    }
	    if (transType.equals("Void"))
	    {
		headerJasperParameters.put("voidedBill", "VOIDED BILL");
	    }

	    boolean flag_isHomeDelvBill = false;
	    String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 "
		    + "from tblhomedelivery where strBillNo=? ;";
	    pst = con.prepareStatement(SQL_HomeDelivery);
	    pst.setString(1, billNo);
	    ResultSet rs_HomeDelivery = pst.executeQuery();

	    List<clsPOSBillDtl> listOfHomeDeliveryDtl = new ArrayList<>();
	    clsPOSBillDtl objBillDtl = new clsPOSBillDtl();

	    if (rs_HomeDelivery.next())
	    {
		flag_isHomeDelvBill = true;

		if (manualBillNo.trim().length() > 0)
		{
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("ORDER NO  : " + manualBillNo);
		    listOfHomeDeliveryDtl.add(objBillDtl);
		}

		if (objSetupHdModel.getStrPrintHomeDeliveryYN().equalsIgnoreCase("Y"))
		{
		    billType = "HOME DELIVERY";
		}
		customerCode = rs_HomeDelivery.getString(2);

		String SQL_CustomerDtl = "";

		if (rs_HomeDelivery.getString(5).equals("Temporary"))
		{
		    SQL_CustomerDtl = "select a.strCustomerName,a.strTempAddress,a.strTempStreet"
			    + " ,a.strTempLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
			    + " from tblcustomermaster a left outer join tblbuildingmaster b "
			    + " on a.strBuldingCode=b.strBuildingCode "
			    + " where a.strCustomerCode=? ;";
		}
		else if (rs_HomeDelivery.getString(5).equals("Office"))
		{
		    SQL_CustomerDtl = "select a.strCustomerName,a.strOfficeBuildingName,a.strOfficeStreetName"
			    + ",a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity,a.strOfficePinCode,a.longMobileNo "
			    + " from tblcustomermaster a "
			    + " where a.strCustomerCode=? ";
		}
		else
		{
		    SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName"
			    + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo "
			    + " from tblcustomermaster a left outer join tblbuildingmaster b "
			    + " on a.strBuldingCode=b.strBuildingCode "
			    + " where a.strCustomerCode=? ;";
		}
		pst = con.prepareStatement(SQL_CustomerDtl);
		pst.setString(1, rs_HomeDelivery.getString(2));
		ResultSet rs_CustomerDtl = pst.executeQuery();
		while (rs_CustomerDtl.next())
		{
		    StringBuilder fullAddress = new StringBuilder();

		    headerJasperParameters.put("NAME", rs_CustomerDtl.getString(1));
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("NAME      : " + rs_CustomerDtl.getString(1).toUpperCase());
		    fullAddress.append(objBillDtl.getStrItemName());
		    listOfHomeDeliveryDtl.add(objBillDtl);

		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("ADDRESS : " + rs_CustomerDtl.getString(2).toUpperCase());
		    fullAddress.append(objBillDtl.getStrItemName());
		    listOfHomeDeliveryDtl.add(objBillDtl);

		    if (rs_CustomerDtl.getString(3).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDtl.getString(3).toUpperCase());//"Street    :" +
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    if (rs_CustomerDtl.getString(4).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDtl.getString(4).toUpperCase());//"Landmark    :" +
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    if (rs_CustomerDtl.getString(6).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDtl.getString(6).toUpperCase());//"City    :" +
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    if (rs_CustomerDtl.getString(7).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDtl.getString(7).toUpperCase());//"Pin    :" +
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    headerJasperParameters.put("FullAddress", fullAddress);

		    if (rs_CustomerDtl.getString(8).isEmpty())
		    {
			headerJasperParameters.put("MOBILE_NO", "");
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("MOBILE_NO  :" + " ");
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    else
		    {
			headerJasperParameters.put("MOBILE_NO", rs_CustomerDtl.getString(8));
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Mobile No    : " + rs_CustomerDtl.getString(8));
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		}
		rs_CustomerDtl.close();

		if (null != rs_HomeDelivery.getString(3) && rs_HomeDelivery.getString(3).trim().length() > 0)
		{
		    String[] delBoys = rs_HomeDelivery.getString(3).split(",");
		    StringBuilder strIN = new StringBuilder("(");
		    for (int i = 0; i < delBoys.length; i++)
		    {
			if (i == 0)
			{
			    strIN.append("'" + delBoys[i] + "'");
			}
			else
			{
			    strIN.append(",'" + delBoys[i] + "'");
			}
		    }
		    strIN.append(")");
		    String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode IN " + strIN + " ;";
		    pst = con.prepareStatement(SQL_DeliveryBoyDtl);

		    ResultSet rs_DeliveryBoyDtl = pst.executeQuery();
		    strIN.setLength(0);
		    for (int i = 0; rs_DeliveryBoyDtl.next(); i++)
		    {
			if (i == 0)
			{
			    strIN.append(rs_DeliveryBoyDtl.getString(1).toUpperCase());
			}
			else
			{
			    strIN.append("," + rs_DeliveryBoyDtl.getString(1).toUpperCase());
			}
		    }

		    if (strIN.toString().isEmpty())
		    {
			headerJasperParameters.put("DELV BOY", "");
		    }
		    else
		    {
			headerJasperParameters.put("DELV BOY", "Delivery Boy : " + strIN);
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Delivery Boy : " + strIN);
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    rs_DeliveryBoyDtl.close();
		}
		else
		{
		    headerJasperParameters.put("DELV BOY", "");
		}
	    }
	    rs_HomeDelivery.close();
	    int result = objPrintingUtility.funPrintTakeAwayForJasper(billhd, billNo);
	    if (result == 1)
	    {
		billType = "Take Away";
		String sql = "select a.strBillNo,a.dteBillDate,a.strCustomerCode,b.strCustomerName,b.longMobileNo "
			+ "from " + billhd + " a,tblcustomermaster b "
			+ "where a.strCustomerCode=b.strCustomerCode "
			+ "and a.strBillNo='" + billNo + "' "
			+ "and date(a.dteBillDate)='" + billDate + "' ";
		ResultSet rsCustomer = objDatabseCon.funGetResultSet(sql);
		if (rsCustomer.next())
		{
		    headerJasperParameters.put("NAME", rsCustomer.getString(4));
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("Name         : " + rsCustomer.getString(4).toUpperCase());
		    listOfHomeDeliveryDtl.add(objBillDtl);

		    headerJasperParameters.put("MOBILE_NO", rsCustomer.getString(5));
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("Mobile No    : " + rsCustomer.getString(5));
		    listOfHomeDeliveryDtl.add(objBillDtl);
		}
		rsCustomer.close();
	    }
	    if (objSetupHdModel.getStrPrintTaxInvoiceOnBill().equalsIgnoreCase("Y"))
	    {
		headerJasperParameters.put("TAX_INVOICE", "TAX INVOICE");
	    }
	    if (clientCode.equals("047.001") && posCode.equals("P03"))
	    {
		headerJasperParameters.put("ClientName", "SHRI SHAM CATERERS");
		String cAddr1 = "Flat No.7, Mon Amour,";
		String cAddr2 = "Thorat Colony,Prabhat Road,";
		String cAddr3 = " Erandwane, Pune 411 004.";
		String cAddr4 = "Approved Caterers of";
		String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
		headerJasperParameters.put("ClientAddress1", cAddr1 + cAddr2);
		headerJasperParameters.put("ClientAddress2", cAddr3 + cAddr4);
		headerJasperParameters.put("ClientAddress3", cAddr5);
	    }
	    else if (clientCode.equals("047.001") && posCode.equals("P02"))
	    {
		headerJasperParameters.put("ClientName", "SHRI SHAM CATERERS");
		String cAddr1 = "Flat No.7, Mon Amour,";
		String cAddr2 = "Thorat Colony,Prabhat Road,";
		String cAddr3 = " Erandwane, Pune 411 004.";
		String cAddr4 = "Approved Caterers of";
		String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
		headerJasperParameters.put("ClientAddress1", cAddr1 + cAddr2);
		headerJasperParameters.put("ClientAddress2", cAddr3 + cAddr4);
		headerJasperParameters.put("ClientAddress3", cAddr5);
	    }
	    else if (clientCode.equals("092.001") || clientCode.equals("092.002") || clientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
	    {
		headerJasperParameters.put("ClientName", "SSPL");
		headerJasperParameters.put("ClientAddress1", objSetupHdModel.getStrAddressLine1());
		headerJasperParameters.put("ClientAddress2", objSetupHdModel.getStrAddressLine2());
		headerJasperParameters.put("ClientAddress3", objSetupHdModel.getStrAddressLine3());
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    headerJasperParameters.put("ClientCity", objSetupHdModel.getStrCityName());
		}
	    }
	    else if (clientCode.equals("190.001"))
	    {
		String licenseName = objSetupHdModel.getStrClientName();
		if (billNo.startsWith("L"))//liqour bill license name
		{
		    licenseName = "STEP-IN-AGENCIES & ESTATE PVT LTD";
		}
		else
		{
		    licenseName = objSetupHdModel.getStrClientName();
		}

		headerJasperParameters.put("ClientName", licenseName);
		headerJasperParameters.put("ClientAddress1", objSetupHdModel.getStrAddressLine1());
		headerJasperParameters.put("ClientAddress2", objSetupHdModel.getStrAddressLine2());
		headerJasperParameters.put("ClientAddress3", objSetupHdModel.getStrAddressLine3());
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    headerJasperParameters.put("ClientCity", objSetupHdModel.getStrCityName());
		}
	    }
	    else if (clientCode.equals("224.001"))//"224.001", "XO ZERO LOUNGE"
	    {
		String licenseName = objSetupHdModel.getStrClientName();
		if (billNo.startsWith("F"))//Food bill license name
		{
		    licenseName = "XO ZERO LOUNGE";
		}
		else if (billNo.startsWith("L"))//Liquor bill license name
		{

		    headerJasperParameters.put("posWiseHeading", "XO ZERO LOUNGE");
		    licenseName = "WILDFIRE RESTAURANT AND BAR";
		}
		else if (billNo.startsWith("H"))//Hukka bill license name
		{
		    headerJasperParameters.put("posWiseHeading", "XO ZERO LOUNGE");
		    licenseName = "WILDFIRE RESTAURANT AND BAR";
		}
		else
		{
		    licenseName = objSetupHdModel.getStrClientName();
		}

		headerJasperParameters.put("ClientName", licenseName);
		headerJasperParameters.put("ClientAddress1", objSetupHdModel.getStrAddressLine1());
		headerJasperParameters.put("ClientAddress2", objSetupHdModel.getStrAddressLine2());
		headerJasperParameters.put("ClientAddress3", objSetupHdModel.getStrAddressLine3());
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    headerJasperParameters.put("ClientCity", objSetupHdModel.getStrCityName());
		}
	    }
	     else if (clientCode.equals("340.001"))//Chandrama
	    {
		String licenseName = objSetupHdModel.getStrClientName();
		if (billNo.startsWith("F"))//Food bill license name
		{
		    licenseName = "RUCHI";
		    headerJasperParameters.put("ClientName", licenseName);
		    headerJasperParameters.put("ClientAddress1", objSetupHdModel.getStrAddressLine1());
			headerJasperParameters.put("ClientAddress2", objSetupHdModel.getStrAddressLine2());
			headerJasperParameters.put("ClientAddress3", objSetupHdModel.getStrAddressLine3());
			    headerJasperParameters.put("TEL NO", String.valueOf("02026145350"));

		}
		else if (billNo.startsWith("L"))//Liquor bill license name
		{

		   // headerJasperParameters.put("posWiseHeading", "CHANDRAMA");
		    licenseName = "CHANDRAMA";
		    headerJasperParameters.put("ClientName", licenseName);
		    headerJasperParameters.put("ClientAddress1", "Opp Bund Garden");
		    headerJasperParameters.put("ClientAddress2", "Koregaon Park Pune-1");
		    headerJasperParameters.put("ClientAddress3","");
		    headerJasperParameters.put("TEL NO", objSetupHdModel.getStrTelephoneNo());
		}
		else
		{
		    licenseName = objSetupHdModel.getStrClientName();
		    headerJasperParameters.put("ClientName", licenseName);
		    headerJasperParameters.put("ClientAddress1", objSetupHdModel.getStrAddressLine1());
			headerJasperParameters.put("ClientAddress2", objSetupHdModel.getStrAddressLine2());
			headerJasperParameters.put("ClientAddress3", objSetupHdModel.getStrAddressLine3());
			headerJasperParameters.put("TEL NO", objSetupHdModel.getStrTelephoneNo());
		}
               
		

		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    headerJasperParameters.put("ClientCity", objSetupHdModel.getStrCityName());
		}
	    }
	    else
	    {
		headerJasperParameters.put("ClientName", objSetupHdModel.getStrClientName());
		headerJasperParameters.put("ClientAddress1", objSetupHdModel.getStrAddressLine1());
		headerJasperParameters.put("ClientAddress2", objSetupHdModel.getStrAddressLine2());
		headerJasperParameters.put("ClientAddress3", objSetupHdModel.getStrAddressLine3());
		
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    headerJasperParameters.put("ClientCity", objSetupHdModel.getStrCityName());
		}
		headerJasperParameters.put("pinCode", "-411 001");
	    }
            
	    if (!clientCode.equals("340.001"))
	    {
		headerJasperParameters.put("TEL NO", objSetupHdModel.getStrTelephoneNo());
	    }
	    headerJasperParameters.put("EMAIL ID",objSetupHdModel.getStrEmail());
	    headerJasperParameters.put("Line", Linefor5);

	    String query = "";
	    String SQL_BillHD = "";
	    String waiterName = "";
	    String waiterNo = "";
	    String tblName = "";
	    ResultSet rsQuery = null;
	    ResultSet rs_BillHD = null;
	    ResultSet rsTblName = null;
	    String sqlTblName = "";
	    String tabNo = "";
	    boolean flag_DirectBillerBlill = false;
	    boolean flgComplimentaryBill = false;
	    String sql = "select b.strSettelmentType from " + billSettlementdtl + " a,tblsettelmenthd b "
		    + " where a.strSettlementCode=b.strSettelmentCode and a.strBillNo='" + billNo + "' and b.strSettelmentType='Complementary' "
		    + "and date(a.dteBillDate)='" + billDate + "' ";
	    ResultSet rsSettlementType = objDatabseCon.funGetResultSet(sql);
	    if (rsSettlementType.next())
	    {
		flgComplimentaryBill = true;
	    }
	    rsSettlementType.close();

	    if (objPrintingUtility.funIsDirectBillerBill(billNo, billhd))
	    {
		flag_DirectBillerBlill = true;
		SQL_BillHD = "select a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
			+ "a.strCustomerCode,a.dblGrandTotal,a.dblTaxAmt,a.strReasonCode,a.strRemarks,a.strUserCreated"
			+ ",ifnull(dblDeliveryCharges,0.00),ifnull(b.dblAdvDeposite,0.00),a.dblDiscountPer,c.strPOSName,a.intOrderNo "
			+ "from " + billhd + " a left outer join tbladvancereceipthd b on a.strAdvBookingNo=b.strAdvBookingNo "
			+ "left outer join tblposmaster c on a.strPOSCode=c.strPOSCode "
			+ "where a.strBillNo=?  "
			+ "and date(a.dteBillDate)=? ";
		flag_DirectBiller = true;
		pst = con.prepareStatement(SQL_BillHD);
		pst.setString(1, billNo);
		pst.setString(2, billDate);
		rs_BillHD = pst.executeQuery();
		rs_BillHD.next();
	    }
	    else
	    {
		SQL_BillHD = "select a.strTableNo,a.strWaiterNo,a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
			+ "a.strCustomerCode,a.dblGrandTotal,a.dblTaxAmt,a.strReasonCode,a.strRemarks,a.strUserCreated"
			+ ",dblDeliveryCharges,ifnull(c.dblAdvDeposite,0.00),a.dblDiscountPer,d.strPOSName,a.intPaxNo,a.intOrderNo "
			+ "from " + billhd + " a left outer join tbltablemaster b on a.strTableNo=b.strTableNo "
			+ "left outer join tbladvancereceipthd c on a.strAdvBookingNo=c.strAdvBookingNo "
			+ "left outer join tblposmaster d on a.strPOSCode=d.strPOSCode "
			+ "where a.strBillNo=? and b.strOperational='Y' and date(a.dteBillDate)=? ";
		pst = con.prepareStatement(SQL_BillHD);
		pst.setString(1, billNo);
		pst.setString(2, billDate);
		rs_BillHD = pst.executeQuery();
		if (rs_BillHD.next())
		{
		    tabNo = rs_BillHD.getString(1);
		    if (rs_BillHD.getString(2).equalsIgnoreCase("null") || rs_BillHD.getString(2).equalsIgnoreCase(""))
		    {
			waiterNo = "";
		    }
		    else
		    {
			waiterNo = rs_BillHD.getString(2);
			query = "select strWShortName from tblwaitermaster where strWaiterNo=? ;";
			pst = con.prepareStatement(query);
			pst.setString(1, waiterNo);
			rsQuery = pst.executeQuery();
			if (rsQuery.next())
			{
			    waiterName = rsQuery.getString(1);
			}
			rsQuery.close();
			pst.close();
		    }
		}

		sqlTblName = "select strTableName from tbltablemaster where strTableNo=? ;";
		pst = con.prepareStatement(sqlTblName);
		pst.setString(1, tabNo);
		rsTblName = pst.executeQuery();
		if (rsTblName.next())
		{
		    tblName = rsTblName.getString(1);
		}
		rsTblName.close();
		pst.close();
	    }

	    // funPrintTakeAway(billhd, billNo, BillOut);
	    if (flag_DirectBillerBlill)
	    {
		headerJasperParameters.put("POS", rs_BillHD.getString(14));

		String orderNo = rs_BillHD.getString(15);
		if (objSetupHdModel.getStrPrintOrderNoOnBillYN().equalsIgnoreCase("Y"))
		{
		    headerJasperParameters.put("orderNo", "Your order no is " + orderNo);
		}

		if (objSetupHdModel.getStrPrintTimeOnBill().equalsIgnoreCase("Y"))
		{
		    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
		    headerJasperParameters.put("DATE_TIME", ft.format(rs_BillHD.getObject(1)));
		}
		else
		{
		    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
		    headerJasperParameters.put("DATE_TIME", ft.format(rs_BillHD.getObject(1)));
		}

//		subTotal = rs_BillHD.getString(4);
		//grandTotal = rs_BillHD.getDouble(6);
//		//user = rs_BillHD.getString(10);
//		deliveryCharge = rs_BillHD.getString(11);
//		advAmount = rs_BillHD.getString(12);
	    }
	    else
	    {
		headerJasperParameters.put("TABLE NAME", tblName);

		if (waiterName.trim().length() > 0)
		{
		    headerJasperParameters.put("waiterName", waiterName);
		}
		headerJasperParameters.put("POS", rs_BillHD.getString(16));
		headerJasperParameters.put("BillNo", billNo);
		headerJasperParameters.put("PaxNo", rs_BillHD.getString(17));
		
		String orderNo = rs_BillHD.getString(18);
		if (objSetupHdModel.getStrPrintOrderNoOnBillYN().equalsIgnoreCase("Y"))
		{
		    headerJasperParameters.put("orderNo", "Your Order No. Is:" + orderNo);
		}

	    if (objSetupHdModel.getStrPrintTimeOnBill().equalsIgnoreCase("Y"))
		{
		    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
		    headerJasperParameters.put("DATE_TIME", ft.format(rs_BillHD.getObject(3)));
		}
		else
		{
		    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
		    headerJasperParameters.put("DATE_TIME", ft.format(rs_BillHD.getObject(3)));
		}

//		subTotal = rs_BillHD.getString(6);
		//grandTotal = rs_BillHD.getDouble(8);
//		//user = rs_BillHD.getString(12);
//		deliveryCharge = rs_BillHD.getString(13);
//		advAmount = rs_BillHD.getString(14);
	    }

	    List<clsPOSBillDtl> listOfHeaderDetail = new ArrayList<>();
	    objBillDtl = new clsPOSBillDtl();
	    objBillDtl.setStrItemName(billDate);
	    listOfHeaderDetail.add(objBillDtl);
	    
	    JRBeanCollectionDataSource headerBeanColleDataSource = new JRBeanCollectionDataSource(listOfHeaderDetail);
	    String reportName = "/WEB-INF/reports/billFormat/rptBillFormat9JasperHeader.jrxml";
	    InputStream isForOuterReport = this.getClass().getClassLoader().getResourceAsStream(reportName);
	    JasperPrint headerJaperPrint = JasperFillManager.fillReport(isForOuterReport, headerJasperParameters, headerBeanColleDataSource);

	    for (int billCount = 0; billCount < arrListBillNos.size(); billCount++)
	    {
                
		
		if(clientCode.equalsIgnoreCase("340.001"))
		{
		    String BillSeriesNo= arrListBillNos.get(billCount);
		    if(!(billNo.equalsIgnoreCase(BillSeriesNo)))
		    {
		       continue;
		    }	
		  
		}
		else
		{
		    billNo = arrListBillNos.get(billCount);
		}    
		
		bodyJasperParameters.put("BillNo", billNo);

		double grandTotal = 0, discAmt = 0, discPer = 0;
		String discountRemark = "";
		String sqlBillHd = "select a.dblGrandTotal,a.dblDiscountAmt,a.dblDiscountPer,a.strDiscountRemark "
			+ "from " + billhd + " a "
			+ "where a.strBillNo='" + billNo + "' "
			+ "and date(a.dteBillDate)='" + billDate + "' ";
		PreparedStatement pstStatement = con.prepareStatement(sqlBillHd);
		ResultSet rsBillHd = pstStatement.executeQuery();
		if (rsBillHd.next())
		{
		    grandTotal = rsBillHd.getDouble(1);
		    discAmt = rsBillHd.getDouble(2);
		    discPer = rsBillHd.getDouble(3);
		    discountRemark = rsBillHd.getString(4);
		}
		rsBillHd.close();

		List<clsPOSBillDtl> listOfGrandTotalDtl = new ArrayList<>();

		objBillDtl = new clsPOSBillDtl();
		objBillDtl.setDblAmount(grandTotal);
		listOfGrandTotalDtl.add(objBillDtl);

		StringBuilder sbZeroAmtItems = new StringBuilder();

		List<clsPOSBillDtl> listOfBillDetail = new ArrayList<>();
		String SQL_BillDtl = "select sum(a.dblQuantity),a.strItemName as ItemLine1"
			+ " ,MID(a.strItemName,23,LENGTH(a.strItemName)) as ItemLine2"
			+ " ,sum(a.dblAmount),a.strItemCode,a.strKOTNo,a.dblRate "
			+ " from " + billdtl + " a "
			+ " where a.strBillNo=? and a.tdhYN='N' and date(a.dteBillDate)=?";
		if (!objSetupHdModel.getStrPrintOpenItemsOnBill().equalsIgnoreCase("Y"))
		{
		    SQL_BillDtl += "and a.dblAmount>0 ";
		}
		SQL_BillDtl += " group by a.strItemCode ;";
		pst = con.prepareStatement(SQL_BillDtl);
		pst.setString(1, billNo);
		pst.setString(2, billDate);
		ResultSet rs_BillDtl = pst.executeQuery();
		while (rs_BillDtl.next())
		{
		    double amt = rs_BillDtl.getDouble(4);
		    if (amt == 0)
		    {
			sbZeroAmtItems.append(",");
			sbZeroAmtItems.append("'" + rs_BillDtl.getString(5) + "'");
		    }

		    double saleQty = rs_BillDtl.getDouble(1);
		    String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " "
			    + " where strBillNo='" + billNo + "' and strItemCode='" + rs_BillDtl.getString(5) + "' "
			    + " and strPromoType='ItemWise' and date(dteBillDate)='" + billDate + "'";
		    ResultSet rsPromoItems = objDatabseCon.funGetResultSet(sqlPromoBills);
		    if (rsPromoItems.next())
		    {
			saleQty -= rsPromoItems.getDouble(1);
		    }
		    rsPromoItems.close();

		    String sqlCompliBills = "select dblQuantity from " + billComplDtl + " "
			    + " where strBillNo='" + billNo + "' "
			    + " and strItemCode='" + rs_BillDtl.getString(5) + "' "
			    + " and strType='ItemComplimentary' "
			    + " and date(dteBillDate)='" + billDate + "' ";
		    ResultSet rsComplimentaryItems =objDatabseCon.funGetResultSet(sqlCompliBills);
		    double compliQty = 1;
		    if (rsComplimentaryItems.next())
		    {
			saleQty -= rsComplimentaryItems.getDouble(1);
			compliQty = rsComplimentaryItems.getDouble(1);;			
		    }
		    rsComplimentaryItems.close();

		    String qty = String.valueOf(saleQty);
		    if (qty.contains("."))
		    {
			String decVal = qty.substring(qty.length() - 2, qty.length());
			if (Double.parseDouble(decVal) == 0)
			{
			    qty = qty.substring(0, qty.length() - 2);
			}
		    }

		    if (saleQty > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setDblQuantity(Double.parseDouble(decimalFormat.format(Double.parseDouble(qty))));
			objBillDtl.setDblRate(rs_BillDtl.getDouble(7));
			objBillDtl.setDblAmount(rs_BillDtl.getDouble(4));
			objBillDtl.setStrItemName(rs_BillDtl.getString(2));
			listOfBillDetail.add(objBillDtl);

			String sqlModifier = "select count(*) "
				+ "from " + billModifierdtl + " where strBillNo=? and left(strItemCode,7)=? and date(dteBillDate)=?";
			if (!objSetupHdModel.getStrPrintZeroAmtModifierInBill().equalsIgnoreCase("Y"))
			{
			    sqlModifier += " and  dblAmount !=0.00 ";
			}
			pst =con.prepareStatement(sqlModifier);

			pst.setString(1, billNo);
			pst.setString(2, rs_BillDtl.getString(5));
			pst.setString(3, billDate);
			ResultSet rs_count = pst.executeQuery();
			rs_count.next();
			int cntRecord = rs_count.getInt(1);
			rs_count.close();
			if (cntRecord > 0)
			{
			    sqlModifier = "select strModifierName,dblQuantity,dblAmount,dblRate "
				    + " from " + billModifierdtl + " "
				    + " where strBillNo=? and left(strItemCode,7)=? and date(dteBillDate)=?";
			    if (!objSetupHdModel.getStrPrintZeroAmtModifierInBill().equalsIgnoreCase("Y"))
			    {
				sqlModifier += " and  dblAmount !=0.00 ";
			    }
			    pst = con.prepareStatement(sqlModifier);

			    pst.setString(1, billNo);
			    pst.setString(2, rs_BillDtl.getString(5));
			    pst.setString(3, billDate);
			    ResultSet rs_modifierRecord = pst.executeQuery();
			    while (rs_modifierRecord.next())
			    {
				if (flgComplimentaryBill)
				{
				    objBillDtl = new clsPOSBillDtl();
				    objBillDtl.setDblQuantity(Double.parseDouble(decimalFormat.format(rs_modifierRecord.getDouble(2))));
				    objBillDtl.setDblRate(rs_modifierRecord.getDouble(4));
				    objBillDtl.setDblAmount(0);
				    objBillDtl.setStrItemName(rs_modifierRecord.getString(1).toUpperCase());
				    listOfBillDetail.add(objBillDtl);
				}
				else
				{
				    objBillDtl = new clsPOSBillDtl();
				    objBillDtl.setDblQuantity(Double.parseDouble(decimalFormat.format(rs_modifierRecord.getDouble(2))));
				    objBillDtl.setDblRate(rs_modifierRecord.getDouble(4));
				    objBillDtl.setDblAmount(rs_modifierRecord.getDouble(3));
				    objBillDtl.setStrItemName(rs_modifierRecord.getString(1).toUpperCase());
				    listOfBillDetail.add(objBillDtl);
				}
			    }
			    rs_modifierRecord.close();
			}
		    }
		}
		rs_BillDtl.close();

		//objPrintingUtility.funPrintPromoItemsInBill(billNo, 4, listOfBillDetail);  // Print Promotion Items in Bill for this billno.
		//objPrintingUtility.funPrintComplimentaryItemsInBill(billNo, listOfBillDetail, 4, posCode, billDate, sbZeroAmtItems);

		List<clsPOSBillDtl> listOfDiscountDtl = new ArrayList<>();

		sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
			+ "from " + billDscFrom + " a ,tblreasonmaster b "
			+ "where  a.strDiscReasonCode=b.strReasonCode "
			+ "and a.strBillNo='" + billNo + "' and date(a.dteBillDate)='" + billDate + "'";
		ResultSet rsDisc = objDatabseCon.funGetResultSet(sql);

		boolean flag = true;
		while (rsDisc.next())
		{
		    if (flag)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Discount");
			listOfDiscountDtl.add(objBillDtl);
			flag = false;
		    }
		    double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
		    String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
		    if (discText.length() > 30)
		    {
			discText = discText.substring(0, 30);
		    }
		    else
		    {
			discText = String.format("%-30s", discText);
		    }

		    String discountOnItem = objUtility.funPrintTextWithAlignment(rsDisc.getString("dblDiscAmt"), 8, "Right");
		    headerJasperParameters.put("Discount", discText + " " + discountOnItem);
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName(discText);
		    objBillDtl.setDblAmount(rsDisc.getDouble("dblDiscAmt"));
		    listOfDiscountDtl.add(objBillDtl);

		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("Reason :" + " " + rsDisc.getString("strReasonName"));
		    listOfDiscountDtl.add(objBillDtl);

		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("Remark :" + " " + rsDisc.getString("strDiscRemarks"));
		    listOfDiscountDtl.add(objBillDtl);

		}

		List<clsPOSBillDtl> listOfTaxDetail = new ArrayList<>();

		if (discAmt > 0)
		{
//		    String sqlDiscPer = "select a.dblDiscountAmt,a.dblDiscountPer "
//			    + "from tblbilldtl a "
//			    + "where strBillNo='"+billNo+"' "
//			    + "and date(a.dteBillDate)='"+billDate+"' "
//			    + "and a.dblDiscountAmt>0 "
//			    + "limit 0,1;";
//		    ResultSet rsDiscPer = clsGlobalVarClass.dbMysql.executeResultSet(sqlDiscPer);
//		    if (rsDiscPer.next())
//		    {
//			objBillDtl = new clsBillDtl();
//			objBillDtl.setStrItemName("DISC. " + stdDecimalFormat.format(rsDiscPer.getDouble(2)) + " %");
//			objBillDtl.setDblAmount(discAmt);
//			listOfTaxDetail.add(objBillDtl);
//		    }
//		    rsDiscPer.close();

		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("DISC.");
		    objBillDtl.setDblAmount(discAmt);

		    listOfTaxDetail.add(objBillDtl);

		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName(discountRemark);

		    listOfTaxDetail.add(objBillDtl);

		}

		String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount),b.strBillNote,b.strTaxCalculation "
			+ " from " + billtaxdtl + " a,tbltaxhd b "
			+ " where a.strBillNo='" + billNo + "' and a.strTaxCode=b.strTaxCode "
			+ " group by a.strTaxCode";
		ResultSet rsTax = objDatabseCon.funGetResultSet(sql_Tax);
                
		while (rsTax.next())
		{
		    if(!rsTax.getString(4).equalsIgnoreCase("Backward"))//For not printing backword taxes
		    {
			if (flgComplimentaryBill)
			{
			    objBillDtl = new clsPOSBillDtl();
			    objBillDtl.setDblAmount(0);
			    objBillDtl.setStrItemName(rsTax.getString(1) + " :");
			    listOfTaxDetail.add(objBillDtl);
			    headerJasperParameters.put("GSTNo", rsTax.getString(3));
			}
			else
			{
			    objBillDtl = new clsPOSBillDtl();
			    objBillDtl.setDblAmount(rsTax.getDouble(2));
			    objBillDtl.setStrItemName(rsTax.getString(1) + " :");
			    listOfTaxDetail.add(objBillDtl);
			    headerJasperParameters.put("GSTNo", rsTax.getString(3));
			}
			
		    }
		    
		}
		rsTax.close();

		List<clsPOSBillDtl> listOfSettlementDetail = new ArrayList<>();
		//settlement breakup part
		String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType "
			+ " from " + billSettlementdtl + " a ,tblsettelmenthd b "
			+ "where a.strBillNo=? and a.strSettlementCode=b.strSettelmentCode"
			+ " and date(a.dteBillDate)=?";
		pst = con.prepareStatement(sqlSettlementBreakup);
		pst.setString(1, billNo);
		pst.setString(2, billDate);
		ResultSet rs_Bill_Settlement = pst.executeQuery();
		while (rs_Bill_Settlement.next())
		{
		    if (flgComplimentaryBill)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_Bill_Settlement.getString(2));
			objBillDtl.setDblAmount(0.00);
			listOfSettlementDetail.add(objBillDtl);
		    }
		    else
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_Bill_Settlement.getString(2));
			objBillDtl.setDblAmount(rs_Bill_Settlement.getDouble(1));

			listOfSettlementDetail.add(objBillDtl);
		    }
		}
		rs_Bill_Settlement.close();

//		//add del charges
//		double delCharges = Double.parseDouble(deliveryCharge);
//		objBillDtl = new clsBillDtl();
//		objBillDtl.setDblAmount(delCharges);
//		objBillDtl.setStrItemName("Del. Charges");
//		if (delCharges > 0)
//		{
//		    listOfTaxDetail.add(objBillDtl);
//		}
		String sqlTenderAmt = "select sum(dblPaidAmt),sum(dblSettlementAmt),(sum(dblPaidAmt)-sum(dblSettlementAmt)) RefundAmt "
			+ " from " + billSettlementdtl + " where strBillNo='" + billNo + "' "
			+ " and date(dteBillDate)='" + billDate + "'"
			+ " group by strBillNo";
		ResultSet rsTenderAmt = objDatabseCon.funGetResultSet(sqlTenderAmt);
		if (rsTenderAmt.next())
		{
		    if (flgComplimentaryBill)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("PAID AMT");
			objBillDtl.setDblAmount(0.00);
			listOfSettlementDetail.add(objBillDtl);
		    }
		    else
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("PAID AMT");
			objBillDtl.setDblAmount(rsTenderAmt.getDouble(1));
			listOfSettlementDetail.add(objBillDtl);
			if (rsTenderAmt.getDouble(3) > 0)
			{

			    objBillDtl = new clsPOSBillDtl();
			    objBillDtl.setStrItemName("REFUND AMT");
			    objBillDtl.setDblAmount(rsTenderAmt.getDouble(3));
			    listOfSettlementDetail.add(objBillDtl);
			}
		    }
		}
		rsTenderAmt.close();

		if (flag_isHomeDelvBill)
		{
		    String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
		    pst = con.prepareStatement(sql_count);
		    pst.setString(1, customerCode);
		    ResultSet rs_Count = pst.executeQuery();
		    rs_Count.next();
		    headerJasperParameters.put("CUSTOMER_COUNT", rs_Count.getString(1));
		}

		List<clsPOSBillDtl> listOfServiceVatDetail = new ArrayList<>();
		if (arrListBillNos.get(billCount).startsWith("F") && arrListBillNos.size() > 1)
		{
		    listOfServiceVatDetail = objPrintingUtility.funPrintServiceVatNoForJasper(billNo, billDate, billtaxdtl);
		}

		/*if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
		{
		    headerJasperParameters.put("ch", "V");
		}
		else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
		{
		    if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
		    {
			headerJasperParameters.put("ch", "V");
		    }
		    else
		    {
			headerJasperParameters.put("ch", "m");
		    }
		}

*/		bodyJasperParameters.put("listOfItemDtl", listOfBillDetail);
		bodyJasperParameters.put("listOfTaxDtl", listOfTaxDetail);
		bodyJasperParameters.put("listOfGrandTotalDtl", listOfGrandTotalDtl);
		bodyJasperParameters.put("listOfServiceVatDetail", listOfServiceVatDetail);
		if (billNo.startsWith("L"))
		{
		    listOfHomeDeliveryDtl.clear();
		}

		bodyJasperParameters.put("listOfHomeDeliveryDtl", listOfHomeDeliveryDtl);
		bodyJasperParameters.put("listOfDiscountDtl", listOfDiscountDtl);
		bodyJasperParameters.put("listOfSettlementDetail", listOfSettlementDetail);

		/*String imagePath = System.getProperty("user.dir");
		imagePath = imagePath + File.separator + "ReportImage";
		imagePath = imagePath + File.separator + "imgBillLogo.jpg";
		System.out.println("imgBillLogo=" + imagePath);
		headerJasperParameters.put("imgBillLogo", imagePath);
*/
		List<List<clsPOSBillDtl>> listData = new ArrayList<>();
		listData.add(listOfBillDetail);

//		//Add prints to main jasper
		InputStream isForInnerReport = this.getClass().getClassLoader().getResourceAsStream("/WebRoot/WEB-INF/reports/billFormat/rptBillFormat9JasperBody.jrxml");
		JasperPrint jp1 = JasperFillManager.fillReport(isForInnerReport, bodyJasperParameters, new JRBeanCollectionDataSource(listData));
		List jp1pages = jp1.getPages();
		for (int j = 0; j < jp1pages.size(); j++)
		{
		    JRPrintPage object = (JRPrintPage) jp1pages.get(j);

		    headerJaperPrint.addPage(headerJaperPrint.getPages().size(), object);//addPage(object);
		}
		isForInnerReport.close();

		if (arrListBillNos.size() > 1 && billCount == 0)
		{
		    //Add prints to main jasper
		    InputStream isForBillSpaceReport = this.getClass().getClassLoader().getResourceAsStream("/WEB-INF/reports/billFormat/rptBillFormat9JasperBillSpace.jrxml");
		    JasperPrint jpBillSpace = JasperFillManager.fillReport(isForBillSpaceReport, bodyJasperParameters, new JRBeanCollectionDataSource(listData));
		    List jpBillSpacepages = jpBillSpace.getPages();
		    for (int j = 0; j < jpBillSpacepages.size(); j++)
		    {
			JRPrintPage object = (JRPrintPage) jpBillSpacepages.get(j);

			headerJaperPrint.addPage(headerJaperPrint.getPages().size(), object);//addPage(object);
		    }
		    isForBillSpaceReport.close();
		}

	    }

	    rs_BillHD.close();

	    List<clsPOSBillDtl> listSummaryBillDtl = new ArrayList<>();
	    double dblAllBillGrandTotal = 0.00;
	    if(objSetupHdModel.getStrEnableBillSeries().equalsIgnoreCase("Y"))
	    {
		for (int billCount = 0; billCount < arrListBillNos.size(); billCount++)
		{
		    if(arrListBillNos.size()==1 && clientCode.equalsIgnoreCase("340.001"))
		    {
			if(billSeriesNoFromFun.startsWith("F"))
			{
			    sqlBillNos = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos,a.dblGrandTotal "
			    + "from tblbillseriesbilldtl a "
			    + "where a.strHdBillNo='" + billSeriesNoFromFun + "' "
			    + "and date(a.dteBillDate)='" + billDate + "' ";
			    rsDtlBillNos = objDatabseCon.funGetResultSet(sqlBillNos); 
			    if (rsDtlBillNos.next())
			    {

				double billFoodTotal = rsDtlBillNos.getDouble(5);
				objBillDtl.setStrItemName("Food Total");
				objBillDtl.setDblAmount(billFoodTotal);
				dblAllBillGrandTotal =  billFoodTotal;
			    }
			    listSummaryBillDtl.add(objBillDtl);
			    break;

			}
			else if(billSeriesNoFromFun.startsWith("L"))
			{
			    sqlBillNos = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos,a.dblGrandTotal "
			    + "from tblbillseriesbilldtl a "
			    + "where a.strHdBillNo='" + billSeriesNoFromFun + "' "
			    + "and date(a.dteBillDate)='" + billDate + "' ";
			    rsDtlBillNos = objDatabseCon.funGetResultSet(sqlBillNos); 
			    if (rsDtlBillNos.next())
			    {

				double billFoodTotal = rsDtlBillNos.getDouble(5);
				objBillDtl.setStrItemName("Liquor Total");
				objBillDtl.setDblAmount(billFoodTotal);
				dblAllBillGrandTotal =  billFoodTotal;
			    }
			    listSummaryBillDtl.add(objBillDtl);
			    break;

			}
			
		    }
		    else if(formName.equalsIgnoreCase("sales report") && clientCode.equalsIgnoreCase("340.001"))
		    {
			if(billSeriesNoFromFun.startsWith("F"))
			{
			    sqlBillNos = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos,a.dblGrandTotal "
			    + "from tblbillseriesbilldtl a "
			    + "where a.strHdBillNo='" + billSeriesNoFromFun + "' "
			    + "and date(a.dteBillDate)='" + billDate + "' ";
			    rsDtlBillNos = objDatabseCon.funGetResultSet(sqlBillNos); 
			    if (rsDtlBillNos.next())
			    {

				double billFoodTotal = rsDtlBillNos.getDouble(5);
				objBillDtl.setStrItemName("Food Total");
				objBillDtl.setDblAmount(billFoodTotal);
				dblAllBillGrandTotal =  billFoodTotal;
			    }
			    listSummaryBillDtl.add(objBillDtl);
			    break;

			}
			else if(billSeriesNoFromFun.startsWith("L"))
			{
			    sqlBillNos = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos,a.dblGrandTotal "
			    + "from tblbillseriesbilldtl a "
			    + "where a.strHdBillNo='" + billSeriesNoFromFun + "' "
			    + "and date(a.dteBillDate)='" + billDate + "' ";
			    rsDtlBillNos = objDatabseCon.funGetResultSet(sqlBillNos); 
			    if (rsDtlBillNos.next())
			    {

				double billFoodTotal = rsDtlBillNos.getDouble(5);
				objBillDtl.setStrItemName("Liquor Total");
				objBillDtl.setDblAmount(billFoodTotal);
				dblAllBillGrandTotal =  billFoodTotal;
			    }
			    listSummaryBillDtl.add(objBillDtl);
			    break;

			}
			
		    }
		    else
		    {
			sqlBillNos = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos,a.dblGrandTotal "
			    + "from tblbillseriesbilldtl a "
			    + "where a.strHdBillNo='" + arrListBillNos.get(billCount) + "' "
			    + "and date(a.dteBillDate)='" + billDate + "' ";
			rsDtlBillNos = objDatabseCon.funGetResultSet(sqlBillNos);
			if (rsDtlBillNos.next())
			{
			    String billSeries = rsDtlBillNos.getString(2);
			    double billTotal = rsDtlBillNos.getDouble(5);

			    objBillDtl = new clsPOSBillDtl();
			    if (billSeries.equalsIgnoreCase("F"))
			    {
				objBillDtl.setStrItemName("Food Total");
				objBillDtl.setDblAmount(billTotal);
			    }
			    else if (billSeries.equalsIgnoreCase("L"))
			    {
				objBillDtl.setStrItemName("Liquor Total");
				objBillDtl.setDblAmount(billTotal);
			    }
			    else if (billSeries.equalsIgnoreCase("H"))
			    {
				objBillDtl.setStrItemName("Sheesha Total");//"Hukkah Total"
				objBillDtl.setDblAmount(billTotal);
			    }
			    else
			    {
				objBillDtl.setStrItemName("Total");
				objBillDtl.setDblAmount(billTotal);
			    }
			    dblAllBillGrandTotal += billTotal;
			    listSummaryBillDtl.add(objBillDtl);
			}
		    }	
		    
		    
		     rsDtlBillNos.close();
		}
		

	    }
	    else
	    {
		sqlBillNos = "select a.dblGrandTotal "
			+ "from " + billhd + " a "
			+ "where a.strBillNo='" + billNo + "' "
			+ "and date(a.dteBillDate)='" + billDate + "' ";
		rsDtlBillNos = objDatabseCon.funGetResultSet(sqlBillNos);
		if (rsDtlBillNos.next())
		{
		    double billTotal = rsDtlBillNos.getDouble(1);

		    dblAllBillGrandTotal += billTotal;
		}
		 rsDtlBillNos.close();
	    }
	   
	   
	    final String ORDERFORSUMMARY = "FLST";
	    Comparator<clsPOSBillDtl> groupNameSorting = new Comparator<clsPOSBillDtl>()
	    {
		@Override
		public int compare(clsPOSBillDtl o1, clsPOSBillDtl o2)
		{
		    return ORDERFORSUMMARY.indexOf(o1.getStrItemName().charAt(0)) - ORDERFORSUMMARY.indexOf(o2.getStrItemName().charAt(0));
		}
	    };
	    Collections.sort(listSummaryBillDtl, groupNameSorting);

	    footerJasperParameters.put("listSummaryBillDtl", listSummaryBillDtl);
	    footerJasperParameters.put("dblAllBillGrandTotal", dblAllBillGrandTotal);

	    List<clsPOSBillDtl> listOfServiceVatDetail = new ArrayList<>();
	    for (int billCount = 0; billCount < arrListBillNos.size(); billCount++)
	    {
		if (arrListBillNos.get(billCount).startsWith("L"))
		{
		    List<clsPOSBillDtl> listOfServiceVat = objPrintingUtility.funPrintServiceVatNoForJasper(arrListBillNos.get(billCount), billDate, billtaxdtl);

		    listOfServiceVatDetail.addAll(listOfServiceVat);
		}
	    }

	    //if only food bill
	    if (arrListBillNos.size() == 1 && arrListBillNos.get(0).startsWith("F"))
	    {
		List<clsPOSBillDtl> listOfServiceVat = objPrintingUtility.funPrintServiceVatNoForJasper(arrListBillNos.get(0), billDate, billtaxdtl);

		listOfServiceVatDetail.addAll(listOfServiceVat);
	    }
	    
	    if (clientCode.equalsIgnoreCase("239.001"))//URBO
	    {
		objBillDtl = new clsPOSBillDtl();
		objBillDtl.setStrItemName("                     Thank you...Visit again...");
		listOfServiceVatDetail.add(objBillDtl);
		objBillDtl = new clsPOSBillDtl();
		objBillDtl.setStrItemName("                     Team URBO");
		listOfServiceVatDetail.add(objBillDtl);
	    }else{
		objBillDtl = new clsPOSBillDtl();
		objBillDtl.setStrItemName(objSetupHdModel.getStrBillFooter());
		listOfServiceVatDetail.add(objBillDtl);
	    }
	   // listOfServiceVatDetail.addAll(listOfFooterDtl);
	 
	    if (listSummaryBillDtl.size() == 0)
	    {
		listOfServiceVatDetail.clear();

		objBillDtl = new clsPOSBillDtl();
		objBillDtl.setStrItemName(objSetupHdModel.getStrBillFooter());

		listOfServiceVatDetail.add(objBillDtl);

		objBillDtl = new clsPOSBillDtl();
		objBillDtl.setStrItemName("");
		listSummaryBillDtl.add(objBillDtl);
	    }
	    
	    footerJasperParameters.put("listOfServiceVatDetail", listOfServiceVatDetail);

	    InputStream isForInnerReport = this.getClass().getClassLoader().getResourceAsStream("/WEB-INF/reports/billFormat/rptBillFormat9JasperFooter.jrxml");
	    JasperPrint jp1 = JasperFillManager.fillReport(isForInnerReport, footerJasperParameters, new JRBeanCollectionDataSource(listSummaryBillDtl));
	    List jp1pages = jp1.getPages();
	    if(arrListBillNos.size() > 1 && billSeriesNoFromFun.startsWith("F") && (clientCode.equalsIgnoreCase("340.001")) && !formName.equalsIgnoreCase("sales report"))
	    {
		
	    }
	    else
	    {
		for (int j = 0; j < jp1pages.size(); j++)
		{

		    JRPrintPage object = (JRPrintPage) jp1pages.get(j);

		    headerJaperPrint.addPage(headerJaperPrint.getPages().size(), object);//addPage(object);
		}
		
	    }	
	    isForInnerReport.close();

//            JasperPrint jp2 = JasperFillManager.fillReport(isForLiquorSubReport, headerJasperParameters, new JRBeanCollectionDataSource(listData));
//            List jp2pages = jp2.getPages();
//            for (int j = 0; j < jp2pages.size(); j++)
//            {
//                JRPrintPage object = (JRPrintPage) jp2pages.get(j);
//                mainJaperPrint.addPage(object);
//
//            }
//            isForLiquorSubReport.close();
//	    JRViewer viewer = new JRViewer(headerJaperPrint);
//	    JFrame jf = new JFrame();
//	    jf.getContentPane().add(viewer);
//	    jf.validate();
//	    if (clsGlobalVarClass.gShowBill)
//	    {
//		jf.setVisible(true);
//		jf.setSize(new Dimension(500, 900));
//		jf.setLocationRelativeTo(null);
//		jf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//	    }
//	    else if (formName.equalsIgnoreCase("sales report"))
//	    {
//		jf.setVisible(true);
//		jf.setSize(new Dimension(500, 900));
//		jf.setLocationRelativeTo(null);
//		jf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//	    }

    final JasperPrint print=headerJaperPrint;
    final String printer=objPrintingUtility.funGetPrinterDetails(posCode);

       new Thread()
	    {
		@Override
		public void run()
		{
		    objPrintingUtility.funPrintJasperExporterInThread(print,printer);
		}
	    }.start();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

}
