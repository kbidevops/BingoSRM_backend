package kr.go.smplatform.itsm.repAttd.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.validator.GenericValidator;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.config.ITSMDefine;

public class RepAttdVO extends BaseVO {
    private String userId;
    private String attdCode;
    private String attdCodeNm;
    private String userLocat;
    private String userNm;
    private Date attdDt;
    private String attdDtDisplay;

    public Date getAttdDt() {
        return attdDt;
    }

    public String getAttdDtDisplay() {
        if (attdDt != null) {
            attdDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(attdDt);
        }
        return attdDtDisplay;
    }

    public void setAttdDtDisplay(String attdDtDisplay) {
        this.attdDtDisplay = attdDtDisplay;
        if (!GenericValidator.isBlankOrNull(this.attdDtDisplay)) {
            try {
                attdDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(attdDtDisplay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAttdDt(Date attdDt) {
        this.attdDt = attdDt;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAttdCode() {
        return attdCode;
    }

    public void setAttdCode(String attdCode) {
        this.attdCode = attdCode;
    }

    public String getAttdCodeNm() {
        return attdCodeNm;
    }

    public void setAttdCodeNm(String attdCodeNm) {
        this.attdCodeNm = attdCodeNm;
    }
}
