var mainFrame = $.getMainFrame();

var isChange = false;
var titleArgs;
$(initBindGroup);
function initBindGroup(){
    $("#titleName").text($("#title").val());
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#ImpowerGroup").attr("disabled","true").addClass("icoNone");
	$("#saveUserRoles").attr("disabled","true").addClass("icoNone");
	loadBindedGroup();
	
	//机构类别
	initDeptSelect();
    
	//绑定用户动作
    $("#saveUserRoles").click(
        function(e){
            var groupAssign = "";
			$("#bindGroupList > tbody tr").each(function(i){
				var groups = "";
				var flags = "";
				$(this).find("input[type='checkbox']").each(function(index){
					if(this.checked === true){
						flags +="_"+this.value;				
					}
				});
				flags = flags.substring(1,flags.length);
				groups = $(this).find("input[type='hidden']").val();
				groupAssign += "&group_assign=" + groups+"**"+flags;
			});
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/knowledge?act=bindgroup&id=" + $("#id").val(),
                data : groupAssign,                
                success : function(xml){
                    var message = $("message",xml);
                    if (message.attr("code") == "1") {
                        $.alert("绑定成功！");
                        mainFrame.getCurrentTab().doCallback();
                    } else if(message.text() !== ""){
                        $.alert("保存失败，原因：" + message.text());
                    }
                    $("#saveUserRoles").attr("disabled","true").addClass("icoNone");
                   	isChange = false;
                },
                error : $.ermpAjaxError
            });
        }
    );
	enWrap();
	
}

function initDeptSelect(){
	var url = ERMP_PATH + "m/datadict/dictsel?dictType=GROUP_TYPE&jsoncallback=?";
	
	$.getJSON(url,function(data){
		$("#deptType").html(data.htmlValue);
		var dptSelect = $("#deptType").ySelect({
			width:55,
			onChange: initGroupTree
		});
		dptSelect.addOption("", "所有...", 0);
		dptSelect.select(0);
	});
}


/**
 * 加载绑定机构
 */
function loadBindedGroup(){
	$.ajax({
		type : "POST",
		cache: false,
		async : true,
		url  : "m/knowledge",
		data : {act:"get_binding_groups",id:$("#id").val()},
		success : function(xml){
		    var message = $("message",xml);
		    if (message.attr("code") == "1") {
		          var tbodyHtml = "";
		          var contents=$(this);
		           $("assign",xml).each(function(index){
		           		var contents = $(this);
		           		var boxFlag = new Array(0,0,0,0);
            		   $("flag",contents).each(function(){
            			 	var flag = $(this).attr("id");
            			 	boxFlag[flag]=1;
            			});	
		               tbodyHtml +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
		               			 +"<td><a class=\"opLink\" onclick=\"delGroups(this);\">删除</a><input type=\"hidden\" value=\"" + $(this).text() + "\"></td>"
		               			 +"<td style='white-space:normal'><div class='klWrap'>" + $(this).text() + "</div></td>"
		               			 +"<td class=\"klBox\">";
		               			 
		            	tbodyHtml +="<span><input  id='"+$(this).text()+"_0' type='checkbox' value='0' onclick='checkBoxCtrl(this)' disabled";
		             	if(boxFlag[0] ==1){
		          			tbodyHtml +=" checked ";
		              	}
		     			tbodyHtml +=" /></span>";
		     			
		               	tbodyHtml +="<span><input   id='"+$(this).text()+"_1'type='checkbox' value='1' onclick='checkBoxCtrl(this)'";
		       			if(boxFlag[1] ==1){
		             		tbodyHtml +=" checked ";
		             	}
		              	tbodyHtml +=" /></span>";
		              	
		               	tbodyHtml +="<span><input   id='"+$(this).text()+"_2'type='checkbox' value='2' onclick='checkBoxCtrl(this)'";
		           		if(boxFlag[2] ==1){
		              		tbodyHtml +=" checked ";
		               	}
		             	tbodyHtml +=" /></span>";
		             	
		               	tbodyHtml +="<span><input   id='"+$(this).text()+"_3'type='checkbox' value='3' onclick='checkBoxCtrl(this)'";
		               	if(boxFlag[3] ==1){
		               		tbodyHtml +=" checked ";
		               	}
		             	tbodyHtml +=" /></span>";
		               	
		             	tbodyHtml +="</td></tr>";
		           });
		          $("#bindGroupList > tbody").append(tbodyHtml).find("td:empty").html("&nbsp;");
		          
		          enWrap();
		          //$.alert("绑定成功！");
		    }else if(message.text() != ""){
		        $.alert("读取已绑定部门失败，原因：" + message.text());
		     };
		},
		error : $.ermpAjaxError
	});
}

function delGroups(a){
	$(a).parent().parent().remove();
	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
}


//载入群组树
function initGroupTree(value){
    //载入群组树
	var url = ERMP_PATH + "m/rbac_group/subgroups?type=" + value + "&jsoncallback=?";
	$.getJSON(url,function(data){
		$("#groupsList > li > ul").html(data.htmlValue);
		
        $("#groupsList").simpleTree({
            animate: true,
            basePath : ERMP_PATH,
	        json:true,
            afterClick: function(o){
                var groupId = $(o).attr("groupid");
                var groupname = $(">span",o).text();
                //判断是否已被添加
                if($("#bindGroupList input[value=" + groupname + "]").length != 0){
                	 $.alert("不能重复绑定");
                    return;
                };
                var groupHTML = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
                          + "<td><a class=opLink>删除</a><input type=\"hidden\" value=\"" + groupname + "\"></td>"
                          + "<td style='white-space:normal'><div class='klWrap'>" + groupname + "</div></td>"
                          + "<td class=\"klBox\">"
                          +"<span><input  id='"+groupname+"_0' type='checkbox' value='0' onclick='checkBoxCtrl(this)' checked disabled/></span>"
	               		  +"<span><input  id='"+groupname+"_1'type='checkbox' value='1' onclick='checkBoxCtrl(this)'/></span>"
	               		  +"<span><input  id='"+groupname+"_2'type='checkbox' value='2' onclick='checkBoxCtrl(this)'/></span>"
	               		  +"<span><input  id='"+groupname+"_3'type='checkbox' value='3' onclick='checkBoxCtrl(this)'/></span>" 
	               		  +"</td></tr>";
                $("#bindGroupList > tbody").append(groupHTML).find("a:last").click(
                    function(e){
                        $(this).parent().parent().remove();
                        $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
                    	isChange = true;
                    }
                );
                $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
                isChange = true;
            }
        });
	});	
}

function checkBoxCtrl(o){
	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
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

//折行
function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}