<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
  /**
  * @Class Name : /itsm/user/mngr/edit.jsp
  * @Description : 사용자 관리 등록/수정 화면
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
        		$('#changePasswordYN').hide();
        	</c:if>
//         	$(".form-control").keydown(function(event) {
//         		if ( event.which == 13 ) {
//         			fncRetrieveList();
//         		}
//         	});
        });
        
        
        function fncDelete() {
            confirm('삭제하시겠습니까?', '사용자 삭제', null, function(request) {
              console.log('request: ' + request);
              if (request) {
                document.listForm.action = "<c:url value='/user/mngr/delete.do'/>";
                document.listForm.submit();
              }
            });
          }
        
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
        	
        	if(document.listForm.elements['userVO.userTyCode'].value == ''){
        		alert('권한을 입력하세요.');
    			return;
        	}
        	if(document.listForm.elements['userVO.userSttusCode'].value == ''){
        		alert('상태를 입력하세요.');
    			return;
        	}
        	if(document.listForm.elements['userVO.userLocat'].value == ''){
        		alert('근무지를 입력하세요.');
    			  return;
        	}
//         	alert(document.listForm.elements['userVO.userTyCode'].value);
        	
        	//if(!validateUserFormVO(frm)){
            //    return;
            //}else
            {
            	confirm('저장하시겠습니까?','사용자 정보 저장', null, function(request){
            		console.log('request: '+request);
            		if(request){
                        frm.enctype = "multipart/form-data";
            			frm.action = "<c:url value="${registerFlag == 'create' ? '/user/mngr/create.do' : '/user/mngr/update.do'}"/>";
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
        
        /* 글 목록 화면 function */
        function fncRetrieveList() {
        	document.listForm.action = "<c:url value='/user/mngr/retrievePagingList.do'/>";
           	document.listForm.submit();
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
    		    url:"<c:url value='/user/mngr/retrieveAjax.do'/>",
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

        var originalSignature = {
            path: "${pageContext.request.contextPath}/atchmnfl/site/retrieve.do?atchmnflId=${userFormVO.userVO.userSignature}&atchmnflSn=1",
            name: "${userFormVO.userVO.userSignatureFileName}",
            size: "${userFormVO.userVO.userSignatureFileSizeCalculated}",
        };
        var elements = {
            preview: function () {
                return document.querySelector("#signaturePreviewer");
            },
            file: function () {
                return document.querySelector("#signatureFile");
            },
            fileNameWrapper: function () {
                return document.querySelector("#fileNameWrapper");
            },
            fileName: function () {
                return document.querySelector("#fileName");
            },
            deleteSignature: function () {
                return document.querySelector("[name='userVO.deleteSignature']");
            },
        }

        function onSignatureChange() {
            var file = elements.file().files[0];

            if (file) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var units = "Byte KiB MiB GiB TiB".split(" ");
                    var size = file.size;
                    var index = 0;

                    while (size >= 1024 && index < units.length) {
                        size /= 1024;
                        index++;
                    }

                    showSignature({
                        path: e.target.result,
                        name: file.name,
                        size: size.toFixed(2) + " " + units[Math.min(index, units.length - 1)],
                    });
                }
                reader.readAsDataURL(file);
            }
            else {
                onSignatureCancel();
            }
        }

        function onSignatureCancel() {
            elements.file().value = "";
            showSignature({
                path: originalSignature.path,
                name: originalSignature.name,
                size: originalSignature.size,
            });
        }

        function deleteSignature() {
            elements.file().value = "";
            showSignature({
                path: "",
                name: "",
                size: "",
            });
        }

        // utils
        function showSignature(option) {
            var option = option || {};
            var path = option.path;
            var name = option.name;
            var size = option.size;

            elements.preview().src = path;
            if (name) {
                elements.fileName().textContent = name + " (" + (size || "0 Byte") + ")";
                elements.fileNameWrapper().style.display = "";
                elements.deleteSignature().value = "";
            }
            else {
                elements.fileNameWrapper().style.display = "none";
                elements.deleteSignature().value = "DELETE";
            }
        }

        $(function() {
            showSignature({
                path: originalSignature.path,
                name: originalSignature.name,
                size: originalSignature.size,
            });
        });
        //-->
    </script>
</head>
	

<body>
	<main id="content" class="mini">
         <div class="page-header">
         	<h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
         </div>
		 <form:form commandName="userFormVO" id="listForm" name="listForm" method="post">
			<form:hidden path="searchUserVO.pageIndex" />
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
							  	<dd><strong><c:out value="${userFormVO.userVO.userId}"/></strong></dd>
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
                            <dd><form:input path="userVO.userNm" cssClass="control" cssStyle="ime-mode:active;" title="성명"/>
								  &nbsp;<form:errors path="userVO.userNm" /></dd>
                        </dl>
                        <dl>
                            <dt>비밀번호
                                <!-- 회원정보수정시에만 나타남 -->
                                <input type="checkbox" id="changePasswordYN" name="userVO.changePasswordYN" onclick="fncChangePasswordYN(this)" class="checkbox" value="Y"/>
                                <label for="changePasswordYN">변경</label>
                                <!-- //회원정보수정시에만 나타남 -->
                            </dt>
                            <dd><form:password path="userVO.userPassword" cssClass="control" readonly="true" title="비밀번호" cssStyle="ime-mode:disabled;"/>
					  			  &nbsp;<form:errors path="userVO.userPassword" /></dd>
                            <dt>비밀번호 확인</dt>
                            <dd><input type="password" name="userPasswordConfirm" class="control" readonly="readonly" style="ime-mode:disabled;"/></dd>
                        </dl>
                        <dl>
                            <dt>이메일</dt>
                            <dd><form:input path="userVO.email" cssClass="control" cssStyle="ime-mode:inactive;" title="이메일"/>
								  &nbsp;<form:errors path="userVO.email" /></dd>
                            <dt>연락처(휴대폰)</dt>
                            <dd><form:input path="userVO.moblphon" id="moblphon" cssClass="control" title="연락처(휴대폰)"/>
								  &nbsp;<form:errors path="userVO.moblphon"/></dd>
                        </dl>
                        <dl>
                            <dt>소속</dt>
                            <dd><form:input path="userVO.psitn" cssClass="control" cssStyle="ime-mode:active;" title="소속"/>
								  &nbsp;<form:errors path="userVO.psitn" /></dd>
                            <dt>직급</dt>
                            <dd><form:input path="userVO.clsf" cssClass="control" cssStyle="ime-mode:active;"/>
								  &nbsp;<form:errors path="userVO.clsf" title="직급" /></dd>
                        </dl>
                        <dl>
                            <dt>근무위치</dt>
                            <dd>
                              <select class="control" name="userVO.userLocat">
                                <option value="" disabled <c:if test="${userFormVO.userVO.userLocat == null or userFormVO.userVO.userLocat eq ''}">selected</c:if>>선택</option>
                              <c:forEach items="${locationCodes}" var="item">
                                <option value="${item.cmmnCode}" <c:if test="${item.cmmnCode eq userFormVO.userVO.userLocat}">selected</c:if>>${item.cmmnCodeNm}</option>
                              </c:forEach>
                              </select>
                            </dd>
                        </dl>
                    </div>
                    <hr>
                    <div class="detailbox type1">
                        <dl>
                            <dt>계정신청사유/비고</dt>
                            <dd><form:textarea path="userVO.acntReqstResn" cssClass="control" cssStyle="ime-mode:active;"/>
								  &nbsp;<form:errors path="userVO.acntReqstResn" title="계정신청사유/비고" rows="5" /></dd>
                        </dl>
                    </div>
                    <hr>
                    <div class="detailbox type1">
                        <dl>
                            <dt>권한</dt>
                            <dd>
	                            <form:select path="userVO.userTyCode" cssClass="control" title="권한">
									<form:option value="">선택하세요</form:option>
									<form:options items="${userTyCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
								</form:select>
                            </dd>
                            <dt>상태</dt>
                            <dd>
	                            <form:select path="userVO.userSttusCode" cssClass="control">
									<form:option value="">선택하세요</form:option>
									<form:options items="${userSttusCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
								</form:select>
                            </dd>
                        </dl>

                    </div>
            </div>
		</form:form>
            <div class="btnbox center mb30">
                <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
<!-- 
			    <button type="button" onclick="fncDelete()" class="btn btn-dark"><i class="ico-delete"></i>삭제</button>
-->
                <button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
            </div>
    </main>
            <!-- //본문영역 -->
    
    
</body>
</html>
