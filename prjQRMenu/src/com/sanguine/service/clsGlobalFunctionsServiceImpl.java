package com.sanguine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.dao.clsGlobalFunctionsDao;



@Service("objGlobalFunctionsService")
@Transactional(value="webPOSTransactionManager",propagation = Propagation.SUPPORTS, readOnly = true)
public class clsGlobalFunctionsServiceImpl implements clsGlobalFunctionsService
{
	@Autowired
	private clsGlobalFunctionsDao objGlobalDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List funGetList(String sql,String queryType)
	{		
		return objGlobalDao.funGetList(sql,queryType);
	}


		

	@SuppressWarnings("rawtypes")
	@Override
	public List funCheckName(String name, String strClientCode, String tableName) {
		
		 return objGlobalDao.funCheckName(name,strClientCode,tableName);
	}


/*
	@Override
	public int funUpdate(String sql,String queryType) {
		
		return objGlobalDao.funUpdate(sql,queryType);
	}
	
	 public int funUpdateAllModule(String sql, String queryType)
	 {
		 return objGlobalDao.funUpdateAllModule(sql,queryType);
	 }*/


	

//	
//	@Override
//	
//	public int funExcuteQuery(String sql) {
//		return objGlobalDao.funExcuteQuery(sql);
//		
//	}




	
	
}
