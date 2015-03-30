var typeSelect;
var postSelect;

$(initGroupManage);

function initGroupManage(){ 
    
    //取得操作类型
    var action = $("#opType").val();
    if($.trim(action) == ""){
        return;
    }

	switch(action){
        case "add" :             
            $("#addSubGroup,#editGroup,#delGroup,#sortSub,#bindRole,#bindPerson,#bindPost").attr("disabled","true").addClass("icoNone");
            
            $("#saveGroup,#resetGroup,#saveAddGroup").removeAttr("disabled").removeClass("icoNone");
            
            $("#groupname,#desc").removeAttr("readonly");
           	$("#showManager").removeAttr("disabled").removeClass("icoNone");
            $("#showGroup").attr("disabled","true").addClass("icoNone");
           	typeSelect = $("#grouptype").ySelect({width:117,json:true, url:"m/datadict/dictsel?dictType=GROUP_TYPE",afterLoad:selectType});
//            postSelect = $("#postid").ySelect({width:117, url:"m/rbac_group/postlist?groupID=" + $("#groupid").val(),afterLoad:afterPostLoad});
            //默认值     
            $("#hidGroupType").val($("#curGroupType").val());
            $("#parentid").val($("#curGroupId").val());
            $("#parentname").val($("#curGroupName").val());
            //重写重置按扭
    		$("#resetGroup").click(function(){
    			document.groupFrm.reset();
    			$("#hidGroupType").val($("#curGroupType").val());
            	$("#parentid").val($("#curGroupId").val());
            	$("#parentname").val($("#curGroupName").val());
            	selectType();
    			return false;
    		});
            bindEvent();
            break;
        case "modify" :
            $("#addSubGroup,#delGroup,#sortSub,#bindRole,#bindPerson,#bindPost").removeAttr("disabled").removeClass("icoNone");
            $("#editGroup").attr("disabled","true").addClass("icoNone");
            
            $("#saveGroup,#resetGroup").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddGroup").attr("disabled","true").addClass("icoNone");
            
            $("#groupname,#desc").removeAttr("readonly");
           	$("#showGroup,#showManager").removeAttr("disabled").removeClass("icoNone");
            
           	typeSelect = $("#grouptype").ySelect({width:117,json:true, url:"m/datadict/dictsel?dictType=GROUP_TYPE",afterLoad:selectType});
            postSelect = $("#postid").ySelect({width:117,json:true, url:"m/rbac_group/postlist?groupID=" + $("#groupid").val(),afterLoad:afterPostLoad});
            
            //重写重置按扭
    		$("#resetGroup").click(function(){
    			document.groupFrm.reset();
            	selectType();
            	selectPost();
    			return false;
    		});
            bindEvent();     
            break;
        case "view" : 
            $("#addSubGroup,#editGroup,#delGroup,#sortSub,#bindRole,#bindPerson,#bindPost").removeAttr("disabled").removeClass("icoNone");
            
            $("#saveGroup,#resetGroup,#saveAddGroup").remove();
            
            $("#showGroup,#showManager").attr("disabled","true").addClass("icoNone");
            typeSelect = $("#grouptype").ySelect({width:117,json:true, url:"m/datadict/dictsel?dictType=GROUP_TYPE",
            		afterLoad:function(){
            			selectType();
            			$("#grouptype").parent().text(typeSelect.getDisplayValue());
            		}
            });
            if ($("#hidPostid").val() == "") {
            	$("#postid").parent().text("");
            } else {
	            postSelect = $("#postid").ySelect({width:117,json:true, url:"m/rbac_group/postlist?groupID=" + $("#groupid").val(),
	            		afterLoad:function(){
	            			afterPostLoad();
	            			$("#postid").parent().text(postSelect.getDisplayValue());
	            		}
	            });
            }
            //标题
            $('#titlName').html("—" + $("#groupname").val());
            //保存页面值
            $("#curGroupId").val($("#groupid").val());
            $("#curGroupName").val($("#groupname").val());
            $("#curGroupType").val($("#hidGroupType").val());
            
            $("#groupname").parent().text($("#groupname").val());
            $("#parentname").parent().text($("#parentname").val());
            $("#desc").parent().text($("#desc").val());
           
            break;
    }
    
    function selectType() {
    	var type = $.trim($("#hidGroupType").val());
    	if (type != ""){
    		typeSelect.select(type);
    	}
    }
    
    function selectPost() {
    	
    	var post = $.trim($("#hidPostid").val());
    	postSelect.select(post);
    }
    
    function afterPostLoad() {
	    postSelect.addOption("", "请选择...", 0);
	    selectPost();
    }

    //显示工具栏
    $('#allTool').show();
}

//绑定事件
function bindEvent(){
    $("#saveGroup").click(function(){
        	saveGroup(false);
        }
    );
    $("#saveAddGroup").click(function(){
        	saveGroup(true);
        }
    );
    
    //上级部门绑定
    $('#showGroup').click(function(){
    	var selector = new DeptDialog(BASE_PATH);
    	selector.setCallbackFun(function(retVal){
			if (retVal != null) {
				$("#parentid").val(retVal.id);
				$("#parentname").val(retVal.name);
			}
		});
		 selector.openDialog("single");
    });

    //回车查询
    $.EnterKeyPress($("#edUserAcc"),$("#searchUserAcc"));
}

//保存
function saveGroup(saveAndAdd){
	if($.validInput("groupname", "名称", true)){
		return false;
	}
	if($.validInput("desc", "备注", false, null, 500)){
		return false;
	}
	if (!typeSelect.getValue()) {
		$.alert("类型不能为空!");
		return false;
	}
   if($("#parentid").val() != "" && $("#groupid").val() == $("#parentid").val()){
        $.alert("不能选自己做为上级!");
        return false;
    }
    
    var servletAction = $("#opType").val();
    var parentid = $.trim($('#parentid').val());
    var postid = postSelect? postSelect.getValue() : "";
    var groupname = $.trim($('#groupname').val());
    var type = typeSelect.getValue();
    var desc = $.trim($('#desc').val());
	var groupId = $('#groupid').val();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_group/"+servletAction,
        dataType : "json",
        data : "parentGroupID="+parentid
        		+"&postID="+ (postid ? postid : "")
        		+"&groupName="+groupname
        		+"&type="+type
        		+"&description="+desc
        		+"&groupID="+groupId,
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
            	$.alert("机构保存成功!"); 
            	var newGroupID= data.msg.text;
               //增加树节点
                if (servletAction == "add"){
//                	var parentNode;
//                    if (parentid != ""){
//                    	parentNode = $("li[groupid=\""+parentid+"\"]","#groupTree");
//                    }else{
//                    	parentNode = $(".root","#groupTree");
//                    }
//					listGroupTree.addNodes(parentNode, "<li id=\"" + data.msg.text + "\" groupid=\"" + data.msg.text + "\">" + "<span groupid=\"" + data.msg.text + "\">" + groupname + "</span></li>");
                    listGroupTree.addNode(parentid, newGroupID, groupname, {groupid :  newGroupID});
                    
                    //模拟树节点的点击事件,增加时用返回的编号
                    if(saveAndAdd){
                        $("#addSubGroup").removeAttr("disabled").click();
                    }else{
                    	listGroupTree.clickNode(newGroupID);
//                        $("li[groupid=\""+data.msg.text+"\"] > span","#groupTree").click();
                    }
                }else{
                    if ($("#oldparentid").val() != parentid){
	                    listGroupTree.removeNode(groupId);
	                    listGroupTree.reloadNodes(parentid, "m/rbac_group/subgroups?groupID=" + parentid);

	                    //$("li[groupid=\""+groupId+"\"] > span","#groupTree").click();

	                    $('#curGroupId').val("");
	                    $('#curGroupName').val("");
	                    $('#curGroupType').val("");
	                    $('#allTool').hide();      
	                    $("#groupModuleMain").html("<div class=\"czbks\">&nbsp;</div>");

                    }else{
//                        $("li[groupid=\""+groupId+"\"] > span","#groupTree").text(groupname);                 
//                        $("li[groupid=\""+groupId+"\"] > span","#groupTree").click();
                    	listGroupTree.modifyNode(groupId, groupname);
                    	listGroupTree.clickNode(groupId);
                    }                                         
                }                       
                       		
        	}
        }
    }); 
}
