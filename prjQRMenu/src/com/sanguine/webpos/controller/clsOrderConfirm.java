package com.sanguine.webpos.controller;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.base.service.clsBaseServiceImpl;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.webpos.bean.clsPOSBillSettlementBean;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsPOSSetupUtility;
import com.sanguine.webpos.util.clsPOSTextFileGenerator;
import com.sanguine.webpos.util.clsPOSUtilityController;

@Controller
public class clsOrderConfirm
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

	@RequestMapping(value = "/frmConfirmOrder", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		String urlHits = "1",voucherNo="";
		clsPOSBillSettlementBean obBillSettlementBean = new clsPOSBillSettlementBean();
		clsSetupHdModel objSetupHdModel=null;
		String clientCode = "",posCode = "", posDate = "",
				userCode = "",posClientCode = "";
		JSONArray listReasonCode,listReasonName;
		try {
            urlHits = "1";
            if (request.getParameter("saddr") != null) {
                urlHits = request.getParameter("saddr").toString();
            }
            String CustOperationType = "Direct Biller";
            String CustName = "";
            String prefix = "";
            if (request.getParameter("OperationFeedType") != null) {
                CustOperationType = "FeedBackCustomer";
                CustName = request.getParameter("CustNameFeedback").toString();
                final String strGender = request.getParameter("gender").toString();
                prefix = "Mr.";
                if (strGender.equals("Female")) {
                    prefix = "Miss";
                }
            }
            if (request.getParameter("OperationResType") != null) {
                CustOperationType = "ReservationCustomer";
                CustName = request.getParameter("CustNameReservation").toString();
                final String strGender = request.getParameter("gender").toString();
                prefix = "Mr.";
                if (strGender.equals("Female")) {
                    prefix = "Miss";
                }
            }
            if (request.getParameter("OperationMakeBillType") != null) {
                CustOperationType = "MakeBillType";
                CustName = request.getParameter("voucherNoForMakeBill").toString();
            }
            if (request.getParameter("operationDirectBillingType") != null) {
                CustOperationType = "HomeDelivery";
            }
            model.put("CustOperationType", CustOperationType);
            model.put("CustNameForOpeartion", CustName);
            model.put("prefix", prefix);
            clientCode = request.getSession().getAttribute("gClientCode").toString();
            posClientCode = request.getSession().getAttribute("gPOSCode").toString();
            posCode = request.getSession().getAttribute("gPOSCode").toString();
            posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
            userCode = request.getSession().getAttribute("gUserCode").toString();
            objSetupHdModel = this.objMasterService.funGetPOSWisePropertySetup(clientCode, posCode);
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
		
	    return new ModelAndView("frmConfirmOrder", "command", obBillSettlementBean);

	}
	
}
