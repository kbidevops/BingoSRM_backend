package kr.go.smplatform.itsm.config.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.go.smplatform.itsm.config.vo.ResponseRequiredVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper("configMapper")
public interface ConfigMapper {

    List<ResponseRequiredVO> selectResponseRequired();

    int updateChkY(@Param("userId") String userId);
}
