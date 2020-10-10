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
<!-- <meta name="viewport" content="width=device-width, user-scalable=no">
 -->
<title>Mode of Operation</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/css/		materialdesignicons.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/fonts/materialdesignicons-webfont.ttf">
	<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@100;300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.0.0/animate.min.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/style.css"/>" />
	
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
	

<style type="text/css">

.firstpage{background: #111;height: 100vh;}
.firstpage .head h2{text-align: center;color: #fff;margin: 0px;padding-top: 23%;font-size: 64px;}
.firstpage .head p{text-align: center;margin: 16px 38px;font-size: 40px;color:#ffffff8c;margin-bottom: 59px;}
.sect1{border:4px solid #fff;text-align: center;margin: 50px auto;width: 82%;height:100px;}
.sect1:hover{background:#615a5ad4;}
.sect1 p{color:#fff;margin: 10px 0px;font-size: 48px;}
.groupcode{
width:100%;background-color:black;color:white;font-size:56px;height:91px;
}
</style>
<script type="text/javascript">


$(document).ready(function() 
		{
			funFillTable();
		///	alert(mapGroup);
		});
function funClickAction(obj,index)
{
	 var gCode=	document.getElementById("strGroupCode."+ index).value;
	 document.frmClickGroup.action = "actionGroupClicked.html?groupCode="+gCode;
  	 document.frmClickGroup.method = "POST";
     document.frmClickGroup.submit(); 

}
function funFillTable()
{
	var mapGroup=${command.jsonArrForMenuItemPricing};
	var $rows = $('#tblGroupTable').empty();
	var tblSettleItemDtl=document.getElementById('tblGroupTable');
	var rowCount = tblSettleItemDtl.rows.length;
	rowCount=0;
	$.each(mapGroup, function(i, obj) 
	{	
		var insertRow = tblSettleItemDtl.insertRow(rowCount);
     	
	    var col1=insertRow.insertCell(0);
	    var col2=insertRow.insertCell(1);
   	    col1.innerHTML = "<div class=\"sect1 animate__animated animate__slideInLeft\"><input readonly=\"readonly\" size=\"33px\"  class=\"groupcode\" onclick=\"funClickAction(this,"+rowCount+")\"  id=\"strGroupName."+(rowCount)+"\" value='"+obj.strGroupName+"' /></div>";
	    col2.innerHTML = "<input type=\"hidden\" size=\"0px\"    id=\"strGroupCode."+(rowCount)+"\"  value='"+obj.strGroupCode+"' />";
	    rowCount++;
	   });
			     	
}
</script>

</head>
<body>
<s:form name="frmClickGroup" method="GET" action=""  style="width: 100%; height: 100%;">

	<div class="container-fuild">
	<div class="icondiv" style="height:85px;">
				<a href="frmModeOfOpearation.html"><i class="mdi mdi-home" style="height:85px;margin:0px 40px;color: #fff;font-weight:bolder;
						font-size: 67px;"  onclick="funBackToDashboard()"></i></a>
				</div>
		<div class="firstpage">
 			<div class="head">
				<h2 class="animate__animated animate__slideInLeft">Dine In Menu</h2>
				<p class="animate__animated animate__slideInLeft">Select your category below</p>
			</div>
			<table id="tblGroupTable" style=" height:auto;border-collapse: collapse;overflow: auto;
									font-size: 14px;">
					<%-- <a href="#frmMainFormWithLoadTable.html?modeOfOperation=Dine In"><div class="sect1 animate__animated animate__slideInLeft">
				<p>items="${groupList}"</p>
				
 						<s:select id="txtGroupName" path="" items="${groupList}" />
 
 </div>
				</a> --%>
						</table>
				
				
				<!-- <a href="#Billing.html"><div class="sect1 animate__animated animate__slideInLeft"><p onclick="funViewTakeAway()">View Take Away Menu</p></div></a>
			    <a href="frmHomeDelivery.html"><div class="sect1 animate__animated animate__slideInLeft"><p>Home Delivery</p></div></a>			
                <a href="frmPOSMakeBill.html"><div class="sect1 animate__animated animate__slideInLeft"><p>Generate Bill</p></div></a>
                <a href="frmReservation.html"><div class="sect1 animate__animated animate__slideInLeft"><p>Reservation</p></div></a>
                <a href="frmFeedback.html"><div class="sect1 animate__animated animate__slideInLeft"><p>Feedback</p></div></a>
               -->  
                
          <!-- frmMainFormWithLoadTable.html?modeOfOperation=Bill Generation -->     
		</div>
	</div>
 <%--  <s:hidden id="hidOperationType" path="operationType"/> --%>

</s:form>
</body>
</html>