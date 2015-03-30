$(initFlowDraft);

//初始化页面
function initFlowDraft(){
	//添加权限约束
    $.handleRights({
        "btnAddFlowDraft" : $.SysConstants.ADD,
        "btnSearch" : $.SysConstants.QUERY
    });
	
	
	//绑定页面事件 
	$("#btnAddFlowDraft").click(function(){
		mainFrame.addTab({
			id:"oa_flow_draft_"+Math.floor(Math.random() * 1000000),
			title:"新建流程草稿",
			url:BASE_PATH + "page/system/flowconf/edit_flow.jsp?act=add",
			callback:loadFlowDraftList		
		});
	});
	
	//刷新
	$("#refresh").click(function(){
		$("#flowDraftList > tbody").html("");
	    loadFlowDraftList();
	});
	
	$("#btnSearch").click(function(){
		gotoPage(1);
	});//搜索
	
	$.EnterKeyPress($("#flowName"),$("#btnSearch"));//回车搜索
	
	var sysSelect = $("#flowDraftSel").ySelect({
		width: 130,
		url : "m/flow_draft?act=flow_class",
		afterLoad:function(){
			sysSelect.addOption("", "请选择...", 0);
			sysSelect.select(0);
		}
	});

	//流程草稿
	gotoPage(1);
}

//转向第几页
function gotoPage(pageNo, totalPage){
    if(isNaN(pageNo)){
        $.alert("页索引只能为数字！");
        return;
    } else if(pageNo < 1){
        pageNo = 1;
    }
    if (totalPage && !isNaN(totalPage) && pageNo > totalPage) {
    	pageNo = totalPage;
    }
    $("#hidNumPage").val(parseInt(pageNo));
    loadFlowDraftList();
};

//-----------------------------草稿列表-----------------------------
//加载草稿列表
function loadFlowDraftList(){

	//设置查询按钮
	$("#btnSearch").attr("disabled","true");
	//分页
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	//查询条件
	var flowCategory = $("#flowClass").val();
	flowCategory = flowCategory ? flowCategory : "";
	var flowName = $("#flowName").val();
	
	//查询字符串
	var queryString = "&pageno=" + pageno + "&pagecount=" + pagecount;
	queryString += "&flowClass=" + flowCategory + "&flowName=" + flowName;
	$.ajax({
      	type : "POST",
		cache: false,
		async : true,//默认为异步
		url  : "m/flow_draft",
		data : "act=query" + queryString,
     	success : function(xml){
     		
     			 
	          //解析XML中的返回代码
	          var message = $("message",xml);
	          var content = $("content",xml);
	          if(message.attr("code") == "1"){	          
		          //分页数据
		    	  $("#pageno").val(content.attr("current-page"));
		    	  $("#pagecount").val(content.attr("page-size"));
	              var tbodyHtml = "";
	              var pageNextHTML="";
	              $("flow-config",xml).each(function(index){
	              
                      	var flowConfig = $(this);
						tbodyHtml += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						tbodyHtml += "<td>" + $("flow-class",flowConfig).text() + "</td>";//流程分类
						tbodyHtml += "<td>" + $("flow-name",flowConfig).text() + "</td>";//流程名称
						tbodyHtml += "<td>" + $("flow-version",flowConfig).text() + "</td>";//流程版本
						tbodyHtml += "<td>" + $("flow-description",flowConfig).text() + "</td>";//流程描述
	                    
	                    //操作
	                    var op = ($.hasRight($.SysConstants.VIEW)?("<a href=\"javascript:void(0);\" onclick=\"viewFlowDraft('" + flowConfig.attr("flow-key") + "');\" id=\"viewFlowDraft" + flowConfig.attr("id") + "\" class=\"opLink\">详情</a>&nbsp;|&nbsp;") : "")
	                    		 + ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyFlowDraft('" + flowConfig.attr("flow-key") + "');\" id=\"modifyFlowDraft" + flowConfig.attr("id") + "\" class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "")
                                 + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteFlowDraft('" + flowConfig.attr("flow-key") + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "")
	                             + ($.hasRight($.OaConstants.ENABLE)?("<a href=\"javascript:void(0);\" onclick=\"publishFlowDraft('" + flowConfig.attr("flow-key") + "');\" id=\"publishFlowDraft" + flowConfig.attr("id") + "\" class=\"opLink\">发布</a>&nbsp;|&nbsp;") : "");
	                    
	                    tbodyHtml += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td>";                   	           
	                    tbodyHtml += "</tr>";          
	                  });
	                  
	              $("#flowDraftList > tbody").html(tbodyHtml);
	              //------------------翻页数据--------------------------
	              $(".pageNext").html($.createTurnPage(xml));
	              $.EnterKeyPress($("#numPage"),$("#numPage").next());
	          }else{
	              $.alert($("message",xml).text());
	          }
	          
	      	$("#btnSearch").removeAttr("disabled");
	      },
	      error :function(){
	      	$("#btnSearch").removeAttr("disabled");
	       	$.ermpAjaxError();
	      }
	  });
}
//-----------------------------草稿列表end-----------------------------

//修改流程草稿
function modifyFlowDraft(flowKey){
	mainFrame.addTab({
		id: "oa_flow_draft_" + Math.floor(Math.random() * 1000000),
		title: "修改流程草稿",
		url: BASE_PATH + "m/flow_draft?act=init_modify&flowKey=" + flowKey,
		callback:loadFlowDraftList
	});
}

//-----------------------------删除流程草稿-----------------------------
//删除流程草稿
function deleteFlowDraft(flowKey){

	if(!flowKey || $.trim(flowKey) === ""){
		$.alert("参数传入出错！");
		return;
	}
	$.confirm("确认要删除吗?", function(r){
		if (r) {
			$.ajax({
				type : "POST",
				cache: false,
				url  : "m/flow_draft",
				data : "act=delete&flowKey=" + flowKey,
				success : deleteFlowDraftSuccess,
				error : $.ermpAjaxError
			});
		}
	});
}

//成功删除流程草稿
function deleteFlowDraftSuccess(xml){
	//解析XML中的返回代码
    var messageCode = $("message",xml).attr("code");
    if(messageCode == "1"){
        $.alert("流程草稿删除成功！");
        loadFlowDraftList();
    }else {
    	$.alert("删除流程草稿失败！原因：" + $("message",xml).text());
    }
}
//-----------------------------删除流程草稿end-----------------------------


//-----------------------------发布流程草稿-----------------------------
//发布流程草稿
function publishFlowDraft(flowKey){
	if(!flowKey || $.trim(flowKey) === ""){
		$.alert("参数传入出错！");
		return;
	}
	$.confirm("是否发布此流程草稿？", function(r){
		if (r) {
			$.ajax({
				type : "POST",
				cache: false,
				url  : "m/flow_draft",
				data : "act=enable&flowKey=" + flowKey,
				success : publishFlowDraftSuccess,
				error : $.ermpAjaxError
			});
		}
	});
}


//成功发布流程草稿
function publishFlowDraftSuccess(xml){
	//解析XML中的返回代码
    var messageCode = $("message",xml).attr("code");
    if(messageCode == "1"){
        $.alert("流程草稿发布成功！");
        loadFlowDraftList();
    }else {
    	$.alert("发布流程草稿失败！原因：" + $("message",xml).text());
    }
}
//-----------------------------发布流程草稿end-----------------------------



//查看流程草稿详情
function viewFlowDraft(flowKey){
	mainFrame.addTab({
		id:"oa_flow_draft_"+Math.floor(Math.random() * 1000000),
		title:"查看流程",
		url:BASE_PATH + "m/flow_draft?act=view&flowKey=" + flowKey + "&flowType=draft"
	});
}
