<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
	
	
	<title>Main Form</title>

</head>

<script type="text/javascript">
var objMenuItemButton;
var objIndex;
var NumPadDialogFor,itemCode;	
var itemChangeQtySelected, itemPrice;
var gPOSCode="${gPOSCode}";
var gClientCode="${gClientCode}";

$(document).ready(function() 
		{
	var gCompanyName="";
	<%if (session.getAttribute("success") != null) {
		if(session.getAttribute("OrderNo") != null){%>
		OrderNO='<%=session.getAttribute("OrderNo").toString()%>';
			<%
			session.removeAttribute("OrderNo");
		}
		boolean test = ((Boolean) session.getAttribute("success")).booleanValue();
		session.removeAttribute("success");
		if (test) {
			%>	
			   $("#lblOrderNo").text("Your Order No "+OrderNO);
			   
			<%
		}
	}%>	  

	gCompanyName='<%=session.getAttribute("gCompanyName").toString()%>';

	  var opType="${CustOperationType}";
	  var opTypeCustName="${CustNameForOpeartion}";
	  var prefix="${prefix}";
	  if(opType=="FeedBackCustomer")
	  {
		  $("#idHeaderConfirm").text("");
		  $("#idHeaderConfirm").text(prefix+" "+opTypeCustName+" Thank You For giving your valuable feedback.This will help us to serve you better");
		  $("#lblOrderNo").text("");
	  }
	  else if(opType=="ReservationCustomer")
	  {
		  $("#idHeaderConfirm").text("");
		  $("#idHeaderConfirm").text(prefix+" "+opTypeCustName+" Your Table Reseravtion is Confirmed !!!");
		  $("#lblOrderNo").text("");

	  }
	  else if(opType=="MakeBillType")
	  {
		  $("#idHeaderConfirm").text("");
		  $("#idHeaderConfirm").text("Your Bill is Generated And Your Bill No is :- "+opTypeCustName);  
		  $("#lblOrderNo").text("");
	  }
	  else if(opType=="HomeDelivery")
      {
		  var strVal = getCookieForCustomerCode("SetCustomerCodeForQRMenu");
		  custDetails=funGetCustCodeData(strVal);
		  var cust=custDetails.split("#");
		  var gender="Miss";		  
		  if(cust[1]=='Male')
		  {
			   gender="Mr";		  

		  }
		  $("#idHeaderConfirm").text("");
		  $("#idHeaderConfirm").text(gender+" "+cust[0]+" Your Order has recieved,It will be delivered shortly.Thanks "+gCompanyName);  
		  $("#lblOrderNo").text("");
		  

      }

		});

function getContextPath() 
{
return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}

function getCookieForCustomerCode(name) {  
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

 
    function funGetCustCodeData(code){

    var custDetails="";
	$.ajax({
		type : "GET",
		url : getContextPath()+ "/GetAllCustomerDataForQRMenu.html?custCode="+code,
		dataType : "json",
		async:false,
		success : function(response)
		{ 	
			custDetails=response[0][1] +"#"+response[0][7];
	         		
		},
		
		error : function(e){
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
	return custDetails;
}

 
 
 

</script>
<body>

<s:form name="frmMainFormTable" method="GET" action=""  style="width: 100%; height: 100%;">

 <div class="container">
    <div class="row d-flex d-md-block flex-nowrap wrapper">
        <main class="container-fluid">
            <div class="row mb-3">
                 <div class="card card-inverse">
                        <div class="card-block">
					 	<h1 class="title" id="idHeaderConfirm"  style="text-align:center;color:#a31717;">Your Order is Confirmed</h1>
						 <div style="text-align:center;"><i class="mdi mdi mdi-checkbox-marked-circle menu-link" style="font-size:50px "></i></div>
						 	<h3 class="title" style="text-align:center;color:#a31717;" id="lblOrderNo"></h3>
						</div>					
				</div>
				 </div>

</main>
</div>
</div>
                
                <s:hidden id="hidOperationType" path="operationType"/>
		 		<s:hidden id="hidTableNo" path="strTableNo"/>
				


</s:form>


	
</body>


</html>