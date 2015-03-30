var currentTab = null;
var lockSelect;
//var flagSelect;
$(initActorAccount);

function initActorAccount(){
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
        	currentTab.setTitle("新增接口帐号", "新增接口帐号");
        	$("#addActorAcc").attr("disabled","true").addClass("icoNone");
            $("#modActorAcc").attr("disabled","true").addClass("icoNone");
            $("#bindService").attr("disabled","true").addClass("icoNone");
            
            $("#saveActorAcc").removeAttr("disabled").removeClass("icoNone");
            $("#resetActorAcc").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddActorAcc").removeAttr("disabled").removeClass("icoNone");
            
            $("#desc").removeAttr("readonly");
            $("#Detail").show();
            $("#displayname").removeAttr("readonly");
            $("#accountid").removeAttr("readonly");
            $("#credence").removeAttr("readonly");
            
    		lockSelect = $("#islock").ySelect({width:95});
            lockSelect.select(0);
//    		flagSelect = $("#cpflag").ySelect({width:95});
//    		flagSelect.select(0);
    		//重写重置按扭
    		$("#resetActorAcc").click(function(){
    			document.mainForm.reset();
    			lockSelect.select(0);
//    			flagSelect.select(0);
    			return false;
    		});
            bindEvent();
            break;
        case "modify" : 
        	currentTab.setTitle("修改接口帐号",$("#displayname").val() + "（" + $("#accountid").val() + "）");
        	$("#addActorAcc").removeAttr("disabled").removeClass("icoNone");
            $("#modActorAcc").attr("disabled","true").addClass("icoNone");
            lockSelect = $("#islock").ySelect({width:95});
            lockSelect.select($("#hidislock").val());
//    		flagSelect = $("#cpflag").ySelect({width:95});
//    		flagSelect.select($("#hidcpflag").val());
            if($("#hidislock").val()=='N'){
            	$("#bindService").removeAttr("disabled").removeClass("icoNone");
            } else {
            	$("#bindService").attr("disabled","true").addClass("icoNone");
            }
            
            $("#saveActorAcc").removeAttr("disabled").removeClass("icoNone");
            $("#resetActorAcc").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddActorAcc").attr("disabled","true").addClass("icoNone");
            
            $("#accountid").hide().parent().append($("#accountid").val());
            $("#desc").removeAttr("readonly");
            $("#Detail").show();
            $("#displayname").removeAttr("readonly");
            $("#credence").removeAttr("readonly");
			//重写重置按扭
    		$("#resetActorAcc").click(function(){
    			document.mainForm.reset();
    			lockSelect.select($("#hidislock").val());
//    			flagSelect.select($("#hidcpflag").val());
    			return false;
    		});
            bindEvent();
            break;
        case "view" : 
            currentTab.setTitle("查看接口帐号",$("#displayname").val() + "（" + $("#accountid").val() + "）");
            $("#addActorAcc").removeAttr("disabled").removeClass("icoNone");
            $("#modActorAcc").removeAttr("disabled").removeClass("icoNone");
 //           $("#islock").ySelect({width:95,isdisabled:true}).select($("#hidislock").val());
 //   		$("#cpflag").ySelect({width:95,isdisabled:true}).select($("#hidcpflag").val());
            if($("#hidislock").val()=='N'){
            	$("#bindService").removeAttr("disabled").removeClass("icoNone");
            } else {
            	$("#bindService").attr("disabled","true").addClass("icoNone");
            }
            
            $("#saveActorAcc,#resetActorAcc,#saveAddActorAcc").remove();
            
            $("#Detail").hide();
 			$("#invaliddate").attr("disabled","true");
 			
 			$("#accountid").parent().text($("#accountid").val());
 			$("#displayname").parent().text($("#displayname").val());
 			$("#credence").parent().text("********");

 			$("#invaliddate").parent().text($("#invaliddate").val());
 			$("#desc").parent().text($("#desc").val());
 			$("#islock").parent().text($("#hidislock").val()=='Y' ? '锁定':'未锁定');
// 			$("#cpflag").parent().text($("#hidcpflag").val()=='Y' ? '可修改':'不可修改');
            break;
    };
}

//绑定事件
function bindEvent(){
    $("#saveActorAcc").click(function(){
        	saveActorAccInfo(false);
        }
    );
    $("#saveAddActorAcc").click(function(){
        	saveActorAccInfo(true);
        }
    );
};

/*保存*/
function saveActorAccInfo(saveAndAdd){
    if($.validInput("accountid", "帐号", true,'\u4e00-\u9fa5')){
		return false;
	}
	if($.validInput("displayname", "显示名", true)){
		return false;
	}
	if($.validInput("credence", "密码凭证", true)){
		return false;
	}
	if($.validInput("desc", "备注", false, null, 500)){
		return false;
	}
	
	$.ajax({
		type : "POST",
	   	cache: false,
	  	url  : "m/actor_account/"+$("#hidAction").val(),
		dataType : "json",
	   	data : "accountID="+$.trim($("#accountid").val())
	   			+"&displayName="+$.trim($("#displayname").val())
	   			+"&isLock="+lockSelect.getValue()
	   			+"&invalidDate="+$("#invaliddate").val()
	   			+"&changePasswordFlag=N"//+flagSelect.getValue()
	   			+"&credence="+$("#credence").val()
	   			+"&description="+$.trim($("#desc").val()),
		success : function(data){
			if ($.checkErrorMsg(data) ) {
      			$.alert("接口帐号保存成功！");
		        //刷新父列表
               	currentTab.doCallback();
		        //保存成功时，进行下一步操作
		        if(saveAndAdd){
		            $("#addActorAcc").removeAttr("disabled").click();
		        } else{
		        	//如果是新增，保存之后应转为修改
		        	var action = $("#hidAction").val();
		        	if (action == "add"){
		        		$("#hidAccountId").val($("#accountid").val());
		        	}
		            $("#hidAction").val("view");
		            getLoadTypeAndModule();
		        };
			}
		}
	});
}

