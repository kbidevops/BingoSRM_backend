package kr.go.smplatform.itsm.progrmaccesauthor.web;

import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.progrmaccesauthor.service.ProgrmAccesAuthorService;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProgrmAccesAuthorController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgrmAccesAuthorController.class);
	
	@Resource(name = "progrmAccesAuthorService")
	private ProgrmAccesAuthorService progrmAccesAuthorService;

	private void retrieveCmmnCode(ModelMap model) throws Exception {
		CmmnCodeVO cmmnCodeVO = new CmmnCodeVO();
        cmmnCodeVO.setCmmnCodeTy(CmmnCodeVO.USER_TY_CODE);
        model.addAttribute("progrmAccesAuthorCodeVOList", cmmnCodeService.retrieveList(cmmnCodeVO));
	} 
	
	/**
	 * 메뉴권한 목록을 조회한다.
	 * @param progrmAccesAuthorFormVO.getSearchProgrmVO() - 조회할 정보가 담긴 ProgrmVO
	 * @param model
	 * @return "/user/mngr/egovSampleList"
	 * @exception Exception
	 */
    @RequestMapping(value="/progrmaccesauthor/mngr/retrieveTreeList.do")
    public String retrieveTreeList(ProgrmAccesAuthorVO progrmAccesAuthorVO, ModelMap model) throws Exception {
    	retrieveCmmnCode(model);
    	
    	if (GenericValidator.isBlankOrNull(progrmAccesAuthorVO.getProgrmAccesAuthorCode())) {
    		final List<CmmnCodeVO> progrmAccesAuthorCodeVOList = (List<CmmnCodeVO>)model.get("progrmAccesAuthorCodeVOList");

			if (progrmAccesAuthorCodeVOList != null && progrmAccesAuthorCodeVOList.size() > 0) {
    			progrmAccesAuthorVO.setProgrmAccesAuthorCode(progrmAccesAuthorCodeVOList.get(0).getCmmnCode());
    		}
    	}

    	LOGGER.debug("ProgrmAccesAuthorCode: "+progrmAccesAuthorVO.getProgrmAccesAuthorCode());
    	
        final List<ProgrmAccesAuthorVO> resultList = progrmAccesAuthorService.retrieveList(progrmAccesAuthorVO);
        model.addAttribute("resultList", resultList);
        
        return "/itsm/progrmaccesauthor/mngr/treeList";
    }
    
    /**
	 * 메뉴권한 목록을 조회한다.
	 * @param progrmAccesAuthorFormVO.getSearchProgrmVO() - 조회할 정보가 담긴 ProgrmVO
	 * @param model
	 * @return "/user/mngr/egovSampleList"
	 * @exception Exception
	 */
    @RequestMapping(value="/progrmaccesauthor/mngr/retrieveTreeListAjax.do")
    public String retrieveTreeListAjax(ProgrmAccesAuthorVO progrmAccesAuthorVO, ModelMap model) {
    	if (GenericValidator.isBlankOrNull(progrmAccesAuthorVO.getProgrmAccesAuthorCode())) {
    		final List<CmmnCodeVO> progrmAccesAuthorCodeVOList = (List<CmmnCodeVO>)model.get("progrmAccesAuthorCodeVOList");

			if (progrmAccesAuthorCodeVOList != null && progrmAccesAuthorCodeVOList.size() > 0) {
    			progrmAccesAuthorVO.setProgrmAccesAuthorCode(progrmAccesAuthorCodeVOList.get(0).getCmmnCode());
    		}
    	}

    	LOGGER.debug("ProgrmAccesAuthorCode: "+progrmAccesAuthorVO.getProgrmAccesAuthorCode());

    	try {
	        final List<ProgrmAccesAuthorVO> resultList = progrmAccesAuthorService.retrieveList(progrmAccesAuthorVO);
	        model.addAttribute("returnMessage", "success");
	        model.addAttribute("resultList", resultList);
    	}
		catch (Exception e) {
    		model.addAttribute("returnMessage", "fail");
    	}

        return "jsonView";
    }
    
    /**
   	 * 메뉴권한 목록을 조회한다.
   	 * @param progrmAccesAuthorFormVO.getSearchProgrmVO() - 조회할 정보가 담긴 ProgrmVO
   	 * @param model
   	 * @return "/user/mngr/egovSampleList"
   	 * @exception Exception
   	 */
	@RequestMapping(value="/progrmaccesauthor/mngr/createListAjax.do")
	public String createListAjax(ProgrmAccesAuthorVO progrmAccesAuthorVO, HttpServletRequest request, ModelMap model) {
		try {
			progrmAccesAuthorService.createList(progrmAccesAuthorVO);
   	        final ServletContext servletContext = request.getServletContext();
   	        
   	        // 권한 메뉴 변경시 처리
   	        final List<ProgrmAccesAuthorVO> progrmAccesAuthorVOList = progrmAccesAuthorService.retrieveList(progrmAccesAuthorVO);
   	        servletContext.removeAttribute(progrmAccesAuthorVO.getProgrmAccesAuthorCode());
			servletContext.setAttribute(progrmAccesAuthorVO.getProgrmAccesAuthorCode(), progrmAccesAuthorVOList);
			
   	        model.addAttribute("returnMessage", "success");
       	}
		catch (Exception e) {
       		model.addAttribute("returnMessage", "fail");
       	}

		return "jsonView";
	}
}
