package com.sanguine.service;

import java.util.List;



@SuppressWarnings("rawtypes")
public interface clsGlobalFunctionsService 
{

	public List funGetList(String sql, String queryType);

	public List funCheckName(String name, String strClientCode, String tableName);


	
}
