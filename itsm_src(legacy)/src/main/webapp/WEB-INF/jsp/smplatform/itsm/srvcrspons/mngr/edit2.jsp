<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
  /**
  * @Class Name : /itsm/srvcrspons/mngr/edit.jsp
  * @Description : SR 관리 등록/수정 화면
  * @Modification Information
  *
  *   수정일         수정자                   수정내용
  *  -------    --------    ---------------------------
  *  2017.09.13 임영수               최초 생성
  *
  * author 지휘곰 개발팀
  * since 2017.09.13
  *
  * Copyright (C) 2017 by COMMANDER BEAR  All right reserved.
  */
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <c:set var="registerFlag" value="${empty srvcRsponsFormVO.srvcRsponsVO.srvcRsponsNo ? 'create' : 'modify'}"/>
    <title>SR관리 <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></title>
    <!--For Commons Validator Client Side-->
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <validator:javascript formName="srvcRsponsFormVO" staticJavascript="false" xhtml="true" cdata="false"/>
	<script type="text/javaScript" language="javascript" defer="defer">
        <!--
        var requstAtchmnflFileCnt = 0;
        
        $(document).ready(function() {
        	 $("#requstDtDateDisplay").mask("9999-99-99");
        	 $("#requstDtTimeDisplay").mask("99:99");
        	 
        	 $("#rspons1stDtDateDisplay").mask("9999-99-99");
        	 $("#rspons1stDtTimeDisplay").mask("99:99");
        	 
        	 $("#processDtDateDisplay").mask("9999-99-99");
        	 $("#processDtTimeDisplay").mask("99:99");
        	 
        	 $('.dropdown-toggle').dropdown();
        	 
//         	 $("#attach").click(function () {
//      		    $("#requstAtchmnfl").trigger('click');
//      	 });
        	 
        	 var url = '<c:url value='/atchmnfl/site/create.do'/>';
        	 
        	 
        	 
        	 $('#requstAtchmnfl').fileupload({
        		 url: url,
        		 dataType: 'json',
        		 formData: {atchmnflId: $("#requstAtchmnflId").val()},
        		 sequentialUploads: true,
        		 add: function (e, data) {
        			 var valid = true;
        			 var re = /^.+\.((doc)|(xls)|(txt)|(xlsx)|(docx)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(gif)|(png)|(bmp))$/i;
        			 
        			 $.each(data.files, function (index, file) {
       			        console.log('Added file: ' + file.name);
       			        console.log('Added file: ' + file.type);
       			        console.log('Added file: ' + file.size);
       			        console.log('Added file: ' + filesize(file.size));
//        			        console.log('file size: ' + data.getNumberOfFiles());
       			        
       			        requstAtchmnflFileCnt++;
       			        
       			     	if (!re.test(file.name)) {
       			     		valid = false;
       			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
       			     		requstAtchmnflFileCnt--;
       			     	}else if (file.size > 5000000){ //5M
       			     	    console.log('5M이하의 파일만 허용 합니다.');
       			     		alert(file.name+' <br/>5M이상의 첨부파일은 제외됩니다.');
       			     		requstAtchmnflFileCnt--;
       			     		valid = false;
       			     	}else if (requstAtchmnflFileCnt>5){ //동시 업로드 5개 제한
	       			     	console.log('5개의 파일만 허용 합니다.'+requstAtchmnflFileCnt);
	   			     		alert(file.name+' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
	   			     		requstAtchmnflFileCnt--;
	   			     		valid = false;
       			     	}else{
       			     		console.log('정상등록 '+requstAtchmnflFileCnt);
       			     		var abortBtn = $( '<a/>' )
			                     .attr( 'href', 'javascript:void(0)' )
			                     .attr( 'class', 'close' )
			                     .attr( 'aria-label', 'Close' )
			                     .append( '<span aria-hidden="true">&times;</span>' )
			                     .click( function() {
				                    	data.abort();
				                        data.context.remove();
				                        data = null;
				                        if(requstAtchmnflFileCnt>0){						                        	
				                        	requstAtchmnflFileCnt--;
				                        	console.log('requstAtchmnflFileCnt: '+requstAtchmnflFileCnt);
				                        }
			                     } );
       			     		
         			         data.context = $( '<div/>' ).appendTo($('#requstAtchmnflFileListDiv'));
           			         data.context.attr( 'class', 'form-row' );
           			         data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
       			     	}
       			        
       			     });
        			 
        			 $("#requstAtchmnflSendBtn").click(function () {        				   
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
//         		        var progress = parseInt(data.percent * 100, 10);
        		        var progress = parseInt(data.loaded / data.total * 100, 10);
        		        console.log('data.bitrate:'+data.bitrate);
        		        console.log('data.bitrate:'+progress);
        		        $('#requstAtchmnfl-progress .progress-bar').css(
        		            'width',
        		            progress+ '%'
        		        );
        		        $('#requstAtchmnfl-progress .progress-bar').html(progress+ '%');
        		        
        		        if(progress == 100){
        		        	frm.action = "<c:url value="${registerFlag == 'create' ? '/srvcrspons/mngr/create.do' : '/srvcrspons/mngr/update.do'}"/>";
        	                frm.submit();
        		        }
        		    },
        		    progressServerRate: 0.3

        	 });
        	 
        	 


//         	 $('#requstAtchmnfl').bind('fileuploadfailed', function (e, data) {
//                  $.each(data.files, function (index, file) {
//                      console.log('Removed file: ' + file.name + ', ' + file.size);
//                      totalSize = totalSize - file.size;
//                      console.log('Total size: ' + totalSize);
//                  });
//              });
        	 
//         	 $("#rqesterCttpc").mask("999-9999-9999"); //전화, 휴대폰, xxxx-xxxx등과 같은 전화번호가 다양해 적용 불가
        	$('#srvcRsponsNo').tooltip('show');

        });
        
                
        function fncSave() {
        	frm = document.listForm;       	
        	
        	
        	if(document.listForm.elements['srvcRsponsVO.trgetSrvcCode'].value == ''){
        		alert('대상서비스를 입력하세요.');
    			return;
        	}
        	if(document.listForm.elements['srvcRsponsVO.srvcRsponsClCode'].value == ''){
        		alert('SR구분을 입력하세요.');
    			return;
        	}
        	if(document.listForm.elements['srvcRsponsVO.processStdrCode'].value == ''){
        		alert('SR처리기준시간을 입력하세요.');
    			return;
        	}
        	if(document.listForm.elements['srvcRsponsVO.changeDfflyCode'].value == ''){
        		alert('만족도를 입력하세요.');
    			return;
        	}
        	
        	if(!validateSrvcRsponsFormVO(frm)){
                return;
            }else{
            	//첨부파일 존재 유무 확인
            	if(requstAtchmnflFileCnt>0){
            		$("#requstAtchmnflSendBtn").trigger("click");	
            	}else{
            		frm.action = "<c:url value="${registerFlag == 'create' ? '/srvcrspons/mngr/create.do' : '/srvcrspons/mngr/update.do'}"/>";
                    frm.submit();	
            	}
            	
            	
            }
        }
        
        <%-- 요청자동일 복사 --%>
        function fncChkCopyRqester(obj){
        	if(obj.checked){
        		document.listForm.elements['srvcRsponsVO.rqester1stNm'].value = document.listForm.elements['srvcRsponsVO.rqesterNm'].value;
        		document.listForm.elements['srvcRsponsVO.rqester1stPsitn'].value = document.listForm.elements['srvcRsponsVO.rqesterPsitn'].value;
        		document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = document.listForm.elements['srvcRsponsVO.rqesterCttpc'].value;
        		document.listForm.elements['srvcRsponsVO.rqester1stEmail'].value = document.listForm.elements['srvcRsponsVO.rqesterEmail'].value;
        	}else{
        		document.listForm.elements['srvcRsponsVO.rqester1stNm'].value = '';
        		document.listForm.elements['srvcRsponsVO.rqester1stPsitn'].value = '';
        		document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = '';
        		document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = '';
        	}
        }
        <%-- 요청자 변경시 처리 --%>
        function fncChangeRqesterNm(value){
        	console.log('요청자 value: '+$.trim(value));
    		$('#rqesterNmDropdown').dropdown('update');
    		
        	if($.trim(value) != ''){
        		console.log('빈값일때');
        		
        		$.ajax({
        		    type:'POST',
        		    url:"<c:url value='/srvcrspons/mngr/retrieveRqesterNmListAjax.do'/>",
        		    dataType: "json",
        			data : { 
        				"rqesterNm":value
        		   			},
        			async:false,
        			success : function(request){
        				fncInitRqesterNmDropdown(request)
        			},
        			error:function(r){
        				alert("정보전송에 실패하였습니다.");
        			}        			
        		});	
        	}
        }
        
        function fncInitRqesterNmDropdown(request){
        	var rqesterNmDropdownHTML = '';
        	console.log('request.returnMessage: '+request.returnMessage);
        	if(request.returnMessage == 'success'){
        		resultList = request.resultList;
        		for(var i=0;i<resultList.length; i++){
        			rqesterNmDropdownHTML += "<a class=\"dropdown-item\" href=\"#\" id=\"rqesterNm"+(i+1);
        			rqesterNmDropdownHTML +="\" onclick=\"fncSelectRqesterNm('rqesterNm"+(i+1)+"')\" data-name=\"";
            		rqesterNmDropdownHTML +=resultList[i].rqesterNm+"\" data-psitn=\""+resultList[i].rqesterPsitn;
            		rqesterNmDropdownHTML +="\" data-cttpc=\""+resultList[i].rqesterCttpc+"\" data-email=\""+resultList[i].rqesterEmail+"\">";
            		rqesterNmDropdownHTML += resultList[i].rqesterNm+"("+resultList[i].rqesterPsitn+")</a>";
            	}
			}else{
				rqesterNmDropdownHTML = '<a class="dropdown-item" href="#">일치하는데이터없음</a>';
			}
        	console.log('rqesterNmDropdownHTML: '+rqesterNmDropdownHTML);
        	$('#rqesterNmDropdown').html(rqesterNmDropdownHTML);
        }
        
        function fncSelectRqesterNm(objId){
        	document.listForm.elements['srvcRsponsVO.rqesterNm'].value = $("#"+objId).data("name");;
    		document.listForm.elements['srvcRsponsVO.rqesterPsitn'].value = $("#"+objId).data("psitn");
    		document.listForm.elements['srvcRsponsVO.rqesterCttpc'].value = $("#"+objId).data("cttpc");
    		document.listForm.elements['srvcRsponsVO.rqesterEmail'].value = $("#"+objId).data("email");;
        }
        <%-- 1차요청자 변경시 처리 --%>
        function fncChangeRqester1stNm(value){
        	console.log('1차 요청자 value: '+$.trim(value));
    		$('#rqester1stNmDropdown').dropdown('update');
    		
        	if($.trim(value) != ''){
        		console.log('빈값일때');
        		
        		$.ajax({
        		    type:'POST',
        		    url:"<c:url value='/srvcrspons/mngr/retrieveRqester1stNmListAjax.do'/>",
        		    dataType: "json",
        			data : { 
        				"rqester1stNm":value
        		   			},
        			async:false,
        			success : function(request){
        				fncInitRqester1stNmDropdown(request)
        			},
        			error:function(r){
        				alert("정보전송에 실패하였습니다.");
        			}        			
        		});	
        	}
        }
        
        function fncInitRqester1stNmDropdown(request){
        	var rqester1stNmDropdownHTML = '';
        	console.log('request.returnMessage: '+request.returnMessage);
        	if(request.returnMessage == 'success'){
        		resultList = request.resultList;
        		for(var i=0;i<resultList.length; i++){
        			rqester1stNmDropdownHTML += "<a class=\"dropdown-item\" href=\"#\" id=\"rqesterNm"+(i+1);
        			rqester1stNmDropdownHTML +="\" onclick=\"fncSelectRqester1stNm('rqesterNm"+(i+1)+"')\" data-name=\"";
        			rqester1stNmDropdownHTML +=resultList[i].rqester1stNm+"\" data-psitn=\""+resultList[i].rqester1stPsitn;
            		rqester1stNmDropdownHTML +="\" data-cttpc=\""+resultList[i].rqester1stCttpc+"\" data-email=\""+resultList[i].rqester1stEmail+"\">";
            		rqester1stNmDropdownHTML += resultList[i].rqester1stNm+"("+resultList[i].rqester1stPsitn+")</a>";
            	}
			}else{
				rqester1stNmDropdownHTML = '<a class="dropdown-item" href="#">일치하는데이터없음</a>';
			}
        	console.log('rqester1stNmDropdownHTML: '+rqester1stNmDropdownHTML);
        	$('#rqester1stNmDropdown').html(rqester1stNmDropdownHTML);
        }
        
        function fncSelectRqester1stNm(objId){
        	document.listForm.elements['srvcRsponsVO.rqester1stNm'].value = $("#"+objId).data("name");;
    		document.listForm.elements['srvcRsponsVO.rqester1stPsitn'].value = $("#"+objId).data("psitn");
    		document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = $("#"+objId).data("cttpc");
    		document.listForm.elements['srvcRsponsVO.rqester1stEmail'].value = $("#"+objId).data("email");;
        }
        
        <%-- 요청 상세내용 복사 --%>
        function fncChkCopySrvcRsponsCn(obj){
        	if(obj.checked){
        		document.listForm.elements['srvcRsponsVO.srvcProcessDtls'].value = document.listForm.elements['srvcRsponsVO.srvcRsponsCn'].value;
        	}else{
        		document.listForm.elements['srvcRsponsVO.srvcProcessDtls'].value = '';
        	}
        }
        
        /* 글 목록 화면 function */
        function fncRetrieveList() {
        	document.listForm.action = "<c:url value='/srvcrspons/mngr/retrievePagingList.do'/>";
           	document.listForm.submit();
        }
        
        function fncDownloadFile(atchmnflId, atchmnflSn){
        	document.downloadForm.action = "<c:url value='/atchmnfl/site/retrieve.do'/>";
        	document.downloadForm.atchmnflId.value = atchmnflId;
        	document.downloadForm.atchmnflSn.value = atchmnflSn;
        	document.downloadForm.submit();
        }
        function fncDeleteFile(atchmnflId, atchmnflSn){
        	confirm('해당 파일을 삭제하시겠습니까?','파일 삭제', null, function(request){
        		console.log('request: '+request);
    			if(request){
    				$.ajax({
            		    type:'POST',
            		    url:"<c:url value='/atchmnfl/site/delete.do'/>",
            		    dataType: "json",
            			data : { 
            				"atchmnflId":atchmnflId,
            				"atchmnflSn":atchmnflSn
            		   			},
            			async:false,
            			success : function(request){
            				console.log('request.returnMessage: '+request.returnMessage);
            				if(request.returnMessage == 'success'){
            					console.log('ddd'+$('#fileId_'+atchmnflId+'_'+atchmnflSn));
            					$('#fileId_'+atchmnflId+'_'+atchmnflSn).remove(); 
            				}
            			},
            			error:function(r){
            				alert("정보전송에 실패하였습니다.");
            			}        			
            		});
    			}
        	});
        }
        //-->
    </script>
</head>
	

<body>
	<form name="downloadForm" action="<c:url value='/atchmnfl/site/retrieve.do'/>" target="">
		<input type="hidden" name="atchmnflId"/>
		<input type="hidden" name="atchmnflSn"/>
	</form>
		  <h1>SR 관리 <small><c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></small></h1>

          <div class="card">
          	<div class="card-header">  
          		<div class="form-row">
          			<div class="col">
          				SR 정보
          			</div>
          			<div class="col text-right">
          				<button type="button" class="btn btn-sm btn-primary" onclick="fncSave()">저 장</button>
          				<button type="button" class="btn btn-sm btn-primary" onclick="fncRetrieveList()">목 록</button>
          			</div>
          		</div>      	
			    
			</div>
			<form:form commandName="srvcRsponsFormVO" id="listForm" name="listForm" method="post" enctype="multipart/form-data">
		  	<form:hidden path="searchSrvcRsponsVO.pageIndex" />
		  	<form:hidden path="searchSrvcRsponsVO.srvcRsponsNo" />
		  	<form:hidden path="searchSrvcRsponsVO.srvcRsponsSj" />
		  	<form:hidden path="searchSrvcRsponsVO.trgetSrvcCode" />
		  	<form:hidden path="searchSrvcRsponsVO.processMt" />
		  	<form:hidden path="searchSrvcRsponsVO.srvcRsponsCn" />
		  	<form:hidden path="searchSrvcRsponsVO.srvcRsponsClCode" />
		  	<form:hidden path="srvcRsponsVO.requstAtchmnflId" id="requstAtchmnflId"/>
		  	<form:hidden path="srvcRsponsVO.rsponsAtchmnflId" id="rsponsAtchmnflId"/>
		  	<form:hidden path="srvcRsponsVO.saveToken" />
		  	
		  	<ul class="list-group list-group-flush">
			   <li class="list-group-item">요청정보</li>
			</ul>
          	<div class="card-body">
          		
<!--           		<h4 class="card-title">검색조건</h4> -->
<!--           		<h6 class="card-subtitle mb-2 text-muted">검색조건</h6> -->
					  
					  	
					  	<div class="form-row">
					  		<div class="col col-md-3 text-right">
					  			<label for="srvcRsponsVO.srvcRsponsNo">SR번호:</label>						
					  		</div>
					  		<div class="col col-md-3 text-left">
					  			<div class="form-row">
				  			  		<div class="col col-md-7 text-left">
								  		<form:input path="srvcRsponsVO.srvcRsponsNo" id="srvcRsponsNo" cssClass="form-control form-control-sm" cssStyle="ime-mode:disabled;" readonly="true" data-toggle="tooltip" data-placement="right" title="자동생성"/>
								  	</div>
								  	&nbsp;<form:errors path="srvcRsponsVO.srvcRsponsNo" />
							  	</div>
					  		</div>
					  		<div class="col col-md-3 text-right">
					  			<label for="srvcRsponsVO.requstDtDateDisplay">요청일시:</label>						
					  		</div>
					  		<div class="col col-md-3 text-left">
								  <div class="form-row">
				  			  		<div class="col col-md-6 text-left">
								  		<form:input path="srvcRsponsVO.requstDtDateDisplay" id="requstDtDateDisplay" placeholder="yyyy-MM-dd" cssClass="form-control form-control-sm" cssStyle="ime-mode:disabled;"/>
								  	</div>
								  	<div class="col col-md-3 text-left">
								  		<form:input path="srvcRsponsVO.requstDtTimeDisplay" id="requstDtTimeDisplay" placeholder="HH:mm" cssClass="form-control form-control-sm" cssStyle="ime-mode:disabled;"/>
								  	</div>
								  	&nbsp;<form:errors path="srvcRsponsVO.requstDtDateDisplay" />
								  	&nbsp;<form:errors path="srvcRsponsVO.requstDtTimeDisplay" />
							  	</div>
					  		</div>
					  	</div>
					  	<div class="form-row">
					  		<div class="col col-md-2 text-right">
					  			<label>요청자:</label>						
					  		</div>
					  		<div class="col col-md-4 text-left">
					  			<div class="form-row">
							  		<div class="col col-md-3 text-right">
							  			<label for="srvcRsponsVO.rqesterNm">성명</label>
							  		</div>
							  		<div class="col col-md-7 text-left">									
							  			<form:input path="srvcRsponsVO.rqesterNm" id="rqesterNm" data-toggle="dropdown" onkeyup="fncChangeRqesterNm(this.value)" cssClass="form-control form-control-sm" tabindex="1" cssStyle="ime-mode:active;"/>
							  			<div class="dropdown-menu" id="rqesterNmDropdown">
										  <a class="dropdown-item" href="#">일치하는데이터없음</a>
										</div>
							  		</div>
							  		&nbsp;<form:errors path="srvcRsponsVO.rqesterNm" />
						  		</div>
					  		</div>
					  		
					  		<div class="col col-md-2 text-right">
					  			<label>1차요청자:</label>						
					  		</div>
					  		<div class="col col-md-4 text-left">
					  			<div class="form-row">
						  			<div class="col col-md-3 text-right">
						  				<label for="srvcRsponsVO.rqester1stNm">성명</label>						
							  		</div>
							  		<div class="col col-md-7 text-left">
										<form:input path="srvcRsponsVO.rqester1stNm" id="rqester1stNm" data-toggle="dropdown" onkeyup="fncChangeRqester1stNm(this.value)" cssClass="form-control form-control-sm" tabindex="11" cssStyle="ime-mode:active;"/>
										<div class="dropdown-menu" id="rqester1stNmDropdown">
										  <a class="dropdown-item" href="#">일치하는데이터없음</a>
										</div>
							  		</div>
							  		&nbsp;<form:errors path="srvcRsponsVO.rqester1stNm" />
						  		</div>
					  		</div>					  		
					  	</div>
					  	<div class="form-row">
					  		<div class="col col-md-2 text-right">
					  							
					  		</div>
					  		<div class="col col-md-4 text-left">
					  			<div class="form-row">
							  		<div class="col col-md-3 text-right">
							  			<label for="srvcRsponsVO.rqesterPsitn">소속</label>						
							  		</div>
							  		<div class="col col-md-7 text-left">
										<form:input path="srvcRsponsVO.rqesterPsitn" cssClass="form-control form-control-sm" tabindex="2" cssStyle="ime-mode:active;"/>
							  		</div>
									  	&nbsp;<form:errors path="srvcRsponsVO.rqesterPsitn" />
						  		</div>
					  		</div>
					  		<div class="col col-md-2 text-right">
					  				(외부고객)				
					  		</div>
					  		<div class="col col-md-4 text-left">
					  			<div class="form-row">
						  			<div class="col col-md-3 text-right">
							  			<label for="srvcRsponsVO.rqester1stPsitn">소속</label>						
							  		</div>
							  		<div class="col col-md-7 text-left">
										<form:input path="srvcRsponsVO.rqester1stPsitn" cssClass="form-control form-control-sm" tabindex="12" cssStyle="ime-mode:active;"/>
							  		</div>
									  	&nbsp;<form:errors path="srvcRsponsVO.rqester1stPsitn" />
						  		</div>
					  		</div>
					  	</div>
					  	
					  	<div class="form-row">
					  		<div class="col col-md-2 text-right">
					  							
					  		</div>
					  		<div class="col col-md-4 text-left">
					  			<div class="form-row">
							  		<div class="col col-md-3 text-right">
						  				<label for="srvcRsponsVO.rqesterCttpc">연락처</label>						
							  		</div>
							  		<div class="col col-md-7 text-left">
										<form:input path="srvcRsponsVO.rqesterCttpc" id="rqesterCttpc" cssClass="form-control form-control-sm" tabindex="3" cssStyle="ime-mode:inactive;"/>
							  		</div>
									  	&nbsp;<form:errors path="srvcRsponsVO.rqesterCttpc" />
						  		</div>
					  		</div>
					  		<div class="col col-md-2 text-right">
					  			<input type="checkbox" name="chkCopyRqester" class="form-check-input" onclick="fncChkCopyRqester(this)"/>요청자동일 					
					  		</div>
					  		<div class="col col-md-4 text-left">
					  			<div class="form-row">
						  			<div class="col col-md-3 text-right">
							  			<label for="srvcRsponsVO.rqester1stCttpc">연락처</label>						
							  		</div>
							  		<div class="col col-md-7 text-left">
										<form:input path="srvcRsponsVO.rqester1stCttpc" cssClass="form-control form-control-sm" tabindex="13" cssStyle="ime-mode:inactive;"/>
							  		</div>
									  	&nbsp;<form:errors path="srvcRsponsVO.rqester1stCttpc" />
						  		</div>
					  		</div>
					  	</div>
					  	
					  	<div class="form-row">
					  		<div class="col col-md-2 text-right">
					  							
					  		</div>
					  		<div class="col col-md-4 text-left">
					  			<div class="form-row">
							  		<div class="col col-md-3 text-right">
							  			<label for="srvcRsponsVO.rqesterEmail">e-mail</label>						
							  		</div>
							  		<div class="col col-md-7 text-left">
										<form:input path="srvcRsponsVO.rqesterEmail" cssClass="form-control form-control-sm" tabindex="4" cssStyle="ime-mode:inactive;"/>
							  		</div>
									  	&nbsp;<form:errors path="srvcRsponsVO.rqesterEmail" />
						  		</div>
					  		</div>
					  		<div class="col col-md-2 text-right">
					  								
					  		</div>
					  		<div class="col col-md-4 text-left">
					  			<div class="form-row">
						  			<div class="col col-md-3 text-right">
							  			<label for="srvcRsponsVO.rqester1stEmail">e-mail</label>						
							  		</div>
							  		<div class="col col-md-7 text-left">
										<form:input path="srvcRsponsVO.rqester1stEmail" cssClass="form-control form-control-sm" tabindex="14" cssStyle="ime-mode:inactive;"/>
							  		</div>
									  	&nbsp;<form:errors path="srvcRsponsVO.rqester1stEmail" />
						  		</div>
					  		</div>
					  	</div>
					  	<div class="form-row">
					  		<div class="col col-md-3 text-right">
					  			<label for="srvcRsponsVO.trgetSrvcCode">대상서비스:</label>					
					  		</div>
					  		<div class="col col-md-4 text-left">
					  			<div class="form-row">
							  		<div class="col col-md-5 text-right">
							  			<form:select path="srvcRsponsVO.trgetSrvcCode" cssClass="form-control form-control-sm">
							  				<form:option value="">선택하세요</form:option>
							  				<form:options items="${trgetSrvcCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
							  			</form:select>							
							  		</div>
									  	&nbsp;<form:errors path="srvcRsponsVO.trgetSrvcCode" />
						  		</div>
					  		</div>
					  	</div>
		  	</div>
		  	<ul class="list-group list-group-flush">
			   <li class="list-group-item">요청사항(상세기술)</li>
			</ul>
				  	
		  	<div class="card-body">
		  		<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.srvcRsponsSj">요청 제목:</label>					
			  		</div>
			  		<div class="col col-md-9 text-left">
			  			<div class="form-row">
					  		<div class="col col-md-11 text-right">
					  			<form:input path="srvcRsponsVO.srvcRsponsSj" cssClass="form-control form-control-sm" tabindex="21" cssStyle="ime-mode:active;"/>							
					  		</div>
							  	&nbsp;<form:errors path="srvcRsponsVO.srvcRsponsSj" />
				  		</div>
			  		</div>
			  	</div>
			  	<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.srvcRsponsCn">요청 상세내용:</label>					
			  		</div>
			  		<div class="col col-md-9 text-left">
			  			<div class="form-row">
					  		<div class="col col-md-11 text-right">
					  			<form:textarea path="srvcRsponsVO.srvcRsponsCn" rows="5" cssClass="form-control form-control-sm" tabindex="22" cssStyle="ime-mode:active;"/>							
					  		</div>
							  	&nbsp;<form:errors path="srvcRsponsVO.srvcRsponsCn" />
				  		</div>
			  		</div>
			  	</div>
			  	<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.srvcRsponsCn">요청 첨부파일:</label>					
			  		</div>
			  		<div class="col col-md-8 text-left">
			  			 <div class="card">
							<ul class="list-group list-group-flush">
							   <li class="list-group-item text-right">
							      <div class="form-row">
							      	<div class="col col-md-4 text-left">
							      		<!-- The global progress bar -->
									    <div id="requstAtchmnfl-progress" class="progress">
									        <div class="progress-bar progress-bar-striped"></div>
									    </div>
									    <!-- The container for the uploaded files -->
							      	</div>
							      	<div class="col col-md-6 text-right">
							      		<!-- The fileinput-button span is used to style the file input field as button -->
									    <span class="btn btn-sm btn-success fileinput-button" id="attach">
									        <i class="glyphicon glyphicon-plus"></i>
									        <span>Add files...</span>
									        <!-- The file input field used as target for the file upload widget -->
									        <input id="requstAtchmnfl" type="file" name="multipartFile" multiple>
									    </span>
<!-- 									    <div class="files"></div> -->
							      	</div>
							      	
							      	<div class="col col-md-2 text-right">
							      		<button type="button" class="btn btn-sm btn-primary" id="requstAtchmnflSendBtn">파일전송</button>
							      	</div>
<!-- 							      	<div class="col col-md-2 text-right"> -->
<!-- 							      		<button type="button" class="btn btn-sm btn-primary" id="fileDeleteBtn" onclick="fncFileDelete()">파일삭제</button> -->
<!-- 							      	</div> -->
							      	
							      </div>
							   </li>
							</ul>
							<div class="card-body" id="requstAtchmnflFileListDiv">
								<c:forEach var="atchmnflVO" items="${requstAtchmnflList}">
									<div class="form-row" id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
										<a href="#" class="close" aria-label="Close" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
										<a href="#" class="font-weight-normal text-dark" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSize}"/>)</a>
									</div>
								</c:forEach>
							</div>
						</div>
			  		</div>
			  	</div>
		  	</div>
		  	<ul class="list-group list-group-flush">
			   <li class="list-group-item">처리 내용(상세기술)</li>
			</ul>
				  	
		  	<div class="card-body">
		  		<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.processDtDateDisplay">처리(적용)일시:</label>						
			  		</div>
			  		<div class="col col-md-3 text-left">
			  			<div class="form-row">
		  			  		<div class="col col-md-6 text-left">
						  		<form:input path="srvcRsponsVO.processDtDateDisplay" id="processDtDateDisplay" placeholder="yyyy-MM-dd" cssClass="form-control form-control-sm" cssStyle="ime-mode:disabled;"/>
						  	</div>
						  	<div class="col col-md-3 text-left">
						  		<form:input path="srvcRsponsVO.processDtTimeDisplay" id="processDtTimeDisplay" placeholder="HH:mm" cssClass="form-control form-control-sm" cssStyle="ime-mode:disabled;"/>
						  	</div>
						  	&nbsp;<form:errors path="srvcRsponsVO.processDtDateDisplay" />
						  	&nbsp;<form:errors path="srvcRsponsVO.processDtTimeDisplay" />
					  	</div>
			  		</div>
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.srvcRsponsClCode">SR구분:</label>						
			  		</div>
			  		<div class="col col-md-3 text-left">
						<div class="form-row">
					  		<div class="col col-md-8 text-right">
					  			<form:select path="srvcRsponsVO.srvcRsponsClCode" cssClass="form-control form-control-sm">
					  				<form:option value="">선택하세요</form:option>
					  				<form:options items="${srvcRsponsClCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
					  			</form:select>							
					  		</div>
							  	&nbsp;<form:errors path="srvcRsponsVO.trgetSrvcCode" />
				  		</div>
			  		</div>
			  	</div>
			  	<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.processDtDateDisplay">1차 응답일시:</label>						
			  		</div>
			  		<div class="col col-md-3 text-left">
			  			<div class="form-row">
		  			  		<div class="col col-md-6 text-left">
						  		<form:input path="srvcRsponsVO.rspons1stDtDateDisplay" id="rspons1stDtDateDisplay" placeholder="yyyy-MM-dd" cssClass="form-control form-control-sm" cssStyle="ime-mode:disabled;"/>
						  	</div>
						  	<div class="col col-md-3 text-left">
						  		<form:input path="srvcRsponsVO.rspons1stDtTimeDisplay" id="rspons1stDtTimeDisplay" placeholder="HH:mm" cssClass="form-control form-control-sm" cssStyle="ime-mode:disabled;"/>
						  	</div>
						  	&nbsp;<form:errors path="srvcRsponsVO.rspons1stDtDateDisplay" />
						  	&nbsp;<form:errors path="srvcRsponsVO.processDtTimeDisplay" />
					  	</div>
			  		</div>
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.requstDtDateDisplay">SR처리기준시간:</label>						
			  		</div>
			  		<div class="col col-md-3 text-left">
						<div class="form-row">
					  		<div class="col col-md-8 text-right">
					  			<form:select path="srvcRsponsVO.processStdrCode" cssClass="form-control form-control-sm">
					  				<form:option value="">선택하세요</form:option>
					  				<form:options items="${processStdrCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
					  			</form:select>							
					  		</div>
							  	&nbsp;<form:errors path="srvcRsponsVO.srvcRsponsClCode" />
				  		</div>
			  		</div>
			  	</div>
			  	<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.processDtDateDisplay">처리담당자:</label>						
			  		</div>
			  		<div class="col col-md-3 text-left">
			  			<div class="form-row">
		  			  		<div class="col col-md-6 text-left">
		  			  			<form:hidden path="srvcRsponsVO.chargerId"/>
						  		<form:input path="srvcRsponsVO.chargerUserNm" cssClass="form-control form-control-sm" readonly="true"/>
						  	</div>
						  	&nbsp;<form:errors path="srvcRsponsVO.processDtDateDisplay" />
						  	&nbsp;<form:errors path="srvcRsponsVO.processDtTimeDisplay" />
					  	</div>
			  		</div>
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.requstDtDateDisplay">만족도:</label>						
			  		</div>
			  		<div class="col col-md-3 text-left">
						<div class="form-row">
					  		<div class="col col-md-8 text-right">
					  			<form:select path="srvcRsponsVO.changeDfflyCode" cssClass="form-control form-control-sm">
					  				<form:option value="">선택하세요</form:option>
					  				<form:options items="${changeDfflyCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
					  			</form:select>							
					  		</div>
							  	&nbsp;<form:errors path="srvcRsponsVO.changeDfflyCode" />
				  		</div>
			  		</div>
			  	</div>
			  	<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.processDtDateDisplay">처리방법:</label>						
			  		</div>
			  		<div class="col col-md-9 text-left">
			  			<div class="form-row">
		  			  		<div class="col col-md-11 text-left">
		  			  			<label class="form-check-label">		  			  			
		  			  				<form:checkbox path="srvcRsponsVO.dataUpdtYn" cssClass="form-check-input" value="Y"/>
		  			  				데이터 수정
		  			  			</label>
						  		<label class="form-check-label">		  			  			
		  			  				<form:checkbox path="srvcRsponsVO.progrmUpdtYn" cssClass="form-check-input" value="Y"/>
		  			  				프로그램 수정
		  			  			</label>
		  			  			<%-- <label class="form-check-label">		  			  			
		  			  				<form:checkbox path="srvcRsponsVO.stopInstlYn" cssClass="form-check-input" value="Y"/>
		  			  				배포(시스템 중단없음)
		  			  			</label>
		  			  			<label class="form-check-label">		  			  			
		  			  				<form:checkbox path="srvcRsponsVO.noneStopInstlYn" cssClass="form-check-input" value="Y"/>
		  			  				배포(시스템중단)
		  			  			</label> --%>
		  			  			<label class="form-check-label">		  			  			
		  			  				<form:checkbox path="srvcRsponsVO.instlYn" cssClass="form-check-input" value="Y"/>
		  			  				배포
		  			  			</label>
		  			  			<label class="form-check-label">		  			  			
		  			  				<form:checkbox path="srvcRsponsVO.infraOpertYn" cssClass="form-check-input" value="Y"/>
		  			  				인프라작업
		  			  			</label>
						  	</div>
						  	&nbsp;<form:errors path="srvcRsponsVO.processDtDateDisplay" />
						  	&nbsp;<form:errors path="srvcRsponsVO.processDtTimeDisplay" />
					  	</div>
			  		</div>
			  	</div>
			  	<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.srvcProcessDtls">
			  				처리내역:
			  				<br/>
			  				<input type="checkbox" name="chkCopySrvcRsponsCn" class="form-check-input" onclick="fncChkCopySrvcRsponsCn(this)"/>요청내용복사
			  			</label>
			  				
			  							
			  		</div>
			  		<div class="col col-md-9 text-left">
			  			<div class="form-row">
					  		<div class="col col-md-11 text-right">
					  			<form:textarea path="srvcRsponsVO.srvcProcessDtls" rows="5" cssClass="form-control form-control-sm" cssStyle="ime-mode:active;"/>							
					  		</div>
							  	&nbsp;<form:errors path="srvcRsponsVO.srvcProcessDtls" />
				  		</div>
			  		</div>
			  	</div>
			  	<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.srvcProcessDtls">기타:</label>					
			  		</div>
			  		<div class="col col-md-9 text-left">
			  			<div class="form-row">
					  		<div class="col col-md-11 text-right">
					  			<form:textarea path="srvcRsponsVO.etc" rows="3" cssClass="form-control form-control-sm" cssStyle="ime-mode:active;"/>							
					  		</div>
							  	&nbsp;<form:errors path="srvcRsponsVO.etc" />
				  		</div>
			  		</div>
			  	</div>
			  	<div class="form-row">
			  		<div class="col col-md-3 text-right">
			  			<label for="srvcRsponsVO.srvcRsponsCn">요청 첨부파일:</label>					
			  		</div>
			  		<div class="col col-md-9 text-left">
			  			<div class="form-row">
					  		
				  		</div>
			  		</div>
			  	</div>
		  	</div>
		  	</form:form>
		  	<div class="card-footer text-right">
		  		<button type="button" class="btn btn-sm btn-primary" onclick="fncSave()">저 장</button>
		  		<button type="button" class="btn btn-sm btn-primary" onclick="fncRetrieveList()">목 록</button>
		  	</div>
		  </div>  
    
    
</body>
</html>
