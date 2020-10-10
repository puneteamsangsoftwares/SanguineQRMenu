<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>

		
<html>
<head>
<style>
</style>
	    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico" type="image/x-icon" sizes="16x16">
	    <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.grid.css"/>" />
<%-- 		<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.grid.min.css"/>" />
 --%>		<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.css"/>" /> 
		<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/design.css"/>" />
		<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/styles/materialdesignicons.min.css"/>" />
		
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript">
		var cntrlIsPressed = false;
		
		
		$(document).keydown(function(event){
		    if(event.which=="17")
		        cntrlIsPressed = true;
		});
		
		$(document).keyup(function(){
		    cntrlIsPressed = false;
		});
		
		function selectMe(mouseButton)
		{
			//alert(mouseButton);
			var _href = $(mouseButton).attr("href");
			
			//$(mouseButton).attr("href", _href + '?saddr=50.1234567,-50.03452');
			var split=_href.split("=");
			
			_href=split[0]+"=1" ;
			$(mouseButton).attr("href", _href);
			
		    if(cntrlIsPressed)
		    {
		    	var split1=_href.split("=");
		    
		    	_href=split1[0];
		    	$(mouseButton).attr("href", _href + '=2');
		    	cntrlIsPressed=false;
		    }
		    cntrlIsPressed=false;
		
		   
		}
		
		function onContextClick(mouseButton){
			var _href = $(mouseButton).attr("href");
			
			//$(mouseButton).attr("href", _href + '?saddr=50.1234567,-50.03452');
			var split=_href.split("=");
			
			_href=split[0]+"=1" ;
			$(mouseButton).attr("href", _href);
			
			var split1=_href.split("=");
		    
			_href=split1[0];
			$(mouseButton).attr("href", _href + '=2');		
		}
		
		
	</script>
</head>
<%-- <c:when test="${draw1.key}"> --%>
<body onload="">
	        <div class="app-sidebar">
        		<ul class="side-menu" id="tree">
        			<c:forEach items="${treeMap}" var="draw1" varStatus="status1">
        				<li>
        				  <a href="#" class="link">
			             	<c:set var="menuMaster" value="Master"/>
			             	<c:set var="menuReport" value="Report"/>
			             	<c:set var="menuTools" value="Tools"/>
			             	<c:set var="menuTransactions" value="Transaction"/>
			             	<c:set var="menuProcessing" value="Processing"/>
			             	<c:set var="menuSetup" value="Setup"/>
			             	<c:set var="menuReports" value="Reports"/> 
			             	<c:set var="menuStore" value="Store"/>
			             	<c:set var="menuAccounts" value="Accounts"/>
			             	<c:set var="menuCostCenter" value="Cost Center"/> 
			             	<c:set var="menuProduction" value="Production"/> 
			             	<c:set var="menuReceiving" value="Receiving"/>   
			             	<c:set var="menuSales" value="Sales"/>       <!--  -->
			             	<c:set var="menuPurchase" value="Purchase"/> 
			             	<c:choose> 
			             		<c:when test="${menuMaster == draw1.key}">
								  <i class="mdi mdi-source-fork rotate"></i>
									<div class="link-text">
									${draw1.key}
									</div>
								  </c:when>
								   <c:when test="${menuReport == draw1.key}">
									<i class="mdi mdi-file-chart-outline"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	    
								  </c:when>
								   <c:when test="${menuTools == draw1.key}">
									<i class="mdi mdi-wrench-outline"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	    
								  </c:when>
								   <c:when test="${menuTransactions == draw1.key}">
									<i class="mdi mdi-credit-card-outline"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	    
								  </c:when>
								  <c:when test="${menuProcessing == draw1.key}">
									<i class="mdi mdi-rotate-right"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	    
								  </c:when>
								  <c:when test="${menuSetup == draw1.key}">
									<i class="mdi mdi-settings"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	    
								  </c:when>
								  <c:when test="${menuReports == draw1.key}">
									<i class="mdi mdi-content-copy"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	  
								  </c:when>
								  <c:when test="${menuStore == draw1.key}">
									<i class="mdi mdi-store"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	    
								  </c:when>
								  <c:when test="${menuAccounts == draw1.key}">
									<i class="mdi mdi-account-plus"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	  
								  </c:when>
								  <c:when test="${menuCostCenter == draw1.key}">
									<i class="mdi mdi-bank"></i>
									<div class="link-text">
									${draw1.key} 				
									</div>	  
								  </c:when>
								  <c:when test="${menuProduction == draw1.key}">
									<i class="mdi mdi-factory"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	    
								  </c:when>
								  <c:when test="${ menuReceiving  == draw1.key}">
									<i class="mdi mdi-read"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	    
`								  </c:when>
								    <c:when test="${menuSales == draw1.key}">
									<i class="mdi mdi-currency-usd"></i>
									<div class="link-text">
									${draw1.key} 				
									</div>	  
								  </c:when>
								    <c:when test="${ menuPurchase  == draw1.key}">
									<i class="mdi  mdi-package-variant"></i>
									<div class="link-text">
									${draw1.key} 
									</div>	    
								  </c:when>
								  <c:otherwise>
								    <i class="mdi mdi-source-fork rotate"></i>
									<div class="link-text">
									${draw1.key} 
									</div>
								  </c:otherwise>
								</c:choose>
			             	 	
			           	 </a>
			              	<div class="submenu">
			           			<c:forEach items="${draw1.value}" var="draw2" varStatus="status2">
								<c:forEach items="${draw2.value}" var="draw3" varStatus="status3">
								<c:if test="${draw3.key=='Parent'}">
									<a href="${draw3.value.strRequestMapping}?saddr=1" id="${draw3.value.strRequestMapping}" onclick="selectMe(this)" oncontextmenu="onContextClick(this)"
			 							title='${draw3.value.strFormDesc}'>${draw3.value.strFormDesc}</a>
							
								</c:if>
								<c:if test="${draw3.key !='Parent'}">
									${draw3.key}
								<c:forEach items="${draw3.value}" var="draw4" varStatus="status4">
							 	<a href="${draw4.strRequestMapping}?saddr=1" id="${draw4.strRequestMapping}" onclick="selectMe(this)" oncontextmenu="onContextClick(this)"
			 									title='${draw4.strFormDesc}'>${draw4.strFormDesc}</a>
								</c:forEach>
								</c:if>
								</c:forEach>
								</c:forEach>
			           
				        	</div>
        				</li>
        			</c:forEach>
        		</ul>     
			 </div>
			 <script>
		/* window.onscroll = function() {myFunction()}; */
		
		var sidebar = document.getElementsByClassName("app-sidebar");
		var sticky = header.offsetTop;
		
		function myFunction() {
		  if (window.pageYOffset > sticky) {
			  sidebar.classList.add("sticky");
		  } else {
			  sidebar.classList.remove("sticky");
		  }
		}
		</script>
</body>
</html>