$(initPage);
function initPage(){
	//添加权限约束
    $.handleRights({
        "toSave" : $.SysConstants.ADD,
        "autoDealApprove" : "autodealapprove"
    });
  	//新增报销单
  	$("#toSave").click(function(){
  		mainFrame.addTab({
			id:"oa_rei_start_adddraft"+Math.floor(Math.random() * 1000000),
			title:"填写报销单",
			url:BASE_PATH + "/m/rei_start?act=initadd",
			callback:queryList
		});
  	});

  	//刷新
  	$("#refresh").click(function(){
  		queryList();
  	});
  	
  	//查询
  	queryList();
  	
  	//自动处理：将流程结束
  	$("#autoDealApprove").click(function(){
  		autoDealApprove("报销流程", "通知发起人", "结束流程", "年终由系统批量结束");
  	});
}

function queryList() {
	$("#draftLists").empty();
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/rei_start?act=query",
	    success : function(xml){
	        var message = $("message",xml);
	        var code = message.attr("code");
	        if(code == "1"){
	            //------------------列表数据----------------------------
	            var listDate = "";
	            $("reimbursement",xml).each(
	                function(index){
	                    var curELe = $(this);
	                    var id = curELe.attr("id");
	                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"

								+ "<td>" + $("apply-date",curELe).text() + "</td>"
								+ "<td>" + $("payee",curELe).text() + "</td>"
								+ "<td>" + $("reimbursement-sum",curELe).text() + "</td>"
								+ "<td>" + $("causa",curELe).text() + "</td>"
								+ "<td>" + ( $.hasRight($.SysConstants.ADD)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initModify('" + id + "')\">修改</a> " : "")
								+ ( $.hasRight($.SysConstants.ADD)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"delDrfat('" + id + "')\">删除</a>" : "")
								+ "</td></tr>";
	                }
	            );
	            $("#draftLists").html(listDate);
	        } else if (code == "-1") {
	        	$.alert("会话已超时,请重新登入");
	        	if (top != self) {
	        		top.location.reload();
	        	} else {
	        		self.location.reload();
	        	}
	        }
	    },
	    error : $.ermpAjaxError
	});
}

//转到新增页面
function initModify(id) {
	mainFrame.addTab({
		id:"oa_rei_start_modifyraft" + id,
		title:"修改报销单",
		url:BASE_PATH + "/m/rei_start?act=initmodify&id=" + id,
		callback:queryList
	});
}

//删除草稿
function delDrfat(id) {
	$.confirm("确认要删除吗?", function(r){
		if (r) {
			$.ajax({
			    type : "POST",
			    cache: false,
			    async : true,
			    url  : "m/rei_start?act=delete&id=" + id,
			    success : function(xml){
			        var message = $("message",xml);
			        if(message.attr("code") == "1"){
			            $.alert("删除成功");
			            queryList();
			        } else {
			        	$.alert(message.text());
			        }
			    },
			    error : $.ermpAjaxError
			});
		}
	});	
}

/**
 * 自动处理流程
 * @param {} flowName 流程名称
 * @param {} fromNodeName 流程起点
 * @param {} toNodeName 流程下一节点
 * @param {} comment 审批意见
 */
function autoDealApprove(flowName, fromNodeName, toNodeName, comment) {
	if (fromNodeName == null || fromNodeName == "" ||
			toNodeName == null || toNodeName == "") {
		return;
	}
	
	flowName = encodeURI(flowName);
	fromNodeName = encodeURI(fromNodeName);
	toNodeName = encodeURI(toNodeName);
	comment = encodeURI(comment);
	
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/rei_deal?act=autodealapprove&flowName=" + flowName + 
	    		"&fromNodeName=" + fromNodeName + "&toNodeName=" + toNodeName + 
	    		"&comment=" + comment,
	    success : function(xml){
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	            $.alert("操作成功");
	        } else {
	        	$.alert(message.text());
	        }
	    },
	    error : $.ermpAjaxError
	});
}
