$(initImpowerFrame);
function initImpowerFrame(){
//   $.handleRights({
//        "ImpowerUser" :  $.OaConstants.ASSIGN,
//        "ImpowerPost" :  $.OaConstants.ASSIGN,
//        "ImpowerGroup" : $.OaConstants.ASSIGN
//    });
    //关闭
	$("#closeBnt").click(function(){
		$.getMainFrame().getCurrentTab().close();
	});
    var flag = $("#flag").val();
    var title =$("#title").val();
    
	$.ajax({
		type : "POST",
		cache: false,
		url  : BASE_PATH +"m/device_assgin?act=init_assign_detail&flag=" + flag+"&title="+encodeURI(title)+"&page=bind_user.jsp",
		success : function(txt){
					$("#contetnMain").html(txt);
				}
	});
	//绑定用户
    $("#ImpowerUser").click(
        function(e){
			$.ajax({
				type : "POST",
				cache: false,
				url  : BASE_PATH +"m/device_assgin?act=init_assign_detail&flag=" + flag+"&title="+encodeURI(title)+"&page=bind_user.jsp",
				success : function(txt){
					$("#contetnMain").html(txt);
				}
			});
   		}
    );
    
    //绑定机构
    $("#ImpowerGroup").click(
        function(e){
			$.ajax({
				type : "POST",
				cache: false,
				url  : BASE_PATH +"m/device_assgin?act=init_assign_detail&flag=" + flag+"&title="+encodeURI(title)+"&page=bind_group.jsp",
				success : function(txt){
					$("#contetnMain").html(txt);
				}
			});
   		}
    );
    
    //绑定职位
    $("#ImpowerPost").click(
        function(e){
			$.ajax({
				type : "POST",
				cache: false,
				url  : BASE_PATH +"m/device_assgin?act=init_assign_detail&flag=" + flag+"&title="+encodeURI(title)+"&page=bind_post.jsp",
				success : function(txt){
					$("#contetnMain").html(txt);
				}
			});
   		}
    );
	
}