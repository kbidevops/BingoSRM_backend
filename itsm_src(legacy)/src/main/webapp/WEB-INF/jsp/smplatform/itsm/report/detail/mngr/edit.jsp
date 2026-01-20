<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <c:set var="registerFlag" value="${empty repFormVO.repDetailVO.userId ? 'create' : 'modify'}"/>
    <title><c:out value="일일 ${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></title>
    <!--For Commons Validator Client Side-->
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <validator:javascript formName="repFormVO" staticJavascript="false" xhtml="true" cdata="false"/>

<script type="text/javascript" language="javascript" defer="defer">
	$(function() {
		$(".singleDate").daterangepicker({
            singleDatePicker: true,
            format: 'YYYY-MM-DD',
            isInvalidDate: function(date){
                if(date.day() == 0 || date.day() == 6){
                	return true;
                }
                return false;
            }
        }); 
		
		frm = document.listForm;
	    
	    <c:if test="${registerFlag == 'modify'}">
	    //확정된 날짜의 보고서일시 수정삭제불가
	      <c:if test='${repStatus == "B302" || repStatus == "B303"}'>
		 	$("textArea").attr('readonly', true);
		 	$("input[name='execAttd']").attr("disabled", "disabled");
		 	$("input[name='planAttd']").attr("disabled", "disabled");
			$("#deleteBtn").attr("disabled", "disabled");
			$("#saveBtn").attr("disabled", "disabled");
			$("#confirm_note").removeAttr("style");
		  </c:if>
		</c:if>
		
		//근태 휴가 내용 생성
		$("#execAttd").on('change', function(){
			var attdCodeId = $(this).children("input:checked").attr("id");
			var attdCodeNm = $("label[for='"+attdCodeId+"']").text();
			
			if(attdCodeNm == "연차" || attdCodeNm == "월차" || attdCodeNm == "공가") {
				var text = "1. " + attdCodeNm + "휴가" + "\n"
							+ "  - 개인 사정에 의한 " + attdCodeNm + "휴가" + "\n"
							+ "  - 대무자: "
				$("textarea[name='execDescs']:eq(0)").val(text);
				return;
			}
			
		});
		
		$("#planAttd").on('change', function(){
			var attdCodeId = $(this).children("input:checked").attr("id");
			var attdCodeNm = $("label[for='"+attdCodeId+"']").text();
			
			if(attdCodeNm == "연차" || attdCodeNm == "월차" || attdCodeNm == "공가"){
				var text = "1. " + attdCodeNm + "휴가" + "\n"
							+ "  - 개인 사정에 의한 " + attdCodeNm + "휴가" + "\n"
							+ "  - 대무자: "
				$("textarea[name='planDescs']:eq(0)").val(text);
				return;
			}
			
		});
	});

	function fncSave(){
		if($("#reportDt").val() == ''){
			alert("날짜를 선택해주세요");
			return;
		}
		
		<c:if test="${registerFlag == 'create'}">
			var exec = $("#execAttd input:checked").next("label").text();
			var plan = $("#planAttd input:checked").next("label").text();

			//sysCode 배열로 만들기
			var sysValue = $("input[name='sysCodes']").length;
			var sysCodes = new Array(sysValue);
			for(var i=0; i<sysValue; i++){                          
				sysCodes[i] = $("input[name='sysCodes']")[i].value;
			}
			
		 	$.ajax({
		 		type : "post",
		 		url : "<c:url value='/itsm/rep/detail/mngr/retrieveAjax.do'/>",
		 		data : {
		 			"reportDt" : frm.elements.reportDt.value,
		 			"sysCodes" : sysCodes,
		 			"repTyCode" : "B001"
		 		},
		 		dataType : "json",
		 		traditional: true,
		 		async: true,
		 		success : function(rq){
		 			console.log(rq);
		 			if(rq.returnMessage == "create"){//중복 없음
		 				
		 				confirm('실적: '+exec +'<br> 계획: '+ plan,'정말 작성하시겠습니까?', null, function(request){
		 				 	if(request){
		 				 		$("input[name='execAttd']").val($("#execAttd input:checked").val());
								$("input[name='planAttd']").val($("#planAttd input:checked").val());
				 				frm.action = "<c:url value='/itsm/rep/detail/mngr/create.do'/>";
				 				frm.submit();
		 				 	}else{
		 				 		return false;
		 				 	}
		 				})
		 			} else if(rq.returnMessage == "confirm"){//마스터 보고서가 확정됨
		 				alert("같은 날짜에 확정된 보고서가 있습니다. \n관리자에게 문의해주세요.")
		 			
		 			}else {//중복된 보고서 있음
		 				confirm('중복된 보고서가 있습니다.','정말 덮어 씌우시겠습니까?', null, function(request){
		 				 	if(request){
		 						$("#repSn").val(rq.returnMessage);
		 						document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/update.do'/>";
		 						document.listForm.submit();
		 				 	}else{
		 				 		return false;
		 				 	}
		 				})
		 			}
		 				
		 		},
		 		error : function(xhr){
		 			alert("보고서를 등록하는데 실패하였습니다. 관리자에게 문의해주세요.");
		 		}
		 		
		 	})
		</c:if>
		
		<c:if test="${registerFlag != 'create'}">
			$("input[name='execAttd']").val($("#execAttd input:checked").val());
			$("input[name='planAttd']").val($("#planAttd input:checked").val());
			document.listForm.elements['repMasterVO.repSn'].value = document.listForm.elements['repDetailVO.repSn'].value;
			frm.action = "<c:url value='/itsm/rep/detail/mngr/update.do'/>";
			frm.submit();
		</c:if>
	}
	
	function fncRetrieveRep(){
		
		if($("#reportDt").val() == ''){
			alert("날짜를 선택해주세요");
			return;
		}
		
		console.log(frm.elements.reportDt.value)
		$.ajax({
	  		type : "post",
	  		url : "<c:url value='/itsm/rep/detail/mngr/retreiveRepDetailAjax.do'/>",
	  		data : {
	  			reportDt : frm.elements.reportDt.value,
				repTyCode : "B001"
	  		},
	  		dataType : "json",
	  		success : function(request){
	  			console.log(request);
	  			if(request.detailVOList.length < 1){
	  				alert("작성된 보고서가 없습니다.")
	  				return;
	  			}
	  			
	  			$("textArea").val("");
	  			$.each(request.detailVOList, function(idx, detailVO){
	  				$("#"+detailVO.sysCode+" textarea[name='execDescs']").val(detailVO.execDesc);
	  				$("#"+detailVO.sysCode+" textarea[name='planDescs']").val(detailVO.planDesc);
	  			});
	  			
	  			$('#execAttd0').prop('checked','checked');
	  			$('#planAttd0').prop('checked','checked');
	  			
	  			if(request.execAttdVO != null){
	  				$('#execAttd input[value="'+request.execAttdVO.attdCode+'"]').attr('checked','checked');
	  			}
	  			if(request.planAttdVO != null){
	  				$('#planAttd input[value="'+request.planAttdVO.attdCode+'"]').attr('checked','checked');
	  			}
	  		},
	  		error : function(xhr){
	  			alert("보고서를 불러오는데 실패하였습니다.");
	  			console.log(xhr.status);
	  		}
	  	})
	}
	
	function fncDelete(){
		confirm('삭제하시겠습니까?','보고서 삭제', null, function(request){
			if(request){
				$("input[name='execAttd']").val($("#execAttd input:checked").val());
				$("input[name='planAttd']").val($("#planAttd input:checked").val());
				
				frm.action = "<c:url value='/itsm/rep/detail/mngr/delete.do'/>";
				frm.submit();
			}
		})
		
	}
	
	function fncRetrieveList(){
		
		$("#reportDt").val("")
		
		document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/retrievePagingList.do'/>";
       	document.listForm.submit();
	}
</script>
</head>

<body>
		<main id="content">
            <form:form commandName="repFormVO" id="listForm" name="listForm" method="post">
                <form:hidden path="repDetailVO.saveToken" />
                <form:hidden path="repMasterVO.repTyCode" />
                <form:hidden path="repMasterVO.repSn" id="repSn"/>
				<input type="hidden" name="registerFlag" value="${registerFlag }">
				<input type="hidden" name="repDetailVO.checkDuo" value="false"/>
	            <c:if test="${registerFlag != 'create'}">
                	<form:hidden path="repDetailVO.repSn"/>
	            	<input type="hidden" value="<fmt:formatDate value='${repFormVO.repDetailVOList.get(0).creatDt }' pattern='yyyy-MM-dd'/>" name="repDetailVO.creatDt">
	            	<input type="hidden" value="${repFormVO.repDetailVOList.get(0).reportDtDisplay }" name="repDetailVO.reportDt">
	            	<input type="hidden" value="${repFormVO.repDetailVOList.get(0).reportDtDisplay }" name="planAttdVO.attdDt">
	          	</c:if>
					<div class="page-header"> 
	                	<div class="row">
	                		<div class="col-4">
	                			<h3 class="page-title">
	                			<c:if test="${registerFlag != 'create'}">
	                			<strong> <fmt:formatDate value="${repFormVO.repDetailVOList.get(0).creatDt }" pattern="yyyy-MM-dd"/></strong>	
	                			</c:if>
	                			일일<c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
	                		</div>
	                		
	                		<div class="col-4">
	                		<c:if test="${registerFlag == 'create'}">
		                		<div class="input-group">
			            			<form:input id="reportDt" path="repDetailVO.reportDt" class="control singleDate" title="작성일 선택기"/>
			            			<button type="button" onclick="fncRetrieveRep()" class="btn btn-primary">이전 보고서 불러오기</button>
			            		</div>
	                		</c:if>
	                		</div>
	                		
	                		<div class="col-4 text-right">
		                        <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
	                			<c:if test="${registerFlag != 'create'}">
		                			<c:if test="${chargerId == LOGIN_USER_VO.userId }">
			                        	<button type="button" id="deleteBtn" onclick="fncDelete()" class="btn btn-dark"><i class="ico-delete"></i>삭제</button>
		                			</c:if>
		                        </c:if>
		                        <c:if test="${registerFlag == 'create' || chargerId == LOGIN_USER_VO.userId}">
		                        	<button type="button" id="saveBtn" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
		                        </c:if>
	                		</div>
	                	</div>
	                	<div id="confirm_note" style="display:none;" class="row">
	            			<p class="col-8 text-left">확정 또는 확정 요청중인 보고서입니다. 수정을 원하시면 관리자에게 문의해주세요.</p>
	            		</div>
	                </div>
	                
                	<table class="reporttbl">
	                    <caption>업무보고서 작성</caption>
	                    <colgroup>
	                        <col style="width:10%">
	                        <col style="width:45%">
	                        <col style="width:45%">
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th scope="col">구분</th>
	                            <th scope="col">실적</th>
	                            <th scope="col">계획</th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                        <tr>
	                            <th scope="row">근태</th>
	                            <td id="execAttd">
	                            	<input id="execAttd0" type="radio" name="execAttd" value="-" class="radio" checked/>
			                        <label for="execAttd0">근무</label>
	                                <c:forEach items="${attdCodeList }" var="attdCodes" varStatus="status">
		                                <c:if test="${attdCodes.cmmnCode == execAttdVO.attdCode }">
			                                <input id="execAttd${status.count }" type="radio" name="execAttd" value="${attdCodes.cmmnCode }" class="radio" checked/>
			                                <label for="execAttd${status.count }">${attdCodes.cmmnCodeNm }</label>
		                                </c:if>
										<c:if test="${attdCodes.cmmnCode != execAttdVO.attdCode }">
			                                <input id="execAttd${status.count }" type="radio" name="execAttd" value="${attdCodes.cmmnCode }" class="radio"/>
			                                <label for="execAttd${status.count }">${attdCodes.cmmnCodeNm }</label>
										</c:if>
	                                </c:forEach>
	                            </td>
	                            <td id="planAttd">
	                            	<input id="planAttd0" type="radio" name="planAttd" value="-" class="radio" checked/>
			                        <label for="planAttd0">근무</label>
	                                <c:forEach items="${attdCodeList }" var="attdCodes" varStatus="status">
		                                <c:if test="${attdCodes.cmmnCode == planAttdVO.attdCode }">
			                                <input type="radio" id="planAttd${status.count }" name="planAttd" value="${attdCodes.cmmnCode }" class="radio" checked/>
			                                <label for="planAttd${status.count }">${attdCodes.cmmnCodeNm }</label>
		                                </c:if>
										<c:if test="${attdCodes.cmmnCode != planAttdVO.attdCode }">
			                                <input type="radio" id="planAttd${status.count }" name="planAttd" value="${attdCodes.cmmnCode }" class="radio"/>
			                                <label for="planAttd${status.count }">${attdCodes.cmmnCodeNm }</label>
										</c:if>
	                                </c:forEach>
	                            </td>
	                        </tr>
	                        
	                        <c:forEach items="${repDetailVOList }" var="repDetailVO" varStatus="status">
	                        <c:if test="${repDetailVO.sysCode != 'B128' && repDetailVO.sysCode != 'B129' && repDetailVO.sysCode != 'B130' && repDetailVO.sysCode != 'B131' && repDetailVO.sysCode != 'B100'}">
		                        <input type="hidden" class="sysCodes" name="sysCodes" value="${repDetailVO.sysCode }">
		                        <c:if test="${registerFlag == 'modify' }"><input type="hidden" name="repSns" value="${repDetailVO.repSn }"></c:if>
		                        <tr id="${repDetailVO.sysCode }">
		                            <th scope="row">${repDetailVO.sysCodeNm }</th>
		                            <td>
		                                <div class="preview">
		                                    <textarea rows="15" cols="50" name="execDescs" ><c:if test="${registerFlag != 'create' }">${repDetailVO.execDesc}</c:if></textarea>
		                                </div>
		                            </td>
		                            <td>
		                                <div class="preview">
		                                    <textarea rows="15" cols="50" name="planDescs"><c:if test="${registerFlag != 'create' }">${repDetailVO.planDesc}</c:if></textarea>
		                                </div>
		                            </td>
		                        </tr>
	                        </c:if>
	                        </c:forEach>
	                    </tbody>
                	</table>
            </form:form>
            </main>
</body>
</html>
