var mainFrame = $.getMainFrame();
$(initPage);
function initPage(){
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
		        	   html += "<tr>" +
		        	   	"<td class=\"listData\">" + (list[i].custName==null ? "" : list[i].custName) + "</td>" +
		        	   	"<td class=\"listData\">" + (list[i].sex==null ? "" : (list[i].sex=="M" ? "男" : "女")) + "</td>" +
		        	   	"<td class=\"listData\">"+(list[i].tel==null ?"":list[i].tel)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].email==null?"":list[i].email)+"</td>" +
	           			"<td class=\"listData\">"+(list[i].recommendProduct==null?"":list[i].recommendProduct)+"</td>" +
			           	"<td><input type=\"image\" class=\"openViewImg\" src=\"themes/comm/images/crmView_ico.gif\" title=\"打开\" onclick=\"openTask('" + id + "')\" /></td>" +
	           			"</tr>";
		           }
		        }
	            $("#customerList").html(html);
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