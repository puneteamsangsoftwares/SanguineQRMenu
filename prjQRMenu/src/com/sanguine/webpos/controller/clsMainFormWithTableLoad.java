package com.sanguine.webpos.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.POSLicence.controller.clsClientDetails;
import com.POSLicence.controller.clsEncryptDecryptClientCode;
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
public class clsMainFormWithTableLoad
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

	@RequestMapping(value = "/frmMainFormWithLoadTable", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		
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
		if(request.getParameter("modeOfOperation")!=null)
		{
			String modeOfOperation=request.getParameter("modeOfOperation");
			model.put("modeOfOperation", modeOfOperation);
		}
		else
		{
			model.put("modeOfOperation", "");

		}
		 
		

			try
    		{
    			urlHits="1";
    	    			
    					objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
    			
    			// //////////////// Direct biller Tab Data

    			JSONArray jsonArrForTableDtl = objMakeKOT.funLoadTableDtl(clientCode, posCode,"All");
    			obBillSettlementBean.setJsonArrForTableDtl(jsonArrForTableDtl);
    			model.put("gPOSCode", posCode);
    			model.put("gClientCode", clientCode);
    		
    			model.put("urlHits", urlHits);
    			
    			
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    			urlHits = "1";
    		}
    		
    		// ///////////Bill Settlement tab ////////////////////////////////////
			return new ModelAndView("frmMainFormWithLoadTable", "command", obBillSettlementBean);
	}
	
	@RequestMapping(value = "/actionTableClicked", method = RequestMethod.POST)
	public ModelAndView funGetOperationMode(@ModelAttribute("command") clsPOSBillSettlementBean objBean, BindingResult result, HttpServletRequest request) throws Exception
	{
	  String OperationType="Take Away";
	  if(objBean.getOperationType()!=null)
	  {
	     if(objBean.getOperationType().equalsIgnoreCase("Dine In"))
	     {
			  OperationType="Dine In !"+objBean.getStrTableNo();
			  request.getSession().setAttribute("tableNo", objBean.getStrTableNo());
	     }
	  }
	  if(OperationType.equals("Take Away"))
	  {
		  return  new ModelAndView("redirect:/frmMenuItem.html?saddr=1&OperationType="+OperationType);
 
	  }
	  else 
	  {
		  return  new ModelAndView("redirect:/frmLoadGroup.html");
	  }

	}
		
}
