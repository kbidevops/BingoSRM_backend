<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
  /**
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
<!DOCTYPE html>
<html lang="ko">
<head>
    <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></title>
	<script type="text/javaScript" language="javascript" defer="defer">
	
	<!--
    var processTimerId = 0;
    var srList = [];
    var isFirst = true;
    
    $.ajaxSetup({
    	cache: false
    });

    $(document).ready(function () {
        if ('Notification' in window && Notification.permission !== 'denied') {
            Notification.requestPermission();
        }
        fncRetrieveListAjax();
    });
    /* 글 수정 화면 function */
    function fncUpdateView(srvcRsponsNo) {
        clearTimeout(processTimerId);
        document.listForm.elements['srvcRsponsVO.srvcRsponsNo'].value = srvcRsponsNo;
        document.listForm.action = "<c:url value="/srvcrspons/mngr/updateView.do" />";
        document.listForm.submit();
    }

    function fncUpdateStateView(srvcRsponsNo) {
        clearTimeout(processTimerId);
        document.listForm.elements['srvcRsponsVO.srvcRsponsNo'].value = srvcRsponsNo;
        document.listForm.action = "<c:url value="/srvcrspons/mngr/updateStateView.do" />";
        document.listForm.submit();
    }
    /* 글 등록 화면 function */
    function fncCreateView() {
        clearTimeout(processTimerId);
        document.listForm.action = "<c:url value="/srvcrspons/mngr/createView.do"/>";
        document.listForm.submit();
    }
    /* 글 목록 화면 function */
    function fncRetrieveList() {
        clearTimeout(processTimerId);
        document.listForm.action = "<c:url value="/srvcrspons/mngr/retrieveList.do"/>";
        document.listForm.submit();
    }
    /* 1차 응답 */
    function fncUpdateStateAjax(srvcRsponsNo) {
        $.ajax({
            type: "POST",
            url: "/srvcrspons/mngr/updateStateAjax.do",
            dataType: "json",
            data: {
                "srvcRsponsNo": srvcRsponsNo
            },
            async: false,
            success: function (request) {
                if (request.returnMessage == 'success') {
//                 	if(alert(" 접수된 SR목록을 갱신하시겠습니까?SR처리 재조회")){
//                 	}; 
                 confirm('데이터변경 4시간 기준으로 1차응답 처리하였습니다.', '', null, function (request) {
                     clearTimeout(processTimerId);
                     fncRetrieveList();
                 });
                } else {
                    alert("처리에 문제가 발생되었습니다.");
                }
            },

            error: function (err) {
                alert("정보전송에 실패하였습니다.");
            }
        });
    }
    /** 
     * SR 목록 새로고침
     */
    function fncRetrieveListAjax() {
    	
    	var saveToken = $("input[name='srvcRsponsVO.saveToken']").val();
    	var loadingURI;
        if (document.listForm.elements['searchSrvcRsponsVO.excludeprocessYn'].checked) {
            loadingURI = "<c:url value="/srvcrspons/mngr/retrieveListDiv.do?excludeprocessYn=Y"/>";
		    loadingURI = loadingURI + "&saveToken="+saveToken
        } else {
            loadingURI = "<c:url value="/srvcrspons/mngr/retrieveListDiv.do"/>";
	        loadingURI = loadingURI + "?saveToken="+saveToken
        }
        
        $('#jobList').load(loadingURI, function() {
            if (isFirst) {
            	$('#jobList i.sr-num').each(function(index) {
                    var srNo = $(this).text();
                    srList.push(srNo);
                });
                isFirst = false;
                console.log("SR list : ", srList);
               
            } else {
            	
                var newSrList = [];
                
                $('#jobList i.sr-num').each(function(index) {
                    var srNo = $(this).text();
                    newSrList.push(srNo);
                });
                
                console.clear();
            	console.log("before SR list : ", srList);
                console.log("after SR list : ", newSrList);
                
                if($(".new").length > 0){
	               $.ajax({
		                type:'POST',
		                url:"<c:url value='/srvcrspons/mngr/retrieveListDivSmsAjax.do'/>",
		                dataType: "json",
		            	data : { 
		            		"newSrList" : newSrList
		               			},
		            	async:false,
		            	success : function(request){
		            		console.log()
		            	},
		            	error:function(r){
		            		alert("정보전송에 실패하였습니다.");
		            	}        			
	            	});	
                }

                if (JSON.stringify(srList) !== JSON.stringify(newSrList)) {
                	if ('Notification' in window) {
                        new Notification('SR 목록이 변경되었습니다.');
                    }
                    srList = newSrList;
                }
            }
			
            clearTimeout(processTimerId);
            processTimerId = setTimeout("fncRetrieveListAjax()", 3 * 10 * 1000);
            console.log('%c%s', 'font-weight: bold; color: #3287FC;', 'processTimerId: ' + processTimerId);
            
            dotdot();
        });
        
        function fillZeros(n, digits) {  
            var zero = '';  
            n = n.toString();  
      
            if (n.length < digits) {  
                for (i = 0; i < digits - n.length; i++)  
                    zero += '0';  
            }  
            return zero + n;  
        }  
        
        function getTimeStamp(date) {  
            var d = new Date(date);  
      
            var s = fillZeros(d.getFullYear(), 4) + '-' +  
                    fillZeros(d.getMonth() + 1, 2) + '-' +  
                    fillZeros(d.getDate(), 2) + ' ' +  
              
                    fillZeros(d.getHours(), 2) + ':' +  
                    fillZeros(d.getMinutes(), 2) + ':' +  
                    fillZeros(d.getSeconds(), 2);  
      
            return s;  
        }  
        
        function dotdot(){
           $(".dotdotSj").each(function(){
                var length = 30;
                $(this).each(function(){
	                if($(this).text().length >= length ){
	                    $(this).text($(this).text().substr(0,length) + "...")
	                }
           		})
      		})
      		
      		$(".dotdotCn").each(function(){
                 var length = 60;
                 $(this).each(function(){
 	                if($(this).text().length >= length ){
 	                    $(this).text($(this).text().substr(0,length) + "...")
 	                }
            		})
       		})
   	    }
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
			<form:hidden path="srvcRsponsVO.saveToken" />
			<input type="hidden" name="srvcRsponsVO.srvcRsponsNo" />
			<input type="hidden" name="returnListMode" value="stateList"/>
	        <div class="card">
	            <div class="card-header">
	                <div class="fl">
	                    <button type="button" onclick="fncCreateView()" class="btn btn-dark"><i class="ico-write"></i>등록</button>
	                </div>
	                <div class="fr"> 
	                    <form:checkbox path="searchSrvcRsponsVO.excludeprocessYn" value="Y" id="chk1"/>
	                    <label for="chk1">완료 제외 </label>
	                    <button type="button" class="btn btn-primary" onclick="fncRetrieveList()"><i class="ico-search"></i>재검색</button>
	                </div>
	            </div>
	        </div>
	
	        <ul class="cardlist type1" id="jobList">
	        </ul> 
        </form:form>
	</main>
</body>
</html>
