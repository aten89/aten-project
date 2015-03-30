$(initBindRole);

//初始化页面
function initBindRole(){
    $("#bindPost,#saveBind").attr("disabled","true").addClass("icoNone");
    $("#addSubGroup,#sortSub,#editGroup,#delGroup,#bindPerson,#bindRole").removeAttr("disabled","true").removeClass("icoNone");
    
    var groupId = $('#curGroupId').val();
    
    //查询
    $("#searchPost").click(function(){
        getPost($.trim($("#postname").val()));
    });
    //添加
    $("#addBind").click(function(){
        addBind();
    }); 
    //删除
    $("#delBind").click(function(){
        delBind();
    }); 
    //保存
    $("#saveBind").click(function(){
        saveBind(groupId);
    });     
    getExPost(groupId);
    //回车搜索
    $.EnterKeyPress($("#postname"),$("#searchPost"));      
}

//取得绑定角色
function getExPost(groupId){
    $("#bindedPost").empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_group/bindedposts",
        dataType : "json",
        data : {groupID : groupId?groupId:""},
        success : function(data) {
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.posts;
        		if (dataList) {
        			var StrHtml = "";
        			$(dataList).each(function(i) {
        				StrHtml+="<option value=\"" + dataList[i].postID + "\">" + dataList[i].postName + "</option>";
        			});
        			$("#bindedPost").html(StrHtml);     
        		}
        	}
        	getPost();
        }
    }); 
}

//取得角色
function getPost(postname){
    $("#post").empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/post/xmlposts",
        dataType : "json",
        data : "binded=N" + (postname?"&postName=" + postname : ""),
        success : function(data) {
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.posts;
        		if (dataList) {
        			var StrHtml = "";
        			$(dataList).each(function(i) {
        				 //已选的不列出
			            if ($("option[value=\""+dataList[i].postID+"\"]","#bindedPost").length == 0){
			                StrHtml+="<option value=\"" + dataList[i].postID + "\">" + dataList[i].postName + "</option>";
			            }
        			});
        			$("#post").html(StrHtml); 
        		}
        	}
        }
    }); 
}


//添加绑定列表
function addBind(){
    $("option:selected","#post").each(function(){
        if ($("option[value='"+$(this).val()+"']","#bindedPost").length == 0){ 
            $(this).appendTo($("#bindedPost"));
        }else{
            $.alert("该角色已职位!");
        }
    });
    $("#saveBind").removeAttr("disabled").removeClass("icoNone");
}

//删除绑定列表
function delBind(){
    $("option:selected","#bindedPost").each(function(){
        if ($("option[value='"+$(this).val()+"']","#post").length == 0){ 
            $(this).appendTo($("#post"));
        }else{
            $(this).remove();
        }
    });
    $("#saveBind").removeAttr("disabled").removeClass("icoNone");
}

//保存绑定
function saveBind(groupId){
    var bindList="";
    $("option","#bindedPost").each(function(){
        bindList+="&postIDs="+$(this).val();
    });
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_group/bindpost",
        dataType : "json",
        data : (groupId?"&groupID=" + groupId : "") + bindList,
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert("职位绑定成功!");
		        $("#saveBind").attr("disabled","true").addClass("icoNone");
        	}
		}
    }); 
}