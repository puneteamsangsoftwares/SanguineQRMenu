package com.sanguine.webpos.printing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.webpos.bean.clsPOSBillDetails;
import com.sanguine.webpos.bean.clsPOSBillDtl;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.model.clsSetupModel_ID;
import com.sanguine.webpos.sevice.clsPOSMasterService;

@Controller
public class clsPOSJasperFormat7ForBillold
{
	@Autowired
	intfBaseService objBaseService;

	@Autowired
	clsPOSMasterService objMasterService;

	@Autowired
	private ServletContext servletContext;

	String strBillPrinterPort = "";
	
	@Autowired
	clsGlobalFunctions objGlobalFunctions;
    
	@Autowired
	clsPrintingUtility objPrinting;
	private String strPrintOrderNoOnBillYN;

	public HttpServletResponse funGenerateBill(String strBillNo, String reprint, String transactionType, String strPosCode, String strBillDate, String strClientCode, String strServerBillPrinterName, boolean isOriginal,HttpServletResponse response)
	{
		HashMap hm = new HashMap();
		List list = new ArrayList();
		StringBuilder sql = new StringBuilder();
		DecimalFormat decimalFormat = new DecimalFormat("#.###");

		try
		{
			// String reportName =
			// servletContext.getRealPath("/WEB-INF/reports/billFormat/rptBillFormat7JasperReport.jrxml");
			final String gDecimalFormatString = objGlobalFunctions.funGetGlobalDecimalFormatString(strClientCode,strPosCode);

			clsSetupModel_ID ob = new clsSetupModel_ID(strClientCode, strPosCode);
			clsSetupHdModel objSetupHdModel = new clsSetupHdModel();
			clsPOSBillDtl objPOSBillDtl = null;

			String billhd = "tblbillhd";
			String billdtl = "tblbilldtl";
			String billModifierdtl = "tblbillmodifierdtl";
			String billSettlementdtl = "tblbillsettlementdtl";
			String billtaxdtl = "tblbilltaxdtl";
			String billPromoDtl = "tblbillpromotiondtl";
			String billDiscount = "tblbilldiscdtl";
			String sql_BillHD = "";
			String strPrintTimeOnBill = "";
			String strPrintOpenItemsOnBill = "";
			String strPrintZeroAmtModifierOnBill = "";
			String strUser = "";
			// double dblSubTotal=0.0;
			double dblGrandTotal = 0.0;
			double dblAdvAmt = 0.0;
			double dblDelCharge = 0.0;
			boolean flag_DirectBillerBill = false;
			boolean flgComplimentaryBill = false;
			String billNo = "", tableName = "", waiterNo = "", strPrintOrderNoOnBillYN = "", billDate = "", operationType = "", strUserName = "", strCustomerCode = "", strBillType = "";
			double grandTotal = 0.0, discount = 0.0;
			int paxNo = 0;
			String subTotal = "";

			if (strClientCode.equals("117.001"))
			{
				if (strPosCode.equals("P01"))
				{
					hm.put("posWiseHeading", "THE PREM'S HOTEL");
				}
				else if (strPosCode.equals("P02"))
				{
					hm.put("posWiseHeading", "SWIG");
				}
			}
			if (strClientCode.equals("239.001"))// URBO
			{
				hm.put("brandName", "URBO");
			}

			boolean isReprint = false;
			if (isOriginal)
			{
				hm.put("duplicate", "[ORIGINAL]");
			}

			if ("reprint".equalsIgnoreCase(reprint))
			{
				isReprint = true;
				hm.put("duplicate", "[DUPLICATE]");
			}
			if (transactionType.equals("Void"))
			{
				hm.put("voidedBill", "VOIDED BILL");
			}
				sql.append(" select a.strBillNo,ifnull(b.strTableName,''),ifnull(c.strWShortName,''),a.dblGrandTotal,a.dblSubTotal," + " a.dblDiscountAmt,a.dteBillDate,a.intPaxNo,a.strOperationType,a.strCustomerCode,a.strUserCreated " + " from tblbillhd a left outer join tbltablemaster b on a.strTableNo=b.strTableNo left outer join tblwaitermaster c " + "on a.strWaiterNo=c.strWaiterNo " + " where a.strBillNo='" + strBillNo + "' " + " and a.strPosCode='" + strPosCode + "' ");
				list = objBaseService.funGetList(sql, "sql");
				if (list != null)
				{
					for (int i = 0; i < list.size(); i++)
					{
						Object[] obj = (Object[]) list.get(i);
						billNo = obj[0].toString();
						tableName = obj[1].toString();
						waiterNo = obj[2].toString();
						grandTotal = Double.parseDouble(obj[3].toString());
						subTotal = obj[4].toString();
						discount = Double.parseDouble(obj[5].toString());
						billDate = obj[6].toString();
						paxNo = Integer.parseInt(obj[7].toString());
						operationType = obj[8].toString();
						strCustomerCode = obj[9].toString();
						strUserName = obj[10].toString();
					}
				}

				boolean flag_isHomeDelvBill = false;
				String sql_HomeDelivery = "select strBillNo,strCustomerCode,strDPCode,tmeTime,strCustAddressLine1 " + "from tblhomedelivery where strBillNo='" + strBillNo + "' ;";
				List listHomeDel = objBaseService.funGetList(new StringBuilder(sql_HomeDelivery), "sql");
				List<clsPOSBillDtl> listOfHomeDeliveryDtl = new ArrayList<>();
				clsPOSBillDtl objBillDtl = new clsPOSBillDtl();

				if (listHomeDel != null)
				{
					flag_isHomeDelvBill = true;
					for (int i = 0; i < listHomeDel.size(); i++)
					{
						Object[] objHomeDel = (Object[]) listHomeDel.get(i);
						if (operationType.equalsIgnoreCase("HomeDelivery"))
						{
							strBillType = "HOME DELIVERY";
						}
						strCustomerCode = objHomeDel[1].toString();
						String SQL_CustomerDtl = "";
						if (objHomeDel[4].toString().equals("Temporary"))
						{
							SQL_CustomerDtl = "select a.strCustomerName,a.strTempAddress,a.strTempStreet" + " ,a.strTempLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo " + " from tblcustomermaster a left outer join tblbuildingmaster b " + " on a.strBuldingCode=b.strBuildingCode " + " where a.strCustomerCode=? ;";
						}
						else if (objHomeDel[4].toString().equals("Office"))
						{
							SQL_CustomerDtl = "select a.strCustomerName,a.strOfficeBuildingName,a.strOfficeStreetName" + ",a.strOfficeLandmark,a.strOfficeArea,a.strOfficeCity,a.strOfficePinCode,a.longMobileNo " + " from tblcustomermaster a " + " where a.strCustomerCode=? ";
						}
						else
						{
							SQL_CustomerDtl = "select a.strCustomerName,a.strCustAddress,a.strStreetName" + " ,a.strLandmark,a.strBuildingName,a.strCity,a.intPinCode,a.longMobileNo " + " from tblcustomermaster a left outer join tblbuildingmaster b " + " on a.strBuldingCode=b.strBuildingCode " + " where a.strCustomerCode=? ;";
						}
						List listCustomerDtl = objBaseService.funGetList(new StringBuilder(SQL_CustomerDtl), "sql");
						if (listCustomerDtl != null)
						{
							StringBuilder fullAddress = new StringBuilder();

							for (int j = 0; j < listCustomerDtl.size(); j++)
							{
								Object[] objCustDtl = (Object[]) listCustomerDtl.get(j);
								hm.put("NAME", objCustDtl[0].toString());
								objBillDtl = new clsPOSBillDtl();
								objBillDtl.setStrItemName("Name         : " + objCustDtl[0].toString().toUpperCase());
								fullAddress.append(objBillDtl.getStrItemName());
								listOfHomeDeliveryDtl.add(objBillDtl);

								objBillDtl = new clsPOSBillDtl();
								objBillDtl.setStrItemName("ADDRESS    :" + objCustDtl[1].toString().toUpperCase());
								fullAddress.append(objBillDtl.getStrItemName());
								listOfHomeDeliveryDtl.add(objBillDtl);

								if (objCustDtl[2].toString().trim().length() > 0)
								{
									objBillDtl = new clsPOSBillDtl();
									objBillDtl.setStrItemName(objCustDtl[2].toString().toUpperCase());// "Street    :"
																										// +
									fullAddress.append(objBillDtl.getStrItemName());
									listOfHomeDeliveryDtl.add(objBillDtl);
								}

								if (objCustDtl[3].toString().trim().length() > 0)
								{
									objBillDtl = new clsPOSBillDtl();
									objBillDtl.setStrItemName(objCustDtl[3].toString().toUpperCase());// "Landmark    :"
																										// +
									fullAddress.append(objBillDtl.getStrItemName());
									listOfHomeDeliveryDtl.add(objBillDtl);
								}

								if (objCustDtl[5].toString().trim().length() > 0)
								{
									objBillDtl = new clsPOSBillDtl();
									objBillDtl.setStrItemName(objCustDtl[5].toString().toUpperCase());// "City    :"
																										// +
									fullAddress.append(objBillDtl.getStrItemName());
									listOfHomeDeliveryDtl.add(objBillDtl);
								}

								if (objCustDtl[6].toString().trim().length() > 0)
								{
									objBillDtl = new clsPOSBillDtl();
									objBillDtl.setStrItemName(objCustDtl[6].toString().toUpperCase());// "Pin    :"
																										// +
									fullAddress.append(objBillDtl.getStrItemName());
									listOfHomeDeliveryDtl.add(objBillDtl);
								}
								hm.put("FullAddress", fullAddress);

								if (objCustDtl[7].toString().isEmpty())
								{
									hm.put("MOBILE_NO", "");
									objBillDtl = new clsPOSBillDtl();
									objBillDtl.setStrItemName("MOBILE_NO  :" + " ");
									listOfHomeDeliveryDtl.add(objBillDtl);
								}
								else
								{
									hm.put("MOBILE_NO", objCustDtl[7].toString());
									objBillDtl = new clsPOSBillDtl();
									objBillDtl.setStrItemName("Mobile No    : " + objCustDtl[7].toString());
									listOfHomeDeliveryDtl.add(objBillDtl);
								}
							}
						}
						if (null != objHomeDel[2].toString() && objHomeDel[2].toString().trim().length() > 0)
						{
							String[] delBoys = objHomeDel[2].toString().split(",");
							StringBuilder strIN = new StringBuilder("(");
							for (int k = 0; k < delBoys.length; k++)
							{
								if (k == 0)
								{
									strIN.append("'" + delBoys[k] + "'");
								}
								else
								{
									strIN.append(",'" + delBoys[k] + "'");
								}
							}
							strIN.append(")");
							String SQL_DeliveryBoyDtl = "select strDPName from tbldeliverypersonmaster where strDPCode IN " + strIN + " ;";
							List listDelBoyDtl = objBaseService.funGetList(new StringBuilder(SQL_DeliveryBoyDtl), "sql");
							strIN.setLength(0);
							for (int l = 0; l < listDelBoyDtl.size(); l++)
							{
								Object[] objDelBoy = (Object[]) listDelBoyDtl.get(l);
								if (l == 0)
								{
									strIN.append(objDelBoy[0].toString().toUpperCase());
								}
								else
								{
									strIN.append("," + objDelBoy[0].toString().toUpperCase());
								}
							}

							if (strIN.toString().isEmpty())
							{
								hm.put("DELV BOY", "");
							}
							else
							{
								hm.put("DELV BOY", "Delivery Boy : " + strIN);
								objBillDtl = new clsPOSBillDtl();
								objBillDtl.setStrItemName("Delivery Boy : " + strIN);
								listOfHomeDeliveryDtl.add(objBillDtl);
							}
						}
						else
						{
							hm.put("DELV BOY", "");
						}
					}
					if (operationType.equalsIgnoreCase("TakeAway"))
					{
						strBillType = "Take Away";
						String sqlTakeAway = "select a.strBillNo,a.dteBillDate,a.strCustomerCode,b.strCustomerName,b.longMobileNo " + "from tblbillhd a,tblcustomermaster b " + "where a.strCustomerCode=b.strCustomerCode " + "and a.strBillNo='" + billNo + "' " + "and date(a.dteBillDate)='" + strBillDate + "' ";
						List listTakeAway = objBaseService.funGetList(new StringBuilder(sqlTakeAway), "sql");
						if (listTakeAway != null)
						{
							for (int m = 0; m < listTakeAway.size(); m++)
							{
								Object[] objTakeAway = (Object[]) listTakeAway.get(m);
								hm.put("NAME", objTakeAway[3].toString());
								objBillDtl = new clsPOSBillDtl();
								objBillDtl.setStrItemName("Name         : " + objTakeAway[3].toString().toUpperCase());
								listOfHomeDeliveryDtl.add(objBillDtl);

								hm.put("MOBILE_NO", objTakeAway[4].toString());
								objBillDtl = new clsPOSBillDtl();
								objBillDtl.setStrItemName("Mobile No    : " + objTakeAway[4].toString());
								listOfHomeDeliveryDtl.add(objBillDtl);
							}
						}
					}
				}

				hm.put("TAX_INVOICE", "TAX INVOICE");

				// Client Details
				objSetupHdModel = objMasterService.funGetPOSWisePropertySetup(strClientCode,strPosCode);
				hm.put("ClientName", objSetupHdModel.getStrClientName());
				hm.put("ClientAddressLine1", objSetupHdModel.getStrAddressLine1());
				hm.put("ClientAddressLine2", objSetupHdModel.getStrAddressLine2());
				hm.put("ClientAddressLine3", objSetupHdModel.getStrAddressLine3());
				hm.put("ClientCity", objSetupHdModel.getStrCityName());
				hm.put("TEL NO", String.valueOf(objSetupHdModel. getStrTelephoneNo()));
				hm.put("EMAIL ID", objSetupHdModel.getStrEmail());
				strPrintTimeOnBill = objSetupHdModel.getStrPrintTimeOnBill();
				strPrintOpenItemsOnBill = objSetupHdModel.getStrPrintOpenItemsOnBill();
				strPrintZeroAmtModifierOnBill = objSetupHdModel.getStrPrintZeroAmtModifierInBill();

				/* String waiterNo=""; */
				String query = "";
				String strWaiterName = "";
				String tableNo = "";
				String strTableName = "";
				Object[] obj = null;

				String sqlSettle = "select b.strSettelmentType from " + billSettlementdtl + " a,tblsettelmenthd b " + " where a.strSettlementCode=b.strSettelmentCode and a.strBillNo='" + strBillNo + "' and b.strSettelmentType='Complementary' " + " and date(a.dteBillDate)='" + strBillDate + "'";
				List listSettle = objBaseService.funGetList(new StringBuilder(sqlSettle), "sql");
				if (listSettle.size() > 0)
				{
					flgComplimentaryBill = true;
				}

				if (funIsDirectBillerBill(strBillNo, billhd))
				{
					flag_DirectBillerBill = true;
					sql_BillHD = "select a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal," + "a.strCustomerCode,a.dblGrandTotal,a.dblTaxAmt,a.strReasonCode,a.strRemarks,a.strUserCreated" + ",ifnull(dblDeliveryCharges,0.00),ifnull(b.dblAdvDeposite,0.00),a.dblDiscountPer,c.strPOSName,a.intOrderNo,a.strKOTToBillNote,a.dblTipAmount  " + "from " + billhd + " a left outer join tbladvancereceipthd b on a.strAdvBookingNo=b.strAdvBookingNo " + "left outer join tblposmaster c on a.strPOSCode=c.strPOSCode " + "where a.strBillNo='" + strBillNo + "'  and date(a.dteBillDate)='" + strBillDate + "' ";
					list = objBaseService.funGetList(new StringBuilder(sql_BillHD), "sql");
					if (list.size() > 0)
					{
						for (int i = 0; i < list.size(); i++)
						{
							obj = (Object[]) list.get(i);
						}
					}
				}
				else
				{
					sql_BillHD = "select a.strTableNo,a.strWaiterNo,a.dteBillDate,time(a.dteBillDate),a.dblDiscountAmt,a.dblSubTotal," + "a.strCustomerCode,a.dblGrandTotal,a.dblTaxAmt,a.strReasonCode,a.strRemarks,a.strUserCreated" + ",dblDeliveryCharges,ifnull(c.dblAdvDeposite,0.00),a.dblDiscountPer,d.strPOSName,a.intPaxNo " + "from " + billhd + " a left outer join tbltablemaster b on a.strTableNo=b.strTableNo " + "left outer join tbladvancereceipthd c on a.strAdvBookingNo=c.strAdvBookingNo " + "left outer join tblposmaster d on a.strPOSCode=d.strPOSCode " + "where a.strBillNo='" + strBillNo + "' and b.strOperational='Y' and date(a.dteBillDate)='" + strBillDate + "' ";
					list = objBaseService.funGetList(new StringBuilder(sql_BillHD), "sql");
					if (list.size() > 0)
					{
						for (int i = 0; i < list.size(); i++)
						{
							obj = (Object[]) list.get(i);
						}

						tableNo = obj[0].toString();
						if (obj[1].toString().equalsIgnoreCase("null") || obj[1].toString().equalsIgnoreCase(""))
						{
							waiterNo = "";
						}
						else
						{
							waiterNo = obj[1].toString();
							query = "SELECT strWShortName FROM tblwaitermaster WHERE strWaiterNo='" + waiterNo + "' " + "AND (strPOSCode='" + strPosCode + "' or strPOSCode='All'); ";
							list = objBaseService.funGetList(new StringBuilder(query), "sql");
							if (list.size() > 0)
							{
								strWaiterName = list.get(0).toString();
							}
						}
					}

					String sqlTblName = "select strTableName from tbltablemaster where strTableNo='" + tableNo + "' ;";
					list = objBaseService.funGetList(new StringBuilder(sqlTblName), "sql");
					if (list.size() > 0)
					{
						strTableName = list.get(0).toString();
					}
				}

				if (flag_DirectBillerBill)
				{
					hm.put("POS", obj[13].toString());
					hm.put("BillNo", strBillNo);

					

					
					  if (strPrintOrderNoOnBillYN.length()>0) {
						  String orderNo = obj[14].toString();
					     hm.put("orderNo", "Your order no is " + orderNo);
					  
					  }
					 

					String billNote = obj[15].toString();
					hm.put("strBillNote", billNote);
					if (strPrintTimeOnBill.length() > 0)

					{
						SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
						hm.put("DATE_TIME", ft.format(obj[0]));
					}
					else
					{
						SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
						hm.put("DATE_TIME", ft.format(obj[0]));
					}

					if (obj[8].toString().trim().isEmpty())
					{
						hm.put("Remarks", "");
					}
					else
					{
						hm.put("Remarks", "Remarks :" + obj[8].toString());
					}

					subTotal = (obj[3].toString());
					dblGrandTotal = Double.parseDouble(obj[5].toString());
					hm.put("user", obj[9].toString());
					dblDelCharge = Double.parseDouble(obj[10].toString());
					dblAdvAmt = Double.parseDouble(obj[11].toString());
					hm.put("tipAmt", obj[16].toString());

				}
				else
				{
					hm.put("TABLE NAME", strTableName);

					if (strWaiterName.trim().length() > 0)
					{
						hm.put("waiterName", strWaiterName);
					}
					hm.put("POS", obj[15].toString());
					hm.put("BillNo", strBillNo);
					hm.put("PaxNo", obj[16].toString());
					String orderNo = obj[14].toString();

					if (strPrintOrderNoOnBillYN.length() > 0)
					{
						hm.put("orderNo", "Your order no is " + orderNo);

					}

					String billNote = obj[15].toString();
					hm.put("strBillNote", billNote);
					if (strPrintTimeOnBill.equals("Y"))
					{
						SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm a ");
						hm.put("DATE_TIME", ft.format(obj[2]));
					}
					else
					{
						SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
						hm.put("DATE_TIME", ft.format(obj[2]));
					}

					if (obj[10].toString().trim().isEmpty())
					{
						hm.put("Remarks", "");
					}
					else
					{
						hm.put("Remarks", "Remarks :" + obj[10].toString());
					}

					subTotal = obj[5].toString();
					dblGrandTotal = Double.parseDouble(obj[7].toString());
					hm.put("user", obj[9].toString());
					dblDelCharge = Double.parseDouble(obj[12].toString());
					dblAdvAmt = Double.parseDouble(obj[13].toString());
					hm.put("tipAmt", obj[16].toString());
				}
				Object[] objBillDetail = null;
				/* MID(a.strItemName,23,LENGTH(a.strItemName)) as ItemLine2 */
				List<clsPOSBillDtl> listOfBillDetail = new ArrayList<>();
				String sql_BillDtl = "select sum(a.dblQuantity),a.strItemName as ItemLine1" + " ,a.strItemName as ItemLine2,sum(a.dblAmount),a.strItemCode,a.strKOTNo " + " from " + billdtl + " a " + " where a.strBillNo='" + strBillNo + "' and a.tdhYN='N' and date(a.dteBillDate)='" + strBillDate + "'";

				if (!strPrintOpenItemsOnBill.equals("Y"))
				{
					sql_BillDtl += "and a.dblAmount>0 ";
				}
				sql_BillDtl += " group by a.strItemCode ;";
				list = objBaseService.funGetList(new StringBuilder(sql_BillDtl), "sql");
				if (list.size() > 0)
				{

					for (int i = 0; i < list.size(); i++)
					{
						objBillDetail = (Object[]) list.get(i);
				
					double saleQty = Double.parseDouble(objBillDetail[0].toString());
					String sqlPromoBills = "select dblQuantity from " + billPromoDtl + " " + " where strBillNo='" + strBillNo + "' and strItemCode='" + objBillDetail[4].toString() + "' " + " and strPromoType='ItemWise' and date(dteBillDate)='" + strBillDate + "' ";
					List listPromoItems = objBaseService.funGetList(new StringBuilder(sqlPromoBills), "sql");
					if (listPromoItems.size() > 0)
					{
						for (int j = 0; j < listPromoItems.size(); j++)
						{
							objBillDetail = (Object[]) listPromoItems.get(j);
						}
						saleQty -= Double.parseDouble(objBillDetail[0].toString());
					}

					String qty = String.valueOf(saleQty);
					if (qty.contains("."))
					{
						String decVal = qty.substring(qty.length() - 2, qty.length());
						if (Double.parseDouble(decVal) == 0)
						{
							qty = qty.substring(0, qty.length() - 2);
						}
					}

					if (saleQty > 0)
					{
						objPOSBillDtl = new clsPOSBillDtl();
						objPOSBillDtl.setDblQuantity(Double.parseDouble(decimalFormat.format(Double.parseDouble(qty))));
						objPOSBillDtl.setDblAmount(Double.parseDouble(objBillDetail[3].toString()));
						objPOSBillDtl.setStrItemName(objBillDetail[1].toString());
						listOfBillDetail.add(objPOSBillDtl);

						String sqlModifier = "select count(*) " + "from " + billModifierdtl + " where strBillNo='" + strBillNo + "' and left(strItemCode,7)='" + objBillDetail[4].toString() + "' " + " and date(dteBillDate)='" + strBillDate + "'";
						if (!strPrintZeroAmtModifierOnBill.equals("N"))
						{
							sqlModifier += " and  dblAmount !=0.00 ";
						}
						List listMod = objBaseService.funGetList(new StringBuilder(sqlModifier), "sql");
						int cntRecord = Integer.parseInt(listMod.get(0).toString());
						if (cntRecord > 0)
						{
							sqlModifier = "select strModifierName,dblQuantity,dblAmount " + " from " + billModifierdtl + " " + " where strBillNo='" + strBillNo + "' and left(strItemCode,7)='" + objBillDetail[4].toString() + "' " + " and date(dteBillDate)='" + strBillDate + "'";
							if (!strPrintZeroAmtModifierOnBill.equals("N"))
							{
								sqlModifier += " and  dblAmount !=0.00 ";
							}
							List listModifier = objBaseService.funGetList(new StringBuilder(sqlModifier), "sql");
							if (listModifier.size() > 0 && listModifier!=null)
							{
								Object[] objMod =null;
								for (int k = 0; k < listModifier.size(); k++)
								{
									objMod = (Object[]) listModifier.get(k);
									if (flgComplimentaryBill)
									{
										
										objPOSBillDtl = new clsPOSBillDtl();
										objPOSBillDtl.setDblQuantity(Double.parseDouble(decimalFormat.format(Double.parseDouble(objMod[1].toString()))));
										objPOSBillDtl.setDblAmount(0);
										objPOSBillDtl.setStrItemName(objMod[0].toString().toUpperCase());
										listOfBillDetail.add(objPOSBillDtl);
									}
									else
									{
										objPOSBillDtl = new clsPOSBillDtl();
										objPOSBillDtl.setDblQuantity(Double.parseDouble(decimalFormat.format(Double.parseDouble(objMod[1].toString()))));
										objPOSBillDtl.setDblAmount(Double.parseDouble(objMod[2].toString()));
										objPOSBillDtl.setStrItemName(objMod[0].toString().toUpperCase());
										listOfBillDetail.add(objPOSBillDtl);
									}
							
								}

							}
						}
					}
				}
				}
				// Discount

				List<clsPOSBillDtl> listOfDiscountDtl = new ArrayList<>();
				String sqlDisc = "select a.dblDiscPer,a.dblDiscAmt,a.strDiscOnType,a.strDiscOnValue,b.strReasonName,a.strDiscRemarks " + "from " + billDiscount + " a ,tblreasonmaster b " + "where  a.strDiscReasonCode=b.strReasonCode " + "and a.strBillNo='" + strBillNo + "' " + " and date(a.dteBillDate)='" + strBillDate + "'";
				List listDisc = objBaseService.funGetList(new StringBuilder(sqlDisc), "sql");
				boolean flag = true;
				if (listDisc.size() > 0)
				{
					for (int i = 0; i < listDisc.size(); i++)
					{
						objBillDetail = (Object[]) listDisc.get(i);
					}
					if (flag)
					{
						objPOSBillDtl = new clsPOSBillDtl();
						objPOSBillDtl.setStrItemName("Discount");
						listOfDiscountDtl.add(objPOSBillDtl);
						flag = false;
					}
					double dbl = Double.parseDouble(objBillDetail[0].toString());
					String discText = String.format("%.1f", dbl) + "%" + " On " + objBillDetail[2].toString() + "";
					if (discText.length() > 30)
					{
						discText = discText.substring(0, 30);
					}
					else
					{
						discText = String.format("%-30s", discText);
					}

					/*
					 * String discountOnItem =
					 * objUtility.funPrintTextWithAlignment
					 * (rsDisc.getString("dblDiscAmt"), 8, "Right");
					 * hm.put("Discount", discText + " " + discountOnItem);
					 */
					objPOSBillDtl = new clsPOSBillDtl();
					objPOSBillDtl.setStrItemName(discText);
					objPOSBillDtl.setDblAmount(Double.parseDouble(objBillDetail[1].toString()));
					listOfDiscountDtl.add(objPOSBillDtl);

					objPOSBillDtl = new clsPOSBillDtl();
					objPOSBillDtl.setStrItemName("Reason :" + " " + objBillDetail[4].toString());
					listOfDiscountDtl.add(objPOSBillDtl);

					objPOSBillDtl = new clsPOSBillDtl();
					objPOSBillDtl.setStrItemName("Remark :" + " " + objBillDetail[5].toString());
					listOfDiscountDtl.add(objPOSBillDtl);
				}

				// Tax
				List<clsPOSBillDtl> listOfTaxDetail = new ArrayList<>();
				String sql_Tax = "select b.strTaxDesc,sum(a.dblTaxAmount),b.strBillNote " + " from " + billtaxdtl + " a,tbltaxhd b " + " where a.strBillNo='" + strBillNo + "' and a.strTaxCode=b.strTaxCode " + " group by a.strTaxCode";
				List listTax = objBaseService.funGetList(new StringBuilder(sql_Tax), "sql");
				if (listTax.size() > 0)
				{
					for (int i = 0; i < listTax.size(); i++)
					{
						objBillDetail = (Object[]) listTax.get(i);

						if (flgComplimentaryBill)
						{
							objPOSBillDtl = new clsPOSBillDtl();
							objPOSBillDtl.setDblAmount(0);
							objPOSBillDtl.setStrItemName(objBillDetail[0].toString());
							listOfTaxDetail.add(objPOSBillDtl);
							hm.put("GSTNo", objBillDetail[2].toString());
						}
						else
						{
							objPOSBillDtl = new clsPOSBillDtl();
							objPOSBillDtl.setDblAmount(Double.parseDouble(objBillDetail[1].toString()));
							objPOSBillDtl.setStrItemName(objBillDetail[0].toString());
							listOfTaxDetail.add(objPOSBillDtl);
							hm.put("GSTNo", objBillDetail[2].toString());
						}
					}
				}

				// add del charges
				double delCharges = dblDelCharge;
				objPOSBillDtl = new clsPOSBillDtl();
				objPOSBillDtl.setDblAmount(delCharges);
				objPOSBillDtl.setStrItemName("Del. Charges");
				if (delCharges > 0)
				{
					listOfTaxDetail.add(objPOSBillDtl);
				}

				List<clsPOSBillDtl> listOfGrandTotalDtl = new ArrayList<>();
				if (dblGrandTotal > 0)
				{
					objPOSBillDtl = new clsPOSBillDtl();
					objPOSBillDtl.setDblAmount(dblGrandTotal);
					listOfGrandTotalDtl.add(objPOSBillDtl);
				}

				List<clsPOSBillDtl> listOfSettlementDetail = new ArrayList<>();
				// settlement breakup part
				String sqlSettlementBreakup = "select a.dblSettlementAmt, b.strSettelmentDesc, b.strSettelmentType " + " from " + billSettlementdtl + " a ,tblsettelmenthd b " + "where a.strBillNo='" + strBillNo + "' and a.strSettlementCode=b.strSettelmentCode" + " and date(a.dteBillDate)='" + strBillDate + "'";
				List listBill_Settlement = objBaseService.funGetList(new StringBuilder(sqlSettlementBreakup), "sql");
				if (listBill_Settlement.size() > 0)
				{
					for (int i = 0; i < listBill_Settlement.size(); i++)
					{
						objBillDetail = (Object[]) listBill_Settlement.get(i);
					}
					if (flgComplimentaryBill)
					{
						objPOSBillDtl = new clsPOSBillDtl();
						objPOSBillDtl.setStrItemName(objBillDetail[1].toString());
						objPOSBillDtl.setDblAmount(0.00);
						listOfSettlementDetail.add(objPOSBillDtl);
					}
					else
					{
						objPOSBillDtl = new clsPOSBillDtl();
						objPOSBillDtl.setStrItemName(objBillDetail[1].toString());
						objPOSBillDtl.setDblAmount(Double.parseDouble(objBillDetail[0].toString()));
						listOfSettlementDetail.add(objPOSBillDtl);
					}
				}

				List<clsPOSBillDtl> listOfServiceVatDetail = funPrintServiceVatNoForJasper(strBillNo, strBillDate, billtaxdtl);
				List<clsPOSBillDtl> listOfFooterDtl = new ArrayList<>();
				objPOSBillDtl = new clsPOSBillDtl();
				objPOSBillDtl.setStrItemName("THANK YOU AND VISIT AGAIN !!!");
				listOfFooterDtl.add(objPOSBillDtl);

				String billType = "Home Delivery";
				hm.put("BillType", billType);
				hm.put("listOfItemDtl", listOfBillDetail);
				hm.put("listOfTaxDtl", listOfTaxDetail);
				hm.put("listOfGrandTotalDtl", listOfGrandTotalDtl);
				hm.put("listOfServiceVatDetail", listOfServiceVatDetail);
				hm.put("listOfFooterDtl", listOfFooterDtl);
				hm.put("listOfDiscountDtl", listOfDiscountDtl);
				hm.put("listOfSettlementDetail", listOfSettlementDetail);
				hm.put("listOfHomeDeliveryDtl", listOfHomeDeliveryDtl);

				hm.put("decimalFormaterForDoubleValue", gDecimalFormatString);
				hm.put("decimalFormaterForIntegerValue", "0");

				List<List<clsPOSBillDtl>> listData = new ArrayList<>();
				listData.add(listOfBillDetail);

				final String printer=objPrinting.funGetPrinterDetails(strPosCode);

				JasperDesign jd = JRXmlLoader.load(servletContext.getResourceAsStream("/WEB-INF/reports/billFormat/rptBillFormat7JasperReport.jrxml"));
				JasperReport jr = JasperCompileManager.compileReport(jd);
				final JasperPrint print = JasperFillManager.fillReport(jr, hm, new JRBeanCollectionDataSource(listData));
                
				new Thread(){
					@Override
					public void run()
					{
						objPrinting.funPrintJasperExporterInThread(print, printer);
					}
				}.start();
				
				
				
				/*				String filePath = System.getProperty("user.dir") + "/downloads/pdf/";
				ServletOutputStream servletOutputStream = response.getOutputStream();
				JRExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, print);
				exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,response.getOutputStream()); // your output goes here
				
				//exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, mainJaperPrint);
				exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
				exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
				exporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=billprint.pdf" );
		*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	public void funPrintJasperExporterInThread(JasperPrint print)
	{

		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();

		int selectedService = 0;
		String billPrinterName = strBillPrinterPort;

		billPrinterName = billPrinterName.replaceAll("#", "\\\\");
		printServiceAttributeSet.add(new PrinterName(billPrinterName, null));

		PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, printServiceAttributeSet);

		try
		{

			JRPrintServiceExporter exporter;
			PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
			printRequestAttributeSet.add(MediaSizeName.NA_LETTER);
			printRequestAttributeSet.add(new Copies(1));

			// these are deprecated
			exporter = new JRPrintServiceExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService[selectedService]);
			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printService[selectedService].getAttributes());
			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
			exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
			exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
			exporter.exportReport();

		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
	}

	
	public boolean funIsDirectBillerBill(String billNo, String billhd)
	{
		boolean flgIsDirectBillerBill = false;
		try
		{
			Object[] objDB = null;
			String sql_checkDirectBillerBill = "select strTableNo,strOperationType " + " from " + billhd + " where strBillNo='" + billNo + "'  ";
			List list = objBaseService.funGetList(new StringBuilder(sql_checkDirectBillerBill), "sql");
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					objDB = (Object[]) list.get(0);
				}
				if (objDB[0].toString() != null && objDB[0].toString().trim().isEmpty())
				{
					flgIsDirectBillerBill = true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return flgIsDirectBillerBill;
	}

	public List<clsPOSBillDtl> funPrintServiceVatNoForJasper(String billNo, String billDate, String billTaxDtl) throws IOException
	{
		List<clsPOSBillDtl> listOfServiceVatDetail = new ArrayList<>();
		clsPOSBillDtl objBillDtl = null;
		/* clsUtility objUtility = new clsUtility(); */
		Map<String, String> mapBillNote = new HashMap<>();

		try
		{
			Object[] obj = null;
			String billNote = "";
			String sql = "select a.strTaxCode,a.strTaxDesc,a.strBillNote " + "from tbltaxhd a," + billTaxDtl + " b " + "where a.strTaxCode=b.strTaxCode " + "and b.strBillNo='" + billNo + "' " + "and date(b.dteBillDate)='" + billDate + "' " + "order by a.strBillNote ";
			List rsBillNote = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (rsBillNote.size() > 0)
			{
				for (int i = 0; i < rsBillNote.size(); i++)
				{
					obj = (Object[]) rsBillNote.get(i);

				}
				billNote = obj[2].toString().trim();
				if (!billNote.isEmpty())
				{
					mapBillNote.put(billNote, billNote);
				}
			}

			sql = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos,a.dblGrandTotal,b.strBillNote " + "from tblbillseriesbilldtl a,tblbillseries b " + "where a.strBillSeries=b.strBillSeries " + "and a.strHdBillNo='" + billNo + "' " + "and date(a.dteBillDate)='" + billDate + "' ";
			rsBillNote = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (rsBillNote.size() > 0)
			{
				for (int i = 0; i < rsBillNote.size(); i++)
				{
					obj = (Object[]) rsBillNote.get(i);

				}
				billNote = obj[5].toString().trim();
				if (!billNote.isEmpty())
				{
					mapBillNote.put(billNote, billNote);
				}
			}

			for (String printBillNote : mapBillNote.values())
			{
				objBillDtl = new clsPOSBillDtl();
				objBillDtl.setStrItemName(printBillNote);
				listOfServiceVatDetail.add(objBillDtl);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return listOfServiceVatDetail;
	}
}
