/*
* jQuery SimpleTree Drag&Drop plugin
* Update on 22th May 2008
* Version 0.3
*
* Licensed under BSD <http://en.wikipedia.org/wiki/BSD_License>
* Copyright (c) 2008, Peter Panov <panov@elcat.kg>, IKEEN Group http://www.ikeen.com
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of the Peter Panov, IKEEN Group nor the
*       names of its contributors may be used to endorse or promote products
*       derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY Peter Panov, IKEEN Group ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL Peter Panov, IKEEN Group BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/


$.fn.simpleTree = function(opt){
	return this.each(function(){
		var TREE = this;
		var ROOT = $('.root',this);
		var mousePressed = false;
		var mouseMoved = false;
		var dragMoveType = false;
		var dragNode_destination = false;
		var dragNode_source = false;
		var dragDropTimer = false;
		TREE.option = {
			drag:		false,
			animate:	false,
			autoclose:	false,
			speed:		'fast',
			afterAjax:	false,
			afterMove:	false,
			afterClick:	false,
			afterDblClick:	false,
			docToFolderConvert:false,
			selectRoot : false,
			createCheckBox : false,
			json:false,
			jsonCallBackParam:"jsoncallback=?",
			basePath : null
		};
		TREE.option = $.extend(TREE.option,opt);
		$.extend(this, {getSelected: function(){
			return $('span.active', this).parent();
		}});
		TREE.closeNearby = function(obj)
		{
			$(obj).siblings().filter('.folder-open, .folder-open-last').each(function(){
				var childUl = $('>ul',this);
				var className = this.className;
				this.className = className.replace('open','close');
				if(TREE.option.animate)
				{
					childUl.animate({height:"toggle"},TREE.option.speed);
				}else{
					childUl.hide();
				}
			});
		};
		TREE.nodeToggle = function(obj, callFun) {//增加callFun为打开或关闭后回调方法
			var childUl = $('>ul',obj);
			if(childUl.is(':visible')) {
				obj.className = obj.className.replace('open','close');
				if (TREE.option.animate) {
					childUl.animate({height:"toggle"},TREE.option.speed);
				} else {
					childUl.hide();
				}
				if(typeof callFun == 'function') {//直接回调
					callFun(childUl);
				}
			} else {
				obj.className = obj.className.replace('close','open');
				if(TREE.option.animate) {
					childUl.animate({height:"toggle"},TREE.option.speed);
				} else {
					childUl.show();
				}
				if(TREE.option.autoclose)TREE.closeNearby(obj);
				if(childUl.is('.ajax')) {
					TREE.setAjaxNodes(childUl, callFun);//加载后回调
				} else {
					if(typeof callFun == 'function') {//直接回调
						callFun(childUl);
					}
				}
			}
		};
		
		//内部调用，加载AJAX HTML信息
		TREE.loadAjaxNodes = function(node, afterAjaxLoad) {
			var url = $.trim($('>li', node).text());
			if(url && url.indexOf('url:'))
			{
				url=$.trim(url.replace(/.*\{url:(.*)\}/i ,'$1'));
				if (TREE.option.json){	//是否以JOSN方式访问
					if (TREE.option.basePath){//绝对路径，处理跨域访问，拼接jsonCallBackParam参数
						url = TREE.option.basePath + url + (url.indexOf('?') ? '&' : '?') + TREE.option.jsonCallBackParam;
					}
					$.getJSON(url,function(data){
						afterAjaxLoad(data.htmlValue);
					});
				}else{
					//兼容原来的方式
					$.ajax({
						type: "POST",
						url: url,
						contentType:'html',
						cache:false,
						success: function(responce){
							afterAjaxLoad(responce);
						}
					});
				}
			}
		}
		
		TREE.setAjaxNodes = function(node, callFun)
		{
			TREE.loadAjaxNodes(node, function(responce){
				node.removeAttr('class');
				node.html(responce);
				TREE.setTreeNodes(node, true);
				if(typeof TREE.option.afterAjax == 'function')
				{
					TREE.option.afterAjax(node);
				}
				if(typeof callFun == 'function') {
					callFun(node);
				}
			});
		};
		TREE.setTreeNodes = function(obj, useParent){
			obj = useParent? obj.parent():obj;
			//add by zpk
			//add click callback function to root
			if(!useParent && TREE.option.selectRoot){
				$(">span",obj).click(function(){
					$('.active',TREE).attr('class','text');
					$(this).addClass("rootactive");
					if(typeof TREE.option.afterClick == 'function')
					{
						TREE.option.afterClick($(this).parent());
					}
					return false;
				});
			};
			//end
			$('li>span', obj).addClass('text')
			.bind('selectstart', function() {
				return false;
			}).click(function(){
				$('.active',TREE).attr('class','text');
				if(TREE.option.selectRoot){$(".rootactive").removeClass("rootactive");};
				if(this.className=='text')
				{
					this.className='active';
				}
				if (TREE.option.createCheckBox){
					var obj = $(this).parent().children("input[type='checkbox']"); 
					if (!obj.attr("disabled")) {
						obj.attr("checked",!obj.attr("checked"));
					}
				}
				if(typeof TREE.option.afterClick == 'function')
				{
					TREE.option.afterClick($(this).parent());
				}
				return false;
			}).dblclick(function(){
				mousePressed = false;
				TREE.nodeToggle($(this).parent().get(0));
				if(typeof TREE.option.afterDblClick == 'function')
				{
					TREE.option.afterDblClick($(this).parent());
				}
				return false;
			}).mousedown(function(event){
				mousePressed = true;
				cloneNode = $(this).parent().clone();
				var LI = $(this).parent();
				if(TREE.option.drag)
				{
					$('>ul', cloneNode).hide();
					$('body').append('<div id="drag_container"><ul></ul></div>');
					$('#drag_container').hide().css({opacity:'0.8'});
					$('#drag_container >ul').append(cloneNode);
					$("<img>").attr({id	: "tree_plus",src	: "images/plus.gif"}).css({width: "7px",display: "block",position: "absolute",left	: "5px",top: "5px", display:'none'}).appendTo("body");
					$(document).bind("mousemove", {LI:LI}, TREE.dragStart).bind("mouseup",TREE.dragEnd);
				}
				return false;
			}).mouseup(function(){
				if(mousePressed && mouseMoved && dragNode_source)
				{
					TREE.moveNodeToFolder($(this).parent());
				}
				TREE.eventDestroy();
			});
			$('li', obj).each(function(i){
				var className = this.className;
				var open = false;
				var cloneNode=false;
				var LI = this;
				var childNode = $('>ul',this);
				if(childNode.size()>0){
					if (className=="root"){
					   //root and first node
					}else{
						var setClassName = 'folder-';
						if(className && className.indexOf('open')>=0){
							setClassName=setClassName+'open';
							open=true;
						}else{
							setClassName=setClassName+'close';
						}
						this.className = setClassName + ($(this).is(':last-child')? '-last':'');
	
						if(!open || className.indexOf('ajax')>=0)childNode.hide();
					}
					TREE.setTrigger(this);
				}else{
					var setClassName = 'doc';
					this.className = setClassName + ($(this).is(':last-child')? '-last':'');
					if (TREE.option.createCheckBox){
						var id = this.id;
						var text = $(this).find("span").text();
						$('>span',this).before('<input id="chk_' + id + '" name="chk_' + text + '" type="checkbox" value="' + id + ":" + text + '">');
					}
				}
			}).before('<li class="line">&nbsp;</li>')
			.filter(':last-child').after('<li class="line-last"></li>');
			TREE.setEventLine($('.line, .line-last', obj));
		};
		TREE.setTrigger = function(node){
			if (TREE.option.createCheckBox){
				var id = node.id;
				var text = $(node).find("span").text();
				$('>span',node).before('<input type="button" class="trigger" value=" "><input id="chk_' + id + '" name="chk_' + text + '" type="checkbox" value="' + id + ":" + text + '">');
			}else{
				$('>span',node).before('<input type="button" class="trigger" value=" ">');
			}

			var trigger = $('>.trigger', node);
			trigger.click(function(event){
				TREE.nodeToggle(node);
			});
			if(!$.browser.msie)
			{
				trigger.css('float','left');
			}
		};
		TREE.dragStart = function(event){
			var LI = $(event.data.LI);
			if(mousePressed)
			{
				mouseMoved = true;
				if(dragDropTimer) clearTimeout(dragDropTimer);
				if($('#drag_container:not(:visible)')){
					$('#drag_container').show();
					LI.prev('.line').hide();
					dragNode_source = LI;
				}
				$('#drag_container').css({position:'absolute', "left" : (event.pageX + 5), "top": (event.pageY + 15) });
				if(LI.is(':visible'))LI.hide();
				var temp_move = false;

				if(event.target.tagName.toLowerCase()=='span' && $.inArray(event.target.className, Array('text','active','trigger'))!= -1)
				{
					var parent = event.target.parentNode;
					var offs = $(parent).offset({scroll:false});
					var screenScroll = {x : (offs.left - 3),y : event.pageY - offs.top};
					var isrc = $("#tree_plus").attr('src');
					var ajaxChildSize = $('>ul.ajax',parent).size();
					screenScroll.x += 19;
					screenScroll.y = event.pageY - screenScroll.y + 5;

					if(parent.className.indexOf('folder-close')>=0 && ajaxChildSize==0)
					{
						if(isrc.indexOf('minus')!=-1)$("#tree_plus").attr('src','images/plus.gif');
						$("#tree_plus").css({"left": screenScroll.x, "top": screenScroll.y}).show();
						dragDropTimer = setTimeout(function(){
							parent.className = parent.className.replace('close','open');
							$('>ul',parent).show();
						}, 700);
					}else if(parent.className.indexOf('folder')>=0 && ajaxChildSize==0){
						if(isrc.indexOf('minus')!=-1)$("#tree_plus").attr('src','images/plus.gif');
						$("#tree_plus").css({"left": screenScroll.x, "top": screenScroll.y}).show();
					}else if(parent.className.indexOf('folder-close')>=0 && ajaxChildSize>0)
					{
						mouseMoved = false;
						$("#tree_plus").attr('src','images/minus.gif');
						$("#tree_plus").css({"left": screenScroll.x, "top": screenScroll.y}).show();
					}else{
						if(TREE.option.docToFolderConvert)
						{
							$("#tree_plus").css({"left": screenScroll.x, "top": screenScroll.y}).show();
						}else{
							$("#tree_plus").hide();
						}
					}
				}else{
					$("#tree_plus").hide();
				}
				return false;
			}
			return true;
		};
		TREE.dragEnd = function(){
			if(dragDropTimer) clearTimeout(dragDropTimer);
			TREE.eventDestroy();
		};
		TREE.setEventLine = function(obj){
			obj.mouseover(function(){
				if(this.className.indexOf('over')<0 && mousePressed && mouseMoved)
				{
					this.className = this.className.replace('line','line-over');
				}
			}).mouseout(function(){
				if(this.className.indexOf('over')>=0)
				{
					this.className = this.className.replace('-over','');
				}
			}).mouseup(function(){
				if(mousePressed && dragNode_source && mouseMoved)
				{
					dragNode_destination = $(this).parents('li:first');
					TREE.moveNodeToLine(this);
					TREE.eventDestroy();
				}
			});
		};
		TREE.checkNodeIsLast = function(node)
		{
			if(node.className.indexOf('last')>=0)
			{
				var prev_source = dragNode_source.prev().prev();
				if(prev_source.size()>0)
				{
					prev_source[0].className+='-last';
				}
				node.className = node.className.replace('-last','');
			}
		};
		TREE.checkLineIsLast = function(line)
		{
			if(line.className.indexOf('last')>=0)
			{
				var prev = $(line).prev();
				if(prev.size()>0)
				{
					prev[0].className = prev[0].className.replace('-last','');
				}
				dragNode_source[0].className+='-last';
			}
		};
		TREE.eventDestroy = function()
		{
			$(document).unbind('mousemove').unbind('mouseup').unbind('mousedown');
			$('#drag_container, #tree_plus').remove();
			if(dragNode_source)
			{
				$(dragNode_source).prev('.line').show();
				$(dragNode_source).show();
			}
			dragNode_destination = dragNode_source = mousePressed = mouseMoved = false;
		};
		TREE.convertToFolder = function(node){
			node[0].className = node[0].className.replace('doc','folder-open');
			node.append('<ul><li class="line-last"></li></ul>');
			TREE.setTrigger(node[0]);
			TREE.setEventLine($('.line, .line-last', node));
		};
		TREE.convertToDoc = function(node){
			$('>ul', node).remove();
			$('img', node).remove();
			node[0].className = node[0].className.replace(/folder-(open|close)/gi , 'doc');
		};
		TREE.moveNodeToFolder = function(node)
		{
			if(!TREE.option.docToFolderConvert && node[0].className.indexOf('doc')!=-1)
			{
				return true;
			}else if(TREE.option.docToFolderConvert && node[0].className.indexOf('doc')!=-1){
				TREE.convertToFolder(node);
			}
			TREE.checkNodeIsLast(dragNode_source[0]);
			var lastLine = $('>ul >.line-last', node);
			if(lastLine.size()>0)
			{
				TREE.moveNodeToLine(lastLine[0]);
			}
		};
		TREE.moveNodeToLine = function(node){
			TREE.checkNodeIsLast(dragNode_source[0]);
			TREE.checkLineIsLast(node);
			var parent = $(dragNode_source).parents('li:first');
			var line = $(dragNode_source).prev('.line');
			$(node).before(dragNode_source);
			$(dragNode_source).before(line);
			node.className = node.className.replace('-over','');
			var nodeSize = $('>ul >li', parent).not('.line, .line-last').filter(':visible').size();
			if(TREE.option.docToFolderConvert && nodeSize==0)
			{
				TREE.convertToDoc(parent);
			}else if(nodeSize==0)
			{
				parent[0].className=parent[0].className.replace('open','close');
				$('>ul',parent).hide();
			}
			if(typeof(TREE.option.afterMove) == 'function')
			{
				var pos = $(dragNode_source).prevAll(':not(.line)').size();
				TREE.option.afterMove($(node).parents('li:first'), $(dragNode_source), pos);
			}
		};
		TREE.init = function(obj)
		{
			TREE.setTreeNodes(obj, false);
		};
		TREE.init(ROOT);
		
		
		/***********提供外部设用方法 开始*********/
		//动态新增树节点
		//parentId：父节点对象或ID
		//ID，名称、属性集合
		TREE.addNode = function(parentId, id, name, attrs) {
			var parentNode = (typeof(parentId) == 'object') ? parentId : TREE.getNode(parentId);
			var nodeHtml =  "<li id='" + id + "'>" + "<span>" + name + "</span></li>";

			//增加节点前需展开父节点以避免Ajax未读取
			//$('>.trigger', parentNode).click();
			//判断是否为第一个节点
			var subul = $('ul', parentNode);
			if (subul.size() == 0){
				parentNode.append("<ul>" + nodeHtml + "</ul>");
			}else{
				$('>ul',parentNode).append(nodeHtml);
			}
			resetNodes(parentNode.parent());
			TREE.expandNode(parentNode);//添加节后展开父节点
			var node = $("#" + id);
			if (attrs) {
				node.attr(attrs);
			}
//			if(typeof TREE.option.afterAjax == 'function') {
//				TREE.option.afterAjax(node);
//			}
			return node;
		};
		
		//动态删除树节点
		TREE.removeNode = function(id) {
			var node = (typeof(id) == 'object') ? id : TREE.getNode(id);
		  	var parentNode = node.parent();
		  	var parentNode2 = parentNode;
		  //判断是否只剩最后一个节点
		  node.remove();
		  subli = $('li', parentNode);
		  if (subli.size()<3){
		      parentNode2 = parentNode.parent().parent();
              if (parentNode.parent().attr("class") == "root"){
                  //root
		          parentNode2 = parentNode.parent();
		          parentNode.html("");
		      }else{
		          parentNode2 = parentNode.parent().parent();
		          parentNode.remove();
		      }
		  }
		  resetNodes(parentNode2);
		};
		
		//动态更新树节点(节点ID，节点名称，节点添加的自定义属性)
		TREE.modifyNode = function(id, name, attrs) {
			var node = (typeof(id) == 'object') ? id : TREE.getNode(id);
			$("> span", node).text(name);
			if (attrs) {
				node.attr(attrs);
			}
		};
		
		//获到树节点，如果ID为空返回根节点
		TREE.getNode = function(id) {
			return id ? $('#' + id, this) : $(".root", this);
		};
		
		//取得当前节点的父节点 add by zsy
		TREE.getParentNode = function(id) {
			var node = TREE.getNode(id);
			if(node.attr('class') == 'root') {
				return ROOT;
			} else {
				return node.parent().parent();
			}
		};
		
		//当前节点同级内，向上移一位
		TREE.moveUp = function(id) {
			var node = id ? $('#' + id, this) : TREE.getSelected();
			var target = node.prevAll('li:has(span):first');
			if(target.size() == 0){//是最前面一个
				return;
			}
			var line = node.prev('li');
    		target.before(node);
			target.before(line);
			nodeClass = node.attr('class');
			if(nodeClass.indexOf('-last') > 0) {//如果移动的是最后一个要修改样式
				node.attr('class', nodeClass.replace('-last',''));
				target.attr('class', target.attr('class') + '-last');
			}
		};
		
		//当前节点同级内，移到最上面
		TREE.moveTop = function(id) {
			var node = id ? $('#' + id, this) : TREE.getSelected();
			var target = node.prevAll('li:has(span):last');
			if(target.size() == 0){//是最前面一个
				return;
			}
			var line = node.prev('li');
       		target.before(node);
			target.before(line);
			
			nodeClass = node.attr('class');
			if(nodeClass.indexOf('-last') > 0) {//如果移动的是最后一个要修改样式
				node.attr('class', nodeClass.replace('-last',''));
				target = node.nextAll('li:has(span):last');//移动后的最后一个节点
				target.attr('class', target.attr('class') + '-last');
			}
		};
		
		//当前节点同级内，向下移一位 
		TREE.moveDown = function(id) {
			var node = id ? $('#' + id, this) : TREE.getSelected();
			var target = node.nextAll('li:has(span):first');
			if(target.size() == 0){//是最后一个
				return;
			}
			var line = node.prev('li');
       		target.after(node);
			target.after(line);	
			
			targetClass = target.attr('class');
			if(targetClass.indexOf('-last') > 0) {//如果移动的是最后一个要修改样式
				target.attr('class', targetClass.replace('-last',''));
				node.attr('class', node.attr('class') + '-last');
			}
		};
		
		//当前节点同级内，移到最底下
		TREE.moveBottom = function(id) {
			var node = id ? $('#' + id, this) : TREE.getSelected();
			var node = TREE.getSelected();
			var target = node.nextAll('li:has(span):last');
			if(target.size() == 0){//是最后一个
				return;
			}
			var line = node.prev('li');
       		target.after(node);
			target.after(line);	
			
			targetClass = target.attr('class');
			if(targetClass.indexOf('-last') > 0) {//如果移动的是最后一个要修改样式
				target.attr('class', targetClass.replace('-last',''));
				node.attr('class', node.attr('class') + '-last');
			}
		};
		
		//点击树
		TREE.clickNode = function(id) {
			var node = (typeof(id) == 'object') ? id : TREE.getNode(id);
			$("> span", node).click();
		};
		
    	//重新读取树节点
    	TREE.reloadNodes = function(parentId, ajaxUrl) {
    		var parentNode = (typeof(parentId) == 'object') ? parentId : TREE.getNode(parentId);
    		
			$('>ul', parentNode).remove();
			parentNode.append("<ul class='ajax'><li>{url:" + ajaxUrl + "}</li></ul>");
			resetNodes(parentNode.parent());
			TREE.expandNode(parentNode);//重新节后展开父节点
		};
		    
		//如果节点未展开，展开节点，传ID或Node对象都可以
		TREE.expandNode = function(id, callFun) {
			var node = (typeof(id) == 'object') ? id : TREE.getNode(id);
			var childUl = $('>ul',node);
			if(!childUl.is(':visible')) {
				//如果不关闭状态，触发打开
				TREE.nodeToggle(node[0], callFun);
			}
		};
		
		//根据路径打开树节点
		//@param nodePath 节点ID路径字符串，如：id22/id33/id44
		TREE.expandNodePath = function(nodePath) {
      	    var nodes = nodePath.split("/");
		    expandNodes(nodes, 0 );
		};
		
		/***********提供外部设用方法 结束*********/
		
		//递归展开节点
		function expandNodes(nodes, startIndex) {
		    for (var i = startIndex; i < nodes.length; i++) {
		    	var  isAjax = $('>ul', "#" + nodes[i]).is('.ajax');//当前是否为AJAX节点（要在展开节点前才能判断）
		    	//展开当前节点
		    	TREE.expandNode(nodes[i], function(){
		    		if (isAjax) {//如是AJAX节点，才要加载后递归回调这个方法
	        			expandNodes(nodes, i);
	        		 }
		    	});
		    	if (isAjax) {//如果是AJAX节点，进入AJAX递归，中止当前循环
                    break;
                }
		        if (i == nodes.length - 1) {//如果遍历到最后一个路径，打开
		        	TREE.clickNode(nodes[i]);
		        }
            }
		};
		
		//清除样式、事件后，重新生成树节点
    	function resetNodes(parentNode) {
			$('li', parentNode).remove('.line').remove('.line-last');
		  	$('img', parentNode).remove(".trigger");
		  	$('li>span', parentNode).unbind();
		  	TREE.setTreeNodes(parentNode, false);
		};
	});
}