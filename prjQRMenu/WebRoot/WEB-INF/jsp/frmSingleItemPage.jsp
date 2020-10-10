<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Single Item Page</title>
<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width">
	<meta name="viewport" content="initial-scale=1.0">
	<meta name="viewport" content="width=590">
	<meta charset="UTF-8" />
	
	<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery-confirm.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/css/		materialdesignicons.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/fonts/materialdesignicons-webfont.ttf">
	<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@100;300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/jquery-confirm.min.css"/>"/>
    
    <script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/mdb.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/print.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/easy-numpad.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/confirm-prompt.js"/>"></script>
	
<style type="text/css">
.card {
/*  background-color: #c7c7c747;
*/  border-radius: 2px;
  box-shadow: -1px 2px 4px rgba(0,0,0,.25), 1px 1px 3px rgba(0,0,0,.1);
  margin: 0 auto;
/*  max-width: 340px;*/  
/*  margin-top: 10px;
*/}
.card-media {width: 100%;}
.card-details {padding: 10px 5px;}
.card-head {font-weight: 400;}
.card-head {color: #000;}
.card-action-button {
  border-radius: 2px;
  color: #188a36;
  cursor: pointer;
  display: inline-block;
  font-weight: 500;
  margin: 0;
  padding: 10px;
  text-decoration: none;
}
button.btn{position: initial;color;}
button.btn a{color:#fff;}
.card-action-button:hover {background-color: rgba(255 ,168 ,58, .2);}
</style>
	
	
<script type="text/javascript">
var objMenuItemButton;
var objIndex;
var NumPadDialogFor;	
var itemChangeQtySelected, itemPrice,strItemCode,strItemCodeFromMainMenu,dblItemRate;


var jsonArrForPricing=${command.jsonArrForDirectBillerMenuItemPricing};	

$(document).ready(function() 
		{
	         funDisplayCart();
	         funShowItem();

	        
		});


function getContextPath() 
{
	return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}


	
    function funDisplayqty()
	{
		//tempVal="vin";
		
		 document.getElementById("btnAddToCart").style.display='none';
		 document.getElementById("qtydiscplay").style.display='block'		 
		 document.getElementById("itemCountInSinglePage").style.display='block';
		 document.getElementById("itemCountInMainPage").style.display='block';
		 document.getElementById("btnAddItem").style.display='block';
	     $("#txtAddQuantity").val(1);
	  //   funMenuItemClicked();
	}
	function funDisplayCart()
	{
		 document.getElementById("btnAddToCart").style.display='block';
		 document.getElementById("qtydiscplay").style.display='none';
		 document.getElementById("btnAddItem").style.display='none';


	}

	function funMenuItemClicked()
	{
		var qty=$("#txtAddQuantity").val();

		var jsonArrForMenuItemPricing=${command.jsonArrForDirectBillerMenuItemPricing};	

		var objMenuItemPricingDtl;
		$.each(jsonArrForMenuItemPricing, function(i, obj) 
		{									
			if(strItemCodeFromMainMenu==obj.strItemCode)
			{
				objMenuItemPricingDtl=obj;
			}	
		});

		//$("#lblItemDesc").text(itemDel);


	var itemCode=objMenuItemPricingDtl.strItemCode;
	//showPopup();
	 funFillMapWithHappyHourItems();


	itemPrice = funGetFinalPrice(objMenuItemPricingDtl);
	funMenuItemClicked1(objMenuItemPricingDtl,qty,itemPrice);
	
	}
	function funFillMapWithHappyHourItems()
	{		
	var searchurl=getContextPath()+"/funFillMapWithHappyHourItems.html";
	$.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        async:false,
		        success: function(response)
		        {
		        	for(var i=0;i<response.ItemPriceDtl.length;i++)
	        		{
//			        		hmHappyHourItems.put(response.ItemCode[i],response.ItemPriceDtl[i]);
		        		hmHappyHourItems[response.ItemCode[i]] = response.ItemPriceDtl[i];
	        		}
		        	gDebitCardPayment=response.gDebitCardPayment;
		        	currentDate=response.CurrentDate;
		        	currentTime=response.CurrentTime;
		        	dayForPricing=response.DayForPricing;
		        	
			},
				error: function(jqXHR, exception) {
		            if (jqXHR.status === 0) {
		                alert('Not connect.n Verify Network.');
		            } else if (jqXHR.status == 404) {
		                alert('Requested page not found. [404]');
		            } else if (jqXHR.status == 500) {
		                alert('Internal Server Error [500].');
		            } else if (exception === 'parsererror') {
		                alert('Requested JSON parse failed.');
		            } else if (exception === 'timeout') {
		                alert('Time out error.');
		            } else if (exception === 'abort') {
		                alert('Ajax request aborted.');
		            } else {
		                alert('Uncaught Error.n' + jqXHR.responseText);
		            }		            
		        }
	      });
	}
	function  funGetFinalPrice( ob)
	{
	var Price = 0.00;
	var fromTime = ob.tmeTimeFrom;
	var toTime = ob.tmeTimeTo;
	var fromAMPM = ob.strAMPMFrom;
	var toAMPM = ob.strAMPMTo;
	var hourlyPricing = ob.strHourlyPricing;
	var strItemCode=ob.strItemCode;
	if (hmHappyHourItems.has(strItemCode))
	{
	    var obHappyHourItem = hmHappyHourItems.get(ob.strItemCode);
	    fromTime = obHappyHourItem.tmeTimeFrom;
	    toTime = obHappyHourItem.tmeTimeTo;
	    fromAMPM = obHappyHourItem.strAMPMFrom;
	    toAMPM = obHappyHourItem.strAMPMTo;

	    var spFromTime = fromTime.split(":");
	    var spToTime = toTime.split(":");
	    var fromHour = parseInt(spFromTime[0]);
	    var fromMin = parseInt(spFromTime[1]);
	    if (fromAMPM=="PM")
	    {
	        fromHour += 12;
	    }
	    var toHour = parseInt(spToTime[0]);
	    var toMin = parseInt(spToTime[1]);
	    if (toAMPM=="PM")
	    {
	        toHour += 12;
	    }
		var spCurrTime = currentTime.split(" ");
	    var spCurrentTime = spCurrTime[0].split(":");

	    var currHour = parseInt(spCurrentTime[0]);
	    var currMin = parseInt(spCurrentTime[1]);
	    var currDate = currentDate;
	    currDate = currDate + " " + currHour + ":" + currMin + ":00";

	    //2014-09-09 23:35:00
	    var fromDate = currentDate;
	    var toDate = currentDate();
	    fromDate = fromDate + " " + fromHour + ":" + fromMin + ":00";
	    toDate = toDate + " " + toHour + ":" + toMin + ":00";

	    var diff1 = CalculateDateDiff(fromDate, currDate);
	    var diff2 = CalculateDateDiff(currDate, toDate);
	    if (diff1 > 0 && diff2 > 0)
	    {
	        switch (dayForPricing)
	        {
	            case "strPriceMonday":
	                Price = obHappyHourItem.strPriceMonday;
	                break;

	            case "strPriceTuesday":
	                Price = obHappyHourItem.strPriceTuesday;
	                break;

	            case "strPriceWednesday":
	                Price = obHappyHourItem.strPriceWednesday;
	                break;

	            case "strPriceThursday":
	                Price = obHappyHourItem.strPriceThursday;
	                break;

	            case "strPriceFriday":
	                Price = obHappyHourItem.strPriceFriday;
	                break;

	            case "strPriceSaturday":
	                Price = obHappyHourItem.strPriceSaturday;
	                break;

	            case "strPriceSunday":
	                Price = obHappyHourItem.strPriceSunday;
	                break;
	        }
	    }
	    else
	    {
	        switch (dayForPricing)
	        {
	            case "strPriceMonday":
	                Price = ob.strPriceMonday;
	                break;

	            case "strPriceTuesday":
	                Price = ob.strPriceTuesday;
	                break;

	            case "strPriceWednesday":
	                Price = ob.strPriceWednesday;
	                break;

	            case "strPriceThursday":
	                Price = ob.strPriceThursday;
	                break;

	            case "strPriceFriday":
	                Price = ob.strPriceFriday;
	                break;

	            case "strPriceSaturday":
	                Price = ob.strPriceSaturday;
	                break;

	            case "strPriceSunday":
	                Price = ob.strPriceSunday;
	                break;
	        }
	    }
	}
	else
	{
	    switch (dayForPricing)
	    {
	        case "strPriceMonday":
	            Price = ob.strPriceMonday;
	            break;

	        case "strPriceTuesday":
	            Price = ob.strPriceTuesday;
	            break;

	        case "strPriceWednesday":
	            Price = ob.strPriceWednesday;
	            break;

	        case "strPriceThursday":
	            Price = ob.strPriceThursday;
	            break;

	        case "strPriceFriday":
	            Price = ob.strPriceFriday;
	            break;

	        case "strPriceSaturday":
	            Price = ob.strPriceSaturday;
	            break;

	        case "strPriceSunday":
	            Price = ob.strPriceSunday;
	            break;
	    }
	}

	return Price;
	}

	function funMenuItemClicked1(objMenuItem,numpadValue,itemPrice)
	{	
	//$("#txtItemSearch").val("");

	funFillMapWithHappyHourItems();

	var objMenuItemPricingDtl=objMenuItem;

	var price = funGetFinalPrice(objMenuItemPricingDtl);

	var isOrdered=funIsAlreadyOrderedItem(objMenuItemPricingDtl);
	var qty=numpadValue;


	if(price==0.00)
	{
		
		 price = funGetFinalPrice(objMenuItemPricingDtl);
		
		 price = itemPrice; //prompt("Enter Price", 0);
	} 
	if(qty==null || price==null)
	{
		 	return false;
	}
	if(isOrdered)
	{
		funUpdateTableBillItemDtlFor(objMenuItemPricingDtl,price,qty);	
	}
	else			
	{
		funFillTableBillItemDtl(objMenuItemPricingDtl,price,qty);	
	} 

	funUpdateCartQty();
	funAddModifierName();
	funBackToDashboard();

	//funFillKOTList();
	//funCalculateTax();
	}
	function funIsAlreadyOrderedItem(objMenuItemPricingDtl)
	{		
	var isOrdered=false;	
	 $.each(listPunchedItmeDtl, function(i, obj) 
	{
		  if(objMenuItemPricingDtl.strItemCode==obj.itemCode)
		  {
			  isOrdered=true;
		  } 
	 })
		
	return isOrdered;

	}

	//function to update item which is alredy order item (duplicate)
	function funUpdateTableBillItemDtlFor(objMenuItemPricingDtl,price,qty)
	{
	   $.each(listPunchedItmeDtl, function(i, obj) 
		{
			  if(objMenuItemPricingDtl.strItemCode==obj.itemCode)
			  {
				  var oldQty = obj.quantity; 
		    	  var oldAmt= obj.amount; 
		    	  var rate=parseFloat(oldAmt)/parseFloat(oldQty);
		    	  var newQty=parseFloat(qty);
		    	  var newAmt=parseFloat(rate)*parseFloat(newQty);
		    	  obj.quantity=newQty;
		    	  obj.amount=newAmt;
		    	  
		    	
			  } 
		 })
//		 funUpdateQtyAndAmount();
	     
		
	}
	var itemPrice=0;
	function funFillTableBillItemDtl(objMenuItemPricingDtl,price,qty)
	{	
		itemPrice=price;
	var itemName=objMenuItemPricingDtl.strItemName.replace(/&#x00A;/g," ");

	var isModifier = false;
	var singleObj = {}
	singleObj['itemName'] =itemName;
	singleObj['quantity'] =parseFloat(qty);
	singleObj['amount'] = parseFloat(qty)*price;
	singleObj['discountPer'] = 0.0;
	singleObj['discountAmt'] =0.0;
	singleObj['strSubGroupCode'] =objMenuItemPricingDtl.strSubGroupCode;
	singleObj['strGroupcode'] =objMenuItemPricingDtl.strGroupcode;
	singleObj['itemCode'] =objMenuItemPricingDtl.strItemCode;
	singleObj['rate'] =price;
	singleObj['isModifier'] =isModifier;
	singleObj['dblCompQty'] ='0';
	singleObj['modItemCode'] ="";
	singleObj['added'] ="abc";


	listPunchedItmeDtl.push(singleObj); 
	//funUpdateQtyAndAmount(); 
	
	}
	function funUpdateQtyAndAmount()
	{
	var quantity=0;
	var amount=0;
	 $.each(listPunchedItmeDtl, function(i, obj) 
	{
		 if(strItemCodeFromMainMenu== obj.itemCode)
		 {
			 quantity= quantity + obj.quantity; 
		 }
		 
	})
	$("#txtAddQuantity").val(quantity);		  

	}
	function funGetSelectedRowIndex(obj)
	{
	 var index = obj.parentNode.parentNode.rowIndex;
	 var table = document.getElementById("tblBillItemDtl");
	 if((selectedRowIndex>0) && (index!=selectedRowIndex))
	 {
		
			 row = table.rows[selectedRowIndex];
			 row.style.backgroundColor='#C0E4FF';
			 selectedRowIndex=index;
			 row = table.rows[selectedRowIndex];
			 row.style.backgroundColor='#fd4de6';
			// row.childNodes[0].childNodes[0].style.color='#fd4de6';
			 row.hilite = true;
	     
		
	 }
	 else
	 {
		 selectedRowIndex=index;
		 row = table.rows[selectedRowIndex];
		 row.style.backgroundColor='#fd4de6';
		// row.childNodes[0].childNodes[0].style.color='#fd4de6';
		 row.hilite = true;
	 }
	 
	  var iteCode=table.rows[selectedRowIndex].cells[3].innerHTML;
	  
	  var codeArr = iteCode.split('value=');
	  var code=codeArr[1].split('onclick=');
	  var itemCode=code[0].substring(1, (code[0].length-2));
		funFillTopModifierButtonList(itemCode);
	}

	function funLoadModifiers(itemCode,rate,onClickIsImage)
	{		
	funGetSingleItemImage(itemCode,onClickIsImage);
	strItemCodeFromMainMenu=itemCode;
	dblItemRate=rate;
     var searchurl = getContextPath() + "/funLoadModifiers.html?itemCode="+ itemCode;
		$.ajax({
			type : "GET",
			url : searchurl,
			dataType : "json",
			success : function(response) {
				funAddModifiersData(response.Modifiers, itemCode);

			},
			error : function(jqXHR, exception) {
				if (jqXHR.status === 0) {
					alert('Not connect.n Verify Network.');
				} else if (jqXHR.status == 404) {
					alert('Requested page not found. [404]');
				} else if (jqXHR.status == 500) {
					alert('Internal Server Error [500].');
				} else if (exception === 'parsererror') {
					alert('Requested JSON parse failed.');
				} else if (exception === 'timeout') {
					alert('Time out error.');
				} else if (exception === 'abort') {
					alert('Ajax request aborted.');
				} else {
					alert('Uncaught Error.n' + jqXHR.responseText);
				}
			}
		});
 
	}
	var strItemCodeForMod = "";
	function funAddModifiersData(Modifiers, itemCode) {
		var $rows = $('#tblModifier').empty();
		
		var tblmodalDataTable = document.getElementById('tblModifier');
		strItemCodeForMod = itemCode;
		$.each(Modifiers,function(i, obj) {
							var name = obj.strModifierName.split(">");
							var modifierDetails = itemCode + "!"+ obj.strModifierCode + "!" + name[1] + "!"+ obj.dblRate;
							var rowCount = tblmodalDataTable.rows.length;

							var insertRow = tblmodalDataTable
									.insertRow(rowCount);

							var col1 = insertRow.insertCell(0);
							var col2 = insertRow.insertCell(1);
							var col3 = insertRow.insertCell(2);
							var itemModCode=itemCode + "!"+ obj.strModifierCode;
							var flagMod=false;
							$.each(listPunchedItmeDtl, function(i, objOld) {
								if (itemModCode == objOld.itemCode) {

									flagMod = true;

								}
							})

                            if(flagMod)
                           	{
    							col1.innerHTML = "<input id=\"ModCheckBox."+(rowCount)+"\" name=\"Modthemes\" checked=\"checked\" type=\"checkbox\" class=\"CustCheckBoxClass\" style=\"margin-bottom:13px; width: 30px; height: 21px;margin-left:8px;\"  value='"+modifierDetails+"' />";
    						}
                            else
                           	{
    							col1.innerHTML = "<input id=\"ModCheckBox."+(rowCount)+"\" name=\"Modthemes\" type=\"checkbox\" class=\"CustCheckBoxClass\" style=\" margin-bottom:13px; width: 30px; height: 21px;margin-left:8px;\"  value='"+modifierDetails+"' />";

                           	}
							//
							col2.innerHTML = "<input readonly=\"readonly\" size=\"50px\" style=\"font-size:23px; font-family:'calibri';font-weight:600; color:black; height:30px;border:none;width:50%;text-align:left;padding-left:3%;margin-bottom:13px;\"    id="+obj.strModifierCode+" value='"+name[1]+"' />";
							col3.innerHTML = "<input readonly=\"readonly\" size=\"6px\"    style=\"text-align: right; font-size:23px; font-weight:600;color:black; height:30px;border:none;margin-right:42px;width:58%;margin-bottom:13px;\"  id=\"dblModRate."
									+ (rowCount)
									+ "\" value='\&#2352;\ "
									+ obj.dblRate + "' />";

						});
		var flagFound = true;
		/* $.each(listPunchedItmeDtl, function(i, obj) {
			if (itemCode == obj.itemCode) {

				$("#txtAddQuantity").val(obj.quantity);
				flagFound = false;

			}
		})

		if (flagFound) {
			$("#txtAddQuantity").val(1);

		}
 */
	}
	
	function funUnCheckAlltextBox()
	{
		 $("input:checkbox").attr('checked', false);

	}
	var modifyAmount = 0;
	
	function funAddModifierName() {

			$('input[name="Modthemes"]:checked').each(function() {
				
				var singleObj = {};
				var modifier = this.value;				
				var modDetails = modifier.split("!");
                var itemOldModCode=modDetails[0] + "!" + modDetails[1];
				if(!funCheckForOldModifier(itemOldModCode))
				{
					singleObj['itemName'] = "-->" + modDetails[2];
					singleObj['quantity'] = 1;
					singleObj['amount'] = modDetails[3];
					singleObj['discountPer'] = 0.0;
					singleObj['discountAmt'] = 0.0;
					singleObj['strSubGroupCode'] = "";
					singleObj['strGroupcode'] = "";
					singleObj['itemCode'] = modDetails[0] + "!" + modDetails[1];
					singleObj['rate'] = modDetails[3];
					singleObj['isModifier'] = true;
					singleObj['dblCompQty'] = '0';
					singleObj['modItemCode'] = modDetails[0];
					singleObj['added'] ="Yes";
					listPunchedItmeDtl.push(singleObj);

				}	
				
			});
 
			
			//funUnTickModifier();
		}

	
	function funCheckForOldModifier(itemOldModCode)
    {
		var flagOldMod=false;
		$.each(listPunchedItmeDtl, function(i, objOld) {
			if (itemOldModCode == objOld.itemCode) {

				flagOldMod = true;

			}
		})
		return flagOldMod;
    }	
	
	function updateColor(el) {
		el.parentNode.style.color = el.checked ? "#a93444" : ""
	};

 	function funIncreaseQty() {
		var quantity = parseInt($("#txtAddQuantity").val());
		var oldQty = quantity + 1;
		$("#txtAddQuantity").val(oldQty);
		///dblItemRate
		var Amt=$("#idPrice").text();
		var TotalAmt=parseFloat(Amt) + parseFloat(dblItemRate);
		$("#idPrice").text(TotalAmt);

		

	}
 
 function funDecreaseQty() {
		if (parseInt($("#txtAddQuantity").val()) == 0) {
			funDisplayCart();
            
		}
		else
		{
			 var Newqty = parseInt($("#txtAddQuantity").val()) - 1;
			 $("#txtAddQuantity").val(Newqty);
			 var Amt=$("#idPrice").text();
			 var TotalAmt=parseFloat(Amt) - parseFloat(dblItemRate);
			 $("#idPrice").text(TotalAmt);

						
		}	
		
	}

	$(document).on('change',
				   '[type=checkbox]',
				    function() {
						var checkbox = $(this).is(':checked');
						var index = this.parentNode.parentNode.rowIndex;
						if (checkbox) {
							
							var  Rate1=document.getElementById("dblModRate."+index).value;
				
	
							var Amt=$("#idPrice").text();
							var TotalAmt=parseFloat(Amt) + parseFloat(Rate1.split(" ")[1]);
							$("#idPrice").text(TotalAmt);
	
							
							var modifierOld = document.getElementById("ModCheckBox."+ index).value;
							
							var modOldDetails = modifierOld.split("!");
			                var itemOldMod=modOldDetails[0] + "!" + modOldDetails[1];
											
							$.each(listPunchedItmeDtl, function(i, objOld1) {
							
								if (itemOldMod == objOld1.itemCode) {
	
									objOld1.added="Yes"
								}
	
							})
						
							
						}
						else 
						{
							
							var  Rate1=document.getElementById("dblModRate."+index).value;
							
							var Amt=$("#idPrice").text();
							var TotalAmt=parseFloat(Amt) - parseFloat(Rate1.split(" ")[1]);
							$("#idPrice").text(TotalAmt);

							var modifierOld = document.getElementById("ModCheckBox."+ index).value;							
							var modOldDetails = modifierOld.split("!");
			                var itemOldMod=modOldDetails[0] + "!" + modOldDetails[1];
							$.each(listPunchedItmeDtl, function(i, objOld1) {
								if (itemOldMod == objOld1.itemCode) {
									objOld1.added="No"
								}

							})
						
						}

					});
	
	
	function funGetSingleItemImage(itemCode,onClickIsImage)
	{

		if(onClickIsImage=='image' || onClickIsImage=='imageWithItemNameAndDeatils')
		{
			var searchUrl1 = getContextPath() + "/loadItemImage.html?itemCode="+itemCode;
			$.ajax({
				type : "GET",
				url : searchUrl1,
				cache : false
			});
			$("#singleItemImage").attr('src', searchUrl1);
	 
		}
		else
		{
			$('#singleItemImage').attr('src', getContextPath()+"/resources/images/stdItemImage.png");

		}	
	}
	
</script>
	
</head>
<body style="margin-left:-8px;">
<s:form name="singleItemPage" method="GET" action=""  style="width: 102%; height: 100%;">
		<div class="container" style="width:615px;">
		
			<div class="icondiv" style="height:85px;">
			
					<i class="mdi mdi-arrow-left" style="margin: 0px 40px;color: #fff;font-size:45px; font-weight:bolder;" onclick="funBackToDashboard()"></i>
 					
 					<a href="#about"><i class="mdi mdi-shopping" style="font-size: 40px; float: right;padding-right:42px;color: #fff;" onclick="funOpenSettlement()"></i></a>
			 		<input type="text"  class="numberCircle" id="itemCountInSinglePage" readonly="readonly" onclick="funOpenSettlement()" />
			
			</div>
			<div class="card">
			
            	<img src="../${pageContext.request.contextPath}/resources/images/stdItemImage.png" id="singleItemImage"  title="Item Image" style=\"width:500px; height:700px;\"  class="card-media"/>
            	
            	<div class="card-details">
            	<div class="row">
            	   <img src="../${pageContext.request.contextPath}/resources/images/icons8-veg.png" style="height:35px;margin-left:3%;float: left;" />
            	      <label class="card-head" id="idItemName"  style="display: inline-block;margin-left:3%;font-size:26px;font-weight:bold; "></label>
            	</div>
            	<div class="row">
            	<label  id="idDetails" style="color: #7b7272; margin-left: 5%; margin-bottom:5%; font-size: 20px;"></label>
            	            	</div>
            	
            	<div class="row">
              <small class="price">
               <label class="mdi mdi-currency-inr" id="idPrice" style="font-weight: 600;font-size: 20px;color: #000;font-family:'calibri';width: 120%;margin-left:30px;"></label>
            	</small>
            	            	</div>
            	
                <%-- 	<h2 class="card-head" id="idItemName" style="display: inline-block;">${command.strItemName}</h2>
                	 
                	<img class="veg-icon" src="../${pageContext.request.contextPath}/resources/images/icons8-veg.png" style="margin-left: 58%;width: 15px;">
                	<p style="margin: 0px;">${command.strBillNo}</p>
                	<small class="price"><span class="mdi mdi-currency-inr" id="idPrice"></span>${command.strSettelmentMode}<i class="mdi mdi-nest-protect"></i></small>
            	 --%>
            	 </div>
         	</div>
         		
      <table id="tblModifier" style="font-family:'calibri';">
	  </table>
    			
    		<div class="row">
         	
         	<div style="text-align: center;"><button class="btn" type="button" id="btnAddToCart" onclick="funDisplayqty()" style="display: block;width: 168%;height: 67%;font-size: 31px;"><a href="#" >Add To Cart</a></button></div>
         	
         	<div class="number" id="qtydiscplay" >
	 							 <span class="minus"  onclick="funDecreaseQty()"style="
								    font-size: 98px;
								    color: black;
								    margin-top: -83%;">-</span>
								<input type="text" id="txtAddQuantity" readonly="readonly"  style="
								    font-size: 48px;
								    margin-top: 6%;
								    font-weight: 800;
								    color: black; width:37%;height:56%;text-align: left" value="0"/>
	 							 <span class="plus"  onclick="funIncreaseQty()"
	 							 style="font-size: 70px;
									    margin-top: -40%;
									    color: black;
									    margin-left: 16%;float:right">+</span>
								</div>
							
			         	<div style="text-align: center;"><button class="btn" type="button" id="btnAddItem" onclick="funMenuItemClicked()" style="display: block;width: 69%;height: 67%;font-size: 31px;"><a href="#" >Add Item</a></button></div>
			
         	</div>
         	
		</div>  


</s:form>

</body>
</html>