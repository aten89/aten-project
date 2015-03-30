var mainFrame = $.getMainFrame();

$(initBindPost);
var titleArgs;
function initBindPost(){
    $("#titleName").text($("#title").val());
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#ImpowerPost").attr("disabled","true").addClass("icoNone");
	$("#saveUserRoles").attr("disabled","true").addClass("icoNone");
	loadBindedPost();
	
	//初始化树
    initPostTree("");
    
	//绑定用户动作
    $("#saveUserRoles").click(
        function(e){
            var userAssign = "";
			$("#bindPostList > tbody tr").each(function(i){
				var posts = "";
				var flags = "";
				$(this).find("input[type='checkbox']").each(function(index){
					if(this.checked === true){
						flags +="_"+this.value;				
					}
				});
				flags = flags.substring(1,flags.length);
				users = $(this).find("input[type='hidden']").val();
				userAssign += "&post_assign=" + users+"**"+flags;
			});
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/knowledge?act=bindpost&id=" + $("#id").val(),
                data : userAssign,
                success : function(xml){
                    var message = $("message",xml);
                    if (message.attr("code") == "1") {
                    	$.alert("绑定成功！");
                    	mainFrame.getCurrentTab().doCallback();
                    } else if(message.text() !== ""){
                        $.alert("保存失败，原因：" + message.text());
                    }
                    $("#saveUserRoles").attr("disabled","true").addClass("icoNone");
                },
                error : $.ermpAjaxError
            });
        }
    );
	
}

/**
 * 绑定职位
 */
function loadBindedPost(){
	$.ajax({
		type : "POST",
		cache: false,
		async : true,
		url  : "m/knowledge",
		data : {act:"get_binding_post",id:$("#id").val()},
		success : function(xml){
		    var message = $("message",xml);
		    if (message.attr("code") == "1") {
		          var tbodyHtml = "";
               
		           $("assign",xml).each(function(index){
	           		    var boxFlag = new Array(0,0,0,0);
	           		    var contents = $(this);
	          		   $("flag",contents).each(function(){
	          			 	var flag = $(this).attr("id");
	          			 	boxFlag[flag]=1;
	          			});	
		               tbodyHtml +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
		               			 +"<td><a class=\"opLink\" onclick=\"delPosts(this);\">删除</a><input type=\"hidden\" value=\"" + $(this).text() + "\"></td>"
		               			 +"<td style='white-space:normal'><div class='klWrap'>" + $(this).text() + "</div></td>"
		               			 +"<td class=\"klBox\">";              			 
					tbodyHtml +="<span><input  id='"+$(this).text()+"_0' type='checkbox' value='0' onclick='checkBoxCtrl(this)'  disabled";
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
		          $("#bindPostList > tbody").append(tbodyHtml).find("td:empty").html("&nbsp;");
		    }else if(message.text() != ""){
		        $.alert("读取已绑定部门失败，原因：" + message.text());
		     };
		},
		error : $.ermpAjaxError
	});
}

function delPosts(a){
	$(a).parent().parent().remove();
	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
}


//载入职位树
function initPostTree(){
	 var url = ERMP_PATH + "m/post/subposts?jsoncallback=?";
	$.getJSON(url,function(data){
		$("#postTree > li > ul").html(data.htmlValue);
		$("#postTree").simpleTree({
            animate: true,
            basePath : ERMP_PATH,
	        json:true,
            afterClick: function(o){
                var groupname = $(">span",o).text();
              
                //判断是否已被添加
                if($("#bindPostList input[value=" + groupname + "]").length != 0){
                	 $.alert("不能重复绑定");
                    return;
                };
                var groupHTML = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
                          + "<td><a class=opLink>删除</a><input type=\"hidden\" value=\"" + groupname + "\"></td>"
                          + "<td style='white-space:normal'><div class='klWrap'>" + groupname + "</div></td>"
                          +"<td class=\"klBox\">"
						  +"<span><input  id='"+groupname+"_0' type='checkbox' value='0' onclick='checkBoxCtrl(this)' checked disabled/></span>"
						  +"<span><input  id='"+groupname+"_1'type='checkbox' value='1' onclick='checkBoxCtrl(this)'/></span>"
						  +"<span><input  id='"+groupname+"_2'type='checkbox' value='2' onclick='checkBoxCtrl(this)'/></span>"
						  +"<span><input  id='"+groupname+"_3'type='checkbox' value='3' onclick='checkBoxCtrl(this)'/></span>" 
						  +"</td></tr>";  
                $("#bindPostList > tbody").append(groupHTML).find("a:last").click(
                    function(e){
                        $(this).parent().parent().remove();
                        $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
                    }
                );
                $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
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