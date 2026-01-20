package kr.go.smplatform.itsm.report.vo;

/**
 * 보고서 개편 하면서 만들어진 VO
 */
public class RepMasterVO2 {
    private String repName;
    private String repTyCode;
    private String reportDt;

    @Override
    public String toString() {
        return "RepMasterVO2{" +
                "repName='" + repName + '\'' +
                ", repTyCode='" + repTyCode + '\'' +
                ", reportDt=" + reportDt +
                '}';
    }

    public RepMasterVO2(String repName, String repTyCode, String reportDt) {
        this.repName = repName;
        this.repTyCode = repTyCode;
        this.reportDt = reportDt;
    }

    public String getRepName() {
        return repName;
    }

    public String getRepTyCode() {
        return repTyCode;
    }

    public String getReportDt() {
        return reportDt;
    }
}
