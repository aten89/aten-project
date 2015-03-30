var mainFrame = $.getMainFrame();

$(initParameterSet);
//var currentSelectorId = "";
//var infoNameSel = null;
var flowClassSel = null;
//当前是否为编辑状态
var edit=false;
function initParameterSet(){
	
	//添加权限约束
	$.handleRights({
	   "btnSort" : $.SysConstants.ORDER,
	   "addInfoFlow" : $.SysConstants.ADD
	});
	
	
	$("#addInfoFlow").click(function(){
		addInfoFlow();
	});
	$("#reflash").click(function(){
	 	$("#flowSet > tbody").html("");
		queryList();
		$("#addInfoFlow").removeAttr("disabled","true").removeClass("icoNone");
		edit=false;
	});
	//查询
	queryList();
	
	/*排序*/
	$("#btnSort").click(
		function(){
			mainFrame.addTab({
				id:"oa_InfoLayout_sort",
				title:"信息配置排序",
				url:BASE_PATH +"/m/info_param?act=initsort",
				callback:queryList
			});
		}
	);
	
};

/**
 * 列表查询
 */
function queryList(){
	
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/info_param",
		data : {act:"query"},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var bodyHTML = "";
					$("info-layout",xml).each(
	                    function(index){
	                        var infoLayout = $(this);
	                        bodyHTML += createHtml(infoLayout.attr("id"), $("name",infoLayout).text(), $("flow-class-name",infoLayout).text(), $("description",infoLayout).text());
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
	
	var infoName =$.trim($("#infoName").val());
	//判断
	if (infoName == "") {
		$.alert("请选择分类名称");
		return;
	}
	var flowClass = flowClassSel.getValue();
	var isEmail = $("#isEmail").attr("checked");
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
		url : "m/info_param",
		data : {
			act : "add",
			infoname : infoName,
			flowclass : flowClass,
			isEmail : isEmail,
			desc : desc
		},
		success : function(xml) {
			// 解析XML中的返回代码
			var messageCode = $("message", xml).attr("code");
			if (messageCode == "1") {
				queryList();
				$("#addInfoFlow").removeAttr("disabled", "true").removeClass("icoNone");
//				infoNameSel = null;
				flowClassSel = null;
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
	flowClassSel = null;
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
	var name = selfObj.eq(0).text();
	var flowClass = selfObj.eq(1).text();
//	var isEmail = selfObj.eq(2).find("input").eq(0).val();
	var description = selfObj.eq(2).text();
	var html =createEditHtml(id, name, flowClass, description);
	TR.replaceWith(html);
//	initInfoNameSel(name);
	initFlowClassSel(flowClass);
	
	cancelEnter($("#addName"));
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
function createEditHtml(id, name, flowClass, description){
//	if(isEmail == "true"){
		var html ="<tr id=\""+id+"\">" +
		"<td class=\"listData\"><input id=\"infoName\" type=\"text\"  maxlength=\"50\" style=\"width:220px\" class=\"ipt01\" value=\""+name+"\"/></td>" +
		"<td class=\"listData\"><div id=\"flowClassSel\" name=\"flowClassSel\" type=\"single\" overflow=\"true\"></div></td>" +
//		"<td class=\"listData\"><input id=\"isEmail\" type=\"checkbox\" class=\"iptSo iptGray\" checked=\""+true+"\" /></td> "+
		"<td class=\"listData\"><input id=\"description\" type=\"text\"  maxlength=\"50\" style=\"width:220px\" class=\"ipt01\" value=\""+description+"\"/></td> ";
//	}else{
//		var html ="<tr id=\""+id+"\">" +
//		"<td class=\"listData\"><div id=\"infoNameSel\" name=\"infoNameSel\" type=\"single\" overflow=\"true\"></div></td>" +
//		"<td class=\"listData\"><div id=\"flowClassSel\" name=\"flowClassSel\" type=\"single\" overflow=\"true\"></div></td>" +
//		"<td class=\"listData\"><input id=\"isEmail\" type=\"checkbox\" class=\"iptSo iptGray\" /></td> "+
//		"<td class=\"listData\"><input id=\"description\" type=\"text\" maxlength=\"50\" style=\"width:280px\" class=\"ipt01\" value=\""+description+"\"/></td> ";
//	}
	if(id == null || id == "") {
		html += "<td class=\"oprateImg\"><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveInfoFlow(this)\">保存</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert(this)\">取消</a></td>";
		
	} else {
		html += "<td class=\"oprateImg\">"+"<a href=\"javascript:void(0);\" class=\"opLink\" onclick=\"modifySuccess(this,'"+id+"')\">保存</a>" +"&nbsp;|&nbsp;<a href=\"javascript:void(0);\"  class=\"opLink\" onclick=\"cancelModify(this,'"+id+"','"+name+"','"+flowClass+"','"+description+"')\">取消</a>" +"</td>";
	}
	html += "</tr>";
	return html;
}

/**
 * 创建只读行
 */
function createHtml(id, name, flowClass, description){
	var html = "";
	html += "<tr id=\""+id+"\" onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + name + "</td>" 
         + "<td>" + flowClass + "</td>"
         + "<td>" + description + "</td>"; 
         //操作
	var op = ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyInfoLayout(this,'" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") 
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteInfoLayout('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "")
               +($.hasRight($.SysConstants.BIND_USER) || $.hasRight($.SysConstants.BIND_GROUP) || $.hasRight($.SysConstants.BIND_POST)?("<a href=\"javascript:void(0);\" onclick=\"impowerDraft('" + id + "','"+name+"')\"  class=\"opLink\">起草授权</a>&nbsp;|<a href=\"javascript:void(0);\" onclick=\"impowerManage('" + id + "','"+name+"')\"  class=\"opLink\">管理授权</a>&nbsp;|&nbsp;") : "");
//    if (isEmail == "true") {
//    	op += ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"maintainEmailAddr('" + id + "','"+name+"')\" class=\"opLink\">维护收件人</a>&nbsp;|&nbsp;") : "");
//    }
	html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));                   	           
    html += "</td></tr>";
    return html;
}

//修改成功
function modifySuccess(self, id) {
	var infoName = $.trim($("#infoName").val());
	// 判断
	if (infoName == "") {
		$.alert("请选择分类名称");
		return;
	}
	var flowClass = flowClassSel.getValue();
	var isEmail = $("#isEmail").attr("checked");
	var desc = $.trim($("#description").val());

	var result = $.validChar(desc, "'\"<>");
	if (result) {
		$.alert("说明不能输入非法字符：" + result);
		$("#description").focus();
		return;
	}

	$.ajax({
		type : "POST",
		cache : false,
		url : "m/info_param",
		data : {
			act : "modify",
			id : id,
			infoname : infoName,
			flowclass : flowClass,
			isEmail : isEmail,
			desc : desc
		},
		success : function(xml) {
			// 解析XML中的返回代码
			var messageCode = $("message", xml).attr("code");
			if (messageCode == "1") {
				$.alert("信息配置修改成功");
				queryList();
			} else {
				$.alert($("message", xml).text());
			}
		},
		error : $.ermpAjaxError
	});
}

// 取消修改
function cancelModify(self,id,name,flow,desc){
	$(createHtml(id,name,flow,desc)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#addInfoFlow").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
//	infoNameSel = null;
	flowClassSel = null;
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
		       url  : "m/info_param",
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
 * 初始化分类名称下拉框
 */
//function initInfoNameSel(infoName) {
//	infoNameSel = $("#infoNameSel").ySelect({
//		width : 100,
//		height : 100,
////		popwidth : 100,
//		url : BASE_PATH + "/m/info_param?act=infoSelect",
//		afterLoad : function() {
////			infoNameSel.addOption("", "请选择...", 0);
//			if (infoName != null && infoName != "") {
//				infoNameSel.selectByName(infoName);
//			} else {
//				infoNameSel.select(0);
//			}
//		},
//		onChange : function(value, name) {
//			//null
//		}
//	});
//}

/**
 * 初始化流程类别下拉框
 */
function initFlowClassSel(flowClass) {
	flowClassSel = $("#flowClassSel").ySelect({
		width : 100,
		height : 100,
//		popwidth : 100,
		url : BASE_PATH + "/m/flow_draft?act=flow_class",
		afterLoad : function() {
			flowClassSel.addOption("", "请选择...", 0);
			if (flowClass != null && flowClass != "") {
				flowClassSel.selectByName(flowClass);
			} else {
				flowClassSel.select(0);
			}
		},
		onChange : function(value, name) {
			//null
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

/**
 * 起草授权
 * @param id
 * @param name
 */
function impowerDraft(id,name){
	mainFrame.addTab({
		id:"oa_impower_draft" + id,
		title:"起草授权",
		url:BASE_PATH+"m/info_param?act=init_assign&id=" + id +"&flag=0&title="+encodeURI(name+"的起草授权")
	});
}

/**
 * 管理授权
 * @param id
 * @param name
 */
function impowerManage(id,name){
	mainFrame.addTab({
		id:"oa_impower_manage" + id,
		title:"管理授权",
		url:BASE_PATH+"m/info_param?act=init_assign&id=" + id +"&flag=1&title="+encodeURI(name+"的管理授权")
	});
}

/**
 * 维护收件人
 * @param id
 * @param name
 */
//function maintainEmailAddr(id,name){
//	mainFrame.addTab({
//		id:"oa_maintain_emailAddr" + id,
//		title:"维护收件人",
//		url:BASE_PATH+"m/info_param?act=init_emailAddr&id=" + id +"&flag=2&title="+encodeURI(name+"的收件人维护")
//	});
//}