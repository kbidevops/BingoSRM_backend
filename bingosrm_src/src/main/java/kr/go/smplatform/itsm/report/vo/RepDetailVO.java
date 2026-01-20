package kr.go.smplatform.itsm.report.vo;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RepDetailVO extends RepMasterVO{
    
    /** 보고서 순번*/
    private int repSn;
    /** 대상시스템 코드*/
    private String sysCode;
    /** 대상시스템 이름*/
    private String sysCodeNm;
    /** 대상 시스템 이름2*/
    private String sysCodeSubNm1;
    /** 담당자 아이디*/
    private String userId;    
    /** 담당자 이름*/
    private String userNm;
    /** 실적 내용*/
    private String execDesc;
    /** 계획 내용*/
    private String planDesc;

    /**
     * reportDt와 다른 일자를 보여지도록 한다.
     * @return
     */
    private String dailyReportName;

    public String getDailyReportName() {
        return dailyReportName;
    }

    public void setDailyReportName(String dailyReportName) {
        this.dailyReportName = dailyReportName;
    }

    public String getSysCodeSubNm1() {
        return sysCodeSubNm1;
    }
    public void setSysCodeSubNm1(String sysCodeSubNm1) {
        this.sysCodeSubNm1 = sysCodeSubNm1;
    }
    public String getUserNm() {
        return userNm;
    }
    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }
    public String getsysCodeNm() {
        return sysCodeNm;
    }
    public void setsysCodeNm(String sysCodeNm) {
        this.sysCodeNm = sysCodeNm;
    }
    public int getRepSn() {
        return repSn;
    }
    public void setRepSn(int repSn) {
        this.repSn = repSn;
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
    public String getExecDesc() {
        return execDesc;
    }
    public void setExecDesc(String execDesc) {
        this.execDesc = execDesc;
    }
    public String getPlanDesc() {
        return planDesc;
    }
    public void setPlanDesc(String planDesc) {
        this.planDesc = planDesc;
    }
    
}
