<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bill Settlement</title>
<script type="text/javascript">

var selectedRowIndex="";
var strTableNo="";
var arrTableNo= new Array(100);

	$(function() 
	{
		$('#tblModifyData tbody').empty()

		funFillUnsettleBillGrid();

		  var voucherNo='';
			<%if (session.getAttribute("success") != null) {
				if(session.getAttribute("voucherNo") != null){%>
				voucherNo='<%=session.getAttribute("voucherNo").toString()%>';
					<%
					session.removeAttribute("voucherNo");
				}
				boolean test = ((Boolean) session.getAttribute("success")).booleanValue();
				session.removeAttribute("success");
				if (test) {
					%>	
					funOpenBillPrint(voucherNo);
					<%
				}
			}%>
	
	});
	function funFillUnsettleBillGrid()
	{
		
		var searchUrl="";
	    var tableName="";
	   
	    
	    searchUrl=getContextPath()+"/fillUnsettleBillData.html?";
		$.ajax({
		        type: "GET",
		        url: searchUrl,
		        async:false,
		        data:"tableName="+tableName,
			    success: function(response)
			    {
			    	funAddFullRow(response.listUnsettlebill,response.gShowBillsType,response.gCMSIntegrationYN);
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
	
	function funAddFullRow(data,gShowBillsType,gCMSIntegrationYN)
	{
		
		
			var table = document.getElementById("tblModifyData");
			var rowCount = table.rows.length;
			var row = table.insertRow(rowCount);

			if(gShowBillsType=="Table Detail Wise")
            {
        	  row.insertCell(0).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"BillNo\" value=Bill No >";
        	  row.insertCell(1).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"TableName\" value=Table >";
        	  row.insertCell(2).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"WaiterName\" value=Waiter >";


                if (gCMSIntegrationYN=='Y')
                {
                	row.insertCell(3).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"Member\" value=Member >";
                }
                else
                {
                	row.insertCell(3).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"Customer\" value=Customer >";
                }
                row.insertCell(4).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"BillTime\" value=Time >";
                row.insertCell(5).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"GrandTotal\" value=Amount >";
           }
           else//Delivery Detail Wise
           {
            	  row.insertCell(0).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"BillNo\" value=Bill No >";
            	  row.insertCell(1).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"TableName\" value=Table >";
            	  
	           if (gCMSIntegrationYN=='Y')
               {
            	  row.insertCell(2).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"Member\" value=Member >";
               }
               else
               {
            	   row.insertCell(2).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"Customer\" value=Customer >";
               }
	           
	            row.insertCell(3).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"Area\" value=Area >";
	            row.insertCell(4).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"DelBoy\" value=Del Boy >";
	            row.insertCell(5).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"BillTime\" value=Time >";
	            row.insertCell(6).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"GrandTotal\" value=Amount >";
        
           }	         
			
			rowCount++;
			
		    for(var i=0;i<data.length;i++)
		    {
		    	row = table.insertRow(rowCount);
		    	var rowData=data[i];
		    	//last element of rowdata is tableNo
		    	arrTableNo[i] = rowData[rowData.length-1];

		    	
		    	for(var j=0;j<rowData.length;j++)
		    	{
		    		  if(gShowBillsType=="Table Detail Wise")
		              {
		    			  row.insertCell(j).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\""+rowData[j]+"\" value='"+rowData[j]+"' onclick=\"funGetSelectedRowIndex(this)\"/>";
				      }
		    		  else
		    		  {
		    			  row.insertCell(j).innerHTML= "<input name=\readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\""+rowData[j]+"\" value='"+rowData[j]+"' onclick=\"funGetSelectedRowIndex(this)\"/>";
		    		  }
		    	}
		    	rowCount++;		    
            }

	}
	
	function funGetSelectedRowIndex(obj)
	{
		 var index = obj.parentNode.parentNode.rowIndex;
		 var table = document.getElementById("tblModifyData");
		 $("#hidTableNo").val(arrTableNo[index-1]);
		 gTableNo=arrTableNo[index-1];
		 if((selectedRowIndex>0) && (index!=selectedRowIndex))
		 {
			 if(selectedRowIndex%2==0)
			 {
				 row = table.rows[selectedRowIndex];
				 row.style.backgroundColor='#A3D0F7';
				 selectedRowIndex=index;
				 row = table.rows[selectedRowIndex];
				 row.style.backgroundColor='#ffd966';
				 row.hilite = true;
			 }
			 else
			 {
				 row = table.rows[selectedRowIndex];
				 row.style.backgroundColor='#C0E4FF';
				 selectedRowIndex=index;
				 row = table.rows[selectedRowIndex];
				 row.style.backgroundColor='#ffd966';
				 row.hilite = true;
	         }
			
		 }
		 else
		 {
			 selectedRowIndex=index;
			 row = table.rows[selectedRowIndex];
			 row.style.backgroundColor='#ffd966';
			 row.hilite = true;
		 }
		 
		 funGetAreaCodeFromTable( gTableNo);
		 funOpenBillSettlement();
	}
	
	function  funGetAreaCodeFromTable(tableNo)
	{
		var searchUrl=getContextPath()+"/funGetAreaCodeFromTable.html?strTableNo="+tableNo;
	    $.ajax({
	        type: "GET",
	        url: searchUrl,
	        async:false,
	        dataType: "json",
		    success: function(response)
		    {
		    	gAreaCode=response.strAreaCode;
		    	$("#hidAreaCode").val(gAreaCode);
		    	
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
	
	 function funOpenBillSettlement()
	 {
		 
		 var searchUrl="";
		 var tableName = document.getElementById("tblModifyData");
	     var dataBilNo= tableName.rows[selectedRowIndex].cells[0].innerHTML; 
	     var btnBackground=dataBilNo.split('value=');
	     var billData=btnBackground[1].split("onclick");
	     var billNo=billData[0].substring(1, (billData[0].length-2));
	      
	     var dataTableNo= tableName.rows[selectedRowIndex].cells[1].innerHTML; 
	     var btnBackgroundTableNo=dataTableNo.split('value=');
	     var tableData=btnBackgroundTableNo[1].split("onclick");
	     var selectedTableName=tableData[0].substring(1, (tableData[0].length-2));
	     
	    
		 
	 	 funSetDataToSettlementWindowTab(billNo,selectedTableName);
	 	 
	 }
	 
	 
	 function funSetDataToSettlementWindowTab(billNo,selectedTableName)
	 {
		

			 /* Disable tab1 and display tab2 */
		     document.getElementById("tab1").style.display='none';
		 	 document.getElementById("tab2").style.display='block';
		 	 /* Disable tab1 and display tab2 */
			  var $rows = $('#tblSettleItemTable').empty();	
		 	 
		 	 //invisible div Extra Fileds div
			  document.getElementById("divAmt").style.display='block'; 
		 	 
			  //invisible settlement amount div
			  document.getElementById("divExtraFileds").style.display='block'; 
			  
			  //invisible div Numeric Pad amount div
			  document.getElementById("divNumericPad").style.display='block'; 
			 document.getElementById("divSettlementButtons").style.display='block';

			  
			  
		 	 
		 	
		 	 
		 	$("#billNoForDisplay").text("Bill No: "+billNo);
		 	
		 	$("#hidBillNo").val(billNo);
		 	
		 	$("#tableNameForDisplay").text("Table No: "+selectedTableName);
		 	$("#billDateForDisplay").text("Date: "+gBillDate);
		 	
		 	
		 	
		 	operationType="DineIn";
		 	transactionType="Modify Bill"; 
		 	 
		  	
		 	
		 	finalSubTotal=0.00;
		 	finalDiscountAmt=0.00;
		 	finalNetTotal=0.00;
		 	taxTotal=0.00;
		 	taxAmt=0.00;
		 	finalGrandTotal=0.00;
		 	
		 	 $("#txtDeliveryCharge").val(finalDelCharges);
		 	 
		 	 var listItmeDtl=[];	   
// 		 	 var hmItempMap=new Map();
		 	 
		 	 
		 	/*Loading bill items  */
		 	var searchurl=getContextPath()+"/funGetItemsFromBill.html?billNo="+billNo;
			$.ajax({
				type: "GET",
			     url: searchurl,				     
			     contentType: 'application/json',
			     async: false,		       
			     success: function (response)
			     {
			    	 operationType=response.operationType;
			    	 
			    	$.each(response.listOfBillItemDetails,function(i,item)
			    	{
        	    		var itemDiscAmt=0;
			 			var isModifier=false;
			 			
			 			var itemName=item.strItemName;
			 			if(itemName.startsWith("-->"))
			 			{
			 	    		isModifier=true;
			 			}
			 			
			 			
			 			var itemQty=item.dblQuantity;
			 			var itemAmt=item.dblAmount;
			 			var itemCode=item.strItemCode;
			 			var itemDiscAmt=item.dblDiscountAmt;
			 			
			 			var itemDiscPer=(itemDiscAmt/itemAmt)*100;
			 						 			
			 	 		var groupcode=item.strGroupCode;
			 	 		var subgroupcode=item.strSubGroupCode;
			 	 		var subgroupName=item.strSubGroupName;
			 	 		var groupName=item.strGroupName;
			 			var comp=item.dblComplQty;
			 	 		
			 			hmGroupMap.set(groupcode, groupName); 
			 			hmSubGroupMap.set(subgroupcode, subgroupName);
			 			
			 			
			 			hmItempMap.set(itemCode,itemName);
			 			
			 			
			 			var singleObj = {}
			 		    singleObj['itemName'] =itemName;
			 		    singleObj['quantity'] =itemQty;
			 		    singleObj['amount'] = itemAmt;
			 		    singleObj['discountPer'] = itemDiscPer;
			 		    singleObj['discountAmt'] =itemDiscAmt;
			 		    singleObj['strSubGroupCode'] =subgroupcode;
			 		    singleObj['strGroupcode'] =groupcode;
			 		    singleObj['itemCode'] =itemCode;
			 		    singleObj['rate'] =item.dblRate;
			 		    singleObj['isModifier'] =isModifier;
			 		    singleObj['dblCompQty'] =comp;
			 		    listItmeDtl.push(singleObj); 
		        	});
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
				
			
		 	
		 	/**
		 	* filling data to grid without calculating promotions and for bill print	
		 	*/
		 	 funNoPromtionCalculation(listItmeDtl)

		 	    
		 	funRefreshSettlementItemGrid();
		 	
		
	 }
	 
	
	


</script>
	</head>
	<body>

	
    
    <div id="formHeading">
		<label>Modify Bill</label>
			</div>
<br/>
<br/>

<%--     <div style=" background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 400px; margin: auto; overflow-x: scroll; overflow-y: scroll; width: 90%;">
	<table id="tblModifyData"
			style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
			class="transTablex col2-right col3-right col4-right col5-right">

	</table>
	
	<p align="center">
	
</div>
	<s:input type="hidden" path="strBillNo" id="hiddBillNo" />
	<s:input type="hidden" path="strTableNo" id="hiddTableNO" />
	<s:input type="hidden" path="selectedRow" id="hiddSelectedRow" />
	</s:form>

 --%>
 <s:form name="ModifyBill" method="GET" action="fillBillSettlementData.html?
saddr=${urlHits}"  target="_blank" class="formoid-default-skyblue" 
style="background-color:#FFFFFF;font-size:14px;font-family:'Open Sans',
'Helvetica Neue','Helvetica',Arial,Verdana,sans-serif;color:#666666;max-width:99%;
min-width:25%;">
    <div class="row" style=" background-color: #ffffff; border: 1px solid #ccc; display: block; margin: auto; width: 90%;height: 100%">
	
	<table id="tblModifyData" style="width: 100%; border: 1px solid black; table-layout: fixed; height:100%; overflow: scroll">
	
	</table>
	
	 <!-- <table id="tblData"
			style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
			class="scroll">

	</table>
 -->	
	<p align="center">
	<s:input  type="text"  id="txtTotalModifyBillAmount" path="" cssStyle="width:120px;" cssClass="longTextBox jQKeyboard form-control"  />
	</p>
</div>

	<s:input type="hidden" path="strBillNo" id="hiddBillNo" />
	<s:input type="hidden" path="strTableNo" id="hiddTableNO" />
	<s:input type="hidden" path="selectedRow" id="hiddSelectedRow" />
	</s:form>

 

	</body>
	</html>
