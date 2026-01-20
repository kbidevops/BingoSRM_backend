<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
  /**
  * 
  * @Class Name : /user/mngr/list.jsp
  * @Description : 사용자 관리 목록 화면
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
				<c:forEach var="result" items="${resultList}" varStatus="status">
	          		<c:if test="${result.orderLevel == '1' }">
	          		  <li class="new">
	          		</c:if>
	          		<c:if test="${result.orderLevel == '2' }">
	          		  <li class="ing">
	          		</c:if>
	          		<c:if test="${result.orderLevel == '3' }">
	          		  <li>
	          		</c:if>
	                    	<div class="item">
	                        <div class="item-head">
	                            <em class="circle"><c:out value="${result.trgetSrvcCodeNm}"/></em>
	                            <i class="sr-num"><c:out value="${result.srvcRsponsNo}"/></i>
	                            <c:if test="${result.orderLevel != '1' }">
	                            <em><c:out value="${result.processStdrCodeNm}"/></em>
	                            </c:if>
	                            <strong class="sr-tit dotdotSj"><c:out value="${result.srvcRsponsSj}"/></strong>
	                            <span class="writer"><c:out value="${result.rqesterNm}"/> / <c:out value="${result.requstDtDateDisplay}"/> <c:out value="${result.requstDtTimeDisplay}"/></span>
	                        </div>
	                        <div class="item-body dotdotCn">
	                        	${result.srvcRsponsCn}
	                        </div>
	                        <div class="btnbox center">
	                            <button type="button" onclick="fncUpdateView('<c:out value="${result.srvcRsponsNo}"/>')" class="btn btn-soft">상세보기</button>
	          					<c:if test="${result.orderLevel == '1' }">
	                            </c:if>
	                        </div>
	                        <c:if test="${result.orderLevel == '1' }">
	                        <span class="step">1차 요청</span>
	                        </c:if>
	                        <c:if test="${result.orderLevel == '2' }">
	                        <span class="step">요청 접수</span>
	                        </c:if>
	                    </div>
	          			<script type="text/javaScript" language="javascript">
		          			$('.item-head').tooltip('enable');
		          			console.log('hi');
		          		</script>
	          		</c:forEach>
	          		<c:if test="${empty resultList}">
	          			<h4>검색된 결과가 없습니다.</h4>
	          		</c:if>
	   
