package com.bestgood.commons.util;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.text.TextUtils;

import com.bestgood.commons.R;
import com.bestgood.commons.util.log.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * 获取上一个月的时间毫秒值
     *
     * @return
     */
    public static long getLastMonthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        return calendar.getTimeInMillis();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 评论时间
     *
     * @param times
     * @return
     */
    public static String getCommTimeStr(long times) {
        Calendar curCal = Calendar.getInstance(Locale.CHINA);
        Calendar reviewCal = Calendar.getInstance(Locale.CHINA);
        reviewCal.setTimeInMillis(times);

        int curYear = curCal.get(Calendar.YEAR);
        int curMonth = curCal.get(Calendar.MONTH);
        int curDay = curCal.get(Calendar.DAY_OF_MONTH);

        int reviewYear = reviewCal.get(Calendar.YEAR);
        int reviewMonth = reviewCal.get(Calendar.MONTH);
        int reviewDay = reviewCal.get(Calendar.DAY_OF_MONTH);

        if (curYear == reviewYear && curMonth == reviewMonth
                && curDay == reviewDay) {
            return "今天";
        }
        if (curYear == reviewYear) {
            return formatdate_MMddHHmm(times);
        }
        return formatdateDot(times);
    }

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    public static String formatOrderUpDayTime(long time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM月dd HH:mm",
                Locale.getDefault());
        try {
            return sDateFormat.format(Long.valueOf(time));
        } catch (Exception e) {
            Logger.e(e, "formatOrderUpDayTime(%d)", time);
        }
        return "";
    }

    public static String formatOrderUpDayTimes(long time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM月dd HH:mm",
                Locale.getDefault());
        try {
            return sDateFormat.format(Long.valueOf(time));
        } catch (Exception e) {
            Logger.e(e, "formatOrderUpDayTimes(%d)", time);
        }
        return "";
    }

    public static String formatOrderUpDay(long time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM月dd日",
                Locale.getDefault());
        try {
            return sDateFormat.format(Long.valueOf(time));
        } catch (Exception e) {
            Logger.e(e, "formatOrderUpDay(%d)", time);
        }
        return "";
    }

    public static String formatOrderUpTime(long time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH时",
                Locale.getDefault());
        try {
            return sDateFormat.format(Long.valueOf(time));
        } catch (Exception e) {
            Logger.e(e, "formatOrderUpTime(%d)", time);
        }
        return "";
    }

    public static String formatOrderUpMinute(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm分", Locale.getDefault());
        try {
            return sdf.format(Long.valueOf(time));
        } catch (Exception e) {
            Logger.e(e, "formatOrderUpMinute(%d)", time);
        }
        return "";
    }

    /**
     * 格式化时间未字符串
     *
     * @param format 格式：如 yyyy-MM-dd HH:mm:ss
     * @param time
     * @return
     */
    public static String formatTime(String format, long time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format,
                Locale.getDefault());
        try {
            return sDateFormat.format(Long.valueOf(time));
        } catch (Exception e) {
            Logger.e(e, "formatTime(%d)", time);
        }
        return "";
    }

    /**
     * 格式化日期yyyy-MM-dd HH:mm:ss
     *
     * @param currenttime
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatdate(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String formatdate(String currenttime) {
        try {
            long curtime = Long.parseLong(currenttime);
            return formatdate(curtime);
        } catch (Exception e) {
            return StringUtils.getNonNullString(currenttime);
        }
    }

    /**
     * 格式化日期yyyy-MM-dd HH:mm:ss:SSS
     *
     * @param currenttime
     * @return
     */
    public static String formatdateSSS(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return sdf.format(date);
    }

    /**
     * 格式化日期HH:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdateHHMM(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    /**
     * 格式化日期HH:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdateHHMM(String currenttime) {
        try {
            long curtime = Long.parseLong(currenttime);
            Date date = new Date(curtime);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(date);
        } catch (Exception e) {
            return "date";
        }
    }

    /**
     * 格式化日期yyyy-MM-dd
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_yyyyMMdd(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 格式化日期yyyy.MM.dd
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_point_yyyyMMdd(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(date);
    }

    public static String formatdate_yyyyMMdd(String currenttime) {
        try {
            long curtime = Long.parseLong(currenttime);
            return formatdate_yyyyMMdd(curtime);
        } catch (Exception e) {
            return StringUtils.getNonNullString(currenttime);
        }
    }

    /**
     * 格式化日期MM/dd
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_Md(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("M/d");
        return sdf.format(date);
    }

    /**
     * 格式化日期yyyy-MM-dd HH:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_yyyyMMddHHmm(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    /**
     * 格式化日期yyyy.MM.dd HH:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdateDot(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return sdf.format(date);
    }

    /**
     * 格式化日期dd/MM hh:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_MMdd_hhmm(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
        return sdf.format(date);
    }

    /**
     * 格式化日期dd/MM hh:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_MMdd_hhmm(String currenttime) {
        long l = System.currentTimeMillis();
        try {
            l = Long.parseLong(currenttime);
        } catch (Exception e) {
        }
        return formatdate_MMdd_hhmm(l);
    }

    /**
     * 格式化日期dd_MM hh:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_MM_dd_hhmm(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM HH:mm");
        return sdf.format(date);
    }

    public static String formatdate_MM_dd_hhmm(Double currenttime) {
        String result = currenttime + "";
        @SuppressWarnings("deprecation")
        Date date = new Date(result);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM HH:mm");
        return sdf.format(date);
    }

    /**
     * 格式化 日期 dd HH:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_ddhhmm(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/HH:mm");
        return sdf.format(date);
    }

    /**
     * 格式化 日期 MM.dd HH:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_MMddHHmm(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd HH:mm");
        return sdf.format(date);
    }

    public static String formatdate_yyyyMM(long currenttime) {
        Date date = new Date(currenttime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(date);
    }

    /**
     * 格式化日期dd_MM hh:mm
     *
     * @param currenttime
     * @return
     */
    public static String formatdate_MM_dd_hhmm(String currenttime) {
        long l = System.currentTimeMillis();
        try {
            l = Long.parseLong(currenttime);
        } catch (Exception e) {
        }
        return formatdate_MM_dd_hhmm(l);
    }

    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @param format
     * @return
     */
    public static long convertDate2long(String date, String format) {
        try {
            if (!TextUtils.isEmpty(date)) {
                SimpleDateFormat sf = new SimpleDateFormat(format);
                return sf.parse(date).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String convertDate2String(long time, String format) {
        if (time > 0l) {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            Date date = new Date(time);

            return sf.format(date);
        }
        return "";
    }

    /**
     * 将以秒为单位的时间转换为时分秒格式
     *
     * @param s
     * @return
     */
    public static String formatSecondTime(Resources res, int s) {
        int hour = s / 3600;
        int minute = (s % 3600) / 60;
        int second = (s % 3600) % 60;
        String time = "";
        if (hour > 0) {
            time += hour + res.getString(R.string.hour);
        }
        if (minute > 0) {
            time += minute + res.getString(R.string.minute);
        }
        time += String.format("%02d", second) + res.getString(R.string.second);
        return time;
    }

    /**
     * 将以秒为单位的时间转换为天时分格式
     *
     * @param s
     * @return
     */
    public static String formatSecondTime(float s) {
        int s1 = (int) s;
        int day = s1 / (24 * 60 * 60);
        int hour = (s1 % (24 * 60 * 60)) / (60 * 60);
        int minute = (s1 % (60 * 60)) / 60;
        String time = "";
        if (day > 0) {
            time += day + "d";
        }
        if (hour > 0) {
            time += hour + "h";
        }
        time += minute + "m";
        return time;
    }

    /**
     * String to calendar
     */
    public static Calendar getCalendar(String strDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 更具 字符串日期 2012-04-26，得到周几
     *
     * @param res
     * @param strDate
     * @return
     */
    public static String getWeek(Resources res, String strDate) {
        try {
            Date date = DateFormat.getDateInstance(DateFormat.SHORT,
                    Locale.CHINESE).parse(strDate);
            Calendar calendar = Calendar.getInstance(Locale.CHINESE);
            calendar.setTime(date);

            // SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY.
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            return res.getStringArray(R.array.day_of_week)[week];
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取周几
     *
     * @param calendar
     * @return
     */
    public static String getWeekStr(Calendar calendar) {
        String str = "";

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY: {
                str = "星期一";
                break;
            }
            case Calendar.TUESDAY: {
                str = "星期二";
                break;
            }
            case Calendar.WEDNESDAY: {
                str = "星期三";
                break;
            }
            case Calendar.THURSDAY: {
                str = "星期四";
                break;
            }
            case Calendar.FRIDAY: {
                str = "星期五";
                break;
            }
            case Calendar.SATURDAY: {
                str = "星期六";
                break;
            }
            case Calendar.SUNDAY: {
                str = "星期天";
                break;
            }
        }
        return str;
    }

    /**
     * 格式化 时间 串
     *
     * @param s
     * @return
     */
    public static String formatDateTime(long s) {
        Calendar curCal = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(curCal.getTimeInMillis());

        if (curCal.get(Calendar.DATE) == cal.get(Calendar.DATE)) {
            return formatdateHHMM(cal.getTimeInMillis());
        }

        return formatdate_yyyyMMddHHmm(cal.getTimeInMillis());
    }

    /**
     * 获取标准时间值（毫秒）
     *
     * @param datevalue  时间值
     * @param datefoemat 时间格式
     * @return
     */
    public static long getLong(String datevalue, String datefoemat) {
        SimpleDateFormat myFormatter = new SimpleDateFormat(datefoemat);
        java.util.Date mydate = null;
        try {
            mydate = myFormatter.parse(datevalue);
        } catch (Exception e) {
        }
        long day = mydate.getTime();
        return day;
    }

    /**
     * 获取指定格式时间
     *
     * @param datevalue  时间值
     * @param datefoemat 时间格式
     * @return
     */
    public static String getString(long datevalue, String datefoemat) {
        SimpleDateFormat sdf = new SimpleDateFormat(datefoemat);
        String formatedDate = sdf.format(datevalue);
        return formatedDate;
    }

    /**
     * 时间String 转化成 Formated String
     *
     * @param dateTime  时间String
     * @param format    时间格式
     * @param oldFormat 原时间格式
     */
    public static String getFormatedDateStringFromDateString(String dateTime,
                                                             String format, String oldFormat) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(oldFormat,
                    Locale.CHINESE);
            date = dateFormat.parse(dateTime);
        } catch (Exception ex) {
            return null;
        }
        return new SimpleDateFormat(format, Locale.CHINESE).format(date);
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(final Date date) {
        return isTheDay(date, new Date());
    }

    /**
     * 是否是指定日期
     *
     * @param date
     * @param day
     * @return
     */
    public static boolean isTheDay(final Date date, final Date day) {
        return date.getTime() >= TimeUtils.dayBegin(day).getTime()
                && date.getTime() <= TimeUtils.dayEnd(day).getTime();
    }

    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date
     * @return
     */
    public static Date dayBegin(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date
     * @return
     */
    public static Date dayEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }
}
