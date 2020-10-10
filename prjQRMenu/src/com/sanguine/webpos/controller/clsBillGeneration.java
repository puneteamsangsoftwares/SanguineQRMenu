package com.sanguine.webpos.controller;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class clsBillGeneration
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

	@Autowired
	clsUserController objUserController;

	@RequestMapping(value = "/frmPOSBillSettlement", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		

		//objUserController.welcome(request);
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
		model.put("operationMode", "Dine In And Make Bill");
	//	model.put("tableOp", request.getSession().getAttribute("tableNo").toString());

		try
		{
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
			


		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
    			
    			// //////////////// Direct biller Tab Data

		model.put("gPOSCode", posCode);
		model.put("gClientCode", clientCode);
	
		model.put("urlHits", urlHits);
		String formToBeOpen="Bill Settlement";
		model.put("formToBeOpen", formToBeOpen);
/*		String formToBeOpen="Make Bill";
		model.put("formToBeOpen", formToBeOpen);
*/		model.put("billDate", posDate);

       model.put("operationMode", "DineIn And Make Bill");


		
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
		return new ModelAndView("frmMenuItem", "command", obBillSettlementBean);

	}
}
