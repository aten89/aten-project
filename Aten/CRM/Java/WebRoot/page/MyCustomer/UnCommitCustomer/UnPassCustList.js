/**********************
我的客户---未提交客户
*********************/ 
/**
 * 主框架
 */
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
 * 状态下拉框
 */
var dataSourceSel;
/**
 * 产品下拉选择框
 */
var recommendProdSel;
/**
 * 对话框对象
 */
var dialogWin;
/**
 * 对话框参数
 */
var param;

$(initUnCommitCustList);
function initUnCommitCustList(){
	
	//添加权限约束
	$.handleRights({
        "lowSearch" : $.SysConstants.QUERY, //查询
        "import" : $.CrmConstants.IMPORT,
        "toAdd" : $.SysConstants.ADD
    },"hidModuleRights");
	
	//初始化表头列排序
//	columnSortObj = $("#tableTH").columnSort();
	
	//进入页面先进行一次搜索
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);
	
  	// 放最后初始化
	// 初始化状态下拉框
	dataSourceSel = $("#dataSourceSel").ySelect({width : 60});
//  	initRecommendProdSel();
  	$.EnterKeyPress($("#custName"),$("#lowSearch"));
	$.EnterKeyPress($("#tel"),$("#lowSearch"));
  	
  	$("#lowSearch").click(function(){
		 trunPageObj.gotoPage(1);
	});
  	
  	$("#toAdd").click(function() {
  		addCustomer();
  	});
  	
  	$("#import").click(function() {
  		initImportPage();
  	});
};

function queryList(){
	//
	if($.validInput("tel", "电话", false, "\<\>\'\"", 50)){
  		return;
  	}
	$("#unCommitCustList").html("");
	var custName = $.trim($("#custName").val());
	var tel = $.trim($("#tel").val());
	var dataSource = "";
	if (dataSourceSel != null) {
		dataSource = dataSourceSel.getValue();
	}
//	var recommendProd = "";
//	recommendProd = $("#recommendProd").val();
//	if (recommendProdSel != null) {
//		recommendProd = recommendProdSel.getValue();
//	}
	var bgnSubmitTime =$.trim($("#bgnSubmitTime").val());
	var endSubmitTime =$.trim($("#endSubmitTime").val());
	if($.compareDate(bgnSubmitTime,endSubmitTime)){
  		alert("开始时间不能大于结束时间");
  		return;
  	}
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
//	var sortCol = columnSortObj.getSortColumn();
//	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/customer/query_unCommitCustList",
			dataType : "json",
			data:{
				multipleCustomerStatus : "-1",
				custName : custName,
				tel : tel,
				dataSource : dataSource,
//				recommendProd : recommendProd,
				bgnSubmitTime : bgnSubmitTime,
				endSubmitTime : endSubmitTime,
				pageNo : pageno,
				pageSize : pagecount,
				sortCol : "submitTime",
				ascend : "false"
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		var page = data.listPage;
			        if( page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var id = list[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\" style=\"padding:7px 6px\">"+(list[i].custName==null ? "":list[i].custName)+"</td>" +
		           			"<td class=\"listData\">" + (list[i].sex==null ? "" : (list[i].sex=="M" ? "男" : "女")) + "</td>" +
		           			"<td class=\"listData\">"+(list[i].tel==null ? "":list[i].tel)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].createTimeStr==null ?"":list[i].createTimeStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].submitTimeStr==null ?"":list[i].submitTimeStr)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].returnVisitCount==null?"":list[i].returnVisitCount)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].dataSource=="company_allot"?"导入分配" : "自己录入")+"</td>" +
		           			"<td>" + 
		           			($.hasRight($.SysConstants.VIEW) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "') \">查看</a>" : "") +"&nbsp;&nbsp;"+
		           			($.hasRight($.SysConstants.DELETE) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"del('" + id + "') \">删除</a>" : "") +
			  				"</td></tr>";
			           }
			        }
		            $("#unCommitCustList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

/**
 * 打开查看
 * @param id
 */
function initView(id) {
	mainFrame.addTab({
		id:"viewCustomerInfo"+id,
		title:"客户资料",
		url:BASE_PATH + "page/MyCustomer/CustomerInfo/customerInfoFrame.jsp?customerId=" + id +"&moduleKey=UNPASS_CUST",
		callback:queryList
	});
}

/**
 * 删除
 * @param id
 */
function del(id) {
	if(!id){
		$.alert("请选择要删除的记录");
		return;
	}
	$.confirm("是否确认删除?删除后不可恢复!",function(result) {
		if (result) {
			$.ajax({
		        type : "POST",
				cache: false,
				async: true,
				url : BASE_PATH + "/m/customer/delete",
				dataType : "json",
				data:{
					customerId : id
				},
		        success : function(data){
		        	if ($.checkErrorMsg(data)) {
		    			$.alert("操作成功!",function(){
		    				queryList();
		    			});
			    	}
		        },
		        error : $.ermpAjaxError
		    });
		}
	} );
}

/**
 * 初始化"推荐产品"下拉框
 */
function initRecommendProdSel(){
	recommendProdSel = $("#recommendProdSel").ySelect({
		width : 80,
		height: 150,
		url : BATH_PATH + "/l/dict/initRecommendProdSel",
		afterLoad : function() {
			recommendProdSel.addOption("", "请选择...", 0);
			recommendProdSel.select(0);
		},
		onChange : function(value, name) {
			//NULL
		}
	});
}

/**
 * 新增客户
 */
function addCustomer(){
	param = new Object();
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "";
	var url = BASE_PATH + "/m/customer/initAdd?customerManage=currentUser";
	var width = 800;
	var height = 480;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

/**
 * 关闭对话框
 */
function closeWin(){
	if (param.returnValue){
		$.alert("操作成功",function(){
			dialogWin.close();
			queryList();
		});
	}else{
		dialogWin.close();
		queryList();
	}
}

/**
 * 导入
 */
function initImportPage() {
	mainFrame.addTab({
		id:"custImportPageId",
		title:"客户导入",
		url: BASE_PATH + "page/DataManage/ImportAllocate/CustomerImport.jsp?allocateFlag=true",
		callback:queryList
	});
}

