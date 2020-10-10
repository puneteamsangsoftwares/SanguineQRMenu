package com.sanguine.webpos.printing;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sanguine.base.service.intfBaseService;

@Controller
public class clsPOSJasperGenerator
{
	@Autowired
	intfBaseService objBaseService;


	//@Autowired
	//clsPOSJasperFormat7ForBillold objJasper7;
	
	@Autowired
	clsJasperFormat7ForBill objJasper7;
	
	@Autowired
	clsJasperFormat4ForBill objJasper4;
	
	@Autowired
	clsJasperFormat5ForBill objJasper5;
	
	@Autowired
	clsJasperFormat8ForBill objJasper8;
	
	@Autowired
	clsJasperFormat9ForBill objJasper9;
	
	@Autowired
	clsJasperFormat10ForBill objJasper10;
	
	StringBuilder sql = new StringBuilder();

	@RequestMapping(value="/getBillPrint",method=RequestMethod.GET)
	public void funCallBillPrint(@RequestParam("voucherNo")String vaoucherNo,HttpServletRequest request,HttpServletResponse response){
		
		try{
			String clientCode = request.getSession().getAttribute("gClientCode").toString();
			String posCode = request.getSession().getAttribute("loginPOS").toString();
			String userCode = request.getSession().getAttribute("gUserCode").toString();
			String posDate = request.getSession().getAttribute("gPOSDate").toString().split(" ")[0];
			
			funGenerateBill(vaoucherNo, "N", "DirectBiller",posCode,  posDate, clientCode, "",true,response);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//return vaoucherNo;
	}
	
	public void funGenerateBill(String billNo, String reprint, String transactionType, String posCode, String strBillDate, String clientCode, String strBillPrinterPort, boolean isOriginal,HttpServletResponse response) throws Exception
	{
		String billFormat = "";
		sql.setLength(0);
		sql.append("select a.strBillFormatType,a.strMultipleBillPrinting from tblsetup a where a.strClientCode='"+clientCode+"' and a.strPOSCode='"+posCode+"'; ");
		List list = objBaseService.funGetList(sql, "sql");
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				Object[] obj = (Object[]) list.get(i);
				billFormat = obj[0].toString();
				reprint = obj[1].toString();
			}
		}
		switch (billFormat)
		{
		    case "Jasper 4":
			objJasper4.funGenerateBill(billNo, reprint, transactionType, posCode, strBillDate, clientCode, strBillPrinterPort, false,response);
			break;

		    case "Jasper 5":
			objJasper5.funGenerateBill(billNo, reprint, transactionType, posCode, strBillDate, clientCode, strBillPrinterPort, false,response);
			break;

			case "Jasper 7":
				objJasper7.funGenerateBill(billNo, reprint, transactionType, posCode, strBillDate, clientCode, strBillPrinterPort, false,response);
				break;
	        
			case "Jasper 8":
				objJasper8.funGenerateBill(billNo, reprint, transactionType, posCode, strBillDate, clientCode, strBillPrinterPort, false,response);
				break;
			
			case "Jasper 9":
				objJasper9.funGenerateBill(billNo, reprint, transactionType, posCode, strBillDate, clientCode, strBillPrinterPort, false,response);
				break;
			
			case "Jasper 10":
				objJasper10.funGenerateBill(billNo, reprint, transactionType, posCode, strBillDate, clientCode, strBillPrinterPort, false,response);
				break;
			
		}
	
	}
}
