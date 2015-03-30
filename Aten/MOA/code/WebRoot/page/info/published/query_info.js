var mainFrame = $.getMainFrame();

$(initPage);
var infoLayoutSel;
var stateSel;
//var typeSel;
function initPage(){
	
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY
    });
    
	
  	$("#refresh").click(function(){
  		$("#informationTab > tbody").html("");
  		loadList();
  	});
	
	//生成状态下拉框
	stateSel = $("#stateSel").ySelect({width:50});
	//生成类别下拉框
//	typeSel = $("#typeSel").ySelect({width:70,isdisabled:true});
	
	//搜索提交
	$("#searchInfo").click(function(){
		$("#informationTab > tbody").html("");
  		gotoPage(1);
  	});
  	//回车搜索
	$.EnterKeyPress($("#title"),$("#searchInfo"));
	
	initInfoLayouts();
}

function initInfoLayouts() {
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/info_param?act=getassignlayout&flag=1",
	    success : function(xml){
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	            var listDate = "";
	            $("info-layout",xml).each(
	                function(index){
	                	
	                    listDate += "<div>" + $("name",$(this)).text() + "**" + $("name",$(this)).text() + "</div>";
	                }
	            );
	            $("#infoLayoutSel").html(listDate);

				//初始化下拉
				//生成板块下拉框
				infoLayoutSel = $("#infoLayoutSel").ySelect({width: 80,onChange : onSelectType});
				infoLayoutSel.select(0);
				gotoPage(1);
				
	        }
	    },
	    error : $.ermpAjaxError
	});
}


function onSelectType(){
//	var val = infoLayoutSel.getDisplayValue();
//	if(val == "请选择..."){
//		typeSel = $("#typeSel").ySelect({width:70,isdisabled:true});
//		return;
//	}
//	typeSel = $("#typeSel").ySelect({
//		width:70, 
//		url:"m/info_param?act=classselect",
//		data:"name=" + val,
//		afterLoad:function(){
//			 typeSel.addOption("", "请选择...", 0);
//		}
//	});
}
//转向第几页
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
    $("#hidNumPage").val(pageNo);
    loadList();
};

//-------------------加载查询列表-------------------
function loadList(){
	
	 //分页
  	var pageno=$.trim($("#hidNumPage").val());
  	var pagecount=$.trim($("#hidPageCount").val());
  	
	var title =  $("#title").val();
	var infoLayout = infoLayoutSel.getValue();
	if (infoLayout == "") {
		return;
	}
	var state = stateSel.getValue();
//	var type = typeSel.getValue()==""?"":typeSel.getDisplayValue();
	$("#searchInfo").attr("disabled","true");
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/info_man",
		data : {
			act:"query",
			pageNo:pageno,
			pageSize:pagecount,
			property:state,
			title:title,
			infolayout:infoLayout
//			type:type
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	var tBodyHTML = "";
	            $("information",xml).each(
	                    function(index){
	                        var informationConfig = $(this);
	                        var flag = $("info-property",informationConfig).text();
	                        tBodyHTML += createTR(
	                        	flag,
	                        	(index + 1),
	                        	$(informationConfig).attr("id"),
	                        	$("subject",informationConfig).text(),
	                        	$("public-date",informationConfig).text(),
	                        	$("group-name",informationConfig).text(),
	                        	$("drafts-man",informationConfig).text(),
	               //         	$("info-layout",informationConfig).text(),
	                        	$("info-class",informationConfig).text(),
	                        	$("info-property-name",informationConfig).text()
	                        );
	                    }
	             );
	            $("#informationTab > tbody").html(tBodyHTML).find("td:empty").html("&nbsp;");
				enWrap();
                //------------------翻页数据--------------------------
	            $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
            }
            else{
               $.alert($("message",xml).text());
            };
            $("#searchInfo").removeAttr("disabled");
        },
        error : $.ermpAjaxError
    });
	
}

//创建TR
function createTR(property,num,id,subject,date,dept,man,infoClass,propertyName){
	var html = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		 + "<td>" + num + "</td>"
		 + "<td>" + subject + "</td>"
//		 + "<td>" + layout + "</td>"
//		 + "<td>" + infoClass + "</td>"
		 + "<td>" + date + "</td>"
		 + "<td>" + dept + "</td>"
		 + "<td>" + man + "</td>"
		 + "<td>" + propertyName + "</td>";
		var op = "";
		if(property == 0){
		 	//置顶
			op += ($.hasRight($.OaConstants.TO_TOP)?("<a href=\"javascript:void(0);\" onclick=\"setProperty('" + id + "','true','totop');\" class=\"opLink\">撤销</a>&nbsp;|&nbsp;") : "");
		} else {
         	op +=  ($.hasRight($.OaConstants.TO_TOP)?("<a href=\"javascript:void(0);\" onclick=\"setProperty('" + id + "','false','totop');\" class=\"opLink\">置顶</a>&nbsp;|&nbsp;") : "");
		}  
		if(property == 2){
		 	//屏蔽
			op += ($.hasRight($.OaConstants.SHIELD)?("<a href=\"javascript:void(0);\" onclick=\"setProperty('" + id + "','true','shield');\"  class=\"opLink\">恢复</a>&nbsp;|&nbsp;") : "");
		} else {
         	op +=  ($.hasRight($.OaConstants.SHIELD)?("<a href=\"javascript:void(0);\" onclick=\"setProperty('" + id + "','false','shield');\"  class=\"opLink\">屏蔽</a>&nbsp;|&nbsp;") : "");
		}    
		op += ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"setProperty('" + id + "','','delete');\"  class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "");
		op += ($.hasRight($.SysConstants.VIEW)?("<a href=\"javascript:void(0);\" onclick=\"view('" + id + "');\"  class=\"opLink\">详情</a>&nbsp;|&nbsp;") : "");
	html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"))+"</td></tr>";
    return html;
	
}
//查看详情
function view(id){
	mainFrame.addTab({
		id:"oa_InfoMan_view_"+id,
		title:"查看信息",
		url:BASE_PATH + "/m/info_man?act=view&id=" + id
	});
}
//屏蔽
function setProperty(id,flag,act){
	var info = "是否"
	if (act =='totop') {
		info += (flag == 'true') ? "撤销" : "置顶";
	} else if (act =='shield') {
		info += (flag == 'true') ? "恢复" : "屏蔽";
	} else {
		info += "删除";
	}
	info += "该信息？";
	$.confirm(info, function(r) {
		if (r) {
			$.ajax({
		        type : "POST",
				cache: false,
				url  : "m/info_man",
				data : {
					act:act,
					id:id,
					flag:flag
				},
		        success : function(xml){
		            //解析XML中的返回代码
		            var message = $("message",xml);
		            if(message.attr("code") == "1"){
//		            	$.alert("操作成功");
		            	loadList();
		            }
		            else{
		               $.alert($("message",xml).text());
		            };
		        },
		        error : $.ermpAjaxError
		    });
		}
	});
}


function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}