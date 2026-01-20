package kr.go.smplatform.itsm.progrm.vo;

import java.util.ArrayList;
import java.util.List;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;

public class ProgrmVO extends BaseVO {
    public static final String ROOT_PROGRAM_SN = "0";

    private String progrmSn;
    private String progrmNm;
    private String progrmUri;
    private String upperProgrmSn;
    private String sortNo;
    private String menuIndictYn;

    private List<ProgrmAccesAuthorVO> progrmAccesAuthorVOList = new ArrayList<ProgrmAccesAuthorVO>();

    public String getProgrmSn() {
        return progrmSn;
    }

    public void setProgrmSn(String progrmSn) {
        this.progrmSn = progrmSn;
    }

    public String getProgrmNm() {
        return progrmNm;
    }

    public void setProgrmNm(String progrmNm) {
        this.progrmNm = progrmNm;
    }

    public String getProgrmUri() {
        return progrmUri;
    }

    public void setProgrmUri(String progrmUri) {
        this.progrmUri = progrmUri;
    }

    public String getUpperProgrmSn() {
        return upperProgrmSn;
    }

    public void setUpperProgrmSn(String upperProgrmSn) {
        this.upperProgrmSn = upperProgrmSn;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getMenuIndictYn() {
        return menuIndictYn;
    }

    public void setMenuIndictYn(String menuIndictYn) {
        this.menuIndictYn = menuIndictYn;
    }

    public List<ProgrmAccesAuthorVO> getProgrmAccesAuthorVOList() {
        return progrmAccesAuthorVOList;
    }

    public void setProgrmAccesAuthorVOList(List<ProgrmAccesAuthorVO> progrmAccesAuthorVOList) {
        this.progrmAccesAuthorVOList = progrmAccesAuthorVOList;
    }
}
