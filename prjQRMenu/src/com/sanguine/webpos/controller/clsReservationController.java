package com.sanguine.webpos.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.base.service.clsBaseServiceImpl;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.webpos.bean.clsPOSCustomerMasterBean;
import com.sanguine.webpos.bean.clsReservationBean;
import com.sanguine.webpos.model.clsReservationModel;
import com.sanguine.webpos.model.clsReservationModel_ID;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsPOSSetupUtility;
import com.sanguine.webpos.util.clsPOSTextFileGenerator;
import com.sanguine.webpos.util.clsPOSUtilityController;


@Controller
public class clsReservationController
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

	@RequestMapping(value = "/frmReservation", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		String urlHits = "1";
		clsReservationBean objReservation = new clsReservationBean();
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
		
	    return new ModelAndView("frmReservation", "command", objReservation);

	}
	
	@RequestMapping(value = "/saveReservation", method = RequestMethod.GET)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsReservationBean objBean, BindingResult result, HttpServletRequest req)
	{
        String strBookingCode="";
		try
		{
		String clientCode = req.getSession().getAttribute("gClientCode").toString();
		long lastNo = 1;
		String propertCode = clientCode.substring(4);
        String strCustCode="";
		List list = objUtilityController.funGetDocumentCode("Booking");
		if ( !list.get(0).toString().equals("0"))
		{
			String strCode = "00";
			String code = list.get(0).toString();
			System.out.println("code-->"+code);
			StringBuilder sb = new StringBuilder(code);
			strCode = sb.substring(4, sb.length());
			lastNo = Long.parseLong(strCode);
			lastNo++;
			strBookingCode = propertCode + "B" + String.format("%07d", lastNo);
		
		}
		else
		{
			strBookingCode = propertCode + "B" + String.format("%07d", lastNo);
		}
		    
		clsReservationModel objModel=new clsReservationModel(new clsReservationModel_ID(strCustCode,strBookingCode,clientCode));	
		String[] strMonth=objBean.getStrBookingDate().split(" ");
		 
		 
	    Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(strMonth[1]);
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int month = cal.get(Calendar.MONTH)+1;
	
		String strBookDateWithYear=strMonth[0]+"-"+String.valueOf(month)+"-"+objBean.getStrYear();		
		String strBookingTime=objBean.getStrBookingTimeHour().split(" ")[0];
		objModel.setStrBookingTime(strBookingTime);
		objModel.setStrBookingDate(objGlobalFunctions.funGetDate("yyyy/MM/dd",strBookDateWithYear));
		objModel.setStrAMPM(objBean.getStrBookingTimeHour().split(" ")[1]);
		objModel.setIntNoOfGuests(objBean.getIntNoOfGuests());
		objModel.setStrStatus("Confirmed");
		objModel.setStrDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
		objBaseServiceImpl.funSave(objModel);
		
		Date dateNew = new Date();  
	   
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		dateNew = formatter.parse(strBookDateWithYear);
		formatter = new SimpleDateFormat("E dd MMM yyyy");  
		String strDate = formatter.format(dateNew);  
		String [] dteSplit=strDate.split(" ");
		strDate=dteSplit[0]+" "+dteSplit[1]+"th "+dteSplit[2]+" "+dteSplit[3]+" @"+objBean.getStrBookingTimeHour() +".";
		req.getSession().setAttribute("bookdateWithMonth", strDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return new ModelAndView("redirect:/frmHomeDelivery.html?bookingNo="+strBookingCode);
	}


	@RequestMapping(value = "/CheckDateAndTimeForBooking", method = RequestMethod.GET)
	public boolean funCheckDateAndTimeForBooking(@RequestParam("bookingdate") String bookingdate,@RequestParam("bookingTime") String bookingTime, BindingResult result, HttpServletRequest request)
	{
		boolean flag=false;
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		
		String bookTime[]=bookingTime.split(" ");
		String sql="select * from tblbooking a where a.strBookingDate='"+objGlobalFunctions.funGetDate("yyyy/MM/dd",bookingdate)+"' and a.strBookingTime='"+bookTime[0]+"' and a.strAMPM='"+bookTime[1]+"'"
				+ " and a.strClientCode='"+clientCode+"';";
		try{
			List list = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if(list!=null && list.size() >0)
			{
				flag=true;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
      return flag;
	}
}
