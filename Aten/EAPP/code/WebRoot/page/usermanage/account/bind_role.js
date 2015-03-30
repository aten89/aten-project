$(initBindRole);

//初始化页面
function initBindRole(){
    //$.getMainFrame().getCurrentTab().setTitle("角色-用户帐号", "用户帐号绑定角色");
    
    $("#modUserAcc").removeAttr("disabled").removeClass("icoNone");
    $("#bindRole").attr("disabled","true").addClass("icoNone");
    $("#bindOrg").removeAttr("disabled").removeClass("icoNone");
	$("#saveBind").attr("disabled","true").addClass("icoNone");
	
	$(".infoTip").html("当前编辑的用户帐号："+$("#hidAccountId").val()+"&nbsp;&nbsp;&nbsp;显示名称：" 
			+ $("#hidDisplayName").val()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;锁定状态：" 
			+ ($("#hidIsLock").val()=="Y" ? "锁定":"未锁定"));
	var accountId=$("#hidAccountId").val();
	//查询
	$("#searchRole").click(function(){
		getRole($.trim($("#rolename").val()));
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
		saveBind(accountId);
	});		
	getExRole(accountId);
    //回车搜索
    $.EnterKeyPress($("#rolename"),$("#searchRole"));	
}

//取得绑定角色
function getExRole(accountId){
	$("#bindedRole").empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/user_account/bindedroles",
        dataType : "json",
        data :  {accountID : accountId?accountId:""},
        success : function(data) {
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.roles;
        		if (dataList) {
        			var StrHtml = "";
					$(dataList).each(function(i) {
						StrHtml+="<option value=\"" + dataList[i].roleID + "\">" + dataList[i].roleName + "</option>";
					});
					$("#bindedRole").html(StrHtml);	 
        		}
        	}
        	getRole();
        }
    });	
}

//取得角色
function getRole(rolename){
	$("#role").empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_role/xmlroles",
        dataType : "json",
        data : {roleName : rolename?rolename:""},
        success :  function(data) {
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.roles;
        		if (dataList) {
        			var StrHtml = "";
					$(dataList).each(function(i) {
						 //已选的不列出
			            if ($("option[value=\""+dataList[i].roleID+"\"]","#bindedRole").length == 0){
			                StrHtml+="<option value=\"" + dataList[i].roleID + "\">" + dataList[i].roleName + "</option>";
			            }	
					});
					$("#role").html(StrHtml);
				}
        	}
        }
    });	
}

//添加绑定列表
function addBind(){
	$("option:selected","#role").each(function(){
		if ($("option[value='"+$(this).val()+"']","#bindedRole").length == 0){ 
			$(this).appendTo($("#bindedRole"));
		}else{
			$.alert("该角色已绑定!");
		}
	});
	$("#saveBind").removeAttr("disabled").removeClass("icoNone");
}

//删除绑定列表
function delBind(){
	$("option:selected","#bindedRole").each(function(){
		if ($("option[value='"+$(this).val()+"']","#role").length == 0){ 
			$(this).appendTo($("#role"));
		}else{
			$(this).remove();
		}
	});
	$("#saveBind").removeAttr("disabled").removeClass("icoNone");
}

//保存绑定
function saveBind(accountId){
	var bindList="";
	$("option","#bindedRole").each(function(){
		bindList+="&roleIDs="+$(this).val();
	});
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/user_account/bindrole",
        dataType : "json",
        data :  (accountId?"&accountID=" + accountId : "") + bindList,
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert("角色绑定成功!");      
        		$("#saveBind").attr("disabled","true").addClass("icoNone");  
        	}
		}
    });	
}