package kr.go.smplatform.itsm.repcharger.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.repcharger.vo.RepChargerVO;
import kr.go.smplatform.itsm.user.vo.UserVO;

@Mapper
public interface RepChargerMapper {
    List<RepChargerVO> retrieveList(RepChargerVO vo) throws Exception;
    List<RepChargerVO> retrieveAssignList(RepChargerVO vo) throws Exception;
    List<RepChargerVO> retrieveUsers(RepChargerVO vo) throws Exception;
    int delete(RepChargerVO vo) throws Exception;
    void create(RepChargerVO vo) throws Exception;
    int update(RepChargerVO vo) throws Exception;
    int updateUserLocat(UserVO userVO);
}
