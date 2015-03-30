var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;

var prodInfoSel = null;

var dialogWin = null;
var param = null;

$(initPage);
function initPage(){
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY
    });
	
  	// 初始化产品下拉框
  	initProInfoSel();
  	// 客户经理弹出框
    $('#openDispacher').click(
	   	function(e){
	   		dialog = new UserDialog(ERMP_PATH, BASE_PATH);
	   		dialog.setCallbackFun(callbackFun);
	   		var user = dialog.openDialog();
		}
	);
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
}

function callbackFun() {
	var selected = dialog.selected;
		if (selected!=null){
			$("#saleManId").val(selected[0].id);
			$("#saleManDis").val(selected[0].name);
		}
}

/**
 * 初始化产品下拉选择框
 */
function initProInfoSel(){
	var html=" <div>**请选择...</div> ";
	var url = BASE_PATH + "m/prod_info/initProdInfoSel";
	$.ajax({
        type : "POST",
		cache: false,
		async: false,
		url  : url,
		dataType : "html",
	    success : function(data){
	   		html+=data;
	   		$("#prodInfoSel").html(html);
	   		prodInfoSel = $("#prodInfoSel").ySelect({
				width:255,
				popwidth:255,
				afterLoad : function() {

				},
				onChange : function(value, name) {
					
				}
			});
  	}});
}


function queryList(){
	var applyTimeBegin = $("#applyTimeBegin").val();
	var applyTimeEnd = $.trim($("#applyTimeEnd").val());
	var custId = $.trim($("#custId").val());
	var saleManId = $.trim($("#saleManId").val());
	var prodId = prodInfoSel.getValue();
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/operationsManage/queryCustRefundArchiveList",
			dataType : "json",
			data:{
				pageNo : pageno,
				pageSize : pagecount,
				sortCol : sortCol,
				ascend : ascend,
				applyTimeBegin : applyTimeBegin,
				applyTimeEnd : applyTimeEnd,
				custId : custId,
				saleManId : saleManId,
				prodId : prodId
			},
			success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		var page = data.listPage;
			        if(page != null && page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
			        	   var id = list[i].id;
			        		html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
			        		"<td class=\"listData\">" + (list[i].proposerName==null ? "" : list[i].proposerName) + "</td>" +
		           			"<td class=\"listData\">" + (list[i].applyTime==null ? "" : list[i].applyTime) + "</td>" +
		           			"<td class=\"listData\">"+(list[i].custName==null ?"":list[i].custName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].prodName==null?"":list[i].prodName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].fullRefundFlag==null?"":(list[i].fullRefundFlag == true ? "是" : "否"))+"</td>" +
		           			"<td class=\"listData\">"+(list[i].refundAmount==null ?"":list[i].refundAmount)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].formStatus==2 ? "归档":"作废")+"</td>" +
		           			"<td class=\"listData\">" +
			        		($.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"viewCustRefund('" + id + "')\">查看</a> " : "") + 
			        		"</td></tr>";
			           }
			        }
		            $("#refuncApplicationArchList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

//详情页面
function viewCustRefund(id) {
	mainFrame.addTab({
		id:"viewCustRefund",
		title:"退款申请详情",
		url:BASE_PATH + "m/operationsManage/custRefundApprove?flag=viewDetail&id=" + id,
		callback:queryList
	});
}