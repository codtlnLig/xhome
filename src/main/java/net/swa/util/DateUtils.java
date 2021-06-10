
package net.swa.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * 本类提供一些日期处理的基本方法
 * 
 * @author magician
 * @version 1.0
 */
public class DateUtils
{
    // ~ Static fields/initializers
    // -----------------------------------------------------------------

    private static final Logger log = Logger.getLogger(DateUtils.class);

    public static final long SECOND = 1000;

    public static final long MINUTE = SECOND * 60;

    public static final long HOUR = MINUTE * 60;

    public static final long DAY = HOUR * 24;

    public static final long WEEK = DAY * 7;

    public static final long MONTH = DAY * 30;

    public static final long YEAR = DAY * 365;

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    private static SimpleDateFormat sdf = new SimpleDateFormat();

    // ~ Constructors
    // -------------------------------------------------------------------------------

    private DateUtils()
    {
    }

    // ~ Methods
    // ------------------------------------------------------------------------------------

    /**
     * 获取可距离某段时间内月份字符串的方法：getAvailableMonth()
     * 
     * 使用说明： getAvailableMonth( -1,-5)
     * 如果当前是200205的话，那么返回的月份就是200101到200204，包括当前月份
     * 
     * getAvailableMonth( 3,7) 如果当前是200202的话，那么返回的月份就是200205到200209，包括当前月份
     * 
     * 注意，开始月份永远是靠近当前月份的。
     * 
     * @param begin
     *            开始月份(距离当前月份)
     * @param end
     *            结束月份(距离当前月份)
     * @return string[] 月份数组，格式为200011
     */
    public static String[] getAvailableMonth(int begin , int end)
    {
        // 需取得月份的个数
        int numberOfMonth = end - begin;

        // 月份数的绝对值
        int index = Math.abs(numberOfMonth) + 1;

        // 返回的月份数组
        String[] strMonth = new String[index];

        // 回滚年数
        int roll = (end > begin) ? (begin - 1) : (end - 1);

        try
        {
            for (int i = 1; i <= index; i++)
            {
                // 依次回滚，计算当前回滚后的月份
                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.MONTH, roll + i);

                Date now = calendar.getTime();

                // 月份格式YYYYMM
                strMonth[i - 1] = getDateString(now, "yyyyMM");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return strMonth;
    }

    /**
     * 根据参数格式化日期为字符串，默认格式为yyyy-MM-dd
     * 
     * @param date
     *            被格式化日期
     * @param formatPattern
     *            格式化格式
     * @return String 格式化后的日期字符串
     */
    public static String getDateString(Date date , String formatPattern)
    {
        if (date == null)
        {
            return "";
        }

        if ((formatPattern == null) || formatPattern.equals(""))
        {
            formatPattern = "yyyy-MM-dd";
        }

        sdf.applyPattern(formatPattern);

        return sdf.format(date);
    }

    /**
     * 获得格式为yyyy-MM-dd的格式化日期为字符串
     * 
     * @param date
     *            被格式化日期
     * @param formatPattern
     *            格式化格式
     * @return String 格式化后的日期字符串
     */
    public static String getDateString(Date date)
    {
        return getDateString(date, null);
    }

    /**
     * 根据参数格式化日期时间为字符串，默认格式为yyyy-MM-dd hh:mm:ss
     * 
     * @param date
     *            被格式化日期
     * @param formatPattern
     *            格式化格式
     * @return String 格式化后的日期字符串
     */
    public static String getDateTimeString(Date date , String formatPattern)
    {
        if (date == null)
        {
            return "";
        }

        if ((formatPattern == null) || formatPattern.equals(""))
        {
            formatPattern = "yyyy-MM-dd HH:mm:ss";
        }

        sdf.applyPattern(formatPattern);

        return sdf.format(date);
    }

    /**
     * 获取格式为yyyy-MM-dd hh:mm:ss的格式化日期时间为字符串
     * 
     * @param date
     *            被格式化日期
     * @return String 格式化后的日期字符串
     */
    public static String getDateTimeString(Date date)
    {
        return getDateTimeString(date, null);
    }

    /**
     * 根据参数格式化字符串为日期，默认格式为yyyy-MM-dd
     * 
     * @param date
     *            字符串形式的日期
     * @param formatPattern
     *            格式化格式
     * @return Date 根据字符串格式化后的日期
     */
    public static Date getStringDate(String date , String formatPattern)
    {
        try
        {
            if ((formatPattern == null) || formatPattern.equals(""))
            {
                formatPattern = "yyyy-MM-dd";
            }

            sdf.applyPattern(formatPattern);

            return sdf.parse(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 根据参数格式化字符串为日期，默认格式为yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     *            字符串形式的日期
     * @param formatPattern
     *            格式化格式
     * @return Date 根据字符串格式化后的日期
     */
    public static Date getStringDateTime(String date , String formatPattern)
    {
        try
        {
            if ((formatPattern == null) || formatPattern.equals(""))
            {
                formatPattern = "yyyy-MM-dd HH:mm:ss";
            }

            sdf.applyPattern(formatPattern);

            return sdf.parse(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 获取格式为yyyy-MM-dd HH:mm:ss的日期
     * 
     * @param date
     *            字符串形式的日期
     * @return 根据字符串格式化后的日期
     */
    public static Date getStringDateTime(String date)
    {
        return getStringDateTime(date, null);
    }

    /**
     * 格式为yyyy-MM-dd的字符串格式化字符串为日期
     * 
     * @param date
     *            字符串形式的日期
     * @param formatPattern
     *            格式化格式
     * @return Date 根据字符串格式化后的日期
     */
    public static Date getStringDate(String date)
    {
        return getStringDate(date, null);
    }

    /**
     * <p>
     * 根据时间单位获取相应时间的毫秒刻度
     * </p>
     * 时间单位的含义为：h表示小时，d表示天，w表示周，M表示月，y表示年，m表示分，s表示秒
     * 
     * @param unit
     *            字符串形式的时间单位
     * @return long 相应时间毫秒刻度
     */
    public static long getTimeByUnit(String unit) throws Exception
    {
        switch (unit.charAt(0))
        {
            case 'h':
                return HOUR;

            case 'd':
                return DAY;

            case 'w':
                return WEEK;

            case 'm':
                return MINUTE;

            case 'n':
                return MONTH;

            case 'y':
                return YEAR;

            case 's':
                return SECOND;

            default:
                throw new IllegalArgumentException("unknown time unit");
        }
    }

    /**
     * <p>
     * 定格式获取时间，时间格式为{u}n，其中u为时间单位，n为多少个时间单位
     * </p>
     * 比如{h}3表示3天，{y}1表示一年
     * 
     * @param timestr
     *            时间格式字符串
     * @return long 相应时间
     * @throws Exception
     */
    public static long getTime(String timestr) throws Exception
    {
        // 格式分析
        int begin = timestr.indexOf("{");
        int end = timestr.indexOf("}");
        String timeUnit = timestr.substring(begin + 1, end);
        int time = Integer.parseInt(timestr.substring(end + 1));

        return getTimeByUnit(timeUnit) * time;
    }

    public static Date changeDay(Date d , int offset)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) + offset));
        return calendar.getTime();
    }

    /*
     * 取得查询条件的前面时间
     */
    public static Date getFirstQueryDate(Date d)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /*
     * 取得查询条件的后面时间
     */
    public static Date getLastQueryDate(Date d)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /*
     * 取得查询条件的后面时间
     */
    public static Date getLastEndDate(Date d)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        // calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 取得当前日期，并将其转换成格式为"dateFormat"的字符串
     * 
     * @param dateFormat
     *            String
     * @return String 例子：假如当前日期是 2003-09-24 则：dateFormat 为 "yyyyMMdd"时，返回
     *         "20030924" dateFormat 为 "yyyy-MM-dd"时，返回 "2003-09-24" dateFormat
     *         为 "yyyy/MM/dd"时，返回 "2003/09/34" ......
     */
    public static String getCurrDate(String dateFormat)
    {
        Date date = new Date();
        if (null == dateFormat)
        {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat myDateFormat = new SimpleDateFormat(dateFormat);
        return myDateFormat.format(date);
    }

    public static String getMonth(java.util.Date date)
    {
        SimpleDateFormat myDateFormat = new SimpleDateFormat("MM");
        return myDateFormat.format(date);
    }

    public static java.util.Date getCurrentDate()
    {
        return new Date();
    }

    public static Timestamp getCurrDateTime()
    {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 取得当前日期
     */
    public static java.sql.Date getCurrDate()
    {
        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * 取得系统年
     */
    public static String getCurrYear()
    {
        return getCurrDate("yyyy");
    }

    /**
     * 取得系统当前月份
     */
    public static String getCurrMonth()
    {
        return getCurrDate("MM");
    }

    public static String dateToString(java.util.Date date)
    {
        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return myDateFormat.format(date);
    }

    public static String dateToString(java.util.Date date , String format)
    {
        SimpleDateFormat myDateFormat = new SimpleDateFormat(format);
        return myDateFormat.format(date);
    }

    /**
     * format "yyyy-mm-dd" 把STRING 变成 java.sql.date
     */
    public static java.sql.Date stringToDate(String strDate)
    {

        java.sql.Date date = null;
        try
        {
            date = java.sql.Date.valueOf(strDate);
        }
        catch (Exception ex)
        {
        }
        return date;
    }

    /**
     * format "yyyy-mm-dd" 把STRING 变成 java.util.date
     */
    public static java.util.Date stringToUtilDate(String strDate)
    {
        java.util.Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            date = df.parse(strDate);
        }
        catch (Exception ex)
        {
        }
        return date;
    }

    public static java.util.Date stringToUtilDate(String strDate , String format)
    {
        java.util.Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        try
        {
            date = df.parse(strDate);
        }
        catch (Exception ex)
        {
        }
        return date;
    }

    /**
     * 加月
     */

    public static String addMonth(int offset , String dateFormat)
    {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, offset);
        return df.format(calendar.getTime());
    }

    /**
     * 给指定的日期加月
     */
    public static Date addMonth(int offset , Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, offset);
        return calendar.getTime();
    }

    /**
     * 给指定的日期加小时
     */
    public static Date addHOUR(int offset , Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, offset);
        return calendar.getTime();
    }

    /**
     * 给指定的日期加秒
     */
    public static Date addSECOND(int offset , Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, offset);
        return calendar.getTime();
    }

    /**
     * 给指定的日期加分
     */
    public static Date addMINUTE(int offset , Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, offset);
        return calendar.getTime();
    }

    /**
     * 给指定的日期加年
     */
    public static Date addYear(int offset , Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, offset);
        return calendar.getTime();
    }

    /**
     * 给指定的日期加天
     */
    public static Date addDay(int offset , Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, offset);
        return calendar.getTime();
    }

    /**
     * 两天相差的周数
     */

    public static int minusWeek(Date startDate , Date endDate)
    {

        // 开始日期大于等于结束日期返回0
        if (startDate.getTime() >= endDate.getTime())
        {
            return 0;
        }

        long startDay = (startDate.getTime() + 4 * DAY) / WEEK;
        long endDay = (endDate.getTime() + 4 * DAY) / WEEK;

        long result = endDay - startDay + 1;

        return new Long(result).intValue();

        /*
         * int compareDate=endDate.compareTo(startDate);
         * 
         * int result=0;
         * 
         * 
         * 
         * Calendar calendar1 = Calendar.getInstance();
         * calendar1.setTime(startDate);
         * 
         * 
         * 
         * int startWeek=calendar1.get(calendar1.DAY_OF_WEEK); switch
         * (startWeek){ case Calendar.SUNDAY: result=(compareDate+7)/7; break;
         * case Calendar.MONDAY: result=(compareDate+8)/7; break; case
         * Calendar.TUESDAY: result=(compareDate+9)/7; break; case
         * Calendar.WEDNESDAY: result=(compareDate+10)/7; break; case
         * Calendar.THURSDAY: result=(compareDate+11)/7; break; case
         * Calendar.FRIDAY: result=(compareDate+12)/7; break; case
         * Calendar.SATURDAY: result=(compareDate+13)/7; break; }
         * 
         * 
         * 
         * 
         * 
         * 
         * return result;
         */
    }

    /**
     * 两天相差的年数
     */

    public static double minusYear(Date startDate , Date endDate)
    {

        // 开始日期大于等于结束日期返回0
        if (startDate.getTime() >= endDate.getTime())
        {
            return 0;
        }

        double startDay = (double) (startDate.getTime() + 4 * DAY) / (double) YEAR;
        double endDay = (double) (endDate.getTime() + 4 * DAY) / (double) YEAR;

        double result = endDay - startDay;

        return new Double(result).doubleValue();

    }

    /**
     * 两天相差的月数
     */

    public static double minusMonth(Date startDate , Date endDate)
    {

        // 开始日期大于等于结束日期返回0
        if (startDate.getTime() >= endDate.getTime())
        {
            return 0;
        }

        double startDay = (double) (startDate.getTime() + 4 * DAY) / (double) MONTH;
        double endDay = (double) (endDate.getTime() + 4 * DAY) / (double) MONTH;

        double result = endDay - startDay;

        return new Double(result).doubleValue();

    }

    /**
     * 两天相差的天数
     */

    public static int minusDay(Date startDate , Date endDate)
    {

        // 开始日期大于等于结束日期返回0
        if (startDate.getTime() > endDate.getTime())
        {
            return -1;
        }

        double startDay = (double) (startDate.getTime() + 4 * DAY) / (double) DAY;
        double endDay = (double) (endDate.getTime() + 4 * DAY) / (double) DAY;

        double result = endDay - startDay;

        return new Double(result).intValue();

    }

    /**
     * 两天相差的小时数
     */

    public static double minusHour(Date startDate , Date endDate)
    {

        // 开始日期大于等于结束日期返回0
        if (startDate.getTime() >= endDate.getTime())
        {
            return 0;
        }

        double startDay = (double) (startDate.getTime() + 4 * DAY) / (double) HOUR;
        double endDay = (double) (endDate.getTime() + 4 * DAY) / (double) HOUR;

        double result = endDay - startDay;

        return new Double(result).doubleValue();

    }

    /**
     * 两天相差的分钟数
     */

    public static double minusMinute(Date startDate , Date endDate)
    {

        // 开始日期大于等于结束日期返回0
        if (startDate.getTime() >= endDate.getTime())
        {
            return 0;
        }

        double startDay = (double) (startDate.getTime() + 4 * DAY) / (double) MINUTE;
        double endDay = (double) (endDate.getTime() + 4 * DAY) / (double) MINUTE;

        double result = endDay - startDay;

        return new Double(result).doubleValue();

    }

    /**
     * 判断时间是否有效，sec内有效
     * 
     * @param ts
     * @param sec
     *            单位分钟
     * @return
     */
    public static boolean checkTime(Long ts , int sec)
    {
        if (null == ts)
        {
            return false;
        }
        else
        {
            Date d = addMINUTE(-sec, getCurrentDate());
            if (null != d)
            {
                log.debug("d.getTime()  is " + d.getTime() + ",ts is " + ts + ",current time is " + getCurrentDate().getTime());
                return (d.getTime() < ts);
            }
        }
        return false;
    }

    /**
     * 将格式format的字符串date 加上m小时后，以format2格式返回
     * @param date
     * @param format
     * @param format2
     * @param m
     * @return
     */
    public static String getDatePlusHour(String date , String format , String format2 , int m)
    {
        Date d = getStringDateTime(date, format);
        Date d2 = addHOUR(m, d);
        return getDateString(d2, format2);
    }

    public static String getfirstDayOfThisMonth()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取前月的第一天
        Calendar cal_1 = Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH, 0);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天 
        return format.format(cal_1.getTime());
    }

    /**
     * 得到i个月前日期时间
     * @param i
     * @return
     */
    public static String queryMonthdBefore(int i)
    {
        Date date = addMonth(-i, (new Date()));
        String formatPattern = "yyyy-MM-dd HH:mm:ss";
        return getDateString(date, formatPattern);
    }
}
