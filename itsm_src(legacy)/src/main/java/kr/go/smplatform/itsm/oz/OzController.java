package kr.go.smplatform.itsm.oz;

import kr.go.smplatform.itsm.report.web.RepMasterController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OzController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepMasterController.class);

    final String ozserver = "/oz70/";

    @RequestMapping(value = "/itsm/rep/master/mngr/OzReportViewer.do")
    private String ozReportViewer(@RequestParam("ozparams") String ozparams, @RequestParam("jsonData") String jsonData, Model model) {
        final String strParam = ozparams
                .replaceAll("\n", "")
                .replaceAll("\'", "");

        model.addAttribute("ozparams", strParam);
        model.addAttribute("jsonData", jsonData);
        model.addAttribute("ozserver", ozserver);

        LOGGER.info("strParam : " + strParam);

        return "/cmmn/OzReportViewer";
    }

    @RequestMapping(value = "/itsm/rep/master/mngr/OzReportViewerFi.do")
    private String OzReportViewerFi(@RequestParam("ozparams") String ozparams, @RequestParam("jsonData") String jsonData, Model model) {
        final String strParam = ozparams
                .replaceAll("\n", "")
                .replaceAll("\'", "");

        model.addAttribute("ozparams", strParam);
        model.addAttribute("jsonData", jsonData);
        model.addAttribute("ozserver", ozserver);

        LOGGER.info("strParam : " + strParam);

        return "/cmmn/OzReportViewerFi";
    }

    @RequestMapping(value = "/itsm/rep/master/mngr/OzReportViewerMulti.do")
    private String ozReportViewerMulti(@RequestParam("ozparams") String ozparams, @RequestParam("jsonData") String jsonData, Model model) {
        final String strParam = ozparams
                .replaceAll("\n", "")
                .replaceAll("\'", "");

        model.addAttribute("ozparams", strParam);
        model.addAttribute("jsonData", jsonData);
        model.addAttribute("ozserver", ozserver);

        LOGGER.info("strParam : " + strParam);

        return "/cmmn/OzReportViewerMulti";
    }

}
