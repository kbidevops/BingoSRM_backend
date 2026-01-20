package kr.go.smplatform.itsm.hist.use.vo;

import java.util.Date;

public class HistUseVO {
	private long logSn;
	private String sessionId;
	private String userId;
	private String userTyCode;
	
	private Date useDt;	
	private String requestUri;
	private String requestMethod;
	private String title;
	private String menuTitle;
	private String subMenuTitle;
	
	public long getLogSn() {
		return logSn;
	}
	public void setLogSn(long logSn) {
		this.logSn = logSn;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserTyCode() {
		return userTyCode;
	}
	public void setUserTyCode(String userTyCode) {
		this.userTyCode = userTyCode;
	}
	public Date getUseDt() {
		return useDt;
	}
	public void setUseDt(Date useDt) {
		this.useDt = useDt;
	}
	public String getRequestUri() {
		return requestUri;
	}
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMenuTitle() {
		return menuTitle;
	}
	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}
	public String getSubMenuTitle() {
		return subMenuTitle;
	}
	public void setSubMenuTitle(String subMenuTitle) {
		this.subMenuTitle = subMenuTitle;
	}
	
	
	
}
