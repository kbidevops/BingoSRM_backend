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
    <validator:javascript formName="repFormVO" staticJavascript="false" xhtml="true" cdata="false"/>
    
	<script type="text/javascript" language="javascript" defer="defer">
	$(function(){
		 <c:if test="${sttusCode != 'B301'}">
		 	$("textArea").attr('readonly', true);
		 </c:if>
		 
		 //전일실적 
		 <c:forEach items="${execAttdList}" var="execAttd">
			 	if($("#"+"${execAttd.userLocat}"+" .exec.${execAttd.attdCode}").text() == ""){
				 	$("#"+"${execAttd.userLocat}"+" .exec.${execAttd.attdCode}").append("${execAttd.userNm}");
				}else{
				 	$("#"+"${execAttd.userLocat}"+" .exec.${execAttd.attdCode}").append(", ${execAttd.userNm}");
				}
		 </c:forEach>
		 
		 //금일계획
		 <c:forEach items="${planAttdList}" var="planAttd">
			if($("#"+"${planAttd.userLocat}"+" .plan.${planAttd.attdCode}").text() == ""){
			 	$("#"+"${planAttd.userLocat}"+" .plan.${planAttd.attdCode}").append("${planAttd.userNm}");
			}else{
			 	$("#"+"${planAttd.userLocat}"+" .plan.${planAttd.attdCode}").append(", ${planAttd.userNm}");
			}
		 </c:forEach>
		 
		 
		 
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
		var ozrFileNm = "/knfc/dailyReport.ozr";
	    var odiFileNm = "/knfc/dailyReport.odi";
	    
	    procPrintList(ozrFileNm, odiFileNm);
	}
	
	var procPrintList = function(ozrFileNm, odiFileNm) {
		
	    var exDate = '<c:out value="${repAttdFormVO.execAttdVO.attdDtDisplay }"/>';
	    var ozDate = '<c:out value="${repFormVO.repMasterVO.reportDtDisplay }"/>';
		
	    var jsonData = "${jsonData }";
	    jsonData = jsonData.replaceAll("'", '"');
		
	    var ozparams = { "ozr" : ozrFileNm
	                   , "odi" : odiFileNm
	                   , "pcount" : 3       // pcount는 jsonData 가 있으므로 밑의 params 갯수보다 1+해서 적어준다.
	                   , "params" : [ 
	                	   				{  id: "repDt",     value: ozDate}
	                	   				,{  id: "execDt",     value: exDate}
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
	
	function fncSave(){
	confirm('저장하시겠습니까?','보고서 저장', null, function(request){
		if(request){
			  <c:if test="${registerFlag != 'create'}">
				  document.listForm.action = "<c:url value='/itsm/rep/master/mngr/update.do'/>";
				  document.listForm.submit();
			  </c:if>
		}else{
			console.log("false")
		}
	})

	}

	function fncDelete(){
		confirm('정말 삭제하시겠습니까?','포함된 모든 보고서가 삭제됩니다.', null, function(request){
			if(request){
				document.listForm.action = "<c:url value='/itsm/rep/master/mngr/delete.do'/>";
				document.listForm.submit();
			}
			
		})
	}

	function fncRetrieveList() {
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
  	
	function fncUsrSelect(){
		var detailVOList = $("textarea");
		var exit = false;
		
		$.each(detailVOList, function(i,detailVO){
			if(detailVO.value == ""){
				alert("작성하지 않은 담당자가 있습니다.");
				exit = true;
				return false;
			}
		})
		
		if(exit) return;
		
        $("#confirmModal").modal("show");
	}
	
	function fncAttdEditModal(attdDt){
		$("#attdDtDisplay").text(attdDt+" 근태 변경");
		$("#attdDt").val(attdDt);
		$("#attdEditModal").modal("show");
	}
	
	function fncAttdAdd(){
		if($("#attdEditForm #attdUserNm").val() == "-"){
			alert("이름은 필수 항목입니다.");
			return;
		}else if($("#attdEditForm #attdCode").val() == "-"){
			alert("근태는 필수 항목입니다.");
			return;
		}
		
		$.ajax({
			type : "post",
			url : "/itsm/rep/master/mngr/addAttdCodeAjax.do",
			dataType : "json",
			data : { 
				userId : $("#attdEditForm #attdUserNm").val(),
				attdCode : $("#attdEditForm #attdCode").val(),
				attdDt : $("#attdDt").val(),
				userLocat : $("input[name='repAttdVO.userLocat']:checked").val()
			},
			success : function(request){
				fncReload()
			},
			error : function(status){
				alert("추가 실패")
			}
		})
	}
	
	function fncAttdDelete(){
		if($("#attdEditForm #attdUserNm").val() == "-"){
			alert("이름은 필수 항목입니다.");
			return;
		}
		
		$.ajax({
			type : "post",
			url : "/itsm/rep/master/mngr/deleteAttdCodeAjax.do",
			dataType : "json",
			data : { 
				userId : $("#attdEditForm #attdUserNm").val(),
				attdDt : $("#attdDt").val(),
			},
			success : function(request){
				fncReload()
			},
			error : function(status){
				alert("삭제 실패")
			}
		})
	}
	
	function fncReload(){
		confirm('취소를 누르면 계속해서 편집','저장되었습니다 화면을 갱신하시겠습니까?', null, function(request){
			if(request){
				location.reload();
			}
		});
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
	function retrieveChargerList(userLocat){
		$.ajax({
			type : "post",
			url : "<c:url value='/itsm/repcharger/mngr/retrieveRepChargerListAjax.do'/>",
			data : {
				'userLocat' : userLocat
	  		},
	  		dataType : "json",
	  		traditional: true,
	  		success : function(request){
	  			
	  		},
	  		error : function(xhr){
	  			alert("불러오기 실패")
	  		}
		})
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
			<form:hidden path="repMasterVO.reportDt"/>
			<form:hidden path="registerFlag"/>
                <div class="page-header">
                    <h3 class="page-title">일일<c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
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
                <c:if test='${repFormVO.repMasterVO.returnResn != "" && repFormVO.repMasterVO.returnResn != " " && repFormVO.repMasterVO.returnResn != null}'>
                <span style="color:red;">반려 사유: <c:out value="${repFormVO.repMasterVO.returnResn }"/></span>
                </c:if>
                
                <table id="masterTb" class="reporttbl mb15">
                    <caption>업무보고서 작성</caption>
                    <colgroup>
                        <col style="width:9%">
                        <c:forEach items="${repAttdList }" var="attd" varStatus="">
                        <col style="width:9.1%">
                        </c:forEach>
                        <c:forEach items="${repAttdList }" var="attd" varStatus="">
                        <col style="width:9.1%">
                        </c:forEach>
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="col">구분</th>
                            <th scope="col" colspan="${repAttdList.size() }">
                             	실적 <c:out value="${repAttdFormVO.execAttdVO.attdDtDisplay }"/>
                             	<c:if test="${sttusCode == 'B301'}">
                             	<button id="execAttdEditBtn" type="button" onclick="fncAttdEditModal('${repAttdFormVO.execAttdVO.attdDtDisplay }')" style="cursor:pointer;" class="label type2">편집</button>
                             	</c:if>
                            </th>
                            <th scope="col" colspan="${repAttdList.size() }">
                            	 계획 <c:out value="${repFormVO.repMasterVO.reportDtDisplay }"/>
                            	 <c:if test="${sttusCode == 'B301'}">
                            	 <span id="planAttdEditBtn" onclick="fncAttdEditModal('${repFormVO.repMasterVO.reportDtDisplay }')" style="cursor:pointer;" class="label type2">편집</span>
                            	 </c:if>
                            </th>
                        </tr>
                        <tr>
                        <th scope="col">전체인원</th>
                        <c:forEach items="${repAttdList }" var="attd" varStatus="">
                            <th scope="col">${attd.cmmnCodeNm }</th>
                        </c:forEach>
                        <c:forEach items="${repAttdList }" var="attd" varStatus="">
                            <th scope="col">${attd.cmmnCodeNm }</th>
                        </c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${locateList }" var="locate" varStatus="status">
                        <tr id="${locate.cmmnCode }">
                            <th scope="row">${locate.cmmnCodeNm}<span class="label type1"> <c:out value="${personNo.get(status.index) }"/></span></th>
                            <c:forEach items="${repAttdList }" var="attd" varStatus="">
                            <td class="exec ${attd.cmmnCode }"></td>
                            </c:forEach>
                            <c:forEach items="${repAttdList }" var="attd" varStatus="">
                            <td class="plan ${attd.cmmnCode }"></td>
                            </c:forEach>
                        </tr>
				        </c:forEach>
                    </tbody>
                </table>

                <table class="reporttbl mb10">
                    <caption>업무보고서</caption>
                    <colgroup>
                        <col style="width:10%">
                        <col style="width:45%">
                        <col style="width:45%">
                    </colgroup>
                    <tbody>
                    <c:forEach  items="${repFormVO.repDetailVOList}" var="repDetailVO" varStatus="status">
                    <c:if test="${repDetailVO.sysCode != 'B100' && repDetailVO.sysCode != 'B128' && repDetailVO.sysCode != 'B129' && repDetailVO.sysCode != 'B130' && repDetailVO.sysCode != 'B131'}">
                    	<input type="hidden" name="sysCodes" value="${repDetailVO.sysCode }">
						<input type="hidden" name="repSns" value="${repDetailVO.repSn }">
                        <tr id="name1">
                            <th scope="row" id="sysCodeNm"><c:out value="${repDetailVO.sysCodeNm }"/><span id="userNm" class="label type2">${repDetailVO.userNm }</span></th>
                            <td>
                                <div class="preview">
                                    <textarea class="control" rows="10" name="execDescs">${repDetailVO.execDesc }</textarea>
                                </div>
                            </td>
                            <td>
                                <div class="preview">
                                    <textarea class="control" rows="10" name="planDescs">${repDetailVO.planDesc }</textarea>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="page-header">
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
                    </div>
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
            
            <!-- attdEdit Modal -->
            <form:form commandName="repAttdFormVO" id="attdEditForm" name="attdEditForm" method="post">
            	<form:hidden id="attdDt" path="repAttdVO.attdDt"/>
            	<div class="modal fade" id="attdEditModal" tabindex="-1" role="dialog">
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				      </div>
				      <div class="modal-body">
				        <div class="container">
				           <h5 id="attdDtDisplay" class="form-signin-heading"></h5><br>
				           <form:select id="attdUserNm" path="repAttdVO.userNm" class="control">
				           	  <form:option value="-">이름</form:option>
					          <form:options itemValue="userId" itemLabel="userNm" items="${repChargerList }"/>
				           </form:select>
				           <form:select id="attdCode" path="repAttdVO.attdCode" class="control">
				           	  <form:option value="-">근태</form:option>
					          <form:options itemValue="cmmnCode" itemLabel="cmmnCodeNm" items="${attdCodeList }"/>
				           </form:select>
				        </div>
				      </div>
				      <div class="modal-footer">
				        <a onclick="fncAttdAdd()" class="btn btn-primary" style="color:white;">추가</a>
				        <a onclick="fncAttdDelete()" class="btn btn-secondary" style="color:white;">삭제</a>
				      </div>
				    </div>
				  </div>
				</div>
            </form:form>
            </main>
</body>
</html>
