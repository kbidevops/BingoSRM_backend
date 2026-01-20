<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <c:set var="registerFlag" value="${empty repFormVO.repMasterVO.creatId ? 'create' : 'modify'}"/>
    <c:set var="sttusCode" value="${repFormVO.repMasterVO.sttusCode}"/>
    <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></title>
    <!--For Commons Validator Client Side-->
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <script type="text/javascript" src="/smarteditor/js/service/HuskyEZCreator.js" charset="utf-8"></script>
    <validator:javascript formName="repFormVO" staticJavascript="false" xhtml="true" cdata="false"/>
    
	<script type="text/javascript" language="javascript" defer="defer">
	$(function(){
		 
	})

	/**
	 * 문자열에서 모든 교체할 문자열을 대체 문자열로 치환한다.
	 * @param pattnStr - 찾을 문자열
	 * @param chngStr - 대체 문자열
	 * @return 치환된 문자열
	 */
	String.prototype.replaceAll = function(pattnStr, chngStr) {
	
	    var retsult = "";
	    var trimStr = this;//.replace(/(^\s*)|(\s*$)/g, "");
	
	    if(trimStr && pattnStr != chngStr) {
	
	        retsult = trimStr;
	
	        while(retsult.indexOf(pattnStr) > -1) {
	            retsult = retsult.replace(pattnStr, chngStr);
	        }
	    }
	
	    return retsult;
	};
	
	function fncDownload(){
		var ozrFileNm = "/knfc/monthlyReport.ozr";
	    var odiFileNm = "/knfc/monthlyReport.odi";
	    
	    procPrintList(ozrFileNm, odiFileNm);
	}
	
	var procPrintList = function(ozrFileNm, odiFileNm) {
		
		var thisMonth = $("th#execDesc").html().trim();
		var nextMonth = $("th#planDesc").html().trim();
		
	    var ozDate = '<c:out value="${repFormVO.repMasterVO.reportDtDisplay }"/>';
		
	    var jsonData = "${jsonData }";
	    jsonData = jsonData.replaceAll("'", '"');
		
	    var ozparams = { "ozr" : ozrFileNm
	                   , "odi" : odiFileNm
	                   , "pcount" : 4       // pcount는 jsonData 가 있으므로 밑의 params 갯수보다 1+해서 적어준다.
	                   , "params" : [ 
	                	   			  {  id:"reportDt",     value: ozDate},
	                	   			  {  id: "thisMonth",	value: thisMonth},
	                	   			  {  id: "nextMonth",	value: nextMonth}
	                	   			]
	                   };
	    
	    var popUrl = "<c:url value='/itsm/rep/master/mngr/OzReportViewer.do'/>";
		
	    var $formOzReport = $( "<form action='" + popUrl + "' method='post', target='_blank' id='formOzReport' name='formOzReport'/>" );
	    var html = $("<input type='hidden' name='ozparams' value='"+JSON.stringify(ozparams)+"'/>"
	            +"<input type='hidden' name='jsonData' value='"+jsonData+"'/>");
		
	    $formOzReport.append(html);
		
	    $('body').append( $formOzReport );
	    $formOzReport.submit();
	};
	
	function fncRetrieveList() {
		document.listForm.repTyCode.value = "B002";
    	document.listForm.action = "<c:url value='/itsm/rep/master/mngr/retrievePagingList.do'/>";
       	document.listForm.submit();
    }
	
	function fncConfirm(){
		confirm('확정하시겠습니까?','확정', null, function(request){
			if(request){
				document.listForm.action = "<c:url value='/itsm/rep/master/mngr/confirmRepMaster.do'/>";
				document.listForm.submit();
			}
		}); //confirm 끝
	}
	
	function fncReturnModal(){
		$("#returnModal").modal("show");
	}
	
	function fncReturn(){
		confirm('반려하시겠습니까?','반려', null, function(request){
			if(request){
				document.listForm.action = "<c:url value='/itsm/rep/master/mngr/returnRepMaster.do'/>";
				document.listForm.submit();
			}
		}); //confirm 끝
	}
	function fncWrite(){
		document.listForm.action = "<c:url value='/itsm/rep/master/mngr/writeRepMaster.do'/>";
		document.listForm.submit();
	}
	function fncCancelConfirm(){
		confirm('확정 취소 하시겠습니까?', '확정취소', null, function(request) {
			if (request) {
				$.ajax({
					url: "${pageContext.request.contextPath}/itsm/rep/master/mngr/cancelConfirmAjax.do",
					method: "POST",
					data: {
						repSn: $("#repSn").val(),
					},
				}).done(function(response) {
					if (response.message) {
						alert(response.message);
					}
					else if (response) {
						alert("확정을 취소했습니다");
					}
					else {
						alert("확정 취소에 실패했습니다");
					}

					location.reload();
				});
			}
		});
	}
	</script>
</head>


<body>
	<main id="content">
		<form:form commandName="repFormVO" id="listForm" name="listForm" method="post" enctype="multipart/form-data">
			<form:hidden path="repMasterVO.pageIndex" />
			<form:hidden path="repMasterVO.saveToken" />
			<form:hidden path="repMasterVO.repSn" id="repSn"/>
			<form:hidden path="repMasterVO.sttusCode"/>
			<form:hidden path="repMasterVO.repTyCode" value="B003" id="repTyCode"/>
			<form:hidden path="searchMasterVO.repTyCode" value="B003"/>
			<form:hidden path="repMasterVO.createDtDisplay"/>
			<form:hidden path="registerFlag"/>
                <div class="page-header">
                    <h3 class="page-title"><strong><c:out value="${month }"/>월 </strong>월간<c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
                    <div class="btnbox">
                    	<c:if test="${sttusCode != 'B301'}">
                        	<button type="button" onclick="fncDownload()" class="btn btn-secondary"><i class="ico-download"></i>다운로드</button>
                        </c:if>
                        <c:if test="${LOGIN_USER_VO.userTyCode != 'R002'}">
                        	<c:if test="${sttusCode == 'B304' || sttusCode == 'B302'}">
	                        	<button type="button" onclick="fncWrite()" class="btn btn-primary"><i class="ico-save"></i>재작성</button>
                        	</c:if>
                        </c:if>
                        <c:if test="${sttusCode == 'B301'}">
	                        <button type="button" onclick="fncDelete()" class="btn btn-secondary"><i class="ico-delete"></i>삭제</button>
	                        <button type="button" onclick="fncUsrSelect()" class="btn btn-dark"><i class="ico-approval"></i>확정</button>
	                        <button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
                        </c:if>
                        <c:if test="${LOGIN_USER_VO.userId == repFormVO.repMasterVO.confirmUsr && sttusCode == 'B302' }">
	                        <button type="button" onclick="fncConfirm()" class="btn btn-dark"><i class="ico-approval"></i>확정</button>
	                        <button type="button" onclick="fncReturnModal()" class="btn btn-secondary"><i class="ico-delete"></i>반려</button>
                        </c:if>
											<c:if test="${LOGIN_USER_VO.userTyCode == 'R002' && sttusCode == 'B303' }">
												<button type="button" onclick="fncCancelConfirm()" class="btn btn-primary"><i class="ico-approval"></i>확정 취소</button>
											</c:if>
                    </div>
                </div>
                <c:if test='${repFormVO.repMasterVO.returnResn != "" && repFormVO.repMasterVO.returnResn != null
                			  && repFormVO.repMasterVO.returnResn != " "}'>
                <span style="color:red;">반려 사유: <c:out value="${repFormVO.repMasterVO.returnResn }"/></span>
                </c:if>
                
	            <table class="reporttbl mb10">
	                <caption>업무보고서</caption>
	                <colgroup>
	                    <col style="width:10%">
	                    <col style="width:45%">
	                    <col style="width:45%">
	                </colgroup>
	                <c:forEach  items="${repFormVO.repDetailVOList}" var="repDetailVO" varStatus="status">
		            	<c:if test="${repDetailVO.sysCode == 'B100' }">
		                    <thead>
		                        <tr style="font-size: 11pt;" id="name1">
		                            <th id="B100" scope="row" class="num">구분</th>
		                            <th style="font-size: 12pt;" scope="col" id="execDesc">
		                            <c:if test="${repDetailVO.execDesc == null ||repDetailVO.execDesc == ''}">
		                                	今月  추진실적 (${dayRange.get(0)} ~ ${dayRange.get(1)})
		                            </c:if>
		                            		${repDetailVO.execDesc }
		                            </th>
		                            <th style="font-size: 12pt;" scope="col" id="planDesc">
		                            <c:if test="${repDetailVO.planDesc == null ||repDetailVO.planDesc == ''}">
		                                	次月  추진계획 (${dayRange.get(2)} ~ ${dayRange.get(3)})
		                            </c:if>
		                            		${repDetailVO.planDesc }
		                            </th>
		                        </tr>
		                   	</thead>
		                    <tbody>
		                </c:if>
		                <c:if test="${repDetailVO.sysCode != 'B100'}">
		                    <tr style="font-size: 11pt;" id="name1">
		                        <th id="${repDetailVO.sysCode }" scope="row" class="num">
		                        	<b><c:out value="${repDetailVO.sysCodeNm }"/></b><br>
		                        </th>
		                        <td style="text-align:left; white-space: pre-wrap;" class="num" id="execDesc">${repDetailVO.execDesc }</td>
		                        <td style="text-align:left; white-space: pre-wrap;" class="num" id="planDesc">${repDetailVO.planDesc }</td>
		                    </tr>
		                </c:if>
	                </c:forEach>
	                </tbody>
	            </table>

			<div id="attachments" style="display: none">
				<c:forEach var="item" items="${fileList}">
					<div class="input-group">
						<p class="control">필수 첨부파일: <a href="${pageContext.request.contextPath}/itsm/atchmnfl/site/retrieve.do?atchmnflSn=1&atchmnflId=${item.requiredFileId}">${item.requiredFileName}</a></p>
<%--						<p class="control">${item.userId} 필수 첨부파일: <a href="${pageContext.request.contextPath}/itsm/atchmnfl/site/retrieve.do?atchmnflSn=1&atchmnflId=${item.requiredFileId}">${item.requiredFileName}</a></p>--%>
					</div>
					<div class="input-group">
						<p class="control">추가 첨부파일: <a href="${pageContext.request.contextPath}/itsm/atchmnfl/site/retrieve.do?atchmnflSn=1&atchmnflId=${item.additionalFileId}">${item.additionalFileName}</a></p>
<%--						<p class="control">${item.userId} 추가 첨부파일: <a href="${pageContext.request.contextPath}/itsm/atchmnfl/site/retrieve.do?atchmnflSn=1&atchmnflId=${item.additionalFileId}">${item.additionalFileName}</a></p>--%>
					</div>
				</c:forEach>
			</div>
                
                <!-- confirm Modal -->
				<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" >
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				      </div>
				      <div class="modal-body">
				        <div class="container">
				           <h3 class="form-signin-heading">담당자</h3>
				           <form:select id="confirmUsr" path="repMasterVO.confirmUsr" class="form-control form-control-sm">
					          <form:options itemValue="userId" itemLabel="userNm" items="${cstmrList }"/>
				           </form:select>
				        </div>
				      </div>
				      <div class="modal-footer">
				        <a onclick="fncConfirm()" class="btn btn-primary" style="color:white;">확인</a>
				      </div>
				    </div>
				  </div>
				</div>
				
				<!-- return Modal -->
				<div class="modal fade" id="returnModal" tabindex="-1" role="dialog" >
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				      </div>
				      <div class="modal-body">
				        <div class="container">
				           <h3 class="form-signin-heading">반려 사유</h3>
				           <form:input class="control" path="repMasterVO.returnResn"/>
				        </div>
				      </div>
				      <div class="modal-footer">
				        <a onclick="fncReturn()" class="btn btn-primary" style="color:white;">확인</a>
				      </div>
				    </div>
				  </div>
				</div>
            </form:form>
</body>
</html>
