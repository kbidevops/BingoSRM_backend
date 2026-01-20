package kr.go.smplatform.itsm.report.vo;

/**
 * 보고서 개편 하면서 만들어진 VO
 */
public class CRRepAttachmentVO {
    private final String repSn;
    private final String userId;
    private final String requiredFile;
    private final String additionalFile;

    @Override
    public String toString() {
        return "CRRepAttachmentVO{" +
                "repSn='" + repSn + '\'' +
                ", userId='" + userId + '\'' +
                ", requiredFile='" + requiredFile + '\'' +
                ", additionalFile='" + additionalFile + '\'' +
                '}';
    }

    public CRRepAttachmentVO(String repSn, String userId, String requiredFile, String additionalFile) {
        this.repSn = repSn;
        this.userId = userId;
        this.requiredFile = requiredFile;
        this.additionalFile = additionalFile;
    }

    public String getRepSn() {
        return repSn;
    }

    public String getUserId() {
        return userId;
    }

    public String getRequiredFile() {
        return requiredFile;
    }

    public String getAdditionalFile() {
        return additionalFile;
    }

    public static final class Builder {
        private String repSn;
        private String userId;
        private String requiredFile;
        private String additionalFile;

        private Builder() {
        }

        public static Builder aCRRepAttachmentVO() {
            return new Builder();
        }

        public Builder repSn(String repSn) {
            this.repSn = repSn;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder requiredFile(String requiredFile) {
            this.requiredFile = requiredFile;
            return this;
        }

        public Builder additionalFile(String additionalFile) {
            this.additionalFile = additionalFile;
            return this;
        }

        public CRRepAttachmentVO build() {
            return new CRRepAttachmentVO(repSn, userId, requiredFile, additionalFile);
        }
    }
}
