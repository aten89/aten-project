var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
//初始化页面
$(initActionList);

function initActionList(){
    //菜单栏［添加］
    $("#addAction").click(
        function(){
        	mainFrame.addTab({
				id:"ermp_action_"+Math.floor(Math.random() * 1000000),
				title:"新增-动作管理",
				url:"m/action/initframe?act=add",
				callback:queryList
			});
        }
    );
    
    //菜单栏［刷新］
    $("#refreshAction").click( function(){
 		$("#data_list").html("");
	    queryList();
	});
    
    //加载列表数据
    $("#searchAction").click(function(){
        	trunPageObj.gotoPage(1);//搜索第一页
	});
    
    $.EnterKeyPress($("#actionName,#actionKey"),$("#searchAction"));
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
};

//编辑动作
function modifyAction(id,actionId){
	mainFrame.addTab({
		id:"ermp_action_"+id,
		title:"修改动作",
		url:"m/action/initframe?act=modify&actionid=" + actionId,
		callback:queryList
	});
};

//删除动作
function deleteAction(actionId){
	if(!actionId){
		$.alert("参数传入出错！");
		return;
	};
    
	$.confirm("是否删除该动作？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				cache: false, 
				url  : "m/action/delete",
				dataType : "json",
				data : {actionID : actionId},
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
	$("#searchAction").attr("disabled","true");
	//分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
    $.ajax({
        type : "POST",
		cache: false, 
		url  : "m/action/query",
        async : true,
        data: {
			pageNo: pageNo,
			pageSize : pageCount,
			name : $.trim($("#actionName").val()),
			actionKey : $.trim($("#actionKey").val())
		},
        dataType : "json",
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.actInfoPage && data.actInfoPage.dataList){
					var dataList = data.actInfoPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td>" + dataList[i].name + "</td>";
						fileList += "<td>" + dataList[i].actionKey + "</td>";
						fileList += "<td>" + (dataList[i].logoURL ? "<img src='" + dataList[i].logoURL+ "' />" : "") + "</td>";
						fileList += "<td>" + (dataList[i].tips ? dataList[i].tips : "") + "</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW, "<a href=\"javascript:void(0);\" onclick=\"viewAction(this.id,'" + dataList[i].actionID + "');\" id=\"editAction" + dataList[i].actionID + "\" class=\"opLink\">详情</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.MODIFY, "<a href=\"javascript:void(0);\" onclick=\"modifyAction(this.id,'" + dataList[i].actionID + "');\" id=\"editAction" + dataList[i].actionID + "\" class=\"opLink\">修改</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"deleteAction('" + dataList[i].actionID + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.actInfoPage);
			}
			$("#searchAction").removeAttr("disabled");
        }
    });
}


function viewAction(id,actionId){
	mainFrame.addTab({
		id:"ermp_action_"+id,
		title:"查看动作",
		url:"m/action/initframe?act=view&actionid=" + actionId,
		callback:queryList
	});
};