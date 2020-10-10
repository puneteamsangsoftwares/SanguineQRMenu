package com.sanguine.webpos.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")
public class clsPOSModifierGroupMasterModel_ID
{
	// Variable Declaration
    @Column(name = "strModifierGroupCode")
    private String strModifierGroupCode;
    
    @Column(name = "strClientCode")
    private String strClientCode;

    
    public clsPOSModifierGroupMasterModel_ID()
    {
    }
    
    public clsPOSModifierGroupMasterModel_ID(String strModifierGroupCode, String strClientCode)
    {
	this.strModifierGroupCode = strModifierGroupCode;
	this.strClientCode = strClientCode;
	
    }
	public String getStrModifierGroupCode()
	{
		return strModifierGroupCode;
	}

	public void setStrModifierGroupCode(String strModifierGroupCode)
	{
		this.strModifierGroupCode = strModifierGroupCode;
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
