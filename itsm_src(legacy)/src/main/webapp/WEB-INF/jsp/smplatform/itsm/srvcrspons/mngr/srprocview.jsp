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
  * @Class Name : /srvcrspons/site/edit.jsp
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
    <script type="text/javaScript" language="javascript" defer="defer">
        <!--
        /* 글 목록 화면 function */
        function fncRetrieveList() {
        	<c:if test="${empty srvcRsponsFormVO.returnListMode}">
    		document.listForm.action = "<c:url value='/srvcrspons/mngr/retrieveSrProcList.do'/>";
	    	</c:if>
	    	<c:if test="${!empty srvcRsponsFormVO.returnListMode}">
	    		document.listForm.action = "<c:url value='/srvcrspons/mngr/retrieveSrProcList.do'/>";
	    	</c:if>
           	document.listForm.submit();
        }
        
        function fncDownloadFile(atchmnflId, atchmnflSn){
        	document.downloadForm.action = "<c:url value='/atchmnfl/site/retrieveSrReqList.do'/>";
        	document.downloadForm.atchmnflId.value = atchmnflId;
        	document.downloadForm.atchmnflSn.value = atchmnflSn;
        	document.downloadForm.submit();
        }
        //-->
    </script>
</head>
	

<body>
	<form name="downloadForm" action="<c:url value='/atchmnfl/site/retrieve.do'/>" target="">
		<input type="hidden" name="atchmnflId" id="atchmnflId"/>
		<input type="hidden" name="atchmnflSn" id="atchmnflSn"/>
	</form>
	<main id="content">
        <div class="page-header">
            <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></h3>
            <div class="btnbox fr">
                <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
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
		  	<form:hidden path="returnListMode" />
		
        <div class="row  srarea equalheight viewpage"><!-- create, update에서는  viewpage 클래스 삭제 -->
            <div class="col-left">
                <div class="card">
                    <div class="card-header">
                        <h4 class="card-title">SR 정보</h4>
                        <dl class="sr-info">
                            <dt>SR번호</dt>
                            <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.srvcRsponsNo}"/></dd>
                            <dt>요청일시</dt>
                            <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.requstDtDateDisplay}"/> <c:out value="${srvcRsponsFormVO.srvcRsponsVO.requstDtTimeDisplay}"/></dd>
                        </dl>
                        <dl class="sr-info">
			              <dt>요청범주</dt>
			              <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.srvcRsponsClCodeNm}"/></dd>
			              <dt>요청구분</dt>
			              <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.trgetSrvcDetailCodeNm}"/></dd>
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
                                        <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqesterNm}"/></dd>
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
			                       <dl>
			                           <dt>참조 아이디</dt>
			                           <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.refIds}"/></dd>
			                       </dl>
                                </div>
                            </div>
                        </div>
                        <!-- //요청자정보 -->
                    </div>
                    <!-- 1차 요청자 -->
                    <!-- 
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">1차 요청자(외부고객)</h5>
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>성명</dt>
                                        <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rqester1stNm}"/></dd>
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
                    </div>
                    -->
                    <!-- //1차 요청자 -->

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
                                        <dd>
                                        	<div class="scrollbox control">
                                        		${fn:replace(srvcRsponsFormVO.srvcRsponsVO.srvcRsponsCn, newLineChar, "<br/>")}	
                                            </div>
                                        </dd>
                                    </dl>
				                  	<dl>
					                    <c:if test="${!empty srvcRsponsFormVO.srvcRsponsVO.reSrvcRsponsSj}">
					                		<dt>1차 SR요청 정보</dt>
					                		<dd>
					                		<textarea path="srvcRsponsVO.srvcRsponsCn" readonly cssStyle="ime-mode:active;" class="control" title="요청상세내용" rows="10"/>
요청제목 : ${srvcRsponsFormVO.srvcRsponsVO.reSrvcRsponsSj}
요청상세내용 :
${srvcRsponsFormVO.srvcRsponsVO.reSrvcRsponsCn}
					                		</textarea>
					
					                        </dd>
					              		</c:if>
					                </dl>
                                    <dl>
                                        <dt>요청첨부파일</dt>
                                        <dd style="display:inline;">
                                        	<c:forEach var="atchmnflVO" items="${requstAtchmnflList}">
	                                        	<div id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
													<a href="#" title="파일다운로드" class="btn-attach btn" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
												</div>
											</c:forEach>
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
                                <dt>접수자</dt>
                                <dd><strong><c:out value="${srvcRsponsFormVO.srvcRsponsVO.cnfrmrUserNm}"/></strong></dd>
                            </dl>

                            <dl>
                                <dt>처리담당</dt>
                                <dd><strong><c:out value="${srvcRsponsFormVO.srvcRsponsVO.chargerUserNm}"/></strong></dd>
                            </dl>
                        </div>
                        <hr>
                        <div class="detailbox type2">
                            <dl>
                                <dt>접수일시</dt>
                                <dd><strong><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rspons1stDtDateDisplay}"/>  <c:out value="${srvcRsponsFormVO.srvcRsponsVO.rspons1stDtTimeDisplay}"/></strong></dd>
                            </dl>
                            <dl>
                                <dt>처리(적용)일시</dt>
                                <dd><strong><c:out value="${srvcRsponsFormVO.srvcRsponsVO.processDtDateDisplay}"/>  <c:out value="${srvcRsponsFormVO.srvcRsponsVO.processDtTimeDisplay}"/></strong></dd>
                            </dl>
                        </div>
                        <hr>
                        <div class="detailbox type2">
                            <dl>
                                <dt>변경분류</dt>
                                <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.srvcRsponsBasisCodeNm}"/></dd>
                            </dl>
                            <dl>
                                <dt>변경유형</dt>
                                <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.trgetSrvcCodeNm}"/></dd>
                            </dl>
                            <dl>
                                <dt>SR 처리시간</dt>
                                <dd><p style="text-align:center;"><c:out value="${srvcRsponsFormVO.srvcRsponsVO.processTerm}"/></p></dd>
                            </dl>
                            <dl>
                                <dt><p style="text-align:center;">시간구분</p></dt>
                                <dd><p style="text-align:center;"><c:out value="${srvcRsponsFormVO.srvcRsponsVO.processStdrCodeNm}"/></p></dd>
                            </dl>
<!--                             
                            <dl>
                                <dt>만족도</dt>
                                <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.changeDfflyCodeNm}"/></dd>
                            </dl>
-->                            
                        </div>
                        <hr>
                        <div class="detailbox type2"> 
                            <dl>
                                <dt>처리방법</dt>
                                <dd>
                                    <ul class="checklist">
                                        <li>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.dataUpdtYn != 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.dataUpdtYn" id="dataUpdtYn" disabled class="checkbox"/>
                                        	</c:if>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.dataUpdtYn == 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.dataUpdtYn" id="dataUpdtYn" disabled  class="checkbox" value="Y" checked/>
                                        	</c:if>
                                            <label for="dataUpdtYn">데이터수정</label>
                                        </li>
                                        <li>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.progrmUpdtYn != 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.progrmUpdtYn" id="progrmUpdtYn" disabled class="checkbox" value="Y"/>
											</c:if>
                                        	<c:if test="${srvcRsponsFormVO.srvcRsponsVO.progrmUpdtYn == 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.progrmUpdtYn" id="progrmUpdtYn" disabled class="checkbox" value="Y" checked/>
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
											<input type="checkbox" name="srvcRsponsVO.instlYn" id="instlYn" disabled class="checkbox" value="Y"/>
											</c:if>
											<c:if test="${srvcRsponsFormVO.srvcRsponsVO.instlYn == 'Y' 
														|| srvcRsponsFormVO.srvcRsponsVO.noneStopInstlYn == 'Y'
														|| srvcRsponsFormVO.srvcRsponsVO.stopInstlYn == 'Y'}">
											<input type="checkbox" name="srvcRsponsVO.instlYn" id="instlYn" disabled class="checkbox" value="Y" checked/>
											</c:if>
                                            <label for="instlYn">배포</label>
                                        </li>
                                        <li>
											<c:if test="${srvcRsponsFormVO.srvcRsponsVO.infraOpertYn != 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.infraOpertYn" id="infraOpertYn" disabled class="checkbox" value="Y"/>
											</c:if>
											<c:if test="${srvcRsponsFormVO.srvcRsponsVO.infraOpertYn == 'Y' }">
											<input type="checkbox" name="srvcRsponsVO.infraOpertYn" id="infraOpertYn" disabled class="checkbox" value="Y" checked/>
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
                                <dt>처리내역</dt>
                                <dd>
                                	<div class="scrollbox control type1">
                                		${fn:replace(srvcRsponsFormVO.srvcRsponsVO.srvcProcessDtls, newLineChar, "<br/>")}
                                    </div>
                                </dd>
                            </dl>
                        </div>
                        <div class="detailbox type2">
                            <dl>
                                <dt>기타</dt>
                                <dd>
                                	<div class="scrollbox control type2">
                                		${fn:replace(srvcRsponsFormVO.srvcRsponsVO.etc, newLineChar, "<br/>")}
                                    </div>
                                </dd>
                            </dl>
                        </div>
                        <div class="detailbox type2">
                            <dl>
                                <dt>응답첨부파일</dt>
                                <dd style="display:inline;">
	                                <c:forEach var="atchmnflVO" items="${rsponsAtchmnflList}">
										<div id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
											<a href="#" title="파일다운로드" class="btn-attach btn" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
										</div>
									</c:forEach>
                                </dd>
                            </dl>
                        </div>
                        <hr>
<!--                         
                        <div class="detailbox type2">
                            <dl>
                                <dt>기능개선</dt>
                                <dd>
                                	<c:choose>
                                		<c:when test="${srvcRsponsFormVO.srvcRsponsVO.fnctImprvmNo == '' || srvcRsponsFormVO.srvcRsponsVO.fnctImprvmNo == null}">없음</c:when>
                                		<c:otherwise>있음</c:otherwise>
                                	</c:choose>
                                </dd>
                            </dl>
                            <dl>
                                <dt>배포확인서</dt>
                                <dd>
                                	<c:choose>
                                		<c:when test="${srvcRsponsFormVO.srvcRsponsVO.wdtbCnfirmNo == '' || srvcRsponsFormVO.srvcRsponsVO.wdtbCnfirmNo == null}">없음</c:when>
                                		<c:otherwise>있음</c:otherwise>
                                	</c:choose>
								</dd>
                            </dl>
                        </div>
-->                        
                    </div>
                </div>
                <!-- //SR 처리 내용 -->

            </div>
        </div>
		</form:form>
		
        <div class="btnbox right mb30">
            <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
        </div>
    </main>
    
</body>
</html>
