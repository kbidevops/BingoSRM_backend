package kr.go.smplatform.itsm.progrmaccesauthor.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.GenericValidator;

import kr.go.smplatform.itsm.progrm.vo.ProgrmVO;

public class ProgrmAccesAuthorVO extends ProgrmVO {
    public static final String LEFT_MENU_PROGRMACCESAUTHORVO = "LEFT_MENU_PROGRMACCESAUTHORVO";
    public static final String TOP_MENU_PROGRMACCESAUTHORVO = "TOP_MENU_PROGRMACCESAUTHORVO";
    public static final String TOP_MENU_LIST = "TOP_MENU_LIST";
    public static final String LEFT_MENU_LIST = "LEFT_MENU_LIST";
    public static final String SELECT_LEFT_MENU = "SELECT_LEFT_MENU";

    private String progrmAccesAuthorCode;
    private String[] progrmSns = new String[0];
    private List<ProgrmAccesAuthorVO> progrmList = new ArrayList<ProgrmAccesAuthorVO>();

    public List<ProgrmAccesAuthorVO> getProgrmList() {
        return progrmList;
    }

    public void setProgrmList(List<ProgrmAccesAuthorVO> progrmList) {
        this.progrmList = progrmList;
    }

    public String getProgrmAccesAuthorCode() {
        return progrmAccesAuthorCode;
    }

    public void setProgrmAccesAuthorCode(String progrmAccesAuthorCode) {
        this.progrmAccesAuthorCode = progrmAccesAuthorCode;
    }

    public String[] getProgrmSns() {
        return progrmSns;
    }

    public void setProgrmSns(String[] progrmSns) {
        this.progrmSns = progrmSns;
    }

    public boolean getChecked() {
        return !GenericValidator.isBlankOrNull(progrmAccesAuthorCode);
    }
}
