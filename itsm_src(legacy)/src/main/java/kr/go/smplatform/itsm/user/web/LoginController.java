package kr.go.smplatform.itsm.user.web;

import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.hist.login.service.HistLoginService;
import kr.go.smplatform.itsm.hist.login.vo.HistLoginVO;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserMngrController.class);
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "histLoginService")
	private HistLoginService histLoginService;

	/**
	 * 로그인 화면을 조회한다.
	 * @return
	 */
    @RequestMapping("/login/site/loginView.do")
    public String loginView(UserVO userVO) {
        //return "/itsm/user/site/login";
        return "/itsm/user/site/loginkr";
    }
    
    /**
	 * 로그인 화면을 조회한다.
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "/user/mngr/egovSampleRegister"
	 * @exception Exception
	 */
    @RequestMapping("/login/site/login.do")
    public String login(UserVO userVO, BindingResult bindingResult, HttpSession session, HttpServletRequest request) throws Exception {
    	// Server-Side Validation
 		beanValidator.validate(userVO, bindingResult);
    	 		
 		if (bindingResult.hasErrors()) {
 			return "/itsm/user/site/loginkr";
 		}
 		
 		userVO.setConectIp(request.getRemoteAddr());
 		userVO.setDeleteYn("N");
    	UserVO existUserVO = userService.retrieve(userVO);
    	userVO.encodeUserPassword(); // 비밀번호 암호화 처리
    	
    	if (existUserVO != null
    			&& !GenericValidator.isBlankOrNull(existUserVO.getUserPassword())
    			&& !GenericValidator.isBlankOrNull(userVO.getUserPassword())
    			&& existUserVO.getUserPassword().equals(userVO.getUserPassword())
		) {
    		userService.updatePasswordFailCntReset(userVO);

        	try {
        		// 로그인 기록
            	final HistLoginVO histLoginVO = new HistLoginVO();
    	    	BeanUtils.copyProperties(userVO, histLoginVO);
    	    	histLoginVO.setSessionId(session.getId());
    	    	histLoginService.create(histLoginVO);
        	}
			catch (Exception e) {
        		LOGGER.error("로그인이력 등록중 에러 발생: "+userVO.getUserId());
        	}
        	
        	session.removeAttribute(UserVO.LOGIN_USER_VO);
        	session.setAttribute(UserVO.LOGIN_USER_VO, existUserVO);
        	LOGGER.debug("exsitUserVO: "+existUserVO);
	    	
        	if (UserVO.USER_STTUS_CODE_WAIT.equals(existUserVO.getUserSttusCode())) {
        		// 사용자 승인대기시 수정화면으로 이동
        		return "redirect:/user/site/updateView.do";
        	}
			else if (UserVO.USER_STTUS_CODE_STOP.equals(existUserVO.getUserSttusCode())) {
        		session.removeAttribute(UserVO.LOGIN_USER_VO);
        		session.setAttribute(ITSMDefine.ERROR_MESSAGE, "사용중지된 계정입니다.");
        	}
        	
        	// 비밀번호 갱신기간 확인
        	final UserVO overPasswordUserVO = userService.retrieveOverPasswordPeriod(userVO);
        	if (overPasswordUserVO != null) {
        		session.setAttribute(ITSMDefine.ERROR_MESSAGE, "비밀번호를 설정하신지 3개월이 초과되었습니다.\\n비밀번호는 비밀번호 안전도에 따라 3개월에 \\n한 번씩 주기적으로 바꾸어 사용하시는 것이 안전합니다.");

        		return "redirect:/user/site/updateView.do";
        	}
	    	
        	return "redirect:/main/index.do";
    	}
		else {
    		userService.updatePasswordFailCntAdd(userVO);
    		final int overCnt = userService.updatePasswordFailCntOverProcess(userVO);
    		
    		if (overCnt > 0) {
        		// 시간나면 5회이상 아이디 불일치시 처리를 위한 로직 추가
    			session.setAttribute(ITSMDefine.ERROR_MESSAGE, "5회이상 비밀번호 불일치로 인해 계정이 잠깁니다. \\n 관라자에게 문의하세요.");
    		}
			else {
    			session.setAttribute(ITSMDefine.ERROR_MESSAGE, "아이디 또는 비밀번호가 불일치 합니다.");
    		}
    	}
    	
    	return "/itsm/user/site/loginkr";
    }
    
    /**
     * 로그인 화면을 조회한다.
	 * @param session
	 * @return "/user/loginConservator/edit"
	 * @exception Exception
     */
    @RequestMapping(value = "/login/site/loginOut.do")    
    public String loginOut(HttpSession session) throws Exception {
    	final UserVO loginUserVO = (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
    	session.removeAttribute(UserVO.LOGIN_USER_VO);  
    	
    	if (loginUserVO != null) {
    		final HistLoginVO histLoginVO = new HistLoginVO();
        	BeanUtils.copyProperties(loginUserVO, histLoginVO);
        	histLoginVO.setLogoutSttusCode(HistLoginVO.LOGOUT_STTUS_CODE_NORMAL);

        	final int cnt = histLoginService.update(histLoginVO);
        	LOGGER.debug("logout cnt: " + cnt);
    	}
    	
    	return "redirect:/login/site/loginView.do";
//    	return "forward:/itsm/main/index.do";
	}
}
