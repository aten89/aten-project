$(initPostManage);

function initPostManage(){ 
    
    //取得操作类型
    var action = $("#opType").val();
    if($.trim(action) == ""){
        return;
    }
   
	switch(action){
        case "add" :             
            $("#addSubPost,#editPost,#delPost,#sortSub,#bindPerson").attr("disabled","true").addClass("icoNone");
            
            $("#savePost,#resetPost,#saveAddPost").removeAttr("disabled").removeClass("icoNone");
            
            $("#postname,#desc").removeAttr("readonly");
           	$("#showGroup").removeAttr("disabled").removeClass("icoNone");
            $("#showPost").attr("disabled","true").addClass("icoNone");
            
            //默认值 
            $("#parentid").val($("#curPostId").val());
            $("#parentname").val($("#curPostName").val());
            //重写重置按扭
    		$("#resetPost").click(function(){
    			document.postFrm.reset();
            	$("#parentid").val($("#curPostId").val());
            	$("#parentname").val($("#curPostName").val());
    			return false;
    		});
            bindEvent();
            break;
        case "modify" :
            $("#addSubPost,#delPost,#sortSub,#bindPerson").removeAttr("disabled").removeClass("icoNone");
            $("#editPost").attr("disabled","true").addClass("icoNone");
            
            $("#savePost,#resetPost").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddPost").attr("disabled","true").addClass("icoNone");
            
            $("#postname,#desc").removeAttr("readonly");
           	$("#showPost,#showGroup").removeAttr("disabled").removeClass("icoNone");
            bindEvent();     
            break;
        case "view" : 
            $("#addSubPost,#editPost,#delPost,#sortSub,#bindPerson").removeAttr("disabled").removeClass("icoNone");
            
            $("#savePost,#resetPost,#saveAddPost").remove();
            
            $("#showPost,#showGroup").attr("disabled","true").addClass("icoNone");

            //标题
            $('#titlName').html("—" + $("#postname").val());
            //保存页面值
            $("#curPostId").val($("#postid").val());
            $("#curPostName").val($("#postname").val());
            
            $("#postname").parent().text($("#postname").val());
            $("#parentname").parent().text($("#parentname").val());
            $("#groupname").parent().text($("#groupname").val());
            $("#desc").parent().text($("#desc").val());
            
            break;
    }

    //显示工具栏
    $('#allTool').show();
}

//绑定事件
function bindEvent(){
    $("#savePost").click(function(){
        	savePost(false);
        }
    );
    $("#saveAddPost").click(function(){
        	savePost(true);
        }
    );
    
    //管理者的绑定
    $('#showGroup').click(function(){
    	var selector = new DeptDialog(BASE_PATH);
    	selector.setCallbackFun(function(retVal){
			if (retVal != null) {
				$("#groupid").val(retVal.id);
				$("#groupname").val(retVal.name);
			}
		});
		selector.openDialog("single");
		
    });     
        
    //上级部门绑定
    $('#showPost').click(function(){
    	var selector = new PostDialog(BASE_PATH);
    	selector.setCallbackFun(function(retVal){
			if (retVal != null) {
				$("#parentid").val(retVal.id);
				$("#parentname").val(retVal.name);
			}
		});
		selector.openDialog("single");
    });
}

//保存
function savePost(saveAndAdd){
	if($.validInput("postname", "名称", true)){
		return false;
	}
	if($.validInput("desc", "备注", false, null, 500)){
		return false;
	}
	
   if($("#parentid").val() != "" && $("#postid").val() == $("#parentid").val()){
        $.alert("不能选自己做为上级!");
        return false;
    } 
    
    var servletAction = $("#opType").val();
    var parentid = $.trim($('#parentid').val());
    var groupid = $('#groupid').val();
    var postname = $.trim($('#postname').val());
    var desc = $.trim($('#desc').val());
	var postId = $('#postid').val();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/post/"+servletAction,
        dataType : "json",
        data : "parentPostID="+parentid
        		+"&groupID="+groupid
        		+"&postName="+postname
        		+"&description="+desc
        		+"&postID="+postId,
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
                $.alert("职位保存成功!"); 
                var newGroupID= data.msg.text;
               //增加树节点
                if (servletAction == "add"){
//					listPostTree.addNodes(parentNode, "<li id=\"" + data.msg.text + "\" postid=\"" + data.msg.text + "\">" + "<span postid=\"" + data.msg.text + "\">" + postname + "</span></li>");
                    listPostTree.addNode(parentid, newGroupID, postname, {postid :  newGroupID});
                    //模拟树节点的点击事件,增加时用返回的编号
                    if(saveAndAdd){
                        $("#addSubPost").removeAttr("disabled").click();
                    }else{
//                        $("li[postid=\""+data.msg.text+"\"] > span","#postTree").click();
                    	listPostTree.clickNode(newGroupID);
                    }
                }else{
                    if ($("#oldparentid").val() != parentid){
	                    listPostTree.removeNode(postId);
	                    listPostTree.reloadNodes(parentid, "m/post/subposts?postID=" + parentid);

	                    //$("li[postid=\""+postId+"\"] > span","#postTree").click();

	                    $('#curPostId').val("");
	                    $('#curPostName').val("");
	                    $('#allTool').hide();      
	                    $("#postModuleMain").html("<div class=\"czbks\">&nbsp;</div>");

                    }else{
//                        $("li[postid=\""+postId+"\"] > span","#postTree").text(postname);                 
//                        $("li[postid=\""+postId+"\"] > span","#postTree").click();
                        listPostTree.modifyNode(postId, postname);
                        listPostTree.clickNode(postId);
                    }                                         
                }                       
        	}
        }
    }); 
}