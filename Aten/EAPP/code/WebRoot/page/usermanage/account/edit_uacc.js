var currentTab = null;
var lockSelect;
var flagSelect;
$(initUserAccount);

function initUserAccount(){
	currentTab = $.getMainFrame().getCurrentTab();
	
	$('.invokeBoth').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});
	
    //取得操作类型
    var action = $("#hidAction").val();
    if($.trim(action) == ""){
        return;
    };
    //记住值
	$("#hidDisplayName").val($("#displayname").val());
	$("#hidIsLock").val($("#hidislock").val());
    switch(action){
        case "add" : 
        	currentTab.setTitle("新增用户帐号", "新增用户帐号");
        	$("#addUserAcc").attr("disabled","true").addClass("icoNone");
            $("#modUserAcc").attr("disabled","true").addClass("icoNone");
            $("#bindRole").attr("disabled","true").addClass("icoNone");
            $("#bindOrg").attr("disabled","true").addClass("icoNone");
            $("#resetPwd").attr("disabled","true").addClass("icoNone");
            
            $("#saveUserAcc").removeAttr("disabled").removeClass("icoNone");
            $("#resetUserAcc").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddUserAcc").removeAttr("disabled").removeClass("icoNone");
            
            $("#desc,#iplimit").removeAttr("readonly");
            $("#displayname").removeAttr("readonly");
            $("#accountid").removeAttr("readonly");
            
    		lockSelect = $("#islock").ySelect({width:95});
            lockSelect.select(0);
    		flagSelect = $("#cpflag").ySelect({width:95});
    		flagSelect.select(0);
            //重写重置按扭
    		$("#resetUserAcc").click(function(){
    			document.mainForm.reset();
    			lockSelect.select(0);
    			flagSelect.select(0);
    			return false;
    		});
            bindEvent();
            break;
        case "modify" : 
        	currentTab.setTitle("修改用户帐号",$("#displayname").val() + "（" + $("#accountid").val() + "）");
        	$("#addUserAcc").removeAttr("disabled").removeClass("icoNone");
            $("#modUserAcc").attr("disabled","true").addClass("icoNone");
           
    		lockSelect = $("#islock").ySelect({width:95});
            lockSelect.select($("#hidislock").val());
    		flagSelect = $("#cpflag").ySelect({width:95});
    		flagSelect.select($("#hidcpflag").val());
            if($("#hidislock").val()=='N'){
            	$("#bindRole").removeAttr("disabled").removeClass("icoNone");
            	$("#bindOrg").removeAttr("disabled").removeClass("icoNone");
            	 $("#resetPwd").removeAttr("disabled").removeClass("icoNone");
            } else {
            	$("#bindRole").attr("disabled","true").addClass("icoNone");
            	$("#bindOrg").attr("disabled","true").addClass("icoNone");
            	$("#resetPwd").attr("disabled","true").addClass("icoNone");
            }
            
            $("#accountid").hide().parent().append($("#accountid").val());
             
            $("#saveUserAcc").removeAttr("disabled").removeClass("icoNone");
            $("#resetUserAcc").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddUserAcc").attr("disabled","true").addClass("icoNone");
            
            $("#desc,#iplimit").removeAttr("readonly");
            $("#displayname").removeAttr("readonly");
			//重写重置按扭
    		$("#resetUserAcc").click(function(){
    			document.mainForm.reset();
    			lockSelect.select($("#hidislock").val());
    			flagSelect.select($("#hidcpflag").val());
    			return false;
    		});
            bindEvent();
            break;
        case "view" : 
        	currentTab.setTitle("查看用户帐号",$("#displayname").val() + "（" + $("#accountid").val() + "）");
        	$("#addUserAcc").removeAttr("disabled").removeClass("icoNone");
            $("#modUserAcc").removeAttr("disabled").removeClass("icoNone");
//    		$("#islock").ySelect({width:95,isdisabled:true}).select($("#hidislock").val());
//    		$("#cpflag").ySelect({width:95,isdisabled:true}).select($("#hidcpflag").val());
            if($("#hidislock").val()=='N'){
            	$("#bindRole").removeAttr("disabled").removeClass("icoNone");
            	$("#bindOrg").removeAttr("disabled").removeClass("icoNone");
            	$("#resetPwd").removeAttr("disabled").removeClass("icoNone");
            } else {
            	$("#bindRole").attr("disabled","true").addClass("icoNone");
            	$("#bindOrg").attr("disabled","true").addClass("icoNone");
            	$("#resetPwd").attr("disabled","true").addClass("icoNone");
            }
            
            $("#saveUserAcc,#resetUserAcc,#saveAddUserAcc").remove();

 			$("#invaliddate").attr("disabled","true");
 			
 			$("#accountid").parent().text($("#accountid").val());
 			$("#displayname").parent().text($("#displayname").val());
 			$("#invaliddate").parent().text($("#invaliddate").val());
 			$("#desc").parent().text($("#desc").val());
 			$("#iplimit").parent().text($("#iplimit").val());
 			$("#islock").parent().text($("#hidislock").val()=='Y' ? '锁定':'未锁定');
 			$("#cpflag").parent().text($("#hidcpflag").val()=='Y' ? '登录时':'不约束');
            break;
    };
}

//绑定事件
function bindEvent(){
    $("#saveUserAcc").click(function(){
        	saveUserAccInfo(false);
        }
    );
    $("#saveAddUserAcc").click(function(){
        	saveUserAccInfo(true);
        }
    );
};

/*保存*/
function saveUserAccInfo(saveAndAdd){ 
	 if($.validInput("accountid", "帐号", true,'\u4e00-\u9fa5')){
		return false;
	}
	if($.validInput("displayname", "显示名", true)){
		return false;
	}
	if($.validInput("desc", "备注", false, null, 500)){
		return false;
	}

    $.ajax({
	   type : "POST",
	   cache: false,
	   url  : "m/user_account/"+$("#hidAction").val(),
	   dataType : "json",
	   data : "accountID="+$.trim($("#accountid").val())
	   			+"&displayName="+$.trim($("#displayname").val())
	   			+"&isLock="+$.trim(lockSelect.getValue())
	   			+"&invalidDate="+$.trim($("#invaliddate").val())
	   			+"&changePasswordFlag="+$.trim(flagSelect.getValue())
	   			+"&description="+$.trim($("#desc").val())
	   			+"&loginIpLimit="+$.trim($("#iplimit").val()),
      success : function(data){
      		if ($.checkErrorMsg(data) ) {
      			$.alert("用户帐号保存成功！");
		        //执行回调函数，刷新父列表
               	currentTab.doCallback();
		        //保存成功时，进行下一步操作
		        if(saveAndAdd){
		            $("#addUserAcc").removeAttr("disabled").click();
		        } else{
		        	//如果是新增，保存之后应转为修改
		        	var action = $("#hidAction").val();
		        	if (action == "add"){
		        		$("#hidAccountId").val($("#accountid").val());
		        	}
		            $("#hidAction").val("view");
		            getLoadTypeAndModule();
		        }
      		}
		}
	});
}