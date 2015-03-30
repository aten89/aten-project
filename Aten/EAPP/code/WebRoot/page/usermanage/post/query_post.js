var listPostTree= null;

$(initLisPost);

//初始化页面
function initLisPost(){
    //currentTab.setTitle("管理-职位管理", "职位管理");
//    $.handleRights({
//        "addSubPost" : $.SysConstants.ADD,
//        "editPost" : $.SysConstants.MODIFY,
//        "delPost" : $.SysConstants.DELETE,
//        "sortSub" : $.SysConstants.ORDER,
//        "bindPerson" : $.SysConstants.BIND_USER,
//        "listSearchPost" : $.SysConstants.QUERY
//    });
    
    
    //高度设置
//    $("#subRight").css("height",document.documentElement.clientHeight - 60);
//    $("#tdScaleMove").css("height",document.documentElement.clientHeight - 48);
//	$("#postList").css("height",document.documentElement.clientHeight - 315);
    //初始化树
    initPostTree();
    
    $("#listSearchPost").click(function(){
        searchPost();
    });
    
    //回车搜索
    $.EnterKeyPress($("#listPostName"),$("#listSearchPost"));
    
    //绑定添加
    $('#addSubPost').click(function(){
        $('#opType').val('add');
        var postID = $("#curPostId").val();
        loadModule("initadd");
         //如果下级是异步且未加载，而触发加载
        if (postID && postID != "" && $("li[postid=\"" + postID + "\"] > ul.ajax").length != 0) {
            $("li[postid=\"" + postID + "\"] > img").click();
        };
    }); 
    
    //绑定修改    
    $('#editPost').click(function(){
        $('#opType').val('modify');
        var postID = $("#curPostId").val();
        loadModule("initmodify",postID);
    });

    //绑定删除
    $('#delPost').click(function(){
    	$.confirm("是否删除该职位?", function(r){
			if (r) {
				var postID = $("#curPostId").val();
		        $.ajax({
		            type : "POST",
		            cache: false, 
		            async : true,
		            url  : "m/post/delete",
		            dataType : "json",
		            data : {postIDs : postID},
		            success : function(data){
		            	if ($.checkErrorMsg(data) ) {
//		            		 $.alert("删除成功！");
		                    
		                    $("#allTool").hide();
		                    $("#postModuleMain").html("<div class=\"czbks\">&nbsp;</div>");
		                    
		                     //删除节点
		                    listPostTree.removeNode(postID);
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
    
    //绑定用户
    $('#bindPerson').click(function(){
        $('#opType').val('initbinduser');
        loadModule("initbinduser");
    });  
    
    /*关闭窗口*/
    $("#closeWindow").click(
        function(){
            $.getMainFrame().getCurrentTab().close();
        }
    );      
}

//载入群组树
function initPostTree(){
	 //载入群组树
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : "m/post/subposts",
		dataType : "json",
		success : function(data){
	        $("#postTree > li > ul").html(data.htmlValue);
	        listPostTree = $("#postTree").simpleTree({
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
function clickTree(o){
	listPostTree.expandNode(o);//点击时展开下级
    var postId = $(o).attr("postid");   
    
    if ($.trim(postId) != ""){
        var p = $("#postTree")[0];
        if($(o).offset().top < $(p).offset().top){
            p.scrollTop = p.scrollTop - ($(p).offset().top - $(o).offset().top) - (p.clientHeight / 2);
        }else if($(o).offset().top + $(o).height() > $(p).offset().top + $(p).height()){
            p.scrollTop = p.scrollHeight - ($(o).offset().top + p.scrollTop - $(p).offset().top - p.clientHeight) + (p.clientHeight / 2);
        }
        $('#opType').val('view');
        loadModule("view", postId);
    }else{
        //清空
        $('#titlName').html("");
        $('#curPostId').val("");
        $('#curPostName').val("");
        //显示工具栏
        $('#allTool').show();
        $("#addSubPost,#sortSub").removeAttr("disabled").removeClass("icoNone");
        $("#editPost,#delPost,#bindRole,#bindPerson").attr("disabled","true").addClass("icoNone");      
        $("#postModuleMain").html("<div class=\"czbks\">&nbsp;</div>");
    }
}

//查询群组
function searchPost(){
	if($.validInput("listPostName", "查询条件", true)){
		return false;
	}

    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/post/query",
        dataType : "json",
        data : {postName : $("#listPostName").val()},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.posts;
        		if (dataList) {
        			var StrHtml = "<ul>";
        			$(dataList).each(function(i) {
        				StrHtml+="<li><a class=\"opLink\" onclick=\"goToPost('"+dataList[i].postID+"','"+dataList[i].idPath+"')\" id=\"" + dataList[i].postID + "\">" + dataList[i].postName + "</a></li>";
        			});
        			StrHtml+="</ul>";
		        	$("#postList").html(StrHtml);  
        		}
        	}
		}
    });  
}

//刷新并定位到某个群组
function goToPost(postId,parentList){
    listPostTree.expandNodePath(parentList.substr(0,parentList.length-1));
}

//载入模块
function loadModule(servletAction,postId){
	var url = "m/post/"+ servletAction;
    var data = {postID : postId?postId:""};
	$.ajaxLoadPage("postModuleMain", url, data);
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