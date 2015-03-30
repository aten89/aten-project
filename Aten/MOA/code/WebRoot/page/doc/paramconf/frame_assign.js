$(initImpowerFrame);
function initImpowerFrame(){

   $.handleRights({
        "ImpowerUser" :  $.SysConstants.BIND_USER,
        "ImpowerPost" :  $.SysConstants.BIND_POST,
        "ImpowerGroup" : $.SysConstants.BIND_GROUP
    });
  
    var title = $("#title").val();
    
	$.ajax({
		type : "POST",
		cache: false,
		url  : "m/doc_class?act=init_assign_detail&title="+encodeURI(title)+"&page=bind_user.jsp",
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
				url  : "m/doc_class?act=init_assign_detail&title="+encodeURI(title)+"&page=bind_user.jsp",
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
				url  : "m/doc_class?act=init_assign_detail&title="+encodeURI(title)+"&page=bind_group.jsp",
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
				url  : "m/doc_class?act=init_assign_detail&title="+encodeURI(title)+"&page=bind_post.jsp",
				success : function(txt){
					$("#contetnMain").html(txt);
				}
			});
   		}
    );
	
}