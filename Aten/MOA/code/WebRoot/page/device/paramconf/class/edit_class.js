//设备属性配置--新增
var classTypeSelect;
var mainFrame = $.getMainFrame();
$(initDeviceConfigEdit);

function initDeviceConfigEdit() {
  $("#sblb").ySelect({width:99});
  initChecklOptionList();
  initAllOptionList();
  $('#addOption').click(function(){
        addOptionSel();
    });
   $('#setTop').click(function(){
        setTop();
    });
    $('#setUp').click(function(){
        setUp();
    });
    $('#setDown').click(function(){
        setDown();
    });
    $('#setBottom').click(function(){
        setBottom();
    });     
    $('#saveOrder').click(function(){
        saveOrder();
    });
    $('#delete').click(function(){
        deleteOptionSel();
    });
   $("#setTop,#setUp,#setDown,#setBottom,#delete").attr("disabled","true").addClass("icoNone");
   //保存
   $('#saveBtn').click(function(){
        saveDeviceClass();
    });
    //取消
    $('#closeBtn').click(function(){
         //关闭
		mainFrame.getCurrentTab().close();
    });
    initClassTypeDiv();
}
function initClassTypeDiv() {
	classTypeSelect = $("#deviceTypeDiv").ySelect({width:99, url:"m/data_dict?act=selectdevtype",
		afterLoad:function(){
			classTypeSelect.addOption("", "请选择...", 0);
			if($("#deviceType").val()!=""){
				classTypeSelect.select($("#deviceType").val());
			}
			
		}
	});
}
function initChecklOptionList(){
	 $('#checkProperty').empty();
	var id = $('#id').val();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/device_class",
        data : "act=findcheckproperty&id="+id,
        success : initCheckSubSuccess,
        error : $.ermpAjaxError
    }); 
}

function initCheckSubSuccess(xml){
    //解析XML中的返回代码
    var messageCode = $("message",xml).attr("code");
    var StrHtml = "";
    if(messageCode == "1"){
        $(xml).find("config-items").each(function(index){
            StrHtml+="<option value=\"" + $(this).find("item-id").text()  + "\">" + $(this).find("item-name").text() + "</option>";
        });
        if(StrHtml!=""){
        	 $("#delete").removeAttr("disabled").removeClass("icoNone");
        }
        $("#checkProperty").html(StrHtml);
    	//列表发生改变事件，则对应的按钮发生相应的可用状态变化
        $("#checkProperty").change(
            function(e){
                $("#setTop,#setUp,#setDown,#setBottom,#delete").removeAttr("disabled").removeClass("icoNone");
                
                if(this.selectedIndex == 0){
                    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
                }
                if(this.selectedIndex == this.length - 1){
                    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
                } 
            }
        );
    }else {
        $.alert($("message",xml).text());     
    } 
}
//设置到顶部
function setTop(){
    var topVal=$('option','#checkProperty').eq(0).val();       
    $('option:selected','#checkProperty').each(function(){
        $(this).insertBefore($('option[value=\''+topVal+'\']','#checkProperty'));
    });
    //设置按钮权限
    $("#setDown,#setBottom,#saveOrder").removeAttr("disabled").removeClass("icoNone");           
    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
}

//设置到底部
function setBottom(){
    $('option:selected','#checkProperty').each(function(){
        $(this).appendTo('#checkProperty');
    });
    //设置按钮权限    
    $("#setTop,#setUp,#saveOrder").removeAttr("disabled").removeClass("icoNone");     
    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");   
}

//设置上移
function setUp(){
    //取得选中的顶
    var selectTopVal=$('option:selected','#checkProperty').eq(0).val();
    var preIndex=0;
    //搜索要插入的位置
    $('option','#checkProperty').each(function(index){
        if ($(this).val() == selectTopVal){
            preIndex=index-1;
            return false;
        }
    });
    var preVal=$('option','#checkProperty').eq(preIndex).val();
    //逐个插入
    $('option:selected','#checkProperty').each(function(){
        $(this).insertBefore($('option[value=\''+preVal+'\']','#checkProperty'));
    }); 
    //判断是否到顶,控制按钮
    if (preIndex == 0){
        $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setTop,#setUp").removeAttr("disabled").removeClass("icoNone");    
    }  
    $("#setDown,#setBottom,#saveOrder").removeAttr("disabled").removeClass("icoNone");        
}

//设置下移
function setDown(){
    //取得选中底部
    var selectDownVal=$('option:selected','#checkProperty').eq($('option:selected','#checkProperty').length-1).val();
    var nextIndex=0;
    //搜索插入位置
    $('option','#checkProperty').each(function(index){
        if ($(this).val() == selectDownVal){
            nextIndex=index+1;
            return false;
        }
    });
    var nextVal=$('option','#checkProperty').eq(nextIndex).val();
    //逐个插入
    $('option:selected','#checkProperty').each(function(){
        $(this).insertAfter($('option[value=\''+nextVal+'\']','#checkProperty'));
        nextVal=$(this).val();
    });
    //判断是否到底,控制按钮
    if (nextIndex == ($('option','#checkProperty').length-1)){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
    }   
    $("#setTop,#setUp,#saveOrder").removeAttr("disabled").removeClass("icoNone");          
}
function initAllOptionList(){
	 $('#allProperty').empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/device_class",
        data : "act=findallproperty",
        success : initSubSuccess,
        error : $.ermpAjaxError
    }); 
}

function initSubSuccess(xml){
    //解析XML中的返回代码
    var messageCode = $("message",xml).attr("code");
    var StrHtml = "";
    if(messageCode == "1"){
        $(xml).find("device-property").each(function(index){
        	var flag = true;
        	var propertyName = $(this).find("property-name").text();
    	  	var option = $(this);
    	  	var id=option.attr("id");
        	$('option','#checkProperty').each(function(){
		            var allIdList=$(this).val();
		            if(allIdList==id){
		            	flag = false;
		            	return;
		            }
		    });
		    if(flag){
		    	StrHtml+="<option value=\"" + id  + "\">" + propertyName+ "</option>";
		    }
            
        });
        $("#allProperty").html(StrHtml);
    }else {
        $.alert($("message",xml).text());     
    } 
}

function addOptionSel(){
	var StrHtml = "";
	$('option:selected','#allProperty').each(function(){
            var ordIdList=$(this).val();
            var ordNameList=$(this).text();
            $(this).remove();
        	StrHtml+="<option value=\"" + ordIdList  + "\">" + ordNameList + "</option>";
    });
   if ($.trim(StrHtml) != ""){
            $("#checkProperty").append(StrHtml);
    }
}

function deleteOptionSel(){
	var StrHtml = "";
	$('option:selected','#checkProperty').each(function(){
            var ordIdList=$(this).val();
             var ordNameList=$(this).text();
            $(this).remove();
        	StrHtml+="<option value=\"" + ordIdList  + "\">" + ordNameList + "</option>";
    });
   if ($.trim(StrHtml) != ""){
            $("#allProperty").append(StrHtml);
    }
}

function saveDeviceClass(){
	var name = $.trim($("#name").val());
	var remark = $.trim($("#remark").val());
	var classType=classTypeSelect.getValue();//资产类别
	if(classType==""){
		$.alert("请选择资产类别");
		return;
	}
	result = $.validChar(name);
	if (name.length <=0) {
		$.alert("设备类型名称不能为空");
		$("#name").focus();
		return;
	}
	if (result) {
		$.alert("设备类型名称不能包含非法字符：" + result);
		$("#name").focus();
		return;
	}
	if (name.length >100) {
		$.alert("设备类型名称长度不能大于100个字节");
		$("#name").focus();
		return;
	}
	
	result = $.validChar(remark);
	if (result) {
		$.alert("备注不能包含非法字符：" + result);
		$("#remark").focus();
		return;
	}
	if (remark.length >300) {
		$.alert("备注长度不能大于300个字节");
		$("#remark").focus();
		return;
	}
	var cfgItemIdList="";
    $('option','#checkProperty').each(function(){
    	if($(this).val()!=""){
    		cfgItemIdList+="&cfgItemIdList="+$(this).val();  
    	}
         
    });
//    if ($.trim(cfgItemIdList)=="") {
//		$.alert("设备属性配置项不能为空");
//		return;
//	}
	$("#saveBtn,#closeBtn").attr("disabled","true").addClass("icoNone");
	var id = $.trim($("#id").val());
	var act = "";
	if(id==""){
		act="add"
	}else{
		act="modify"
	}
	$.ajax({
		type : "POST",
		cache: false,
		url  : "m/device_class?1=1"+cfgItemIdList,
		data : {
			act: act,
			id:id,
			classType:classType,
			name:name,
			remark:remark
		},
   		success : saveSuccess,
		error : $.ermpAjaxError
	});
};

//保存成功回调函数
function saveSuccess(xml){
    var messageCode = $("message",xml).attr("code");
    var messageInfo = $("message",xml).text();
	
    if(messageCode == "1"){
    	$.alert("保存成功", function(){
	    	 //刷新父列表
			mainFrame.getCurrentTab().doCallback();
	       //关闭
			mainFrame.getCurrentTab().close();
    	});
    	
    }else{
    	$.alert(messageInfo);
    	$("#saveBtn,#closeBtn").removeAttr("disabled","true").removeClass("icoNone");
    };
};



