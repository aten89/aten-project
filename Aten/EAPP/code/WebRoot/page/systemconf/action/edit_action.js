var currentTab = null;

$(initActionManage);

function initActionManage(){
	currentTab = $.getMainFrame().getCurrentTab();
    
    //取得操作类型
    var action = $("#hidAction").val();
    if($.trim(action) == ""){
        return;
    };
    
    switch(action){
        case "add" : 
            currentTab.setTitle("新增动作", "创建新动作");
            $("#addAction,#modifyAction").attr("disabled","true").addClass("icoNone");
            $("#actoinName,#actionkey,#logourl,#tips,#description").removeAttr("readonly");
            bindEvent();
            break;
        case "modify" : 
            currentTab.setTitle("修改动作",$("#actoinName").val());
            $("#modifyAction").attr("disabled","true").addClass("icoNone");
            $("#saveAddAction").removeAttr("disabled").removeClass("icoNone");
            $("#actoinName,#logourl,#tips,#description").removeAttr("readonly");
            $("#saveAddAction").attr("disabled","true").addClass("icoNone");
            
            $("#actionkey").hide().parent().append($("#actionkey").val());
            bindEvent();
            break;
        case "view" : 
            currentTab.setTitle("查看动作",$("#actoinName").val());
            $("#addAction,#modifyAction").removeAttr("disabled").removeClass("icoNone");
            $("#saveAction,#resetAction,#saveAddAction").remove();
            
            $("#actoinName").parent().text($("#actoinName").val());
 			$("#actionkey").parent().text($("#actionkey").val());
 			$("#logourl").parent().text($("#logourl").val());
 			$("#tips").parent().text($("#tips").val());
 			$("#description").parent().text($("#description").val());
            break;
    };
}

//绑定事件
function bindEvent(){
    $("#saveAction").click(function(){
        	saveActionInfo(false);
        }
    );
    $("#saveAddAction").click(function(){
        	saveActionInfo(true);
        }
    );
};


//保存信息
function saveActionInfo(saveAndAdd){
	if($.validInput("actoinName", "动作名称", true)){
		return false;
	}
	
	if($.validInput("actionkey", "动作代码", true)){
		return false;
	}
	
	if($.validInput("description", "描述", false, null, 500)){
		return false;
	}
		
	var actoinName = $.trim($("#actoinName").val());
	var actionkey = $.trim($("#actionkey").val());
	var logourl = $.trim($("#logourl").val());
	var tips = $.trim($("#tips").val());
	var description = $.trim($("#description").val());
	
	$.ajax({
	   type : "POST",
	   cache: false, 
	   url  : "m/action/" + $("#hidAction").val(),
	   dataType : "json",
	   data : "actionID="+$("#hidActionId").val()
	   			+"&name="+actoinName
	   			+"&actionKey="+actionkey
	   			+"&logoURL="+logourl
	   			+"&tips="+tips
	   			+"&description="+description,
		success : function(data){
			if ($.checkErrorMsg(data) ) {
	      		$.alert("动作信息保存成功！");
	             //刷新父列表
				currentTab.doCallback();
		         //保存成功时，进行下一步操作
				if(saveAndAdd){
					$("#addAction").removeAttr("disabled").click();
				}else{
		        	var action = $("#hidAction").val();
		        	if (action == "add"){
		        		 $("#hidActionId").val(data.msg.text);
		        	}
		            $("#hidAction").val("view");
		            getLoadTypeAndModule();
				}
			}
		}
	});
}