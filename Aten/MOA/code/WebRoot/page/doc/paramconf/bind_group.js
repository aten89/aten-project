var isChange = false;
var titleArgs;
$(initBindGroup);
function initBindGroup(){
    $("#titleName").text($("#titleArgs").val());
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#ImpowerGroup").attr("disabled","true").addClass("icoNone");
	$("#saveUserRoles").attr("disabled","true").addClass("icoNone");
	loadBindedGroup();
	initDeptSelect();
	
	//初始化树
    initGroupTree("");
    
	//绑定用户动作
    $("#saveUserRoles").click(
        function(e){
            var groups = "";
			$("#bindGroupList > tbody tr").each(function(i){
				groups += "&group_ids=" + $(this).find("input").val();
			});
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/doc_class?act=bindgroup&id=" + $("#id").val(),
                data : groups,
                success : function(xml){
                    var message = $("message",xml);
                    if (message.attr("code") == "1") {
                        $.alert("绑定成功！");
                    } else if(message.text() != ""){
                        $.alert("保存失败，原因：" + message.text());
                    };
                    $("#saveUserRoles").attr("disabled","true").addClass("icoNone");
                   	isChange = false;
                },
                error : $.ermpAjaxError
            });
        }
    );
	
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

//绑定机构
function loadBindedGroup(){
	$.ajax({
		type : "POST",
		cache: false,
		async : true,
		url  : "m/doc_class",
		data : {act:"get_binding_groups",id:$("#id").val()},
		success : function(xml){
		    var message = $("message",xml);
		    if (message.attr("code") == "1") {
		          var tbodyHtml = "";
		           $("assign",xml).each(function(index){
		               tbodyHtml +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
		               			 +"<td><a class=\"opLink\" onclick=\"delGroups(this);\">删除</a><input type=\"hidden\" value=\"" + $(this).text() + "\"></td>"
		               			 +"<td>" + $(this).text() + "</td></tr>";
		           })
		          $("#bindGroupList > tbody").html(tbodyHtml).find("td:empty").html("&nbsp;");
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
                          + "<td>" + groupname + "</td>"
                          + "</tr>";
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