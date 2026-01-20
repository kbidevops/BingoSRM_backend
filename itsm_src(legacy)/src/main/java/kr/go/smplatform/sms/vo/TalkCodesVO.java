package kr.go.smplatform.sms.vo;

public class TalkCodesVO {
    private String uri;
    private String apiKey;
    private String userId;
    private String profileKey;
    private String templateCode;

    @Override
    public String toString() {
        return "TalkCodeVO{" +
                "uri='" + uri + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", userId='" + userId + '\'' +
                ", profileKey='" + profileKey + '\'' +
                ", templateCode='" + templateCode + '\'' +
                '}';
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
}
