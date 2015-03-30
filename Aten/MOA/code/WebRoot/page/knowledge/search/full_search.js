var mainFrame = $.getMainFrame();
var firstTypeSel; // 一级分类下拉框
var secondTypeSel; // 二级分类下拉框
var statusSel;
var upstatusSel;
$(initPage);

function initPage(){
	//搜索提交
	$("#searchKb").click(function(){
  		gotoPage(1);
  	});
  	//回车搜索
	$.EnterKeyPress($("#keyword"),$("#searchKb"));
	$.EnterKeyPress($("#title"),$("#upSearch"));
	$.EnterKeyPress($("#beginPublishDate"),$("#upSearch"));
	$.EnterKeyPress($("#endPublishDate"),$("#upSearch"));
  	
  	if ($("#keyword").val() != "" || $("#prodName").val() != "") {
	  	gotoPage(1);
	}
	$("#advancedSo").click(function(){
		$("#soCon").show();
		$("#searchKnowledge").hide();
		$("#loadflag").val("1");
		$("#searchList").empty();
		$(".pageNext").empty();
		upstatusSel.select(0);
		firstTypeSel.select(0);
		secondTypeSel.select(0);
		$("#title").val("");
		$("#beginPublishDate").val("");
		$("#endPublishDate").val("");		
		$("#publisher").val("");
		$("#publisherName").val("");
	});
	$("#advancedUpSo").click(function(){
		$("#soCon").hide();
		$("#searchKnowledge").show();
		$("#loadflag").val("0");
		$("#searchList").empty();
		$(".pageNext").empty();
		statusSel.select(0);
		$("#keyword").val("");
	});
	//打开用户帐号
   $("#openUserSelect").click(function(e){
		var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
		dialog.setCallbackFun(function(user){
			if (user != null) {
				$("#publisher").val(user.id);
				$("#publisherName").val(user.name);
			}
		});
		dialog.openDialog("single");
	});
	//搜索提交
	$("#upSearch").click(function(){	
  		gotoPage(1);
  	});
	
	//关键字
	initKeyword();
	
	statusSel = $("#statusDiv").ySelect({width: 60});
	upstatusSel = $("#upstatusDiv").ySelect({width: 60});
	// 一级分类
	firstTypeSel = $("#firstTypeSel").ySelect({
		width: 80,
		url  : "m/knowledge", 
		data : {act:"firstType"},
		afterLoad : function() {
			firstTypeSel.addOption("", "所有...", 0);
			firstTypeSel.select(0);
		}
	});
	
	// 二级分类
	secondTypeSel = $("#secondTypeSel").ySelect({
		width: 80,
		url  : "m/knowledge", 
		data : {act:"secondType"},
		afterLoad : function() {
			secondTypeSel.addOption("", "所有...", 0);
			secondTypeSel.select(0);
		}
	});
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
    $("#hidNumPage").val(parseInt(pageNo));
    var flag=$("#loadflag").val();
    if(flag==0){
		loadList();
    }else{
		upLoadList();
    }
}

//-------------------加载查询列表-------------------
function upLoadList(){
	$("#searchList").empty();
	$(".pageNext").empty();
	
	var status = upstatusSel.getValue();
	var firstType = firstTypeSel.getValue();// 一级分类
	var secondType = secondTypeSel.getValue();// 二级分类
	var subject = $.trim($("#title").val());
	var result = $.validChar(subject);
	if (result) {
		$.alert("标题不能包含非法字符：" + result);
		$("#subject").focus();
		return ;
	}
	// 发布者
	var publisher = $("#publisher").val();
	var beginPublishDate = $.trim($("#beginPublishDate").val());
	var endPublishDate = $.trim($("#endPublishDate").val());
	if(	$.compareDate(beginPublishDate,endPublishDate)){
		$.alert("发布开始时间不能大于结束时间！");
		return;
	};		
	
	//分页参数
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());

	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/kb_index",
		data : {
			act:"searchByArgs",
			pageno:pageno,
			pagecount:pagecount,
			status:status,
			firstType:firstType,
			secondType:secondType,
			subject:subject,
			publisher:publisher,
			beginPublishDate:beginPublishDate,
			endPublishDate:endPublishDate
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	var tBodyHTML="";
	            $("knowledge",xml).each(function(index) {
	            	tBodyHTML += "<div class='klMain'><div class='klTit'><a href=\"javascript:view('" + $(this).attr("id") + "')\">" 
	         			+ $("subject",$(this)).text() + "</a></div>"

	   					+ "<div class='klCont' style='color:#999;padding-bottom:3px'>" + $("publisher",$(this)).text() + "</div>"

	   					+ "<div class='klCont'>" + $("apply-date",$(this)).text() + "</div></div>";
        		});
				if(tBodyHTML==""){
					tBodyHTML="<div class='nothing'>"
							+"<h1>找不到和您的查询相符的内容或信息。</h1>"
						
							+"</div>";
				}
				
	            $("#searchList").html(tBodyHTML);
                //------------------翻页数据--------------------------
	            $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
        	}
        },
        error : $.ermpAjaxError
    });
}

//-------------------加载查询列表-------------------
function loadList(){
	$("#searchList").empty();
	$(".pageNext").empty();
	
  	var keyword = $("#keyword").val();
	if (keyword == "") {
		return;
	}
	var status = statusSel.getValue();
	//分页
  	var pageno = $("#hidNumPage").val();
  	var pagecount = $("#hidPageCount").val();
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/kb_index",
		data : {
			act:"search",
			pageno:pageno,
			pagecount:pagecount,
			keyword:keyword,
			status:status
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	var tBodyHTML = "";
	            $("knowledge",xml).each(function(index) {
	         		tBodyHTML += "<div class='klMain'><div class='klTit'><a href=\"javascript:view('" + $(this).attr("id") + "')\">" 
	         			+ $("match-subject",$(this)).text() + "</a></div>"

	   					+ "<div class='klCont' style='color:#999;padding-bottom:3px'>" + $("match-class-path-name",$(this)).text() + "</div>"

	   					+ "<div class='klCont'>" + $("match-text",$(this)).text() + "</div></div>";
	   					
        		});
				if(tBodyHTML==""){
					tBodyHTML="<div class='nothing'>"
							+"<h1>找不到和您的查询<b> \""+keyword+"\" </b>相符的内容或信息。</h1>"
							+"建议："
							+"<ul><li>请检查输入字词有无错误。</li>"
							+"<li>请换用另外的查询字词。</li>"
							+"<li>请改用较常见的字词。</li></ul>"
							+"</div>";
				}
	            $("#searchList").html(tBodyHTML);
                //------------------翻页数据--------------------------
	            $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
        	}
        },
        error : $.ermpAjaxError
    });
}

//查看详情
function view(id){
	mainFrame.addTab({
		id:"oa_Knowledge_view_"+id,
		title:"查看知识库",
		url:BASE_PATH + "/m/knowledge?act=knowledgeinfo&id=" + id,
		callback:refreshList
	});
	
}

function refreshList(){
	var flag=$("#loadflag").val();
	if(flag==0){
		loadList();
    }else{
		upLoadList();
    }
}

//关键字
function initKeyword(){
	$.getJSON("m/kb_label?act=autocomplete",function(data){
		$("#keyword").autocomplete(data, {
			minChars: 0,
			width: 150,
			matchContains: true,
			autoFill: false,
			multiple: true,
			multipleSeparator: " ",
			max:10
		});
	});	
}

