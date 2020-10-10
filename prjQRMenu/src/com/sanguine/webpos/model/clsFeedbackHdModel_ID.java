package com.sanguine.webpos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")
public class clsFeedbackHdModel_ID implements Serializable
{
 
	
	@Column(name="strFeedbackCode")
	private String strFeedbackCode;
	
	@Column(name="strClientCode")
	private String strClientCode;
	 public clsFeedbackHdModel_ID()
	{
		
	}
	
	public clsFeedbackHdModel_ID(String strFeedbackCode, String strClientCode)
	{
		this.strFeedbackCode = strFeedbackCode;
		this.strClientCode = strClientCode;
	}

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

	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((strClientCode == null) ? 0 : strClientCode.hashCode());
		result = prime * result + ((strFeedbackCode == null) ? 0 : strFeedbackCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		clsFeedbackHdModel_ID other = (clsFeedbackHdModel_ID) obj;
		if (strClientCode == null)
		{
			if (other.strClientCode != null)
				return false;
		}
		else if (!strClientCode.equals(other.strClientCode))
			return false;
		if (strFeedbackCode == null)
		{
			if (other.strFeedbackCode != null)
				return false;
		}
		else if (!strFeedbackCode.equals(other.strFeedbackCode))
			return false;
		return true;
	}
}
