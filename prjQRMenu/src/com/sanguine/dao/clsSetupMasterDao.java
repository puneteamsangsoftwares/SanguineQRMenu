package com.sanguine.dao;



import java.util.List;

import com.sanguine.webpos.model.clsSetupHdModel;




public interface clsSetupMasterDao {


public void funDeleteWorkFlowAutorization(String propertyCode,String clientCode);



public void funDeleteWorkFlowForslabBasedAuth(String propertyCode,String clientCode);


public List<clsSetupHdModel> funGetListSetupModel();

}
