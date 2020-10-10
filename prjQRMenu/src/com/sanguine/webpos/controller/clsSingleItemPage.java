package com.sanguine.webpos.controller;

import java.text.DecimalFormat;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.base.service.clsBaseServiceImpl;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.controller.clsUserController;
import com.sanguine.webpos.bean.clsPOSBillSettlementBean;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsPOSSetupUtility;
import com.sanguine.webpos.util.clsPOSTextFileGenerator;
import com.sanguine.webpos.util.clsPOSUtilityController;

@Controller
public class clsSingleItemPage
{

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



	@RequestMapping(value = "/frmSingleItemPage", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		
		ModelAndView objMV=null;
		ModelAndView mAndV=null;
		String urlHits = "1",voucherNo="";
		clsPOSBillSettlementBean obBillSettlementBean = new clsPOSBillSettlementBean();
		clsSetupHdModel objSetupHdModel=null;
		String clientCode = "",posCode = "", posDate = "",
				userCode = "",posClientCode = "";
		JSONArray listReasonCode,listReasonName;
		clientCode = request.getSession().getAttribute("gClientCode").toString();
		posClientCode = request.getSession().getAttribute("gPOSCode").toString();
		posCode = request.getSession().getAttribute("gPOSCode").toString();
		posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		userCode = request.getSession().getAttribute("gUserCode").toString();
		
		try
		{
		    objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
		    String gAreaWisePricing=objSetupHdModel.getStrAreaWisePricing();
			String gDirectAreaCode=objSetupHdModel.getStrDirectAreaCode();
			
			String strItemCode="";
			if(request.getParameter("itemCode")!=null)
			{
				strItemCode = request.getParameter("itemCode").toString();

			}
			JSONObject jObj = objBillingAPI.funGetItemPricingDtl(clientCode, posDate, posCode,gAreaWisePricing,gDirectAreaCode,strItemCode);

			JSONArray jsonArrForDirectBillerMenuItemPricing = (JSONArray) jObj.get("MenuItemPricingDtl");
			
		     DecimalFormat decimalFormat = new DecimalFormat("######.##");

			JSONObject objItem=(JSONObject)jsonArrForDirectBillerMenuItemPricing.get(0);
			obBillSettlementBean.setStrItemCode(objItem.get("strItemCode").toString());

			obBillSettlementBean.setStrItemName(objItem.get("strItemName").toString());
			obBillSettlementBean.setStrSettelmentMode(decimalFormat.format(Double.parseDouble(objItem.get("strPriceMonday").toString())));//Amount
			obBillSettlementBean.setStrBillNo((objItem.get("strItemDetails").toString()));//item details
			
			obBillSettlementBean.setJsonArrForDirectBillerMenuItemPricing(jsonArrForDirectBillerMenuItemPricing);



		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
    			
    			// //////////////// Direct biller Tab Data

    			JSONArray jsonArrForTableDtl = objMakeKOT.funLoadTableDtl(clientCode, posCode,"All");
    			obBillSettlementBean.setJsonArrForTableDtl(jsonArrForTableDtl);
    			model.put("gPOSCode", posCode);
    			model.put("gClientCode", clientCode);
    		
    			model.put("urlHits", urlHits);
    			
    			String formToBeOpen="Single Item Page";
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
//    			funLoadAllReasonMasterData(request);
    			
    			
    			/*model.put("listReasonCode", listReasonCode);
    			model.put("listReasonName", listReasonName);
    	*/
    			double gMaxDiscount = objSetupHdModel.getDblMaxDiscount();
    			String gApplyDiscountOn = objSetupHdModel.getStrApplyDiscountOn();
		        return new ModelAndView("frmMenuItem", "command", obBillSettlementBean);

	
	}

}
