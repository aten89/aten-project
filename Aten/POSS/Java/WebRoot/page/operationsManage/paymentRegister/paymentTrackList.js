var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;

var payTypeSel = null;
var prodInfoSel = null;
var customerInfoSel = null;

$(initPage);
function initPage(){
	
	//添加权限约束
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY //查询
    },"hidModuleRights");
	
	initPayTypeSel();
	initProInfoSel();
//	initCustomerInfoSel();
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
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
	
	var standardFlag = $("#standardFlag").val(); 
	
	var bgnTransferDateTime =$.trim($("#bgnTransferDateTime").val());
	var endTransferDateTime = $.trim($("#endTransferDateTime").val());
	
	var customerId = "";
	var custName = $("#custName").val();
	var oriSelected = $("#oriSelected").val();
	if(custName == oriSelected){
		customerId = $("#custId").val();
	}
	
	var prodId = prodInfoSel.getValue();
	var payType = payTypeSel.getValue();

	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/operationsManage/queryTrackCustPaymentList",
			dataType : "json",
			data:{
				flag : standardFlag,
				bgnTransferDate : bgnTransferDateTime,
				endTransferDate : endTransferDateTime,
				custId : customerId,
				custName : custName,
				prodId : prodId,
				payType : payType,
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
			        	   var id = list[i].id;
			        		html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
			        		"<td class=\"listData\">" + (list[i].cusManageName==null ? "" : list[i].cusManageName) + "</td>" +
		           			"<td class=\"listData\">" + (list[i].transferDate==null ? "" : list[i].transferDate) + "</td>" +
		           			"<td class=\"listData\">"+(list[i].custName==null ?"":list[i].custName)+"</td>" + 
		           			"<td class=\"listData\">"+(list[i].custProperty==null?"":(list[i].custProperty=="0" ? "个人":"机构"))+"</td>" +
		           			"<td class=\"listData\">"+(list[i].proName==null?"":list[i].proName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].appointmentAmount==null ?"":list[i].appointmentAmount)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].payTypeName==null?"":list[i].payTypeName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].task.nodeName==null?"":list[i].task.nodeName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].task.transactorDisplayName==null?"":list[i].task.transactorDisplayName)+"</td>" +
		           			"<td class=\"listData\">" +
//			        		"<input type=\"image\" class=\"openViewImg\" src=\"themes/comm/images/crmView_ico.gif\" title=\"查看\" onclick=\"initViewPayment('" + id + "')\" />" + 
			        			($.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initViewPayment('" + id + "')\">查看</a> " : "") + 
//			        			"<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initViewPayment('" + id + "')\">查看</a> " + 
			        		"</td>";
			        		html += "</tr>";
			           }
			        }
		            $("#paymentArchList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

/**
 * 查看详情
 * @param id
 */
function initViewPayment(id){
	mainFrame.addTab({
		id:"view_payment" + id,
		title:"划款详情",
		url:BASE_PATH + "m/operationsManage/paymentApprove?id=" + id + "&flag=viewInfo",
		callback:queryList
	});
}

function initPayTypeSel(){
	payTypeSel = $("#payTypeSel").ySelect({
		name : "payTypeSel",
		width:107,
		popwidth:140
	});
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


/**
 * 初始化客户下拉选择框
 */
function initCustomerInfoSel(){

	var html=" <div>**请选择...</div> ";
	var url = CRM_PATH + "m/customer/initCustomerInfoSel?format=json&jsoncallback=?";
	$.ajax({
        type : "POST",
		cache: false,
		async: false,
		url  : url,
		dataType : "html",
	    success : function(data){
	   		html+=data;
	   		$("#customerInfoSel").html(html);
	   		customerInfoSel = $("#customerInfoSel").ySelect({
				width:107,
				popwidth:140,
				afterLoad : function() {

				},
				onChange : function(value, name) {
					
				}
			});
  	}});
}
