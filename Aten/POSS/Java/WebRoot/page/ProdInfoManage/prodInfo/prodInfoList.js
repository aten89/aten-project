var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;

var supplierSel;

var prodTypeSel;

var secondClassifySel;

var prodStatusSel;

$(initPage);
function initPage(){
	
	//添加权限约束
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY, //查询
        "addProdInfo" : $.SysConstants.ADD
    },"hidModuleRights");
	
	// 初始化下拉框
	initSupplierSel();
	initProdTypeSel();
	initSecondClassifySel("");
	initProdStatusSel();
	
	//初始化表头列排序
//	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(15);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//搜索
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	// add prodInfo
	$("#addProdInfo").click(function(){
		initAddProdInfo();
	});
	
}


function queryList(){
	var sellDateBegin = $.trim($("#sellDateBegin").val());
	var sellDateEnd = $.trim($("#sellDateEnd").val());
	
	var supplierId = "";
	if (supplierSel != null) {
		supplierId = supplierSel.getValue();
	}
	var prodType = "";
	if (prodTypeSel != null) {
		prodType = prodTypeSel.getValue();
	}
	var prodSecondaryClassify = "";
	if (secondClassifySel != null) {
		prodSecondaryClassify = secondClassifySel.getValue();
	}
	var prodStatus = "";
	if (prodStatusSel != null) {
		prodStatus = prodStatusSel.getValue();
	}
	
	var pageNo = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
//	var sortCol = columnSortObj.getSortColumn();
//	var ascend =  columnSortObj.getAscend();
	
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/prod_info/query_prodInfoListPage",
			dataType : "json",
			data:{
				sellDateBegin : sellDateBegin,
				sellDateEnd : sellDateEnd,
				supplierId : supplierId,
				prodType : prodType,
				prodSecondaryClassify : prodSecondaryClassify,
				prodStatus : prodStatus,
				pageNo : pageNo,
				pageSize : pagecount,
				sortCol : "prodCode",
				ascend : "true"
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		var page = data.prodInfoListPage;
			        if( page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
				           	var id = list[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\">"+(list[i].prodCode==null ? "":list[i].prodCode)+"</td>" +
		           			"<td class=\"listData\">" + 
		           			($.hasRight($.SysConstants.VIEW)? "<a href='javascript:void(0);'  onclick=\"view('" + id + "')\">" : "") +
		           			(list[i].prodName==null ? "":list[i].prodName)+"</a></td>" +
		           			"<td class=\"listData\">"+(list[i].prodTypeName==null ? "":list[i].prodTypeName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].secondClassifyName==null ? "":list[i].secondClassifyName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].supplier==null ? "":list[i].supplier.supplier)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].prodStatusName==null ? "":list[i].prodStatusName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].pjtTotalAmount==null ? "":list[i].pjtTotalAmount)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].sellTimeLimit==null ? "":list[i].sellTimeLimit)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].sellDate==null ?"":list[i].sellDate)+"</td>" +
				           	"<td>" + ($.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"view('" + id + "')\">查看</a> " : "") +
							   ($.hasRight($.SysConstants.DELETE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"del('" + id + "')\">删除</a> " : "") +
			  				"</td>";
		           			"</tr>";
			           }
			        }
		            $("#prodInfoList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

function initAddProdInfo() {
	mainFrame.addTab({
		id:"add_prodInfo",
		title:"新增产品",
		url:BASE_PATH + "/m/prod_info/init_addProdInfoPage",
		callback: queryList
	});
}

/**
 * 查看
 * @param id
 */
function view(id) {
	mainFrame.addTab({
		id:"view_prodInfo_"+id,
		title:"查看产品详情",
		url:BASE_PATH + "/m/prod_info/init_prodInfoFrame?prodInfoId=" + id
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
				url : BASE_PATH + "/m/prod_info/delete_prodInfo",
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

function initSupplierSel() {
	if ($("#supplierSel").size() > 0) {
		supplierSel = $("#supplierSel").ySelect({
			width : 107,
			height: 150,
			url : BASE_PATH + "/m/supplier/query_supplierSel",
			afterLoad : function() {
				supplierSel.addOption("", "请选择...", 0);
				// 设置默认值
				supplierSel.select(0);
			},
			onChange : function(value, name) {
				// null
			}
		});
	}
}

function initProdTypeSel() {
	if ($("#prodTypeSel").size() > 0) {
		prodTypeSel = $("#prodTypeSel").ySelect({
			width : 107,
			height: 150,
			url : BASE_PATH + "/m/prod_type/query_prodTypeSel?prodTypeID=" + "",
			afterLoad : function() {
				prodTypeSel.addOption("", "请选择...", 0);
				// 设置默认值
				prodTypeSel.select(0);
			},
			onChange : function(value, name) {
				var prodTypeId = value;
				// 初始化变更操作
				initSecondClassifySel(prodTypeId);
			}
		});
	}
}

function initSecondClassifySel(prodTypeId) {
	if (prodTypeId == "undefined" || prodTypeId == "") {
		prodTypeId = "";
	}
	if ($("#secondClassifySel").size() > 0) {
		if (prodTypeId == "undefined" || prodTypeId == "") {
			prodTypeId = "";
			secondClassifySel = $("#secondClassifySel").ySelect({
				width : 107,
				height: 150,
				afterLoad : function() {
					if (secondClassifySel != null) {
						secondClassifySel.addOption("", "请选择...", 0);
						// 设置默认值
						secondClassifySel.select(0);
					}
				},
				onChange : function(value, name) {
					// null
				}
			});
		} else {
			secondClassifySel = $("#secondClassifySel").ySelect({
				width : 107,
				height: 150,
				url : BASE_PATH + "/m/prod_type/query_prodTypeSel?prodTypeID=" + prodTypeId,
				afterLoad : function() {
					secondClassifySel.addOption("", "请选择...", 0);
					// 设置默认值
					secondClassifySel.select(0);
				},
				onChange : function(value, name) {
					// null
				}
			});
		}
		
	}
}

function initProdStatusSel() {
	if ($("#prodStatusSel").size() > 0) {
		prodStatusSel = $("#prodStatusSel").ySelect({
			width : 107,
			height: 150,
			url : BASE_PATH + "/l/dict/initProdStatusSel",
			afterLoad : function() {
				prodStatusSel.addOption("", "请选择...", 0);
				// 设置默认值
				prodTypeSel.select(0);
			},
			onChange : function(value, name) {
				// null
			}
		});
	}
}

