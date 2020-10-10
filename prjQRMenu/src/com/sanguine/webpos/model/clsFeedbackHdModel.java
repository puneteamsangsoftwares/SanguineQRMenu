package com.sanguine.webpos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.sanguine.base.model.clsBaseModel;

@Entity
@Table(name="tblfeedbackhd")
@IdClass(clsFeedbackHdModel_ID.class)
public class clsFeedbackHdModel extends clsBaseModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="strFeedbackCode")
	private String strFeedbackCode;
	
	@Column(name="strClientCode")
	private String strClientCode;
	
	@Column(name="strCustomerCode")
	private String strCustomerCode;
	
	@Column(name="strRemark")
	private String strRemark;
	
	@Column(name="dteDateCreated")
	private String dteDateCreated;
	
	@Column(name="dblAvgRating")
	private double dblAvgRating;

	public clsFeedbackHdModel()
	{
		// TODO Auto-generated constructor stub
	}
		
	
	
	public clsFeedbackHdModel(clsFeedbackHdModel_ID objModel)
	{
		this.strFeedbackCode = objModel.getStrFeedbackCode();
		this.strClientCode = objModel.getStrClientCode();
	}

	@CollectionOfElements(fetch=FetchType.EAGER)
    @JoinTable(name="tblfeedbackdtl" , joinColumns={@JoinColumn(name="strClientCode"),@JoinColumn(name="strFeedbackCode")})
	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode")),
		@AttributeOverride(name="strFeedbackCode",column=@Column(name="strFeedbackCode"))
	})
	@Embedded
	@Fetch(FetchMode.SUBSELECT)
	private List<clsFeedbackDtlModel> listFeedbackDtl= new ArrayList<clsFeedbackDtlModel>();
	

	public String getStrFeedbackCode()
	{
		return strFeedbackCode;
	}

	public void setStrFeedbackCode(String strFeedbackCode)
	{
		this.strFeedbackCode = strFeedbackCode;
	}

	public String getStrClientCode()
	{
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode)
	{
		this.strClientCode = strClientCode;
	}

	public String getStrCustomerCode()
	{
		return strCustomerCode;
	}

	public void setStrCustomerCode(String strCustomerCode)
	{
		this.strCustomerCode = strCustomerCode;
	}

	public String getStrRemark()
	{
		return strRemark;
	}

	public void setStrRemark(String strRemark)
	{
		this.strRemark = strRemark;
	}

	public String getDteDateCreated()
	{
		return dteDateCreated;
	}

	public void setDteDateCreated(String dteDateCreated)
	{
		this.dteDateCreated = dteDateCreated;
	}

	public double getDblAvgRating()
	{
		return dblAvgRating;
	}

	public void setDblAvgRating(double dblAvgRating)
	{
		this.dblAvgRating = dblAvgRating;
	}



	public List<clsFeedbackDtlModel> getListFeedbackDtl()
	{
		return listFeedbackDtl;
	}



	public void setListFeedbackDtl(List<clsFeedbackDtlModel> listFeedbackDtl)
	{
		this.listFeedbackDtl = listFeedbackDtl;
	}
	
	
	
	
}
