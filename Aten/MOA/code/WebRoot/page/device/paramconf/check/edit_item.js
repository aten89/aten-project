//设备检查配置--新增
var mainFrame = $.getMainFrame();
var deviceClassSel;//设备类别下拉列表
var deviceTypeSel;
$(initDeviceConfigEdit);

function initDeviceConfigEdit() {
	//$.cancelUntitledAjax();
	
	var deviceClassID = $.trim($("#deviceClassID").val());
	initDeviceTypeSel($.trim($("#deviceTypeCode").val()), deviceClassID);
//  	initDeviceClassSel(deviceClassID);
	$("#addItemBtn").click(function(){
		addItem();
	});
	initSort();
	$("#saveBtn").click(function(){
		saveDeviceCheckItemCfg();
	});
	$("#closeBtn").click(function(){
		  //关闭
		mainFrame.getCurrentTab().close();
	});
	$.EnterKeyPress($("#itemName"),$("#addItemBtn"));
}
/**
 * 初始化设备类型下拉列表
 * @param {} deviceTypeCode 设备类型代码
 */
function initDeviceTypeSel(deviceTypeCode, deviceClassCode) {
	deviceTypeSel = $("#deviceTypeSel").ySelect({
		width : 99, 
		url:BASE_PATH+"m/data_dict?act=selectdevtype",
		afterLoad : function() {
			deviceTypeSel.addOption("", "请选择...", 0);
			if (deviceTypeCode != null && deviceTypeCode != "") {
				deviceTypeSel.select(deviceTypeCode);
				deviceTypeSel.disable(true);
			} else {
				deviceTypeSel.select(0);
			}
			
		},onChange : function(value, text) {
			initDeviceClassSel(value, deviceClassCode);
		}
	});     
}
/**
 * 初始化设备类别下拉列表
 * @param {} deviceClassCode 设备类别代码
 */
function initDeviceClassSel(deviceTypeCode, deviceClassCode) {
	if(deviceTypeCode==""){
		classSelect = $("#deviceClassSel").ySelect({
		width : 99});   
		classSelect.addOption("", "请选择...", 0);
		return;
	}
	deviceClassSel = $("#deviceClassSel").ySelect({
		width : 99, 
		url:BASE_PATH+"m/device_class?act=classselect&deviceType=" + deviceTypeCode,
		afterLoad : function() {
			deviceClassSel.addOption("", "请选择...", 0);
			if (deviceClassCode != null && deviceClassCode != "") {
				deviceClassSel.select(deviceClassCode);
				deviceClassSel.disable(true);
			} else {
				deviceClassSel.select(0);
			}
		}, 
		onChange : function(value, text) {
		}
	});     
}

/**
 * 追加设备检查项
 */
function addItem() {
	var remark = $.trim($("#remark").val());
	if($.validInput("remark", "备注", false, "\<\>\'\"", 512)){
		return false;
	}
	var itemName = $.trim($("#itemName").val());
	if($.validInput("itemName", "检查项名称", true, "\<\>\'\"；;", 128)){
		return false;
	}
	var bAddSuccess = true;
	$('#childModules option').each(function(){
		if ($(this).text() == itemName) {
			$.alert("添加失败：检查项已存在!");
			bAddSuccess = false;
			$("#itemName").val("");
			return false;
		}
	});
	if (!bAddSuccess) {
		return;
	}
	var randomString = (Math.random()) * 10000000000;
	$("#childModules").append("<option value='" + randomString + "'>" + itemName + "</option>");
	setTimeout(function() { 
		$('#childModules option').attr('selected',''); 
	}, 1);
	setTimeout(function() { 
		$('#childModules option:last').attr('selected','selected'); 
	}, 1);
	$("#itemName").val("");
	$("#setTop,#setUp,#setDown,#setBottom,#deleteItem").removeAttr("disabled").removeClass("icoNone");
    if($("#childModules").get(0).selectedIndex == 0){
        $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
    }
    if($("#childModules").get(0).selectedIndex == $("#childModules").get(0).length - 1){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    } 
}

function initSort(){
    //设置工具栏的状态为禁用状态
    if ($("#curModuleId").val() != "") {
    	$("#sortSubModule").attr("disabled","true").addClass("icoNone");
    	$("#addSubModule,#modifyModule,#delModule,#bindModuleAction").removeAttr("disabled","true").removeClass("icoNone");     
    }else {
    	$("#sortSubModule,#modifyModule,#delModule,#bindModuleAction").attr("disabled","true").addClass("icoNone");
    	$("#addSubModule").removeAttr("disabled","true").removeClass("icoNone");
    }
    //建立副本,以备重置使用
    $("#childModules").clone().attr("id","childModulesBak").insertAfter("#childModules").hide();
	
	//重置按钮事件
    $("#reset").click(function(e){
             //注：FF在进行副本的拷贝时，也会拷贝注释，这将会发生错误
             $("#childModules").html($("#childModulesBak").html());
             $("#setTop,#setUp,#setDown,#setBottom,#saveOrder,#reset").attr("disabled", "true").addClass("icoNone");
         }
     );
     
	$("#childModules").change(
        function(e){
            $("#setTop,#setUp,#setDown,#setBottom,#deleteItem").removeAttr("disabled").removeClass("icoNone");
            
            if(this.selectedIndex == 0){
                $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
            }
            if(this.selectedIndex == this.length - 1){
                $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
            } 
        }
    );
        
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
    $('#deleteItem').click(function(){
        deleteItem();
    });     
    $('#saveOrder').click(function(){
        saveOrder();
    });       
    $("#setTop,#setUp,#setDown,#setBottom,#deleteItem,#saveOrder,#reset").attr("disabled","true").addClass("icoNone");
}


//设置到顶部
function setTop(){
    var topVal=$('option','#childModules').eq(0).val();       
    $('option:selected','#childModules').each(function(){
        $(this).insertBefore($('option[value=\''+topVal+'\']','#childModules'));
    });
    //设置按钮权限
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");           
    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
}

//设置到底部
function setBottom(){
    $('option:selected','#childModules').each(function(){
        $(this).appendTo('#childModules');
    });
    //设置按钮权限    
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");     
    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");   
}

//设置上移
function setUp(){
    //取得选中的顶
    var selectTopVal=$('option:selected','#childModules').eq(0).val();
    var preIndex=0;
    //搜索要插入的位置
    $('option','#childModules').each(function(index){
        if ($(this).val() == selectTopVal){
            preIndex=index-1;
            return false;
        }
    });
    var preVal=$('option','#childModules').eq(preIndex).val();
    //逐个插入
    $('option:selected','#childModules').each(function(){
        $(this).insertBefore($('option[value=\''+preVal+'\']','#childModules'));
    }); 
    //判断是否到顶,控制按钮
    if (preIndex == 0){
        $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setTop,#setUp").removeAttr("disabled").removeClass("icoNone");    
    }  
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");        
}

//设置下移
function setDown(){
    //取得选中底部
    var selectDownVal=$('option:selected','#childModules').eq($('option:selected','#childModules').length-1).val();
    var nextIndex=0;
    //搜索插入位置
    $('option','#childModules').each(function(index){
        if ($(this).val() == selectDownVal){
            nextIndex=index+1;
            return false;
        }
    });
    var nextVal=$('option','#childModules').eq(nextIndex).val();
    //逐个插入
    $('option:selected','#childModules').each(function(){
        $(this).insertAfter($('option[value=\''+nextVal+'\']','#childModules'));
        nextVal=$(this).val();
    });
    //判断是否到底,控制按钮
    if (nextIndex == ($('option','#childModules').length-1)){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
    }   
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");          
}

function deleteItem() {
	$('#childModules :selected').remove();
	setTimeout(function() { 
		$('#childModules option:last').attr('selected','selected'); 
	}, 1);
	if ($('option:selected','#childModules').size() == 0) {
		$("#deleteItem").attr("disabled","true").addClass("icoNone");
	}
}

function saveDeviceCheckItemCfg(){
	var id = $.trim($("#deviceClassID").val());
	var deviceType = deviceTypeSel.getValue();
	if (deviceType == "") {
		$.alert("请选择资产类别！");
		return;
	}
	var deviceClassID = deviceClassSel.getValue();
	if (deviceClassID == "") {
		$.alert("请选择设备类别！");
		return;
	}
	var itemNames = "";        
    $("#childModules > option").each(
        function(){
        	if (itemNames != "") {
        		itemNames += $.OaConstants.ONE_LEVEL_SPLIT_STRING;
        	}
            itemNames += this.text;
        }
    );
    var remark = $.trim($("#remark").val());
    var act;
    if(id == "") {
		act = "add";
	} else {
		act = "modify";
	}
    $.ajax({
        type:"POST",
		cache:false,
		url:"m/device_check_item?act="+act,//
		data:{
			deviceClassID : deviceClassID,
			remark : remark,
			itemNames : itemNames
		},
		success:saveSuccess,
        error:$.ermpAjaxError
    });  
}

//保存成功回调函数
function saveSuccess(xml){
    var message = $("message", xml);
	var messageCode = message.attr("code");
	if(messageCode == "1"){
		$.alert("保存成功", function(){
	    	 //刷新父列表
			mainFrame.getCurrentTab().doCallback();
	       //关闭
			mainFrame.getCurrentTab().close();
    	});
	}
	else{
		$.alert(message.text());
	}
}