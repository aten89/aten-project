//设备类别列表
var mainFrame = $.getMainFrame();
$(initNumberRule);

function initNumberRule() {
	  //添加权限约束
   $.handleRights({
   		"add_deviceflow" : $.SysConstants.ADD
	});
	//新增
	$("#add_deviceflow").click(function(){
	    deviceClassConfigAdd();
	});
	//刷新
	$("#DeviceCon_flash").click(function(){
	    gotoPage(1);
	});
	gotoPage(1)
}

//新增
function deviceClassConfigAdd(){
	mainFrame.addTab({
		id:"oa_device_area_add"+Math.floor(Math.random() * 1000000),
		title:"区域设备新增", 
		url:BASE_PATH + "page/device/paramconf/area/edit_area.jsp",
		callback:queryList
	});
	

}

//转向修改页面
function deviceConfigUpdate(id){
	mainFrame.addTab({
		id:"oa_device_area_modify"+id,
		title:"区域设备修改", 
		url:BASE_PATH + "m/device_areacfg?act=initupdate&id="+id,
		callback:queryList
	});
	

}

//详情
function deviceClassConfigView(id){
		mainFrame.addTab({
			id:"oa_deviceClassConfigView"+id,
			title:"区域设备配置详情",
			url:BASE_PATH +"m/device_areacfg?act=view&id="+id
		});
}
/**
 * 转到第几页
 * @param {Number} pageNo 页码
 * @param {Number} totalPage 总页数
 */
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
    queryList();
}

//查询
function queryList(){
	$("#deviceConfig > tbody").empty();
	//提交到后台
	 //分页
  	var pageno=$.trim($("#hidNumPage").val());
  	var pagecount=$.trim($("#hidPageCount").val());
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/device_areacfg",
		data : {act:"query",
				pageNo:pageno,
				pageSize:pagecount
		},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var html = "";
					$("area-config",xml).each(
	                    function(index){
	                        var deviceAreaConfig = $(this);
	                        var id=deviceAreaConfig.attr("id");
	                        var mainDevFlag ="否";
	                        if($("config-mainDevFlag",deviceAreaConfig).text()=="true"){
	                        	mainDevFlag ="是";
	                        }
	                        var manyTimesFlag  ="否";
	                         if($("config-manyTimesFlag",deviceAreaConfig).text()=="true"){
	                        	manyTimesFlag ="是";
	                        }
								html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
								html += "<td>" + $("area-name",deviceAreaConfig).text()+ "</td>" 
								html += "<td>" + $("device-typename",deviceAreaConfig).text()+ "</td>" 
							    html += "<td>" + $("class-name",deviceAreaConfig).text()+ "</td>" 
							    html += "<td>" + $("config-viewcode",deviceAreaConfig).text()+ "</td>" 
//							    html += "<td>设备领用流程："+ $("config-useApply",deviceAreaConfig).text() +"<br>"
//          										+"设备调拨流程："+ $("config-allocate",deviceAreaConfig).text() +"<br>"
//          										+"设备报废流程：" + $("config-discard",deviceAreaConfig).text() + "<br>"
//          										+"离职处理流程："+ $("config-dimission",deviceAreaConfig).text() +" <br>";
//          						html += "<td>" + $("config-devPuerpose",deviceAreaConfig).text() + "</td>" 
							     html += "<td>" +mainDevFlag+ "</td>" 
							     
							    
							         //操作
								var op = ($.hasRight($.SysConstants.VIEW)?("<a href=\"javascript:void(0);\" onclick=\"deviceClassConfigView('" + id + "');\"  class=\"opLink\">查看</a>&nbsp;|&nbsp;") : "") 
										   +($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"deviceConfigUpdate('" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") 
							               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteDeviceConfig('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "")
							    html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));   
							    html += "</td></tr>";
								                });
	                $("#deviceConfig > tbody").html(html);
	                //------------------翻页数据--------------------------
		            $(".pageNext").html($.createTurnPage(xml));
		            $.EnterKeyPress($("#numPage"),$("#numPage").next());
				}else{
               		$.alert($("message",xml).text());
           		}
	       },
	       error : $.ermpAjaxError
	});	
}

//删除
function deleteDeviceConfig(id){
	if(""==id || null==id){
		return;
	}
	
	$.confirm("确认要删除该区域设备配置吗？",function(r){
		if (r) {
			$.ajax({
		        type : "POST",
				cache: false,
				url  : "m/device_areacfg",  //
				data : {
					act: "delete",
					id:id
				},
		        success : function(xml){
		            var message = $("message",xml);
		            if(message.attr("code") == "1"){
//		            	$.alert("操作成功");
						queryList();
		            } 
		            else{
		            	$.alert($("message",xml).text());
		            }
		        },
		        error : $.ermpAjaxError
		    });  
		}
	});
}


