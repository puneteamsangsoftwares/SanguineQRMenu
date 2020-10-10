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
	
	
<%-- 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.grid.min.css"/>" />
 --%>	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.min.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/style.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/materialdesignicons.min.css"/>" />
<%-- 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/mdb.min.css"/>" />
 --%>	
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
<%-- 	<script type="text/javascript" src="<spring:url value="/resources/js/mdb.min.js"/>"></script>
 --%>
		<script type="text/javascript" src="<spring:url value="/resources/js/print.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/easy-numpad.js"/>"></script>
		 <link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/jquery-confirm.min.css"/>"/>
<script type="text/javascript" src="<spring:url value="/resources/js/jquery-confirm.min.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/confirm-prompt.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>
	
	
	<title>Main Form</title>

</head>

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
	
			
		});

 
 
 
 
 

</script>
<body>

<s:form name="frmMainFormTable" method="GET" action=""  style="width: 100%; height: 100%;">

 <div class="container">
    <div class="row d-flex d-md-block flex-nowrap wrapper">
        <main class="container-fluid">
            <div class="row mb-3">
                 <div class="card card-inverse">
                        <div class="card-block">
					 	<h1 class="title" style="text-align:center;color:#a31717;">Please Contact Technical Support</h1>
						 	<h3 class="title" style="text-align:center;color:#a31717;" id="lblOrderNo">PUNE +91 7776002200 <br> MUMBAI 022 26209898 </h3>
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