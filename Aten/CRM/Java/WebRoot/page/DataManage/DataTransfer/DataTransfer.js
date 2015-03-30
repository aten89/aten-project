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
 * 客户经理下拉框
 */
var customerManageSel;
/**
 * 客户状态
 */
var statusSel;

var dialogWin = null;
var param = null;

$(initPage);
function initPage(){
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY,
         "dataTransfer" : $.CrmConstants.ALLOT
    });
	//初始化客户经理下拉框
	initCustomerManageSel();
	// 初始化客户状态
	initStatusSel();
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	$("#dataTransfer").click(function() {
		var strSelectedIds = "";//选中的id串
    	
    	$("input[name='_doc_chk'][checked]", "#custList").each(function(){
    		if( this.checked == true ){
				strSelectedIds += this.value + ",";
			}
    	});
		if(strSelectedIds != "" ){
			strSelectedIds = strSelectedIds.substring( 0, strSelectedIds.length - 1);
		}
		
		dataTransfer(strSelectedIds);
  	});
	
	 //刷新
    $("#refresh").click(function(){
    	//清空
    	$("#custList").empty();
		queryList();//搜索
	});   
	
	$("#AllSelectState").click(function(){
		$("input[name='_doc_chk']", "#custList").attr("checked", this.checked);
 });
}

function queryList(){
	var customerManage = customerManageSel.getValue();
	var customerStatus = statusSel.getValue();;
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/customer/queryCustomerInfoList",
			dataType : "json",
			data:{
				queryFlag : "allCustomerInfo",
				customerManage : customerManage,
				customerStatus : customerStatus,
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
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
				           	"<td class=\"listCBoxW\"><input name=\"_doc_chk\" type=\"checkbox\" value=\"" + id + "\" class=\"cBox\"/></td>" +
		           			"<td class=\"listData\" style=\"padding:7px 6px\">"+(list[i].custName==null ? "":list[i].custName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].sex==null ? "":(list[i].sex=="M" ? "男":"女"))+"</td>" +
		           			"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].email==null?"":list[i].email)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].statusName==null?"":list[i].statusName)+"</td>" +
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

function dataTransfer(strSelectedIds){
	if(!strSelectedIds){
		alert( "请至少选择一条客户" );
		return;
	}
	//参数
	param = new Object();
	param.strSelectedIds = strSelectedIds;
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "数据转移";
	var url = BASE_PATH+"page/DataManage/DataTransfer/CustomerManage.jsp";
	var width = 400;
	var height = 200;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

function closeWin(){
	
	if (param.returnValue){
		$.alert("操作成功",function(){
			dialogWin.close();
			//改变客户信息对应的客户经理
			initCustomerInfo(param.strSelectedIds, param.customerManage);
			queryList();
		});
	}else{
		dialogWin.close();
	}
}

//改变客户信息对应的客户经理
function initCustomerInfo(strSelectedIds, customerManage){
	
	$.ajax({
		async:false,
		type : "POST",
		cache: false,
		dataType : "json",
      	url : BASE_PATH  + "m/customer/changeSaleMan",
      	data :{
      		customerInfoIds : strSelectedIds,
      		customerManage : customerManage
		},
      	success : function(data){
//      		if ($.checkErrorMsg(data)) {
//        		alert(data.msg.text);
//        	}
		},
      	error : $.ermpAjaxError
	});
}

function initCustomerManageSel(){
	customerManageSel = $("#customerManageSel").ySelect({
		width : 80,
		height: 150,
		url : BASE_PATH + "/m/group_ext/query_allSaleStaff",
		afterLoad : function() {
//			customerManageSel.addOption("", "请选择...", 0);
			// 设置默认值
//			customerManageSel.select(0);
		},
		onChange : function(value, name) {
			
		}
	});
}

/**
 * 初始化“客户状态”下拉框
 */
function initStatusSel(){
	statusSel = $("#statusSel").ySelect({
		width : 80
	});
}

