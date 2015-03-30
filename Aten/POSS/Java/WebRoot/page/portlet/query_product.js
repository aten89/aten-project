var mainFrame = $.getMainFrame();
$(initPage);
function initPage(){
	
	//信息内容块切换	
	$("#crmTab li").each(function(i){
		$(this).click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			if ($(this).attr("id") == "foundProdLI") {
				//加载最后一个列表内容
				$("#tab0_last").show().siblings().hide();
				loadFoundProductList();
			} else {
				$("#tab0" + i).show().siblings().hide();
				loadProductList(i);
			}
		});	
	}) ;
	//选中第一个
	$("#crmTab li:first-child").click();
	
	//设置固定宽度
	$(".gnmkBk,#crmTab").width($(".boxShadow").width());
	$(".gnmkBk").height($(window).height()-45);
}

function loadProductList(i){
	var index = i;
	var prodType = $("#prodType"+i).val();
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/prod_info/query_prodInfoListPage?pageNo=1&pageSize=10&prodType=" + prodType,
		dataType : "json",
        success : function(data) {
        	loadSuccess(data, $("#prodList" + index));
        },
        error : $.ermpAjaxError
    });
}

function loadFoundProductList(){
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/prod_info/query_prodInfoListPage?pageNo=1&pageSize=10&prodStatus=STATUS_FOUND",
		dataType : "json",
        success : function(data) {
        	loadSuccess(data, $("#prodList_last"));
        },
        error : $.ermpAjaxError
    });
}

function loadSuccess(data, dataShowEle) {
   	var html="";
	if ($.checkErrorMsg(data)) {
		var page = data.prodInfoListPage;
        if( page.dataList!=null){
           var list = page.dataList;
           for(var i =0; i<list.length; i++){
	           	var id = list[i].id;
	           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
	           	"<td class=\"listData\"><a href='javascript:void(0);'  onclick=\"view('" + id + "')\">"+(list[i].prodName==null ? "":list[i].prodName)+"</a></td>" +
	           	"<td class=\"listData\">"+(list[i].secondClassifyName==null ? "":list[i].secondClassifyName)+"</td>" +
	           	"<td class=\"listData\">"+(list[i].supplier==null ? "":list[i].supplier.supplier)+"</td>" +
	           	"<td class=\"listData\">"+(list[i].sellAmount==null ? "":list[i].sellAmount)+"</td>" +
	           	"<td class=\"listData\">"+(list[i].expectSellDate==null ? "":list[i].expectSellDate)+"</td>" +
	           	"<td class=\"listData\">"+(list[i].operationPeriod==null ? "":list[i].operationPeriod)+"</td>" +
	           	"<td class=\"listData\">"+(list[i].sellTimeLimit==null ? "":list[i].sellTimeLimit)+"</td>" +
	           	"<td class=\"listData\">"+(list[i].sellRankName==null ? "":list[i].sellRankName)+"</td>" +
	           	"<td class=\"listData\">"+(list[i].totalAppointmentAmount==null ? "":list[i].totalAppointmentAmount)+"</td>" +
	           	"<td class=\"listData\">"+(list[i].transferAmount==null ? "":list[i].transferAmount)+"</td>" +
	           	"<td class=\"listData\">"+(list[i].toAccountAmount==null ? "":list[i].toAccountAmount)+"</td>" +
	        	"<td class=\"listData\">"+(list[i].virtualRemainAmount==null ? "":list[i].virtualRemainAmount)+"</td>" +
	           	"<td><a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"view('" + id + "')\">详情</a>" +
  				"</td>";
       			"</tr>";
           }
        }
        dataShowEle.html(html);
	}
}

/**
 * 查看
 * @param id
 */
function view(id) {
	mainFrame.addTab({
		id:"view_prodInfo_"+id,
		title:"查看产品信息",
		url:BASE_PATH + "/m/prod_info/init_prodInfoFrame?prodInfoId=" + id
	});
}