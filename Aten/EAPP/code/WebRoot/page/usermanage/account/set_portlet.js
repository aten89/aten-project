$(initPortletConfig);

function initPortletConfig(){
    
    $("#savePortletConfig").click(
        function(e){
            $(this).attr("disabled","true");
            var portletConfig = "";
            var o = this;
            $(".portOp","#_customPortletDIV").each(function(idx){
     			portletConfig += $("input",this).val() + ",1," + (idx + 1) + "@";
			});
            portletConfig = portletConfig.substr(0,portletConfig.length - 1);

            $.ajax({
                type : "POST",
        		cache: false,
                async : true,
        		url  : "m/user_account/setdefault",
        		dataType : "json",
        		data : {
        			accountID : $("#hidAccountId").val(),
        			portletConfig : portletConfig
        		},
                success : function(data){
                	if ($.checkErrorMsg(data) ) {
                		$.alert("保存版式布局成功！");
                	}
                    $(o).removeAttr("disabled");
                }
            });
        }
    );
    //载入用户已定制的版块
    loadPortal();
    //载入剩余的版块
    loadOtherPortal();
    
    //拖动效果
    $(".portOp").draggable({
		helper:  function(ui) {
			var width = $(this).width();
			var dragObj = $(this).clone();
			dragObj.width(width);//原本百分比的宽度，拖动副本改为固定
			return dragObj;
		}
	});
	$('#_noCustomPortletDIV').droppable({
		accept: '.portOp',
		tolerance: 'pointer',
		drop: function(ev, ui) {
			ui.draggable.height(18).width(110);
			$('#_noCustomPortletDIV').append(ui.draggable);
		}
	});
	
	var draggableFlag = false;//是否拖动
	var draggable;//拖动的对像
	$("#_customPortletDIV").droppable({
		accept: '.portOp',
		tolerance: 'pointer',
		drop: function(ev, ui) {
			draggable = ui.draggable;
			draggableFlag = true;
		}
	});
	
	
	$("#_customPortletDIV").mousemove(function(e){
		if (draggableFlag) {
			draggableFlag = false;
			var portlet;//是否找到鼠标落在的门户块
			var preProtlet;//右边最近的门户块
			
			var scrollTop = this.scrollTop;
			var scrollLeft = this.scrollLeft;
			
			$(".portOp", this).each(function(){
				var lt = this.offsetLeft;
				var rt = this.offsetLeft + this.offsetWidth;
				var topY = this.offsetTop;
				var bottomY = this.offsetTop + this.offsetHeight;
				e = e || event;
				var mouseXX = e.clientX + scrollLeft;
				var mouseYY = e.clientY + scrollTop;
				if(mouseXX>lt && mouseXX<rt && mouseYY>topY && mouseYY<bottomY){//鼠标坐标落在门户块内
					portlet = this;
				}
				if (portlet == null && mouseYY > topY && mouseYY < bottomY) {//鼠标Y轴落在门户块内
					if (preProtlet == null || preProtlet.offsetLeft < lt) {
						preProtlet = this;
					}
				}
			});
			
			var width = draggable.attr("width");
			var height = draggable.attr("height");
			draggable.height(height).width(width);
			portlet =  portlet || preProtlet;
			if (portlet) {
				if($(portlet).attr("id") != draggable.attr("id")) {
					$(portlet).after(draggable);//添加到找到门户块的后面
				}
			} else {
				$(".portMain","#_customPortletDIV").append(draggable);
			}
		}
	});
};


//解释用户门户XML信息，添加到相应的列中
//add by zsy
function loadPortal() {
	$.ajax({
		type : "POST",
        async : false,
        cache : false,
      	url : "m/user_account/userportal",
      	dataType : "json",
      	data : {
        			accountID : $("#hidAccountId").val()
        		},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		var dataList = data.portlets;
				if (dataList) {
					$(dataList).each(function(i) {
						var style = dataList[i].style;
	            		var width = "33.3%";
	            		var height = "104px";
	            		if (!style){
	            			style = "width:33.3%;height:220px";
	            		}
		        		style = style.replace(/width:([0-9.]+)([\%px]+);height:([0-9.]+)([\%px]+)/g,function(a,b,c,d,e){
							if (c) width = b + c;
							if (d) height = d / 2 + e;
							return "";
						}); 
	                   	$(".portMain","#_customPortletDIV").append("<div id='" + dataList[i].portletID + "' width='" + width + "' height='" + height + "' class='portOp' style='width:" + width + ";height:" + height + ";'><div class='portDiv'><h1>"+ dataList[i].portletName +"</h1><input type='hidden' value='" + dataList[i].portletID + "'/><div></div>");
					});
				}
        	}
        }
    });
};

function loadOtherPortal(){
 	$.ajax({
		type : "POST",
		async : false,
        cache : false,
      	url : "m/user_account/userallportal",
      	dataType : "json",
      	data : {
        			accountID : $("#hidAccountId").val()
        		},
         success : function(data){
        	if ($.checkErrorMsg(data) ) {
				var dataList = data.portlets;
				if (dataList) {
					$(dataList).each(function(i) {
						 if ($("#" + dataList[i].portletID).length == 0) {
	                    	var style = dataList[i].style;
	                		var width = "33.3%";
	                		var height = "104px";
	                		if (!style){
	                			style = "width:33.3%;height:220px";
	                		}
			        		style = style.replace(/width:([0-9.]+)([\%px]+);height:([0-9.]+)([\%px]+)/g,function(a,b,c,d,e){
								if (c) width = b + c;
								if (d) height = d / 2 + e;
								return "";
							}); 
							$("#_noCustomPortletDIV").append("<div id='" + dataList[i].portletID + "' width='" + width + "' height='" + height + "' class='portOp' style='width:110px;height:18px;'><div class='portDiv'><h1>"+dataList[i].portletName+"</h1><input type='hidden' value='" + dataList[i].portletID + "'/></div></div>");
	                    };
					});
				}
        	}
        }
    });   
};