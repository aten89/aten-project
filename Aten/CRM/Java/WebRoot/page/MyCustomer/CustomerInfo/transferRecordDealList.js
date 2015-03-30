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
			url  : BASE_PATH + "m/customer/queryTransactionRecordPage",
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
	        		var page = data.transactionRecordListPage;
			        if(page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var id = list[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\" style=\"padding:7px 6px\">"+(list[i].prodName==null ? "":list[i].prodName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].pamentMoneySum==null ?"":list[i].pamentMoneySum)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].sellTimeLimit==null ?"":list[i].sellTimeLimit)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].transactionDateStr==null ?"":list[i].transactionDateStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].prodSetUpDateStr==null ?"":list[i].prodSetUpDateStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].actualCashDateStr==null ?"":list[i].actualCashDateStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].sellTimeLimit==null ?"":list[i].sellTimeLimit)+"</td>" +
		           			"</tr>";
			           }
			        }
		            $("#transferRecordList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        }
	    });
}