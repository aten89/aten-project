var currentTab = null;
var subsystemSelector = null;
$(initPortletManage);

function initPortletManage(){
	currentTab = $.getMainFrame().getCurrentTab();
   	$("#hidPortletName").val($("#portletName").val());//记住版块名称
    switch($("#hidAction").val()){
        case "initadd" : 
            currentTab.setTitle("新增门户", "新增企业门户");
             //初始化系统下拉列表
			subsystemSelector = $("#subSystemList").ySelect({width: 166,json:true, url : "m/subsystem/allsystem",afterLoad:selectSystem});
            $("#addPortlet,#modifyPortlet,#bindRole").attr("disabled","true").addClass("icoNone");
            bindEvent();
            break;
        case "initmodify" : 
            currentTab.setTitle("修改门户",$("#portletName").val());
             //初始化系统下拉列表
			subsystemSelector = $("#subSystemList").ySelect({width: 166,json:true, url : "m/subsystem/allsystem",afterLoad:selectSystem});
            $("#addPortlet,#bindRole").removeAttr("disabled").removeClass("icoNone");
            $("#modifyPortlet").attr("disabled","true").addClass("icoNone");
            $("#savePortletAndAdd").attr("disabled","true").addClass("icoNone");
            bindEvent();
            break;
        case "view" : 
            currentTab.setTitle("查看门户",$("#portletName").val());
             //初始化系统下拉列表
			subsystemSelector = $("#subSystemList").ySelect({width: 166,json:true, url : "m/subsystem/allsystem",
				afterLoad:function(){
					selectSystem();
					$("#subSystemList").parent().text(subsystemSelector.getDisplayValue());
				}
			});
            $("#addPortlet,#modifyPortlet,#bindRole").removeAttr("disabled").removeClass("icoNone");
            $("#savePortlet,#savePortletAndAdd,#resetPortlet").remove();
            
            $("#portletName").parent().text($("#portletName").val());
 			$("#portletUrl").parent().text($("#portletUrl").val());
 			$("#morePortletUrl").parent().text($("#morePortletUrl").val());
 			$("#portletStyle").parent().text($("#portletStyle").val());
            break;
    }
    
    function selectSystem() {
    	var sysId = $.trim($("#hidSubSystemId").val());
    	if (sysId != ""){
    		subsystemSelector.select(sysId);
    	}
    }
}

//绑定事件
function bindEvent(){
    $("#savePortlet").click(function(){
        	savePorletInfo(false);
        }
    );
    $("#savePortletAndAdd").click(function(){
        	savePorletInfo(true);
        }
    );
}

function savePorletInfo(saveAndAdd) {
	if($.validInput("portletName", "板块标题", true)){
		return false;
	}
	if(subsystemSelector.getValue() == "") {
    	 $.alert("所属系统不能为空");
    	 return false;
    }
    if($.validInput("portletUrl", "内容地址", true, null, 500)){
		return false;
	}
    if($.validInput("morePortletUrl", "更多内容地址", false, null, 500)){
		return false;
	}
	
    $("#savePortlet,#savePortletAndAdd").attr("disabled","true");
    
    var portletId = $.trim($("#hidPortletId").val());
    var portletName = $.trim($("#portletName").val());
    var subSystemId = subsystemSelector.getValue();
    var portletUrl = $.trim($("#portletUrl").val());
    var morePortletUrl = $.trim($("#morePortletUrl").val());
    var portletStyle = $.trim($("#portletStyle").val());
    var action = $("#hidAction").val() == "initadd"?"add" : "modify";
   
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/portlet/" + action,
        dataType : "json",
        data : {
            portletID : portletId,
            portletName : portletName,
            subSystemID : subSystemId,
            url : portletUrl,
            moreUrl : morePortletUrl,
            style : portletStyle
        },
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert("板块内容保存成功！");
				//刷新父列表
               	currentTab.doCallback();
                if(saveAndAdd) {
                    $("#addPortlet").removeAttr("disabled").click();
                }else {
                	if (action == "add"){
		        		 $("#hidPortletId").val(data.msg.text);
		        	}
                    $("#hidAction").val("view");
                    getLoadTypeAndModule();
                }
        	}
            $("#savePortlet,#savePortletAndAdd").removeAttr("disabled");
        }
    });
}