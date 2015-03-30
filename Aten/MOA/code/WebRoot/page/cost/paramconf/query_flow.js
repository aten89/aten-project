var mainFrame = $.getMainFrame();

$(initParameterSet);
var flowNameSel = null;
//当前是否为编辑状态
var edit=false;
function initParameterSet(){
	
	//添加权限约束
	$.handleRights({
	   "addInfoFlow" : $.SysConstants.ADD
	});
	
	
	$("#addInfoFlow").click(function(){
		addInfoFlow();
	});
	$("#reflash").click(function(){
	 	$("#flowSet > tbody").empty();
		queryList();
		$("#addInfoFlow").removeAttr("disabled","true").removeClass("icoNone");
		edit=false;
	});
	//查询
	queryList();
	
};

/**
 * 列表查询
 */
function queryList(){
	
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/rei_param",
		data : {act:"query"},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var bodyHTML = "";
					$("reim-flow",xml).each(
	                    function(index){
	                        var infoLayout = $(this);
	                        bodyHTML += createHtml(infoLayout.attr("id"), $("group-name",infoLayout).text(), $("flow-name",infoLayout).text(), $("description",infoLayout).text());
	                });
	                $("#flowSet > tbody").html(bodyHTML);
	                $("#addInfoFlow").removeAttr("disabled","true").removeClass("icoNone");
					edit=false;
				}else{
               		$.alert($("message",xml).text());
           		};
	       },
	       error : $.ermpAjaxError
	});	
}

/**
 * 新增信息流程
 */
function addInfoFlow(){
	$("#addInfoFlow").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	
	var infoFlowList = createEditHtml("","","","");
	if($("#flowSet > tbody tr").length==0){
	  $("#flowSet > tbody").append(infoFlowList);
	}else{
	  $(infoFlowList).insertBefore($("#flowSet > tbody tr:eq(0)"));
	}
	cancelEnter($("#description"));
	
	//生成信息类别下拉框
//	initInfoNameSel(null);
	//生成流程类别下拉框
	initFlowClassSel(null);

//    $("#description").focus();
}

//保存
function saveInfoFlow(self) {
	
	var groupName =$.trim($("#groupName").val());
	//判断
	if (!groupName) {
		$.alert("请选择部门名称");
		return;
	}
	var flowKey = flowNameSel.getValue();
	if (!flowKey) {
		$.alert("请选择流程名称");
		return;
	}
	var desc = $.trim($("#description").val());

	var result = $.validChar(desc, "'\"<>");
	if (result) {
		$.alert("说明不能输入非法字符：" + result);
		$("#description").focus();
		return;
	}

	// 提交到后台
	$.ajax({
		type : "POST",
		cache : false,
		async : true,
		url : "m/rei_param",
		data : {
			act : "add",
			groupname : groupName,
			flowkey : flowKey,
			desc : desc
		},
		success : function(xml) {
			// 解析XML中的返回代码
			var messageCode = $("message", xml).attr("code");
			if (messageCode == "1") {
				$.alert("保存成功");
				queryList();
				$("#addInfoFlow").removeAttr("disabled", "true").removeClass("icoNone");
//				infoNameSel = null;
				flowNameSel = null;
			} else {
				$.alert($("message", xml).text());
			}
			edit = false;
		},
		error : $.ermpAjaxError
	});
}

/**
 * 取消新增
 * @param self
 */
function cancelInsert(self) {
	$(self).parent().parent().remove();
	$("#addInfoFlow").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
//	infoNameSel = null;
	flowNameSel = null;
}

//修改
function modifyInfoLayout(self,id){
	$("#addInfoFlow").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var TR = $(self).parent().parent();
//	var id = TR.attr("id");
	var selfObj=$(self).parent().parent().find("td");
	var groupName = selfObj.eq(0).text();
	var flowName = selfObj.eq(1).text();
	var description = selfObj.eq(2).text();
	var html =createEditHtml(id, groupName, flowName, description);
	TR.replaceWith(html);
//	initInfoNameSel(name);
	initFlowClassSel(flowName);
	
	cancelEnter($("#description"));
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
function createEditHtml(id, groupName, flowName, description){
	var html ="<tr id=\""+id+"\">" +
	"<td class=\"listData\"><input id=\"groupName\" type=\"text\" readonly style=\"width:150px\" class=\"ipt01\" value=\""+groupName+"\"/><input type=\"button\" id=\"openDeptSelect\" class=\"selBtn\" /></td>" +
	"<td class=\"listData\"><div id=\"flowNameSel\" name=\"flowName\" type=\"single\" overflow=\"true\"></div></td>" +
	"<td class=\"listData\"><input id=\"description\" type=\"text\"  maxlength=\"50\" style=\"width:300px\" class=\"ipt01\" value=\""+description+"\"/></td> ";
	if(id == null || id == "") {
		html += "<td class=\"oprateImg\"><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveInfoFlow(this)\">保存</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert(this)\">取消</a></td>";
		
	} else {
		html += "<td class=\"oprateImg\">"+"<a href=\"javascript:void(0);\" class=\"opLink\" onclick=\"modifySuccess(this,'"+id+"')\">保存</a>" +"&nbsp;|&nbsp;<a href=\"javascript:void(0);\"  class=\"opLink\" onclick=\"cancelModify(this,'"+id+"','"+groupName+"','"+flowName+"','"+description+"')\">取消</a>" +"</td>";
	}
	html += "</tr>";
	return html;
}

/**
 * 创建只读行
 */
function createHtml(id, groupName, flowName, description){
	var html = "";
	html += "<tr id=\""+id+"\" onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + groupName + "</td>" 
         + "<td>" + flowName + "</td>"
         + "<td>" + description + "</td>"; 
         //操作
	var op = ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyInfoLayout(this,'" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") 
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteInfoLayout('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "");
	html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));                   	           
    html += "</td></tr>";
    return html;
}

//修改成功
function modifySuccess(self, id) {
	var groupName =$.trim($("#groupName").val());
	//判断
	if (!groupName) {
		$.alert("请选择部门名称");
		return;
	}
	var flowKey = flowNameSel.getValue();
	if (!flowKey) {
		$.alert("请选择流程名称");
		return;
	}
	var desc = $.trim($("#description").val());

	var result = $.validChar(desc, "'\"<>");
	if (result) {
		$.alert("说明不能输入非法字符：" + result);
		$("#description").focus();
		return;
	}
	
	var result = $.validChar(desc, "'\"<>");
	if (result) {
		$.alert("说明不能输入非法字符：" + result);
		$("#description").focus();
		return;
	}

	$.ajax({
		type : "POST",
		cache : false,
		url : "m/rei_param",
		data : {
			act : "modify",
			id : id,
			groupname : groupName,
			flowkey : flowKey,
			desc : desc
		},
		success : function(xml) {
			// 解析XML中的返回代码
			var messageCode = $("message", xml).attr("code");
			if (messageCode == "1") {
				$.alert("修改成功");
				queryList();
			} else {
				$.alert($("message", xml).text());
			}
		},
		error : $.ermpAjaxError
	});
}

// 取消修改
function cancelModify(self,id,groupName, flowName,desc){
	$(createHtml(id,groupName,flowName,desc)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#addInfoFlow").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
//	infoNameSel = null;
	flowNameSel = null;
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
		       url  : "m/rei_param",
		       data : {act:"delete",id:id},
		       success : function(xml){
		           var messageCode = $("message",xml).attr("code");
		           if(messageCode == "1"){
		           		$.alert("删除成功!");
		           		queryList();
		           }else{
		           		$.alert($("message",xml).text());
		           }
		       },
		       error : $.ermpAjaxError
		   	});
		}
	});
}


/**
 * 初始化流程类别下拉框
 */
function initFlowClassSel(flowName) {
	//打开部门
    $('#openDeptSelect').click(
	   	function(e){
	   		var dialog = new DeptDialog(ERMP_PATH, BASE_PATH);
	   		dialog.setCallbackFun(function(user){
				if (user != null) {
	   				$("#groupName").val(user.name);
	   				$("#groupName").focus();
				}
			});
	   		dialog.openDialog();
		}
	);
	
	flowNameSel = $("#flowNameSel").ySelect({
		width:160, 
		url:"m/flow_man?act=getByKey",
		data:"flowClass=BXLC",
		afterLoad:function(){
			flowNameSel.addOption("", "请选择...", 0);
			if (flowName) {
				flowNameSel.selectByName(flowName);
			} else {
				flowNameSel.select(0);
			}
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