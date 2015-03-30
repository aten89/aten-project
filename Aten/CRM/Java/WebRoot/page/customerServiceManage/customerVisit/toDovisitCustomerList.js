var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;
var visitStatusSel = null;
var customerManageSel = null;
var saleGroupSel = null;

var markImgSrc = "themes/comm/images/mark.png";
var unMarkImgSrc = "themes/comm/images/unMark.png";

$(initPage);
function initPage(){
	//添加权限约束
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY
    },"hidModuleRights");
    
	initVisitStatusSel();
	initCustomerManageSel();
	initSaleGroupSel();
	
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

/**
 * 初始化下拉选择框
 */
function initSaleGroupSel() {
	if ($("#saleGroupSel").size() > 0) {
		saleGroupSel = $("#saleGroupSel").ySelect({
			width : 100,
			height: 150,
			url : BASE_PATH + "/m/group_ext/queryAllSaleGroupSel",
			afterLoad : function() {
				saleGroupSel.addOption("", "请选择...", 0);
				// 设置默认值
				saleGroupSel.select(0);
			},
			onChange : function(value, name) {
				
			}
		});
	}
}

function initCustomerManageSel(){
	customerManageSel = $("#customerManageSel").ySelect({
		width : 100,
		height: 150,
		url : BASE_PATH + "/m/user_account_ext/salesel",
		data:"jsoncallback=true",
		afterLoad : function() {
			customerManageSel.addOption("", "请选择...", 0);
//			 设置默认值
			customerManageSel.select(0);
		},
		onChange : function(value, name) {
			
		}
	});
}

function initVisitStatusSel(){
	visitStatusSel = $("#visitStatusSel").ySelect({
		name : "visitStatusSel",
		width:50
	});
}

function queryList(){
	var multipleCustomerStatus = visitStatusSel.getValue();
	var customerName = $.trim($("#customerName").val());
	var tel = $.trim($("#tel").val());
	var customerManage = customerManageSel.getValue();
	var bgnSubmitTime =$.trim($("#bgnSubmitTime").val());
	var endSubmitTime =$.trim($("#endSubmitTime").val());
	if($.compareDate(bgnSubmitTime,endSubmitTime)){
  		alert("提交开始时间不能大于结束时间");
  		return;
  	}
  	var bgnVistTime =$.trim($("#bgnVistTime").val());
	var endVistTime =$.trim($("#endVistTime").val());
	if($.compareDate(bgnVistTime,endVistTime)){
  		alert("最后回访开始时间不能大于结束时间");
  		return;
  	}
  	
	var saleGroupId = saleGroupSel.getValue();
	
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
				multipleCustomerStatus : multipleCustomerStatus,
				custName : customerName,
				customerManage : customerManage,
				tel : tel,
				bgnSubmitTime : bgnSubmitTime,
				endSubmitTime : endSubmitTime,
				bgnVistTime : bgnVistTime,
				endVistTime : endVistTime,
				saleGroupId : saleGroupId,
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
			        	   var markImg = list[i].memoMark == "1" ?markImgSrc : unMarkImgSrc;
			        		html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
			        		"<td class=\"listData\" style=\"cursor:pointer\" onclick=\"changeMark('" + id + "')\"><img id='markImg" + id + "' mark='" + list[i].memoMark + "' src='" + markImg + "'/></td>" +
		           			"<td class=\"listData\">" + (list[i].custName==null ? "" : list[i].custName) + "</td>" +
		           			"<td class=\"listData\">" + (list[i].sex==null ? "" : (list[i].sex=="M" ? "男" : "女")) + "</td>" +
		           			"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
		           			"<td class=\"listData\">" + (list[i].fullDeptName==null ? "" : list[i].fullDeptName) + "</td>" +
		           			"<td class=\"listData\">" + (list[i].saleManName==null ? "" : list[i].saleManName) + "</td>" +
		           			"<td class=\"listData\">"+(list[i].submitTimeStr==null ?"":list[i].submitTimeStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].returnVisitCount==null?"":list[i].returnVisitCount)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].lastReturnVisitTime==null?"":list[i].lastReturnVisitTime)+"</td>" +
		           			"<td class=\"listData\">"+getStatusStr (list[i].status)+"</td>" +
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

function getStatusStr(status) {
	if (!status) {
		return "";
	} else if (status == 1) {
		return "待回访";
	} else if (status == 2) {
		return "驳回";
	} else {
		return "不通过";
	}
}

function changeMark(id) {
	var obj = $("#markImg" + id);
	
	var memoMark = obj.attr("mark");
	memoMark = memoMark != "1" ? "1" : "0";
	$.ajax({
		async:false,
		type : "POST",
		cache: false,
		dataType : "json",
      	url : BASE_PATH  + "m/customer/mark",
      	data :{
      		customerId : id,
      		memoMark : memoMark
		},
      	success : function(data){
      		if ($.checkErrorMsg(data)) {
				obj.attr("mark", memoMark).attr("src", memoMark == "1" ? markImgSrc : unMarkImgSrc);
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
		url:BASE_PATH + "page/MyCustomer/CustomerInfo/customerInfoFrame.jsp?customerId=" + customerId +"&moduleKey=TO_VISIT_CUST",
		callback:queryList
	});
}
