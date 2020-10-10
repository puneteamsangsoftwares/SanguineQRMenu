package com.sanguine.webpos.sevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanguine.base.model.clsBaseModel;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.webpos.model.clsMenuHeadMasterModel;
import com.sanguine.webpos.model.clsMenuItemMasterModel;
import com.sanguine.webpos.model.clsModifierGroupMasterHdModel;
import com.sanguine.webpos.model.clsModifierMasterHdModel;
//import com.sanguine.webpos.model.clsPOSPromationMasterHdModel;
import com.sanguine.webpos.model.clsPricingMasterHdModel;
import com.sanguine.webpos.model.clsSetupHdModel;
import com.sanguine.webpos.model.clsSubMenuHeadMasterModel;
import com.sanguine.webpos.model.clsTableMasterModel;
import com.sanguine.webpos.model.clsTaxMasterModel;

@Service
public class clsPOSMasterService
{

	@Autowired
	intfBaseService obBaseService;

	public void funSaveReasonMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	
	public void funSaveSettlementMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public void funSaveUpdateAreaMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public void funSaveUpdateGiftVoucherMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	/*public clsGiftVoucherMasterModel funSelectedGiftVoucherMasterData(String giftVoucherCode, String clientCode) throws Exception
	{
		clsGiftVoucherMasterModel objGiftVoucherMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("giftVoucherCode", giftVoucherCode);
		hmParameters.put("clientCode", clientCode);
		objGiftVoucherMasterModel = (clsGiftVoucherMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getGiftVoucherMaster", hmParameters);
		System.out.println();
		return objGiftVoucherMasterModel;
	}

	public clsGiftVoucherMasterModel funLoadGiftVoucherMaster(Map hmParameters) throws Exception
	{
		clsGiftVoucherMasterModel obGiftVoucherMasterModel = null;
		obGiftVoucherMasterModel = (clsGiftVoucherMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getGiftVoucherMaster", hmParameters);
		return obGiftVoucherMasterModel;
	}

	public List<clsGiftVoucherMasterModel> funGetAllGiftVoucherForMaster(String clientCode) throws Exception
	{
		List<clsGiftVoucherMasterModel> list = null;
		list = obBaseService.funLoadAll(new clsGiftVoucherMasterModel(), clientCode);
		return list;
	}
*/
	public void funSaveUpdateCostCenterMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

		public void funSaveUpdateCustomerAreaMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	
	public void funSaveCustomerMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public List funGetPOSList(String clientCode) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select strPOSCode,strPOSName from tblposmaster where strOperationalYN='Y' and strClientCode='" + clientCode + "' ");
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public void funSaveUpdateCustomerTypeMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}


	public List funGetCityList(String strClientCode) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select a.strCityName,a.strState,a.strCountry  from tblsetup a where a.strClientCode=" + strClientCode);
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

		public void funSaveUpdateDeliverPersonMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

		public void funSaveUpdateGroupMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

/*	public clsGroupMasterModel funSelectedGroupMasterData(String groupCode, String clientCode) throws Exception
	{
		clsGroupMasterModel objGroupMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("groupCode", groupCode);
		hmParameters.put("clientCode", clientCode);
		objGroupMasterModel = (clsGroupMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getGroupMaster", hmParameters);
		return objGroupMasterModel;
	}*/
	
	

	public void funSaveUpdateMenuHeadMasterData(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public void funSaveUpdateSubMenuHeadMasterData(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public clsMenuHeadMasterModel funSelectedMenuHeadMasterData(String menuHeadCode, String clientCode) throws Exception
	{
		clsMenuHeadMasterModel objMenuHeadMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("menuCode", menuHeadCode);
		hmParameters.put("clientCode", clientCode);
		objMenuHeadMasterModel = (clsMenuHeadMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getMenuHeadMaster", hmParameters);
		return objMenuHeadMasterModel;
	}

	public clsSubMenuHeadMasterModel funSelectedSubMenuHeadMasterData(String subMenuHeadCode, String clientCode) throws Exception
	{
		clsSubMenuHeadMasterModel objSubMenuHeadMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("subMenuCode", subMenuHeadCode);
		hmParameters.put("clientCode", clientCode);
		objSubMenuHeadMasterModel = (clsSubMenuHeadMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getSubMenuHeadMaster", hmParameters);
		return objSubMenuHeadMasterModel;
	}

	public clsMenuItemMasterModel funGetMenuItemMasterData(String itemCode, String clientCode) throws Exception
	{
		clsMenuItemMasterModel objMenuItemMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("itemCode", itemCode);
		hmParameters.put("clientCode", clientCode);
		objMenuItemMasterModel = (clsMenuItemMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getMenuItemMaster", hmParameters);
		return objMenuItemMasterModel;
	}

	public void funSaveUpdateMenuItemMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}
	public void funSaveUpdatePosMasterData(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public List funLoadAllMenuHeadForMaster(String strClientCode) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select strMenuCode, strMenuName from tblmenuhd where strOperational='Y' and strClientCode='"+strClientCode+"' ORDER by intSequence");
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public List<clsSubMenuHeadMasterModel> funLoadAllSubMenuHeadMaster(String clientCode) throws Exception
	{
		List<clsSubMenuHeadMasterModel> list = null;
		list = obBaseService.funLoadAll(new clsSubMenuHeadMasterModel(), clientCode);
		return list;
	}

	public clsPricingMasterHdModel funGetMenuItemPricingMaster(String pricingId, String clientCode) throws Exception
	{
		clsPricingMasterHdModel objPricingMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("longPricingId", pricingId);
		hmParameters.put("clientCode", clientCode);
		objPricingMasterModel = (clsPricingMasterHdModel) obBaseService.funGetAllMasterDataByDocCodeWise("getMenuItemPricing", hmParameters);
		return objPricingMasterModel;
	}

	public void funSaveUpdatePricingMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public clsPricingMasterHdModel funCheckDuplicateItemPricing(String strItemCode, String strPosCode, String strAreaCode, String strHourlyPricing, String clientCode) throws Exception
	{
		clsPricingMasterHdModel objPricingMasterHdModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("strPosCode", strPosCode);
		hmParameters.put("strItemCode", strItemCode);
		hmParameters.put("strAreaCode", strAreaCode);
		hmParameters.put("strHourlyPricing", strHourlyPricing);
		hmParameters.put("clientCode", clientCode);
		objPricingMasterHdModel = (clsPricingMasterHdModel) obBaseService.funGetAllMasterDataByDocCodeWise("getMenuItemPricing", hmParameters);
		return objPricingMasterHdModel;
	}

	public clsPricingMasterHdModel funLoadDataToUpdateItemPrice(String pricingId, String clientCode) throws Exception
	{
		clsPricingMasterHdModel objPricingMasterModel = null;
		long strPricingId = Long.parseLong(pricingId);
		objPricingMasterModel = (clsPricingMasterHdModel) obBaseService.funGetMenuItemPricingMaster("getMenuItemPricing", strPricingId, clientCode);
		return objPricingMasterModel;
	}

	public void funSaveUpdateWaiterMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}
	public void funSaveUpdateItemWiseIncentiveMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}


	public List funGetAllGroupNamesForBillSeries(String clientCode, boolean addFilter, String filter) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.setLength(0);
		sqlBuilder.append(" select a.strGroupName,a.strGroupCode from tblgrouphd a ");
		if (addFilter)
		{
			sqlBuilder.append(" where a.strGroupCode NOT IN ");
			sqlBuilder.append(filter);
		}
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public List funGetAllSubGroupNamesForBillSeries(String clientCode, boolean addFilter, String filter) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.setLength(0);
		sqlBuilder.append("select a.strSubGroupName,a.strSubGroupCode from tblsubgrouphd a ");
		if (addFilter)
		{
			sqlBuilder.append(" where a.strSubGroupCode NOT IN ");
			sqlBuilder.append(filter);
		}

		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public List funGetAllMenuHeadNamesForBillSeries(String clientCode, boolean addFilter, String filter) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.setLength(0);
		sqlBuilder.append("select a.strMenuName,a.strMenuCode from tblmenuhd a ");
		if (addFilter)
		{
			sqlBuilder.append(" where a.strMenuCode NOT IN ");
			sqlBuilder.append(filter);
		}

		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public List funGetAllRevenueHeadNamesForBillSeries(String clientCode, boolean addFilter, String filter) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.setLength(0);
		sqlBuilder.append("select a.strRevenueHead,a.strRevenueHead  from tblitemmaster a ");
		if (addFilter)
		{
			sqlBuilder.append(" where a.strRevenueHead NOT IN ");
			sqlBuilder.append(filter);
		}
		sqlBuilder.append(" group by a.strRevenueHead;");
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	
	public void funSaveUpdateDiscountMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public clsMenuItemMasterModel funSelectedDiscountDiscountOnItemData(String code, String clientCode) throws Exception
	{
		clsMenuItemMasterModel objMenuItemMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("itemCode", code);
		hmParameters.put("clientCode", clientCode);
		objMenuItemMasterModel = (clsMenuItemMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getMenuItemMaster", hmParameters);
		return objMenuItemMasterModel;

	}


	public clsSetupHdModel funGetPOSWisePropertySetup(String clientCode, String POSCode) throws Exception
	{
		clsSetupHdModel objSetupHdModel = new clsSetupHdModel();
		try{
			List list = obBaseService.funLoadAllPOSWise(objSetupHdModel, clientCode, POSCode);
			if (list.size() > 0)
			{
				objSetupHdModel = (clsSetupHdModel) list.get(0);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return objSetupHdModel;
	}
	public clsMenuItemMasterModel funGetItemImage(String itemCode, String clientCode)
	{
		clsMenuItemMasterModel objMenuItemMasterModel = new clsMenuItemMasterModel();
		try{
			List list = obBaseService.funLoadItemImage(objMenuItemMasterModel, itemCode, clientCode);
			if (list.size() > 0)
			{
				objMenuItemMasterModel = (clsMenuItemMasterModel) list.get(0);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return objMenuItemMasterModel;
	}
	public List funFillCityCombo(String strClientCode) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select a.strCityName,a.strState,a.strCountry  from tblsetup a where a.strClientCode=" + strClientCode);
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public List funFillPOSCombo(String strClientCode) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select strPOSCode,strPOSName from tblposmaster where strOperationalYN='Y'");
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public List funFullPOSCombo(String strClientCode) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder("select strPOSCode,strPOSName from tblposmaster where strOperationalYN='Y' and strClientCode=" + strClientCode);
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public void funSaveUpdatePOSWiseItemIncentive(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public void funSaveSubGroupMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}


	public void funSaveShiftMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}
	public void funSaveTableMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public clsTableMasterModel funSelectedTableMasterData(String tableNo, String clientCode) throws Exception
	{
		clsTableMasterModel objTableMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("tableNo", tableNo);
		hmParameters.put("clientCode", clientCode);
		objTableMasterModel = (clsTableMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getTableMaster", hmParameters);
		return objTableMasterModel;
	}

	public clsTaxMasterModel funSelectedTaxMasterData(String taxCode, String clientCode) throws Exception
	{
		clsTaxMasterModel objTaxMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("taxCode", taxCode);
		hmParameters.put("clientCode", clientCode);
		objTaxMasterModel = (clsTaxMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getTaxMaster", hmParameters);
		return objTaxMasterModel;
	}

	public List funGetAllPOSData(String clientCode) throws Exception
	{
		List listPOSMaster = null;
		listPOSMaster = obBaseService.funGetSerachList("getAllPOSMaster", clientCode);
		return listPOSMaster;
	}

	public List<clsTaxMasterModel> funGetAllTaxForMaster(String clientCode) throws Exception
	{
		List<clsTaxMasterModel> list = null;
		list = obBaseService.funLoadAll(new clsTaxMasterModel(), clientCode);
		return list;
	}

	public void funSaveTaxMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public Map funGetFormListByModuleType(String moduleType) throws Exception
	{
		List listData = new ArrayList<>();
		Map hmRet = new TreeMap<>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select a.strFormName ,a.strModuleName from tblforms  a where a.strModuleType='" + moduleType + "'");
		List rsReports = obBaseService.funGetList(sqlBuilder, "sql");
		if (rsReports != null)
		{
			for (int i = 0; i < rsReports.size(); i++)
			{
				Map hmData = new TreeMap();
				Boolean flag = new Boolean(false);
				Object[] obj = (Object[]) rsReports.get(i);
				String moduleName = obj[0].toString();
				hmData.put("moduleName", moduleName);
				hmData.put("flag", flag);
				listData.add(hmData);
			}
		}
		hmRet.put("jArr", listData);
		return hmRet;
	}

	public List<clsTableMasterModel> funGetTableList(String clientCode) throws Exception
	{
		List<clsTableMasterModel> listModel = null;
		listModel = obBaseService.funLoadAll(new clsTableMasterModel(), clientCode);
		return listModel;
	}

	public List<clsModifierGroupMasterHdModel> funLoadAllModifierGroup(String strClientCode) throws Exception
	{

		List<clsModifierGroupMasterHdModel> list = null;
		list = obBaseService.funLoadAll(new clsModifierGroupMasterHdModel(), strClientCode);
		return list;

	}

	public List funLoadItemPricingMasterData(String menuCode, String modifierCode, String clientCode) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT m.strItemName,m.strItemCode,ifnull(n.strModifierCode,''),ifnull(n.strDefaultModifier,''),if(n.strItemCode is Null,'N','Y')\n" + " FROM tblmenuitempricingdtl m \n" + " left outer join tblitemmodofier n on m.strItemCode=n.strItemCode and  n.strModifierCode='" + modifierCode + "' \n" + " WHERE m.strMenuCode='" + menuCode + "'and n.strClientCode='"+clientCode+"' group by m.strItemCode");

		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public void funSaveUpdateItemModifierMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public clsModifierMasterHdModel funGetItemModifierMasterData(Map hmParameters) throws Exception
	{
		clsModifierMasterHdModel objModifierMasterModel = null;

		objModifierMasterModel = (clsModifierMasterHdModel) obBaseService.funGetAllMasterDataByDocCodeWise("getModifierMaster", hmParameters);
		return objModifierMasterModel;
	}

	public List funFillAllItemList(String strClientCode) throws Exception
	{
		List list = null;
		list = obBaseService.funLoadAll(new clsMenuItemMasterModel(), strClientCode);
		return list;
	}


	
	public List funFillAllRevenuHeadCombo() throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder("select * from tblitemmaster order by strRevenueHead; ");
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	public List funGetBilledTableName(String strPosCode, String strClientCode) throws Exception
	{
		List list = null;

		StringBuilder sb = new StringBuilder();
		sb.append("select a.strTableNo ,b.strTableName " + "from tblbillhd a,tbltablemaster b " + "where a.strTableNo=b.strTableNo " + "and a.strPOSCode='" + strPosCode + "' and a.strClientCode='" + strClientCode + "' " + "and a.strBillNo NOT IN (SELECT strBillNo FROM tblbillsettlementdtl) " + "group by b.strTableNo " + "order by b.strTableNo ");
		list = obBaseService.funGetList(sb, "sql");
		return list;

	}

	// Pratiksha 06-02-2019

	public void funSaveUpdateUserRegistrationMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	// Pratiksha 5-03-2019
	public clsMenuHeadMasterModel funGetMenuHeadMasterData(String menuCode, String clientCode) throws Exception
	{
		clsMenuHeadMasterModel objMenuItemMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("menuCode", menuCode);
		hmParameters.put("clientCode", clientCode);
		objMenuItemMasterModel = (clsMenuHeadMasterModel) obBaseService.funGetAllMasterDataByDocCodeWise("getMenuItemMaster", hmParameters);
		return objMenuItemMasterModel;
	}

	public void funSavePrinterSetupMaster(clsBaseModel objBaseModel) throws Exception
	{
		obBaseService.funSave(objBaseModel);
	}

	public List funFullCostCenterCombo(String strClientCode) throws Exception
	{
		List list = null;
		StringBuilder sqlBuilder = new StringBuilder("select strCostCenterCode ,strCostCenterName from tblcostcentermaster where strClientCode=" + strClientCode);
		list = obBaseService.funGetList(sqlBuilder, "sql");
		return list;
	}

	

	//Pratiksha 20-05-2019
	public List funFillUserCodeCombo(String clientCode) throws Exception
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select strUserCode,strUserName from tbluserhd ");
		List list = obBaseService.funGetList(sql, "sql");
		return list;

		// TODO Auto-generated method stub
	}
	public clsModifierGroupMasterHdModel funSelectedGroupModifierMasterData(String groupModifierCode, String clientCode)
	{
		// TODO Auto-generated method stub
		clsModifierGroupMasterHdModel objModifierGroupMasterModel = null;
		Map<String, String> hmParameters = new HashMap<String, String>();
		hmParameters.put("groupModifierCode", groupModifierCode);
		hmParameters.put("clientCode", clientCode);
		objModifierGroupMasterModel = (clsModifierGroupMasterHdModel) obBaseService.funGetAllMasterDataByDocCodeWise("getModifierGroupMaster", hmParameters);
		System.out.println();
		return objModifierGroupMasterModel;
	}
	
	
	public void funSaveUpdateGroupModifierMaster(clsBaseModel objBaseModel) throws Exception
	{
		// TODO Auto-generated method stub
		obBaseService.funSave(objBaseModel);

	}

}
