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
.sect1{border:4px solid #fff;text-align: center;margin: 50px auto;width: 82%;height:6%;}
.sect1:hover{background:#615a5ad4;}
.sect1 p{color:#fff;margin: 10px 0px;font-size: 48px;}
</style>
<script type="text/javascript">
        $(document).ready(function() 
		{
        	var modeOfOperation="";
        	<%if(session.getAttribute("ModeOfOpType")!=null)  
        	{%>
        		
        		modeOfOperation='<%=session.getAttribute("ModeOfOpType").toString()%>';
        		
        	<%}%>       		
        	if(modeOfOperation=='TA')
        	{
        		funViewTakeAway();
        	}	
	     
		});
		function funViewTakeAway()
		{		
			 $("#hidOperationType").val('Take Away');
			 document.frmModeOfOperation.action = "actionTableClicked.html";//?kotNo="+kotNo
		  	 document.frmModeOfOperation.method = "POST";
		     document.frmModeOfOperation.submit();
		}

</script>

</head>
<body>
<s:form name="frmModeOfOperation" method="GET" action=""  style="width: 100%; height: 100%;">

	<div class="container-fuild">
		<div class="fixheader" style="margin-top: 0px;">
            <a href="#"><img src="../${pageContext.request.contextPath}/resources/images/Sanguine_Logo.png" style="height:auto;width:44%;"></a>
            <a href="#" style="margin-left: 94px;"><img src="../${pageContext.request.contextPath}/resources/images/Restaurant.png" style="height:auto;width:30%;margin-left:70px;margin-top:3%"></a>
        </div>
        
<!-- 	</div>
 --><!-- 	<div class="container-fuild">
 -->	<div class="firstpage">
 			<div class="head">
				<h2 class="animate__animated animate__slideInLeft">Welcome to <br>${gCompanyName}</h2>
				<p class="animate__animated animate__slideInLeft">Select your order type below to view the menu and place your order online.</p>
			</div>
				<a href="frmMainFormWithLoadTable.html?modeOfOperation=Dine In"><div class="sect1 animate__animated animate__slideInLeft"><p>View Dine In Menu</p></div></a>
				<a href="#Billing.html"><div class="sect1 animate__animated animate__slideInLeft"><p onclick="funViewTakeAway()">View Take Away Menu</p></div></a>
			    <a href="frmHomeDelivery.html"><div class="sect1 animate__animated animate__slideInLeft"><p>Home Delivery</p></div></a>			
<!--                 <a href="frmPOSBillSettlement.html"><div class="sect1 animate__animated animate__slideInLeft"><p>Generate Bill</p></div></a>
                <a href="frmReservation.html"><div class="sect1 animate__animated animate__slideInLeft"><p>Reservation</p></div></a>
                <a href="frmFeedback.html"><div class="sect1 animate__animated animate__slideInLeft"><p>Feedback</p></div></a>
 -->                
                
          <!-- frmMainFormWithLoadTable.html?modeOfOperation=Bill Generation -->     
		</div>
	</div>
  <s:hidden id="hidOperationType" path="operationType"/>

</s:form>
</body>
</html>