/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanguine.webpos.printing;

import java.awt.Dimension;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.swing.JRViewer;

@Controller

public class clsJasperFormat10ForBill 
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

	    if (objSetupHdModel.getStrBenowIntegrationYN().equalsIgnoreCase("Y"))
	    {
		String sqlSettlementAmt = "select dblGrandTotal from " + billhd + " where strBillNo='" + billNo + "' and strClientCode='" + clientCode + "' ";
		ResultSet rsSettlementAmt =objDatabseCon.funGetResultSet(sqlSettlementAmt);
		/*if (rsSettlementAmt.next())
		{
		    try{
			clsBenowIntegration objBenow = new clsBenowIntegration();
			String QRStringForBenow = objBenow.funGetDynamicQRString(billNo, rsSettlementAmt.getDouble(1)); //add dblSettlementAmount
			if (!QRStringForBenow.equalsIgnoreCase("NotFound"))
			{
			    objBenow.funGenerateQrCode(QRStringForBenow);
			}
		    }catch(Exception e){
			e.printStackTrace();
		    }
		   
		}*/
	    }

	    String grandTotal = "";
	    String customerCode = "";

	    if (clientCode.equals("117.001"))
	    {
		if (posCode.equals("P01"))
		{
		    hm.put("posWiseHeading", "THE PREM'S HOTEL");
		}
		else if (posCode.equals("P02"))
		{
		    hm.put("posWiseHeading", "SWIG");
		}
	    }

	   boolean isReprint = false;
	  if(isOriginal)
	    {
		hm.put("duplicate", "[ORIGINAL]");
	    }
	  
	    if ("reprint".equalsIgnoreCase(reprint))
	    {
		isReprint = true;
		hm.put("duplicate", "[DUPLICATE]");
	    }
	    if (transType.equals("Void"))
	    {
		hm.put("voidedBill", "VOIDED BILL");
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

		if (objSetupHdModel.getStrPrintHomeDeliveryYN().equalsIgnoreCase("Y"))
		{
		    billType = "HOME DELIVERY";
		}
		customerCode = rs_HomeDelivery.getString(2);

		String SQL_CustomerDtl = "";

		if (rs_HomeDelivery.getString(5).equals("Temporary"))
		{
		    SQL_CustomerDtl = "select a.strCustomerName,a.strTempAddress,a.strTempStreet"
			    + " ,a.strTempLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo ,a.strOfficeName ,a.strGSTNo  "
			    + " from tblcustomermaster a left outer join tblbuildingmaster b "
			    + " on a.strBuldingCode=b.strBuildingCode "
			    + " where a.strCustomerCode=? ;";
		}
		else if (rs_HomeDelivery.getString(5).equals("Office"))
		{
		    SQL_CustomerDtl = "select a.strCustomerName,a.strOfficeBuildingName,a.strOfficeStreetName"
			    + ",a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity,a.strOfficePinCode,a.longMobileNo ,a.strOfficeName,a.strGSTNo  "
			    + " from tblcustomermaster a "
			    + " where a.strCustomerCode=? ";
		}
		else
		{
		    SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName"
			    + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo ,a.strOfficeName ,a.strGSTNo  "
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

		    hm.put("NAME", rs_CustomerDtl.getString(1));
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("Name         : " + rs_CustomerDtl.getString(1).toUpperCase());
		    fullAddress.append(objBillDtl.getStrItemName());
		    listOfHomeDeliveryDtl.add(objBillDtl);

		    if(rs_CustomerDtl.getString(9).trim().length() > 0){
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Office Name : " + rs_CustomerDtl.getString(9).toUpperCase());
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("ADDRESS    :" + rs_CustomerDtl.getString(2).toUpperCase());
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
		    
		    if (rs_CustomerDtl.getString(5).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDtl.getString(5).toUpperCase());//"Area   :" +
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

		    hm.put("FullAddress", fullAddress);

		    if (rs_CustomerDtl.getString(8).isEmpty())
		    {
			hm.put("MOBILE_NO", "");
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("MOBILE_NO  :" + " ");
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    else
		    {
			hm.put("MOBILE_NO", rs_CustomerDtl.getString(8));
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Mobile No    : " + rs_CustomerDtl.getString(8));
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    if (rs_CustomerDtl.getString(9).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Company Name : "+rs_CustomerDtl.getString(9).toUpperCase());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    if (rs_CustomerDtl.getString(10).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("GST No : "+rs_CustomerDtl.getString(10).toUpperCase());
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
			hm.put("DELV BOY", "");
		    }
		    else
		    {
			hm.put("DELV BOY", "Delivery Boy : " + strIN);
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Delivery Boy : " + strIN);
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    rs_DeliveryBoyDtl.close();
		}
		else
		{
		    hm.put("DELV BOY", "");
		}
	    }
	    rs_HomeDelivery.close();
	    if(!flag_isHomeDelvBill)
	    {
		String sql1 = "select b.strCustomerName,b.strCustAddress,b.strStreetName ,b.strLandmark, "
			    + " b.strBuildingName,b.strCity,b.intPinCode,b.longMobileNo ,b.strOfficeName  ,b.strGSTNo "
			    + " from " + billhd + " a,tblcustomermaster b "
			    + " where a.strCustomerCode=b.strCustomerCode "
			    + " and a.strBillNo='" + billNo + "' "
			    + " and date(a.dteBillDate)='" + billDate + "' ";
	        ResultSet rs_CustomerDetail =objDatabseCon.funGetResultSet(sql1);
		while (rs_CustomerDetail.next())
		{
		    StringBuilder fullAddress = new StringBuilder();

		    hm.put("NAME", rs_CustomerDetail.getString(1));
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("Name         : " + rs_CustomerDetail.getString(1).toUpperCase());
		    fullAddress.append(objBillDtl.getStrItemName());
		    listOfHomeDeliveryDtl.add(objBillDtl);

		    if(rs_CustomerDetail.getString(9).trim().length() > 0){
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Office Name : " + rs_CustomerDetail.getString(9).toUpperCase());
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setStrItemName("ADDRESS    :" + rs_CustomerDetail.getString(2).toUpperCase());
		    fullAddress.append(objBillDtl.getStrItemName());
		    listOfHomeDeliveryDtl.add(objBillDtl);

		    if (rs_CustomerDetail.getString(3).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDetail.getString(3).toUpperCase());//"Street    :" +
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    if (rs_CustomerDetail.getString(4).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDetail.getString(4).toUpperCase());//"Landmark    :" +
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    if (rs_CustomerDetail.getString(5).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDetail.getString(5).toUpperCase());//"Area   :" +
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    if (rs_CustomerDetail.getString(6).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDetail.getString(6).toUpperCase());//"City    :" +
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    if (rs_CustomerDetail.getString(7).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName(rs_CustomerDetail.getString(7).toUpperCase());//"Pin    :" +
			fullAddress.append(objBillDtl.getStrItemName());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		    hm.put("FullAddress", fullAddress);

		    if (rs_CustomerDetail.getString(8).isEmpty())
		    {
			hm.put("MOBILE_NO", "");
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("MOBILE_NO  :" + " ");
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    else
		    {
			hm.put("MOBILE_NO", rs_CustomerDetail.getString(8));
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Mobile No    : " + rs_CustomerDetail.getString(8));
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    if (rs_CustomerDetail.getString(9).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("Company Name : "+rs_CustomerDetail.getString(9).toUpperCase());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }
		    if (rs_CustomerDetail.getString(10).trim().length() > 0)
		    {
			objBillDtl = new clsPOSBillDtl();
			objBillDtl.setStrItemName("GST No : "+rs_CustomerDetail.getString(10).toUpperCase());
			listOfHomeDeliveryDtl.add(objBillDtl);
		    }

		}
	    }
		
	    int result = objPrintingUtility.funPrintTakeAwayForJasper(billhd, billNo);
	    if (result == 1)
	    {
		billType = "Take Away";
	    }
	    if (objSetupHdModel.getStrPrintTaxInvoiceOnBill().equalsIgnoreCase("Y"))
	    {
		hm.put("TAX_INVOICE", "TAX INVOICE");
	    }
	    if (clientCode.equals("047.001") && posCode.equals("P03"))
	    {
		hm.put("ClientName", "SHRI SHAM CATERERS");
		String cAddr1 = "Flat No.7, Mon Amour,";
		String cAddr2 = "Thorat Colony,Prabhat Road,";
		String cAddr3 = " Erandwane, Pune 411 004.";
		String cAddr4 = "Approved Caterers of";
		String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
		hm.put("ClientAddress1", cAddr1 + cAddr2);
		hm.put("ClientAddress2", cAddr3 + cAddr4);
		hm.put("ClientAddress3", cAddr5);
	    }
	    else if (clientCode.equals("047.001") && posCode.equals("P02"))
	    {
		hm.put("ClientName", "SHRI SHAM CATERERS");
		String cAddr1 = "Flat No.7, Mon Amour,";
		String cAddr2 = "Thorat Colony,Prabhat Road,";
		String cAddr3 = " Erandwane, Pune 411 004.";
		String cAddr4 = "Approved Caterers of";
		String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
		hm.put("ClientAddress1", cAddr1 + cAddr2);
		hm.put("ClientAddress2", cAddr3 + cAddr4);
		hm.put("ClientAddress3", cAddr5);
	    }
	    else if (clientCode.equals("092.001") || clientCode.equals("092.002") || clientCode.equals("092.003"))//Shree Sound Pvt. Ltd.(Waters)
	    {
		hm.put("ClientName", "SSPL");
		hm.put("ClientAddress1", objSetupHdModel.getStrAddressLine1());
		hm.put("ClientAddress2", objSetupHdModel.getStrAddressLine2());
		hm.put("ClientAddress3", objSetupHdModel.getStrAddressLine3());

		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    hm.put("ClientCity", objSetupHdModel.getStrCityName());
		}
	    }
	    else
	    {
		hm.put("ClientName", objSetupHdModel.getStrClientName());
		hm.put("ClientAddress1", objSetupHdModel.getStrAddressLine1());
		hm.put("ClientAddress2", objSetupHdModel.getStrAddressLine2());
		hm.put("ClientAddress3", objSetupHdModel.getStrAddressLine3());

		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    hm.put("ClientCity", objSetupHdModel.getStrCityName());
		}
	    }

	    hm.put("TEL NO",objSetupHdModel.getStrTelephoneNo());
	    hm.put("EMAIL ID",objSetupHdModel.getStrEmail());
	    hm.put("Line", Linefor5);

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
		    + " where a.strSettlementCode=b.strSettelmentCode and a.strBillNo='" + billNo + "' and b.strSettelmentType='Complementary' ";
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
			+ ",ifnull(dblDeliveryCharges,0.00),ifnull(b.dblAdvDeposite,0.00),a.dblDiscountPer,c.strPOSName "
			+ "from " + billhd + " a left outer join tbladvancereceipthd b on a.strAdvBookingNo=b.strAdvBookingNo "
			+ "left outer join tblposmaster c on a.strPOSCode=c.strPOSCode "
			+ "where a.strBillNo=?  "
			+ " and date(a.dteBillDate)=?";
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
			+ ",dblDeliveryCharges,ifnull(c.dblAdvDeposite,0.00),a.dblDiscountPer,d.strPOSName,a.intPaxNo "
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
	    List<clsPOSBillDtl> listSubTotal = new ArrayList<>();

	    if (flag_DirectBillerBlill)
	    {
		hm.put("POS", rs_BillHD.getString(14));
		hm.put("BillNo", billNo);

		if (objSetupHdModel.getStrPrintTimeOnBill().equalsIgnoreCase("Y"))
		{
		    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
		    hm.put("DATE_TIME", ft.format(rs_BillHD.getObject(1)));
		}
		else
		{
		    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
		    hm.put("DATE_TIME", ft.format(rs_BillHD.getObject(1)));
		}
		objBillDtl = new clsPOSBillDtl();
		objBillDtl.setDblAmount(rs_BillHD.getDouble(4));;
		listSubTotal.add(objBillDtl);
		grandTotal = rs_BillHD.getString(6);
	    }
	    else
	    {
		hm.put("TABLE NAME", tblName);

		if (waiterName.trim().length() > 0)
		{
		    hm.put("waiterName", waiterName);
		}
		hm.put("POS", rs_BillHD.getString(16));
		hm.put("BillNo", billNo);
		hm.put("PaxNo", rs_BillHD.getString(17));

		if (objSetupHdModel.getStrPrintTimeOnBill().equalsIgnoreCase("Y"))
		{
		    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
		    hm.put("DATE_TIME", ft.format(rs_BillHD.getObject(3)));
		}
		else
		{
		    SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
		    hm.put("DATE_TIME", ft.format(rs_BillHD.getObject(3)));
		}
		objBillDtl = new clsPOSBillDtl();
		objBillDtl.setDblAmount(rs_BillHD.getDouble(6));;
		listSubTotal.add(objBillDtl);
		grandTotal = rs_BillHD.getString(8);
	    }

	    StringBuilder sbZeroAmtItems = new StringBuilder();

	    List<clsPOSBillDtl> listOfFoodBillDetail = new ArrayList<>();
	    List<clsPOSBillDtl> listOfBeverageBillDetail = new ArrayList<>();
	    List<clsPOSBillDtl> listOfLiqourBillDetail = new ArrayList<>();
	    List<clsPOSBillDtl> listGeneralBillDtl = new ArrayList<>();
	    String SQL_BillDtl = "SELECT SUM(a.dblQuantity), "
		    + "LEFT(a.strItemName,22) AS ItemLine1, MID(a.strItemName,23, LENGTH(a.strItemName)) AS ItemLine2, SUM(a.dblAmount),a.strItemCode "
		    + ",a.strKOTNo,e.strGroupName,d.strSubGroupName "
		    + "FROM " + billdtl + " a," + billhd + " b,tblitemmaster c,tblsubgrouphd d,tblgrouphd e "
		    + "WHERE a.strBillNo=b.strBillNo  "
		    + "and a.strItemCode=c.strItemCode "
		    + "and c.strSubGroupCode=d.strSubGroupCode "
		    + "and d.strGroupCode=e.strGroupCode "
		    + "AND a.strClientCode=b.strClientCode "
		    + " and a.strBillNo=? "
		    + " and b.strPOSCode=?  ";
	    if (! objSetupHdModel.getStrPrintTDHItemsInBill().equalsIgnoreCase("Y"))
	    {
		SQL_BillDtl += "and a.tdhYN='N' ";
	    }
	    if (!objSetupHdModel.getStrPrintOpenItemsOnBill().equalsIgnoreCase("Y"))
	    {
		SQL_BillDtl += "and a.dblAmount>0 ";
	    }
	    SQL_BillDtl += " GROUP BY e.strGroupCode,a.strItemCode "
		    + "order BY e.strGroupCode ";
	    pst = con.prepareStatement(SQL_BillDtl);
	    pst.setString(1, billNo);
	    pst.setString(2, posCode);
	    ResultSet rs_BillDtl = pst.executeQuery();
	    while (rs_BillDtl.next())
	    {
		String groupName = rs_BillDtl.getString(7);
		if (groupName.trim().equalsIgnoreCase("BEVERAGES"))
		{
		    listGeneralBillDtl = listOfBeverageBillDetail;
		}
		//else if (groupName.trim().equalsIgnoreCase("LIQUER") || groupName.trim().equalsIgnoreCase("LIQUOR"))
		else if (groupName.trim().equalsIgnoreCase("ALCOHOL") || groupName.trim().equalsIgnoreCase("ALCOHOL"))
		{
		    listGeneralBillDtl = listOfLiqourBillDetail;
		}
		else
		{
		    listGeneralBillDtl = listOfFoodBillDetail;
		}

		double amt = rs_BillDtl.getDouble(4);
		if (amt == 0)
		{
		    sbZeroAmtItems.append(",");
		    sbZeroAmtItems.append("'" + rs_BillDtl.getString(5) + "'");
		}

		double saleQty = rs_BillDtl.getDouble(1);
		String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " "
			+ " where strBillNo='" + billNo + "' and strItemCode='" + rs_BillDtl.getString(5) + "'"
			+ " and date(dteBillDate)='" + billDate + "' ";

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
		ResultSet rsComplimentaryItems = objDatabseCon.funGetResultSet(sqlCompliBills);
		double compliQty = 1;
		if (rsComplimentaryItems.next())
		{
		    saleQty -= rsComplimentaryItems.getDouble(1);
		    compliQty = rsComplimentaryItems.getDouble(1);;
		}
		rsComplimentaryItems.close();

		if (saleQty > 0)
		{
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setDblQuantity(saleQty);
		    objBillDtl.setDblAmount(rs_BillDtl.getDouble(4));
		    objBillDtl.setStrItemName(rs_BillDtl.getString(2));
		    listGeneralBillDtl.add(objBillDtl);

		    String sqlModifier = "select count(*) "
			    + "from " + billModifierdtl + " where strBillNo=? and left(strItemCode,7)=? "
			    + " and date(dteBillDate)=? ";
		    if (!objSetupHdModel.getStrPrintZeroAmtModifierInBill().equalsIgnoreCase("Y"))
		    {
			sqlModifier += " and  dblAmount !=0.00 ";
		    }
		    pst = con.prepareStatement(sqlModifier);

		    pst.setString(1, billNo);
		    pst.setString(2, rs_BillDtl.getString(5));
		    pst.setString(3, billDate);
		    ResultSet rs_count = pst.executeQuery();
		    rs_count.next();
		    int cntRecord = rs_count.getInt(1);
		    rs_count.close();
		    if (cntRecord > 0)
		    {
			sqlModifier = "select strModifierName,dblQuantity,dblAmount "
				+ " from " + billModifierdtl + " "
				+ " where strBillNo=? and left(strItemCode,7)=? "
				+ " and date(dteBillDate)=? ";
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
				objBillDtl.setDblQuantity(rs_modifierRecord.getDouble(2));
				objBillDtl.setDblAmount(0);
				objBillDtl.setStrItemName(rs_modifierRecord.getString(1).toUpperCase());
				listGeneralBillDtl.add(objBillDtl);
			    }
			    else
			    {
				objBillDtl = new clsPOSBillDtl();
				objBillDtl.setDblQuantity(rs_modifierRecord.getDouble(2));
				objBillDtl.setDblAmount(rs_modifierRecord.getDouble(3));
				objBillDtl.setStrItemName(rs_modifierRecord.getString(1).toUpperCase());
				listGeneralBillDtl.add(objBillDtl);
			    }
			}
			rs_modifierRecord.close();
		    }
		}
	    }
	    rs_BillDtl.close();

	  //  objPrintingUtility.funPrintPromoItemsInBill(billNo, 4, listGeneralBillDtl);  // Print Promotion Items in Bill for this billno.
	  //  objPrintingUtility.funPrintComplimentaryItemsInBill(billNo, listGeneralBillDtl, 4, posCode, billDate, sbZeroAmtItems);

	    List<clsPOSBillDtl> listOfDiscountDtl = new ArrayList<>();
	    sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
		    + "from " + billDscFrom + " a ,tblreasonmaster b "
		    + "where  a.strDiscReasonCode=b.strReasonCode "
		    + "and a.strBillNo='" + billNo + "' "
		    + " and date(a.dteBillDate)='" + billDate + "' ";
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
		hm.put("Discount", discText + " " + discountOnItem);
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
	    String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount),b.strBillNote "
		    + " from " + billtaxdtl + " a,tbltaxhd b "
		    + " where a.strBillNo='" + billNo + "' and a.strTaxCode=b.strTaxCode "
		    + " group by a.strTaxCode "
		    + " order by b.strTaxDesc ";
	    ResultSet rsTax = objDatabseCon.funGetResultSet(sql_Tax);
	    while (rsTax.next())
	    {
		if (flgComplimentaryBill)
		{
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setDblAmount(0);
		    objBillDtl.setStrItemName(rsTax.getString(1));
		    listOfTaxDetail.add(objBillDtl);
		    hm.put("GSTNo", rsTax.getString(3));
		}
		else
		{
		    objBillDtl = new clsPOSBillDtl();
		    objBillDtl.setDblAmount(rsTax.getDouble(2));
		    objBillDtl.setStrItemName(rsTax.getString(1));
		    listOfTaxDetail.add(objBillDtl);
		    hm.put("GSTNo", rsTax.getString(3));
		}
	    }
	    rsTax.close();
	    List<clsPOSBillDtl> listOfGrandTotalDtl = new ArrayList<>();
	    if (Double.parseDouble(grandTotal) > 0)
	    {
		objBillDtl = new clsPOSBillDtl();
		objBillDtl.setDblAmount(Double.parseDouble(grandTotal));
		listOfGrandTotalDtl.add(objBillDtl);
	    }

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
	    String sqlTenderAmt = "select sum(dblPaidAmt),sum(dblSettlementAmt),(sum(dblPaidAmt)-sum(dblSettlementAmt)) RefundAmt "
		    + " from " + billSettlementdtl + " where strBillNo='" + billNo + "' "
		    + " and date(dteBillDate)='" + billDate + "' "
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
		hm.put("CUSTOMER_COUNT", rs_Count.getString(1));
	    }

	    List<clsPOSBillDtl> listOfServiceVatDetail = objPrintingUtility.funPrintServiceVatNoForJasper(billNo, billDate, billtaxdtl);
	    List<clsPOSBillDtl> listOfFooterDtl = new ArrayList<>();
	    
	    objBillDtl = new clsPOSBillDtl();
	    objBillDtl.setStrItemName(objSetupHdModel.getStrBillFooter());
//	    objBillDtl.setStrItemName("THANK YOU AND VISIT AGAIN !!!");
	    listOfFooterDtl.add(objBillDtl);
/*
	    if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		hm.put("ch", "V");
	    }
	    else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
		{
		    hm.put("ch", "V");
		}
		else
		{
		    hm.put("ch", "m");
		}
	    }

*/	    
	 
	    /* if (clsGlobalVarClass.gBenowIntegrationYN)
	    {
		String qrCodeImgPath = "QRCode.png";
		hm.put("qrCodeImgPath", qrCodeImgPath);

		String imgBHIMAppLogo = getClass().getResource("/com/POSGlobal/images/imgBHIMLogo.png").toString();
		String imgUPILogo = getClass().getResource("/com/POSGlobal/images/imgUPILogo.png").toString();

		hm.put("imgBHIMAppLogo", imgBHIMAppLogo);
		hm.put("imgUPILogo", imgUPILogo);
	    }
*/
	    hm.put("BillType", billType);
	    hm.put("listOfFoodBillDetail", listOfFoodBillDetail);
	    hm.put("listOfBeverageBillDetail", listOfBeverageBillDetail);
	    hm.put("listOfLiqourBillDetail", listOfLiqourBillDetail);
	    hm.put("listOfTaxDtl", listOfTaxDetail);
	    hm.put("listOfGrandTotalDtl", listOfGrandTotalDtl);
	    hm.put("listOfServiceVatDetail", listOfServiceVatDetail);
	    hm.put("listOfFooterDtl", listOfFooterDtl);
	    hm.put("listOfHomeDeliveryDtl", listOfHomeDeliveryDtl);
	    hm.put("listOfDiscountDtl", listOfDiscountDtl);
	    hm.put("listOfSettlementDetail", listOfSettlementDetail);
	    hm.put("listSubToal", listSubTotal);

	    hm.put("decimalFormaterForDoubleValue", gDecimalFormatString);
	    hm.put("decimalFormaterForIntegerValue", "0");

	    List<List<clsPOSBillDtl>> listData = new ArrayList<>();
	    listData.add(listOfFoodBillDetail);
	   
	    String reportName = reportName = "/WEB-INF/reports/billFormat/rptBillFormat10JasperBillPrint.jrxml";

	    JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listData);
	    InputStream is = this.getClass().getClassLoader().getResourceAsStream(reportName);
	    final JasperPrint print = JasperFillManager.fillReport(is, hm, beanColDataSource);
	    final String printer=objPrintingUtility.funGetPrinterDetails(posCode);

	    new Thread()
		{
			@Override
			public void run()
			{
			    objPrintingUtility.funPrintJasperExporterInThread(print,printer);
			}
		}.start();

//	    JRViewer viewer = new JRViewer(print);
//	    JFrame jf = new JFrame();
//	    jf.getContentPane().add(viewer);
//	    jf.validate();
//	    if (formName.equalsIgnoreCase("sales report"))
//	    {
//		jf.setVisible(true);
//		jf.setSize(new Dimension(500, 900));
//		jf.setLocationRelativeTo(null);
//		jf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//	    }
//	    else
//	    {
//		if (clsGlobalVarClass.gShowBill)
//		{
//		    jf.setVisible(true);
//		    jf.setSize(new Dimension(500, 900));
//		    jf.setLocationRelativeTo(null);
//		    jf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		}
//	    }
        /*   objPrintingUtility.funShowJasperFile(formName, print);

	    if (false)//clsGlobalVarClass.gMultiBillPrint
	    {
		for (int i = 0; i < 2; i++)
		{
		    new Thread()
		    {
			@Override
			public void run()
			{
			    if (viewORprint.equalsIgnoreCase("print"))
			    {
				funPrintJasperExporterInThread(print);
			    }
			}
		    }.start();
		}
	    }
	    else
	    {
		new Thread()
		{
		    @Override
		    public void run()
		    {
			if (viewORprint.equalsIgnoreCase("print"))
			{
			    funPrintJasperExporterInThread(print);
			}
		    }
		}.start();

	    }
*/	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
    
    
    /**
     *
     * @param print
     *//*
    public void funPrintJasperExporterInThread(JasperPrint print)
    {
	JRPrintServiceExporter exporter = new JRPrintServiceExporter();

	//--- Set print properties
	PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
	// printRequestAttributeSet.add(MediaSizeName.ISO_A6);
	// printRequestAttributeSet.add(MediaSizeName.MONARCH_ENVELOPE);

	printRequestAttributeSet.add(new Copies(1));

	//----------------------------------------------------     
	//printRequestAttributeSet.add(new Destination(new java.net.URI("file:d:/output/report.ps")));
	//----------------------------------------------------     
	PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();

	String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort;

	billPrinterName = billPrinterName.replaceAll("#", "\\\\");
	printServiceAttributeSet.add(new PrinterName(billPrinterName, null));

	//--- Set print parameters      
	exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
	exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
	exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
	exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
	exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

	//--- Print the document
	try
	{
	    exporter.exportReport();
	}
	catch (JRException e)
	{
	    e.printStackTrace();
	}

    }
*/
}
