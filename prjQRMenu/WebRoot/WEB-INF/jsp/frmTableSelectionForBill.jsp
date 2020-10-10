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
	
	
	<title>Main Form</title>


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
	funShowTables();
			});

 
 
 
 
 
  function funTableNoClicked(obj)
 {
	 $("#hidOperationType").val('Dine In');
	 $("#hidTableNo").val(obj.id);
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
		    				col.innerHTML = "<td><input type=\"button\" id="+obj.strTableName+" value='"+obj.strTableName+"'    style='"+style+"'  onclick=\"funTableNoClicked(this)\" class='"+cssClass+"' /></td>";
		    				col.style.padding = "1px";

		    				insertCol++;
		    		   }
		    			 else
		    			{					
		    				insertTR=tblMenuItemDtl.insertRow();									
		    				insertCol=0;
		    				var col=insertTR.insertCell(insertCol);
		    				col.innerHTML = "<td><input type=\"button\" id="+obj.strTableName+" value='"+obj.strTableName+"'   style='"+style+"'   onclick=\"funTableNoClicked(this)\" class='"+cssClass+"' /></td>";
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
		        		
    		    		
		        		hmItempMap.set(itemCode,itemName);
		        		
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
     	//funNoPromtionCalculation(listItmeDtl)
     	//funCalculatePromotion
	    abc();
		funRefreshSettlementItemGrid();

}


</script>
</head>
<body>




<s:form name="frmMainFormTable" method="GET" action=""  style="width: 100%; height: 100%;">

 
 <div class="row d-flex d-md-block flex-nowrap wrapper"> 
        <main class="container-fluid" style="width: 637px;margin-left: -49px;">
			 <div class="card card-inverse">
					  <div id="divItemDtl" style="border: 1px solid rgb(204, 204, 204);overflow: auto;width: 100%;display: block; margin-top:2px; margin-left:2px; font-family: sans-serif;">									
                        <div class="head" style="text-align:center;padding:20px 10px;">
                         	<h5 style="color:#1b1717; font-size:28px;font-family:'trebuchet ms';">Select Your Table No</h5>
                        </div>
								<table id="tblMasterTableDtl">
										
									<c:set var="sizeOfTables" value="${fn:length(command.jsonArrForTableDtl)}"></c:set>									   
									<c:set var="itemCounter" value="${0}"></c:set>							
									     
										 <c:forEach var="objTableDtl" items="${command.jsonArrForTableDtl}"  varStatus="varTable">																																		
											<tr>
												<%
												for(int x=0; x<4; x++)
													{
												%>														
														<c:if test="${itemCounter lt sizeOfTables}">																																		
															<td style="padding: 5px;"><input type="button" class="btn btn-primary" style= "width: 100px;height: 100px;margin:20px;background-color:#d8d2d2;border: 1px solid #deb8b8;color:#000;font-family:'trebuchet ms'; "; id="${command.jsonArrForTableDtl[itemCounter].strTableNo}"  value="${command.jsonArrForTableDtl[itemCounter].strTableName}"  onclick="funTableNoClicked(this,${itemCounter})"  />
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
                </div>
                  
		</main>
 </div>       
           <s:hidden id="hidOperationType" path="operationType"/>
	       <s:hidden id="hidTableNo" path="strTableNo"/>
				

</s:form>

</body>

</html>