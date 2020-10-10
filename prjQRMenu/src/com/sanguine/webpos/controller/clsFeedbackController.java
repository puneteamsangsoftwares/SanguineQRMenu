package com.sanguine.webpos.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.base.service.clsBaseServiceImpl;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.webpos.bean.clsFeedbackBean;
import com.sanguine.webpos.bean.clsReservationBean;
import com.sanguine.webpos.model.clsFeedbackDtlModel;
import com.sanguine.webpos.model.clsFeedbackHdModel;
import com.sanguine.webpos.model.clsFeedbackHdModel_ID;
import com.sanguine.webpos.model.clsReservationModel;
import com.sanguine.webpos.model.clsReservationModel_ID;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsPOSSetupUtility;
import com.sanguine.webpos.util.clsPOSTextFileGenerator;
import com.sanguine.webpos.util.clsPOSUtilityController;


@Controller
public class clsFeedbackController
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
	private clsPOSUtilityController objUtilityController;

	@Autowired
	clsPOSMasterService objMasterService;

	@RequestMapping(value = "/frmFeedback", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		String urlHits = "1";
		clsFeedbackBean objFeedback = new clsFeedbackBean();
		clsSetupHdModel objSetupHdModel=null;
		String clientCode = "",posCode = "", posDate = "",
				userCode = "",posClientCode = "";
		try
		{
			urlHits="1";
			if(request.getParameter("saddr")!=null)
			{
				urlHits = request.getParameter("saddr").toString();

			}
			
			
			clientCode = request.getSession().getAttribute("gClientCode").toString();
			posClientCode = request.getSession().getAttribute("gPOSCode").toString();
			posCode = request.getSession().getAttribute("gPOSCode").toString();
			posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
			userCode = request.getSession().getAttribute("gUserCode").toString();

			objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
			
			// //////////////// Direct biller Tab Data

			//JSONArray jsonArrForTableDtl = objMakeKOT.funLoadTableDtl(clientCode, posCode,"All");
			//obBillSettlementBean.setJsonArrForTableDtl(jsonArrForTableDtl);
			model.put("gPOSCode", posCode);
			model.put("gClientCode", clientCode);
			model.put("posDate", posDate);
	
		
			model.put("urlHits", urlHits);
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			urlHits = "1";
		}
		
		// ///////////Bill Settlement tab ////////////////////////////////////
		
	    return new ModelAndView("frmFeedback", "command", objFeedback);

	}
    
	@RequestMapping(value = "/getQuestionForFeedback", method = RequestMethod.GET)
	public @ResponseBody List fungetQuestionForFeedback( HttpServletRequest request)
	{
		boolean flag=false;
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		List list=new ArrayList();
		String sql="select a.strQuestionCode,a.strQuestion,a.strType,a.intRating "
				+ " from tblfeedbackmaster a "
				+ " where a.strClientCode='"+clientCode+"' and a.strOperational='Y'";
		try{
			list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
      return list;
	}
	@RequestMapping(value = "/saveFeedback", method = RequestMethod.GET)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsFeedbackBean objBean, BindingResult result, HttpServletRequest req)
	{
        String strFbCode="";

		try
		{
			req.getSession().getAttribute("allQuestion");
			req.getSession().removeAttribute("allQuestion");
		String clientCode = req.getSession().getAttribute("gClientCode").toString();
		long lastNo = 1;
		String propertCode = clientCode.substring(4);
		
        String strCustCode="";
		List list = objUtilityController.funGetDocumentCode("Feedback");
		if ( !list.get(0).toString().equals("0"))
		{
			String strCode = "00";
			String code = list.get(0).toString();
			System.out.println("code-->"+code);
			StringBuilder sb = new StringBuilder(code);

			strCode = sb.substring(5, sb.length());

			lastNo = Long.parseLong(strCode);
			
			
			lastNo++;
			strFbCode = propertCode + "FB" + String.format("%07d", lastNo);
		
		}
		else
		{
			strFbCode = propertCode + "FB" + String.format("%07d", lastNo);
		}

		clsFeedbackHdModel objModel= new clsFeedbackHdModel(new clsFeedbackHdModel_ID(strFbCode,clientCode));
		clsFeedbackDtlModel objDtl=new clsFeedbackDtlModel();
		List<clsFeedbackDtlModel> listFeedbackDtl= new ArrayList<>();
		
		String[] strAllQue=objBean.getStrAllQueRating().split("#");
		Double queCount=Double.parseDouble(String.valueOf(strAllQue.length));
		double rating=0;
		for(String strQue:strAllQue)
		{
			if(strQue.length() >0)
			{
				objDtl=new clsFeedbackDtlModel();
				String[] que=strQue.split("!");
				objDtl.setStrQuestionCode(que[0]);
				objDtl.setIntRating(Integer.parseInt(que[1]));
				rating +=Double.parseDouble(que[1]);
				listFeedbackDtl.add(objDtl);
			}
			

			
		}
		objModel.setStrRemark(objGlobalFunctions.funIfNull(objBean.getStrRemark(), "", objBean.getStrRemark()));
		
		objModel.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrCustomerCode("");
		objModel.setDblAvgRating(rating/queCount);
		objModel.setListFeedbackDtl(listFeedbackDtl);
		objBaseServiceImpl.funSave(objModel);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return new ModelAndView("redirect:/frmHomeDelivery.html?feedbackCode="+strFbCode);
	}
	
}
