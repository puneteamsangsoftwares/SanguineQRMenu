package com.sanguine.webpos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.base.model.clsBaseModel;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.webpos.bean.clsPOSBillSettlementBean;

@Controller
public class clsLoadAllGroup
{

    @Autowired
    intfBaseService objBaseService;
    
	@RequestMapping(value = "/frmLoadGroup", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		clsPOSBillSettlementBean objBean= new clsPOSBillSettlementBean();	
		
		String clientCode = request.getSession().getAttribute("gClientCode").toString();
		String posCode = request.getSession().getAttribute("gPOSCode").toString();
		String posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
		String userCode = request.getSession().getAttribute("gUserCode").toString();
		
		JSONArray jArr = new JSONArray();
		
		String sql="select a.strGroupCode,a.strGroupName from tblgrouphd a where a.strClientCode='"+clientCode+"'";
		try{
		List list=objBaseService.funGetList(new StringBuilder(sql), "sql");
		if(list!=null && list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				Object[] obj= (Object[])list.get(i);
				JSONObject objGroup = new JSONObject();
				objGroup.put("strGroupCode", obj[0].toString());
				objGroup.put("strGroupName", obj[1].toString());
				jArr.add(objGroup);
			}
			
		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		model.put("groupList", jArr);
		objBean.setJsonArrForMenuItemPricing(jArr);
	    return new ModelAndView("frmLoadGroup", "command", objBean);
	}
	@RequestMapping(value = "/actionGroupClicked", method = RequestMethod.POST)
	public ModelAndView funOpenManuItem(@ModelAttribute("command") @Valid clsPOSBillSettlementBean objGroupBean ,BindingResult result,HttpServletRequest request)
	{
		
		if(request.getParameter("groupCode")!=null)
		{
		   String groupCode=request.getParameter("groupCode").toString();
		   request.getSession().setAttribute("groupCode", groupCode);
		   System.out.println(groupCode);
		}
		 
		return  new ModelAndView("redirect:/frmMenuItem.html");

	}
}
