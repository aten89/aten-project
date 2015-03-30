var mainFrame = $.getMainFrame();

$(initVacationApproval);

function initVacationApproval() {
	$.handleRights({
        "addEntry" : "entry",
        "addResign" : "resign",
        "addMyResign" : "myresign",
        "refresh" : $.SysConstants.QUERY
    });
    
    if ($("#applyTypeSel li").size() == 0) {
    	$("#toAdd").remove();
    }
	//刷新
  	$("#refresh").click(function(){
  	    $("#infoDraftList").empty();
  		queryList();
  	});
  	
  	var addImg = $("#toAdd").offset();
	if (addImg) {
		$("#newType").css({"top":(addImg.top - 4 ) + "px"});
		$("#newType").css({"left":(addImg.left - 5) + "px"});
		
	    $("#toAdd,#toAdd1").click(function(){
	    	if ($('#newType').is(':visible')) {
	    		$('#newType').slideUp();
	    	} else {
	    		$('#newType').slideDown();
	    	}
	    });
	}
	$("#applyTypeSel li").hover(function(){
		$(this).addClass("current");
	},function(){
		$(this).removeClass("current");
	}).click(function(){
		$("#newType").hide();
		var applyType = $(this).attr("val");
		mainFrame.addTab({
			id:"oa_staffflow_draft"+Math.floor(Math.random() * 1000000),
			title:"新增" + (applyType == "1" ? "入职审批" : "离职审批"),
			url:BASE_PATH + "m/staff_start?act=initadd&applyType=" + applyType,
			callback:queryList
		});
	});
  	
//  	//新增
//  	$("#toAdd").click(function(){
//  		mainFrame.addTab({
//			id:"oa_holiday_draft",
//			title:"填写请假单",
//			url:BASE_PATH + "m/holi_start?act=initadd",
//			callback:queryList
//		});
//  	});
  	
  	queryList();
};


function queryList() {
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/staff_start?act=query",
	    success : function(xml){
	        var listDate = "";
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	          $(xml).find('staff-flow').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    var applyType = $("apply-type",curELe).text();
                    var op = ( $.hasRight($.SysConstants.QUERY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"modifyDraft('" + id + "','" + applyType + "')\">修改</a>&nbsp;|&nbsp;" : "")
                    	   + ( $.hasRight($.SysConstants.QUERY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"delDraft('" + id + "')\">删除</a>&nbsp;|&nbsp;" : "");
                   	listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + (applyType == "1" ? "入职申请":"离职申请") + "</td>"
							+ "<td>" + (applyType == "2" ? $("resign-date",curELe).text() : $("entry-date",curELe).text()) + "</td>"
							+ "<td>" + $("company-area-name",curELe).text() + "</td>"
							+ "<td>" + $("dept",curELe).text() + "</td>"
							+ "<td>" + $("user-name",curELe).text() + "</td>"
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



function modifyDraft(id, applyType){
	mainFrame.addTab({
		id:"oa_holiday_modify"+id,
		title:"修改" + (applyType == "1" ? "入职审批" : "离职审批"),
		url:BASE_PATH + "m/staff_start?act=initmodify&id=" + id,
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
			    url  : "m/staff_start",
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
