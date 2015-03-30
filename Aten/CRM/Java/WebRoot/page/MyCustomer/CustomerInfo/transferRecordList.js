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
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
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
			url  : BASE_PATH + "m/customer/queryPaymentRecordPage",
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
	        		var page = data.paymentRecordListPage;
			        if(page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var id = list[i].paymentId;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\" style=\"padding:7px 6px\">"+list[i].transferDateStr+"</td>" +
		           			"<td class=\"listData\">"+(list[i].prodName==null ? "":list[i].prodName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].transferAmount==null ? "" : list[i].transferAmount)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].totalRefundAmount > 0 ? "是":"否")+"</td>" +
		           			"<td class=\"listData\">"+(list[i].totalRefundAmount > 0 ? list[i].totalRefundAmount : "")+"</td>" +
		           			"<td class=\"listData\">"+(list[i].remark==null ?"":list[i].remark)+"</td>" +
		           			"<td class=\"oprateImg\"><input type=\"image\" src=\"themes/comm/images/crmView_ico.gif\" title=\"查看\" onclick=\"initView('" + id + "');\" /></td>";
		           			"</tr>";
			           }
			        }
		            $("#transferRecordList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        }
	    });
}


/**
 * 查看预约记录 
 */
function initView(transferRecordId){
	mainFrame.addTab({
		id:"view_payment" + transferRecordId,
		title:"划款详情",
		url:POSS_PATH + "m/operationsManage/paymentApprove?id=" + transferRecordId + "&flag=viewInfo"
	});
}
