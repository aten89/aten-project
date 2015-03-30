$(initShortCutManage);
var typeList = null;
function initShortCutManage(){
	typeList = $("#openTra").ySelect({width: 117, json: true,
			url:"m/datadict/dictsel?dictType=windowTargetType",
			afterLoad:function() {
				var  targ = $("#windowTarget").val();
				if (targ)  typeList.select(targ);
			}
	});
	 
	$("#iconList").find("input").click(function(){
    		$("#iconList").find("span").removeClass("ShortCutBg");
        	$(this).parent().addClass("ShortCutBg");
        }
    );
    
    var  menuIcon = $("#shortCutIco").val();
    if (menuIcon) $("#iconList input[type=radio][value='" + menuIcon + "']").click();
    
    //关闭
    $("#closeShortCut").click(function(e){
    	parent.window.returnValue1 = false;
    	parent.closeDialog();
    });
    
    //保存
    $("#saveShortCut").click(function(e){
    	if($.validInput("shortCutName", "名称", true)){
			return false;
		}
		if($.validInput("shortCutUrl", "链接", true)){
			return false;
		}
		if($.validInput("shortCutIco", "链接图标", true)){
			return false;
		}
		
    	var shortCutId = $.trim($("#shortCutID").val());
    	var action = shortCutId ? "modshortcut" : "addshortcut";
		
		var shortCutName = $.trim($("#shortCutName").val());
		var shortCutUrl = $.trim($("#shortCutUrl").val());
		var shortCutIco = $.trim($("#shortCutIco").val());
		var openTraget = $.trim(typeList.getValue());
		var isValid = $.trim($("#isValid").val());
		
		$.ajax({
	   		type : "POST",
	   		cache: false,
	    	async : true,
	   		url  : "l/frame/" + action,
	   		dataType : "json",
	   		data : {
	   			menuTitle : shortCutName,
	   			menuLink : shortCutUrl,
	            logoURL : shortCutIco,
	   			windowTarget : openTraget,
	            shortCutMenuID : shortCutId,
	            status : isValid == "false" ? "0": "1",
	   			type : "CUSTOM"
	   		},
	   		success : function(data){
	   			if ($.checkErrorMsg(data) ) {
	   				var sid = shortCutId ? shortCutId : data.msg.text;
	   				var obj = {shortCutId:sid,
	   					shortCutName:shortCutName,
	   					shortCutIco:shortCutIco
	   				};
	   				parent.window.returnValue1 = obj;
	   				$.alert("保存快捷方式成功！",function(){
	   					parent.closeDialog();
	   				});
	   			}
	  		}
		});
    });
 }