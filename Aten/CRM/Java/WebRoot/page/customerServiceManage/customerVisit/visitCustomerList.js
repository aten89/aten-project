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
	//添加权限约束
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY
    },"hidModuleRights");
    
	//初始化表头列排序
//	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//支持回车
	$.EnterKeyPress($("#customerName"),$("#searchInfo"));
	$.EnterKeyPress($("#tel"),$("#searchInfo"));
	$.EnterKeyPress($("#email"),$("#searchInfo"));
	$.EnterKeyPress($("#recommendPro"),$("#searchInfo"));
	
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});

}

function queryList(){
//	var status = $("#status").val();
	var customerName = $.trim($("#customerName").val());
	var tel = $.trim($("#tel").val());
	var email = $.trim($("#email").val());
	var recommendPro = $.trim($("#recommendPro").val());
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
//	var sortCol = columnSortObj.getSortColumn();
//	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/customerService/queryCustomerInfoList",
			dataType : "json",
			data:{
				status : 5,
				custName : customerName,
				tel : tel,
				email : email,
				recommendProduct : recommendPro,
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
			        	   var id = list[i].id;
			        		html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\">" + (list[i].custName==null ? "" : list[i].custName) + "</td>" +
		           			"<td class=\"listData\">" + (list[i].sex==null ? "" : (list[i].sex=="M" ? "男" : "女")) + "</td>" +
		           			"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].email==null?"":list[i].email)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].recommendProduct==null?"":list[i].recommendProduct)+"</td>" +
		           			"<td>" + 
	           				($.hasRight($.SysConstants.VIEW) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"openCustomerInfo('" + id + "')\">查看</a>" : "") +"&nbsp;&nbsp;"+
		           			($.hasRight($.CrmConstants.RETURNVISIT) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"returnVisit('" + id + "')\">回访</a>" : "") +
		           			"</td></tr>";
			           }
			        }
		            $("#waitVisitCustomerList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

//客服回访
function returnVisit(id){
	mainFrame.addTab({
		id:"addReturnVisit" + id,
		title:"添加回访",
		url:BASE_PATH + "m/customer/initAddReturnVist?customerId=" + id,
		callback:queryList
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
		url:BASE_PATH + "page/MyCustomer/CustomerInfo/customerInfoFrame.jsp?customerId=" + customerId +"&moduleKey=V_FORMAL_CUST",
		callback:queryList
	});
}
