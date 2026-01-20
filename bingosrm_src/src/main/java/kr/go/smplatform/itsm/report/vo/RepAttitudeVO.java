package kr.go.smplatform.itsm.report.vo;

/**
 * 보고서 개편 하면서 만들어진 VO
 */
public class RepAttitudeVO {
    private final String attitudeCode;
    private final String attitudeName;
    private final boolean attitudePick;

    @Override
    public String toString() {
        return "RepAttitudeVO{" +
                "attitudeCode='" + attitudeCode + '\'' +
                ", attitudeName='" + attitudeName + '\'' +
                ", attitudePick='" + attitudePick + '\'' +
                '}';
    }

    public RepAttitudeVO(String attitudeCode, String attitudeName, Long attitudePick) {
        this.attitudeCode = attitudeCode;
        this.attitudeName = attitudeName;
        this.attitudePick = attitudePick != 0;
    }

    public String getAttitudeCode() {
        return attitudeCode;
    }

    public String getAttitudeName() {
        return attitudeName;
    }

    public boolean getAttitudePick() {
        return attitudePick;
    }
}
