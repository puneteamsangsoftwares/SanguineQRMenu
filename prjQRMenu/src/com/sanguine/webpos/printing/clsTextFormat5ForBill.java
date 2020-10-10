/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanguine.webpos.printing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsDatabaseConnection;
import com.sanguine.webpos.util.clsPOSUtilityController;

import javafx.scene.shape.Line;
@Controller
public class clsTextFormat5ForBill 
{

    // private DecimalFormat decimalFormat = new DecimalFormat("#.###");

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
	billDate = billDate.split(" ")[0];

	String Linefor5 = "  --------------------------------------";

	DecimalFormat decimalFormat = new DecimalFormat("#.###");
	DecimalFormat decimalFormat0Decimal = new DecimalFormat("0");
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
	    String billhd = "tblqbillhd";
	    String billdtl = "tblqbilldtl";
	    String billModifierdtl = "tblqbillmodifierdtl";
	    String billSettlementdtl = "tblqbillsettlementdtl";
	    String billtaxdtl = "tblqbilltaxdtl";
	    String billDscFrom = "tblqbilldiscdtl";
	    String billPromoDtl = "tblqbillpromotiondtl";
	    String billComplDtl = "tblqbillcomplementrydtl";

	    String advBookBillHd = "tblqadvbookbillhd";
	    String advBookBillDtl = "tblqadvbookbilldtl";
	    String advBookBillCharDtl = "tblqadvbookbillchardtl";
	    String advReceiptHd = "tblqadvancereceipthd";

	    objPrintingUtility.funCreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    File Text_Bill = new File(filePath + "/Temp/Temp_Bill.txt");
	    String subTotal = "";
	    String grandTotal = "";
	    String advAmount = "";
	    String deliveryCharge = "";
	    String customerCode = "";
	    String waiterName = "";
	    String tblName = "";
	    ResultSet rs_BillHD = null;
	    boolean flgComplimentaryBill = false;
	    double dblUSDRateConvertion = 0.00;
	    boolean isReprint = false;
	    StringBuilder sqlBillHeaderDtl = new StringBuilder();
	    sqlBillHeaderDtl.append("select ifnull(a.strTableNo,''),ifnull(a.strWaiterNo,''),a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal,"
		    + "ifnull(a.strCustomerCode,''),a.dblGrandTotal,a.dblTaxAmt,ifnull(a.strReasonCode,''),ifnull(a.strRemarks,''),a.strUserCreated "
		    + ",ifnull(dblDeliveryCharges,0.00),ifnull(i.dblAdvDeposite,0.00),a.dblDiscountPer,b.strPOSName,a.intPaxNo "
		    + ",ifnull(c.strTableName,''),ifnull(d.strWShortName,''),ifnull(d.strWFullName,''),ifnull(l.strSettelmentType,''),ifnull(j.strReasonName,'') as voidedReason, "
		    + "ifnull(g.strReasonName,''),ifnull(e.strCustomerName,''),ifnull(a.strAdvBookingNo,''),ifnull(h.strMessage,''),ifnull(h.strShape,''),ifnull(h.strNote,''),ifnull(a.dblTipAmount,0.00) "
		    + ",a.strOperationType,ifnull(a.strTakeAwayRemarks,''),ifnull(e.longMobileNo,''),ifnull(m.strCustType,''),ifnull(e.strExternalCode,'')"
		    + ",ifnull(DATE_FORMAT(date(e.dteDOB),'%d-%m-%Y'),''),ifnull(e.strGSTNo,''),a.strKOTToBillNote,dblUSDConverionRate,a.intOrderNo  "//39
		    + "from " + billhd + " a "
		    + "left outer join tblposmaster b on a.strposCode=b.strPosCode  "
		    + "left outer join tbltablemaster c on a.strTableNo=c.strTableNo and a.strClientCode=c.strClientCode "
		    + "left outer join tblwaitermaster d on a.strWaiterNo=d.strWaiterNo and a.strClientCode=d.strClientCode "
		    + "left outer join tblcustomermaster e on a.strCustomerCode=e.strCustomerCode and a.strClientCode=e.strClientCode "
		    + "left outer join tbldebitcardmaster f on a.strCardNo=f.strCardNo "
		    + "left outer join tblreasonmaster g on a.strReasonCode=g.strReasonCode "
		    + "left outer join " + advBookBillHd + " h on a.strAdvBookingNo=h.strAdvBookingNo and a.strClientCode=h.strClientCode "
		    + "left outer join " + advReceiptHd + " i on h.strAdvBookingNo=i.strAdvBookingNo and a.strClientCode=i.strClientCode "
		    + "left outer join tblvoidbillhd j on a.strBillNo=j.strBillNo and a.strposCode=j.strPosCode and a.strClientCode=j.strClientCode "
		    + "left outer join " + billSettlementdtl + " k on a.strBillNo=k.strBillNo and a.strClientCode=k.strClientCode AND DATE(a.dteBillDate)=DATE(k.dteBillDate) "
		    + "left outer join tblsettelmenthd l on k.strSettlementCode=l.strSettelmentCode "
		    + "LEFT OUTER JOIN tblcustomertypemaster m ON e.strCustomerType=m.strCustTypeCode "
		    + "where a.strBillNo=? "
		    + "and a.strposCode=? "
		    + "and date(a.dteBillDate)=? "
		    + "group by a.strBillNo; ");
	    pst = con.prepareStatement(sqlBillHeaderDtl.toString());
	    pst.setString(1, billNo);
	    pst.setString(2, posCode);
	    pst.setString(3, billDate);
	    rs_BillHD = pst.executeQuery();
	    if(rs_BillHD.next());
	    {
		if (rs_BillHD.getString(21).equals("Complementary"))
		{
		    flgComplimentaryBill = true;
		}
		
	    }
	    
	    FileWriter fstream_bill = new FileWriter(Text_Bill);
	    BufferedWriter BillOut = new BufferedWriter(fstream_bill);

	    dblUSDRateConvertion = rs_BillHD.getDouble(38);

	    if (clientCode.equals("117.001"))
	    {
		if (posCode.equals("P01"))
		{
		    objPrintingUtility.funPrintBlankSpace("THE PREM'S HOTEL", BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write("THE PREM'S HOTEL");
		    BillOut.newLine();
		}
		else if (posCode.equals("P02"))
		{
		    objPrintingUtility.funPrintBlankSpace("SWIG", BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write("SWIG");
		    BillOut.newLine();
		}
	    }

	    if (clientCode.equals("228.001"))//motimahal,chinawall
	    {
		if (posCode.equals("P01"))
		{
		    objPrintingUtility.funPrintBlankSpace("MR. MANCHURIAN", BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write("MR. MANCHURIAN");
		    BillOut.newLine();
		}
		else if (posCode.equals("P02"))
		{
		    objPrintingUtility.funPrintBlankSpace("INDIAN MASALA HOUSE", BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write("INDIAN MASALA HOUSE");
		    BillOut.newLine();
		}
	    }
	   

	  if(isOriginal)
	    {
		objPrintingUtility.funPrintBlankSpace("[ORIGINAL]", BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write("[ORIGINAL]");
		BillOut.newLine();
	    }
	   
	    if ("reprint".equalsIgnoreCase(reprint))
	    {
		isReprint = true;
		objPrintingUtility.funPrintBlankSpace("[DUPLICATE]", BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write("[DUPLICATE]");
		BillOut.newLine();
	    }
	    if (transType.equals("Void"))
	    {
		objPrintingUtility.funPrintBlankSpace("VOIDED BILL", BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write("VOIDED BILL");
		BillOut.newLine();
	    }
	    deliveryCharge = rs_BillHD.getString(13);
	    boolean flag_isHomeDelvBill = false;
	    String SQL_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 ,dblHomeDeliCharge "
		    + "from tblhomedelivery "
		    + "where strBillNo=? "
		    + "and date(dteDate)=? ;";
	    pst = con.prepareStatement(SQL_HomeDelivery);
	    pst.setString(1, billNo);
	    pst.setString(2, billDate);
	    ResultSet rs_HomeDelivery = pst.executeQuery();
	    if (rs_HomeDelivery.next())
	    {

		flag_isHomeDelvBill = true;
		customerCode = rs_HomeDelivery.getString(2);
		deliveryCharge = rs_HomeDelivery.getString(6);
		if (objSetupHdModel.getStrPrintHomeDeliveryYN().equalsIgnoreCase("Y"))
		{
		    objPrintingUtility.funPrintBlankSpace("HOME DELIVERY", BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write("HOME DELIVERY");
		    BillOut.newLine();
		}

		String SQL_CustomerDtl = "";
		if (rs_HomeDelivery.getString(5).equals("Temporary"))
		{
		    SQL_CustomerDtl = "select a.strCustomerName,a.strTempAddress,a.strTempStreet"
			    + " ,a.strTempLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo,a.strGSTNo "
			    + " from tblcustomermaster a "
			    + " left outer join tblbuildingmaster b "
			    + " on a.strBuldingCode=b.strBuildingCode "
			    + " where a.strCustomerCode=? ;";
		}
		else if (rs_HomeDelivery.getString(5).equals("Office"))
		{
		    SQL_CustomerDtl = "select a.strCustomerName,a.strOfficeBuildingName,a.strOfficeStreetName"
			    + ",a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity,a.strOfficePinCode,a.longMobileNo,a.strGSTNo "
			    + " from tblcustomermaster a "
			    + " where a.strCustomerCode=? ";
		}
		else
		{
		    SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName"
			    + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo,a.strGSTNo "
			    + " from tblcustomermaster a left outer join tblbuildingmaster b "
			    + " on a.strBuldingCode=b.strBuildingCode "
			    + " where a.strCustomerCode=? ;";
		}
		pst = con.prepareStatement(SQL_CustomerDtl);
		pst.setString(1, rs_HomeDelivery.getString(2));
		ResultSet rs_CustomerDtl = pst.executeQuery();
		while (rs_CustomerDtl.next())
		{
		    BillOut.write("  NAME      :" + rs_CustomerDtl.getString(1).toUpperCase());
		    BillOut.newLine();
		    // Building Name    
		    String add = rs_CustomerDtl.getString(2);
		    int strlen = add.length();
		    String add1 = "";
		    if (strlen < 28)
		    {
			add1 = add.substring(0, strlen);
			BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
			BillOut.newLine();
		    }
		    else
		    {
			add1 = add.substring(0, 28);
			BillOut.write("  ADDRESS1  :" + add1.toUpperCase().replaceAll("\n", " "));
			BillOut.newLine();
		    }
		    for (int i = 28; i <= strlen;)
		    {
			int end = 0;
			end = i + 28;
			if (strlen > end)
			{
			    add1 = add.substring(i, end);
			    i = end;
			    BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
			    BillOut.newLine();
			}
			else
			{
			    add1 = add.substring(i, strlen);
			    BillOut.write("             " + add1.toUpperCase().replaceAll("\n", " "));
			    BillOut.newLine();
			    i = strlen + 1;
			}
		    }
		    // Street Name    
		    String street = rs_CustomerDtl.getString(3);
		    String street1;
		    int streetlen = street.length();
		    for (int i = 0; i <= streetlen;)
		    {
			int end = 0;
			end = i + 28;
			if (streetlen > end)
			{
			    street1 = street.substring(i, end);
			    BillOut.write("             " + street1.toUpperCase());
			    BillOut.newLine();
			    i = end;
			}
			else
			{
			    street1 = street.substring(i, streetlen);
			    BillOut.write("             " + street1.toUpperCase());
			    BillOut.newLine();
			    i = streetlen + 1;
			}
		    }
		    // Landmark Name    
		    if (rs_CustomerDtl.getString(4).trim().length() > 0)
		    {
			BillOut.write("             " + rs_CustomerDtl.getString(4).toUpperCase());
			BillOut.newLine();
		    }
		    // Area Name    
		    if (rs_CustomerDtl.getString(5).trim().length() > 0)
		    {
			BillOut.write("             " + rs_CustomerDtl.getString(5).toUpperCase());
			BillOut.newLine();
		    }
		    // City Name    
		    if (rs_CustomerDtl.getString(6).trim().length() > 0)
		    {
			BillOut.write("             " + rs_CustomerDtl.getString(6).toUpperCase());
			BillOut.newLine();
		    }
		    // Pin Code    
		    if (rs_CustomerDtl.getString(7).trim().length() > 0)
		    {
			BillOut.write("             " + rs_CustomerDtl.getString(7).toUpperCase());
			BillOut.newLine();
		    }
		    // Mobile No    
		    BillOut.write("  MOBILE NO :" + rs_CustomerDtl.getString(8));
		    BillOut.newLine();

		    // GST No    
		    BillOut.write("  GST NO    :" + rs_CustomerDtl.getString(9));
		    BillOut.newLine();
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
		    BillOut.write("  DELV BOY  :" + strIN);
		    BillOut.newLine();
		    rs_DeliveryBoyDtl.close();
		}
		BillOut.write(dashedLineFor40Chars);
		BillOut.newLine();
	    }
	    else
	    {
		String customerType = rs_BillHD.getString(33);

		if (rs_BillHD.getString(7).length() > 0 && !(customerType.equalsIgnoreCase("Liqour") || customerType.equalsIgnoreCase("Liquor")))//customerCode
		{
		    BillOut.write("  NAME      :" + rs_BillHD.getString(24).toUpperCase());
		    BillOut.newLine();
		    // Mobile No    
		    BillOut.write("  MOBILE NO :" + rs_BillHD.getString(32));
		    BillOut.newLine();
		    // customer GST No    
		    BillOut.write("  GST NO    :" + rs_BillHD.getString(36));
		    BillOut.newLine();
		}
	    }
	    rs_HomeDelivery.close();
	    boolean flag_MakeKot=false;
            if(rs_BillHD.getString(18).length()>0)
	    {
		flag_MakeKot=true;
	    }
	    if(objSetupHdModel.getStrPrintOrderNoOnBillYN().equalsIgnoreCase("Y") && !flag_MakeKot)
	    {
		BillOut.write("         Your Order No is : " +rs_BillHD.getInt(39));
		BillOut.newLine();
		
	    }
	    else if(objSetupHdModel.getStrPrintOrderNoOnMakeKot().equalsIgnoreCase("Y") && flag_MakeKot)
	    {
		BillOut.write("         Your Order No is : " +rs_BillHD.getInt(39));
		BillOut.newLine();
		
	    }
	    //print take away
	    int billPrintSize = 4;
	    if (rs_BillHD.getString(30).equals("TakeAway"))
	    {
		objPrintingUtility.funPrintBlankSpace("Take Away", BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write("Take Away");
		BillOut.newLine();
	    }
	    if (objSetupHdModel.getStrPrintTaxInvoiceOnBill().equalsIgnoreCase("Y"))
	    {
		objPrintingUtility.funPrintBlankSpace("TAX INVOICE", BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write("TAX INVOICE");
		BillOut.newLine();
	    }
	    if (clientCode.equals("047.001") && posCode.equals("P03"))
	    {
		objPrintingUtility.funPrintBlankSpace("SHRI SHAM CATERERS", BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write("SHRI SHAM CATERERS");
		BillOut.newLine();
		String cAddr1 = "Flat No.7, Mon Amour,";
		objPrintingUtility.funPrintBlankSpace(cAddr1, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr1.toUpperCase());
		BillOut.newLine();
		String cAddr2 = "Thorat Colony,Prabhat Road,";
		objPrintingUtility.funPrintBlankSpace(cAddr2, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr2.toUpperCase());
		BillOut.newLine();
		String cAddr3 = " Erandwane, Pune 411 004.";
		objPrintingUtility.funPrintBlankSpace(cAddr3, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr3.toUpperCase());
		BillOut.newLine();
		String cAddr4 = "Approved Caterers of";
		objPrintingUtility.funPrintBlankSpace(cAddr4, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr4.toUpperCase());
		BillOut.newLine();
		String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
		objPrintingUtility.funPrintBlankSpace(cAddr5, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr5.toUpperCase());
		BillOut.newLine();
	    }
	    else if (clientCode.equals("047.001") && posCode.equals("P02"))
	    {
		objPrintingUtility.funPrintBlankSpace("SHRI SHAM CATERERS", BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write("SHRI SHAM CATERERS");
		BillOut.newLine();
		String cAddr1 = "Flat No.7, Mon Amour,";
		objPrintingUtility.funPrintBlankSpace(cAddr1, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr1.toUpperCase());
		BillOut.newLine();
		String cAddr2 = "Thorat Colony,Prabhat Road,";
		objPrintingUtility.funPrintBlankSpace(cAddr2, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr2.toUpperCase());
		BillOut.newLine();
		String cAddr3 = " Erandwane, Pune 411 004.";
		objPrintingUtility.funPrintBlankSpace(cAddr3, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr3.toUpperCase());
		BillOut.newLine();
		String cAddr4 = "Approved Caterers of";
		objPrintingUtility.funPrintBlankSpace(cAddr4, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr4.toUpperCase());
		BillOut.newLine();
		String cAddr5 = "ROYAL CONNAUGHT BOAT CLUB";
		objPrintingUtility.funPrintBlankSpace(cAddr5, BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(cAddr5.toUpperCase());
		BillOut.newLine();
	    }
	    else if (clientCode.equals("092.001") || clientCode.equals("092.002") || clientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
	    {
		objPrintingUtility.funPrintBlankSpace("SSPL", BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write("SSPL");
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
	    else if (clientCode.equals("092.001") || clientCode.equals("092.002") || clientCode.equals("092.003"))//Shree Sound Pvt. Ltd.
	    {
		objPrintingUtility.funPrintBlankSpace("SSPL", BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write("SSPL");
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
	    else if (clientCode.equals("190.001"))//"190.001", "SQUARE ONE HOSPITALITY LLP" (Quarter House)
	    {
		String licenseName =objSetupHdModel.getStrClientName();
		if (billNo.startsWith("L"))//liqour bill license name
		{
		    licenseName = "STEP-IN-AGENCIES & ESTATE PVT LTD";
		}
		else
		{
		    licenseName = objSetupHdModel.getStrClientName();
		}
		objPrintingUtility.funPrintBlankSpace(licenseName, BillOut,objSetupHdModel.getIntColumnSize());

		BillOut.write(licenseName);
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
//	    else if (clsGlobalVarClass.gClientCode.equals("197.001"))//"197.001", "Juhu Hotel Pvt Ltd"
//	    {
//		objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gClientName, BillOut);
//		BillOut.write(clsGlobalVarClass.gClientName.toUpperCase());
//		BillOut.newLine();
//		objPrintingUtility.funPrintBlankSpace("[REZBERRY RHINOCERES]", BillOut);
//		BillOut.write("[REZBERRY RHINOCERES]".toUpperCase());
//		BillOut.newLine();
//
//		objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gClientAddress1, BillOut);
//		BillOut.write(clsGlobalVarClass.gClientAddress1.toUpperCase());
//		BillOut.newLine();
//		if (clsGlobalVarClass.gClientAddress2.trim().length() > 0)
//		{
//		    objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gClientAddress2, BillOut);
//		    BillOut.write(clsGlobalVarClass.gClientAddress2.toUpperCase());
//		    BillOut.newLine();
//		}
//		if (clsGlobalVarClass.gClientAddress3.trim().length() > 0)
//		{
//		    objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gClientAddress3, BillOut);
//		    BillOut.write(clsGlobalVarClass.gClientAddress3.toUpperCase());
//		    BillOut.newLine();
//		}
//		if (clsGlobalVarClass.gCityName.trim().length() > 0)
//		{
//		    objPrintingUtility.funPrintBlankSpace(clsGlobalVarClass.gCityName, BillOut);
//		    BillOut.write(clsGlobalVarClass.gCityName.toUpperCase());
//		    BillOut.newLine();
//		}
//	    }
	    else if (clientCode.equals("224.001"))//"224.001", "XO ZERO LOUNGE"
	    {
		String licenseName =objSetupHdModel.getStrClientName();
		if (billNo.startsWith("F"))//Food bill license name
		{
		    licenseName = "XO ZERO LOUNGE";
		}
		else if (billNo.startsWith("L"))//Liquor bill license name
		{
		    licenseName = "WILDFIRE RESTAURANT AND BAR";
		}
		else if (billNo.startsWith("H"))//Hukka bill license name
		{
		    licenseName = "XO ZERO LOUNGE";
		}
		else
		{
		    licenseName = objSetupHdModel.getStrClientName();
		}
		objPrintingUtility.funPrintBlankSpace(licenseName, BillOut,objSetupHdModel.getIntColumnSize());

		BillOut.write(licenseName);
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
	    else if (clientCode.equals("228.001"))//"Motimahal n china wall
	    {

		String licenseName =objSetupHdModel.getStrClientName();
		if (posCode.equals("P01"))
		{
		    licenseName = "MR. MANCHURIAN-KAM Foods and Beverage";
		}
		else if (posCode.equals("P02"))
		{
		    licenseName = "INDIAN MASALA HOUSE-MYK Foods and Beverage";
		}

		objPrintingUtility.funPrintBlankSpace(licenseName, BillOut,objSetupHdModel.getIntColumnSize());

		BillOut.write(licenseName);
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
	    else if (clientCode.equals("148.001"))//MURPHIES
	    {

		String licenseName =objSetupHdModel.getStrClientName();
		if (billNo.startsWith("L"))//Food bill license name
		{
		    licenseName = "PINGALE HOSPITALITY";
		}
		else if (billNo.startsWith("F"))//Food bill license name
		{
		    licenseName = "JYOTI ENTERPRISES";
		}
		else
		{
		    licenseName = objSetupHdModel.getStrClientName();
		}
		objPrintingUtility.funPrintBlankSpace(licenseName, BillOut,objSetupHdModel.getIntColumnSize());

		BillOut.write(licenseName);
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
	    else if (clientCode.equals("251.001"))//Tribe Hospitality
	    {

		String licenseName = "NOMAD THE LOUNGE BAR";

		objPrintingUtility.funPrintBlankSpace(licenseName, BillOut,objSetupHdModel.getIntColumnSize());

		BillOut.write(licenseName);
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
	    else if (clientCode.equals("048.001"))//MURPHIES
	    {

		String licenseName =objSetupHdModel.getStrClientName();
		if (billNo.startsWith("F"))//Food bill license name
		{
		    licenseName = "JAI SANTOSHI MAA HOSPITALITY";
		}
		else if (billNo.startsWith("L"))//Liquor bill license name
		{
		    licenseName = "SHRI SIDDHI VINAYAK FOODS";
		}
		else
		{
		    licenseName =objSetupHdModel.getStrClientName();
		}
		objPrintingUtility.funPrintBlankSpace(licenseName, BillOut,objSetupHdModel.getIntColumnSize());

		BillOut.write(licenseName);
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
	    else if (clientCode.equals("309.001"))//" D Hopsitality
	    {
		String licenseName = objSetupHdModel.getStrClientName();
		if (posCode.equals("P01"))
		{
		    licenseName = "D SQUARE HOSPITALITY";
		}
		else if (posCode.equals("P02"))
		{
		    licenseName = "POUNDBERRY HOSPITALITY";
		}

		objPrintingUtility.funPrintBlankSpace(licenseName, BillOut,objSetupHdModel.getIntColumnSize());

		BillOut.write(licenseName);
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
	    else
	    {
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrClientName(), BillOut,objSetupHdModel.getIntColumnSize());
		if (clientCode.equals("124.001"))
		{
		    BillOut.write(objSetupHdModel.getStrClientName());
		}
		else
		{
		    BillOut.write(objSetupHdModel.getStrClientName().toUpperCase());
		}
		BillOut.newLine();
		objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine1(), BillOut,objSetupHdModel.getIntColumnSize());
		BillOut.write(objSetupHdModel.getStrAddressLine1().toUpperCase());
		BillOut.newLine();
		if (objSetupHdModel.getStrAddressLine2().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine2(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine2().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrAddressLine3().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrAddressLine3(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrAddressLine3().toUpperCase());
		    BillOut.newLine();
		}
		if (objSetupHdModel.getStrCityName().trim().length() > 0)
		{
		    objPrintingUtility.funPrintBlankSpace(objSetupHdModel.getStrCityName(), BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write(objSetupHdModel.getStrCityName().toUpperCase());
		    BillOut.newLine();
		}
	    }
	    if (!clientCode.equalsIgnoreCase("247.001"))// BHAGAT TARACHAND
	    {
		BillOut.write("  TEL NO.    :" + " ");
		BillOut.write(objSetupHdModel.getStrTelephoneNo());
		BillOut.newLine();
		BillOut.write("  EMAIL ID   :" + " ");
		BillOut.write(objSetupHdModel.getStrEmail());
		BillOut.newLine();
	    }
          
	    tblName = rs_BillHD.getString(18);
	    if (tblName.length() > 0)
	    {
		
		if (clientCode.equalsIgnoreCase("136.001"))//KINKI
		{
		    BillOut.write("  TABLE No   :");
		}
		else
		{
		    BillOut.write("  TABLE NAME:" + "  ");
		}
		BillOut.write(tblName);
		BillOut.newLine();
	    }
	    waiterName = rs_BillHD.getString(19);
	    if (waiterName.trim().length() > 0)
	    {
		BillOut.write("  STEWARD   :" + "  ");
		BillOut.write(waiterName);
		BillOut.newLine();
	    }

	    String kotToBillNote = rs_BillHD.getString(37);
	    if (kotToBillNote.trim().length() > 0)
	    {
		if(kotToBillNote.contains("OrderFrom")){
		    //get online Order service provider name and Code (For Wera service) //OrderFromONLINE 304595
		    objPrintingUtility.funPrintContentWithSpace("Right", "  "+kotToBillNote.substring(9, kotToBillNote.indexOf(" "))+" CODE", 13, BillOut);
		    //BillOut.write(": "+kotToBillNote.split(" ")[1]);
		    String strKotBillNote=kotToBillNote.split(" ")[1];
		    BillOut.write(": "+strKotBillNote.split("#")[0]);
		    BillOut.newLine();
		    BillOut.write("  OTP        : "+kotToBillNote.split("#")[1]);
		}else{
		    BillOut.write("  ZOMATO CODE:" + "  ");
		    BillOut.write(kotToBillNote);
		}
		BillOut.newLine();
	    }

	    BillOut.write(Linefor5);
	    BillOut.newLine();
	    BillOut.write("  POS         : ");
	    BillOut.write(rs_BillHD.getString(16));
	    BillOut.newLine();
	    BillOut.write("  BILL NO.    : ");
	    BillOut.write(billNo);
	    BillOut.newLine();
	    BillOut.write("  PAX NO.     : ");
	    BillOut.write(rs_BillHD.getString(17));
	    BillOut.newLine();
	    if (objSetupHdModel.getStrPrintTimeOnBill().equalsIgnoreCase("Y"))
	    {
		BillOut.write("  DATE & TIME : ");
		SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
		BillOut.write(ft.format(rs_BillHD.getObject(3)));
		BillOut.newLine();

	    }
	    else
	    {
		BillOut.write("  DATE        : ");
		SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
		BillOut.write(ft.format(rs_BillHD.getObject(3)));
		BillOut.newLine();
	    }
	    if (rs_BillHD.getString(11).trim().length() > 0 && !flgComplimentaryBill)
	    {
		String textRemarks = rs_BillHD.getString(11).trim();
		String lblRemarks = "  Remarks     : ";

		String remarks = lblRemarks + textRemarks;
		if (remarks.length() > 40)
		{
		    BillOut.write(remarks.substring(0, 40));
		    BillOut.newLine();
		    remarks = remarks.substring(40, remarks.length());

		    BillOut.write("               " + remarks);
		    BillOut.newLine();
		}
		else
		{
		    BillOut.write(remarks);
		    BillOut.newLine();
		}

	    }
	    subTotal = rs_BillHD.getString(6);
	    grandTotal = rs_BillHD.getString(8);
	    user = rs_BillHD.getString(12);
	    
	    advAmount = rs_BillHD.getString(14);
	    //print card available balance
	    String isSttled = "select a.strBillNo from " + billSettlementdtl + " a," + billhd + " b "
		    + "where a.strBillNo=b.strBillNo "
		    + "and a.strClientCode=b.strClientCode "
		    + "and a.strBillNo='" + billNo + "' "
		    + "and b.strposCode='" + posCode + "' "
		    + "and date(a.dteBillDate)=date(b.dteBillDate) ";
	    ResultSet rsIsSettled = objDatabseCon.funGetResultSet(isSttled);
	    if (rsIsSettled.next())
	    {
		rsIsSettled.close();
		String availBal = "select a.strCardNo,(b.dblRedeemAmt)"
			+ "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
			+ "where a.strBillNo='" + billNo + "' "
			+ "and a.strposCode='" + posCode + "' "
			+ "and date(a.dteBillDate)='" + billDate + "' ";
		ResultSet rsAvailBal = objDatabseCon.funGetResultSet(availBal);
		if (rsAvailBal.next())
		{
		    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
		    BillOut.newLine();
		}
	    }
	    else
	    {
		String availBal = "select a.strCardNo,(b.dblRedeemAmt-a.dblGrandTotal)"
			+ "from " + billhd + " a inner join tbldebitcardmaster b on a.strCardNo=b.strCardNo "
			+ "where a.strBillNo='" + billNo + "' "
			+ "and a.strposCode='" + posCode + "' "
			+ "and date(a.dteBillDate)='" + billDate + "' ";
		ResultSet rsAvailBal = objDatabseCon.funGetResultSet(availBal);
		if (rsAvailBal.next())
		{
		    BillOut.write("  Available Balance(" + rsAvailBal.getString(1) + "):" + rsAvailBal.getString(2));
		    BillOut.newLine();
		}
	    }
	    //print card available balance
	    if (transType.equals("Void"))
	    {
		BillOut.write("  Reason      :" + " " + rs_BillHD.getString(22));//voided reason
		BillOut.newLine();
	    }
	    else if (flgComplimentaryBill)
	    {

		BillOut.write("  Reason      :" + " " + rs_BillHD.getString(23));
		BillOut.newLine();
		BillOut.write("  Remark      :" + " " + rs_BillHD.getString(11));
		BillOut.newLine();
	    }
	    if(objSetupHdModel.getStrCMSIntegrationYN().equalsIgnoreCase("Y"))
	    {
		BillOut.write("  Member Code : ");
		BillOut.write(rs_BillHD.getString(7));
		BillOut.newLine();
		BillOut.write("  Member Name : ");
		objPrintingUtility.funWriteToTextMemberNameForFormat5(BillOut, rs_BillHD.getString(24), "Format5");
		BillOut.newLine();
		BillOut.write(Linefor5);
	    }
	    String customerType = rs_BillHD.getString(33);
	    if (customerType.equalsIgnoreCase("Liqour") || customerType.equalsIgnoreCase("Liquor"))
	    {
		if (rs_BillHD.getString(7).length() > 0)//customerCode
		{
		    BillOut.write("  NAME        : " + rs_BillHD.getString(24).toUpperCase());
		    BillOut.newLine();

		    BillOut.write("  PERMIT NO   : " + rs_BillHD.getString(34));
		    BillOut.newLine();

		    BillOut.write("  EXP. DATE   : " + rs_BillHD.getString(35));
		    BillOut.newLine();
		}
	    }

	    if (rs_BillHD.getString(25) != null && rs_BillHD.getString(25).length() > 0)
	    {
		if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
		{
		    BillOut.newLine();
		    objPrintingUtility.funPrintBlankSpace("ORDER DETAIL", BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write("ORDER DETAIL");
		    BillOut.newLine();
		    BillOut.write(Linefor5);
		    BillOut.newLine();
		}
		StringBuilder strValue = new StringBuilder();
		strValue.setLength(0);
		if (rs_BillHD.getString(26).length() > 0)
		{
		    strValue.append(rs_BillHD.getString(26));
		}
		else
		{
		    strValue.append("");
		}
		int strlenMsg = strValue.length();
		if (strlenMsg > 0)
		{
		    String msg1 = "";
		    if (strlenMsg < 27)
		    {
			msg1 = strValue.substring(0, strlenMsg);
			BillOut.write("  MESSAGE     :" + msg1);
			BillOut.newLine();
		    }
		    else
		    {
			msg1 = strValue.substring(0, 27);
			BillOut.write("  MESSAGE     :" + msg1);;
			BillOut.newLine();
		    }
		    for (int i = 27; i <= strlenMsg; i++)
		    {
			int endmsg = 0;
			endmsg = i + 27;
			if (strlenMsg > endmsg)
			{
			    msg1 = strValue.substring(i, endmsg);
			    i = endmsg;
			    BillOut.write("               " + msg1);
			    BillOut.newLine();
			}
			else
			{
			    msg1 = strValue.substring(i, strlenMsg);
			    BillOut.write("               " + msg1);
			    BillOut.newLine();
			    i = strlenMsg + 1;
			}
		    }
		}
		strValue.setLength(0);
		if (rs_BillHD.getString(27).length() > 0)//shape
		{
		    strValue.append(rs_BillHD.getString(27));
		}
		else
		{
		    strValue.append("");
		}
		int strlenShape = strValue.length();
		if (strlenShape > 0)
		{
		    String shape1 = "";
		    if (strlenShape < 27)
		    {
			shape1 = strValue.substring(0, strlenShape);
			BillOut.write("  SHAPE       :" + shape1);
			BillOut.newLine();
		    }
		    else
		    {
			shape1 = strValue.substring(0, 27);
			BillOut.write("  SHAPE       :" + shape1);
			BillOut.newLine();
		    }
		    for (int j = 27; j <= strlenShape; j++)
		    {
			int endShape = 0;
			endShape = j + 27;
			if (strlenShape > endShape)
			{
			    shape1 = strValue.substring(j, endShape);
			    j = endShape;
			    BillOut.write("               " + shape1);
			    BillOut.newLine();
			}
			else
			{
			    shape1 = strValue.substring(j, strlenShape);
			    BillOut.write("               " + shape1);
			    BillOut.newLine();
			    j = strlenShape + 1;
			}
		    }
		}

		strValue.setLength(0);
		if (rs_BillHD.getString(28).length() > 0)//note
		{
		    strValue.append(rs_BillHD.getString(28));
		}
		else
		{
		    strValue.append("");
		}
		int strlenNote = strValue.length();
		if (strlenNote > 0)
		{
		    String note1 = "";
		    if (strlenNote < 27)
		    {
			note1 = strValue.substring(0, strlenNote);
			BillOut.write("  NOTE        :" + note1);
			BillOut.newLine();
		    }
		    else
		    {
			note1 = strValue.substring(0, 27);
			BillOut.write("  NOTE        :" + note1);
			BillOut.newLine();
		    }
		    for (int i = 27; i <= strlenNote; i++)
		    {
			int endNote = 0;
			endNote = i + 27;
			if (strlenNote > endNote)
			{
			    note1 = strValue.substring(i, endNote);
			    i = endNote;
			    BillOut.write("               " + note1);
			    BillOut.newLine();
			}
			else
			{
			    note1 = strValue.substring(i, strlenNote);
			    BillOut.write("               " + note1);
			    BillOut.newLine();
			    i = strlenNote + 1;
			}
		    }
		}
		if (rs_BillHD.getString(26).length() > 0 || rs_BillHD.getString(27).length() > 0 || rs_BillHD.getString(28).length() > 0)
		{

		    BillOut.write(Linefor5);
		    BillOut.newLine();
		}
	    }
	    
            
	    
	    StringBuilder sbZeroAmtItems = new StringBuilder();

	    double totalQty=0.00;
	    
	    BillOut.write(Linefor5);
	    BillOut.newLine();
	    BillOut.write("     QTY ITEM NAME                  AMT");
	    BillOut.newLine();
	    BillOut.write(Linefor5);
	    BillOut.newLine();
	    String SQL_BillDtl = "select sum(a.dblQuantity),left(a.strItemName,22) as ItemLine1"
		    + " ,MID(a.strItemName,23,LENGTH(a.strItemName)) as ItemLine2"
		    + " ,sum(a.dblAmount),a.strItemCode,a.strKOTNo "
		    + " from " + billdtl + " a," + billhd + " b,tblitemmaster c "
		    + " where a.strBillNo=b.strBillNo and a.strClientCode=b.strClientCode "
		    + " and a.strItemCode=c.strItemCode and date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and a.strBillNo=? "
		    + " and b.strposCode=?  "
		    + " and date(b.dteBillDate)=? ";
	    if (!objSetupHdModel.getStrPrintTDHItemsInBill().equalsIgnoreCase("Y"))
	    {
		SQL_BillDtl += "and a.tdhYN='N' ";
	    }
	    if (!objSetupHdModel.getStrPrintOpenItemsOnBill().equalsIgnoreCase("Y"))
	    {
		SQL_BillDtl += "and c.strOpenItem='N' ";
	    }
	    SQL_BillDtl += " group by a.strItemCode ";
	    pst = con.prepareStatement(SQL_BillDtl);
	    pst.setString(1, billNo);
	    pst.setString(2, posCode);
	    pst.setString(3, billDate);
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
			+ " where strBillNo='" + billNo + "' "
			+ " and strItemCode='" + rs_BillDtl.getString(5) + "' "
			+ " and strPromoType='ItemWise' "
			+ " and date(dteBillDate)='" + billDate + "' ";
		ResultSet rsPromoItems = objDatabseCon.funGetResultSet(sqlPromoBills);
		if (rsPromoItems.next())
		{
		    saleQty -= rsPromoItems.getDouble(1);
		}
		rsPromoItems.close();

		boolean flgComplBill = false;
		String sqlCompliBills = "select SUM(dblQuantity) from " + billComplDtl + " "
			+ " where strBillNo='" + billNo + "' "
			+ " and strItemCode='" + rs_BillDtl.getString(5) + "' "
			+ " and strType='ItemComplimentary' "
			+ " and date(dteBillDate)='" + billDate + "'  group by strItemCode ";
		ResultSet rsComplimentaryItems = objDatabseCon.funGetResultSet(sqlCompliBills);
		double compliQty = 1;
		if (rsComplimentaryItems.next())
		{
		    saleQty -= rsComplimentaryItems.getDouble(1);
		    compliQty = rsComplimentaryItems.getDouble(1);;
		    flgComplBill = true;
		}
		rsComplimentaryItems.close();

		String qty = String.valueOf(saleQty);
		if (saleQty == 0 && flgComplBill)
		{
		    qty = String.valueOf(compliQty);
		}
		if (qty.contains("."))
		{
		    String decVal = qty.substring(qty.length() - 2, qty.length());
		    if (Double.parseDouble(decVal) == 0)
		    {
			qty = qty.substring(0, qty.length() - 2);
		    }
		}
		if (saleQty >= 0)
		{

		    if (Double.parseDouble(qty) > 0)
		    {
			totalQty+=Double.parseDouble(qty);
			
			objPrintingUtility.funPrintContentWithSpace("Right", qty, 8, BillOut);//Qty Print
			BillOut.write(" ");
			objPrintingUtility.funPrintContentWithSpace("Left", rs_BillDtl.getString(2), 22, BillOut);//Item Name
			if (flgComplimentaryBill)
			{
			    objPrintingUtility.funPrintContentWithSpace("Right", "0.00", 9, BillOut);//Amount
			}
			else
			{
			    objPrintingUtility.funPrintContentWithSpace("Right", gDecimalFormat.format(rs_BillDtl.getDouble(4)), 9, BillOut);//Amount
			}
			BillOut.newLine();

			if (rs_BillDtl.getString(3).trim().length() > 0)
			{
			    String line = rs_BillDtl.getString(3);
			    if (line.length() > 22)
			    {
				BillOut.write("         " + line.substring(0, 22));
				BillOut.newLine();

				BillOut.write("         " + line.substring(22, line.length()));
				BillOut.newLine();
			    }
			    else
			    {
				BillOut.write("         " + line);
				BillOut.newLine();
			    }
			}
			String sqlModifier = "select count(*) "
				+ "from " + billModifierdtl + " "
				+ "where strBillNo=? "
				+ "and left(strItemCode,7)=? "
				+ "and date(dteBillDate)=? ";
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
			    sqlModifier = "SELECT left(strModifierName,22)line1,dblQuantity,dblAmount,MID(strModifierName,23,LENGTH(strModifierName)) as line2  "
				    + " from " + billModifierdtl + " "
				    + " where strBillNo=? "
				    + " and left(strItemCode,7)=? "
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

				double modifierAmt = rs_modifierRecord.getDouble(3);
				int modifierQty = rs_modifierRecord.getInt(2);
				if (modifierAmt > 0)
				{
				    objPrintingUtility.funPrintContentWithSpace("Right", String.valueOf(modifierQty), 8, BillOut);//Qty Print
				    BillOut.write(" ");
				}
				else
				{
				    objPrintingUtility.funPrintContentWithSpace("Right", "", 8, BillOut);//Qty Print
				    BillOut.write(" ");
				}
				objPrintingUtility.funPrintContentWithSpace("Left", rs_modifierRecord.getString(1).toUpperCase(), 22, BillOut);//Item Name
				if (flgComplimentaryBill)
				{
				    objPrintingUtility.funPrintContentWithSpace("Right", "0.00", 9, BillOut);//Amount
				}
				else
				{
				    objPrintingUtility.funPrintContentWithSpace("Right", gDecimalFormat.format(rs_modifierRecord.getDouble(3)), 9, BillOut);//Amount
				}
				BillOut.newLine();
				
				if (rs_modifierRecord.getString(4).trim().length() > 0)
				{
				    String line2 = rs_modifierRecord.getString(4).toUpperCase();
				    if (line2.length() > 22)
				    {
					BillOut.write("         " + line2.substring(0, 22));
					BillOut.newLine();

					BillOut.write("         " + line2.substring(22, line2.length()));
					BillOut.newLine();
				    }
				    else
				    {
					BillOut.write("         " + line2);
					BillOut.newLine();
				    }
				}
				
				
			    }
			    rs_modifierRecord.close();
			}

			String sql = "select b.strItemCode,b.dblWeight "
				+ " from " + billhd + " a," + advBookBillDtl + " b "
				+ " where a.strAdvBookingNo=b.strAdvBookingNo "
				+ " and a.strClientCode=b.strClientCode "
				+ " and a.strBillNo='" + billNo + "' "
				+ " and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
				+ " and a.strposCode='" + posCode + "' "
				+ " and date(a.dteBillDate)='" + billDate + "' ";
			ResultSet rsWeight = objDatabseCon.funGetResultSet(sql);
			while (rsWeight.next())
			{
			    BillOut.write("     Weight");
			    BillOut.write("     " + rsWeight.getDouble(2));
			    BillOut.newLine();
			}
			rsWeight.close();
			sql = "select c.strCharName,b.strCharValues "
				+ " from " + billhd + " a," + advBookBillCharDtl + " b,tblcharactersticsmaster c "
				+ " where a.strAdvBookingNo=b.strAdvBookingNo "
				+ " and b.strCharCode=c.strCharCode "
				+ " and a.strBillNo='" + billNo + "' "
				+ " and b.strItemCode='" + rs_BillDtl.getString(5) + "' "
				+ " and a.strposCode='" + posCode + "' "
				+ " and date(a.dteBillDate)='" + billDate + "' "
				+ " and a.strClientCode=b.strClientCode ";
			ResultSet rsCharDtl = objDatabseCon.funGetResultSet(sql);
			while (rsCharDtl.next())
			{
			    String charName = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(1), 12, "Left");
			    BillOut.write("     " + charName);
			    String charVal = objUtility.funPrintTextWithAlignment(rsCharDtl.getString(2), 28, "Left");
			    BillOut.write("     " + charVal);
			    BillOut.newLine();
			}
			rsCharDtl.close();
		    }
		}
	    }
	    rs_BillDtl.close();

	   // objPrintingUtility.funPrintComplimentaryItemsInBill(billNo, BillOut, 4, posCode, billDate, sbZeroAmtItems,billhd,billComplDtl);

	  //  objPrintingUtility.funPrintPromoItemsInBill(billNo, BillOut, 4, billPromoDtl);  // Print Promotion Items in Bill for this billno.

	    BillOut.write(Linefor5);
	    BillOut.newLine();
	    if (objSetupHdModel.getStrPointsOnBillPrint().equalsIgnoreCase("Y"))
	    {
		String sqlCRMPoints = "select b.dblPoints from " + billhd + " a, tblcrmpoints b "
			+ " where a.strBillNo=b.strBillNo "
			+ " and a.strClientCode=b.strClientCode "
			+ " and a.strBillNo='" + billNo + "' "
			+ " and a.strposCode='" + posCode + "' "
			+ " and date(a.dteBillDate)='" + billDate + "' ";
		ResultSet rsCRMPoints = objDatabseCon.funGetResultSet(sqlCRMPoints);
		if (rsCRMPoints.next())
		{
		    objPrintingUtility.funWriteTotal("POINTS ", rsCRMPoints.getString(1), BillOut, "Format5");
		}
		rsCRMPoints.close();
		BillOut.newLine();
	    }
	    
	    String subTotalLabel="SUB TOTAL";
	    if(clientCode.equals("185.001"))
	    {
		String strTotalQty=gDecimalFormat.format(totalQty);
		strTotalQty=String.format("%7s", strTotalQty);
		subTotalLabel=strTotalQty;
	    }
	    
	    if (flgComplimentaryBill)
	    {
		objPrintingUtility.funWriteTotal(subTotalLabel, gDecimalFormat.format(Double.parseDouble("0.00")), BillOut, "Format5");
		BillOut.newLine();
	    }
	    else
	    {
		objPrintingUtility.funWriteTotal(subTotalLabel, gDecimalFormat.format(Double.parseDouble(subTotal)), BillOut, "Format5");
		BillOut.newLine();
	    }
	    String sql = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks "
		    + " from " + billDscFrom + " a ,tblreasonmaster b," + billhd + " c "
		    + " where  a.strDiscReasonCode=b.strReasonCode "
		    + " and a.strBillNo=c.strBillNo "
		    + " and a.strClientCode=c.strClientCode "
		    + " and date(a.dteBillDate)=date(c.dteBillDate) "
		    + " and a.strBillNo='" + billNo + "' "
		    + " and c.strposCode='" + posCode + "' "
		    + " and date(c.dteBillDate)='" + billDate + "' ";
	    ResultSet rsDisc = objDatabseCon.funGetResultSet(sql);
	    boolean flag = true;
	    while (rsDisc.next())
	    {
		if (flag)
		{
		    flag = false;
		    BillOut.write("  DISCOUNT");
		    BillOut.newLine();
		}
		double dbl = Double.parseDouble(rsDisc.getString("dblDiscPer"));
		String dbl2 = gDecimalFormat.format(dbl);
//                String discText = String.format("%.1f", dbl) + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
		String discText = dbl2 + "%" + " On " + rsDisc.getString("strDiscOnValue") + "";
		if (discText.length() > 30)
		{
		    discText = discText.substring(0, 30);
		}
		else
		{
		    discText = String.format("%-30s", discText);
		}
		BillOut.write("  " + discText);
		String discountOnItem = objUtility.funPrintTextWithAlignment(gDecimalFormat.format(rsDisc.getDouble("dblDiscAmt")), 8, "Right");
		BillOut.write(discountOnItem);
		BillOut.newLine();
		BillOut.write("  Reason  : ");
		String discReason = objUtility.funPrintTextWithAlignment(rsDisc.getString(5), 20, "Left");
		BillOut.write(discReason);
		BillOut.newLine();
		BillOut.write("  Remarks : ");
		String discRemarks = objUtility.funPrintTextWithAlignment(rsDisc.getString(6), 20, "Left");
		BillOut.write(discRemarks);
		BillOut.newLine();

	    }
	    String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount) "
		    + " from " + billtaxdtl + " a,tbltaxhd b," + billhd + " c "
		    + " where a.strBillNo='" + billNo + "' "
		    + " and a.strTaxCode=b.strTaxCode "
		    + " and a.strBillNo=c.strBillNo "
		    + " and a.strClientCode=c.strClientCode "
		    + " and date(a.dteBillDate)=date(c.dteBillDate) "
		    + " and c.strposCode='" + posCode + "' "
		    + " and b.strTaxCalculation='Forward' "
		    + " and date(c.dteBillDate)='" + billDate + "' "
		    + " group by a.strTaxCode";
	    ResultSet rsTax = objDatabseCon.funGetResultSet(sql_Tax);
	    while (rsTax.next())
	    {
		if (flgComplimentaryBill)
		{
		    objPrintingUtility.funWriteTotal(rsTax.getString(1), gDecimalFormat.format(Double.parseDouble("0.00")), BillOut, "Format5");
		    BillOut.newLine();
		}
		else
		{
		    objPrintingUtility.funWriteTotal(rsTax.getString(1), gDecimalFormat.format(rsTax.getDouble(2)), BillOut, "Format5");
		    BillOut.newLine();
		}
	    }
	    if (deliveryCharge != null && deliveryCharge.trim().length() > 0 && !"0.00".equalsIgnoreCase(deliveryCharge)&& !"0.0000".equalsIgnoreCase(deliveryCharge))
	    {
		objPrintingUtility.funWriteTotal("DELV./PACKING CHARGE", gDecimalFormat.format(Double.parseDouble(deliveryCharge)), BillOut, "Format5");
		BillOut.newLine();
	    }
	    if (advAmount.trim().length() > 0 && !"0.00".equalsIgnoreCase(advAmount))
	    {
		objPrintingUtility.funWriteTotal("ADVANCE", gDecimalFormat.format(Double.parseDouble(advAmount)), BillOut, "Format5");
		BillOut.newLine();
	    }
	    BillOut.write(Linefor5);
	    BillOut.newLine();

	    if (flgComplimentaryBill)
	    {
		objPrintingUtility.funWriteTotal("TOTAL(ROUNDED)", gDecimalFormat.format(Double.parseDouble("0.00")), BillOut, "Format5");
		if (dblUSDRateConvertion > 0)
		{
		    BillOut.newLine();
		    objPrintingUtility.funWriteTotal("USD", "$ " + gDecimalFormat.format(Double.parseDouble(grandTotal) / dblUSDRateConvertion), BillOut, "Format5");
		}
		BillOut.newLine();
		BillOut.write(Linefor5);
	    }
	    else
	    {
		objPrintingUtility.funWriteTotal("TOTAL(ROUNDED)", gDecimalFormat.format(Double.parseDouble(grandTotal)), BillOut, "Format5");
		if (dblUSDRateConvertion > 0)
		{
		    BillOut.newLine();
		    objPrintingUtility.funWriteTotal("USD", "$ " + gDecimalFormat.format(Double.parseDouble(grandTotal) / dblUSDRateConvertion), BillOut, "Format5");
		}
		BillOut.newLine();
		BillOut.write(Linefor5);
	    }

	    //print Grand total of other bill nos from bill series
	    if (objSetupHdModel.getStrEnableBillSeries().equalsIgnoreCase("Y"))
	    {
		String sqlPrintGT = "select a.strPrintGTOfOtherBills,b.strDtlBillNos,b.dblGrandTotal "
			+ "from tblbillseries a,tblbillseriesbilldtl b "
			+ "where (a.strposCode=b.strposCode or a.strposCode='All') "
			+ "and a.strBillSeries=b.strBillSeries "
			+ "and b.strHdBillNo='" + billNo + "' "
			+ "and b.strposCode='" + posCode + "' "
			+ "and date(b.dteBillDate)='" + billDate + "' ";
		ResultSet rsPrintGTYN = objDatabseCon.funGetResultSet(sqlPrintGT);
		double dblOtherBillsGT = 0.00;
		if (rsPrintGTYN.next())
		{
		    if (rsPrintGTYN.getString(1).equalsIgnoreCase("Y"))
		    {
			String billSeriesDtlBillNos = rsPrintGTYN.getString(2);
			String[] dtlBillSeriesBillNo = billSeriesDtlBillNos.split(",");
			dblOtherBillsGT += rsPrintGTYN.getDouble(3);
			if (dtlBillSeriesBillNo.length > 0)
			{
			    for (int i = 0; i < dtlBillSeriesBillNo.length; i++)
			    {
				sqlPrintGT = "select a.strHdBillNo,a.dblGrandTotal "
					+ "from tblbillseriesbilldtl a "
					+ "where a.strHdBillNo='" + dtlBillSeriesBillNo[i] + "' "
					+ "and a.strposCode='" + posCode + "' "
					+ "and date(a.dteBillDate)='" + billDate + "' ";
				ResultSet rsPrintGT = objDatabseCon.funGetResultSet(sqlPrintGT);
				if (rsPrintGT.next())
				{
				    BillOut.newLine();
				    objPrintingUtility.funWriteTotal(dtlBillSeriesBillNo[i] + " TOTAL(ROUNDED)", rsPrintGT.getString(2), BillOut, "Format5");
				    dblOtherBillsGT += rsPrintGT.getDouble(2);
				    if (dblUSDRateConvertion > 0)
				    {
					BillOut.newLine();
					objPrintingUtility.funWriteTotal(dtlBillSeriesBillNo[i] + " USD", "$ " + gDecimalFormat.format((rsPrintGT.getDouble(2) / dblUSDRateConvertion)), BillOut, "Format5");
				    }
				}
			    }
			    BillOut.newLine();
			    BillOut.write(Linefor5);
			    BillOut.newLine();
			    objPrintingUtility.funWriteTotal("GRAND TOTAL(ROUNDED)", gDecimalFormat.format(dblOtherBillsGT), BillOut, "Format5");
			    if (dblUSDRateConvertion > 0)
			    {
				BillOut.newLine();
				objPrintingUtility.funWriteTotal("USD", "$ " + gDecimalFormat.format((dblOtherBillsGT / dblUSDRateConvertion)), BillOut, "Format5");
			    }
			    BillOut.newLine();
			    BillOut.write(Linefor5);
			    BillOut.newLine();
			}
		    }
		}
	    }

	    //settlement breakup part
	    double dblDiscountOffer=0;
	    String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType "
		    + " from " + billSettlementdtl + " a ,tblsettelmenthd b," + billhd + " c "
		    + " where a.strBillNo=? "
		    + " and a.strBillNo=c.strBillNo "
		    + " and a.strClientCode=c.strClientCode "
		    + " and a.strSettlementCode=b.strSettelmentCode "
		    + " AND date(a.dteBillDate)=date(c.dteBillDate) "
		    + " and c.strposCode=? "
		    + " and date(c.dteBillDate)=? ";
	    pst = con.prepareStatement(sqlSettlementBreakup);
	    pst.setString(1, billNo);
	    pst.setString(2, posCode);
	    pst.setString(3, billDate);
	    boolean flgSettlement = false;
	    boolean creditSettlement = false;
	    ResultSet rsBillSettlement = pst.executeQuery();
	    while (rsBillSettlement.next())
	    {
		if (flgComplimentaryBill)
		{
		    BillOut.newLine();
		    objPrintingUtility.funWriteTotal(rsBillSettlement.getString(2), gDecimalFormat.format(Double.parseDouble("0.00")), BillOut, "Format5");
		}
		else
		{
		    BillOut.newLine();
		    objPrintingUtility.funWriteTotal(rsBillSettlement.getString(2), gDecimalFormat.format(rsBillSettlement.getDouble(1)), BillOut, "Format5");
		}
		flgSettlement = true;
		if (rsBillSettlement.getString(3).equals("Credit"))
		{
		    creditSettlement = true;
		}
		if (rsBillSettlement.getString(3).equals("Gift Voucher"))
		{
		    dblDiscountOffer=Double.parseDouble(gDecimalFormat.format(rsBillSettlement.getDouble(1)));
		}
		
	    }
	    rsBillSettlement.close();

	    if (flgSettlement)
	    {
		BillOut.newLine();
		if (creditSettlement)
		{
//                    objPrintingUtility.funWriteTotal("Credit Remarks ", rs_BillHD.getString(11), BillOut, "Format5");
//                    BillOut.newLine();

		    String textRemarks = rs_BillHD.getString(11).trim();
		    String lblRemarks = "  Credit Remarks :";

		    String remarks = lblRemarks + textRemarks;
		    if (remarks.length() > 40)
		    {
			BillOut.write(remarks.substring(0, 40));
			BillOut.newLine();
			remarks = remarks.substring(40, remarks.length());

			BillOut.write("                  " + remarks);
			BillOut.newLine();
		    }
		    else
		    {
			BillOut.write(remarks);
			BillOut.newLine();
		    }

		    String custName = rs_BillHD.getString(24);
		    if (!custName.isEmpty())
		    {
			objPrintingUtility.funWriteTotal("Customer " + custName, "", BillOut, "Format5");
		    }
		    BillOut.newLine();
		    BillOut.write(Linefor5);
		}
	    }

	    String sqlTenderAmt = "select sum(a.dblPaidAmt),sum(a.dblSettlementAmt),(sum(a.dblPaidAmt)-sum(a.dblSettlementAmt)) RefundAmt "
		    + " from " + billSettlementdtl + " a," + billhd + " b "
		    + " where a.strBillNo=b.strBillNo "
		    + " and a.strClientCode=b.strClientCode "
		    + " AND date(a.dteBillDate)=date(b.dteBillDate) "
		    + " and b.strBillNo='" + billNo + "' "
		    + " and b.strposCode='" + posCode + "' "
		    + " and date(b.dteBillDate)='" + billDate + "' "
		    + " group by a.strBillNo";
	    ResultSet rsTenderAmt = objDatabseCon.funGetResultSet(sqlTenderAmt);
	    if (rsTenderAmt.next() && !creditSettlement)
	    {
		BillOut.newLine();
		if (flgComplimentaryBill)
		{
		    objPrintingUtility.funWriteTotal("PAID AMT", gDecimalFormat.format(Double.parseDouble("0.00")), BillOut, "Format5");
		    BillOut.newLine();
		}
		else
		{
		    objPrintingUtility.funWriteTotal("PAID AMT", gDecimalFormat.format(rsTenderAmt.getDouble(1)-dblDiscountOffer), BillOut, "Format5");
		    BillOut.newLine();
		    if (rsTenderAmt.getDouble(3) > 0)
		    {
			objPrintingUtility.funWriteTotal("REFUND AMT", gDecimalFormat.format(rsTenderAmt.getDouble(3)), BillOut, "Format5");
			BillOut.newLine();
		    }
		}
		BillOut.write(Linefor5);
	    }
	    rsTenderAmt.close();

	    if (rs_BillHD.getDouble(29) > 0)
	    {
		BillOut.newLine();
		objPrintingUtility.funWriteTotal("TIP AMT", rs_BillHD.getString(29), BillOut, "Format5");
		BillOut.newLine();
	    }
	    if (flag_isHomeDelvBill)
	    {
		BillOut.newLine();
		String sql_count = "select count(*) from tblhomedelivery where strCustomerCode=?";
		pst = con.prepareStatement(sql_count);
		pst.setString(1, customerCode);
		ResultSet rs_Count = pst.executeQuery();
		rs_Count.next();
		BillOut.write("  CUSTOMER COUNT : " + rs_Count.getString(1));
		rs_Count.close();
		BillOut.newLine();
		BillOut.write(Linefor5);
	    }
	    BillOut.newLine();

	    /**
	     * print Tax Nos
	     */
	    objPrintingUtility.funPrintServiceVatNo(BillOut, 4, billNo, billDate, billtaxdtl);

	    if (objSetupHdModel.getStrEnableBillSeries().equalsIgnoreCase("Y"))
	    {
		sql = "select b.strPrintInclusiveOfTaxOnBill "
			+ " from tblbillseriesbilldtl a,tblbillseries b "
			+ " where a.strBillSeries=b.strBillSeries "
			+ " and a.strHdBillNo='" + billNo + "' "
			+ " and a.strClientCode=b.strClientCode "
			+ " and date(a.dteBillDate)='" + billDate + "' ";
		ResultSet rsBillSeries = objDatabseCon.funGetResultSet(sql);
		if (rsBillSeries.next())
		{
		    if (rsBillSeries.getString(1).equals("Y"))
		    {
			BillOut.write(dashedLineFor40Chars);
			BillOut.newLine();
			objPrintingUtility.funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut,objSetupHdModel.getIntColumnSize());
			BillOut.write("(INCLUSIVE OF ALL TAXES)");
			BillOut.newLine();
		    }
		}
		rsBillSeries.close();
	    }
	    else
	    {
		if (objSetupHdModel.getStrPrintInclusiveOfAllTaxesOnBill().equalsIgnoreCase("Y"))
		{
		    BillOut.write(dashedLineFor40Chars);
		    BillOut.newLine();
		    objPrintingUtility.funPrintBlankSpace("(INCLUSIVE OF ALL TAXES)", BillOut,objSetupHdModel.getIntColumnSize());
		    BillOut.write("(INCLUSIVE OF ALL TAXES)");
		    BillOut.newLine();
		}
	    }

	    int num = objSetupHdModel.getStrBillFooter().trim().length() / 30;
	    int num1 = objSetupHdModel.getStrBillFooter().trim().length() % 30;
	    int cnt1 = 0;
	    for (int cnt = 0; cnt < num; cnt++)
	    {
		String footer = objSetupHdModel.getStrBillFooter().trim().substring(cnt1, (cnt1 + 30));
		footer = footer.replaceAll("\n", "");
		BillOut.write("     " + footer.trim());
		BillOut.newLine();
		cnt1 += 30;
	    }
	    BillOut.write("     " + objSetupHdModel.getStrBillFooter().trim().substring(cnt1, (cnt1 + num1)).trim());
	    BillOut.newLine();
	    objPrintingUtility.funPrintBlankSpace(user, BillOut,objSetupHdModel.getIntColumnSize());
	    BillOut.write(user);
	    BillOut.newLine();
	    BillOut.newLine();
	    BillOut.newLine();
	    BillOut.newLine();
	    BillOut.newLine();

	   /* if (!clsGlobalVarClass.gOpenCashDrawerAfterBillPrintYN)
	    {
		if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
		{
		    BillOut.write("V");//Linux
		}
		else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
		{
		    if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
		    {
			BillOut.write("V");
		    }
		    else
		    {
			BillOut.write("m");//windows
		    }
		}
	    }
	    */
	    rs_BillHD.close();
	    BillOut.close();
	    fstream_bill.close();
	    pst.close();
	    final String printer=objPrintingUtility.funGetPrinterDetails(posCode);
        final String fileName=filePath + "/Temp/Temp_Bill.txt";
	    new Thread()
		{
			@Override
			public void run()
			{
			    objPrintingUtility.funPrintTextBill(printer,fileName);
			}
		}.start();

	    /*if (formName.equalsIgnoreCase("sales report"))
	    {
		objPrintingUtility.funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
	    }
	    else
	    {
		if (clsGlobalVarClass.gShowBill)
		{
		    objPrintingUtility.funShowTextFile(Text_Bill, formName, clsGlobalVarClass.gBillPrintPrinterPort);
		}
	    }

	    if (!formName.equalsIgnoreCase("sales report"))
	    {
		if (transType.equalsIgnoreCase("void"))
		{
		    if (clsGlobalVarClass.gPrintOnVoidBill)
		    {
			if (!viewORprint.equalsIgnoreCase("view"))
			{
			    objPrintingUtility.funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint,"");
			}
		    }
		}
		else
		{
		    if (!clsGlobalVarClass.flgReprintView)
		    {
			if (!viewORprint.equalsIgnoreCase("view"))
			{
			    objPrintingUtility.funPrintToPrinter(clsGlobalVarClass.gBillPrintPrinterPort, "", "bill", "N", isReprint,"");
			}
		    }
		    else
		    {
			clsGlobalVarClass.flgReprintView = false;
		    }
		}
	    }
*/	    //if (formName.equalsIgnoreCase("sales report"))

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

}
