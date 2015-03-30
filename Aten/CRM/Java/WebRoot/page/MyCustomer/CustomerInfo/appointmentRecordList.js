var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;

var dialogWin = null;
var param = null;

$(initPage);
function initPage(){
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//新增预约
  	$("#appointmentRecord_add").click(function() {
  		initAddAppointmentRecord();
  	});

}

function queryList(){
	var customerId = $("#customerId").val();
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/customer/queryAppointmentRecordList",
			dataType : "json",
			data:{
				customerId : customerId,
				pageNo : pageno,
				pageSize : pagecount,
				sortCol : sortCol,
				ascend : ascend
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		var page = data.appointmentList;
			        if(page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var id = list[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\" style=\"padding:7px 6px\">"+(list[i].createorName==null ? "":list[i].createorName)+"</td>" +
//		           			"<td class=\"listData\">"+(list[i].appointmentTime==null ?"":list[i].appointmentTime)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].appointmentTimeStr==null ?"":list[i].appointmentTimeStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].warnOpportunity==null ?"":list[i].warnOpportunity)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].appointmentTypeStr==null ?"":list[i].appointmentTypeStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].remark==null ?"":list[i].remark)+"</td>" +
		           			"<td class=\"oprateImg\">" +    
			           			"<input type=\"image\" src=\"themes/comm/images/crmEdit_ico.gif\" title=\"修改\" onclick=\"initModify('" + id + "');\" />" + 
			           			"<input type=\"image\" src=\"themes/comm/images/crmDel_ico.gif\" title=\"删除\" onclick=\"del('" + id + "')\" />" +
			  				"</td>";
		           			"</tr>";
			           }
			        }
		            $("#appointmentRecordList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

/**
 * 删除
 * @param id
 */
function del(id) {
	if(!id){
		alert( "请选择要删除的记录" );
		return;
	}
	if(!window.confirm( "是否确认删除?删除后不可恢复!" ) ){
		return;
	}
	
	$.ajax({
        type : "POST",
		cache: false,
		async: true,
		url : BASE_PATH + "/m/customer/delete_appointmentRecord",
		dataType : "json",
		data:{
			customerAppointmentId : id
		},
        success : function(data){
        	if ($.checkErrorMsg(data)) {
				queryList();
	    	}
        },
        error : $.ermpAjaxError
    });
}

/**
 * 新增预约记录
 */
function initAddAppointmentRecord(){
//	var customerId = $("#customerId").val();
//	var params = new Object();
//	var sUrl = BASE_PATH + "page/MyCustomer/CustomerInfo/editAppointmentRecord.jsp?customerId=" + customerId;
//	var sFeature = "dialogHeight:220px;dialogWidth:610px;status:no;scroll:auto;help:no";
//	var returnValue = window.showModalDialog(sUrl, params, sFeature);
//	if(returnValue){
//		queryList();
//	}
	
	
	var customerId = $("#customerId").val();
	param = new Object();
	param.callback = function(){
		if (param.returnValue){
			$.alert("操作成功",function(){
				dialogWin.close();
				queryList();
			});
		}else{
			dialogWin.close();
			queryList();
		}
	};
	
	window.dialogParam = param;
	var title = "增加预约";
	var url = BASE_PATH + "page/MyCustomer/CustomerInfo/editAppointmentRecord.jsp?customerId=" + customerId;
	var width = 410;
	var height = 200;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

/**
 * 修改预约记录
 */
function initModify(id){
//	var params = new Object();
//	var sUrl = BASE_PATH + "/m/customer/initModifyAppointmentRecord?customerAppointmentId=" + id;
//	var sFeature = "dialogHeight:220px;dialogWidth:500px;status:no;scroll:auto;help:no";
//	var returnValue = window.showModalDialog(sUrl,params,sFeature);
//	if(returnValue){
//		queryList();
//	}
	
	param = new Object();
	param.callback = function(){
		if (param.returnValue){
			$.alert("操作成功",function(){
				dialogWin.close();
				queryList();
			});
		}else{
			dialogWin.close();
			queryList();
		}
	};
	
	window.dialogParam = param;
	var title = "修改预约";
	var url = BASE_PATH + "/m/customer/initModifyAppointmentRecord?customerAppointmentId=" + id;
	var width = 410;
	var height = 200;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}
