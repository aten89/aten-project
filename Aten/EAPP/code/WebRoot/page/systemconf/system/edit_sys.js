var currentTab = null;

$(initSubSystemManage);

function initSubSystemManage(){
	currentTab = $.getMainFrame().getCurrentTab();

    
    //取得操作类型
    var action = $("#hidAction").val();
    if($.trim(action) == ""){
        return;
    };
    
    switch(action){
        case "add" : 
            currentTab.setTitle("新增子系统", "新增子系统");
            $("#addSubSystem,#modifySubSystem").attr("disabled","true").addClass("icoNone");
            $("#subSysName,#logourl,#ipaddress,#servername,#domainname,#subSysPort,#description").removeAttr("readonly");
            bindEvent();
            break;
        case "modify" : 
        	currentTab.setTitle("修改子系统",$("#subSysName").val());
        	$("#modifySubSystem").attr("disabled","true").addClass("icoNone");
        	$("#addSubSystem").removeAttr("disabled").removeClass("icoNone");
            $("#subSysName,#logourl,#ipaddress,#servername,#domainname,#subSysPort,#description").removeAttr("readonly");
            $("#saveAddSubSystem").attr("disabled","true").addClass("icoNone");
            bindEvent();
            break;
        case "view" : 
            currentTab.setTitle("查看子系统",$("#subSysName").val());
            $("#addSubSystem,#modifySubSystem").removeAttr("disabled").removeClass("icoNone");
            $("#saveSubSystem,#resetSubSystem,#saveAddSubSystem").remove();
//            $("input", "#isValidTD").attr("disabled","true").addClass("icoNone");
            $("#isValidTD").text(($("input:checked", "#isValidTD").val() =="true" ? "是" : "否"));
            $("#subSysName").parent().text($("#subSysName").val());
 			$("#logourl").parent().text($("#logourl").val());
 			$("#ipaddress").parent().text($("#ipaddress").val());
 			$("#servername").parent().text($("#servername").val());
 			$("#domainname").parent().text($("#domainname").val());
 			$("#subSysPort").parent().text($("#subSysPort").val());
// 			$("#logoutUrl").parent().text($("#logoutUrl").val());
 			$("#description").parent().text($("#description").val());
            break;
    };
};

//绑定事件
function bindEvent(){
    $("#saveSubSystem").click(function(){
        	saveSubSystemInfo(false);
        }
    );
    $("#saveAddSubSystem").click(function(){
        	saveSubSystemInfo(true);
        }
    );
}

//保存信息
function saveSubSystemInfo(saveAndAdd){
	if($.validInput("subSysName", "系统名称", true)){
		return false;
	}
	if($.validInput("domainname", "域名", true)){
		return false;
	}
	if($.validNumber("subSysPort", "端口号", true, 65535, 0)){
		return false;
	}
	if($.validInput("description", "备注", false, null, 500)){
		return false;
	}
	
    var subSysName = $.trim($("#subSysName").val());
	var logourl = $.trim($("#logourl").val());
	var ipaddress = $.trim($("#ipaddress").val());
	var servername = $.trim($("#servername").val());
	var domainname = $.trim($("#domainname").val());
	var subSysPort = $.trim($("#subSysPort").val());
	var description = $.trim($("#description").val());
	var isValid = $("input:checked", "#isValidTD").val();
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   url  : "m/subsystem/" + $("#hidAction").val(),
	   dataType : "json",
	   data : "subSystemID="+$("#hidSubSystemId").val()
	   			+"&name="+subSysName
	   			+"&logoURL="+logourl
	   			+"&ipAddress="+ipaddress
	   			+"&serverName="+servername
	   			+"&domainName="+domainname
	   			+"&port="+subSysPort
	   			+"&description="+description
	   			+"&isValid=" + isValid,
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				 $.alert("子系统配置信息保存成功！");
		        //刷新父列表
               	currentTab.doCallback();
		        if(saveAndAdd){
		            $("#addSubSystem").removeAttr("disabled").click();
		        } else{
		        	var action = $("#hidAction").val();
		        	if (action == "add"){
		        		 $("#hidSubSystemId").val(data.msg.text);
		        	}
		            $("#hidAction").val("view");
		            getLoadTypeAndModule();
		        }
			}
		}
	});
};