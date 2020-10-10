package com.sanguine.webpos.printing;

import javax.servlet.http.HttpServletResponse;

public interface clsPOSBillGenerationFormat
{
	public void funGenerateBill(String billNo,String reprint,String  transactionType,String posCode, String strBillDate, String clientCode,String strBillPrinterPort,boolean isOriginal);
}
