package com.sanguine.webpos.bean;

public class clsPOSComplimentayDtlsOnBill
{
        
	
	private String strItemCode;
    private String strItemName;
    
    private double dblRate;
    private double dblQuantity;
    private double dblCompQuantity;
    private double dblAmount;
    
    public String getStrItemCode()
	{
		return strItemCode;
	}
	public void setStrItemCode(String strItemCode)
	{
		this.strItemCode = strItemCode;
	}
	public String getStrItemName()
	{
		return strItemName;
	}
	public void setStrItemName(String strItemName)
	{
		this.strItemName = strItemName;
	}
	public double getDblRate()
	{
		return dblRate;
	}
	public void setDblRate(double dblRate)
	{
		this.dblRate = dblRate;
	}
	public double getDblQuantity()
	{
		return dblQuantity;
	}
	public void setDblQuantity(double dblQuantity)
	{
		this.dblQuantity = dblQuantity;
	}
	public double getDblCompQuantity()
	{
		return dblCompQuantity;
	}
	public void setDblCompQuantity(double dblCompQuantity)
	{
		this.dblCompQuantity = dblCompQuantity;
	}
	public double getDblAmount()
	{
		return dblAmount;
	}
	public void setDblAmount(double dblAmount)
	{
		this.dblAmount = dblAmount;
	}
	

}
