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
        "searchInfo" : $.SysConstants.QUERY
    },"hidModuleRights");
	
	// 初始化下拉框
//	initProdSel();
	
	//初始化表头列排序
//	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//支持回车
	$.EnterKeyPress($("#customerName"),$("#searchInfo"));
	$.EnterKeyPress($("#tel"),$("#searchInfo"));
	$.EnterKeyPress($("#email"),$("#searchInfo"));
	
	//搜索
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	 //刷新
    $("#refresh").click(function(){
    	//清空
    	$("#fomalCust").empty();
		queryList();//搜索
	});   
}

function initProdSel(){
	if ($("#prodSel").size() > 0) {
		prodSel = $("#prodSel").ySelect({
			width : 100,
			url : BASE_PATH + "/l/dict/initCommTypeSel",
			afterLoad : function() {
				prodSel.addOption("", "请选择...", 0);
				prodSel.select(0);
			}
		});
	}
}

function queryList(){
	var customerName = $.trim($("#customerName").val());
	var tel = $.trim($("#tel").val());
	var email = $.trim($("#email").val());
	var recommendProduct =  $.trim($("#recommendProd").val());
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
//	var sortCol = columnSortObj.getSortColumn();
//	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/customer/query_fomal",
			dataType : "json",
			data:{
				custName : customerName,
				tel : tel,
				email : email,
				recommendProduct : recommendProduct,
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
		           			"<td class=\"listData\">"+(list[i].custName==null ? "":list[i].custName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].sexName==null ? "":list[i].sexName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].email==null?"":list[i].email)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].recommendProduct==null?"":list[i].recommendProduct)+"</td>"+
		           			"<td class=\"listData\">"+(list[i].submitTimeStr==null ?"":list[i].submitTimeStr)+"</td>" +
				           	"<td>" + 
		           			($.hasRight($.SysConstants.VIEW) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"view('" + id + "') \">查看</a>" : "") +"&nbsp;&nbsp;"+
		           			($.hasRight($.SysConstants.DELETE) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"del('" + id + "') \">删除</a>" : "") +
			  				"</td></tr>";
			           }
			        }
		            $("#fomalCust").html(html);
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
function view(id) {
	mainFrame.addTab({
		id:"viewCustomerInfo"+id,
		title:"客户资料",
		url:BASE_PATH + "page/MyCustomer/CustomerInfo/customerInfoFrame.jsp?customerId=" + id +"&moduleKey=FORMAL_CUST"
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


function modify(id){
	param = new Object();
	param.callback = callback;
	window.dialogParam = param;
	var title = "";
	var url = BASE_PATH + "/m/customer/initmodify?customerId=" + id;
	var width = 800;
	var height = 480;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

function callback(){
	if (param.returnValue){
		$.alert("操作成功",function(){
			dialogWin.close();
			//清空
	    	$("#fomalCust").empty();
			queryList();//搜索
		});
	}else{
		dialogWin.close();
	}
}
