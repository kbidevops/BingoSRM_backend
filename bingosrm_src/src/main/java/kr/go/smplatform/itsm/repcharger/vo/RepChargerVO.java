package kr.go.smplatform.itsm.repcharger.vo;

import java.util.ArrayList;
import java.util.List;

import kr.go.smplatform.itsm.user.vo.UserVO;

public class RepChargerVO extends UserVO {
    private String sysCode;
    private String sysCodeNm;
    private String sysCodeSubNm1;
    private String userId;
    private String userLocat;
    private String userNm;
    private String repChargerId;
    private List<String> sysCodes;
    private List<RepChargerVO> repChargerVOList;
    private boolean reportCharger = false;

    public RepChargerVO() {
        sysCodes = new ArrayList<String>();
        repChargerVOList = new ArrayList<RepChargerVO>();
    }

    public boolean getReportCharger() {
        return reportCharger;
    }

    public void setReportCharger(boolean reportCharger) {
        this.reportCharger = reportCharger;
    }

    public String getSysCodeSubNm1() {
        return sysCodeSubNm1;
    }

    public void setSysCodeSubNm1(String sysCodeSubNm1) {
        this.sysCodeSubNm1 = sysCodeSubNm1;
    }

    public List<String> getSysCodes() {
        return sysCodes;
    }

    public void setSysCodes(List<String> sysCodes) {
        this.sysCodes = sysCodes;
    }

    public List<RepChargerVO> getRepChargerVOList() {
        return repChargerVOList;
    }

    public void setRepChargerVOList(List<RepChargerVO> repChargerVOList) {
        this.repChargerVOList = repChargerVOList;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getUserLocat() {
        return userLocat;
    }

    public void setUserLocat(String userLocat) {
        this.userLocat = userLocat;
    }

    public String getSysCodeNm() {
        return sysCodeNm;
    }

    public void setSysCodeNm(String sysCodeNm) {
        this.sysCodeNm = sysCodeNm;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRepChargerId() {
        return repChargerId;
    }

    public void setRepChargerId(String repChargerId) {
        this.repChargerId = repChargerId;
    }
}
