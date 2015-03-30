$(initDataDictReload);
function initDataDictReload(){
//	$.handleRights(
//        {
//            "reloaddatadict" : $.SysConstants.REDEPLOY
//        }
//    );
    $("#reloaddatadict").click(function(){
            $.ajax({
                type : "POST",
        		cache: false,
                async : true,
        		url  : "m/systemdata/redeploy",
        		dataType : "json",
                success : function(data){
                	if ($.checkErrorMsg(data) ) {
                		 $.alert("重加载成功！");
                	}
                }
            });
        });
};