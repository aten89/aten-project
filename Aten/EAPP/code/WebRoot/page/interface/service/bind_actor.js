var isChange = false;
$(initBindRight);
function initBindRight(){
    //$.getMainFrame().getCurrentTab().setTitle("帐号-服务配置", "接口服务绑定接口帐号");
    
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#bindJrzh").attr("disabled","true").addClass("icoNone");
    //保存按扭不可用
    $("#saveActorService").attr("disabled","true").addClass("icoNone");
    var selectService = $("#hidServiceId").val();
    /*如果ID为空则不能进行修改操作,不为空则可修改且列出唯一项*/
    if(selectService=='null' || selectService== ""){
    	$("#modFwpz").attr("disabled","true").addClass("icoNone");
    	$.ajax({
			type : "POST",
			cache: false,
			async : true,
			dataType : "json",
		   	url  : "m/service/xmlservices",
		    success : searchServiceBindSuccess
		});
    }else{
    	$("#txtSearchService").attr("disabled","true").addClass("icoNone");
    	$("#search_Service").attr("disabled","true").addClass("icoNone");
    	var serviceName = $("#hidServiceName").val();
    	var StrHtml ="";
    	StrHtml += "<div><input name=\"serviceid_1\" type=\"radio\" value=\"" + selectService + "\" id=\"" + selectService + "\" onclick=\"javascript:selectService('"+selectService+"','"+serviceName+"');\"/>" 
                                   + "<label>" + serviceName + "</label></div>";

		$(".jsdw").html(StrHtml);
    	$("#"+selectService).click();
    }
    //服务列表筛选
    $("#search_Service").click(
        function(e){
        	$(".jsdw > div:hidden").show();
        	var tvalue = $.trim($("#txtSearchService").val());
            if(tvalue == ""){
                return;
            };
            $(".jsdw label:not(:contains('" + tvalue + "'))").parent().hide();
            //选中的不隐藏
            $(".jsdw input:checked").parent().show();
        }
    );
	$.EnterKeyPress($("#txtSearchService"),$("#search_Service")); 
    
    //绑定接口帐号
    $("#saveActorService").click(
        function(e){
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/service/bindactor",
        		dataType : "json",
                data : $("#mainForm").serialize(),
                success : function(data){
                	if ($.checkErrorMsg(data) ) {
                		 $.alert("绑定成功！");
                	}
                   $("#saveActorService").attr("disabled","true").addClass("icoNone");
                   isChange = false;
                }
            });
        }
    );
   //搜索接入帐号
   $("#searchActorAccount").click(
	   	function(e){
		   	var keyword= $.trim($("#actorkeyword").val());
		  //  var displayName= $.trim($("#actorName").val());
		    $.ajax({
		        type : "POST",
		        cache: false,
		        async : true,
				url  : "m/actor_account/xmlactors",
		        data : "keyword="+keyword,
		        dataType : "json",
		        success : function(data){
		        	if ($.checkErrorMsg(data) ) {
        				var dataList = data.actorAccounts;
        				if (dataList) {
        					var moduleActionHTML = "";
        					$(dataList).each(function(i) {
        						moduleActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		                                         + "<td><a class=opLink>绑定</a><input type=\"hidden\" value=\"" + dataList[i].actorID + "\"></td>"
		                                         + "<td>" + dataList[i].displayName + "</td>" 
		                                         + "<td>" + dataList[i].accountID + " </td>"
		                                         + "</tr>";
        					});
        					$("#actorAccountList > tbody").html(moduleActionHTML).find("td:empty").html("&nbsp;");
        					
        					//点击绑定事件
			                $("#actorAccountList a").click(
			                    function(e){
			                        if($(".jsdw input:checked").length == 0){
			                            $.alert("请选择要绑定的服务！");
			                            return;
			                        };
			                        
			                        //判断是否已被添加
			                        if($("#bindActorList input[value='" + $("+input",this).val() + "']").length != 0){
			                        	$.alert("不能重复绑定");
			                            return;
			                        };
			                        
			                        $("#bindActorList > tbody").append(
			                            $(this).parent().parent().clone().removeClass("over")
			                            
			                        )
			                        .find("a:last").html("删除").click(
			                            function(e){
			                                $(this).parent().parent().remove();
			                                $("#saveActorService").removeAttr("disabled").removeClass("icoNone");
			                                isChange = true;
			                            }
			                        ).next().attr("name","actorIDs");
			                        
			                        $("#saveActorService").removeAttr("disabled").removeClass("icoNone");
			                        isChange = true;
			                    }
			                );
		                
        				} else {
        					$("#actorAccountList > tbody").html("");
        				}
		            }
		        }
		    });
	   	}
   );
//   $("#searchActorAccount").click();
    
    //回车搜索
 //   $.EnterKeyPress($("#actorName"),$("#searchActorAccount"));
    $.EnterKeyPress($("#actorkeyword"),$("#searchActorAccount"));        
}

//搜索所有有效服务
function searchServiceBindSuccess(data){
	if ($.checkErrorMsg(data) ) {
		var dataList = data.services;
		if (dataList) {
			var StrHtml = "";
			$(dataList).each(function(i) {
				var sid = dataList[i].serviceID;
	    		var sName = dataList[i].serviceName;
				StrHtml += "<div><input name=\"serviceid_1\" type=\"radio\" value=\"" + sid + "\"  onclick=\"javascript:selectService('"+sid+"','"+sName+"');\"/>" 
	            	+ "<label>" + sName + "</label></div>";
			});
			$(".jsdw").html(StrHtml); 
		}
	}
}

//左边的单选单击
function selectService(o, name){
	if (isChange) {
		$.confirm("是否先保存当前修改？", function(r){
    		if (r) {
    			$("#saveActorService").click();
    		} 
    		loadBindedAtcors(o, name);
    		
    	});
		isChange=false;
	} else {
		loadBindedAtcors(o, name);
	}
}

function loadBindedAtcors(o, name) {
 //   $("#hidServiceId").val(o);
 //   $("#hidServiceName").val(name);
 //   $("#modFwpz").removeAttr("disabled").removeClass("icoNone");
	$.ajax({
		type : "POST",
		async : true,
		cache: false,
	   	url  : "m/service/bindedactors",
	   	dataType : "json",
		data : {serviceID: o},
	    success : function(data){
	    	if ($.checkErrorMsg(data) ) {
	    		var dataList = data.actorAccounts;
        		if (dataList) {
        			var rolesActionHTML = "";
					$(dataList).each(function(i) {
						 rolesActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		                               + "<td><a class=opLink>删除</a><input type=\"hidden\" name=\"actorIDs\" value=\"" + dataList[i].actorID + "\"></td>" 
		                               + "<td>"+dataList[i].displayName+"</td>" 
		                               + "<td>"+dataList[i].accountID+"</td>"
		                               + "</tr>";
					});
					$("#bindActorList > tbody").html(rolesActionHTML).find("td:empty").html("&nbsp;");
			        $("#serviceid").val(o);
			        //点击解绑定事件
			        $("#bindActorList > tbody a").click(
			            function(e){
			                $(this).parent().parent().remove();
			                $("#saveActorService").removeAttr("disabled").removeClass("icoNone");
			                isChange = true;
			            }
			        );
				} else {
					$("#bindActorList > tbody").empty();
				}
	    	}
		}
	});	
}