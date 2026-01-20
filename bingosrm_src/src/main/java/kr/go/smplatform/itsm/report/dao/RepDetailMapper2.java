package kr.go.smplatform.itsm.report.dao;

import org.apache.ibatis.annotations.Mapper;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.report.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface RepDetailMapper2 {

    List<CmmnCodeVO> selectSolarList();

    List<CmmnCodeVO> selectLunarList();

    int selectLastRepSn(@Param("repTyCode") String repTyCode, @Param("reportDt") Date reportDt);

    String selectRepSnByCodeAndReportDt(@Param("repTyCode") String repTyCode, @Param("reportDt") Date reportDt);

    boolean selectIsConfirmed(String repSn);

    RepMasterVO2 selectMasterInfo(@Param("repSn") String repSn);

    List<RepAttitudeVO> selectAttitudeList(@Param("attitudeDt") Date attitudeDt, @Param("userId") String userId);

    List<RepDetailVO2> selectDetailList(@Param("userId") String userId, @Param("repSn") String repSn);

    CRRepAttachmentVO selectAttachment(@Param("repSn") String repSn, @Param("userId") String userId);

    List<RepAttachmentNameAndSizeVO> selectAttachmentNameAndSize(@Param("repSn") String repSn, @Param("userId") String userId);

    int insertOrUpdateAttachment(CRRepAttachmentVO vo);

    int removeAdditionalFile(@Param("repSn") String repSn, @Param("userId") String userId);

    List<RepAssignVO> selectAssignList(@Param("userId") String userId, @Param("repTyCode") String repTyCode);

    int insertMaster(CRepMasterVO masterVO);

    int deleteDetails(@Param("repSn") String repSn, @Param("userId") String userId);

    int insertDetail(CRepDetailVO detailVO);

    int insertAttitude(CRepAttitudeVO attitudeVO);
}
