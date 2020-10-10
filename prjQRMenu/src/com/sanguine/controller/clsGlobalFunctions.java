package com.sanguine.controller;

import java.io.InputStream;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;

import com.mysql.jdbc.Connection;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;

import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.sevice.clsPOSMasterService;
import com.sanguine.webpos.util.clsPOSSetupUtility;

@Controller
public class clsGlobalFunctions
{
	public String currentDateTime,
			currentDate;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;


	@Autowired
	private ServletContext servletContext;


	@Autowired
	private clsPOSMasterService objMasterService;
	
	@Autowired
	static clsPOSSetupUtility objPOSSetupUtility;
	

	public final String NARRATION = "strNarration";
	public final String COMPANY_NAME = "strCompanyName";
	public final String USER_CODE = "strUserCode";
	public final String AGAINST = "strAgainst";
	public final String AUTHORISE = "strAuthorise";
	public final String FROM_LOCATION = "strFromLoc";
	public final String TO_LOCATION = "strToLoc";
	public final String FROM_DATE = "dtFromDate";
	public final String TO_DATE = "dtToDate";
	Map<String, Double> hmChildNodes;

	/*
	 * @Value("${database.urlWebStocks}") public String conUrl;
	 * 
	 * @Value("${database.urlExcise}") String urlExcise;
	 * 
	 * @Value("${database.urlCRM}") public String urlCRM;
	 * 
	 * @Value("${database.urlwebbooks}") public String urlwebbooks;
	 * 
	 * @Value("${database.urlwebclub}") public String urlwebclub;
	 * 
	 * @Value("${database.user}") public String urluser;
	 * 
	 * @Value("${database.password}") public String urlPassword;
	 */

	public static String conUrl;
	public static String urlExcise;
	public static String urlCRM;
	public static String urlwebbooks;
	public static String urlwebclub;
	public static String urlwebpms;
	public static String urluser;
	public static String urlPassword;

	public List listChildNodes1;

	public static String POSWSURL = "http://localhost:8080/prjSanguineWebService";

	static
	{

		try
		{

			Properties prop = new Properties();
			Resource resource = new ClassPathResource("resources/database.properties");
			InputStream input = resource.getInputStream();
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			// conUrl = prop.getProperty("database.urlWebStocks");

			// changed for WebPOS connection
			conUrl = prop.getProperty("database.urlWebPOS");
			urluser = prop.getProperty("database.user");
			urlPassword = prop.getProperty("database.password");

			input.close();
			resource.exists();

		}
		catch (Exception e)
		{
			System.out.println("Property File Not Found in Global Functions");
			e.printStackTrace();
		}
	}

	/*public long funGetLastNo(String tableName, String masterName, String columnName)
	{
		return objGlobalFunctionsService.funGetLastNo(tableName, masterName, columnName, "");
	}*/

	public long funCompareTime(Date d2, Date d1)
	{
		long diff = 0;
		try
		{
			diff = d2.getTime() - d1.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			String time = diffHours + ":" + diffMinutes + ":" + diffSeconds;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			return diff;
		}
	}

	public String funGetCurrentDateTime(String pattern)
	{
		Date dt = new Date();
		if (pattern.equals("yyyy-MM-dd"))
		{
			currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
		}
		else
		{
			currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dt);
		}
		return currentDateTime;
	}

	public String funGetCurrentDate(String pattern)
	{
		Date dt = new Date();
		currentDate = new SimpleDateFormat(pattern).format(dt);
		return currentDate;
	}

	public String funGetDate(String pattern, String date)
	{
		String retDate = date;

		if (pattern.equals("yyyy-MM-dd")) // From JSP to Database yyyy-MM-dd
		{
			String[] spDate = date.split("-");
			retDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
		}
		else if (pattern.equals("yyyy/MM/dd")) // From Database to JSP
		{
			String[] sp = date.split(" ");
			String[] spDate = sp[0].split("-");
			retDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
		}
		else if (pattern.equals("dd-MM-yyyy")) // For Jasper Report show
		{
			String[] sp = date.split(" ");
			String[] spDate = sp[0].split("-");
			retDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
		}
		return retDate;
	}

	public long funGetMonth()
	{
		return new Date().getMonth();
	}

	public String funGetAlphabet(int no)
	{
		String[] alphabets = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		return alphabets[no];
	}

	public String[] funGetAlphabetSet()
	{
		String[] alphabetSet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		return alphabetSet;
	}

	@SuppressWarnings("deprecation")
	public String funGetTransactionCode(String initCode, String propCode, String startYear)
	{
		String transCode = "";
		transCode = propCode;
		Date dt = new Date();
		String years = String.valueOf((dt.getYear() + 1900) - Integer.parseInt(startYear));
		transCode = propCode + initCode + funGetAlphabet(Integer.parseInt(years)) + funGetAlphabet(dt.getMonth());
		return transCode;
	}

	public String funIfNull(String input, String defaultValue, String assignedValue)
	{
		String op = "notnull";
		if (null == input)
		{
			op = defaultValue;
		}
		else if(input=="null")
		{
			op = defaultValue;
		}
		else
		{
			op = assignedValue;
		}
		return op;
	}

	public Date funStringToDate(String strDate)
	{

		Date date = new Date();
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			date = formatter.parse(strDate);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return date;
	}

	/*@SuppressWarnings("rawtypes")
	public List funGetList(String query)
	{
		return objGlobalFunctionsService.funGetList(query);
	}*/

	public Object funSetDefaultValue(Object value, Object defaultValue)
	{
		if (value != null && (value instanceof String && value.toString().length() > 0))
		{
			return value;
		}
		else if (value != null && !(value instanceof String))
		{
			return value;
		}
		else
		{
			return defaultValue;
		}
		// return value !=null && (value instanceof String &&
		// value.toString().length()>0) ? value : defaultValue;
	}

	// you are login in which model that will connect you of that database
	public Connection funGetConnection(HttpServletRequest req)
	{
		Connection con = null;
		String modelno = req.getSession().getAttribute("moduleNo").toString();

		try
		{
			switch (modelno)
			{
			case "1":
				con = (Connection) DriverManager.getConnection(conUrl + "?user=" + urluser + "&password=" + urlPassword);
				break;

			case "2":
				con = (Connection) DriverManager.getConnection(urlExcise + "?user=" + urluser + "&password=" + urlPassword);
				// con = (Connection)
				// DriverManager.getConnection("jdbc:mysql://localhost:3306/dbwebexcise?user=root&password=root");
				break;

			case "3":
				con = (Connection) DriverManager.getConnection(urlwebpms + "?user=" + urluser + "&password=" + urlPassword);
				break;

			case "4":
				con = (Connection) DriverManager.getConnection(urlwebclub + "?user=" + urluser + "&password=" + urlPassword);
				break;

			case "5":
				con = (Connection) DriverManager.getConnection(urlwebbooks + "?user=" + urluser + "&password=" + urlPassword);
				break;

			case "6":
				con = (Connection) DriverManager.getConnection(conUrl + "?user=" + urluser + "&password=" + urlPassword);
				break;

			// case "7":
			// con = (Connection)
			// DriverManager.getConnection("jdbc:mysql://localhost:3306/dbwebmms?user=root&password=root");
			// break;

			default:
				con = (Connection) DriverManager.getConnection(conUrl + "?user=" + urluser + "&password=" + urlPassword);
				break;
			}
		}
		catch (Exception e)
		{
			System.out.println("Error in conection");
		}
		return con;
	}

	@SuppressWarnings("rawtypes")
	public boolean funCheckName(String Name, String strClientCode, String formName)
	{
		List countList = objGlobalFunctionsService.funCheckName(Name, strClientCode, formName);
		int count = Integer.parseInt(countList.get(0).toString());
		if (count > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private boolean funCheckTaxOnSubGroup(String taxOnSubGroup, String productCode, String taxCode)
	{
		boolean flgTaxOnSG = false;

		if (taxOnSubGroup.equalsIgnoreCase("N"))
		{
			flgTaxOnSG = true;
		}
		else
		{
			String sql = "select strSGCode from tblproductmaster " + " where strProdCode='" + productCode + "' ";
			List listProdSG = objGlobalFunctionsService.funGetList(sql, "sql");
			if (listProdSG.size() > 0)
			{
				String SGCode = (String) listProdSG.get(0);
				sql = "select * from tbltaxsubgroupdtl " + " where strTaxCode='" + taxCode + "' and strSGCode='" + SGCode + "' ";
				List listTaxSG = objGlobalFunctionsService.funGetList(sql, "sql");
				if (listTaxSG.size() > 0)
				{
					flgTaxOnSG = true;
				}
			}
		}

		return flgTaxOnSG;
	}

	private double funGetTaxAmtForTaxOnTaxCal(Map<String, clsTaxDtl> hmTaxDtl, String taxOnTaxCode)
	{
		double taxAmtForTaxOnTax = 0;

		if (null != hmTaxDtl)
		{
			String[] spArrTaxOnTaxCode = taxOnTaxCode.split(",");

			for (int cnt = 0; cnt < spArrTaxOnTaxCode.length; cnt++)
			{
				if (null != hmTaxDtl.get(spArrTaxOnTaxCode[cnt]))
				{
					clsTaxDtl objTaxDtl = hmTaxDtl.get(spArrTaxOnTaxCode[cnt]);
					taxAmtForTaxOnTax += objTaxDtl.getTaxAmt();
				}
			}
		}

		return taxAmtForTaxOnTax;
	}

	// Function used to sort HashMap on hashmap values

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap funSortByValues(HashMap map)
	{
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}

	private class clsTaxDtl
	{
		private String taxCode;

		private String taxName;

		private String taxCal;

		private double taxableAmt;

		private double taxAmt;

		private double taxPer;

		private String strTaxShorName;

		public String getTaxCode()
		{
			return taxCode;
		}

		public void setTaxCode(String taxCode)
		{
			this.taxCode = taxCode;
		}

		public String getTaxName()
		{
			return taxName;
		}

		public void setTaxName(String taxName)
		{
			this.taxName = taxName;
		}

		public String getTaxCal()
		{
			return taxCal;
		}

		public void setTaxCal(String taxCal)
		{
			this.taxCal = taxCal;
		}

		public double getTaxableAmt()
		{
			return taxableAmt;
		}

		public void setTaxableAmt(double taxableAmt)
		{
			this.taxableAmt = taxableAmt;
		}

		public double getTaxAmt()
		{
			return taxAmt;
		}

		public void setTaxAmt(double taxAmt)
		{
			this.taxAmt = taxAmt;
		}

		public double getTaxPer()
		{
			return taxPer;
		}

		public void setTaxPer(double taxPer)
		{
			this.taxPer = taxPer;
		}

		public String getStrTaxShorName()
		{
			return strTaxShorName;
		}

		public void setStrTaxShorName(String strTaxShorName)
		{
			this.strTaxShorName = strTaxShorName;
		}

	}




	public  String funGetGlobalDecimalFormatString(String strClientCode, String POSCode)
	{
		StringBuilder decimalFormatBuilderForDoubleValue = new StringBuilder("0");
		try{
			clsSetupHdModel objSetupHdModel=objMasterService.funGetPOSWisePropertySetup(strClientCode,POSCode);
			int gNoOfDecimalPlace = objSetupHdModel.getDblNoOfDecimalPlace();
			for (int i = 0; i < gNoOfDecimalPlace; i++)
			{
				if (i == 0)
				{
					decimalFormatBuilderForDoubleValue.append(".0");
				}
				else
				{
					decimalFormatBuilderForDoubleValue.append("0");
				}
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return decimalFormatBuilderForDoubleValue.toString();
	}

}
