package com.sanguine.webpos.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class clsTesting
{

	public static void main() throws ParseException
	{
		
		Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		date = formatter.parse("12-09-2020");
		formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");  
		String strDate = formatter.format(date);  
		System.out.println(strDate);
		
	}
}
