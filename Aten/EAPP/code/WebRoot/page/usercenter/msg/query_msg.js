var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
//初始化页面
$(initSysMsgList);

function initSysMsgList(){
    
    //加载列表数据
    $("#searchMsg").click(function(){
        	trunPageObj.gotoPage(1);//搜索第一页
	});
	
	 /*刷新*/
    $("#refresh").click(function(){
            $("#data_list").html("");
	    	queryList();
  	});
  	
  	/*批量删除*/
	$("#delAllMsg").click(function(e){       
		    if ($("input:checkbox:checked","tbody").length==0){
		       $.alert('请选择要删除的消息!');	       
		    }
		    else{
		    	$.confirm("是否删除所选的所有消息?", function(r){
					if (r) {
						var msgIdList="";
						$("input:checkbox:checked","tbody").each(function(e){
							msgIdList+="&msgIDs="+$(this).attr("name");
						});
						$.ajax({
							type : "POST",
							cache: false,
							url  : "l/frame/delmsg",
							dataType : "json",
							data : msgIdList,
							success : function(data) {
								if ($.checkErrorMsg(data) ) {
//									$.alert("删除用户帐号成功!");
								    queryList();
								}
							}
						});
					}
				});
			}
	});
    
    $.EnterKeyPress($("#msgSender"),$("#searchMsg"));
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
};

//全选 
function checkAll(){
	$("#data_list > tr").each(function(){
		$("input").attr("checked",$("#checkbox")[0].checked);
	});
    if ($("input:checkbox:checked","tbody").length==0){
        $("#delAllMsg").attr("disabled","true").addClass("icoNone");
    } else{
       $("#delAllMsg").removeAttr("disabled").removeClass("icoNone");
    }	
}

//选择单个
function checkSingle(){
    if ($("input:checkbox:checked","#data_list").length==0){
        $("#checkbox")[0].checked=false;
        $("#delAllMsg").attr("disabled","true").addClass("icoNone");
    } else{
       $("#delAllMsg").removeAttr("disabled").removeClass("icoNone");
    }
    //取消事件冒泡
	event.cancelBubble=true;
}

function queryList(){
	//设置查询按钮
	$("#searchMsg").attr("disabled","true");
	//分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
    $.ajax({
        type : "POST",
		cache: false, 
		url  : "l/frame/mymsgs",
        async : true,
        data: {
			pageNo: pageNo,
			pageSize : pageCount,
			msgSender : $.trim($("#msgSender").val()),
			beginTime:$("#begintime").val(),
			endTime:$("#endtime").val()
		},
        dataType : "json",
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.sysMsgPage && data.sysMsgPage.dataList){
					var dataList = data.sysMsgPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\" "
	                    		+ (dataList[i].viewFlag ? ">" : "style=\"font-weight:bold;cursor:pointer;\" onclick=\"viewMsg(this,'" + dataList[i].msgID + "')\">");
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td> <input id=\"checkbox"+(i + 1)+"\" type=\"checkbox\" name=\""+dataList[i].msgID+"\" onclick=\"checkSingle();\"/></td>";
						fileList += "<td>" + dataList[i].msgSender + "</td>";
						fileList += "<td>" + dataList[i].sendTime + "</td>";
						fileList += "<td>" + dataList[i].msgContent + "</td>";
	                    
                        fileList += "</tr>";
					});
				}
				
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.sysMsgPage);
			}
			$("#searchMsg").removeAttr("disabled");
        }
    });
}

function viewMsg(obj, msgID) {
	$.ajax({
		type : "POST",
		cache: false, 
		url  : "l/frame/viewmsg",
		dataType : "json",
		data : {msgID : msgID},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				$(obj).removeAttr("style").removeAttr("onclick");
//                queryList();
			}
        }
	});
}
