package com.sanguine.webpos.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;

@Controller
public class clsDatabaseConnection
{

	
	public ResultSet funGetResultSet(String sql)
	{
		ResultSet res=null;
		try
		{
			Connection con=funOpenPOSCon("mysql");
			Statement st=con.createStatement();
		    res=st.executeQuery(sql);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return res;
	}

	public Connection funOpenPOSCon(String dbType) throws Exception
    {
	    Properties prop = new Properties();
		Resource resource = new ClassPathResource("resources/database.properties");
		InputStream input = resource.getInputStream();
		// load a properties file
		prop.load(input);
		// get the property value and print it out
		String dbURL=prop.getProperty("database.urlWebPOS");
		String urluser = prop.getProperty("database.user");
		String urlPassword = prop.getProperty("database.password");
		resource.exists();

        Connection dbCon=null;
        if(dbType.equalsIgnoreCase("mysql"))
        {
            Class.forName("com.mysql.jdbc.Driver");
            dbCon = DriverManager.getConnection(dbURL,urluser,urlPassword);
        }        
        return dbCon;
    }

}
