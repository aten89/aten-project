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
 * 分配对话框
 */
var dialogWin;
/**
 * 对话框参数
 */
var param;

$(initPage);
function initPage(){
	
	//添加权限约束
	$.handleRights({
		"searchInfo" : $.SysConstants.QUERY,
        "import_customer" : $.CrmConstants.IMPORT,
        "manualAllot" : $.CrmConstants.ALLOT,
        "autoAllot" : $.CrmConstants.ALLOT
    },"hidModuleRights");
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//支持回车
	$.EnterKeyPress($("#batchNumber"),$("#searchInfo"));
	$.EnterKeyPress($("#customerName"),$("#searchInfo"));
	$.EnterKeyPress($("#tel"),$("#searchInfo"));
	$.EnterKeyPress($("#email"),$("#searchInfo"));
	
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	$("#import_customer").click(function() {
  		initCustomerImport();
  	});
	
	 //刷新
    $("#refresh").click(function(){
    	//清空
    	$("#importCust").empty();
		queryList();//搜索
	});   
	
	$("#AllSelectState").click(function(){
		$("input[name='_doc_chk']", "#importCust").attr("checked", this.checked);
	});
	
	// 手动分配
	$("#manualAllot").click(function() {
		manualAllotImportCustomer();
	});
	
	// 自动分配
	$("#autoAllot").click(function() {
		autoAllotImportCustomer();
	});
}

function queryList(batchNumber){
	if(!batchNumber){
		batchNumber = $.trim($("#batchNumber").val());
	}
	var customerName = $.trim($("#customerName").val());
	var tel = $.trim($("#tel").val());
	var email = $.trim($("#email").val());
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
				batchNumber : batchNumber,
				customerName : customerName,
				tel : tel,
				email : email,
				allocateFlag : false,
				pageNo : pageno,
				pageSize : pagecount,
				sortCol : sortCol,
				ascend : ascend
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
				           	"<td class=\"listCBoxW\"><input name=\"_doc_chk\" type=\"checkbox\" value=\"" + id + "\" class=\"cBox\"/></td>" +
		           			"<td class=\"listData\" style=\"padding:7px 6px\">"+(list[i].batchNumber==null ? "":list[i].batchNumber)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].custName==null ? "":list[i].custName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].sex==null ? "":(list[i].sex=="M" ? "男":"女"))+"</td>" +
		           			"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].email==null?"":list[i].email)+"</td>" +
		           			"<td>" + 
							   ($.hasRight($.SysConstants.DELETE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"del('" + id + "')\">删除</a> " : "") +
			  				"</td></tr>";
			           }
			        }
		            $("#importCust").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

/**
 * 删除
 * @param id
 */
function del(id) {
	if(!id){
		alert( "请选择要删除的记录" );
		return;
	}
	
	$.confirm("是否确认删除?删除后不可恢复!",function(result){
		if(result){
			$.ajax({
		        type : "POST",
				cache: false,
				async: true,
				url : BASE_PATH + "/m/importCustomer/delete_importCustomer",
				dataType : "json",
				data:{
					id : id
				},
		        success : function(data){
		        	if ($.checkErrorMsg(data)) {
						queryList();
			    	}
		        },
		        error : $.ermpAjaxError
		    });
		}
	});
}

/**
 * 打开导入客户页面
 */
function initCustomerImport(){
			mainFrame.addTab({
			id:"customerImportId",
			title:"客户导入",
			url: BASE_PATH + "page/DataManage/ImportAllocate/CustomerImport.jsp",
			callback:queryList
		});
}

/**
 * 手动分配
 */
function manualAllotImportCustomer() {
	// 判断是否选择要分配的客户
	var importCustomerIds = "";
	$(":checkbox:checked[name='_doc_chk']").each(function(i,value) {
		var importCustId = $(this).val();
		if (importCustId != "") {
			importCustomerIds += importCustId + ",";
		}
	});
	if (importCustomerIds == "") {
		$.confirm("您没有选择要分配的客户，默认会分配全部的客户", function(result) {
			if (result) {
				param = new Object();
				param.importCustomerIds = importCustomerIds;
				param.callback = closeWin;
				window.dialogParam = param;
				var title = "选择销售人员";
				var url = BASE_PATH + "m/importCustomer/init_manualAllotPage";
				var width = 400;
				var height = 250;
				var onClose = queryList;
				var position = "";
				dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
			}
		});
		return;
	}
	param = new Object();
	param.importCustomerIds = importCustomerIds;
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "选择销售人员";
	var url = BASE_PATH + "m/importCustomer/init_manualAllotPage";
	var width = 400;
	var height = 250;
	var onClose = queryList;
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

/**
 * 自动分配
 */
function autoAllotImportCustomer() {
	param = new Object();
	var totalToAllotNum = 0;
	if ($("#_totalCount").size() > 0) {
		totalToAllotNum = $("#_totalCount").text();
	}
	param.totalToAllotNum = totalToAllotNum;
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "自动分配";
	var url = BASE_PATH + "m/group_ext/init_autoAllotImportCust";
	var width = 400;
	var height = 300;
	var onClose = queryList;
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
