<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
  .bsns-logo {
      width: 40%;
      height: 100px;
      display: inline-block;
      object-fit: contain;
  }
</style>

<script>
  var controller = (function() {
    var form = {
      getInfo: function() {
        return new FormData(document.forms.editForm);
      },
      setInfo: function(info) {
        if (!info) {
          return;
        }

        var keys = Object.keys(info);

        $(keys).each(function(i, key) {
          $(document.forms.editForm[key]).val(info[key]);
        });

        function getLogoSrc(logoFileId) {
          if (!logoFileId) {
            return "";
          }

          return "${pageContext.request.contextPath}/itsm/atchmnfl/site/retrieve.do?atchmnflSn=1&atchmnflId=" + logoFileId;
        }

        $("#infoLogoA").attr("src", getLogoSrc(info.infoLogoA));
        $("#infoLogoA2").attr("src", getLogoSrc(info.infoLogoA));
        $("#infoLogoB").attr("src", getLogoSrc(info.infoLogoB));
        $("#infoLogoB2").attr("src", getLogoSrc(info.infoLogoB));
      },
    };

    var remote = {
      getChargerList: function(userTyCodes) {
        return $.ajax({
          url: "/itsm/bsnsInfo/mngr/getChargersAjax.do?userTyCodes=" + userTyCodes.join(","),
          method: "GET",
        });
      },
      getInfo: function() {
        return $.ajax({
          url: "/itsm/bsnsInfo/mngr/getInfoAjax.do",
          method: "GET",
        });
      },
      updateInfo: function(info) {
        return $.ajax({
          url: "/itsm/bsnsInfo/mngr/setInfoAjax.do",
          method: "POST",
          cache: false,
          contentType: false,
          processData: false,
          data: info,
        });
      },
    };

    return {
      form: form,
      remote: remote,
    };
  })();

  function onChangeFile(targetId, inputElement) {
    var file = inputElement.files[0];

    if (!file) {
      $(targetId)[0].src = "";
    }

    var fileReader = new FileReader();

    fileReader.onload = function(event) {
      $(targetId).attr("src", event.target.result);
    };

    fileReader.readAsDataURL(file);
  }

  function fncReset() {
    controller.remote.getInfo().done(function(response) {
      controller.form.setInfo(response.data);
    }).fail(function() {
      alert("데이터를 불러오는 중 오류가 발생했습니다");
    });

    $("[name=logoA]")[0].value = "";
    $("[name=logoB]")[0].value = "";
    $("#infoLogoA2")[0].src = $("#infoLogoA")[0].src;
    $("#infoLogoB2")[0].src = $("#infoLogoB")[0].src;
  }

  function fncSave() {
    confirm("저장하시겠습니까?", "사업정보관리", null, function (request) {
      console.log("request = " + request);

      controller.remote.updateInfo(
        controller.form.getInfo()
      ).done(function(response) {
        if (response.result === 1) {
          alert("저장했습니다");
        }
        else {
          alert("저장에 실패했습니다");
        }

        fncReset();
      }).fail(function() {
        alert("저장 중 오류가 발생했습니다");
      });
    });
  }

  $(function() {
    // 운영책임자: 시스템관리자, 시스템담당자 (PM/PL)
    controller.remote.getChargerList(["R001", "R003"]).done(function(response) {
      $(response.list).each(function(i, charger) {
        $("<option>", {
          value: charger.userId,
          text: charger.userName,
        }).appendTo(document.forms.editForm.infoOpCharger);
      })
    });

    // 사업담당자: 기관담당자(책임급)
    controller.remote.getChargerList(["R002"]).done(function(response) {
      $(response.list).each(function(i, charger) {
        $("<option>", {
          value: charger.userId,
          text: charger.userName,
        }).appendTo(document.forms.editForm.infoBsnsCharger);
      })
    });

    // 사업책임자: 기관담당자(부장, 실장급)
    controller.remote.getChargerList(["R002"]).done(function(response) {
      $(response.list).each(function(i, charger) {
        $("<option>", {
          value: charger.userId,
          text: charger.userName,
        }).appendTo(document.forms.editForm.infoBsnsManager);
      })
    });

    fncReset();
  });
</script>

<main id="content" class="mini">
  <div class="page-header">
    <h3 class="page-title">사업정보관리</h3>
  </div>

  <div class="row">
    <div class="col-12">
      <form name="editForm">
        <div class="card">
          <div class="card-header">
            <h4 class="card-title">사업정보</h4>
          </div>
          <div class="card-body">
            <div class="detailbox type1">
              <dl>
                <dt>로고(기관)</dt>
                <dd>
                  <img src="" alt="">
                  <div class="file-box">
                    <img id="infoLogoA" class="bsns-logo" src="" alt="기관 로고"> →
                    <img id="infoLogoA2" class="bsns-logo" src="" alt="바뀔 기관 로고">
                    <!-- The fileinput-button span is used to style the file input field as button -->
                    <span class="btn btn-secondary fileinput-button">
                      <i class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
                      <input type="file" name="logoA" onchange="onChangeFile('#infoLogoA2', this)">
                    </span>
                  </div>
                </dd>
              </dl>
              <dl>
                <dt>로고(수행사)</dt>
                <dd>
                  <img id="infoLogoB" class="bsns-logo" src="" alt="수행사 로고"> →
                  <img id="infoLogoB2" class="bsns-logo" src="" alt="바뀔 수행사 로고">
                  <div class="file-box">
                    <!-- The fileinput-button span is used to style the file input field as button -->
                    <span class="btn btn-secondary fileinput-button">
                      <i class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
                      <input type="file" name="logoB" onchange="onChangeFile('#infoLogoB2', this)">
                    </span>
                  </div>
                </dd>
              </dl>
              <dl>
                <dt>사업명</dt>
                <dd><input type="text" name="infoBsnsName" class="control" title="사업명"></dd>
              </dl>
              <dl>
                <dt>발주기관</dt>
                <dd><input type="text" name="infoAgency" class="control" title="발주기관"></dd>
              </dl>
              <dl>
                <dt>사업자</dt>
                <dd><input type="text" name="infoBsnsPerson" class="control" title="사업자"></dd>
              </dl>
              <dl>
                <dt>주관부서</dt>
                <dd><input type="text" name="infoBsnsDepart" class="control" title="주관부서"></dd>
              </dl>
              <dl>
                <dt>운영책임자</dt>
                <dd>
                  <select class="control" name="infoOpCharger"></select>
                </dd>
              </dl>
              <dl>
                <dt>사업담당자</dt>
                <dd>
                  <select class="control" name="infoBsnsCharger"></select>
                </dd>
              </dl>
              <dl>
                <dt>사업책임자</dt>
                <dd>
                  <select class="control" name="infoBsnsManager"></select>
                </dd>
              </dl>
              <dl>
                <dt>사업기간</dt>
                <dd><input type="text" name="infoBsnsPeriod" class="control" title="사업기간" placeholder="예시) 2023.1.1 ~ 2024.5.4. 1"></dd>
              </dl>
            </div>
          </div>
        </div>
      </form>

      <div class="btnbox right">
        <button type="button" onclick="fncReset()" class="btn btn-secondary"><i class="ico-reset"></i>초기화</button>
        <button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
      </div>
    </div>
  </div>
</main>