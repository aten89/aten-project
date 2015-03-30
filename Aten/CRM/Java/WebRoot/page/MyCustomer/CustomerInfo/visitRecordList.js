var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;

$(initPage);
function initPage(){
	//添加权限约束-问题管理
	$.handleRights({
        "visitRecord_add" : $.CrmConstants.RETURNVISIT
    });
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//新增回访记录
  	$("#visitRecord_add").click(function() {
  		initAddVisitRecord();
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
			url  : "m/customer/queryVisitRecordList",
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
	        		var page = data.visitList;
			        if( page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var id = list[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\" style=\"padding:7px 6px\">"+(list[i].returnVistUserName==null ? "":list[i].returnVistUserName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].returnVistTimeStr==null ? "":list[i].returnVistTimeStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].content==null ?"":list[i].content)+"</td>" +
		           			"<td>" + 
	           				($.hasRight($.CrmConstants.RETURNVISIT) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initModify('" + id + "')\">修改</a>&nbsp;&nbsp;<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"del('" + id + "')\">删除</a>" : "")  +
		           			"</td></tr>";
			           }
			        }
		            $("#visitRecordList").html(html);
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
	
	$.confirm("是否确认删除?删除后不可恢复!",function(result){
		if(result){
			$.ajax({
		        type : "POST",
				cache: false,
				async: true,
				url : BASE_PATH + "/m/customer/delete_visitRecord",
				dataType : "json",
				data:{
					returnVistId : id
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
 * 修改回访记录
 */
function initModify(id){
	
	var customerId = $("#customerId").val();
	mainFrame.addTab({
		id:"initModify" + id,
		title:"修改回访",
		url:BASE_PATH + "m/customer/initmodifyReturnVist?customerId=" + customerId + "&returnVistId=" + id,
		callback:queryList
	});
}

/**
 * 新增回访记录
 */
function initAddVisitRecord(){
	var id = $("#customerId").val();
	mainFrame.addTab({
		id:"initAddVisitRecord" + id,
		title:"添加回访",
		url:BASE_PATH + "m/customer/initAddReturnVist?customerId=" + id,
		callback:queryList
	});
}
