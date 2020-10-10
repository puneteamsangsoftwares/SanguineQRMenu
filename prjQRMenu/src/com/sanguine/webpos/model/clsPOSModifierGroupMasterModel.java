package com.sanguine.webpos.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Id;

import com.sanguine.base.model.clsBaseModel;

public class clsPOSModifierGroupMasterModel extends clsBaseModel implements Serializable
{
	private static final long serialVersionUID = 1L;

	public clsPOSModifierGroupMasterModel()
	{
	}

	public clsPOSModifierGroupMasterModel(clsPOSModifierGroupMasterModel_ID objModelID)
	{
		strModifierGroupCode = objModelID.getStrModifierGroupCode();
		strClientCode = objModelID.getStrClientCode();

	}

	@Id
	@AttributeOverrides({ @AttributeOverride(name = "strModifierGroupCode", column = @Column(name = "strModifierGroupCode")), @AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode")) })
	@Column(name = "strClientCode")
	private String strClientCode;

	@Column(name = "strModifierGroupCode")
	private String strModifierGroupCode;

	@Column(name = "strModifierGroupName")
	private String strModifierGroupName;

	@Column(name = "strModifierGroupShortName")
	private String strModifierGroupShortName;

	
	@Column(name = "strMinItemLimit")
	private long strMinItemLimit;

	@Column(name = "strMaxModifierSelection")
	private String strMaxModifierSelection;

	@Column(name = "strMaxItemLimit")
	private long strMaxItemLimit;

	@Column(name = "strSequenceNo")
	private int strSequenceNo;

	@Column(name = "strModGrpOperational")
	private String strModGrpOperational;

	@Column(name = "strOperationType")
	private String strOperationType;

	@Column(name = "strDataPostFlag")
	private String strDataPostFlag;

	@Column(name = "strPOSCode")
	private String strPOSCode;
}
