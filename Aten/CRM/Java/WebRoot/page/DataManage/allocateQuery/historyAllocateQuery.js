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

/**
 * 页面初始化
 */
function initPage(){
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY
    });
    
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	 //刷新
    $("#refresh").click(function(){
    	//清空
    	$("#custList").empty();
		queryList();//搜索
	});   
}

/**
 * 列表查询
 */
function queryList(){
	var allocateTimeBegin = $.trim($("#allocateTimeBegin").val());
	var allocateTimeEnd = $.trim($("#allocateTimeEnd").val());
	var batchNumber = $.trim($("#batchNumber").val());
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/importCustomer/query_customerList",
		dataType : "json",
		data:{
			allocateTimeBegin : allocateTimeBegin,
			allocateTimeEnd : allocateTimeEnd,
			batchNumber : batchNumber,
			allocateFlag : true,
			custName : pagecount,
			pageNo : pageno,
			pageSize : pagecount,
			sortCol : sortCol,
			ascend : ascend
		},
        success : function(data,i){
           	var html="";
        	if ($.checkErrorMsg(data)) {
        		var page = data.listPage;
		        if(page != null && page.dataList!=null){
		           var list = page.dataList;
		           for(var i =0; i<list.length; i++){
			           	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
	           			"<td class=\"listData\">" + (list[i].batchNumber==null ? "" : list[i].batchNumber) + "</td>" +
	           			"<td class=\"listData\">" + (list[i].custName==null ? "" : list[i].custName) + "</td>" +
	           			"<td class=\"listData\">" + (list[i].sex==null ? "" : (list[i].sex=="M" ? "男" : "女")) + "</td>" +
	           			"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].email==null?"":list[i].email)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].allocateTimeStr==null?"":list[i].allocateTimeStr)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].allocateToName==null?"":list[i].allocateToName)+"</td>" +
	           			"<td class=\"listData\"><a href=\"javascript:void();\" onclick=\"openCustomerInfo('" + (list[i].relateCustid==null?"":list[i].relateCustid) + "');\">查看</a></td>" +
	           			"</tr>";
		           }
		        }
	            $("#custList").html(html);
	            trunPageObj.setPageData(page);
        	}
        },
        error : $.ermpAjaxError
    });
}

/**
 * 客户详情
 * @param customerId 客户ID
 */
function openCustomerInfo(customerId) {
	mainFrame.addTab({
		id:"viewCustomerInfo" + customerId,
		title:"客户资料",
		url:BASE_PATH + "page/MyCustomer/CustomerInfo/customerInfoFrame.jsp?customerId=" + customerId,
		callback:queryList
	});
}
