<%-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>
 --%><%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width">
	<meta name="viewport" content="initial-scale=1.0">
	<meta name="viewport" content="width=590">
		<meta name="theme-color" content="#111">
	
	<meta name="viewport" content="width=device-width,  user-scalable=no"/> <!--320-->
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/css/		materialdesignicons.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/5.3.45/fonts/materialdesignicons-webfont.ttf">
	<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@100;300;400;500;700&display=swap" rel="stylesheet">
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
	
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	
	<title>Dashboard</title>

</head>


   
 <style type="text/css">
 .table{
 margin:0px;}
 
 .vegitem{
    font-size: 45px;
	color: black;
	font-weight:700;
	text-align: left;	
	padding: 9px 0 0px;
	background-image: url('../${pageContext.request.contextPath}/resources/images/icons8-veg.png');
	background-repeat: no-repeat;
	padding-left: 25px;
	float: left;
	padding-top: 0px;
	
	}
.img-square-wrapper{width: 450px;
    height:450px;
 }
 
 .vegitemwithimage{
    font-size: 21px;
	color: #960606;
	text-align: center;	
	padding: 9px 0 0px;
	background-image: url('../${pageContext.request.contextPath}/resources/images/item-1.png');
	background-repeat: no-repeat;
	padding-left: 25px;
	float: left;
	padding-top: 0px;
	width:90%;
	height:200px;
	}
.itemName{
	font-size: 45px;
	color: black;
	border:none;
	width:100%;
	font-weight:700;
	font-family:'calibri';
	
}
.itemDetails{
font-family:'calibri';
width:200%;
font-size:22px;
margin-top:2%;
margin-bottom:25px;
}
 
/* body 
{ background-image:url('../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif'); 
background-repeat:repeat; } */
</style>





<script type="text/javascript">
// //	background-image: url('../${pageContext.request.contextPath}/resources/images/item-1.png');
//	background-image: url('../${pageContext.request.contextPath}/resources/images/item-1.png');


$(document).ready(function() 
		{
			   funShowItem();
	           var opMode="${operationMode}"	  
			   var tblNo="${tableOp}"
	           if(opMode == 'Take Away')
	       	   {
	               $("#operationMode").text("Take Away");
	       	   }
	           else
	           {
	               $("#operationMode").text("Table No: "+tblNo);
	           }	   
				var jsonArrForMenuHeads=${command.jsonArrForMenuHeads};	
				funFillMenuHeadMap(jsonArrForMenuHeads);
				funMenuHeadButtonClicked();
				document.getElementById("itemCountInMainPage").style.display='none';
				
				var gCompanyCode='<%=session.getAttribute("gClientCode").toString()%>';

				if(gCompanyCode=='240.001')
				{
					document.getElementById("idShoppingCart").style.display='none';
					document.getElementById("idHome").style.display='none';
					document.getElementById("idSideBarPanel").style.display='none';

					

				}
			});

 function aa()
 {
	 alert('dashborad');
 }
function funFillMenuHeadMap(jsonArrForMenuHeads)
{
	for(var i=0;i<jsonArrForMenuHeads.length;i++)
	{
		mapMenuHead.set(jsonArrForMenuHeads[i].strMenuCode,jsonArrForMenuHeads[i].strMenuName);
		mapMenuHeadWithNameKey.set(jsonArrForMenuHeads[i].strMenuName,jsonArrForMenuHeads[i].strMenuCode);
	}
	

}


 function funMenuHeadButtonClicked()
{
	var $rows = $('#tblMenuItemDtl').empty();
	
	var tblMenuItemDtl=document.getElementById('tblMenuItemDtl');
	
	//$("#txtMenuHeadName").val(mapMenuHead.get(selctedMenuHeadCode));

	
	flagPopular="menuhead";
	var jsonArrForMenuItemPricing=${command.jsonArrForDirectBillerMenuItemPricing};	
	var rowCount = tblMenuItemDtl.rows.length;	
	var insertRow = tblMenuItemDtl.insertRow(rowCount);
//text-align:center;color:#521414; font-size:27px;font-weight:600;
	itemPriceDtlList=new Array();
	var insertCol=0;
	var rowNo=0;
	var insertTR=tblMenuItemDtl.insertRow();
	var index=0;
	var FlagMenuHead=true;
	var SelectedMenuHeadName="";
	mapMenuHead.forEach(function(value, key) {
		var selctedMenuHeadCode=key;
		var header=true;
    
		$.each(jsonArrForMenuItemPricing, function(i, obj) 
		{									
			if(obj.strMenuCode==selctedMenuHeadCode)
			{									
				var tmpprice=Math.round(obj.strPriceMonday);
	            if(header){
	    		    var insertFirstRow = tblMenuItemDtl.insertRow(rowNo)

	            	var colFirstRow=insertFirstRow.insertCell(0);

	    		    colFirstRow.innerHTML = "<input readonly=\"readonly\"  class=\"\" \"  id='"+selctedMenuHeadCode+"'  value='"+mapMenuHead.get(selctedMenuHeadCode).trim()+"' style=\"text-align:left;color:#000;border-style:none; font-size:65px;font-weight:1000;margin-top:73px;margin-bottom:73px;width:auto;height:25%;padding-left:30px;\"  />";
	    		    rowNo++;
	    		    header=false;
                }

				
			    var insertRow = tblMenuItemDtl.insertRow(rowNo)
			    var col1=insertRow.insertCell(0);
			    var col2=insertRow.insertCell(1);
			    var col3=insertRow.insertCell(2);
			    var col4=insertRow.insertCell(3);
				if(obj.strItemImage.length >0)
				{
					col1.innerHTML = "<div class=\"col-md-12 mt-3 box\" style=\"padding-left:0px;margin:30px 0px;\"><div class=\"card\" style=\"border:none;\"><div class=\"card-horizontal\"  style=\"border:none;\"><div class=\"img-square-wrapper\" style=\"height:450px;width:450px;\"><img  src=\"\" readonly=\"readonly\" style=\" margin-top: -20%; height: 134%;\"  class=\"itemName\"  id='itemImage"+obj.strItemCode+"'  onclick=\"funOpenSingleItem(this,'image')\" ></div>"

			           + " <div class=\"card-body container\" style=\"background-color: #fbf9f9;\"><div class=\"vegitem\" style=\"height:20px;margin-left:465px;\"></div><h3 readonly=\"readonly\" style=\"font-size:45px; font-weight:700; padding-left:45px; width: 55%;margin-bottom:10px; margin-left:-35px; font-family:calibri;\" class=\"card-title\" \"  id='"+obj.strItemCode+"'   onclick=\"funOpenSingleItem(this,'')\"  /> "+obj.strItemName+" </h3>"
			           + " <p readonly=\"readonly\"   class=\"card-text\"  style=\"margin: 0px;margin-right:425px; font-size:30px; color:#676666; width:41%; height:285px; font-weight:700; padding-left:12px; overflow-y:scroll;\" onclick=\"funOpenSingleItem(this,'')\"  id='"+obj.strItemCode+"' />"+obj.strItemDetails+"</p> "

			           + " <div style=\"margin-top:5px;\"><small class=\"price\" style=\"font-size:36px;padding-left:12px; font-weight:700;\"><span class=\"mdi mdi-currency-inr\"></span>"+tmpprice+"<i class=\"mdi mdi-nest-protect\"></i></small></div></div></div></div></div>";

			           funloadItemImage(obj.strItemCode);
     			}
				else{          //			       this is without image
                       col1.innerHTML = "<div class=\"vegitem\"><a readonly=\"readonly\" class=\"itemName\"  id='"+obj.strItemCode+"'  style=\"margin-top:-5px\" onclick=\"funOpenSingleItem(this,'')\" > "+obj.strItemName+" </a><input readonly=\"readonly\" size=\"\"   class=\"itemRate\" id='"+obj.strItemCode+"'style=\"width:100%;left:812px;font-size:37px;position: absolute;font-weight:700;height:30px;\" value='\&#2352;\ "+tmpprice+"' onclick=\"funOpenSingleItem(this,'')\" /></div>";                      
                       var itemdeatilsCount=obj.strItemDetails.length;
                        
				if(itemdeatilsCount>50)
				{
					itemdeatilsCount=itemdeatilsCount -50;
					//obj.strItemDetails.substring(0,50)
					rowNo++; 
					insertRow = tblMenuItemDtl.insertRow(rowNo);
			        var colNextRow=insertRow.insertCell(0);
		            colNextRow.innerHTML = "<input readonly=\"readonly\" size=\"200px\"  class=\"itemDetails\" style=\"text-align: left; color:#676666;font-size:30px; height:30px;border:none; padding-left:5px;margin:15px 25px;margin-bottom:45px;\"  id='"+obj.strItemCode+"' onclick=\"funOpenSingleItem(this,'')\"  value='"+obj.strItemDetails.substring(0,50)+"'  />";
			        if(itemdeatilsCount >50)
			        {
			        	itemdeatilsCount=itemdeatilsCount -50;
						//obj.strItemDetails.substring(0,50)
						rowNo++; 
						insertRow = tblMenuItemDtl.insertRow(rowNo);
				        var colNextRow=insertRow.insertCell(0);
			            colNextRow.innerHTML = "<input readonly=\"readonly\" size=\"200px\"  class=\"itemDetails\" style=\"text-align: left; color:#676666;font-size:30px; height:30px;border:none; padding-left:5px;margin:15px 25px;margin-bottom:45px;\"  id='"+obj.strItemCode+"' onclick=\"funOpenSingleItem(this,'')\"   value='"+obj.strItemDetails.substring(50,101)+"'  />";
			            if(itemdeatilsCount >50)
						{
							itemdeatilsCount=itemdeatilsCount -50;
							//obj.strItemDetails.substring(0,50)
							rowNo++; 
							insertRow = tblMenuItemDtl.insertRow(rowNo);1
					        var colNextRow=insertRow.insertCell(0);
				            colNextRow.innerHTML = "<input readonly=\"readonly\" size=\"200px\" onclick=\"funOpenSingleItem(this,'')\"  class=\"itemDetails\" style=\"text-align: left; color:#676666; font-size:30px;height:30px;border:none; padding-left:5px;margin:15px 25px;margin-bottom:45px;\"  id='"+obj.strItemCode+"'  value='"+obj.strItemDetails.substring(101,151)+"'  />";
				            if(itemdeatilsCount >50)
							{
								itemdeatilsCount=itemdeatilsCount -50;
								//obj.strItemDetails.substring(0,50)
								rowNo++; 
								insertRow = tblMenuItemDtl.insertRow(rowNo);1
						        var colNextRow=insertRow.insertCell(0);
					            colNextRow.innerHTML = "<input readonly=\"readonly\" size=\"200px\"  class=\"itemDetails\" style=\"text-align: left; color:#676666;font-size:30px; height:30px;border:none; padding-left:5px;margin:15px 25px;margin-bottom:45px;\"  id='"+obj.strItemCode+"' onclick=\"funOpenSingleItem(this,'')\"  value='"+obj.strItemDetails.substring(151,200)+"'  />";
						            
							
							}
							else
							{
								rowNo++; 
								insertRow = tblMenuItemDtl.insertRow(rowNo);
						        var colNextRow=insertRow.insertCell(0);
					            colNextRow.innerHTML = "<input readonly=\"readonly\" size=\"200px\"  class=\"itemDetails\" style=\"text-align: left; color:#676666;font-size:30px; height:30px;border:none; padding-left:5px;margin:15px 25px;margin-bottom:45px;\"  id='"+obj.strItemCode+"'  onclick=\"funOpenSingleItem(this,'')\" value='"+obj.strItemDetails.substring(151,obj.strItemDetails.length)+"'  />";
						        
							}    
						
						}
						else
						{
							rowNo++; 
							insertRow = tblMenuItemDtl.insertRow(rowNo);
					        var colNextRow=insertRow.insertCell(0);
				            colNextRow.innerHTML = "<input readonly=\"readonly\" size=\"200px\"  class=\"itemDetails\" style=\"text-align: left; color:#676666;font-size:30px; height:30px;border:none; padding-left:5px;margin:15px 25px;margin-bottom:45px;\" onclick=\"funOpenSingleItem(this,'')\"  id='"+obj.strItemCode+"'  value='"+obj.strItemDetails.substring(101,obj.strItemDetails.length)+"'  />";
					        
						}	
			        }
			        else
			        {
			        	rowNo++; 
						insertRow = tblMenuItemDtl.insertRow(rowNo);
				        var colNextRow=insertRow.insertCell(0);
			            colNextRow.innerHTML = "<input readonly=\"readonly\" size=\"200px\"  class=\"itemDetails\" style=\"text-align: left; color:#676666;font-size:30px; height:30px;border:none; padding-left:5px;margin:15px 25px;margin-bottom:45px;\"  onclick=\"funOpenSingleItem(this,'')\"  id='"+obj.strItemCode+"'  value='"+obj.strItemDetails.substring(50,obj.strItemDetails.length)+"'  />";
				        
			        }	
					
				}
				else
				{
					rowNo++; 
					insertRow = tblMenuItemDtl.insertRow(rowNo);
			        var colNextRow=insertRow.insertCell(0);
		            colNextRow.innerHTML = "<input readonly=\"readonly\" size=\"200px\"  class=\"itemDetails\" style=\"text-align: left; color:#676666;font-size:30px; height:30px;border:none; padding-left:5px;margin:15px 25px;margin-bottom:45px;\"  id='"+obj.strItemCode+"'  onclick=\"funOpenSingleItem(this,'')\"  value='"+obj.strItemDetails+"'  />";
			
				}	
				
				
		}			
				itemPriceDtlList[insertCol]=obj;
				insertCol++;
				rowCount++;
				rowNo++;
		}
		});

	
	
	});
//	$("#wait").css("display", "none");
	}0

function hidePopup()
{
	document.getElementById('popover').style.cssText = 'display: none';

}
function funMenuHeadButtonClickedSearched(objSearch)
{
	var key=  mapMenuHeadWithNameKey.get(objSearch.id);
	  document.getElementById(key).focus();
}	

function funloadItemImage(itemCode)
 {
	
		var searchUrl1 = getContextPath() + "/loadItemImage.html?itemCode="+itemCode;
		$.ajax({
			type : "GET",
			url : searchUrl1,
			cache : false
		});
		$("#itemImage"+itemCode).attr('src', searchUrl1);
		//$("#memImage").attr('src', searchUrl1);
	}
	function funLoadGIF()
	{
	//	$("#wait").css("display", "block");
	//	alert("gif");
	}
	function openNav() {
		  document.getElementById("mySidepanel").style.width = "50%";
		  document.getElementById("mySidepanel").style.height = "100vh";

		}

		function closeNav() {
		  document.getElementById("mySidepanel").style.width = "0";
		}
		function funViewTakeAway()
		{
		
			 $("#hidOperationType").val('Take Away');
			 document.Billing.action = "actionTableClicked.html";//?kotNo="+kotNo
		  	 document.Billing.method = "POST";
		     document.Billing.submit();
		
		}

</script>

	<body style="font-family:'calibri'; margin-top:-39px;margin-left:-25px;width:34%;"  onload="funLoadGIF();">

	<s:form name="Billing" method="GET" action=""  style="width: 100%; height: 100%;">

	<div class="header">
		<div class="container">	
			<div class="fixheader" style="width:100%;position:fixed;z-index:1;">
			<div class="topnav" style="padding:0px 75px; padding-top:24px;background:#000;">
				<div id="mySidepanel" class="sidepanel">
				<!-- href="javascript:void(0)" -->	<a  class="closebtn"><i class="mdi mdi-close" style="font-size: 60px; float: right;margin-right:-15%;color: #fff;" onclick="closeNav()"></i></a>
					<a href="frmMainFormWithLoadTable.html?modeOfOperation=Dine">Dine In</a>
					<a href="#" onclick="funViewTakeAway()">Take Away</a>
					<a href="frmHomeDelivery.html">Home Delivery</a>
					<a href="frmPOSBillSettlement.html">Bill Generation</a>
					<a href="frmReservation.html">Reservation</a>
					<a href="frmFeedback.html">Feedback</a>
				</div>
				<button class="openbtn" id="idSideBarPanel" type="button"   onclick="openNav()" style="font-size: 60px;"><i class="mdi mdi-menu" style="font-size: 60px; float: left;padding-right:5px;color: #fff;"></i>Menu</button>
				
				<div class="topnav-right">
					<a href="frmModeOfOpearation.html"><i class="mdi mdi-home"  id="idHome" style="font-size: 80px;float: right;margin-right: 8%;color: #fff;margin-top: -20%;"  onclick=""></i></a>
					<a href="#"><i class="mdi mdi-shopping" id="idShoppingCart" style="font-size: 78px;float: right;margin-right:-15%;color: #fff;margin-top: -20%;" onclick="funOpenSettlement()"></i></a>
			 		<input type="text" class="numberCircle" readonly="readonly" id="itemCountInMainPage" style="margin-right: -1%;" onclick="funOpenSettlement()" />
			
				</div>
			</div> 
  
	 	
			   <div class="scrollmenu">
				 
				 <table id="tblMenuHeadDtl"    style="border-collapse: separate;margin:0px;height:170px;padding-top:67px;background:#000;"> <!-- class="table table-striped table-bordered table-hover" -->
									 <c:set var="sizeOfmenu" value="${fn:length(command.jsonArrForDirectBillerMenuHeads)}"></c:set>
									 <c:set var="menuCount" value="${0}"></c:set>
									 
									  <c:forEach var="objMenuHeadDtl" items="${command.jsonArrForDirectBillerMenuHeads}"  varStatus="varMenuHeadStatus">																																		
												<tr>
												<% 
												for(int k=0;k<50;k++) 
												{
												%>	
												
												<c:if test="${menuCount lt sizeOfmenu}">
													<td style="padding-right: 5px;diaplay:inline;" >
														<label  id="${command.jsonArrForDirectBillerMenuHeads[menuCount].strMenuName}"    style= "width:auto;display:inline-block; text-align:center;label:active{underline};white-space:nowrap;color:white;font-weight:600;font-size:37px;padding-left:65px;"       
														onclick="funMenuHeadButtonClickedSearched(this)"  >${command.jsonArrForDirectBillerMenuHeads[menuCount].strMenuName}
														</label>
													</td>
													<c:set var="menuCount" value="${menuCount +1}"></c:set>
												</c:if>																						 													
																
													
												<%
												}
												%>
											   </tr>																																
										</c:forEach>									   				   									   									   							
									</table>
				 
				</div> 
			
		</div>	
	</div>
</div>
	<div class="container" style="margin-top: 253px;">
		<div class="itembox" >
             <table id="tblMenuItemDtl" style="margin:0px;margin-left:20px;">
										
																		
			</table>

		 </div>	
	</div>
	<div id="wait"
			style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 45%; left: 45%; padding: 2px;">
			<img
				src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
				width="60px" height="60px" />
	</div>
</s:form>
</body>


</html>