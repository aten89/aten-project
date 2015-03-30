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
		url  : "m/flow_pub/query",
        async : true,
        data: {
			pageNo: pageNo,
			pageSize : pageCount,
			flowClass : $.trim(flowClassSelect.getValue()),
			flowName : $.trim($("#flowName").val()),
			draftFlag : 2
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
                                   + $.wrapActionRight($.SysConstants.DISABLE, "<a href=\"javascript:void(0);\" onclick=\"disableFlow('" + dataList[i].flowKey + "');\" class=\"opLink\">禁用</a>&nbsp;|&nbsp;");
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
		url:"page/flowmanage/view_flow.jsp?draftFlag=2&flowKey=" + flowKey
	});
};

//编辑
function modifyFlow(flowKey){
	//复制为草稿后再修改
	$.ajax({
		type : "POST",
		cache: false, 
		url  : "m/flow_pub/modify",
		dataType : "json",
		data : {flowKey : flowKey},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
                mainFrame.addTab({
					id:"ermp_flow_"+flowKey,
					title:"修改流程",
					url:"page/flowmanage/draft/edit_flow.jsp?flowKey=" + flowKey,
					callback:queryList
				});
			}
        }
	});
};


//禁用
function disableFlow(flowKey){
	if(!flowKey){
		$.alert("参数传入出错！");
		return;
	};
    
	$.confirm("是否禁用该流程？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				cache: false, 
				url  : "m/flow_pub/disable",
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
