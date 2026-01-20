package kr.go.smplatform.itsm.report.vo;

/**
 * 보고서 개편 하면서 만들어진 VO
 */
public class RepDescVO {
    private String code;
    private String currentDescription;
    private String nextDescription;

    @Override
    public String toString() {
        return "RepDescVO{" +
                "code='" + code + '\'' +
                ", currentDescription='" + currentDescription + '\'' +
                ", nextDescription='" + nextDescription + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrentDescription() {
        return currentDescription;
    }

    public void setCurrentDescription(String currentDescription) {
        this.currentDescription = currentDescription;
    }

    public String getNextDescription() {
        return nextDescription;
    }

    public void setNextDescription(String nextDescription) {
        this.nextDescription = nextDescription;
    }
}
