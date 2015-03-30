var childModulesTree = null;

var subsystemSelector = null;
$(initModuleList);

function initModuleList(){
    //$.getMainFrame().getCurrentTab().setTitle("管理-系统模块", "系统模块管理");
//    $.handleRights(
//        {
//            "addSubModule" : $.SysConstants.ADD,
//            "modifyModule" : $.SysConstants.MODIFY,
//            "delModule" : $.SysConstants.DELETE,
//            "sortSubModule" : $.SysConstants.ORDER,
//            "bindModuleAction" : $.SysConstants.BIND_ACTION,
//            "searchMd" : $.SysConstants.QUERY
//        }
//    );
    
    
	//高度设置
//	$("#moduleMain").css("height",document.documentElement.clientHeight - 60);
//	$("#tdScaleMove").css("height",document.documentElement.clientHeight - 50);
//	$("#searchResults").css("height",document.documentElement.clientHeight - 337);
	
	//初始化系统下拉列表
	subsystemSelector = $("#subSystemList").ySelect({width: 157, json: true, url : "m/subsystem/allsystem",onChange : subSystemList_OnChange});

    $("#searchMd").click(function(){
        searchModlue();
    });
    
    //回车搜索
    $.EnterKeyPress($("#searchModuleName"),$("#searchMd"));
    
	//添加子模块
	$("#addSubModule").click(function(e){
		$('#opType').val('add');
		var moduleId = $("#curModuleId").val();
		loadModule("initadd");
		
		//展开左边的树
        if (moduleId && moduleId != "" && $("#" + moduleId + " > ul.ajax").length != 0) {
            $("#" + moduleId + " > img").click();
        }
	});
    
    //修改子模块
    $("#modifyModule").click(function(e){
		$('#opType').val('modify');
		var moduleId = $("#curModuleId").val();
		loadModule("initmodify",moduleId);
	});
    
    //删除模块
    $("#delModule").click(function(e){
    	$.confirm("是否删除本模块？", function(r){
			if (r) {
	 			var moduleId = $("#curModuleId").val();
		       	$.ajax({
		          	type : "POST",
		           	cache: false, 
		       		async : true,
					url  : "m/module/delete",
					dataType : "json",
					data : {moduleID : moduleId},
		        	success : function(data){
		        		if ($.checkErrorMsg(data) ) {
//		        			 $.alert("删除成功！");
		                     
		                     $("#manageToolBar").hide();
		                     $("#systemModuleMain").html("<div class=\"czbks\">&nbsp;</div>");
		                     
		                      //删除节点
		                     childModulesTree.removeNode(moduleId);
		        		}
					}
				});
			}
		});
	});
    
    //排序子模块
    $("#sortSubModule").click(function(e){
    	$('#opType').val('initorder');
  		var subSystemId = $("#curSubSystemId").val();
   		var moduleId = $("#curModuleId").val();
   		loadModule("initorder",moduleId, subSystemId);
	});
    
    //绑定模块动作
    $("#bindModuleAction").click(function(e){
  		$('#opType').val('initbinding');
  		var moduleId = $("#curModuleId").val();
  		loadModule("initbinding",moduleId);
	});
}

//系统列表发生选择事情时的回调函数
function subSystemList_OnChange(value,text){
    $("#searchResults").empty();
    //保存当前系统信息
    $("#curSubSystemId").val(value);
    $("#curSubSystemName").val(text);
    //清空值
   	$('#titlName').html("");
  	$('#curModuleId').val("");
 	$('#curModuleName').val("");
 	
 	$("#manageToolBar").hide();
    $("#systemModuleMain").html("<div class=\"czbks\">&nbsp;</div>");
    loadTree(value);
}

function loadTree(systemID) {
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : "m/module/subsystemtree",
		dataType : "json",
		data : {subSystemID : systemID},
		success : function(data){
	        $("#systemModules").html(data.htmlValue);
	        childModulesTree = $("#systemModules").simpleTree({
	        	animate: true,
	        	autoclose:false,
	        	selectRoot : true,
	        	json: true,
	        	afterClick : systemModulesClick})[0];
		}
	});
}

//单击树节点进行加载信息
function systemModulesClick(o){
	childModulesTree.expandNode(o);//点击时展开下级
    var moduleId = $(o).attr("moduleid");
    
    if ($.trim(moduleId) != "") {
        var p = $("#systemModules")[0];
        if ($(o).offset().top < $(p).offset().top) {
            p.scrollTop = p.scrollTop - ($(p).offset().top - $(o).offset().top) - (p.clientHeight / 2);
        }else if ($(o).offset().top + $(o).height() > $(p).offset().top + $(p).height()) {
      		p.scrollTop = p.scrollHeight - ($(o).offset().top + p.scrollTop - $(p).offset().top - p.clientHeight) + (p.clientHeight / 2);
     	}
     	$('#opType').val('view');
     	loadModule("view", moduleId);
    } else {
    	//清空
        $('#titlName').html("");
        $('#curModuleId').val("");
        $('#curModuleName').val("");
        //显示工具栏
        $('#manageToolBar').show();
        $("#addSubModule,#sortSubModule").removeAttr("disabled").removeClass("icoNone");
        $("#modifyModule,#delModule,#bindModuleAction").attr("disabled","true").addClass("icoNone");      
        $("#systemModuleMain").html("<div class=\"czbks\">&nbsp;</div>");
    }
}

//查找模块
function searchModlue(){
	if($.validInput("curSubSystemId", "子系统", true)){
		return false;
	}
	if($.validInput("searchModuleName", "查询条件", true)){
		return false;
	}

    $.ajax({
		type : "POST",
		cache: false, 
        url  : "m/module/query",
        dataType : "json",
        data : {subSystemID : $("#curSubSystemId").val(),
                name : $.trim($("#searchModuleName").val())},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.modules;
        		if (dataList) {
        			var strHtml = "<ul>";
	        		$(dataList).each(function(i) {
	                   strHtml += "<li><a class=\"opLink\" onclick=\"goToModule('" + dataList[i].moduleID + "')\">" + dataList[i].name + "</a></li>";
	        		});
	                strHtml+="</ul>";
	                
                	$("#searchResults").html(strHtml);
        		}
        	}
        }
	});
}

//刷新并定位到某个模块
function goToModule(moduleId){
    $.ajax({
        type : "POST",
        cache: false, 
        url  : "m/module/parentlist",
        dataType : "json",
        data : {moduleID : moduleId},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		 childModulesTree.expandNodePath(data.msg.text);
        	}
        }
	});
}

//载入模块
function loadModule(servletAction,moduleid,subsystemid){
	var url = "m/module/"+ servletAction;
    var data = {moduleID: moduleid ? moduleid : "",
    		subSystemID : subsystemid ? subsystemid : ""
    	};
	$.ajaxLoadPage("systemModuleMain", url, data);
}
   
////左右模块拖拉,如果鼠标移动太快的话，最小宽度会有偏差；
//function MouseDownToResize(e,obj){ 
//	e = e || event;
//	obj.mouseDownX=e.clientX; 	
//	obj.pareneTdW=obj.parentElement.offsetWidth; 
//	obj.pareneTableW=theObjTable.offsetWidth; 
//	obj.setCapture(); 
//} 
//function MouseMoveToResize(e,obj){ 
//	e = e || event;
//	if(!obj.mouseDownX) return false; 
//		var newWidth=obj.pareneTdW + e.clientX-obj.mouseDownX; 
//	if(newWidth>=185) 
//	{ 
//		obj.parentElement.style.width = newWidth; 
//		theObjTable.style.width="100%"; 
//		subsystemSelector.resize(newWidth-31);
//	} else{return}
//} 
//function MouseUpToResize(e,obj){ 
//	obj.releaseCapture(); 
//	obj.mouseDownX=0; 
//} 


