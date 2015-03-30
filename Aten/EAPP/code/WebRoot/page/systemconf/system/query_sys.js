var mainFrame = $.getMainFrame();
//初始化页面
$(initPage);

function initPage(){
    
    //菜单栏［添加］
    $("#addSubSystem").click(
        function(){
        	mainFrame.addTab({
				id:"ermp_subsystem_"+Math.floor(Math.random() * 1000000),
				title:"新增子系统",
				url:"m/subsystem/initframe?act=add",
				callback:queryList
			});
        }
    );
    
    /*排序*/
	$("#opSort").click(
		function(){
			mainFrame.addTab({
				id:"ermp_subsystem_sort",
				title:"排序子系统",
				url:"m/subsystem/initorder",
				callback:queryList
			});
		}
	);
    
    //菜单栏［刷新］
    $("#refreshSubSystem").click(function(){
		$("#data_list").html("");
	    queryList();
     });
    
    queryList();
};

//编辑子系统
function modifySubSystem(id,subSystemId){
	mainFrame.addTab({
		id:"ermp_subsystem_"+subSystemId,
		title:"修改子系统",
		url:"m/subsystem/initframe?act=modify&subSystemId=" + subSystemId,
		callback:queryList
	});
};

//删除子系统
function deleteSubSystem(subSysId){
	if(!subSysId){
		$.alert("参数传入出错！");
		return;
	};
    
	$.confirm("是否删除该子系统？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				cache: false,
				url  : "m/subsystem/delete",
				dataType : "json",
				data : {subSystemID : subSysId},
				success : function(data){
					if ($.checkErrorMsg(data) ) {
//						 $.alert("子系统配置信息删除成功！");
		                queryList();
					}
				}
			});
		}
	});
};


function viewSubSystem(id,subSystemId){
	mainFrame.addTab({
		id:"ermp_subsystem_"+subSystemId,
		title:"查看子系统",
		url:"m/subsystem/initframe?act=view&subSystemId=" + subSystemId,
		callback:queryList
	});
};

function queryList(){
    $.ajax({
        type : "POST",
		cache: false,
		url  : "m/subsystem/query",
		dataType : "json",
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.subSystems){
					var dataList = data.subSystems;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td><div class='clip'>" + dataList[i].name + "</div></td>";
						fileList += "<td>" + dataList[i].subSystemID + "</td>";
						fileList += "<td>" + (dataList[i].logoURLPath?"<img src=" + dataList[i].logoURLPath + " onerror=\"this.src='themes/comm/images/defaultSysImg.png'\"  width='18' height='18'/>" : "") + "</td>";
						fileList += "<td><div class='clip'>" + (dataList[i].domainName ? dataList[i].domainName : "") + "</div></td>";
						fileList += "<td>" + (dataList[i].port ? dataList[i].port : "") + "</td>";
						fileList += "<td>" + (dataList[i].serverName ? dataList[i].serverName : "") + "</td>";
						fileList += "<td>" + (dataList[i].isValid ?"是" : "否") + "</td>";

						//操作
                 		var op = $.wrapActionRight($.SysConstants.VIEW, "<a href=\"javascript:void(0);\" onclick=\"viewSubSystem(this.id,'" + dataList[i].subSystemID + "');\" id=\"viewSubSys" + dataList[i].subSystemID + "\" class=\"opLink\">详情</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.MODIFY, "<a href=\"javascript:void(0);\" onclick=\"modifySubSystem(this.id,'" + dataList[i].subSystemID + "');\" id=\"editSubSys" + dataList[i].subSystemID + "\" class=\"opLink\">修改</a>&nbsp;|&nbsp;") 
                                   + $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"deleteSubSystem('" + dataList[i].subSystemID + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				$("#data_list").html(fileList);
        	}
        }
    });
};