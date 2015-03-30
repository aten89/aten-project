
var assignType = "A";
var selectType = "single";

$(initDocDialog);
function initDocDialog(){
	var docClassName = $("#docClassName").val();
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/dis_start",
	   data : {
	   		act:"flowselect",
	   		docclass:docClassName
	   },             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find('flow-config').each(function(index){
		    		var curELe = $(this);
                   listDate += "<input name='flow' type='radio' id='"+$("key",curELe).text()+"' myType='" + $("type",curELe).text() + "' value='"+ $("key",curELe).text() +"' class='cBox02' style='margin:0 0 0 15px' onclick='changeRadio(this)'/><label for='"+$("key",curELe).text()+"'>"+  $("name",curELe).text() +"</label><br /><br />";
				});
				
		    } 
		    if (listDate == ""){
		       listDate += " <input name='problemType' type='radio' value='DEFAULT_VALUE_ATONCE' class='cBox02'/><label for='qwwt3'>直接发布</label><br /><br />";
		    }
		    $("#selectFlows").html(listDate);
			$("input[type='radio']").eq(0).click();
	   },
	   error : function(){
	        $.ermpAjaxError();
	    }
	});
	
	$("#submit").click(function(){
		if ((assignType=="B1" || assignType=="B2") && $('#appointTo').val()==""){
			$.alert("请选择“指定接受人”！");
			return false;
		}
		var arg = window.dialogArguments;
		arg.flowKey = $("input[type='radio'][checked]").val();
		arg.appointTo = $('#appointTo').val();
		//modify zhubiyong 2010-7-26
		arg.endTime = $("#endTime").val();
		window.returnValue = true;
		window.close();
  	});
  	
  	$("#cancel").click(function(){
		window.close();
  	});
	
	$("input[type='radio']").eq(0).attr("checked",true);
	
	//打开用户帐号
   $("#openAppointToSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH,BASE_PATH);
			if (selectType=="single"){
				dialog.setCallbackFun(function(user){
					if (user != null) {
						$("#appointTo").val(user.id);
						$("#appointToName").val(user.name);
					}
				});
				dialog.openDialog("single");
			}else{
				var userIds = $("#appointTo").val().split(",");
				var userNames = $("#appointToName").val().split(",");
				for (var i=0 ; i<userIds.length ; i++){
					if (userIds[i]!="" && userNames[i]!=""){
						dialog.appendSelectedUser({name:userNames[i],id:userIds[i]});
					}
				}
				dialog.setCallbackFun(function(user){
					if (users != null) {
						var ids = "";
						var names = "";
						for (var i=0 ; i<users.length ; i++){
							ids += users[i].id + ",";
							names += users[i].name + ",";
						}
						$("#appointTo").val(ids);
						$("#appointToName").val(names);
					}
				});
				dialog.openDialog("multi");
			}
		}
	);
};

function openHelp(){
	return false;
};

//modify zhubiyong 2010-7-26
function changeRadio(obj){
    assignType = $(obj).attr("myType");    //A：不选，B1：必单选，Ｂ2：可多选；C1：可单选；C2：可多选
    switch (assignType){
        case "B1":
            $("#assignPanel").show();
            $("#assignText").html("（必选）");
            $("#executionTime").val("");
            $("#executionTime").hide();
            selectType = "single";
            break;
        case "B2":
            $("#assignPanel").show();
//            $("#executionTime").show();
            $("#executionTime").val("");
			$("#executionTime").hide();
            $("#assignText").html("（必选）");
            selectType = "multi";
            break;
        case "C1":
            $("#assignPanel").show();
            $("#assignText").html("（可选）");
            $("#executionTime").val("");
			$("#executionTime").hide();
            selectType = "single";
            break;
        case "C2":
            $("#assignPanel").show();
            $("#assignText").html("（可选）");
            $("#executionTime").val("");
			$("#executionTime").hide();
            selectType = "multi";
            break;
         default:
			$("#assignPanel").hide();
			$("#executionTime").val("");
			$("#executionTime").hide();
    }
}
/*
function changeRadio(obj){
	assignType = obj.myType;	//A：不选，B1：必单选，Ｂ2：可多选；C1：可单选；C2：可多选
	switch (assignType){
		case "A":
			$("#assignPanel").hide();
			break;
		case "B1":
			$("#assignPanel").show();
			$("#assignText").html("（必选）");
			selectType = "single";
			break;
		case "B2":
			$("#assignPanel").show();
			$("#assignText").html("（必选）");
			selectType = "multi";
			break;
		case "C1":
			$("#assignPanel").show();
			$("#assignText").html("（可选）");
			selectType = "single";
			break;
		case "C2":
			$("#assignPanel").show();
			$("#assignText").html("（可选）");
			selectType = "multi";
			break;
	}
};
*/