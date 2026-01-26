package kr.go.smplatform.itsm.user.web;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.validator.GenericValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.hist.login.service.HistLoginService;
import kr.go.smplatform.itsm.hist.login.vo.HistLoginVO;
import kr.go.smplatform.itsm.progrmaccesauthor.service.ProgrmAccesAuthorService;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthApiController.class);

    private final UserService userService;
    private final HistLoginService histLoginService;
    private final ProgrmAccesAuthorService progrmAccesAuthorService;

    public AuthApiController(UserService userService, HistLoginService histLoginService,
                             ProgrmAccesAuthorService progrmAccesAuthorService) {
        this.userService = userService;
        this.histLoginService = histLoginService;
        this.progrmAccesAuthorService = progrmAccesAuthorService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserVO user, HttpServletRequest request)
            throws Exception {
        user.setConectIp(request.getRemoteAddr());
        user.setDeleteYn("N");

        UserVO existUserVO = userService.retrieve(user);
        user.encodeUserPassword();

        Map<String, Object> response = new LinkedHashMap<String, Object>();

        if (existUserVO != null
                && !GenericValidator.isBlankOrNull(existUserVO.getUserPassword())
                && !GenericValidator.isBlankOrNull(user.getUserPassword())
                && existUserVO.getUserPassword().equals(user.getUserPassword())) {
            userService.updatePasswordFailCntReset(user);

            try {
                HistLoginVO histLoginVO = new HistLoginVO();
                histLoginVO.setUserId(existUserVO.getUserId());
                histLoginVO.setUserTyCode(existUserVO.getUserTyCode());
                histLoginVO.setConectIp(user.getConectIp());
                histLoginVO.setSessionId(request.getSession(true).getId());
                histLoginService.create(histLoginVO);
            } catch (Exception e) {
                LOGGER.warn("Failed to record login history for userId={}", user.getUserId(), e);
            }

            response.put("authenticated", true);
            response.put("userTyCode", existUserVO.getUserTyCode());
            response.put("userSttusCode", existUserVO.getUserSttusCode());

            if (UserVO.USER_STTUS_CODE_WAIT.equals(existUserVO.getUserSttusCode())) {
                response.put("status", "WAIT");
            } else if (UserVO.USER_STTUS_CODE_STOP.equals(existUserVO.getUserSttusCode())) {
                response.put("status", "STOP");
            } else {
                response.put("status", "OK");
            }

            UserVO overPasswordUserVO = userService.retrieveOverPasswordPeriod(user);
            response.put("passwordExpired", overPasswordUserVO != null);
            response.put("userId", existUserVO.getUserId());

            ProgrmAccesAuthorVO accessParam = new ProgrmAccesAuthorVO();
            accessParam.setProgrmAccesAuthorCode(existUserVO.getUserTyCode());
            response.put("assigned", progrmAccesAuthorService.retrieveAssignList(accessParam));

            return ResponseEntity.ok(response);
        }

        userService.updatePasswordFailCntAdd(user);
        int overCnt = userService.updatePasswordFailCntOverProcess(user);

        response.put("authenticated", false);
        response.put("locked", overCnt > 0);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
