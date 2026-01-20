<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : /itsm/user/mngr/list.jsp
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
<!DOCTYPE html>
<html lang="ko">
<head>
    <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></title>
    
	<script type="text/javaScript" language="javascript" defer="defer">
        <!--
        $(document).ready(function() {
        	$(".form-control").keydown(function(event) {
        		if ( event.which == 13 ) {
        			fncRetrieveList();
        		}
        	});
        });
        
        /* 글 수정 화면 function */
        function fncUpdateView(userId) {
        	document.listForm.elements['userVO.userId'].value = userId;
           	document.listForm.action = "<c:url value='/user/mngr/updateView.do'/>";
           	document.listForm.submit();
        }
        
        /* 글 등록 화면 function */
        function fncCreateView() {
           	document.listForm.action = "<c:url value='/user/mngr/createView.do'/>";
           	document.listForm.submit();
        }
        
        /* 글 목록 화면 function */
        function fncRetrieveList() {
        	document.listForm.action = "<c:url value='/user/mngr/retrievePagingList.do'/>";
        	document.listForm.elements['searchUserVO.pageIndex'].value = 1;
           	document.listForm.submit();
        }
        
        /* pagination 페이지 링크 function */
        function fncRetrievePagingList(pageNo){
        	document.listForm.elements['searchUserVO.pageIndex'].value = pageNo;
        	document.listForm.action = "<c:url value='/user/mngr/retrievePagingList.do'/>";
           	document.listForm.submit();
        }
        
        function fncReset(){
        	document.listForm.elements['searchUserVO.userId'].value = '';
        	document.listForm.elements['searchUserVO.userNm'].value = '';
        	document.listForm.elements['searchUserVO.userTyCode'].value = '';
        	document.listForm.elements['searchUserVO.userSttusCode'].value = '';
        	document.listForm.elements['searchUserVO.psitn'].value = '';
        	document.listForm.elements['searchUserVO.clsf'].value = '';
        	document.listForm.elements['searchUserVO.pageIndex'].value = 1;
        }
        //-->
    </script>
</head>
	

<body>
		  <main id="content">
                <div class="page-header">
                    <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
                </div>
                <div class="row">
                    <div class="col-fix">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">검색조건</h4>
                            </div>
                            <div class="card-body">
							<form:form commandName="userFormVO" id="listForm" name="listForm" method="post">
							<form:hidden path="searchUserVO.pageIndex" />
							<input type="hidden" name="userVO.userId" />
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>사용자아이디</dt>
                                        <dd><form:input path="searchUserVO.userId" cssClass="control" title="사용자아이디"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>성명</dt>
                                        <dd><form:input path="searchUserVO.userNm" cssClass="control" title="성명"/></dd>
                                    </dl>
                                </div>
                                <hr>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>권한</dt>
                                        <dd>
                                        <form:select path="searchUserVO.userTyCode" cssClass="control" title="권한">
										  	<form:option value="">전체</form:option>
										  	<form:options items="${userTyCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
										</form:select>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>소속</dt>
                                        <dd>
                                        	<form:input path="searchUserVO.psitn" cssClass="control" title="소속"/>
                                        </dd>
                                    </dl>
                                </div>
                                <hr>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>직급</dt>
                                        <dd>
                                        	<form:input path="searchUserVO.clsf" cssClass="control" title="직급"/>
                                        </dd>
                                    </dl>                                
                                    <dl>
                                        <dt>상태</dt>
                                        <dd>
                                          <form:select path="searchUserVO.userSttusCode" cssClass="control" title="상태">
										  	<form:option value="">전체</form:option>
										  	<form:options items="${userSttusCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
										  </form:select>
                                        </dd>
                                    </dl>
                                </div>
                                <hr>
                                <div class="btnbox center">
                                    <button type="button" onclick="fncReset()" class="btn btn-secondary"><i class="ico-reset"></i>초기화</button>
                                    <button type="button" onclick="fncRetrieveList()" class="btn btn-primary"><i class="ico-search"></i>검색</button>
                                </div>                         
		                    </form:form>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <span class="numbadge">${paginationInfo.totalRecordCount}</span>
                                <div class="btnbox fr">
<!--                                     <button type="button" class="btn btn-dark"><i class="ico-download"></i>다운로드</button> -->
                                    <button type="button" onclick="fncCreateView()" class="btn btn-primary"><i class="ico-write"></i>등록</button>
                                </div>
                            </div>
                            <!-- 검색결과 0건일 경우
                            <div class="nolist">총 <strong>0</strong>건이 조회되었습니다.</div>
                            -->
                            <table class="gridtbl">
                                <caption>사용자 목록</caption>
                                <colgroup>
                                    <col style="width:80px">
                                    <col>
                                    <col>
                                    <col>
                                    <col>
                                    <col>
                                    <col>
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">No</th>
                                        <th scope="col">사용자 ID</th>
                                        <th scope="col">성명</th>
                                        <th scope="col">소속</th>
                                        <th scope="col">직급</th>
                                        <th scope="col">권한</th>
                                        <th scope="col">상태</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="result" items="${resultList}" varStatus="status">
	            					<tr>
			            				<td class="num" scope="row"><c:out value="${paginationInfo.totalRecordCount+1 - ((userFormVO.searchUserVO.pageIndex-1) * userFormVO.searchUserVO.pageSize + status.count)}"/></td>
			            				<td><a href="javascript:fncUpdateView('<c:out value="${result.userId}"/>')" class="text-dark font-weight-bold"><c:out value="${result.userId}"/></a></td>
			           			   	    <td><c:out value="${result.userNm}"/></td>
			            				<td><c:out value="${result.psitn}"/></td>
			            				<td><c:out value="${result.clsf}"/></td>
			            				<td><c:out value="${result.userTyCodeNm}"/></td>
			            				<td><c:out value="${result.userSttusCodeNm}"/></td>
			            			</tr>
			        			</c:forEach>
			        			<c:if test="${empty resultList}">
			        				<tr>
			        					<td colspan="7">검색된 데이터가 없습니다.</td>
			        				</tr>
			        			</c:if>
                                </tbody>
                            </table>
                        </div>
                       <div class="pagingbox">
					       <ul class="paging light-theme simple-pagination">
							   	<ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="fncRetrievePagingList" />
						   </ul>
						</div>
                    </div>
                </div>
            </main>
            <!-- //본문영역 -->
		    <script>
		        
		    </script>
</body>
</html>
