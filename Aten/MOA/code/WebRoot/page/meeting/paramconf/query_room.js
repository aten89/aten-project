var mainFrame=$.getMainFrame();
//当前是否为编辑状态
var edit=false;
var re = /^[0-9]\d*$/;
var areaSel;//所属地区
$(initParameterSet);
function initParameterSet(){
	//添加权限约束
	$.handleRights({
	   "btnSort" : $.SysConstants.ORDER,
	   "addMeetingRoom" : $.SysConstants.ADD
	});
	$("#addMeetingRoom").click(function(){
		addMeetingRoom();
	});
	$("#reflash").click(function(){
	 	$("#roomSet > tbody").html("");
		queryList();
		$("#addMeetingRoom").removeAttr("disabled","true").removeClass("icoNone");
		edit=false;
	});
	//查询
	queryList();
	
	/*排序*/
	$("#btnSort").click(
		function(){
			mainFrame.addTab({
				id:"oa_MeetingRoom_sort",
				title:"会议室排序",
				url:BASE_PATH +"/m/meet_param?act=initsort",
				callback:queryList
			});
		}
	);
	
}

function cancelInsert(self) {
	$(self).parent().parent().remove();
	$("#addMeetingRoom").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}

function addMeetingRoom(){
	$("#addMeetingRoom").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	
	var roomNum=Math.floor(Math.random() * 1000000);
	var roomList="<tr id=\"meetingRoom"+ roomNum + "\">"
					  +"<td><div id=\"areaSel\" name=\"areaSel\"></div></td>"
	                  +"<td><input id=\"name"+ roomNum +"\" name=\"name\" type=\"text\" maxlength=\"30\" class=\"ipt05\"  style=\"width:90px\"/></td>"
	                  +"<td><input id=\"seatNum"+ roomNum +"\" name=\"seatNum\" type=\"text\" maxlength=\"3\" class=\"ipt05\"  style=\"width:20px\"/></td>"
	                  +"<td><input id=\"powerNum"+ roomNum +"\" name=\"powerNum\" type=\"text\" maxlength=\"3\" class=\"ipt05\"  style=\"width:20px\"/></td>"
	                  +"<td><input id=\"cableNum"+ roomNum +"\" name=\"cableNum\" type=\"text\" maxlength=\"3\" class=\"ipt05\"  style=\"width:20px\"/></td>"
	                  +"<td><input id=\"lineNum"+ roomNum +"\" name=\"lineNum\" type=\"text\" maxlength=\"3\" class=\"ipt05\"  style=\"width:20px\"/></td>"
	                  +"<td><input id=\"phoneNumber"+ roomNum +"\" name=\"phoneNumber\" type=\"text\" maxlength=\"13\" class=\"ipt05\"  style=\"width:65px\"/></td>"
	                  +"<td><input id=\"status"+roomNum+"\" type=\"checkbox\"></td>"
	                  +"<td><input id=\"environment"+ roomNum +"\" name=\"environment\" type=\"text\" maxlength=\"64\" class=\"ipt05\"  style=\"width:100px\"/></td>"
	                  +"<td><input id=\"remark"+ roomNum +"\" name=\"remark\" type=\"text\" maxlength=\"200\" class=\"ipt05\"  style=\"width:100px\"/></td>"
	                  +"<td><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveMeetingRoom(this,'" 
	                  + roomNum + "')\">保存</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert(this)\">取消</a></td>"
	                  +"</tr>";
	if($("#roomSet > tbody tr").length==0){
	  $("#roomSet > tbody").append(roomList)
	}else{
	  $(roomList).insertBefore($("#roomSet > tbody tr:eq(0)"));
	}
	
	areaSel = $("#areaSel").ySelect({
		width : 35, 
		url:BASE_PATH+"m/data_dict?act=ereasel",
		afterLoad : function() {
			areaSel.select(0);
		}
	}); 

	cancelEnter($("#name"+ roomNum));
	cancelEnter($("#seatNum"+ roomNum));
	cancelEnter($("#environment"+ roomNum));
	cancelEnter($("#powerNum"+ roomNum));
	cancelEnter($("#cableNum"+ roomNum));
	cancelEnter($("#lineNum"+ roomNum));
	cancelEnter($("#phoneNumber"+ roomNum));
	cancelEnter($("#status"+ roomNum));
	cancelEnter($("#remark"+ roomNum));
    $("#name" + roomNum).focus();
}

//解决IE6下按回车转到其它页面
function cancelEnter(obj) {
	obj.keypress(
	       function(e){
	           if(e.keyCode == 13){
	               return false;
	           }
	       }
	   );
}

//保存
function saveMeetingRoom(self,roomNum){
	var areaCode = $.trim(areaSel.getValue());
	var areaName = $.trim(areaSel.getDisplayValue());
	var name = $.trim($("#name" + roomNum).val());
	var seatNum = $.trim($("#seatNum" + roomNum).val());
	var environment = $.trim($("#environment" + roomNum).val());
	var powerNum = $.trim($("#powerNum" + roomNum).val());
	var cableNum = $.trim($("#cableNum" + roomNum).val());
	var lineNum = $.trim($("#lineNum" + roomNum).val());
	var phoneNumber = $.trim($("#phoneNumber" + roomNum).val());
	var status;
	var remark = $.trim($("#remark" + roomNum).val());
	
	//非空校验
	if("" == areaName) {
		$.alert("所属地区不能为空");
		$("#areaName" + roomNum).focus();
		return;
	}
	if("" == name) {
		$.alert("会议室名称不能为空");
		$("#name" + roomNum).focus();
		return;
	}
	if("" == seatNum) {
		$.alert("座位数不能为空");
		$("#seatNum" + roomNum).focus();
		return;
	}
		
	if("" == powerNum) {
		$.alert("插座数不能为空");
		$("#powerNum" + roomNum).focus();
		return;
	}
	if("" == cableNum) {
		$.alert("网线数不能为空");
		$("#cableNum" + roomNum).focus();
		return;
	}
	if("" == lineNum) {
		$.alert("电话线数不能为空");
		$("#lineNum" + roomNum).focus();
		return;
	}
	
	var result = $.validChar(name,"'\"<>");
	if (result){
		$.alert("会议室名称不能输入非法字符：" + result);
		$("#name" + roomNum).focus();
		return;
	}
	
	result = $.validChar(environment,"'\"<>");
	if (result){
		$.alert("环境不能输入非法字符：" + result);
		$("#environment" + roomNum).focus();
		return;
	}
	
	result = $.validChar(remark,"'\"<>");
	if (result){
		$.alert("备注不能输入非法字符：" + result);
		$("#remark" + roomNum).focus();
		return;
	}
	if (!re.test(seatNum)){
        $.alert("座位数必须为非负整数!");
        $("#seatNum" + roomNum).focus();
        return;
     }
    
    if (!re.test(powerNum)){
        $.alert("插座数必须为非负整数!");
        $("#powerNum" + roomNum).focus();
        return;
     }
     
	if (!re.test(cableNum)){
        $.alert("网线数必须为非负整数!");
        $("#cableNum" + roomNum).focus();
        return;
     }
     
    if (!re.test(lineNum)){
        $.alert("电话线数必须为非负整数!");
        $("#lineNum" + roomNum).focus();
        return;
     }
	
	if (isNaN(phoneNumber)) {
		$.alert("电话号码只能为数字！");
		$("#phoneNumber" + roomNum).focus();
		return;
	}
	
	if($("#status" + roomNum)[0].checked){
		status="Y";
	}else{
		status="N";
	}
	
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		async : true,
		url  : "m/meet_param",
		data : {act:"add",
				areaCode:areaCode,
				name:name,
				seatNum:seatNum,
				environment:environment,
				powerNum:powerNum,
				cableNum:cableNum,
				lineNum:lineNum,
				phoneNumber:phoneNumber,
				status:status,
				remark:remark},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					$.alert("保存成功");
					queryList();
					$("#addMeetingRoom").removeAttr("disabled","true").removeClass("icoNone");
				}else {
					$.alert($("message",xml).text());
				}
				edit = false;
	       },
	       error : $.ermpAjaxError
	});	
}



//查询
function queryList(){
	
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/meet_param",
		data : {act:"query"},
	       success : function(xml){
	       		//$.alert(xml.xml)
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var bodyHTML = "";
					$("meet-param",xml).each(
	                    function(index){
	                        var meetRoom = $(this);
	                        bodyHTML += createTR(meetRoom.attr("id"), $("areaCode",meetRoom).text(), $("areaName",meetRoom).text(), 
	                        					 $("name",meetRoom).text(),$("seatNum",meetRoom).text(),
	                        					 $("powerNum",meetRoom).text(), $("cableNum",meetRoom).text(), 
	                        					 $("lineNum",meetRoom).text(), $("phoneNumber",meetRoom).text(), 
	                        					 $("status",meetRoom).text(), $("environment",meetRoom).text(), 
	                        					 $("remark",meetRoom).text());
	                });
	                $("#roomSet > tbody").html(bodyHTML);
	                $("#addMeetingRoom").removeAttr("disabled","true").removeClass("icoNone");
					edit=false;
					enWrap();
				}else{
               		$.alert($("message",xml).text());
           		};
	       },
	       error : $.ermpAjaxError
	});	
	
}

//创建行
function createTR(id,areaCode, areaName,name, seatNum,powerNum,cableNum,lineNum,phoneNumber,status,environment,remark){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		 + "<td>" + areaName + "<input type=\"hidden\" name=\"areaCode\" value=\"" + areaCode + "\"/></td>" 
         + "<td>" + name + "</td>" 
         + "<td>" + seatNum + "</td>"
         + "<td>" + powerNum + "</td>"
         + "<td>" + cableNum + "</td>" 
         + "<td>" + lineNum + "</td>"
         + "<td>" + phoneNumber + "</td>" 
         + "<td>" + status + "</td>"
         + "<td>" + environment + "</td>" 
         + "<td>" + remark + "</td>"; 
         //操作
	var op = ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyMeetingRoom(this,'" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") 
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteMeetingRoom('" + id + "','" + name + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "");
    html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));                   	           
    html += "</td></tr>";
    return html;
}

//删除
function deleteMeetingRoom(id, name){
	
	if (edit) {
		$.alert("请先保存");
		return;
	}
	$.confirm("确定要删除“" + name + "”吗？", function(r){
		if (r) {
			$.ajax({
		       type : "POST",
		       cache: false,
		       url  : "m/meet_param",
		       data : {act:"delete",id:id},
		       success : function(xml){
		           var messageCode = $("message",xml).attr("code");
		           if(messageCode == "1"){
//		           		$.alert("删除成功!")
		           		queryList();
		           }else{
		           		$.alert($("message",xml).text());
		           }
		       },
		       error : $.ermpAjaxError
		   	});
		}
	});
}
//修改
function modifyMeetingRoom(self,id){
	
	$("#addMeetingRoom").attr("disabled","true").addClass("icoNone");

	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}

	var selfObj=$(self).parent().parent().find("td");
	var areaCode = $.trim($("input[name='areaCode']", selfObj.get(0)).val());
	var newOperate="<a href=\"javascript:void(0);\" onclick=\"modifySuccess(this,'" + id + "')\" class=\"opLink\">保存</a>&nbsp;|&nbsp"
	              +"<a href=\"javascript:void(0);\" onclick=\"cancelModify(this,'" + id + "','" + areaCode + "','" + selfObj.eq(0).text() + "','" + selfObj.eq(1).text() + "','" + selfObj.eq(2).text() + "','" + selfObj.eq(3).text()
	              + "','" + selfObj.eq(4).text() + "','" + selfObj.eq(5).text() + "','" + selfObj.eq(6).text() + "','" + selfObj.eq(7).text() 
	              + "','" + selfObj.eq(8).text() + "','" + selfObj.eq(9).text() + "')\" class=\"opLink\">取消</a>";
	
	selfObj.eq(0).html("<div id=\"areaSel"+ id +"\" name=\"areaSel\"></div><input type=\"hidden\" name=\"areaCode\" value=\"" + areaCode + "\"/>");
	selfObj.eq(1).html("<input id=\"name"+id+"\" name=\"name\" type=\"text\" maxlength=\"30\" class=\"ipt05\"  style=\"width:90px\" value=\""+selfObj.eq(1).text()+"\">");
	selfObj.eq(2).html("<input id=\"seatNum"+id+"\" name=\"seatNum\" type=\"text\" maxlength=\"3\" class=\"ipt05\"  style=\"width:20px\" value=\""+selfObj.eq(2).text()+"\">");
	selfObj.eq(3).html("<input id=\"powerNum"+id+"\" name=\"powerNum\" type=\"text\" maxlength=\"3\" class=\"ipt05\"  style=\"width:20px\" value=\""+selfObj.eq(3).text()+"\">");
	selfObj.eq(4).html("<input id=\"cableNum"+id+"\" name=\"cableNum\" type=\"text\" maxlength=\"3\" class=\"ipt05\"  style=\"width:20px\" value=\""+selfObj.eq(4).text()+"\">");
	selfObj.eq(5).html("<input id=\"lineNum"+id+"\" name=\"lineNum\" type=\"text\" maxlength=\"3\" class=\"ipt05\"  style=\"width:20px\" value=\""+selfObj.eq(5).text()+"\">");
	selfObj.eq(6).html("<input id=\"phoneNumber"+id+"\" name=\"phoneNumber\" type=\"text\" maxlength=\"13\" class=\"ipt05\"  style=\"width:65px\" value=\""+selfObj.eq(6).text()+"\">");
	selfObj.eq(7).html("<input id=\"status"+id+"\" type=\"checkbox\" " + ((selfObj.eq(7).text()=="是")?"checked":"") + ">");
	selfObj.eq(8).html("<input id=\"environment"+id+"\" name=\"environment\" type=\"text\" maxlength=\"64\" class=\"ipt05\"  style=\"width:100px\" value=\""+selfObj.eq(8).text()+"\">");
	selfObj.eq(9).html("<input id=\"remark"+id+"\" name=\"remark\" type=\"text\" maxlength=\"200\" class=\"ipt05\"  style=\"width:100px\" value=\""+selfObj.eq(9).text()+"\">");

	selfObj.eq(10).html(newOperate);
	
	areaSel = $("#areaSel"+ id).ySelect({
		width : 35, 
		url:BASE_PATH+"m/data_dict?act=ereasel",
		afterLoad : function() {
			if (areaCode != null && areaCode != "") {
				areaSel.select(areaCode);
			}
		}
	}); 
	
	cancelEnter($("#name"+ id));
	cancelEnter($("#seatNum"+ id));
	cancelEnter($("#environment"+ id));
	cancelEnter($("#powerNum"+ id));
	cancelEnter($("#cableNum"+ id));
	cancelEnter($("#lineNum"+ id));
	cancelEnter($("#phoneNumber"+ id));
	cancelEnter($("#status"+ id));
	cancelEnter($("#remark"+ id));
    $("#name" + id).focus();

}


//修改成功
function modifySuccess(self,id){
	var selfObj=$(self).parent().parent().find("td");
	var areaCode = $.trim(areaSel.getValue());
	var areaName = $.trim(areaSel.getDisplayValue());
	var name = $.trim(selfObj.eq(1).find("input").val());
	var seatNum = $.trim(selfObj.eq(2).find("input").val());
	var powerNum = $.trim(selfObj.eq(3).find("input").val());//插座数
	var cableNum = $.trim(selfObj.eq(4).find("input").val());//网线
	var lineNum = $.trim(selfObj.eq(5).find("input").val());//电话线
	var phoneNumber = $.trim(selfObj.eq(6).find("input").val());//电话号码
	var environment = $.trim(selfObj.eq(8).find("input").val());//环境
	var remark = $.trim(selfObj.eq(9).find("input").val());//备注
	
	if("" == areaName) {
		$.alert("所属地区不能为空");
		return;
	}
	if("" == name) {
		$.alert("会议室名称不能为空");
		selfObj.eq(1).find("input").focus();
		return;
	}
	if("" == seatNum) {
		$.alert("座位数不能为空");
		selfObj.eq(2).find("input").focus();
		return;
	}
		
	if("" == powerNum) {
		$.alert("插座数不能为空");
		selfObj.eq(3).find("input").focus();
		return;
	}
	if("" == cableNum) {
		$.alert("网线数不能为空");
		selfObj.eq(4).find("input").focus();
		return;
	}
	if("" == lineNum) {
		$.alert("电话线数不能为空");
		selfObj.eq(5).find("input").focus();
		return;
	}
	
	var result = $.validChar(name,"'\"<>");
	if (result){
		$.alert("会议室名称不能输入非法字符：" + result);
		selfObj.eq(1).find("input").focus();
		return;
	}
	
	result = $.validChar(environment,"'\"<>");
	if (result){
		$.alert("环境不能输入非法字符：" + result);
		selfObj.eq(8).find("input").focus();
		return;
	}
	
	result = $.validChar(remark,"'\"<>");
	if (result){
		$.alert("备注不能输入非法字符：" + result);
		selfObj.eq(9).find("input").focus();
		return;
	}
	
	
	
	if (!re.test(seatNum)){
        $.alert("座位数必须为非负整数!");
        selfObj.eq(2).find("input").focus();
        return;
     }
    
    if (!re.test(powerNum)){
        $.alert("插座数必须为非负整数!");
        selfObj.eq(3).find("input").focus();
        return;
     }
     
	if (!re.test(cableNum)){
        $.alert("网线数必须为非负整数!");
        selfObj.eq(4).find("input").focus();
        return;
     }
     
    if (!re.test(lineNum)){
        $.alert("电话线数必须为非负整数!");
        selfObj.eq(5).find("input").focus();
        return;
     }
	
	if (isNaN(phoneNumber)) {
		$.alert("电话号码只能为数字！");
		selfObj.eq(6).find("input").focus();
		return;
	}
	
	var status;
	if($("#status" + id)[0].checked){
		status="Y";
	}else{
		status="N";
	}
	
	
	$.ajax({
	  type : "POST",
	  cache: false,
	  url  : "m/meet_param",
	  data : {act:"modify",
	 			id:id,
	 			areaCode:areaCode,
	 			name:name,
	  			seatNum:seatNum,
				environment:environment,
				powerNum:powerNum,
				cableNum:cableNum,
				lineNum:lineNum,
				phoneNumber:phoneNumber,
				status:status,
				remark:remark},
	     success : function(xml){
		      //解析XML中的返回代码
		      var messageCode = $("message",xml).attr("code");
		      if(messageCode == "1"){
		         $.alert("会议室资料修改成功");
		         queryList();
		        }else {
			        $.alert( $("message",xml).text());
			    }
	       },
	     error : $.ermpAjaxError
	});
}
//取消修改
function cancelModify(self,id,areaCode, areaName, name,seatNum,powerNum,cableNum,lineNum,phoneNumber,status,environment,remark){
	$(createTR(id,areaCode, areaName,name,seatNum,powerNum,cableNum,lineNum,phoneNumber,status,environment,remark)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#addMeetingRoom").removeAttr("disabled","true").removeClass("icoNone");
					edit=false;
}

//折行
function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}