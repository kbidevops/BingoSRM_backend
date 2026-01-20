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
    </script>
</head>
<body>
	<main id="content" class="mini">
         <div class="page-header">
         	<h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
         </div>
		 <form:form commandName="infraOpertFormVO" id="listForm" name="listForm" method="post">
			<form:hidden path="infraOpertVO.saveToken" />
			<input type="hidden" name="checkDuo" value="false"/>
            <div class="card userbox">
                <div class="detailbox type1">
                        <dl>
                            <dt>작업계획서</dt>
                            <dd>
                            </dd>
                        </dl>
                </div>
                <hr>
                <div class="detailbox type1">
                    <dl>
                        <dt>작업결과서</dt>
                        <dd>
					</dd>
                    </dl>
                </div>
                <hr>
                <div class="detailbox type1">
                    <dl>
                        <dt>기타파일</dt>
                        <dd>
                        </dd>
                    </dl>
                </div>
            </div>
		</form:form>
        <div class="btnbox center mb30">
            <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
            <button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
        </div>
    </main>
            <!-- //본문영역 -->
    
    
</body>
</html>
