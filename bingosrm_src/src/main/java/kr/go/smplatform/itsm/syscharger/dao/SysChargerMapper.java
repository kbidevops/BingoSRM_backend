package kr.go.smplatform.itsm.syscharger.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.go.smplatform.itsm.syscharger.vo.SysChargerVO;

@Mapper
public interface SysChargerMapper {
    Long create(SysChargerVO vo) throws Exception;
    int deleteList(SysChargerVO vo) throws Exception;
    List<SysChargerVO> retrieveList(SysChargerVO vo) throws Exception;
    List<SysChargerVO> retrieveAssignList(SysChargerVO vo) throws Exception;
    List<SysChargerVO> retrieveChargerList(SysChargerVO vo) throws Exception;
}
