<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Bill Settlement</title>

	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width">
	<meta name="viewport" content="initial-scale=1.0">
	<meta name="viewport" content="width=590">
	
	
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.grid.min.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.min.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/style.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/materialdesignicons.min.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/mdb.min.css"/>" />
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/css/		materialdesignicons.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/fonts/materialdesignicons-webfont.ttf">
	<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@100;300;400;500;700&display=swap" rel="stylesheet">
	
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/mdb.min.js"/>"></script>

	<script type="text/javascript" src="<spring:url value="/resources/js/print.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/easy-numpad.js"/>"></script>
	 <link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/jquery-confirm.min.css"/>"/>
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery-confirm.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/confirm-prompt.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>
	
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js.js"/>"></script>
	<title>Dashboard</title>

</head>
<script type="text/javascript">
var objMenuItemButton;
var objIndex;
var NumPadDialogFor,itemCode;	
var itemChangeQtySelected, itemPrice;

$(document).ready(function() 
		{
	//funShowMenuHead();
	
	    	<%-- var tableNo='<%=session.getAttribute("tableNo").toString()%>';
	    	<%session.removeAttribute("tableNo");%> --%>
	    	//alert("make bill");
	    	funBackToDashboard();
	    	 var opType1="${operationMode}";
	    	if(opType1 =="DineIn And Make Bill")
    		{
	    		gTableNo=getTableNo();
	    		if(gTableNo==null)
	    	    {
	    			 $('#idYourOrderHeader').text("");
	    			 $('#idHeaderWithCompanyName').text("");

	    			$('#idHeaderCompanyName').text("");
	    			var $rows = $('#tblSettleItemTable').empty();
	        		var $rows = $('#tblSettleItemTableHead').empty();
	        		var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
	        		var rowCount = tblSettleItemDtl.rows.length;
	        		var insertRow = tblSettleItemDtl.insertRow(rowCount);
	        				     	
	        	    var col1=insertRow.insertCell(0);

	        	    //col1.style.width ='10';
	        	   
	        	    /* col1.style.backgroundColor="lavenderblush";
	        	    col2.style.backgroundColor="lavenderblush";
	        	    col3.style
	        	    .backgroundColor="lavenderblush"; */
	        	    
	        	    
	        	    
	        	    
	        	    col1.innerHTML = "<input readonly=\"readonly\" size=\"33px\"   style=\"text-align:center;font-size:28px;color:#a31717; margin-top:9%;margin-left:4%;width:90%;\"  value='Please Select Your Table No For Order' />";
	        	 	   document.getElementById("idBtnProceed").style.display='none';

	    	    }
	    		else
	    		{
			    	funMakeBillBtnClicked(gTableNo);

	    		}	
		   
    		}
	        
		});


function getTablNoFromCookie(name) {  
    var dc = document.cookie;  
    var prefix = name +"=";  
    var begin = dc.indexOf("; " + prefix);  
    if (begin == -1) {  
        begin = dc.indexOf(prefix);  
        if (begin != 0)return null;  
    } else {  
        begin += 2;  
    }  
    var end = document.cookie.indexOf(";", begin);  
    if (end == -1) {  
        end = dc.length;  
    }  
    return unescape(dc.substring(begin + prefix.length, end));  
}  
  
function getTableNo() {  
    var strVal = getTablNoFromCookie("SetTableNo");  
    return strVal;
}  

/* function funGenerateBill(listItmeDtl)
{
	//aa();
	alert('abc');
	funNoPromtionCalculation(listItmeDtl);
 	//funCalculatePromotion
//    abc();
	funRefreshSettlementItemGrid();
}
 */
 function funBackButtonClicked()
{
	  finalSubTotal=0;
	  listBillItem=[];
	  document.getElementById("tab2").style.display='none';		
	  document.getElementById("tab1").style.display='block';  
}
function funClickedConfirmOrder()
{
	var btnConfirm=$("#btnConfirmOrder").val();

	if(btnConfirm == 'Generate Bill')
	{
		document.frmBillSettlement.action = "actionBillSettlementKOT.html";
		document.frmBillSettlement.method = "POST";
	    document.frmBillSettlement.submit();

	}
	else
	{
		funConfirmOrder();
	}	
	
   }
    function funConfirmOrder()
    {
       var opType="${operationMode}";

   	   if(opType == 'Take Away' || opType == 'Home Delivery')
   	   {
   		   funClickedSettleBtnForDirectBiller();
   	   }
   	   else if(opType =="Dine In")
   	   {
   		   funDoneBtnKOT();
   	   }
   	   else if(opType =="DineIn And Make Bill")
       {
   		    $("#hidTableNo").val(gTableNo);
	   		document.frmBillSettlement.action ="actionBillSettlementKOT.html?tableNo="+gTableNo; //"actionBillSettlementKOT.html";
			document.frmBillSettlement.method = "POST";
		    document.frmBillSettlement.submit();
       }	   

    }
function funClickedSettleBtnForDirectBiller()
{


  	 document.frmBillSettlement.action = "actionBillSettlement.html";
  	 document.frmBillSettlement.method = "POST";
	 document.frmBillSettlement.submit();

}
function funDoneBtnKOT()
{			 
	 var kotNo=funGenerateKOTNo();
	 gTableNo=getTableNo();
	 var tableNo=gTableNo;
	 	1
		var dblTaxAmt =0;
	var searchurl=getContextPath()+"/saveKOT.html?kotNo="+kotNo+"&tableNo="+tableNo;
	$.ajax({
		 type: "POST",
	        url: searchurl,
	        data : JSON.stringify(listPunchedItmeDtl),
	        contentType: 'application/json',
	        async: false,
        success: function (response)
        {
        	
        	if(response=="true")
        	{
	            
        		
        		$("#txtAmount").val('');
        		$("#txtQty").val('');
        		listPunchedItmeDtl=[];
        		var $rows = $('#tblSettleItemTable').empty();
        		var $rows = $('#tblSettleItemTableHead').empty();
        		var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
        		var rowCount = tblSettleItemDtl.rows.length;
        		var insertRow = tblSettleItemDtl.insertRow(rowCount);
        				     	
        	    var col1=insertRow.insertCell(0);

        	    //col1.style.width ='10';
        	   
        	    /* col1.style.backgroundColor="lavenderblush";
        	    col2.style.backgroundColor="lavenderblush";
        	    col3.style
        	    .backgroundColor="lavenderblush"; */
        	    
        	    
        	    
        	    
        	    col1.innerHTML = "<input readonly=\"readonly\" size=\"33px\"  class=\"itemName\" style=\"text-align:center;font-size:28px;color:#a31717;\"  value='Your Order is Confirmed' />";
        	    rowCount++;
        	    insertRow = tblSettleItemDtl.insertRow(rowCount);
        	    
        	    var col2=insertRow.insertCell(0);

        	    
        	    col2.innerHTML = "<i class=\"mdi mdi mdi-checkbox-marked-circle menu-link\" aria-hidden=\"true\" style=\"font-size:50px ;text-align:center;color:#a31717; margin-top:30%;margin-left:45%;\"   /></i>"
            	  
        	    	rowCount++;
        	    insertRow = tblSettleItemDtl.insertRow(rowCount);
        	    
            	 var col3=insertRow.insertCell(0);

            	 col3.innerHTML = "<input readonly=\"readonly\" size=\"33px\"  class=\"itemName\" style=\"text-align:center;font-size:22px;color:#a31717;\"  value='Your Order No is "+kotNo.split("T")[1]+" ' />";

            	 $("#itemCountInSinglePage").val("");
            		$("#itemCountInMainPage").val("");
            		document.getElementById("idBtnProceed").style.display='none';
         	    //<i class="mdi mdi mdi-checkbox-marked-circle menu-link" style="font-size:50px "></i>
        	    /* 
        	    <h1 class="title" style="text-align:center;color:#a31717;">Your Orde
        	    r is Confirmed</h1>
				 <div style="text-align:center;"><i class="mdi mdi mdi-checkbox-marked-circle menu-link" style="font-size:50px "></i></div>
			 */	
        	}
        		      
        },
        error: function(jqXHR, exception)
        {
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
function funGenerateKOTNo()
{		
	var kot="";
	var searchurl=getContextPath()+"/funGenerateKOTNo.html";
	 $.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        async:false,
		        success: function(response)
		        {	
		        	kot=response.strKOTNo;
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
	 return kot;
}

</script>
<body>

<s:form name="frmBillSettlement" method="GET" action=""  style="width: 100%; height: 100%;">

<!--  
 <div class="row d-flex d-md-block flex-nowrap wrapper"> 
        <main class="container-fluid">
			 <div class="card card-inverse">
					 	
                       <div class="row" style="background-color: #fff;margin-bottom:  5px;display: -webkit-box;">
								<div class="" style="width: 23%;margin-left:15px;">
									<label class="title" style="font-weight: 600;font-size: 19px;color: #6f73ec;font-family:'calibri';">Your Order</label>
								</div>
								
								<div class=""  style="width: 10%;">
						             <input type="button" id="btnBack" value="Back" style="width: 80px; height:40px;margin-left:15px;" onclick="funBackButtonClicked()" class="btn btn-outline-info"></input>
								</div>
									 
								<div class=""  style="width: 10%;">
						             <input type="button" id="btnConfirmOrder" value="Confirm my Order" style="width: 150px; height:40px;margin-left:60px;" onclick="funClickedConfirmOrder()" class="btn btn-outline-info"></input>
								</div>
								
								
							</div>
			
					
                     <div id="divSettleItemDtl" style="border: 1px solid rgb(204, 204, 204);overflow: auto;width: 100%;display: block; margin-top:2px; margin-left:2px; font-family:calibri;">									
                    	
                        <table id="tblSettleItemTableHead" style="width: 100%; height:40px; border-collapse: collapse;overflow: auto;
								font-size: 10px;font-weight: bold;table-layout:fixed;"> 
						<tr style="margin-bottom:0px">
							  <th><input type="button" value="Description" style="width: 275px; height: 37px;text-align: left;padding-left: 8px;font-family:'calibri';" class="btn" ></input></th>
							  <th><input type="button" value="Qty" style="width: 48%;margin-left:74%; height: 37px;font-family:'calibri';" class="btn" ></input></th>
							  <th><input type="button" value="Amount" style="width: 58%; margin-left:11%;height: 37px;font-family:'calibri';" class="btn"></input></th>
						</tr>
					</table> 
					
						<table id="tblSettleItemTable" style=" height:auto;border-collapse: collapse;overflow: auto;
									font-size: 14px;">
						</table>
                        
                        
							</div>
				
 
 
 	
			 </div>
		</main>
					                 
 </div>
             
 -->            
               <div class="container-fluid">
			<div class="payment">
				<div class="icondiv" style="height:85px;">
				<a href="#frmMenuItem.html"><i class="mdi mdi-arrow-left" style="height:85px;margin:0px 40px;color: #fff;font-weight:bolder;
						font-size: 45px;"  onclick="funBackToDashboard()"></i></a>
				</div>
					<h5 style="font-size:40px;font-weight:700;margin-left:25px;margin-top:35px;" id="idYourOrderHeader">Your Order</h5>
					<p style="margin-left:27px;font-size:22px;font-weight:300;" id="idHeaderWithCompanyName">From ${gCompanyName}</p>
				<div style="overflow: auto;padding-left: 25px;border-top:1px solid gray;border-bottom:1px solid gray;">
					<table class="order" id="tblSettleItemTable" style="margin:30px 0px;">
						
						</table>
				</div>
				<%-- <div style="padding-left: 4px;">
					<table class="checkout">
						<tr>
							<td style="width: 84%">Item Total</td>
							<td style="width: 15%"><span class="mdi mdi-currency-inr"></span>450</td>
						</tr>
						<tr>
							<td style="width: 84%">Taxes</td>
							<td style="width: 15%"><span class="mdi mdi-currency-inr"></span>20</td>
						</tr>
						<tr>
							<td style="width: 84%;font-size: 15px;font-weight: 600;">TO Pay</td>
							<td style="width: 15%;font-size: 15px;font-weight: 600;"><span class="mdi mdi-currency-inr"></span>600</td>
						</tr>
					</table>
				 --%></div>
				<div style="text-align: center;"><button type="button"   id="idBtnProceed"   style="background-color:green;width:92%;height:55px;font-size:25px;font-weight:bold;" class="btn" onclick="funConfirmOrder()">Proceed</button></div>
			</div>
		</div>


               
                <s:hidden id="hidSubTotal" path="dblSubTotal"/>
		 		<s:hidden id="hidDiscountTotal" path="dblDicountTotal"/>
		 		<s:hidden id="hidNetTotal" path="dblNetTotal"/>
		 		<s:hidden id="hidGrandTotal" path="dblGrandTotal"/>
		 		<s:hidden id="hidRefund" path="dblRefund"/>
		 	    <s:hidden id="hidTaxTotal" path="dblTotalTaxAmt"/>
 		 		<s:hidden id="hidSettlemnetType" path="strSettlementType"/> 
 		 		<s:hidden id="hidBalanceAmt" path=""/> 
 		 		
 		 		
 		 		<s:hidden id="hidCustMobileNo" path="custMobileNo"/>
		 		<s:hidden id="hidCustomerCode" path="strCustomerCode"/>
		 		<s:hidden id="hidCustomerName" path="customerName"/>
		 		<s:hidden id="hidOperationType" path="operationType"/>
		 		<s:hidden id="hidTransactionType" path="transactionType"/>
		 		<s:hidden id="hidTakeAway" path="takeAway"/>
		 		<s:hidden id="hidDeliveryBoyCode" path="strDeliveryBoyCode"/>
		 		<s:hidden id="hidDeliveryBoyName" path="strDeliveryBoyName"/>
		 		<s:hidden id="hidTableNo" path="strTableNo"/>
				<s:hidden id="hidWaiterNo" path="strWaiter"/>
				<s:hidden id="hidAreaCode" path="strAreaCode"   />
				<s:hidden id="hidBillNo" path="strBillNo"   />
				<s:hidden id="hidIsSettleBill" path="isSettleBill"   />
				
				

</s:form>

</body>

</html>