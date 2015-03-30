var icoTimer = null;
var typeList = null;
var dialogWin = null;
$(initShortCurConfig);
		
function initShortCurConfig(){
    //初始化拖动效果
    initShortDrag();
    
    //保存定制
    $("#saveShortCut").click(function(e){
            $("#cutEnabledForm input:hidden").attr("name","shortCutsEnable");
            $("#cutDisabledForm input:hidden").attr("name","shortCutsDisable");
            var o = this;
            $(o).attr("disabled","true");
            $.ajax({
        		type : "POST",
        		cache: false,
                async : true,
        		url  : "l/frame/savescms",
        		dataType : "json",
        		data : {
        			enableScmConfig : $("#cutEnabledForm").serialize().replace(/shortCutsEnable=/gi,"").replace(/&/gi,","),
        			disableScmConfig : $("#cutDisabledForm").serialize().replace(/shortCutsDisable=/gi,"").replace(/&/gi,",")
        		},
        		success : function(data) {
        			if ($.checkErrorMsg(data) ) {
        				$.alert("保存快捷方式成功！");
        				parent.loadShortCutMenu();//刷新主页框架的快捷列表，
        			}
                    $(o).removeAttr("disabled");
                },
                error : function(){
                    $(o).removeAttr("disabled");
                }
            });
        }
    );
    
    //新增快捷方式按扭
    $("#addShortCut").click(function(){
    	dialogWin = $.showModalDialog("新增快捷菜单", BASE_PATH + "l/frame/initedit", 600, 320, function(){
			if (window.returnValue1) {
				var obj = window.returnValue1;
				var addHtml = $("<div class=\"icoBk\" onmouseover=\"this.className='icoBk icoOver'\" onmouseout=\"this.className='icoBk'\">"
							+ "<img src=\"" + obj.shortCutIco +"\"/><label>" + obj.shortCutName
							+ "</label><input type=\"hidden\" id=\"hid" + obj.shortCutId + "\" value=\"" + obj.shortCutId + "\"/></div>");
				$("#cutEnabledForm").find(".noSel").append(addHtml);
				//拖动效果
			    addHtml.draggable({
					helper:  "clone"
				});
				//鼠标移过显示工具栏
			   addHtml.mouseover(function(){
			        clearTimeout(icoTimer);
			   		$("#manageToolbar").show().css({
			       		left : $(this).offset().left - 22,
			          	top : $(this).offset().top + $(this).height() + 12
			      	});
			       	$("#hidShortCutId").val($("input",this).val());                    
					}).mouseout(function(){
				            clearTimeout(icoTimer);
				            icoTimer = setTimeout(
				                function(){
				                    $("#manageToolbar").hide();
				                },
				                1000
				            );
					});
					
				parent.loadShortCutMenu();//刷新主页框架的快捷列表，
			}
		});
	});
}

function initShortDrag() {
	//拖动效果
    $(".icoBk").draggable({
		helper:  "clone"
	});
	
	$("#_customShortCut").droppable({
		accept: '.icoBk',
		tolerance: 'pointer',
		drop: function(ev, ui) {
			$("#_customShortCut").append(ui.draggable);
		}
	});
	$("#_noCustomShortCut").droppable({
		accept: '.icoBk',
		tolerance: 'pointer',
		drop: function(ev, ui) {
			$("#_noCustomShortCut").append(ui.draggable);
		}
	});

    //悬浮操作按扭
    $("#manageToolbar").mouseover( function(){
  		clearTimeout(icoTimer);
	}).mouseout(function(){
        clearTimeout(icoTimer);
        icoTimer = setTimeout(
            function(){
                $("#manageToolbar").hide();
            },
            1000
        );
	});
    
    //鼠标移过显示工具栏
    $(".icoBk").mouseover(function(){
        clearTimeout(icoTimer);
   		$("#manageToolbar").show().css({
       		left : $(this).offset().left - 22,
          	top : $(this).offset().top + $(this).height() + 12
      	});
       	$("#hidShortCutId").val($("input",this).val());                    
	}).mouseout(function(){
            clearTimeout(icoTimer);
            icoTimer = setTimeout(
                function(){
                    $("#manageToolbar").hide();
                },
                1000
            );
	});
}

function modifyCut(){
	dialogWin = $.showModalDialog("修改快捷菜单", BASE_PATH + "l/frame/initedit?shortCutMenuID=" + $("#hidShortCutId").val(), 600, 320, function(){
			if (window.returnValue1) {
				var obj = window.returnValue1;
				var imgo = $("#hid" + obj.shortCutId).parent();
				imgo.find("label").text(obj.shortCutName);
				imgo.find("img").attr("src", obj.shortCutIco);
				parent.loadShortCutMenu();//刷新主页框架的快捷列表，
			}
	});
};

function deleteCut(){
	$.confirm("是否删除该快捷方式？", function(r){
		if (r) {
			$.ajax({
		   		type : "POST",
		   		cache: false,
		    	async : true,
		   		url  : "l/frame/delshortcut",
		   		dataType : "json",
		   		data : {
		            shortCutMenuID : $("#hidShortCutId").val()
		   		},
		   		success : function(data){
		   			if ($.checkErrorMsg(data) ) {
		   				$("#hid" + $("#hidShortCutId").val()).parent().remove();
		    			$("#manageToolbar").hide();
		    			parent.loadShortCutMenu();//刷新主页框架的快捷列表，
		   			}
		  		}
			});
		}
	});
}

function closeDialog() {
		if (this.dialogWin) {
			this.dialogWin.close();
		}
	};