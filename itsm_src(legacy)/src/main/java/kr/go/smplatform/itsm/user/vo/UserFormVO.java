package kr.go.smplatform.itsm.user.vo;

public class UserFormVO {
	private UserVO searchUserVO;
	private UserVO userVO;
	
	public UserFormVO(){
		searchUserVO = new UserVO();
		userVO = new UserVO();
	}

	public UserVO getSearchUserVO() {
		return searchUserVO;
	}

	public void setSearchUserVO(UserVO searchUserVO) {
		this.searchUserVO = searchUserVO;
	}

	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}
}
