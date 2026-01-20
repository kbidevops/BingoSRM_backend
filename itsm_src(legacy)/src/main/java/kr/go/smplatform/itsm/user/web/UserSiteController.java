package kr.go.smplatform.itsm.user.web;

import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserFormVO;
import kr.go.smplatform.itsm.user.vo.UserVO;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class UserSiteController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserMngrController.class);
	
	@Resource(name = "userService")
	private UserService userService;
	
	/**
	 * 글 등록 화면을 조회한다.
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @return "/user/site/egovSampleRegister"
	 * @exception Exception
	 */
    @RequestMapping("/user/site/createView.do")
    public String createView(UserFormVO userFormVO, HttpSession session) throws Exception {
    	userFormVO.setUserVO(new UserVO());
//    	retrieveCmmnCode(model);
//    	userFormVO.getUserVO().setUserTyCode(UserVO.USER_TY_CODE_NORMAL);
    	
    	// 중복등록 방지 처리
    	final String saveToken = ITSMDefine.generateSaveToken(session);
    	userFormVO.getUserVO().setSaveToken(saveToken);

        return "/itsm/user/site/edit";
    }
    
    /**
	 * 글을 등록한다.
	 * @param userVO - 등록할 정보가 담긴 VO
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @return "forward:/user/site/retrievePagingList.do"
	 * @exception Exception
	 */
    @RequestMapping("/user/site/create.do")
    public String create(HttpSession session, UserFormVO userFormVO, BindingResult bindingResult) throws Exception {
    	// Server-Side Validation
    	beanValidator.validate(userFormVO, bindingResult);
    	
    	if (bindingResult.hasErrors()) {
//    		model.addAttribute("userVO", userFormVO.getUserVO());
			return "/itsm/user/site/edit";
    	}
    	
    	// 중복등록 방지 처리
    	if (!ITSMDefine.checkSaveToken(session, userFormVO.getUserVO().getSaveToken())) {
    		return "forward:/user/site/updateView.do";
    	}

    	userFormVO.getUserVO().setCreatId(userFormVO.getUserVO().getUserId());
    	
    	userFormVO.getUserVO().setUserSttusCode(UserVO.USER_STTUS_CODE_WAIT); // 승인요청/승인대기
    	userFormVO.getUserVO().setUserTyCode(UserVO.USER_TY_CODE_TEMP); // 임시사용자로 설정
    	
    	if (!userService.create(userFormVO.getUserVO())) {
    		session.setAttribute(ITSMDefine.ERROR_MESSAGE, "이미 등록된 아이디 입니다.");
    	}
		else {
    		session.setAttribute(ITSMDefine.ERROR_MESSAGE, "회원가입이 정상적으로 처리되었습니다.");
    	}
    	
        return "forward:/user/site/updateView.do";
    }
    
    /**
	 * 글 수정화면을 조회한다.
	 * @param id - 수정할 글 id
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "/user/site/egovSampleRegister"
	 * @exception Exception
	 */
    @RequestMapping("/user/site/updateView.do")
    public String updateView(UserFormVO userFormVO, HttpSession session) throws Exception {
    	if (GenericValidator.isBlankOrNull(userFormVO.getUserVO().getUserId())) {
    		// 로그인 후 미승인 사용자인경우 id가 없어서 session으로 부터 가져올 것
    		final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
    		userFormVO.setUserVO(loginUserVO);
    	}
    	
    	userFormVO.setUserVO(userService.retrieve(userFormVO.getUserVO()));
//        retrieveCmmnCode(model);

        return "/itsm/user/site/edit";
    }

    /**
	 * 글을 수정한다.
	 * @param userVO - 수정할 정보가 담긴 VO
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @return "forward:/user/site/retrievePagingList.do"
	 * @exception Exception
	 */
    @RequestMapping("/user/site/update.do")
    public String update(HttpSession session, UserFormVO userFormVO, BindingResult bindingResult) throws Exception {
    	LOGGER.debug("userVO: " + userFormVO.getUserVO());
    	beanValidator.validate(userFormVO, bindingResult);
    	
    	if (bindingResult.hasErrors()) {
    		userFormVO.setUserVO(userFormVO.getUserVO());

			return "/itsm/user/site/edit";
    	}
    	
    	final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
    	
    	userFormVO.getUserVO().setUpdtId(loginUserVO.getUserId());

    	// 사용자는 권한과 상태 변경이 불가능 하도록 처리
    	final UserVO existUserVO = userService.retrieve(userFormVO.getUserVO());
    	userFormVO.getUserVO().setUserTyCode(existUserVO.getUserTyCode());
    	userFormVO.getUserVO().setUserSttusCode(existUserVO.getUserSttusCode());
    	
        userService.update(userFormVO.getUserVO());
        
        return "forward:/user/site/updateView.do";
    }

	/**
	 * AJAX를 통해 사용중인 ID인지 검사 한다.
	 * @param userVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/user/site/retrieveAjax.do")    
    public String retrieveAjax(UserVO userVO, ModelMap model) throws Exception {
    	final UserVO existUserVO = userService.retrieve(userVO);
    	
    	// 자료실은 특별히 다운로드 제약이 없으므로 파일에 대한 접근 권한은 체크 하지 않는다.
    	if (existUserVO == null) {
	    	model.addAttribute("returnMessage", "success");
		}
		else {
			model.addAttribute("returnMessage", "fail");
		}
    	
    	return "jsonView";
	}
}
