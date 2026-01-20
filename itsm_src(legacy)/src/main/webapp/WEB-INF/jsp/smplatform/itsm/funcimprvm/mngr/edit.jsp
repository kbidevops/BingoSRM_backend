<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html>
<html lang="ko">

<head>
  <c:set var="registerFlag" value="${empty funcImprvmFormVO.funcImprvmVO.fnctImprvmNo ? 'create' : 'modify'}"/>
  <title>
    <c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/>
  </title>

  <!--For Commons Validator Client Side-->
  <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
  <validator:javascript formName="funcImprvmFormVO" staticJavascript="false" xhtml="true" cdata="false"/>
  <script type="text/javaScript" language="javascript" defer="defer">
    var fiAtchmnflFileCnt = 0;
    var asisAtchmnflFileCnt = 0;
    var tobeAtchmnflFileCnt = 0;

    $(document).ready(function() {
      var frm = document.listForm;
      $("#applyPlanDtDisplay").mask("9999-99-99");
      $("#applyRDtDisplay").mask("9999-99-99");
      $("#srvcRsponsNo").mask("SR-9999-999");
      $('#asisAtchmnflSendBtn').hide();
      $('#tobeAtchmnflSendBtn').hide();
      $('#fiAtchmnflSendBtn').hide();

      // 라디오버튼 선택에 따른 텍스트박스 활성화
      if ($("input[name='funcImprvmVO.conectSysYn']").val() === "N") {
        $("input[name='funcImprvmVO.conectSys']").attr("disabled","disabled");
      }

      if ($("input[name='funcImprvmVO.cnfrmYn']").val() === "Y") {
        $("input[name='funcImprvmVO.noCnfrmResn']").attr("disabled","disabled");
      }

      $("input[name='funcImprvmVO.conectSysYn']").on("click", function() {
        if ($(this).val() === "Y") {
          $("input[name='funcImprvmVO.conectSys']").removeAttr("disabled");
        }
        else {
          $("input[name='funcImprvmVO.conectSys']").attr("disabled","disabled");
        }
      });

      $("input[name='cnfrmYn']").on("click", function() {
        if ($(this).val() === "Y") {
          $("input[name='funcImprvmVO.noCnfrmResn']").attr("disabled","disabled");
        }
        else {
          $("input[name='funcImprvmVO.noCnfrmResn']").removeAttr("disabled");
        }
      });

      <c:if test="${registerFlag == 'create'}">
      // 개선 계획 양식
      var fiPlanTemplate = ""
        + "■ 전체일정 : "
        + "<c:out value='${funcImprvmFormVO.funcImprvmVO.applyPlanDtDisplay}'/>. - "
        + "<c:out value='${funcImprvmFormVO.funcImprvmVO.applyRDtDisplay}'/>.\n"
        + "■ 적용Tool\n"
        + "	. 개발 툴 : eclipse(java)";

      $("textarea[name='funcImprvmVO.fiPlan']").val(fiPlanTemplate);

      // 백업 방안
      var backupPlan = "svn을 통한 자동 백업 실시";
      $("input[name='funcImprvmVO.backupPlan']").val(backupPlan);
      </c:if>

      var url = '<c:url value='/itsm/atchmnfl/site/create.do'/>';

      $('#fiAtachmnfl').fileupload({
        url: url,
        dataType: 'json',
        formData: {
          atchmnflId: $("#fiAtchmnflId").val()
        },
        sequentialUploads: true,
        add: function(e, data) {
          var valid = true;
          var re = /^.+\.((doc)|(txt)|(xls)|(xlsx)|(docx)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(gif)|(png)|(bmp))$/i;

          $.each(data.files, function(index, file) {
            console.log('Added file: ' + file.name);
            console.log('Added file: ' + file.type);
            console.log('Added file: ' + file.size);
            console.log('Added file: ' + filesize(file.size));
            // console.log('file size: ' + data.getNumberOfFiles());

            fiAtchmnflFileCnt++;

            if (!re.test(file.name)) {
              valid = false;
              alert(file.name + ' <br/>허용되지 않은 첨부파일은 제외됩니다.');
              fiAtchmnflFileCnt--;
            }
            // 5M 제한
            else if (file.size > 5000000) {
              console.log('5M이하의 파일만 허용 합니다.');
              alert(file.name + ' <br/>5M이상의 첨부파일은 제외됩니다.');
              fiAtchmnflFileCnt--;
              valid = false;
            }
            // 동시 업로드 5개 제한
            else if (fiAtchmnflFileCnt > 5) {
              console.log('5개의 파일만 허용 합니다.' + fiAtchmnflFileCnt);
              alert(file.name + ' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
              fiAtchmnflFileCnt--;
              valid = false;
            }
            else {
              console.log('정상등록 ' + fiAtchmnflFileCnt);

              var abortBtn = $('<a/>')
                .attr('href', 'javascript:void(0)')
                .attr('class', 'close')
                .attr('aria-label', 'Close')
                .append('<span aria-hidden="true">&times;</span>')
                .click(function() {
                  data.abort();
                  data.context.remove();
                  data = null;

                  if (fiAtchmnflFileCnt > 0) {
                    fiAtchmnflFileCnt--;
                    console.log('fiAtchmnflFileCnt: ' + fiAtchmnflFileCnt);
                  }
                });

              data.context = $('<div/>').appendTo($('#fiAtachmnflListDiv'));
              data.context.attr('class', 'form-row');
              data.context.append(abortBtn).append(file.name + '(' + filesize(file.size) + ')');
            }
          });

          $("#fiAtchmnflSendBtn").click(function () {
            // validate file...
            if (valid && data !== null) {
              data.submit();
              // 해당 폼 전송 추가 하면 저장 버튼 완료
            }
          });
        },
        done: function(e, data) {
          console.log('done: ' + data.errorThrown);
          console.log('done: ' + data.textStatus);
          console.log('done: ' + data.jqXHR);
        },
        progressall: function(e, data) {
          if (asisAtchmnflFileCnt > 0) {
            $("#asisAtchmnflSendBtn").trigger("click");
          }
          else {
            frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/funcimprvm/mngr/create.do' : '/itsm/funcimprvm/mngr/update.do'}"/>";
            frm.submit();
          }
        },
        progressServerRate: 0.3
      });

      $('#asisAtchmnfl').fileupload({
        url: url,
        dataType: 'json',
        formData: {
          atchmnflId: $("#asisAtchmnflId").val()
        },
        sequentialUploads: true,
        add: function(e, data) {
          var valid = true;
          var re = /^.+\.((jpg)|(gif)|(png)|(jpeg))$/i;

          $.each(data.files, function(index, file) {
            console.log('Added file: ' + file.name);
            console.log('Added file: ' + file.type);
            console.log('Added file: ' + file.size);
            console.log('Added file: ' + filesize(file.size));
            // console.log('file size: ' + data.getNumberOfFiles());

            asisAtchmnflFileCnt++;

            if (!re.test(file.name)) {
              valid = false;
              alert(file.name + ' <br/>사진파일만 첨부할 수 있습니다.');
              asisAtchmnflFileCnt--;
            }
            // 5M 제한
            else if (file.size > 5000000) {
              console.log('5M이하의 파일만 허용 합니다.');
              alert(file.name + ' <br/>5M이상의 첨부파일은 제외됩니다.');
              asisAtchmnflFileCnt--;
              valid = false;
            }
            // 동시 업로드 5개 제한
            else if (asisAtchmnflFileCnt > 5) {
              console.log('5개의 파일만 허용 합니다.' + asisAtchmnflFileCnt);
              alert(file.name + ' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
              asisAtchmnflFileCnt--;
              valid = false;
            }
            else {
              console.log('정상등록 ' + asisAtchmnflFileCnt);
              var abortBtn = $( '<a/>' )
                .attr('href', 'javascript:void(0)')
                .attr('class', 'close')
                .attr('aria-label', 'Close')
                .append('<span aria-hidden="true">&times;</span>')
                .click(function() {
                  data.abort();
                  data.context.remove();
                  data = null;

                  if (asisAtchmnflFileCnt > 0) {
                    asisAtchmnflFileCnt--;
                    console.log('asisAtchmnflFileCnt: ' + asisAtchmnflFileCnt);
                  }
                });

              data.context = $('<div/>').appendTo($('#asisAtchmnflListDiv'));
              data.context.attr('class', 'form-row');
              data.context.append(abortBtn).append(file.name + '(' + filesize(file.size) + ')');
            }
          });

          $("#asisAtchmnflSendBtn").click(function() {
            // validate file...
            if (valid && data !== null) {
              data.submit();
              // 해당 폼 전송 추가 하면 저장 버튼 완료
            }
          });
        },
        done: function(e, data) {
          console.log('done: '+data.errorThrown);
          console.log('done: '+data.textStatus);
          console.log('done: '+data.jqXHR);
        },
        progressall: function(e, data) {
          if (tobeAtchmnflFileCnt > 0) {
            $("#tobeAtchmnflSendBtn").trigger("click");
          }
          else {
            frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/funcimprvm/mngr/create.do' : '/itsm/funcimprvm/mngr/update.do'}"/>";
            frm.submit();
          }
        },
        progressServerRate: 0.3
      });

      $('#tobeAtchmnfl').fileupload({
        url: url,
        dataType: 'json',
        formData: {
          atchmnflId: $("#tobeAtchmnflId").val()
        },
        sequentialUploads: true,
        add: function(e, data) {
          var valid = true;
          var re = /^.+\.((jpg)|(gif)|(png)|(jpeg))$/i;

          $.each(data.files, function (index, file) {
            console.log('Added file: ' + file.name);
            console.log('Added file: ' + file.type);
            console.log('Added file: ' + file.size);
            console.log('Added file: ' + filesize(file.size));
            // console.log('file size: ' + data.getNumberOfFiles());

            tobeAtchmnflFileCnt++;

            if (!re.test(file.name)) {
              valid = false;
              alert(file.name+' <br/>사진파일만 첨부할 수 있습니다.');
              tobeAtchmnflFileCnt--;
            }
            // 5M 제한
            else if (file.size > 5000000) {
              console.log('5M이하의 파일만 허용 합니다.');
              alert(file.name + ' <br/>5M이상의 첨부파일은 제외됩니다.');
              tobeAtchmnflFileCnt--;
              valid = false;
            }
            // 동시 업로드 5개 제한
            else if (tobeAtchmnflFileCnt > 5) {
              console.log('5개의 파일만 허용 합니다.' + tobeAtchmnflFileCnt);
              alert(file.name+' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
              tobeAtchmnflFileCnt--;
              valid = false;
            }
            else {
              console.log('정상등록 ' + tobeAtchmnflFileCnt);
              var abortBtn = $('<a/>')
                .attr('href', 'javascript:void(0)')
                .attr('class', 'close')
                .attr('aria-label', 'Close')
                .append('<span aria-hidden="true">&times;</span>')
                .click(function() {
                  data.abort();
                  data.context.remove();
                  data = null;

                  if (tobeAtchmnflFileCnt > 0) {
                    tobeAtchmnflFileCnt--;
                    console.log('tobeAtchmnflFileCnt: ' + tobeAtchmnflFileCnt);
                  }
                });

              data.context = $('<div/>').appendTo($('#tobeAtchmnflListDiv'));
              data.context.attr( 'class', 'form-row' );
              data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
            }
          });

          $("#tobeAtchmnflSendBtn").click(function() {
            // validate file...
            if (valid && data !== null) {
              data.submit();
              // 해당 폼 전송 추가 하면 저장 버튼 완료
            }
          });
        },
        done: function(e, data) {
          console.log('done: ' + data.errorThrown);
          console.log('done: ' + data.textStatus);
          console.log('done: ' + data.jqXHR);
        },
        progressall: function(e, data) {
          frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/funcimprvm/mngr/create.do' : '/itsm/funcimprvm/mngr/update.do'}"/>";
          frm.submit();
        },
        progressServerRate: 0.3
      });
    });

    function fncChangeSrNo(value) {
      value = value.substr(0, value.indexOf("_"));
      console.log('SRNOvalue: ' + $.trim(value));
      $('#srNoDropdown').dropdown('update');

      if ($.trim(value) !== '') {
        console.log('빈 값일때');

        $.ajax({
          type: 'POST',
          url: "${pageContext.request.contextPath}/itsm/funcimprvm/mngr/retrieveSrNoListAjax.do",
          dataType: "json",
          data: {
            "srvcRsponsNo": value
          },
          async: false,
          success: function(request) {
            fncInitsrvcRsponsNoDropdown(request)
          },
          error: function(r) {
            alert("정보전송에 실패하였습니다.");
          }
        });
      }
    }

    function fncSave() {
      var frm = document.listForm;

      <c:if test="${registerFlag == create}">
      if (frm.elements['funcImprvmVO.srvcRsponsNo'].value === '') {
        alert('SR번호를 입력하세요.');
        return;
      }
      </c:if>

      if (frm.elements['funcImprvmVO.fiCl'].value === '') {
        alert('개선분류를 선택하세요.');
        return;
      }

      if (frm.elements['funcImprvmVO.fiPlan'].value === '') {
        alert('개선계획을 입력하세요.');
        return;
      }

      if (frm.elements['funcImprvmVO.navigation'].value === '') {
        alert('네비게이션을 입력하세요.');
        return;
      }

      if (frm.elements['funcImprvmVO.fiRquire'].value === '') {
        alert('개선 요구사항을 입력하세요.');
        return;
      }

      if (frm.elements['funcImprvmVO.fiCn'].value === '') {
        alert('개선사항을 입력하세요.');
        return;
      }

      if (!validateFuncImprvmFormVO(frm)) {
        return;
      }

      var fiCl = $("input[name='funcImprvmVO.fiCl']:checked").val();
      if ($("#asisAtchmnflListDiv li").length < 1 && asisAtchmnflFileCnt < 1 && fiCl !== "F002") {
        alert('ASIS 항목은 필수입니다.');
        return;
      }
      else if ($("#tobeAtchmnflListDiv li").length < 1 && tobeAtchmnflFileCnt < 1) {
        alert('TOBE 항목은 필수입니다.');
        return;
      }

      // 첨부파일 존재 유무 확인
      if (fiAtchmnflFileCnt > 0 && asisAtchmnflFileCnt > 0 && tobeAtchmnflFileCnt > 0) {
        $("#fiAtchmnflSendBtn").trigger("click");
      }
      else if (fiAtchmnflFileCnt > 0) {
        $("#fiAtchmnflSendBtn").trigger("click");
      }
      else if (asisAtchmnflFileCnt > 0) {
        $("#asisAtchmnflSendBtn").trigger("click");
      }
      else if (tobeAtchmnflFileCnt > 0) {
        $("#tobeAtchmnflSendBtn").trigger("click");
      }
      else {
        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/funcimprvm/mngr/create.do' : '/itsm/funcimprvm/mngr/update.do'}"/>";
        frm.submit();
      }
    }

    function fncDelete(usr) {
      confirm('삭제하시겠습니까?', '기능개선 삭제', null, function(request) {
        console.log('request: ' + request);

        if (request) {
          document.listForm.action = "<c:url value='/itsm/funcimprvm/mngr/delete.do'/>";
          document.listForm.submit();
        }
      });
    }

    function fncRetrieveList() {
      document.listForm.action = "<c:url value='/itsm/funcimprvm/mngr/retrievePagingList.do'/>";
      document.listForm.submit();
    }

    function fncDownloadFile(atchmnflId, atchmnflSn) {
      document.downloadForm.action = "<c:url value='/itsm/atchmnfl/site/retrieve.do'/>";
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
        console.log('request: '+request);

        if (request) {
          $.ajax({
            type: 'POST',
            url: "<c:url value='/itsm/atchmnfl/site/delete.do'/>",
            dataType: "json",
            data: {
              "atchmnflId": $('#atchmnflId').val(),
              "atchmnflSn": $('#atchmnflSn').val()
            },
            async: false,
            success: function(request) {
              console.log('request.returnMessage: ' + request.returnMessage);

              if (request.returnMessage === 'success') {
                console.log('delete: ' + $('#fileId_'+$('#atchmnflId').val() + '_'+$('#atchmnflSn').val()));
                console.log('delete: ' + '#fileId_' + $('#atchmnflId').val() + '_'+$('#atchmnflSn').val());

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

    function fncFnctImprvmDownload(fiCategory) {
      if (fiCategory === "plan") {
        var ozrFileNm = "/knfc/funcImprvmPlan.ozr";
        var odiFileNm = "/knfc/funcImprvmPlan.odi";
      }
      else if (fiCategory === "result") {
        var ozrFileNm = "/knfc/funcImprvmResult.ozr";
        var odiFileNm = "/knfc/funcImprvmResult.odi";
      }

      procPrintList(ozrFileNm, odiFileNm, fiCategory);
    }

    /**
     * 문자열에서 모든 교체할 문자열을 대체 문자열로 치환한다.
     * @param pattnStr - 찾을 문자열
     * @param chngStr - 대체 문자열
     * @return 치환된 문자열
     */
    String.prototype.replaceAll = function(pattnStr, chngStr) {
      var retsult = "";
      var trimStr = this;//.replace(/(^\s*)|(\s*$)/g, "");

      if (trimStr && pattnStr !== chngStr) {
        retsult = trimStr;

        while (retsult.indexOf(pattnStr) > -1) {
          retsult = retsult.replace(pattnStr, chngStr);
        }
      }

      return retsult;
    };

    var procPrintList = function(ozrFileNm, odiFileNm, fiCategory) {
      var funcImprvmNo = '<c:out value="${funcImprvmFormVO.funcImprvmVO.fnctImprvmNo}"/>';
      var itsmDomain = location.origin + "/";
      var flName = funcImprvmNo + "-";

      if (fiCategory === "plan") {
        flName = flName + "기능개선계획서";
      }
      else if (fiCategory === "result") {
        flName = flName + "기능개선결과서";
      }

      var jsonData = "${jsonData }";
      jsonData = jsonData.replaceAll("'", '"');

      var ozparams = {
        ozr: ozrFileNm,
        odi: odiFileNm,
        flName: flName,
        pcount: 3, // pcount는 jsonData 가 있으므로 밑의 params 갯수보다 1+해서 적어준다.
        params: [
          {
            id: "funcImprvmNo",
            value: funcImprvmNo,
          },
          {
            id: "itsmDomain",
            value: itsmDomain,
          },
        ],
      };

      var popUrl = "<c:url value='/itsm/rep/master/mngr/OzReportViewer.do'/>";
      var $formOzReport = $("<form action='" + popUrl + "' method='post', target='_blank' id='formOzReport' name='formOzReport'/>");
      var html = $("<input type='hidden' name='ozparams' value='" + JSON.stringify(ozparams) + "'/>"
        + "<input type='hidden' name='jsonData' value='" + jsonData + "'/>");

      $formOzReport.append(html);
      $('body').append($formOzReport);
      $formOzReport.submit();
    };
  </script>
</head>

<body>
  <main id="content">
    <form name="downloadForm" action="<c:url value='/itsm/atchmnfl/site/retrieve.do'/>" target="">
      <input type="hidden" name="atchmnflId" id="atchmnflId">
      <input type="hidden" name="atchmnflSn" id="atchmnflSn">
    </form>
    <div class="page-header">
      <h3 class="page-title">기능개선 처리 <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></h3>
      <div class="btnbox fr">
        <c:if test="${registerFlag != 'create' }">
          <button type="button" onclick="fncFnctImprvmDownload('plan')" class="btn btn-primary">
            <i class="ico-download"></i>계획서내려받기
          </button>
          <button type="button" onclick="fncFnctImprvmDownload('result')" class="btn btn-primary">
            <i class="ico-download"></i>결과서내려받기
          </button>
        </c:if>

        <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary">
          <i class="ico-list"></i>목록
        </button>

        <c:if test="${registerFlag != 'create' && LOGIN_USER_VO.userTyCode == 'R001' }">
          <button type="button" onclick="fncDelete()" class="btn btn-dark">
            <i class="ico-delete"></i>삭제
          </button>
        </c:if>

        <button type="button" onclick="fncSave()" class="btn btn-primary">
          <i class="ico-save"></i>저장
        </button>
      </div>
    </div>

    <form:form commandName="funcImprvmFormVO" id="listForm" name="listForm" method="post" enctype="multipart/form-data">
      <form:hidden path="funcImprvmVO.fnctImprvmNo"/>
      <form:hidden path="funcImprvmVO.fiAtchmnflId" id="fiAtchmnflId"/>
      <form:hidden path="funcImprvmVO.asisAtchmnflId" id="asisAtchmnflId"/>
      <form:hidden path="funcImprvmVO.tobeAtchmnflId" id="tobeAtchmnflId"/>
      <form:hidden path="funcImprvmVO.chargerId"/>
      <form:hidden path="funcImprvmVO.saveToken"/>

      <div class="row equalheight">
        <div class="col-left">
          <div class="card">
            <div class="card-header">
              <h4 class="card-title">기능개선번호</h4>
              <dl class="sr-info">
                <dd>
                  <c:if test="${registerFlag == 'create' }">
                    자동생성
                  </c:if>
                  <c:if test="${registerFlag != 'create' }">
                    <c:out value="${funcImprvmFormVO.funcImprvmVO.fnctImprvmNo}"/>
                  </c:if>
                </dd>
                <dt style="margin-right:20px;margin-left:20px;">
                  대상서비스
                </dt>
                <dd id="trgetSrvcCodeNm">
                  <c:if test="${registerFlag == 'create' }">
                    <c:out value="${srvcRsponsVO.trgetSrvcCodeNm}"/>
                  </c:if>
                  <c:out value="${funcImprvmFormVO.funcImprvmVO.trgetSrvcCodeNm}"/>
                </dd>
              </dl>
            </div>
          </div>

          <div class="row">
            <div class="col">
              <!-- 요청자정보 -->
              <div class="card">
                <div class="card-header">
                  <h5 class="card-title">
                    개선요청 사항 (상세기술)
                  </h5>
                </div>
                <div class="card-body">
                  <div class="detailbox type1">
                    <dl>
                      <dt>개선제목</dt>
                      <c:if test="${registerFlag == 'create'}">
                        <dd>
                          <c:out value="${srvcRsponsVO.srvcRsponsSj }"/>
                        </dd>
                      </c:if>
                      <c:if test="${registerFlag != 'create'}">
                        <dd>
                          <form:input path="funcImprvmVO.srvcRsponsSj"  class="control" title="개선제목" disabled="true"/>
                        </dd>
                      </c:if>
                    </dl>

                    <dl>
                      <dt>연계유무</dt>
                      <dd>
                        <c:if test="${registerFlag == 'create' ||  funcImprvmFormVO.funcImprvmVO.conectSysYn != 'Y'}">
                          <input type="radio" id="conectSysN" value="N" name="funcImprvmVO.conectSysYn" class="checkbox" checked="checked">
                          <label for="conectSysN">&nbsp;타 시스템 연계 없음</label>
                          <input type="radio" id="conectSysY" value="Y" name="funcImprvmVO.conectSysYn" class="checkbox">
                          <label for="conectSysY">&nbsp;타 시스템과 연계 필요</label>
                        </c:if>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.conectSysYn == 'Y' }">
                          <input type="radio" id="conectSysN" value="N" name="funcImprvmVO.conectSysYn" class="checkbox">
                          <label for="conectSysN">&nbsp;타 시스템 연계 없음</label>
                          <input type="radio" id="conectSysY" value="Y" name="funcImprvmVO.conectSysYn" class="checkbox" checked="checked">
                          <label for="conectSysY">&nbsp;타 시스템과 연계 필요</label>
                        </c:if>

                      </dd>
                    </dl>

                    <dl>
                      <dt>연계시스템</dt>
                      <dd>
                        <form:input path="funcImprvmVO.conectSys" id="conectSys" class="control" title="연계시스템"/>
                      </dd>
                    </dl>

                    <dl>
                      <dt>개선분류</dt>
                      <dd>
                        <c:forEach items="${fiClList }" var="fiCl" >
                          <c:if test="${fiCl.cmmnCode ==  funcImprvmFormVO.funcImprvmVO.fiCl}">
                            <input type="radio" id="${fiCl.cmmnCode }" value="${fiCl.cmmnCode }" name="funcImprvmVO.fiCl" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${fiCl.cmmnCode !=  funcImprvmFormVO.funcImprvmVO.fiCl}">
                            <input type="radio" id="${fiCl.cmmnCode }" value="${fiCl.cmmnCode }" name="funcImprvmVO.fiCl" class="checkbox">
                          </c:if>

                          <label for="${fiCl.cmmnCode }">&nbsp;${fiCl.cmmnCodeNm }</label>
                        </c:forEach>
                      </dd>
                    </dl>

                    <dl>
                      <dt>상세내역</dt>
                      <c:if test="${registerFlag == 'create'}">
                        <dd>
                          <textarea class="control" title="상세내역" disabled="true" rows="5"><c:out value="${srvcRsponsVO.srvcRsponsCn }" escapeXml="false"/></textarea>
                        </dd>
                      </c:if>

                      <c:if test="${registerFlag != 'create'}">
                        <dd>
                          <form:textarea path="funcImprvmVO.srvcRsponsCn" class="control" title="상세내역" disabled="true"></form:textarea>
                        </dd>
                      </c:if>
                    </dl>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-12">
              <div class="card">
                <div class="card-header">
                  <h5 class="card-title">개선계획 및 백업, 원상복구, 기타사항</h5>
                </div>
                <div class="card-body">
                  <div class="detailbox type1">
                    <dl>
                      <dt>개선계획</dt>
                      <dd><form:textarea path="funcImprvmVO.fiPlan"  class="control" title="개선계획" rows="5"></form:textarea></dd>
                    </dl>
                    <dl>
                      <dt>산출물</dt>
                      <dd style="margin-left:11px;">
                        <div class="row">
                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.rquireDfnYn}">
                            <input type="checkbox" id="rquireDfnYn" value="Y" name="funcImprvmVO.rquireDfnYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.rquireDfnYn}">
                            <input type="checkbox" id="rquireDfnYn" value="Y" name="funcImprvmVO.rquireDfnYn" class="checkbox">
                          </c:if>

                          <label for="rquireDfnYn">&nbsp;요구사항정의서</label>

                          <c:if test="${funcImprvmFormVO.funcImprvmVO.rquireSpcYn ==  'Y'}">
                            <input type="checkbox" id="rquireSpcYn" value="Y" name="funcImprvmVO.rquireSpcYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${funcImprvmFormVO.funcImprvmVO.rquireSpcYn !=  'Y'}">
                            <input type="checkbox" id="rquireSpcYn" value="Y" name="funcImprvmVO.rquireSpcYn" class="checkbox">
                          </c:if>

                          <label for="rquireSpcYn">&nbsp;요구사항명세서</label>

                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.rquireTrcYn}">
                            <input type="checkbox" id="rquireTrcYn" value="Y" name="funcImprvmVO.rquireTrcYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.rquireTrcYn}">
                            <input type="checkbox" id="rquireTrcYn" value="Y" name="funcImprvmVO.rquireTrcYn" class="checkbox">
                          </c:if>

                          <label for="rquireTrcYn">&nbsp;요구사항추적표</label>
                        </div>

                        <div class="row">
                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.pckgProgrmListYn}">
                            <input type="checkbox" id="pckgProgrmListYn" value="Y" name="funcImprvmVO.pckgProgrmListYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.pckgProgrmListYn}">
                            <input type="checkbox" id="pckgProgrmListYn" value="Y" name="funcImprvmVO.pckgProgrmListYn" class="checkbox">
                          </c:if>

                          <label for="pckgProgrmListYn">&nbsp;프로그램목록</label>

                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.uiDsignYn}">
                            <input type="checkbox" id="uiDsignYn" value="Y" name="funcImprvmVO.uiDsignYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.uiDsignYn}">
                            <input type="checkbox" id="uiDsignYn" value="Y" name="funcImprvmVO.uiDsignYn" class="checkbox">
                          </c:if>

                          <label for="uiDsignYn">&nbsp;UI설계서</label>

                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.progrmDsignYn}">
                            <input type="checkbox" id="progrmDsignYn" value="Y" name="funcImprvmVO.progrmDsignYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.progrmDsignYn}">
                            <input type="checkbox" id="progrmDsignYn" value="Y" name="funcImprvmVO.progrmDsignYn" class="checkbox">
                          </c:if>

                          <label for="progrmDsignYn">&nbsp;프로그램설계서</label>
                        </div>

                        <div class="row">
                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.tableDsignYn}">
                            <input type="checkbox" id="tableDsignYn" value="Y" name="funcImprvmVO.tableDsignYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.tableDsignYn}">
                            <input type="checkbox" id="tableDsignYn" value="Y" name="funcImprvmVO.tableDsignYn" class="checkbox">
                          </c:if>

                          <label for="tableDsignYn">&nbsp;테이블설계서</label>

                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.progrmListYn}">
                            <input type="checkbox" id="progrmListYn" value="Y" name="funcImprvmVO.progrmListYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.progrmListYn}">
                            <input type="checkbox" id="progrmListYn" value="Y" name="funcImprvmVO.progrmListYn" class="checkbox">
                          </c:if>

                          <label for="progrmListYn">&nbsp;프로그램목록</label>

                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.userMnualYn}">
                            <input type="checkbox" id="userMnualYn" value="Y" name="funcImprvmVO.userMnualYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.userMnualYn}">
                            <input type="checkbox" id="userMnualYn" value="Y" name="funcImprvmVO.userMnualYn" class="checkbox">
                          </c:if>

                          <label for="userMnualYn">&nbsp;사용자메뉴얼</label>
                        </div>

                        <div class="row">
                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.admnMnualYn}">
                            <input type="checkbox" id="admnMnualYn" value="Y" name="funcImprvmVO.admnMnualYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.admnMnualYn}">
                            <input type="checkbox" id="admnMnualYn" value="Y" name="funcImprvmVO.admnMnualYn" class="checkbox">
                          </c:if>

                          <label for="admnMnualYn">&nbsp;운영자메뉴얼</label>

                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.unitTestYn}">
                            <input type="checkbox" id="unitTestYn" value="Y" name="funcImprvmVO.unitTestYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.unitTestYn}">
                            <input type="checkbox" id="unitTestYn" value="Y" name="funcImprvmVO.unitTestYn" class="checkbox">
                          </c:if>

                          <label for="unitTestYn">&nbsp;단위테스트</label>

                          <c:if test="${'Y' ==  funcImprvmFormVO.funcImprvmVO.unionTestYn}">
                            <input type="checkbox" id="unionTestYn" value="Y" name="funcImprvmVO.unionTestYn" checked="checked" class="checkbox">
                          </c:if>

                          <c:if test="${'Y' !=  funcImprvmFormVO.funcImprvmVO.unionTestYn}">
                            <input type="checkbox" id="unionTestYn" value="Y" name="funcImprvmVO.unionTestYn" class="checkbox">
                          </c:if>

                          <label for="unionTestYn">&nbsp;통합테스트</label>
                        </div>
                      </dd>
                    </dl>

                    <hr>

                    <dl>
                      <dt>변경방안</dt>
                      <dd><form:input path="funcImprvmVO.chngePlan"  class="control" title="변경방안"/></dd>
                    </dl>

                    <dl>
                      <dt>백업방안</dt>
                      <dd><form:input path="funcImprvmVO.backupPlan"  class="control" title="백업방안"/></dd>
                    </dl>

                    <dl>
                      <dt>원복방안</dt>
                      <dd><form:input path="funcImprvmVO.rstorePlan"  class="control" title="원복방안"/></dd>
                    </dl>

                    <dl>
                      <dt>제약조건</dt>
                      <dd><form:input path="funcImprvmVO.constrnt"  class="control" title="제약조건"/></dd>
                    </dl>

                    <dl>
                      <dt>고려사항</dt>
                      <dd><form:input path="funcImprvmVO.consder"  class="control" title="고려사항"/></dd>
                    </dl>

                    <dl>
                      <dt>첨부자료</dt>
                      <dd class="d-block">
                        <div class="file-box">
                          <!-- The fileinput-button span is used to style the file input field as button -->
                          <span class="btn btn-secondary fileinput-button">
                          <i class="ico-search"></i>파일찾기
                            <!-- The file input field used as target for the file upload widget -->
                          <input id="fiAtachmnfl" type="file" name="multipartFile" multiple>
                        </span>
                        </div>

                        <button type="button" class="btn btn-sm btn-primary" id="fiAtchmnflSendBtn">파일전송</button>

                        <ul id="fiAtachmnflListDiv" class="file-list-group">
                          <c:forEach var="atchmnflVO" items="${fiAtchmnflList}">
                            <li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
                              <a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a>
                              <a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
                            </li>
                          </c:forEach>
                        </ul>
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-left">
          <div class="card" style="position:static;">
            <div class="card-header type2">
              <h4 class="card-title">SR번호</h4>
              <dl class="sr-info">
                <dd style="width:210px;">
                  <c:if test="${registerFlag == 'create' }">
                    <form:hidden path="funcImprvmVO.srvcRsponsNo" id="srvcRsponsNo"/>
                    <c:out value="${srvcRsponsVO.srvcRsponsNo }"/>
                  </c:if>

                  <c:if test="${registerFlag != 'create' }">
                    <c:out value="${funcImprvmFormVO.funcImprvmVO.srvcRsponsNo }"/>
                  </c:if>
                </dd>

                <dt style="margin-right:10px;">
                  <b>요청자</b>
                </dt>

                <dd id="rqesterNm" style="margin-right:20px;">
                  <c:if test="${registerFlag == 'create' }">
                    <c:out value="${srvcRsponsVO.rqester1stNm }"/>
                  </c:if>

                  <c:if test="${registerFlag != 'create' }">
                    <c:out value="${funcImprvmFormVO.funcImprvmVO.rqester1stNm }"/>
                  </c:if>
                </dd>

                <dt style="margin-right:10px;">
                  <b>소속</b>
                </dt>
                <dd id="rqesterPsitn" style="margin-right:20px;">
                  <c:if test="${registerFlag == 'create' }">
                    <c:out value="${srvcRsponsVO.rqester1stPsitn }"/>
                  </c:if>

                  <c:if test="${registerFlag != 'create' }">
                    <c:out value="${funcImprvmFormVO.funcImprvmVO.rqester1stPsitn }"/>
                  </c:if>
                </dd>
              </dl>
            </div>
          </div>

          <div class="row">
            <div class="col">
              <div class="card">
                <div class="card-header">
                  <h5 class="card-title">기능개선 처리 및 개선계획일</h5>
                </div>

                <div class="card-body">
                  <div class="detailbox type1">
                    <dl>
                      <dt>요청일자</dt>
                      <c:if test="${registerFlag != 'create'}">
                        <dd id="requstDt">
                          <c:out value="${funcImprvmFormVO.funcImprvmVO.requstDtDateDisplay }"/>
                        </dd>
                      </c:if>

                      <c:if test="${registerFlag == 'create'}">
                        <dd id="requstDt">
                          <c:out value="${srvcRsponsVO.requstDtDateDisplay }"/>
                        </dd>
                      </c:if>
                      <dt>담당자</dt>
                      <dd id="chargerUserNm"><c:out value="${funcImprvmFormVO.funcImprvmVO.chargerUserNm }"/></dd>
                    </dl>

                    <dl>
                      <dt>적용예정일</dt>
                      <dd>
                        <form:input path="funcImprvmVO.applyPlanDtDisplay" id="applyPlanDtDisplay" class="control inpdate" title="적용예정일" placeholder="YYYY-MM-DD"/>
                      </dd>
                      <dt>적용배포일</dt>
                      <dd>
                        <form:input path="funcImprvmVO.applyRDtDisplay" id="applyRDtDisplay" class="control inpdate" title="적용배포일" placeholder="YYYY-MM-DD"/>
                      </dd>
                    </dl>

                    <hr>

                    <dl>
                      <dt>승인여부</dt>
                      <dd>
                        <c:if test="${registerFlag == 'create' ||  funcImprvmFormVO.funcImprvmVO.cnfrmYn == 'Y'}">
                          <input type="radio" id="cnfrmY" name="funcImprvmVO.cnfrmYn" value="Y" class="checkbox" title="수용" checked="checked">
                          <label for="cnfrmY">&nbsp;수용</label>
                          <input type="radio" id="cnfrmN" name="funcImprvmVO.cnfrmYn" value="N" class="checkbox" title="미수용">
                          <label for="cnfrmN">&nbsp;미수용</label>
                        </c:if>

                        <c:if test="${registerFlag != 'create' && funcImprvmFormVO.funcImprvmVO.cnfrmYn != 'Y'}">
                          <input type="radio" id="cnfrmY" name="funcImprvmVO.cnfrmYn" value="Y" class="checkbox" title="수용">
                          <label for="cnfrmY">&nbsp;수용</label>
                          <input type="radio" id="cnfrmN" name="funcImprvmVO.cnfrmYn" value="N" class="checkbox" checked="checked" title="미수용">
                          <label for="cnfrmN">&nbsp;미수용</label>
                        </c:if>
                      </dd>
                    </dl>

                    <dl>
                      <dt>미수용사유</dt>
                      <dd>
                        <form:input path="funcImprvmVO.noCnfrmResn" id="noCnfrmResn" class="control" title="미수용사유" disabled="disabled"/>
                      </dd>
                    </dl>

                    <dl>
                      <dt>개선시스템</dt>
                      <dd>
                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiRunSvrYn == 'Y'}">
                          <input type="checkbox" id="fiRunSvrYn" value="Y" name="funcImprvmVO.fiRunSvrYn" checked="checked" class="checkbox" title="운영서버">
                        </c:if>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiRunSvrYn != 'Y'}">
                          <input type="checkbox" id="fiRunSvrYn" value="Y" name="funcImprvmVO.fiRunSvrYn" class="checkbox" title="운영서버">
                        </c:if>

                        <label for="fiRunSvrYn">&nbsp;운영서버</label>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiDevSvrYn == 'Y'}">
                          <input type="checkbox" id="fiDevSvrYn" value="Y" name="funcImprvmVO.fiDevSvrYn" checked="checked" class="checkbox" title="개발서버">
                        </c:if>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiDevSvrYn != 'Y'}">
                          <input type="checkbox" id="fiDevSvrYn" value="Y" name="funcImprvmVO.fiDevSvrYn" class="checkbox" title="개발서버">
                        </c:if>

                        <label for="fiDevSvrYn">&nbsp;개발서버</label>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiDbYn == 'Y'}">
                          <input type="checkbox" id="fiDbYn" value="Y" name="funcImprvmVO.fiDbYn" checked="checked" class="checkbox" title="DB">
                        </c:if>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiDbYn != 'Y'}">
                          <input type="checkbox" id="fiDbYn" value="Y" name="funcImprvmVO.fiDbYn" class="checkbox" title="DB">
                        </c:if>

                        <label for="fiDbYn">&nbsp;DB</label>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiVmYn == 'Y'}">
                          <input type="checkbox" id="fiVmYn" value="Y" name="funcImprvmVO.fiVmYn" checked="checked" class="checkbox" title="해당">
                        </c:if>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiVmYn != 'Y'}">
                          <input type="checkbox" id="fiVmYn" value="Y" name="funcImprvmVO.fiVmYn" class="checkbox" title="해당">
                        </c:if>

                        <label for="fiVmYn">&nbsp;해당VM</label>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiEtcYn == 'Y'}">
                          <input type="checkbox" id="fiEtcYn" value="Y" name="funcImprvmVO.fiEtcYn" checked="checked" class="checkbox" title="기타">
                        </c:if>

                        <c:if test="${funcImprvmFormVO.funcImprvmVO.fiEtcYn != 'Y'}">
                          <input type="checkbox" id="fiEtcYn" value="Y" name="funcImprvmVO.fiEtcYn" class="checkbox" title="기타">
                        </c:if>

                        <label for="fiEtcYn">&nbsp;기타</label>
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-12">
              <div class="card">
                <div class="card-header">
                  <h5 class="card-title">개선 결과</h5>
                </div>

                <div class="card-body">
                  <div class="detailbox type1">
                    <dl>
                      <dt>네비게이션</dt>
                      <dd>
                        <form:textarea id="navigation" rows="2" path="funcImprvmVO.navigation" class="control" title="네비게이션" />
                      </dd>
                    </dl>

                    <dl>
                      <dt>개선요구사항</dt>
                      <dd>
                        <form:input path="funcImprvmVO.fiRquire" class="control" title="개선요구사항"/>
                      </dd>
                    </dl>

                    <dl>
                      <dt>개선사항</dt>
                      <dd>
                        <form:textarea path="funcImprvmVO.fiCn" class="control" title="개선사항" rows="5"></form:textarea>
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col">
                  <div class="card">
                    <div class="card-body">
                      <div class="detailbox type1">
                        <dl>
                          <dt>AS-IS</dt>
                          <dd class="d-block">
                            <div class="file-box">
                              <!-- The fileinput-button span is used to style the file input field as button -->
                              <span class="btn btn-secondary fileinput-button">
                              <i class="ico-search"></i>파일찾기
                                <!-- The file input field used as target for the file upload widget -->
                              <input id="asisAtchmnfl" type="file" name="multipartFile" multiple>
                            </span>
                            </div>
                            <button type="button" class="btn btn-sm btn-primary" id="asisAtchmnflSendBtn">파일전송</button>
                            <ul id="asisAtchmnflListDiv" class="file-list-group">
                              <c:forEach var="atchmnflVO" items="${asisAtchmnflList}">
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
                    </div>
                  </div>
                </div>

                <div class="col">
                  <div class="card">
                    <div class="card-body">
                      <div class="detailbox type1">
                        <dl>
                          <dt>TO-BE</dt>
                          <dd class="d-block">
                            <div class="file-box">
                              <!-- The fileinput-button span is used to style the file input field as button -->
                              <span class="btn btn-secondary fileinput-button">
                              <i class="ico-search"></i>파일찾기
                                <!-- The file input field used as target for the file upload widget -->
                              <input id="tobeAtchmnfl" type="file" name="multipartFile" multiple>
                            </span>
                            </div>
                            <button type="button" class="btn btn-sm btn-primary" id="tobeAtchmnflSendBtn">파일전송</button>
                            <ul id="tobeAtchmnflListDiv" class="file-list-group">
                              <c:forEach var="atchmnflVO" items="${tobeAtchmnflList}">
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
                    </div>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col">
                  <div class="card">
<%--                    <div class="card-header">--%>
<%--                      <h5 class="card-title">담당자</h5>--%>
<%--                    </div>--%>

                    <div class="card-body">
                      <div class="detailbox type1">
                        <dl>
                          <dt>기관담당자</dt>
                          <dd>
                            <select class="control" name="funcImprvmVO.confirmUsr">
                              <c:forEach items="${cstmrList}" var="item">
                                <option <c:if test="${funcImprvmFormVO.funcImprvmVO.confirmUsr eq item.userId}">selected</c:if> value="${item.userId}">${item.userNm}</option>
                              </c:forEach>
                            </select>
                          </dd>
                        </dl>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form:form>

    <div class="btnbox right mb30">
      <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary">
        <i class="ico-list"></i>목록
      </button>
      <c:if test="${registerFlag != 'create' }">
        <button type="button" onclick="fncDelete()" class="btn btn-dark">
          <i class="ico-delete"></i>삭제
        </button>
      </c:if>
      <button type="button" onclick="fncSave()" class="btn btn-primary">
        <i class="ico-save"></i>저장
      </button>
    </div>
  </main>
</body>
</html>
