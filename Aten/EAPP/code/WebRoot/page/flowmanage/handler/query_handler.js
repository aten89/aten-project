var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
var flowClassSelect;
var globalFlagSelect;
//初始化页面
$(initPage);

function initPage(){
	flowClassSelect = $("#flowClassDiv").ySelect({width: 120,json:true,url : "m/datadict/dictsel?dictType=FLOW_CLASS",afterLoad:addSysOption});
   	function addSysOption() {
		flowClassSelect.addOption("", "所有...", 0);
	}
	
	globalFlagSelect = $("#globalFlagDiv").ySelect({width: 35, onChange: function(v, n){
		if (v == "true") {
			flowClassSelect.disable(true);
			flowClassSelect.select(0);
		} else {
			flowClassSelect.disable(false);
		}
	}});
	
    //菜单栏［添加］
    $("#addVar").click(
        function(){
        	mainFrame.addTab({
				id:"ermp_flowhandler_"+Math.floor(Math.random() * 1000000),
				title:"新增-处理程序",
				url:"m/flow_handler/initframe?act=add",
				callback:queryList
			});
        }
    );
    
    //菜单栏［刷新］
    $("#refreshVar").click( function(){
 		$("#data_list").html("");
	    queryList();
	});
    
    //加载列表数据
    $("#searchVar").click(function(){
        	trunPageObj.gotoPage(1);//搜索第一页
	});
    
    $.EnterKeyPress($("#name"),$("#searchVar"));
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
};

//编辑动作
function modifyVar(handId){
	mainFrame.addTab({
		id:"ermp_flowhandler_"+handId,
		title:"修改处理程序",
		url:"m/flow_handler/initframe?act=modify&handId=" + handId,
		callback:queryList
	});
};

//删除动作
function deleteVar(handId){
	if(!handId){
		$.alert("参数传入出错！");
		return;
	};
    
	$.confirm("是否删除该处理程序？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				cache: false, 
				url  : "m/flow_handler/delete",
				dataType : "json",
				data : {handId : handId},
				success : function(data){
					if ($.checkErrorMsg(data) ) {
//						 $.alert("动作信息删除成功！");
		                queryList();
					}
		        }
			});
		}
	});
};


function queryList(){
	//设置查询按钮
	$("#searchVar").attr("disabled","true");
	//分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
    $.ajax({
        type : "POST",
		cache: false, 
		url  : "m/flow_handler/query",
        async : true,
        data: {
			pageNo: pageNo,
			pageSize : pageCount,
			name : $.trim($("#name").val()),
			flowClass : $.trim(flowClassSelect.getValue()),
			globalFlag : $.trim(globalFlagSelect.getValue())
		},
        dataType : "json",
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.flowHandlerPage && data.flowHandlerPage.dataList){
					var dataList = data.flowHandlerPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td>" +  (dataList[i].globalFlag ? "全局公用" : dataList[i].flowClassName) + "</td>";
						fileList += "<td>" + dataList[i].name + "</td>";
						fileList += "<td>" + dataList[i].handlerClass + "</td>";
						fileList += "<td>" + dataList[i].type + "</td>";
						fileList += "<td style='text-align: center;'>" + (dataList[i].globalFlag ? "是" : "否") + "</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW, "<a href=\"javascript:void(0);\" onclick=\"viewVar('" + dataList[i].handId + "');\" class=\"opLink\">详情</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.MODIFY, "<a href=\"javascript:void(0);\" onclick=\"modifyVar('" + dataList[i].handId + "');\" class=\"opLink\">修改</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"deleteVar('" + dataList[i].handId + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.flowHandlerPage);
			}
			$("#searchVar").removeAttr("disabled");
        }
    });
}


function viewVar(handId){
	mainFrame.addTab({
		id:"ermp_flowhandler_"+handId,
		title:"查看处理程序",
		url:"m/flow_handler/initframe?act=view&handId=" + handId,
		callback:queryList
	});
};