package kr.go.smplatform.itsm.report.vo;

/**
 * 보고서 개편 하면서 만들어진 VO
 */
public class RepAssignVO {
    private final String assignCode;
    private final String assignName;

    @Override
    public String toString() {
        return "RepAssignVO{" +
                "assignCode='" + assignCode + '\'' +
                ", assignName='" + assignName + '\'' +
                '}';
    }

    public RepAssignVO(String assignCode, String assignName) {
        this.assignCode = assignCode;
        this.assignName = assignName;
    }

    public String getAssignCode() {
        return assignCode;
    }

    public String getAssignName() {
        return assignName;
    }
}
