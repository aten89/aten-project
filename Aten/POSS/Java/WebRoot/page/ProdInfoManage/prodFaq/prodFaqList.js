var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;

var dialogWin;
/**
 * 对话框参数
 */
var param;

var prodInfoSel;

var creatorSel;

var hasAnswerSel;

$(initPage);
function initPage(){
	
	//添加权限约束
	$.handleRights({
		"searchInfo" : $.SysConstants.QUERY,
        "addProdFaq" : $.SysConstants.ADD
    },"hidModuleRights");

	
	// 初始化下拉框
	initProdInfoSel();
	initCreatorSel();
	initHasAnswerSel();
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(15);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//搜索
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	// addProdFaq
	$("#addProdFaq").click(function(){
		initAddProdFaq();
	});
	
}


function queryList(){
	var createTimeBegin = $.trim($("#createTimeBegin").val());
	var createTimeEnd = $.trim($("#createTimeEnd").val());
	var prodInfoId = "";
	if (prodInfoSel != null) {
		prodInfoId = prodInfoSel.getValue();
	}
	var creator = "";
	if (creatorSel != null) {
		creator = creatorSel.getValue();
	}
	var hasAnswer = "";
	if (hasAnswerSel != null) {
		hasAnswer = hasAnswerSel.getValue();
	}
	var pageNo = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/prod_faq/query_prodFaqListPage",
			dataType : "json",
			data:{
				createTimeBegin : createTimeBegin,
				createTimeEnd : createTimeEnd,
				prodInfoId : prodInfoId,
				creator : creator,
				hasAnswer : hasAnswer,
				pageNo : pageNo,
				pageSize : pagecount,
				sortCol : sortCol,
				ascend : ascend
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		var page = data.prodFaqListPage;
			        if( page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var id = list[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\">"+(list[i].creatorName==null ? "":list[i].creatorName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].createTime==null ? "":list[i].createTime)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].prodInfoName==null ? "":list[i].prodInfoName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].title==null ? "":list[i].title)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].hasAnswer ? "是":"否")+"</td>" +
				           	"<td>" + ($.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"view('" + id + "')\">查看</a> " : "") +
							   ($.hasRight($.SysConstants.MODIFY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"modify('" + id + "')\">修改</a> " : "") +
						//	   ($.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"answer('" + id + "')\">解答</a> " : "") +
							   ($.hasRight($.SysConstants.DELETE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"del('" + id + "')\">删除</a> " : "") +
			  				"</td>"+
		           			"</tr>";
			           }
			        }
		            $("#prodFaqList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

function initAddProdFaq() {
	param = new Object();
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "新增问题";
	var url = BASE_PATH + "m/prod_faq/init_editProdFaqPage";
	var width = 600;
	var height = 310;
	var onClose = queryList;
	dialogWin = $.showModalDialog(title, url, width, height, onClose);
}

function modify(id) {
	param = new Object();
	param.id = id;
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "修改问题";
	var url = BASE_PATH + "m/prod_faq/init_editProdFaqPage?id="+id;
	var width = 600;
	var height = 310;
	var onClose = queryList;
	dialogWin = $.showModalDialog(title, url, width, height, onClose);
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
 * 查看
 * @param id
 */
function view(id) {
	mainFrame.addTab({
		id:"view_prodFaq_"+id,
		title:"查看问题",
		url:BASE_PATH + "/m/prod_faq/view_prodFaq?id=" + id
	});
}

function answer(id) {
	mainFrame.addTab({
		id:"view_prodFaq_"+id,
		title:"查看问题",
		url:BASE_PATH + "/m/prod_faq/view_prodFaq?id=" + id
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
				url : BASE_PATH + "/m/prod_faq/delete_prodFaq",
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

function initProdInfoSel() {
	if ($("#prodInfoSel").size() > 0) {
		prodInfoSel = $("#prodInfoSel").ySelect({
			width : 107,
			height: 150,
			url : BASE_PATH + "/m/prod_info/initProdInfoSel",
			afterLoad : function() {
				prodInfoSel.addOption("", "请选择...", 0);
				// 设置默认值
				prodInfoSel.select(0);
			},
			onChange : function(value, name) {
				// null
			}
		});
	}
}

function initCreatorSel() {
	if ($("#creatorSel").size() > 0) {
		creatorSel = $("#creatorSel").ySelect({
			width : 107,
			height: 150,
			url : BASE_PATH + "/m/prod_faq/init_prodFaqCreatorSel",
			afterLoad : function() {
				creatorSel.addOption("", "请选择...", 0);
				// 设置默认值
				creatorSel.select(0);
			},
			onChange : function(value, name) {
				// null
			}
		});
	}
}

function initHasAnswerSel() {
	if ($("#hasAnswerSel").size() > 0) {
		hasAnswerSel = $("#hasAnswerSel").ySelect({
			width : 107,
			height: 150,
			afterLoad : function() {
				// null
			},
			onChange : function(value, name) {
				// null
			}
		});
	}
}


