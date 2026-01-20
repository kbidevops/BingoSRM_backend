package kr.go.smplatform.itsm.repcharger.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.go.smplatform.itsm.repcharger.service.RepChargerService;
import kr.go.smplatform.itsm.repcharger.vo.RepChargerVO;
import kr.go.smplatform.itsm.report.web.RepDetailController;
import kr.go.smplatform.itsm.syscharger.vo.SysChargerVO;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;

@Controller
public class RepChargerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RepDetailController.class);
	
	@Resource(name = "repChargerService")
	private RepChargerService repChargerService;
	
	/**
	 * 담당자 목록 조회
	 * @param repChargerVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/repcharger/mngr/retrieveList.do")
    public String retrievePagingList(RepChargerVO repChargerVO, ModelMap model) throws Exception {
		repChargerVO.setUserTyCode(UserVO.USER_TY_CODE_CHARGER); // 시스템 담당자만 불러오기 위해
		repChargerVO.setUserSttusCode(UserVO.USER_STTUS_CODE_ALLOW);
		
		final List<RepChargerVO> list = repChargerService.retrieveUsers(repChargerVO);
		final List<RepChargerVO> repChargerVOList = repChargerService.retrieveAssignList(repChargerVO);
		
		LOGGER.info("{}", repChargerVOList.size());
		
		for (RepChargerVO chargerVO : list) {
			for (RepChargerVO inRepChargerVO : repChargerVOList) {
				if (chargerVO.getUserId().equals(inRepChargerVO.getUserId())) {
					chargerVO.getRepChargerVOList().add(inRepChargerVO);
				}
			}
		}
		
        model.addAttribute("resultList", list);        
        
        return "/itsm/repcharger/mngr/list";
    }
	
	/**
	 * 특정 담당자의 담당 목록 조회
	 * @param repChargerVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/repcharger/mngr/retrieveRepChargerListAjax.do")
    public String retrieveSysChargerListAjax(RepChargerVO repChargerVO, ModelMap model) throws Exception {
    	if (!GenericValidator.isBlankOrNull(repChargerVO.getUserId())) {
    		LOGGER.debug("sysChargerVO.getUserId(): "+repChargerVO.getUserId());

			final List<RepChargerVO> resultList = repChargerService.retrieveList(repChargerVO);
			model.addAttribute("returnMessage", "success");
			model.addAttribute("resultList", resultList);
    	}
		else {
    		model.addAttribute("returnMessage", "fail");
    	}

        return "jsonView";
    }    
	
	/**
	 * 담당자 생성
	 * @param repChargerVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/repcharger/mngr/createListAjax.do")       
    public String createListAjax(RepChargerVO repChargerVO) throws Exception {
    	repChargerService.create(repChargerVO);
    	
        return "jsonView";
    }
}
