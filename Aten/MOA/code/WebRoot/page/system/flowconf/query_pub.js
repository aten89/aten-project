$(initPage);

//初始化页面
function initPage(){

 	//添加权限约束
    $.handleRights({
        "btnSearch" : $.SysConstants.QUERY
    });
    
	//刷新
  	$("#refresh").click(function(){
  		loadFlowPublishedList();
  	});
	
	
	//流程类别下拉框	
	var sysSelect = $("#flowDraftSel").ySelect({width: 130,url : "m/flow_draft?act=flow_class",afterLoad:addOption});
	function addOption() {
		sysSelect.addOption("", "请选择...", 0);
		sysSelect.select(0);
	}
	

	$("#btnSearch").click(function(){
		gotoPage(1);
	});//搜索
	
	$.EnterKeyPress($("#flowName"),$("#btnSearch"));//回车搜索
	
	//加载已发布流程列表
	gotoPage(1);
};

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
    loadFlowPublishedList();
};


//-----------------------------流程列表-----------------------------
//加载已发布流程列表
function loadFlowPublishedList(){

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
		url  : "m/flow_man",
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
	                    var op = ($.hasRight($.SysConstants.VIEW)?("<a href=\"javascript:void(0);\" onclick=\"viewFlowPublished('" + flowConfig.attr("flow-key") + "');\"    class=\"opLink\">详情</a>&nbsp;|&nbsp;") : "")
	                    op += ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyFlowPublished('" + flowConfig.attr("flow-key") + "');\"   class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "")
                        op += ($.hasRight($.OaConstants.DISABLE)?("<a href=\"javascript:void(0);\" onclick=\"disableFlowPublished('" + flowConfig.attr("flow-key") + "');\"  class=\"opLink\">禁用</a>&nbsp;|&nbsp;") : "")
	                //    op += ($.hasRight($.SysConstants.BIND_USER) || $.hasRight($.SysConstants.BIND_GROUP) || $.hasRight($.SysConstants.BIND_POST)?("<a href=\"javascript:void(0);\" onclick=\"flowImpower('" + flowConfig.attr("flow-key") + "','"+$("flow-name",flowConfig).text()+"')\"  class=\"opLink\">授权</a>&nbsp;|&nbsp;") : "")
	                   // op += ($.hasRight($.SysConstants.VIEW)?("<a href=\"javascript:void(0);\" onclick=\"viewFlowDraft('" + flowConfig.attr("flow-key") + "');\"      class=\"opLink\">绑定模板</a>&nbsp;|&nbsp;") : "");   
	                    
	                    tbodyHtml += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td>";                   	           
	                    tbodyHtml += "</tr>";          
	                  });
	                  
	              $("#publishedFlowList > tbody").html(tbodyHtml).find("td:empty").html("&nbsp;");
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


//查看已发布流程详情
function viewFlowPublished(flowKey){
	mainFrame.addTab({
		id:"oa_flow_draft_"+Math.floor(Math.random() * 1000000),
		title:"查看流程",
		url:BASE_PATH + "m/flow_man?act=view&flowKey=" + flowKey + "&flowType=publihed"
	});
}


/**
  * 修改已发布流程
  * 1.如果相同flowKey的流程草稿存在，则加载相应的流程草稿进行修改
  * 2.如果相同flowKey的流程草稿不存在，则拷贝当前流程定义为草稿保存到数据库，进行加载
  */	  	
function modifyFlowPublished(flowKey){
	
	if(!flowKey || $.trim(flowKey) === ""){
		$.alert("修改流程失败！参数传入出错！");
		return;
	}
	
	mainFrame.addTab({
		id:"oa_flow_draft_"+Math.floor(Math.random() * 1000000),
		title:"修改流程草稿",
		url:BASE_PATH + "m/flow_man?act=init_modify&flowKey=" + flowKey
	});
	
}


//-----------------------------禁用已发布流程-----------------------------
//禁用已发布流程
function disableFlowPublished(flowKey){

	if(!flowKey || $.trim(flowKey) === ""){
		$.alert("参数传入出错！");
		return;
	}
	$.confirm("是否禁用此流程?", function(r){
		if (r) {
			$.ajax({
				type : "POST",
				cache: false,
				url  : "m/flow_man",
				data : "act=disable&flowKey=" + flowKey,
				
				success : function(xml){//成功禁用已发布流程
					//解析XML中的返回代码
				    var messageCode = $("message",xml).attr("code");
				    if(messageCode == "1"){
				        $.alert("禁用流程成功！");
				        loadFlowPublishedList();
				    }else {
				    	$.alert("禁用流程失败！原因：" + $("message",xml).text());
				    }
				},
				error : $.ermpAjaxError
			});
		}
	});
}

//-----------------------------删除流程草稿end-----------------------------

//绑定模板
function bindingTemplate(){

}

//授权
//function flowImpower(flowKey,name){
//	mainFrame.addTab({
//		id:"oa_flow_binduser" + flowKey,
//		title:"流程授权",
//		url:BASE_PATH+"m/flow_man?act=init_assign&flowkey="+flowKey+"&title="+encodeURI(name+"的授权")
//	});
//}