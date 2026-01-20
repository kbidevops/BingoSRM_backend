package kr.go.smplatform.itsm.progrm.web;

import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.progrm.service.ProgrmService;
import kr.go.smplatform.itsm.progrm.vo.ProgrmVO;
import kr.go.smplatform.itsm.progrmaccesauthor.service.ProgrmAccesAuthorService;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;
import kr.go.smplatform.itsm.user.vo.UserVO;
import kr.go.smplatform.itsm.user.web.UserMngrController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProgrmController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserMngrController.class);
	
	@Resource(name = "progrmService")
	private ProgrmService progrmService;
	
	@Resource(name = "progrmAccesAuthorService")
	private ProgrmAccesAuthorService progrmAccesAuthorService;
	
	/**
	 * 메뉴 목록을 조회한다.
	 * @return "/user/mngr/egovSampleList"
	 * @exception Exception
	 */
    @RequestMapping(value="/progrm/mngr/retrieveTreeList.do")
    public String retrieveTreeList() {
        return "/itsm/progrm/mngr/treeList";
    }
    
    @RequestMapping(value="/progrm/mngr/retrieveTreeListAjax.do")
    public String retrieveTreeListAjax(ProgrmVO progrmVO, ModelMap model) {
    	try {
    		progrmVO.setDeleteYn("N");
			final List<ProgrmVO> list = progrmService.retrieveList(progrmVO);
			model.addAttribute("returnMessage", "success");
	        model.addAttribute("resultList", list);
    	}
		catch (Exception e) {
    		model.addAttribute("returnMessage", "fail");
    	}
        
        return "jsonView";
    }

    @RequestMapping(value="/progrm/mngr/createAjax.do")
    public String createAjax(ProgrmVO progrmVO, BindingResult bindingResult, HttpSession session, ModelMap model) {
    	// Server-Side Validation
    	beanValidator.validate(progrmVO, bindingResult);
    	
    	if (bindingResult.hasErrors()) {
    		model.addAttribute("returnMessage", "fail");
			return "jsonView";
    	}
    	
    	try {
    		// session에서 로그인 정보를 가져온다.
        	final UserVO loginUserVO =  (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        	progrmVO.setCreatId(loginUserVO.getUserId());
        	
    		final Long progrmSn = progrmService.create(progrmVO);
    		LOGGER.info("progrmSn: " + progrmSn);
			model.addAttribute("returnMessage", "success");
    	}
		catch (Exception e) {
    		model.addAttribute("returnMessage", "fail");
    	}
        
        return "jsonView";
    }
    
    @RequestMapping(value="/progrm/mngr/updateAjax.do")
    public String updateAjax(ProgrmVO progrmVO, BindingResult bindingResult, HttpSession session, ModelMap model) {
    	// Server-Side Validation
    	beanValidator.validate(progrmVO, bindingResult);
    	
    	if (bindingResult.hasErrors()) {
    		model.addAttribute("returnMessage", "fail");
			return "jsonView";
    	}
    	
    	try {
    		// session에서 로그인 정보를 가져온다.
        	final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        	progrmVO.setUpdtId(loginUserVO.getUserId());

    		final int cnt = progrmService.update(progrmVO);
    		LOGGER.info("update cnt: " + cnt);
			model.addAttribute("returnMessage", "success");
			
			// 전체 권한 메뉴 재설정 기능 추가 필요
			final ServletContext servletContext = session.getServletContext();
			// 공통코드 읽어서 처리하도록 구현
			final CmmnCodeVO paramCmmnCodeVO = new CmmnCodeVO(CmmnCodeVO.USER_TY_CODE);
			final List<CmmnCodeVO> cmmnCodeVOList = cmmnCodeService.retrieveList(paramCmmnCodeVO);

			for (CmmnCodeVO cmmnCodeVO : cmmnCodeVOList) {
				final ProgrmAccesAuthorVO paramProgrmAccesAuthorVO = new ProgrmAccesAuthorVO();
				paramProgrmAccesAuthorVO.setProgrmAccesAuthorCode(cmmnCodeVO.getCmmnCode());

				final List<ProgrmAccesAuthorVO> progrmAccesAuthorVOList = progrmAccesAuthorService.retrieveAssignList(paramProgrmAccesAuthorVO);
	   	        servletContext.removeAttribute(cmmnCodeVO.getCmmnCode());
				servletContext.setAttribute(cmmnCodeVO.getCmmnCode(), progrmAccesAuthorVOList);
			}
    	}
		catch (Exception e) {
    		model.addAttribute("returnMessage", "fail");
    	}
        
        return "jsonView";
    }
    
    @RequestMapping(value="/progrm/mngr/deleteAjax.do")
    public String deleteAjax(ProgrmVO progrmVO, HttpSession session, ModelMap model) {
    	try {
    		// session에서 로그인 정보를 가져온다.
        	final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
        	progrmVO.setUpdtId(loginUserVO.getUserId());

    		final int cnt = progrmService.delete(progrmVO);

    		LOGGER.info("update cnt: " + cnt);

			model.addAttribute("returnMessage", "success");
			
			// 전체 권한 메뉴 재설정 기능 추가 필요
			final ServletContext servletContext = session.getServletContext();

			// 공통코드 읽어서 처리하도록 구현
			final CmmnCodeVO paramCmmnCodeVO = new CmmnCodeVO(CmmnCodeVO.USER_TY_CODE);
			final List<CmmnCodeVO> cmmnCodeVOList = cmmnCodeService.retrieveList(paramCmmnCodeVO);
			for (CmmnCodeVO cmmnCodeVO : cmmnCodeVOList) {
				final ProgrmAccesAuthorVO paramProgrmAccesAuthorVO = new ProgrmAccesAuthorVO();
				paramProgrmAccesAuthorVO.setProgrmAccesAuthorCode(cmmnCodeVO.getCmmnCode());

				final List<ProgrmAccesAuthorVO> progrmAccesAuthorVOList = progrmAccesAuthorService.retrieveAssignList(paramProgrmAccesAuthorVO);
	   	        servletContext.removeAttribute(cmmnCodeVO.getCmmnCode());
				servletContext.setAttribute(cmmnCodeVO.getCmmnCode(), progrmAccesAuthorVOList);
			}			
    	}
		catch (Exception e) {
    		model.addAttribute("returnMessage", "fail");
    	}
        
        return "jsonView";
    }
    
    @RequestMapping(value="/progrm/mngr/menuSwitchOnAjax.do")
    public String menuSwitchOnAjax(HttpSession session, boolean isSwitchOn) {
    	session.setAttribute("isSwitchOn", isSwitchOn);
    	
    	LOGGER.debug("isSwitchOn : {}", session.getAttribute("isSwitchOn"));
    	
    	return "jsonView";
    }
}
