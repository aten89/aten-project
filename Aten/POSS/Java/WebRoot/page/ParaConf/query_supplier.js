var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;


//当前是否为编辑状态
var edit=false;

$(initParameterSet);

function initParameterSet(){
	
	//添加权限约束
	$.handleRights({
	   "add" : $.SysConstants.ADD
	});
	
	//快捷工具栏中没有任何按钮，则清除该DIV
    if($("#divAddTool input").length == 0){
    	$("#divAddTool").remove();
    }
	
	$("#add").click(function(){
		add();
	});
	$("#reflash").click(function(){
	 	$("#flowSet > tbody").empty();
		queryList();
		$("#add").removeAttr("disabled","true").removeClass("icoNone");
		edit=false;
	});
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	//查询
	queryList();
	
};

/**
 * 列表查询
 */
function queryList(){
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/supplier/query",
		dataType : "json",
		data:{
			pageNo : pageno,
			pageSize : pagecount,
			sortCol : sortCol,
			ascend : ascend
		},
        success : function(data,i){
           	var html="";
        	if ($.checkErrorMsg(data)) {
        		var page = data.supplierListPage;
		        if( page.dataList!=null){
		           var list = page.dataList;
		           for(var i =0; i<list.length; i++){
			           	var id = list[i].id;
			           	var supplier = list[i].supplier;
			           	var remark = list[i].remark;
	           			html += createHtml(id, supplier, remark);
		           }
		           edit=false;
		        }
		        $("#flowSet > tbody").html(html);
                $("#add").removeAttr("disabled","true").removeClass("icoNone");
	            trunPageObj.setPageData(page);
        	}
        },
        error : $.ermpAjaxError
    });
}

/**
 * 新增信息流程
 */
function add(){
	$("#add").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	
	var infoFlowList = createEditHtml("","","");
	if($("#flowSet > tbody tr").length==0){
	  $("#flowSet > tbody").append(infoFlowList);
	}else{
	  $(infoFlowList).insertBefore($("#flowSet > tbody tr:eq(0)"));
	}
	cancelEnter($("#remark"));

    $("#supplier").focus();
}

//保存
function saveInfoFlow(self) {
	
	var supplier =$.trim($("#supplier").val());
	//判断
	if (!supplier) {
		$.alert("请输入供应商名称");
		return;
	}
	var remark = $.trim($("#remark").val());

	// 提交到后台
	$.ajax({
        type : "POST",
        cache: false,
        url  : "m/supplier/add",
        dataType : "json",
        data : {
				supplier : supplier,
				remark : remark},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert("保存成功!");
        		queryList();
        	}
        }
    }); 
}

/**
 * 取消新增
 * @param self
 */
function cancelInsert(self) {
	$(self).parent().parent().remove();
	$("#add").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}

//修改
function modifyInfoLayout(self,id){
	$("#add").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var TR = $(self).parent().parent();
//	var id = TR.attr("id");
	var selfObj=$(self).parent().parent().find("td");
	var supplier = selfObj.eq(0).text();
	var remark = selfObj.eq(1).text();
	var html =createEditHtml(id, supplier, remark);
	TR.replaceWith(html);
	cancelEnter($("#remark"));
}

/**
 * 创建编辑行
 * @param id
 * @param name
 * @param flowClass
 * @param isEmail
 * @param description
 * @returns {String}
 */
function createEditHtml(id, supplier, remark){
	var html ="<tr id=\""+id+"\">" +
	"<td class=\"listData\"><input id=\"supplier\" type=\"text\"  maxlength=\"50\" style=\"width:300px\" class=\"ipt01\" value=\""+supplier+"\"/></td>" +
	"<td class=\"listData\"><input id=\"remark\" type=\"text\"  maxlength=\"50\" style=\"width:300px\" class=\"ipt01\" value=\""+remark+"\"/></td> ";
	if(id == null || id == "") {
		html += "<td class=\"oprateImg\"><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveInfoFlow(this)\">保存</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert(this)\">取消</a></td>";
		
	} else {
		html += "<td class=\"oprateImg\">"+"<a href=\"javascript:void(0);\" class=\"opLink\" onclick=\"modifySuccess(this,'"+id+"')\">保存</a>" +"&nbsp;|&nbsp;<a href=\"javascript:void(0);\"  class=\"opLink\" onclick=\"cancelModify(this,'"+id+"','"+supplier+"','"+remark+"')\">取消</a>" +"</td>";
	}
	html += "</tr>";
	return html;
}

/**
 * 创建只读行
 */
function createHtml(id, supplier, remark){
	var html = "";
	html += "<tr id=\""+id+"\" onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + (supplier==null?"":supplier) + "</td>" 
         + "<td>" + (remark==null?"":remark) + "</td>"; 
         //操作
//	var op = "<a href=\"javascript:void(0);\" onclick=\"modifyInfoLayout(this,'" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;"
//  + "<a href=\"javascript:void(0);\" onclick=\"deleteInfoLayout('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;";
		var op = ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyInfoLayout(this,'" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") 
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteInfoLayout('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "");
	html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));                   	           
    html += "</td></tr>";
    return html;
}

//修改成功
function modifySuccess(self, id) {
	var supplier =$.trim($("#supplier").val());
	//判断
	if (!supplier) {
		$.alert("请输入供应商名称");
		return;
	}
	var remark = $.trim($("#remark").val());

	$.ajax({
        type : "POST",
        cache: false,
        url  : "m/supplier/modify",
        dataType : "json",
        data : {
				id : id,
				supplier : supplier,
				remark : remark},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert("修改成功!");
        		queryList();
        	}
        }
    }); 
}

// 取消修改
function cancelModify(self, id, supplier, remark){
	$(createHtml(id, supplier, remark)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#add").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}

/**
 * 删除
 * @param id
 */
function deleteInfoLayout(id){
	if (edit) {
		$.alert("请先保存");
		return;
	}
	$.confirm("确认要删除吗?", function(r){
		if (r) {
			$.ajax({
		        type : "POST",
				cache: false,
				async: true,
				url : BASE_PATH + "/m/supplier/delete",
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

//解决IE6下按回车转到其它页面
function cancelEnter(obj) {
	obj.keypress(function(e) {
		if (e.keyCode == 13) {
			return false;
		}
	});
}