$(initBindPost);
var titleArgs;
function initBindPost(){
    $("#titleName").text($("#titleArgs").val());
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#ImpowerPost").attr("disabled","true").addClass("icoNone");
	$("#saveUserRoles").attr("disabled","true").addClass("icoNone");
	loadBindedPost();
	
	//初始化树
    initPostTree("");
    
	//绑定用户动作
    $("#saveUserRoles").click(
        function(e){
            var posts = "";
			$("#bindPostList > tbody tr").each(function(i){
				posts += "&post_ids=" + $(this).find("input").val();
			});
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/doc_class?act=bindpost&id=" + $("#id").val(),
                data : posts,
                success : function(xml){
                    var message = $("message",xml);
                    if (message.attr("code") == "1") {
                        $.alert("绑定成功！");
                    } else if(message.text() != ""){
                        $.alert("保存失败，原因：" + message.text());
                    };
                    $("#saveUserRoles").attr("disabled","true").addClass("icoNone");
                },
                error : $.ermpAjaxError
            });
        }
    );
	
}

//绑定职位
function loadBindedPost(){
	$.ajax({
		type : "POST",
		cache: false,
		async : true,
		url  : "m/doc_class",
		data : {act:"get_binding_post",id:$("#id").val()},
		success : function(xml){
		    var message = $("message",xml);
		    if (message.attr("code") == "1") {
		          var tbodyHtml = "";
		           $("assign",xml).each(function(index){
		               tbodyHtml +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
		               			 +"<td><a class=\"opLink\" onclick=\"delPosts(this);\">删除</a><input type=\"hidden\" value=\"" + $(this).text() + "\"></td>"
		               			 +"<td>" + $(this).text() + "</td></tr>";
		           })
		          $("#bindPostList > tbody").html(tbodyHtml).find("td:empty").html("&nbsp;");
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
                          + "<td>" + groupname + "</td>"
                          + "</tr>";
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