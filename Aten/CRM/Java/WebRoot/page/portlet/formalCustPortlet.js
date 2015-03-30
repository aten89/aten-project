var mainFrame = $.getMainFrame();
$(initPage);
function initPage(){
	
	//信息内容块切换	
	$("#crmTab li").each(function(i){
		$(this).click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			$("#tab0" + i).show().siblings().hide();
		});	
	}) ;
	
	loadFormalCusts();
	
	loadPotentialCusts();
	
}

function loadFormalCusts(){
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/customer/queryCustomerInfoList?customerStatus=5&pageNo=1&pageSize=15",
		dataType : "json",
		data:{
			
		},
        success : function(data,i){
           	var html="";
        	if ($.checkErrorMsg(data)) {
        		var page = data.listPage;
		        if(page != null && page.dataList!=null){
		           var list = page.dataList;
		           for(var i =0; i<list.length; i++){
		        	   var id = list[i].id;
		        	   html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		        	   	"<td class=\"listData\">" + (list[i].custName==null ? "" : list[i].custName) + "</td>" +
		        	   	"<td class=\"listData\">" + (list[i].sex==null ? "" : (list[i].sex=="M" ? "男" : "女")) + "</td>" +
		        	   	"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].email==null?"":list[i].email)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].recommendProduct==null?"":list[i].recommendProduct)+"</td>" +
			           	"<td><a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"openTask('" + id + "')\">详情</a></td>" +
	           			"</tr>";
		           }
		        }
	            $("#formalList").html(html);
        	}
        },
        error : $.ermpAjaxError
    });
}

function loadPotentialCusts(){
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/customer/queryCustomerInfoList?customerStatus=3&pageNo=1&pageSize=15",
		dataType : "json",
		data:{
			
		},
        success : function(data,i){
           	var html="";
        	if ($.checkErrorMsg(data)) {
        		var page = data.listPage;
		        if(page != null && page.dataList!=null){
		           var list = page.dataList;
		           for(var i =0; i<list.length; i++){
		        	   var id = list[i].id;
		        	   html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		        	   	"<td class=\"listData\">" + (list[i].custName==null ? "" : list[i].custName) + "</td>" +
		        	   	"<td class=\"listData\">" + (list[i].sex==null ? "" : (list[i].sex=="M" ? "男" : "女")) + "</td>" +
		        	   	"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].email==null?"":list[i].email)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].recommendProduct==null?"":list[i].recommendProduct)+"</td>" +
			           	"<td><a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"openTask('" + id + "')\">详情</a></td>" +
	           			"</tr>";
		           }
		        }
	            $("#potentialList").html(html);
        	}
        },
        error : $.ermpAjaxError
    });
}

function openTask(id) {
	mainFrame.addTab({
		id:"view_customer_"+id,
		title:"查看客户资料",
		url:BASE_PATH + "/m/customer/initframe?customerId=" + id
	});
}