package com.sanguine.webpos.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONArray;
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
import com.sanguine.webpos.bean.clsPOSBillSettlementBean;
import com.sanguine.webpos.bean.clsPOSCustomerMasterBean;
import com.sanguine.webpos.model.clsCustomerMasterModel;
import com.sanguine.webpos.model.clsCustomerMasterModel_ID;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsPOSSetupUtility;
import com.sanguine.webpos.util.clsPOSTextFileGenerator;
import com.sanguine.webpos.util.clsPOSUtilityController;

@Controller
public class clsHomeDelivery
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
	private clsGlobalFunctions objGlobal;

	@Autowired
	private clsPOSUtilityController objUtilityController;

	@Autowired
	clsPOSMasterService objMasterService;

	@RequestMapping(value = "/frmHomeDelivery", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		String urlHits = "1";
		clsPOSCustomerMasterBean objCustomerMasterBean = new clsPOSCustomerMasterBean();
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
			
			String bookingNo="";
			String bookingYN="HomeDelivery";
			if(request.getParameter("bookingNo")!=null)
			{
				bookingNo = request.getParameter("bookingNo").toString();
				bookingYN="bookingYes";
			}
		    if(request.getParameter("feedbackCode")!=null)
		    {
		    	bookingNo = request.getParameter("feedbackCode").toString();//feedback code
				bookingYN="Feedback";
		    }
			model.put("bookingNo", bookingNo);
			model.put("bookingYN", bookingYN);
			

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
		
			model.put("urlHits", urlHits);
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			urlHits = "1";
		}
		
		// ///////////Bill Settlement tab ////////////////////////////////////
		
	    return new ModelAndView("frmHomeDelivery", "command", objCustomerMasterBean);

	}
	
	

	@RequestMapping(value = "/savePOSCustomerMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsPOSCustomerMasterBean objBean, BindingResult result, HttpServletRequest req)
	{
		String urlHits = "1";
		String customerMasterCode = objBean.getStrCustomerCode();
		String clientCode = req.getSession().getAttribute("gClientCode").toString();

		try
		{
			if(req.getParameter("saddr")!=null)
			{
				urlHits = req.getParameter("saddr").toString();

			}
			String webStockUserCode = req.getSession().getAttribute("gUserCode").toString();
			String strBuildingCode = "";

			if (!(customerMasterCode != null && customerMasterCode.trim().length()>0))
			{
			
			}
			clsCustomerMasterModel objModel = new clsCustomerMasterModel(new clsCustomerMasterModel_ID(customerMasterCode, clientCode));
	
			objModel.setStrCustomerName(objGlobal.funIfNull(objBean.getStrCustomerName(), "", objBean.getStrCustomerName()));
			objModel.setStrBuldingCode(objGlobal.funIfNull(strBuildingCode,"",strBuildingCode));
			objModel.setStrBuildingName(objGlobal.funIfNull(objBean.getStrBuildingName(),"",objBean.getStrBuildingName()));
			objModel.setStrStreetName(objGlobal.funIfNull(objBean.getStrStreetName(),"",objBean.getStrStreetName()));
			objModel.setStrLandmark(objGlobal.funIfNull(objBean.getStrLandmark(),"",objBean.getStrLandmark()));
			objModel.setStrArea(objGlobal.funIfNull(objBean.getStrArea(),"",objBean.getStrCity()));
			objModel.setStrCity(objGlobal.funIfNull(objBean.getStrCity(),"",objBean.getStrCity()));
			objModel.setStrState(objGlobal.funIfNull(objBean.getStrState(),"",objBean.getStrState()));
			objModel.setIntPinCode(objGlobal.funIfNull(objBean.getIntPinCode(),"",objBean.getIntPinCode()));
			objModel.setLongMobileNo(objGlobal.funIfNull(objBean.getIntlongMobileNo(),"",objBean.getIntlongMobileNo()));
			objModel.setStrOfficeBuildingCode(objGlobal.funIfNull(objBean.getStrOfficeBuildingCode(),"",objBean.getStrOfficeBuildingCode()));
			objModel.setStrOfficeBuildingName(objGlobal.funIfNull(objBean.getStrOfficeBuildingName(),"",objBean.getStrOfficeBuildingName()));
			objModel.setStrOfficeStreetName(objGlobal.funIfNull(objBean.getStrOfficeStreetName(),"",objBean.getStrOfficeStreetName()));
			objModel.setStrOfficeLandmark("N");
			objModel.setStrOfficeArea(objGlobal.funIfNull(objBean.getStrOfficeArea(),"",objBean.getStrOfficeArea()));
			objModel.setStrOfficeCity(objBean.getStrOfficeCity());
			objModel.setStrOfficePinCode(objGlobal.funIfNull(objBean.getStrOfficePinCode(),"",objBean.getStrOfficePinCode()));
			objModel.setStrOfficeState(objBean.getStrOfficeState());
			objModel.setStrOfficeNo(objGlobal.funIfNull(objBean.getStrOfficeNo(),"",objBean.getStrOfficeNo()));
			objModel.setStrOfficeAddress("N");
			objModel.setStrExternalCode(objGlobal.funIfNull(objBean.getStrExternalCode(),"",objBean.getStrExternalCode()));
			objModel.setStrCustomerType(objGlobal.funIfNull(objBean.getStrCustomerType(),"",objBean.getStrCustomerType()));
			objModel.setDteDOB(objGlobal.funIfNull(objBean.getDteDOB(),"",objBean.getDteDOB()));
			objModel.setStrGender(objGlobal.funIfNull(objBean.getStrGender(),"",objBean.getStrGender()));
			objModel.setDteAnniversary(objGlobal.funIfNull(objBean.getDteAnniversary(),"",objBean.getDteAnniversary()));
			objModel.setStrEmailId(objGlobal.funIfNull(objBean.getStrEmailId(),"",objBean.getStrEmailId()));
			objModel.setStrClientCode(clientCode);
			objModel.setStrUserCreated(objGlobal.funIfNull(webStockUserCode,"",webStockUserCode));
			objModel.setStrUserEdited(objGlobal.funIfNull(webStockUserCode,"",webStockUserCode));
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrCRMId("N");
			objModel.setStrCustAddress(objGlobal.funIfNull(objBean.getStrCustAddress(),"",objBean.getStrCustAddress()));
			objModel.setStrDataPostFlag("N");
			objModel.setStrGSTNo(objGlobal.funIfNull(objBean.getStrGSTNo(),"",objBean.getStrGSTNo()));
			objModel.setStrTempAddress(objGlobal.funIfNull(objBean.getStrTempAddress(),"",objBean.getStrTempAddress()));
			objModel.setStrTempLandmark(objGlobal.funIfNull(objBean.getStrTempLandmark(),"",objBean.getStrTempLandmark()));
			objModel.setStrTempStreet(objGlobal.funIfNull(objBean.getStrTempStreetName(),"",objBean.getStrTempStreetName()));

			objMasterService.funSaveCustomerMaster(objModel);
		}
		catch (Exception ex)
		{
			urlHits = "1";
			ex.printStackTrace();
		}
		if(req.getParameter("bookingYN").toString().equalsIgnoreCase("Feedback"))
		{
			String feedbackCode=req.getParameter("bookingNO").toString();
			String sql="update tblfeedbackhd a set a.strCustomerCode='"+customerMasterCode+"' where a.strFeedbackCode='"+feedbackCode+"' and a.strClientCode='"+clientCode+"'";
			try{
			objBaseService.funExecuteUpdate(sql, "sql");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			return new ModelAndView("redirect:/frmConfirmOrder.html?saddr=" + urlHits+"&CustNameFeedback="+objBean.getStrCustomerName()+"&OperationFeedType=FeedBackCust&gender="+objBean.getStrGender()+"");

		}
		else if(req.getParameter("bookingYN").toString().equalsIgnoreCase("bookingYes")){
			
			String bookCode=req.getParameter("bookingNO").toString();
			String sql="update tblbooking a set a.strCustomerCode='"+customerMasterCode+"' where a.strBookingCode='"+bookCode+"' and a.strClientCode='"+clientCode+"'";
			try{
			objBaseService.funExecuteUpdate(sql, "sql");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			return new ModelAndView("redirect:/frmConfirmOrder.html?saddr=" + urlHits+"&CustNameReservation="+objBean.getStrCustomerName()+"&OperationResType=ReservationCust&gender="+objBean.getStrGender()+"");
		}
		else
		{
			return new ModelAndView("redirect:/frmMenuItem.html?saddr=" + urlHits+"&CustCode="+customerMasterCode+"&OperationType=Home Delivery");

		}

	}
	@RequestMapping(value = "/GetCustomerCodeForQRMenu", method = RequestMethod.GET)
	public @ResponseBody List funGetCustomerCodeForQRMenu(HttpServletRequest req)
	{
		String urlHits = "1";
		String clientCode = req.getSession().getAttribute("gClientCode").toString();
		long lastNo = 1;
		String propertCode = clientCode.substring(4);
		List listCustomer=new ArrayList();
		String customerMasterCode="";
		try
		{
			List list = objUtilityController.funGetDocumentCode("POSCustomerMaster");
			if (!list.get(0).toString().equals("0"))
			{
				String strCode = "00";
				String code = list.get(0).toString();
				System.out.println("code-->"+code);
				StringBuilder sb = new StringBuilder(code);

				strCode = sb.substring(1, sb.length());

				lastNo = Long.parseLong(strCode);
				
				
				lastNo++;
				customerMasterCode = propertCode + "C" + String.format("%07d", lastNo);
			
			}
			else
			{
				customerMasterCode = propertCode + "C" + String.format("%07d", lastNo);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
		listCustomer.add(customerMasterCode);
		return listCustomer;
	}
	@RequestMapping(value = "/GetAllCustomerDataForQRMenu", method = RequestMethod.GET)
	public @ResponseBody List funGetAllCustomerDataForQRMenu(@RequestParam("custCode")String custCode,HttpServletRequest req)
	{
		String urlHits = "1";
		String clientCode = req.getSession().getAttribute("gClientCode").toString();
		
		List listCustomerData=new ArrayList();
		try
		{
		  StringBuilder strBuild= new StringBuilder();
		  strBuild.append("select a.strCustomerCode,a.strCustomerName,a.strCustAddress,a.strStreetName,a.strLandmark,a.longMobileNo,a.strEmailId,a.strGender"
		  		+ " from tblcustomermaster a where a.strCustomerCode='"+custCode+"' and a.strClientCode='"+clientCode+"'");
		
		  listCustomerData=objBaseService.funGetList(strBuild, "sql");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
		return listCustomerData;
	}
	
}
	