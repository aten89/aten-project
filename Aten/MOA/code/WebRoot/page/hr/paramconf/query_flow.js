var mainFrame = $.getMainFrame();

$(initFlowList);
var flowSel1 = null;
var flowSel2 = null;
var flowSel3 = null;
var flowSel4 = null;
var flowSel5 = null;
var flowSel6 = null;
//当前是否为编辑状态
var edit=false;
function initFlowList() {
	$.handleRights({
        "addFlow" : $.SysConstants.ADD//新增
    });
    
    //新增请假流程配置
	$("#addFlow").click(function(){
		addInfoFlow();
	});
	//刷新
  	$("#refresh").click(function(){
  	    $("#holidayFlowList").empty();
  		queryList();
  		$("#addFlow").removeAttr("disabled","true").removeClass("icoNone");
		edit=false;
  	});
	//查询
	queryList();
}

function queryList(){
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/holi_flow",
		data : "act=query",
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
                var tBodyHTML = "";
                $("holiday-flow",xml).each(
                    function(index){
                        var holidayFlow = $(this);
                         tBodyHTML += createHtml(holidayFlow.attr("id"), $("group-name",holidayFlow).text(), $("holi-flow-name",holidayFlow).text(), 
                         		$("canholi-flow-name",holidayFlow).text(),$("entry-flow-name",holidayFlow).text(),$("resign-flow-name",holidayFlow).text(),$("transfer-flow-name",holidayFlow).text(),$("positive-flow-name",holidayFlow).text());
                    }
                );
                $("#holidayFlowList").html(tBodyHTML);
                $("#addFlow").removeAttr("disabled","true").removeClass("icoNone");
					edit=false;
            } else{
               $.alert($("message",xml).text());
            };
        },
        error : $.ermpAjaxError
    });
}
//新增流程配置
function addInfoFlow(){
	$("#addFlow").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var infoFlowList = createEditHtml("","","","","","","","");
	if($("#holidayFlowList tr").length==0){
	  $("#holidayFlowList").append(infoFlowList);
	}else{
	  $(infoFlowList).insertBefore($("#holidayFlowList tr:eq(0)"));
	}
	cancelEnter($("#description"));
	
	//生成流程类别下拉框
	initFlowClassSel(null, null,null,null,null,null);
}

//保存
function saveInfoFlow(self) {
	
	var groupName =$.trim($("#groupName").val());
	//判断
	if (!groupName) {
		$.alert("请选择部门名称");
		return;
	}
	var flow1 = flowSel1.getValue();
	if (!flow1) {
		$.alert("请选择请假流程");
		return;
	}
	var flow2 = flowSel2.getValue();
	if (!flow2) {
		$.alert("请选择销假流程");
		return;
	}
	var flow3 = flowSel3.getValue();
	if (!flow3) {
		$.alert("请选择入职流程");
		return;
	}
	var flow4 = flowSel4.getValue();
	if (!flow4) {
		$.alert("请选择离职流程");
		return;
	}
	
	var flow5 = flowSel5.getValue();
	if (!flow5) {
		$.alert("请选择异动流程");
		return;
	}
	
	var flow6 = flowSel6.getValue();
	if (!flow6) {
		$.alert("请选择转正流程");
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
		url : "m/holi_flow",
		data : {
			act : "add",
			groupname : groupName,
			holflowkey : flow1,
			canflowkey : flow2,
			entflowkey : flow3,
			resflowkey : flow4,
			tranflowkey : flow5,
			posiflowkey : flow6,
			desc : desc
		},
		success : function(xml) {
			// 解析XML中的返回代码
			var messageCode = $("message", xml).attr("code");
			if (messageCode == "1") {
				$.alert("保存成功");
				queryList();
				$("#addFlow").removeAttr("disabled", "true").removeClass("icoNone");
				flow1 = null;
				flow2 = null;
				flow3 = null;
				flow4 = null;
				flow5 = null;
				flow6 = null;
			} else {
				$.alert($("message", xml).text());
			}
			edit = false;
		},
		error : $.ermpAjaxError
	});
}

function cancelInsert(self) {
	$(self).parent().parent().remove();
	$("#addFlow").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
	flow1 = null;
	flow2 = null;
	flow3 = null;
	flow4 = null;
	flow5 = null;
	flow6 = null;
}


//修改
function modifyInfoLayout(self,id){
	$("#addFlow").attr("disabled","true").addClass("icoNone");
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
	var flowName1= selfObj.eq(1).text();
	var flowName2 = selfObj.eq(2).text();
	var flowName3 = selfObj.eq(3).text();
	var flowName4 = selfObj.eq(4).text();
	var flowName5 = selfObj.eq(5).text();
	var flowName6 = selfObj.eq(6).text();
//	var description = selfObj.eq(5).text();
	var html =createEditHtml(id, groupName, flowName1, flowName2, flowName3, flowName4, flowName5, flowName6);
	TR.replaceWith(html);
	initFlowClassSel(flowName1, flowName2, flowName3, flowName4, flowName5, flowName6);
	
//	cancelEnter($("#description"));
}

function createEditHtml(id, groupName, flow1, flow2, flow3, flow4, flow5,flow6){
	var html ="<tr id=\""+id+"\">" +
	"<td class=\"listData\"><input id=\"groupName\" type=\"text\" readonly style=\"width:68%\" class=\"ipt01\" value=\""+groupName+"\"/><input type=\"button\" id=\"openDeptSelect\" class=\"selBtn\" /></td>" +
	"<td class=\"listData\"><div id=\"holiFlowSel\" name=\"holiFlowSel\" type=\"single\" overflow=\"true\"></div></td>" +
	"<td class=\"listData\"><div id=\"holicanFlowSel\" name=\"holicanFlowSel\" type=\"single\" overflow=\"true\"></div></td>" +
	"<td class=\"listData\"><div id=\"entryFlowSel\" name=\"entryFlowSel\" type=\"single\" overflow=\"true\"></div></td>" +
	"<td class=\"listData\"><div id=\"resignFlowSel\" name=\"resignFlowSel\" type=\"single\" overflow=\"true\"></div></td>" +
	"<td class=\"listData\"><div id=\"transferFlowSel\" name=\"transferFlowSel\" type=\"single\" overflow=\"true\"></div></td>" +
	"<td class=\"listData\"><div id=\"positiveFlowSel\" name=\"positiveFlowSel\" type=\"single\" overflow=\"true\"></div></td>";
	if(id == null || id == "") {
		html += "<td class=\"oprateImg\"><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveInfoFlow(this)\">保存</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert(this)\">取消</a></td>";
		
	} else {
		html += "<td class=\"oprateImg\">"+"<a href=\"javascript:void(0);\" class=\"opLink\" onclick=\"modifySuccess(this,'"+id+"')\">保存</a>" +"&nbsp;|&nbsp;<a href=\"javascript:void(0);\"  class=\"opLink\" onclick=\"cancelModify(this,'"+id+"','"+groupName+"','"+flow1+"','"+flow2+"','"+flow3+"','"+flow4+"','"+flow5+"','"+flow6+"')\">取消</a>" +"</td>";
	}
	html += "</tr>";
	return html;
}

function createHtml(id, groupName, flow1, flow2, flow3, flow4, flow5, flow6){
	var html = "";
	html += "<tr id=\""+id+"\" onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + groupName + "</td>" 
         + "<td>" + flow1 + "</td>"
         + "<td>" + flow2 + "</td>"
         + "<td>" + flow3 + "</td>"
         + "<td>" + flow4 + "</td>"
         + "<td>" + flow5 + "</td>"
         + "<td>" + flow6 + "</td>"; 
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
	var flow1 = flowSel1.getValue();
	if (!flow1) {
		$.alert("请选择请假流程");
		return;
	}
	var flow2 = flowSel2.getValue();
	if (!flow2) {
		$.alert("请选择销假流程");
		return;
	}
	var flow3 = flowSel3.getValue();
	if (!flow3) {
		$.alert("请选择入职流程");
		return;
	}
	var flow4 = flowSel4.getValue();
	if (!flow4) {
		$.alert("请选择离职流程");
		return;
	}
	var flow5 = flowSel5.getValue();
	if (!flow5) {
		$.alert("请选择异动流程");
		return;
	}
	
	var flow6 = flowSel6.getValue();
	if (!flow6) {
		$.alert("请选择转正流程");
		return;
	}
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
		url : "m/holi_flow",
		data : {
			act : "modify",
			id : id,
			groupname : groupName,
			holflowkey : flow1,
			canflowkey : flow2,
			entflowkey : flow3,
			resflowkey : flow4,
			tranflowkey : flow5,
			posiflowkey : flow6,
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
function cancelModify(self,id,groupName, flowName1, flowName2,flowName3,flowName4,flowName5,flowName6){
	$(createHtml(id,groupName, flowName1, flowName2,flowName3,flowName4,flowName5,flowName6)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#addFlow").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
	flow1 = null;
	flow2 = null;
	flow3 = null;
	flow4 = null;
	flow5 = null;
	flow6 = null;
}

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
		       url  : "m/holi_flow",
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
function initFlowClassSel(flow1, flow2, flow3, flow4, flow5, flow6) {
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
	
	flowSel1 = $("#holiFlowSel").ySelect({
		width:70, 
		url:"m/flow_man?act=getByKey",
		data:"flowClass=QJLC",
		afterLoad:function(){
			flowSel1.addOption("", "请选择...", 0);
			if (flow1) {
				flowSel1.selectByName(flow1);
			} else {
				flowSel1.select(0);
			}
		}
	});
	
	flowSel2 = $("#holicanFlowSel").ySelect({
		width:70, 
		url:"m/flow_man?act=getByKey",
		data:"flowClass=QJLC",
		afterLoad:function(){
			flowSel2.addOption("", "请选择...", 0);
			if (flow2) {
				flowSel2.selectByName(flow2);
			} else {
				flowSel2.select(0);
			}
		}
	});
	
	flowSel3 = $("#entryFlowSel").ySelect({
		width:70, 
		url:"m/flow_man?act=getByKey",
		data:"flowClass=HRLC",
		afterLoad:function(){
			flowSel3.addOption("", "请选择...", 0);
			if (flow3) {
				flowSel3.selectByName(flow3);
			} else {
				flowSel3.select(0);
			}
		}
	});
	
	flowSel4 = $("#resignFlowSel").ySelect({
		width:70, 
		url:"m/flow_man?act=getByKey",
		data:"flowClass=HRLC",
		afterLoad:function(){
			flowSel4.addOption("", "请选择...", 0);
			if (flow4) {
				flowSel4.selectByName(flow4);
			} else {
				flowSel4.select(0);
			}
		}
	});
	
	flowSel5 = $("#transferFlowSel").ySelect({
		width:70, 
		url:"m/flow_man?act=getByKey",
		data:"flowClass=YDLC",
		afterLoad:function(){
			flowSel5.addOption("", "请选择...", 0);
			if (flow4) {
				flowSel5.selectByName(flow5);
			} else {
				flowSel5.select(0);
			}
		}
	});
	
	flowSel6 = $("#positiveFlowSel").ySelect({
		width:70, 
		url:"m/flow_man?act=getByKey",
		data:"flowClass=ZZLC",
		afterLoad:function(){
			flowSel6.addOption("", "请选择...", 0);
			if (flow6) {
				flowSel6.selectByName(flow6);
			} else {
				flowSel6.select(0);
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