package com.sanguine.webpos.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

import com.sanguine.base.model.clsBaseModel;
@Entity
@Table(name="tblbooking")
public class clsReservationModel extends clsBaseModel implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	
	public clsReservationModel(){
		
	}
	
     public clsReservationModel(clsReservationModel_ID objModelID){
		
    	 strCustomerCode = objModelID.getStrCustomerCode();
 		 strClientCode = objModelID.getStrClientCode();
 		 strBookingCode=objModelID.getStrBookingCode();
 	
	}
	
	
	
	@CollectionOfElements(fetch = FetchType.EAGER)
	@Id
	@AttributeOverrides(
	{
			@AttributeOverride(name = "strCustomerCode", column = @Column(name = "strCustomerCode")),
			@AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode")),
			@AttributeOverride(name = "strBookingCode", column = @Column(name = "strBookingCode"))

	})
		

	@Column(name = "strBookingCode")
	private String strBookingCode;

	@Column(name="strClientCode")
    private String strClientCode;


	@Column(name="strCustomerCode")
    private String strCustomerCode;
	
	@Column(name="strBookingDate")
    private String strBookingDate;
	
	
	@Column(name="strBookingTime")
    private String strBookingTime;
	
	
	@Column(name="strAMPM")
    private String strAMPM;
	
	@Column(name="strDateCreated")
    private String strDateCreated;
	
	@Column(name="intNoOfGuests")
    private int intNoOfGuests;
	
	@Column(name="strStatus")
    private String strStatus;
	
		public String getStrCustomerCode()
	{
		return strCustomerCode;
	}

	public void setStrCustomerCode(String strCustomerCode)
	{
		this.strCustomerCode = strCustomerCode;
	}

	public String getStrBookingDate()
	{
		return strBookingDate;
	}

	public void setStrBookingDate(String strBookingDate)
	{
		this.strBookingDate = strBookingDate;
	}

	public String getStrBookingTime()
	{
		return strBookingTime;
	}

	public void setStrBookingTime(String strBookingTime)
	{
		this.strBookingTime = strBookingTime;
	}

	public String getStrAMPM()
	{
		return strAMPM;
	}

	public void setStrAMPM(String strAMPM)
	{
		this.strAMPM = strAMPM;
	}

	public String getStrDateCreated()
	{
		return strDateCreated;
	}

	public void setStrDateCreated(String strDateCreated)
	{
		this.strDateCreated = strDateCreated;
	}

	public int getIntNoOfGuests()
	{
		return intNoOfGuests;
	}

	public void setIntNoOfGuests(int intNoOfGuests)
	{
		this.intNoOfGuests = intNoOfGuests;
	}

	public String getStrStatus()
	{
		return strStatus;
	}

	public void setStrStatus(String strStatus)
	{
		this.strStatus = strStatus;
	}

	public String getStrClientCode()
	{
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode)
	{
		this.strClientCode = strClientCode;
	}

	public String getStrBookingCode()
	{
		return strBookingCode;
	}

	public void setStrBookingCode(String strBookingCode)
	{
		this.strBookingCode = strBookingCode;
	}
	
	
	
}
