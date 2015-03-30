$(initBindService);

//初始化页面
function initBindService(){
	//$.getMainFrame().getCurrentTab().setTitle("服务-接口帐号","接口帐号绑定服务");
    $("#modActorAcc").removeAttr("disabled").removeClass("icoNone");
    $("#bindService").attr("disabled","true").addClass("icoNone");
    
    $("#saveBind").attr("disabled","true").addClass("icoNone");
    
	$(".infoTip").html("当前编辑的接口帐号："+$("#hidAccountId").val()+"&nbsp;&nbsp;&nbsp;显示名称：" 
			+ $("#hidDisplayName").val()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;锁定状态：" 
			+ ($("#hidIsLock").val()=="Y" ? "锁定":"未锁定"));
	var accountId=$("#hidAccountId").val();
	//查询
	$("#searchSer").click(function(){
		getSer($.trim($("#sername").val()));
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
	getExSer(accountId);
	//回车搜索
	$.EnterKeyPress($("#sername"),$("#searchSer"));
}

//取得绑定角色
function getExSer(accountId){
	$("#bindedSer").empty();
    $.ajax({
        type : "POST",
        cache: false,
        url : "m/actor_account/bindedservices",
        dataType : "json",
        data : {accountID : accountId?accountId:""},
        success : function(data) {
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.services;
        		if (dataList) {
        			var StrHtml = "";
					$(dataList).each(function(i) {
						StrHtml+="<option value=\"" + dataList[i].serviceID + "\">" + dataList[i].serviceName + "</option>";
					});
					$("#bindedSer").html(StrHtml);	 
        		}
        	}
        	getSer();
        }
    });	
}


//取得角色
function getSer(servicename){
	$("#service").empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/service/xmlservices",
        dataType : "json",
        data : {serviceName : servicename?servicename:""},
        success : function(data) {
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.services;
        		if (dataList) {
        			var StrHtml = "";
					$(dataList).each(function(i) {
						 //已选的不列出
			            if ($("option[value='"+dataList[i].serviceID+"']","#bindedSer").length == 0){
			                StrHtml+="<option value=\"" + dataList[i].serviceID + "\">" + dataList[i].serviceName + "</option>";
			            }	
					});
					$("#service").html(StrHtml);
				}
        	}
        }
    });	
}

//添加绑定列表
function addBind(){
	$("option:selected","#service").each(function(){
		if ($("option[value='"+$(this).val()+"']","#bindedSer").length == 0){
			$(this).appendTo($("#bindedSer"));
		} else{
			$.alert("该服务已经绑定!");
		}
	});
    $("#saveBind").removeAttr("disabled").removeClass("icoNone");
}

//删除绑定列表
function delBind(){
	$("option:selected","#bindedSer").each(function(){
		if ($("option[value='"+$(this).val()+"']","#service").length == 0){ 
			$(this).appendTo($("#service"));
		} else {
			$(this).remove();
		}
	});
	$("#saveBind").removeAttr("disabled").removeClass("icoNone");
}

//保存绑定
function saveBind(accountId){
	var bindList="";
	$("option","#bindedSer").each(function(){
		bindList+="&serviceIDs="+$(this).val();
	});
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/actor_account/bindservice",
        dataType : "json",
        data : (accountId?"&accountID=" + accountId : "") + bindList,
        success :  function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert("服务绑定成功!");      
        		$("#saveBind").attr("disabled","true").addClass("icoNone");
        	}
		}
    });	
}