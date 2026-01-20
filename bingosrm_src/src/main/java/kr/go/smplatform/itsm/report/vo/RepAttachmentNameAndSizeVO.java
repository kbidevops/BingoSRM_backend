package kr.go.smplatform.itsm.report.vo;

public class RepAttachmentNameAndSizeVO {
    private String requiredFileId;
    private String requiredFileName;
    private String requiredFileSize;
    private String additionalFileId;
    private String additionalFileName;
    private String additionalFileSize;

    private String repSn;
    private String userId;

    @Override
    public String toString() {
        return "RepAttachmentNameAndSizeVO{" +
                "requiredFileId='" + requiredFileId + '\'' +
                ", requiredFileName='" + requiredFileName + '\'' +
                ", requiredFileSize='" + requiredFileSize + '\'' +
                ", additionalFileId='" + additionalFileId + '\'' +
                ", additionalFileName='" + additionalFileName + '\'' +
                ", additionalFileSize='" + additionalFileSize + '\'' +
                ", repSn='" + repSn + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getRequiredFileId() {
        return requiredFileId;
    }

    public void setRequiredFileId(String requiredFileId) {
        this.requiredFileId = requiredFileId;
    }

    public String getRequiredFileName() {
        return requiredFileName;
    }

    public void setRequiredFileName(String requiredFileName) {
        this.requiredFileName = requiredFileName;
    }

    public String getRequiredFileSize() {
        return requiredFileSize;
    }

    public void setRequiredFileSize(String requiredFileSize) {
        this.requiredFileSize = requiredFileSize;
    }

    public String getAdditionalFileId() {
        return additionalFileId;
    }

    public void setAdditionalFileId(String additionalFileId) {
        this.additionalFileId = additionalFileId;
    }

    public String getAdditionalFileName() {
        return additionalFileName;
    }

    public void setAdditionalFileName(String additionalFileName) {
        this.additionalFileName = additionalFileName;
    }

    public String getAdditionalFileSize() {
        return additionalFileSize;
    }

    public void setAdditionalFileSize(String additionalFileSize) {
        this.additionalFileSize = additionalFileSize;
    }

    public String getRepSn() {
        return repSn;
    }

    public void setRepSn(String repSn) {
        this.repSn = repSn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
