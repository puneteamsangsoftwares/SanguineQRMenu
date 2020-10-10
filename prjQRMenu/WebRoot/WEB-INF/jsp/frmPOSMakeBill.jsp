<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width">
	<meta name="viewport" content="initial-scale=1.0">
	<meta name="viewport" content="width=590">
	
	
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/css/materialdesignicons.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/fonts/materialdesignicons-webfont.ttf">
	<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@100;300;400;500;700&display=swap" rel="stylesheet">
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/style.css"/>" />
 	<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/jquery-confirm.min.css"/>"/>
 	<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/jquery-confirm.min.css"/>"/>
 	
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/mdb.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/print.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/easy-numpad.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery-confirm.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/confirm-prompt.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>	
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js.js"/>"></script>
	

	
	<title>Main Form</title>
<style type="text/css">
	.taketable{text-align: center;}
	.taketable h3{margin-top: 18px;}
	.boxes{background:#c7c7c74b; height: 63px;}
	.boxes:hover{background: #827a7a; color: #fff;}
	.boxes p{padding-top: 20px;margin: 0px;}
	table td{padding: 2px 3px;width: 34px;}
</style>


<style type="text/css">



.btlDineIn
{
	width: 100px;
    height: 50px;
    white-space: normal;
    color: red;
    background-color: lightblue;
}

.btlHomeDelivery
{
	width: 100px;
    height: 50px;
    white-space: normal;
    color: red;
    background-color: lightblue;
}

.btlTakeAway
{
	width: 100px;
    height: 50px;
    white-space: normal;
    color: red;
    background-color: lightblue;
}

#divMenuHeadDtl::-webkit-scrollbar-track
{
    -webkit-box-shadow: inset 0 0 3px rgba(0,0,0,0.6);
    background-color: #CCCCCC;
}

#divMenuHeadDtl::-webkit-scrollbar
{
    width: 05px;
    background-color: #
}

#divMenuHeadDtl::-webkit-scrollbar-thumb
{
    background-color: #FFF;
    background-image: -webkit-linear-gradient(90deg,
                                              rgba(0, 0, 0, 1) 0%,
                                              rgba(0, 0, 0, 1) 25%,
                                              transparent 100%,
                                              rgba(0, 0, 0, 1) 75%,
                                              transparent)
}



#divItemDtl::-webkit-scrollbar-track
{
    -webkit-box-shadow: inset 0 0 3px rgba(0,0,0,0.6);
    background-color: #CCCCCC;
}

#divItemDtl::-webkit-scrollbar
{
    width: 05px;
    background-color: #F5F5F5;
}

#divItemDtl::-webkit-scrollbar-thumb
{
    background-color: #FFF;
    background-image: -webkit-linear-gradient(90deg,
                                              rgba(0, 0, 0, 1) 0%,
                                              rgba(0, 0, 0, 1) 25%,
                                              transparent 100%,
                                              rgba(0, 0, 0, 1) 75%,
                                              transparent)
}



input#takeaway.btn .btn-primary:hover{
	background-color:#fff;
}

</style>

<script type="text/javascript">

var objMenuItemButton;
var objIndex;
var NumPadDialogFor,itemCode;	
var itemChangeQtySelected, itemPrice;
var gPOSCode="${gPOSCode}";
var gClientCode="${gClientCode}";
var tblMenuItemDtl_MAX_COL_SIZE=4;

$(document).ready(function() 
		{
	//funShowTables();
	var title='Single Item Page';
    document.title=title;
	
			});

 
 
 
 
 
  function funTableNoClicked(obj)
 {
	 $("#hidOperationType").val('Dine In');
	 gTableNo=obj.id;
	 funMakeBillBtnClicked();
 }
 

 
function funShowTables()
{
	   document.getElementById("tab1").style.display='block';

		document.getElementById("tab2").style.display='none';

	var $rows = $('#tblMasterTableDtl').empty();
	
	var tblMenuItemDtl=document.getElementById('tblMasterTableDtl');
	var style=" width: 100px;height: 100px;margin:10px;background-color:#d8d2d2;border: 1px solid #deb8b8;color:#000;  ";
	
	var cssClass="btn btn-primary";
	
				
	var insertCol=0;
	/* var col=insertTR.insertCell(insertCol);
	col.innerHTML = "<td><input type=\"button\" id='takeaway' value='Take Away'  style=\" width: 100px;height: 100px;margin:20px; border: solid 2px #07cac1; background:#fff; color:#000;\"   onclick=\"funTakeAwayClicked(this)\" class='"+cssClass+"' /></td>";
	col.style.padding = "1px";
	 */
	 //insertTR=tblMenuItemDtl.insertRow();
		var insertTR=tblMenuItemDtl.insertRow();

	var jsonArrForTableDtl=${command.jsonArrForTableDtl};	
	var areaCode="All";
	
	
	/**
	*Free Table
	*Busy/Occupied Table border: 5px solid #a94442;
	*Billed Table border: 5px solid #2ba5cc;
	*/
	var searchurl=getContextPath()+"/funLoadTablesForMakeKOT.html?clientCode="+gClientCode+"&posCode="+gPOSCode+"&areaCode="+areaCode;

	var delCharges=0;  
	$.ajax({
		 type: "GET",
		        url: searchurl,
		        dataType: "json",
		        async: false,
		        success: function(response)
		        {
		        	$.each(response, function(i, obj) 
		        	{
		        		//removed border-radius: 40px;
		        		
		        		
		    			if(insertCol<tblMenuItemDtl_MAX_COL_SIZE)
		    			{
		    				var col=insertTR.insertCell(insertCol);
		    				col.innerHTML = "<td><input type=\"button\" id="+obj.strTableNo+" value='"+obj.strTableName+"'    style='"+style+"'  onclick=\"funTableNoClicked(this)\" class='"+cssClass+"' /></td>";
		    				col.style.padding = "1px";

		    				insertCol++;
		    		   }
		    			 else
		    			{					
		    				insertTR=tblMenuItemDtl.insertRow();									
		    				insertCol=0;
		    				var col=insertTR.insertCell(insertCol);
		    				col.innerHTML = "<td><input type=\"button\" id="+obj.strTableNo+" value='"+obj.strTableName+"'   style='"+style+"'   onclick=\"funTableNoClicked(this)\" class='"+cssClass+"' /></td>";
		    				col.style.padding = "1px";
		    				
		    				
		    				
		    				
		    				insertCol++;
		    			}
     	        	});
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
function funMakeBillBtnClicked()
{

	var $rows = $('#tblSettleItemTable').empty();
	document.getElementById("tab2").style.display='block';
	   document.getElementById("tab3").style.display='none';

    document.getElementById("tab1").style.display='none';
    
    operationType="DineIn";
    transactionType="Make Bill";
    
    finalSubTotal=0.00;
	finalDiscountAmt=0.00;
	finalNetTotal=0.00;
	taxTotal=0.00;
	taxAmt=0.00;
	finalGrandTotal=0.00;
	

    var listItmeDtl=[];
    
	$("#hidTableNo").val(gTableNo);

    
    var searchurl=getContextPath()+"/funGetItemsForTable.html?bussyTableNo="+gTableNo;
	 $.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        async:false,
		        success: function(response)
		        {
		        	$.each(response.itemList, function(i,item)
    				{			
		        		
		        		var itemName=item.strItemName;									
						var itemQty=item.dblQuantity;									
						var itemAmt=item.dblAmount;
						var rate=item.dblRate;
						var itemCode=item.strItemCode;
		        		
    		    		
		        		var isModifier=false;
		  		    	if(itemName.startsWith("-->"))
		  				{
		  		    		isModifier=true;
		  				}
		  		    	
		  		    	
		  		    	var subgroupcode=item.strSubGroupCode;
				 		var subgroupName=item.strSubGroupName;
		  		    	var groupcode=item.strGroupCode;
				 		var groupName=item.strGroupName;
									 							
						
		  		    	
						
						var singleObj = {}

					    singleObj['itemCode'] =itemCode;
						singleObj['itemName'] =itemName;				
						singleObj['quantity'] =itemQty;																	    								   
					    singleObj['rate'] =rate;				   								    
					    singleObj['amount'] = itemAmt;								    
					    singleObj['isModifier'] =isModifier;								    
					    singleObj['discountPer'] = 0.0;
					    singleObj['discountAmt'] =0.0;
					    singleObj['strSubGroupCode'] =subgroupcode;
					    singleObj['strGroupcode'] =groupcode;
					    singleObj['dblCompQty'] ='0';

					   
					    
					    listItmeDtl.push(singleObj);
		  		    	
		  		    	
		  		    	
		  		    	
    		    		
    			  	});					        	  
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
    
	 /**
		*calculating promotions and filling data to grid for bill print	
		*/
	//	ppp();
//	 pp();
//alert('funNoPromtionCalculation');


				$("#btnConfirmOrder").attr('value','Generate Bill');

		     	funNoPromtionCalculationForMakeBill(listItmeDtl)
		     	//funCalculatePromotion
		//	    abc();
				funRefreshSettlementItemGridForMakeBill();
		}
			
			function funNoPromtionCalculationForMakeBill(listItmeDtl)
			{
				listBillItem=[];
				//$('#tblmodalDataTable tbody').empty();
				$.each(listItmeDtl,function(i,item)
				{
					if(item.quantity >0 || item.amount >0)
					{
						funFillSettleTableForMakeBill(item.itemName,item.quantity,item.amount,item.discountPer,item.discountAmt,item.strGroupcode,item.strSubGroupCode,item.itemCode,item.rate,item.dblCompQty);
			
					}
					//funFillModalTable(item.itemName,item.quantity,item.amount,item.dblCompQty,item.itemCode,item.rate);
				});
			
			}
			
		function funFillSettleTableForMakeBill(strItemName,dblQuantity,dblAmount,dblDiscountPer1,dblDiscountAmt1,strGroupCode,strSubGroupCode,strItemCode,dblRate,dblCompQty)
		{
			var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
			var rowCount = tblSettleItemDtl.rows.length;
			var insertRow = tblSettleItemDtl.insertRow(rowCount);
					     	
		    var col1=insertRow.insertCell(0);
		    //col1.style.width ='10';
		    var col2=insertRow.insertCell(1);
		    var col3=insertRow.insertCell(2);
		    var col4=insertRow.insertCell(3);
		    var col5=insertRow.insertCell(4);
		    var col6=insertRow.insertCell(5);
		    var col7=insertRow.insertCell(6);
		    var col8=insertRow.insertCell(7);
		    var col9=insertRow.insertCell(8);
		    var col10=insertRow.insertCell(9);
		
		    /* col1.style.backgroundColor="lavenderblush";
		    col2.style.backgroundColor="lavenderblush";
		    col3.style.backgroundColor="lavenderblush"; */
		    
		    
		    
		    col1.innerHTML = "<input readonly=\"readonly\" size=\"33px\"  class=\"itemName\" style=\"text-align: left; color:black; height:30px;width:275px;border:none; padding-left:5px;font-size:14px;font-family:'trebuchet ms';\"   name=\"listOfBillItemDtl["+(rowCount)+"].itemName\" id=\"strItemName."+(rowCount)+"\" value='"+strItemName+"' />";
		    if(dblAmount == 0 && (strItemName.startsWith('-->')))
		    {
		    	col2.innerHTML = "<input  type=\"hidden\" readonly=\"readonly\" size=\"0px\"   class=\"itemQty\" style=\"text-align: right; color:black; height:30px;width:50px;border:none;font-family:'trebuchet ms';\"  name=\"listOfBillItemDtl["+(rowCount)+"].quantity\" id=\"dblQuantity."+(rowCount)+"\" value='"+dblQuantity+"' />";
		        col3.innerHTML = "<input   type=\"hidden\" readonly=\"readonly\" size=\"0px\"   class=\"itemAmt\" style=\"text-align: right; color:black; height:30px;width:70px;border:none;padding-right:20px;font-family:'trebuchet ms';\"  name=\"listOfBillItemDtl["+(rowCount)+"].amount\" id=\"dblAmount."+(rowCount)+"\" value='"+dblAmount+"'/>";
		        
		    }	
		    else
		    {
		    	col2.innerHTML = "<input readonly=\"readonly\" size=\"6px\"   class=\"itemQty\" style=\"text-align: right; color:black; height:30px;width:50px;border:none;font-family:'trebuchet ms';\"  name=\"listOfBillItemDtl["+(rowCount)+"].quantity\" id=\"dblQuantity."+(rowCount)+"\" value='"+dblQuantity+"' />";
		        col3.innerHTML = "<input readonly=\"readonly\" size=\"20px\"   class=\"itemAmt\" style=\"text-align: right; color:black; height:30px;width:70px;border:none;padding-right:20px;font-family:'trebuchet ms';\"  name=\"listOfBillItemDtl["+(rowCount)+"].amount\" id=\"dblAmount."+(rowCount)+"\" value='"+dblAmount+"'/>";
		        	
		    }	
		    col4.innerHTML = "<input type=\"hidden\" size=\"0px\" class=\"discountPer\"      name=\"listOfBillItemDtl["+(rowCount)+"].discountPer\" id=\"tblDiscountPer."+(rowCount)+"\" value='"+dblDiscountPer1+"' />";
		    col5.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"discountAmt\"    name=\"listOfBillItemDtl["+(rowCount)+"].discountAmt\" id=\"tblDiscountAmt."+(rowCount-1)+"\" value='"+dblDiscountAmt1+"' />"; 
		    col6.innerHTML = "<input type=\"hidden\"  size=\"0px\"   class=\"groupcode\"    name=\"listOfBillItemDtl["+(rowCount)+"].strGroupcode\" id=\"strGroupcode."+(rowCount)+"\" value='"+strGroupCode+"' />";	    
		    col7.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"subGroupCode\"  name=\"listOfBillItemDtl["+(rowCount)+"].strSubGroupCode\" id=\"strSubGroupCode."+(rowCount)+"\" value='"+strSubGroupCode+"' />";
		   	col8.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"itemCode\"  name=\"listOfBillItemDtl["+(rowCount)+"].itemCode\" id=\"itemCode."+(rowCount)+"\" value='"+strItemCode+"' />";
		    col9.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"rate\"  name=\"listOfBillItemDtl["+(rowCount)+"].rate\" id=\"rate."+(rowCount)+"\" value='"+dblRate+"' />";
		    col10.innerHTML = "<input type=\"hidden\" size=\"0px\"    name=\"listOfBillItemDtl["+(rowCount)+"].dblCompQty\" id=\"dblCompQuantity."+(rowCount)+"\"  value='"+dblCompQty+"' />";
		
		  //For Calculaing Discount Fill the list with item Dtl
		    var singleObj = {}
		    singleObj['itemName'] =strItemName;
		    singleObj['quantity'] =dblQuantity;
		    singleObj['amount'] = dblAmount;
		    singleObj['discountPer'] = dblDiscountPer1;
		    singleObj['discountAmt'] =dblDiscountAmt1;
		    singleObj['strSubGroupCode'] =strSubGroupCode;
		    singleObj['strGroupcode'] =strGroupCode;
		    singleObj['itemCode'] =strItemCode;
		    singleObj['rate'] =dblRate;
		    singleObj['dblCompQty'] =dblCompQty;
		
		    listBillItem.push(singleObj);
		    
		    finalSubTotal=finalSubTotal+parseFloat(dblAmount);
			//finalDiscountAmt=finalDiscountAmt+parseFloat(dblDiscountAmt1);//(itemDiscAmt);
		// 			  })	
		}

		function funRefreshSettlementItemGridForMakeBill()
		{
			
		
		
		
		    finalNetTotal=finalSubTotal;
		    finalGrandTotal=finalNetTotal;
		    funFillTableFooterDtlForMakeBill("","","");  
		    funFillTableFooterDtlForMakeBill(" SUB TOTAL",finalSubTotal,"bold");
		   // funFillTableFooterDtl(" Discount",finalDiscountAmt,"");
		   // funFillTableFooterDtl(" NetTotal",finalNetTotal,"");
		    
			var taxTotal= funCalculateTaxForItemTblForMakeBill();
		
		    finalGrandTotal=taxTotal+finalNetTotal;
		    finalGrandTotal=funCalculateRoundOffAmtForMakeBill(finalGrandTotal);
		    funFillTableFooterDtlForMakeBill("","","");  
		
		    funFillTableFooterDtlForMakeBill(" GRAND TOTAL",finalGrandTotal,"bold");
		 	$('#hidSubTotal').val(finalSubTotal);
		 	$('#hidNetTotal').val(finalNetTotal);
		 	$('#hidGrandTotal').val(finalGrandTotal);
		 	
		 	$("#hidTakeAway").val(gTakeAway);
			//$("#hidCustomerCode").val(gCustomerCode);
			//$("#hidCustomerName").val(gCustomerName);
			
			$("#hidOperationType").val(operationType);
			$("#hidTransactionType").val(transactionType);
			
			/* $("#hidAreaCode").val(gAreaCode);
			
			$("#hidTableNo").val(gTableNo);
			$("#hidWaiterNo").val(gWaiterNo);
			 */
		}
		
			function funCalculateTaxForItemTblForMakeBill()
			{
					
					
			
				var taxTotal=0;
				 var rowCountTax=0;
				var searchurl=getContextPath()+"/funCalculateTaxInSettlement.html?operationTypeForTax="+operationType;
				$.ajax({
					 type: "POST",
				        url: searchurl,
				        data : JSON.stringify(listBillItem),
				        contentType: 'application/json',
				        async: false,
			        success: function (response)
			        {
			        	    	$.each(response,function(i,item)
			        	    	{
					        		taxTotal=taxTotal+response[i].taxAmount;
					        		
					        		funFillTableTaxDetialsForMakeBill(response[i].taxName,response[i].taxAmount,response[i].taxCode,response[i].taxCalculationType,response[i].taxableAmount ,rowCountTax);
					        		
					        		rowCountTax++;
					        	});
			         
			        	    	finalGrandTotal=finalGrandTotal+taxTotal;
			        	    	 $('#hidTaxTotal').val(taxTotal);
			        },
			        error: function(jqXHR, exception)
			        {
			            if (jqXHR.status === 0) 
			            {
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
				return taxTotal;
			}
			
		function funFillTableTaxDetialsForMakeBill(taxName,taxAmount,taxCode,taxCalculationType,taxableAmount,rowCountTax)
		{
					var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
					var rowCount = tblSettleItemDtl.rows.length;
					var insertRow = tblSettleItemDtl.insertRow(rowCount);
					
					var col1=insertRow.insertCell(0);
					var col2=insertRow.insertCell(1);
					var col3=insertRow.insertCell(2);
						var col4=insertRow.insertCell(3);
					var col5=insertRow.insertCell(4);
					var col6=insertRow.insertCell(5);
					var col7=insertRow.insertCell(6);
					var col8=insertRow.insertCell(7);
					
					/* col1.style.backgroundColor="white";
					col2.style.backgroundColor="#F5F5F5";
					col3.style.backgroundColor="#F5F5F5"; */
					
					col1.innerHTML = "<input readonly=\"readonly\" size=\"30px\"  name=\"  listTaxDtlOnBill["+(rowCountTax)+"].taxName\" id=\"taxName."+(rowCountTax)+"\" style=\"text-align: left; color:black; width:275px;height:30px;border:none;padding-left: 4px; font-family:'trebuchet ms';\"  value='"+taxName+"' />";
					col2.innerHTML = "<input readonly=\"readonly\" size=\"6px\"  style=\"text-align: right; color:black; height:30px; border:none;\"   />";
					col3.innerHTML = "<input readonly=\"readonly\" size=\"20px\"  name=\"  listTaxDtlOnBill["+(rowCountTax)+"].taxAmount\" id=\"taxAmount."+(rowCountTax)+"\"  style=\"text-align: right; color:black; width:70px; height:30px;border:none;padding-right:20px;font-family:'trebuchet ms';\"  value='"+taxAmount+"'  />";
					/* col4.innerHTML = "<input readonly=\"readonly\" size=\"1px\"   style=\"text-align: right; color:blue; height:20px;\"  />";
					col5.innerHTML = "<input readonly=\"readonly\" size=\"1px\"   style=\"text-align: right; color:blue; height:20px;\"  />"; */
					col6.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"taxCode\"  name=\"listTaxDtlOnBill["+(rowCountTax)+"].taxCode\" id=\"taxCode."+(rowCountTax)+"\" value='"+taxCode+"' />";
					col7.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"taxCalculationType\"  name=\"listTaxDtlOnBill["+(rowCountTax)+"].taxCalculationType\" id=\"taxCalculationType."+(rowCountTax)+"\" value='"+taxCalculationType+"' />";
					col8.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"taxableAmount\"  name=\"listTaxDtlOnBill["+(rowCountTax)+"].taxableAmount\" id=\"taxableAmount."+(rowCountTax)+"\" value='"+taxableAmount+"' />";
		
		}
		


		function funFillTableFooterDtlForMakeBill(column1,column2,font){
		
				
				var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
				var rowCount = tblSettleItemDtl.rows.length;
				var insertRow = tblSettleItemDtl.insertRow(rowCount);
				totalItemRow=rowCount;
				var col1=insertRow.insertCell(0);
				var col2=insertRow.insertCell(1);
				var col3=insertRow.insertCell(2);
				var col4=insertRow.insertCell(3);
				var col5=insertRow.insertCell(4);
				
					var styleLeft="style=\"text-align: left; color:black; height:30px; border:none;widht:240px;font-family:'trebuchet ms';\""; 
				var styleRight="style=\"text-align: right; color:black; height:30px; padding-right:20px;border:none;width: 82px;font-family:'trebuchet ms';\"";
				if(column1=="" && column2=="")
				{
					styleLeft="style=\"text-align: left; color:blue; height:30px;width:28%;  border:none;font-family:'trebuchet ms'; \" "; 
				    styleRight="style=\"text-align: right; color:blue; height:30px;border:none;width: 82px;font-family:'trebuchet ms';\" ";
				}else if(font.includes("bold")){
					styleLeft="style=\"text-align: left; color:green; height:30px; width:28%; border:none;font-weight: bold;font-size:15px;font-family:'trebuchet ms'; \" ";
					styleRight="style=\"text-align: right; color:green; height:30px; padding-right:20px;border:none;width: 82px;font-weight: bold;font-size:20px; font-family:'trebuchet ms';\"";
				}
				
				
				col1.innerHTML = "<input readonly=\"readonly\" size=\"30px\"  "+styleLeft+" id=\"column1."+(rowCount)+"\" value='"+column1+"'  />";
				col2.innerHTML = "<input readonly=\"readonly\" size=\"6px\" "+styleLeft+"  />";
				col3.innerHTML = "<input readonly=\"readonly\" size=\"20px\"   "+styleRight+" id=\"column2."+(rowCount)+"\" value='"+column2+"' />";
				
		
		}
		function funCalculateRoundOffAmtForMakeBill(settlementAmt)
	    {

		var roundOffTo = "${roundoff}";

		if (roundOffTo == 0.00)
		{
		    roundOffTo = 1.00;
		}

		var roundOffSettleAmt = settlementAmt;
		var remainderAmt = (settlementAmt % roundOffTo);
		var roundOffToBy2 = roundOffTo / 2;
		var x = 0.00;

		if (remainderAmt <= roundOffToBy2)
		{
		    x = (-1) * remainderAmt;

		    roundOffSettleAmt = (Math.floor(settlementAmt / roundOffTo) * roundOffTo);

		    //System.out.println(settleAmt + " " + roundOffSettleAmt + " " + x);
		}
		else
		{
		    x = roundOffTo - remainderAmt;

		    roundOffSettleAmt = (Math.ceil(settlementAmt / roundOffTo) * roundOffTo);

		    // System.out.println(settleAmt + " " + roundOffSettleAmt + " " + x);
		}

		return roundOffSettleAmt;

	    }

			

</script>
</head>

<body>


<s:form name="frmMakeBill" method="GET" action=""  style="width: 100%; height: 100%;">

  <div class="container-fulid">
		<div class="icondiv">
		<a href="frmModeOfOpearation.html"><i class="mdi mdi-arrow-left" style="padding-left: 13px;color: #fff;font-size: 20px;"></i></a></div>

			<div class="taketable">
				<h3>Select Your Table NO</h3>

				<table  id="tblMenuItemDtl" style="margin:0px;">
									
									
									<c:set var="sizeOfTables" value="${fn:length(command.jsonArrForTableDtl)}"></c:set>									   
									<c:set var="itemCounter" value="${0}"></c:set>							
									     
										 <c:forEach var="objTableDtl" items="${command.jsonArrForTableDtl}"  varStatus="varTable">																																		
											<tr>
												<%
												for(int x=0; x<4; x++)
													{
												%>														
														<c:if test="${itemCounter lt sizeOfTables}">																																		
															<td >
															<div class="boxes">
 														
															<label id="${command.jsonArrForTableDtl[itemCounter].strTableNo}"  style="width: 100%;height: 100%; " onclick="funTableNoClicked(this)"  >${command.jsonArrForTableDtl[itemCounter].strTableName}</label>
															
 															
 															
                              <%--         <input type="button"  id="${command.jsonArrForTableDtl[itemCounter].strTableNo}"  value="${command.jsonArrForTableDtl[itemCounter].strTableName}"  onclick="funTableNoClicked(this)"  />																																																															
  --%>
                                                          </div>
															</td>																																																	
														<c:set var="itemCounter" value="${itemCounter +1}"></c:set>
														</c:if>													
												<% 
												}
												%>
											   </tr>																																
										</c:forEach>
					
									
									
									
				</table>
			</div>
		</div>
 
  <s:hidden id="hidOperationType" path="operationType"/>
	       <s:hidden id="hidTableNo" path="strTableNo"/>
				

</s:form>

</body>

</html>