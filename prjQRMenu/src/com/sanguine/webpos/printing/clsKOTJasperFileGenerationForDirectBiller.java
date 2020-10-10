package com.sanguine.webpos.printing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Dimension;
import java.io.InputStream;
import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.webpos.bean.clsBillItemDtlBean;
import com.sanguine.webpos.model.clsBillDtlModel;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

@Controller
public class clsKOTJasperFileGenerationForDirectBiller
{


	@Autowired
	intfBaseService objBaseService;

	@Autowired
	clsPOSMasterService objMasterService;
	
	@Autowired
	private ServletContext servletContext;

    @Autowired
    clsPrintingUtility objPrinting;

    private DecimalFormat decimalFormat = new DecimalFormat("#.###");
    private SimpleDateFormat ddMMyyyyAMPMDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
    
    /**
     *
     * @param CostCenterCode
     * @param ShowKOT
     * @param AreaCode
     * @param BillNo
     * @param Reprint
     * @param primaryPrinterName
     * @param secondaryPrinterName
     * @param CostCenterName
     * @param labelOnKOT
     */
    public HttpServletResponse funGenerateJasperForKOTDirectBiller(String CostCenterCode, String ShowKOT, String AreaCode, String BillNo, String Reprint, String primaryPrinterName, String secondaryPrinterName, String CostCenterName, String labelOnKOT,int primaryCopies,int secondaryCopies, HttpServletRequest request,String areaCodeForAll,HttpServletResponse response)
    {
        HashMap hm = new HashMap();
        List<List<clsBillItemDtlBean>> listData = new ArrayList<>();
        try
        {
        	StringBuilder sqlBuilder=new StringBuilder();
    	    String clientCode = request.getSession().getAttribute("gClientCode").toString();
    		String POSCode = request.getSession().getAttribute("gPOSCode").toString();
    		String POSName = request.getSession().getAttribute("gPOSName").toString();

    		String POSDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
    		String userCode = request.getSession().getAttribute("gUserCode").toString();
    		clsSetupHdModel  objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,POSCode);
    		List list = new ArrayList();
    		boolean isReprint = false;
		    String kotToBillNote="";
		    String kotNote="";
		    String delInstruction="";
	        if ("Reprint".equalsIgnoreCase(Reprint))
            {
                isReprint = true;
                hm.put("dublicate", "[DUPLICATE]");
            }
	    
            else
            {
                hm.put("dublicate", "");
            }
	    
            String operationType = "";

	        sqlBuilder.setLength(0);
	        sqlBuilder.append("select strOperationType,intOrderNo,strKOTToBillNote from tblbillhd where strBillNo='"+BillNo+"' ");
	        list=new ArrayList<>();
            list = objBaseService.funGetList(sqlBuilder, "sql");

 			if (list.size() > 0 && list != null)
 			{
 				Object[] objOpKot=(Object[])list.get(0);
 				operationType = objOpKot[0].toString();
 				kotToBillNote=  objOpKot[2].toString();
 				hm.put("orderNo", "Your order no is " +objOpKot[1].toString());

 			}

		/*    list= new ArrayList<>();
		     list = objBaseService.funGetList(sqlBuilder, "sql");
		    if (list.size() > 0 && list!=null)
		    {
		    	for(int i=0;i<list.size();i++)
		    	{
		    		Object[] obj=(Object[])list.get(i);
*/
         hm.put("Type", "");
	    
	    if(kotToBillNote.contains("OrderFrom"))
	    {
		if(kotToBillNote.contains("#")) 
		{
		    delInstruction=kotToBillNote.split("#")[1];
		}
		hm.put("delInstruction",delInstruction);
		//print online order 
		if(kotToBillNote.length()>9){
		    kotNote=kotToBillNote.substring(9, kotToBillNote.indexOf(" "));
		}
		if (operationType.equalsIgnoreCase("HomeDelivery"))
		{
		    if (objSetupHdModel.getStrPrintHomeDeliveryYN().equalsIgnoreCase("Y"))
		    {
			hm.put("Type", "Home Delivery ["+kotNote+"]");
		    }
		}
		else if (operationType.equalsIgnoreCase("TakeAway"))
		{
		    hm.put("Type", "Take Away ["+kotNote+"]");
		}
	    }
	    else{
		if (operationType.equalsIgnoreCase("HomeDelivery"))
		{
		    if (objSetupHdModel.getStrPrintHomeDeliveryYN().equalsIgnoreCase("Y"))
		    {
			hm.put("Type", "Home Delivery");
		    }
		}
		else if (operationType.equalsIgnoreCase("TakeAway"))
		{
		    hm.put("Type", "Take Away");
		}
	    }
	  /*  if(clsGlobalVarClass.gWERAOnlineOrderIntegration)//for online order 
	    {
		 if(clsGlobalVarClass.gStrSelectedOnlineOrderFrom.equalsIgnoreCase("ZOMATO"))
	         {
		     CostCenterName="ZOMATO";
	         }
		 else if(clsGlobalVarClass.gStrSelectedOnlineOrderFrom.equalsIgnoreCase("SWIGGY"))
		 {
		     CostCenterName="SWIGGY";
		 }
	    }*/

            
            hm.put("KOT", labelOnKOT);
            hm.put("POS",POSName);
            hm.put("CostCenter", CostCenterName);
            hm.put("DIRECT BILLER", "DIRECT BILLER");
            hm.put("BILL No", BillNo);

            //hm.put("kotByUser", clsGlobalVarClass.gUserCode);
         
	    
		    		
	   /* if (clsGlobalVarClass.gPrintDeviceAndUserDtlOnKOTYN)
	    {
		InetAddress ipAddress = InetAddress.getLocalHost();
		String hostName = ipAddress.getHostName();
		hm.put("KOT From", hostName);
		hm.put("kotByUser", clsGlobalVarClass.gUserCode);
		String physicalAddress = objUtility.funGetCurrentMACAddress();
		hm.put("device_id", physicalAddress);
	    }
*/
		    sqlBuilder.setLength(0);
		    sqlBuilder.append("select dteBillDate from tblbilldtl where strBillNo='"+BillNo+"' ");
            list=new ArrayList<>();
            list = objBaseService.funGetList(sqlBuilder, "sql");

 			if (list.size() > 0 && list != null)
 			{
 				SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
                hm.put("DATE & TIME", dateTimeFormat.format(list.get(0)));
            
 			}

	    
	    
	    String areaCodeForTransaction=areaCodeForAll;
	    if(operationType.equalsIgnoreCase("HomeDelivery"))
	    {
		areaCodeForTransaction=objSetupHdModel.getStrHomeDeliveryAreaForDirectBiller();
	    }
	    else if (operationType.equalsIgnoreCase("TakeAway"))
	    {
		areaCodeForTransaction=objSetupHdModel.getStrTakeAwayAreaForDirectBiller();
	    }
	    else
	    {
		areaCodeForTransaction=objSetupHdModel.getStrDirectAreaCode();
	    }
	    
        List<clsBillItemDtlBean> listOfKOTDetail = new ArrayList<>();

	    sqlBuilder.setLength(0);
             sqlBuilder.append( "select a.strItemCode,a.strItemName,sum(a.dblQuantity),d.strShortName,a.strSequenceNo "
                    + "from tblbilldtl a,tblmenuitempricingdtl b,tblprintersetup c,tblitemmaster d "
                    + "where  a.strBillNo='"+BillNo+"' and  b.strCostCenterCode=c.strCostCenterCode "
                    + "and a.strItemCode=d.strItemCode "
                    + "and b.strCostCenterCode='"+CostCenterCode+"' and (b.strAreaCode='"+AreaCode+"' or b.strAreaCode='" + areaCodeForTransaction + "') "
                    + "and a.strItemCode=b.strItemCode"
		            + " And b.strHourlyPricing='No' "
		            + " and (b.strPOSCode='"+POSCode+"' OR b.strPOSCode='All')   "
                    + "group by a.strItemCode,a.strSequenceNo "
                    + "ORDER BY a.strSequenceNo;;");
            list= new ArrayList<>();
		     list = objBaseService.funGetList(sqlBuilder, "sql");
		    if (list.size() > 0 && list!=null)
		    {
		    	for(int j=0;j<list.size();j++)
		    	{
		    		Object[] objItem=(Object[])list.get(j);
                    String itemName = objItem[1].toString();
                if (objSetupHdModel.getStrPrintShortNameOnKOT().equalsIgnoreCase("Y")    &&   !objItem[3].toString().trim().isEmpty())
                {
                    itemName = objItem[3].toString();
                }

                clsBillItemDtlBean objBillDtl = new clsBillItemDtlBean();
                objBillDtl.setDblQuantity(Double.parseDouble(objItem[2].toString()));
                objBillDtl.setStrItemName(itemName);
                listOfKOTDetail.add(objBillDtl);
		String seqNo=objItem[4].toString();
		if(seqNo.length()>1){
		    seqNo=objItem[4].toString().substring(0, 1);
		}
		sqlBuilder.setLength(0);
		
		sqlBuilder.append(" select a.strModifierName,a.dblQuantity,ifnull(b.strDefaultModifier,'N'),a.strDefaultModifierDeselectedYN "
                        + "from tblbillmodifierdtl a "
                        + "left outer join tblitemmodofier b on left(a.strItemCode,7)=if(b.strItemCode='',a.strItemCode,b.strItemCode) "
                        + "and a.strModifierCode=if(a.strModifierCode=null,'',b.strModifierCode) "
                        + "where a.strBillNo='"+BillNo+"' "
			+ "and left(a.strItemCode,7)='"+objItem[0]+"' ");
			//+ " and left(a.strSequenceNo,1)='" + seqNo + "' ");
                //System.out.println(sql_Modifier);
   		    List listModifier = objBaseService.funGetList(sqlBuilder, "sql");
   		    if (listModifier.size() > 0 && listModifier!=null)
   		    {
   		    	for(int k=0;k<list.size();k++)
   		    	{
   		    		Object[] objItemMod=(Object[])listModifier.get(k);
   		    		clsBillItemDtlBean objBillDtl1 = new clsBillItemDtlBean();
                    if (!objSetupHdModel.getStrPrintModifierQtyOnKOT().equalsIgnoreCase("Y"))//dont't print modifier qty
                    {
                        if (objItemMod[2].toString().equalsIgnoreCase("Y") && objItemMod[3].toString().equalsIgnoreCase("Y"))
                        {
                            objBillDtl1.setDblQuantity(0);
                            objBillDtl1.setStrItemName("        " + "No " + objItemMod[0].toString());
                        }
                        else if (!objItemMod[2].toString().equalsIgnoreCase("Y"))
                        {
                            objBillDtl1.setDblQuantity(0);
                            objBillDtl1.setStrItemName(objItemMod[0].toString());
                        }else{
			             objBillDtl1.setDblQuantity(0);
                            objBillDtl1.setStrItemName(objItemMod[0].toString());
			            }
 			           listOfKOTDetail.add(objBillDtl1);

                    }
                    else
                    {
                        if (objItemMod[2].toString().equalsIgnoreCase("Y") &&objItemMod[3].toString().equalsIgnoreCase("Y"))
                        {
                            objBillDtl1.setDblQuantity(Double.parseDouble(objItemMod[1].toString()));
                            objBillDtl1.setStrItemName("  " + objItemMod[1].toString() + "      " + "No " + objItemMod[0].toString());
                        }
                        else if (!objItemMod[2].toString().equalsIgnoreCase("Y"))
                        {
                            objBillDtl1.setDblQuantity(Double.parseDouble(objItemMod[1].toString()));
                            objBillDtl1.setStrItemName("  " + objItemMod[1].toString() + "      " + objItemMod[0].toString());
                        }
						else
						{
						    objBillDtl1.setDblQuantity(Double.parseDouble(objItemMod[1].toString()));
						    objBillDtl1.setStrItemName("  " + objItemMod[1].toString() + "      " + objItemMod[0].toString());
						}
						
 			           listOfKOTDetail.add(objBillDtl1);

                   }

                }
            }
            }
		    }
            hm.put("listOfItemDtl", listOfKOTDetail);
            listData.add(listOfKOTDetail);
            String reportName = "/WEB-INF/reports/kotFormat/rptGenrateKOTJasperReportForDirectBiller.jrxml";
	    if(clientCode.equalsIgnoreCase("302.001")) //For Moshig cuppa change KOT Format
	    {
	       reportName="/WEB-INF/reports/kotFormat/rptGenrateKOTJasperReportForDirectBiller2.jrxml";
	    
	    }
	    
		JasperDesign jd = JRXmlLoader.load(servletContext.getResourceAsStream(reportName));
		JasperReport jr = JasperCompileManager.compileReport(jd);
		final JasperPrint print = JasperFillManager.fillReport(jr, hm, new JRBeanCollectionDataSource(listData));
		String printerName=primaryPrinterName;
        if(primaryPrinterName.equalsIgnoreCase("Not")){
        	printerName=secondaryPrinterName;
        }
        
		final String printer=printerName;
		new Thread(){
			@Override
			public void run()
			{
				objPrinting.funPrintJasperExporterInThread(print,printer);
			}
		}.start();
		
		/*	String filePath = System.getProperty("user.dir") + "/downloads/pdf/";
		ServletOutputStream servletOutputStream = response.getOutputStream();
		JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,response.getOutputStream()); // your output goes here
		
		//exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, mainJaperPrint);
		exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
		exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
		exporter.exportReport();
		servletOutputStream.flush();
		servletOutputStream.close();
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=billprint.pdf" );
*/
/*            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listData);
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(reportName);
            JasperPrint print = JasperFillManager.fillReport(is, hm, beanColDataSource);
//            JRViewer viewer = new JRViewer(print);
//            JFrame jf = new JFrame();
//            jf.getContentPane().add(viewer);
//            jf.validate();
//            if (clsGlobalVarClass.gShowBill)
//            {
//                jf.setVisible(true);
//                jf.setSize(new Dimension(500, 900));
//                jf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//            }
              objPrintingUtility.funShowJasperFile("PrintKot", print);

//            JRPrintServiceExporter exporter = new JRPrintServiceExporter();
//            //--- Set print properties
//            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//            printRequestAttributeSet.add(MediaSizeName.ISO_A4);
//            if (clsGlobalVarClass.gMultipleKOTPrint)
//            {
//                printRequestAttributeSet.add(new Copies(2));
//            }
//
//            //----------------------------------------------------     
//            //printRequestAttributeSet.add(new Destination(new java.net.URI("file:d:/output/report.ps")));
//            //----------------------------------------------------     
//            PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
//
//            String billPrinterName = primaryPrinterName;
//
//            billPrinterName = billPrinterName.replaceAll("#", "\\\\");
//            printServiceAttributeSet.add(new PrinterName(billPrinterName, null));
//
//            //--- Set print parameters      
//            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
//            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
//            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
//            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
//
//            //--- Print the document
//            try
//            {
//                exporter.exportReport();
//            }
//            catch (JRException e)
//            {
//                e.printStackTrace();
//            }
            //--- Print the document
            clsUtility2 objUtility2 = new clsUtility2();

            ResultSet rsPrinter = clsGlobalVarClass.dbMysql.executeResultSet("select a.strPrinterPort,a.strSecondaryPrinterPort,a.strPrintOnBothPrinters from tblcostcentermaster  a where a.strCostCenterCode='" + CostCenterCode + "' ");
            if (rsPrinter.next())
            {
                String primary = rsPrinter.getString(1);
                String secondary = rsPrinter.getString(2);
                String printOnBothPrinters = rsPrinter.getString(3);
		
                if(clsGlobalVarClass.gWERAOnlineOrderIntegration)
		{
		    if(clsGlobalVarClass.gStrSelectedOnlineOrderFrom.equalsIgnoreCase("SWIGGY")||clsGlobalVarClass.gStrSelectedOnlineOrderFrom.equalsIgnoreCase("ZOMATO")) 
		    {
		       primary=clsGlobalVarClass.gStrOnlinePrinterName;
		       secondary=clsGlobalVarClass.gStrOnlinePrinterName;
		    }
		}		
		

                //primary = primary.replaceAll("#", "\\\\");
                //printServiceAttributeSet.add(new PrinterName(primary, null));
                primary = primary.replaceAll("#", "\\\\");
                secondary = secondary.replaceAll("#", "\\\\");

                if (printOnBothPrinters.equalsIgnoreCase("Y"))
                {
                    if (clsGlobalVarClass.gMultipleKOTPrint)
                    {
                       for(int i=0;i<primaryCopies;i++)
			{   
			    objUtility2.funPrintJasperKOT(primary, print);
			}
			
			for(int i=0;i<secondaryCopies;i++)
			{   
			    objUtility2.funPrintJasperKOT(secondary, print);
			}
			
                    }                   
                }
                else
                {
		    if (clsGlobalVarClass.gMultipleKOTPrint)
		    {
			for(int i=0;i<primaryCopies;i++)
			{	
			    if (!objUtility2.funPrintJasperKOT(primary, print))
			    {
				objUtility2.funPrintJasperKOT(secondary, print);
			    }
			}
		     }

                }
		if(clsGlobalVarClass.gPrintKotToLocaPrinter){
		    String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort.replaceAll("#", "\\\\");;
		    objUtility2.funPrintJasperKOT(billPrinterName, print);
		}
            }
*/
	    
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }
    

    
}
