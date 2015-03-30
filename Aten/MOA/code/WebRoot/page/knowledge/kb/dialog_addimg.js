var params = window.dialogArguments; 
var id=params.id;
$(initTreeDialog);

function initTreeDialog(){
	$("#selImg").click(function(){
		params.szURL =$("input[name='img'][checked]","#selPic").val();			
		
		if (!params.szURL) {
			$.alert("请选择图片");
			return;
		}
		window.returnValue=true;
		window.close();
	});
	imageList(id);
}

function ajaxFileUpload() {
		var uploadImg = $("#uploadImg").val();
		if(uploadImg==""){
			return;
		}
		var ext = uploadImg.substr(uploadImg.length-4, 4);
		ext = ext.toLowerCase();
		if (".jpg.jpeg.gif.bmp.png".indexOf(ext) < 0) {
			$.alert("只能上传图片格式");
			return;
		}
		$.ajaxFileUpload
		(
			{
				url:'m/knowledge?act=uploadcontentatt', 
				secureuri:false,
				fileElementId:'uploadImg',
				dataType: 'xml',
				success: function (data, status)
				{
					var message = $("message",data);
	        		if(message.attr("code") == "1") {
						imageList(id);
					} else {
						$.alert(message.text());
					}
				},
				error: function (data, status, e)
				{
					$.alert(e);
				}
			}
		)
		return false;
}  

function imageList(id){
	$.ajax({
			type : "POST",
			cache: false,
	   		async : true,
	      	url : "m/knowledge",
	      	dataType : "xml",
	      	data : {
	      		act : "knowledgeimagelist",
	      		referId	: id
	      	},
	        success : function(xml){
	        	var message = $("message",xml);
	        	var attHtml="";
	        	if(message.attr("code") == "1") {
	        		$("attachment", xml).each(function (index){
	        			var attachment = $(this);
		        		attHtml +="<li><div class=\"pic01\"><img src=\""+$("file-path",attachment).text()+"\" onclick='selectImg(this)'/></div><div class=\"text01\"><input name=\"img\" type=\"radio\" value=\""+$("file-path",attachment).text()+"\" />"
		        				+ $("display-name",attachment).text()+"&nbsp;<span> <img src='themes/comm/images/treeDel.gif'class='delPic'  onclick=\"delImage('"+attachment.attr("id")+"','"+id+"','img')\"/></span></div></li>";	        			

	        		});
	        		
	        	}
	        	$("#selPic").html(attHtml);
	        },
        	error : $.ermpAjaxError
		}
	);
}

function selectImg(obj) {
	var inputLi = $(obj).parent().next().find("input");
	inputLi.click();
}

function delImage(attId,referId){
	$.ajax({
			type : "POST",
			cache: false,
	   		async : true,
	      	url : "m/knowledge",
	      	dataType : "xml",
	      	data : {
	      		act : "delcontentatt",
	      		referid	: referId,
	      		attId	: attId
	      	},
	        success : function(xml){
	        	var message = $("message",xml);
	        	if(message.attr("code") == "1") {
	        		imageList(referId);
	        	}
	        },
        	error : $.ermpAjaxError
		}
	);
}
