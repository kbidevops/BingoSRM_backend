package kr.go.smplatform.itsm.main.web;

import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.user.vo.UserVO;
import kr.go.smplatform.itsm.user.web.UserMngrController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MainIndexController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMngrController.class);

    /**
     * 로그인 화면을 조회한다.
     * @param session
     * @return
     */
    @RequestMapping("/main/index.do")
    public String loginView(HttpSession session) {
        LOGGER.info("메인페이지");
        
        // 사용자 정보에 따른 화면 분기
        final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);

        if (loginUserVO == null
                || UserVO.USER_TY_CODE_TEMP.equals(loginUserVO.getUserTyCode())) { // 임시사용자
            if (!UserVO.TEMP_USER.equals(loginUserVO.getUserId())) {
                session.setAttribute(ITSMDefine.ERROR_MESSAGE, "권한 미배정 상태입니다. \\n 관리자에게 문의하세요.");
                return "redirect:/user/site/updateView.do";
            }

            return "redirect:/login/site/loginView.do";
        }
        // 시스템관리자
        else if (UserVO.USER_TY_CODE_MNGR.equals(loginUserVO.getUserTyCode())) {
            return "redirect:/user/mngr/retrievePagingList.do";
        }
        // 시스템담당자, 기관 담당자
        else if (UserVO.USER_TY_CODE_CHARGER.equals(loginUserVO.getUserTyCode()) || UserVO.USER_TY_CODE_CSTMR.equals(loginUserVO.getUserTyCode())) {
//            return "redirect:/itsm/srvcrspons/mngr/retrieveList.do";
        	return "redirect:/srvcrspons/site/retrieveSrRcvList.do";
        }
        // 상담센터, 협단체
        else if (UserVO.USER_TY_CODE_CNSLT.equals(loginUserVO.getUserTyCode())
                || UserVO.USER_TY_CODE_R005.equals(loginUserVO.getUserTyCode())) {
//            return "redirect:/itsm/srvcrspons/site/retrievePagingList.do";
            return "redirect:/srvcrspons/site/retrieveSrReqList.do";
        }

        return "redirect:/login/site/loginView.do";
    }
}
