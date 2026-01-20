package kr.go.smplatform.sms.vo;

public class TalkVO {
    private final String uri;
    private final String apiKey;
    private final String userId;
    private final String profileKey;
    private final String templateCode;
    private final String receiver;
    private final String message;

    @Override
    public String toString() {
        return "TalkVO{" +
                "uri='" + uri + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", userId='" + userId + '\'' +
                ", profileKey='" + profileKey + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public TalkVO(String uri, String apiKey, String userId, String profileKey, String templateCode, String receiver, String message) {
        this.uri = uri;
        this.apiKey = apiKey;
        this.userId = userId;
        this.profileKey = profileKey;
        this.templateCode = templateCode;
        this.receiver = receiver;
        this.message = message;
    }

    public String getUri() {
        return uri;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public static final class Builder {
        private String uri;
        private String apiKey;
        private String userId;
        private String profileKey;
        private String templateCode;
        private String receiver;
        private String message;

        private Builder() {
        }

        public static Builder aTalkVO() {
            return new Builder();
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder profileKey(String profileKey) {
            this.profileKey = profileKey;
            return this;
        }

        public Builder templateCode(String templateCode) {
            this.templateCode = templateCode;
            return this;
        }

        public Builder receiver(String receiver) {
            this.receiver = receiver;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public TalkVO build() {
            return new TalkVO(uri, apiKey, userId, profileKey, templateCode, receiver, message);
        }
    }
}
