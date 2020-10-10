<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="True" %>
<!DOCTYPE html>
<html>
  <head>
      <meta charset="UTF-8">
     <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	  <script type="text/javascript" src="<spring:url value="/resources/js/jquery-3.0.0.min.js"/>"></script>
	  <script type="text/javascript" src="<spring:url value="/resources/js/jQKeyboard.js"/>"> </script>
	  <script type="text/javascript" src="<spring:url value="/resources/js/slideKeyboard/jquery.ml-keyboard.js"/>"></script>
	
		<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/jQKeyboard.css "/>" />
		<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/slideKeyboard/jquery.ml-keyboard.css"/>" />
		<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/slideKeyboard/jquery.ml-keyboard.css"/>" />	
		 <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.css"/>" />
	 <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.min.css"/>" />
	 <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/design.css"/>" />
	 <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap-grid.css"/>" />
	 <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap-grid.min.css"/>" />
	 
  <script type="text/javascript">
  	/**
	 *  Set Focus
	**/
  $(document).ready(function(){
       $('#username').focus();
      
       var strTouchScreenMode=localStorage.getItem("lsTouchScreenMode");
       if (strTouchScreenMode == null) 
		{
		   localStorage.setItem("lsTouchScreenMode", "N");
		}   
		/*if(strTouchScreenMode=='Y')
		{
		   $('input#username').mlKeyboard({layout: 'en_US'});
		   $('input#password').mlKeyboard({layout: 'en_US'});
		   $('#username').focus();
		}*/
		
	
  });
  	
  	
  	function funKeyBoard()
  	{
  			var lsKeyBoardYN= localStorage.getItem("lsTouchScreenMode");
  			if(lsKeyBoardYN=='Y')
  			{
  				localStorage.setItem("lsTouchScreenMode", "N");
  			}
  			if(lsKeyBoardYN=='N')
  			{
  				localStorage.setItem("lsTouchScreenMode", "Y");
  			}
  	
  	}
  	
</script>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Web Stocks</title>
  </head>

<body>
	<div class="container">
		  <s:form name="login" method="POST" action="validateUser.html">
		  <div class="row">
		    <div class="col-sm-12 col-lg-12">
				<div class="box">
							<h2>Sanguine Softwares Solutions Pvt.Ltd</h2>
							<h3  style="border: 2px solid #2e6eb1;border-bottom-right-radius:50%;width:50%s; background-color:#2e6eb1; color:#fff;"> Login</h3>
					<form style="padding-left:30px;">
						<s:label path="strUserCode">Username</s:label>
						<div class="inputbox">
					         <s:input name="usercode" type="text" path="strUserCode" autocomplete="off" id="username" required="true" cssStyle=" text-transform: uppercase; margin-bottom:20px" /> <s:errors path="strUserCode"></s:errors>
						</div>
						<s:label path="strPassword">Password</s:label>	
						<div class="inputbox">
							
							<s:input type="password" required="true" name="pass" path="strPassword" id="password" /> <s:errors path="strPassword"></s:errors>
						
						</div>
						<h2><input type="submit" name="" value="Submit" style="margin-left:190px; font-weight:700px"></h2>
					</form>
				</div>
				</div>
				</div>
			</s:form>
			                                 
			                                 		
	</div>
	
	
<c:if test="${!empty invalid}">
<script type="text/javascript">
	alert("Invalid Login");
</script>
</c:if> 
<c:if test="${!empty LicenceExpired}">
<script type="text/javascript">
	alert("Licence is Expired \n Please Contact Technical Support");
</script>
</c:if> 

</body>
</html>