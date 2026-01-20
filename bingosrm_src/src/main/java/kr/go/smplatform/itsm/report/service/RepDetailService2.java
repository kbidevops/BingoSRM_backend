package kr.go.smplatform.itsm.report.service;

import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.report.dao.RepDetailMapper2;
import kr.go.smplatform.itsm.report.vo.*;
import kr.go.smplatform.itsm.util.DateUtil;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("repDetailService2")
public class RepDetailService2 {

    private final RepDetailMapper2 repDetailMapper2;

    public RepDetailService2(RepDetailMapper2 repDetailMapper2) {
        this.repDetailMapper2 = repDetailMapper2;
    }

    /**
     * 일일보고 - 이전 보고일자 구하기
     * @param reportDt
     * @return
     * @throws ParseException
     */
    public Date getLastWeekday(Date reportDt) throws ParseException {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(reportDt);
        cal.add(Calendar.DATE, -1);

        long date = DateUtil.dateToLong(cal.getTime());

        final List<CmmnCodeVO> solar = repDetailMapper2.selectSolarList();
        final List<CmmnCodeVO> lunar = repDetailMapper2.selectLunarList();

        while (DateUtil.isHoliday(date, solar, lunar)) {
            cal.add(Calendar.DATE, -1);
            date = DateUtil.dateToLong(cal.getTime());
        }

        return cal.getTime();
    }

    /**
     * 보고서 별 마스터 보고일자 구하기
     * @param reportDate
     * @param repTyCode
     * @return
     * @throws Exception
     */
    public Date getNextWeekday(Date reportDate, String repTyCode) throws Exception {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(reportDate);

        final List<CmmnCodeVO> solar = repDetailMapper2.selectSolarList();
        final List<CmmnCodeVO> lunar = repDetailMapper2.selectLunarList();

        // 일일보고서: 보고일 다음날로
        if (repTyCode.equals(RepMasterVO.REP_TY_CODE_DAILY)) {
            cal.add(Calendar.DATE, +1);
            long date = DateUtil.dateToLong(cal.getTime());

            while (DateUtil.isHoliday(date, solar, lunar)) {
                cal.add(Calendar.DATE, +1);
                date = DateUtil.dateToLong(cal.getTime());
            }
        }
        // 주간보고서: 그 주 금요일로
        else if (repTyCode.equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
            while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                cal.add(Calendar.DATE, +1);
            }

            long date = DateUtil.dateToLong(cal.getTime());

            while (DateUtil.isHoliday(date, solar, lunar)) {
                cal.add(Calendar.DATE, -1);
                date = DateUtil.dateToLong(cal.getTime());
            }
        }
        // 월간보고서: 그 달의 말일로
        else {
            final int year = cal.get(Calendar.YEAR);
            final int month = cal.get(Calendar.MONTH);
            final int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(year, month, day);
            long date = DateUtil.dateToLong(cal.getTime());

            while (DateUtil.isHoliday(date,solar,lunar)) {
                cal.add(Calendar.DATE, -1);
                date = DateUtil.dateToLong(cal.getTime());
            }
        }

        return cal.getTime();
    }

    public int getLastRepSn(String repTyCode, Date reportDt) {
        return repDetailMapper2.selectLastRepSn(repTyCode, reportDt);
    }

    public List<RepAttitudeVO> getAttitudeList(Date attitudeDt, String userId) {
        return repDetailMapper2.selectAttitudeList(attitudeDt, userId);
    }

    public RepMasterVO2 getMasterInfo(String repSn) {
        return repDetailMapper2.selectMasterInfo(repSn);
    }

    public List<RepDetailVO2> getDetailList(String userId, String repSn) {
        return repDetailMapper2.selectDetailList(userId, repSn);
    }

    public CRRepAttachmentVO getAttachmentIds(String repSn, String userId) {
        return repDetailMapper2.selectAttachment(repSn, userId);
    }

    public List<RepAttachmentNameAndSizeVO> getAttachmentInfo(String repSn, String userId) {
        return repDetailMapper2.selectAttachmentNameAndSize(repSn, userId);
    }

    public int updateAttachment(CRRepAttachmentVO vo) {
        return repDetailMapper2.insertOrUpdateAttachment(vo);
    }

    public int removeAdditionalFile(String repSn, String userId) {
        return repDetailMapper2.removeAdditionalFile(repSn, userId);
    }

    public List<RepAssignVO> getAssignList(String userId, String repTyCode) {
        return repDetailMapper2.selectAssignList(userId, repTyCode);
    }

    public String getMasterSn(Date reportDt, String repTyCode) throws Exception {
        final Date nextDate = getNextWeekday(reportDt, repTyCode);
        return repDetailMapper2.selectRepSnByCodeAndReportDt(repTyCode, nextDate);
    }

    public boolean isConfirmed(String repSn) {
        return repDetailMapper2.selectIsConfirmed(repSn);
    }

    public String createMaster(RepFormVO2 formData, String userId) throws Exception {
        final String repTyCode = formData.getType();
        final Date reportDt = formData.getDate();
        final Date nextDate = getNextWeekday(reportDt, repTyCode);
        final String repSn = repDetailMapper2.selectRepSnByCodeAndReportDt(repTyCode, nextDate);
        
        if (repSn == null || "".equals(repSn)) {
            final CRepMasterVO master = CRepMasterVO
                    .Builder
                    .aCRepMasterVO()
                    .repTyCode(repTyCode)
                    .reportDt(nextDate)
                    .userId(userId)
                    .build();

            repDetailMapper2.insertMaster(master);

            return repDetailMapper2.selectRepSnByCodeAndReportDt(repTyCode, nextDate);
        }

        if (repDetailMapper2.selectIsConfirmed(repSn)) {
            throw new Exception("이미 확정된 보고서입니다");
        }

        return repSn;
    }

    public void updateDetails(String repSn, RepFormVO2 formData, String userId) throws Exception {
        final RepMasterVO2 master = repDetailMapper2.selectMasterInfo(repSn);
        final String repTyCode = master.getRepTyCode();
        final Date reportDt = new SimpleDateFormat("yyyy-MM-dd").parse(master.getReportDt());

        repDetailMapper2.deleteDetails(repSn, userId);

        // 주간보고서일 경우 추진실적 날짜 넣어줌 (B100)
        if ("B002".equals(repTyCode)) {
            final List<String> dayRange = getDayRange(reportDt, repTyCode);
            final CRepDetailVO detail = CRepDetailVO.Builder
                    .aCRepDetailVO()
                    .repSn(repSn)
                    .userId("admin")
                    .sysCode("B100")
                    .currentDescription("今週  추진실적 (" + dayRange.get(0) + " ~ " + dayRange.get(1) + ")")
                    .planDescription("次週  추진계획 (" + dayRange.get(2) + " ~ " + dayRange.get(3) + ")")
                    .build();

            repDetailMapper2.insertDetail(detail);
        }

        // 월간보고서일 경우 추진실적 날짜 넣어줌 (B100)
        if ("B003".equals(repTyCode)) {
            final List<String> dayRange = getDayRange(reportDt, repTyCode);
            final CRepDetailVO detail = CRepDetailVO.Builder
                    .aCRepDetailVO()
                    .repSn(repSn)
                    .userId("admin")
                    .sysCode("B100")
                    .currentDescription("今月  추진실적 (" + dayRange.get(0) + " ~ " + dayRange.get(1) + ")")
                    .planDescription("次月  추진계획 (" + dayRange.get(2) + " ~ " + dayRange.get(3) + ")")
                    .build();

            repDetailMapper2.insertDetail(detail);
        }

        // 모든 detail insert
        formData
                .getDescriptions()
                .forEach(description ->  {
                    final CRepDetailVO detail = CRepDetailVO
                            .Builder
                            .aCRepDetailVO()
                            .repSn(repSn)
                            .sysCode(description.getCode())
                            .currentDescription(description.getCurrentDescription())
                            .planDescription(description.getNextDescription())
                            .userId(userId)
                            .build();

                    repDetailMapper2.insertDetail(detail);
                });


        // 근태 업데이트
        if ("B001".equals(repTyCode)) {
            final CRepAttitudeVO currentAttitude = CRepAttitudeVO.BUilder
                    .aCRepAttitudeVO()
                    .attitudeCode(formData.getCurrentAttitude())
                    .attitudeDt(getLastWeekday(reportDt))
                    .userId(userId)
                    .build();

            final CRepAttitudeVO nextAttitude = CRepAttitudeVO.BUilder
                    .aCRepAttitudeVO()
                    .attitudeCode(formData.getPlanAttitude())
                    .attitudeDt(reportDt)
                    .userId(userId)
                    .build();

            repDetailMapper2.insertAttitude(currentAttitude);
            repDetailMapper2.insertAttitude(nextAttitude);
        }
    }

    public int deleteDetails(String repSn, String userId) throws Exception {
        final boolean isConfirmed = repDetailMapper2.selectIsConfirmed(repSn);

        if (isConfirmed) {
            throw new Exception("이미 확정 또는 확정 요청된 보고서입니다");
        }

        return repDetailMapper2.deleteDetails(repSn, userId);
    }

    // ---- private ----

    private List<String> getDayRange(Date reportDt, String repTyCode) throws ParseException {
        final List<String> dayRange = new ArrayList<String>();

        if (repTyCode.equals(RepMasterVO.REP_TY_CODE_WEEKLY)) {
            final List<CmmnCodeVO> solar = repDetailMapper2.selectSolarList();
            final List<CmmnCodeVO> lunar = repDetailMapper2.selectLunarList();

            // 금주, 차주 범위 구하기
            final Calendar cal = Calendar.getInstance();
            cal.setTime(reportDt);
            cal.setFirstDayOfWeek(Calendar.MONDAY); // 주차의 기준을 월요일로 정한다.
            cal.setMinimalDaysInFirstWeek(7); // 주차에 포함될 최소 일수는 7일이다.

            final SimpleDateFormat weekDateFormat = new SimpleDateFormat("MM.dd");
            long date;
            final int[] week = { 2, 6, 2, 6 }; // 월, 금, 월, 금

            // 금주와 차주 범위 구하는 로직 시작
            for (int i = 0; i < week.length; i++) {
                if (i == 2) {
                    cal.add(Calendar.DATE, +7); // 다음주로
                }

                // 요일의 날짜구하기
                cal.set(Calendar.DAY_OF_WEEK, week[i]);
                date = DateUtil.dateToLong(cal.getTime());

                while (DateUtil.isHoliday(date,solar,lunar)) {
                    // 월요일이면 +1(다음날로), 금요일이면 -1(전일로)
                    if (week[i] == Calendar.MONDAY) {
                        cal.add(Calendar.DATE, +1);
                    }
                    else {
                        cal.add(Calendar.DATE, -1);
                    }

                    date = DateUtil.dateToLong(cal.getTime());
                }
                dayRange.add(weekDateFormat.format(cal.getTime()));
            }
        }
        else if (repTyCode.equals(RepMasterVO.REP_TY_CODE_MONTHLY)) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(reportDt);

            final int thisMonth = cal.get(Calendar.MONTH) + 1; // reportDt가 어떤 달인지 구하기
            int nextMonth = thisMonth + 1;

            if (nextMonth == 13) {
                nextMonth = 1; // 13이면 1월
            }

            dayRange.add(thisMonth + ".1");
            dayRange.add(thisMonth + "." + cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 현재달 말일 구하기
            dayRange.add(nextMonth + ".1");
            cal.add(Calendar.MONTH, 1);
            dayRange.add(nextMonth + "." + cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 다음달 말일 구하기
        }

        return dayRange;
    }
}
