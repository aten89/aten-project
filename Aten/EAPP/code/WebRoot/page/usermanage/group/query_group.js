var listGroupTree= null;

$(initListGroup);

//初始化页面
function initListGroup(){
    //currentTab.setTitle("管理-机构管理", "机构管理");
//    $.handleRights({
//        "addSubGroup" : $.SysConstants.ADD,
//        "editGroup" : $.SysConstants.MODIFY,
//        "delGroup" : $.SysConstants.DELETE,
//        "sortSub" : $.SysConstants.ORDER,
//        "bindRole" : $.SysConstants.BIND_ROLE,
//        "bindPerson" : $.SysConstants.BIND_USER,
//        "listSearchGroup" : $.SysConstants.QUERY,
//        "bindPost" : $.SysConstants.BIND_POST
//    });
    
    
    //高度设置
//    $("#subRight").css("height",document.documentElement.clientHeight - 60);
//    $("#tdScaleMove").css("height",document.documentElement.clientHeight - 48);
//	$("#groupList").css("height",document.documentElement.clientHeight - 315);
    //初始化树
    initGroupTree("");
    
    $("#listSearchGroup").click(function(){
        searchGroup();
    });
    
    //回车搜索
    $.EnterKeyPress($("#listGroupName"),$("#listSearchGroup"));
    
    //绑定添加
    $('#addSubGroup').click(function(){
        $('#opType').val('add');
        var groupID = $("#curGroupId").val();
        loadModule("initadd");
         //如果下级是异步且未加载，而触发加载
        if (groupID && groupID != "" && $("li[groupid=\"" + groupID + "\"] > ul.ajax").length != 0) {
            $("li[groupid=\"" + groupID + "\"] > img").click();
        };
    }); 
    
    //绑定修改    
    $('#editGroup').click(function(){
        $('#opType').val('modify');
        var groupID = $("#curGroupId").val();
        loadModule("initmodify",groupID);
    });

    //绑定删除
    $('#delGroup').click(function(){
    	$.confirm("是否删除该群组?", function(r){
			if (r) {
				var groupID = $("#curGroupId").val();
		        $.ajax({
		            type : "POST",
		            cache: false, 
		            async : true,
		            url  : "m/rbac_group/delete",
		            dataType : "json",
		            data : {groupIDs : groupID},
		            success : function(data){
		            	if ($.checkErrorMsg(data) ) {
//		            		$.alert("删除成功！");
		                    
		                    $("#allTool").hide();
		                    $("#groupModuleMain").html("<div class=\"czbks\">&nbsp;</div>");
		                    
		                     //删除节点
		                    listGroupTree.removeNode(groupID);
		            	}
		            }
		        });
			}
		});       
    }); 
    
    //绑定排序
    $('#sortSub').click(function(){
        $('#opType').val('initorder');
        loadModule("initorder");
    });
    
    //绑定角色
    $('#bindRole').click(function(){
        $('#opType').val('initbindrole'); 
        loadModule("initbindrole");     
    });  
    
    //绑定用户
    $('#bindPerson').click(function(){
        $('#opType').val('initbinduser');
        loadModule("initbinduser");
    });  
    
    //绑定角色
    $('#bindPost').click(function(){
        $('#opType').val('initbindpost'); 
        loadModule("initbindpost");     
    });  
}

//载入群组树
function initGroupTree(value){
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : "m/rbac_group/subgroups",
		dataType : "json",
		data : {type : value},
		success : function(data){
	        $("#groupTree > li > ul").html(data.htmlValue);
	        listGroupTree = $("#groupTree").simpleTree({
	        	animate: true,
	        	autoclose:false,
	        	selectRoot : true,
	        	json: true,
	        	afterClick: clickTree
	        })[0];
		}
	});
}

//选中树节点
function clickTree(o){//alert(o.attr("groupid"));
	listGroupTree.expandNode(o);//点击时展开下级
    var groupId = $(o).attr("groupid");   
    
    if ($.trim(groupId) != ""){
        var p = $("#groupTree")[0];
        if($(o).offset().top < $(p).offset().top){
            p.scrollTop = p.scrollTop - ($(p).offset().top - $(o).offset().top) - (p.clientHeight / 2);
        }else if($(o).offset().top + $(o).height() > $(p).offset().top + $(p).height()){
            p.scrollTop = p.scrollHeight - ($(o).offset().top + p.scrollTop - $(p).offset().top - p.clientHeight) + (p.clientHeight / 2);
        }
        $('#opType').val('view');
        loadModule("view", groupId);
    }else{
        //清空
        $('#titlName').html("");
        $('#curGroupId').val("");
        $('#curGroupName').val("");
        $('#curGroupType').val("");
        //显示工具栏
        $('#allTool').show();
        $("#addSubGroup,#sortSub").removeAttr("disabled").removeClass("icoNone");
        $("#editGroup,#delGroup,#bindRole,#bindPerson,#bindPost").attr("disabled","true").addClass("icoNone");      
        $("#groupModuleMain").html("<div class=\"czbks\">&nbsp;</div>");
    }
}

//查询群组
function searchGroup(){
	if($.validInput("listGroupName", "查询条件", true)){
		return false;
	}
	
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_group/query",
        dataType : "json",
        data : {groupName : $("#listGroupName").val()},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.groups;
        		if (dataList) {
        			var StrHtml = "<ul>";
        			$(dataList).each(function(i) {
        				StrHtml+="<li><a class=\"opLink\" onclick=\"goToGroup('"+dataList[i].groupID+"','"+dataList[i].idPath+"')\" id=\"" + dataList[i].groupID + "\">" + dataList[i].groupName + "</a></li>";
        			});
        			StrHtml+="</ul>";
		        	$("#groupList").html(StrHtml);          
        		}
        	}
		}
    });  
}

//刷新并定位到某个群组
function goToGroup(groupId,parentList){
    listGroupTree.expandNodePath(parentList.substr(0,parentList.length-1));
}

//载入模块
function loadModule(servletAction,groupId){
	var url = "m/rbac_group/" + servletAction;
    var data = {groupID : groupId?groupId:""};
	$.ajaxLoadPage("groupModuleMain", url, data);
}


//左右模块拖拉,如果鼠标移动太快的话，最小宽度会有偏差；
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
//	} else{return}
//} 
//function MouseUpToResize(e,obj){ 
//	obj.releaseCapture(); 
//	obj.mouseDownX=0; 
//} 