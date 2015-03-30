$(initPasswordModify);

function initPasswordModify(){
    
    //保存密码
    $("#savePwd").click(function(){
    	if($.validInput("oldPwd", "旧密码", true)){
			return false;
		}
		if($.validInput("newPwd", "新密码", true)){
			return false;
		}
		if($.validInput("newPwdAg", "新密码确认", true)){
			return false;
		}
        if($("#newPwd").val() != $("#newPwdAg").val()){
            $.alert("两次输入的密码不一致");
            return false;
        }
    
		$.ajax({
			type : "POST",
	       	cache: false,
	       	url  : "l/frame/setpsw",
	       	dataType : "json",
	       	data : {
				oldPassword : $("#oldPwd").val(),
				newPassword : $("#newPwd").val()
	       	},
			success : function(data)  {
        		if ($.checkErrorMsg(data) ) {
        			$.alert("密码修改成功！", function(){
        				resetForm();
        				//如果是首页弹出强制修改密码的窗口，回调关闭窗口
	        			if (parent.window.changePasswordDialogCloseFun) {
	        				parent.window.changePasswordDialogCloseFun();
	        			}
        			});
        		}
			}
		});
	});
    
    //重置清空
    $("#reset").click(function(){
		resetForm();
    });
};

function resetForm() {
	$("#oldPwd").val("");
	$("#newPwd").val("");
	$("#newPwdAg").val("");
}

