package kr.go.smplatform.itsm.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.user.vo.UserVO;

@Mapper
public interface UserMapper {
    void create(UserVO vo) throws Exception;
    int update(UserVO vo) throws Exception;
    int deleteSignature(String userId) throws Exception;
    int delete(UserVO vo) throws Exception;
    UserVO retrieve(UserVO vo) throws Exception;
    List<UserVO> retrievePagingList(UserVO vo) throws Exception;
    int retrievePagingListCnt(UserVO vo) throws Exception;
    List<UserVO> retrieveList(UserVO vo) throws Exception;
    int updatePasswordFailCntAdd(UserVO vo) throws Exception;
    int updatePasswordFailCntReset(UserVO vo) throws Exception;
    int updatePasswordFailCntOverProcess(UserVO vo) throws Exception;
    UserVO retrieveOverPasswordPeriod(UserVO vo) throws Exception;
}
