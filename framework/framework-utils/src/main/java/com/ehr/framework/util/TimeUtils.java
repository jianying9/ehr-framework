package com.ehr.framework.util;

import com.ehr.framework.entity.field.type.TypeHandler;
import com.ehr.framework.logger.LogFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;

/**
 * 时间处理辅助类,用于Date类型和String类型时间转换
 *
 * @author zoe
 */
public final class TimeUtils {

    /**
     * 日志对象
     */
    private final static Logger logger = LogFactory.getFrameworkLogger();
    public final static SimpleDateFormat FM_YYMM = new SimpleDateFormat("yyyy-MM");
    public final static SimpleDateFormat FM_YYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat FM_YYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat FM_YYMMDD_HHMM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public final static SimpleDateFormat FM_HHMMSS = new SimpleDateFormat("HH:mm:ss");

    private TimeUtils() {
    }

    /**
     * 过滤时间空值
     *
     * @param dateStr
     * @return
     */
    public static String filterDefaultValue(String dateStr) {
        dateStr = TimeUtils.formatYYYYMMDDHHmmSS(dateStr);
        return dateStr.equals(TypeHandler.DEFAULT_DATE_VALUE) ? "" : dateStr;
    }

    /**
     * 获取当前时间yyyy-MM-dd
     *
     * @return String 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getDateFotmatYYMMDD() {
        Date currentTime = new Date();
        return TimeUtils.FM_YYMMDD.format(currentTime);
    }

    /**
     * 获取当前时间yyyy-MM-dd HH:mm:ss
     *
     * @return String 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getDateFotmatYYMMDDHHmmSS() {
        Date currentTime = new Date();
        return TimeUtils.FM_YYMMDD_HHMMSS.format(currentTime);
    }

    /**
     * 获取下一天时间yyyy-MM-dd
     *
     * @return String 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getNextDateFotmatYYMMDD() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String dateString = TimeUtils.FM_YYMMDD.format(calendar.getTime());
        return dateString;
    }
    
    /**
     * 获取下一天时间yyyy-MM-dd HH:mm:ss
     *
     * @return String 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getNextDateFotmatYYMMDDHHmmSS() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String dateString = TimeUtils.FM_YYMMDD_HHMMSS.format(calendar.getTime());
        return dateString;
    }

    /**
     * 获取当前时间+1年
     *
     * @return String 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getNextYearFotmatYYMMDDHHmmSS() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        String dateString = TimeUtils.FM_YYMMDD_HHMMSS.format(calendar.getTime());
        return dateString;
    }

    /**
     * 获取当前时间+1年
     *
     * @return String 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getNextYearFotmatYYMMDDHHmmSS(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = TimeUtils.FM_YYMMDD_HHMMSS.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("There was an error parsing String to Date. ".concat(dateStr));
        }
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        String dateString = TimeUtils.FM_YYMMDD_HHMMSS.format(calendar.getTime());
        return dateString;
    }

    /**
     * 获取当前时间+n天
     *
     * @return String 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getDayFotmatYYMMDDHHmmSS(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, amount);
        String dateString = TimeUtils.FM_YYMMDD_HHMMSS.format(calendar.getTime());
        return dateString;
    }

    /**
     * 获取当前时间+n月
     *
     * @return String 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getMonthFotmatYYMMDDHHmmSS(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, amount);
        String dateString = TimeUtils.FM_YYMMDD_HHMMSS.format(calendar.getTime());
        return dateString;
    }

    public static String getNextMonthFotmatYYMMDDHHmmSS() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        String dateString = TimeUtils.FM_YYMMDD_HHMMSS.format(calendar.getTime());
        return dateString;
    }

    public static String getNextDateFormatYYMMDD(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = TimeUtils.FM_YYMMDD.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("There was an error parsing String to Date. ".concat(dateStr));
        }
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        String dateString = TimeUtils.FM_YYMMDD.format(calendar.getTime());
        return dateString;
    }

    /*
     * 获取当前时间+1月 @return String 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getNextMonthFotmatYYMMDDHHmmSS(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = TimeUtils.FM_YYMMDD_HHMMSS.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        String dateString = TimeUtils.FM_YYMMDD_HHMMSS.format(calendar.getTime());
        return dateString;
    }

    public static String getMonthFotmatYYMMDDHHmmSS(String dateStr, int amount) {
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = TimeUtils.FM_YYMMDD_HHMMSS.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, amount);
        String dateString = TimeUtils.FM_YYMMDD_HHMMSS.format(calendar.getTime());
        return dateString;
    }

    /**
     * 判断时间dateOneStr是否大于dateTwoStr,是返回true,否和异常返回false
     *
     * @param dateOneStr 时间字符串格式yyyy-MM-dd HH:mm:ss
     * @param dateTwoStr 时间字符串格式yyyy-MM-dd HH:mm:ss
     * @exception ParseException
     * @return result true or false
     */
    public static boolean ifAfter(String dateOneStr, String dateTwoStr) {
        boolean result;
        int flag = dateOneStr.compareTo(dateTwoStr);
        result = flag >= 0 ? true : false;
        return result;
    }

    /**
     * 判断时间dateOneStr是否大于当前系统时间,是返回true,否和异常返回false
     *
     * @param dateStr 时间字符串格式yyyy-MM-dd HH:mm:ss
     * @exception ParseException
     * @return result true or false
     */
    public static boolean ifAfter(String dateStr) {
        boolean result;
        String dateNow = getDateFotmatYYMMDDHHmmSS();
        int flag = dateStr.compareTo(dateNow);
        result = flag >= 0 ? true : false;
        return result;
    }

    /**
     * 根据出生日期计算年龄的近似值,最大为127岁,最小为1
     *
     * @param birthDate
     * @return byte
     */
    public static byte getAgeByBirth(String dateOfBirth) {
        byte age = -1;
        long currentTime = System.currentTimeMillis();
        long birthTime = 0;
        try {
            Date birthDate = TimeUtils.FM_YYMMDD.parse(dateOfBirth);
            birthTime = birthDate.getTime();
        } catch (ParseException e) {
            logger.error(dateOfBirth, e);
        } catch (NumberFormatException ne) {
            logger.error(dateOfBirth, ne);
        }
        if (currentTime > birthTime) {
            long diffetenceDay = (currentTime - birthTime) / 86400000;
            long ageTemp = diffetenceDay / 365 + 1;
            if (ageTemp > 127) {
                age = 0;
            } else {
                age = (byte) ageTemp;
            }
        }
        return age;
    }

    public static String getAgeStringByBirth(String dateOfBirth) {
        String result = "";
        byte age = getAgeByBirth(dateOfBirth);
        if (age != -1) {
            result = Byte.toString(age);
        }
        return result;
    }

    public static String formatYYYYMMDD(String dateTimeStr) {
        String result = dateTimeStr;//return
        if (dateTimeStr.length() > 10) {
            result = dateTimeStr.substring(0, 10);
        }
        return result;
    }

    public static String formatYYYYMMDD(Date date) {
        return TimeUtils.FM_YYMMDD.format(date);
    }

    public static String formatYYYYMMDDHHmm(String dateTimeStr) {
        String result = dateTimeStr;//return
        if (dateTimeStr.length() > 16) {
            result = dateTimeStr.substring(0, 16);
        }
        return result;
    }

    public static String formatYYYYMMDDHHmm(Date date) {
        return TimeUtils.FM_YYMMDD_HHMM.format(date);
    }

    public static String formatYYYYMMDDHHmmSS(String dateTimeStr) {
        String result = dateTimeStr;//return
        if (dateTimeStr.length() > 19) {
            result = dateTimeStr.substring(0, 19);
        }
        return result;
    }

    public static String formatYYYYMMDDHHmmSS(Date date) {
        return TimeUtils.FM_YYMMDD_HHMMSS.format(date);
    }

    public static long getMillis(String date) {
        long result = 0;
        try {
            result = TimeUtils.FM_YYMMDD_HHMMSS.parse(date).getTime();
        } catch (ParseException e) {
        }
        return result;
    }

    /**
     * 计算两个时间的间隔YYMMDD_HHMMSS，单位为天，dateTwoStr-dateOneStr
     *
     * @return
     */
    public static int computeGapDays(String dateOneStr, String dateTwoStr) {
        Date dateOne = null;
        Date dataTwo = null;
        try {
            dateOne = TimeUtils.FM_YYMMDD_HHMMSS.parse(dateOneStr);
            dataTwo = TimeUtils.FM_YYMMDD_HHMMSS.parse(dateTwoStr);
        } catch (ParseException e) {
            logger.error("computeGapDays error", e);
            StringBuilder errBuilder = new StringBuilder(256);
            errBuilder.append("There was an error parsing YYMMDD_HHMMSS String to Date.dateOneStr:").append(dateOneStr).append(",dateTwoStr:").append(dateTwoStr);
            throw new RuntimeException(errBuilder.toString());
        }
        long gap = dataTwo.getTime() - dateOne.getTime();
        long dayMillisecond = 24 * 60 * 60 * 1000;
        int result = (int) (gap / dayMillisecond);
        return result;
    }
    /**
     * 计算两个时间的间隔YYMMDD，单位为天，dateTwoStr-dateOneStr
     * @param dateOneStr
     * @param dateTwoStr
     * @return 
     */
    public static int computeGapDaysYYMMDD(String dateOneStr, String dateTwoStr) {
        Date dateOne = null;
        Date dataTwo = null;
        try {
            dateOne = TimeUtils.FM_YYMMDD.parse(dateOneStr);
            dataTwo = TimeUtils.FM_YYMMDD.parse(dateTwoStr);
        } catch (ParseException e) {
            logger.error("computeGapDaysYYMMDD error", e);
            StringBuilder errBuilder = new StringBuilder(256);
            errBuilder.append("There was an error parsing YYMMDD_HHMMSS String to Date.dateOneStr:").append(dateOneStr).append(",dateTwoStr:").append(dateTwoStr);
            throw new RuntimeException(errBuilder.toString());
        }
        long gap = dataTwo.getTime() - dateOne.getTime();
        long dayMillisecond = 24 * 60 * 60 * 1000;
        int result = (int) (gap / dayMillisecond);
        return result;
    }
    
    /**
     * 格式化时间，将时分秒设置为0
     * @param dateTimeStr
     * @return 
     */
    public static String formatYYYYMMDD000000(String dateTimeStr){
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = TimeUtils.FM_YYMMDD_HHMMSS.parse(dateTimeStr);
            calendar.setTime(date);
        }catch (ParseException e) {
            logger.error("formatYYYYMMDD000000 error", e);
            StringBuilder errBuilder = new StringBuilder(256);
            errBuilder.append("There was an error parsing FM_YYMMDD_HHMMSS String to Date.dateTimeStr:").append(dateTimeStr);
            throw new RuntimeException(errBuilder.toString());
        }
        
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return TimeUtils.FM_YYMMDD_HHMMSS.format(calendar.getTime());
    }
}
