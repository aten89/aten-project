//设备检查配置
var mainFrame = $.getMainFrame();
$(initDeviceExamine);

function initDeviceExamine() {
	$("#addDeviceCheckItemBtn").click(function(){
		addDeviceCheckItem();
	});
	
	$("#refreshBtn").click(function(){
		queryPage();
	});
	
	queryPage();
}

//新增
function addDeviceCheckItem(){
	mainFrame.addTab({
		id:"oa_device_item_add"+Math.floor(Math.random() * 1000000),
		title:"验收检查项新增", 
		url:BASE_PATH + "m/device_check_item?act=initadd",
		callback:queryPage
	});
	
//	var sURL = BASE_PATH + "m/device_check_item?act=initadd";
//	var sFeature = "dialogHeight:518px;dialogWidth:478px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:573px;dialogWidth:486px;status:no;scroll:auto;help:no";
//	}
//	var result = $.showModalDialog(sURL, "", sFeature);
//	if (result) {
//		queryPage();
//	}
}

//转到修改页面
function initModify(id) {
	mainFrame.addTab({
		id:"oa_device_item_add"+id,
		title:"验收检查项修改", 
		url:BASE_PATH + "m/device_check_item?act=initmodify&id=" + id,
		callback:queryPage
	});
//	var sURL = BASE_PATH + "m/device_check_item?act=initmodify&id=" + id;
//	var sFeature = "dialogHeight:518px;dialogWidth:478px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:573px;dialogWidth:486px;status:no;scroll:auto;help:no";
//	}
//	var result = $.showModalDialog(sURL, "", sFeature);
//	if (result) {
//		queryPage();
//	}
}

function gotoPage(pageNo, totalPage){
    if(isNaN(pageNo)){
        $.alert("页索引只能为数字！");
        return;
    } else if(pageNo < 1){
        pageNo = 1;
    }
    if (totalPage && !isNaN(totalPage) && pageNo > totalPage) {
    	pageNo = totalPage;
    }
    $("#hidNumPage").val(parseInt(pageNo));
    queryPage();
}

function queryPage() {	
	//设置查询按钮
 	$("#deviceCheckItemList").empty();
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : BASE_PATH + "m/device_check_item",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount,             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	
		    	$(xml).find('document').each(function(index){
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
                    		+ "<td>" + $("device-type-name",curELe).text() + "</td>"
			  				+ "<td>" + $("device-class-name",curELe).text() + "</td>"
							+ "<td>" + $("device-check-item-str",curELe).text() + "</td>"
							+ "<td>" + $("device-check-item-remark",curELe).text() + "</td>";
							
							var op = ($.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a>&nbsp;|&nbsp;" : "")
							+ ($.hasRight($.SysConstants.MODIFY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initModify('" + id + "')\">修改</a>&nbsp;|&nbsp;" : "")
							+ ($.hasRight($.SysConstants.DELETE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"deleteDeviceCheckItemCfg('" + id + "')\">删除</a>&nbsp;|&nbsp;" : "");
							listData += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));   
							listData += "</td></tr>";

				});
				$("#deviceCheckItemList").html(listData);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $(".pageNext").html("");
		    };
	   		$("#refreshBtn").removeAttr("disabled");
	   },
	   error : function(){
	        $("#refreshBtn").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
	});
}

//转到详情查看页面
function initView(id) {
	mainFrame.addTab({
		id:"oa_devicecheckitem_view_" + id,
		title:"检查项配置详情", 
		url:BASE_PATH + "m/device_check_item?act=view&id=" + id,
		callback:queryPage
	});
}

function deleteDeviceCheckItemCfg(id) {
	$.confirm("是否确认删除？",function(r){
		if(r) {
			$.ajax({
	        type : "POST",
			cache: false,
			url  : BASE_PATH + "m/device_check_item",  //
			data : {
				act : "delete",
				id : id
			},
	        success : function(xml){
	        	var message = $("message", xml);
				var messageCode = message.attr("code");
				if(messageCode == "1"){
//					$.alert("操作成功");
					queryPage();
				}
				else{
					$.alert(message.text());
				}
	        },
	        error : $.ermpAjaxError
	    });  
		}
	});
}

