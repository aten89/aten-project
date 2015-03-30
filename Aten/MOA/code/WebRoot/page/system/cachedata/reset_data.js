$(initDataDictReload);
function initDataDictReload(){
	$.handleRights(
        {
            "reloaddatadict" : $.SysConstants.REDEPLOY
        }
    );
    //重新加载数据字典
    $("#reloaddatadict").click(function(){
            $.ajax({
                type : "POST",
        		cache: false,
                async : true,
        		url  : "m/datadictreload",
        		data : {
        			act : $.SysConstants.REDEPLOY
        		},
                success : function(xml){
                	var message = $("message",xml);
                	if(message.attr("code") == "1") {
                		$.alert($("message",xml).text());
//                        alert("重加载成功！");
                	} else{
                        $.alert($("message",xml).text());
                	}
                },
                error : function(res){
                	$.alert("AJAX调用出错！");
                }
            });
        });
       
};