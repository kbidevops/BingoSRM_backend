package kr.go.smplatform.itsm.hist.login.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.user.vo.UserVO;

public class HistLoginVO extends UserVO {
    public static final String LOGOUT_STTUS_CODE_NORMAL = "A101";
    public static final String LOGOUT_STTUS_CODE_TIMEOUT = "A102";

    private String sessionId;
    private Date loginDt;
    private String loginDtDisplay;

    private Date logoutDt;
    private String logoutDtDisplay;
    private String logoutSttusCode;

    public String getLoginDtDisplay() {
        if (loginDt != null) {
            loginDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).format(loginDt);
        }
        return loginDtDisplay;
    }

    public void setLoginDtDisplay(String loginDtDisplay) {
        this.loginDtDisplay = loginDtDisplay;
        try {
            loginDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(loginDtDisplay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getLogoutDtDisplay() {
        if (logoutDt != null) {
            logoutDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).format(logoutDt);
        }
        return logoutDtDisplay;
    }

    public void setLogoutDtDisplay(String logoutDtDisplay) {
        this.logoutDtDisplay = logoutDtDisplay;
        try {
            logoutDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(logoutDtDisplay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLoginDt() {
        return loginDt;
    }

    public void setLoginDt(Date loginDt) {
        this.loginDt = loginDt;
    }

    public Date getLogoutDt() {
        return logoutDt;
    }

    public void setLogoutDt(Date logoutDt) {
        this.logoutDt = logoutDt;
    }

    public String getLogoutSttusCode() {
        return logoutSttusCode;
    }

    public void setLogoutSttusCode(String logoutSttusCode) {
        this.logoutSttusCode = logoutSttusCode;
    }
}
