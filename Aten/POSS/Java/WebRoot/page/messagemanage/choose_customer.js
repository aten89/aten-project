/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

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
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(8);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//支持回车
	$.EnterKeyPress($("#customerName"),$("#searchInfo"));
	$.EnterKeyPress($("#tel"),$("#searchInfo"));
	
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	$("#AllSelectState").click(function(){
		$("input[name='_doc_chk']", "#chooseCust").attr("checked", this.checked);
	});
	
	// 保存
	$("#save").click(function() {
		saveCustomer();
	});
	
	//取消
	$("#close").click(function() {
		args.returnValue = false;
		args.callback();
	});
}

function queryList(batchNumber){
	var customerName = $.trim($("#customerName").val());
	var tel = $.trim($("#tel").val());
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/message/query_customerList",
			dataType : "json",
			data:{
				customerName : customerName,
				tel : tel,
				allocateFlag : false,
				pageNo : pageno,
				pageSize : pagecount,
				sortCol : sortCol,
				ascend : ascend
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		var page = data.custListPage;
			        if( page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var custStr = list[i].tel + "(" + list[i].custName + ")";
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
				           	"<td class=\"listCBoxW\"><input name=\"_doc_chk\" type=\"checkbox\" value=\"" + custStr + "\" class=\"cBox\"/></td>" +
		           			"<td class=\"listData\">"+(list[i].custName==null ? "":list[i].custName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].recommendProduct==null?"":list[i].recommendProduct)+"</td>" +
			  				"</td>";
		           			"</tr>";
			           }
			        }
		            $("#chooseCust").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

function saveCustomer() {
	// 选择客户
	var custStr = "";
	$(":checkbox[name='_doc_chk'][checked]").each(function(i){
		custStr = custStr + this.value + ";";
	});
	args.returnValue = true;
	args.custStr = custStr;
	args.callback();
}