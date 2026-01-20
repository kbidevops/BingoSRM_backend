<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
            var jsonData = '${jsonData}';
            ozparams = ozparams.replace(/&quot;/g, '"');
            $('#ozparams').val(ozparams);
            var paramObj = JSON.parse(ozparams);

            if (jsonData != null) {
                jsonData = jsonData.replace(/&quot;/g, "'");
            }
			console.log(ozparams);
			console.log(jsonData);
			
            function SetOZParamters_OZViewer(){
                var oz;
                oz = document.getElementById("OZViewer");
                
                oz.sendToActionScript("print.alldocument", "true");
                oz.sendToActionScript("viewer.childcount", paramObj.length);
                oz.sendToActionScript("viewer.showtree", "true");
                oz.sendToActionScript("viewer.focus_doc_index", "0");
                oz.sendToActionScript("global.concatpage", "false");
                oz.sendToActionScript("export.savemultidoc", "true");
                console.log("length: "+ paramObj.length);
                
                var funcImprvmNo = paramObj.fiNo.split(",");
                
                for(var i=0; i<=funcImprvmNo.length; i++){
                	if(i==0){
		                oz.sendToActionScript("connection.servlet", ozserver+"server");
	                	oz.sendToActionScript("connection.reportname", "/knfc/funcImprvmPlan.ozr");
		                oz.sendToActionScript("export.filename", funcImprvmNo[i] + "-기능개선계획서");
		                oz.sendToActionScript("connection.pcount", "3");
		                oz.sendToActionScript("connection.args1", "funcImprvmNo="+funcImprvmNo[i]);
		                oz.sendToActionScript("connection.args2", "itsmDomain=" + location.origin + "/");
                	}else{
		                oz.sendToActionScript("child"+i+".connection.servlet", ozserver+"server");
	                	oz.sendToActionScript("child"+i+".connection.reportname", paramObj.ozr);
		                oz.sendToActionScript("child"+i+".export.filename", funcImprvmNo[i] + "-기능개선계획서");
		                oz.sendToActionScript("child"+i+".connection.pcount", "3");
		                oz.sendToActionScript("child"+i+".connection.args1", "funcImprvmNo="+funcImprvmNo[i]);
		                oz.sendToActionScript("child"+i+".connection.args2", "itsmDomain=" + location.origin + "/");
                	}
                }
                
                for(var i=0; i<=funcImprvmNo.length; i++){
		            oz.sendToActionScript("child"+(funcImprvmNo.length+i)+".connection.servlet", ozserver+"server");
	                oz.sendToActionScript("child"+(funcImprvmNo.length+i)+".connection.reportname", "/knfc/funcImprvmResult.ozr");
		            oz.sendToActionScript("child"+(funcImprvmNo.length+i)+".export.filename", funcImprvmNo[i] + "-기능개선결과서");
		            oz.sendToActionScript("child"+(funcImprvmNo.length+i)+".connection.pcount", "3");
		            oz.sendToActionScript("child"+(funcImprvmNo.length+i)+".connection.args1", "funcImprvmNo="+funcImprvmNo[i]);
		            oz.sendToActionScript("child"+(funcImprvmNo.length+i)+".connection.args2", "itsmDomain=" + location.origin + "/");
                }
                
                return true;
            }
			
            var opt = [];
            opt["print_flash"] = false;
            
            start_ozjs("OZViewer","${ozserver}ozhviewer/", opt);
        </script>
<%--         <input type="hidden" name="ozparams" value="${ozparams}"/> --%>
 <input type="hidden" id="ozparams" name="ozparams" value=""/>
    </body>
</html>