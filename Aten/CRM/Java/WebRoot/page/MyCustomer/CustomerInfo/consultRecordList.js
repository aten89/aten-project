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
	//添加权限约束-问题管理
	$.handleRights({
        "consultRecord_add" : $.CrmConstants.REQUEST
    });
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//新增咨询记录
  	$("#consultRecord_add").click(function() {
  		initAddConsultRecord();
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
			url  : "m/customer/queryConsultRecordList",
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
	        		var page = data.consultList;
			        if( page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var id = list[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\" style=\"padding:7px 6px\">"+(list[i].createorName==null ? "":list[i].createorName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].consultTimeStr==null ? "":list[i].consultTimeStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].content==null ?"":list[i].content)+"</td>" +
		           			"<td>" + 
	           				($.hasRight($.CrmConstants.REQUEST) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initModify('" + id + "','"+ encodeURI(list[i].content) +"')\">修改</a>&nbsp;&nbsp;<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"del('" + id + "')\">删除</a>" : "")  +
		           			"</td></tr>";
			           }
			        }
		            $("#consultRecordList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}


function del(id){
	if(!id){
		alert( "请选择要删除的记录" );
		return;
	}
	
	$.confirm("是否确认删除?删除后不可恢复!",function(result){
		if(result){
			$.ajax({
		        type : "POST",
				cache: false,
				async: true,
				url : BASE_PATH + "/m/customer/delete_consultRecord",
				dataType : "json",
				data:{
					customerConsultId : id
				},
		        success : function(data){
		        	if ($.checkErrorMsg(data)) {
						queryList();
			    	}
		        },
		        error : $.ermpAjaxError
		    });
		}
	});
}


/**
 * 删除
 * @param id
 */
function del1(id) {
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
		url : BASE_PATH + "/m/customer/delete_consultRecord",
		dataType : "json",
		data:{
			customerConsultId : id
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
 * 新增咨询记录
 */
function initAddConsultRecord(){
	
	var customerId = $("#customerId").val();
	//参数
	param = new Object();
	param.customerId = customerId;
	param.callback = closeWin;
	window.dialogParam = param;
	
	var title = "新增咨询记录";
	var url = BASE_PATH + "page/MyCustomer/CustomerInfo/addConsultRecord.jsp";
	var width = 585;
	var height = 280;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

function closeWin(){
	
	if (param.returnValue){
		$.alert("操作成功",function(){
			dialogWin.close();
			queryList();
		});
	}else{
		dialogWin.close();
	}
}

/**
 * 修改咨询记录
 */
function initModify(id, content){
	var customerId = $("#customerId").val();
	//参数
	param = new Object();
	param.id = id;
	param.content = content;
	param.customerId = customerId;
	param.callback = closeWin;
	window.dialogParam = param;
	
	var title = "修改咨询记录";
	var url = BASE_PATH + "page/MyCustomer/CustomerInfo/addConsultRecord.jsp";
	var width = 650;
	var height = 280;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}
