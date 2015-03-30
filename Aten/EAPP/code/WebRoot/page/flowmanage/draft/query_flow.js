var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
var flowClassSelect;
//初始化页面
$(initActionList);

function initActionList(){
	flowClassSelect = $("#flowClassDiv").ySelect({width: 120,json:true,url : "m/datadict/dictsel?dictType=FLOW_CLASS",afterLoad:addSysOption});
   	function addSysOption() {
		flowClassSelect.addOption("", "所有...", 0);
	}
	
    //菜单栏［添加］
    $("#addFlow").click(
        function(){
        	mainFrame.addTab({
				id:"ermp_flow_"+Math.floor(Math.random() * 1000000),
				title:"新增-流程草稿",
				url:"page/flowmanage/draft/edit_flow.jsp",
				callback:queryList
			});
        }
    );
    
    //菜单栏［刷新］
    $("#refreshFlow").click( function(){
 		$("#data_list").html("");
	    queryList();
	});
    
    //加载列表数据
    $("#searchFlow").click(function(){
        trunPageObj.gotoPage(1);//搜索第一页
	});
    
    $.EnterKeyPress($("#flowName"),$("#searchFlow"));
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
};

function queryList(){
	//设置查询按钮
	$("#searchFlow").attr("disabled","true");
	//分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
    $.ajax({
        type : "POST",
		cache: false, 
		url  : "m/flow_draft/query",
        async : true,
        data: {
			pageNo: pageNo,
			pageSize : pageCount,
			flowClass : $.trim(flowClassSelect.getValue()),
			flowName : $.trim($("#flowName").val()),
			draftFlag : 1
		},
        dataType : "json",
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.flowConfigPage && data.flowConfigPage.dataList){
					var dataList = data.flowConfigPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td>" + dataList[i].flowClassName + "</td>";
						fileList += "<td>" + dataList[i].flowName + "</td>";
						fileList += "<td>" + dataList[i].flowKey + "</td>";
						fileList += "<td>" + dataList[i].flowVersion + "</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW, "<a href=\"javascript:void(0);\" onclick=\"viewFlow('" + dataList[i].flowKey + "');\" class=\"opLink\">详情</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.MODIFY, "<a href=\"javascript:void(0);\" onclick=\"modifyFlow('" + dataList[i].flowKey + "');\" class=\"opLink\">修改</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"deleteFlow('" + dataList[i].flowKey + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;")
                                   + $.wrapActionRight($.SysConstants.ENABLE, "<a href=\"javascript:void(0);\" onclick=\"enableFlow('" + dataList[i].flowKey + "');\" class=\"opLink\">发布</a>&nbsp;|&nbsp;");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.actInfoPage);
			}
			$("#searchFlow").removeAttr("disabled");
        }
    });
}

function viewFlow(flowKey){
	mainFrame.addTab({
		id:"ermp_flow_"+flowKey,
		title:"查看流程",
		url:"page/flowmanage/view_flow.jsp?draftFlag=1&flowKey=" + flowKey
	});
};

//编辑
function modifyFlow(flowKey){
	mainFrame.addTab({
		id:"ermp_flow_"+flowKey,
		title:"修改流程",
		url:"page/flowmanage/draft/edit_flow.jsp?flowKey=" + flowKey,
		callback:queryList
	});
};

//删除
function deleteFlow(flowKey){
	if(!flowKey){
		$.alert("参数传入出错！");
		return;
	};
    
	$.confirm("是否删除该流程？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				cache: false, 
				url  : "m/flow_draft/delete",
				dataType : "json",
				data : {flowKey : flowKey},
				success : function(data){
					if ($.checkErrorMsg(data) ) {
		                queryList();
					}
		        }
			});
		}
	});
};

//发布
function enableFlow(flowKey){
	if(!flowKey){
		$.alert("参数传入出错！");
		return;
	};
    
	$.confirm("是否发布该流程？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				cache: false, 
				url  : "m/flow_draft/enable",
				dataType : "json",
				data : {flowKey : flowKey},
				success : function(data){
					if ($.checkErrorMsg(data) ) {
		                queryList();
					}
		        }
			});
		}
	});
};
