package com.sanguine.webpos.printing;

import com.sanguine.base.service.intfBaseService;
import com.sanguine.webpos.bean.clsBillItemDtlBean;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;

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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;

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
public class clsKOTJasperFileGenerationForMakeKOT
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
     * @param billingType
     * @param tableNo
     * @param CostCenterCode
     * @param ShowKOT
     * @param AreaCode
     * @param KOTNO
     * @param Reprint
     * @param primaryPrinterName
     * @param secondaryPrinterName
     * @param CostCenterName
     * @param printYN
     * @param NCKotYN
     * @param labelOnKOT
     */
    public void funGenerateJasperForTableWiseKOT(String billingType, String tableNo, String CostCenterCode, String ShowKOT, String AreaCode, String KOTNO, String Reprint, String primaryPrinterName, String secondaryPrinterName, String CostCenterName, String printYN, String NCKotYN, String labelOnKOT,int primaryCopies,int secondaryCopies,HttpServletRequest request)
    {
    	StringBuilder sqlBuilder=new StringBuilder();
	    String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String POSCode = request.getSession().getAttribute("gPOSCode").toString();
		String POSName = request.getSession().getAttribute("gPOSName").toString();

		String POSDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		String userCode = request.getSession().getAttribute("gUserCode").toString();

	HashMap hm = new HashMap();
	List<List<clsBillItemDtlBean>> listData = new ArrayList<>();

	try
	{
		clsSetupHdModel  objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,POSCode);

	    boolean isReprint = false;
	    if("Reprint".equalsIgnoreCase(Reprint))	
	    {	
		isReprint = true;
		hm.put("dublicate", "[DUPLICATE]");
	    
	    }
	    	
	    if ("Y".equalsIgnoreCase(NCKotYN))
	    {
		hm.put("KOTorNC", "NCKOT");
	    }
	    else
	    {
		hm.put("KOTorNC", labelOnKOT);
	    }
	    hm.put("POS", POSName);
	    hm.put("costCenter", CostCenterName);

	    String tableName = "";
	    int pax = 0;
	    String SQL_KOT_Dina_tableName = "select strTableName,intPaxNo "
		    + " from tbltablemaster "
		    + " where strTableNo='"+tableNo+"' and strOperational='Y'";
	    
	  List  list = objBaseService.funGetList(new StringBuilder(SQL_KOT_Dina_tableName), "sql");
	    if (list.size() > 0 && list!=null)
	    {
	    	for(int i=0;i<list.size();i++)
	    	{
	    		Object[] obj=(Object[])list.get(i);
	
		tableName = obj[0].toString();
		pax = Integer.parseInt(obj[1].toString());
	    }
	    }

	    String sqlKOTItems = "";
	    List<clsBillItemDtlBean> listOfKOTDetail = new ArrayList<>();
	    if (objSetupHdModel.getStrAreaWisePricing().equals("Y"))
	    {
		sqlKOTItems = "select LEFT(a.strItemCode,7),b.strItemName,a.dblItemQuantity,a.strKOTNo,a.strSerialNo,d.strShortName "
			+ " from tblitemrtemp a,tblmenuitempricingdtl b,tblprintersetup c,tblitemmaster d "
			+ " where a.strTableNo='"+tableNo+"' and a.strKOTNo='"+KOTNO+"' and b.strCostCenterCode=c.strCostCenterCode "
			+ " and b.strCostCenterCode='"+CostCenterCode+"' and a.strItemCode=d.strItemCode "
			+ " and (b.strPOSCode='"+POSCode+"' or b.strPOSCode='All') "
			+ " and (b.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo='"+tableNo+"' )) "
			+ " and LEFT(a.strItemCode,7)=b.strItemCode and b.strHourlyPricing='No' "
			+ " order by a.strSerialNo ";
	    }
	    else
	    {
		sqlKOTItems = "select LEFT(a.strItemCode,7),b.strItemName,a.dblItemQuantity,a.strKOTNo,a.strSerialNo,d.strShortName "
			+ " from tblitemrtemp a,tblmenuitempricingdtl b,tblprintersetup c,tblitemmaster d "
			+ " where a.strTableNo='"+tableNo+"' and a.strKOTNo='"+KOTNO+"' and b.strCostCenterCode=c.strCostCenterCode "
			+ " and b.strCostCenterCode='"+CostCenterCode+"' and a.strItemCode=d.strItemCode "
			+ " and (b.strPOSCode='"+POSCode+"' or b.strPOSCode='All') "
			+ " and (b.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo='"+tableNo+"' ) "
			+ " OR b.strAreaCode ='" + AreaCode + "') "
			+ " and LEFT(a.strItemCode,7)=b.strItemCode and b.strHourlyPricing='No' "
			+ " order by a.strSerialNo ";
	    }
	 	String KOTType = "DINE";
	    
	 	hm.put("KOTType", KOTType);
	    
	 	hm.put("KOT", KOTNO);
	    hm.put("tableNo", tableName);
	    if (clientCode.equals("124.001"))
	    {
		hm.put("124.001", tableName);
	    }
	  //  hm.put("PAX", String.valueOf(pax));

	    /*String sqlWaiterDtl = "select strWaiterNo from tblitemrtemp where strKOTNo='"+KOTNO+"'  and strTableNo='"+tableNo+"' group by strKOTNo ;";
	    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlWaiterDtl);
	    pst.setString(1, KOTNO);
	    pst.setString(2, tableNo);
	    ResultSet rsWaiterDtl = pst.executeQuery();
	    if (rsWaiterDtl.next())
	    {
		if (!"null".equalsIgnoreCase(rsWaiterDtl.getString(1)) && rsWaiterDtl.getString(1).trim().length() > 0)
		{
		    sqlWaiterDtl = "select strWShortName from tblwaitermaster where strWaiterNo=? ;";
		    pst = clsGlobalVarClass.conPrepareStatement.prepareStatement(sqlWaiterDtl);
		    pst.setString(1, rsWaiterDtl.getString(1));
		    ResultSet rs = pst.executeQuery();
		    rs.next();
		    hm.put("waiterName", rs.getString(1));
		    rs.close();
		}
	    }*/
	    
	    //rsWaiterDtl.close();
	    
	    String sql_KOTDate = "select dteDateCreated from tblitemrtemp where strKOTNo='"+KOTNO+"'  and strTableNo='"+tableNo+"' group by strKOTNo ;";
	    
	    List listKOTDate=objBaseService.funGetList(new StringBuilder(sql_KOTDate), "sql");
	    if(listKOTDate.size()>0 && listKOTDate !=null){
		    hm.put("DATE_TIME", ddMMyyyyAMPMDateFormat.format(listKOTDate.get(0)));

	    
	    }
	    	

	  
	  /*  if (clsGlobalVarClass.gPrintDeviceAndUserDtlOnKOTYN)
	    {
		InetAddress ipAddress = InetAddress.getLocalHost();
		String hostName = ipAddress.getHostName();
		String physicalAddress = objUtility.funGetCurrentMACAddress();
		hm.put("KOT From", hostName);
		hm.put("kotByUser", clsGlobalVarClass.gUserCode);
		hm.put("device_id", physicalAddress);
	    }

*/	    
	    
	    List list_KOT_Items = objBaseService.funGetList(new StringBuilder(sqlKOTItems), "sql");    
        if(list_KOT_Items.size() >0 && list_KOT_Items!=null)
        {
	        for(int i=0;i<list_KOT_Items.size();i++)
	    	{
	        	
	        	Object[] objKOTItems=(Object[])list_KOT_Items.get(i);
		String itemName =objKOTItems[1].toString();
		if (objSetupHdModel.getStrPrintShortNameOnKOT().equalsIgnoreCase("Y") && !objKOTItems[5].toString().trim().isEmpty())
		{
		    itemName = objKOTItems[5].toString();
		}

		clsBillItemDtlBean objBillDtl = new clsBillItemDtlBean();
		objBillDtl.setDblQuantity(Double.parseDouble(objKOTItems[2].toString()));
		objBillDtl.setStrItemName(itemName);
		listOfKOTDetail.add(objBillDtl);
		String sql_Modifier = "select a.strItemName,sum(a.dblItemQuantity) from tblitemrtemp a "
			+ " where a.strItemCode like'" + objKOTItems[0].toString() + "M%' and a.strKOTNo='" + KOTNO + "' "
			//+ " and strSerialNo like'" + rs_KOT_Items.getString(5) + ".%' "
			+ " group by a.strItemCode,a.strItemName ";
		//System.out.println(sql_Modifier);
		List listModifierItems = objBaseService.funGetList(new StringBuilder(sql_Modifier), "sql");
		if(listModifierItems.size()>0 && listModifierItems!=null)
		{
			for(int j=0 ;j<listModifierItems.size();j++)
			{
				Object[] objModifier=(Object[])listModifierItems.get(j);
		    objBillDtl = new clsBillItemDtlBean();
		    String modifierName = objModifier[0].toString();
		    if (modifierName.startsWith("-->"))
		    {
			if ( objSetupHdModel.getStrPrintModifierQtyOnKOT().equalsIgnoreCase("Y"))
			{
			    objBillDtl.setDblQuantity(Double.parseDouble(objModifier[1].toString()));
			    objBillDtl.setStrItemName(objModifier[0].toString());
			}
			else
			{
			    objBillDtl.setDblQuantity(0);
			    objBillDtl.setStrItemName(objModifier[0].toString());
			}
		    }
		    
		    listOfKOTDetail.add(objBillDtl);
		}
	    	}
	    }
	    }

	    for (int cntLines = 0; cntLines < Integer.parseInt(String.valueOf(objSetupHdModel.getStrNoOfLinesInKOTPrint())); cntLines++)
	    {
	    	clsBillItemDtlBean objBillDtl = new clsBillItemDtlBean();
		objBillDtl.setDblQuantity(0);
		objBillDtl.setStrItemName("  ");
		listOfKOTDetail.add(objBillDtl);
	    }

	    hm.put("listOfItemDtl", listOfKOTDetail);
	    listData.add(listOfKOTDetail);
	    String reportName = "/WEB-INF/reports/kotFormat/rptGenrateKOTJasperReport.jrxml";
	    if(clientCode.equalsIgnoreCase("343.001"))
	    {
		reportName = "/WEB-INF/reports/kotFormat/rptGenrateKOTJasperReport3.jrxml";	
	    }
	    JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listData);
	    InputStream is = this.getClass().getClassLoader().getResourceAsStream(reportName);

	    final JasperPrint print = JasperFillManager.fillReport(is, hm, beanColDataSource);
//	    JRViewer viewer = new JRViewer(print);
//	    JFrame jf = new JFrame();
//	    jf.getContentPane().add(viewer);
//	    jf.validate();
//	    if (clsGlobalVarClass.gShowBill)
//	    {
//		jf.setVisible(true);
//		jf.setSize(new Dimension(500, 900));
//		jf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//	    }
        
	    if(printYN.equals("Y")){
		final String primary = primaryPrinterName;
		final String secondary = secondaryPrinterName;
		String printOnBothPrinters = "N";
		new Thread(){
			@Override
			public void run()
			{
				objPrinting.funPrintJasperExporterInThread(print,primary);
			}
		}.start();
		
	/*	if (clsGlobalVarClass.gAreaWiseCostCenterKOTPrinting)
		{
		    String areaCodeOfTable = AreaCode;
           	    String sqlArea = "select strTableName,intPaxNo,strAreaCode "
           		    + " from tbltablemaster "
           		    + " where strTableNo='"+tableNo+"' "
           		    + " and strOperational='Y' ";
           	    ResultSet rsArea = clsGlobalVarClass.dbMysql.executeResultSet(sqlArea);
           	    if (rsArea.next())
           	    {
           	    	areaCodeOfTable = rsArea.getString(3);
           	    }
           	    rsArea.close();
		    String sqlAreaWiseCostCenterKOTPrinting = "select a.strPrimaryPrinterPort,a.strSecondaryPrinterPort,a.strPrintOnBothPrintersYN "
			    + "from tblprintersetupmaster a "
			    + "where (a.strPOSCode='"+clsGlobalVarClass.gPOSCode+"' or a.strPOSCode='All') "
			    + "and a.strAreaCode='"+areaCodeOfTable+"' "
			    + "and a.strCostCenterCode='"+CostCenterCode+"' "
			    + "and a.strPrinterType='Cost Center' ";
		    ResultSet rsPrinter = clsGlobalVarClass.dbMysql.executeResultSet(sqlAreaWiseCostCenterKOTPrinting);
		    if (rsPrinter.next())
		    {
			primary = rsPrinter.getString(1);
			secondary = rsPrinter.getString(2);
			printOnBothPrinters = rsPrinter.getString(3);
		    }
		    rsPrinter.close();
		}
		
		primary = primary.replaceAll("#", "\\\\");
		secondary = secondary.replaceAll("#", "\\\\");
		
		String printerName=primaryPrinterName;
		if(primaryPrinterName.equalsIgnoreCase("Not")){
		    printerName=secondaryPrinterName;
		}
    */       
	/*	objUtility2.funPrintJasperKOT(printerName, print);
		
		if(clsGlobalVarClass.gPrintKotToLocaPrinter){
		    String billPrinterName = clsGlobalVarClass.gBillPrintPrinterPort.replaceAll("#", "\\\\");;
		    objUtility2.funPrintJasperKOT(billPrinterName, print);
		}
	*/	
}
	    
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
