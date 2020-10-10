package com.sanguine.webpos.printing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sanguine.base.service.intfBaseService;
import com.sanguine.webpos.bean.clsPOSBillDtl;
import com.sanguine.webpos.util.clsDatabaseConnection;

@Controller

public class clsPrintingUtility
{
    @Autowired
    intfBaseService objBaseService;
    @Autowired
    clsDatabaseConnection objDataCon;
	public String funGetPrinterDetails(String strPosCode)
	{
		String strBillPrinterPort="";
		try
		{
			String sql = "select strBillPrinterPort,strAdvReceiptPrinterPort from tblposmaster where strPOSCode='" + strPosCode + "'";
			List listPrint = objBaseService.funGetList(new StringBuilder(sql), "sql");
			if (listPrint != null)
			{
				for (int i = 0; i < listPrint.size(); i++)
				{
					Object[] objPrint = (Object[]) listPrint.get(i);
				    strBillPrinterPort = objPrint[0].toString();
					
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return strBillPrinterPort;
	}

	public void funPrintJasperExporterInThread(JasperPrint print,String billPrinterName)
    {

		 PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		
		 int selectedService = 0;
	     //String billPrinterName = strBillPrinterPort;
	/*	 String filePath = System.getProperty("user.dir");
		    String filename = "";
		    filename = (filePath + "/Temp/TempBill1.pdf");
		    */
	     billPrinterName = billPrinterName.replaceAll("#", "\\\\");
	     printServiceAttributeSet.add(new PrinterName(billPrinterName, null));

	     PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, printServiceAttributeSet);

	     try {
	       
	    	    JRPrintServiceExporter exporter;
	    	    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
	    	    printRequestAttributeSet.add(MediaSizeName.NA_LETTER);
	    	    printRequestAttributeSet.add(new Copies(1));

	    	    // these are deprecated
	    	    exporter = new JRPrintServiceExporter();
	    	    exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
	    	    if(printService.length>0){
	    	    	exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService[selectedService]);
		    	    exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printService[selectedService].getAttributes());
		    	    
	    	    }
	    	    exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
	    	    exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
	    	    exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
	    	    exporter.exportReport();
	    	    
	    	    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
	    	    if(printService.length>0){
	    	    	DocPrintJob job = printService[0].createPrintJob();	
	    	    }
	   		    /*FileInputStream fis = new FileInputStream(filename);
	   		    DocAttributeSet das = new HashDocAttributeSet();
	   		    Doc doc = new SimpleDoc(fis, flavor, das);*/
	   		    //job.print(doc, printerReqAtt);

	     } catch (Exception e) {
	    	 
	    	 e.printStackTrace();
	     }
		
	     
	 
    }
	@SuppressWarnings("finally")
	public boolean funIsDirectBillerBill(String billNo, String billhd)
    {
	boolean flgIsDirectBillerBill = false;
	try
	{
	    String sql_checkDirectBillerBill = "select ifnull(strTableNo,''),strOperationType "
		    + " from " + billhd + " where strBillNo='"+billNo+"'  ";
	    //pst.setString(1, billhd);
	    List list= objBaseService.funGetList(new StringBuilder(sql_checkDirectBillerBill), "sql");
	    if (list.size()>0 && list!=null)
	    {
	    	Object[] obj=(Object[])list.get(0);
			if (obj[0].toString().trim().isEmpty())
			{
			    flgIsDirectBillerBill = true;
			}
			
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally{
		return flgIsDirectBillerBill;

	}
    }
	public int funPrintTakeAwayForJasper(String billHdTableName, String billNo) throws Exception
    {
		int res = 0;
		String sqlTakeAway = "select strOperationType from " + billHdTableName + " "
			+ " where strBillNo='" + billNo + "'";
		ResultSet rsBill = objDataCon.funGetResultSet(sqlTakeAway);
		if (rsBill.next())
		{
		    if (rsBill.getString(1).equals("TakeAway"))
		    {
			res = 1;
		    }
		}
		rsBill.close();
		return res;
    }
	  public List<clsPOSBillDtl> funPrintServiceVatNoForJasper(String billNo, String billDate, String billTaxDtl) throws IOException
	    {
		List<clsPOSBillDtl> listOfServiceVatDetail = new ArrayList<>();
		clsPOSBillDtl objBillDtl = null;
		Map<String, String> mapBillNote = new HashMap<>();

		try
		{
		    String billNote = "";
		    String sql = "select a.strTaxCode,a.strTaxDesc,a.strBillNote "
			    + "from tbltaxhd a," + billTaxDtl + " b "
			    + "where a.strTaxCode=b.strTaxCode "
			    + "and b.strBillNo='" + billNo + "' "
			    + "and date(b.dteBillDate)='" + billDate + "' "
			    + "order by a.strBillNote ";
		    ResultSet rsBillNote = objDataCon.funGetResultSet(sql);
		    while (rsBillNote.next())
		    {
			billNote = rsBillNote.getString(3).trim();
			if (!billNote.isEmpty())
			{
			    mapBillNote.put(billNote, billNote);
			}

		    }
		    rsBillNote.close();

		    sql = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos,a.dblGrandTotal,b.strBillNote "
			    + "from tblbillseriesbilldtl a,tblbillseries b "
			    + "where a.strBillSeries=b.strBillSeries "
			    + "and a.strHdBillNo='" + billNo + "' "
			    + "and date(a.dteBillDate)='" + billDate + "' ";
		    rsBillNote = objDataCon.funGetResultSet(sql);
		    if (rsBillNote.next())
		    {
			billNote = rsBillNote.getString(6).trim();
			if (!billNote.isEmpty())
			{
			    mapBillNote.put(billNote, billNote);
			}

		    }
		    rsBillNote.close();

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

	  
	  public void funCreateTempFolder()
	    {
		try
		{
		    String filePath = System.getProperty("user.dir");
		    File Text_KOT = new File(filePath + "/Temp");
		    if (!Text_KOT.exists())
		    {
			   Text_KOT.mkdirs();
		    }
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	  public void funPrintBlankSpace(String printWord, BufferedWriter BWOut,long columnSize)
	    {
		try
		{
		    int wordSize = printWord.length();
		    int actualPrintingSize = Integer.parseInt(String.valueOf(columnSize));
		    int availableBlankSpace = actualPrintingSize - wordSize;

		    int leftSideSpace = availableBlankSpace / 2;
		    if (leftSideSpace > 0)
		    {
			for (int i = 0; i < leftSideSpace; i++)
			{
			    BWOut.write(" ");
			}
		    }

		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	  public int funPrintContentWithSpace(String align, String textToPrint, int totalLength, BufferedWriter pw) throws Exception
	    {
		int len = totalLength - textToPrint.trim().length();
		if (align.equalsIgnoreCase("Left"))
		{
		    pw.write(textToPrint.trim());
		    for (int cnt = 0; cnt < len; cnt++)
		    {
			pw.write(" ");
		    }
		}
		else if (align.equalsIgnoreCase("Right"))
		{
		    for (int cnt = 0; cnt < len; cnt++)
		    {
			pw.write(" ");
		    }
		    pw.write(textToPrint.trim());
		}

		return 1;
	    }
	  public void funWriteToTextMemberNameForFormat5(BufferedWriter out, String memberName, String format)
	    {
		try
		{
		    int counter = 0;
		    counter = counter + 2;
		    //Item Write 
		    String tempItemName = memberName;
		    int length = tempItemName.length();
		    if (length < 25)
		    {
			out.write(tempItemName);
			counter = counter + length;
		    }
		    else
		    {
			String partOne = tempItemName.substring(0, 24);
			out.write(partOne);
			counter = counter + partOne.length();
			String partTwo = tempItemName.substring(24, length - 1);
			out.newLine();
			out.write("                " + partTwo);
		    }
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	  public void funWriteTotal(String title, String total, BufferedWriter out, String format)
	    {
		try
		{
//		    if(format.equalsIgnoreCase("TAX"))
//		    {
//			out.write("  ");
//			out.write(title);
//			funWriteFormattedAmt(0, total, out, format);
//		       
//		    }
//		    else
//		    {	
			int counter = 0;
			out.write("  ");
			counter = counter + 2;
			int length = title.length();
			out.write(title);
			counter = counter + length;
			funWriteFormattedAmt(counter, total, out, format);
		    //}
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }

	  public void funWriteTotalStationery(String title, String total, BufferedWriter out, String format, String space)
	    {
		try
		{
		    int counter = 0;
		    out.write(space);
		    counter = counter + 2;
		    int length = title.length();
		    out.write(title);
		    counter = counter + length;
		    funWriteFormattedAmt(counter, total, out, format);
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	  private void funWriteFormattedAmt(int counter, String Amount, BufferedWriter out, String format)
	    {
		try
		{
		    int space = 30;

		    if (format.equals("Format1"))
		    {
			space = 29;
		    }
		    if (format.equals("Format3"))
		    {
			space = 29;
		    }
		    if (format.equals("Format4"))
		    {
			space = 34;
		    }
		    if (format.equals("Format5"))
		    {
			space = 29;
		    }
		    if (format.equals("Format6"))
		    {
			space = 30;
		    }
		    if (format.equals("Format11"))
		    {
			space = 12;
		    }
		    if (format.equals("Format13"))
		    {
			space = 29;
		    }
		    String tempAmount = Amount;

		    int length = tempAmount.length();
		    int usedSpace = space - counter;
		    if(format.equalsIgnoreCase("TAX"))//for tax to print
		    {
			usedSpace=22-counter;
			int width1=7;
			String[] dblAmount=tempAmount.split(" ");
			if(dblAmount[0].length()==8)
			{
			    width1=5;
			    usedSpace=21-counter;
			}  
			if(dblAmount[0].length()==9)
			{
			    width1=5;
			    usedSpace=20-counter;
			}  
			String dblAmount1=funPrintTextWithAlignment(dblAmount[0], width1, "right");
			String dblAmount2=funPrintTextWithAlignment(dblAmount[4], 10, "right");
			tempAmount=dblAmount1+dblAmount2;
			length=30;
		    }
		   
		    for (int i = 0; i < usedSpace; i++)
		    {
			out.write(" ");
		    }
		     
		    if(counter!=32 && counter!=0 )//for COMPLIMENTARY BILLS we dont need space 
		    {
			 out.write("  ");
		    }
		   
		    switch (length)
		    {
			case 1:
			    out.write("        " + tempAmount);//8
			    break;
			case 2:
			    out.write("       " + tempAmount);//7
			    break;
			case 3:
			    out.write("      " + tempAmount);//6
			    break;
			case 4:
			    out.write("     " + tempAmount);//5
			    break;
			case 5:
			    out.write("    " + tempAmount);//4
			    break;
			case 6:
			    out.write("   " + tempAmount);//3
			    break;
			case 7:
			    out.write("  " + tempAmount);//2
			    break;
			case 8:
			    out.write(" " + tempAmount);//1
			    break;
			case 9:
			    out.write(tempAmount);//0
			    break;
			default:
			    out.write(tempAmount);//0
			    break;
		    }

		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	   public String funPrintTextWithAlignment(String text, int totalLength, String alignment)
	    {
		StringBuilder sbText = new StringBuilder();
		if (alignment.equalsIgnoreCase("Center"))
		{
		    int textLength = text.length();
		    int totalSpace = (totalLength - textLength) / 2;

		    for (int i = 0; i < totalSpace; i++)
		    {
			sbText.append(" ");
		    }
		    sbText.append(text);
		}
		else if (alignment.equalsIgnoreCase("Left")) 
		{
		    sbText.setLength(0);
		    int textLength = text.length();
		    int totalSpace = (totalLength - textLength);
		    sbText.append(text);
		    for (int i = 0; i < totalSpace; i++)
		    {
			sbText.append(" ");
		    }
		}
		else
		{
		    sbText.setLength(0);
		    int textLength = text.length();
		    int totalSpace = (totalLength - textLength);
		    for (int i = 0; i < totalSpace; i++)
		    {
			sbText.append(" ");
		    }
		    sbText.append(text);
		}

		return sbText.toString();
	    }
	   public void funPrintServiceVatNo(BufferedWriter objBillOut, int billPrinteSize, String billNo, String billDate, String billTaxDtl) throws IOException
	    {
		Map<String, String> mapBillNote = new HashMap<>();

		try
		{
		    String billNote = "";
		    String sql = "select a.strTaxCode,a.strTaxDesc,a.strBillNote "
			    + "from tbltaxhd a," + billTaxDtl + " b "
			    + "where a.strTaxCode=b.strTaxCode "
			    + "and b.strBillNo='" + billNo + "' "
			    + "and date(b.dteBillDate)='" + billDate + "' "
			    + "order by a.strBillNote ";
		    ResultSet rsBillNote = objDataCon.funGetResultSet(sql);
		    while (rsBillNote.next())
		    {
			billNote = rsBillNote.getString(3).trim();
			if (!billNote.isEmpty())
			{
			    mapBillNote.put(billNote, billNote);
			}
		    }
		    rsBillNote.close();

		    sql = "select a.strPOSCode,a.strBillSeries,a.strHdBillNo,a.strDtlBillNos,a.dblGrandTotal,b.strBillNote "
			    + "from tblbillseriesbilldtl a,tblbillseries b "
			    + "where a.strBillSeries=b.strBillSeries "
			    + "and a.strHdBillNo='" + billNo + "' "
			    + "and date(a.dteBillDate)='" + billDate + "' ";
		    rsBillNote = objDataCon.funGetResultSet(sql);
		    if (rsBillNote.next())
		    {
			billNote = rsBillNote.getString(6).trim();
			if (!billNote.isEmpty())
			{
			    mapBillNote.put(billNote, billNote);
			}

		    }
		    rsBillNote.close();

		    for (String printBillNote : mapBillNote.values())
		    {
			objBillOut.write("  " + printBillNote);
			objBillOut.newLine();
		    }

		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }

	   public String funPrintTextBill(String BillPrinterName,String file)
	    {
	    	String result="";
	    	String filename =file;
	    	
	    	
			try
			{
			    int printerIndex = 0;
			    String printerStatus = "Not Found";
			    // System.out.println("Primary Name="+primaryPrinterName);
			    // System.out.println("Sec Name="+secPrinterName);
			    
			    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			    BillPrinterName = BillPrinterName.replaceAll("#", "\\\\");
			
			    
			    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
			    for (int i = 0; i < printService.length; i++)
			    {
					//System.out.println("Service=" + printService[i].getName() + "\tPrim P=" + primaryPrinterName);
					String printerServiceName = printService[i].getName();
					
					if (BillPrinterName.equalsIgnoreCase(printerServiceName))
					{
					    System.out.println("Print send on \t" + BillPrinterName + " printer ");
					    printerIndex = i;
					    printerStatus = "Found";
					    break;
					}
					else
					{
						// System.out.println("Primary Printer not found");
						 result="Bill Printer Not Found";
					}
			    }
			    
			    if (printerStatus.equals("Found"))
			    {
			    	DocPrintJob job = printService[printerIndex].createPrintJob();
					FileInputStream fis = new FileInputStream(filename);
					DocAttributeSet das = new HashDocAttributeSet();
					Doc doc = new SimpleDoc(fis, flavor, das);					
					job.print(doc, pras);// 1 st print normal on primary
					System.out.println("Successfully Print on " + BillPrinterName);
					String printerInfo = "";
					result="Bill Printer Found";
					
					
			    }
			   
			}
			catch (Exception e)
			{
			    
			    e.printStackTrace();
			   
			}
			
			return result;
	    }
	   private String funPrintOnSecPrinter(String secPrinterName, String fileName,String primaryPrinterStatus) throws Exception
	    {
	    	String result="";
			String printerStatus = "Not Found";
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
			int printerIndex = 0;
			for (int i = 0; i < printService.length; i++)
			{
			    //System.out.println("Service="+printService[i].getName()+"\tSec P="+secPrinterName);
			    String printerServiceName = printService[i].getName();
			    
			    if (secPrinterName.equalsIgnoreCase(printerServiceName))
			    {
					System.out.println("Sec Printer=" + secPrinterName + "found");
					printerIndex = i;
					printerStatus = "Found";
					break;
			    }
			    else
				{
			    	// System.out.println("Secondary Printer not found");
					 result=primaryPrinterStatus+"#"+"Secondary Not Found";
				}
			}
			if (printerStatus.equals("Found"))
			{
			    String printerInfo = "";
			    DocPrintJob job = printService[printerIndex].createPrintJob();
			    FileInputStream fis = new FileInputStream(fileName);
			    DocAttributeSet das = new HashDocAttributeSet();
			    Doc doc = new SimpleDoc(fis, flavor, das);
			    job.print(doc, pras);
			    
			    result=primaryPrinterStatus+"#"+"Secondary Found";
			}
			else
			{
			    System.out.println("funPrintOnSecPrinter = Printer Not Found " + secPrinterName);
		    }
		
			return result;
			
	    }

	    
	    private void funAppendDuplicate(String fileName)
	    {
		try
		{
		    File fileKOTPrint = new File(fileName);
		    String filePath = System.getProperty("user.dir");
		    filePath += "/Temp/Temp_KOT2.txt";
		    File fileKOTPrint2 = new File(filePath);
		    BufferedWriter KotOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileKOTPrint2), "UTF8"));
		    funPrintBlankSpace("DUPLICATE", KotOut, 40);
		    KotOut.write("[DUPLICATE]");
		    KotOut.newLine();

		    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileKOTPrint)));
		    String line = null;
		    while ((line = br.readLine()) != null)
		    {
			KotOut.write(line);
			KotOut.newLine();
		    }
		    br.close();
		    KotOut.close();

		    String content = new String(Files.readAllBytes(Paths.get(filePath)));
		    Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);

		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
		
		public void funPrintBlankSpace(String printWord, BufferedWriter objBWriter, int actualPrintingSize) {
	        try {
	            int wordSize = printWord.length();
	            int availableBlankSpace = actualPrintingSize - wordSize;
	            
	            int leftSideSpace = availableBlankSpace / 2;
	            if (leftSideSpace > 0) {
	                for (int i = 0; i < leftSideSpace; i++) {
	                	objBWriter.write(" ");
	                }
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		 private String funPrintKOTWindows(String primaryPrinterName, String secPrinterName,String KOTType,String printOnBothPrinter,int noOfCopiesPrimaryPrinter,int noOfCopiesSecPrinter,String reprint)
		    {
		    	String result="";
		    	String filePath = System.getProperty("user.dir");
		    	String filename ="";
		    	
		    	if(KOTType.equals("MasterKOT"))
		    	{
		    		filename = (filePath + "/Temp/Temp_Master_KOT.txt");
		    	}
		    	else if(KOTType.equals("CheckKOT"))
		    	{
		    		filename = (filePath + "/Temp/Temp_Check_KOT.txt");
		    	}
		    	else if(KOTType.equals("ConsolidateKOT"))
		    	{
		    		filename = (filePath + "/Temp/Temp_Consolidated_KOT.txt");
		    	}
		    	else if(KOTType.equals("KOT"))
		    	{
		    		filename = (filePath + "/Temp/Temp_KOT.txt");
		    	}
		    	else if(KOTType.equals("Test"))
		    	{
		    		filename = (filePath + "/Temp/Test_Print.txt");
		    	}
			
				try
				{
				    int printerIndex = 0;
				    String printerStatus = "Not Found";
				    // System.out.println("Primary Name="+primaryPrinterName);
				    // System.out.println("Sec Name="+secPrinterName);
				    
				    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
				    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
				    primaryPrinterName = primaryPrinterName.replaceAll("#", "\\\\");
				    secPrinterName = secPrinterName.replaceAll("#", "\\\\");
				
				    
				    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
				    for (int i = 0; i < printService.length; i++)
				    {
						//System.out.println("Service=" + printService[i].getName() + "\tPrim P=" + primaryPrinterName);
						String printerServiceName = printService[i].getName();
						
						if (primaryPrinterName.equalsIgnoreCase(printerServiceName))
						{
						    System.out.println("Print send on \t" + primaryPrinterName + " printer ");
						    printerIndex = i;
						    printerStatus = "Found";
						    break;
						}
						else
						{
							// System.out.println("Primary Printer not found");
							 result="Primary Not Found";
						}
				    }
				    
				    if (printerStatus.equals("Found"))
				    {
				    	DocPrintJob job = printService[printerIndex].createPrintJob();
						FileInputStream fis = new FileInputStream(filename);
						DocAttributeSet das = new HashDocAttributeSet();
						Doc doc = new SimpleDoc(fis, flavor, das);
						
						job.print(doc, pras);// 1 st print normal on primary
						
						if(printOnBothPrinter.equalsIgnoreCase("Y")){
							if(noOfCopiesSecPrinter>0){
								result=funPrintOnSecPrinter(secPrinterName, filename,result); //for printing single print
								/*if(!reprint.equalsIgnoreCase("Reprint")){
									reprint="Reprint";
									funAppendDuplicate(filename);	
								}*/
								reprint="Reprint";
								funAppendDuplicate(filename);
								
								for(int i=0;i<noOfCopiesSecPrinter-1;i++){
									job = printService[printerIndex].createPrintJob();
									fis = new FileInputStream(filename);
									das = new HashDocAttributeSet();
									doc = new SimpleDoc(fis, flavor, das);
									result=funPrintOnSecPrinter(secPrinterName, filename,result); //for printing on both printer	
								}
									
							}
							 
						}
						if(noOfCopiesPrimaryPrinter>1){
							if(!reprint.equalsIgnoreCase("Reprint")){
								reprint="Reprint";
								funAppendDuplicate(filename);
							}
							
							for(int i=0;i<noOfCopiesPrimaryPrinter-1;i++){
								job = printService[printerIndex].createPrintJob();
								fis = new FileInputStream(filename);
								das = new HashDocAttributeSet();
								doc = new SimpleDoc(fis, flavor, das);
								
								job.print(doc, pras);	//All primary 
							}	
							
						}
						
						System.out.println("Successfully Print on " + primaryPrinterName);
						String printerInfo = "";
						
						 result="Primary Found";
						
						
				    }
				    else
				    {
				    	for(int j=0;j<noOfCopiesSecPrinter;j++){
				    		//funAppendDuplicate(filename);
					    	result=funPrintOnSecPrinter(secPrinterName, filename,result);	
				    	}
				    	
				    }
				    
				}
				catch (Exception e)
				{
				    
				    e.printStackTrace();
				    try
				    {
				    	result=funPrintOnSecPrinter(secPrinterName, filename,result);
				    }
				    catch (Exception ex)
				    {
					ex.printStackTrace();
				    }
				}
				
				return result;
		    }
/*	  public int funPrintPromoItemsInBill(String billNo, int billPrintSize, List<clsBillDtl> listOfBillDetail) throws Exception
	    {
		String sqlBillPromoDtl = "select b.strItemName,a.dblQuantity,'0',dblRate "
			+ " from tblbillpromotiondtl a,tblitemmaster b "
			+ " where a.strItemCode=b.strItemCode and a.strBillNo='" + billNo + "' and a.strPromoType!='Discount' ";
		ResultSet rsBillPromoItemDtl = clsGlobalVarClass.dbMysql.executeResultSet(sqlBillPromoDtl);
		clsBillDtl objBillDtl = null;
		while (rsBillPromoItemDtl.next())
		{
		    objBillDtl = new clsBillDtl();
		    objBillDtl.setDblQuantity(rsBillPromoItemDtl.getDouble(2));
		    objBillDtl.setDblAmount(rsBillPromoItemDtl.getDouble(3));
		    objBillDtl.setStrItemName(rsBillPromoItemDtl.getString(1).toUpperCase());
		    listOfBillDetail.add(objBillDtl);
		}
		rsBillPromoItemDtl.close();
		return 1;
	    }
*/

}
