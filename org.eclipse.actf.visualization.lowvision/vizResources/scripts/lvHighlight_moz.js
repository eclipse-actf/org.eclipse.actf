/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/ 

var mother = null;
var simImg = null;
var coverDiv = document.createElement( "DIV" );
var curTRTop = 0;
var BORDER_WIDTH = 4;
//var HIGHLIGHT_BGCOLOR = "#ffdddd";
var HIGHLIGHT_BGCOLOR = "rgb(255, 221, 221)";
function onloadFunc(){
  //mother = document.body.all( "mother" );
  mother = document.getElementById( "mother" );
  //simImg = mother.all( "simImg" );
  simImg = document.getElementById( "simImg" );
  simOffLeft = simImg.offsetLeft;
  simOffTop = simImg.offsetTop;
 
  for( i=0; i<divArray.length; i++ ){
    if( divLeft[i] < 0 ){ divLeft[i] = 0; }
    if( divTop[i] < 0 ){ divTop[i] = 0; }
    if( divWidth[i] < 0 ){ divWidth[i] = 0; }
    if( divHeight[i] < 0 ){ divHeight[i] = 0; }

    divArray[i] = document.createElement( "DIV" );
    divArray[i].style.position = "absolute";
    divArray[i].style.left = divLeft[i] + simOffLeft;
    divArray[i].style.top = divTop[i] + simOffTop;
    divArray[i].style.width = divWidth[i];
    divArray[i].style.height = divHeight[i];
    divArray[i].style.borderStyle = "groove";
    divArray[i].style.borderColor = "white";
    divArray[i].style.borderWidth = BORDER_WIDTH;
    divArray[i].style.zIndex = 10;
    divArray[i].style.fontSize = "0pt";
    divArray[i].style.lineHeight = 0;
    divArray[i].style.display = "none";
    mother.appendChild( divArray[i] );
    sLeft[i] = divLeft[i] + simOffLeft;
    sTop[i] = divTop[i] + simOffTop;
  }
}

function DIVOnclickFunc(){
  document.body.scrollLeft = 0;
  document.body.scrollTop = curTRTop;
}

function TROnclickFunc( _tr ){
  //window.event.cancelBubble = true; 
  clearDiv(); 
  showDiv( _tr.id );
  _tr.style.backgroundColor = HIGHLIGHT_BGCOLOR;

  curTRTop = 0;
  for( e=_tr; e!=document.body; e=e.offsetParent ){
    curTRTop += e.offsetTop;
  }
}

function clearDiv(){
  //var allTR = document.body.all.tags( "TR" );
  allTR=document.getElementsByTagName("tr");
  for( i=0; i<allTR.length; i++ ){
    if( allTR[i].style.backgroundColor == HIGHLIGHT_BGCOLOR ){
      allTR[i].style.backgroundColor = "transparent";
    }
  }
  coverDiv.style.width = 0;
  coverDiv.style.height = 0;
  for( i=0; i<divArray.length; i++ ){
    divArray[i].style.display = "none";
    divArray[i].onclick = null;
  }
}

function showDiv( _id ){
  divArray[_id].style.display = "block";
  coverDiv.style.left = 0;
  coverDiv.style.top = 0;
  var coverDivWidth = divWidth[_id]-BORDER_WIDTH*2;
  if( coverDivWidth > 0 ){ coverDiv.style.width = coverDivWidth; }
  else{ coverDivWidth = 0; }
  var coverDivHeight = divHeight[_id]-BORDER_WIDTH*2;
  if( coverDivHeight > 0 ){ coverDiv.style.height = coverDivHeight; }
  else{ coverDivHeight = 0; }
  coverDiv.style.fontSize = "0pt";
  coverDiv.style.lineHeight = 0;
  coverDiv.style.zIndex = 20;
  //coverDiv.style.backgroundColor = "red";
  var finX = divWidth[_id]-BORDER_WIDTH;
  if( finX < 0 ){ finX = 0; }
  var finY = divHeight[_id]-BORDER_WIDTH;
  if( finY < 0 ){ finY = 0; }
  coverDiv.style.filter = "alpha(opacity=0,finishopacity=0,style=0,startx=" + BORDER_WIDTH + ",starty=" + BORDER_WIDTH + ",finishx=" + finX + ",finishy=" + finY + ")";
  divArray[_id].appendChild( coverDiv );
  divArray[_id].onclick = DIVOnclickFunc;
  document.body.scrollLeft = sLeft[_id];
  document.body.scrollTop = sTop[_id];
}
