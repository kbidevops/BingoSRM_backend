<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
  <title>itsm</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <meta http-equiv="Content-Script-Type" content="text/javascript" />
  <meta http-equiv="Content-Style-Type" content="text/css" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <script src="${pageContext.request.contextPath}/js/jquery-2.0.3.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/ui/1.12.1/jquery-ui.min.js"></script>
  <script type="text/javascript" src="${ozserver}ozhviewer/jquery.dynatree.js" charset="utf-8"></script>
  <script type="text/javascript" src="${ozserver}ozhviewer/OZJSViewer.js" charset="utf-8"></script>
  <link rel="stylesheet" href="${ozserver}ozhviewer/ui.dynatree.css" type="text/css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ui/1.12.1/themes/base/jquery-ui.css" type="text/css"/>
  <script src="${pageContext.request.contextPath}/js/jquery.ui.widget.js"></script>
</head>

<body>

<div id="OZViewer" style="width:100%; height:970px;"></div>
<script type="text/javascript" >
  var ozserver = '${ozserver}';
  var ozparams = '${ozparams}';
  var jsonData = '${jsonData}'.replace(/&quot;/g, "'");
  ozparams = ozparams.replace(/&quot;/g, '"');
  $('#ozparams').val(ozparams);

  var paramObj = JSON.parse(ozparams);
  paramObj.pcount++;
  paramObj.params.push({ id: "filePath", value: location.origin + "/${pageContext.request.contextPath}/itsm/atchmnfl/site/retrieve.do?atchmnflSn=1&atchmnflId=".replace("//", "/") });

  console.log(ozserver);
  console.log(ozparams);
  console.log(jsonData);

  function SetOZParamters_OZViewer() {
    var oz = document.getElementById("OZViewer");
    oz.sendToActionScript("connection.servlet", ozserver + "server");
    oz.sendToActionScript("connection.reportname", paramObj.ozr);
    oz.sendToActionScript("connection.pcount", paramObj.pcount);

    if (paramObj.flName !== null) {
      oz.sendToActionScript("export.filename", paramObj.flName);
    }

    var startVal = 1;

    if (jsonData !== null) {
      oz.sendToActionScript("connection.args1", "jsondata=" + jsonData);
      startVal = 2;
    }

    for (var i = 0; i < paramObj.pcount; i++) {
      var paramName = paramObj.params[i].id;
      var paramValue = paramObj.params[i].value;

      oz.sendToActionScript("connection.args" + startVal, paramName + "=" + paramValue);
      startVal++;
    }

    return true;
  }

  var opt = [];
  opt["print_flash"] = false;
  start_ozjs("OZViewer", "${ozserver}ozhviewer/", opt);
</script>
<%--         <input type="hidden" name="ozparams" value="${ozparams}"/> --%>
<input type="hidden" id="ozparams" name="ozparams" value=""/>

</body>
</html>