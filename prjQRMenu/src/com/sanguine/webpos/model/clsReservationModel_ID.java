package com.sanguine.webpos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class clsReservationModel_ID implements Serializable
{
public clsReservationModel_ID(){
		
	}
	
	
	public clsReservationModel_ID(String strCustomerCode, String strBookingCode, String strClientCode)
	{
		this.strCustomerCode = strCustomerCode;
		this.strBookingCode = strBookingCode;
		this.strClientCode = strClientCode;
	}

	@Column(name = "strCustomerCode")
	private String strCustomerCode;
	

	@Column(name = "strBookingCode")
	private String strBookingCode;
	
	@Column(name = "strClientCode")
	private String strClientCode;

	
	
	public String getStrCustomerCode()
	{
		return strCustomerCode;
	}

	public void setStrCustomerCode(String strCustomerCode)
	{
		this.strCustomerCode = strCustomerCode;
	}

	public String getStrBookingCode()
	{
		return strBookingCode;
	}

	public void setStrBookingCode(String strBookingCode)
	{
		this.strBookingCode = strBookingCode;
	}

	public String getStrClientCode()
	{
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode)
	{
		this.strClientCode = strClientCode;
	}


}
