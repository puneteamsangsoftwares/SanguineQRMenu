package com.sanguine.webpos.bean;



public class clsReservationBean
{
	
    private String strCustomerCode;
		
    private String strBookingDate;
	
    private String strYear;
    
    private String strBookingTimeHour;

    private String strBookingTimeMin;

    private String strAMPM;
	
    private String strDateCreated;
	
    private int intNoOfGuests;
	
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

	private String strClientCode;

	public String getStrBookingTimeHour()
	{
		return strBookingTimeHour;
	}

	public void setStrBookingTimeHour(String strBookingTimeHour)
	{
		this.strBookingTimeHour = strBookingTimeHour;
	}

	public String getStrBookingTimeMin()
	{
		return strBookingTimeMin;
	}

	public void setStrBookingTimeMin(String strBookingTimeMin)
	{
		this.strBookingTimeMin = strBookingTimeMin;
	}

	public String getStrYear()
	{
		return strYear;
	}

	public void setStrYear(String strYear)
	{
		this.strYear = strYear;
	}

}
