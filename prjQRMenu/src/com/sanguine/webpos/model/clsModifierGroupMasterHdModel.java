package com.sanguine.webpos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;

import org.hibernate.annotations.CollectionOfElements;

import com.sanguine.base.model.clsBaseModel;


@Entity
@Table(name="tblmodifiergrouphd")
@IdClass(clsModifierGroupMasterModel_ID.class)
@NamedQueries({
	@NamedQuery (name="POSModifierGroupMaster" ,query="select a.strModifierGroupCode ,a.strModifierGroupName , a.strModifierGroupShortName,"
		+ " a.strOperational  from clsModifierGroupMasterHdModel a where strClientCode=:clientCode"),
	@NamedQuery (name="getModifierGroupMaster",query=" from clsModifierGroupMasterHdModel where strModifierGroupCode=:groupModifierCode and strClientCode=:clientCode") 	})
public class clsModifierGroupMasterHdModel extends clsBaseModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public clsModifierGroupMasterHdModel(){}

	public clsModifierGroupMasterHdModel(clsModifierGroupMasterModel_ID objModelID){
		strModifierGroupCode = objModelID.getStrModifierGroupCode();
		strClientCode = objModelID.getStrClientCode();
	}
	@CollectionOfElements(fetch=FetchType.EAGER)
	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strModifierGroupCode",column=@Column(name="strModifierGroupCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strModifierGroupCode")
	private String strModifierGroupCode;

	@Column(name="strModifierGroupName")
	private String strModifierGroupName;

	@Column(name="strModifierGroupShortName")
	private String strModifierGroupShortName;

	@Column(name="strApplyMaxItemLimit")
	private String strApplyMaxItemLimit;

	@Column(name="intItemMaxLimit")
	private int intItemMaxLimit;

	@Column(name="strOperational")
	private String strOperational;

	@Column(name="strUserCreated")
	private String strUserCreated;

	@Column(name="strUserEdited")
	private String strUserEdited;

	@Column(name="dteDateCreated")
	private String dteDateCreated;

	@Column(name="dteDateEdited")
	private String dteDateEdited;

	@Column(name="strClientCode")
	private String strClientCode;

	@Column(name="strDataPostFlag")
	private String strDataPostFlag;

	@Column(name="strApplyMinItemLimit")
	private String strApplyMinItemLimit;

	@Column(name="intItemMinLimit")
	private int intItemMinLimit;

	@Column(name="intSequenceNo")
	private int intSequenceNo;

//Setter-Getter Methods
	public String getStrModifierGroupCode(){
		return strModifierGroupCode;
	}
	public void setStrModifierGroupCode(String strModifierGroupCode){
		this. strModifierGroupCode = (String) setDefaultValue( strModifierGroupCode, "NA");
	}

	public String getStrModifierGroupName(){
		return strModifierGroupName;
	}
	public void setStrModifierGroupName(String strModifierGroupName){
		this. strModifierGroupName = (String) setDefaultValue( strModifierGroupName, "NA");
	}

	public String getStrModifierGroupShortName(){
		return strModifierGroupShortName;
	}
	public void setStrModifierGroupShortName(String strModifierGroupShortName){
		this. strModifierGroupShortName = (String) setDefaultValue( strModifierGroupShortName, "NA");
	}

	

	public String getStrApplyMaxItemLimit()
	{
		return strApplyMaxItemLimit;
	}

	public void setStrApplyMaxItemLimit(String strApplyMaxItemLimit)
	{
		this.strApplyMaxItemLimit = strApplyMaxItemLimit;
	}


	public String getStrOperational(){
		return strOperational;
	}
	public void setStrOperational(String strOperational){
		this. strOperational = (String) setDefaultValue( strOperational, "NA");
	}

	public String getStrUserCreated(){
		return strUserCreated;
	}
	public void setStrUserCreated(String strUserCreated){
		this. strUserCreated = (String) setDefaultValue( strUserCreated, "NA");
	}

	public String getStrUserEdited(){
		return strUserEdited;
	}
	public void setStrUserEdited(String strUserEdited){
		this. strUserEdited = (String) setDefaultValue( strUserEdited, "NA");
	}

	public String getDteDateCreated(){
		return dteDateCreated;
	}
	public void setDteDateCreated(String dteDateCreated){
		this.dteDateCreated=dteDateCreated;
	}

	public String getDteDateEdited(){
		return dteDateEdited;
	}
	public void setDteDateEdited(String dteDateEdited){
		this.dteDateEdited=dteDateEdited;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this. strClientCode = (String) setDefaultValue( strClientCode, "NA");
	}

	public String getStrDataPostFlag(){
		return strDataPostFlag;
	}
	public void setStrDataPostFlag(String strDataPostFlag){
		this. strDataPostFlag = (String) setDefaultValue( strDataPostFlag, "NA");
	}





	public int getIntItemMaxLimit()
	{
		return intItemMaxLimit;
	}

	public void setIntItemMaxLimit(int intItemMaxLimit)
	{
		this.intItemMaxLimit = intItemMaxLimit;
	}

	public String getStrApplyMinItemLimit()
	{
		return strApplyMinItemLimit;
	}

	public void setStrApplyMinItemLimit(String strApplyMinItemLimit)
	{
		this.strApplyMinItemLimit = strApplyMinItemLimit;
	}

	
	public int getIntSequenceNo()
	{
		return intSequenceNo;
	}

	public void setIntSequenceNo(int intSequenceNo)
	{
		this.intSequenceNo = intSequenceNo;
	}

	public int getIntItemMinLimit()
	{
		return intItemMinLimit;
	}

	public void setIntItemMinLimit(int intItemMinLimit)
	{
		this.intItemMinLimit = intItemMinLimit;
	}

	


}
