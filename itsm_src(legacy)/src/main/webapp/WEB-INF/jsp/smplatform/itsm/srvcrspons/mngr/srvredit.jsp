<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
  /**
  * @Class Name : /srvcrspons/mngr/edit.jsp
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
  <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
  <validator:javascript formName="srvcRsponsFormVO" staticJavascript="false" xhtml="true" cdata="false"/>
  <script type="text/javaScript" language="javascript" defer="defer">
    var requstAtchmnflFileCnt = 0;
    var rsponsAtchmnflFileCnt = 0;

    $(document).ready(function() {
      $("#subject").on("input", function() {
        var value = $(this).val();

        if (value.length > 40) {
          $(this).val(value.substring(0, 40));
        }
      });

      $("#subject").on("change", function() {
        var value = $(this).val();

        if (value.length > 40) {
          $(this).val(value.substring(0, 40));
        }
      });

      if ($("#srvcRsponsClCode").val() === "S103" || $("#srvcRsponsClCode").val() === "S107") {
        $("#processStdrCode").prop("disabled", false);
      }
      else {
        $("#processStdrCode").prop("disabled", true);
      }

      $("#requstDtDateDisplay").mask("9999-99-99");
      $("#requstDtTimeDisplay").mask("99:99");
      $("#rspons1stDtDateDisplay").mask("9999-99-99");
      $("#rspons1stDtTimeDisplay").mask("99:99");
      $("#processDtDateDisplay").mask("9999-99-99");
      $("#processDtTimeDisplay").mask("99:99");
      $('.dropdown-toggle').dropdown();
      $('#requstAtchmnflSendBtn').hide();
      $('#rsponsAtchmnflSendBtn').hide();

      var url = "<c:url value='/atchmnfl/site/create.do'/>";

      $("#srvcRsponsClCode").on("change", function() {
        var srvcRsponsClCode = $(this).val();

        if (srvcRsponsClCode === "S104") {
          $("#processStdrCode").children("option[value='S204']").prop("selected", true);
          $("#processStdrCode").prop("disabled", true);
        }
        else if (srvcRsponsClCode === "S101" || srvcRsponsClCode === "S102") {
          $("#processStdrCode").children("option[value='S201']").prop("selected", true);
          $("#processStdrCode").prop("disabled", true);
        }
        else if (srvcRsponsClCode === "S105" || srvcRsponsClCode === "S106") {
          $("#processStdrCode").children("option[value='S202']").prop("selected", true);
          $("#processStdrCode").prop("disabled", true);
        }
        else if (srvcRsponsClCode === "S103") {
          $("#processStdrCode").children("option[value='S202']").prop("selected", true);
          $("#processStdrCode").children("option[value='S201']").prop("disabled", true);
          $("#processStdrCode").prop("disabled", false);
        }
        else {
          $("#processStdrCode").prop("disabled", false);
        }
      });

      $('#requstAtchmnfl').fileupload({
        url: url,
        dataType: "json",
        formData: {
          atchmnflId: $("#requstAtchmnflId").val(),
        },
        sequentialUploads: true,
        add: function (e, data) {
          var valid = true;
          var re = /^.+\.((doc)|(txt)|(xls)|(xlsx)|(docx)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(gif)|(png)|(bmp))$/i;

          $.each(data.files, function (index, file) {
            console.log('Added file: ' + file.name);
            console.log('Added file: ' + file.type);
            console.log('Added file: ' + file.size);
            console.log('Added file: ' + filesize(file.size));
            // console.log('file size: ' + data.getNumberOfFiles());

            requstAtchmnflFileCnt++;

            if (!re.test(file.name)) {
              valid = false;
              alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
              requstAtchmnflFileCnt--;
            }
            // 5M
            else if (file.size > 5000000) {
              console.log('5M이하의 파일만 허용 합니다.');
              alert(file.name + ' <br/>5M이상의 첨부파일은 제외됩니다.');
              requstAtchmnflFileCnt--;
              valid = false;
            }
            // 동시 업로드 5개 제한
            else if (requstAtchmnflFileCnt > 5) {
              console.log('5개의 파일만 허용 합니다.' + requstAtchmnflFileCnt);
              alert(file.name + ' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
              requstAtchmnflFileCnt--;
              valid = false;
            }
            else {
              console.log('정상등록 ' + requstAtchmnflFileCnt);
              var abortBtn = $('<a/>')
                .attr('href', 'javascript:void(0)')
                .attr('class', 'close')
                .attr('aria-label', 'Close')
                .append('<span aria-hidden="true">&times;</span>')
                .click(function() {
                  data.abort();
                  data.context.remove();
                  data = null;

                  if (requstAtchmnflFileCnt>0) {
                    requstAtchmnflFileCnt--;
                    console.log('requstAtchmnflFileCnt: ' + requstAtchmnflFileCnt);
                  }
                });

              data.context = $('<div/>').appendTo($('#requstAtchmnflFileListDiv'));
              data.context.attr('class', 'form-row');
              data.context.append(abortBtn).append(file.name + '(' + filesize(file.size) + ')');
            }
          });

          $("#requstAtchmnflSendBtn").click(function () {
            // validate file...
            if (valid && data !== null) {
              data.submit();
              // 해당 폼 전송 추가 하면 저장 버튼 완료
            }
          });
        },
        done: function (e, data) {
          console.log('done: '+data.errorThrown);
          console.log('done: '+data.textStatus);
          console.log('done: '+data.jqXHR);
        },
        progressall: function (e, data) {
          // var progress = parseInt(data.percent * 100, 10);
        	var progress = parseInt(data.loaded / data.total * 100, 10);

          console.log('data.bitrate:' + data.bitrate);
          console.log('data.bitrate:' + progress);

          $('#requstAtchmnfl-progress .progress-bar').css('width', progress + '%');
          $('#requstAtchmnfl-progress .progress-bar').html(progress + '%');

          if (progress === 100) {
            if (rsponsAtchmnflFileCnt > 0) {
              $("#rsponsAtchmnflSendBtn").trigger("click");
            }
            else {
              frm.action = "<c:url value="${registerFlag == 'create' ? '/srvcrspons/mngr/updatesrproc.do' : '/srvcrspons/mngr/updatesrproc.do'}"/>";
              frm.submit();
            }
          }
        },
        progressServerRate: 0.3,
      });

      $('#rsponsAtchmnfl').fileupload({
        url: url,
        dataType: "json",
        formData: {
          atchmnflId: $("#rsponsAtchmnflId").val(),
        },
        sequentialUploads: true,
        add: function (e, data) {
          var valid = true;
          var re = /^.+\.((doc)|(xls)|(xlsx)|(docx)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(gif)|(png)|(bmp))$/i;

          $.each(data.files, function (index, file) {
            console.log('rsponsAtchmnfl Added file: ' + file.name);
            console.log('rsponsAtchmnfl Added file: ' + file.type);
            console.log('rsponsAtchmnfl Added file: ' + file.size);
            console.log('rsponsAtchmnfl Added file: ' + filesize(file.size));
            // console.log('file size: ' + data.getNumberOfFiles());

            rsponsAtchmnflFileCnt++;

            if (!re.test(file.name)) {
              valid = false;
              alert(file.name + ' <br/>허용되지 않은 첨부파일은 제외됩니다.');
              rsponsAtchmnflFileCnt--;
            }
            // 5M
            else if (file.size > 5000000) {
              console.log('5M이하의 파일만 허용 합니다.');
              alert(file.name + ' <br/>5M이상의 첨부파일은 제외됩니다.');
              rsponsAtchmnflFileCnt--;
              valid = false;
            }
            // 동시 업로드 5개 제한
            else if (rsponsAtchmnflFileCnt > 5) {
              console.log('5개의 파일만 허용 합니다.' + rsponsAtchmnflFileCnt);
              alert(file.name + ' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
              rsponsAtchmnflFileCnt--;
              valid = false;
            }
            else {
              console.log('rsponsAtchmnfl 정상등록 ' + rsponsAtchmnflFileCnt);
              var abortBtn = $( '<a/>' )
                .attr('href', 'javascript:void(0)')
                .attr('class', 'close')
                .attr('aria-label', 'Close')
                .append('<span aria-hidden="true">&times;</span>')
                .click(function() {
                  data.abort();
                  data.context.remove();
                  data = null;

                  if (rsponsAtchmnflFileCnt > 0) {
                    rsponsAtchmnflFileCnt--;
                    console.log('rsponsAtchmnflFileCnt: ' + rsponsAtchmnflFileCnt);
                  }
                });

              data.context = $('<div/>' ).appendTo($('#rsponsAtchmnflFileListDiv'));
              data.context.attr('class', 'form-row');
              data.context.append(abortBtn).append(file.name+'(' + filesize(file.size) + ')');
            }
          });

          $("#rsponsAtchmnflSendBtn").click(function () {
            // validate file...
            if (valid && data !== null) {
              data.submit();
              // 해당 폼 전송 추가 하면 저장 버튼 완료
            }
          });
        },
        done: function (e, data) {
          console.log('done: ' + data.errorThrown);
          console.log('done: ' + data.textStatus);
          console.log('done: ' + data.jqXHR);
        },
        progressall: function (e, data) {
          // var progress = parseInt(data.percent * 100, 10);
        	var progress = parseInt(data.loaded / data.total * 100, 10);
          console.log('data.bitrate:' + data.bitrate);
          console.log('data.bitrate:' + progress);

          $('#rsponsAtchmnfl-progress .progress-bar').css('width', progress + '%');
          $('#rsponsAtchmnfl-progress .progress-bar').html(progress + '%');

          if (progress === 100) {
            frm.action = "<c:url value="${registerFlag == 'create' ? '/srvcrspons/mngr/updatesrproc.do' : '/srvcrspons/mngr/updatesrproc.do'}"/>";
            frm.submit();
          }
        },
        progressServerRate: 0.3,
      });

      // $("#rqesterCttpc").mask("999-9999-9999"); //전화, 휴대폰, xxxx-xxxx등과 같은 전화번호가 다양해 적용 불가
      $('#srvcRsponsNo').tooltip('show');
    });

    function fncRequest() {
        frm = document.listForm;

        if (document.listForm.elements['srvcRsponsVO.reRequestDtDateDisplay'].value !== '') {
            alert('재요청은 1회만 가능합니다.');
            return;
        }
/*
        if (document.listForm.elements['srvcRsponsVO.changeDfflyCode'].value === '') {
            alert('만족도를 입력하세요.');
            return;
        }
*/        
        frm.action = "<c:url value="${registerFlag == 'create' ? '/srvcrspons/mngr/updateresr.do' : '/srvcrspons/mngr/updateresr.do'}"/>";
        frm.submit();
    }

    function fncSave() {
      frm = document.listForm;
/*
      if (document.listForm.elements['srvcRsponsVO.trgetSrvcCode'].value === "") {
        alert('대상서비스를 입력하세요.');
        return;
      }

      if (document.listForm.elements['srvcRsponsVO.srvcRsponsBasisCode'].value === "") {
        alert('SR근거를 입력하세요.');
        return;
      }

      if (document.listForm.elements['srvcRsponsVO.srvcRsponsClCode'].value === "") {
        alert('SR구분을 입력하세요.');
        return;
      }

      if (document.listForm.elements['srvcRsponsVO.processStdrCode'].value === '') {
        alert('SR처리기준시간을 입력하세요.');
        return;
      }

      if (document.listForm.elements['srvcRsponsVO.changeDfflyCode'].value === '') {
        alert('만족도를 입력하세요.');
        return;
      }

      if (document.listForm.elements['srvcRsponsVO.srvcRsponsClCode'].value === "S103") {
        if (!($("#progrmUpdtYn").is(":checked") || $("#stopInstlYn").is(":checked") || $("#noneStopInstlYn").is(":checked")
          || $("#instlYn").is(":checked") || $("#infraOpertYn").is(":checked"))) {
          alert("응용기능개선은 프로그램수정 또는 배포중 하나를 선택해야합니다.");
          return;
        }
      }

      if (!validateSrvcRsponsFormVO(frm)) {
        return;
      }
*/
if (document.listForm.elements['srvcRsponsVO.verifyDtDateDisplay'].value === '') {
    alert('검증일시를 입력하세요.');
    return;
 }
if (document.listForm.elements['srvcRsponsVO.verifyDtTimeDisplay'].value === '') {
    alert('검증일시를 입력하세요.');
    return;
 }


      $("#processStdrCode").prop("disabled", false);
      // 첨부파일 존재 유무 확인
      if (requstAtchmnflFileCnt > 0 && rsponsAtchmnflFileCnt > 0) {
        $("#requstAtchmnflSendBtn").trigger("click");
      }
      else if (requstAtchmnflFileCnt > 0) {
        $("#requstAtchmnflSendBtn").trigger("click");
      }
      else if (rsponsAtchmnflFileCnt > 0) {
        $("#rsponsAtchmnflSendBtn").trigger("click");
      }
      else {
        frm.action = "<c:url value="${registerFlag == 'create' ? '/srvcrspons/mngr/updatesrvr.do' : '/srvcrspons/mngr/updatesrvr.do'}"/>";
        frm.submit();
      }
    }

    function fncDelete() {
      confirm('삭제하시겠습니까?', 'SR요청 삭제', null, function(request) {
        console.log('request: ' + request);

        if (request) {
          document.listForm.action = "<c:url value='/srvcrspons/mngr/delete.do'/>";
          document.listForm.submit();
        }
      });
    }

    <%-- 요청자동일 복사 --%>
    function fncChkCopyRqester(obj) {
      if (obj.checked) {
        document.listForm.elements['srvcRsponsVO.rqester1stNm'].value = document.listForm.elements['srvcRsponsVO.rqesterNm'].value;
        document.listForm.elements['srvcRsponsVO.rqester1stPsitn'].value = document.listForm.elements['srvcRsponsVO.rqesterPsitn'].value;
        document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = document.listForm.elements['srvcRsponsVO.rqesterCttpc'].value;
        document.listForm.elements['srvcRsponsVO.rqester1stEmail'].value = document.listForm.elements['srvcRsponsVO.rqesterEmail'].value;
      }
      else {
        document.listForm.elements['srvcRsponsVO.rqester1stNm'].value = "";
        document.listForm.elements['srvcRsponsVO.rqester1stPsitn'].value = "";
        document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = "";
        document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = "";
      }
    }

    <%-- 요청자 변경시 처리 --%>
    function fncChangeRqesterNm(value) {
      console.log('요청자 value: ' + $.trim(value));
      $('#rqesterNmDropdown').dropdown('update');

      if ($.trim(value) !== "") {
        console.log('빈값일때');

        $.ajax({
          type: "POST",
          url: "<c:url value='/srvcrspons/mngr/retrieveRqesterNmListAjax.do'/>",
          dataType: "json",
          data: {
            rqesterNm: value,
          },
          async: false,
          success: function(request) {
            fncInitRqesterNmDropdown(request);
          },
          error: function(r) {
            alert("정보전송에 실패하였습니다.");
          },
        });
      }
    }

    function fncInitRqesterNmDropdown(request) {
      var rqesterNmDropdownHTML = "";

      console.log('request.returnMessage: ' + request.returnMessage);

      if (request.returnMessage === 'success') {
        resultList = request.resultList;

        for (var i = 0; i < resultList.length; i++) {
          rqesterNmDropdownHTML += "<a class=\"dropdown-item\" href=\"#\" id=\"rqesterNm" + (i + 1);
          rqesterNmDropdownHTML += "\" onclick=\"fncSelectRqesterNm('rqesterNm" + (i + 1) + "')\" data-name=\"";
          rqesterNmDropdownHTML += resultList[i].rqesterNm+"\" data-psitn=\"" + resultList[i].rqesterPsitn;
          rqesterNmDropdownHTML += "\" data-cttpc=\"" + resultList[i].rqesterCttpc + "\" data-email=\"" + resultList[i].rqesterEmail + "\">";
          rqesterNmDropdownHTML += resultList[i].rqesterNm + "(" + resultList[i].rqesterPsitn + ")</a>";
        }
      }
      else {
        rqesterNmDropdownHTML = '<a class="dropdown-item" href="#">일치하는데이터없음</a>';
      }

      console.log('rqesterNmDropdownHTML: ' + rqesterNmDropdownHTML);
      $('#rqesterNmDropdown').html(rqesterNmDropdownHTML);
    }

    function fncSelectRqesterNm(objId) {
      document.listForm.elements['srvcRsponsVO.rqesterNm'].value = $("#" + objId).data("name");
      document.listForm.elements['srvcRsponsVO.rqesterPsitn'].value = $("#" + objId).data("psitn");
      document.listForm.elements['srvcRsponsVO.rqesterCttpc'].value = $("#" + objId).data("cttpc");
      document.listForm.elements['srvcRsponsVO.rqesterEmail'].value = $("#" + objId).data("email");
    }

    <%-- 1차요청자 변경시 처리 --%>
    function fncChangeRqester1stNm(value) {
      console.log('1차 요청자 value: ' + $.trim(value));
      $('#rqester1stNmDropdown').dropdown('update');

      if ($.trim(value) !== "") {
        console.log('빈값일때');

        $.ajax({
          type: "POST",
          url: "<c:url value='/srvcrspons/mngr/retrieveRqester1stNmListAjax.do'/>",
          dataType: "json",
          data: {
            rqester1stNm: value,
          },
          async: false,
          success: function(request) {
            fncInitRqester1stNmDropdown(request);
          },
          error:function(r) {
            alert("정보전송에 실패하였습니다.");
          },
        });
      }
    }

    function fncInitRqester1stNmDropdown(request) {
      var rqester1stNmDropdownHTML = "";

      console.log('request.returnMessage: ' + request.returnMessage);

      if (request.returnMessage === "success") {
        resultList = request.resultList;

        for (var i = 0; i < resultList.length; i++) {
          rqester1stNmDropdownHTML += "<a class=\"dropdown-item\" href=\"#\" id=\"rqesterNm" + (i + 1);
          rqester1stNmDropdownHTML += "\" onclick=\"fncSelectRqester1stNm('rqesterNm" + (i + 1) + "')\" data-name=\"";
          rqester1stNmDropdownHTML += resultList[i].rqester1stNm + "\" data-psitn=\"" + resultList[i].rqester1stPsitn;
          rqester1stNmDropdownHTML += "\" data-cttpc=\"" + resultList[i].rqester1stCttpc + "\" data-email=\"" + resultList[i].rqester1stEmail + "\">";
          rqester1stNmDropdownHTML += resultList[i].rqester1stNm + "("+resultList[i].rqester1stPsitn + ")</a>";
        }
      }
      else {
        rqester1stNmDropdownHTML = '<a class="dropdown-item" href="#">일치하는데이터없음</a>';
      }

      console.log('rqester1stNmDropdownHTML: ' + rqester1stNmDropdownHTML);
      $('#rqester1stNmDropdown').html(rqester1stNmDropdownHTML);
    }

    function fncSelectRqester1stNm(objId) {
      document.listForm.elements['srvcRsponsVO.rqester1stNm'].value = $("#" + objId).data("name");
      document.listForm.elements['srvcRsponsVO.rqester1stPsitn'].value = $("#" + objId).data("psitn");
      document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = $("#" + objId).data("cttpc");
      document.listForm.elements['srvcRsponsVO.rqester1stEmail'].value = $("#" + objId).data("email");
    }

    <%-- 요청 상세내용 복사 --%>
    function fncChkCopySrvcRsponsCn(obj) {
      if (obj.checked) {
        document.listForm.elements['srvcRsponsVO.srvcProcessDtls'].value = document.listForm.elements['srvcRsponsVO.srvcRsponsCn'].value;
      }
      else {
        document.listForm.elements['srvcRsponsVO.srvcProcessDtls'].value = "";
      }
    }

    /* 글 목록 화면 function */
    function fncRetrieveList() {
      <c:if test="${empty srvcRsponsFormVO.returnListMode}">
      document.listForm.action = "<c:url value='/srvcrspons/mngr/retrieveSrVrList.do'/>";
      </c:if>
      <c:if test="${!empty srvcRsponsFormVO.returnListMode}">
      document.listForm.action = "<c:url value='/srvcrspons/mngr/retrieveSrVrList.do'/>";
      </c:if>
      document.listForm.submit();
    }

    function fncDownloadFile(atchmnflId, atchmnflSn) {
      document.downloadForm.action = "<c:url value='/atchmnfl/site/retrieve.do'/>";
      document.downloadForm.atchmnflId.value = atchmnflId;
      document.downloadForm.atchmnflSn.value = atchmnflSn;
      document.downloadForm.submit();
    }

    function fncDeleteFile(atchmnflId, atchmnflSn) {
      console.log('atchmnflId: ' + atchmnflId);
      console.log('atchmnflSn: ' + atchmnflSn);
      $('#atchmnflId').val(atchmnflId);
      $('#atchmnflSn').val(atchmnflSn);

      confirm('해당 파일이 즉시 삭제됩니다. 계속 진행하시겠습니까?', '파일 삭제', null, function(request) {
        console.log('request: ' + request);

        if (request) {
          $.ajax({
            type: "POST",
            url: "<c:url value='/atchmnfl/site/delete.do'/>",
            dataType: "json",
            data: {
              atchmnflId: $('#atchmnflId').val(),
              atchmnflSn: $('#atchmnflSn').val(),
            },
            async: false,
            success: function(request) {
              console.log('request.returnMessage: ' + request.returnMessage);

              if (request.returnMessage === "success") {
                console.log('delete: ' + $('#fileId_' + $('#atchmnflId').val() + '_'+$('#atchmnflSn').val()));
                console.log('delete: ' + '#fileId_' + $('#atchmnflId').val() + '_' + $('#atchmnflSn').val());
                $('#fileId_' + $('#atchmnflId').val() + '_'+$('#atchmnflSn').val()).remove();
              }
            },
            error: function(r) {
              console.dir(r);
              alert("정보전송에 실패하였습니다.");
            }
          });
        }
      });
    }
  </script>
</head>

<body>

<main id="content">
	<form name="downloadForm" action="<c:url value='/atchmnfl/site/retrieve.do'/>" target="">
		<input type="hidden" name="atchmnflId" id="atchmnflId"/>
		<input type="hidden" name="atchmnflSn" id="atchmnflSn"/>
	</form>

  <div class="page-header">
    <h3 class="page-title">
      <c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/>
    </h3>
    <div class="btnbox fr">
      <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary">
        <i class="ico-list"></i>목록
      </button>
<!--       
      <button type="button" onclick="fncDelete()" class="btn btn-dark">
        <i class="ico-delete"></i>삭제
      </button>
-->
	  <c:if test="${empty srvcRsponsFormVO.srvcRsponsVO.reRequestDt}">
		<c:if test="${empty srvcRsponsFormVO.srvcRsponsVO.reSrvcRsponsNo}">
			<button type="button" onclick="fncRequest()" class="btn btn-dark">
	      		<i class="ico-write"></i>재요청
			</button>
	    </c:if>
	  </c:if>
	  
      <button type="button" onclick="fncSave()" class="btn btn-primary">
        <i class="ico-save"></i>저장
      </button>
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
    <form:hidden path="srvcRsponsVO.rqesterId" />
    <form:hidden path="srvcRsponsVO.chargerUserNm" />
    <form:hidden path="srvcRsponsVO.chargerId"/>
    <form:hidden path="srvcRsponsVO.srvcRsponsNo" />
    <form:hidden path="srvcRsponsVO.saveToken" />
    
    <form:hidden path="srvcRsponsVO.reRequestDtDateDisplay" />
    
<!-- 
    <form:hidden path="srvcRsponsVO.processDtDateDisplay" />
    <form:hidden path="srvcRsponsVO.processDtTimeDisplay" />
-->
    <form:hidden path="returnListMode" />

    <form:hidden path="srvcRsponsVO.rspons1stDtDateDisplay" />
    <form:hidden path="srvcRsponsVO.rspons1stDtTimeDisplay" />
    <form:hidden path="srvcRsponsVO.srvcRsponsBasisCode" />
    <form:hidden path="srvcRsponsVO.rqesterNm" />
    <form:hidden path="srvcRsponsVO.rqesterPsitn" />
    <form:hidden path="srvcRsponsVO.rqesterCttpc" />
    <form:hidden path="srvcRsponsVO.rqesterEmail" />
    <form:hidden path="srvcRsponsVO.rqester1stNm" />
    <form:hidden path="srvcRsponsVO.rqester1stCttpc" />
    <form:hidden path="srvcRsponsVO.rqester1stPsitn" />
    <form:hidden path="srvcRsponsVO.rqester1stEmail" />
    <form:hidden path="srvcRsponsVO.trgetSrvcCode" />
    <form:hidden path="srvcRsponsVO.srvcRsponsSj" />
    <form:hidden path="srvcRsponsVO.srvcRsponsCn" />



    <div class="row srarea equalheight viewpage">
      <div class="col-left">
        <div class="card">
          <div class="card-header">
            <h4 class="card-title">SR 정보</h4>
            <dl class="sr-info">
              <dt></dt>
              <dd>${srvcRsponsFormVO.srvcRsponsVO.srvcRsponsNo }</dd>
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
                <div class="control-group mt5">
                  <input type="checkbox" name="chkCopyRqester" id="chk1" class="checkbox" onclick="fncChkCopyRqester(this)"/>
                  <label for="chk1">요청자와 동일</label>
                </div>
              </div>

              <div class="card-body">
                <div class="detailbox type1">
                  <dl>
                    <dt>성명</dt>
                    <dd>
                      <form:input path="srvcRsponsVO.rqester1stNm" id="rqester1stNm" data-toggle="dropdown" onkeyup="fncChangeRqester1stNm(this.value)"  class="control" title="성명"/>
                      <div class="dropdown-menu" id="rqester1stNmDropdown">
                        <a class="dropdown-item" href="#">일치하는데이터없음</a>
                      </div>
                    </dd>
                  </dl>
                  <dl>
                    <dt>소속</dt>
                    <dd><form:input path="srvcRsponsVO.rqester1stPsitn" class="control" title="소속"/></dd>
                  </dl>
                  <dl>
                    <dt>연락처</dt>
                    <dd><form:input path="srvcRsponsVO.rqester1stCttpc" class="control" title="연락처"/></dd>
                  </dl>
                  <dl>
                    <dt>이메일</dt>
                    <dd><form:input path="srvcRsponsVO.rqester1stEmail" class="control" title="이메일"/></dd>
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
                <dd><strong>${srvcRsponsFormVO.srvcRsponsVO.chargerUserNm }</strong></dd>
              </dl>
              <dl>
                  <dt>검증담당</dt>
                  <dd><strong>${recvuser}</strong></dd>
              </dl>
              <dl>
                  <dt>완료담당</dt>
                  <dd></dd>
              </dl>

            </div>

            <hr>

            <div class="detailbox type1">
              <dl>
                <dt>접수일시</dt>
                <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.rspons1stDtDateDisplay}"/>  <c:out value="${srvcRsponsFormVO.srvcRsponsVO.rspons1stDtTimeDisplay}"/></dd>
              </dl>
              <dl>
                <dt>처리일시</dt>
                <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.processDtDateDisplay}"/>  <c:out value="${srvcRsponsFormVO.srvcRsponsVO.processDtTimeDisplay}"/></dd>
              </dl>
              <dl>
	              <dt>검증일시</dt>
	                <dd>
	                  <div class="input-group">
	                    <form:input path="srvcRsponsVO.verifyDtDateDisplay" id="verifyDtDateDisplay" class="control inpdate" title="검증일" placeholder="YYYY-MM-DD" readonly="true"/>
	                    <form:input path="srvcRsponsVO.verifyDtTimeDisplay" id="verifyDtTimeDisplay" class="control inptime" title="검증시간" placeholder="HH:MM" readonly="true"/>
	                  </div>
	                </dd>
	          </dl>
              <c:if test="${srvcRsponsFormVO.srvcRsponsVO.reRequestDtDateDisplay != null }">
	              <dl>
	                <dt>재요청일시</dt>
                      <dd><c:out value="${srvcRsponsFormVO.srvcRsponsVO.reRequestDtDateDisplay}"/>  <c:out value="${srvcRsponsFormVO.srvcRsponsVO.reRequestDtTimeDisplay}"/></dd>
	              </dl>
              </c:if>

              <dl>
                <dt>완료일시</dt>
                <dd></dd>
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
                  <dd><p style="text-align:center;"><c:out value="${srvcRsponsFormVO.srvcRsponsVO.processTerm}" /></p></dd>
              </dl>
              <dl>
                  <dt><p style="text-align:center;">시간구분</p></dt>
                  <dd><p style="text-align:center;"><c:out value="${srvcRsponsFormVO.srvcRsponsVO.processStdrCodeNm}"/></p></dd>
              </dl>

<!-- 
              <dl>
                <dt>SR 구분</dt>
                <dd>
                  <form:select id="srvcRsponsClCode" path="srvcRsponsVO.srvcRsponsClCode" cssClass="control" title="SR 구분">
                    <form:option value="">선택하세요</form:option>
                    <form:options items="${srvcRsponsClCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
                  </form:select>
                </dd>
              </dl>
              <dl>
                <dt>SR처리기준시간</dt>
                <dd>
                  <form:select id="processStdrCode" path="srvcRsponsVO.processStdrCode" cssClass="control" title="SR처리기준시간">
                    <form:option value="">선택하세요</form:option>
                    <form:options items="${processStdrCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
                  </form:select>
                </dd>
              </dl>
              <dl class="col-4">
                <dt>만족도</dt>
                <dd>
                  <form:select path="srvcRsponsVO.changeDfflyCode" cssClass="control">
                    <form:option value="">선택하세요</form:option>
                    <form:options items="${changeDfflyCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
                  </form:select>
                </dd>
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
                        <input type="checkbox" name="srvcRsponsVO.dataUpdtYn" id="dataUpdtYn" disabled class="checkbox" value="Y"/>
                      </c:if>
                      <c:if test="${srvcRsponsFormVO.srvcRsponsVO.dataUpdtYn == 'Y' }">
                        <input type="checkbox" name="srvcRsponsVO.dataUpdtYn" id="dataUpdtYn" disabled class="checkbox" value="Y" checked/>
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
                <dt>검증내역</dt>
                <dd><form:textarea path="srvcRsponsVO.srvcVerifyDtls" class="control" title="검증내역" rows="2"></form:textarea></dd>
              </dl>
            </div>

            <div class="detailbox type2">
              <dl>
                <dt>응답첨부파일</dt>
                <dd class="d-block">
                  <div class="file-box">
                    <!-- The fileinput-button span is used to style the file input field as button -->
                    <span class="btn btn-secondary fileinput-button">
                      <i class="ico-search"></i>
                      <!-- The file input field used as target for the file upload widget -->
                      <input id="rsponsAtchmnfl" type="file" name="multipartFile" multiple>
                    </span>
                  </div>
                  <button type="button" class="btn btn-sm btn-primary" id="rsponsAtchmnflSendBtn">파일전송</button>
                  <ul id="rsponsAtchmnflFileListDiv" class="file-list-group">
                    <c:forEach var="atchmnflVO" items="${rsponsAtchmnflList}">
                      <li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
                        <a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')">
                          <span aria-hidden="true">&times;</span>
                        </a>
												<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')">
                          <i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)
                        </a>
											</li>
										</c:forEach>
									</ul>
                </dd>
              </dl>
            </div>

            <hr>
<!-- 
            <div class="detailbox type2">
              <dl>
                <dt>기능개선</dt>
                <dd><form:input readonly="true" path="srvcRsponsVO.fnctImprvmNo" id="fnctImprvmNo" class="control" title="기능개선"/></dd>
              </dl>
              <dl>
                <dt>배포확인서</dt>
                <dd><form:input readonly="true" path="srvcRsponsVO.wdtbCnfirmNo" id="wdtbCnfirmNo" class="control" title="배포확인서"/></dd>
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
    <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary">
      <i class="ico-list"></i>목록
    </button>
<!--     
    <button type="button" onclick="fncDelete()" class="btn btn-dark">
      <i class="ico-delete"></i>삭제
    </button>
-->    

    <c:if test="${empty srvcRsponsFormVO.srvcRsponsVO.reRequestDt}">
	  <c:if test="${empty srvcRsponsFormVO.srvcRsponsVO.reSrvcRsponsNo}">
		<button type="button" onclick="fncRequest()" class="btn btn-dark">
      		<i class="ico-write"></i>재요청
		</button>
      </c:if>
    </c:if>

    <button type="button" onclick="fncSave()" class="btn btn-primary">
      <i class="ico-save"></i>저장
    </button>
  </div>
</main>
    
</body>
</html>