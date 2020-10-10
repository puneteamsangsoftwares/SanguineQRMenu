package com.sanguine.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface clsGlobalFunctionsDao 
{

	public List funCheckName(String name, String strClientCode, String formName);


	public List funGetList(String sql, String queryType);
	
}
