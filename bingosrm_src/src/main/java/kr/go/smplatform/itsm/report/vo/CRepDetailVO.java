package kr.go.smplatform.itsm.report.vo;

/**
 * 보고서 개편 하면서 만들어진 VO
 */
public class CRepDetailVO {
    private final String repSn;
    private final String sysCode;
    private final String userId;
    private final String currentDescription;
    private final String planDescription;

    private CRepDetailVO(String repSn, String sysCode, String userId, String currentDescription, String planDescription) {
        this.repSn = repSn;
        this.sysCode = sysCode;
        this.userId = userId;
        this.currentDescription = currentDescription;
        this.planDescription = planDescription;
    }

    @Override
    public String toString() {
        return "CRepDetailVO{" +
                "repSn='" + repSn + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", userId='" + userId + '\'' +
                ", currentDescription='" + currentDescription + '\'' +
                ", planDescription='" + planDescription + '\'' +
                '}';
    }

    public String getRepSn() {
        return repSn;
    }

    public String getSysCode() {
        return sysCode;
    }

    public String getUserId() {
        return userId;
    }

    public String getCurrentDescription() {
        return currentDescription;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public static final class Builder {
        private String repSn;
        private String sysCode;
        private String userId;
        private String currentDescription;
        private String planDescription;

        private Builder() {
        }

        public static Builder aCRepDetailVO() {
            return new Builder();
        }

        public Builder repSn(String repSn) {
            this.repSn = repSn;
            return this;
        }

        public Builder sysCode(String sysCode) {
            this.sysCode = sysCode;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder currentDescription(String currentDescription) {
            this.currentDescription = currentDescription;
            return this;
        }

        public Builder planDescription(String planDescription) {
            this.planDescription = planDescription;
            return this;
        }

        public CRepDetailVO build() {
            return new CRepDetailVO(repSn, sysCode, userId, currentDescription, planDescription);
        }
    }
}
