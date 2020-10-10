package com.sanguine.base.dao;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sanguine.base.model.clsBaseModel;
import com.sanguine.base.service.clsSetupService;
import com.sanguine.webpos.bean.clsPOSBillItemDtl;
import com.sanguine.webpos.bean.clsPOSBillSettlementDtl;

import com.sanguine.webpos.model.clsPricingMasterHdModel;

@Repository("intfBaseDao")
@Transactional(value = "webPOSTransactionManager")
public class clsBaseDaoImpl implements intfBaseDao
{

	@Autowired
	private SessionFactory webPOSSessionFactory;

	@Autowired
	private clsSetupService objSetupService;

	Map<String, List<Map<String, clsPOSBillSettlementDtl>>> mapPOSDtlForSettlement;
	private Map<String, Map<String, clsPOSBillItemDtl>> mapPOSItemDtl;
	private Map<String, Map<String, clsPOSBillItemDtl>> mapPOSMenuHeadDtl;

	double TotSale = 0;

	@Override
	public String funSave(clsBaseModel objBaseModel)
	{
		webPOSSessionFactory.getCurrentSession().saveOrUpdate(objBaseModel);
		return objBaseModel.getDocCode();
	}

	@Override
	public clsBaseModel funLoad(clsBaseModel objBaseModel, Serializable key)
	{
		return (clsBaseModel) webPOSSessionFactory.getCurrentSession().load(objBaseModel.getClass(), key);
	}

	@Override
	public clsBaseModel funGet(clsBaseModel objBaseModel, Serializable key)
	{
		return (clsBaseModel) webPOSSessionFactory.getCurrentSession().get(objBaseModel.getClass(), key);
	}

	@Override
	public List funLoadAll(clsBaseModel objBaseModel, String clientCode)
	{
		Criteria cr = webPOSSessionFactory.getCurrentSession().createCriteria(objBaseModel.getClass());
		cr.add(Restrictions.eq("strClientCode", clientCode));

		return webPOSSessionFactory.getCurrentSession().createCriteria(objBaseModel.getClass()).list();
	}

	@Override
	public List funGetSerachList(String sql, String clientCode) throws Exception
	{
		Query query = webPOSSessionFactory.getCurrentSession().getNamedQuery(sql);
		query.setParameter("clientCode", clientCode);

		return query.list();
	}

	public List funGetList(StringBuilder strQuery, String queryType) throws Exception
	{
		Query query;
		if (queryType.equals("sql"))
		{
			query = webPOSSessionFactory.getCurrentSession().createSQLQuery(strQuery.toString());
			return query.list();
		}
		else
		{
			query = webPOSSessionFactory.getCurrentSession().createQuery(strQuery.toString());
			return query.list();
		}
	}

	public int funExecuteUpdate(String strQuery, String queryType) throws Exception
	{
		Query query;
		if (queryType.equalsIgnoreCase("sql"))
		{
			query = webPOSSessionFactory.getCurrentSession().createSQLQuery(strQuery);
			return query.executeUpdate();
		}
		else
		{
			query = webPOSSessionFactory.getCurrentSession().createQuery(strQuery);
			return query.executeUpdate();
		}
	}

	@Override
	public List funLoadAllPOSWise(clsBaseModel objBaseModel, String clientCode, String strPOSCode) throws Exception
	{
		Criteria cr = webPOSSessionFactory.getCurrentSession().createCriteria(objBaseModel.getClass());
		cr.add(Restrictions.eq("strClientCode", clientCode));
		cr.add(Restrictions.eq("strPOSCode", strPOSCode));

		return cr.list();
	}
	@Override
	public List funLoadItemImage(clsBaseModel objBaseModel, String itemCode, String clientCode) throws Exception
	{
		Criteria cr = webPOSSessionFactory.getCurrentSession().createCriteria(objBaseModel.getClass());
		cr.add(Restrictions.eq("strItemCode", itemCode));
		cr.add(Restrictions.eq("strClientCode", clientCode));

		return cr.list();
	}
	
	@Override
	public List funLoadAllCriteriaWise(clsBaseModel objBaseModel, String criteriaName, String criteriaValue)
	{
		Criteria cr = webPOSSessionFactory.getCurrentSession().createCriteria(objBaseModel.getClass());
		cr.add(Restrictions.eq(criteriaName, criteriaValue));

		return webPOSSessionFactory.getCurrentSession().createCriteria(objBaseModel.getClass()).list();
	}

	@Override
	public clsBaseModel funGetAllMasterDataByDocCodeWise(String sql, Map<String, String> hmParameters)
	{
		Query query = webPOSSessionFactory.getCurrentSession().getNamedQuery(sql);
		for (Map.Entry<String, String> entrySet : hmParameters.entrySet())
		{
			query.setParameter(entrySet.getKey(), entrySet.getValue());
		}
		List list = query.list();

		clsBaseModel model = new clsBaseModel();
		if (list.size() > 0)
		{
			model = (clsBaseModel) list.get(0);

		}
		return model;
	}

	@Override
	public clsBaseModel funGetMenuItemPricingMaster(String sql, long id, String clientCode)
	{
		Query query = webPOSSessionFactory.getCurrentSession().getNamedQuery(sql);
		query.setParameter("longPricingId", id);
		query.setParameter("clientCode", clientCode);
		List list = query.list();

		clsPricingMasterHdModel model = new clsPricingMasterHdModel();
		if (list.size() > 0)
		{
			model = (clsPricingMasterHdModel) list.get(0);

		}
		return model;

	}

	public String funGetValue(StringBuilder strQuery, String queryType) throws Exception
	{
		Query query;
		if (queryType.equals("sql"))
		{
			query = webPOSSessionFactory.getCurrentSession().createSQLQuery(strQuery.toString());
			return query.toString();
		}
		else
		{
			query = webPOSSessionFactory.getCurrentSession().createQuery(strQuery.toString());
			return query.toString();
		}
	}
	
	@Override
    public int funDeletePOSUser(String strUserCode, String clientCode)
    {
		Query query = webPOSSessionFactory.getCurrentSession().createQuery("delete clsUserHdModel where strUserCode = :userCode and strClientCode= :clientCode");
		query.setParameter("clientCode", clientCode);
		query.setParameter("userCode", strUserCode);
		int res=query.executeUpdate();
		
		query = webPOSSessionFactory.getCurrentSession().createQuery("delete clsUserDetailHdModel where strUserCode = :userCode");
		query.setParameter("userCode", strUserCode);
		res=query.executeUpdate();
		
		query = webPOSSessionFactory.getCurrentSession().createQuery("delete clsSuperUserDetailHdModel where strUserCode = :userCode ");
		query.setParameter("userCode", strUserCode);
		res=query.executeUpdate();
		
		return res;
    }

	private void funGenerateItemWiseSales(List list, String fromDate, String toDate, String strPOSCode, String strShiftNo, String strUserCode, String field, String strPayMode, String strOperator, String strFromBill, String strToBill, String reportType, String Type, String Customer, String ConsolidatePOS, String ReportName)
	{
		try
		{
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);
					String itemCode = obj[0].toString();// itemCode
					String itemName = obj[1].toString();// itemName
					String posName = obj[2].toString();// posName
					double qty = Double.parseDouble(obj[3].toString());// qty
					double salesAmt = Double.parseDouble(obj[7].toString());// salesAmount
					double subTotal = Double.parseDouble(obj[5].toString());// subTotal
					double discAmt = Double.parseDouble(obj[8].toString());// discount
					String date = obj[9].toString();// date
					String posCode = obj[10].toString();// posCode

					String compare = itemCode;
					if (itemCode.contains("M"))
					{
						compare = itemName;
					}
					else
					{
						compare = itemCode;
					}

					if (mapPOSItemDtl.containsKey(posCode))
					{
						Map<String, clsPOSBillItemDtl> mapItemDtl = mapPOSItemDtl.get(posCode);
						if (mapItemDtl.containsKey(compare))
						{
							clsPOSBillItemDtl objItemDtl = mapItemDtl.get(compare);
							objItemDtl.setQuantity(objItemDtl.getQuantity() + qty);
							objItemDtl.setAmount(objItemDtl.getAmount() + salesAmt);
							objItemDtl.setSubTotal(objItemDtl.getSubTotal() + subTotal);
							objItemDtl.setDiscountAmount(objItemDtl.getDiscountAmount() + discAmt);
						}
						else
						{
							clsPOSBillItemDtl objItemDtl = new clsPOSBillItemDtl(date, itemCode, itemName, qty, salesAmt, discAmt, posName, subTotal);
							mapItemDtl.put(compare, objItemDtl);
						}
					}
					else
					{
						Map<String, clsPOSBillItemDtl> mapItemDtl = new LinkedHashMap<>();
						clsPOSBillItemDtl objItemDtl = new clsPOSBillItemDtl(date, itemCode, itemName, qty, salesAmt, discAmt, posName, subTotal);
						mapItemDtl.put(compare, objItemDtl);
						mapPOSItemDtl.put(posCode, mapItemDtl);
					}

					if (!itemCode.contains("M"))
					{
						funCreateModifierQuery(itemCode, fromDate, toDate, strPOSCode, strShiftNo, strUserCode, field, strPayMode, strOperator, strFromBill, strToBill, reportType, Type, Customer, ConsolidatePOS, ReportName);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void funCreateModifierQuery(String itemCode, String fromDate, String toDate, String strPOSCode, String strShiftNo, String strUserCode, String field, String strPayMode, String strOperator, String strFromBill, String strToBill, String reportType, String Type, String Customer, String ConsolidatePOS, String ReportName)
	{
		try
		{
			String sqlModLive = "select a.strItemCode,a.strModifierName,c.strPOSName" + ",sum(a.dblQuantity),'0.0',sum(a.dblAmount)-sum(a.dblDiscAmt),'" + strUserCode + "' " + ",sum(a.dblAmount),sum(a.dblDiscAmt),DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),b.strPOSCode " + "from tblbillmodifierdtl a,tblbillhd b,tblposmaster c\n" + "where a.strBillNo=b.strBillNo and b.strPOSCode=c.strPosCode  \n" + "and date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' " + "and left(a.strItemCode,7)='" + itemCode + "' " + "and a.dblAmount>0 ";

			// String pos = funGetSelectedPosCode();
			String sqlFilters = "";
			if (!strPOSCode.equals("All") && !strOperator.equals("All"))
			{
				sqlFilters += " AND b.strPOSCode = '" + strPOSCode + "' and b.strUserCreated='" + strOperator + "' ";
			}
			else if (!strPOSCode.equals("All") && strOperator.equals("All"))
			{
				sqlFilters += " AND b.strPOSCode = '" + strPOSCode + "' ";
			}
			else if (strPOSCode.equals("All") && !strOperator.equals("All"))
			{
				sqlFilters += " AND b.strUserCreated='" + strOperator.toString() + "' ";
			}
			if (strFromBill.length() == 0 && strToBill.length() == 0)
			{

			}
			else
			{
				sqlFilters += " and a.strbillno between '" + strFromBill + "' " + " and '" + strToBill + "'";
			}

			// sqlFilters += " AND b.intShiftCode = '" + strShiftNo + "' ";

			sqlFilters += " group by a.strItemCode,a.strModifierName,c.strPOSName  " + " order by b.dteBillDate ";

			sqlModLive = sqlModLive + " " + sqlFilters;

			Query queryModLive = webPOSSessionFactory.getCurrentSession().createSQLQuery(sqlModLive.toString());
			List listModLive = queryModLive.list();
			funGenerateItemWiseSales(listModLive, fromDate, toDate, strPOSCode, strShiftNo, strUserCode, field, strPayMode, strOperator, strFromBill, strToBill, reportType, Type, Customer, ConsolidatePOS, ReportName);

			/*
			 * ResultSet
			 * rs=clsGlobalVarClass.dbMysql.executeResultSet(sqlModLive
			 * .toString()); funGenerateItemWiseSales(rs);
			 */

			// qmodifiers
			String sqlModQFile = "select a.strItemCode,a.strModifierName,c.strPOSName" + ",sum(a.dblQuantity),'0.0',sum(a.dblAmount)-sum(a.dblDiscAmt),'" + strUserCode + "' " + ",sum(a.dblAmount),sum(a.dblDiscAmt),DATE_FORMAT(date(b.dteBillDate),'%d-%m-%Y'),b.strPOSCode " + "from tblqbillmodifierdtl a,tblqbillhd b,tblposmaster c\n" + "where a.strBillNo=b.strBillNo and b.strPOSCode=c.strPosCode  \n" + "and date( b.dteBillDate ) BETWEEN '" + fromDate + "' AND '" + toDate + "' " + "and left(a.strItemCode,7)='" + itemCode + "' " + "and a.dblAmount>0  ";

			sqlModQFile = sqlModQFile + " " + sqlFilters;

			Query queryModLiveQ = webPOSSessionFactory.getCurrentSession().createSQLQuery(sqlModQFile.toString());
			List listModLiveQ = queryModLiveQ.list();
			funGenerateItemWiseSales(listModLiveQ, fromDate, toDate, strPOSCode, strShiftNo, strUserCode, field, strPayMode, strOperator, strFromBill, strToBill, reportType, Type, Customer, ConsolidatePOS, ReportName);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void funGenerateSettlementWiseSales(List list)
	{
		try
		{
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					String posCode = obj[0].toString();
					String posName = obj[2].toString();
					String settlementCode = obj[1].toString();
					String settlementDesc = obj[3].toString();
					double settlementAmt = Double.parseDouble(obj[4].toString());
					String settlementType = obj[6].toString();

					if (mapPOSDtlForSettlement.containsKey(posCode))
					{
						List<Map<String, clsPOSBillSettlementDtl>> listOfSettlement = mapPOSDtlForSettlement.get(posCode);
						boolean isSettlementExists = false;
						int settlementIndex = 0;
						for (int j = 0; j < listOfSettlement.size(); j++)
						{
							if (listOfSettlement.get(j).containsKey(settlementCode))
							{
								isSettlementExists = true;
								settlementIndex = j;
								break;
							}
						}
						if (isSettlementExists)
						{
							Map<String, clsPOSBillSettlementDtl> mapSettlementCodeDtl = listOfSettlement.get(settlementIndex);
							clsPOSBillSettlementDtl objBillSettlementDtl = mapSettlementCodeDtl.get(settlementCode);
							objBillSettlementDtl.setStrSettlementCode(settlementCode);
							objBillSettlementDtl.setDblSettlementAmt(objBillSettlementDtl.getDblSettlementAmt() + settlementAmt);
							objBillSettlementDtl.setPosName(posName);
							TotSale = TotSale + settlementAmt;
						}
						else
						{
							Map<String, clsPOSBillSettlementDtl> mapSettlementCodeDtl = new LinkedHashMap<>();
							clsPOSBillSettlementDtl objBillSettlementDtl = new clsPOSBillSettlementDtl(settlementCode, settlementDesc, settlementAmt, posName, settlementType);
							mapSettlementCodeDtl.put(settlementCode, objBillSettlementDtl);
							listOfSettlement.add(mapSettlementCodeDtl);
							TotSale = TotSale + settlementAmt;
						}
					}
					else
					{
						List<Map<String, clsPOSBillSettlementDtl>> listOfSettelment = new ArrayList<>();
						Map<String, clsPOSBillSettlementDtl> mapSettlementCodeDtl = new LinkedHashMap<>();
						clsPOSBillSettlementDtl objBillSettlementDtl = new clsPOSBillSettlementDtl(settlementCode, settlementDesc, settlementAmt, posName, settlementType);
						mapSettlementCodeDtl.put(settlementCode, objBillSettlementDtl);
						listOfSettelment.add(mapSettlementCodeDtl);
						TotSale = TotSale + settlementAmt;
						mapPOSDtlForSettlement.put(posCode, listOfSettelment);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void funGenerateMenuHeadWiseSales(List list)
	{
		try
		{
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					Object[] obj = (Object[]) list.get(i);

					String posCode = obj[9].toString();// posCode
					String posName = obj[4].toString();// posName
					String menuCode = obj[0].toString();// menuCode
					String menuName = obj[1].toString();// menuName
					double qty = Double.parseDouble(obj[2].toString());// qty
					double salesAmt = Double.parseDouble(obj[7].toString());// salesAmt
					double subTotal = Double.parseDouble(obj[3].toString());// subTotal
					double discAmt = Double.parseDouble(obj[8].toString());// disc

					if (mapPOSMenuHeadDtl.containsKey(posCode))
					{
						Map<String, clsPOSBillItemDtl> mapItemDtl = mapPOSMenuHeadDtl.get(posCode);
						if (mapItemDtl.containsKey(menuCode))
						{
							clsPOSBillItemDtl objItemDtl = mapItemDtl.get(menuCode);
							objItemDtl.setQuantity(objItemDtl.getQuantity() + qty);
							objItemDtl.setAmount(objItemDtl.getAmount() + salesAmt);
							objItemDtl.setSubTotal(objItemDtl.getSubTotal() + subTotal);
							objItemDtl.setDiscountAmount(objItemDtl.getDiscountAmount() + discAmt);
						}
						else
						{
							clsPOSBillItemDtl objItemDtl = new clsPOSBillItemDtl(qty, salesAmt, discAmt, posName, subTotal, menuCode, menuName);
							mapItemDtl.put(menuCode, objItemDtl);
						}
					}
					else
					{
						Map<String, clsPOSBillItemDtl> mapItemDtl = new LinkedHashMap<>();
						clsPOSBillItemDtl objItemDtl = new clsPOSBillItemDtl(qty, salesAmt, discAmt, posName, subTotal, menuCode, menuName);
						mapItemDtl.put(menuCode, objItemDtl);
						mapPOSMenuHeadDtl.put(posCode, mapItemDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}




}
