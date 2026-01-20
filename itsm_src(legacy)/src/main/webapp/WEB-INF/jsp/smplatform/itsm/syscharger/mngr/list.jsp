<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></title>
	<script type="text/javaScript" language="javascript" defer="defer">
	
    
    /* 담당 서비스 목록 조회 */
	function fncRetrieveSysChargerListAjax(userId, userNm){
    	
		$.ajax({
		    type:'POST',
		    url:"<c:url value='/syscharger/mngr/retrieveSysChargerListAjax.do'/>",
		    dataType: "json",
			data : { 
				"userId":userId
		   			},
			async:false,
			success : function(request){
				if(request.returnMessage == 'success'){
					$('#userId').val(userId);
					$('.infobox').html("<strong>"+userNm+"</strong>("+userId+")");
					
					fncInitSysCharger(request.resultList);
				}else{
					alert("등록된 메뉴가 없습니다. 관리자에게 문의하세요.");	
				}
			},
			
			error:function(r){
				alert("정보전송에 실패하였습니다.");
			}
			
		});		
	}
    
    function fncInitSysCharger(resultList){
    	console.log(resultList.length);
    	 var sysChargerTBodyHTML = "<ul class='checklist'>";
    	for(var i=0;i<resultList.length; i++){
    		console.log(resultList[i].userId);
    		if(resultList[i].userId == null){
    			sysChargerTBodyHTML += "<li><input id='chk"+i+"' type='checkbox' class='checkbox' name='sysCodes' value='"+resultList[i].sysCode+"'/>";
    		}else{
    			sysChargerTBodyHTML += "<li><input id='chk"+i+"' type='checkbox' class='checkbox' name='sysCodes' checked='checked' value='"+resultList[i].sysCode+"'/>";	
    		}
    		
    		sysChargerTBodyHTML += "<label for='chk"+i+"'>"+resultList[i].sysCodeNm+"</label></li>";
    	}
    		sysChargerTBodyHTML += "</ul>";
    	$('#sysCheckbox').html(sysChargerTBodyHTML);
    	
    	$(".scrollbox").mCustomScrollbar("update");
    	
    }
    
    function fncSave(){
    	var frm = document.editForm;
    	
    	if(frm.sysCodes.length == undefined){
    		if(!frm.sysCodes.checked){
    			alert('선택된 시스템이 없습니다.');
    			return;
    		} 
    	}else{
    		var isChecked = false;
    		for(var i=0;i<frm.sysCodes.length;i++){
    			if(frm.sysCodes[i].checked){
    				isChecked = frm.sysCodes[i].checked;
        		} 
    		}
    		
    		if(!isChecked){
    			alert('선택된 시스템이 없습니다.');
    			return;
    		}
    	}
    	
    	confirm('저장하시겠습니까?','시스템별 담당 저장', null, function(request){
			console.log('request: '+request);
			if(request){
				fncCreateAjax();
			}
    	});
    	
    }
   
    function fncCreateAjax(){
		$.ajax({
		    type:'POST',
		    url:"<c:url value='/syscharger/mngr/createListAjax.do'/>",
		    dataType: "json",
			data : $('#editForm').serialize(),
			async:false,
			success : function(request){
				if(request.returnMessage == 'success'){
					fncExecRetrieveList();
				}else{
					alert("저장중 문제가 발생하였습니다. 지속될 경우 관리자에게 문의하세요.");	
				}
			},
			
			error:function(r){
				alert("정보전송에 실패하였습니다.");
			}
			
		});		
	}
    
    function fncRetrieveList() {
    	confirm('저장하지 않은 정보는 잃어버릴 수 있습니다. 계속하시겠습니까?','시스템담당자 재조회', null, function(request){
			console.log('request: '+request);
			if(request){
				fncExecRetrieveList();
			}
    	});
    }
    	
    
    function fncExecRetrieveList() {
    	$('#userId').val('');
		document.editForm.action = "<c:url value='/syscharger/mngr/retrieveList.do'/>";
       	document.editForm.submit();
    }
    
   
    </script>
</head>
	

<body>
         <main id="content">
                <div class="page-header">
                    <h3 class="page-title">시스템 담당자 관리</h3>
                </div>

                <div class="row">
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">시스템 담당자</h4>
                                <button type="button" onclick="fncRetrieveList()" class="btn btn-primary fr"><i class="ico-search"></i>재조회</button>
                            </div>
                        </div>

                        <ul class="cardlist type2">
                        	<c:forEach var="result" items="${resultList}" varStatus="status">
                            <li>
                            	<div onclick="fncRetrieveSysChargerListAjax('<c:out value="${result.userId}"/>', '<c:out value="${result.userNm}"/>')" class="item">
<%--                                 <a href="javascript:fncRetrieveSysChargerListAjax('<c:out value="${result.userId}"/>', '<c:out value="${result.userNm}"/>')" class="item"> --%>
                                    <div class="row">
                                        <div class="col-3">
                                            <em class="circle"><c:out value="${result.userNm}"/></em>
                                        </div>
                                        <div class="col">
                                            <div class="viewbox type1">
                                                <dl>
                                                    <dt>아이디</dt>
                                                    <dd><b><c:out value="${result.userId}"/></b></dd>
                                                </dl>
                                                <hr>
                                                <dl>
                                                    <dt>담당</dt>
                                                    <dd>
                                               	 	<c:forEach var="sysChargerVO" items="${result.sysChargerVOList}" varStatus="status">
                                                    <c:out value="${sysChargerVO.sysCodeNm}"/> ${status.last ? '' : ',' }
				            						</c:forEach>
                                                    </dd>
                                                </dl>
                                            </div>
                                        </div>
                                    </div>
                                  </div>
<!--                                 </a> -->
                            </li>
				        	</c:forEach>
				        	<c:if test="${empty resultList}">
				        		<li>
				        			<dl>검색된 데이터가 없습니다.</dl>
				        		</li>
				        	</c:if>
                        </ul>
                        
                    </div>
                    <form name="editForm" id="editForm">
                    <input type="hidden" id="userId" name="userId"/>
                    <div class="col-fix">
                        <div class="card stickyaside">
                            <div class="card-header type2">
                                <h4 class="card-title">담당시스템</h4>
                                <button type="button" onclick="fncSave()" class="btn btn-dark fr"><i class="ico-save"></i>저장</button>
                            </div>
                            <div id="infoDiv" class="card-body scrollbox" style='max-height:550px;overflow-x:hidden'>
                                <!-- 선택전 -->
                                <div class="infobox">
                                    담당자를 선택해주세요!
                                </div>
                                <div id="sysCheckbox" >
                                </div>
                                <!-- 선택전 -->
                            </div>
                        </div>
                    </div>
                    </form>
                </div>
            </main>
                      		
          		
       
</body>
</html>
