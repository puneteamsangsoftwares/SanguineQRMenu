package com.sanguine.webpos.printing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsDatabaseConnection;
import com.sanguine.webpos.util.clsPOSUtilityController;

/**
 *
 * @author Ajim
 * @date Aug 26, 2017
 */
@Controller

public class clsKOTTextFileGenerationForDirectBiller
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

    private DecimalFormat decimalFormat = new DecimalFormat("#.###");
    private SimpleDateFormat ddMMyyyyAMPMDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
    private final String dashedLineFor40Chars = "  --------------------------------------";

    /**
     *
     * @param costCenterCode
     * @param areaCode
     * @param billNo
     * @param reprint
     * @param primaryPrinterName
     * @param secondaryPrinterName
     * @param costCenterName
     * @param labelOnKOT
     */
    public void funGenerateTextFileForKOTDirectBiller(String costCenterCode, String areaCode, String billNo, String reprint, String primaryPrinterName, String secondaryPrinterName, String costCenterName, String labelOnKOT,HttpServletRequest request)
    {
	try
	{
		
	    PreparedStatement pst = null;
	    String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String posCode = request.getSession().getAttribute("gPOSCode").toString();
		String POSName = request.getSession().getAttribute("gPOSName").toString();

		String POSDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		String userCode = request.getSession().getAttribute("gUserCode").toString();

		clsSetupHdModel   objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
	    final DecimalFormat gDecimalFormat = objUtility.funGetGlobalDecimalFormatter(objSetupHdModel.getDblNoOfDecimalPlace());;
		final String gDecimalFormatString = objUtility.funGetGlobalDecimalFormatString(objSetupHdModel.getDblNoOfDecimalPlace());;
        String formName="Settlement";
        Connection  con=objDatabseCon.funOpenPOSCon("mysql");
	
	    objPrintingUtility.funCreateTempFolder();
	    String filePath = System.getProperty("user.dir");
	    File Text_KOT = new File(filePath + "/Temp/Temp_KOT.txt");
	    FileWriter fstream = new FileWriter(Text_KOT);
	    //BufferedWriter KotOut = new BufferedWriter(fstream);
	    BufferedWriter KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Text_KOT), "UTF8"));
	    boolean isReprint = false;
	    String isWas = "Is";
	    if ("Reprint".equalsIgnoreCase(reprint))
	    {
		isReprint = true;
		objPrintingUtility.funPrintBlankSpace("[DUPLICATE]", KotOut,objSetupHdModel.getIntColumnSize());
		KotOut.write("[DUPLICATE]");
		KotOut.newLine();

		isWas = "Was";
	    }
	    String sql_PrintHomeDelivery = "select strOperationType,intOrderNo,strKOTToBillNote from tblbillhd where strBillNo=? ";
	    //String sql_PrintHomeDelivery = "select strOperationType,intOrderNo from tblbillhd where strBillNo=? ";
	    pst = con.prepareStatement(sql_PrintHomeDelivery);
	    pst.setString(1, billNo);
	    ResultSet rs_PrintHomeDelivery = pst.executeQuery();
	    String operationType = "" , strBillOnNote="" , orderNo="" ;
	    if (rs_PrintHomeDelivery.next())
	    {
		operationType = rs_PrintHomeDelivery.getString(1);
		strBillOnNote = rs_PrintHomeDelivery.getString(3);
		//if (clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Text 19") || clsGlobalVarClass.gBillFormatType.equalsIgnoreCase("Jasper 4"))//for only "MING YANG", Raju Ki Chai("206.001","Gaurika Enterprises Pvt. Ltd.")
		//{
		if(objSetupHdModel.getStrPrintOrderNoOnBillYN().equalsIgnoreCase("Y"))		
		{
		    KotOut.write("         Your order no is : " + rs_PrintHomeDelivery.getString(2));
		    KotOut.newLine();    
		}
		    
		//}
		if (strBillOnNote.trim().length() > 0)
		{
		    if(strBillOnNote.contains("  Manual Token No:  "))
		    {
			KotOut.write(strBillOnNote);
			KotOut.newLine();
		    }

		}
	    }
	    
	    rs_PrintHomeDelivery.close();
	    if (operationType.equalsIgnoreCase("HomeDelivery"))
	    {
		if (objSetupHdModel.getStrPrintHomeDeliveryYN().equalsIgnoreCase("Y"))
		{
		    objPrintingUtility.funPrintBlankSpace("Home Delivery", KotOut,objSetupHdModel.getIntColumnSize());
		    KotOut.write("Home Delivery");
		    KotOut.newLine();
		}

	    }
	    else if (operationType.equalsIgnoreCase("TakeAway"))
	    {
		objPrintingUtility.funPrintBlankSpace("Take Away", KotOut,objSetupHdModel.getIntColumnSize());
		KotOut.write("Take Away");
		KotOut.newLine();
	    }

	    objPrintingUtility.funPrintBlankSpace(labelOnKOT, KotOut,objSetupHdModel.getIntColumnSize());
	    KotOut.write(labelOnKOT);
	    KotOut.newLine();
	    objPrintingUtility.funPrintBlankSpace(POSName, KotOut,objSetupHdModel.getIntColumnSize());
	    KotOut.write(POSName);
	    KotOut.newLine();
	    objPrintingUtility.funPrintBlankSpace(costCenterName, KotOut,objSetupHdModel.getIntColumnSize());
	    KotOut.write(costCenterName);
	    KotOut.newLine();

	    objPrintingUtility.funPrintBlankSpace("DIRECT BILLER", KotOut,objSetupHdModel.getIntColumnSize());
	    KotOut.write("DIRECT BILLER");
	    KotOut.newLine();
	    KotOut.write(dashedLineFor40Chars);
	    KotOut.newLine();
	    KotOut.write("  BILL No: " + billNo);
	    KotOut.newLine();
	   
	 /*   if(clsGlobalVarClass.gWERAOnlineOrderIntegration && clsGlobalVarClass.gStrSelectedOnlineOrderFrom.length()>0 )
	    {
		KotOut.write("  Order From : " +clsGlobalVarClass.gStrSelectedOnlineOrderFrom );
		KotOut.newLine();
	    }
	*/    
	    KotOut.write(dashedLineFor40Chars);
	    
	    String sql_DirectKOT_Date = "select dteBillDate from tblbilldtl where strBillNo=? ";
	    pst = con.prepareStatement(sql_DirectKOT_Date);
	    pst.setString(1, billNo);
	    ResultSet rs_DirectKOT_Date = pst.executeQuery();
	    if (rs_DirectKOT_Date.next())
	    {
		KotOut.newLine();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
		KotOut.write("  DATE & TIME: " + dateTimeFormat.format(rs_DirectKOT_Date.getObject(1)));
	    }
	    rs_DirectKOT_Date.close();

	    KotOut.newLine();
	   /* if (clsGlobalVarClass.gPrintDeviceAndUserDtlOnKOTYN)
	    {
		
		InetAddress ipAddress = InetAddress.getLocalHost();
		String hostName = ipAddress.getHostName();
		KotOut.write("  KOT From  :" + hostName);
		KotOut.newLine();
		KotOut.write("  KOT By User      :" + clsGlobalVarClass.gUserCode);
		KotOut.newLine();
		String physicalAddress = objUtility.funGetCurrentMACAddress();
		KotOut.write("  Device ID        :" + physicalAddress);
		KotOut.newLine();
	    }
	  */
	    KotOut.write(dashedLineFor40Chars);
	    KotOut.newLine();
	    KotOut.write("  QTY        ITEM NAME  ");
	    KotOut.newLine();
	    KotOut.write(dashedLineFor40Chars);
/*
	    String areaCodeForTransaction = clsGlobalVarClass.gAreaCodeForTrans;
	    if (operationType.equalsIgnoreCase("HomeDelivery"))
	    {
		areaCodeForTransaction = clsGlobalVarClass.gHomeDeliveryAreaForDirectBiller;
	    }
	    else if (operationType.equalsIgnoreCase("TakeAway"))
	    {
		areaCodeForTransaction = clsGlobalVarClass.gTakeAwayAreaForDirectBiller;
	    }
	    else
	    {
		areaCodeForTransaction = clsGlobalVarClass.gDineInAreaForDirectBiller;
	    }
*/
	    String sql_DirectKOT_Items = "select a.strItemCode,a.strItemName,a.dblQuantity,d.strShortName,a.strSequenceNo "
		    + "from tblbilldtl a,tblmenuitempricingdtl b,tblprintersetup c,tblitemmaster d "
		    + "where  a.strBillNo=? and  b.strCostCenterCode=c.strCostCenterCode "
		    + "and a.strItemCode=d.strItemCode "
		    + "and b.strCostCenterCode=? "//and (b.strAreaCode=? or b.strAreaCode='" + areaCodeForTransaction + "') "
		    + "and a.strItemCode=b.strItemCode "
		    + "and (b.strPOSCode=? OR b.strPOSCode='All')   "
		    + "group by a.strItemCode,a.strSequenceNo "
		    + "ORDER BY a.strSequenceNo;;";
	    //System.out.println(sql_DirectKOT_Items);

	    pst = con.prepareStatement(sql_DirectKOT_Items);
	    pst.setString(1, billNo);
	    pst.setString(2, costCenterCode);
	   // pst.setString(3, areaCode);
	    pst.setString(3, posCode);
	    ResultSet rs_DirectKOT_Items = pst.executeQuery();
	    while (rs_DirectKOT_Items.next())
	    {
		String kotItemName = rs_DirectKOT_Items.getString(2).toUpperCase();
		if (objSetupHdModel.getStrPrintShortNameOnKOT().equalsIgnoreCase("Y") && !rs_DirectKOT_Items.getString(4).trim().isEmpty())
		{
		    kotItemName = rs_DirectKOT_Items.getString(4).toUpperCase();
		}

		KotOut.newLine();

		String itemQty = String.valueOf(decimalFormat.format(rs_DirectKOT_Items.getDouble(3)));

		KotOut.write("  " + itemQty + "      ");
		if (kotItemName.length() <= 25)
		{
		    KotOut.write(kotItemName);
		}
		else
		{
		    KotOut.write(kotItemName.substring(0, 25));
		    KotOut.newLine();
		    KotOut.write("            " + kotItemName.substring(25, kotItemName.length()));
		}
		//following code called for modifier
		String seqNo=rs_DirectKOT_Items.getString(5);
		if(seqNo.length()>1){
		    seqNo=rs_DirectKOT_Items.getString(5).substring(0, 1);
		}
		String sql_Modifier = " select a.strModifierName,a.dblQuantity,ifnull(b.strDefaultModifier,'N'),a.strDefaultModifierDeselectedYN "
			+ "from tblbillmodifierdtl a "
			+ "left outer join tblitemmodofier b on left(a.strItemCode,7)=if(b.strItemCode='',a.strItemCode,b.strItemCode) "
			+ "and a.strModifierCode=if(a.strModifierCode=null,'',b.strModifierCode) "
			+ "where a.strBillNo=? "
			+ "and left(a.strItemCode,7)=? "
			+ "and left(a.strSequenceNo,1)='" + seqNo + "' ";
		//System.out.println(sql_Modifier);

		pst = con.prepareStatement(sql_Modifier);
		pst.setString(1, billNo);
		pst.setString(2, rs_DirectKOT_Items.getString(1));
		ResultSet rs_Modifier = pst.executeQuery();
		while (rs_Modifier.next())
		{
		    String modiQty = String.valueOf(decimalFormat.format(rs_Modifier.getDouble(2)));

		    if (!objSetupHdModel.getStrPrintModifierQtyOnKOT().equalsIgnoreCase("Y"))//dont't print modifier qty
		    {
			if (rs_Modifier.getString(3).equalsIgnoreCase("Y") && rs_Modifier.getString(4).equalsIgnoreCase("Y"))
			{
			    KotOut.newLine();
			    KotOut.write("        " + "No " + rs_Modifier.getString(1).toUpperCase());
			}
			else if (!rs_Modifier.getString(3).equalsIgnoreCase("Y"))
			{
			    KotOut.newLine();
			    KotOut.write("        " + rs_Modifier.getString(1).toUpperCase());
			}else
			{
			    KotOut.newLine();
			    KotOut.write("        " + rs_Modifier.getString(1).toUpperCase());
			}
		    }
		    else
		    {
			if (rs_Modifier.getString(3).equalsIgnoreCase("Y") && rs_Modifier.getString(4).equalsIgnoreCase("Y"))
			{
			    KotOut.newLine();
			    KotOut.write("  " + modiQty + "      " + "No " + rs_Modifier.getString(1).toUpperCase());
			}
			else if (!rs_Modifier.getString(3).equalsIgnoreCase("Y"))
			{
			    KotOut.newLine();
			    KotOut.write("  " + modiQty + "      " + rs_Modifier.getString(1).toUpperCase());
			}else
			{
			    KotOut.newLine();
			    KotOut.write("  " + modiQty + "      " + rs_Modifier.getString(1).toUpperCase());
			}
		    }
		}
		rs_Modifier.close();
	    }

	    rs_DirectKOT_Items.close();
	    KotOut.newLine();
	    KotOut.write(dashedLineFor40Chars);
	    KotOut.newLine();
	    KotOut.newLine();
	    KotOut.newLine();
	    KotOut.newLine();
	    KotOut.newLine();
	    KotOut.newLine();
	   /* if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		KotOut.write("V");//Linux
	    }
	    else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
	    {
		if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
		{
		    KotOut.write("V");
		}
		else
		{
		    KotOut.write("m");//windows
		}
	    }
	    */// KotOut.write("m");
	    KotOut.close();
	    fstream.close();
	    pst.close();
/*
	    if (clsGlobalVarClass.gShowBill)
	    {
		objPrintingUtility.funShowTextFile(Text_KOT, "PrintKot", "Printer Info!2");
	    }
*/
	    if (objSetupHdModel.getStrItemWiseKOTYN().equalsIgnoreCase("Y"))
	    {
		funGenerateTextFileItemWiseKOTForDirectBiller(costCenterCode, areaCode, billNo, reprint, primaryPrinterName, secondaryPrinterName, costCenterName,con,posCode,objSetupHdModel,POSName);
	    }

	    String sql = "select strPrintOnBothPrinters,intPrimaryPrinterNoOfCopies,intSecondaryPrinterNoOfCopies from tblcostcentermaster where strCostCenterCode='" + costCenterCode + "'";
	    ResultSet rsCostCenter = objDatabseCon.funGetResultSet(sql);
	    if (rsCostCenter.next())
	    {
		   
		//objPrintingUtility.funPrintToPrinter(primaryPrinterName, secondaryPrinterName, "kot", rsCostCenter.getString(1), isReprint,costCenterCode);
		
	    }
	     //For print KOT on local printer use following function 
	  /*  if(clsGlobalVarClass.gPrintKotToLocaPrinter){
		String fileName = "Temp/Temp_KOT.txt";
		String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort.replaceAll("#", "\\\\");;
		objPrintingUtility.funPrintOnSecPrinter(billPrinterName, fileName);
	    }*/
	    rsCostCenter.close();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /*
     * Item wise KOT print for direct biller
     */
    private void funGenerateTextFileItemWiseKOTForDirectBiller(String costCenterCode, String areaCode, String billNo, String reprint, String primaryPrinterName, String secondaryPrinterName, String costCenterName,Connection con,String posCode,clsSetupHdModel objSetupHd,String posName)
    {
	try
	{
	    PreparedStatement pst = null;

	    String sql_DirectKOT_Items = "select a.strItemCode,d.strItemName,a.dblQuantity,strItemWiseKOTYN "
		    + ",left(a.strSequenceNo,1),d.strShortName "
		    + "from tblbilldtl a,tblmenuitempricingdtl b,tblprintersetup c,tblitemmaster d "
		    + "where  a.strBillNo=? and  b.strCostCenterCode=c.strCostCenterCode "
		    + "and a.strItemCode=d.strItemCode "
		    + "and b.strCostCenterCode=? "//and (b.strAreaCode=? or b.strAreaCode='" + clsGlobalVarClass.gAreaCodeForTrans + "') "
		    + "and a.strItemCode=b.strItemCode "
		    + "and (b.strPOSCode=? OR b.strPOSCode='All')   "
		    + "GROUP BY left(a.strSequenceNo,1),a.strItemCode; ";

	    pst = con.prepareStatement(sql_DirectKOT_Items);
	    pst.setString(1, billNo);
	    pst.setString(2, costCenterCode);
	   // pst.setString(3, areaCode);
	    pst.setString(3, posCode);
	    ResultSet rs_DirectKOT_Items = pst.executeQuery();
	    int i = 0;
	    while (rs_DirectKOT_Items.next())
	    {

		String itemName = rs_DirectKOT_Items.getString(2).toUpperCase();
		if (objSetupHd.getStrPrintShortNameOnKOT().equalsIgnoreCase("Y") && !rs_DirectKOT_Items.getString(6).trim().isEmpty())
		{
		    itemName = rs_DirectKOT_Items.getString(6).toUpperCase();
		}

		if (rs_DirectKOT_Items.getString(4).equalsIgnoreCase("Y"))
		{
		    String itemCode = rs_DirectKOT_Items.getString(1);
		    String fileName = "KOTFOR" + rs_DirectKOT_Items.getString(2).toUpperCase() + "" + (++i);
		    BufferedWriter KotOut = funGenerateItemWiseKOTHeaderForDirectBiller(billNo, fileName, reprint, costCenterName,objSetupHd,con,posName);

		    KotOut.newLine();
		    KotOut.write("  " + rs_DirectKOT_Items.getString(3) + "      " + itemName);
		    //following code called for modifier
		    String sql_Modifier = " select b.strModifierName ,b.dblQuantity,a.strDefaultModifier,b.strDefaultModifierDeselectedYN "
			    + " from tblitemmodofier a,tblbillmodifierdtl b "
			    + " where a.strItemCode=left(b.strItemCode,7) and a.strModifierCode=b.strModifierCode "
			    + " and b.strBillNo=? and left(b.strItemCode,7)=? AND left(b.strSequenceNo,1)=? ";
		    pst = con.prepareStatement(sql_Modifier);
		    pst.setString(1, billNo);
		    pst.setString(2, rs_DirectKOT_Items.getString(1));
		    pst.setString(3, rs_DirectKOT_Items.getString(5));
		    ResultSet rs_Modifier = pst.executeQuery();
		    while (rs_Modifier.next())
		    {
			if (rs_Modifier.getString(3).equalsIgnoreCase("Y") && rs_Modifier.getString(4).equalsIgnoreCase("Y"))
			{
			    KotOut.newLine();
			    KotOut.write("  " + rs_Modifier.getString(2) + "      " + "No " + rs_Modifier.getString(1).toUpperCase());
			}
			else if (!rs_Modifier.getString(3).equalsIgnoreCase("Y"))
			{
			    KotOut.newLine();
			    KotOut.write("  " + rs_Modifier.getString(2) + "      " + rs_Modifier.getString(1).toUpperCase());
			}
		    }
		    rs_Modifier.close();

		    //seperate items
		    KotOut.newLine();
		    KotOut.write(dashedLineFor40Chars);
		    KotOut.newLine();
		    KotOut.newLine();
		    KotOut.newLine();
		    KotOut.newLine();
		    KotOut.newLine();
		    KotOut.newLine();
		   /* if ("linux".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
		    {
			KotOut.write("V");//Linux
		    }
		    else if ("windows".equalsIgnoreCase(clsPosConfigFile.gPrintOS))
		    {
			if ("Inbuild".equalsIgnoreCase(clsPosConfigFile.gPrinterType))
			{
			    KotOut.write("V");
			}
			else
			{
			    KotOut.write("m");//windows
			}
		    }
		    */
		    KotOut.close();
		    pst.close();
		   // objPrintingUtility.funPrintToPrinterForItemWise(primaryPrinterName, secondaryPrinterName, "ItemWiseKOT", fileName);
		    //For print KOT on local printer use following function 
		    /*if(clsGlobalVarClass.gPrintKotToLocaPrinter){
			fileName = "Temp/Temp_KOT.txt";
			String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort.replaceAll("#", "\\\\");;
			objPrintingUtility.funPrintOnSecPrinter(billPrinterName, fileName);
		    }*/
		}
	    }
	    rs_DirectKOT_Items.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /*
     * generate Itemwise KOT text file
     */
    private BufferedWriter funGenerateItemWiseKOTHeaderForDirectBiller(String BillNo, String fileName, String Reprint, String CostCenterName,clsSetupHdModel objSetUpHd,Connection con,String posName)
    {
	BufferedWriter KotOut = null;

	try
	{
	    PreparedStatement pst = null;
	    objPrintingUtility.funCreateTempFolder();

	    String filePath = System.getProperty("user.dir");
	    File Text_KOT = new File(filePath + "/Temp/" + fileName + ".txt");
	    KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Text_KOT), "UTF8"));
	    boolean isReprint = false;
	    if ("Reprint".equalsIgnoreCase(Reprint))
	    {
		isReprint = true;
		objPrintingUtility.funPrintBlankSpace("[DUPLICATE]", KotOut,objSetUpHd.getIntColumnSize());
		KotOut.write("[DUPLICATE]");
		KotOut.newLine();
	    }

	    String sql_PrintHomeDelivery = "select strOperationType from tblbillhd where strBillNo=? ";
	    pst = con.prepareStatement(sql_PrintHomeDelivery);
	    pst.setString(1, BillNo);
	    ResultSet rs_PrintHomeDelivery = pst.executeQuery();
	    String operationType = "";
	    if (rs_PrintHomeDelivery.next())
	    {
		operationType = rs_PrintHomeDelivery.getString(1);
	    }
	    rs_PrintHomeDelivery.close();
	    if (operationType.equalsIgnoreCase("HomeDelivery"))
	    {
		if (objSetUpHd.getStrPrintHomeDeliveryYN().equalsIgnoreCase("Y"))
		{
		    objPrintingUtility.funPrintBlankSpace(operationType, KotOut,objSetUpHd.getIntColumnSize());
		    KotOut.write(operationType);
		    KotOut.newLine();
		}
	    }
	    else if (operationType.equalsIgnoreCase("TakeAway"))
	    {
		objPrintingUtility.funPrintBlankSpace(operationType, KotOut,objSetUpHd.getIntColumnSize());
		KotOut.write(operationType);
		KotOut.newLine();
	    }

	    objPrintingUtility.funPrintBlankSpace("KOT", KotOut,objSetUpHd.getIntColumnSize());
	    KotOut.write("KOT");
	    KotOut.newLine();
	    objPrintingUtility.funPrintBlankSpace(posName, KotOut,objSetUpHd.getIntColumnSize());
	    KotOut.write(posName);
	    KotOut.newLine();
	    objPrintingUtility.funPrintBlankSpace(CostCenterName, KotOut,objSetUpHd.getIntColumnSize());
	    KotOut.write(CostCenterName);
	    KotOut.newLine();

	    //item will pickup from tblbilldtl           
	    objPrintingUtility.funPrintBlankSpace("DIRECT BILLER", KotOut,objSetUpHd.getIntColumnSize());
	    KotOut.write("DIRECT BILLER");
	    KotOut.newLine();
	    KotOut.write(dashedLineFor40Chars);
	    KotOut.newLine();
	    KotOut.write("  BILL No: " + BillNo);
	    KotOut.newLine();
	    KotOut.write(dashedLineFor40Chars);

	    String sql_DirectKOT_Date = "select date(dteBillDate), time(dteBillDate) from tblbilldtl where strBillNo=? ";
	    pst = con.prepareStatement(sql_DirectKOT_Date);
	    pst.setString(1, BillNo);
	    ResultSet rs_DirectKOT_Date = pst.executeQuery();
	    if (rs_DirectKOT_Date.next())
	    {
		KotOut.newLine();
		KotOut.write("  DATE & TIME: " + rs_DirectKOT_Date.getString(1) + " " + rs_DirectKOT_Date.getString(2));
	    }
	    rs_DirectKOT_Date.close();
	    KotOut.newLine();
	    KotOut.write(dashedLineFor40Chars);
	    KotOut.newLine();
	    KotOut.write("  QTY        ITEM NAME  ");
	    KotOut.newLine();
	    KotOut.write(dashedLineFor40Chars);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    return KotOut;
	}
    }

}
