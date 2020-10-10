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
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
	<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@100;300;400;500;700&display=swap" rel="stylesheet">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.0.0/animate.min.css"/>
	
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
	
	<!-- Latest compiled and minified CSS -->
	  
	<!--jQuery library -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	
 <!--jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/wow/1.1.2/wow.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/wow/1.1.2/wow.min.js"></script>
 -->

<style type="text/css">

body{line-height:0.5;}
.firstpage{background: #111;height:300vh;padding:0px 40px;color: #fff;}
.firstpage .head h2{margin: 0px;font-size:65px;padding: 65px 0px;font-weight:700;}
.head h3{font-size: 2.75rem;margin-bottom:20px;}
.firstpage .head p{margin: 16px 0px;font-size: 14px;color:#ffffff;margin-bottom: 15px;}
.sect1{border:4px solid #fff;text-align: center;margin: 20px auto;width: 257px;}
.sect1:hover{background:#615a5ad4;}
.sect1 p{color:#fff;margin: 8px 0px;font-size: 20px;}

.btn-outline-primary {color: #ffffff;font-size:40px;font-weight:600;background-color:green;border-radius:0.75rem;border-color: #000;padding:5px 100px;margin-top:40px;margin-left:4%;width:92%;height:85px;text-align:center;}
.btn-outline-primary hover{color: #000;background-color: #ffffff;}
.form-control{
  background: #111;
  color:#ffffff;
  height:85px;
  margin-bottom:50px;
  font-size:41px;
}

</style>

<script type="text/javascript">

var bookingNO="${bookingNo}";
var bookingYN="${bookingYN}";
$(document).ready(function() 
		{
             getCustCodeFromCookie();
		});

function getContextPath() 
{
return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}
function setCookieForCustmerCode(c_name, value, expiredays) {  
    var exdate = new Date();  
     exdate.setDate(exdate.getDate() + expiredays);  
     document.cookie = c_name + "=" + value + ";path=/" + ((expiredays ==null) ? "" : ";expires=" + exdate.toGMTString());  
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
 
 function getCustCodeFromCookie() {  
     var strVal = getCookieForCustomerCode("SetCustomerCodeForQRMenu"); 
    
    if(strVal==null)
   	{
    	  var custCode=funGetCustCodeForQRMenu();
          setCookieForCustmerCode("SetCustomerCodeForQRMenu", custCode, 1825); 
          $("#hidCustCode").val(custCode);
   	}
    else
   	{
    	
    	 funSetCustCodeDataForQRMenu(strVal);
   	}  
 }  

		function funSaveCustDetails() {
			
			if($("#custName").val()== "")
			{
				alert("Please Enter Customer Name");
				return false;
			}	
			if($("#MobNumber").val()== "")
			{
				alert(" Enter Mobile Number");
				return false;
			}
			if($("#address").val()== "" && bookingYN=="HomeDelivery")
			{
				alert("Please Enter Address");
				return false;
			}
			document.frmHomeDelivery.action ="savePOSCustomerMaster.html?bookingNO="+bookingNO+"&bookingYN="+bookingYN+""; //"actionBillSettlementKOT.html";
			document.frmHomeDelivery.method = "POST";
		    document.frmHomeDelivery.submit();
			 
			
		}
		
		function funGetCustCodeForQRMenu(){

			var code="";
			$.ajax({
				type : "GET",
				url : getContextPath()+ "/GetCustomerCodeForQRMenu.html",
				dataType : "json",
				async:false,
				success : function(response)
				{ 
					
					code=response;
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
			return code;
		}

		function funSetCustCodeDataForQRMenu(code){

			$.ajax({
				type : "GET",
				url : getContextPath()+ "/GetAllCustomerDataForQRMenu.html?custCode="+code,
				dataType : "json",
				async:false,
				success : function(response)
				{ 
					
			          $("#hidCustCode").val(response[0][0]);
			          $("#custName").val(response[0][1]);
			          $("#address").val(response[0][2]);
			          $("#HouseNo").val(response[0][3]);
			          $("#NearestLandmark").val(response[0][4]);
			          $("#MobNumber").val(response[0][5]);
			          $("#emailId").val(response[0][6]);

					
					
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
		}


 </script>




</head>
<body>
<s:form name="frmHomeDelivery" method="GET" action="" style="width: 100%; height: 100%;">

	<div class="container-fuild">


 <div class="firstpage">
   <div class="head">
          <h2>Customer Details</h2>
    </div>
    <br><br><br><br>
 <%-- <div class="form-group">
    <label for="sell">Select list:</label>
    <select class="form-control" id="sell" style="border:none;border-bottom: 1px solid;">
        <option>Viman Nagar,Pune,Maharashtra 411014,India</option>
    </select>
  </div>
  --%>
            <div class="head">
                 <h3>Customer Name <span style="color:red;">*<span></h3>
           </div>
           <div class="form-group">
             <s:input type="text" class="form-control" id="custName" name="Custmer Name" path="strCustomerName" />
           </div>
            <div class="head">
                  <h3>Mobile Number</h3>
            </div>
           <div class="form-group">
            <s:input type="text" class="form-control" id="MobNumber" name="Mobile Number" path="intlongMobileNo"/>
           </div>
            <div class="head">
                  <h3>Email ID</h3>
            </div>
           <div class="form-group">
            <s:input type="text" class="form-control" id="emailId" name="Emain ID" path="strEmailId"/>
           </div>
           <div class="head">
                  <h3>Address</h3>
            </div>
           <div class="form-group">
            <s:input type="text" class="form-control" id="address" name="Address" path="strCustAddress" />
           </div>
  
  
           <div class="head">
                  <h3>Gender</h3>
            </div>
           <div  class="form-group">
		   <s:select id="idGender"   style="width: 18%;height: 53px;font-size: 38px;" path="strGender" >
		   <option value="Male">Male</option>
		   <option value="Female">Female</option>
		   </s:select>
		   </div>
        
          <div class="head">
                 <h3>House No/Apartment <span style="color:red;">*<span></h3>
           </div>
           <div class="form-group">
            <s:input type="text" class="form-control" id="HouseNo" name="House No/Apartment" path="strStreetName"/>
           </div>

           <div class="head">
                  <h3>Nearest Landmark</h3>
            </div>
           <div class="form-group">
            <s:input type="text" class="form-control" id="NearestLandmark" name="Nearest Landmark" path="strLandmark"/>
           </div>
           
           
   <br><br>
          <div>
             <button type="button"  class="btn btn-outline-primary" onclick="funSaveCustDetails()">CONTINUE</button>
      	  </div>
    </div>
    </div>
   <s:hidden id="hidCustCode" path="strCustomerCode"/>
</s:form>

</body>
</html>