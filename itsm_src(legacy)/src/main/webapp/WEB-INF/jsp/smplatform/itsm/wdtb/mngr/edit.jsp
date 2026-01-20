<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator"
	uri="http://www.springmodules.org/tags/commons-validator"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:set var="registerFlag"
	value="${empty wdtbFormVO.wdtbVO.wdtbCnfirmNo ? 'create' : 'modify'}" />
<title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}" />
	<c:out value="${registerFlag == 'create' ? '등록' : '수정'}" /></title>
<!--For Commons Validator Client Side-->
<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<%--     <validator:javascript formName="srvcRsponsFormVO" staticJavascript="false" xhtml="true" cdata="false"/> --%>
<script type="text/javaScript" language="javascript" defer="defer">
	var solutConectflFileCnt = 0;
	var opertResultflFileCnt = 0;
	var loginResultflFileCnt = 0;
	var serverOneLogflFileCnt = 0;
	var serverTwoLogflFileCnt = 0;
	
	$(document).ready(function() {
		 var frm = document.listForm; 
		
		$("#wdtbDtDateDisplay").mask("9999-99-99");
		$("#wdtbDtOneDateDisplay").mask("9999-99-99");
		$("#wdtbDtOneTimeDisplay").mask("99:99");
		$("#wdtbDtTwoDateDisplay").mask("9999-99-99");
		$("#wdtbDtTwoTimeDisplay").mask("99:99");
		$('#solutConectflSendBtn').hide();
		$('#opertResultflSendBtn').hide();
		$('#loginResultflSendBtn').hide();
		$('#serverOneLogflSendBtn').hide();
		$('#serverTwoLogflSendBtn').hide();
		
        <c:if test="${registerFlag != 'modify'}">
	        $("#imprvmMatter").val($("input[name='funcImprvmVO.fiCn']").val());
	        $("#navigation").val($("input[name='funcImprvmVO.navigation']").val());
        </c:if>
        
        var url = "<c:url value='/itsm/atchmnfl/site/create.do'/>";
        $('#solutConectfl').fileupload({
      		 url: url,
      		 dataType: 'json',
      		 formData: {atchmnflId: $("#solutConectflId").val()},
      		 sequentialUploads: true,
      		 add: function (e, data) {
      			 var valid = true;
      			 var re = /^.+\.((jpg)|(gif)|(png)|(jpeg))$/i;
      			 
      			 $.each(data.files, function (index, file) {
     			        console.log('Added file: ' + file.name);
     			        console.log('Added file: ' + file.type);
     			        console.log('Added file: ' + file.size);
     			        console.log('Added file: ' + filesize(file.size));
//      			    console.log('file size: ' + data.getNumberOfFiles());
     			        
     			        solutConectflFileCnt++;
     			        
     			     	if (!re.test(file.name)) {
     			     		valid = false;
     			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
     			     		solutConectflFileCnt--;
     			     	}else if (file.size > 5000000){ //5M
     			     	    console.log('5M이하의 파일만 허용 합니다.');
     			     		alert(file.name+' <br/>5M이상의 첨부파일은 제외됩니다.');
     			     		solutConectflFileCnt--;
     			     		valid = false;
     			     	}else if($('#solutConectflListDiv li').length > 0){
     			     		console.log('1개의 파일만 첨부 가능합니다.');
     			     		alert(file.name+' <br/>1개의 파일만 첨부 가능합니다.');
     			     	}else if (solutConectflFileCnt>1){ //동시 업로드 1개 제한
         			     	console.log('1개의 파일만 허용 합니다.'+solutConectflFileCnt);
     			     		alert(file.name+' <br/>동시에 1개의 파일만 첨부 허용 합니다.');
     			     		solutConectflFileCnt--;
     			     		valid = false;
     			     	}else{
     			     		console.log('정상등록 '+solutConectflFileCnt);
     			     		var abortBtn = $( '<a/>' )
   		                     .attr( 'href', 'javascript:void(0)' )
   		                     .attr( 'class', 'close' )
   		                     .attr( 'aria-label', 'Close' )
   		                     .append( '<span aria-hidden="true">&times;</span>' )
   		                     .click( function() {
   			                    	data.abort();
   			                        data.context.remove();
   			                        data = null;
   			                        if(solutConectflFileCnt>0){						                        	
   			                        	solutConectflFileCnt--;
   			                        	console.log('solutConectflFileCnt: '+solutConectflFileCnt);
   			                        }
   		                     } );
     			     		
       			         data.context = $( '<div/>' ).appendTo($('#solutConectflListDiv'));
         			         data.context.attr( 'class', 'form-row' );
         			         data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
     			     	}
     			        
     			     });
      			 
      			 $("#solutConectflSendBtn").click(function () {        				   
      			        // validate file...        			        
      			        if(valid && data != null){
      			        	data.submit();
      			        	//해당 폼 전송 추가 하면 저장 버튼 완료
      			        }
      			            
      			  });
      		 },
      		 done: function (e, data) {
      			 console.log('done: '+data.errorThrown);
      			 console.log('done: '+data.textStatus);
      			 console.log('done: '+data.jqXHR);
      		 },
      		 progressall: function (e, data) {
      			if(opertResultflFileCnt > 0){
	   		    	$("#opertResultflSendBtn").trigger("click");
	      		}else if(loginResultflFileCnt>0){
	            	$("#loginResultflSendBtn").trigger("click");
	            }else if(serverOneLogflFileCnt>0){
	            	$("#serverOneLogflSendBtn").trigger("click");
	            }else if(serverTwoLogflFileCnt>0){
	            	$("#serverTwoLogflSendBtn").trigger("click");
	            }else{
	    	        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/wdtb/mngr/create.do' : '/itsm/wdtb/mngr/update.do'}"/>";
	    	        frm.submit();	
	            }
      		 },
      		    progressServerRate: 0.3

      		});
        
        $('#opertResultfl').fileupload({
      		 url: url,
      		 dataType: 'json',
      		 formData: {atchmnflId: $("#opertResultflId").val()},
      		 sequentialUploads: true,
      		 add: function (e, data) {
      			 var valid = true;
      			 var re = /^.+\.((jpg)|(gif)|(png)|(jpeg))$/i;
      			 
      			 $.each(data.files, function (index, file) {
     			        console.log('Added file: ' + file.name);
     			        console.log('Added file: ' + file.type);
     			        console.log('Added file: ' + file.size);
     			        console.log('Added file: ' + filesize(file.size));
//      			    console.log('file size: ' + data.getNumberOfFiles());
     			        
     			        opertResultflFileCnt++;
     			        
     			     	if (!re.test(file.name)) {
     			     		valid = false;
     			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
     			     		opertResultflFileCnt--;
     			     	}else if (file.size > 5000000){ //5M
     			     	    console.log('5M이하의 파일만 허용 합니다.');
     			     		alert(file.name+' <br/>5M이상의 첨부파일은 제외됩니다.');
     			     		opertResultflFileCnt--;
     			     		valid = false;
     			     	}else if (opertResultflFileCnt>5){ //동시 업로드 5개 제한
         			     	console.log('5개의 파일만 허용 합니다.'+opertResultflFileCnt);
     			     		alert(file.name+' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
     			     		opertResultflFileCnt--;
     			     		valid = false;
     			     	}else{
     			     		console.log('정상등록 '+opertResultflFileCnt);
     			     		var abortBtn = $( '<a/>' )
   		                     .attr( 'href', 'javascript:void(0)' )
   		                     .attr( 'class', 'close' )
   		                     .attr( 'aria-label', 'Close' )
   		                     .append( '<span aria-hidden="true">&times;</span>' )
   		                     .click( function() {
   			                    	data.abort();
   			                        data.context.remove();
   			                        data = null;
   			                        if(opertResultflFileCnt>0){						                        	
   			                        	opertResultflFileCnt--;
   			                        	console.log('opertResultflFileCnt: '+opertResultflFileCnt);
   			                        }
   		                      } );
     			     		
       			         	data.context = $( '<div/>' ).appendTo($('#opertResultflListDiv'));
         			        data.context.attr( 'class', 'form-row' );
         			        data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
     			     	}
     			        
     			     });
      			 
      			 $("#opertResultflSendBtn").click(function () {        				   
      			        // validate file...        			        
      			        if(valid && data != null){
      			        	data.submit();
      			        	//해당 폼 전송 추가 하면 저장 버튼 완료
      			        }
      			            
      			    });
      		 },
      		 done: function (e, data) {
      			 console.log('done: '+data.errorThrown);
      			 console.log('done: '+data.textStatus);
      			 console.log('done: '+data.jqXHR);
      		 },
      		 progressall: function (e, data) {
      			if(loginResultflFileCnt>0){
	            	$("#loginResultflSendBtn").trigger("click");
	            }else if(serverOneLogflFileCnt>0){
	            	$("#serverOneLogflSendBtn").trigger("click");
	            }else if(serverTwoLogflFileCnt>0){
	            	$("#serverTwoLogflSendBtn").trigger("click");
	            }else{
	    	        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/wdtb/mngr/create.do' : '/itsm/wdtb/mngr/update.do'}"/>";
	    	        frm.submit();	
	            }
      		 },
      		    progressServerRate: 0.3

      		});
        
        $('#loginResultfl').fileupload({
      		 url: url,
      		 dataType: 'json',
      		 formData: {atchmnflId: $("#loginResultflId").val()},
      		 sequentialUploads: true,
      		 add: function (e, data) {
      			 var valid = true;
      			 var re = /^.+\.((jpg)|(gif)|(png)|(jpeg))$/i;
      			 
      			 $.each(data.files, function (index, file) {
     			        console.log('Added file: ' + file.name);
     			        console.log('Added file: ' + file.type);
     			        console.log('Added file: ' + file.size);
     			        console.log('Added file: ' + filesize(file.size));
//      			    console.log('file size: ' + data.getNumberOfFiles());
     			        
     			        loginResultflFileCnt++;
     			        
     			     	if (!re.test(file.name)) {
     			     		valid = false;
     			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
     			     		loginResultflFileCnt--;
     			     	}else if (file.size > 5000000){ //5M
     			     	    console.log('5M이하의 파일만 허용 합니다.');
     			     		alert(file.name+' <br/>5M이상의 첨부파일은 제외됩니다.');
     			     		loginResultflFileCnt--;
     			     		valid = false;
     			     	}else if($('#loginResultflListDiv li').length > 0){
     			     		console.log('1개의 파일만 첨부 가능합니다.');
     			     		alert(file.name+' <br/>1개의 파일만 첨부 가능합니다.');
     			     	}else if (loginResultflFileCnt>1){ //동시 업로드 1개 제한
         			     	console.log('1개의 파일만 허용 합니다.'+loginResultflFileCnt);
     			     		alert(file.name+' <br/>동시에 1개의 파일만 첨부 허용 합니다.');
     			     		loginResultflFileCnt--;
     			     		valid = false;
     			     	}else{
     			     		console.log('정상등록 '+loginResultflFileCnt);
     			     		var abortBtn = $( '<a/>' )
   		                     .attr( 'href', 'javascript:void(0)' )
   		                     .attr( 'class', 'close' )
   		                     .attr( 'aria-label', 'Close' )
   		                     .append( '<span aria-hidden="true">&times;</span>' )
   		                     .click( function() {
   			                    	data.abort();
   			                        data.context.remove();
   			                        data = null;
   			                        if(loginResultflFileCnt>0){						                        	
   			                        	loginResultflFileCnt--;
   			                        	console.log('loginResultflFileCnt: '+loginResultflFileCnt);
   			                        }
   		                      } );
     			     		
       			         	data.context = $( '<div/>' ).appendTo($('#loginResultflListDiv'));
         			        data.context.attr( 'class', 'form-row' );
         			        data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
     			     	}
     			        
     			     });
      			 
      			 $("#loginResultflSendBtn").click(function () {        				   
      			        // validate file...        			        
      			        if(valid && data != null){
      			        	data.submit();
      			        	//해당 폼 전송 추가 하면 저장 버튼 완료
      			        }
      			            
      			    });
      		 },
      		 done: function (e, data) {
      			 console.log('done: '+data.errorThrown);
      			 console.log('done: '+data.textStatus);
      			 console.log('done: '+data.jqXHR);
      		 },
      		 progressall: function (e, data) {
      			if(serverOneLogflFileCnt>0){
	            	$("#serverOneLogflSendBtn").trigger("click");
	            }else if(serverTwoLogflFileCnt>0){
	            	$("#serverTwoLogflSendBtn").trigger("click");
	            }else{
	    	        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/wdtb/mngr/create.do' : '/itsm/wdtb/mngr/update.do'}"/>";
	    	        frm.submit();	
	            }
      		 },
      		    progressServerRate: 0.3

      		});
        
        $('#serverOneLogfl').fileupload({
     		 url: url,
     		 dataType: 'json',
     		 formData: {atchmnflId: $("#serverOneLogflId").val()},
     		 sequentialUploads: true,
     		 add: function (e, data) {
     			 var valid = true;
     			 var re = /^.+\.((jpg)|(gif)|(png)|(jpeg))$/i;
     			 
     			 $.each(data.files, function (index, file) {
    			        console.log('Added file: ' + file.name);
    			        console.log('Added file: ' + file.type);
    			        console.log('Added file: ' + file.size);
    			        console.log('Added file: ' + filesize(file.size));
//     			    console.log('file size: ' + data.getNumberOfFiles());
    			        
    			        serverOneLogflFileCnt++;
    			        
    			     	if (!re.test(file.name)) {
    			     		valid = false;
    			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
    			     		serverOneLogflFileCnt--;
    			     	}else if (file.size > 5000000){ //5M
    			     	    console.log('5M이하의 파일만 허용 합니다.');
    			     		alert(file.name+' <br/>5M이상의 첨부파일은 제외됩니다.');
    			     		serverOneLogflFileCnt--;
    			     		valid = false;
    			     	}else if($('#serverOneLogflListDiv li').length > 0){
     			     		console.log('1개의 파일만 첨부 가능합니다.');
     			     		alert(file.name+' <br/>1개의 파일만 첨부 가능합니다.');
     			     	}else if (serverOneLogflFileCnt>1){ //동시 업로드 1개 제한
        			     	console.log('1개의 파일만 허용 합니다.'+serverOneLogflFileCnt);
    			     		alert(file.name+' <br/>동시에 1개의 파일만 첨부 허용 합니다.');
    			     		serverOneLogflFileCnt--;
    			     		valid = false;
    			     	}else{
    			     		console.log('정상등록 '+serverOneLogflFileCnt);
    			     		var abortBtn = $( '<a/>' )
  		                     .attr( 'href', 'javascript:void(0)' )
  		                     .attr( 'class', 'close' )
  		                     .attr( 'aria-label', 'Close' )
  		                     .append( '<span aria-hidden="true">&times;</span>' )
  		                     .click( function() {
  			                    	data.abort();
  			                        data.context.remove();
  			                        data = null;
  			                        if(opertResultflFileCnt>0){						                        	
  			                        	serverOneLogflFileCnt--;
  			                        	console.log('serverOneLogflFileCnt: '+serverOneLogflFileCnt);
  			                        }
  		                      } );
    			     		
      			         	data.context = $( '<div/>' ).appendTo($('#serverOneLogflListDiv'));
        			        data.context.attr( 'class', 'form-row' );
        			        data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
    			     	}
    			        
    			     });
     			 
     			 $("#serverOneLogflSendBtn").click(function () {        				   
     			        // validate file...        			        
     			        if(valid && data != null){
     			        	data.submit();
     			        	//해당 폼 전송 추가 하면 저장 버튼 완료
     			        }
     			            
     			    });
     		 },
     		 done: function (e, data) {
     			 console.log('done: '+data.errorThrown);
     			 console.log('done: '+data.textStatus);
     			 console.log('done: '+data.jqXHR);
     		 },
     		 progressall: function (e, data) {
	  		    if(serverTwoLogflFileCnt > 0){
  		    		$("#serverTwoLogflSendBtn").trigger("click");
	  		    }else {
		  	        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/wdtb/mngr/create.do' : '/itsm/wdtb/mngr/update.do'}"/>";
		  	        frm.submit();	
	  		    }
     		 },
     		    progressServerRate: 0.3

     		});
        
        $('#serverTwoLogfl').fileupload({
     		 url: url,
     		 dataType: 'json',
     		 formData: {atchmnflId: $("#serverTwoLogflId").val()},
     		 sequentialUploads: true,
     		 add: function (e, data) {
     			 var valid = true;
     			 var re = /^.+\.((jpg)|(gif)|(png)|(jpeg))$/i;
     			 
     			 $.each(data.files, function (index, file) {
    			        console.log('Added file: ' + file.name);
    			        console.log('Added file: ' + file.type);
    			        console.log('Added file: ' + file.size);
    			        console.log('Added file: ' + filesize(file.size));
//     			    	console.log('file size: ' + data.getNumberOfFiles());
    			        
    			        serverTwoLogflFileCnt++;
    			        
    			     	if (!re.test(file.name)) {
    			     		valid = false;
    			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
    			     		serverTwoLogflFileCnt--;
    			     	}else if (file.size > 5000000){ //5M
    			     	    console.log('5M이하의 파일만 허용 합니다.');
    			     		alert(file.name+' <br/>5M이상의 첨부파일은 제외됩니다.');
    			     		serverTwoLogflFileCnt--;
    			     		valid = false;
    			     	}else if($('#serverTwoLogflListDiv li').length > 0){
     			     		console.log('1개의 파일만 첨부 가능합니다.');
     			     		alert(file.name+' <br/>1개의 파일만 첨부 가능합니다.');
     			     	}else if (serverTwoLogflFileCnt>1){ //동시 업로드 1개 제한
        			     	console.log('1개의 파일만 허용 합니다.'+serverTwoLogflFileCnt);
    			     		alert(file.name+' <br/>동시에 1개의 파일만 첨부 허용 합니다.');
    			     		serverTwoLogflFileCnt--;
    			     		valid = false;
    			     	}else{
    			     		console.log('정상등록 '+serverTwoLogflFileCnt);
    			     		var abortBtn = $( '<a/>' )
  		                     .attr( 'href', 'javascript:void(0)' )
  		                     .attr( 'class', 'close' )
  		                     .attr( 'aria-label', 'Close' )
  		                     .append( '<span aria-hidden="true">&times;</span>' )
  		                     .click( function() {
  			                    	data.abort();
  			                        data.context.remove();
  			                        data = null;
  			                        if(serverTwoLogflFileCnt>0){						                        	
  			                        	serverTwoLogflFileCnt--;
  			                        	console.log('serverTwoLogflFileCnt: '+serverTwoLogflFileCnt);
  			                        }
  		                      } );
    			     		
      			         	data.context = $( '<div/>' ).appendTo($('#serverTwoLogflListDiv'));
        			        data.context.attr( 'class', 'form-row' );
        			        data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
    			     	}
    			        
    			     });
     			 
     			 $("#serverTwoLogflSendBtn").click(function () {        				   
     			        // validate file...        			        
     			        if(valid && data != null){
     			        	data.submit();
     			        	//해당 폼 전송 추가 하면 저장 버튼 완료
     			        }
     			            
     			 });
     		 },
     		 done: function (e, data) {
     			 console.log('done: '+data.errorThrown);
     			 console.log('done: '+data.textStatus);
     			 console.log('done: '+data.jqXHR);
     		 },
     		 progressall: function (e, data) {
	  	        	frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/wdtb/mngr/create.do' : '/itsm/wdtb/mngr/update.do'}"/>";
	  	            frm.submit();	
     		 },
     		    progressServerRate: 0.3

     		});
        
	});
	
	function fncSave() {
    	frm = document.listForm; 
    	 
    	if(document.listForm.elements['wdtbVO.wdtbSe'].value == ''){
 			alert('배포구분을 선택하세요.');
			return;
 		}
    	if(document.listForm.elements['wdtbVO.wdtbIp'].value == ''){
    		alert('배포IP를 입력하세요.');
			return;
    	}
    	if(document.listForm.elements['wdtbVO.wdtbNoOne'].value == ''){
    		alert('운영#1의 배포NO를 입력하세요.');
			return;
    	}
    	if(document.listForm.elements['wdtbVO.wdtbCoOne'].value == ''){
    		alert('운영#1의 배포횟수를 입력하세요.');
			return;
    	}
    	if(document.listForm.elements['wdtbVO.opertReason'].value == ''){
 			alert('작업사유를 입력하세요.');
			return;
 		}
    	if(document.listForm.elements['wdtbVO.wdtbNavigation'].value == ''){
 			alert('네비게이션을 입력하세요.');
			return;
 		}
    	if(document.listForm.elements['wdtbVO.imprvmMatter'].value == ''){
 			alert('개선사항을 입력하세요.');
			return;
 		}
    	if($("#solutConectflListDiv li").length <1 && solutConectflFileCnt<1){
    		alert("솔루션접속 파일을 첨부해주세요.");
    		return;
    	}
    	if($("#opertResultflListDiv li").length <1 && opertResultflFileCnt<1){
    		alert("작업사항결과 파일을 첨부해주세요.");
    		return;
    	}
    	if($("#loginResultflListDiv li").length <1 && loginResultflFileCnt<1){
    		alert("로그인결과 파일을 첨부해주세요.");
    		return;
    	}
    	if($("#serverOneLogflListDiv li").length <1 && serverOneLogflFileCnt<1){
    		alert("서버Log#1 파일을 첨부해주세요.");
    		return;
    	}
    	
        //첨부파일 존재 유무 확인
        if(solutConectflFileCnt>0 && opertResultflFileCnt>0 && loginResultflFileCnt>0 && serverOneLogflFileCnt>0 && serverTwoLogflFileCnt>0){
        	$("#solutConectflSendBtn").trigger("click");	
        }else if(solutConectflFileCnt>0){
        	$("#solutConectflSendBtn").trigger("click");
        }else if(opertResultflFileCnt>0){
        	$("#opertResultflSendBtn").trigger("click");
        }else if(loginResultflFileCnt>0){
        	$("#loginResultflSendBtn").trigger("click");
        }else if(serverOneLogflFileCnt>0){
        	$("#serverOneLogflSendBtn").trigger("click");
        }else if(serverTwoLogflFileCnt>0){
        	$("#serverTwoLogflSendBtn").trigger("click");
        }else{
	        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/wdtb/mngr/create.do' : '/itsm/wdtb/mngr/update.do'}"/>";
	        frm.submit();	
        }
    }
	
	function fncDelete(){
	    confirm('삭제하시겠습니까?','기능개선 삭제', null, function(request){
			console.log('request: '+request);
			if(request){
				document.listForm.action = "<c:url value='/itsm/wdtb/mngr/delete.do'/>";
		        document.listForm.submit();
			}
		});
	 }
	
	 function fncDownloadFile(atchmnflId, atchmnflSn){
	    	document.downloadForm.action = "<c:url value='/itsm/atchmnfl/site/retrieve.do'/>";
	    	document.downloadForm.atchmnflId.value = atchmnflId;
	    	document.downloadForm.atchmnflSn.value = atchmnflSn;
	    	document.downloadForm.submit();
	 }
	 
	 function fncDeleteFile(atchmnflId, atchmnflSn){
	    	console.log('atchmnflId: '+atchmnflId);
	    	console.log('atchmnflSn: '+atchmnflSn);
	    	$('#atchmnflId').val(atchmnflId);
	    	$('#atchmnflSn').val(atchmnflSn);
	    	confirm('해당 파일이 즉시 삭제됩니다. 계속 진행하시겠습니까?','파일 삭제', null, function(request){
	    		console.log('request: '+request);
				if(request){
					$.ajax({
		    		    type:'POST',
		    		    url:"<c:url value='/itsm/atchmnfl/site/delete.do'/>",
		    		    dataType: "json",
		    			data : { 
		    				"atchmnflId":$('#atchmnflId').val(),
		    				"atchmnflSn":$('#atchmnflSn').val()
		    		   			},
		    			async:false,
		    			success : function(request){
		    				console.log('request.returnMessage: '+request.returnMessage);
		    				
		    				if(request.returnMessage == 'success'){
		    					console.log('delete: '+$('#fileId_'+$('#atchmnflId').val()+'_'+$('#atchmnflSn').val()));
		    					console.log('delete: '+'#fileId_'+$('#atchmnflId').val()+'_'+$('#atchmnflSn').val());
		    					$('#fileId_'+$('#atchmnflId').val()+'_'+$('#atchmnflSn').val()).remove(); 
		    				}
		    			},
		    			error:function(r){
		    				console.dir(r);
		    				alert("정보전송에 실패하였습니다.");
		    			}        			
		    		});
				}
	    	});
	 }
	 
	 function fncWdtbCnfirmDownload(){
		    var ozrFileNm = "/knfc/wdtbCnfirm.ozr";
			var odiFileNm = "/knfc/wdtbCnfirm.odi";
		    
		    procPrintList(ozrFileNm, odiFileNm);
	}
	 
	 /**
	 * 문자열에서 모든 교체할 문자열을 대체 문자열로 치환한다.
	 * @param pattnStr - 찾을 문자열
	 * @param chngStr - 대체 문자열
	 * @return 치환된 문자열
	 */
	String.prototype.replaceAll = function(pattnStr, chngStr) {
	
	    var retsult = "";
	    var trimStr = this;//.replace(/(^\s*)|(\s*$)/g, "");
	
	    if(trimStr && pattnStr != chngStr) {
	
	        retsult = trimStr;
	
	        while(retsult.indexOf(pattnStr) > -1) {
	            retsult = retsult.replace(pattnStr, chngStr);
	        }
	    }
	
	    return retsult;
	};
		
	var procPrintList = function(ozrFileNm, odiFileNm) {
		
	    var wdtbCnfirmNo = '<c:out value="${wdtbFormVO.wdtbVO.wdtbCnfirmNo}"/>';
		var itsmDomain = location.origin + "/";
		var wdtbDtDateDisplay = $("#wdtbDtDateDisplay").val();
	    var flName = wdtbCnfirmNo 
	    			+ "-솔루션배포확인서_"+wdtbDtDateDisplay.replaceAll("-","")
	    			+ "_"
	    			+ $("input[name='wdtbVO.chargerUserNm']").val();
		
	    var jsonData = "${jsonData }";
	    jsonData = jsonData.replaceAll("'", '"');
		
	    var ozparams = { "ozr" : ozrFileNm
	                   , "odi" : odiFileNm
	                   , "flName" : flName
	                   , "pcount" : 3       // pcount는 jsonData 가 있으므로 밑의 params 갯수보다 1+해서 적어준다.
	                   , "params" : [ {  id: "keyNo",     value: wdtbCnfirmNo},
	                	  			  {  id: "itsmDomain",     value: itsmDomain}]
	                   };
	    
	    var popUrl = "<c:url value='/itsm/rep/master/mngr/OzReportViewer.do'/>";
		
	    var $formOzReport = $( "<form action='" + popUrl + "' method='post', target='_blank' id='formOzReport' name='formOzReport'/>" );
	    var html = $("<input type='hidden' name='ozparams' value='"+JSON.stringify(ozparams)+"'/>"
	            +"<input type='hidden' name='jsonData' value='"+jsonData+"'/>");
		
	    $formOzReport.append(html);
		
	    $('body').append( $formOzReport );
	    $formOzReport.submit();
	};
    </script>
	<style type="text/css">
	.serverInfo {
		background-color: rgb(205, 205, 205);
		font-weight: bold;
		color: white;
		margin: 3px 15px;
	}
	</style>
</head>

<body>
	<main id="content">
	<form name="downloadForm" action="<c:url value='/itsm/atchmnfl/site/retrieve.do'/>" target="">
		<input type="hidden" name="atchmnflId" id="atchmnflId" />
		<input type="hidden" name="atchmnflSn" id="atchmnflSn" />
	</form>
	<div class="page-header">
		<h3 class="page-title">
			배포 처리
			<c:out value="${registerFlag == 'create' ? '등록' : '수정'}" />
		</h3>
		<div class="btnbox fr">
			<c:if test="${registerFlag != 'create' }">
				<button type="button" onclick="fncWdtbCnfirmDownload()" class="btn btn-primary">
					<i class="ico-download"></i>확인서내려받기
				</button>
			</c:if>
			<button type="button" onclick="fncRetrieveList()" class="btn btn-secondary">
				<i class="ico-list"></i>목록
			</button>
			<c:if test="${registerFlag != 'create' && LOGIN_USER_VO.userTyCode == 'R001'}">
				<button type="button" onclick="fncDelete()" class="btn btn-dark">
					<i class="ico-delete"></i>삭제
				</button>
			</c:if>
			<button type="button" onclick="fncSave()" class="btn btn-primary">
				<i class="ico-save"></i>저장
			</button>
		</div>
	</div>
	
	<form:form commandName="wdtbFormVO" id="listForm" name="listForm" method="post" enctype="multipart/form-data">
		<form:hidden path="wdtbVO.saveToken" />
		<form:hidden path="wdtbVO.wdtbCnfirmNo" />
		<form:hidden path="wdtbVO.chargerId" />
		<form:hidden path="wdtbVO.chargerUserNm" />
		<form:hidden path="funcImprvmVO.fiCn" />
		<form:hidden path="funcImprvmVO.navigation" />
		<form:hidden path="wdtbVO.solutConectflId" id="solutConectflId" />
		<form:hidden path="wdtbVO.opertResultflId" id="opertResultflId" />
		<form:hidden path="wdtbVO.loginResultflId" id="loginResultflId" />
		<form:hidden path="wdtbVO.serverOneLogflId" id="serverOneLogflId" />
		<form:hidden path="wdtbVO.serverTwoLogflId" id="serverTwoLogflId" />

		<div class="row equalheight">
			<div class="col-left">
				<div class="card">
					<div class="card-header">
						<h4 style="margin-left: 20px;" class="card-title">배포번호</h4>
						<dl class="sr-info">
							<dd>
								<c:if test="${registerFlag == 'create' }">자동생성</c:if>
								<c:if test="${registerFlag != 'create' }">
									<c:out value="${wdtbFormVO.wdtbVO.wdtbCnfirmNo}" />
								</c:if>
							</dd>

							<dt style="margin-right: 20px; margin-left: 20px;">
								<b>대상서비스</b>
							</dt>
							<dd id="trgetSrvcCodeNm">
								<c:out value="${wdtbFormVO.srvcRsponsVO.trgetSrvcCodeNm}" />
							</dd>
						</dl>
					</div>
				</div>

				<div class="row">
					<div class="col">
						<!-- 요청자정보 -->
						<div class="card">
							<div class="card-header">
								<h5 class="card-title">배포 기본정보</h5>
							</div>
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>배포일자</dt>
										<dd>
											<form:input path="wdtbVO.wdtbDtDateDisplay"
												id="wdtbDtDateDisplay" class="control inpdate" title="배포일자"
												placeholder="YYYY-MM-DD" />
										</dd>
									</dl>
									<dl>
										<dt>SR제목</dt>
										<dd>
											<form:input path="srvcRsponsVO.srvcRsponsSj" class="control"
												title="개선제목" disabled="true" />
										</dd>
									</dl>
									<dl>
										<dt>배포구분</dt>
										<dd>
											<c:forEach items="${wdtbSeList }" var="wdtbSe">
												<c:if test="${wdtbSe.cmmnCode ==  wdtbFormVO.wdtbVO.wdtbSe}">
													<input type="radio" id="${wdtbSe.cmmnCode }"
														value="${wdtbSe.cmmnCode }" name="wdtbVO.wdtbSe"
														checked="checked" class="checkbox">
												</c:if>
												<c:if test="${wdtbSe.cmmnCode !=  wdtbFormVO.wdtbVO.wdtbSe}">
													<input type="radio" id="${wdtbSe.cmmnCode }"
														value="${wdtbSe.cmmnCode }" name="wdtbVO.wdtbSe"
														class="checkbox">
												</c:if>
												<label for="${wdtbSe.cmmnCode }">&nbsp;${wdtbSe.cmmnCodeNm }</label>
											</c:forEach>
										</dd>
									</dl>
									<dl>
										<dt>SR번호</dt>
										<dd>
											<c:forEach items="${wdtbFormVO.srvcRsponsNos }"
												var="srvcRsponsNo" varStatus="status">
												<form:hidden path="srvcRsponsNos" value="${srvcRsponsNo}" />
												<c:if test="${status.count == 1 }">
													<c:out value="${srvcRsponsNo }" />
												</c:if>
												<c:if test="${status.count != 1 }">
                                    		, <c:out value="${srvcRsponsNo }" />
												</c:if>
											</c:forEach>
										</dd>
									</dl>
									<dl>
										<dt>기능개선</dt>
										<dd>
											<c:if test="${empty wdtbFormVO.funcImprvmVO.fnctImprvmNo}">없음</c:if>
											<c:out value="${wdtbFormVO.funcImprvmVO.fnctImprvmNo}" />
										</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
					<div class="col-12">
						<div class="card">
							<div class="card-header">
								<h5 class="card-title">배포 사항</h5>
							</div>
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>솔루션명</dt>
										<dd>
											<form:input path="srvcRsponsVO.trgetSrvcCodeSubNm3"
												class="control" title="솔루션명" disabled="true" />
										</dd>
									</dl>
									<dl>
										<dt>배포IP<br>(Jenkins)</dt>
										<dd>
											<form:input path="wdtbVO.wdtbIp" class="control" title="배포IP" />
										</dd>
									</dl>
									<hr>
									<dl>
										<dt>서버정보</dt>
										<dd style="text-align: center; justify-content: center;"
											class="serverInfo">운영#1</dd>
										<dd style="text-align: center; justify-content: center;"
											class="serverInfo">운영#2</dd>
									</dl>
									<dl>
										<dt>배포NO</dt>
										<dd>
											<form:input path="wdtbVO.wdtbNoOne" class="control"
												title="운영#1배포번호" />
										</dd>
										<dd>
											<form:input path="wdtbVO.wdtbNoTwo" class="control"
												title="운영#2배포번호" />
										</dd>
									</dl>
									<dl>
										<dt>배포시간</dt>
										<dd>
											<div class="input-group datetime">
												<form:input path="wdtbVO.wdtbDtOneDateDisplay"
													id="wdtbDtOneDateDisplay" class="control inpdate"
													title="운영#1배포일" placeholder="YYYY-MM-DD" />
												<form:input path="wdtbVO.wdtbDtOneTimeDisplay"
													id="wdtbDtOneTimeDisplay" class="control inptime"
													title="운영#1배포시간" placeholder="HH:MM" />
											</div>
										</dd>
										<dd>
											<div class="input-group datetime">
												<form:input path="wdtbVO.wdtbDtTwoDateDisplay"
													id="wdtbDtTwoDateDisplay" class="control inpdate"
													title="운영#2배포일" placeholder="YYYY-MM-DD" />
												<form:input path="wdtbVO.wdtbDtTwoTimeDisplay"
													id="wdtbDtTwoTimeDisplay" class="control inptime"
													title="운영#2배포시간" placeholder="HH:MM" />
											</div>
										</dd>
									</dl>
									<dl>
										<dt>배포횟수</dt>
										<dd>
											<form:input path="wdtbVO.wdtbCoOne" class="control"
												title="배포횟수" />
										</dd>
										<dd>
											<form:input path="wdtbVO.wdtbCoTwo" class="control"
												title="배포횟수" />
										</dd>
									</dl>
									<dl>
										<dt>오류사유</dt>
										<dd>
											<form:input path="wdtbVO.errorReasonOne" class="control"
												title="오류사유" />
										</dd>
										<dd>
											<form:input path="wdtbVO.errorReasonTwo" class="control"
												title="오류사유" />
										</dd>
									</dl>
									<dl>
										<dt>기타</dt>
										<dd>
											<form:input path="wdtbVO.wdtbEtc" class="control" title="기타" />
										</dd>
									</dl>
									<dl>
										<dt>특이사항</dt>
										<dd>
											<form:input path="wdtbVO.partclrMatter" class="control"
												title="특이사항" />
										</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-left">
				<div class="card" style="position: static;">
					<div class="card-header type2">
						<dl class="sr-info">
							<dt style="margin-right: 20px; margin-left: 20px;">
								<b>요청자</b>
							</dt>
							<dd id="rqesterNm" style="margin-right: 20px;">
								<c:out value="${wdtbFormVO.srvcRsponsVO.rqester1stNm }" />
							</dd>
							<dt style="margin-right: 20px; margin-left: 20px;">
								<b>소속</b>
							</dt>
							<dd id="rqesterPsitn" style="margin-right: 20px;">
								<c:out value="${wdtbFormVO.srvcRsponsVO.rqester1stPsitn }" />
							</dd>
						</dl>
					</div>
				</div>

				<div class="row">
					<div class="col">
						<div class="card">
							<div class="card-header">
								<h5 class="card-title">개선 사항</h5>
							</div>
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>작업사유</dt>
										<dd>
											<form:textarea rows="3" path="wdtbVO.opertReason"
												class="control" title="작업사유"></form:textarea>
										</dd>
									</dl>
									<dl>
										<dt>네비게이션</dt>
										<dd>
											<form:textarea id="navigation" rows="2"
												path="wdtbVO.wdtbNavigation" class="control" title="네비게이션" />

										</dd>
									</dl>
									<dl>
										<dt>개선사항</dt>
										<dd>
											<form:textarea id="imprvmMatter" rows="3"
												path="wdtbVO.imprvmMatter" class="control" title="개선사항" />
										</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col">
						<div class="card">
							<div class="card-header">
								<h5 class="card-title">서비스 확인</h5>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col">
						<div class="card">
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>솔루션접속</dt>
										<dd class="d-block">
											<div class="file-box">

												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i
													class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="solutConectfl" type="file" name="multipartFile" multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary" id="solutConectflSendBtn">파일전송</button>
											<ul id="solutConectflListDiv" class="file-list-group">
											<c:forEach var="atchmnflVO" items="${solutConectflList}">
												<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
													<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
													<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
												</li>
											</c:forEach>
											</ul>
										</dd>
									</dl>
									<dl>
										<dt>로그인결과</dt>
										<dd class="d-block">
											<div class="file-box">

												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i
													class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="loginResultfl" type="file" name="multipartFile"
													multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary"
												id="loginResultflSendBtn">파일전송</button>
											<ul id="loginResultflListDiv" class="file-list-group">
												<c:forEach var="atchmnflVO" items="${loginResultflList}">
													<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
														<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
														<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
													</li>
												</c:forEach>
											</ul>
										</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
					<div class="col">
						<div class="card">
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>서버Log#1</dt>
										<dd class="d-block">
											<div class="file-box">

												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i
													class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="serverOneLogfl" type="file" name="multipartFile"
													multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary"
												id="serverOneLogflSendBtn">파일전송</button>
											<ul id="serverOneLogflListDiv" class="file-list-group">
												<c:forEach var="atchmnflVO" items="${serverOneLogflList}">
													<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
														<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
														<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
													</li>
												</c:forEach>
											</ul>
										</dd>
									</dl>
									<dl>
										<dt>서버Log#2</dt>
										<dd class="d-block">
											<div class="file-box">
												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i
													class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="serverTwoLogfl" type="file" name="multipartFile"
													multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary"
												id="serverTwoLogflSendBtn">파일전송</button>
											<ul id="serverTwoLogflListDiv" class="file-list-group">
												<c:forEach var="atchmnflVO" items="${serverTwoLogflList}">
													<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
														<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
														<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
													</li>
												</c:forEach>
											</ul>
										</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col">
						<div class="card">
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>작업사항결과</dt>
										<dd class="d-block">
											<div class="file-box">
												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i
													class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="opertResultfl" type="file" name="multipartFile"
													multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary"
												id="opertResultflSendBtn">파일전송</button>
											<ul id="opertResultflListDiv" class="file-list-group">
												<c:forEach var="atchmnflVO" items="${opertResultflList}">
													<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
														<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
														<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
													</li>
												</c:forEach>
											</ul>
										</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col">
						<div class="card">
								<%--                    <div class="card-header">--%>
								<%--                      <h5 class="card-title">담당자</h5>--%>
								<%--                    </div>--%>

							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>기관담당자</dt>
										<dd>
											<select class="control" name="wdtbVO.confirmUsr">
												<c:forEach items="${cstmrList}" var="item">
													<option <c:if test="${wdtbFormVO.wdtbVO.confirmUsr eq item.userId}">selected</c:if> value="${item.userId}">${item.userNm}</option>
												</c:forEach>
											</select>
										</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</form:form>

	<div class="btnbox right mb30">
		<button type="button" onclick="fncRetrieveList()"
			class="btn btn-secondary">
			<i class="ico-list"></i>목록
		</button>
		<c:if test="${registerFlag != 'create' }">
			<button type="button" onclick="fncDelete()" class="btn btn-dark">
				<i class="ico-delete"></i>삭제
			</button>
		</c:if>
		<button type="button" onclick="fncSave()" class="btn btn-primary">
			<i class="ico-save"></i>저장
		</button>
	</div>
	</main>
</body>
</html>
