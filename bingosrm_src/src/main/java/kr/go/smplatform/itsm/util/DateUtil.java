package kr.go.smplatform.itsm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.ChineseCalendar;

import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;

public class DateUtil {
	
	static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	
	
	/**
	 * Date를 Long으로 변환
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long dateToLong(Date date) throws ParseException {
		return format.parse(format.format(date)).getTime();
		
	}
	 
    /**
     * 공휴일 여부
     * @param date
     */
    public static boolean isHoliday(long date, List<CmmnCodeVO> solarList, List<CmmnCodeVO> lunarList) {
    	logger.debug("isHoliday, isWeekend, isAlternative : {} {} {}",
    			isLegalHoliday(date, solarList, lunarList), isWeekend(date), isAlternative(date));
        return isLegalHoliday(date, solarList, lunarList) || isWeekend(date) || isAlternative(date);
    }
    
    /**
     * 음력날짜 구하기
     * @param date
     * @return
     */
    public static String getLunarDate(long date) {
        ChineseCalendar cc = new ChineseCalendar(new java.util.Date(date));
        String m = String.valueOf(cc.get(ChineseCalendar.MONTH) + 1);
        m = StringUtils.leftPad(m, 2, "0");
        String d = String.valueOf(cc.get(ChineseCalendar.DAY_OF_MONTH));
        d = StringUtils.leftPad(d, 2, "0");
       
        return m + d;
    }
   
    /**
     * 법정휴일
     * @param date
     * @return
     * @throws Exception 
     */
    public static boolean isLegalHoliday(long date, List<CmmnCodeVO> solarList, List<CmmnCodeVO> lunarList){
        boolean result = false;
        
        String solarDate = DateFormatUtils.format(date, "MMdd");
        ChineseCalendar cc = new ChineseCalendar(new java.util.Date(date));
       
        String m = String.valueOf(cc.get(ChineseCalendar.MONTH) + 1);
        m = StringUtils.leftPad(m, 2, "0");
        String d = String.valueOf(cc.get(ChineseCalendar.DAY_OF_MONTH));
        d = StringUtils.leftPad(d, 2, "0");
       
        String lunarDate = m + d;
        
        for(CmmnCodeVO vo : solarList) {
	        if (solarDate.equals(vo.getCmmnCodeNm())) {
	        	logger.debug("양력공휴일 : {}", solarDate);
	            return true;
	        }
        }
        for(CmmnCodeVO vo : lunarList) {
        	if (lunarDate.equals(vo.getCmmnCodeNm())) {
        		logger.debug("음력공휴일 : {}", lunarDate);
        		return true;
        	}
        }
       
        return result;
    }
   
    /**
     * 주말 (토,일)
     * @param date
     * @return
     */
    public static boolean isWeekend(long date) {
        boolean result = false;
       
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
       
        //SUNDAY:1 SATURDAY:7
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            result = true;
        }
       
        return result;
    }
   
    /**
     * 대체공휴일
     * @param date
     * @return
     */
    public static boolean isAlternative(long date) {
        boolean result = false;
       
        //설날 연휴와 추석 연휴가 다른 공휴일과 겹치는 경우 그 날 다음의 첫 번째 비공휴일을 공휴일로 하고
        //어린이날,광복절,개천절,한글날이 일요일 또는 다른 공휴일과 겹치는 경우 그 날 다음의 첫 번째 비공휴일을 공휴일로 함
        String[] alternative = {"0505", "0815", "1003", "1009"};
        
        //어린이날
        String year = DateFormatUtils.format(date, "yyyy");
        java.util.Date d = null;
        
        for(String dt : alternative) {
	        try {
	            d = DateUtils.parseDate(year+dt, "yyyyMMdd");
	        } catch (ParseException e) {}
	        if (isWeekend(d.getTime()) == true) {
	            d = DateUtils.addDays(d, 1);
	        }
	        if (isWeekend(d.getTime()) == true) {
	            d = DateUtils.addDays(d, 1);
	        }
	        if (DateUtils.isSameDay(new java.util.Date(date), d) == true) {
	        	logger.debug("대체공휴일 : {}", d);
	            result = true;
	            break;
	        }
        }
        
        
        //설
        String lunarDate = getLunarDate(date);
        Calendar calendar = Calendar.getInstance();
        d = new java.util.Date(date);
        if (StringUtils.equals(lunarDate, "0103")) {
            d = DateUtils.addDays(d, -1);
            calendar.setTime(d);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return true;
            }
            
            d = DateUtils.addDays(d, -1);
            calendar.setTime(d);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return true;
            }
            
            d = DateUtils.addDays(d, -1);
            calendar.setTime(d);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return true;
            }
        }
       
        //추석
        d = new java.util.Date(date);
        if (StringUtils.equals(lunarDate, "0817")) {
            d = DateUtils.addDays(d, -1);
            calendar.setTime(d);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return true;
            }
           
            d = DateUtils.addDays(d, -1);
            calendar.setTime(d);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return true;
            }
           
            d = DateUtils.addDays(d, -1);
            calendar.setTime(d);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return true;
            }
        }
       
        return result;
    }

}
