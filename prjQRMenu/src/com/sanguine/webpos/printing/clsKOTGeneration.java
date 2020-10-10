package com.sanguine.webpos.printing;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sanguine.base.service.intfBaseService;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
/**
 *
 * @author Ajim
 * @date Aug 11, 2017
 */

@Controller
public class clsKOTGeneration
{

	@Autowired
	intfBaseService objBaseService;

	@Autowired
	clsPOSMasterService objMasterService;

	@Autowired
	clsKOTJasperFileGenerationForDirectBiller objKOTJasperFileGenerationForDirectBiller;
	@Autowired
	clsKOTJasperFileGenerationForMakeKOT   objKOTJasperFileGenerationForMakeKOT;
    /**
     *
     */
	@RequestMapping(value="/getKotPrint",method=RequestMethod.GET)
    public HttpServletResponse funKOTGeneration(String tableNo,String KOTNo,String billNo, String reprint,String type,String printYN, HttpServletRequest request,HttpServletResponse response)
   {
	   
	HttpServletResponse kotResponse=null;

	try
	{

	  	String sql = "";
	    PreparedStatement pst = null;
	    String ncKOTYN = "N";
	    String pricingTable = "tblmenuitempricingdtl";
	    StringBuilder sqlBuilder=new StringBuilder();
	    String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String POSCode = request.getSession().getAttribute("gPOSCode").toString();
		String POSDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		String userCode = request.getSession().getAttribute("gUserCode").toString();
		clsSetupHdModel  objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,POSCode);
		List list = new ArrayList();
		String areaCodeForAll = "";
	    sqlBuilder.setLength(0);
	    sqlBuilder.append("select strAreaCode from tblareamaster where strAreaName='All';") ;
	     list = objBaseService.funGetList(sqlBuilder, "sql");

		if (list.size() > 0 && list != null)
		{
			areaCodeForAll = (String) list.get(0);

		}

	    switch (type)
	    {
		case "Dina":

			     StringBuilder sbSql =new StringBuilder("select a.strItemName,a.strNCKotYN,d.strCostCenterCode,d.strPrimaryPrinterPort,d.strSecondaryPrinterPort,d.strCostCenterName "
				    + " ,ifnull(e.strLabelOnKOT,'KOT') strLabelOnKOT,sum(a.dblPrintQty),e.intPrimaryPrinterNoOfCopies,e.intSecondaryPrinterNoOfCopies,e.strPrintOnBothPrinters "
				    + " from tblitemrtemp a "
				    + " left outer join " + pricingTable + " c on a.strItemCode = c.strItemCode "
				    + " left outer join tblprintersetup d on c.strCostCenterCode=d.strCostCenterCode "
				    + " left outer join tblcostcentermaster e on c.strCostCenterCode=e.strCostCenterCode  "
				    + " where a.strKOTNo='"+KOTNo+"' and a.strTableNo='"+tableNo+"' and (c.strPosCode='"+POSCode+"' or c.strPosCode='All') "
				    + " and (c.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo='"+tableNo+"' ) ");
				    /*if(clsGlobalVarClass.gAreaWisePricing.equalsIgnoreCase("N")){
					sbSql.append(" OR c.strAreaCode ='"+areaCodeForAll+"' ");
				    }*/
				   sbSql.append(") group by d.strCostCenterCode ");
				   
			   /* if (clsGlobalVarClass.gFireCommunication)
			    {
				sbSql.append( " having sum(a.dblPrintQty)>0 ");
			    }
			    */
			    list= new ArrayList<>();
			     list = objBaseService.funGetList(sbSql, "sql");
			    if (list.size() > 0 && list!=null)
			    {
			    	for(int i=0;i<list.size();i++)
			    	{
			    		Object[] obj=(Object[])list.get(i);
			
				ncKOTYN = obj[1].toString();//NC KOT YN
				// funGenerateTextFileForKOT("Dina", tableNo, rsPrint.getString(3), "", areaCodeForAll, KOTNo, reprint, rsPrint.getString(4), rsPrint.getString(5), rsPrint.getString(6), printYN, rsPrint.getString(2));
				if (objSetupHdModel.getStrPrintType().equals("Jasper"))
				{
				    int noOfCopiesPrimaryPrinter= Integer.parseInt(obj[8].toString());//rsPrint.getInt(9);
				    int noOfCopiesSecPrinter=Integer.parseInt(obj[9].toString());//rsPrint.getInt(10);
				    if (clientCode.equalsIgnoreCase("239.001"))//urbo
				    {
					/*//objJasperFormat2FileGenerationForMakeKOT = new clsKOTJasperFormat2FileGenerationForMakeKOT();
					objJasperFormat2FileGenerationForMakeKOT.funGenerateJasperForTableWiseKOT("Dina", tableNo, obj[2].toString(), "", areaCodeForAll, KOTNo, reprint,obj[3].toString(), obj[4].toString(), obj[5].toString(), printYN, obj[1].toString(),obj[6].toString(),1,0);
					if(Integer.parseInt(obj[9].toString())>1)
					{    
					objJasperFormat2FileGenerationForMakeKOT.funGenerateJasperForTableWiseKOT("Dina", tableNo, rsPrint.getString(3), "", areaCodeForAll, KOTNo, reprint, rsPrint.getString(4), rsPrint.getString(5), rsPrint.getString(6), printYN, rsPrint.getString(2), rsPrint.getString(7),0,1);
					}
					if(Integer.parseInt(rsPrint.getString(9))>1)
					{
					    for(int i=0;i<Integer.parseInt(rsPrint.getString(9))-1;i++)
					    {  
						objJasperFormat2FileGenerationForMakeKOT.funGenerateJasperForTableWiseKOT("Dina", tableNo, rsPrint.getString(3), "", areaCodeForAll, KOTNo, "Reprint", rsPrint.getString(4), rsPrint.getString(5), rsPrint.getString(6), printYN, rsPrint.getString(2), rsPrint.getString(7),rsPrint.getInt(9)-1,0);
					    }
					}
					if(Integer.parseInt(rsPrint.getString(10))>1)
					{
					    for(int i=0;i<Integer.parseInt(rsPrint.getString(10))-1;i++)
					    {  
						objJasperFormat2FileGenerationForMakeKOT.funGenerateJasperForTableWiseKOT("Dina", tableNo, rsPrint.getString(3), "", areaCodeForAll, KOTNo, "Reprint", rsPrint.getString(4), rsPrint.getString(5), rsPrint.getString(6), printYN, rsPrint.getString(2), rsPrint.getString(7),rsPrint.getInt(10)-1,0);
					    }
					}

				    
*/				   
				    }
				    else
				    {
					if(obj[10].toString().equalsIgnoreCase("Y")){
					    if(noOfCopiesSecPrinter>0){
						//secondary copies with reprint 
						for(int j=0;j<Integer.parseInt(obj[9].toString());j++)
						{
						    objKOTJasperFileGenerationForMakeKOT.funGenerateJasperForTableWiseKOT("Dina", tableNo, obj[2].toString(), "", areaCodeForAll, KOTNo, "Reprint",obj[4].toString(), obj[4].toString(), obj[5].toString(), printYN, obj[1].toString(), obj[6].toString(),0,Integer.parseInt(obj[9].toString())-1,request);  
						}
					    }
					}
					objKOTJasperFileGenerationForMakeKOT.funGenerateJasperForTableWiseKOT("Dina", tableNo,  obj[2].toString(), "", areaCodeForAll, KOTNo, reprint,obj[3].toString(), obj[4].toString(), obj[5].toString(), printYN,  obj[1].toString(),obj[6].toString(),1,1,request);
					if(Integer.parseInt(obj[8].toString())>1)
					{
					    for(int k=0;k<Integer.parseInt(obj[8].toString())-1;k++)
					    {
						objKOTJasperFileGenerationForMakeKOT.funGenerateJasperForTableWiseKOT("Dina", tableNo, obj[2].toString(), "", areaCodeForAll, KOTNo, "Reprint",obj[3].toString(), obj[4].toString(),obj[5].toString(), printYN,  obj[1].toString(),obj[6].toString(),Integer.parseInt(obj[8].toString())-1,0,request);  
					    }
					}    
					   
				    }
				}
			
				//Kot format
				/*	else if (clsGlobalVarClass.gClientCode.equalsIgnoreCase("171.001") && clsGlobalVarClass.gPrintType.equals("Text File"))//china grill-pimpri menu head wise items kot format
				{
				    objKOTTextFileGenerationForMakeKOT.funGenerateTextKOTForMakeKOTForMenuHeadWise(tableNo, rsPrint.getString(3), areaCodeForAll, KOTNo, reprint, rsPrint.getString(4), rsPrint.getString(5), rsPrint.getString(6), printYN, rsPrint.getString(2), rsPrint.getString(7));
				}
				else //default kot format
				{
				    objKOTTextFileGenerationForMakeKOT.funGenerateTextFileForTableWiseKOT(tableNo, rsPrint.getString(3), areaCodeForAll, KOTNo, reprint, rsPrint.getString(4), rsPrint.getString(5), rsPrint.getString(6), printYN, rsPrint.getString(2), rsPrint.getString(7));
				}
			*/    	
			    	
			    	}
			    }
			    
			    
			 
			    
			    /*
			    if (clsGlobalVarClass.gConsolidatedKOTPrinterPort.length() > 0)//print consolidated KOT only if it set
			    {
				if (printYN.equalsIgnoreCase("Y") && ncKOTYN.equalsIgnoreCase("N"))//print consolidated KOT only if it is not NC KOT
				{
				    if(clsGlobalVarClass.gPrintType.equals("Jasper"))
					objConsolidatedKOTJasperGenerationForMakeKOT.funConsolidatedKOTForMakeKOTJasperFileGeneration(tableNo, KOTNo);
				    else	
				    objConsolidatedKOTTextFileGenerationForMakeKOT.funConsolidatedKOTForMakeKOTTextFileGeneration(tableNo, KOTNo);
				}
			    }*/
			    break;
			    
		      case "DirectBiller":
			    sqlBuilder.setLength(0);

			    sqlBuilder.append("select a.strItemName,c.strCostCenterCode,c.strPrimaryPrinterPort "
                            + ",c.strSecondaryPrinterPort,c.strCostCenterName,d.strLabelOnKOT,d.intPrimaryPrinterNoOfCopies,d.intSecondaryPrinterNoOfCopies  "
                            + " from tblbilldtl  a,tblmenuitempricingdtl b,tblprintersetup c,tblcostcentermaster d,tblbillhd e"
                            + " where a.strBillNo='"+billNo+"' "
                            + " and  a.strItemCode=b.strItemCode "
                            + " and b.strCostCenterCode=c.strCostCenterCode "
                            + " and b.strCostCenterCode=d.strCostCenterCode "
                            + " and (b.strPosCode='"+POSCode+"' or b.strPosCode='All') "
                            + " AND a.strBillNo=e.strBillNo "
                            + " group by c.strCostCenterCode;");

//7,8
		    list= new ArrayList<>();
		     list = objBaseService.funGetList(sqlBuilder, "sql");
		    if (list.size() > 0 && list!=null)
		    {
		    	for(int i=0;i<list.size();i++)
		    	{
		    		Object[] obj=(Object[])list.get(i);
		
			//funGenerateTextFileForKOTDirectBiller(rsPrintDirect.getString(2), "", clsGlobalVarClass.gDirectAreaCode, billNo, reprint, rsPrintDirect.getString(3), rsPrintDirect.getString(4), rsPrintDirect.getString(5));
			if (objSetupHdModel.getStrPrintType().equals("Jasper"))
			{
				kotResponse=objKOTJasperFileGenerationForDirectBiller.funGenerateJasperForKOTDirectBiller(obj[1].toString(), "", objSetupHdModel.getStrDirectAreaCode(), billNo, reprint, obj[2].toString(),obj[3].toString(), obj[4].toString(), obj[5].toString(),1, 1,request,areaCodeForAll,response);
			    if(Integer.parseInt(obj[6].toString())>1)
			    {
				for(int j=0;j<Integer.parseInt(obj[6].toString())-1;j++)
				    {
					kotResponse=  objKOTJasperFileGenerationForDirectBiller.funGenerateJasperForKOTDirectBiller(obj[1].toString(), "", objSetupHdModel.getStrDirectAreaCode(), billNo, reprint, obj[2].toString(),obj[3].toString(), obj[4].toString(), obj[5].toString(),Integer.parseInt(obj[6].toString())-1, 0,request,areaCodeForAll,response);

				    }
			    
			    }
			    if(Integer.parseInt(obj[7].toString())>1)
				{
				    for(int k=0;k<Integer.parseInt(obj[7].toString())-1;k++)
				    {
				    	kotResponse=  objKOTJasperFileGenerationForDirectBiller.funGenerateJasperForKOTDirectBiller(obj[1].toString(), "", objSetupHdModel.getStrDirectAreaCode(), billNo, reprint, obj[2].toString(),obj[3].toString(), obj[4].toString(), obj[5].toString(),0, Integer.parseInt(obj[7].toString())-1,request,areaCodeForAll,response);

					
				    }
				}
			    
			}
		    	}
		    }
/*			else if (clsGlobalVarClass.gClientCode.equalsIgnoreCase("171.001") && clsGlobalVarClass.gPrintType.equals("Text File"))//menu head wise items kot format
			{
			    objKOTTextFileGenerationForDirectBiller.funGenerateTextFileForKOTDirectBiller(rsPrintDirect.getString(2), clsGlobalVarClass.gDineInAreaForDirectBiller, billNo, reprint, rsPrintDirect.getString(3), rsPrintDirect.getString(4), rsPrintDirect.getString(5), rsPrintDirect.getString(6));
			    
			    	
			}
			else //if(clsGlobalVarClass.gPrintType.equals("Text File"))//default kot format
			{
			    objKOTTextFileGenerationForDirectBiller.funGenerateTextFileForKOTDirectBiller(rsPrintDirect.getString(2), clsGlobalVarClass.gDineInAreaForDirectBiller, billNo, reprint, rsPrintDirect.getString(3), rsPrintDirect.getString(4), rsPrintDirect.getString(5), rsPrintDirect.getString(6));
			    
			    
			}
		    }
		    }
		    rsPrintDirect.close();
		    pst.close();

		    
		    if (clsGlobalVarClass.gConsolidatedKOTPrinterPort.length() > 0 && printYN.equalsIgnoreCase("Y"))//print consolidated KOT 
		    {
			if(clsGlobalVarClass.gPrintType.equals("Jasper"))
			    objConsolidatedKOTJasperGenerationForDirectBiller.funConsolidatedKOTForDirectBillerJasperFileGeneration(billNo);
			else    
			objConsolidatedKOTTextFileGenerationForDirectBiller.funConsolidatedKOTForDirectBillerTextFileGeneration(billNo);
		    }*/
//		    if (clsGlobalVarClass.gConsolidatedKOTPrinterPort.length() > 0 && printYN.equalsIgnoreCase("Y"))//print consolidated KOT 
//		    {
//			objConsolidatedKOTTextFileGenerationForDirectBiller.funConsolidatedKOTForDirectBillerTextFileGeneration(billNo);
//		    }

		    break;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return kotResponse;
    }

    /**
     *
     * @param TableNo
     * @param WaiterName
     * @param printYN
     */
/*    public void funCkeckKotTextFile(String TableNo, String WaiterName, String printYN, String KOTFrom)
    {
	clsCheckKOT objCheckKOT = new clsCheckKOT();
	objCheckKOT.funCkeckKotTextFile(TableNo, WaiterName, printYN, KOTFrom);
    }

    *//**
     *
     * @param TableNo
     * @param WaiterName
     * @param printYN
     *//*
    public void funCkeckKotForJasper(String TableNo, String WaiterName, String printYN)
    {
	clsCheckKOT objCheckKOT = new clsCheckKOT();
	objCheckKOT.funCkeckKotForJasper(TableNo, WaiterName, printYN);
    }

    *//**
     * Kitchen note
     *//*
    public void funPrintKOTMessage(String costCenterCode, String costCenterName, String kitchenNote)
    {
	if (clsGlobalVarClass.gPrintType.equals("Jasper"))
	{
	    clsKitchenNote objKitchenNote = new clsKitchenNote();
	    objKitchenNote.funPrintKOTJasperMessage(costCenterCode, costCenterName, kitchenNote);
	}
	else
	{
	    clsKitchenNote objKitchenNote = new clsKitchenNote();
	    objKitchenNote.funPrintKOTTextMessage(costCenterCode, costCenterName, kitchenNote);
	}
    }
*/
}
