package kr.go.smplatform.itsm.user.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;

@RestController
@RequestMapping("/api/v1/users")
public class UserApiController {
    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Map<String, Object> list(UserVO search) throws Exception {
        int pageIndex = search.getPageIndex() > 0 ? search.getPageIndex() : 1;
        int recordCount = search.getRecordCountPerPage() > 0 ? search.getRecordCountPerPage() : 15;
        int firstIndex = (pageIndex - 1) * recordCount;

        search.setPageIndex(pageIndex);
        search.setRecordCountPerPage(recordCount);
        search.setFirstIndex(firstIndex);

        List<UserVO> list = userService.retrievePagingList(search);
        int totalCount = userService.retrievePagingListCnt(search);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("pageIndex", pageIndex);
        pagination.put("recordCountPerPage", recordCount);
        pagination.put("totalCount", totalCount);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("resultList", list);
        response.put("pagination", pagination);
        return response;
    }

    @GetMapping("/{userId}")
    public UserVO get(@PathVariable("userId") String userId) throws Exception {
        UserVO param = new UserVO();
        param.setUserId(userId);
        return userService.retrieve(param);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody UserVO user,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        if (actorUserId != null && !actorUserId.isEmpty()) {
            user.setCreatId(actorUserId);
        }

        boolean created = userService.create(user);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("created", created);

        if (!created) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(
            @PathVariable("userId") String userId,
            @RequestBody UserVO user,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        user.setUserId(userId);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            user.setUpdtId(actorUserId);
        }
        userService.update(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable("userId") String userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorUserId) throws Exception {
        UserVO user = new UserVO();
        user.setUserId(userId);
        user.setUserSttusCode(UserVO.USER_STTUS_CODE_STOP);
        if (actorUserId != null && !actorUserId.isEmpty()) {
            user.setUpdtId(actorUserId);
        }
        userService.delete(user);
        return ResponseEntity.noContent().build();
    }
}
