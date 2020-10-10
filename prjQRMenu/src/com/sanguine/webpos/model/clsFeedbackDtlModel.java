package com.sanguine.webpos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
public class clsFeedbackDtlModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Column(name="strQuestionCode")
	private String strQuestionCode;

	@Column(name="intRating")
	private int intRating;

	
	public String getStrQuestionCode()
	{
		return strQuestionCode;
	}

	public void setStrQuestionCode(String strQuestionCode)
	{
		this.strQuestionCode = strQuestionCode;
	}

	public int getIntRating()
	{
		return intRating;
	}

	public void setIntRating(int intRating)
	{
		this.intRating = intRating;
	}

	

}
