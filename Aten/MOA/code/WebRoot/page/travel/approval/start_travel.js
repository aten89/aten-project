/**********************
出差审批--启动列表
*********************/ 
var mainFrame = $.getMainFrame();
$(initApprovalStart);
function initApprovalStart(){
	//刷新
  	$("#refresh").click(function(){
  	    $("#infoDraftList").empty();
  		queryList();
  	});
  	
  	//新增
  	$("#tripAdd").click(function(){
  		mainFrame.addTab({
			id:"oa_bustrip_draft",
			title:"填写出差申请单",
			url:BASE_PATH + "m/trip_start?act=initadd",
			callback:queryList
		});
  	});
  	
  	queryList();
};

function queryList() {
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/trip_start?act=query",
	    success : function(xml){
	        var listDate = "";
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	          $(xml).find('busTripApply').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    var op = ( $.hasRight($.SysConstants.MODIFY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"modifyDraft('" + id + "')\">修改</a>&nbsp;|&nbsp;" : "")
                    	   + ( $.hasRight($.SysConstants.DELETE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"delDraft('" + id + "')\">删除</a>&nbsp;|&nbsp;" : "")
                   	listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
                   			+ "<td>" + $("trip-sche",curELe).text() + "</td>"
							+ "<td>" + $("days",curELe).text() + "</td>"
							+ "<td>" + $("apply-date",curELe).text() + "</td>"
							+ "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"))
							+ "</td></tr>";
				});
				
				$("#infoDraftList").html(listDate);
				enWrap();
	        }
	    },
	    error : $.ermpAjaxError
	});
};



function modifyDraft(id){
	mainFrame.addTab({
		id:"oa_bustrip_modify"+id,
		title:"修改出差申请单",
		url:BASE_PATH + "m/trip_start?act=initmodify&id=" + id,
		callback:queryList
	});
};


function delDraft(id){
	$.confirm("确认要删除吗?", function(r){
		if (r) {
			$.ajax({
			    type : "POST",
			    cache: false,
			    async : true,
			    url  : "m/trip_start",
			    data :{act:"delete",id:id},
			    success : function(xml){
			        var message = $("message",xml);
			        if(message.attr("code") == "1"){
			        	$.alert($("message",xml).text());
			        	queryList();
			        }else{
			        	$.alert($("message",xml).text());
			        }
			    },
			    error : $.ermpAjaxError
			});
		}
	});
};

function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
};
