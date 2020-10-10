package com.sanguine.dao;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sanguine.webpos.model.clsSetupHdModel;


@Repository("clsSetupMasterDao")
public class clsSetupMasterDaoImpl implements clsSetupMasterDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private SessionFactory webPOSSessionFactory;

	
	
	@Override
	public void funDeleteWorkFlowAutorization(String propertyCode,String clientCode) {
		Query query=sessionFactory.getCurrentSession().createQuery("DELETE clsWorkFlowModel WHERE strPropertyCode= :propertyCode and strClientCode=:clientCode");
		query.setParameter("propertyCode", propertyCode);
		query.setParameter("clientCode", clientCode);
		query.executeUpdate();
	}


	@Override
	public void funDeleteWorkFlowForslabBasedAuth(String propertyCode,
			String clientCode) {
		Query query=sessionFactory.getCurrentSession().createQuery("DELETE clsWorkFlowForSlabBasedAuth WHERE strPropertyCode= :propertyCode and strClientCode=:clientCode");
		query.setParameter("propertyCode", propertyCode);
		query.setParameter("clientCode", clientCode);
		query.executeUpdate();
		
	}





	


	
	@Override
	public List<clsSetupHdModel> funGetListSetupModel() {
		String sql="from clsSetupHdModel order by strPOSCode asc";
		Query query=webPOSSessionFactory.getCurrentSession().createQuery(sql);
		@SuppressWarnings("unchecked")
		List<clsSetupHdModel> list=query.list();
		return (List<clsSetupHdModel>) list;
	}
	
}
