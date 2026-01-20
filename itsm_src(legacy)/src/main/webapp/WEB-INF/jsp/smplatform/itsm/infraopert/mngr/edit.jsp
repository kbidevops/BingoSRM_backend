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
<c:set var="registerFlag" value="${empty infraOpertFormVO.infraOpertVO.infraOpertNo ? 'create' : 'modify'}" />
<title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}" />
	<c:out value="${registerFlag == 'create' ? '등록' : '수정'}" /></title>
<!--For Commons Validator Client Side-->
<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<%--  <validator:javascript formName="srvcRsponsFormVO" staticJavascript="false" xhtml="true" cdata="false"/> --%>
<script type="text/javaScript" language="javascript" defer="defer">
var infraPlanAtchmnflFileCnt = 0;
var infraResultAtchmnflFileCnt = 0;
var infraPlanEtcAtchmnflFileCnt = 0;
var infraResultEtcAtchmnflFileCnt = 0;

$(document).ready(function() {
	
	$("#infraOpertNo_sub").mask("IO-9999-999");
	$("#infraOpertNo_sub").val($("#infraOpertNo").val())
	
	$('#infraPlanAtchmnflSendBtn').hide();
	$('#infraResultAtchmnflSendBtn').hide();
	$('#infraPlanEtcAtchmnflSendBtn').hide();
	$('#infraResultEtcAtchmnflSendBtn').hide();
	
	var atchmnflCnt = 0;
	<c:if test="${registerFlag != create}">
	atchmnflCnt = 2;
	</c:if>
	
	 var url = "<c:url value='/itsm/atchmnfl/site/create.do'/>";
	$('#infraPlanAtchmnfl').fileupload({
 		 url: url,
 		 dataType: 'json',
 		 formData: {atchmnflId: $("#infraPlanAtchmnflId").val()},
 		 sequentialUploads: true,
 		 add: function (e, data) {
 			 var valid = true;
 			 var re = /^.+\.((doc)|(hwpx)|(hwp)|(pdf)|(pts)|(zip))$/i;
 			 
 			 $.each(data.files, function (index, file) {
			        console.log('Added file: ' + file.name);
			        console.log('Added file: ' + file.type);
			        console.log('Added file: ' + file.size);
			        console.log('Added file: ' + filesize(file.size));
// 			   		console.log('file size: ' + data.getNumberOfFiles());
			        
			        infraPlanAtchmnflFileCnt++;
			        
			     	if (!re.test(file.name)) {
			     		valid = false;
			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
			     		infraPlanAtchmnflFileCnt--;
			     	}else if (file.size > 10000000){ //5M
			     	    console.log('10M이하의 파일만 허용 합니다.');
			     		alert(file.name+' <br/>10M이상의 첨부파일은 제외됩니다.');
			     		infraPlanAtchmnflFileCnt--;
			     		valid = false;
			     	}else if($('#infraPlanAtchmnflListDiv li').length > atchmnflCnt){
			     		console.log('1개의 파일만 첨부 가능합니다.');
			     		alert(file.name+' <br/>1개의 파일만 첨부 가능합니다.');
			     	}else if (infraPlanAtchmnflFileCnt>1){ //동시 업로드 1개 제한
    			     	console.log('1개의 파일만 허용 합니다.'+infraPlanAtchmnflFileCnt);
			     		alert(file.name+' <br/>동시에 1개의 파일만 첨부 허용 합니다.');
			     		infraPlanAtchmnflFileCnt--;
			     		valid = false;
			     	}else{
			     		console.log('정상등록 '+infraPlanAtchmnflFileCnt);
			     		var abortBtn = $( '<a/>' )
		                     .attr( 'href', 'javascript:void(0)' )
		                     .attr( 'class', 'close' )
		                     .attr( 'aria-label', 'Close' )
		                     .append( '<span aria-hidden="true">&times;</span>' )
		                     .click( function() {
			                    	data.abort();
			                        data.context.remove();
			                        data = null;
			                        if(infraPlanAtchmnflFileCnt>0){						                        	
			                        	infraPlanAtchmnflFileCnt--;
			                        	console.log('infraPlanAtchmnflFileCnt: '+infraPlanAtchmnflFileCnt);
			                        }
		                     } );
			     		
  			         data.context = $( '<div/>' ).appendTo($('#infraPlanAtchmnflListDiv'));
    			         data.context.attr( 'class', 'form-row' );
    			         data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
			     	}
			        
			     });
 			 
 			 $("#infraPlanAtchmnflSendBtn").click(function () {        				   
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
 			if(infraResultAtchmnflFileCnt > 0){
  		    	$("#infraResultAtchmnflSendBtn").trigger("click");
     		}else if(infraPlanEtcAtchmnflFileCnt>0){
           		$("#infraPlanEtcAtchmnflSendBtn").trigger("click");
     		}else if(infraResultEtcAtchmnflFileCnt>0){
               	$("#infraResultEtcAtchmnflSendBtn").trigger("click");
            }else{
	   	        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/infraOpert/mngr/create.do' : '/itsm/infraOpert/mngr/update.do'}"/>";
	   	        frm.submit();	
            }
 		 },
 		    progressServerRate: 0.3

 		});
	
	$('#infraResultAtchmnfl').fileupload({
		 url: url,
		 dataType: 'json',
		 formData: {atchmnflId: $("#infraResultAtchmnflId").val()},
		 sequentialUploads: true,
		 add: function (e, data) {
			 var valid = true;
			 var re = /^.+\.((doc)|(hwpx)|(hwp)|(pdf)|(pts)|(zip))$/i;
			 
			 $.each(data.files, function (index, file) {
			        console.log('Added file: ' + file.name);
			        console.log('Added file: ' + file.type);
			        console.log('Added file: ' + file.size);
			        console.log('Added file: ' + filesize(file.size));
//			   		console.log('file size: ' + data.getNumberOfFiles());
			        
			        infraResultAtchmnflFileCnt++;
			        
			     	if (!re.test(file.name)) {
			     		valid = false;
			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
			     		infraResultAtchmnflFileCnt--;
			     	}else if (file.size > 10000000){ //5M
			     	    console.log('10M이하의 파일만 허용 합니다.');
			     		alert(file.name+' <br/>10M이상의 첨부파일은 제외됩니다.');
			     		infraResultAtchmnflFileCnt--;
			     		valid = false;
			     	}else if($('#infraResultAtchmnflListDiv li').length > atchmnflCnt){
			     		console.log('1개의 파일만 첨부 가능합니다.');
			     		alert(file.name+' <br/>1개의 파일만 첨부 가능합니다.');
			     	}else if (infraResultAtchmnflFileCnt>1){ //동시 업로드 1개 제한
   			     	console.log('1개의 파일만 허용 합니다.'+infraResultAtchmnflFileCnt);
			     		alert(file.name+' <br/>동시에 1개의 파일만 첨부 허용 합니다.');
			     		infraResultAtchmnflFileCnt--;
			     		valid = false;
			     	}else{
			     		console.log('정상등록 '+infraResultAtchmnflFileCnt);
			     		var abortBtn = $( '<a/>' )
		                     .attr( 'href', 'javascript:void(0)' )
		                     .attr( 'class', 'close' )
		                     .attr( 'aria-label', 'Close' )
		                     .append( '<span aria-hidden="true">&times;</span>' )
		                     .click( function() {
			                    	data.abort();
			                        data.context.remove();
			                        data = null;
			                        if(infraResultAtchmnflFileCnt>0){						                        	
			                        	infraResultAtchmnflFileCnt--;
			                        	console.log('infraPlanAtchmnflFileCnt: '+infraResultAtchmnflFileCnt);
			                        }
		                     } );
			     		
 			         data.context = $( '<div/>' ).appendTo($('#infraResultAtchmnflListDiv'));
   			         data.context.attr( 'class', 'form-row' );
   			         data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
			     	}
			        
			     });
			 
			 $("#infraResultAtchmnflSendBtn").click(function () {        				   
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
			if(infraResultAtchmnflFileCnt > 1){
	 			alert("인프라 작업 결과서는 하나의 파일만 첨부해주세요.");
	 			return;
	 		}
			 
			if(infraPlanEtcAtchmnflFileCnt>0){
          		$("#infraPlanEtcAtchmnflSendBtn").trigger("click");
			}else if(infraResultEtcAtchmnflFileCnt>0){
				$("#infraPlanEtcAtchmnflSendBtn").trigger("click");
            }else{
	   	        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/infraOpert/mngr/create.do' : '/itsm/infraOpert/mngr/update.do'}"/>";
	   	        frm.submit();
            }
		 },
		    progressServerRate: 0.3

		});
	
	$('#infraPlanEtcAtchmnfl').fileupload({
		 url: url,
		 dataType: 'json',
		 formData: {atchmnflId: $("#infraPlanEtcAtchmnflId").val()},
		 sequentialUploads: true,
		 add: function (e, data) {
			 var valid = true;
			 var re = /^.+\.((doc)|(txt)|(xls)|(xlsx)|(docx)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(gif)|(png)|(bmp))$/i;
			 
			 $.each(data.files, function (index, file) {
			        console.log('Added file: ' + file.name);
			        console.log('Added file: ' + file.type);
			        console.log('Added file: ' + file.size);
			        console.log('Added file: ' + filesize(file.size));
//			   		console.log('file size: ' + data.getNumberOfFiles());
			        
			        infraPlanEtcAtchmnflFileCnt++;
			        
			     	if (!re.test(file.name)) {
			     		valid = false;
			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
			     		infraPlanEtcAtchmnflFileCnt--;
			     	}else if (file.size > 10000000){ //5M
			     	    console.log('10M이하의 파일만 허용 합니다.');
			     		alert(file.name+' <br/>10M이상의 첨부파일은 제외됩니다.');
			     		infraPlanEtcAtchmnflFileCnt--;
			     		valid = false;
			     	}else if($('#infraPlanEtcAtchmnflListDiv li').length > 4){
			     		console.log('5개의 파일만 첨부 가능합니다.');
			     		alert(file.name+' <br/>5개의 파일만 첨부 가능합니다.');
			     	}else if (infraPlanEtcAtchmnflFileCnt>5){ //동시 업로드 5개 제한
  			     		console.log('5개의 파일만 허용 합니다.'+infraPlanEtcAtchmnflFileCnt);
			     		alert(file.name+' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
			     		infraPlanEtcAtchmnflFileCnt--;
			     		valid = false;
			     	}else{
			     		console.log('정상등록 '+infraPlanEtcAtchmnflFileCnt);
			     		var abortBtn = $( '<a/>' )
		                     .attr( 'href', 'javascript:void(0)' )
		                     .attr( 'class', 'close' )
		                     .attr( 'aria-label', 'Close' )
		                     .append( '<span aria-hidden="true">&times;</span>' )
		                     .click( function() {
			                    	data.abort();
			                        data.context.remove();
			                        data = null;
			                        if(infraPlanEtcAtchmnflFileCnt>0){						                        	
			                        	infraPlanEtcAtchmnflFileCnt--;
			                        	console.log('infraPlanEtcAtchmnflFileCnt: '+infraPlanEtcAtchmnflFileCnt);
			                        }
		                     } );
			     		
				         data.context = $( '<div/>' ).appendTo($('#infraPlanEtcAtchmnflListDiv'));
	  			         data.context.attr( 'class', 'form-row' );
	  			         data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
			     	}
			        
			     });
			 
			 $("#infraPlanEtcAtchmnflSendBtn").click(function () {        				   
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
			 if(infraResultEtcAtchmnflFileCnt>0){
	          	 $("#infraResultEtcAtchmnflSendBtn").trigger("click");
	         }else{
		   	     frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/infraOpert/mngr/create.do' : '/itsm/infraOpert/mngr/update.do'}"/>";
		   	     frm.submit();
	         }
		 },
		    progressServerRate: 0.3

	});
	
	$('#infraResultEtcAtchmnfl').fileupload({
		 url: url,
		 dataType: 'json',
		 formData: {atchmnflId: $("#infraResultEtcAtchmnflId").val()},
		 sequentialUploads: true,
		 add: function (e, data) {
			 var valid = true;
			 var re = /^.+\.((doc)|(txt)|(xls)|(xlsx)|(docx)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(gif)|(png)|(bmp))$/i;
			 
			 $.each(data.files, function (index, file) {
			        console.log('Added file: ' + file.name);
			        console.log('Added file: ' + file.type);
			        console.log('Added file: ' + file.size);
			        console.log('Added file: ' + filesize(file.size));
//			   		console.log('file size: ' + data.getNumberOfFiles());
			        
			        infraResultEtcAtchmnflFileCnt++;
			        
			     	if (!re.test(file.name)) {
			     		valid = false;
			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
			     		infraResultEtcAtchmnflFileCnt--;
			     	}else if (file.size > 10000000){ //5M
			     	    console.log('10M이하의 파일만 허용 합니다.');
			     		alert(file.name+' <br/>10M이상의 첨부파일은 제외됩니다.');
			     		infraResultEtcAtchmnflFileCnt--;
			     		valid = false;
			     	}else if($('#infraResultEtcAtchmnflListDiv li').length > 4){
			     		console.log('5개의 파일만 첨부 가능합니다.');
			     		alert(file.name+' <br/>5개의 파일만 첨부 가능합니다.');
			     	}else if (infraResultEtcAtchmnflFileCnt>5){ //동시 업로드 5개 제한
 			     		console.log('1개의 파일만 허용 합니다.'+infraResultEtcAtchmnflFileCnt);
			     		alert(file.name+' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
			     		infraResultEtcAtchmnflFileCnt--;
			     		valid = false;
			     	}else{
			     		console.log('정상등록 '+infraResultEtcAtchmnflFileCnt);
			     		var abortBtn = $( '<a/>' )
		                     .attr( 'href', 'javascript:void(0)' )
		                     .attr( 'class', 'close' )
		                     .attr( 'aria-label', 'Close' )
		                     .append( '<span aria-hidden="true">&times;</span>' )
		                     .click( function() {
			                    	data.abort();
			                        data.context.remove();
			                        data = null;
			                        if(infraResultEtcAtchmnflFileCnt>0){						                        	
			                        	infraResultEtcAtchmnflFileCnt--;
			                        	console.log('infraResultEtcAtchmnflFileCnt: '+infraResultEtcAtchmnflFileCnt);
			                        }
		                     } );
			     		
				         data.context = $( '<div/>' ).appendTo($('#infraResultEtcAtchmnflListDiv'));
	  			         data.context.attr( 'class', 'form-row' );
	  			         data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
			     	}
			        
			     });
			 
			 $("#infraResultEtcAtchmnflSendBtn").click(function () {        				   
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
	   	    frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/infraOpert/mngr/create.do' : '/itsm/infraOpert/mngr/update.do'}"/>";
	   	    frm.submit();
		 },
		    progressServerRate: 0.3

	});
});

function fncSave() {
	frm = document.listForm; 
	
	if($("#infraPlanAtchmnflListDiv li").length <1 && infraPlanAtchmnflFileCnt<1){
		alert("인프라작업계획서를 첨부해주세요.");
		return;
	}
	if($("#infraPlanAtchmnflListDiv li").length >0 && infraPlanAtchmnflFileCnt>0){
		alert("인프라작업계획서는 하나만 등록해주세요.");
		return;
	}
	if($("#infraResultAtchmnflListDiv li").length >0 && infraResultAtchmnflFileCnt>0){
		alert("인프라작업결과서는 하나만 등록해주세요.");
		return;
	}
	
	<c:if test="${registerFlag != 'create'}">
		var exp = /^(IO-)[0-9]{4}-[0-9]{3}$/g; //작업번호 정규식
		if(!exp.test(frm.infraOpertNo_sub.value)){
			alert("작업번호를 확인해주세요.");
			return;
		}
		
		$.ajax({
	 		type : "post",
	 		url : "<c:url value='/itsm/infraOpert/mngr/retrieveAjax.do'/>",
	 		data : {
	 			"infraOpertNo" : frm.infraOpertNo_sub.value
	 		},
	 		dataType : "json",
	 		traditional: true,
	 		async: true,
	 		success : function(rq){
	 			console.log(rq.returnMessage)
	 			if(rq.returnMessage == null){//중복 없음
	 				//첨부파일 존재 유무 확인
	 			    if(infraPlanAtchmnflFileCnt>0 && infraResultAtchmnflFileCnt>0 && infraPlanEtcAtchmnflFileCnt>0 && infraResultEtcAtchmnflFileCnt>0){
	 			    	$("#infraPlanAtchmnflSendBtn").trigger("click");	
	 			    }else if(infraPlanAtchmnflFileCnt>0){
	 			    	$("#infraPlanAtchmnflSendBtn").trigger("click");
	 			    }else if(infraResultAtchmnflFileCnt>0){
	 			    	$("#infraResultAtchmnflSendBtn").trigger("click");
	 			    }else if(infraPlanEtcAtchmnflFileCnt>0){
	 			    	$("#infraPlanEtcAtchmnflSendBtn").trigger("click");
	 			    }else if(infraResultEtcAtchmnflFileCnt>0){
	 			        $("#infraResultEtcAtchmnflSendBtn").trigger("click");
	 			    }else{
	 			        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/infraOpert/mngr/create.do' : '/itsm/infraOpert/mngr/update.do'}"/>";
	 			        frm.submit();	
	 			    }
	 				console.log("중복없음")
	 			}else {
	 				if($("input[name='infraOpertVO.infraOpertNo']").val() == $("#infraOpertNo_sub").val()){
	 					if(infraPlanAtchmnflFileCnt>0 && infraResultAtchmnflFileCnt>0 && infraPlanEtcAtchmnflFileCnt>0 && infraResultEtcAtchmnflFileCnt>0){
		 			    	$("#infraPlanAtchmnflSendBtn").trigger("click");	
		 			    }else if(infraPlanAtchmnflFileCnt>0){
		 			    	$("#infraPlanAtchmnflSendBtn").trigger("click");
		 			    }else if(infraResultAtchmnflFileCnt>0){
		 			    	$("#infraResultAtchmnflSendBtn").trigger("click");
		 			    }else if(infraPlanEtcAtchmnflFileCnt>0){
		 			    	$("#infraPlanEtcAtchmnflSendBtn").trigger("click");
		 			    }else if(infraResultEtcAtchmnflFileCnt>0){
		 			        $("#infraResultEtcAtchmnflSendBtn").trigger("click");
		 			    }else{
		 			        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/infraOpert/mngr/create.do' : '/itsm/infraOpert/mngr/update.do'}"/>";
		 			        frm.submit();	
		 			    }
	 				}else{
		 				alert("이미 있는 작업번호 입니다.");
	 				}
	 				return;
	 			}
	 				
	 		},
	 		error : function(xhr){
	 			alert("보고서를 등록하는데 실패하였습니다. 관리자에게 문의해주세요.");
	 		}
	 	})
	</c:if>
		
	<c:if test="${registerFlag == 'create'}">
		if(infraPlanAtchmnflFileCnt>0 && infraResultAtchmnflFileCnt>0 && infraPlanEtcAtchmnflFileCnt>0 && infraResultEtcAtchmnflFileCnt>0){
	    	$("#infraPlanAtchmnflSendBtn").trigger("click");	
	    }else if(infraPlanAtchmnflFileCnt>0){
	    	$("#infraPlanAtchmnflSendBtn").trigger("click");
	    }else if(infraResultAtchmnflFileCnt>0){
	    	$("#infraResultAtchmnflSendBtn").trigger("click");
	    }else if(infraPlanEtcAtchmnflFileCnt>0){
	    	$("#infraPlanEtcAtchmnflSendBtn").trigger("click");
	    }else if(infraResultEtcAtchmnflFileCnt>0){
	        $("#infraResultEtcAtchmnflSendBtn").trigger("click");
	    }else{
	        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/infraOpert/mngr/create.do' : '/itsm/infraOpert/mngr/update.do'}"/>";
	        frm.submit();	
	    }
	</c:if>
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

function fncDownloadFile(atchmnflId, atchmnflSn){
	document.downloadForm.action = "<c:url value='/itsm/atchmnfl/site/retrieve.do'/>";
	document.downloadForm.atchmnflId.value = atchmnflId;
	document.downloadForm.atchmnflSn.value = atchmnflSn;
	document.downloadForm.submit();
}

function fncDelete(){
    confirm('삭제하시겠습니까?','인프라작업 삭제', null, function(request){
		console.log('request: '+request);
		if(request){
			document.listForm.action = "<c:url value='/itsm/infraOpert/mngr/delete.do'/>";
	        document.listForm.submit();
		}
	});
 }
</script>
</head>
<body>
	<main id="content">
	<form name="downloadForm" action="<c:url value='/itsm/atchmnfl/site/retrieve.do'/>" target="">
		<input type="hidden" name="atchmnflId" id="atchmnflId" />
		<input type="hidden" name="atchmnflSn" id="atchmnflSn" />
	</form>
	<div class="page-header">
		<h3 class="page-title">
			인프라 작업
			<c:out value="${registerFlag == 'create' ? '등록' : '수정'}" />
		</h3>
		<div class="btnbox fr">
			<button type="button" onclick="fncRetrieveList()" class="btn btn-secondary">
				<i class="ico-list"></i>목록
			</button>
			<c:if test="${registerFlag != 'create' && LOGIN_USER_VO.userTyCode == 'R001'}">
				<button type="button" onclick="fncDelete()" class="btn btn-dark">
					<i class="ico-delete"></i>삭제
				</button>
			</c:if>
			<button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
		</div>
	</div>
	
	<form:form commandName="infraOpertFormVO" id="listForm" name="listForm" method="post" enctype="multipart/form-data">
		<form:hidden path="infraOpertVO.saveToken" />
		<form:hidden path="infraOpertVO.infraOpertNo" id="infraOpertNo"/>
		<form:hidden path="infraOpertVO.chargerId" />
		<form:hidden path="infraOpertVO.chargerUserNm" />
		<form:hidden path="infraOpertVO.infraPlanAtchmnflId" id="infraPlanAtchmnflId" />
		<form:hidden path="infraOpertVO.infraResultAtchmnflId" id="infraResultAtchmnflId" />
		<form:hidden path="infraOpertVO.infraPlanEtcAtchmnflId" id="infraPlanEtcAtchmnflId" />
		<form:hidden path="infraOpertVO.infraResultEtcAtchmnflId" id="infraResultEtcAtchmnflId" />
		
		<div class="row equalheight">
			<div class="col-left">
				<div class="card">
					<div class="card-header">
						<h4 style="margin-left: 20px;" class="card-title">작업번호</h4>
						<dl class="sr-info">
							<dd>
								<c:if test="${registerFlag == 'create' }">자동생성</c:if>
								<c:if test="${registerFlag != 'create' }">
									<form:input path="infraOpertVO.infraOpertNo_sub" class="control"
												title="기능개선번호" id="infraOpertNo_sub" />
								</c:if>
							</dd>
							<dt style="margin-right: 20px; margin-left: 20px;">
								<b>대상서비스</b>
							</dt>
							<dd id="trgetSrvcCodeNm">
								<c:out value="${infraOpertFormVO.srvcRsponsVO.trgetSrvcCodeNm}" />
							</dd>
						</dl>
					</div>
				</div>
				<div class="row">
					<div class="col">
						<!-- 요청자정보 -->
						<div class="card">
							<div class="card-header">
								<h5 class="card-title">작업 정보</h5>
							</div>
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>SR제목</dt>
										<dd>
											<form:input path="srvcRsponsVO.srvcRsponsSj" class="control"
												title="개선제목" disabled="true" />
										</dd>
									</dl>
									<dl>
										<dt>SR번호</dt>
										<dd>
											<c:forEach items="${infraOpertFormVO.srvcRsponsNos }"
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
										<dt>SR내용</dt>
										<dd>
											<form:textarea id="srvcRsponsCn" rows="5"
												path="srvcRsponsVO.srvcRsponsCn" class="control" disabled="true" title="SR내용" />
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
								<c:out value="${infraOpertFormVO.srvcRsponsVO.rqester1stNm }" />
							</dd>
							<dt style="margin-right: 20px; margin-left: 20px;">
								<b>소속</b>
							</dt>
							<dd id="rqesterPsitn" style="margin-right: 20px;">
								<c:out value="${infraOpertFormVO.srvcRsponsVO.rqester1stPsitn }" />
							</dd>
						</dl>
					</div>
				</div>
				<div class="row">
					<div class="col">
						<div class="card">
							<div class="card-header">
								<h5 class="card-title">작업 계획서</h5>
							</div>
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>작업계획서</dt>
										<dd class="d-block">
											<div class="file-box">

												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="infraPlanAtchmnfl" type="file" name="multipartFile" multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary" id="infraPlanAtchmnflSendBtn">파일전송</button>
											<ul id="infraPlanAtchmnflListDiv" class="file-list-group">
											<c:forEach var="atchmnflVO" items="${infraPlanAtchmnflList}">
												<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
													<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
													<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
												</li>
											</c:forEach>
											</ul>
										</dd>
									</dl>
									<dl>
										<dt>기타첨부자료</dt>
										<dd class="d-block">
											<div class="file-box">
												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="infraPlanEtcAtchmnfl" type="file" name="multipartFile" multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary" id="infraPlanEtcAtchmnflSendBtn">파일전송</button>
											<ul id="infraPlanEtcAtchmnflListDiv" class="file-list-group">
											<c:forEach var="atchmnflVO" items="${infraPlanEtcAtchmnflList}">
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
							<div class="card-header">
								<h5 class="card-title">작업 결과서</h5>
							</div>
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>작업결과서</dt>
										<dd class="d-block">
											<div class="file-box">
		
												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="infraResultAtchmnfl" type="file" name="multipartFile" multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary" id="infraResultAtchmnflSendBtn">파일전송</button>
											<ul id="infraResultAtchmnflListDiv" class="file-list-group">
											<c:forEach var="atchmnflVO" items="${infraResultAtchmnflList}">
												<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
													<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
													<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
												</li>
											</c:forEach>
											</ul>
										</dd>
									</dl>
									<dl>
										<dt>기타첨부자료</dt>
										<dd class="d-block">
											<div class="file-box">
												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="infraResultEtcAtchmnfl" type="file" name="multipartFile" multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary" id="infraResultEtcAtchmnflSendBtn">파일전송</button>
											<ul id="infraResultEtcAtchmnflListDiv" class="file-list-group">
											<c:forEach var="atchmnflVO" items="${infraResultEtcAtchmnflList}">
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
			</div>
		</div>
	</form:form>

	<div class="btnbox right mb30">
		<button type="button" onclick="fncRetrieveList()"class="btn btn-secondary">
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
