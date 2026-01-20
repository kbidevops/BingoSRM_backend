<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
  /**
  * @Class Name : /itsm/user/site/edit.jsp
  * @Description : 사용자 등록/수정 화면
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
    <c:set var="registerFlag" value="${empty userFormVO.userVO.userId ? 'create' : 'modify'}"/>
    <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></title>
    <!--For Commons Validator Client Side-->
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <validator:javascript formName="userFormVO" staticJavascript="false" xhtml="true" cdata="false"/>
	<script type="text/javaScript" language="javascript" defer="defer">
        <!--
        $(document).ready(function() {
        	 $("#moblphon").mask("999-9999-9999");
        	 
        	<c:if test="${registerFlag == 'create'}">
        		document.listForm.elements['userVO.changePasswordYN'].checked=true;
        		fncChangePasswordYN(document.listForm.elements['userVO.changePasswordYN']);
        	</c:if>
        	<c:if test="${registerFlag != 'create'}">
        		$('.pwbox').removeClass('d-none');
        	</c:if>
        		
//         	$(".form-control").keydown(function(event) {
//         		if ( event.which == 13 ) {
//         			fncRetrieveList();
//         		}
//         	});
        });
        
        function fncSave() {
        	frm = document.listForm;
        	<c:if test="${registerFlag == 'create'}">
        	 if(!fncCheckDuo()){
        		 return;
        	 }
        	 </c:if>
        	if(document.listForm.elements['userVO.changePasswordYN'].checked){
        		var userPassword = document.listForm.elements['userVO.userPassword'].value;
        		var userPasswordConfirm = document.listForm.elements['userPasswordConfirm'].value;
        		userPassword = $.trim(userPassword);
        		userPasswordConfirm = $.trim(userPasswordConfirm);
        		
        		if(userPassword == ''){
        			alert('비밀번호를 입력하세요.');
        			return;
        		}
        		
        		if(userPasswordConfirm == ''){
        			alert('비밀번호 확인을 입력하세요.');
        			return;
        		}
        		
        		if(userPassword != userPasswordConfirm){
        			alert('비밀번호가 일치하지 않습니다.');
        			return;
        		}        		
        	}
        	
        	if(!validateUserFormVO(frm)){
                return;
            }else{
            	confirm('저장하시겠습니까?','사용자 정보 저장', null, function(request){
            		console.log('request: '+request);
            		if(request){
            			frm.action = "<c:url value="${registerFlag == 'create' ? '/user/site/create.do' : '/user/site/update.do'}"/>";
                        frm.submit();
            		}
            	});
            }
        }
        
        function fncChangePasswordYN(obj){
        	if(obj.checked){
        		document.listForm.elements['userVO.userPassword'].readOnly=false;
        		document.listForm.elements['userPasswordConfirm'].readOnly=false;
        		document.listForm.elements['userVO.userPassword'].value='';
        		document.listForm.elements['userPasswordConfirm'].value='';
        	}else{
        		document.listForm.elements['userVO.userPassword'].readOnly=true;
        		document.listForm.elements['userPasswordConfirm'].readOnly=true;
        		
        	}
        }
        
        function fncCheckDuo(){
    		if(document.listForm.checkDuo.value=='false'){
    			alert("중복된 사용자ID인지 확인이 되지 않았습니다.");
    			return false;
    		}else{
    			return true;
    		}
    	}
    	
    	/* 사용자ID 중복 체크 */
    	function fncRetrieveAjax(){
    		var userId = document.listForm.elements['userVO.userId'].value;
    		
    		if(jQuery.trim(userId) == ''){
    			alert('사용자ID를 입력하세요.');
    			return;
    		}
    		
    		$.ajax({
    		    type:'GET',
    		    url:"<c:url value='/user/site/retrieveAjax.do'/>",
    		    dataType: "json",
    			data : { "userId":userId
    		   			},
    			async:false,
    			success : function(request){
    				if(request.returnMessage == 'success'){
    					document.listForm.checkDuo.value='true';
    					alert("등록가능한 사용자ID입니다.");
    				}else{
    					document.listForm.checkDuo.value='false';
    					alert("중복된 사용자ID입니다.");	
    				}
    			},
    			
    			error:function(r){
    				alert("정보전송에 실패하였습니다.");
    			}
    			
    		});		
    	}
       	
    	/* 글 목록 화면 function */
        function fncLoginView() {
        	document.listForm.action = "<c:url value='/login/site/loginView.do'/>";
           	document.listForm.submit();
        }
    	
        //-->
    </script>
</head>
	
<body>
<main id="content" class="mini">
	<div class="page-header">
       <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
    </div>
	<c:if test="${registerFlag == 'create'}">
		  	<%-- 회원가입에 대한 간단한 안내 메시지 추가 --%>
		<div class="guidebox">
         	<p>
		                        ITSM은 용역 사용자와 기관 사용자와의 업무상 필요에 의해 사용하도록 제한되어 있습니다.<br/>
		                        본 시스템과 관련되지 않으신 분은 가입신청 후 승인이 안될 수 있습니다.
         	</p>
   		</div>
	</c:if> 
	
	<c:if test="${userFormVO.userVO.userSttusCode == userFormVO.userVO.USER_STTUS_CODE_WAIT}">
		<div class="guidebox">
         	<p>
		                       승인대기상태 입니다.<br/>
		                        관리자에 의해 승인 후 사용가능합니다. 빠른 처리를 원하시면 유선 또는 메일을 통해 관리자에게 접속 허용 요청하시기 바랍니다.
         	</p>
    	</div>
	</c:if>
	
	<form:form commandName="userFormVO" id="listForm" name="listForm" method="post">
		<form:hidden path="searchUserVO.userId" />
		<form:hidden path="searchUserVO.userNm" />
		<form:hidden path="searchUserVO.userTyCode" />
		<form:hidden path="searchUserVO.psitn" />
		<form:hidden path="searchUserVO.clsf" />
		<form:hidden path="searchUserVO.userSttusCode" />
		<form:hidden path="userVO.saveToken" />
		<input type="hidden" name="checkDuo" value="false"/>
	
   		<div class="card userbox">
                <div class="detailbox type1">
                        <dl>
                            <dt>사용자 아이디</dt>
                            <c:if test="${registerFlag != 'create'}">
							  	<form:hidden path="userVO.userId"/>
							  	<dd><c:out value="${userFormVO.userVO.userId}"/></dd>
							</c:if>
                            <c:if test="${registerFlag == 'create'}">
                            <dd>
                                <div class="input-group">
                                    <form:input path="userVO.userId" cssClass="control" title="사용자아이디" cssStyle="ime-mode:disabled;"/>
                                    <input type="button" name="email_check" onclick="fncRetrieveAjax()" class="btn btn-inline" value="중복확인"/>
                                </div>
                                <form:errors path="userVO.userId" />
                            </dd>
                            </c:if>
                            <dt>성명</dt>
                            <dd>
                            <form:input path="userVO.userNm" cssClass="control" cssStyle="ime-mode:active;" title="성명"/>
			  				&nbsp;<form:errors path="userVO.userNm" />
                            </dd>
                        </dl>
                        <dl>
                            <dt>비밀번호
                            	<span class="pwbox d-none">
	                                <!-- 회원정보수정시에만 나타남 -->
	                                <input type="checkbox" id="changePasswordYN" name="userVO.changePasswordYN" onclick="fncChangePasswordYN(this)" class="checkbox" value="Y"/>
	                                <label for="changePasswordYN">변경</label>
	                                <!-- //회원정보수정시에만 나타남 -->
	                            </span>
                            </dt>
                            <dd><form:password path="userVO.userPassword" cssClass="control" readonly="true" title="비밀번호"/>
                             &nbsp;<form:errors path="userVO.userPassword" /></dd>
                            <dt>비밀번호 확인</dt>
                            <dd><input type="password" name="userPasswordConfirm" class="control" readonly="readonly" style="ime-mode:disabled;"/></dd>
                        </dl>
                        <dl>
                            <dt>이메일</dt>
                            <dd><form:input path="userVO.email" cssClass="control" title="이메일"/>
								  &nbsp;<form:errors path="userVO.email" /></dd>
                            <dt>연락처(휴대폰)</dt>
                            <dd><form:input path="userVO.moblphon" id="moblphon" class="control" title="연락처(휴대폰)"/>
								  &nbsp;<form:errors path="userVO.moblphon" /></dd>
                        </dl>
                        <dl>
                            <dt>소속</dt>
                            <dd><form:input path="userVO.psitn" cssClass="control" title="소속"/>
								  &nbsp;<form:errors path="userVO.psitn" /></dd>
                            <dt>직급</dt>
                            <dd><form:input path="userVO.clsf" class="control" title="직급"/>
								  &nbsp;<form:errors path="userVO.clsf" /></dd>
                        </dl>
                    </div>
                    <hr>
                    <div class="detailbox type1">
                        <dl>
                            <dt>계정신청사유/비고</dt>
                            <dd><form:textarea path="userVO.acntReqstResn" class="control" title="계정신청사유/비고" rows="5"/>
								  &nbsp;<form:errors path="userVO.acntReqstResn" /></dd>
                        </dl>
                    </div>
            </div>
        </form:form>
		  	
		  	<div class="btnbox center mb30">
		  		<c:if test="${userFormVO.userVO.userSttusCode == userFormVO.userVO.USER_STTUS_CODE_WAIT}">
               		<button type="button" class="btn btn-dark" onclick="fncLoginView()"><i class="ico-login"></i>로그인 화면으로</button>
                </c:if>
                <button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
		  	</div>
    
</main> 
</body>
</html>
