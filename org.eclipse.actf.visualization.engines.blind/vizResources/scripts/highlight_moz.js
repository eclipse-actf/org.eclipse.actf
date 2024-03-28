/*******************************************************************************
 * Copyright (c) 2004, 2024 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/ 

prevNodeArray = new Array();
prevBorderWidthArray = new Array();
prevBorderColorArray = new Array();
prevBorderStyleArray = new Array();
prevBackgroundColorArray = new Array();
prevColorArray = new Array();
prevFilterArray = new Array();
var x1=0;
var x2=0;
var y1=0;
var y2=0;
var idNumber=0;
var idLine=0;
var alength=30;
var angleValue=30;
var font_Size=16;
var sux;
var suy;
var arrayIdex=0;
arrayLine=new Array();
arrayPoint=new Array();
var start_switch =true;
var est;
var idNumber_Old=0;
var refresh=false;
//var HIGHLIGHT_BGCOLOR = "#ffdddd";
var HIGHLIGHT_BGCOLOR = "rgb(255, 221, 221)";
var MAX_PROBLEM_SELECTION = 500;

// call it like: setHighlight2(new Array(43,47,49), new Array(45,47,50));
function setHighlight2(firstNodeArray, lastNodeArray) {	
	if(firstNodeArray.length > MAX_PROBLEM_SELECTION) {
		if (!confirm("You selected more than " + MAX_PROBLEM_SELECTION + " problems. It will take time to highlight all of them. Do you want to continue?")) {
			return;
		}
	}
	//document.body.scrollTop=0;
	//document.body.scrollLeft=0;
	clearHighlight();	
	var bScrolled=false;
	var size = firstNodeArray.length;	
	for (var i = 0; i < size; i++) {
		for(var j = firstNodeArray[i]; j <= lastNodeArray[i]; j++) {	
			//curNode = document.all["id" + j];
			curNode=document.getElementById("id"+j);
			if(curNode!=null){				
				if (curNode != null && !bScrolled) {
					_scrollToVisible(curNode);
					bScrolled=true;
				}
				recursiveHighlight2(curNode,1);
			}
		}
	}	
}

function isHighLighted(nodeId){
	var preSize=prevNodeArray.length;
	for(var i=0;i<preSize;i++){
		curNode = prevNodeArray[i];
		if(curNode.getAttribute('id')==nodeId){
			return true;
		}
	}
	return false;
}

function recursiveHighlight2(curNode, start){
	if (curNode != null) {
		if (curNode.nodeType == 1) {
			var nodeid=curNode.getAttribute('id');
			if((nodeid != "") && !isHighLighted(nodeid)){
				prevNodeArray.push(curNode);
				prevBorderWidthArray.push(curNode.style.borderWidth);
				prevBorderColorArray.push(curNode.style.borderColor);
				prevBorderStyleArray.push(curNode.style.borderStyle);
				prevBackgroundColorArray.push(curNode.style.backgroundColor);
				prevColorArray.push(curNode.style.color);
				if (curNode.nodeName.match(/IMG/i) != null) {
					prevFilterArray.push(curNode.style.filter);
				}
	
				curNode.style.borderWidth = "1px";
				curNode.style.borderColor = "red";
				curNode.style.borderStyle = "dotted";
				curNode.style.backgroundColor = "yellow";
				curNode.style.color = "black";
				if (curNode.nodeName.match(/IMG/i) != null) {
					curNode.style.filter = "invert()";
				}
			}
		}

		var curChild = curNode.firstChild;
		while(curChild != null) {
			recursiveHighlight2(curChild, 0);			
			curChild = curChild.nextSibling;
		}
	}
}

function setHighlight(firstNode, lastNode) {
	//document.body.scrollTop=0;
	//document.body.scrollLeft=0;
	clearHighlight();
	for(var i = firstNode; i < lastNode + 1; i++) {
		//curNode = document.all["id" + i];
		curNode=document.getElementById("id"+i);		
		if(curNode!=null){					
			_scrollToVisible(curNode);
			recursiveHighlight(curNode, 1);	
		}
	}
}

function recursiveHighlight(curNode, start){
	if (curNode != null) {
		if (curNode.nodeType == 1) {
			var nodeid=curNode.getAttribute('id');
			if((nodeid != "") && !isHighLighted(nodeid)){	
				prevNodeArray.push(curNode);
				prevBorderWidthArray.push(curNode.style.borderWidth);
				prevBorderColorArray.push(curNode.style.borderColor);
				prevBorderStyleArray.push(curNode.style.borderStyle);
				prevBackgroundColorArray.push(curNode.style.backgroundColor);
				prevColorArray.push(curNode.style.color);
				if (curNode.nodeName.match(/IMG/i) != null) {
					prevFilterArray.push(curNode.style.filter);
				}
				
				curNode.style.borderWidth = "1px";
				curNode.style.borderColor = "red";
				curNode.style.borderStyle = "dotted";
				curNode.style.backgroundColor = "yellow";
				curNode.style.color = "black";
				if (curNode.nodeName.match(/IMG/i) != null) {
					curNode.style.filter = "invert()";
				}
			}
		}
		
		var curChild = curNode.firstChild;
		while(curChild != null) {
			recursiveHighlight(curChild, 0);			
			curChild = curChild.nextSibling;
		}
	}
}

function clearHighlight() {
	var size = prevNodeArray.length;
	for (var i = 0; i < size; i++) {
		curNode = prevNodeArray[i];
		curNode.style.borderWidth = prevBorderWidthArray[i];
		curNode.style.borderColor = prevBorderColorArray[i];
		curNode.style.borderStyle = prevBorderStyleArray[i];
		curNode.style.backgroundColor = prevBackgroundColorArray[i];
		curNode.style.color = prevColorArray[i];
		if (curNode.nodeName.match(/IMG/i) != null) {
			curNode.style.filter = prevFilterArray[i];
		}
	}
	prevNodeArray = new Array();
	prevBorderWidthArray = new Array();
	prevBorderColorArray = new Array();
	prevBorderStyleArray = new Array();
	prevBackgroundColorArray = new Array();
	prevColorArray = new Array();
	prevFilterArray = new Array();
}

function _scrollToVisible(node) {
  var left=0, top=0;
  var width=node.offsetWidth;
  var height=node.offsetHeight;

  if ((node.tagName.toLowerCase() == "area") || (node.tagName.toLowerCase() == "map")) return;

  while( node!=document.body )
	{
	  top+=node.offsetTop;
	  left+=node.offsetLeft;
	  node = node.offsetParent;
	}
	
  var ele;
  if(document.documentElement.clientHeight==0){	
	ele=node;		
  }else{
	ele=document.documentElement;
  }
  if((ele.scrollLeft>left)||(ele.scrollLeft<(left+width-ele.clientWidth))){
	ele.scrollLeft=left;	
  }
  
  ele.scrollTop=top;					
}



// popup from here

function moveDivTo(div,left,top){
	//div.style.pixelLeft=left;
	div.style.left=left+'px';
	//div.style.pixelTop =top;
	div.style.top =top+'px';
	return;
}
function getEventPageX(e){
	//return document.body.scrollLeft+event.clientX;
	if(document.body.scrollLeft>document.documentElement.scrollLeft){
		return document.body.scrollLeft+e.clientX;
	}else{
		return document.documentElement.scrollLeft+e.clientX;
	}
}
function getEventPageY(e){
	//return document.body.scrollTop+event.clientY;
	if(document.body.scrollTop>document.documentElement.scrollTop){
		return document.body.scrollTop+e.clientY;
	}else{
		return document.documentElement.scrollTop+e.clientY;
	}
}

var balloonDiv=null; 
var messageDiv=null; 

function updateBaloon(curId) {
	if(messageDiv != null){
		messageDiv.style.visibility = "visible";
		messageDiv.innerHTML = "<b>&nbsp";
		messageDiv.innerHTML += id2time[curId];
		messageDiv.innerHTML += "</b> seconds from top.";

		if (id2time[curId] > 90) {
			messageDiv.innerHTML += "<img src='" + baseUrl +acc_imageDir+ "face-sad.gif'>";						
		} else if (id2time[curId] > 30) { 
			messageDiv.innerHTML += "<img src='" + baseUrl +acc_imageDir+ "face-usual.gif'>";
		} else {
			messageDiv.innerHTML += "<img src='" + baseUrl +acc_imageDir+ "face-smile.gif'>";
		}
		if (id2comment[curId].length > 0) {
			messageDiv.innerHTML += "<br><b>" + id2comment[curId] + "</b>";
		}
	}
}

function updateBaloon2(curId, message) {
  if(messageDiv != null){
	messageDiv.style.visibility = "visible";
	messageDiv.innerHTML = "<b>&nbsp";
	messageDiv.innerHTML += id2time[curId];
	messageDiv.innerHTML += "</b> seconds from top.";

	if (id2time[curId] > 90) {
	  messageDiv.innerHTML += "<img src='" + baseUrl +acc_imageDir+ "face-sad.gif'>";		

				
	} else if (id2time[curId] > 30) { 
	  messageDiv.innerHTML += "<img src='" + baseUrl +acc_imageDir+ "face-usual.gif'>";
	} else {
	  messageDiv.innerHTML += "<img src='" + baseUrl +acc_imageDir+ "face-smile.gif'>";
	}
	messageDiv.innerHTML += "<br><b>" + message + "</b>";

  }
}

function stopHandler(e){

	//var e = window.event;
	set_panel();
		if (e.target != null) {
			if ((servletMode == false ) || getEventPageY(e)<document.getElementById('suwen').offsetTop) {
				if (id2time[e.target.id] != null) {
					updateBaloon(e.target.id);
				}
			} 
	}

}

function turnonHandler(){
	var e = window.event;
	document.onmouseup=movingHandler;
	document.onmousemove=movingHandler;
	document.onmousedown=turnoffHandler;

	return false;
}

function init(){
	if(baloonSwitch == 1){
		balloonDiv=document.getElementById('balloon');    
		messageDiv=document.getElementById('message');    
		if( balloonDiv != null && messageDiv!=null){
			document.onmouseup=movingHandler;
			document.onmousemove=movingHandler;
			document.onmousedown=turnoffHandler;			
			//window.onmousemove=movingHandler;
		}
	} 
	document.onclick=cancelLink;
}

function movingHandler(e){
	//var e = window.event;
	//window.event.returnValue = false;
	set_panel();
	if(servletMode == false ){	
		if (e.target != null) {
			//alert(id2time[e.target.id]);
			if (id2time[e.target.id] != null) {
				updateBaloon(e.target.id);
			}
		} 	

		if(start_switch==true){
			if(balloonDiv.style.width<250)
			{
				balloonDiv.style.width=250;
			}
			moveDivTo(balloonDiv,getEventPageX(e)+20,getEventPageY(e));
		}
	}
}

function turnoffHandler(){
	init();
	document.onmousemove=stopHandler;
	document.onmousedown=turnonHandler;
	return false;
}

function cancelMapLink(event) {
	event.returnValue = false;
	cancelLink(event);
}
function cancelLink(e) {
	//var e = window.event;
	var nodes =e.target;
	set_panel();
	//window.event.returnValue = false;
	while(  nodes != null && nodes.tagName != null){
			if( nodes.tagName.toLowerCase() == "img") {
				if(nodes.getAttribute('src').toLowerCase().indexOf("jump.gif") > 0 
					|| nodes.getAttribute('src').toLowerCase().indexOf("dest.gif") > 0){
					alt=nodes.getAttribute('alt');
					destName=alt.substring(alt.lastIndexOf(":")+2);
					if(nodes.getAttribute('src').toLowerCase().indexOf("jump.gif") > 0){
						sourceName=nodes.getAttribute('name');
					} else{
						sourceName=null;
					}
					if(sourceName==null){
						for (var i=0;i<arrayLine.length;i++){
							if(arrayLine[i]["destName"]==destName){
								search_Line(arrayLine[i]["Start"],arrayLine[i]["End"],i);
							}
						}
						break;
					} else {
						for (var i=0;i<arrayLine.length;i++){
							if(arrayLine[i]["destName"]==destName && arrayLine[i]["sourceName"]==sourceName){					
								search_Line(arrayLine[i]["Start"],arrayLine[i]["End"],i);
							}
						}
						break;
					}


				}
			}

			nodes=nodes.parentNode;
			node_Id=nodes.getAttribute('id');
			if( node_Id!=null && !isNaN(parseInt(node_Id)) && node_Id>=0 && node_Id<idNumber) {
				for (var i=0;i<arrayLine.length;i++){
					if(arrayLine[i]["Start"]<=node_Id
						&& arrayLine[i]["End"]>=node_Id){
						search_Line(arrayLine[i]["Start"],arrayLine[i]["End"],i);
						break;
					}
				}
			}
			if(nodes!= null && nodes.firstChild != null){
				if( nodes.firstChild.tagName !=null && nodes.firstChild.tagName.toLowerCase() == "img"){
					if(nodes.firstChild.getAttribute('src').toLowerCase().indexOf("jump.gif") > 0 
						|| nodes.firstChild.getAttribute('src').toLowerCase().indexOf("dest.gif") > 0){
						alt=nodes.firstChild.getAttribute('alt');
						destName=alt.substring(alt.lastIndexOf(":")+2);
						if(nodes.firstChild.getAttribute('src').toLowerCase().indexOf("jump.gif") > 0){
							sourceName=nodes.firstChild.getAttribute('name');
						} else{
							sourceName=null;
						}
						if(sourceName==null){
							for (var i=0;i<arrayLine.length;i++){
								if(arrayLine[i]["destName"]==destName){
									search_Line(arrayLine[i]["Start"],arrayLine[i]["End"],i);
								}
							}
							break;
						} else {
							for (var i=0;i<arrayLine.length;i++){
								if(arrayLine[i]["destName"]==destName && arrayLine[i]["sourceName"]==sourceName){
									search_Line(arrayLine[i]["Start"],arrayLine[i]["End"],i);
								}
							}
							break;
						}

					}
				}
				break;

			}


	}
}

function search_Jump() {
	var i=0;
	var dest;
	var alt;
	init();
	est =document.getElementById('test');
//	est.style.visibility = "visible";
	est.style.visibility = "hidden";
	est.innerHTML = "<img src='" + acc_imageDir+"line_filled.gif'>";
	var x1=arrayLine.length;

	if(refresh==true){
		if(idNumber<1500||confirm("This process may take 30 seconds or more. Would you like to continue it?")==1){
			for (var h=0;h<x1;h++){
				var su=arrayLine[h]["sourceName"];
				nodelist=document.getElementsByName(su);
				node=nodelist[0];
				looplink_Jump(node,arrayLine[h]["destName"]);
			}
		}
	} else {
		while(true){
			if( document.getElementsByTagName("img")[i]==null){
				break;
			}

			if(document.getElementsByTagName("img")[i].getAttribute('src').toLowerCase().indexOf("jump.gif") > 0){
				alt=document.getElementsByTagName("img")[i].getAttribute('alt');
				dest=alt.substring(alt.indexOf(":")+2);
				looplink_Jump(document.getElementsByTagName("img")[i],dest);
			}
			i=i+1;
			
			if(isAlert==true && idNumber>3000){
				alert("This page contains too many intra-page links.\nIn this page, ACTF can visualize some of intra-page links by arrow.");
				break;
			}

		}
	}
} 

function looplink_Jump(nodeImg,z2) {
				x1=0;
				x2=0;
				y1=0;
				y2=0;

				var node=nodeImg;

//				while( node!=document.body){
				while( node!=null){
					if(node.nodeName !=null && node.nodeName!="TR"){
						if(!isNaN(node.offsetLeft)){
							x1 =x1+node.offsetLeft;
						}
						if(!isNaN(node.offsetTop)){
							y1 =y1+node.offsetTop;
						}
					}
					node = node.offsetParent;
				}

				nodelist=document.getElementsByName(z2);
				if(nodelist.length>1){
					var finded=false;					
					for(var k=0;k<nodelist.length;k++){
						var cur=nodelist[k].firstChild;
						while(cur!=null){
							if(cur.nodeName.toLowerCase()=="img"){
								node=nodelist[k];
								finded=true;
								break;
							}
							cur=cur.nextSibling;
						}
					
						if(finded==true){
							break;
						}
					}
				} else {
					node=nodelist[0];
				}
//				while( node!=document.body){
				while( node!=null){
					if( node.nodeName !=null && node.nodeName!="TR"){
						if(!isNaN(node.offsetLeft)){
							x2 =x2+node.offsetLeft;
						} 
						if(!isNaN(node.offsetTop)){
							y2 =y2+node.offsetTop;
						}
					}
					node = node.offsetParent;
				}
				//alert(x1+","+y1);
				//alert(x2+","+y2);

				var x3=x1-x2;

				var copynode;
				var lPointx;
				var lPointy;
				var rPointx;
				var rPointy;
				var angle;
				if(x2<5 && y2<5){
					x2=20;
					y2=20;
				}
				if (arrayLine[arrayIdex]==null){
					arrayLine[arrayIdex]=new Array();
				}
				arrayLine[arrayIdex]["sourceName"]=nodeImg.getAttribute('name');
				arrayLine[arrayIdex]["destName"]=z2;

				arrayLine[arrayIdex]["Start"]=idNumber;
				if(refresh==false){
					arrayLine[arrayIdex]["Type"]="hidden";
				}
				idLine=0;
				writeLine(x1+5,y1+5,x2,y2,0,50,arrayLine[arrayIdex]["Type"]);
				angle_Value=angleValue/57.3;
				if(y2>y1){
					angle=Math.atan((y2-y1)/x3);
					if(angle<0){
						angle=angle+180/57.3;
					}
					lPointx=x2+Math.cos(angle+angle_Value)*alength;
					lPointy=y2-Math.sin(angle+angle_Value)*alength;
					rPointx=x2+Math.cos(angle-angle_Value)*alength;
					rPointy=y2-Math.sin(angle-angle_Value)*alength;
					writeLine(lPointx,lPointy,x2,y2,font_Size,5,arrayLine[arrayIdex]["Type"]);
					writeLine(rPointx,rPointy,x2,y2,font_Size,5,arrayLine[arrayIdex]["Type"]);
				} else {
					angle=Math.atan(Math.abs(x3/(y2-y1)));

					if(x2<x1){
						lPointy=y2+Math.cos(angle-angle_Value)*alength;
						lPointx=x2+Math.sin(angle-angle_Value)*alength;
						rPointy=y2+Math.cos(angle+angle_Value)*alength;
						rPointx=x2+Math.sin(angle+angle_Value)*alength;
					} else {
						lPointy=y2+Math.cos(angle+angle_Value)*alength;
						lPointx=x2-Math.sin(angle+angle_Value)*alength;
						rPointy=y2+Math.cos(angle-angle_Value)*alength;
						rPointx=x2-Math.sin(angle-angle_Value)*alength;
					}

					writeLine(lPointx,lPointy,x2,y2,font_Size,5,arrayLine[arrayIdex]["Type"]);
					writeLine(rPointx,rPointy,x2,y2,font_Size,5,arrayLine[arrayIdex]["Type"]);
				}
				arrayLine[arrayIdex]["End"]=idNumber-1;
				arrayLine[arrayIdex]["Valid"]=idLine;
				arrayIdex=arrayIdex+1;
//				if(refresh==true){
//
//				}


}

function writeLine(sx,sy,dx,dy,font,leap,visibile){
		var y=dy-sy;
		var x=dx-sx;
		
		var abs_Value;
		abs_Value=(Math.abs(y)>Math.abs(x))? Math.abs(y):Math.abs(x);

		for(var i=0;i<abs_Value;i=i+leap){
			if(refresh==false){
				if( idNumber<1000 ){
					copynode=est.cloneNode(true);
					copynode.setAttribute("id", idNumber);
					document.body.appendChild(copynode);
					moveDivTo(copynode,sx+Math.round(x/abs_Value*i),sy+Math.round(i*(y/abs_Value)));					
					idLine+=1;
							
				} else {
					arrayPoint[idNumber]=new Array();
					arrayPoint[idNumber]["X"]=sx+Math.round(x/abs_Value*i);
					arrayPoint[idNumber]["Y"]=sy+Math.round(i*(y/abs_Value));
				}
				idNumber += 1;
			} else {
				if(idNumber<1000){
					var est2 =document.getElementById(idNumber);
					est2.style.visibility =visibile;
					moveDivTo(est2,sx+Math.round(x/abs_Value*i),sy+Math.round(i*(y/abs_Value)));
					idLine+=1;
				} else {
					arrayPoint[idNumber]=new Array();
					arrayPoint[idNumber]["X"]=sx+Math.round(x/abs_Value*i);
					arrayPoint[idNumber]["Y"]=sy+Math.round(i*(y/abs_Value));
				}
				idNumber += 1;
			}
		}

}
function search_Line(Start,End,Line) {
		var type;
		var valid_count;
		var visable;
		if(arrayLine[Line]["Type"]=="hidden"){
			visable= "visible";
		} else {
			visable= "hidden";
		}

		valid_count=arrayLine[Line]["Valid"];
		if(refresh==true){
			visable= arrayLine[Line]["Type"];
		}
		arrayLine[Line]["Type"]=visable;
		var valid_start=Start+valid_count;
		for (var j=Start;j<=End;j++){
			if(j<valid_start){
				est1 =document.getElementById(j);
				est1.style.visibility = visable;
			} else {
				copynode=est.cloneNode(true);
				copynode.setAttribute("id", j);
				copynode.style.visibility = visable;
				document.body.appendChild(copynode);
				moveDivTo(copynode,arrayPoint[j]["X"],arrayPoint[j]["Y"]);
				valid_count+=1;
			}
		}
		arrayLine[Line]["Valid"]=valid_count;
} 

function set_panel() {
		if(balloonDiv == null)
			return;
		//var py=document.body.scrollTop+document.body.clientHeight-150;
		//var by=document.body.scrollTop+document.body.clientHeight-balloonDiv.offsetHeight-150;
		var px,bx,py,by;
		var bodyHeight=document.body.scrollTop+document.body.clientHeight;
		var eleHeight=document.documentElement.scrollTop+document.documentElement.clientHeight;
		
		if(document.body.scrollTop>document.documentElement.scrollTop){
			px=document.body.scrollLeft+document.body.clientWidth-40;
			bx=document.body.scrollLeft+document.body.clientWidth-160;
			py=bodyHeight-150;
			by=bodyHeight-balloonDiv.offsetHeight-150;
		}else if(document.body.scrollTop<document.documentElement.scrollTop){
			px=document.documentElement.scrollLeft+document.documentElement.clientWidth-40;
			bx=document.documentElement.scrollLeft+document.documentElement.clientWidth-160;
			py=eleHeight-150;
			by=eleHeight-balloonDiv.offsetHeight-150;
		}else{		
			if(eleHeight<bodyHeight && eleHeight!=0){
				px=document.documentElement.scrollLeft+document.documentElement.clientWidth-40;
				bx=document.documentElement.scrollLeft+document.documentElement.clientWidth-160;
				py=eleHeight-150;
				by=eleHeight-balloonDiv.offsetHeight-150;
			}else{
				px=document.body.scrollLeft+document.body.clientWidth-40;
				bx=document.body.scrollLeft+document.body.clientWidth-160;
				py=bodyHeight-150;
				by=bodyHeight-balloonDiv.offsetHeight-150;
			}

		}
		
		if( servletMode == true ){
			var su_Y=document.getElementById('suwen').offsetTop;
			if(py>(su_Y-150)){
				py=su_Y-150;
				by=su_Y-balloonDiv.offsetHeight-150;
			}
		}
		var div=document.getElementById('control_pane');
		if(div.style.width<29){
			div.style.width=29;
		}
		moveDivTo(div,document.body.scrollLeft+document.body.clientWidth-40,py);		
		if(start_switch==false){
			moveDivTo(balloonDiv,document.body.scrollLeft+document.body.clientWidth-160,by);
		}
} 

function refresh_Jump(){
		if( idNumber<1500 || confirm("This process may take 30 seconds or more. Would you like to continue it?")==1){

			idNumber_Old=idNumber;
			idNumber=0;
			arrayIdex=0;
			arrayPoint=new Array();
			refresh=true;
			for (var i=0;i<arrayLine.length;i++){
				var start_value;
				var delte_value;
				if(arrayLine[i]["End"]>=1000){
					start_value=arrayLine[i]["Start"];
					start_value=(start_value>1000)? start_value:1000;
					delte_value=arrayLine[i]["Start"]+arrayLine[i]["Valid"];
					for (var k=start_value;k<delte_value;k++){
						document.body.removeChild(document.getElementById(k));
					}
				}
			}
			search_Jump();
			for (var i=0;i<arrayLine.length;i++){
				if(arrayLine[i]["Type"]=="visible"){
					search_Line(arrayLine[i]["Start"],arrayLine[i]["End"],i);
				}
			}
			if(idNumber_Old<1000){
				for (var i=idNumber;i<idNumber_Old;i++){
					var est5 =document.getElementById(i);
					if(est5 !=null){
						est5.style.visibility ="hidden";
					}
				}
			}
			refresh=false;
		}
}
function control_moving(){
	var node =document.getElementById('control_pane').firstChild;

	if(start_switch==false){
		while(node!=null){		
			if(node.src!=null && node.src.indexOf("move.gif")>=0){
				node.src=acc_imageDir+"stop.gif";
				start_switch=true;
				break;
			}
			node=node.nextSibling;
		}
	} else {
		while(node!=null){
			if(node.src!=null && node.src.indexOf("stop.gif")>=0){
				node.src=acc_imageDir+"move.gif";
				start_switch=false;
				break;
			}
			node=node.nextSibling;
		}
	}
}
function clean_Line() {
	if( idNumber<1500 || confirm("This process may take 30 seconds or more. Would you like to continue it?")==1){

		for (var i=0;i<arrayLine.length;i++){
			if(arrayLine[i]["Type"] != "hidden"){
				for (var j=arrayLine[i]["Start"];j<=arrayLine[i]["End"];j++){
					est1 =document.getElementById(j);
					est1.style.visibility ="hidden";
				} 
			arrayLine[i]["Type"]= "hidden";
			}
		}
	}

} 
function draw_all_Line() {
		var node;
		var copynode;

		if( idNumber<1500 || confirm("This process may take 30 seconds or more. Would you like to continue it?")==1){
			for (var i=0;i<arrayLine.length;i++){

				if(arrayLine[i]["Type"] == "hidden"){
					var valid=arrayLine[i]["Valid"];
					var start_point=arrayLine[i]["Start"];
					var end_point=arrayLine[i]["End"];
					var valid_start=start_point+valid;
					for (var j=start_point;j<=end_point;j++){
						if(j<valid_start){
							node =document.getElementById(j);
							node.style.visibility ="visible";
						} else {
							copynode=est.cloneNode(true);
							copynode.setAttribute("id", j);
							copynode.style.visibility = "visible";
							document.body.appendChild(copynode);
							moveDivTo(copynode,arrayPoint[j]["X"],arrayPoint[j]["Y"]);
							valid+=1;
						}

					} 
					arrayLine[i]["Type"]= "visible";
					arrayLine[i]["Valid"]=valid;
				}
			}
		}
} 

window.onload=search_Jump;
window.onresize=set_panel;


function highlightTr( _tr ){
  clearTrHighlight();
  _tr.style.backgroundColor = HIGHLIGHT_BGCOLOR;
}

function clearTrHighlight(){
  //var allTR = document.body.all.tags( "TR" );
  allTR=document.getElementsByTagName("tr");
  for(var i=0; i<allTR.length; i++ ){
  if(allTR[i].style.backgroundColor!="rgb(255, 255, 255)"&&allTR[i].style.backgroundColor!="")
    if( allTR[i].style.backgroundColor == HIGHLIGHT_BGCOLOR ){
      allTR[i].style.backgroundColor = "transparent";
    }
  }
}
