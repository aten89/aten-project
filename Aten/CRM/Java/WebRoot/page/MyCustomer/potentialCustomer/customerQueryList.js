var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;
/**
 * 推荐产品下拉框组件
 */
var recommendProductSel;

$(initPage);

/**
 * 页面初始化
 */
function initPage(){
	//添加权限约束
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY
    },"hidModuleRights");
	
	//初始化表头列排序
//	columnSortObj = $("#tableTH").columnSort();
	
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
	//状态
	var status = $.trim($("#status").val());
	//推荐产品
//	var recommendProduct = recommendProductSel.getValue();
//	var recommendProduct = $.trim($("#recommendProduct").val());
	var bgnSubmitTime =$.trim($("#bgnSubmitTime").val());
	var endSubmitTime =$.trim($("#endSubmitTime").val());
	if($.compareDate(bgnSubmitTime,endSubmitTime)){
  		alert("开始时间不能大于结束时间");
  		return;
  	}
  	
	//客户名称
	var custName = $.trim($("#custName").val());
	//电话
	var tel = $.trim($("#tel").val());
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
//	var sortCol = columnSortObj.getSortColumn();
//	var ascend =  columnSortObj.getAscend();
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/customer/queryCustomerInfoList",
		dataType : "json",
		data:{
			batchNumber : pageno,
			custName : custName,
			tel : tel,
//			recommendProduct : recommendProduct,
			bgnSubmitTime : bgnSubmitTime,
			endSubmitTime : endSubmitTime,
			customerStatus : status,
			pageNo : pageno,
			pageSize : pagecount,
			sortCol : "submitTime",
			ascend : "false"
		},
        success : function(data,i){
           	var html="";
        	if ($.checkErrorMsg(data)) {
        		var page = data.listPage;
		        if(page != null && page.dataList!=null){
		           var list = page.dataList;
		           for(var i =0; i<list.length; i++){
			           	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
	           			"<td class=\"listData\">" + (list[i].custName==null ? "" : list[i].custName) + "</td>" +
	           			"<td class=\"listData\">" + (list[i].sex==null ? "" : (list[i].sex=="M" ? "男" : "女")) + "</td>" +
	           			"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].createTimeStr==null ?"":list[i].createTimeStr)+"</td>" +
		           		"<td class=\"listData\">"+(list[i].submitTimeStr==null ?"":list[i].submitTimeStr)+"</td>" +
		           		"<td class=\"listData\">"+(list[i].returnVisitCount==null?"":list[i].returnVisitCount)+"</td>" +
	           			"<td>" + 
	           				($.hasRight($.SysConstants.VIEW) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"openCustomerInfo('" + list[i].id + "') \">查看</a>" : "") +"&nbsp;&nbsp;"+
		           			($.hasRight($.SysConstants.DELETE) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"delCustomerInfo('" + list[i].id + "') \">删除</a>" : "") +
		           		"</td></tr>";
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
	var status = $("#status").val();
	var moduleKey = status == "3" ?"S_POTENTIAL_CUST":"APPROVE_CUST";
	mainFrame.addTab({
		id:"viewCustomerInfo" + customerId,
		title:"客户资料",
		url:BASE_PATH + "page/MyCustomer/CustomerInfo/customerInfoFrame.jsp?customerId=" + customerId +"&moduleKey=" +moduleKey,
		callback:queryList
	});
}

/**
 * 删除客户详情
 * @param customerId
 */
function delCustomerInfo(customerId) {
	$.confirm("是否确认删除?删除后不可恢复!",function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				cache : false,
				url : BASE_PATH + "m/customer/delete",
				data : {
					customerId : customerId
				},
				dataType : "json",
				success : function(data) {
					if($.checkErrorMsg(data)) {
						$.alert("操作成功!",function(){
		    				queryList();
		    			});
					}
				},
		        error : $.ermpAjaxError
			});
		}
	});
}

/**
 * 初始化推荐产品下拉框
 */
function initRecommendProductSel() {
	recommendProductSel = $("#recommendProductSel").ySelect({
		width : 80,
		height: 150,
		url : BASE_PATH + "/l/dict/initProductSel"
	});
}