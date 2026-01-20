package kr.go.smplatform.itsm.syscharger.vo;

import kr.go.smplatform.itsm.user.vo.UserVO;

public class SysChargerVO extends UserVO {
    private String sysCode;
    private String sysCodeNm;
    private String sysCodeSubNm1;
    private String[] sysCodes;

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysCodeNm() {
        return sysCodeNm;
    }

    public void setSysCodeNm(String sysCodeNm) {
        this.sysCodeNm = sysCodeNm;
    }

    public String getSysCodeSubNm1() {
        return sysCodeSubNm1;
    }

    public void setSysCodeSubNm1(String sysCodeSubNm1) {
        this.sysCodeSubNm1 = sysCodeSubNm1;
    }

    public String[] getSysCodes() {
        return sysCodes;
    }

    public void setSysCodes(String[] sysCodes) {
        this.sysCodes = sysCodes;
    }
}
