var currentTab = null;
var typeSelect;
$(initShortCutManage);

function initShortCutManage(){
	currentTab = $.getMainFrame().getCurrentTab();
    
    $("#iconList").find("input").click(function(){
    		$("#iconList").find("span").removeClass("ShortCutBg");
        	$(this).parent().addClass("ShortCutBg");
        }
    );
    
    //取得操作类型
    var action = $("#hidAction").val();
    if($.trim(action) == ""){
        return;
    };
    
    
    switch(action){
        case "add" : 
            currentTab.setTitle("新增快捷方式", "新增快捷方式");
            $("#opAdd,#opModify").attr("disabled","true").addClass("icoNone");
            $("#menuTitle,#menuLink").removeAttr("readonly");
            $("#status_true").click();
            typeSelect = $("#typeSelect").ySelect({width:120, json: true, url:"m/datadict/dictsel?dictType=windowTargetType",afterLoad:selectFirst});
            //重写重置按扭
    		$("#btreset").click(function(){
    			document.mainForm.reset();
    			selectFirst();
    			$("#status_true").click();
    			$("#iconList").find("span").removeClass("ShortCutBg");
    			return false;
    		});
            bindEvent();
            break;
        case "modify" : 
            currentTab.setTitle("修改快捷方式",$("#menuTitle").val());
            $("#opModify").attr("disabled","true").addClass("icoNone");
            $("#opAdd").removeAttr("disabled").removeClass("icoNone");
            $("#menuTitle,#menuLink").removeAttr("readonly");
            $("#btnSaveAndAdd").attr("disabled","true").addClass("icoNone");
            typeSelect = $("#typeSelect").ySelect({width:120, json: true, url:"m/datadict/dictsel?dictType=windowTargetType",afterLoad:selectDefault});
            $("#iconList input[type=radio][value='" + $("#menuIcon").val() + "']").click();
            //重写重置按扭
    		$("#btreset").click(function(){
    			document.mainForm.reset();
    			selectDefault();
    			$("#iconList input[type=radio][value='" + $("#menuIcon").val() + "']").click();
    			return false;
    		});
            bindEvent();
            break;
        case "view" : 
            currentTab.setTitle("查看快捷方式",$("#menuTitle").val());
            $("#opAdd,#opModify").removeAttr("disabled").removeClass("icoNone");
            $("#btnSave,#btnSaveAndAdd,#btreset").remove();
            typeSelect = $("#typeSelect").ySelect({width:120, json: true, url:"m/datadict/dictsel?dictType=windowTargetType",
            	afterLoad:function() {
            		selectDefault();
            		$("#typeSelect").parent().text(typeSelect.getDisplayValue());
            	}
            });
            $("#iconList input[type=radio][value='" + $("#menuIcon").val() + "']").attr("checked","true").click();
//            $("input[type=radio]").attr("disabled","true");
            $("#statusTD").text(($("input:checked", "#statusTD").val() =="1" ? "启用" : "停用"));
            
            $("#menuTitle").parent().text($("#menuTitle").val());
 			$("#menuLink").parent().text($("#menuLink").val());
            break;
    };
    function selectFirst() {
    	typeSelect.select(0);
    }   
    function selectDefault() {
    	typeSelect.select($("#windowTarget").val());
    }
 }
 
 //绑定事件
function bindEvent(){
    $("#btnSave").click(function(){
        	saveShortCutMenuInfo(false);
        }
    );
    $("#btnSaveAndAdd").click(function(){
        	saveShortCutMenuInfo(true);
        }
    );
}

function saveShortCutMenuInfo(saveAndAdd){
	if($.validInput("menuTitle", "菜单标题", true)){
		return false;
	}
	if($.validInput("menuLink", "菜单链接", true)){
		return false;
	}
	if($.validInput("menuIcon", "菜单图标", true)){
		return false;
	}
	
	
	var shortCutID = $.trim($("#shortCutID").val());
	var menuTitle = $.trim($("#menuTitle").val());
	var menuLink = $.trim($("#menuLink").val());
	var menuIcon = $.trim($("#menuIcon").val());
	var openType = $.trim(typeSelect.getValue());
	var menuStatus = $.trim($("#menuStatus").val());
	var action = $("#hidAction").val();

	$.ajax({
	   type : "POST",
	   url  : "m/shortcutmenu/"+action,
	   dataType : "json",
	   data : "shortCutMenuID=" + shortCutID
	   			+"&menuTitle=" + menuTitle
	   			+"&menuLink="+menuLink
	   			+"&logoURL="+menuIcon
	   			+"&windowTarget="+openType
	   			+"&status="+menuStatus,
      success : function(data){
      		if ($.checkErrorMsg(data) ) {
      			$.alert("保存成功");
		    	//刷新父列表
               	currentTab.doCallback();
		    	//进行后续处理
		        if(saveAndAdd){
		        	//保存并新增
		            $("#opAdd").removeAttr("disabled").click();
		        }else{
		        	if (action == "add"){
		        		 $("#hidshortCutID").val(data.msg.text);
		        	}
		        	$("#hidAction").val("view");
                    getLoadTypeAndModule();
				};
      		}
		}
	});
}