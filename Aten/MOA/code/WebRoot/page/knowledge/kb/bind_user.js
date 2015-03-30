var mainFrame = $.getMainFrame();

var deptTree = null;
var isChange = false;
$(initImpowerUser);
function initImpowerUser(){
    $("#titleName").text($("#title").val());
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#ImpowerUser").attr("disabled","true").addClass("icoNone");
    $("#saveUserRoles").attr("disabled","true").addClass("icoNone");

	//选择机构
	$.getJSON(ERMP_PATH + "m/rbac_group/groupselect?jsoncallback=?",function(data){
		$("#groupIdDiv").html(data.htmlValue);
		var groupSelect = $("#groupIdDiv").ySelect({
			width:55,
			onChange: function(value){
				if (value != "") {
					$("#searchGroupUsers").click();
				} else {
					$("#searchGroupUsers > tbody").empty();
				}
			}
		});
		groupSelect.addOption("", "所有...", 0);
		groupSelect.select(0);
	});
	

	//绑定用户帐号
    $("#saveUserRoles").click(
        function(e){
        	var userAssign="";
			$("#bindUserList > tbody tr").each(function(i){
				var users = "";
				var flags = "";
				$(this).find("input[type='checkbox']").each(function(index){
					if(this.checked === true){
						flags +="_"+this.value;				
					}
				});
				flags = flags.substring(1,flags.length);
				users = $(this).find("input[type='hidden']").val();
				userAssign += "&user_assign=" + users+"**"+flags;
			});
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
                url  : "m/knowledge?act=binduser&id="+$("#id").val(),
				data : userAssign,
                success : function(xml){
                    var message = $("message",xml);
                    if (message.attr("code") == "1") {
                    	$.alert("绑定成功！");
                    	mainFrame.getCurrentTab().doCallback();
                        $("#saveUserRoles").attr("disabled","true").addClass("icoNone");
                    }else if(message.text() !== ""){
                        $.alert(message.text());
                    }
                   
                },
                error : $.ermpAjaxError
            });
        }
    );
	//搜索用户帐号
   $("#searchGroupUsers").click(
	   	function(e){
		   	var groupId = $.trim($("#hidGroupId").val());
    		var keyword = $.trim($("#userKeyword").val());
    		var bindusers="";
    		if (groupId == "" && keyword == "") {
    			$.alert("请输入查询条件！");
    			$("#userKeyword").focus();
    			return;
    		}
    		
    		var url = ERMP_PATH + "m/user_account/xmlusers?groupID="+groupId+"&keyword="+encodeURI(keyword) + "&jsoncallback=?";
			$.getJSON(url,function(data){
				if ($.checkErrorMsg(data) ) {
					var dataList = data.userAccounts;
        			if (dataList) {
		                var moduleActionHTML = "";
		                $(dataList).each(function(i) {
		                    	var groupName = dataList[i].groupNames;
		                        moduleActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
									+ "<td><a class=opLink>选择</a><input type=\"hidden\" value=\"" + dataList[i].accountID + "\"></td>"
									+ "<td>" + dataList[i].accountID + "</td>"
									+ "<td>" + dataList[i].displayName + "</td>" 
									+ "<td>" + groupName + "</td>"
									+ "</tr>";
		                });
		                $("#actorUserList > tbody").html(moduleActionHTML).find("td:empty").html("&nbsp;");
		                //点击绑定事件
		                $("#actorUserList a").click(function(e){
		                    //判断是否已被添加
	                        if($("#bindUserList input[type='hidden'][value='" + $("+input",this).val() + "']").length != 0){
	                        	$.alert("不能重复绑定");
	                            return;
	                        };
	                        $("#bindUserList > tbody").append("<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\"><td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input type=\"hidden\" value=\"" + $(this).parent().find("input").val() + "\"></td><td style='white-space:normal'><div class='klWrap'>" + $(this).parent().parent().find("td").clone().eq(1).html() +"</div></td>" +"<td  class=\"klBox\">"
							 +"<span><input id='"+$(this).parent().find("input").val()+"_0' type='checkbox' value='0' onclick='checkBoxCtrl(this)' checked disabled/></span>"
	               			 +"<span><input id='"+$(this).parent().find("input").val()+"_1'type='checkbox' value='1' onclick='checkBoxCtrl(this)' /></span>"
	               			 +"<span><input id='"+$(this).parent().find("input").val()+"_2'type='checkbox' value='2' onclick='checkBoxCtrl(this)' /></span>"
	               			 +"<span><input id='"+$(this).parent().find("input").val()+"_3'type='checkbox' value='3' onclick='checkBoxCtrl(this)' /></span>" 
							 +"</td></tr>");
	                        $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
		             	});
		        	}
	            } else {
	                $("#actorUserList > tbody").empty();
	            }
			});	
	   	}
   );

   loadBindedUsers();
   $.EnterKeyPress($("#userKeyword"),$("#searchGroupUsers"));//回车搜索
}

/**
 * 加载绑定的用户
 */
function loadBindedUsers(){
	$.ajax({
		type : "POST",
		cache: false,
		async : true,
		url  : "m/knowledge",
		data : {act:"get_binding_users",id:$("#id").val()},
		success : function(xml){
		    var message = $("message",xml);		   
		    if (message.attr("code") == "1") {
				   var tbodyHtml = "";
		           $("assign",xml).each(function(index){
		               var contents=$(this);
		               var boxFlag = new Array(0,0,0,0);
            		   $("flag",contents).each(function(){
            			 	var flag = $(this).attr("id");
            			 	boxFlag[flag]=1;
            			});
            			var checkStatus = false;
            			if(contents.text()=="all_user"){
            				for(var i=0; i<5; i++){
            					if(boxFlag[i]==1){
            						$("#assign_all_"+i).attr("checked","true");
            						if(i>0){
            							checkStatus = true;
            						}
            					}
            				}
	            			if(checkStatus){
	            				$("#assign_all_0").attr("checked","true");
								$("#assign_all_0").attr("disabled","disabled");
		            			if ( boxFlag[6] ==1) {
		            				$("#assign_all_3").attr("checked","true");
									$("#assign_all_3").attr("disabled","disabled");
		            			}
	            			}
            				return;
            			}	
						tbodyHtml +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
		               			 +"<td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input type=\"hidden\" value=\"" + contents.text() + "\"></td>"
		               			 +"<td style='white-space:normal'><div class='klWrap'>" + contents.text() + "</div></td>"
		               			 +"<td class=\"klBox\">";
						tbodyHtml +="<span><input  id='"+contents.text()+"_0' type='checkbox' value='0' onclick='checkBoxCtrl(this)' disabled";
           			 	if(boxFlag[0] ==1){
           			 		 tbodyHtml +=" checked ";
           			 	}
		               	tbodyHtml +=" /></span>";
		               	
						tbodyHtml +="<span><input   id='"+contents.text()+"_1'type='checkbox' value='1'  onclick='checkBoxCtrl(this)'";
						if(boxFlag[1] ==1){
       			 			tbodyHtml +=" checked ";
       			 		}
       			 		tbodyHtml +=" /></span>";
       			 		
		               	tbodyHtml +="<span><input   id='"+contents.text()+"_2'type='checkbox' value='2' onclick='checkBoxCtrl(this)'";
		         		if(boxFlag[2] ==1){
		               		tbodyHtml +=" checked ";
		             	}
		              	tbodyHtml +=" /></span>";
		              	
		               	tbodyHtml +="<span><input   id='"+contents.text()+"_3'type='checkbox' value='3' onclick='checkBoxCtrl(this)'";
		            	if(boxFlag[3] ==1){
		               		tbodyHtml +=" checked ";
		               	}
		             	tbodyHtml +=" /></span>";
		             	
		               	tbodyHtml +="</td></tr>";
		               			 
		           });
		          $("#bindUserList > tbody").append(tbodyHtml).find("td:empty").html("&nbsp;");
		    }else if(message.text() != ""){
		        $.alert("读取已绑定用户失败，原因：" + message.text());
		     };
		},
		error : $.ermpAjaxError
	});
}

function delUsers(a){
	$(a).parent().parent().remove();
	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
}

function checkBoxCtrl(o){

	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
}

function assignAllBoxCtrl(obj){
	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
	if(obj.checked ==true){
		$("#assign_all_0").attr("checked","true");
		$("#assign_all_0").attr("disabled","disabled");
	}else{
		var hasChecked = false;
		$(obj).parent().parent().find("input[type='checkbox']").each(function(){
			if(this.checked == true && $(this).val() !=0){
				hasChecked= true;
			}	
		});
		if(!hasChecked){
			$("#assign_all_0").removeAttr("disabled").removeClass("icoNone");
			$("#assign_all_0").removeAttr("checked");
		}
	}
}

/**
 * 点击 强制评分 checkBox
 */
function clickLastBoxCtrl(obj){
	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
	if(obj.checked ==true){
		$("#assign_all_0").attr("checked","true");
		$("#assign_all_0").attr("disabled","disabled");
		$("#assign_all_3").attr("checked","true");
		$("#assign_all_3").attr("disabled","disabled");
	}else{
		
		// 移除 分类权限
		$("#assign_all_3").removeAttr("disabled").removeClass("icoNone");
			
		var hasChecked = false;
		$(obj).parent().parent().find("input[type='checkbox']").each(function(){
			if(this.checked == true && $(this).val() !=0){
				hasChecked= true;
			}	
		});
		if(!hasChecked){
			$("#assign_all_0").removeAttr("disabled").removeClass("icoNone");
			$("#assign_all_0").removeAttr("checked");
		}
	}
}

/**
 * 页面加载的 强制评分
 */
function checkScoreBoxCtrl(obj) {
	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
	if (obj.checked ==true) {
		$(obj).parent().parent().find("input[type='checkbox']").eq(3).attr("checked","true");
		$(obj).parent().parent().find("input[type='checkbox']").eq(3).attr("disabled","disabled");
	}else{
		// 移除 分类权限
		$(obj).parent().parent().find("input[type='checkbox']").eq(3).removeAttr("disabled").removeClass("icoNone");
	}
}
