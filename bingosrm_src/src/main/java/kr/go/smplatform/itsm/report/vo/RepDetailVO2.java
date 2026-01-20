package kr.go.smplatform.itsm.report.vo;

/**
 * 보고서 개편 하면서 만들어진 VO
 */
public class RepDetailVO2 {
    private final String assignCode;
    private final String assignName;
    private final String execDesc;
    private final String planDesc;

    @Override
    public String toString() {
        return "RepDetailVO2{" +
                "assignCode='" + assignCode + '\'' +
                ", assignName='" + assignName + '\'' +
                ", execDesc='" + execDesc + '\'' +
                ", planDesc='" + planDesc + '\'' +
                '}';
    }

    public RepDetailVO2(String assignCode, String assignName, String execDesc, String planDesc) {
        this.assignCode = assignCode;
        this.assignName = assignName;
        this.execDesc = execDesc;
        this.planDesc = planDesc;
    }

    public String getAssignCode() {
        return assignCode;
    }

    public String getAssignName() {
        return assignName;
    }

    public String getExecDesc() {
        return execDesc;
    }

    public String getPlanDesc() {
        return planDesc;
    }
}
