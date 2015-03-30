//设备属性配置
var mainFrame = $.getMainFrame();
$(initDeviceConfig);

function initDeviceConfig() {
		  //添加权限约束
   $.handleRights({
   		"config_add" : $.SysConstants.ADD
	});
	//新增
	$("#config_add").click(function(){
	    deviceConfigAdd();
	});
	//刷新
	$("#DeviceCon_flash").click(function(){
	    gotoPage(1);
	});
	gotoPage(1);
}

//新增
function deviceConfigAdd(){
	mainFrame.addTab({
		id:"oa_device_class_add"+Math.floor(Math.random() * 1000000),
		title:"设备类别新增", 
		url:BASE_PATH + "page/device/paramconf/class/edit_class.jsp",
		callback:queryList
	});
}
//修改
function deviceConfigUpdate(id){
	mainFrame.addTab({
		id:"oa_device_class_modify"+id,
		title:"设备类别修改", 
		url:BASE_PATH + "m/device_class?act=initmodify&id="+id,
		callback:queryList
	});
	
}

//详情
function openView(id){
		mainFrame.addTab({
			id:"oa_device_class_view"+id,
			title:"设备类别详情",
			url:BASE_PATH +"m/device_class?act=view&id="+id
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
	$("#configList > tbody").empty();
	 //分页
  	var pageno=$.trim($("#hidNumPage").val());
  	var pagecount=$.trim($("#hidPageCount").val());
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/device_class",
		data : {act:"query",
				pageNo:pageno,
				pageSize:pagecount
		},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var html = "";
					$("device-class",xml).each(
	                    function(index){
	                        var deviceclassLayout = $(this);
	                        var id=deviceclassLayout.attr("id");
								html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
								 html += "<td>" + $("type-name",deviceclassLayout).text()+ "</td>" 
							    html += "<td>" + $("class-name",deviceclassLayout).text()+ "</td>" 
							    html += "<td>" + $("class-item-name",deviceclassLayout).text() + "</td>" 
							    html += "<td>" + $("class-remark",deviceclassLayout).text() + "</td>" 
							         //操作
								var op = ($.hasRight($.SysConstants.VIEW)?("<a href=\"javascript:void(0);\" onclick=\"openView('" + id + "');\"  class=\"opLink\">查看</a>&nbsp;|&nbsp;") : "") 
										   +($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"deviceConfigUpdate('" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") ;
								op+=($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteDeviceClass('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "");
								
							    html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));   
							    html += "</td></tr>";
								                });
	                $("#configList > tbody").html(html);
	                //------------------翻页数据--------------------------
		            $(".pageNext").html($.createTurnPage(xml));
		            $.EnterKeyPress($("#numPage"),$("#numPage").next());
					edit=false;
				}else{
               		$.alert($("message",xml).text());
           		}
	       },
	       error : $.ermpAjaxError
	});	
}

//删除
function deleteDeviceClass(id){
	if(""==id || null==id){
		return;
	}
	
	$.confirm("确认要删除该设备类别吗？",function(r){
		if(r) {
			$.ajax({
		        type : "POST",
				cache: false,
				url  : "m/device_class",  //
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
