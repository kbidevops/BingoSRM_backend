package kr.go.smplatform.itsm.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.go.smplatform.itsm.user.dao.UserMapper;
import kr.go.smplatform.itsm.user.vo.UserVO;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public boolean create(UserVO vo) throws Exception {
        boolean isSave = false;
        vo.encodeUserPassword();

        if (userMapper.retrieve(vo) == null) {
            userMapper.create(vo);
            isSave = true;
        }

        return isSave;
    }

    public int update(UserVO vo) throws Exception {
        vo.encodeUserPassword();
        return userMapper.update(vo);
    }

    public int deleteSignature(String userId) throws Exception {
        return userMapper.deleteSignature(userId);
    }

    public int delete(UserVO vo) throws Exception {
        return userMapper.delete(vo);
    }

    public UserVO retrieve(UserVO vo) throws Exception {
        return userMapper.retrieve(vo);
    }

    public List<UserVO> retrievePagingList(UserVO vo) throws Exception {
        return userMapper.retrievePagingList(vo);
    }

    public int retrievePagingListCnt(UserVO vo) throws Exception {
        return userMapper.retrievePagingListCnt(vo);
    }

    public List<UserVO> retrieveList(UserVO vo) throws Exception {
        return userMapper.retrieveList(vo);
    }

    public int updatePasswordFailCntAdd(UserVO vo) throws Exception {
        return userMapper.updatePasswordFailCntAdd(vo);
    }

    public int updatePasswordFailCntReset(UserVO vo) throws Exception {
        return userMapper.updatePasswordFailCntReset(vo);
    }

    public int updatePasswordFailCntOverProcess(UserVO vo) throws Exception {
        return userMapper.updatePasswordFailCntOverProcess(vo);
    }

    public UserVO retrieveOverPasswordPeriod(UserVO vo) throws Exception {
        return userMapper.retrieveOverPasswordPeriod(vo);
    }
}
