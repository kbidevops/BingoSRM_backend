<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : /itsm/srvcrspons/mngr/list.jsp
  * @Description : SR 관리 목록 화면
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
        function fncUpdateView(srvcRsponsNo) {
        	document.listForm.elements['srvcRsponsVO.srvcRsponsNo'].value = srvcRsponsNo;
           	document.listForm.action = "<c:url value='/srvcrspons/mngr/updateView.do'/>";
           	document.listForm.submit();
        }
        
        /* 글 등록 화면 function */
        function fncCreateView() {
           	document.listForm.action = "<c:url value='/srvcrspons/mngr/createView.do'/>";
           	document.listForm.submit();
        }
        
        /* 글 목록 화면 function */
        function fncRetrieveList() {
        	document.listForm.action = "<c:url value='/srvcrspons/mngr/retrievePagingList.do'/>";
        	document.listForm.elements['searchSrvcRsponsVO.pageIndex'].value = 1;
           	document.listForm.submit();
        }
        
        /* 글 목록 화면 function */
        function fncRetrieveExcelList() {
        	
//         	var processMt = prompt("년월입력 미입력시("+document.listForm.processMt.value+"):", document.listForm.processMt.value);
//         	console.log('processMt: '+processMt);
			
			var processMt = $("#processMtInput").val();
        	if($.trim(processMt) != ''){
        		document.listForm.processMt.value = processMt;	
        	}
			
        	$("#downloadModal").modal("hide");
        	
        	document.listForm.action = "<c:url value='/srvcrspons/mngr/retrieveExcelList.do'/>";
           	document.listForm.submit();
        }
        
        /* pagination 페이지 링크 function */
        function fncRetrievePagingList(pageNo){
        	document.listForm.elements['searchSrvcRsponsVO.pageIndex'].value = pageNo;
        	document.listForm.action = "<c:url value='/srvcrspons/mngr/retrievePagingList.do'/>";
           	document.listForm.submit();
        }
        
        function fncReset(){
        	document.listForm.elements['searchSrvcRsponsVO.srvcRsponsNo'].value = '';
        	document.listForm.elements['searchSrvcRsponsVO.srvcRsponsSj'].value = '';
        	document.listForm.elements['searchSrvcRsponsVO.processMt'].value = '';
        	document.listForm.elements['searchSrvcRsponsVO.srvcRsponsCn'].value = '';
        	
        	document.listForm.elements['searchSrvcRsponsVO.trgetSrvcCode'].value = '';
        	document.listForm.elements['searchSrvcRsponsVO.srvcRsponsClCode'].value = '';
        	
        	document.listForm.elements['searchSrvcRsponsVO.pageIndex'].value = 1;
        }
        
        function fncDownloadModal(){
        	$("#downloadModal").modal("show");
        }
        //-->
    </script>
</head>
	

<body>
		<main id="content">
            <div class="page-header">
                <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
            </div>
				
		    <form:form commandName="srvcRsponsFormVO" id="listForm" name="listForm" method="post">
				<form:hidden path="searchSrvcRsponsVO.pageIndex" />
				<form:hidden path="processMt" />
				<input type="hidden" name="srvcRsponsVO.srvcRsponsNo" />
                <div class="row">
                    <div class="col-fix">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">검색조건</h4>
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>SR번호</dt>
                                        <dd><form:input path="searchSrvcRsponsVO.srvcRsponsNo" class="control" title="SR번호"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>신청년월</dt>
                                        <dd><form:input path="searchSrvcRsponsVO.processMt" class="control" title="신청년월" placeholder="예)202301"/></dd>
                                    </dl>
                                </div>
                                <hr>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>대상서비스</dt>
                                        <dd>
                                          <form:select path="searchSrvcRsponsVO.trgetSrvcCode" cssClass="control" title="대상서비스">
										  	<form:option value="">전체</form:option>
										  	<form:options items="${trgetSrvcCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
										  </form:select>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>SR분류</dt>
                                        <dd>
                                            <form:select path="searchSrvcRsponsVO.srvcRsponsClCode" cssClass="control" title="SR분류">
											  	<form:option value="">전체</form:option>
											  	<form:options items="${srvcRsponsClCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
											</form:select>
                                        </dd>
                                    </dl>
                                </div> 
                                <hr>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>제목</dt>
                                        <dd><form:input path="searchSrvcRsponsVO.srvcRsponsSj" class="control" title="제목"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>내용</dt>
                                        <dd><form:input path="searchSrvcRsponsVO.srvcRsponsCn" class="control" title="내용"/></dd>
                                    </dl>
                                </div>
                                <hr>
                                <div class="btnbox center">
                                    <button type="button" onclick="fncReset()" class="btn btn-secondary"><i class="ico-reset"></i>초기화</button>
                                    <button type="button" onclick="fncRetrieveList()" class="btn btn-primary"><i class="ico-search"></i>검색</button>
                                </div>                         
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <span class="numbadge">${paginationInfo.totalRecordCount}</span>
                                <div class="btnbox fr">
                                    <button type="button" onclick="fncDownloadModal()" class="btn btn-dark"><i class="ico-download"></i>다운로드</button>
<!-- 
                                    <button type="button" onclick="fncCreateView()" class="btn btn-primary"><i class="ico-write"></i>등록</button>
-->
                                </div>
                            </div>
                            
                            <table class="gridtbl">
                                <caption>SR 현황 목록</caption>
                                <colgroup>
                                    <col style="width:60px">
                                    <col style="width:100px">
                                    <col style="width:auto">
                                    <col style="width:80px">
                                    <col style="width:100px">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">No</th>
                                        <th scope="col">SR번호</th>
                                        <th scope="col">SR제목</th>
                                        <th scope="col">신청년월</th>
                                        <th scope="col">대상서비스</th>
                                        <th scope="col">요청일</th>
                                        <th scope="col">접수일시</th>
                                        <th scope="col">처리일시</th>
                                        <th scope="col">재요청일시</th>
                                    </tr>
                                </thead>
                                <tbody>
                                  <c:forEach var="result" items="${resultList}" varStatus="status">
                                    <tr>
                                        <td class="num"><c:out value="${paginationInfo.totalRecordCount+1 - ((srvcRsponsFormVO.searchSrvcRsponsVO.pageIndex-1) * srvcRsponsFormVO.searchSrvcRsponsVO.pageSize + status.count)}"/></td>
                                        <td class="num"><c:out value="${result.srvcRsponsNo}"/></td>
                                        <td><a href="javascript:fncUpdateView('<c:out value="${result.srvcRsponsNo}"/>')" class="ellipsis"><c:out value="${result.srvcRsponsSj}"/></a></td>
                                        <td class="num"><c:out value="${result.processMt}"/></td>
                                        <td><c:out value="${result.trgetSrvcCodeNm}"/></td>
                                        <td class="num"><c:out value="${result.requstDtDateDisplay}"/> <c:out value="${result.requstDtTimeDisplay}"/></td>
                                        <td class="num"><c:out value="${result.rspons1stDtDateDisplay}"/> <c:out value="${result.rspons1stDtTimeDisplay}"/></td>
                                        <td class="num"><c:out value="${result.processDtDateDisplay}"/> <c:out value="${result.processDtTimeDisplay}"/></td>
                                        <td class="num"><c:out value="${result.reRequestDtDateDisplay}"/> <c:out value="${result.reRequestDtTimeDisplay}"/></td>
                                    </tr>
                                  </c:forEach>
                                </tbody>
                            </table>
                            <c:if test="${empty resultList}">
	                            <div class="nolist"> 총 <strong>0</strong>건이 조회되었습니다.</div>
	                        </c:if>
                        </div>
                        <div class="pagingbox">
                            <div class="paging"><ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="fncRetrievePagingList" /></div>
                        </div>
                    </div>
                </div>
                
                <!-- Modal -->
				<div class="modal fade" id="downloadModal" tabindex="-1" role="dialog" >
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				      </div>
				      <div class="modal-body">
				        <div class="container">
				           <h5 class="form-signin-heading">년월 입력 (미입력시 - ${srvcRsponsFormVO.processMt })</h5>
				           <input id="processMtInput" class="control" name="processMtInput" value="${srvcRsponsFormVO.processMt }">
				        </div>
				      </div>
				      <div class="modal-footer">
				        <a onclick="fncRetrieveExcelList()" class="btn btn-primary" style="color:white;">확인</a>
				      </div>
				    </div>
				  </div>
				</div>
                
            </form:form>
        </main>
</body>
</html>
