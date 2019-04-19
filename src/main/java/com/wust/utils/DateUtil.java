
package com.wust.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: WUST_WXL
 * @Description: 日期常用工具方法类
 */
@SuppressWarnings("unused")
public final class DateUtil {

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            String dt = dateStr.replaceAll("-", "/");
            if ((!dt.equals("")) && (dt.length() < format.length())) {
                dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]",
                        "0");
            }
            date = (Date) dateFormat.parse(dt);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * Description: 格式化日期
     * @return Date YYYY-MM-DD 格式
     */
    public static Date parseDate(String dateStr) {

        return parseDate(dateStr, "yyyy-MM-dd");
    }

    /**
     * @Description: 返回当前时间的时间戳字符串, 例如 1510532553293
     */
    public static String getCurrDateTimeStr() {
        Date newdate = new Date();
        long datetime = newdate.getTime();
        Timestamp timestamp = new Timestamp(datetime);
        String str = timestamp.toString();
        return new StringBuffer()
                .append(datetime)
                .toString();
    }

    /**
     * @Description: 返回当前具体日期, 具体到秒, 例如 2017-11-13 08:30:17
     */
    public static String getCurrDateTime() {
        Date newdate = new Date();
        long datetime = newdate.getTime();
        Timestamp timestamp = new Timestamp(datetime);
        return (timestamp.toString()).substring(0, 19);
    }

    /**
     * @Description: 返回当前日期, 例如 2017-11-13
     */
    public static String getCurrentDate() {
        Date newdate = new Date();
        long datetime = newdate.getTime();
        Timestamp timestamp = new Timestamp(datetime);
        String currentdate = (timestamp.toString()).substring(0, 4) + "-" + (timestamp.toString()).substring(5, 7) + "-" + (timestamp.toString()).substring(8, 10);
        return currentdate;
    }

    /**
     * @Description: 返回当前时间, 例如 08:30:17
     */
    public static String getCurrentTime() {
        Date newdate = new Date();
        long datetime = newdate.getTime();
        Timestamp timestamp = new Timestamp(datetime);
        String currenttime = (timestamp.toString()).substring(11, 13) + ":" + (timestamp.toString()).substring(14, 16) + ":" + (timestamp.toString()).substring(17, 19);
        return currenttime;
    }

    /**
     * @Description: 返回"yyyy-MM-dd"格式日期,参数为Date类型
     */
    public static String getDateYYYYMMDD(Date newdate) {
        long datetime = newdate.getTime();
        Timestamp timestamp = new Timestamp(datetime);
        String currentdate = (timestamp.toString()).substring(0, 4) + "-" + (timestamp.toString()).substring(5, 7) + "-" + (timestamp.toString()).substring(8, 10);
        return currentdate;
    }

    /**
     * @Description: 返回"yyyy-MM-dd"格式日期
     */
    public static String getDateYYYYMMDD() {
        Date newdate = new Date();
        return getDateYYYYMMDD(newdate);
    }

    /**
     * 功能描述：格式化输出日期
     *
     * @param date Date 日期
     * @param format String 格式
     * @return 返回字符型日期
     */
    public static String dateFormat(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                DateFormat dateFormat = new SimpleDateFormat(format);
                result = dateFormat.format(date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 功能描述：返回年份
     *
     * @param date Date 日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 功能描述：返回月份
     *
     * @param date Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 功能描述：返回日份
     *
     * @param date Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 功能描述：返回小时
     *
     * @param date Date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能描述：返回分钟
     *
     * @param date Date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 功能描述：返回毫秒
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    /**
     * 计算开始时间和结束时间之间的天数
     *
     * @param datebegin The first String. 格式yyyy-MM-dd
     * @param dateend The second String. 格式yyyy-MM-dd
     * @return 天数
     */
    public static long getDaysBetween(String datebegin, String dateend) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateBegin = sdf.parse(datebegin);
            Date dateEnd = sdf.parse(dateend);
            return (dateEnd.getTime() - dateBegin.getTime()) / (3600 * 24 * 1000);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 计算2个日期之间间隔天数方法
     *
     * @param d1 The first Calendar.
     * @param d2 The second Calendar.
     * @return 天数
     */
    public static long getDaysBetween(Calendar d1, Calendar d2) {

        return DateUtil.getDaysBetween(d1.toString(),d2.toString());
    }

    /**
     * 计算两个日期之间的时间间隔(d1-d2)，可选择是否计算工作日
     *
     * @param d1 The first String. 格式yyyy-MM-dd
     * @param d2 The second String. 格式yyyy-MM-dd
     * @return 天数
     */
    public static long getDaysBetween(String d1, String d2, boolean onlyWorkDay) {
        if (!onlyWorkDay) {
            return getDaysBetween(d1, d2);
        } else {
            long days = 0;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dt1 = sdf.parse(d1);
                Date dt2 = sdf.parse(d2);
                days = (dt1.getTime() - dt2.getTime()) / (3600 * 24 * 1000);
                for (calendar.setTime(dt1); !calendar.getTime().before(dt2); calendar.add(Calendar.DAY_OF_YEAR, -1)) {
                    int week = calendar.get(Calendar.DAY_OF_WEEK);
                    if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
                        days--;
                    }
                }
                if (days < 0) {
                    days = 0;
                }
            } catch (Exception e) {
            }
            return days;
        }
    }

    /**
     * 判断日期是否为工作日(周六和周日为非工作日)
     *
     * @param date
     * @return
     */
    public static boolean isWorkDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 计算两个时间之间的间隔  单位：分钟(minutes)
     * 格式 yyyy-MM-dd/HH:mm:ss
     */
    public static long getMinutesBetween(String s1, String s2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = sdf.parse(s1);
            Date dt2 = sdf.parse(s2);
            return (dt1.getTime() - dt2.getTime()) / (60 * 1000);
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 计算两个日期之间的时间间隔
     * 返回 按月分隔的list，list里面每个月一个map,第一天begindate，最后一天enddate
     *
     * @param d1 开始日期 格式yyyy-MM-dd
     * @param d2 结束日期 格式yyyy-MM-dd
     */
    public static List<HashMap> getDateBetween(String d1, String d2) {
        ArrayList<HashMap> list = new ArrayList<HashMap>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(sdf.parse(d1));
            cal2.setTime(sdf.parse(d2));

            int monthnum = (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
            for (int i = 0; i < monthnum; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("begindate", sdf.format(cal1.getTime()));
                cal1.add(Calendar.DATE, cal1.getActualMaximum(Calendar.DATE) - cal1.get(Calendar.DATE));
                map.put("enddate", sdf.format(cal1.getTime()));
                list.add(map);
                cal1.add(Calendar.MONTH, 1);
                cal1.add(Calendar.DATE, 1 - cal1.get(Calendar.DATE));
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("begindate", sdf.format(cal1.getTime()));
            map.put("enddate", d2);
            list.add(map);
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    /**
     * 比较2个格式为yyyy-MM-dd的日期的先后顺序
     * 若d1小于d2返回true
     * d1=2007-10-01,d2=2007-10-15,则返回true
     */
    public static boolean  orderOfDateStr(String d1, String d2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = sdf.parse(d1);
            Date dt2 = sdf.parse(d2);
            return dt1.getTime() < dt2.getTime();
        } catch (Exception e) {
            return false;
        }
    }

    /**
    * 移动日期
    * @param date 原日期
    * @param len 移动量
    * @return 移动后日期,例如 2017-12-03
    */
    public static String dayMove(String date, int len) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(date));
            cal.add(Calendar.DATE, len);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return date;
        }
    }

    /**
    * 移动月份
    * @param date 原日期
    * @param len 移动量
    * @return 移动后日期,例如 2018-01
    */
    public static String monthMove(String date, int len) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(date));
            cal.add(Calendar.MONTH, len);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return date;
        }
    }

    /**
    * 截取2个时间段相交的时间段
    *
    * @return  String[] = {array[0]=begindate ,array[1]=enddate}
    * 不相交    array[0]=""
    *
    */
    public static String[] getBetweenDate(String b1, String e1, String b2, String e2) {
        String[] date = new String[2];
        String begindate = "";
        String enddate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar begin1 = Calendar.getInstance();
            Calendar end1 = Calendar.getInstance();
            Calendar begin2 = Calendar.getInstance();
            Calendar end2 = Calendar.getInstance();
            begin1.setTime(sdf.parse(b1));
            end1.setTime(sdf.parse(e1));
            begin2.setTime(sdf.parse(b2));
            end2.setTime(sdf.parse(e2));
            if ((begin2.getTime().getTime() >= end1.getTime().getTime() && end2.getTime().getTime() >= end1.getTime().getTime()) ||
                    (begin2.getTime().getTime() <= begin1.getTime().getTime() && end2.getTime().getTime() <= begin1.getTime().getTime())) {
                date[0] = "";
                return date;
            }

            if (begin2.getTime().getTime() >= begin1.getTime().getTime()) {
                begindate = sdf.format(begin2.getTime());
            } else {
                begindate = sdf.format(begin1.getTime());
            }
            if (end2.getTime().getTime() >= end1.getTime().getTime()) {
                enddate = sdf.format(end1.getTime());
            } else {
                enddate = sdf.format(end2.getTime());
            }

            if (!begindate.equals("") && !enddate.equals("")) {
                date[0] = begindate;
                date[1] = enddate;
            }
        } catch (Exception e) {

        }
        return date;
    }

}
