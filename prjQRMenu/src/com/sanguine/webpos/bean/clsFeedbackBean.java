package com.sanguine.webpos.bean;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;

public class clsFeedbackBean
{

	
	private JSONArray jsonAllQuestion=new JSONArray();
	
	
	private Map mapAllQuestion=new HashMap();
	
	private String strRemark;
	
	private String strAllQueRating;

	public JSONArray getJsonAllQuestion()
	{
		return jsonAllQuestion;
	}

	public void setJsonAllQuestion(JSONArray jsonAllQuestion)
	{
		this.jsonAllQuestion = jsonAllQuestion;
	}

	public Map getMapAllQuestion()
	{
		return mapAllQuestion;
	}

	public void setMapAllQuestion(Map mapAllQuestion)
	{
		this.mapAllQuestion = mapAllQuestion;
	}

	public String getStrRemark()
	{
		return strRemark;
	}

	public void setStrRemark(String strRemark)
	{
		this.strRemark = strRemark;
	}

	public String getStrAllQueRating()
	{
		return strAllQueRating;
	}

	public void setStrAllQueRating(String strAllQueRating)
	{
		this.strAllQueRating = strAllQueRating;
	}


}
