package com.sanguine.webpos.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.POSLicence.controller.clsClientDetails;
import com.POSLicence.controller.clsEncryptDecryptClientCode;
import com.sanguine.controller.clsUserController;
import com.sanguine.webpos.bean.clsPOSBillSettlementBean;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;

@Controller
public class clsModeOfOperation
{

	@Autowired
	clsUserController objUserController;
	@Autowired
	clsPOSMasterService objMasterService;


	@RequestMapping(value = "/frmModeOfOpearation", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		
		if(request.getParameter("WebClientCode")!=null)
		{
			String cl = request.getParameter("WebClientCode").toString();
			System.out.println(cl);
			request.getSession().setAttribute("WebClientCode", cl);
		}
		if(request.getParameter("WebQrPOSCode")!=null)
		{
			String posCode = request.getParameter("WebQrPOSCode").toString();
			System.out.println(posCode);
			request.getSession().setAttribute("WebQrPOSCode", posCode);
		}
		
		if(request.getParameter("ModeOfOpType")!=null)
		{
			String opType = request.getParameter("ModeOfOpType").toString();
			System.out.println(opType);
			request.getSession().setAttribute("ModeOfOpType", opType);
		}
		
		if(request.getParameter("GroupNames")!=null)
		{
			String groupName = request.getParameter("GroupNames").toString();
			System.out.println(groupName);
			request.getSession().setAttribute("GroupNames", groupName);
		}
		clsPOSBillSettlementBean obBillSettlementBean = new clsPOSBillSettlementBean();
		objUserController.welcome(request);
		ModelAndView objMV=null;
		ModelAndView mAndV=null;
		String urlHits = "1",voucherNo="";
		clsSetupHdModel objSetupHdModel=null;
		String clientCode = "",posCode = "", posDate = "",
				userCode = "",posClientCode = "";
		JSONArray listReasonCode,listReasonName;
		clientCode = request.getSession().getAttribute("gClientCode").toString();
		posClientCode = request.getSession().getAttribute("gPOSCode").toString();
		posCode = request.getSession().getAttribute("gPOSCode").toString();
		posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		userCode = request.getSession().getAttribute("gUserCode").toString();

		boolean flag=funCheckLicense(request);

		if(true)
        {
        	try
    		{
    			urlHits="1";
    			if(request.getParameter("saddr")!=null)
    			{
    				urlHits = request.getParameter("saddr").toString();

    			}
    			if(request.getParameter("WebClientCode")!=null)
    			{
    				String cl = request.getParameter("WebClientCode").toString();
    				System.out.println(cl);

    			}

    			
    					objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(clientCode,posCode);
    			
    			// //////////////// Direct biller Tab Data

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
        	mAndV = new ModelAndView("frmModeOfOpearation", "command", obBillSettlementBean);

        }
        else
        {
    		mAndV = new ModelAndView("frmLicenseExpirePage","command", obBillSettlementBean);
        }
				
         return mAndV;

		//return new ModelAndView("frmModeOfOpearation", "command", obBillSettlementBean);

	}
	private boolean funCheckLicense(HttpServletRequest req )
	{
		boolean flag=false;
		String clientCodeFromDB=req.getSession().getAttribute("gClientCode").toString();
		String companyNameFromDB=req.getSession().getAttribute("gCompanyName").toString();
		
		String encryptedClientCodeFromDB = clsEncryptDecryptClientCode.funEncryptClientCode(clientCodeFromDB);
		String encryptedClientNameFromDB = clsEncryptDecryptClientCode.funEncryptClientCode(companyNameFromDB);
					
		clsClientDetails.funAddClientCodeAndName();
		
//		String decryptedClientCodeFromHm = clsEncryptDecryptClientCode.funDecryptClientCode(clsClientDetails.hmClientDtl.get(encryptedClientCodeFromDB).);
		String decryptedClientNameFromHm = clsEncryptDecryptClientCode.funDecryptClientCode(clsClientDetails.hmClientDtl.get(encryptedClientCodeFromDB).Client_Name);
		
		String isQRMenu = clsEncryptDecryptClientCode.funDecryptClientCode(clsClientDetails.hmClientDtl.get(encryptedClientCodeFromDB).getStrIsQRMenu());
	
		
		/*clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clsEncryptDecryptClientCode.funEncryptClientCode(clientCode));
			String trminal = clsEncryptDecryptClientCode.funDecryptClientCode(String.valueOf(objClientDetails.getStrPOSBITerminal()));
			*/
					
		if(companyNameFromDB.equalsIgnoreCase(decryptedClientNameFromHm))
		{
			 SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
             try
             {
            	 Date systemDate = dFormat.parse(dFormat.format(new Date()));
            	 
            	 String encryptedExpDate=clsClientDetails.hmClientDtl.get(encryptedClientCodeFromDB).expiryDate;
            	 String decryptedExpDate=clsEncryptDecryptClientCode.funDecryptClientCode(encryptedExpDate);
            	 
            	 Date webPOSExpiryDate = dFormat.parse(decryptedExpDate);
				 if (systemDate.compareTo(webPOSExpiryDate)<=0 && isQRMenu.equalsIgnoreCase("QRMenu")) 
				 {
					 flag=true;
					System.out.println("License varified"); 
				 }
             }
             catch(Exception ex)
             {
            	 ex.printStackTrace();
             }
	  }
		return flag;
	}
}
