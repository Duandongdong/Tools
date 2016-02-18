package com.baigu.util;

import android.text.TextUtils;

import com.baigu.util.log.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String Utils
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p/>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * is null or its length is 0
     * <p/>
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    /**
     * compare two string
     *
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * 比较两个字符串是否相等，null 和 "" 相等
     *
     * @param actual
     * @param expected
     * @return
     */
    public static boolean isEqualsIgnoreEmptyStr(String actual, String expected) {
        if (TextUtils.isEmpty(actual)) {
            return TextUtils.isEmpty(expected);
        }
        return actual.equals(expected);
    }

    /**
     * get length of CharSequence
     * <p/>
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     *
     * @param str
     * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     * <p/>
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    /**
     * capitalize first letter
     * <p/>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     * <p/>
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * <p/>
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>if href is null, return ""</li>
     * <li>if not match regx, return source</li>
     * <li>return the last string that match regx</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     * <p/>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * <p/>
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * <p/>
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    public static String token = null;

    /**
     * 得到一个不为NULL的字符串
     *
     * @param str
     * @return
     */
    public static String getNonNullString(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    /**
     * 得到一个不为NULL的字符串
     *
     * @param n
     * @return
     */
    public static String getNonNullString(Long n) {
        return n == null ? "" : n.toString();
    }

    /**
     * 得到一个不为NULL的字符串
     *
     * @param n
     * @return
     */
    public static String getNonNullString(Integer n) {
        return n == null ? "" : n.toString();
    }

    /**
     * 得到一个不为NULL的字符串
     *
     * @param f
     * @return
     */
    public static String getNonNullString(Float f) {
        return f == null ? "" : f.toString();
    }

    /**
     * 得到一个不为NULL的字符串
     *
     * @param d
     * @return
     */
    public static String getNonNullString(Double d) {
        return d == null ? "" : d.toString();
    }

    /**
     * 数字转换为字母（0,1,2... --> A,B,C....AA,AB,AC...AAA,AAB,AAC...）
     *
     * @param num
     * @return
     */
    public static String int2MoreChar(int num) {
        String str = "";
        if (num < 0) {
            return str;
        }
        if (num > 25) {
            for (int j = 0; j < num / 26; j++) {
                str += "A";
            }
            num = num % 26;
        }
        char c = (char) (num + 65);
        str += c;
        return str;
    }

    /**
     * String[] 转换 为 String
     *
     * @param array
     * @return
     */
    public static String strArray2Str(String[] array) {
        StringBuffer sb = new StringBuffer();
        if (array == null) {
            return sb.toString();
        }
        for (String str : array) {
            if (TextUtils.isEmpty(str)) {
                continue;
            }
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 保留指定的小数点位数
     *
     * @param decimal
     * @param f
     * @return 返回字符串
     */
    public static String formatFloat2Str(int decimal, float f) {
        String str = "";
        DecimalFormat format;
        switch (decimal) {
            case 1: {
                format = new DecimalFormat("0.0");// 设置输出数值的格式为XX.X
                break;
            }
            case 2: {
                format = new DecimalFormat("0.00");// 设置输出数值的格式为XX.XX
                break;
            }
            case 3: {
                format = new DecimalFormat("0.000");// 设置输出数值的格式为XX.XXX
                break;
            }
            default:
                format = new DecimalFormat("");// 设置输出数值的格式为XX
                break;
        }
        str = format.format(f);
        return str;
    }

    /**
     * 判断字符是否 是数字或字母
     *
     * @param str
     * @return
     */
    public static boolean isNumOrLetter(String str) {
        boolean flag = false;
        if (TextUtils.isEmpty(str)) {
            return flag;
        }
        for (int i = 0; i < str.length(); i++) {
            char current = str.charAt(i);
            flag = (current >= 48 && current <= 57) || (current >= 65 && current <= 90)
                    || (current >= 97 && current <= 122);
            if (!flag) {
                break;
            }
        }
        return flag;
    }

    /**
     * 获取一个字符串最后几个字符
     *
     * @param str
     * @param num
     * @return
     */
    public static String getLastNumStr(String str, int num) {
        if (str == null) {
            return "";
        }

        if (str.length() < num) {
            return str;
        }
        str = str.substring(str.length() - num);
        return str;
    }

    /**
     * 字符串 转换 为 int，转换异常 返回 Integer.MIN_VALUE
     *
     * @param str
     * @return
     */
    public static int strToInt(String str) {
        int n = Integer.MIN_VALUE;

        if (TextUtils.isEmpty(str)) {
            return n;
        }
        if (!TextUtils.isDigitsOnly(str)) {
            return n;
        }
        try {
            n = Integer.valueOf(str);
        } catch (Exception e) {
            Logger.e(e, "strToInt(%)s", str);
        }
        return n;
    }

    /**
     * 比较时间的方法
     */
    public static int compare_date(String DATE1) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse("2015-01-01  00:00");
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 取得包含token的map集合
     *
     * @return 包含token的map集合
     */
    public static final HashMap<String, String> getMapToken() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        return map;
    }

    /**
     * 保留指定的小数点位数
     *
     * @param decimal
     * @param f
     * @return 返回double型数据
     */
    public static double formatFloat2Double(int decimal, float f) {
        double pow = Math.pow(10, decimal);

        long value = Math.round(f * pow);

        double result = value / pow;

        return result;
        // BigDecimal b = new BigDecimal(f);
        // double ret = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // return (float) ret;
    }


}
