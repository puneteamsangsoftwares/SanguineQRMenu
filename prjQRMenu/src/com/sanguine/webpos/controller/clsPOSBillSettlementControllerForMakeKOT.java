package com.sanguine.webpos.controller;

import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.base.service.clsBaseServiceImpl;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.webpos.bean.clsPOSBillDtl;
import com.sanguine.webpos.bean.clsPOSBillSeriesBillDtl;
import com.sanguine.webpos.bean.clsPOSBillSettlementBean;
import com.sanguine.webpos.bean.clsPOSItemDtlForTax;
import com.sanguine.webpos.bean.clsPOSItemsDtlsInBill;
import com.sanguine.webpos.bean.clsPOSKOTItemDtl;
import com.sanguine.webpos.bean.clsPOSPromotionItems;
import com.sanguine.webpos.bean.clsPOSTaxCalculationBean;
import com.sanguine.webpos.model.clsMakeKOTHdModel;
import com.sanguine.webpos.model.clsMakeKOTModel_ID;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.printing.clsKOTGeneration;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsPOSSetupUtility;
import com.sanguine.webpos.util.clsPOSTextFileGenerator;
import com.sanguine.webpos.util.clsPOSUtilityController;

@Controller
public class clsPOSBillSettlementControllerForMakeKOT
{

	@Autowired
	intfBaseService objBaseService;


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
	@Autowired
	clsKOTGeneration objGenerationKOTPrint;
	/**
	 * global attributes
	 */
//	String gGetWebserviceURL = (String) clsPOSGlobalFunctionsController.hmPOSSetupValues.get("GetWebserviceURL");
//	String gOutletUID = (String) clsPOSGlobalFunctionsController.hmPOSSetupValues.get("OutletUID");;

//	double taxAmt = 0.00;
//	String globalTableNo = "",
//			strCounterCode = "",
//			gAreaCodeForTrans = "",
//			clsAreaCode = "",
//			gInrestoPOSIntegrationYN = "";
	//ArrayList<String> ListTDHOnModifierItem = new ArrayList<>();
	//ArrayList<Double> ListTDHOnModifierItemMaxQTY = new ArrayList<>();
	// String clientCode="",posCode="",posDate="",userCode="",posClientCode="";
//String clientCode="";
//	private Map<String, clsPOSPromotionItems> hmPromoItem = new HashMap<String, clsPOSPromotionItems>();
//	JSONArray listReasonCode,
	//		listReasonName;


	@RequestMapping(value = "/funLoadTablesForMakeKOT", method = RequestMethod.GET)
	public @ResponseBody JSONArray funLoadTableDtl(@RequestParam("clientCode") String clientCode, @RequestParam("posCode") String posCode,@RequestParam("areaCode") String areaCode)
	{
		List list = null;
		StringBuilder sql=new StringBuilder();
		Map<String, Integer> hmTableSeq = new HashMap<String, Integer>();
		JSONArray jArrData = new JSONArray();
		try
		{
			
			//String gCMSIntegrationY = "N";
			String gCMSIntegrationYN = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gCMSIntegrationYN");
//			if (clsPOSGlobalFunctionsController.hmPOSSetupValues.get("CMSIntegrationYN") != null)
//			{
//				gCMSIntegrationY = clsPOSGlobalFunctionsController.hmPOSSetupValues.get("CMSIntegrationYN").toString();
//			}
			String gTreatMemberAsTable = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gTreatMemberAsTable");
//			if (clsPOSGlobalFunctionsController.hmPOSSetupValues.get("TreatMemberAsTable") != null)
//			{
//				gTreatMemberAsTable = clsPOSGlobalFunctionsController.hmPOSSetupValues.get("TreatMemberAsTable").toString();
//			}

			if (gCMSIntegrationYN.equalsIgnoreCase("Y"))
			{

				if (gTreatMemberAsTable.equalsIgnoreCase("Y"))
				{
					sql.append("select strTableNo,strTableName,intSequence,strStatus,intPaxNo from tbltablemaster " + " where (strPOSCode='" + posCode + "' or strPOSCode='All') " + " and strOperational='Y' and strStatus!='Normal' " + " and strClientCode='"+clientCode+"' order by strTableName");
				}
				else
				{
					sql.append("select strTableNo,strTableName,intSequence,strStatus,intPaxNo from tbltablemaster " + " where (strPOSCode='" + posCode + "' or strPOSCode='All') " + " and strOperational='Y' " + " and strClientCode='"+clientCode+"' order by intSequence");
				}
			}
			else
			{
				if(areaCode.isEmpty() || areaCode.equalsIgnoreCase("All")){
					sql.append("select strTableNo,strTableName,intSequence,strStatus,intPaxNo from tbltablemaster " + " where  " + "  strOperational='Y' " + " and strClientCode='"+clientCode+"' order by intSequence");	
				}else{
					sql.append("select strTableNo,strTableName,intSequence,strStatus,intPaxNo from tbltablemaster " + " where (strPOSCode='" + posCode + "' or strPOSCode='All') " + " and strOperational='Y' " + " and strClientCode='"+clientCode+"' and strAreaCode='"+areaCode+"' order by intSequence");
				}
				
			}

			list = objBaseService.funGetList(sql, "sql");

			jArrData = new JSONArray();
			if (list.size() > 0)
			{
				JSONObject jobj = new JSONObject();
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					hmTableSeq.put(obj[0].toString() + "!" + obj[1].toString(), (int) obj[2]);
					jobj = new JSONObject();
					
					jobj.put("strTableName", obj[1].toString());
					jobj.put("strTableNo", obj[0].toString());
					jobj.put("strStatus", obj[3].toString());
					jobj.put("intPaxNo", obj[4].toString());
					jArrData.add(jobj);
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			return jArrData;
		}
	}

	public JSONArray funGetButttonList(String transName, String posCode, String posClientCode)
	{
		List list = null;
		JSONArray jArrData = new JSONArray();
		try
		{

			String sql = "select strButtonName from tblbuttonsequence where strTransactionName='" + transName + "' and (strPOSCode='All' or strPOSCode='" + posCode + "') and strClientCode='" + posClientCode + "' " + "  order by intSeqNo ";

			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list != null)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object obj = (Object) list.get(i);
					jArrData.add(obj.toString());
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return jArrData;
		}
	}

	public JSONArray funGetWaiterList(String posCode,String clientCode)
	{
		List list = null;
		JSONArray jArrData = new JSONArray();
		try
		{

			String sql = "select strWaiterNo,strWShortName,strWFullName " + " from tblwaitermaster where strOperational='Y' and (strPOSCode='All' or strPOSCode='" + posCode + "') and strClientCode='"+clientCode+"' ";

			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list != null)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					JSONObject objSettle = new JSONObject();
					objSettle.put("strWaiterNo", obj[0].toString());
					objSettle.put("strWShortName", obj[1].toString());
					objSettle.put("strWFullName", obj[2].toString());
					jArrData.add(objSettle);
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return jArrData;
		}
	}

	public JSONArray funGetMenuHeads(String strPOSCode, String userCode,String clientCode)
	{
		LinkedHashMap<String, ArrayList<JSONObject>> mapBillHd;
		mapBillHd = new LinkedHashMap<String, ArrayList<JSONObject>>();
		JSONArray jArr = new JSONArray();
		List list = null;
		String strCounterWiseBilling = "";
		try
		{
			StringBuilder sql = new StringBuilder("select strCounterWiseBilling from tblposmaster where strPOSCode='"+strPOSCode+"' and strClientCode='"+clientCode+"'   ");

			list = objBaseService.funGetList(sql, "sql");
			if (list.size() > 0)
				strCounterWiseBilling = (String) list.get(0);

			sql = new StringBuilder("select strCounterCode from tblcounterhd " + " where strUserCode='" + userCode + "' and strPOSCode='"+strPOSCode+"' and strClientCode='"+clientCode+"' ");
			list = objBaseService.funGetList(sql, "sql");
			String strCounterCode="";
			if (list.size() > 0)
				strCounterCode = (String) list.get(0);

			if (strCounterWiseBilling.equalsIgnoreCase("Yes"))
			{

				sql = new StringBuilder("select distinct(a.strMenuCode),b.strMenuName " + "from tblmenuitempricingdtl a left outer join tblmenuhd b on a.strMenuCode=b.strMenuCode and a.strClientCode=b.strClientCode " + "left outer join tblcounterdtl c on b.strMenuCode=c.strMenuCode  and a.strClientCode=b.strClientCode " + "left outer join tblcounterhd d on c.strCounterCode=d.strCounterCode " + "where d.strOperational='Yes' " + "and (a.strPosCode='" + strPOSCode + "' or a.strPosCode='ALL') and a.strClientCode='"+clientCode+"' " + "and c.strCounterCode='" + strCounterCode + "' " + "order by b.intSequence ");
			}
			else
			{
				sql = new StringBuilder("select distinct(a.strMenuCode),b.strMenuName " + "from tblmenuitempricingdtl a left outer join tblmenuhd b " + "on a.strMenuCode=b.strMenuCode " + "where  b.strOperational='Y' " + "and (a.strPosCode='" + strPOSCode + "' or a.strPosCode='ALL') and a.strClientCode='"+clientCode+"' " + "order by b.intSequence");
			}

			list = objBaseService.funGetList(sql, "sql");

			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					JSONObject objSettle = new JSONObject();
					String strMenuName = obj[1].toString();// .replace(" ", "&#x00A;");
					objSettle.put("strMenuCode", obj[0].toString());
					objSettle.put("strMenuName", strMenuName);
					jArr.add(objSettle);
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return jArr;
	}

	public JSONArray funGetPopularItem(String clientCode, String posDate, String strPOSCode,String gDirectAreaCode)
	{

		JSONArray jArr = new JSONArray();
		List list = null;
		try
		{
			String sql = "select strAreaCode from tblareamaster where strAreaName like '%All%' and strClientCode='"+clientCode+"' ";

			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			String gAreaCodeForTrans="";
			if (list.size() > 0)
				gAreaCodeForTrans = (String) list.get(0);
			//String gDirectAreaCode = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, strPOSCode, "gDirectAreaCode");

			sql = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday," + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, " + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo," + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable " + " FROM tblmenuitempricingdtl a ,tblitemmaster b " + " where a.strPopular='Y' and  a.strItemCode= b.strItemCode " + " and date(a.dteFromDate)<='" + posDate + "' and date(a.dteToDate)>='" + posDate + "' " + " and (a.strPosCode='" + strPOSCode + "' or a.strPosCode='All') " + " and (a.strAreaCode='" + gDirectAreaCode + "' or a.strAreaCode='" + gAreaCodeForTrans + "') and a.strClientCode='"+clientCode+"' and a.strClientCode=b.strClientCode ";

			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					JSONObject objSettle = new JSONObject();
					String strItemName = obj[1].toString();// .replace(" ", "&#x00A;");
					objSettle.put("strItemCode", obj[0].toString());
					objSettle.put("strItemName", strItemName);
					objSettle.put("strTextColor", obj[2].toString());
					objSettle.put("strPriceMonday", obj[3].toString());
					objSettle.put("strPriceTuesday", obj[4].toString());
					objSettle.put("strPriceWednesday", obj[5]);

					objSettle.put("strPriceThursday", obj[6].toString());
					objSettle.put("strPriceFriday", obj[7].toString());
					objSettle.put("strPriceSaturday", obj[8].toString());
					objSettle.put("strPriceSunday", obj[9].toString());
					objSettle.put("tmeTimeFrom", obj[10].toString());
					objSettle.put("strAMPMFrom", obj[11].toString());
					objSettle.put("tmeTimeTo", obj[12].toString());
					objSettle.put("strAMPMTo", obj[13].toString());
					objSettle.put("strCostCenterCode", obj[14].toString());
					objSettle.put("strHourlyPricing", obj[15].toString());
					objSettle.put("strSubMenuHeadCode", obj[16].toString());
					objSettle.put("dteFromDate", obj[17].toString());
					objSettle.put("dteToDate", obj[18].toString());
					objSettle.put("strStockInEnable", obj[19].toString());

					jArr.add(objSettle);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return jArr;
	}
/*
	public JSONArray funGetItemPricingDtl(String clientCode, String posDate, String strPOSCode)
	{
		LinkedHashMap<String, ArrayList<JSONObject>> mapBillHd;
		mapBillHd = new LinkedHashMap<String, ArrayList<JSONObject>>();
		JSONArray jArr = new JSONArray();
		List list = null;
		String sql_ItemDtl;
		try
		{
			String gAreaWisePricing = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, strPOSCode, "gAreaWisePricing");
			if (gAreaWisePricing.equalsIgnoreCase("N"))
			{
				sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday," + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, " + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo," + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable ,a.strMenuCode ,b.strSubGroupCode,c.strGroupCode ,c.strSubGroupName,d.strGroupName " + " FROM tblmenuitempricingdtl a ,tblitemmaster b left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode " + " left outer join  tblgrouphd d  on c.strGroupCode= d.strGroupCode  " + " WHERE  a.strItemCode=b.strItemCode " + " and a.strAreaCode='" + gAreaCodeForTrans + "' " + " and (a.strPosCode='" + strPOSCode + "' or a.strPosCode='All')  and a.strClientCode='"+clientCode+"' " + " and date(dteFromDate)<='" + posDate + "' and date(dteToDate)>='" + posDate + "' " + " ORDER BY b.strItemName ASC";
			}
			else
			{
				sql_ItemDtl = "SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday," + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, " + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo," + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate,b.strStockInEnable ,a.strMenuCode,b.strSubGroupCode,c.strGroupCode,c.strSubGroupName,d.strGroupName   " + " FROM tblmenuitempricingdtl a ,tblitemmaster b left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode " + " left outer join  tblgrouphd d  on c.strGroupCode= d.strGroupCode  " + " WHERE a.strAreaCode='" + clsAreaCode + "' " + "  and a.strItemCode=b.strItemCode "
				// + "WHERE (a.strAreaCode='" + clsAreaCode + "') "
						+ " and (a.strPosCode='" + strPOSCode + "' or a.strPosCode='All')  and a.strClientCode='"+clientCode+"' " + " and date(a.dteFromDate)<='" + posDate + "' and date(a.dteToDate)>='" + posDate + "' " + " ORDER BY b.strItemName ASC";
			}
			list = objBaseService.funGetList(new StringBuilder(sql_ItemDtl), "sql");
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					String itemName = obj[1].toString();// .replace(" ", "&#x00A;");
					JSONObject objSettle = new JSONObject();
					objSettle.put("strItemCode", obj[0].toString());
					objSettle.put("strItemName", itemName);
					objSettle.put("strTextColor", obj[2].toString());
					objSettle.put("strPriceMonday", obj[3].toString());
					objSettle.put("strPriceTuesday", obj[4].toString());
					objSettle.put("strPriceWednesday", obj[5]);

					objSettle.put("strPriceThursday", obj[6].toString());
					objSettle.put("strPriceFriday", obj[7].toString());
					objSettle.put("strPriceSaturday", obj[8].toString());
					objSettle.put("strPriceSunday", obj[9].toString());
					objSettle.put("tmeTimeFrom", obj[10].toString());
					objSettle.put("strAMPMFrom", obj[11].toString());
					objSettle.put("tmeTimeTo", obj[12].toString());
					objSettle.put("strAMPMTo", obj[13].toString());
					objSettle.put("strCostCenterCode", obj[14].toString());
					objSettle.put("strHourlyPricing", obj[15].toString());
					objSettle.put("strSubMenuHeadCode", obj[16].toString());
					objSettle.put("dteFromDate", obj[17].toString());
					objSettle.put("dteToDate", obj[18].toString());
					objSettle.put("strStockInEnable", obj[19].toString());
					objSettle.put("strMenuCode", obj[20].toString());
					objSettle.put("strSubGroupCode", obj[21].toString());
					objSettle.put("strGroupcode", obj[22].toString());
					objSettle.put("strSubGroupName", obj[23].toString());
					objSettle.put("strGroupName", obj[24].toString());

					jArr.add(objSettle);
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return jArr;
	}
*/
	@RequestMapping(value = "/funabc", method = RequestMethod.GET)
	public void funLoadModifiers(HttpServletRequest request){
}

	@RequestMapping(value = "/funLoadModifiers", method = RequestMethod.GET)
	public @ResponseBody JSONObject funLoadModifiers(@RequestParam("itemCode") String itemCode, HttpServletRequest request)
	{
		JSONObject jObj = new JSONObject();
		List list = null;

		try
		{

			
			String clientCode = request.getSession().getAttribute("gClientCode").toString();
			if(request.getSession().getAttribute("WebClientCode")!=null)
			{
				String sql = "select a.strModifierName,a.strModifierCode" + " ,b.dblRate,a.strModifierGroupCode,b.strDefaultModifier " + " from tblmodifiermaster a,tblitemmodofier b " + " where a.strModifierCode=b.strModifierCode " + " and b.strItemCode='" + itemCode + "' and a.strClientCode='"+clientCode+"' and a.strClientCode=b.strClientCode and b.strApplicable='Y'"
						+ "   group by a.strModifierCode;";
				list = objBaseService.funGetList(new StringBuilder(sql), "sql");

			}
			else
			{
				String sql = "select a.strModifierName,a.strModifierCode" + " ,b.dblRate,a.strModifierGroupCode,b.strDefaultModifier " + ""
						+ "  from tblmodifiermaster a,tblitemmodofier b " + " where a.strModifierCode=b.strModifierCode " + " and b.strItemCode='" + itemCode + "' and a.strClientCode='"+clientCode+"' and b.strApplicable='Y'"
						+ "   group by a.strModifierCode;";
				list = objBaseService.funGetList(new StringBuilder(sql), "sql");

			}

			
			JSONArray jArr = new JSONArray();
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);
					JSONObject objSettle = new JSONObject();
					String strItemName = obj[0].toString();// .replace(" ", "&#x00A;");
					objSettle.put("strModifierName", obj[0].toString());
					objSettle.put("strModifierCode", obj[1].toString());
					objSettle.put("dblRate", obj[2]);
					objSettle.put("strModifierGroupCode", obj[3].toString());
					objSettle.put("strDefaultModifier", obj[4].toString());

					jArr.add(objSettle);
				}
			}
			jObj.put("Modifiers", jArr);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			return jObj;
		}

	}

	@RequestMapping(value = "/funFillTopModifierButtonList", method = RequestMethod.GET)
	public @ResponseBody JSONObject funFillTopModifierButtonList(@RequestParam("itemCode") String itemCode, HttpServletRequest request)
	{
		List list = null;
		JSONObject jObj = new JSONObject();
		try
		{

			String clientCode = request.getSession().getAttribute("gClientCode").toString();
			
			String sql = "select a.strModifierGroupCode,a.strModifierGroupShortName,a.strApplyMaxItemLimit," + "a.intItemMaxLimit,a.strApplyMinItemLimit,a.intItemMinLimit  from tblmodifiergrouphd a,tblmodifiermaster b,tblitemmodofier c " + "where a.strOperational='YES' and a.strModifierGroupCode=b.strModifierGroupCode and " + "b.strModifierCode=c.strModifierCode and c.strItemCode='" + itemCode + "' and a.strClientCode='"+clientCode+"' and a.strClientCode=b.strClientCode and a.strClientCode=c.strClientCode group by a.strModifierGroupCode";

			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			JSONArray jArr = new JSONArray();
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					JSONObject objSettle = new JSONObject();
					String strItemName = obj[1].toString();// .replace(" ", "&#x00A;");
					objSettle.put("strModifierGroupCode", obj[0].toString());
					objSettle.put("strModifierGroupShortName", strItemName);
					objSettle.put("strApplyMaxItemLimit", obj[2].toString());
					objSettle.put("intItemMaxLimit", obj[3].toString());
					objSettle.put("strApplyMinItemLimit", obj[4].toString());
					objSettle.put("intItemMinLimit", obj[5]);

					jArr.add(objSettle);
				}
			}
			jObj.put("topButtonModifier", jArr);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			return jObj;
		}
	}

	@RequestMapping(value = "/funCalculateTax", method = RequestMethod.POST)
	public @ResponseBody String funCalculateTax(@RequestParam("arrKOTItemDtlList") List<String> arrKOTItemDtlList, HttpServletRequest request)
	{
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String posCode = request.getSession().getAttribute("gPOSCode").toString();
		String posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];

		String total = "",strAreaCode="";
		double amt = 0.00,taxAmt=0;
		StringBuilder sqlBuilder = new StringBuilder();
		List<clsPOSItemDtlForTax> arrListItemDtls = new ArrayList<clsPOSItemDtlForTax>();
		List list = null;
		try
		{
			for (int i = 0; i < arrKOTItemDtlList.size(); i++)
			{
				String itemDtl = arrKOTItemDtlList.get(i);
				String[] arrItemDtl = itemDtl.split("_");

				if (arrItemDtl[2].trim().length() > 0)
				{
					double dblAmount = Double.parseDouble(arrItemDtl[2]);

					clsPOSItemDtlForTax objItemDtl = new clsPOSItemDtlForTax();
					objItemDtl.setItemCode(arrItemDtl[0]);
					objItemDtl.setItemName(arrItemDtl[1]);
					objItemDtl.setAmount(dblAmount);
					objItemDtl.setDiscAmt(0);
					arrListItemDtls.add(objItemDtl);

					amt += dblAmount;
				}
			}
			if (strAreaCode.equals(""))
			{
				clsSetupHdModel objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
				strAreaCode=objSetupHdModel.getStrDirectAreaCode();
//				sqlBuilder.setLength(0);
//				sqlBuilder.append("select strDirectAreaCode from tblsetup where (strPOSCode='" + posCode + "'  OR strPOSCode='All') and strClientCode='" + clientCode + "'");
//				List listAreCode = objBaseServiceImpl.funGetList(sqlBuilder, "sql");
//				if (listAreCode.size() > 0)
//				{
//					for (int cnt = 0; cnt < listAreCode.size(); cnt++)
//					{
//						Object obj = (Object) listAreCode.get(cnt);
//						strAreaCode = (obj.toString());
//					}
//				}
			}
			String gCalculateTaxOnMakeKOT = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gCalculateTaxOnMakeKOT");
			if (gCalculateTaxOnMakeKOT.equalsIgnoreCase("Y"))
			{
				String dtPOSDate = posDate.split(" ")[0];
////funCalculateTax(List<clsPOSItemDetailFrTaxBean> arrListItemDtl, String POSCode, String dtPOSDate, String billAreaCode, String operationTypeForTax, double subTotal, double discountAmt, String transType, String settlementCode, String taxOnSP,String strSCTaxForRemove,String strClientCode) throws Exception				
				List<clsPOSTaxCalculationBean> listTax = objUtility.funCalculateTax(arrListItemDtls, posCode, dtPOSDate, strAreaCode, "DineIn", 0, 0, "","S01","Sales","N",clientCode);
				taxAmt = 0;
				for (clsPOSTaxCalculationBean objTaxDtl : listTax)
				{
					if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
					{
						taxAmt = taxAmt + objTaxDtl.getTaxAmount();
					}
				}
				amt += taxAmt;

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return String.valueOf(amt);
	}

	@RequestMapping(value = "/funChekReservation", method = RequestMethod.GET)
	public @ResponseBody JSONObject funChekReservation(@RequestParam("strTableNo") String strTableNo, HttpServletRequest request)
	{
		List list = null;
		JSONObject jObjTableData = new JSONObject();
		try
		{
			String clientCode = request.getSession().getAttribute("gClientCode").toString();

			String sql = "select a.strTableNo,a.strTableName,a.strStatus " + "from tbltablemaster a " + "where a.strTableNo='" + strTableNo + "' " + " and a.strStatus='Reserve'  and a.strClientCode='"+clientCode+"' ";

			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list.size() > 0)
			{
				sql = "select a.strResCode,a.strCustomerCode,b.strCustomerName,b.longMobileNo " + "from tblreservation a,tblcustomermaster b " + "where a.strTableNo='" + strTableNo + "' " + "and a.strCustomerCode=b.strCustomerCode " + " and a.strClientCode='"+clientCode+"' order by a.strResCode desc " + "limit 1; ";

				list = objBaseService.funGetList(new StringBuilder(sql), "sql");
				if (list.size() > 0)
				{
					Object[] obj = (Object[]) list.get(0);
					jObjTableData.put("strCustomerCode", obj[1].toString());
					jObjTableData.put("strCustomerName", obj[2].toString());
					jObjTableData.put("MobileNo", obj[3]);
					jObjTableData.put("flag", true);

				}
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return jObjTableData;
		}

	}

	@RequestMapping(value = "/funFillMapWithHappyHourItems", method = RequestMethod.GET)
	public @ResponseBody JSONObject funFillMapWithHappyHourItems(HttpServletRequest request)
	{
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String posCode = request.getSession().getAttribute("gPOSCode").toString();
		String posDate = request.getSession().getAttribute("gPOSDate").toString();

		List list = null;
		StringBuilder sql = new StringBuilder();
		JSONObject jObjTableData = new JSONObject();

		try
		{
			sql.append("select strAreaCode from tblareamaster where strAreaName like '%All%' and strClientCode='"+clientCode+"' ");
			list = objBaseService.funGetList(sql, "sql");
			String gAreaCodeForTrans="";
			if (list.size() > 0)
				gAreaCodeForTrans = (String) list.get(0);
			
			sql.setLength(0);
			String gAreaWisePricing = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gAreaWisePricing");
			if (gAreaWisePricing.equalsIgnoreCase("N"))
			{
				sql = new StringBuilder("SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday," + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday, " + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo," + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate" + " ,b.strStockInEnable " + " FROM tblmenuitempricingdtl a ,tblitemmaster b " + " WHERE a.strItemCode=b.strItemCode " + " and a.strAreaCode='" + gAreaCodeForTrans + "' " + " and (a.strPosCode='" + posCode + "' or a.strPosCode='All') " + " and date(a.dteFromDate)<='" + posDate + "' and date(a.dteToDate)>='" + posDate + "' and a.strClientCode='"+clientCode+"' " + " and a.strHourlyPricing='Yes'  and a.strClientCode=b.strClientCode ");
			}
			else
			{
				sql = new StringBuilder("SELECT a.strItemCode,b.strItemName,a.strTextColor,a.strPriceMonday,a.strPriceTuesday," + " a.strPriceWednesday,a.strPriceThursday,a.strPriceFriday," + " a.strPriceSaturday,a.strPriceSunday,a.tmeTimeFrom,a.strAMPMFrom,a.tmeTimeTo,a.strAMPMTo," + " a.strCostCenterCode,a.strHourlyPricing,a.strSubMenuHeadCode,a.dteFromDate,a.dteToDate" + ",b.strStockInEnable " + " FROM tblmenuitempricingdtl a ,tblitemmaster b " + " WHERE a.strAreaCode='" + gAreaCodeForTrans + "' " + " and a.strItemCode=b.strItemCode " + " and (a.strPosCode='" + posCode + "' or a.strPosCode='All') " + " and date(a.dteFromDate)<='" + posDate + "' and date(a.dteToDate)>='" + posDate + "' and a.strClientCode='"+clientCode+"' " + " and a.strHourlyPricing='Yes' and a.strClientCode=b.strClientCode ");
			}

			list = objBaseService.funGetList(sql, "sql");
			JSONArray jArrData = new JSONArray();
			JSONArray jArrItemCodeData = new JSONArray();
			if (list != null)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					String itemName = obj[1].toString();// .replace(" ", "&#x00A;");
					JSONObject objSettle = new JSONObject();
					objSettle.put("strItemCode", obj[0].toString());
					objSettle.put("strItemName", itemName);
					objSettle.put("strTextColor", obj[2].toString());
					objSettle.put("strPriceMonday", obj[3].toString());
					objSettle.put("strPriceTuesday", obj[4].toString());
					objSettle.put("strPriceWednesday", obj[5]);

					objSettle.put("strPriceThursday", obj[6].toString());
					objSettle.put("strPriceFriday", obj[7].toString());
					objSettle.put("strPriceSaturday", obj[8].toString());
					objSettle.put("strPriceSunday", obj[9].toString());
					objSettle.put("tmeTimeFrom", obj[10].toString());
					objSettle.put("strAMPMFrom", obj[11].toString());
					objSettle.put("tmeTimeTo", obj[12].toString());
					objSettle.put("strAMPMTo", obj[13].toString());
					objSettle.put("strCostCenterCode", obj[14].toString());
					objSettle.put("strHourlyPricing", obj[15].toString());
					objSettle.put("strSubMenuHeadCode", obj[16].toString());
					objSettle.put("dteFromDate", obj[17].toString());
					objSettle.put("dteToDate", obj[18].toString());
					objSettle.put("strStockInEnable", obj[19].toString());

					jArrData.add(objSettle);
					jArrItemCodeData.add(obj[0].toString());
				}
			}
			jObjTableData.put("ItemPriceDtl", jArrData);
			jObjTableData.put("ItemCode", jArrItemCodeData);

			jObjTableData.put("CurrentDate", objUtility.funGetCurrentDate());
			jObjTableData.put("CurrentTime", objUtility.funGetCurrentTime());
			jObjTableData.put("DayForPricing", objUtility.funGetDayForPricing());
		//	jObjTableData.put("ListTDHOnModifierItem", ListTDHOnModifierItem);
		//	jObjTableData.put("ListTDHOnModifierItemMaxQTY", ListTDHOnModifierItemMaxQTY);

			sql = new StringBuilder("select strPosCode,strPosName,strPosType,strDebitCardTransactionYN" + " ,strPropertyPOSCode,strCounterWiseBilling,strDelayedSettlementForDB,strBillPrinterPort" + " ,strAdvReceiptPrinterPort,strPrintVatNo,strPrintServiceTaxNo,strVatNo,strServiceTaxNo" + " ,strEnableShift from tblposmaster where strClientCode='"+clientCode+"' ");

			list = list = objBaseService.funGetList(sql, "sql");
			if (list.size() > 0)
			{
				Object[] obj = (Object[]) list.get(0);
				jObjTableData.put("gDebitCardPayment", obj[3].toString());
			}
			//gInrestoPOSIntegrationYN = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gInrestoPOSIntegrationYN");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return jObjTableData;
		}

	}

	@RequestMapping(value = "/funChekCustomerDtl", method = RequestMethod.GET)
	public @ResponseBody JSONObject funChekCustomerDtl(@RequestParam("strTableNo") String strTableNo, HttpServletRequest request)
	{
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String posCode = request.getSession().getAttribute("gPOSCode").toString();

		List list = null;
		
		JSONObject jObjTableData = new JSONObject();
		try
		{

			StringBuilder sql = new StringBuilder(" select a.strWaiterNo,a.intPaxNo,sum(a.dblAmount),a.strCardNo,if(a.strCustomerCode='null','',a.strCustomerCode) " + ",ifnull(b.strCustomerName,''),ifnull(b.strBuldingCode,''),a.strHomeDelivery " + " from tblitemrtemp a left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode and a.strClientCode=b.strClientCode " + " where a.strTableNo='" + strTableNo + "' and a.strPrintYN='Y' and a.strNCKotYN='N' and a.strClientCode='"+clientCode+"' " + " group by a.strTableNo");

			list = objBaseService.funGetList(sql, "sql");
			if (list.size() > 0)
			{

				Object[] obj = (Object[]) list.get(0);
				jObjTableData.put("strWaiterNo", obj[0].toString());
				jObjTableData.put("intPaxNo", obj[1].toString());
				jObjTableData.put("strCustomerCode", obj[4].toString());
				jObjTableData.put("strCustomerName", obj[5].toString());
				jObjTableData.put("strHomeDelivery", obj[7].toString());
				jObjTableData.put("flag", true);

				sql = new StringBuilder("select strWaiterNo from tbltablemaster where strTableNo='" + strTableNo + "' and strClientCode='"+clientCode+"' ");
				String waiterNo = "";
				list = objBaseService.funGetList(sql, "sql");
				if (list.size() > 0)
				{
					Object objTime = (Object) list.get(0);
					waiterNo = objTime.toString();
					jObjTableData.put("waiterNo", waiterNo);
				}
			}
			String gCMSIntegrationYN = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gCMSIntegrationYN");

			if (gCMSIntegrationYN.equalsIgnoreCase("Y"))
			{
				sql = new StringBuilder("select strCustomerCode ,strCustomerName " + "from tblitemrtemp where strtableno = '" + strTableNo + "' and strCustomerCode <>''  and strClientCode='"+clientCode+"'  ");
				list = objBaseService.funGetList(sql, "sql");
				if (list.size() > 0)
				{
					Object[] objTime = (Object[]) list.get(0);
					jObjTableData.put("CustomerCode", objTime[0].toString());
					jObjTableData.put("CustomerName", objTime[1].toString());
				}

			}
			sql = new StringBuilder("select a.strAreaCode,b.strAreaName from tbltablemaster a,tblareamaster b " + "where a.strTableNo='" + strTableNo + "' and a.strAreaCode=b.strAreaCode  and a.strClientCode='"+clientCode+"'  and a.strClientCode=b.strClientCode ");

			list = objBaseService.funGetList(sql, "sql");
			if (list.size() > 0)
			{
				Object[] objTime = (Object[]) list.get(0);
			//	clsAreaCode = objTime[0].toString();

				jObjTableData.put("AreaCode", objTime[0].toString());
				jObjTableData.put("AreaName", objTime[1].toString());
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return jObjTableData;
		}

	}

	@RequestMapping(value = "/funChekCMSCustomerDtl", method = RequestMethod.GET)
	public @ResponseBody JSONObject funChekCMSCustomerDtl(@RequestParam("strTableNo") String strTableNo, HttpServletRequest request)
	{
		List list = null;
		JSONObject objSettle = new JSONObject();
		try
		{
			String clientCode = request.getSession().getAttribute("gClientCode").toString();
			String sql = "select ifnull(sum(dblAmount),0),ifnull(strCustomerCode,''),ifnull(strCustomerName,'') " + "from tblitemrtemp where strtableno = '" + strTableNo + "' and strCustomerCode <>''  and strClientCode='"+clientCode+"'  ";

			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list != null)
			{
				Object[] obj = (Object[]) list.get(0);
				objSettle.put("dblAmount", obj[0]);
				objSettle.put("strCustomerCode", obj[1].toString());
				objSettle.put("strCustomerName", obj[2].toString());
				objSettle.put("flag", true);
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			return objSettle;
		}
	}

	@RequestMapping(value = "/funChekCardDtl", method = RequestMethod.GET)
	public @ResponseBody JSONObject funChekCardDtl(@RequestParam("strTableNo") String strTableNo, HttpServletRequest request)
	{
		List list = null;
		JSONObject jObjTableData = new JSONObject();
		try
		{
			String clientCode = request.getSession().getAttribute("gClientCode").toString();
			String sql = " select a.strWaiterNo,a.intPaxNo,sum(a.dblAmount),b.dblRedeemAmt,a.strCardNo " + " from tblitemrtemp a,tbldebitcardmaster b " + " where a.strCardNo=b.strCardNo and strTableNo='" + strTableNo + "' " + " and strPrintYN='Y' and strNCKotYN='N'  and a.strClientCode=b.strClientCode  and a.strClientCode='"+clientCode+"' group by strTableNo ";

			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list.size() > 0)
			{
				Object[] obj = (Object[]) list.get(0);
				if ((double) obj[3] > 0)
				{
					double debitCardBal = (double) obj[3];
					debitCardBal = objUtility.funGetKOTAmtOnTable(obj[4].toString(),clientCode);

					jObjTableData.put("cardBalnce", debitCardBal);
					jObjTableData.put("kotAmt", (double) obj[2]);
					jObjTableData.put("balAmt", (double) obj[3]);
					jObjTableData.put("flag", true);
				}
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return jObjTableData;
		}
	}

	/* need cms data from cms webservice so this function was not changed */
	@RequestMapping(value = "/funCheckMemeberBalance", method = RequestMethod.GET)
	public @ResponseBody JSONObject funCheckMemeberBalance(@RequestParam("strCustomerCode") String strCustomerCode, HttpServletRequest request)
	{

		JSONObject jObj =null; //objGlobal.funGETMethodUrlJosnObjectData(clsPOSGlobalFunctionsController.POSWSURL + "/clsMakeKOTController/funCheckMemeberBalance?strCustomerCode=" + strCustomerCode);

		return jObj;
	}

	@RequestMapping(value = "/funFillOldKOTItems", method = RequestMethod.GET)
	public @ResponseBody JSONObject funFillOldKOTItems(@RequestParam("strTableNo") String strTableNo, HttpServletRequest request)
	{
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String posCode = request.getSession().getAttribute("gPOSCode").toString();
		String posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];

		double amt = 0.00,taxAmt=0;
		List<clsPOSItemDtlForTax> arrListItemDtls = new ArrayList<clsPOSItemDtlForTax>();
		List list = null;
		JSONObject jObjTableData = new JSONObject();
		try
		{
			StringBuilder sql = new StringBuilder();
			sql.append("select strAreaCode from tblareamaster where strAreaName like '%All%' and strClientCode='"+clientCode+"' ");
			list = objBaseService.funGetList(sql, "sql");
			String gAreaCodeForTrans="";
			if (list.size() > 0)
				gAreaCodeForTrans = (String) list.get(0);
			
			sql.setLength(0);

			sql = new StringBuilder("select distinct(strKOTNo) " + " from tblitemrtemp " + " where (strPosCode='" + posCode + "' or strPosCode='All') " + " and strTableNo='" + strTableNo + "' and strPrintYN='Y' and strNCKotYN='N' and strClientCode='"+clientCode+"' order by strKOTNo DESC");
			list = objBaseService.funGetList(sql, "sql");
			if (list.size() > 0)
			{
				JSONArray jArr = new JSONArray();
				for (int i = 0; i < list.size(); i++)
				{
					JSONObject jObjData = new JSONObject();

					String kotNo = (String) list.get(i);
					jObjData.put("kotNo", kotNo);

					String sqlKot = "select DATE_FORMAT(dteDateCreated,'%H:%i') from tblitemrtemp where strKOTNo='" + kotNo + "'  and strClientCode='"+clientCode+"'  limit 1";

					List timelist = objBaseService.funGetList(new StringBuilder(sqlKot), "sql");
					if (timelist.size() > 0)
					{
						Object objTime = (Object) timelist.get(0);
						jObjData.put("kotTime", objTime.toString());
					}
					jArr.add(jObjData);
				}
				jObjTableData.put("OldKOTTimeDtl", jArr);

				sql = new StringBuilder("SELECT a.strKOTNo,a.strTableNo,a.strWaiterNo,a.strItemName,a.strItemCode,a.dblItemQuantity, " + " a.dblAmount,a.intPaxNo,a.strPrintYN,a.tdhComboItemYN,a.strSerialNo,a.strNcKotYN,a.dblRate ,ifNull(b.strSubGroupCode,''),ifNull(c.strGroupCode,'') ,ifNull(c.strSubGroupName,''),ifNull(d.strGroupName,'') " + " FROM tblitemrtemp a left outer join tblitemmaster b on a.strItemCode=b.strItemCode and a.strClientCode=b.strClientCode  " + " left outer join tblsubgrouphd c on b.strSubGroupCode=c.strSubGroupCode  and b.strClientCode=c.strClientCode " + " left outer join tblgrouphd d on c.strGroupCode=d.strGroupCode  and d.strClientCode=c.strClientCode where strTableNo='" + strTableNo + "' " + " and (strPosCode='" + posCode + "' or strPosCode='All') " + " and strNcKotYN='N' " + " and a.strClientCode='"+clientCode+"' order by strKOTNo desc ,strSerialNo");
				list = objBaseService.funGetList(sql, "sql");
				if (list.size() > 0)
				{
					boolean flag = false;
					JSONArray jArrData = new JSONArray();
					String lastWaiterNo = "",
							lastWaiterName = "",
							lastPAXNo = "0";

					for (int i = 0; i < list.size(); i++)
					{
						Object[] obj = (Object[]) list.get(i);
						JSONObject objSettle = new JSONObject();
						BigDecimal dblAmount = (BigDecimal) obj[6];
						if (dblAmount.doubleValue() >= 0)
						{
							objSettle.put("strKOTNo", obj[0].toString());
							objSettle.put("strTableNo", obj[1].toString());
							objSettle.put("strWaiterNo", obj[2].toString());
							objSettle.put("strItemName", obj[3].toString());
							objSettle.put("strItemCode", obj[4].toString());
							objSettle.put("dblItemQuantity", obj[5]);
							objSettle.put("dblAmount", dblAmount.doubleValue());
							objSettle.put("intPaxNo", obj[7].toString());
							objSettle.put("strPrintYN", obj[8].toString());
							objSettle.put("tdhComboItemYN", obj[9].toString());
							objSettle.put("strSerialNo", obj[10].toString());
							objSettle.put("strNcKotYN", obj[11].toString());
							objSettle.put("dblRate", obj[12].toString());

							objSettle.put("strSubGroupCode", obj[13].toString());
							objSettle.put("strGroupcode", obj[14].toString());
							objSettle.put("strSubGroupName", obj[15].toString());
							objSettle.put("strGroupName", obj[16].toString());

							clsPOSItemDtlForTax objItemDtl = new clsPOSItemDtlForTax();
							objItemDtl.setItemCode(obj[4].toString());
							objItemDtl.setItemName(obj[3].toString());
							objItemDtl.setAmount(dblAmount.doubleValue());
							objItemDtl.setDiscAmt(0);
							arrListItemDtls.add(objItemDtl);
							amt += dblAmount.doubleValue();
							flag = true;
							jArrData.add(objSettle);
						}
					}
					jObjTableData.put("OldKOTItems", jArrData);
					jObjTableData.put("flag", flag);

					String gCalculateTaxOnMakeKOT = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gCalculateTaxOnMakeKOT");
					if (gCalculateTaxOnMakeKOT.equalsIgnoreCase("Y"))
					{
						String dtPOSDate = posDate.split(" ")[0];
//funCalculateTax(List<clsPOSItemDetailFrTaxBean> arrListItemDtl, String POSCode, String dtPOSDate, String billAreaCode, String operationTypeForTax, double subTotal, double discountAmt, String transType, String settlementCode, String taxOnSP,String strSCTaxForRemove,String strClientCode) throws Exception						
						List<clsPOSTaxCalculationBean> listTax = objUtility.funCalculateTax(arrListItemDtls, posCode, dtPOSDate, gAreaCodeForTrans, "DineIn", 0, 0, "","S01","Sales","N",clientCode);
						taxAmt = 0;
						for (clsPOSTaxCalculationBean objTaxDtl : listTax)
						{
							if (objTaxDtl.getTaxCalculationType().equalsIgnoreCase("Forward"))
							{
								taxAmt = taxAmt + objTaxDtl.getTaxAmount();
							}
						}
						amt += taxAmt;
					}
					jObjTableData.put("Total", amt);
					jObjTableData.put("TaxTotal", taxAmt);
				}
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return jObjTableData;
		}

	}

	@RequestMapping(value = "/funCheckDebitCardString", method = RequestMethod.GET)
	public @ResponseBody JSONObject funCheckDebitCardString(@RequestParam("debitCardNo") String debitCardNo, HttpServletRequest request)
	{
		String cardString = funGetSingleTrackData(debitCardNo);
		String posCode = request.getSession().getAttribute("gPOSCode").toString();
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String waiterNo = "";
		JSONObject jObj = new JSONObject();
		try
		{

			String sql = "select strWaiterNo,strWShortName,strWFullName,strOperational " + " from tblwaitermaster " + " where strDebitCardString='" + debitCardNo + "' and (strPOSCode='All' or strPOSCode='" + posCode + "') and  a.strClientCode='"+clientCode+"' ";
			List list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list.size() > 0)
			{
				Object[] obj = (Object[]) list.get(0);
				if (!obj[3].toString().equals("N"))
				{
					waiterNo = obj[0].toString() + "#" + obj[1].toString();
				}
			}

			jObj.put("waiterNo", waiterNo);
		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
		finally
		{
			return jObj;
		}

	}

	@RequestMapping(value = "/funLoadPopularItems", method = RequestMethod.GET)
	public @ResponseBody JSONObject funPopularItem(HttpServletRequest request)
	{
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String posCode = request.getSession().getAttribute("gPOSCode").toString();
		String posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];

		JSONObject jObjTableData = new JSONObject();
		try{
			clsSetupHdModel objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
			JSONArray jsonArrForPopularItems = funGetPopularItem(clientCode, posDate, posCode,objSetupHdModel.getStrDirectAreaCode());
			jObjTableData.put("PopularItems", jsonArrForPopularItems);

		}catch(Exception e){
			e.printStackTrace();
		}

		return jObjTableData;
	}

	@RequestMapping(value = "/funFillTopButtonList", method = RequestMethod.GET)
	public @ResponseBody JSONObject funFillTopButtonList(@RequestParam("menuHeadCode") String menuHeadCode, HttpServletRequest request)
	{

		String posCode = request.getSession().getAttribute("gPOSCode").toString();
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		JSONObject jObj = new JSONObject();
		try
		{
			StringBuilder sql = new StringBuilder();
			sql.append("select strAreaCode from tblareamaster where strAreaName like '%All%' and strClientCode='"+clientCode+"' ");
			List list = objBaseService.funGetList(sql, "sql");
			String gAreaCodeForTrans="";
			if (list.size() > 0)
				gAreaCodeForTrans = (String) list.get(0);
			
			
			String sqlItems = "";
			String gMenuItemSortingOn = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gMenuItemSortingOn");
			if (gMenuItemSortingOn.equalsIgnoreCase("subgroupWise"))
			{
				sqlItems = "select c.strSubGroupName,b.strSubGroupCode " + " from tblmenuitempricingdtl a,tblitemmaster b,tblsubgrouphd c " + " where a.strItemCode=b.strItemCode " + " and b.strSubGroupCode=c.strSubGroupCode " + " and (a.strPosCode='" + posCode + "' or a.strPosCode='All') " + " and date(a.dteFromDate)<='" + posDate + "' and date(a.dteToDate)>='" + posDate + "' and b.strClientCode=c.strClientCode and a.strClientCode=b.strClientCode and a.strClientCode='"+clientCode+"' ";

				if (menuHeadCode.equalsIgnoreCase("Popular"))
				{
					sqlItems += " and a.strPopular='Y' and a.strAreaCode='" + gAreaCodeForTrans + "' " + " group by c.strSubGroupCode ORDER by c.strSubGroupName";
				}
				else
				{
					sqlItems += " and a.strMenuCode='" + menuHeadCode + "' " + " group by c.strSubGroupCode ORDER by c.strSubGroupName";
				}
			}
			else if (gMenuItemSortingOn.equalsIgnoreCase("subMenuHeadWise"))
			{
				sqlItems = "select b.strSubMenuHeadName,a.strSubMenuHeadCode " + " from tblmenuitempricingdtl a left outer join tblsubmenuhead b " + " on a.strSubMenuHeadCode=b.strSubMenuHeadCode and a.strMenuCode=b.strMenuCode " + " where b.strSubMenuHeadName is not null and b.strSubMenuOperational='Y' " + " and (a.strPosCode='" + posCode + "' or a.strPosCode='All') " + " and date(a.dteFromDate)<='" + posDate + "' and date(a.dteToDate)>='" + posDate + "' and a.strClientCode=b.strClientCode  and a.strClientCode='"+clientCode+"' ";

				if (menuHeadCode.equalsIgnoreCase("Popular"))
				{
					sqlItems += " and a.strPopular='Y' " + "group by a.strSubMenuHeadCode";
				}
				else
				{
					sqlItems += " and a.strMenuCode='" + menuHeadCode + "' group by a.strSubMenuHeadCode";
				}
			}
			list = objBaseService.funGetList(new StringBuilder(sqlItems), "sql");
			JSONArray jArr = new JSONArray();
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					JSONObject objSettle = new JSONObject();
					objSettle.put("strCode", obj[1].toString());
					objSettle.put("strName", obj[0].toString());

					jArr.add(objSettle);
				}
			}
			jObj.put("topButtonList", jArr);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			return jObj;
		}

	}

	@RequestMapping(value = "/funCheckHomeDelivery", method = RequestMethod.GET)
	public @ResponseBody JSONObject funCheckHomeDelivery(@RequestParam("strTableNo") String strTableNo, HttpServletRequest request)
	{

		String posCode = request.getSession().getAttribute("gPOSCode").toString();
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		List list = null;
		JSONObject jObjTableData = new JSONObject();
		try
		{

			String sql = "select a.strTableNo,ifnull(a.strCustomerCode,''),ifnull(b.strCustomerName,'ND')" + " ,ifnull(b.strBuldingCode,''),ifnull(a.strDelBoyCode,'NA'),ifnull(c.strDPName,'NA') " + " from tblitemrtemp a left outer join tblcustomermaster b on a.strCustomerCode=b.strCustomerCode and  a.strClientCode=b.strClientCode " + " left outer join tbldeliverypersonmaster c on a.strDelBoyCode=c.strDPCode and a.strClientCode=c.strClientCode " + " where a.strHomeDelivery='Yes' and a.strTableNo='" + strTableNo + "' " + " and a.strPOSCode='" + posCode + "' and a.strClientCode= '"+clientCode+"' ";

			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list.size() > 0)
			{
				Object[] obj = (Object[]) list.get(0);
				jObjTableData.put("strCustomerCode", obj[1].toString());
				jObjTableData.put("strCustomerName", obj[2].toString());
				jObjTableData.put("strBuldingCode", obj[3].toString());
				jObjTableData.put("strDelBoyCode", obj[4].toString());
				jObjTableData.put("strDPName", obj[5].toString());
				jObjTableData.put("flag", true);

			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return jObjTableData;
		}

	}

	@RequestMapping(value = "/funCheckCustomer", method = RequestMethod.GET)
	public @ResponseBody JSONObject fuCkeckCustomer(@RequestParam("strMobNo") String strMobNo, HttpServletRequest request)
	{
		List list = null;
		JSONObject objSettle = new JSONObject();
		try
		{
			String clientCode = request.getSession().getAttribute("gClientCode").toString();
			String sql = "select count(strCustomerCode) from tblcustomermaster where longMobileNo like '%" + strMobNo + "%' and strClientCode='"+clientCode+"' ";
			list = objBaseService.funGetList(new StringBuilder(sql), "sql");

			if (list != null)
			{
				int cnt = ((BigInteger) list.get(0)).intValue();
				if (cnt > 0)
				{
					sql = "select strCustomerCode,strCustomerName,strBuldingCode,longMobileNo " + "from tblcustomermaster where longMobileNo like '%" + strMobNo + "%'  and strClientCode='"+clientCode+"' ";

					list = objBaseService.funGetList(new StringBuilder(sql), "sql");
					if (list != null)
					{
						Object[] obj = (Object[]) list.get(0);
						objSettle.put("strCustomerCode", obj[0].toString());
						objSettle.put("strCustomerName", obj[1].toString());
						objSettle.put("strBuldingCode", obj[2].toString());
						objSettle.put("longMobileNo", obj[3].toString());
						objSettle.put("flag", true);
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			return objSettle;
		}

	}
/*
	@RequestMapping(value = "/funCheckKOTSave", method = RequestMethod.GET)
	public @ResponseBody boolean funCheckKOTSave(@RequestParam("strKOTNo") String strKOTNo, HttpServletRequest request)
	{
		List list = null;
		boolean flag = false;

		try
		{

			String sql = "select strTableNo from tblitemrtemp where strTableNo='" + globalTableNo + "'";
			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list.size() > 0)
			{
				sql = "select strPrintYN from tblitemrtemp where strKOTNo='" + strKOTNo + "' " + "and strTableNo='" + globalTableNo + "' and strPrintYN='N' group by  strPrintYN";

				list = objBaseService.funGetList(new StringBuilder(sql), "sql");
				if (list.size() > 0)
				{
					flag = true;
				}
			}
			else
			{

				flag = true;
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			return flag;
		}
	}
*/
	
	
	@RequestMapping(value = "/funFillitemsSubMenuWise", method = RequestMethod.GET)
	public @ResponseBody JSONObject funFillitemsSubMenuWise(HttpServletRequest req)
	{
		String strMenuCode = req.getParameter("strMenuCode");
		String flag = req.getParameter("flag");
		String selectedButtonCode = req.getParameter("selectedButtonCode");

		String posCode = req.getSession().getAttribute("gPOSCode").toString();
		String clientCode = req.getSession().getAttribute("gClientCode").toString();
		String posDate = req.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		JSONObject jObj = new JSONObject();
		try
		{
			StringBuilder sql = new StringBuilder();
			sql.append("select strAreaCode from tblareamaster where strAreaName like '%All%' and strClientCode='"+clientCode+"' ");
			List list = objBaseService.funGetList(sql, "sql");
			String gAreaCodeForTrans="";
			if (list.size() > 0)
				gAreaCodeForTrans = (String) list.get(0);
			
			String clsAreaCode=gAreaCodeForTrans;
			
			String sqlItems = "";
			String gMenuItemSortingOn = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gMenuItemSortingOn");
			if (gMenuItemSortingOn.equalsIgnoreCase("subgroupWise"))
			{
				sqlItems = "SELECT b.strItemCode,c.strItemName,b.strTextColor,b.strPriceMonday,b.strPriceTuesday," + "b.strPriceWednesday,b.strPriceThursday,b.strPriceFriday,  " + "b.strPriceSaturday,b.strPriceSunday,b.tmeTimeFrom,b.strAMPMFrom,b.tmeTimeTo,b.strAMPMTo," + "b.strCostCenterCode,b.strHourlyPricing,b.strSubMenuHeadCode,b.dteFromDate,b.dteToDate,c.strStockInEnable " + "FROM tblmenuhd a LEFT OUTER JOIN tblmenuitempricingdtl b ON a.strMenuCode = b.strMenuCode and  a.strClientCode= b.strClientCode " + "RIGHT OUTER JOIN tblitemmaster c ON b.strItemCode = c.strItemCode  and  c.strClientCode= b.strClientCode " + "WHERE "
						+ " a.strClientCode='"+clientCode+"' and  ";

				if (flag.equalsIgnoreCase("Popular"))
				{
					sqlItems += " b.strPopular = 'Y' and c.strSubGroupCode='" + selectedButtonCode + "' and (b.strPosCode='" + posCode + "' or b.strPosCode='All')   " + " and date(b.dteFromDate)<='" + posDate + "' and date(b.dteToDate)>='" + posDate + "' ";

				}
				else
				{
					sqlItems += " a.strMenuCode = '" + strMenuCode + "' and c.strSubGroupCode='" + selectedButtonCode + "' and (b.strPosCode='" + posCode + "' or b.strPosCode='All')  " + " and date(b.dteFromDate)<='" + posDate + "' and date(b.dteToDate)>='" + posDate + "' ";
				}
				String gAreaWisePricing = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gAreaWisePricing");
				if (gAreaWisePricing.equalsIgnoreCase("Y"))
				{
					sqlItems = sqlItems + " and (b.strAreaCode='" + gAreaCodeForTrans + "' or b.strAreaCode='" + clsAreaCode + "')";
				}
				sqlItems = sqlItems + "  ORDER BY c.strItemName ASC";
			}
			else if (gMenuItemSortingOn.equalsIgnoreCase("subMenuHeadWise"))
			{
				sqlItems = "SELECT b.strItemCode,c.strItemName,b.strTextColor,b.strPriceMonday,b.strPriceTuesday," + "b.strPriceWednesday,b.strPriceThursday,b.strPriceFriday,  " + "b.strPriceSaturday,b.strPriceSunday,b.tmeTimeFrom,b.strAMPMFrom,b.tmeTimeTo,b.strAMPMTo," + "b.strCostCenterCode,b.strHourlyPricing,b.strSubMenuHeadCode,b.dteFromDate,b.dteToDate,c.strStockInEnable " + "FROM tblmenuitempricingdtl b,tblitemmaster c " + "WHERE c.strClientCode= b.strClientCode and "
						+ " b.strClientCode='"+clientCode+"' and  ";

				if (flag.equalsIgnoreCase("Popular"))
				{
					sqlItems += " b.strMenuCode = '" + selectedButtonCode + "' and b.strItemCode=c.strItemCode and b.strSubMenuHeadCode='" + selectedButtonCode + "' and (b.strPosCode='" + posCode + "' or b.strPosCode='All')  " + " and date(b.dteFromDate)<='" + posDate + "' and date(b.dteToDate)>='" + posDate + "' ";
				}
				else
				{
					sqlItems += " b.strMenuCode = '" + strMenuCode + "' and b.strItemCode=c.strItemCode and b.strSubMenuHeadCode='" + selectedButtonCode + "' and (b.strPosCode='" + posCode + "' or b.strPosCode='All')   " + " and date(b.dteFromDate)<='" + posDate + "' and date(b.dteToDate)>='" + posDate + "' ";

				}
				String gAreaWisePricing = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gAreaWisePricing");
				if (gAreaWisePricing.toString().equalsIgnoreCase("Y"))
				{
					sqlItems = sqlItems + " and (b.strAreaCode='" + gAreaCodeForTrans + "' or b.strAreaCode='" + clsAreaCode + "')";
				}
				sqlItems = sqlItems + " ORDER BY c.strItemName ASC";
			}
			
			list = objBaseService.funGetList(new StringBuilder(sqlItems), "sql");
			JSONArray jArr = new JSONArray();
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					JSONObject objSettle = new JSONObject();
					String strItemName = obj[1].toString();// .replace(" ", "&#x00A;");
					objSettle.put("strItemCode", obj[0].toString());
					objSettle.put("strItemName", strItemName);
					objSettle.put("strTextColor", obj[2].toString());
					objSettle.put("strPriceMonday", obj[3].toString());
					objSettle.put("strPriceTuesday", obj[4].toString());
					objSettle.put("strPriceWednesday", obj[5]);

					objSettle.put("strPriceThursday", obj[6].toString());
					objSettle.put("strPriceFriday", obj[7].toString());
					objSettle.put("strPriceSaturday", obj[8].toString());
					objSettle.put("strPriceSunday", obj[9].toString());
					objSettle.put("tmeTimeFrom", obj[10].toString());
					objSettle.put("strAMPMFrom", obj[11].toString());
					objSettle.put("tmeTimeTo", obj[12].toString());
					objSettle.put("strAMPMTo", obj[13].toString());
					objSettle.put("strCostCenterCode", obj[14].toString());
					objSettle.put("strHourlyPricing", obj[15].toString());
					objSettle.put("strSubMenuHeadCode", obj[16].toString());
					objSettle.put("dteFromDate", obj[17].toString());
					objSettle.put("dteToDate", obj[18].toString());
					objSettle.put("strStockInEnable", obj[19].toString());

					jArr.add(objSettle);
				}
			}

			jObj.put("SubMenuWiseItemList", jArr);
		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
		finally
		{
			return jObj;
		}

	}

	@RequestMapping(value = "/funGenerateKOTNo", method = RequestMethod.GET)
	public @ResponseBody JSONObject funGenerateKOTNo(HttpServletRequest request)
	{
		List list = null;
		JSONObject jObjTableData = new JSONObject();
		String kotNo = "";
		try
		{
			String clientCode = request.getSession().getAttribute("gClientCode").toString();
			long code = 0;
			String sql="";
			if(request.getSession().getAttribute("WebClientCode")!=null)
			{
				 sql = "select dblLastNo from tblinternal where strTransactionType='KOTNo' and  strClientCode='"+clientCode+"' ";

			}
			else
			{
				 sql = "select dblLastNo from tblinternal where strTransactionType='KOTNo'  ";

			}
			list = objBaseService.funGetList(new StringBuilder(sql), "sql");

			if (list.size() > 0)
			{
				code = ((BigInteger) list.get(0)).longValue();
				code = code + 1;
				kotNo = "KT" + String.format("%07d", code);
			}
			else
			{
				kotNo = "KT0000001";
			}
			jObjTableData.put("strKOTNo", kotNo);
			sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='KOTNo' ";

			if(request.getSession().getAttribute("WebClientCode")!=null)
			{
				sql = "update tblinternal set dblLastNo='" + code + "' where strTransactionType='KOTNo'  and  strClientCode='"+clientCode+"' ";

			}

			objBaseService.funExecuteUpdate(sql, "sql");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return jObjTableData;
		}
	}

	private String funGetSingleTrackData(String cardString)
	{
		String cardNo = "";
		if (cardString.contains("?") || cardString.contains(";"))
		{
			if (cardString.length() > 0)
			{
				StringBuilder sb = new StringBuilder(cardString);
				int percIndex = sb.indexOf("%");
				String allTracks = "";
				if (sb.toString().contains("?"))
				{
					allTracks = sb.substring(percIndex, sb.lastIndexOf("?") + 1);
				}
				else
				{
					allTracks = sb.toString();
				}
				String[] arrText = allTracks.split(";");
				String track1 = "",
						track2 = "",
						track3 = "";

				if (arrText.length > 0)
				{
					if (sb.toString().contains("?"))
					{
						track1 = arrText[0].substring(1, arrText[0].indexOf("?")).replaceAll("%", "");
						if (arrText.length > 1)
						{
							track2 = arrText[1].substring(1, arrText[1].indexOf("?")).replaceAll("%", "");
						}
						if (arrText.length > 2)
						{
							track3 = arrText[2].substring(1, arrText[2].indexOf("?")).replaceAll("%", "");
						}
					}
					else
					{
						track1 = arrText[0].replaceAll("%", "");
						track2 = arrText[1].replaceAll("%", "");
						track3 = arrText[2].replaceAll("%", "");
					}
				}

				if (!track1.isEmpty())
				{
					cardNo = track1;
				}
				else if (!track2.isEmpty())
				{
					cardNo = track2;
				}
				else if (!track3.isEmpty())
				{
					cardNo = track2;
				}

			}
		}
		else
		{
			cardNo = cardString;
		}

		return cardNo;
	}

	// on direct biller action performed
	// @RequestMapping(value = "/saveKOT", method = RequestMethod.POST)
	// public ModelAndView funSaveKOT(@ModelAttribute("command") @Valid
	// clsPOSMakeKOTBean objBean,BindingResult result,HttpServletRequest
	// req,@RequestParam("ncKot") String strNCKotYN,@RequestParam("takeAway")
	// String strTakeAwayYesNo,@RequestParam("globalDebitCardNo") String
	// globalDebitCardNo,@RequestParam("cmsMemCode") String
	// cmsMemCode,@RequestParam("cmsMemName") String
	// cmsMemName,@RequestParam("reasonCode") String
	// reasonCode,@RequestParam("homeDeliveryForTax") String
	// homeDeliveryForTax,@RequestParam("arrListHomeDelDetails") List<String>
	// arrListHomeDelDetails)
	// {
	@RequestMapping(value = "/saveKOT", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	private @ResponseBody String funSaveKOT(@RequestBody List listItmeDtl, HttpServletRequest req,HttpServletResponse response, @RequestParam("kotNo") String kotNo, @RequestParam("tableNo") String tableNo) throws Exception
	{
		String result = "true";
		try
		{

			String posCode = req.getSession().getAttribute("gPOSCode").toString();
			String userCode = req.getSession().getAttribute("gUserCode").toString();
			String clientCode = req.getSession().getAttribute("gClientCode").toString();
			String posDate = req.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
			String strTableNo =tableNo;

			/*String sqlTable="select a.strTableNo from tbltablemaster a where a.strTableName='"+tableNo+"' and a.strClientCode='"+clientCode+"'";
			List listTable = objBaseService.funGetList(new StringBuilder(sqlTable), "sql");
			if (listTable.size() > 0 && listTable!=null)
			{
				strTableNo=(String) listTable.get(0);
				
			}
*/
			
			int strPaxNo =0;
			String strKOTNo = kotNo;
			String strWaiterNo = "";

			Date dt = new Date();
			String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
			String dateTime = posDate + " " + currentDateTime.split(" ")[1];

			String homeDelivery = "No",
					strDelBoyCode = "",
					strCounterCode = "";
			double rate = 0;
			String sqlCounter = "select strCounterCode from tblcounterhd " + " where strUserCode='" + userCode + "' and strClientCode='"+clientCode+"' ";
			List list = objBaseService.funGetList(new StringBuilder(sqlCounter), "sql");
			if (list.size() > 0)
				strCounterCode = (String) list.get(0);

			Map<String, String> hmSerNoForItems = new HashMap<String, String>();
			for (int cnt = 0; cnt < listItmeDtl.size(); cnt++)
			{
				Map listItemDtl = (Map) listItmeDtl.get(cnt);

				String strItemName = listItemDtl.get("itemName").toString();
				double dblQuantity = Double.parseDouble(listItemDtl.get("quantity").toString());
				double dblAmount = Double.parseDouble(listItemDtl.get("amount").toString());
				String strSubGroupCode = listItemDtl.get("strSubGroupCode").toString();
				String strGroupCode = listItemDtl.get("strGroupcode").toString();
				String strItemCode = listItemDtl.get("itemCode").toString();

				if (strItemName.contains("-->"))
	   			{
	   				String code[] = strItemCode.split("!");
	   				strItemCode = code[0] + "" + code[1];
	   			}
				int serNo = cnt + 1;
				/*
				 * int serNo=1; if(hmSerNoForItems.containsKey(strItemName)) {
				 * if(strItemName.contains("-->")) {
				 * serNo=Integer.parseInt(hmSerNoForItems.get(strItemName)); serNo++; } else {
				 * serNo=Integer.parseInt(hmSerNoForItems.get(strItemName)); serNo++; } }
				 * hmSerNoForItems.put(strItemName, String.valueOf(serNo));
				 */
				clsMakeKOTHdModel objModel=null;
			    objModel = new clsMakeKOTHdModel(new clsMakeKOTModel_ID(String.valueOf(serNo), strTableNo, strItemCode, strItemName, strKOTNo));				
				objModel.setStrActiveYN("N");
				objModel.setStrCardNo("");
				objModel.setStrCardType(" ");
				objModel.setStrCounterCode(strCounterCode);
				objModel.setStrCustomerCode("");
				objModel.setStrCustomerName("");

				objModel.setStrDelBoyCode(strDelBoyCode);
				objModel.setStrHomeDelivery(homeDelivery);

				objModel.setStrManualKOTNo(" ");
				objModel.setStrNCKotYN("N");
				objModel.setStrOrderBefore(" ");
				objModel.setStrPOSCode(posCode);
				objModel.setStrPrintYN("Y");
				objModel.setStrPromoCode(" ");
				objModel.setStrReason("");
				objModel.setStrWaiterNo(strWaiterNo);
				objModel.setStrTakeAwayYesNo("No");
				objModel.setDblAmount(dblAmount);
				objModel.setDblBalance(0.00);
				objModel.setDblCreditLimit(0.00);
				objModel.setDblItemQuantity(dblQuantity);
				rate = dblAmount / dblQuantity;
				objModel.setDblRate(rate);
				objModel.setDblRedeemAmt(0);
				objModel.setDblTaxAmt(0);
				objModel.setIntId(0);
				objModel.setIntPaxNo(strPaxNo);

				objModel.setDteDateCreated(dateTime);
				objModel.setDteDateEdited(dateTime);
				objModel.setStrUserCreated(userCode);
				objModel.setStrUserEdited(userCode);
				objBaseService.funSave(objModel);
			}
			funUpdateKOT(dateTime, strKOTNo, strTableNo, "", homeDelivery, "", strPaxNo, 0,clientCode,0,req);
			funInsertIntoTblItemRTempBck(strTableNo, strKOTNo,clientCode,req);
			//printPDFResource1(response,req);//strTableNo,strKOTNo, costCenterCode, costCenterName,areaCode
			//response.getWriter().write("saved="+strKOTNo);
			if(objMasterService.funGetPOSWisePropertySetup(clientCode,posCode).getStrFireCommunication().equalsIgnoreCase("Y"))
			{		
				//Fire kot option in on ,dont print kot
			}
			else
			{
				//objGenerationKOTPrint.funKOTGeneration(tableNo, strKOTNo, "", "", "Dina", "Y",req,response);
			}
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = "false";
			response.getWriter().write("faild");
		}
		finally
		{
			return result;
		}

	}

	public void funUpdateKOT(String dateTime, String strKOTNo, String strTableNo, String strCustomerCode, String strHomeDelivery, String strNCKotYN, int intPaxNo, double KOTAmt,String strClientCode,double dblTaxAmt,HttpServletRequest req)
	{

		try
		{
			if(req.getSession().getAttribute("WebClientCode")!=null)
			{
				/*	String sql = "insert into tblkottaxdtl " + "values ('" + strTableNo + "','" + strKOTNo + "'," + KOTAmt + "," + dblTaxAmt + ",'"+strClientCode+"')";

				objBaseService.funExecuteUpdate(sql, "sql");
	*/
				String sql="";
				String sql_update = "update tblitemrtemp set strPrintYN='Y',dteDateCreated='" + dateTime + "' " + "where strKOTNo='" + strKOTNo + "' and strTableNo='" + strTableNo + "' and strClientCode ='"+strClientCode+"' ";

				objBaseService.funExecuteUpdate(sql_update, "sql");

				 sql_update = "update tblitemrtemp set strClientCode='"+strClientCode+"' " + "where strKOTNo='" + strKOTNo + "' and strTableNo='" + strTableNo + "'  ";

				objBaseService.funExecuteUpdate(sql_update, "sql");


				/*if (strHomeDelivery.equals("Yes"))
				{
					sql = "update tblitemrtemp set strHomeDelivery='Yes',strCustomerCode='" + strCustomerCode + "' " + "where strTableNo='" + strTableNo + "'  and strClientCode ='"+strClientCode+"' ";
					objBaseService.funExecuteUpdate(sql, "sql");
				}

				else
				{
					sql = "update tblitemrtemp set strHomeDelivery='No',strCustomerCode='" + strCustomerCode + "' " + "where strTableNo='" + strTableNo + "'  and strClientCode ='"+strClientCode+"' ";
					objBaseService.funExecuteUpdate(sql, "sql");
				}
				sql = "update tbldebitcardtabletemp set strPrintYN='Y' where strTableNo='" + strTableNo + "'  and strClientCode ='"+strClientCode+"' ";
				objBaseService.funExecuteUpdate(sql, "sql");

	*/		
				/*if ("Y".equals(strNCKotYN))
				{

					sql = "update tbltablemaster set strStatus='Normal' " + " where strTableNo='" + strTableNo + "' and strStatus='Normal'  and strClientCode ='"+strClientCode+"' ";
					objBaseService.funExecuteUpdate(sql, "sql");
				}
	*/			//else
				//{

					sql = "update tbltablemaster set strStatus='Occupied' where strTableNo='" + strTableNo + "'  and strClientCode ='"+strClientCode+"' ";
					objBaseService.funExecuteUpdate(sql, "sql");
			//	}

				sql = "update tbltablemaster set intPaxNo='" + intPaxNo + "' where strTableNo='" + strTableNo + "'  and strClientCode ='"+strClientCode+"' ";
				objBaseService.funExecuteUpdate(sql, "sql");

				/*
				 * if(gInrestoPOSIntegrationYN.equalsIgnoreCase("Y")) {
				 * 
				 * }
				 */

			}
			else
			{
				String sql="";
				String sql_update = "update tblitemrtemp set strPrintYN='Y',dteDateCreated='" + dateTime + "' " + "where strKOTNo='" + strKOTNo + "' and strTableNo='" + strTableNo + "' ";

				objBaseService.funExecuteUpdate(sql_update, "sql");


			    sql = "update tbltablemaster set strStatus='Occupied' where strTableNo='" + strTableNo + "' ";
				objBaseService.funExecuteUpdate(sql, "sql");

				sql = "update tbltablemaster set intPaxNo='" + intPaxNo + "' where strTableNo='" + strTableNo + "'   ";
				objBaseService.funExecuteUpdate(sql, "sql");


			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void funInsertIntoTblItemRTempBck(String tableNo, String kotNo,String strClientCode,HttpServletRequest req)
	{
		try
		{
			String sql="";
			if(req.getSession().getAttribute("WebClientCode")!=null)
			{
				 sql = "delete from tblitemrtemp_bck where strTableNo='" + tableNo + "' and strKOTNo='" + kotNo + "'  and strClientCode ='"+strClientCode+"' ";
				  objBaseService.funExecuteUpdate(sql, "sql");

				sql = "insert into tblitemrtemp_bck (select * from tblitemrtemp where strTableNo='" + tableNo + "' and strKOTNo='" + kotNo + "'  and strClientCode ='"+strClientCode+"'  )";
				objBaseService.funExecuteUpdate(sql, "sql");

			}
			else
			{
				 sql = "delete from tblitemrtemp_bck where strTableNo='" + tableNo + "' and strKOTNo='" + kotNo + "'  ";
				  objBaseService.funExecuteUpdate(sql, "sql");

				sql = "insert into tblitemrtemp_bck (select * from tblitemrtemp where strTableNo='" + tableNo + "' and strKOTNo='" + kotNo + "'   )";
				objBaseService.funExecuteUpdate(sql, "sql");

			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
/*
	@SuppressWarnings("finally")
	@RequestMapping(value = "/funCallWebService", method = RequestMethod.GET)
	public @ResponseBody JSONObject funCallWebService(@RequestParam("strMobNo") String strMobNo, HttpServletRequest request)
	{
		String strCustomerCode = "";
		List list = null;
		JSONObject obj = new JSONObject();
		try
		{
			String sql = "select strCustomerCode from tblcustomermaster where longMobileNo=" + strMobNo;
			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (list != null)
			{
				strCustomerCode = (String) list.get(0).toString();
			}

			DefaultHttpClient httpClient = new DefaultHttpClient();
			String getWebServiceURL = gGetWebserviceURL;
			getWebServiceURL += "" + strMobNo + "/outlet/" + gOutletUID + "/";
			HttpGet getRequest = new HttpGet(getWebServiceURL);
			HttpResponse response = httpClient.execute(getRequest);
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output = "", op = "";

			while ((output = br.readLine()) != null)
			{
				op += output;
			}
			// System.out.println(op);
			JSONParser p = new JSONParser();
			Object objJSON = p.parse(op);
			obj = (JSONObject) objJSON;

			return obj;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
		finally
		{
			return obj;
		}
	}

*/	// ///////Promotion Calculation

	
	
	@RequestMapping(value = "/promotionCalculateForKOT", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	private @ResponseBody Map funPromotionCalculate(@RequestBody List listItmeDtl, HttpServletRequest request) throws Exception
	{
		Map<String, clsPOSPromotionItems> hmPromoItem = new HashMap<String, clsPOSPromotionItems>();
		String clientCode = "",
				posCode = "",
				posDate = "",
				userCode = "",
				posClientCode = "";
		String areaCode = "";
		double dblDiscountAmt = 0.0;
		StringBuilder sqlBuilder = new StringBuilder();
		if (areaCode.equals(""))
		{
			clsSetupHdModel objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
			areaCode=objSetupHdModel.getStrDirectAreaCode();
			/*sqlBuilder.setLength(0);
			sqlBuilder.append("select strDirectAreaCode from tblsetup where (strPOSCode='" + posCode + "'  OR strPOSCode='All') and strClientCode='" + clientCode + "'");
			List listAreCode = objBaseServiceImpl.funGetList(sqlBuilder, "sql");
			if (listAreCode.size() > 0)
			{
				for (int cnt = 0; cnt < listAreCode.size(); cnt++)
				{
					Object obj = (Object) listAreCode.get(cnt);
					areaCode = (obj.toString());
				}
			}*/
		}

		Map mapResult = new HashMap();
		String checkPromotion = "N";
		List<clsPOSBillSettlementBean> listofItems = new ArrayList<clsPOSBillSettlementBean>();
		for (int cnt = 0; cnt < listItmeDtl.size(); cnt++)
		{
			Map listItemDtl = (Map) listItmeDtl.get(cnt);
			System.out.println(listItemDtl.get("itemName"));
			JSONObject objRows = new JSONObject();
			if (listItemDtl.get("itemCode") != null)
			{
				clsPOSBillSettlementBean objWebPOSBillSettlementBean = new clsPOSBillSettlementBean();
				objWebPOSBillSettlementBean.setStrItemCode(listItemDtl.get("itemCode").toString());
				objWebPOSBillSettlementBean.setStrItemName(listItemDtl.get("itemName").toString());
				objWebPOSBillSettlementBean.setDblQuantity(Double.parseDouble(listItemDtl.get("quantity").toString()));
				objWebPOSBillSettlementBean.setDblAmount(Double.parseDouble(listItemDtl.get("amount").toString()));
				objWebPOSBillSettlementBean.setStrSubGroupCode(listItemDtl.get("strSubGroupCode").toString());
				objWebPOSBillSettlementBean.setStrGroupCode(listItemDtl.get("strGroupcode").toString());
				objWebPOSBillSettlementBean.setStrItemCode(listItemDtl.get("itemCode").toString());
				listofItems.add(objWebPOSBillSettlementBean);
			}
		}

		Map<String, clsPOSPromotionItems> hmPromoItemDtl = null;
		boolean flgApplyPromoOnBill = false;
		String gActivePromotions = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gActivePromotions");
		String gPopUpToApplyPromotionsOnBill = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gPopUpToApplyPromotionsOnBill");
		if (gActivePromotions.equalsIgnoreCase("Y"))
		{
			/*hmPromoItemDtl = objUtility.funCalculatePromotions("DirectBiller", "", "", new ArrayList(), "", areaCode, "", request, listofItems);
			if (null != hmPromoItemDtl)
			{
				if (hmPromoItemDtl.size() > 0)
				{
					checkPromotion = "Y";
					flgApplyPromoOnBill = true;
				}
			}*/
		}

		Map<String, clsPOSPromotionItems> hmPromoItemsToDisplay = new HashMap<String, clsPOSPromotionItems>();

		List<clsPOSItemDtlForTax> arrListItemDtls = new ArrayList<clsPOSItemDtlForTax>();
		List<clsPOSBillSettlementBean> listOfPromotionItem = new ArrayList<clsPOSBillSettlementBean>();
		Map<String, clsPOSBillSettlementBean> mapPromoItemDisc = new HashMap<String, clsPOSBillSettlementBean>();
		// Make KOT

		for (int cnt = 0; cnt < listItmeDtl.size(); cnt++)
		{
			Map listItemDtl = (Map) listItmeDtl.get(cnt);
			double freeAmount = 0.00;
			String item = listItemDtl.get("itemName").toString().trim();

			double quantity = Double.parseDouble(listItemDtl.get("quantity").toString());
			double amount = Double.parseDouble(listItemDtl.get("amount").toString());
			String groupCode = listItemDtl.get("strGroupcode").toString().trim();
			String subGroupCode = listItemDtl.get("strSubGroupCode").toString().trim();
			int i = item.compareTo(groupCode);
			double rate = Double.parseDouble(listItemDtl.get("rate").toString().trim());
			String itemCode = listItemDtl.get("itemCode").toString();

			clsPOSItemDtlForTax objItemDtlForTax = new clsPOSItemDtlForTax();
			objItemDtlForTax.setItemCode(itemCode);
			objItemDtlForTax.setItemName(item);
			objItemDtlForTax.setAmount(0);
			objItemDtlForTax.setDiscAmt(0);

			if (gActivePromotions.equalsIgnoreCase("Y") && flgApplyPromoOnBill)
			{
				if (null != hmPromoItemDtl)
				{
					if (hmPromoItemDtl.containsKey(itemCode))
					{
						if (null != hmPromoItemDtl.get(itemCode))
						{
							clsPOSPromotionItems objPromoItemsDtl = hmPromoItemDtl.get(itemCode);
							if (objPromoItemsDtl.getPromoType().equals("ItemWise"))
							{
								double freeQty = objPromoItemsDtl.getFreeItemQty();
								if (freeQty > 0)
								{
									freeAmount = freeAmount + (rate * freeQty);
									amount = amount - freeAmount;
									hmPromoItem.put(itemCode, objPromoItemsDtl);
									hmPromoItemsToDisplay.put(itemCode + "!" + item, objPromoItemsDtl);
									hmPromoItemDtl.remove(itemCode);
								}
							}
							else if (objPromoItemsDtl.getPromoType().equals("Discount"))
							{
								double discA = 0;
								double discP = 0;
								if (objPromoItemsDtl.getDiscType().equals("Value"))
								{
									discA = objPromoItemsDtl.getDiscAmt();
									discP = (discA / amount) * 100;
									hmPromoItem.put(itemCode, objPromoItemsDtl);
									hmPromoItemsToDisplay.put(itemCode + "!" + item, objPromoItemsDtl);
									hmPromoItemDtl.remove(itemCode);

									clsPOSBillSettlementBean objItemPromoDiscount = new clsPOSBillSettlementBean();
									objItemPromoDiscount.setStrItemCode(itemCode);
									objItemPromoDiscount.setStrItemName(item);
									objItemPromoDiscount.setDblDiscountAmt(discA);
									objItemPromoDiscount.setDblDiscountPer(discP);
									objItemPromoDiscount.setDblAmount(amount);

									mapPromoItemDisc.put(itemCode, objItemPromoDiscount);
								}
								else
								{
									discP = objPromoItemsDtl.getDiscPer();
									discA = (discP / 100) * amount;
									// hmPromoItem.put(itemCode,
									// objPromoItemsDtl);
									hmPromoItemDtl.remove(itemCode);
									clsPOSBillSettlementBean objItemPromoDiscount = new clsPOSBillSettlementBean();
									objItemPromoDiscount.setStrItemCode(itemCode);
									objItemPromoDiscount.setStrItemName(item);
									objItemPromoDiscount.setDblDiscountAmt(discA);
									objItemPromoDiscount.setDblDiscountPer(discP);
									objItemPromoDiscount.setDblAmount(amount);

									mapPromoItemDisc.put(itemCode, objItemPromoDiscount);
								}
							}
						}
					}
				}
			}

			// temp_Total += amount;
			objItemDtlForTax.setAmount(objItemDtlForTax.getAmount() + amount);
			arrListItemDtls.add(objItemDtlForTax);

			// listItemCode.add(itemCode);
			// hmItemList.put(item, itemCode);

			if (gActivePromotions.equalsIgnoreCase("Y") && flgApplyPromoOnBill)
			{
				double discAmt = 0;
				double discPer = 0;
				if (mapPromoItemDisc.containsKey(itemCode))
				{
					clsPOSBillSettlementBean objItemPromoDiscount = mapPromoItemDisc.get(itemCode);
					// discAmt = objItemPromoDiscount.getDiscountAmt();
					// discPer = objItemPromoDiscount.getDiscountPer();

					objItemPromoDiscount.setDblQuantity(quantity);
					objItemPromoDiscount.setStrSubGroupCode(subGroupCode);
					objItemPromoDiscount.setStrGroupCode(groupCode);
					objItemPromoDiscount.setDblRate(rate);
					listOfPromotionItem.add(objItemPromoDiscount);
					// funFillListForItemRow(item, quantity, amount, itemCode,
					// discAmt, discPer);
					dblDiscountAmt = dblDiscountAmt + discAmt;
					// txtDiscountAmt.setText(String.valueOf(dblDiscountAmt));
					// txtDiscountPer.setText(String.valueOf(discPer));
				}
				else
				{
					clsPOSBillSettlementBean objItemPromoDiscount = new clsPOSBillSettlementBean();
					objItemPromoDiscount.setStrItemCode(itemCode);
					objItemPromoDiscount.setStrItemName(item);
					objItemPromoDiscount.setDblDiscountAmt(discAmt);
					objItemPromoDiscount.setDblDiscountPer(discPer);
					objItemPromoDiscount.setDblAmount(amount);
					objItemPromoDiscount.setDblQuantity(quantity);
					objItemPromoDiscount.setStrSubGroupCode(subGroupCode);
					objItemPromoDiscount.setStrGroupCode(groupCode);
					objItemPromoDiscount.setDblRate(rate);
					listOfPromotionItem.add(objItemPromoDiscount);
					// funFillListForItemRow(item, quantity, amount, itemCode,
					// discAmt, discPer);
					// dblDiscountAmt = dblDiscountAmt + discAmt;
					// txtDiscountAmt.setText(String.valueOf(dblDiscountAmt));
					// txtDiscountPer.setText(String.valueOf(discPer));
				}

				// Iterator<Map.Entry<String, clsWebPOSBillSettlementBean>>
				// itPromoDisc = mapPromoItemDisc.entrySet().iterator();
				// while (clsWebPOSBillSettlementBean listOfPromotionItem)
				// {
				// clsWebPOSBillSettlementBean objItemDtl =
				// itPromoDisc.next().getValue();
				// if (mapPromoItemDisc.containsKey(objItemDtl.getItemCode()))
				// {
				// mapBillDiscDtl.put("ItemWise!" + objItemDtl.getItemName() +
				// "!P", new clsBillDiscountDtl("Promotion Discount", "R01",
				// objItemDtl.getDiscountPercentage(),
				// objItemDtl.getDiscountAmount(), objItemDtl.getAmount()));
				// }
				// }
			}
			else
			{
				checkPromotion = "N";
				clsPOSBillSettlementBean objItemPromoDiscount = new clsPOSBillSettlementBean();
				objItemPromoDiscount.setStrItemCode(itemCode);
				objItemPromoDiscount.setStrItemName(item);
				objItemPromoDiscount.setDblDiscountAmt(0);
				objItemPromoDiscount.setDblDiscountPer(0);
				objItemPromoDiscount.setDblAmount(amount);
				objItemPromoDiscount.setDblQuantity(quantity);
				objItemPromoDiscount.setStrSubGroupCode(subGroupCode);
				objItemPromoDiscount.setStrGroupCode(groupCode);
				objItemPromoDiscount.setDblRate(rate);
				listOfPromotionItem.add(objItemPromoDiscount);
				// funFillListForItemRow(item, quantity, amount, itemCode, 0,
				// 0);
			}
			//
			// Object[] rows =
			// {
			// item, df.format(quantity), df.format(amount)
			// };
			// dm.addRow(rows);
		}

		mapResult.put("listOfPromotionItem", listOfPromotionItem);
		mapResult.put("checkPromotion", checkPromotion);
		mapResult.put("hmPromoItem", hmPromoItem);
		
		return mapResult;
	}

	@RequestMapping(value = "/actionBillSettlementKOT", method = RequestMethod.POST)
	public ModelAndView printBill(@ModelAttribute("command") clsPOSBillSettlementBean objBean, BindingResult result, HttpServletRequest request) throws Exception
	{
		String clientCode = "",
				POSCode = "",
				POSDate = "",
				userCode = "",
				posClientCode = "";
		int totalPAXNo = objBean.getIntPaxNo();
		String tableNo = objBean.getStrTableNo();

		tableNo=request.getParameter("tableNo").toString();
		objBean.setStrTableNo(tableNo);
		clientCode = request.getSession().getAttribute("gClientCode").toString();
		POSCode = request.getSession().getAttribute("gPOSCode").toString();
		POSDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		userCode = request.getSession().getAttribute("gUserCode").toString();
		Map<String, clsPOSPromotionItems> hmPromoItem =new HashMap<>();
		try{
			
			// Map mapPromo=funPromotionCalculate(listItmeDtl, request);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String split = POSDate;
		String billDateTime = split;
		String custCode = "";

		Date dt = new Date();
		String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
		String dateTime = POSDate + " " + currentDateTime.split(" ")[1];

		boolean isBillSeries = false;
		clsSetupHdModel objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,POSCode);
		
		if (objSetupHdModel.getStrEnableBillSeries().equalsIgnoreCase("Y"))
		{
			isBillSeries = true;
		}

		
		//This map is to check complimentary item
		Map<String,clsPOSItemsDtlsInBill> mapToCheckCompItem= new HashMap();
		for (clsPOSItemsDtlsInBill objItem : objBean.getListOfBillItemDtl())
		{
			if(objItem.getDblCompQty()>0)
			{
				mapToCheckCompItem.put(objItem.getItemCode(), objItem);
			}
		}
		/**
		 * Filling KOT item details in a list
		 */
		List<clsPOSKOTItemDtl> listOfWholeKOTItemDtl = new ArrayList<clsPOSKOTItemDtl>();

		StringBuilder sbSql = new StringBuilder();
		sbSql.setLength(0);
		if(request.getSession().getAttribute("WebClientCode")!=null)
		{
			sbSql.append("select strItemCode,upper(strItemName),dblItemQuantity " + " ,dblAmount,strKOTNo,strManualKOTNo,concat(Time(dteDateCreated),''),strCustomerCode " + " ,strCustomerName,strCounterCode,strWaiterNo,strPromoCode,dblRate,strCardNo,concat(tmeOrderProcessing,''),concat(tmeOrderPickup,'') " + ",strWaiterNo " + " from tblitemrtemp " + " where strPosCode='" + POSCode + "' " + " and strTableNo='" + tableNo + "' " + " and strNCKotYN='N' and strClientCode='"+clientCode+"' order by strTableNo ASC");

		}
		else
		{
			sbSql.append("select strItemCode,upper(strItemName),dblItemQuantity " + " ,dblAmount,strKOTNo,strManualKOTNo,concat(Time(dteDateCreated),''),strCustomerCode " + " ,strCustomerName,strCounterCode,strWaiterNo,strPromoCode,dblRate,strCardNo,concat(tmeOrderProcessing,''),concat(tmeOrderPickup,'') " + ",strWaiterNo " + " from tblitemrtemp " + " where strPosCode='" + POSCode + "' " + " and strTableNo='" + tableNo + "' " + " and strNCKotYN='N'  order by strTableNo ASC");

		}
		
		List listItemKOTDtl = objBaseServiceImpl.funGetList(sbSql, "sql");
		if (listItemKOTDtl.size() > 0)
		{
			for (int i = 0; i < listItemKOTDtl.size(); i++)
			{
				
				Object[] arrKOTItem = (Object[]) listItemKOTDtl.get(i);

				String iCode = arrKOTItem[0].toString();
				String iName = arrKOTItem[1].toString();
				double iQty = new Double(arrKOTItem[2].toString());
				double iAmt =  Double.parseDouble(arrKOTItem[3].toString());

				if(mapToCheckCompItem.containsKey(iCode))
				{
					iAmt -= mapToCheckCompItem.get(iCode).getDblCompQty() * mapToCheckCompItem.get(iCode).getRate();
				}

				double rate = Double.parseDouble(arrKOTItem[12].toString());
				String kotNo = arrKOTItem[4].toString();
				String manualKOTNo = arrKOTItem[5].toString();
				billDateTime = split + " " + arrKOTItem[6].toString();
				custCode = arrKOTItem[7].toString();
				String custName = arrKOTItem[8].toString();
				String promoCode = arrKOTItem[11].toString();
				String cardNo = arrKOTItem[13].toString();
				String orderProcessTime = arrKOTItem[14].toString();
				String orderPickupTime = arrKOTItem[15].toString();

				String waiterNo = arrKOTItem[16].toString();

				clsPOSKOTItemDtl objKOTItem = new clsPOSKOTItemDtl();

				objKOTItem.setStrItemCode(iCode);
				objKOTItem.setStrItemName(iName);
				objKOTItem.setDblItemQuantity(iQty);
				objKOTItem.setDblAmount(iAmt);
				objKOTItem.setDblRate(rate);
				objKOTItem.setStrKOTNo(kotNo);
				objKOTItem.setStrManualKOTNo(manualKOTNo);
				objKOTItem.setStrKOTDateTime(billDateTime);
				objKOTItem.setStrCustomerCode(custCode);
				objKOTItem.setStrCustomerName(custName);
				objKOTItem.setStrPromoCode(promoCode);
				objKOTItem.setStrCardNo(cardNo);
				objKOTItem.setStrOrderProcessTime(orderProcessTime);
				objKOTItem.setStrOrderPickupTime(orderPickupTime);
				objKOTItem.setStrWaiterNo(waiterNo);

				listOfWholeKOTItemDtl.add(objKOTItem);
			}
		}

		/**
		 * Bill series code
		 */

		Map<String, List<clsPOSKOTItemDtl>> mapBillSeries = null;
		List<clsPOSBillSeriesBillDtl> listBillSeriesBillDtl = new ArrayList<clsPOSBillSeriesBillDtl>();
		String voucherNo = "";
		if (isBillSeries)
		{
			if ((mapBillSeries = objBillingAPI.funGetBillSeriesList(listOfWholeKOTItemDtl, POSCode,clientCode)).size() > 0)
			{
				if (mapBillSeries.containsKey("NoBillSeries"))
				{
					result.addError(new ObjectError("BillSeries", "Please Create Bill Series"));

					objBillingAPI.funUpdateTableStatus(tableNo, "Occupied",clientCode);

					return new ModelAndView("/frmMenuItem.html");
				}
				// to calculate PAX per bill if there is a bill series or bill splited
				Map<Integer, Integer> mapPAXPerBill = objBillingAPI.funGetPAXPerBill(totalPAXNo, mapBillSeries.size());

				Iterator<Map.Entry<String, List<clsPOSKOTItemDtl>>> billSeriesIt = mapBillSeries.entrySet().iterator();
				int billCount = 0;
				while (billSeriesIt.hasNext())
				{
					Map.Entry<String, List<clsPOSKOTItemDtl>> billSeriesEntry = billSeriesIt.next();
					String key = billSeriesEntry.getKey();
					List<clsPOSKOTItemDtl> listOfItemsBillSeriesWise = billSeriesEntry.getValue();

					int intBillSeriesPaxNo = 0;
					if (mapPAXPerBill.containsKey(billCount))
					{
						intBillSeriesPaxNo = mapPAXPerBill.get(billCount);
					}

					String billSeriesBillNo = objBillingAPI.funGetBillSeriesBillNo(key, POSCode,clientCode);

					/* To save billseries bill */
					objBillingAPI.funSaveBill(isBillSeries, key, listBillSeriesBillDtl, billSeriesBillNo, listOfItemsBillSeriesWise, objBean, request, hmPromoItem);

					billCount++;
				}

				// save bill series bill detail
				for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
				{
					clsPOSBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
					String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
					double grandTotal = objBillSeriesBillDtl.getDblGrandTotal();

					String sqlInsertBillSeriesDtl = "insert into tblbillseriesbilldtl " + "(strPOSCode,strBillSeries,strHdBillNo,strDtlBillNos,dblGrandTotal,strClientCode,strDataPostFlag" + ",strUserCreated,dteCreatedDate,strUserEdited,dteEditedDate,dteBillDate) " + "values ('" + POSCode + "','" + objBillSeriesBillDtl.getStrBillSeries() + "'" + ",'" + hdBillNo + "','" + objBillingAPI.funGetBillSeriesDtlBillNos(listBillSeriesBillDtl, hdBillNo) + "'" + ",'" + grandTotal + "'" + ",'" + clientCode + "','N','" + userCode + "'" + ",'" + currentDateTime + "','" + userCode + "'" + ",'" + currentDateTime + "','" + POSDate + "')";
					objBaseService.funExecuteUpdate(sqlInsertBillSeriesDtl, "sql");

					sbSql.setLength(0);
					sbSql.append("select * " + "from tblbillcomplementrydtl a " + "where a.strBillNo='" + hdBillNo + "' " + "and date(a.dteBillDate)='" + POSDate + "' " + "and a.strType='Complimentary' ");
					List listCompli = objBaseServiceImpl.funGetList(sbSql, "sql");
					if (listCompli != null && listCompli.size() > 0)
					{
						String sqlUpdate = "update tblbillseriesbilldtl set dblGrandTotal=0.00 where strHdBillNo='" + hdBillNo + "' " + " and strPOSCode='" + POSCode + "' " + " and date(dteBillDate)='" + POSDate + "' ";
						objBaseService.funExecuteUpdate(sqlUpdate, "sql");
					}

				}

				boolean flagBillForItems = false;
				// clear temp kot table
				if (flagBillForItems)
				{
					// objBillingAPI.funUpdateKOTTempTable();
				}
				else
				{
					objBillingAPI.funClearRTempTable(tableNo, POSCode,clientCode,request);
				}

				for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
				{
					clsPOSBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
					String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
					boolean flgHomeDelPrint = objBillSeriesBillDtl.isFlgHomeDelPrint();

					/* printing bill............... */
					objTextFileGeneration.funGenerateAndPrintBill(hdBillNo, POSCode, clientCode);
				}

			}
		}
		else // No Bill Series
		{
			voucherNo = objBillingAPI.funGenerateBillNo(POSCode,clientCode,request);

			/* To save normal bill */
			objBillingAPI.funSaveBill(isBillSeries, "", listBillSeriesBillDtl, voucherNo, listOfWholeKOTItemDtl, objBean, request, hmPromoItem);

			boolean flagBillForItems = false;
			// clear temp kot table
			if (flagBillForItems)
			{
				// objBillingAPI.funUpdateKOTTempTable();
			}
			else
			{
				objBillingAPI.funClearRTempTable(tableNo, POSCode,clientCode,request);
			}

			/* printing bill............... */
		//	objTextFileGeneration.funGenerateAndPrintBill(voucherNo, POSCode, clientCode);
		}

		return new ModelAndView("redirect:/frmConfirmOrder.html?saddr=1&voucherNoForMakeBill="+voucherNo+"&OperationMakeBillType=MakeBillType");
			
		
	}


	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/showKOTfile", method = RequestMethod.GET)
	public void printPDFResource1(HttpServletResponse response, HttpServletRequest request)
	{ //String tableNo,String kotNo,String costCenterCode,String costCenterName,String areaCode
		//funCreateTempFolder();
		
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String posCode = request.getSession().getAttribute("loginPOS").toString();
		String userCode = request.getSession().getAttribute("gUserCode").toString();
		
		String tableNo = request.getParameter("tableNo").toString();
		String kotNo = request.getParameter("kotNo").toString();
		String areaCode = request.getParameter("areaCode").toString();
		String Reprint = "",NCKotYN = "N",labelOnKOT = "KOT";
		
		List<clsPOSBillDtl> listCost=funFillRGridData(request);
		StringBuilder sql = new StringBuilder();
		try
		{
			int cntprint=0;
			JasperPrint listJPrint=null;
			for(clsPOSBillDtl objPOSBillDtl:listCost){

				String costCenterCode=objPOSBillDtl.getStrItemCode();
				String costCenterName=objPOSBillDtl.getStrItemName();
				areaCode=objPOSBillDtl.getStrArea();
				
				String imagePath = servletContext.getRealPath("/WEB-INF/images/company_Logo.png");
				HashMap hm = new HashMap();
				List<List<clsPOSBillDtl>> listData = new ArrayList<>();

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

				List listPos = objMasterService.funFillPOSCombo(clientCode);
				for (int cnt = 0; cnt < listPos.size(); cnt++)
				{
					Object obj = listPos.get(cnt);
					if (posCode.equals(Array.get(obj, 0).toString()))
					{
						hm.put("POS", Array.get(obj, 1).toString());
						break;
					}
				}
				hm.put("costCenter", costCenterName);
				hm.put("CounterName", "");

				String tableName = "";
				int pax = 0;
				sql.setLength(0);
				sql.append("select strTableName,intPaxNo " + " from tbltablemaster " + " where strTableNo='" + tableNo + "' and strOperational='Y' and strClientCode='"+clientCode+"' ");
				List list = objBaseService.funGetList(sql, "sql");
				if (list.size() > 0)
				{
					for (int i = 0; i < list.size(); i++)
					{
						Object[] obj = (Object[]) list.get(i);
						tableName = obj[0].toString();
						pax = Integer.parseInt(obj[1].toString());
					}

				}
				String gAreaWisePricing = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gAreaWisePricing");
				List<clsPOSBillDtl> listOfKOTDetail = new ArrayList<>();
				sql.setLength(0);
				if (gAreaWisePricing.equals("Y"))
				{
					sql.append("select LEFT(a.strItemCode,7),b.strItemName,a.dblItemQuantity,a.strKOTNo,a.strSerialNo,d.strShortName " + " from tblitemrtemp a,tblmenuitempricingdtl b,tblprintersetup c,tblitemmaster d " + " where a.strTableNo='" + tableNo + "' and a.strKOTNo='" + kotNo + "' and b.strCostCenterCode=c.strCostCenterCode " + " and b.strCostCenterCode='" + costCenterCode + "'and a.strItemCode=d.strItemCode " + " and (b.strPOSCode='" + posCode + "'or b.strPOSCode='All') " + " and (b.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo='" + tableNo + "' )) " + " and LEFT(a.strItemCode,7)=b.strItemCode and b.strHourlyPricing='No' "
							+ " and a.strClientCode=b.strClientCode and a.strClientCode=c.strClientCode  and a.strClientCode='"+clientCode+"' and a.strClientCode=d.strClientCode order by a.strSerialNo ");
				}
				else
				{
					sql.append("select LEFT(a.strItemCode,7),b.strItemName,a.dblItemQuantity,a.strKOTNo,a.strSerialNo,d.strShortName "
							+ " from tblitemrtemp a,tblmenuitempricingdtl b,tblprintersetup c,tblitemmaster d "
							+ " where a.strTableNo='" + tableNo + "' and a.strKOTNo='" + kotNo + "' and b.strCostCenterCode=c.strCostCenterCode "
							+ " and b.strCostCenterCode='" + costCenterCode + "' and a.strItemCode=d.strItemCode " + " and (b.strPOSCode='" + posCode + "' or b.strPOSCode='All') " + " and (b.strAreaCode IN (SELECT strAreaCode FROM tbltablemaster where strTableNo='" + tableNo + "' ) " + " OR b.strAreaCode ='" + areaCode + "') " + " and LEFT(a.strItemCode,7)=b.strItemCode and b.strHourlyPricing='No' "
							+ " and a.strClientCode='"+clientCode+"' and a.strClientCode=b.strClientCode and a.strClientCode=c.strClientCode and a.strClientCode=d.strClientCode "
							+ " order by a.strSerialNo ");
					// + " group by a.strKOTNo,a.strItemCode order by a.strSerialNo ");
				}
				// System.out.println(sqlKOTItems);

				hm.put("KOTType", "DINE");
				hm.put("KOT", kotNo);
				hm.put("tableNo", tableName);
				if (clientCode.equals("124.001"))
				{
					hm.put("124.001", tableName);
				}
				hm.put("PAX", String.valueOf(pax));

				StringBuilder sqlQuery = new StringBuilder();
				sqlQuery.append("select b.strWaiterNo,ifnull(b.strWShortName,''),a.dteDateCreated from tblitemrtemp a left outer join tblwaitermaster b on a.strWaiterNo=b.strWaiterNo" + " where a.strKOTNo='" + kotNo + "'  and a.strTableNo='" + tableNo + "'"
						+ " and a.strClientCode=b.strClientCode and a.strClientCode='"+clientCode+"' group by a.strKOTNo ;");

				List listDtl = objBaseService.funGetList(sqlQuery, "sql");
				if (listDtl.size() > 0)
				{
					for (int i = 0; i < listDtl.size(); i++)
					{
						Object[] obj = (Object[]) listDtl.get(i);
						hm.put("waiterName", obj[1].toString());
						hm.put("DATE_TIME", obj[2].toString());
					}
				}

				InetAddress ipAddress = InetAddress.getLocalHost();
				String hostName = ipAddress.getHostName();
				hm.put("KOT From", hostName);
				hm.put("kotByUser", userCode);

				list = objBaseService.funGetList(sql, "sql");
				if (list.size() > 0)
				{
					for (int i = 0; i < list.size(); i++)
					{
						Object[] obj = (Object[]) list.get(i);
						String itemName = obj[1].toString();
						/*
						 * if (clsGlobalVarClass.gPrintShortNameOnKOT &&
						 * !rs_KOT_Items.getString(6).trim().isEmpty()) { itemName =
						 * rs_KOT_Items.getString(6); }
						 */

						clsPOSBillDtl objBillDtl = new clsPOSBillDtl();
						objBillDtl.setDblQuantity(Double.parseDouble(obj[2].toString()));
						objBillDtl.setStrItemName(itemName);
						listOfKOTDetail.add(objBillDtl);
						sqlQuery.setLength(0);
						sqlQuery.append("select a.strItemName,sum(a.dblItemQuantity) from tblitemrtemp a " + " where a.strItemCode like'" + obj[0].toString() + "M%' and a.strKOTNo='" + kotNo + "' " + " and strSerialNo like'" + obj[4].toString() + ".%' "
								+ "and a.strClientCode='"+clientCode+"' group by a.strItemCode,a.strItemName ");
						List listModifier = objBaseService.funGetList(sqlQuery, "sql");
						if (listModifier.size() > 0)
						{
							for (int cnt = 0; cnt < listModifier.size(); cnt++)
							{
								Object[] objModifier = (Object[]) listModifier.get(cnt);
								objBillDtl = new clsPOSBillDtl();
								String modifierName = objModifier[0].toString();
								if (modifierName.startsWith("-->"))
								{
									objBillDtl.setDblQuantity(Double.parseDouble(objModifier[1].toString()));
									objBillDtl.setStrItemName(objModifier[0].toString());
								}
								listOfKOTDetail.add(objBillDtl);
							}
						}
					}

				}

				hm.put("listOfItemDtl", listOfKOTDetail);
				listData.add(listOfKOTDetail);

				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listData);

				// StringBuilder sqlBuilder = new StringBuilder("select
				// a.strProdCode,p.strProdName,a.dblQty,a.dblUnitPrice,a.dblTotalPrice,a.strRemarks,p.strIssueUOM,p.strBinNo"
				// + " from tblreqdtl a, tblproductmaster p where a.strProdCode=p.strProdCode "
				// + "and a.strReqCode='" + reqCode + "' and a.strClientCode='" + clientCode +
				// "' and p.strClientCode='" + clientCode + "' ");

				JasperDesign jd = JRXmlLoader.load(servletContext.getResourceAsStream("/WEB-INF/reports/webpos/rptGenrateKOTJasperReport.jrxml"));
				JasperReport jr = JasperCompileManager.compileReport(jd);

				hm.put("strCompanyName", "Prems");

				//JasperPrint p = JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);
				JasperPrint listJPrint1=null;
				if(listJPrint!=null){
					listJPrint1=JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);
				}else{
					listJPrint = JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);	
				}
				
				if(cntprint>0 && listJPrint!=null){
					List jp1pages = listJPrint1.getPages();
					for (int j = 0; j < jp1pages.size(); j++)
					{
					    JRPrintPage object = (JRPrintPage) jp1pages.get(j);
					    listJPrint.addPage(listJPrint.getPages().size(), object);//addPage(object);
					}	
				}
				
				cntprint++;
			
				
			}

			/*String filePath = System.getProperty("user.dir") + "/downloads/pdf/";
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, p);
			exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, new FileOutputStream(filePath + fileName)); // your output goes here
			exporter.exportReport();

			Path file = Paths.get(filePath, fileName);
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			if (Files.exists(file))
			{
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			}
			*/
			ServletOutputStream servletOutputStream = response.getOutputStream();
			
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, listJPrint);
			exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,response.getOutputStream()); // your output goes here
			//exporter.setParameter(JRPdfExporterParameter.PAGE_INDEX,1);
			
			//exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, mainJaperPrint);
			exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
			exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
			exporter.exportReport();
			servletOutputStream.flush();
			servletOutputStream.close();
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=kotprint.pdf" );
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// return responseBuilder.build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/funGetCostCenterListForKOT", method = RequestMethod.GET)
	public @ResponseBody List<clsPOSBillDtl> funFillRGridData(HttpServletRequest req)
	{
		String POSCode = req.getSession().getAttribute("loginPOS").toString();
		String tableNo = req.getParameter("tableNo").toString();
		String kotNo = req.getParameter("kotNo").toString();
		String gClientCode = req.getSession().getAttribute("gClientCode").toString();
		StringBuilder sql = new StringBuilder();
		String areaCode = "";
		List<clsPOSBillDtl> listOfKOTDetail = new ArrayList<>();
		try
		{

			String SearchBillNo = "";
			sql.append("select strAreaCode from tblareamaster where strAreaName='All' and strClientCode='"+gClientCode+"'");
			List listDtl = objBaseService.funGetList(sql, "sql");
			if (listDtl.size() > 0)
			{
				for (int i = 0; i < listDtl.size(); i++)
				{
					areaCode = (String) listDtl.get(i);
				}
			}else{
				sql.setLength(0);
				sql.append("select strAreaCode from tbltablemaster where strTableNo='"+tableNo+"' and strClientCode='"+gClientCode+"'");
				listDtl = objBaseService.funGetList(sql, "sql");
				if (listDtl.size() > 0)
				{
					areaCode = (String) listDtl.get(0);
				}	
			}

			sql.setLength(0);
			sql.append("SELECT a.strItemName,e.strCostCenterCode,IFNULL(d.strPrimaryPrinterPort,''),IFNULL(d.strSecondaryPrinterPort,''),e.strCostCenterName,a.strNCKotYN, "
					+ " ifnull(e.strLabelOnKOT,'KOT') strLabelOnKOT ,sum(a.dblPrintQty) FROM tblitemrtemp a "
					+ " LEFT OUTER JOIN tblmenuitempricingdtl c ON a.strItemCode = c.strItemCode and a.strClientCode=c.strClientCode "
					+ " left outer join tblprintersetup d on c.strCostCenterCode=d.strCostCenterCode  and d.strClientCode=c.strClientCode "
					+ " LEFT OUTER JOIN tblcostcentermaster e ON c.strCostCenterCode=e.strCostCenterCode  and e.strClientCode=c.strClientCode "
					+ " WHERE a.strKOTNo='" + kotNo + "' AND a.strTableNo='" + tableNo + "' AND (c.strPOSCode='" + POSCode + "' OR c.strPOSCode='All') "
					+ " AND (c.strAreaCode IN ( SELECT strAreaCode FROM tbltablemaster WHERE strTableNo='" + tableNo + "') OR c.strAreaCode ='" + areaCode + "')"
					+ " And c.strClientCode='"+gClientCode+"' "
					+ " group by d.strCostCenterCode ");
			listDtl = objBaseService.funGetList(sql, "sql");
			if (listDtl.size() > 0)
			{
				for (int i = 0; i < listDtl.size(); i++)
				{
					Object[] obj = (Object[]) listDtl.get(i);
					clsPOSBillDtl objBillDtl = new clsPOSBillDtl();
					objBillDtl.setStrItemCode(obj[1].toString());
					objBillDtl.setStrItemName(obj[4].toString());
					objBillDtl.setStrArea(areaCode);
					listOfKOTDetail.add(objBillDtl);
				}
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return listOfKOTDetail;

	}

	
	
}
