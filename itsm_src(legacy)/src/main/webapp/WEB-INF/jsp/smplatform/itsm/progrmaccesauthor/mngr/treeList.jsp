<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
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
<%--     <link type="text/css" rel="stylesheet" href="<c:url value='/css/zTreeStyle/zTreeStyle.css'/>"/>     --%>
<%--     <link type="text/css" rel="stylesheet" href="<c:url value='/css/awesomeStyle/awesome.css'/>"/>     --%>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/metroStyle/metroStyle.css'/>"/>    
    <script src="<c:url value='/js/jquery.ztree.all.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <validator:javascript formName="progrmVO" staticJavascript="false" xhtml="true" cdata="false"/>
	<script type="text/javaScript" language="javascript" defer="defer">
        <!--
        var setting = {
        		view: {
        			selectedMulti: false,
        			enable: true,
    				showIcon : true,
    				showLine : true
    			},
    			check: {
    				chkboxType : { "Y" : "ps", "N" : "ps" },
    				chkStyle : "checkbox",
    				enable: true,
    			},    			
    			data: {
    				key: {
                		name: "progrmNm"
                		,title: "progrmNm"
                		,checked: "checked"
                	},
	    			simpleData: {
	                    enable: true
	    				,idKey: "progrmSn"
	    				,pIdKey: "upperProgrmSn"
	    				,rootPId: "0"
	                }
    			}
    		};
        
        
        $(document).ready(function() {
        	fncRetrieveTreeListAjax();
        	$.ajaxSettings.traditional = true;
        });
        function fncInitTree(zNodes){
        	$.fn.zTree.init($("#menuTree"), setting, zNodes);
        	
        	var zTree = $.fn.zTree.getZTreeObj("menuTree");
        	zTree.expandAll(true);
        }
        
        
        /* 메뉴 다시 조회 */
    	function fncRetrieveTreeListAjax(){
    		$.ajax({
    		    type:'GET',
    		    url:"<c:url value='/progrmaccesauthor/mngr/retrieveTreeListAjax.do'/>",
    		    dataType: "json",
    			data : { 
    				"progrmAccesAuthorCode":$('input[name=progrmAccesAuthorCode]').val()
    		   			},
    			async:false,
    			success : function(request){
    				if(request.returnMessage == 'success'){
    					fncInitTree(request.resultList);
    				}else{
    					alert("등록된 메뉴가 없습니다. 관리자에게 문의하세요.");	
    				}
    			},
    			
    			error:function(r){
    				alert("정보전송에 실패하였습니다.");
    			}
    			
    		});		
    	}
        
        function fncSave(){
        	var treeObj = $.fn.zTree.getZTreeObj("menuTree");
        	var nodes = treeObj.getCheckedNodes(true);
        	console.log(nodes.length);
        	
        	var dataObj = new Object();
        	dataObj.progrmAccesAuthorCode = $('input[name=progrmAccesAuthorCode]').val();
        	dataObj.progrmSns = new Array();
        	 
        	for(var i=0;i<nodes.length;i++){
        		dataObj.progrmSns[i] = nodes[i].progrmSn;
        	}
        	
        	
        	
        	console.log(dataObj);
        	
        	$.ajax({
    		    type:'POST',
    		    url:"<c:url value='/progrmaccesauthor/mngr/createListAjax.do'/>",
    		    dataType: "json",
    			data : dataObj
    		   	,
    			async:false,
    			success : function(request){
    				if(request.returnMessage == 'success'){
    					fncRetrieveTreeListAjax();
    					alert("성공적으로 저장 되었습니다.");
    				}else{
    					alert("저장중 문제가 발생하였습니다. 지속될 경우 관리자에게 문의하세요.");	
    				}
    			},
    			
    			error:function(r){
    				alert("정보전송에 실패하였습니다.");
    			}
    			
    		});	
        }
        
        function fncChangeAuthor(progrmAccesAuthorCode){
        	
        	console.log('progrmAccesAuthorCode: '+progrmAccesAuthorCode);
        	$('input[name=progrmAccesAuthorCode]').val(progrmAccesAuthorCode);
        	$('.authorCodeBtn').removeClass(function(){
        		return "btn-success";
        	});
        	$('.authorCodeBtn').removeClass(function(){
        		return "btn-secondary";
        	});
        	$('.authorCodeBtn').addClass(function(){
        		return "btn-secondary";
        	});
        	$('#btn_'+progrmAccesAuthorCode).removeClass(function(){
        		return "btn-secondary";
        	});        	
        	$('#btn_'+progrmAccesAuthorCode).addClass(function(){
        		return "btn-success";
        	}); 
        	
        	fncRetrieveTreeListAjax();
        }
        //-->
    </script>
</head>
	

<body>
<main id="content" class="mini">
                <div class="page-header">
                    <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
                </div>
                
		<form name="editForm" id="editForm">
		<input type="hidden" name="progrmAccesAuthorCode" value="${progrmAccesAuthorVO.progrmAccesAuthorCode}"/>
                <div class="row">                    
                    <div class="col-7">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">사용자별 권한</h4>
                            </div>
                        </div>
                        <ul class="cardlist type2 authorlist">
						<c:forEach var="result" items="${progrmAccesAuthorCodeVOList}" varStatus="status">
                            <li>
                                <a href="javscript:void(0)" onclick="fncChangeAuthor('${result.cmmnCode}')"
                               			class="item ${result.cmmnCode == progrmAccesAuthorVO.progrmAccesAuthorCode ? 'active' : ''}">
                                    <strong class="author  author${result.sortNo}"><c:out value="${result.cmmnCodeNm}"/></strong>
                                </a>
                            </li>
						</c:forEach>
                        </ul>
                    </div>
                    <div class="col-5">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title"><strong>시스템관리자</strong> 접근권한</h4>
                                <button type="button" onclick="fncSave()" class="btn btn-primary fr"><i class="ico-save"></i>저장</button>
                            </div>
                            <div class="card-body">
                                <div class="scrollbox" style="height:380px">
                                    <ul class="ztree" id="menuTree"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>   
        </form>             
</main>
</body>
</html>
