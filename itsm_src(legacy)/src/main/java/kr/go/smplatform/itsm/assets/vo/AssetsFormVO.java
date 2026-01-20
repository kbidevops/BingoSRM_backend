package kr.go.smplatform.itsm.assets.vo;

public class AssetsFormVO {
	
	private AssetsVO assetsVO;
	private AssetsVO searchAssetsVO;
	
	public AssetsFormVO() {
		assetsVO = new AssetsVO();
		searchAssetsVO = new AssetsVO();
	}
	
	public AssetsVO getAssetsVO() {
		return assetsVO;
	}

	public void setAssetsVO(AssetsVO assetsVO) {
		this.assetsVO = assetsVO;
	}

	public AssetsVO getSearchAssetsVO() {
		return searchAssetsVO;
	}

	public void setSearchAssetsVO(AssetsVO searchAssetsVO) {
		this.searchAssetsVO = searchAssetsVO;
	}
	
	
}
