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
				id:"ermp_flowvar_"+Math.floor(Math.random() * 1000000),
				title:"新增-流程变量",
				url:"m/flow_var/initframe?act=add",
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
    
    $.EnterKeyPress($("#displayName"),$("#searchVar"));
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
};

//编辑动作
function modifyVar(varId){
	mainFrame.addTab({
		id:"ermp_flowvar_"+varId,
		title:"修改流程变量",
		url:"m/flow_var/initframe?act=modify&varId=" + varId,
		callback:queryList
	});
};

//删除动作
function deleteVar(varId){
	if(!varId){
		$.alert("参数传入出错！");
		return;
	};
    
	$.confirm("是否删除该流程变量？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				cache: false, 
				url  : "m/flow_var/delete",
				dataType : "json",
				data : {varId : varId},
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
		url  : "m/flow_var/query",
        async : true,
        data: {
			pageNo: pageNo,
			pageSize : pageCount,
			displayName : $.trim($("#displayName").val()),
			flowClass : $.trim(flowClassSelect.getValue()),
			globalFlag : $.trim(globalFlagSelect.getValue())
		},
        dataType : "json",
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.flowVarPage && data.flowVarPage.dataList){
					var dataList = data.flowVarPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td>" +  (dataList[i].globalFlag ? "全局公用" : dataList[i].flowClassName) + "</td>";
						fileList += "<td>" + dataList[i].name + "</td>";
						fileList += "<td>" + dataList[i].displayName + "</td>";
						fileList += "<td>" + dataList[i].type + "</td>";
						fileList += "<td style='text-align: center;'>" + (dataList[i].notNull ? "是" : "否") + "</td>";
						fileList += "<td style='text-align: center;'>" + (dataList[i].globalFlag ? "是" : "否") + "</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW, "<a href=\"javascript:void(0);\" onclick=\"viewVar('" + dataList[i].varId + "');\" class=\"opLink\">详情</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.MODIFY, "<a href=\"javascript:void(0);\" onclick=\"modifyVar('" + dataList[i].varId + "');\" class=\"opLink\">修改</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"deleteVar('" + dataList[i].varId + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.flowVarPage);
			}
			$("#searchVar").removeAttr("disabled");
        }
    });
}


function viewVar(varId){
	mainFrame.addTab({
		id:"ermp_flowvar_"+varId,
		title:"查看流程变量",
		url:"m/flow_var/initframe?act=view&varId=" + varId,
		callback:queryList
	});
};