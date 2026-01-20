<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
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
    <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></title>
    <!--For Commons Validator Client Side-->
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <validator:javascript formName="srvcRsponsFormVO" staticJavascript="false" xhtml="true" cdata="false"/>
	<script type="text/javaScript" language="javascript" defer="defer">
        <!--
        var rsponsAtchmnflFileCnt = 0;
        $(document).ready(function() {
        	var url = '<c:url value='/atchmnfl/site/create.do'/>';
        	$('#rsponsAtchmnflSendBtn').hide();
        	
        	$("#srvcRsponsClCode").on("change", function(){
       		 var srvcRsponsClCode = $(this).val();
       		 if(srvcRsponsClCode == "S104"){
       			 $("#processStdrCode").children("option[value='S204']").prop("selected",true);
       			 $("#processStdrCode").prop("disabled", true);
       		 }else if(srvcRsponsClCode == "S101" || srvcRsponsClCode == "S102"){
       			 $("#processStdrCode").children("option[value='S201']").prop("selected",true);
       			 $("#processStdrCode").prop("disabled", true);
       		 }else if(srvcRsponsClCode == "S105" || srvcRsponsClCode == "S106"){
       			 $("#processStdrCode").children("option[value='S202']").prop("selected",true);
       			 $("#processStdrCode").prop("disabled", true);
       		 }else if(srvcRsponsClCode == "S103"){
       			 $("#processStdrCode").children("option[value='S202']").prop("selected",true);
       			 $("#processStdrCode").children("option[value='S201']").prop("disabled",true);
       			 $("#processStdrCode").prop("disabled", false);
       		 }else{
       			 $("#processStdrCode").prop("disabled", false);
       		 }
       	 });
        	
        	$('#rsponsAtchmnfl').fileupload({
       		 url: url,
       		 dataType: 'json',
       		 formData: {atchmnflId: $("#rsponsAtchmnflId").val()},
       		 sequentialUploads: true,
       		 add: function (e, data) {
       			 var valid = true;
       			 var re = /^.+\.((doc)|(xls)|(xlsx)|(docx)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(gif)|(png)|(bmp))$/i;
       			 
       			 $.each(data.files, function (index, file) {
      			        console.log('rsponsAtchmnfl Added file: ' + file.name);
      			        console.log('rsponsAtchmnfl Added file: ' + file.type);
      			        console.log('rsponsAtchmnfl Added file: ' + file.size);
      			        console.log('rsponsAtchmnfl Added file: ' + filesize(file.size));
//       			        console.log('file size: ' + data.getNumberOfFiles());
      			        
      			        rsponsAtchmnflFileCnt++;
      			        
      			     	if (!re.test(file.name)) {
      			     		valid = false;
      			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
      			     		rsponsAtchmnflFileCnt--;
      			     	}else if (file.size > 5000000){ //5M
      			     	    console.log('5M이하의 파일만 허용 합니다.');
      			     		alert(file.name+' <br/>5M이상의 첨부파일은 제외됩니다.');
      			     		rsponsAtchmnflFileCnt--;
      			     		valid = false;
      			     	}else if (rsponsAtchmnflFileCnt>5){ //동시 업로드 5개 제한
	       			     	console.log('5개의 파일만 허용 합니다.'+rsponsAtchmnflFileCnt);
	   			     		alert(file.name+' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
	   			     		rsponsAtchmnflFileCnt--;
	   			     		valid = false;
      			     	}else{
      			     		console.log('rsponsAtchmnfl 정상등록 '+rsponsAtchmnflFileCnt);
      			     		var abortBtn = $( '<a/>' )
			                     .attr( 'href', 'javascript:void(0)' )
			                     .attr( 'class', 'close' )
			                     .attr( 'aria-label', 'Close' )
			                     .append( '<span aria-hidden="true">&times;</span>' )
			                     .click( function() {
				                    	data.abort();
				                        data.context.remove();
				                        data = null;
				                        if(rsponsAtchmnflFileCnt>0){						                        	
				                        	rsponsAtchmnflFileCnt--;
				                        	console.log('rsponsAtchmnflFileCnt: '+rsponsAtchmnflFileCnt);
				                        }
			                     } );
      			     		
        			         data.context = $( '<div/>' ).appendTo($('#rsponsAtchmnflFileListDiv'));
          			         data.context.attr( 'class', 'form-row' );
          			         data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
      			     	}
      			        
      			     });
       			 
       			 $("#rsponsAtchmnflSendBtn").click(function () {        				   
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
//        		        var progress = parseInt(data.percent * 100, 10);
       		        var progress = parseInt(data.loaded / data.total * 100, 10);
       		        console.log('data.bitrate:'+data.bitrate);
       		        console.log('data.bitrate:'+progress);
       		        $('#rsponsAtchmnfl-progress .progress-bar').css(
       		            'width',
       		            progress+ '%'
       		        );
       		        $('#rsponsAtchmnfl-progress .progress-bar').html(progress+ '%');
       		        
       		        if(progress == 100){
       		        	frm.action = "<c:url value='/srvcrspons/mngr/updateState.do'/>";
       	                frm.submit();
       		        }
       		    },
       		    progressServerRate: 0.3

       	 	});
        });
        function fncSave() {
        	frm = document.listForm;
        	
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
        	if(document.listForm.elements['srvcRsponsVO.srvcProcessDtls'].value == ''){
        		alert('처리내역을 입력하세요.');
    			return;
        	}
        	
        	confirm('완료처리하시겠습니까?','SR요청 완료처리', null, function(request){
        		console.log('request: '+request);
        		if(request){
        			$("#processStdrCode").prop("disabled", false);
        			if(rsponsAtchmnflFileCnt>0){
                		$("#rsponsAtchmnflSendBtn").trigger("click");
                	}else{
			        	frm.action = "<c:url value='/srvcrspons/mngr/updateState.do'/>";
			            frm.submit();
                	}
        		}
        	});
        }
        
        function fncDelete(){
        	confirm('삭제하시겠습니까?','SR요청 삭제', null, function(request){
    			console.log('request: '+request);
    			if(request){
    				document.listForm.action = "<c:url value='/srvcrspons/mngr/delete.do'/>";
    	           	document.listForm.submit();
    			}
    		});
        }
        
        function fncChkCopySrvcRsponsCn(obj){
        	if(obj.checked){
        		document.listForm.elements['srvcRsponsVO.srvcProcessDtls'].value = document.listForm.elements['srvcRsponsVO.srvcRsponsCn'].value;
        	}else{
        		document.listForm.elements['srvcRsponsVO.srvcProcessDtls'].value = '';
        	}
        }
        
        /* 글 목록 화면 function */
        function fncRetrieveList() {
        	<c:if test="${empty srvcRsponsFormVO.returnListMode}">
	    		document.listForm.action = "<c:url value='/srvcrspons/mngr/retrievePagingList.do'/>";
	    	</c:if>
	    	<c:if test="${!empty srvcRsponsFormVO.returnListMode}">
	    		document.listForm.action = "<c:url value='/srvcrspons/mngr/retrieveList.do'/>";
	    	</c:if>
           	document.listForm.submit();
        }
        
        function fncDownloadFile(atchmnflId, atchmnflSn){
        	document.downloadForm.action = "<c:url value='/atchmnfl/site/retrieve.do'/>";
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
    	    		    url:"<c:url value='/atchmnfl/site/delete.do'/>",
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
        //-->
    </script>
</head>
	

<body>
	<main id="content">
	<form name="downloadForm" action="<c:url value='/atchmnfl/site/retrieve.do'/>" target="">
		<input type="hidden" name="atchmnflId" id="atchmnflId"/>
		<input type="hidden" name="atchmnflSn" id="atchmnflSn"/>
	</form>
        <div class="page-header">
            <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></h3>
            <div class="btnbox fr">
                <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
                <button type="button" onclick="fncDelete()" class="btn btn-dark"><i class="ico-delete"></i>삭제</button>
                <button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>완료처리</button>
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
		  	<form:hidden path="searchSrvcRsponsVO.excludeprocessYn" />
		  	<form:hidden path="srvcRsponsVO.requstAtchmnflId" id="requstAtchmnflId"/>
		  	<form:hidden path="srvcRsponsVO.rsponsAtchmnflId" id="rsponsAtchmnflId"/>
		  	<form:hidden path="srvcRsponsVO.saveToken" />
		  	<form:hidden path="srvcRsponsVO.srvcRsponsNo" />
		  	<form:hidden path="srvcRsponsVO.srvcRsponsCn" />
		  	<form:hidden path="srvcRsponsVO.srvcRsponsBasisCode" />
		  	<form:hidden path="srvcRsponsVO.rqesterId" />
		  	<form:hidden path="returnListMode" />
		  	
        <div class="row srarea equalheight">
            <div class="col-left">
                <div class="card">
                    <div class="card-header">
                        <h4 class="card-title">SR 정보</h4>
                        <dl class="sr-info">
                            <dt></dt>
                            <dd>${srvcRsponsFormVO.srvcRsponsVO.srvcRsponsNo }</dd>
                            <dt>요청일시</dt>
                            <dd>
                                <div class="input-group datetime">
                                    <c:out value="${srvcRsponsFormVO.srvcRsponsVO.requstDtDateDisplay}"/>
						  	 		<c:out value="${srvcRsponsFormVO.srvcRsponsVO.requstDtTimeDisplay}"/>
						  	 	</div>
                            </dd>
                        </dl>
                        <dl class="sr-info">
                            <dt>대상서비스</dt>
                            <dd>
                            <c:out value="${srvcRsponsFormVO.srvcRsponsVO.trgetSrvcCodeNm}"/>
                            </dd>
                            <dt>SR근거</dt>
                            <dd>
                              <c:out value="${srvcRsponsFormVO.srvcRsponsVO.srvcRsponsBasisCodeNm}"/>
                            </dd>
                        </dl>
                    </div>
                </div>

                <div class="row">
                    <div class="col">
                        <!-- 요청자정보 -->
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">요청자 정보</h5>
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>성명</dt>
                                        <dd>
                                        <c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqesterNm}"/>	
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>소속</dt>
                                        <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqesterPsitn}"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>연락처</dt>
                                        <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqesterCttpc}"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>이메일</dt>
                                        <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqesterEmail}"/></dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
                        <!-- //요청자정보 -->
                    </div>
                    <div class="col">
                        <!-- 1차 요청자 -->
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">1차 요청자(외부고객)</h5>
                                <div class="control-group mt5">
                                    <input type="checkbox" name="chkCopyRqester" id="chk1" class="checkbox" onclick="fncChkCopyRqester(this)"/>
                                    <label for="chk1">요청자와 동일</label>
                                </div>
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>성명</dt>
                                        <dd>
                                        <c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqester1stNm}"/>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>소속</dt>
                                        <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqester1stPsitn}"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>연락처</dt>
                                        <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqester1stCttpc}"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>이메일</dt>
                                        <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqester1stEmail}"/></dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
                        <!-- //1차 요청자 -->
                    </div>
                    <div class="col-12">
                        <!-- 요청사항 -->
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">요청사항(상세기술)</h5>
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>요청제목</dt>
                                        <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.srvcRsponsSj}"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>요청상세내용</dt>
                                        <dd>${fn:replace(srvcRsponsFormVO.srvcRsponsVO.srvcRsponsCn, newLineChar, "<br/>")}</dd>
                                    </dl>
                                    <dl>
                                        <dt>요청첨부파일</dt>
                                        <dd class="d-block">
											<ul id="requstAtchmnflFileListDiv" class="file-list-group">
												<c:forEach var="atchmnflVO" items="${requstAtchmnflList}">
													<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
														<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
														<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
													</li>
												</c:forEach>
												<c:if test="${empty requstAtchmnflList}">
													첨부파일 없음.
												</c:if>
											</ul>
                                         </dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
                        <!-- //요청사항 -->
                    </div>
                </div>
            </div>
            <div class="col-right">

                <!-- SR 처리 내용 -->
                <div class="card">
                    <div class="card-header type2">
                        <h5 class="card-title">SR 처리 내용(상세기술)</h5>
                    </div>
                    <div class="card-body">
                        <div class="detailbox type2">
                            <dl>
                                <dt>담당</dt>
                                <dd><strong>${srvcRsponsFormVO.srvcRsponsVO.chargerUserNm }</strong></dd>
                            </dl>
                        </div>
                        <hr>
                        <div class="detailbox type2">
                            <dl>
                                <dt>1차 응답일시</dt>
                                <dd>
                                    <div class="input-group">
                                       <form:input path="srvcRsponsVO.rspons1stDtDateDisplay" id="rspons1stDtDateDisplay"  class="control inpdate" title="요청일" placeholder="YYYY-MM-DD"/>
                                       <form:input path="srvcRsponsVO.rspons1stDtTimeDisplay" id="rspons1stDtTimeDisplay" class="control inptime" title="요청시간" placeholder="HH:MM"/>
                                    </div>
                                </dd>
                            </dl>
                            <dl>
                                <dt>2차 응답일시</dt>
                                <dd>
                                    <div class="input-group">
                                        <form:input path="srvcRsponsVO.processDtDateDisplay" id="processDtDateDisplay" class="control inpdate" title="요청일" placeholder="YYYY-MM-DD"/>
                                        <form:input path="srvcRsponsVO.processDtTimeDisplay" id="processDtTimeDisplay" class="control inptime" title="요청시간" placeholder="HH:MM"/>
                                    </div>
                                </dd>
                            </dl>
                        </div>
                        <hr>
                        <div class="detailbox type2">
                            <dl>
                                <dt>SR 구분</dt>
                                <dd>
                                  <form:select id="srvcRsponsClCode" path="srvcRsponsVO.srvcRsponsClCode" cssClass="control" title="SR 구분">
					  				<form:option value="">선택하세요</form:option>
					  				<form:options items="${srvcRsponsClCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
					  			  </form:select>
                                </dd>
                            </dl>
                            <dl>
                                <dt>SR처리기준시간</dt>
                                <dd>
                                  <form:select id="processStdrCode" path="srvcRsponsVO.processStdrCode" cssClass="control" title="SR처리기준시간">
					  				<form:option value="">선택하세요</form:option>
					  				<form:options items="${processStdrCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
					  			  </form:select>	
                                </dd>
                            </dl>
                            <dl class="col-4">
                                <dt>만족도</dt>
                                <dd>
                                  <form:select path="srvcRsponsVO.changeDfflyCode" cssClass="control">
					  				<form:option value="">선택하세요</form:option>
					  				<form:options items="${changeDfflyCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
					  			  </form:select>
                                </dd>
                            </dl>
                        </div>
                        <hr>
                        <div class="detailbox type2"> 
                            <dl>
                                <dt>처리방법</dt>
                                <dd>
                                    <ul class="checklist">
                                        <li>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.dataUpdtYn != 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.dataUpdtYn" id="dataUpdtYn" class="checkbox" value="Y"/>
                                        	</c:if>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.dataUpdtYn == 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.dataUpdtYn" id="dataUpdtYn" class="checkbox" value="Y" checked/>
                                        	</c:if>
                                            <label for="dataUpdtYn">데이터수정</label>
                                        </li>
                                        <li>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.progrmUpdtYn != 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.progrmUpdtYn" id="progrmUpdtYn" class="checkbox" value="Y"/>
											</c:if>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.progrmUpdtYn == 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.progrmUpdtYn" id="progrmUpdtYn" class="checkbox" value="Y" checked/>
											</c:if>
                                            <label for="progrmUpdtYn">프로그램수정</label>
                                        </li>
                                        <%-- <li>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.stopInstlYn != 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.stopInstlYn" id="stopInstlYn" class="checkbox" value="Y"/>
											</c:if>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.stopInstlYn == 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.stopInstlYn" id="stopInstlYn" class="checkbox" value="Y" checked/>
											</c:if>
                                            <label for="stopInstlYn">배포(시스템중단없음)</label>
                                        </li>
                                        <li>
											<c:if test="${srvcRsponsFormVO.srvcRsponsVO.noneStopInstlYn != 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.noneStopInstlYn" id="noneStopInstlYn" class="checkbox" value="Y"/>
											</c:if>
											<c:if test="${srvcRsponsFormVO.srvcRsponsVO.noneStopInstlYn == 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.noneStopInstlYn" id="noneStopInstlYn" class="checkbox" value="Y" checked/>
											</c:if>
                                            <label for="noneStopInstlYn">배포(시스템중단)</label>
                                        </li> --%>
                                        <li>
											<c:if test="${srvcRsponsFormVO.srvcRsponsVO.instlYn != 'Y' 
														&& srvcRsponsFormVO.srvcRsponsVO.noneStopInstlYn != 'Y'
														&& srvcRsponsFormVO.srvcRsponsVO.stopInstlYn != 'Y'}">
											<input type="checkbox" name="srvcRsponsVO.instlYn" id="instlYn" class="checkbox" value="Y"/>
											</c:if>
											<c:if test="${srvcRsponsFormVO.srvcRsponsVO.instlYn == 'Y' 
														|| srvcRsponsFormVO.srvcRsponsVO.noneStopInstlYn == 'Y'
														|| srvcRsponsFormVO.srvcRsponsVO.stopInstlYn == 'Y'}">
											<input type="checkbox" name="srvcRsponsVO.instlYn" id="instlYn" class="checkbox" value="Y" checked/>
											</c:if>
                                            <label for="instlYn">배포</label>
                                        </li>
                                        <li>
											<c:if test="${srvcRsponsFormVO.srvcRsponsVO.infraOpertYn != 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.infraOpertYn" id="infraOpertYn" class="checkbox" value="Y"/>
											</c:if>
											<c:if test="${srvcRsponsFormVO.srvcRsponsVO.infraOpertYn == 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.infraOpertYn" id="infraOpertYn" class="checkbox" value="Y" checked/>
											</c:if>
                                            <label for="infraOpertYn">인프라작업</label>
                                        </li>
                                    </ul>
                                </dd>
                            </dl>
                        </div>
                        <hr>
                        <div class="detailbox type2">
                            <dl>
                                <dt>처리내역
                                    <div class="fr">
                                        <input type="checkbox" name="chkCopySrvcRsponsCn" onclick="fncChkCopySrvcRsponsCn(this)" id="chk2" class="checkbox"/>
                                        <label for="chk2">요청내용복사</label>
                                    </div>
                                </dt>
                                <dd><form:textarea path="srvcRsponsVO.srvcProcessDtls"  class="control" title="처리내역" rows="5"></form:textarea></dd>
                            </dl>
                        </div>
                        <div class="detailbox type2">
                            <dl>
                                <dt>기타</dt>
                                <dd><form:textarea path="srvcRsponsVO.etc" class="control" title="기타" rows="2"></form:textarea></dd>
                            </dl>
                        </div>
                        <div class="detailbox type2">
                            <dl>
                                <dt>응답첨부파일</dt>
                                <dd class="d-block">
	                                	<div class="file-box">
	                                   
										<!-- The fileinput-button span is used to style the file input field as button -->
									    <span class="btn btn-secondary fileinput-button">
									    	<i class="ico-search"></i>
									        파일찾기
									        <!-- The file input field used as target for the file upload widget -->
									        <input id="rsponsAtchmnfl" type="file" name="multipartFile" multiple>
									    </span>
									</div>
									<button type="button" class="btn btn-sm btn-primary" id="rsponsAtchmnflSendBtn">파일전송</button>
									<ul id="rsponsAtchmnflFileListDiv" class="file-list-group">
										<c:forEach var="atchmnflVO" items="${rsponsAtchmnflList}">
											<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
												<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
												<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
											</li>
										</c:forEach>
									</ul>
                                </dd>
                            </dl>
                        </div>
                        <hr>
                        <div class="detailbox type2">
                            <dl>
                                <dt>기능개선</dt>
                                <dd><form:input readonly="true" path="srvcRsponsVO.fnctImprvmNo" id="fnctImprvmNo" class="control" title="기능개선"/></dd>
                            </dl>
                            <dl>
                                <dt>배포확인서</dt>
                                <dd><form:input readonly="true" path="srvcRsponsVO.wdtbCnfirmNo" id="wdtbCnfirmNo" class="control" title="배포확인서"/></dd>
                            </dl>
                        </div>
                    </div>
                </div>
                <!-- //SR 처리 내용 -->

            </div>
        </div>
		</form:form>
		
        <div class="btnbox right mb30">
            <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
            <button type="button" onclick="fncDelete()" class="btn btn-dark"><i class="ico-delete"></i>삭제</button>
            <button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>완료처리</button>
        </div>
    </main>
    
</body>
</html>
