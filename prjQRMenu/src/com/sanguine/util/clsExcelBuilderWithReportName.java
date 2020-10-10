package com.sanguine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.document.AbstractExcelView;

@Controller
public class clsExcelBuilderWithReportName extends AbstractExcelView{

	

	@SuppressWarnings({ "rawtypes"})
	@Override
	    protected void buildExcelDocument(Map<String, Object> model,
	            HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
	            throws Exception {
	        // get data model which is passed by the Spring container
			
		 	List Datalist = (List) model.get("listWithReportName");
		 	String reportName= (String) Datalist.get(0);
		 	String[] HeaderList= (String[]) Datalist.get(1);
		 	String[] HeaderListt= (String[]) Datalist.get(1);
	        List listStock = new ArrayList();
		 	if(Datalist.size()>=4)
		 	{
			 	 reportName= (String) Datalist.get(0);
			 	 HeaderListt= (String[]) Datalist.get(1);
			 	 HeaderList= (String[]) Datalist.get(2);
		        
		        try{
		        	listStock =(List) Datalist.get(3);
		        }catch(Exception e){
		        	listStock=new ArrayList();
		        }
		 	}
		 	else
		 	{
		 		reportName= (String) Datalist.get(0);
			 	 HeaderList= (String[]) Datalist.get(1);		      
		        try{
		        	listStock =(List) Datalist.get(2);
		        }catch(Exception e){
		        	listStock=new ArrayList();
		        }
		 	}
		 	
		 	response.setContentType("application/vnd.ms-excel");
		 	response.setHeader("Content-disposition", "attachment; filename="+reportName.trim() +".xls");
		 	
		 	
	        
	        // create a new Excel sheet
	        HSSFSheet sheet = workbook.createSheet("Sheet");
	        sheet.setDefaultColumnWidth(20);
	         
	        // create style for header cells
	        CellStyle style = workbook.createCellStyle();
	        Font font = workbook.createFont();
	        font.setFontName("Arial");
	        style.setFillForegroundColor(HSSFColor.BLUE.index);
	        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        font.setColor(HSSFColor.WHITE.index);
	        style.setFont(font);
	         
	        
	   /*  // create header row
	        HSSFRow headerr = sheet.createRow(0);
	        for (int rowCount = 0;rowCount<HeaderListt.length;rowCount++)
	        {
		        headerr.createCell(rowCount).setCellValue(HeaderListt[rowCount].toString());
		        headerr.getCell(rowCount).setCellStyle(style);
	        }*/
	        
	        // create header row
	        int ColrowCount = 1  ;
	        if(Datalist.size()>=4)
		 	{
		        HSSFRow header = sheet.createRow(0);
		        for (int rowCount = 0;rowCount<HeaderListt.length;rowCount++)
		        {
		        	header.createCell(rowCount).setCellValue(HeaderListt[rowCount].toString());		        
		        }
		        HSSFRow headerr = sheet.createRow(2);
		        for (int rowCount = 0;rowCount<HeaderList.length;rowCount++)
		        {
		        	headerr.createCell(rowCount).setCellValue(HeaderList[rowCount].toString());		
		        	headerr.getCell(rowCount).setCellStyle(style);
		        }
		        ColrowCount = 3  ;
		 	}
	        else
	        {	        	
		        HSSFRow headerr = sheet.createRow(0);
		        for (int rowCount = 0;rowCount<HeaderList.length;rowCount++)
		        {
		        	headerr.createCell(rowCount).setCellValue(HeaderList[rowCount].toString());		
		        	headerr.getCell(rowCount).setCellStyle(style);
		        }
	        	
	        }
	        	        
	        // create data rows
	        // aRow is  add Row
	        
	        int Countt=0;
	        for(int rowCount=0;rowCount<listStock.size();rowCount++)
				{
	        		
	            	HSSFRow aRow = sheet.createRow(ColrowCount++);
	            	
	            	if(listStock.get(rowCount).equals(""))
	            	{	            		       								
						 aRow.createCell(Countt).setCellValue("");            		
	            	}	            	
	            	else
	            	{
	            	List  arrObj=(List) listStock.get(rowCount);
	                for(int Count=0;Count<arrObj.size();Count++)
					{
						if(null!=arrObj.get(Count) && arrObj.get(Count).toString().length()>0)
						{
							
							
							if(isNumeric(arrObj.get(Count).toString()))
							{
								aRow.createCell(Count).setCellValue(Double.parseDouble(arrObj.get(Count).toString()));
							}
							else
							{
								aRow.createCell(Count).setCellValue(arrObj.get(Count).toString());
							}
						}
						else
						{
							aRow.createCell(Count).setCellValue("");
						}
						Countt=Count;
					}		
	            	
	            	}
	        }

	        
	        
	}      
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	
}
