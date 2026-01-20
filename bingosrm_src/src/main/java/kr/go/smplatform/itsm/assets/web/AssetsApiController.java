package kr.go.smplatform.itsm.assets.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.assets.service.AssetsService;
import kr.go.smplatform.itsm.assets.vo.AssetsVO;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetsApiController {
    private final AssetsService assetsService;

    public AssetsApiController(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @GetMapping
    public Map<String, Object> retrievePagingList(AssetsVO search) throws Exception {
        if (search.getAssetsSe1() == null) {
            search.setAssetsSe1(AssetsVO.ASSETS_SE_HW);
        }

        int pageIndex = search.getPageIndex() > 0 ? search.getPageIndex() : 1;
        int recordCount = search.getRecordCountPerPage() > 0 ? search.getRecordCountPerPage() : 15;
        int firstIndex = (pageIndex - 1) * recordCount;

        search.setPageIndex(pageIndex);
        search.setRecordCountPerPage(recordCount);
        search.setFirstIndex(firstIndex);

        int totalCount = assetsService.retrieveListCnt(search);
        List<AssetsVO> list = assetsService.retrievePagingList(search);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("pageIndex", pageIndex);
        pagination.put("recordCountPerPage", recordCount);
        pagination.put("totalCount", totalCount);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        response.put("pagination", pagination);
        return response;
    }
}
