var mainFrame = $.getMainFrame();
//初始化页面
$(initActionList);

function initActionList(){
	var d=new Date(); 
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    var date = d.getDate();
	var curDate=year+ (month < 10 ? "-0" : "-") + month + (date < 10 ? "-0" : "-") + date;
	$("#endCreateTime").val(curDate);
    if (month == 1) {
    	month = 12;
    	year = year -1;
    } else {
    	month = month - 1;
    }
    curDate=year+ (month < 10 ? "-0" : "-") + month + (date < 10 ? "-0" : "-") + date;
    $("#startCreateTime").val(curDate);
    
    //加载列表数据
    $("#searchTask").click(function(){
        	queryList();
	});
    
    $.EnterKeyPress($("#taskName,#startCreateTime,#endCreateTime"),$("#searchTask"));
    queryList();
};

function queryList(){
	//设置查询按钮
	$("#searchTask").attr("disabled","true");
	//分页
    $.ajax({
        type : "POST",
		cache: false, 
		url  : "m/flow_inst/query",
        async : true,
        data: {
			taskName : $.trim($("#taskName").val()),
			startCreateTime : $.trim($("#startCreateTime").val()),
			endCreateTime : $.trim($("#endCreateTime").val())
		},
        dataType : "json",
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.flowTaskBeans){
					var dataList = data.flowTaskBeans;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td>" +  (dataList[i].taskDescription ? dataList[i].taskDescription : "") + "</td>";
						fileList += "<td>" + dataList[i].taskName + "</td>";
						fileList += "<td>" +  (dataList[i].formId ? dataList[i].formId : "") + "</td>";
						fileList += "<td>" +  (dataList[i].userAccountName ? dataList[i].userAccountName : "") + "</td>";
						fileList += "<td>" + (dataList[i].actorName ? dataList[i].actorName : "") + "</td>";
						fileList += "<td>" + (dataList[i].createTime ? dataList[i].createTime : "") + "</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW, "<a href=\"javascript:void(0);\" onclick=\"viewFlowInst('" + dataList[i].flowInstanceId + "');\" class=\"opLink\">查看</a>&nbsp;|&nbsp;");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				
				$("#data_list").html(fileList);
			}
			$("#searchTask").removeAttr("disabled");
        }
    });
}

function viewFlowInst(flowInstId) {
	mainFrame.addTab({
		id:"ermp_flowinst_"+flowInstId,
		title:"查看任务实例",
		url:"page/flowmanage/view_inst.jsp?flowInstanceId=" + flowInstId,
		callback:queryList
	});
}