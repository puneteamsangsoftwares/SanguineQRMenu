/*package com.sanguine.webpos.printing;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;

import org.springframework.beans.factory.annotation.Autowired;

import com.sanguine.base.service.clsBaseServiceImpl;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.webpos.bean.clsPOSBillDtl;
import com.sanguine.webpos.controller.clsPOSBillingAPIController;
import com.sanguine.webpos.controller.clsPOSGlobalFunctionsController;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsPOSSetupUtility;
import com.sanguine.webpos.util.clsPOSTextFileGenerator;
import com.sanguine.webpos.util.clsPOSUtilityController;

public class clsKOTJasperFormat2FileGenerationForMakeKOT
{
	@Autowired
	intfBaseService objBaseService;

	@Autowired
	private clsPOSGlobalFunctionsController objPOSGlobal;

	@Autowired
	private clsGlobalFunctions objGlobal;

	@Autowired
	private clsPOSUtilityController objUtility;

	@Autowired
	private clsPOSSetupUtility objPOSSetupUtility;

	@Autowired
	clsPOSTextFileGenerator objTextFileGeneration;

	@Autowired
	private clsGlobalFunctions objGlobalFunctions;

	@Autowired
	clsBaseServiceImpl objBaseServiceImpl;

	@Autowired
	private clsPOSBillingAPIController objBillingAPI;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	clsPOSMasterService objMasterService;

	public void funGenerateJasperForTableWiseKOT(String billingType, String tableNo, String CostCenterCode, String ShowKOT, String AreaCode, String KOTNO, String Reprint, String primaryPrinterName, String secondaryPrinterName, String CostCenterName, String printYN, String NCKotYN, String labelOnKOT, int primaryCopies, int secondaryCopies)
	{
		HashMap hm = new HashMap();
		StringBuilder SQL_KOT_Dina_tableName = new StringBuilder();
		StringBuilder sqlWaiterDtl = new StringBuilder();
		StringBuilder sql_KOTDate=new StringBuilder();
		StringBuilder	sql_Modifier=new StringBuilder();
		String clientCode = request.getSession().getAttribute("gClientCode").toString();

		String posCode = request.getSession().getAttribute("loginPOS").toString();
		boolean isReprint = false;

		if ("Reprint".equalsIgnoreCase(Reprint))
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
		hm.put("POS", clsGlobalVarClass.gPOSName);
		hm.put("costCenter", CostCenterName);

		String tableName = "";
		String areaCodeOfTable = "";
		int pax = 0;

		SQL_KOT_Dina_tableName.append("select strTableName,intPaxNo,strAreaCode " + " from tbltablemaster " + " where strTableNo='" + tableNo + "' and strOperational='Y'");

		List list = objBaseService.funGetList(SQL_KOT_Dina_tableName, "sql");
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				Object[] obj = (Object[]) list.get(i);

				tableName = obj[0].toString();
				pax = Integer.parseInt(obj[1].toString());
				areaCodeOfTable = obj[2].toString();
			}
		}

		StringBuilder sqlKOTItems = new StringBuilder();
		List<clsPOSBillDtl> listOfKOTDetail = new ArrayList<>();
		String gAreaWisePricing = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gAreaWisePricing");

		if (gAreaWisePricing.equalsIgnoreCase("Y"))
		{
			sqlKOTItems.append("select LEFT(a.strItemCode,7),b.strItemName,a.dblItemQuantity,a.strKOTNo,a.strSerialNo,d.strShortName " + " from tblitemrtemp a,tblmenuitempricingdtl b,tblprintersetup c,tblitemmaster d " + " where a.strTableNo='" + tableNo + "' and a.strKOTNo='" + KOTNO + "' and b.strCostCenterCode=c.strCostCenterCode " + " and b.strCostCenterCode=? and a.strItemCode=d.strItemCode " + " and (b.strPOSCode=? or b.strPOSCode='All') " + " and (b.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo='" + tableNo + "' )) " + " and LEFT(a.strItemCode,7)=b.strItemCode and b.strHourlyPricing='No' " + " order by a.strSerialNo ");
		}
		else
		{
			sqlKOTItems.append("select LEFT(a.strItemCode,7),b.strItemName,a.dblItemQuantity,a.strKOTNo,a.strSerialNo,d.strShortName " + " from tblitemrtemp a,tblmenuitempricingdtl b,tblprintersetup c,tblitemmaster d " + " where a.strTableNo='" + tableNo + "' and a.strKOTNo='" + KOTNO + "' and b.strCostCenterCode=c.strCostCenterCode " + " and b.strCostCenterCode='" + costCenterName + "' and a.strItemCode=d.strItemCode " + " and (b.strPOSCode=? or b.strPOSCode='All') " + " and (b.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo='" + tableNo + "') " + " OR b.strAreaCode ='" + AreaCode + "') " + " and LEFT(a.strItemCode,7)=b.strItemCode and b.strHourlyPricing='No' " + " order by a.strSerialNo ");
		}

		String KOTType = "DINE";
		if (null != clsGlobalVarClass.hmTakeAway.get(tableNo))
		{
			KOTType = "Take Away";
		}
		hm.put("KOTType", KOTType);

		String gCounterWise = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gCounterWise");

		if (gCounterWise.equalsIgnoreCase("Yes"))
		{
			String gCounterName = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gCounterName");

			hm.put("CounterName", gCounterName);
		}
		hm.put("KOT", KOTNO);
		hm.put("tableNo", tableName);
		if (clientCode.equals("124.001"))
		{
			hm.put("124.001", tableName);
		}
		hm.put("PAX", String.valueOf(pax));
		sqlWaiterDtl.append("select strWaiterNo from tblitemrtemp where strKOTNo='" + KOTNO + "'  and strTableNo='" + tableNo + "' group by strKOTNo ;");
		List listWaiter = objBaseServiceImpl.funGetList(sqlWaiterDtl, "sql");
		if (listWaiter.size() > 0)
		{
			for (int i = 0; i < listWaiter.size(); i++)
			{
				Object[] obj = (Object[]) listWaiter.get(i);
				if (!"null".equalsIgnoreCase(obj[0].toString()) && obj[0].toString().trim().length() > 0)
				{
				    sqlWaiterDtl.append("select strWShortName from tblwaitermaster where strWaiterNo='"+obj[0]+"' ;");
				    for (int j = 0; j < listWaiter.size(); j++)
					{
				    	
						Object[] obj1 = (Object[]) listWaiter.get(i);

					    hm.put("waiterName", obj1[0]);
	
					}
			}
		}
		
	}
		sql_KOTDate.append("select dteDateCreated from tblitemrtemp where strKOTNo='"+KOTNO+"'  and strTableNo='"+tableNo+"' group by strKOTNo ;");
		List listDate = objBaseServiceImpl.funGetList(sql_KOTDate, "sql");
		if (listDate.size() > 0)
		{
			for (int i = 0; i < listDate.size(); i++)
			{
				Object[] obj = (Object[]) listDate.get(i);
			    hm.put("DATE_TIME", ddMMyyyyAMPMDateFormat.format(obj[0].toString()));

			}
		}
		 InetAddress ipAddress = InetAddress.getLocalHost();
		    String hostName = ipAddress.getHostName();
			String gPrintDeviceAndUserDtlOnKOTYN = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gPrintDeviceAndUserDtlOnKOTYN");
		    
		    if (gPrintDeviceAndUserDtlOnKOTYN.equalsIgnoreCase("Y"))
		    {
			hm.put("KOT From", hostName);
			hm.put("kotByUser", clsGlobalVarClass.gUserCode);
		    }
			List listKOTItem = objBaseServiceImpl.funGetList(sqlKOTItems, "sql");
			if (listKOTItem.size() > 0)
			{
				for (int i = 0; i < listKOTItem.size(); i++)
				{
					Object[] objKOTItem = (Object[]) listKOTItem.get(i);
				String itemName =	objKOTItem[1].toString();
				String gPrintShortNameOnKOT = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gPrintShortNameOnKOT");
		
				if (gPrintShortNameOnKOT && !objKOTItem[5].trim().isEmpty())
				{
				    itemName = objKOTItem[5].toString();
				}
				clsPOSBillDtl objBillDtl = new clsPOSBillDtl();
				objBillDtl.setDblQuantity(Double.parseDouble(rs_KOT_Items.getString(3)));
				objBillDtl.setStrItemName(itemName);
				listOfKOTDetail.add(objBillDtl);
				 sql_Modifier.append("select a.strItemName,sum(a.dblItemQuantity) from tblitemrtemp a "
					+ " where a.strItemCode like'" + objKOTItem[0] + "M%' and a.strKOTNo='" + KOTNO + "' "
					+ " and strSerialNo like'" + objKOTItem[4] + ".%' "
					+ " group by a.strItemCode,a.strItemName ");
					List listModifier = objBaseServiceImpl.funGetList(sql_Modifier, "sql");
					if (listModifier.size() > 0)
					{
						for (int j = 0; j < listModifier.size(); j++)
						{
							Object[] objModItem = (Object[]) listModifier.get(j);
							objBillDtl = new clsPOSBillDtl();
						    String modifierName = objModItem[0].toString();
						    if (modifierName.startsWith("-->"))
						    {
						    	
								String gPrintModQtyOnKOT = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gPrintModQtyOnKOT");

							if (gPrintModQtyOnKOT.equalsIgnoreCase("Y"))
							{
							    objBillDtl.setDblQuantity(Double.parseDouble(objModItem[1].toString()));
							    objBillDtl.setStrItemName(objModItem[0].toString());
							}
							else
							{
							    objBillDtl.setDblQuantity(0);
							    objBillDtl.setStrItemName(objModItem[0].toString());
							}
						    }
						    listOfKOTDetail.add(objBillDtl);

				
				}
			}
    
				}
				String gNoOfLinesInKOTPrint = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gNoOfLinesInKOTPrint");

				  for (int cntLines = 0; cntLines < Integer.parseInt(gNoOfLinesInKOTPrint); cntLines++)
				    {
					clsPOSBillDtl objBillDtl = new clsPOSBillDtl();
					objBillDtl.setDblQuantity(0);
					objBillDtl.setStrItemName("");
					listOfKOTDetail.add(objBillDtl);
				    }
					List<List<clsPOSBillDtl>> listData = new ArrayList<>();
	  
				  hm.put("listOfItemDtl", listOfKOTDetail);
				    listData.add(listOfKOTDetail);
				    String reportName = "com/POSGlobal/reports/rptGenrateKOTJasperFormat2.jasper";
				    JasperDesign jd = JRXmlLoader.load(reportName);
					JasperReport jr = JasperCompileManager.compileReport(jd);
					List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
					JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listData);
					JasperPrint print = JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);
					String filePath = System.getProperty("user.dir") + "/DayEndMailReports/";
					String extension = ".pdf";
					if (!strDocType.equals("PDF"))
					{
						strDocType = "EXCEL";
						extension = ".xls";
					}
					if (jprintlist.size() > 0)
					{
						ServletOutputStream servletOutputStream = resp.getOutputStream();
						if (strDocType.equals("PDF"))
						{
							JRExporter exporter = new JRPdfExporter();
							resp.setContentType("application/pdf");
							exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
							exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
							exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
							resp.setHeader("Content-Disposition", "inline;filename=GenrateKOTJasperReport_" + webStockUserCode + ".pdf");

							exporter.exportReport();
							JRViewer viewer = new JRViewer(print);
							viewer.setVisible(true);
							servletOutputStream.flush();
							servletOutputStream.close();
						}
						else
						{
							JRExporter exporter = new JRXlsExporter();
							resp.setContentType("application/xlsx");
							exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jprintlist);
							exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletOutputStream);
							exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
							resp.setHeader("Content-Disposition", "inline;filename=GenrateKOTJasperReport_" + webStockUserCode + ".xls");
							exporter.exportReport();
							JRViewer viewer = new JRViewer(print);
							viewer.setVisible(true);
							servletOutputStream.flush();
							servletOutputStream.close();
						}
					}
					else
					{
						resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
						resp.getWriter().append("No Record Found");

					}

}
*/