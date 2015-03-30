$(initInfoDialog);
var docWord;
var yearPrefix;
var currentYear;
var yearPostfix;
var orderPrefix;
var orderNumber;
var orderPostfix;
var redStr;
var docTmpId;
var redHeaderArgs = new Object();
redHeaderArgs = window.dialogArguments; 
function initInfoDialog(){
	
	$("#docNum").change(function(){	
		setDocNumNull();
		initPage($(this).val());		
	});
	
	$("#year").change(function(){
		currentYear = $.trim($(this).val());
		redStr =docWord+yearPrefix+currentYear+yearPostfix+orderPrefix+orderNumber+ orderPostfix;
		redHeaderArgs.redHeader =redStr;
		redHeaderArgs.year =currentYear;
		$("#docNumPre").html(redStr);	  
	});
	
	$("#num").change(function() {
		orderNumber = $.trim($(this).val());
		redStr =docWord+yearPrefix+currentYear+yearPostfix+orderPrefix+orderNumber+ orderPostfix;
		redHeaderArgs.redHeader =redStr;
		redHeaderArgs.docOrder =orderNumber;
		$("#docNumPre").html(redStr);	  
	});
	
	$("#submit").click(function(){
		submitRedHead();
  	});
  	$("#cancel").click(function(){
		window.close();
  	});
};

function validParam(obj, name) {
	var value = $.trim(obj.val());
	var result = $.validChar(value);
	if (result) {
		$.alert(name + "不能包含非法字符：" + result);
		obj.select();
		return false;
	}
	if (value == "") {
		$.alert(name + "不能为空");
		obj.focus();
		return false;
	} else if (isNaN(value) || value < 0) {
		$.alert(name + "只能为正整数");
		obj.select();
		return false;
	}
	return true;
}
function setDocNumNull(){
	 docWord="";
	 yearPrefix="";
	 currentYear="";
	 yearPostfix="";
	 orderPrefix="";
	 orderNumber="";
	 orderPostfix="";
	 redStr="";
	 redHeaderArgs.redHeaderUrl="";
}

function initPage(docId){
	if (docId == "") {
		$("#year").val("").attr("disabled", true);
		$("#num").val("").attr("disabled", true);
	} else {
		$("#year").removeAttr("disabled");
		$("#num").removeAttr("disabled");
	}
	$("#docNumPre").html("");
	$.ajax({
		  type : "POST",
		   cache: false,
		   async : true,
		   url  : "m/doc_no",
		   data : {
		   		act:"finddocinfo",
		   		id:docId
		   },             
		   success : function (xml){
		   		var message = $("message",xml);
				var content = $("content",xml);
				if (message.attr("code") == "1") {	
			    	$(xml).find("doc-no").each(function(index){
			    		var curELe = $(this);
	                    docTmpId = curELe.attr("id");
						docWord=$("docWord",curELe).text();
						yearPrefix=$("yearPrefix",curELe).text();
						currentYear=$("currentYear",curELe).text();
						yearPostfix=$("yearPostfix",curELe).text();
						orderPrefix=$("orderPrefix",curELe).text();
						orderNumber=Number($("orderNumber",curELe).text());
						orderPostfix=$("orderPostfix",curELe).text();
						var url = $("docTmpUrl",curELe).text();
						redStr =docWord+yearPrefix+currentYear+yearPostfix+orderPrefix+orderNumber+ orderPostfix;
						 $("#docNumPre").html(redStr);	
						 $("#year").val(currentYear);
						 $("#num").val(orderNumber);
						redHeaderArgs.redHeaderUrl =url;
					    redHeaderArgs.redHeader =redStr;
					    redHeaderArgs.docTmpId=docTmpId;
					    redHeaderArgs.docOrder =orderNumber;
					    redHeaderArgs.year =currentYear;
			    	});
			    	enWrap();
			   };
		   },
		   error : function(){
		        $.ermpAjaxError();
		  }
	})
}


function submitRedHead(){
	if(!redStr){
		$.alert("请选择公文字号");
		return;
	}
	if (!validParam($("#num"), "流水号")) {
		return;
	}
	if (!validParam($("#year"), "年份")) {
		return;
	}
	$.ajax({
       	type : "POST",
       	cache: false,
       	async: false,
       	url  : "m/dis_start",
       	data : {act:"chackdocno",
       			headerStr:redHeaderArgs.redHeader},
		success : function(xml){
           	var messageCode = $("message",xml).attr("code");
           	var msg = $("message",xml).text();
           	if(messageCode == "1"){
				window.returnValue = "submit";
				window.close();
               return;
           	} else {
           		$.alert("该流水号已经存在");
			}
		}
	});
	
}

function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}