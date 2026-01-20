package kr.go.smplatform.itsm.cmmncode.vo;

import kr.go.smplatform.itsm.base.vo.BaseVO;

public class CmmnCodeTyVO extends BaseVO {
    private String cmmnCodeTy;
    private String cmmnCodeTyNm;
    private String cmmnCodeTyDc;

    public String getCmmnCodeTy() {
        return cmmnCodeTy;
    }

    public void setCmmnCodeTy(String cmmnCodeTy) {
        this.cmmnCodeTy = cmmnCodeTy;
    }

    public String getCmmnCodeTyNm() {
        return cmmnCodeTyNm;
    }

    public void setCmmnCodeTyNm(String cmmnCodeTyNm) {
        this.cmmnCodeTyNm = cmmnCodeTyNm;
    }

    public String getCmmnCodeTyDc() {
        return cmmnCodeTyDc;
    }

    public void setCmmnCodeTyDc(String cmmnCodeTyDc) {
        this.cmmnCodeTyDc = cmmnCodeTyDc;
    }
}
