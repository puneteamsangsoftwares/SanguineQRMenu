package com.sanguine.service;



import java.util.List;

import com.sanguine.webpos.model.clsSetupHdModel;

public interface clsSetupMasterService {
	
	public void funDeleteProcessSetup(String propertyCode,String clientCode);
	
	public void funDeleteWorkFlowAutorization(String propertyCode,String clientCode);
	


	public void funDeleteWorkFlowForslabBasedAuth(String propertyCode,String clientCode);
	
	
	
	
	
	


	public List<clsSetupHdModel> funGetListSetupModel();
}
