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
	$.handleRights({
        "addRefuncNotice" : $.SysConstants.ADD
    });
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//支持回车
	$.EnterKeyPress($("#trustCompany"), $("#searchInfo"));
	
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	//新建
	$("#addRefuncNotice").click(function(){
		addRefuncNotice();
	});

}

function queryList(){
	var createTimeBegin = $.trim($("#createTimeBegin").val());
	var createTimeEnd = $.trim($("#createTimeEnd").val());
	var trustCompany = $.trim($("#trustCompany").val());
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/operationsManage/queryRefuncNoticeListPage",
			dataType : "json",
			data:{
				pageNo : pageno,
				pageSize : pagecount,
				sortCol : sortCol,
				ascend : ascend,
				createTimeBegin : createTimeBegin,
				createTimeEnd : createTimeEnd,
				trustCompany : trustCompany
			},
			success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		var page = data.listPage;
			        if(page != null && page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
			        		html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\">" + (list[i].trustCompany==null ? "" : list[i].trustCompany) + "</td>" +
		           			"<td class=\"listData\">" + (list[i].refundNotice==null ? "" : list[i].refundNotice) + "</td>" +
		           			"<td class=\"listData\"><img src=\"themes/comm/images/attachment.gif\"/>(" + (list[i].attachmentCount==null ? "" : list[i].attachmentCount) + ")个文件</td>" +
		           			"<td class=\"listData\">"+(list[i].linkmanName==null?"":list[i].linkmanName)+"</td>" +
//		           			"<td class=\"listData\">"+(list[i].createTimeStr==null ?"":list[i].createTimeStr)+"</td>" +
			        		"<td class=\"listData\">" +
			        			($.hasRight($.SysConstants.VIEW) ? "<a href=\"javascript:void();\" onclick=\"viewRefuncNotice('" + (list[i].id==null?"":list[i].id) + "');\">查看</a>&nbsp;&nbsp;" : "") +
			        			($.hasRight($.SysConstants.MODIFY) ? "<a href=\"javascript:void();\" onclick=\"modifyRefuncNotice('" + (list[i].id==null?"":list[i].id) + "');\">修改</a>&nbsp;&nbsp;" : "") +
			        			($.hasRight($.SysConstants.DELETE) ? "<a href=\"javascript:void();\" onclick=\"delRefuncNotice('" + (list[i].id==null?"":list[i].id) + "');\">删除</a>" : "") +
			        		"</td>";
			        		html += "</tr>";
			           }
			        }
		            $("#paymentToDoList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

//关闭窗口
function closeWin(){
	if (param.returnValue){
		dialogWin.close();
		$.alert("操作成功",function(){
			queryList();
		});
	}else{
		dialogWin.close();
	}
}

//新建退款须知
function addRefuncNotice(){
	param = new Object();
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "";
	var url = BASE_PATH + "page/operationsManage/refuncApplication/editRefuncNotice.jsp";
	var width = 800;
	var height = 420;
	dialogWin = $.showModalDialog(title, url, width, height);
}

//修改退款须知
function modifyRefuncNotice(id) {
	param = new Object();
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "";
	var url = BASE_PATH + "m/operationsManage/initEditRefuncNotice?id=" + id;
	var width = 800;
	var height = 420;
	dialogWin = $.showModalDialog(title, url, width, height);
}

//修改退款须知
function viewRefuncNotice(id) {
	param = new Object();
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "";
	var url = BASE_PATH + "m/operationsManage/initViewRefuncNotice?id=" + id;
	var width = 800;
	var height = 420;
	dialogWin = $.showModalDialog(title, url, width, height);
}

//删除退款须知
function delRefuncNotice(id) {
	$.confirm("是否确认删除?删除后不可恢复!",function(result){
		if(result){
			$.ajax({
		        type : "POST",
				cache: false,
				url  : "m/operationsManage/delRefuncNotice",
				dataType : "json",
				data:{
					id : id
				},
				success : function(data,i){
					if ($.checkErrorMsg(data)) {
						$.alert("操作成功",function(){
							queryList();
						});
					} else {
						$.alert("操作失败",function(){
							queryList();
						});
					}
				},
		        error : $.ermpAjaxError
			});
		}
	});
}
