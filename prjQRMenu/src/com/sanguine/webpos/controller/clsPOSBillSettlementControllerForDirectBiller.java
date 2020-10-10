package com.sanguine.webpos.controller;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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






import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sanguine.base.service.clsBaseServiceImpl;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.controller.clsUserController;
import com.sanguine.webpos.bean.clsPOSBillSeriesBillDtl;
import com.sanguine.webpos.bean.clsPOSBillSettlementBean;
import com.sanguine.webpos.bean.clsPOSItemDtlForTax;
import com.sanguine.webpos.bean.clsPOSItemsDtlsInBill;
import com.sanguine.webpos.bean.clsPOSKOTItemDtl;
import com.sanguine.webpos.bean.clsPOSTaxCalculation;
import com.sanguine.webpos.bean.clsPOSTaxCalculationBean;
import com.sanguine.webpos.bean.clsPOSTaxDtlsOnBill;
import com.sanguine.webpos.model.clsMenuItemMasterModel;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.printing.clsKOTGeneration;
import com.sanguine.webpos.printing.clsPOSJasperGenerator;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsPOSSetupUtility;
import com.sanguine.webpos.util.clsPOSTextFileGenerator;
import com.sanguine.webpos.util.clsPOSUtilityController;

@Controller
public class clsPOSBillSettlementControllerForDirectBiller
{

	/*private List listItemCode;
	String clientCode = "",
			posCode = "", posDate = "",
			userCode = "",
			posClientCode = "";*/

	@Autowired
	private clsGlobalFunctions objGlobalFunctions;

	@Autowired
	clsBaseServiceImpl objBaseServiceImpl;
	@Autowired
	clsPOSUtilityController objUtility;

	@Autowired
	clsPOSTextFileGenerator objTextFileGeneration;

	@Autowired
	clsPOSSetupUtility objPOSSetupUtility;

	@Autowired
	clsPOSBillingAPIController objBillingAPI;

	@Autowired
	clsPOSBillSettlementControllerForMakeKOT objMakeKOT;

	@Autowired
	intfBaseService objBaseService;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	clsPOSMasterService objMasterService;

	@Autowired
	clsUserController objUserController;
	
	@Autowired
	clsKOTGeneration objGenerationKOTPrint;
	
	@Autowired
	clsPOSJasperGenerator objBillJasper;
//	JSONArray listReasonCode,
//			listReasonName;


	// for Direct Biller
	@RequestMapping(value = "/frmMenuItem", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		String urlHits = "1",voucherNo="";
		clsPOSBillSettlementBean obBillSettlementBean = new clsPOSBillSettlementBean();
		clsSetupHdModel objSetupHdModel=null;
		String clientCode = "",posCode = "", posDate = "",
				userCode = "",posClientCode = "";
		JSONArray listReasonCode,listReasonName;
		try
		{
			urlHits="1";
			if(request.getParameter("saddr")!=null)
			{
				urlHits = request.getParameter("saddr").toString();

			}
			request.getSession().setAttribute("customerMobile", ""); // mobile no
			model.put("voucherNo", voucherNo);
			voucherNo=request.getParameter("voucherNo");
			if(voucherNo!=null&&!voucherNo.isEmpty()){
				model.put("voucherNo", voucherNo);
			}
			model.put("operationMode", "");
			model.put("tableOp", "");
            
			String custCode="";
           if(request.getParameter("CustCode")!=null)        	   
           {
        	   custCode=request.getParameter("CustCode").toString();
           }
			
           model.put("custCode", custCode);
		   String OperationType=request.getParameter("OperationType");
			if(OperationType!=null&&!OperationType.isEmpty()){
				
				if(OperationType.equalsIgnoreCase("Take Away") ||OperationType.equalsIgnoreCase("Home Delivery"))
				{
					model.put("operationMode", OperationType);
					model.put("tableOp", "");

				}
				/*else
				{
					String[] TableNo=OperationType.split("!");
					model.put("operationMode", "Dine In");
					model.put("tableOp", TableNo[1]);
				}*/
			}

			if(request.getSession().getAttribute("tableNo")!=null && OperationType==null)
			{
				model.put("operationMode", "Dine In");
				model.put("tableOp", request.getSession().getAttribute("tableNo").toString());
				//request.getSession().removeAttribute("tableNo");
			}
			clientCode = request.getSession().getAttribute("gClientCode").toString();
			posClientCode = request.getSession().getAttribute("gPOSCode").toString();
			posCode = request.getSession().getAttribute("gPOSCode").toString();
			posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
			userCode = request.getSession().getAttribute("gUserCode").toString();

			objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
			
			// //////////////// Direct biller Tab Data

			String gAreaWisePricing=objSetupHdModel.getStrAreaWisePricing();
			String gDirectAreaCode=objSetupHdModel.getStrDirectAreaCode();
			
			JSONObject jObj = objBillingAPI.funGetItemPricingDtl(clientCode, posDate, posCode,gAreaWisePricing,gDirectAreaCode,"");

			JSONArray jsonArrForDirectBillerMenuItemPricing = (JSONArray) jObj.get("MenuItemPricingDtl");
			obBillSettlementBean.setJsonArrForDirectBillerMenuItemPricing(jsonArrForDirectBillerMenuItemPricing);

			jObj = objBillingAPI.funGetMenuHeads(posCode, userCode, clientCode, request);
			JSONArray jsonArrForDirectBillerMenuHeads = (JSONArray) jObj.get("MenuHeads");
			obBillSettlementBean.setJsonArrForMenuHeads(jsonArrForDirectBillerMenuHeads);
			obBillSettlementBean.setJsonArrForDirectBillerMenuHeads(jsonArrForDirectBillerMenuHeads);


		    //Area List
			     
			model.put("gPOSCode", posCode);
			model.put("gClientCode", clientCode);
			model.put("areaList", "");
			model.put("urlHits", urlHits);
			model.put("billNo", "");
			
			model.put("billDate", posDate.split("-")[2] + "-" + posDate.split("-")[1] + "-" + posDate.split("-")[0]);
			model.put("gCustAddressSelectionForBill", objSetupHdModel.getStrCustAddressSelectionForBill());
			model.put("gCMSIntegrationYN",objSetupHdModel.getStrCMSIntegrationYN());
			model.put("gCRMInterface", objSetupHdModel.getStrCRMInterface());			
			model.put("gItemQtyNumpad", false);
			model.put("roundoff",objSetupHdModel.getDblRoundOff());
			if(objSetupHdModel.getStrItemQtyNumpad().equalsIgnoreCase("Y")){
				model.put("gItemQtyNumpad", true);	
			}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			urlHits = "1";
		}
		
		// ///////////Bill Settlement tab ////////////////////////////////////

		String usertype = request.getSession().getAttribute("gUserType").toString();
		boolean isSuperUser = false;
		if (usertype.equalsIgnoreCase("yes"))
		{
			isSuperUser = true;
		}
		else
		{
			isSuperUser = false;
		}

		
		String operationFrom = "directBiller";
		model.put("operationFrom", operationFrom);
		
		String formToBeOpen="Billing";
		model.put("formToBeOpen", formToBeOpen);
		
		
		model.put("billDate", posDate);


		model.put("gCMSIntegrationYN", objSetupHdModel.getStrCMSIntegrationYN());
		model.put("gCRMInterface", objSetupHdModel.getStrCRMInterface());// clsPOSGlobalFunctionsController.hmPOSSetupValues.get("CRMInterface"));
		//String gPopUpToApplyPromotionsOnBill = objPOSSetupUtility.funGetParameterValuePOSWise(clientCode, posCode, "gPopUpToApplyPromotionsOnBill");
		model.put("gPopUpToApplyPromotionsOnBill", objSetupHdModel.getStrPopUpToApplyPromotionsOnBill()); //gPopUpToApplyPromotionsOnBill);
		model.put("gCreditCardSlipNo",objSetupHdModel.getStrCreditCardSlipNoCompulsoryYN());//.getStr clsPOSGlobalFunctionsController.hmPOSSetupValues.get("CreditCardSlipNoCompulsoryYN"));
		model.put("gCreditCardExpiryDate", objSetupHdModel.getStrCreditCardExpiryDateCompulsoryYN());// clsPOSGlobalFunctionsController.hmPOSSetupValues.get("CreditCardExpiryDateCompulsoryYN"));
		model.put("gSkipPax",objSetupHdModel.getStrSkipPax());// clsPOSGlobalFunctionsController.hmPOSSetupValues.get("SkipPax"));
		model.put("gSkipWaiter",objSetupHdModel.getStrSkipWaiter());//CreditCard clsPOSGlobalFunctionsController.hmPOSSetupValues.get("SkipWaiter"));
        model.put("gEnableSettleBtnForDirectBiller",objSetupHdModel.getStrSettleBtnForDirectBillerBill());
//		funLoadAllReasonMasterData(request);
		
		
		/*model.put("listReasonCode", listReasonCode);
		model.put("listReasonName", listReasonName);
*/
		double gMaxDiscount = objSetupHdModel.getDblMaxDiscount();
		String gApplyDiscountOn = objSetupHdModel.getStrApplyDiscountOn();
		return new ModelAndView("frmMenuItem", "command", obBillSettlementBean);

	}

	public void funLoadAllReasonMasterData(HttpServletRequest request)
	{
		Map<String, String> mapModBill = new HashMap<String, String>();
		Map<String, String> mapComplementry = new HashMap<String, String>();
		Map<String, String> mapDiscount = new HashMap<String, String>();
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		JSONArray listReasonCode,listReasonName;
		
		JSONObject jObj = objBillingAPI.funLoadAllReasonMasterData(clientCode);
		if (jObj != null)
		{
			JSONObject JObjBill = new JSONObject();
			// JSONObject JObjComplementry=new JSONObject();
			// JSONObject JObjDiscount=new JSONObject();
			JSONArray jArr = new JSONArray();

			jArr = (JSONArray) jObj.get("ModifyBill");
			if (jArr != null)
			{
				for (int i = 0; i < jArr.size(); i++)
				{
					JObjBill = (JSONObject) jArr.get(i);
					mapModBill.put(JObjBill.get("strReasonCode").toString(), JObjBill.get("strReasonName").toString());
				}
			}

			jArr = new JSONArray();
			jArr = (JSONArray) jObj.get("Complementry");
			if (jArr != null)
			{

				for (int i = 0; i < jArr.size(); i++)
				{
					JObjBill = (JSONObject) jArr.get(i);
					mapComplementry.put(JObjBill.get("strReasonCode").toString(), JObjBill.get("strReasonName").toString());
				}
			}
			jArr = new JSONArray();
			jArr = (JSONArray) jObj.get("Discount");
			if (jArr != null)
			{

				for (int i = 0; i < jArr.size(); i++)
				{
					JObjBill = (JSONObject) jArr.get(i);
					mapDiscount.put(JObjBill.get("strReasonCode").toString(), JObjBill.get("strReasonName").toString());
				}
			}
			listReasonCode = new JSONArray();
			listReasonName = new JSONArray();

			jArr = new JSONArray();
			jArr = (JSONArray) jObj.get("AllReason");
			if (jArr != null)
			{

				for (int i = 0; i < jArr.size(); i++)
				{
					JObjBill = (JSONObject) jArr.get(i);
					listReasonCode.add(JObjBill.get("strReasonCode"));
					listReasonName.add(JObjBill.get("strReasonName"));
				}
			}
		}
	}

	public List funLoadItemsGroupSubGroupData()
	{
		List listDiscountCombo = new ArrayList<List>();
		try
		{

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return listDiscountCombo;
	}

	public JSONObject funFillGroupSubGroupList(ArrayList<String> arrListItemCode)
	{
		JSONObject jsonAllListsData = new JSONObject();
		StringBuilder sb = new StringBuilder();
		try
		{
			StringBuilder sqlBuilder = new StringBuilder();
			List listSubGroupName = new ArrayList<>();
			List listSubGroupCode = new ArrayList<>();
			List listGroupName = new ArrayList<>();
			List listGroupCode = new ArrayList<>();
			listSubGroupName.add("--select--");
			listSubGroupCode.add("--select--");
			listGroupName.add("--select--");
			listGroupCode.add("--select--");
			boolean first = true;
			for (String test : arrListItemCode)
			{
				if (first)
				{
					sb.append("'").append(test).append("");
					first = false;
				}
				else
				{
					sb.append("','").append(test).append("");
				}
			}
			// String t1 = sb.toString()+"'";
			sb.append("'");
			if (sb.toString().trim().length() > 1)
			{

				sqlBuilder.setLength(0);
				sqlBuilder.append("select a.strSubGroupCode,b.strSubGroupName,c.strGroupCode,c.strGroupName " + " from tblitemmaster a , tblsubgrouphd b,tblgrouphd c" + " where a.strItemCode IN (" + sb.toString() + ") and a.strDiscountApply='Y' " + " and a.strSubGroupCode=b.strSubGroupCode and b.strGroupCode=c.strGroupCode;");

				List list = objBaseServiceImpl.funGetList(sqlBuilder, "sql");
				if (list.size() > 0)
				{
					for (int i = 0; i < list.size(); i++)
					{
						Object[] ob = (Object[]) list.get(i);
						if (!listSubGroupCode.contains(ob[0].toString()))
						{
							listSubGroupCode.add(ob[0].toString());
							listSubGroupName.add(ob[1].toString());
						}
						if (!listGroupCode.contains(ob[2].toString()))
						{
							listGroupCode.add(ob[2].toString());
							listGroupName.add(ob[3].toString());
						}
					}
				}

			}

			Gson gson = new Gson();
			Type type = new TypeToken<List<String>>()
			{
			}.getType();
			String strSubGroupCode = gson.toJson(listSubGroupCode, type);
			String strSubGroupName = gson.toJson(listSubGroupName, type);
			String strGroupCode = gson.toJson(listGroupCode, type);
			String strGroupName = gson.toJson(listGroupName, type);

			jsonAllListsData.put("listSubGroupCode", strSubGroupCode);
			jsonAllListsData.put("listSubGroupName", strSubGroupName);
			jsonAllListsData.put("listGroupCode", strGroupCode);
			jsonAllListsData.put("listGroupName", strGroupName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sb = null;
		}
		return jsonAllListsData;
	}

	// @RequestMapping(value = "/funCalculateTaxInSettlement", method =
	// RequestMethod.POST)
	@RequestMapping(value = "/funCalculateTaxInSettlement", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	private @ResponseBody List<clsPOSTaxDtlsOnBill> funCalculateTax(@RequestBody List listBillItem, HttpServletRequest request)
	{
		List<clsPOSTaxDtlsOnBill> listTaxDtlOnBill = new ArrayList<clsPOSTaxDtlsOnBill>();
//HomeDelivery DineIn TakeAway
		String operationTypeForTax = "DineIn";
		if(request.getParameter("operationTypeForTax")!=null)
		{
			operationTypeForTax = request.getParameter("operationTypeForTax");
		}
		String clientCode = "",posCode = "", posDate = "",
				userCode = "",posClientCode = "";

		// customerType=objDirectBillerBean.getStrCustomerType();
		
		clientCode = request.getSession().getAttribute("gClientCode").toString();
		posClientCode = request.getSession().getAttribute("gClientCode").toString();
		posCode = request.getSession().getAttribute("gPOSCode").toString();
		posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		userCode = request.getSession().getAttribute("gUserCode").toString();
		clsSetupHdModel  objSetupHdModel=null;
		try
		{
			objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
		}
		catch(Exception ex)
		{
		  ex.printStackTrace();
		}
		
		
        DecimalFormat deciformat=objUtility.funGetGlobalDecimalFormatter(objSetupHdModel.getDblNoOfDecimalPlace());
		JSONObject jsonSelectedItemDtl = new JSONObject();
		JSONArray jArrClass = new JSONArray();
		for (int cnt = 0; cnt < listBillItem.size(); cnt++)
		{
			Map listItemDtl = (Map) listBillItem.get(cnt);
			System.out.println(listItemDtl.get("itemName"));
			JSONObject objRows = new JSONObject();
			if (listItemDtl.get("itemCode") != null)
			{
				objRows.put("strPOSCode", posCode);
				objRows.put("strItemCode", listItemDtl.get("itemCode"));
				objRows.put("strItemName", listItemDtl.get("itemName"));
				objRows.put("dblItemQuantity", listItemDtl.get("quantity"));
				objRows.put("dblAmount", listItemDtl.get("amount"));
				objRows.put("dblDiscAmt", listItemDtl.get("discountAmt"));
				objRows.put("strClientCode", clientCode);
				objRows.put("OperationType", operationTypeForTax);// operationTypeFor
																	// Tax
				objRows.put("AreaCode", "");
				objRows.put("POSDate", posDate);

				jArrClass.add(objRows);
			}

		}
		jsonSelectedItemDtl.put("TaxDtl", jArrClass);

		// call WebService

		JSONObject jObj = funCalculateTax(jsonSelectedItemDtl);
		JSONArray jArrTaxList = (JSONArray) jObj.get("listOfTax");
		String totalTaxAmt = jObj.get("totalTaxAmt").toString();
		clsPOSTaxDtlsOnBill objTaxDtl;
		JSONObject jsonTax = new JSONObject();
		for (int i = 0; i < jArrTaxList.size(); i++)
		{
			objTaxDtl = new clsPOSTaxDtlsOnBill();
			jsonTax = (JSONObject) jArrTaxList.get(i);
			objTaxDtl.setTaxName(jsonTax.get("TaxName").toString());
			objTaxDtl.setTaxAmount(Double.parseDouble(deciformat.format(Double.parseDouble(jsonTax.get("TaxAmt").toString()))));
			objTaxDtl.setTaxCode(jsonTax.get("taxCode").toString());
			objTaxDtl.setTaxCalculationType(jsonTax.get("taxCalculationType").toString());
			objTaxDtl.setTaxableAmount(Double.parseDouble(deciformat.format(Double.parseDouble(jsonTax.get("taxableAmount").toString()))));

			listTaxDtlOnBill.add(objTaxDtl);

		}

		return listTaxDtlOnBill;
	}

	public JSONObject funCalculateTax(JSONObject objKOTTaxData)
	{

		String taxAmt = "";
		double subTotalForTax = 0;
		double taxAmount = 0.0;
		JSONObject jsTaxDtl = new JSONObject();
		try
		{

			String posCode = "",
					areaCode = "",
					operationType = "",
					clientCode = "";

			List<clsPOSItemDtlForTax> arrListItemDtls = new ArrayList<clsPOSItemDtlForTax>();
			JSONArray mJsonArray = (JSONArray) objKOTTaxData.get("TaxDtl");
			String sql = "";
			String posDate = "";
			ResultSet rs;
			JSONObject mJsonObject = new JSONObject();
			for (int i = 0; i < mJsonArray.size(); i++)
			{
				clsPOSItemDtlForTax objItemDtl = new clsPOSItemDtlForTax();
				mJsonObject = (JSONObject) mJsonArray.get(i);
				String itemName = mJsonObject.get("strItemName").toString();
				String itemCode = mJsonObject.get("strItemCode").toString();
				double discAmt = Double.parseDouble(mJsonObject.get("dblDiscAmt").toString());
				
				
				System.out.println(itemName);
				double amt = Double.parseDouble(mJsonObject.get("dblAmount").toString());
				operationType = mJsonObject.get("OperationType").toString();
				posCode = mJsonObject.get("strPOSCode").toString();
				areaCode = mJsonObject.get("AreaCode").toString();
				posDate = mJsonObject.get("POSDate").toString();
				clientCode = mJsonObject.get("strClientCode").toString();
				StringBuilder sqlBuilder = new StringBuilder();
				if (areaCode.equals(""))
				{
					sqlBuilder.setLength(0);
					sqlBuilder.append("select strDirectAreaCode from tblsetup where (strPOSCode='" + posCode + "'  OR strPOSCode='All') and strClientCode='" + clientCode + "'");
					List listAreCode = objBaseServiceImpl.funGetList(sqlBuilder, "sql");
					if (listAreCode.size() > 0)
					{
						for (int cnt = 0; cnt < listAreCode.size(); cnt++)
						{
							Object obj = (Object) listAreCode.get(cnt);
							areaCode = (obj.toString());
						}
					}
				}

				objItemDtl.setItemCode(itemCode);
				objItemDtl.setItemName(itemName);
				objItemDtl.setAmount(amt);
				objItemDtl.setDiscAmt(discAmt);
				
				arrListItemDtls.add(objItemDtl);
				
				subTotalForTax += amt;
				

			}

			Date dt = new Date();
			String date = (dt.getYear() + 1900) + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
			clsPOSTaxCalculation objTaxCalculation = new clsPOSTaxCalculation();
//funCalculateTax(List<clsPOSItemDetailFrTaxBean> arrListItemDtl, String POSCode, String dtPOSDate, String billAreaCode, String operationTypeForTax, double subTotal, double discountAmt, String transType, String settlementCode, String taxOnSP,String strSCTaxForRemove,String strClientCode) throws Exception
			List<clsPOSTaxCalculationBean> arrListTaxDtl = objUtility.funCalculateTax(arrListItemDtls, posCode, posDate, areaCode, operationType, subTotalForTax, 0.0, "","S01","Sales","N",clientCode);

			JSONArray jAyyTaxList = new JSONArray();
			JSONObject jsTax;
			for (int cnt = 0; cnt < arrListTaxDtl.size(); cnt++)
			{
				jsTax = new JSONObject();
				clsPOSTaxCalculationBean obj = (clsPOSTaxCalculationBean) arrListTaxDtl.get(cnt);
				System.out.println("Tax Dtl= " + obj.getTaxCode() + "\t" + obj.getTaxName() + "\t" + obj.getTaxAmount());
				taxAmount += obj.getTaxAmount();
				taxAmt = String.valueOf(taxAmount);
				jsTax.put("TaxName", obj.getTaxName());
				jsTax.put("TaxAmt", obj.getTaxAmount());
				jsTax.put("taxCode", obj.getTaxCode());
				jsTax.put("taxCalculationType", obj.getTaxCalculationType());
				jsTax.put("taxableAmount", obj.getTaxableAmount());

				jAyyTaxList.add(jsTax);
			}
			jsTaxDtl.put("listOfTax", jAyyTaxList);
			jsTaxDtl.put("totalTaxAmt", taxAmt);

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsTaxDtl;// Response.status(201).entity(jsTaxDtl).build();
	}

		@RequestMapping(value = "/funCalculateDeliveryChages", method = RequestMethod.POST)
	public @ResponseBody double funCalclulateDeliveryCharges(@RequestParam("buildingCode") String buildingCode, @RequestParam("gCustomerCode") String gCustomerCode, @RequestParam("totalBillAmount") double totalBillAmount,HttpServletRequest request)
	{
		String clientCode = "",posCode = "";
		clientCode = request.getSession().getAttribute("gClientCode").toString();
		posCode = request.getSession().getAttribute("gPOSCode").toString();
		double deliverycharges = objUtility.funCalculateDeliveryChages(buildingCode, totalBillAmount, gCustomerCode, clientCode, posCode);
		return deliverycharges;
	}

	@RequestMapping(value = "/actionBillSettlement", method = RequestMethod.POST)
	public ModelAndView printBill(@ModelAttribute("command") clsPOSBillSettlementBean objBean, BindingResult result, HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		ModelAndView obMdv=null;
		
		String clientCode = "",
				POSCode = "",
				POSDate = "",
				userCode = "",
				posClientCode = "";

		clientCode = request.getSession().getAttribute("gClientCode").toString();
		POSCode = request.getSession().getAttribute("gPOSCode").toString();
		POSDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		userCode = request.getSession().getAttribute("gUserCode").toString();

		String split = POSDate;
		String billDateTime = split;
		String custCode = "";

		Date dt = new Date();
		String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
		String dateTime = POSDate + " " + currentDateTime.split(" ")[1];

		int totalPAXNo = 0;
		String tableNo = "";

		StringBuilder sbSql = new StringBuilder();
		sbSql.setLength(0);

		boolean isBillSeries = false;
		clsSetupHdModel objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,POSCode);
		if (objSetupHdModel.getStrEnableBillSeries().equalsIgnoreCase("Y"))
		{
			isBillSeries = true;
		}

		/**
		 * Filling KOT item details in a list
		 */
		List<clsPOSKOTItemDtl> listOfWholeKOTItemDtl = new ArrayList<clsPOSKOTItemDtl>();
		for (clsPOSItemsDtlsInBill objDirectBillerItem : objBean.getListOfBillItemDtl())
		{
		   
		   String itemCode = objDirectBillerItem.getItemCode();
		   String itemName = objDirectBillerItem.getItemName();
           if(itemCode!=null && itemName!=null)
           {
        	if (itemName.contains("-->"))
   			{
   				String code[] = itemCode.split("!");
   				itemCode = code[0] + "" + code[1];
   			}

   			clsPOSKOTItemDtl objBillDtl = new clsPOSKOTItemDtl();

   			objBillDtl.setStrItemCode(itemCode);
   			objBillDtl.setStrItemName(itemName);
   			objBillDtl.setStrAdvBookingNo("");
   			objBillDtl.setDblRate(objDirectBillerItem.getRate());
   			objBillDtl.setDblItemQuantity(objDirectBillerItem.getQuantity());
   			objBillDtl.setDblAmount(objDirectBillerItem.getAmount());
   			objBillDtl.setDblTaxAmount(0);
   			objBillDtl.setDteBillDate(POSDate);
   			objBillDtl.setStrKOTNo("");
   			objBillDtl.setStrCounterCode("");
   			objBillDtl.setStrOrderProcessTime("00:00:00");
   			objBillDtl.setStrDataPostFlag("N");
   			objBillDtl.setStrMMSDataPostFlag("N");
   			objBillDtl.setStrManualKOTNo("");
   			boolean tdYN = objDirectBillerItem.isTdhYN();
   			if (tdYN)
   			{
   				objBillDtl.setStrTdhYN("Y");
   			}
   			else
   			{
   				objBillDtl.setStrTdhYN("N");
   			}
   			objBillDtl.setStrPromoCode(objDirectBillerItem.getPromoCode());
   			objBillDtl.setStrCounterCode("");
   			objBillDtl.setStrWaiterNo("");
   			objBillDtl.setStrSequenceNo(objDirectBillerItem.getSeqNo());
   			objBillDtl.setStrOrderPickupTime("00:00:00");

   			objBillDtl.setDblDiscAmt(objDirectBillerItem.getDiscountAmt() * objDirectBillerItem.getQuantity());
   			objBillDtl.setDblDiscPer(objDirectBillerItem.getDiscountPer());

   			listOfWholeKOTItemDtl.add(objBillDtl);

           }
			
		}

		/**
		 * Bill series code
		 */

		Map<String, List<clsPOSKOTItemDtl>> mapBillSeries = null;
		List<clsPOSBillSeriesBillDtl> listBillSeriesBillDtl = new ArrayList<clsPOSBillSeriesBillDtl>();
		String voucherNo ="";
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
					
					Map hmPromoItem = new HashMap<>();
					objBillingAPI.funSaveBill(isBillSeries, key, listBillSeriesBillDtl, billSeriesBillNo, listOfItemsBillSeriesWise, objBean, request, hmPromoItem);

					billCount++;
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

				for (int i = 0; i < listBillSeriesBillDtl.size(); i++)
				{
					clsPOSBillSeriesBillDtl objBillSeriesBillDtl = listBillSeriesBillDtl.get(i);
					String hdBillNo = objBillSeriesBillDtl.getStrHdBillNo();
					boolean flgHomeDelPrint = objBillSeriesBillDtl.isFlgHomeDelPrint();

					/* printing bill............... */
					objGenerationKOTPrint.funKOTGeneration("", "", hdBillNo, "", "DirectBiller", "Y",request,response);
     				objBillJasper.funCallBillPrint(hdBillNo, request, response);
					
				}
//				objGenerationKOTPrint.funKOTGeneration("", "", voucherNo, "", "DirectBiller", "Y",request,response);
//				objBillJasper.funCallBillPrint(voucherNo, request, response);
//				

			}
		}
		else // No Bill Series
		{
			voucherNo = objBillingAPI.funGenerateBillNo(POSCode,clientCode,request);

			/* To save normal bill */
			Map hmPromoItem = new HashMap<>();
			objBillingAPI.funSaveBill(isBillSeries, "", listBillSeriesBillDtl, voucherNo, listOfWholeKOTItemDtl, objBean, request, hmPromoItem);
/*            if(objBean.getIsSettleBill().equalsIgnoreCase("Y"))
            {
    			StringBuilder hqlBuilder=new StringBuilder();
    			hqlBuilder.append(" from clsBillHdModel where strBillNo='" + voucherNo + "' and strClientCode='" + clientCode + "' and dtBillDate='" + POSDate + "'  ");
    			List<clsBillHdModel> listOfHd = objBaseService.funGetList(hqlBuilder, "hql");

    			clsBillHdModel objBillHdModel = listOfHd.get(0);
    			List<clsBillDtlModel> listBillDtlModel = objBillHdModel.getListBillDtlModel();

    			objBillHdModel.setStrTransactionType(objBillHdModel.getStrTransactionType() + "," + "Settle Bill");

            	List<clsPOSSettlementDtlsOnBill> listObjBillSettlementDtl = objBean.getListSettlementDtlOnBill();

    			List<clsBillSettlementDtlModel> listBillSettlementDtlModel = new ArrayList<clsBillSettlementDtlModel>();

    			boolean isComplementarySettle = false;
    			if (listObjBillSettlementDtl.size() == 1 && listObjBillSettlementDtl.get(0).getStrSettelmentType().equalsIgnoreCase("Complementary"))
    			{
    				isComplementarySettle = true;
    			}
    			String strSettlement="";
    			for (clsPOSSettlementDtlsOnBill objBillSettlementDtl : listObjBillSettlementDtl)
    			{
    				clsBillSettlementDtlModel objSettleModel = new clsBillSettlementDtlModel();

    				if(objBillSettlementDtl.getStrSettelmentCode()!=null && objBillSettlementDtl.getDblPaidAmt()>0){
    					strSettlement=objBillSettlementDtl.getStrSettelmentDesc();
    					objSettleModel.setStrSettlementCode(objBillSettlementDtl.getStrSettelmentCode());
    					if (isComplementarySettle)
    					{
    						objSettleModel.setDblSettlementAmt(0.00);
    						objSettleModel.setDblPaidAmt(0.00);
    						
    						objSettleModel.setDblActualAmt(0.00);
    						objSettleModel.setDblRefundAmt(0.00);
    						
    						objBillHdModel.setDblDeliveryCharges(0.00);
    						objBillHdModel.setDblDiscountAmt(0);
    						objBillHdModel.setDblDiscountPer(0);
    						objBillHdModel.setDblGrandTotal(0);
    						objBillHdModel.setDblRoundOff(0);
    						objBillHdModel.setDblSubTotal(0);
    						objBillHdModel.setDblTaxAmt(0);
    						objBillHdModel.setDblTipAmount(0);
    						
    					}
    					else
    					{
    						objSettleModel.setDblSettlementAmt(objBillSettlementDtl.getDblPaidAmt());
    						objSettleModel.setDblPaidAmt(objBillSettlementDtl.getDblPaidAmt());
    						
    						objSettleModel.setDblActualAmt(objBillSettlementDtl.getDblActualAmt());
    						objSettleModel.setDblRefundAmt(objBillSettlementDtl.getDblRefundAmt());
    					}
    					
    					
    					
    					objSettleModel.setStrExpiryDate("");
    					objSettleModel.setStrCardName("");
    					objSettleModel.setStrRemark("");
    					objSettleModel.setStrCustomerCode("");
                        if(objBillSettlementDtl.getStrSettelmentType().equalsIgnoreCase("Credit"))
                        {
        					objSettleModel.setStrCustomerCode(objGlobalFunctions.funIfNull(objBean.getStrCustomerCode(), "", objBean.getStrCustomerCode()));

                        }
    					
    					objSettleModel.setStrGiftVoucherCode("");
    					objSettleModel.setStrDataPostFlag("");

    					objSettleModel.setStrFolioNo("");
    					objSettleModel.setStrRoomNo("");

    					listBillSettlementDtlModel.add(objSettleModel);

    				}
    				
    			}

    			objBillHdModel.setStrSettelmentMode("");

    			if (listBillSettlementDtlModel != null && listBillSettlementDtlModel.size() == 0)
    			{
    				objBillHdModel.setStrSettelmentMode("");
    			}
    			else if (listBillSettlementDtlModel != null && listBillSettlementDtlModel.size() == 1)
    			{
    				objBillHdModel.setStrSettelmentMode(strSettlement);
    			}
    			else
    			{
    				objBillHdModel.setStrSettelmentMode("MultiSettle");
    			}

    			objBillHdModel.setListBillSettlementDtlModel(listBillSettlementDtlModel);
    			objBillHdModel.setStrCustomerCode(objGlobalFunctions.funIfNull(objBean.getStrCustomerCode(), "", objBean.getStrCustomerCode()));
    			
    			 Save Bill HD 
    			objBaseServiceImpl.funSave(objBillHdModel);
    			objBillingAPI.funUpdateTableStatus(objBillHdModel.getStrTableNo(), "Normal",clientCode);

            }
*/			/* printing bill............... */
		
			
			//	objGenerationKOTPrint.funKOTGeneration("", "", voucherNo, "", "DirectBiller", "Y",request,response);
		//	objBillJasper.funCallBillPrint(voucherNo, request, response);
			
		

		Map<String, Object> model = new HashMap<String, Object>();

		// return new ModelAndView("frmWebPOSBilling","command",obBillSettlementBean);
        if (objBean.getOperationType().equals("HomeDelivery")) {
            obMdv = new ModelAndView("redirect:/frmConfirmOrder.html?saddr=1&operationDirectBillingType=HomeDelivery");
        }
        else {
            obMdv = new ModelAndView("redirect:/frmConfirmOrder.html?saddr=1");
        }
	}
		return obMdv;
	}

	
	
/*	// generate bill no.
	private String funGenerateBillNo()
	{
		String voucherNo = "";
		try
		{
			long code = 0;
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.setLength(0);
			sqlBuilder.append("select strBillNo from tblstorelastbill where strPosCode='" + posCode + "'");
			List listItemDtl = objBaseServiceImpl.funGetList(sqlBuilder, "sql");

			if (listItemDtl != null && listItemDtl.size() > 0)
			{

				Object objItemDtl = (Object) listItemDtl.get(0);

				code = Math.round(Double.parseDouble(objItemDtl.toString()));
				code = code + 1;

				voucherNo = posCode + String.format("%05d", code);
				objBaseServiceImpl.funExecuteUpdate("update tblstorelastbill set strBillNo='" + code + "' where strPosCode='" + posCode + "'", "sql");
			}
			else
			{
				voucherNo = posCode + "00001";
				sqlBuilder.setLength(0);
				objBaseServiceImpl.funExecuteUpdate("insert into tblstorelastbill values('" + posCode + "','1')", "sql");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			return voucherNo;
		}
	}
*/
/*
	private List funSaveBillDiscountDetail(String voucherNo, clsPOSBillSettlementBean objBean, String dateTime)
	{
		List<clsBillDiscDtlModel> listBillDiscDtlModel = new ArrayList<clsBillDiscDtlModel>();
		try
		{
			double totalDiscAmt = 0.00,
					finalDiscPer = 0.00;
			for (clsPOSDiscountDtlsOnBill objBillDiscDtl : objBean.getListDiscountDtlOnBill())
			{
				String discOnType = objBillDiscDtl.getDiscountOnType();
				String discOnValue = objBillDiscDtl.getDiscountOnValue();
				String remark = objBillDiscDtl.getDiscountRemarks();
				String reason = objBillDiscDtl.getDiscountReasonCode();
				double discPer = objBillDiscDtl.getDiscountPer();
				double discAmt = objBillDiscDtl.getDiscountAmt();
				double discOnAmt = objBillDiscDtl.getDiscountOnAmt();

				clsBillDiscDtlModel objDiscModel = new clsBillDiscDtlModel();
				objDiscModel.setStrPOSCode(posCode);
				objDiscModel.setDblDiscAmt(discAmt);
				objDiscModel.setDblDiscPer(discPer);
				objDiscModel.setDblAmount(discOnAmt);
				objDiscModel.setStrDiscOnType(discOnType);
				objDiscModel.setStrDiscOnValue(discOnValue);
				objDiscModel.setDteDateCreated(dateTime);
				objDiscModel.setDteDateEdited(dateTime);
				objDiscModel.setStrUserCreated(userCode);
				objDiscModel.setStrUserEdited(userCode);
				objDiscModel.setStrDiscReasonCode(reason);
				objDiscModel.setStrDiscRemarks(remark);
				objDiscModel.setStrDataPostFlag("N");
				listBillDiscDtlModel.add(objDiscModel);
				totalDiscAmt += discAmt;
			}

			// if (_subTotal == 0.00)
			// {
			// }
			// else
			// {
			// finalDiscPer = (totalDiscAmt / _subTotal) * 100;
			// }
			// dblDiscountAmt = totalDiscAmt;
			// dblDiscountPer = finalDiscPer;
		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
		finally
		{
			return listBillDiscDtlModel;
		}
	}

	private List funInsertBillSettlementDtlTable(List<clsPOSSettlementDtlsOnBill> listObjBillSettlementDtl, String userCode, String dtCurrentDate, String voucherNo) throws Exception
	{
		String sqlDelete = "delete from tblbillsettlementdtl where strBillNo='" + voucherNo + "'";
		objBaseServiceImpl.funExecuteUpdate(sqlDelete, "sql");
		List<clsBillSettlementDtlModel> listBillSettlementDtlModel = new ArrayList<clsBillSettlementDtlModel>();
		for (clsPOSSettlementDtlsOnBill objBillSettlementDtl : listObjBillSettlementDtl)
		{
			clsBillSettlementDtlModel objSettleModel = new clsBillSettlementDtlModel();
			// objSettleModel.setStrBillNo(
			// objBillSettlementDtl.getStrBillNo());
			objSettleModel.setStrSettlementCode(objBillSettlementDtl.getStrSettelmentCode());
			objSettleModel.setDblSettlementAmt(objBillSettlementDtl.getDblSettlementAmt());
			objSettleModel.setDblPaidAmt(objBillSettlementDtl.getDblPaidAmt());
			objSettleModel.setStrExpiryDate("");
			objSettleModel.setStrCardName("");
			objSettleModel.setStrRemark("");
			// objSettleModel.setStrClientCode(objBillSettlementDtl.getStrClientCode());
			objSettleModel.setStrCustomerCode("");
			objSettleModel.setDblActualAmt(objBillSettlementDtl.getDblActualAmt());
			objSettleModel.setDblRefundAmt(objBillSettlementDtl.getDblRefundAmt());
			objSettleModel.setStrGiftVoucherCode("");
			objSettleModel.setStrDataPostFlag("");
			// objSettleModel.setDteBillDate(dtCurrentDate);
			objSettleModel.setStrFolioNo("");
			objSettleModel.setStrRoomNo("");
			listBillSettlementDtlModel.add(objSettleModel);
			// objBaseService.funSave(objSettleModel);

		}
		return listBillSettlementDtlModel;
		// StringBuilder sb1 = new StringBuilder(sqlInsertBillSettlementDtl);
		// int index1 = sb1.lastIndexOf(",");
		// sqlInsertBillSettlementDtl = sb1.delete(index1,
		// sb1.length()).toString();

	}

	public List funInsertIntoPromotion(String voucherNo, clsPOSBillSettlementBean objBean)
	{

		List<clsBillPromotionDtlModel> listBillPromotionDtlModel = new ArrayList<clsBillPromotionDtlModel>();
		for (clsPOSItemsDtlsInBill objBillDtl : objBean.getListOfBillItemDtl())
		{
			double freeQty = 0;
			if (null != hmPromoItem)
			{
				if (null != hmPromoItem.get(objBillDtl.getItemCode()))
				{
					clsPOSPromotionItems objPromoItemDtl = hmPromoItem.get(objBillDtl.getItemCode());
					if (objPromoItemDtl.getPromoType().equals("ItemWise"))
					{
						freeQty = objPromoItemDtl.getFreeItemQty();
						double freeAmt = freeQty * objBillDtl.getRate();

						clsBillPromotionDtlModel objPromortion = new clsBillPromotionDtlModel();
						objPromortion.setStrItemCode(objBillDtl.getItemCode());
						objPromortion.setStrPromotionCode(objPromoItemDtl.getPromoCode());
						objPromortion.setDblAmount(freeAmt);
						objPromortion.setDblDiscountAmt(0);
						objPromortion.setDblDiscountPer(0);
						objPromortion.setDblQuantity(freeQty);
						objPromortion.setDblRate(objBillDtl.getRate());
						objPromortion.setStrDataPostFlag("N");
						objPromortion.setStrPromoType(objPromoItemDtl.getPromoType());
						objPromortion.setStrPromotionCode(objPromoItemDtl.getPromoCode());

						listBillPromotionDtlModel.add(objPromortion);

						hmPromoItem.remove(objBillDtl.getItemCode());
					}
					else if (objPromoItemDtl.getPromoType().equals("Discount"))
					{
						if (objPromoItemDtl.getDiscType().equals("Value"))
						{
							double amount = freeQty * objBillDtl.getRate();
							double discAmt = objPromoItemDtl.getDiscAmt();

							clsBillPromotionDtlModel objPromortion = new clsBillPromotionDtlModel();
							objPromortion.setStrItemCode(objBillDtl.getItemCode());
							objPromortion.setStrPromotionCode("");
							objPromortion.setDblAmount(amount);
							objPromortion.setDblDiscountAmt(discAmt);
							objPromortion.setDblDiscountPer(objPromoItemDtl.getDiscPer());
							objPromortion.setDblQuantity(0);
							objPromortion.setDblRate(objBillDtl.getRate());
							objPromortion.setStrDataPostFlag("N");
							objPromortion.setStrPromoType(objPromoItemDtl.getPromoType());
							objPromortion.setStrPromotionCode(objPromoItemDtl.getPromoCode());

							listBillPromotionDtlModel.add(objPromortion);

							hmPromoItem.remove(objBillDtl.getItemCode());
						}
						else
						{
							double totalAmt = objBillDtl.getQuantity() * objBillDtl.getRate();
							double discAmt = totalAmt - (totalAmt * (objPromoItemDtl.getDiscPer() / 100));

							clsBillPromotionDtlModel objPromortion = new clsBillPromotionDtlModel();
							objPromortion.setStrItemCode(objBillDtl.getItemCode());
							objPromortion.setStrPromotionCode("");
							objPromortion.setDblAmount(totalAmt);
							objPromortion.setDblDiscountAmt(discAmt);
							objPromortion.setDblDiscountPer(objPromoItemDtl.getDiscPer());
							objPromortion.setDblQuantity(0);
							objPromortion.setDblRate(objBillDtl.getRate());
							objPromortion.setStrDataPostFlag("N");
							objPromortion.setStrPromoType(objPromoItemDtl.getPromoType());
							objPromortion.setStrPromotionCode(objPromoItemDtl.getPromoCode());

							listBillPromotionDtlModel.add(objPromortion);

							hmPromoItem.remove(objBillDtl.getItemCode());
						}
					}
				}
			}
		}

		return listBillPromotionDtlModel;
	}

	public void funSaveHomeDelivery(String voucherNo, clsPOSBillSettlementBean objBean) throws Exception
	{

		Calendar c = Calendar.getInstance();
		int hh = c.get(Calendar.HOUR);
		int mm = c.get(Calendar.MINUTE);
		int ss = c.get(Calendar.SECOND);
		int ap = c.get(Calendar.AM_PM);
		String ampm = "AM";
		if (ap == 1)
		{
			ampm = "PM";
		}
		String currentTime = hh + ":" + mm + ":" + ss + ":" + ampm;//
		// String sql =
		// "select
		// a.strCustomerCode,a.strCustomerName,a.longMobileNo,a.strBuldingCode,a.strCustAddress
		// as strHomeAddress "
		// + ",a.strStreetName,a.strLandmark,a.intPinCode,a.strCity,a.strState "
		// +
		// ",a.strOfficeBuildingCode,a.strOfficeBuildingName as
		// strOfficeAddress,a.strOfficeStreetName,a.strOfficeLandmark,a.intPinCode "
		// + ",a.strOfficeCity,a.strOfficeState "
		// + ",a.strTempAddress,a.strTempStreet,a.strTempLandmark "
		// + "from tblcustomermaster a "
		// + "where longMobileNo like '%" + objBean.getSt + "%' ";
		if (objBean.getStrDeliveryBoyCode() != null)
		{
			clsHomeDeliveryHdModel objHomeDeliveryHdModel = new clsHomeDeliveryHdModel();
			objHomeDeliveryHdModel.setStrBillNo(voucherNo);
			objHomeDeliveryHdModel.setStrCustomerCode(objBean.getStrCustomerCode());
			objHomeDeliveryHdModel.setStrDPCode(objBean.getStrDeliveryBoyCode());
			objHomeDeliveryHdModel.setDteDate(posDate);
			objHomeDeliveryHdModel.setTmeTime(currentTime);
			objHomeDeliveryHdModel.setStrPOSCode(posCode);
			objHomeDeliveryHdModel.setStrCustAddressLine1("");
			objHomeDeliveryHdModel.setStrCustAddressLine2("");
			objHomeDeliveryHdModel.setStrCustAddressLine3("");
			objHomeDeliveryHdModel.setStrCustAddressLine4("");
			objHomeDeliveryHdModel.setStrCustCity("");
			objHomeDeliveryHdModel.setStrClientCode(clientCode);
			objHomeDeliveryHdModel.setDblHomeDeliCharge(objBean.getDblDeliveryCharges());
			objHomeDeliveryHdModel.setDblLooseCashAmt(0);
			objHomeDeliveryHdModel.setStrDataPostFlag("N");
			objBaseServiceImpl.funSave(objHomeDeliveryHdModel);
		}
		else
		{
			// String sql_tblhomedelivery =
			// "insert into tblhomedelivery(strBillNo,strCustomerCode,dteDate,tmeTime"
			// +
			// ",strPOSCode,strCustAddressLine1,strCustAddressLine2,strCustAddressLine3,strCustAddressLine4"
			// + ",strCustCity,strClientCode,dblHomeDeliCharge)"
			// + " values('" + voucherNo + "','" + custCode + "','"
			// + clsGlobalVarClass.gPOSDateForTransaction + "','" + currentTime
			// + "','"
			// + clsGlobalVarClass.gPOSCode + "','" + custAddType + "','',''"
			// + ",'','','" + clsGlobalVarClass.gClientCode + "'," +
			// _deliveryCharge + ")";

			clsHomeDeliveryHdModel objHomeDeliveryHdModel = new clsHomeDeliveryHdModel();
			objHomeDeliveryHdModel.setStrBillNo(voucherNo);
			objHomeDeliveryHdModel.setStrCustomerCode(objBean.getStrCustomerCode());
			objHomeDeliveryHdModel.setStrDPCode(objBean.getStrDeliveryBoyCode());
			objHomeDeliveryHdModel.setDteDate(posDate);
			objHomeDeliveryHdModel.setTmeTime(currentTime);
			objHomeDeliveryHdModel.setStrPOSCode(posCode);
			objHomeDeliveryHdModel.setStrCustAddressLine1("");
			objHomeDeliveryHdModel.setStrCustAddressLine2("");
			objHomeDeliveryHdModel.setStrCustAddressLine3("");
			objHomeDeliveryHdModel.setStrCustAddressLine4("");
			objHomeDeliveryHdModel.setStrCustCity("");
			objHomeDeliveryHdModel.setStrClientCode(clientCode);
			objHomeDeliveryHdModel.setDblHomeDeliCharge(objBean.getDblDeliveryCharges());
			objHomeDeliveryHdModel.setDblLooseCashAmt(0);

			objBaseServiceImpl.funSave(objHomeDeliveryHdModel);
		}
		// //Saving for home delivery Detail data

		clsHomeDeliveryDtlModel objDtlModel = new clsHomeDeliveryDtlModel();
		objDtlModel.setStrBillNo(voucherNo);
		objDtlModel.setDblDBIncentives(0);
		objDtlModel.setDteBillDate(posDate);
		objDtlModel.setStrClientCode(clientCode);
		objDtlModel.setStrDataPostFlag("N");
		objDtlModel.setStrDPCode(objBean.getStrDeliveryBoyCode());
		objDtlModel.setStrSettleYN("N");
		objBaseServiceImpl.funSave(objDtlModel);

	}
*/
	@RequestMapping(value = "/loadItemImage", method = RequestMethod.GET)
	public void getImage(@RequestParam("itemCode") String itemCode,HttpServletRequest req, HttpServletResponse response) throws Exception {
		String clientCode = req.getSession().getAttribute("gClientCode").toString();
		String gPOSCode = req.getSession().getAttribute("gPOSCode").toString();			
		clsMenuItemMasterModel objMenuItemMasterModel = objMasterService.funGetItemImage(itemCode,clientCode);
		
	
		
		try {
			//Blob image = null;
			byte[] imgData = null;
		//	image = objModel.getStrProductImage();
			//if (null != image && image.length() > 0) {
				imgData =objMenuItemMasterModel.getImgImage();
				response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
				OutputStream o = response.getOutputStream();
				o.write(imgData);
				o.flush();
				o.close();
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
