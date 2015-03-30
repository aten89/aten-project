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
 * 推荐产品下拉框
 */
var prodSel;

var dialogWin = null;
var param = null;

$(initPage);
function initPage(){
	
	//添加权限约束
	$.handleRights({
	   "add" : $.SysConstants.ADD,
	   "searchInfo" : $.SysConstants.QUERY
	});
	
	//快捷工具栏中没有任何按钮，则清除该DIV
    if($("#divAddTool input").length == 0){
    	$("#divAddTool").remove();
    }
	
	// 初始化下拉框
	initProdSel();
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//支持回车
//	$.EnterKeyPress($("#customerName"),$("#searchInfo"));
	
	//搜索
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	$("#add").click(function(){
		addMessage();
	});
	
	
}

function initProdSel(){
	if ($("#prodSel").size() > 0) {
		prodSel = $("#prodSel").ySelect({
			width : 100,
			url : BASE_PATH + "/m/prod_info/initProdInfoSel",
			afterLoad : function() {
				prodSel.addOption("", "请选择...", 0);
				prodSel.select(0);
			}
		});
	}
}

function queryList(){
//	var customerName = $.trim($("#customerName").val());
//	var tel = $.trim($("#tel").val());
//	var email = $.trim($("#email").val());
//	var recommendProduct = prodSel.getValue() == "" ? "" : prodSel.getValue();
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/message/query",
			dataType : "json",
			data:{
//				custName : customerName,
//				tel : tel,
//				email : email,
//				recommendProduct : recommendProduct,
				pageNo : pageno,
				pageSize : pagecount,
				sortCol : sortCol,
				ascend : ascend
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		var page = data.messageListPage;
			        if( page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var id = list[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\">"+(list[i].receiverNo==null ? "":list[i].receiverNo)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].sendTime==null ? "":list[i].sendTime)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].prodName==null ?"":list[i].prodName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].content==null?"":list[i].content)+"</td>" +
				           	"<td>" + ($.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"view('" + id + "')\">查看</a> " : "") + //通过userAccountId绑定修改
				           	 ($.hasRight($.SysConstants.DELETE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"del('" + id + "')\">删除</a> " : "") +
//				         	"<td><a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"view('" + id + "')\">详情</a> " + 
				           	"</td>";
		           			"</tr>";
			           }
			        }
		            $("#messageList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}


/**
 * 新增客户
 */
function addMessage(){
	param = new Object();
	param.callback = callback;
	window.dialogParam = param;
	var title = "";
	var url = BASE_PATH + "/m/message/initAdd";
	var width = 600;
	var height = 380;
	var onClose = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose);
}

/**
 * 查看
 * @param id
 */
function view(id) {
	mainFrame.addTab({
		id:"view_message_"+id,
		title:"短信详细信息",
		url:BASE_PATH + "/m/message/view?id=" + id
	});
}

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
				url : BASE_PATH + "/m/message/delete",
				dataType : "json",
				data:{
					id : id
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


function callback(){
	if (param.returnValue){
		$.alert("操作成功",function(){
			dialogWin.close();
			//清空
	    	$("#messageList").empty();
			queryList();//搜索
		});
	}else{
		dialogWin.close();
	}
}
