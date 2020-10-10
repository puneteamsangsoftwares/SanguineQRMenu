package com.sanguine.dao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;




@Repository("clsGlobalFunctionsDao")
public class clsGlobalFunctionsDaoImpl implements clsGlobalFunctionsDao
{
	@Autowired
	private SessionFactory webPOSSessionFactory;
	@Autowired
	HttpServletRequest req;

	
	SessionFactory sessionFactory1 = null;		
	
//	@Autowired
//	private SessionFactory CRMSessionFactory;
	

	

	
 
	final static Logger logger=Logger.getLogger(clsGlobalFunctionsDaoImpl.class);
	    @SuppressWarnings(
    { "rawtypes" })
    @Override
    public List funGetList(String sql, String queryType)
    {
	    	
	    	Query query = webPOSSessionFactory.getCurrentSession().createSQLQuery(sql);
		    return query.list();
    }

    
    @SuppressWarnings(
    { "rawtypes", "finally" })
    @Override
    public List funCheckName(String name, String strClientCode, String formName)
    {
	String sql = "";
	List list = new ArrayList();
	try
	{
	    switch (formName)
	    {
		case "frmSubGroupMaster":
		    sql = "select count(LOWER(strSGName)) from tblsubgroupmaster where strSGName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		
		case "frmGroupMaster":
		    sql = "select count(LOWER(strGName)) from tblgroupmaster where strGName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		    
		case "frmSubMenuHead":
		    sql = "select count(LOWER(strSubMenuHeadCode)) from tblsubmenuhead where strSubMenuHeadName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		    
		case "frmMenuHead":
		    sql = "select count(LOWER(strMenuCode)) from tblmenuhd where strMenuName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		
		    
		case "frmLocationMaster":
		    sql = "select count(LOWER(strLocName)) from tbllocationmaster where strLocName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		
		case "frmPropertyMaster":
		    sql = "select count(LOWER(strPropertyName)) from tblpropertymaster where strPropertyName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		
		case "frmProductMaster":
		    sql = "select count(LOWER(strProdName)) from tblproductmaster where strProdName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		
		case "frmReasonMaster":
		    sql = "select count(LOWER(strReasonName)) from tblreasonmaster where strReasonName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		
		case "frmSupplierMaster":
		    sql = "select count(LOWER(strPName)) from tblpartymaster where strPName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		
		case "frmTaxMaster":
		    sql = "select count(LOWER(strTaxDesc)) from tbltaxhd where strTaxDesc='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
		
		case "frmTCMaster":
		    sql = "select count(LOWER(strTCName)) from tbltcmaster where strTCName='" + name + "' and strClientCode='" + strClientCode + "'";
		    break;
	    }
	    Query query = webPOSSessionFactory.getCurrentSession().createSQLQuery(sql);
	    list = query.list();
	}
	catch (Exception e)
	{
	    logger.error(e);
	    e.printStackTrace();
	}
	finally
	{
	    return list;
	}
	
    }
//    
//    @Override
//    public int funUpdate(String sql, String queryType)
//    {
//	int res = 0;
//	try
//	{
//	    if (queryType.equalsIgnoreCase("sql"))
//	    {
//		
//		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
//		res = query.executeUpdate();
//	    }
//	    if (queryType.equalsIgnoreCase("hql"))
//	    {
//		Query query = sessionFactory.getCurrentSession().createQuery(sql);
//		res = query.executeUpdate();
//	    }
//	}
//	catch (Exception e)
//	{
//	    logger.error(e);
//	}
//	return res;
//    }
    
   /* 
    @Override
    public List funGetDataList(String sql, String queryType)
    {
	List listResult = null;
	
	SessionFactory sessionFactory1 = null;
	
	
	    sessionFactory1 = sessionFactory;
	

	
	if (queryType.equalsIgnoreCase("hql"))
	{
	    try
	    {
		Query query = sessionFactory1.getCurrentSession().createQuery(sql);
		listResult = query.list();
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
	else
	{
	    try
	    {
		listResult = sessionFactory1.getCurrentSession().createSQLQuery(sql).list();
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
	return listResult;
    }
   
    */
    
    /*
    @Override
    public long funGetNextNo(String tableName, String masterName, String columnName, String clientCode, String condition)
    {
		
		
		try
		{
		    @SuppressWarnings("rawtypes")
		    List listLastNo = webPOSSessionFactory.getCurrentSession().createSQLQuery("select ifnull(max(" + columnName + "),0) from " + tableName + " where strClientCode='" + clientCode + "' "+condition+" ").list();
		    String lastCode = "0";
		    if (listLastNo.size() > 0)
		    {
			lastCode = (String) listLastNo.get(0).toString();
		    }
		    else
		    {
			lastCode = "0";
		    }
		    
		    String num = "0";
		    String regex = "(\\d+)";
		    Matcher matcher = Pattern.compile(regex).matcher(lastCode);
		    while (matcher.find())
		    {
			String n = matcher.group();
			num = num + n;
		    }
		    
		    nextNo = Long.parseLong(num);
		    nextNo=nextNo+1;
		    System.out.print("nextNo" + nextNo);
		    
		}
		catch (Exception e)
		{
		    nextNo = 0;
		    logger.error(e);
		    e.printStackTrace();
		}
		finally
		{
		    return nextNo;
		}
    }
    */
}
