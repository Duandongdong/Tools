package com.baigu.util.log;

/**
 * Created by negwiki on 15/9/10.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Logger is a wrapper of {@link Log}
 * But more pretty, simple and powerful
 *
 * @author Orhan Obut
 */
final class LoggerPrinter implements Printer {

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    /**
     * It is used for json pretty print
     */
    private static final int JSON_INDENT = 4;

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 3;

    /**
     * It is used to determine log settings such as method count, thread info visibility
     */
    private static final LoggerSettings settings = new LoggerSettings();

    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "══════════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "──────────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    /**
     * TAG is used for the Log, the name is a little different
     * in order to differentiate the logs easily with the filter
     */
    private static String TAG_PREFIX = "bestgood";

    private String TAG_GENERATE = "";

    /**
     * Localize single tag and method count for each thread
     */
    private static final ThreadLocal<String> LOCAL_TAG = new ThreadLocal<String>();
    private static final ThreadLocal<Integer> LOCAL_METHOD_COUNT = new ThreadLocal<Integer>();

    /**
     * It is used to change the tag
     */
    @Override
    public LoggerSettings init() {
        return settings;
    }

    @Override
    public LoggerSettings getSettings() {
        return settings;
    }

    @Override
    public Printer t(String tag, int methodCount) {
        if (tag != null) {
            LOCAL_TAG.set(tag);
        }
        LOCAL_METHOD_COUNT.set(methodCount);
        return this;
    }

    @Override
    public void generateTag(String tag) {
        TAG_GENERATE = tag;
    }

    //==============================================================================================
    @Override
    public void v(String message, Object... args) {
        log(Log.VERBOSE, message, args);
    }

    @Override
    public void d(String message, Object... args) {
        log(Log.DEBUG, message, args);
    }

    @Override
    public void i(String message, Object... args) {
        log(Log.INFO, message, args);
    }

    @Override
    public void w(String message, Object... args) {
        log(Log.WARN, message, args);
    }

    @Override
    public void e(String message, Object... args) {
        e(null, message, args);
    }

    @Override
    public void e(Throwable throwable, String message, Object... args) {
        if (throwable != null && message != null) {
            message += "\r\n" + Log.getStackTraceString(throwable);
        }
        if (throwable != null && message == null) {
            message = Log.getStackTraceString(throwable);
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        log(Log.ERROR, message, args);
    }


    @Override
    public void wtf(String message, Object... args) {
        log(Log.ASSERT, message, args);
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void d(Intent intent, String message, Object... args) {
        message += "\n" + Utils.buildIntent(intent);
        log(Log.DEBUG, message, args);
    }


    @Override
    public void d(Bundle bundle, String message, Object... args) {
        message += "\n" + Utils.buildBundle(bundle, "");
        log(Log.DEBUG, message, args);
    }


    @Override
    public void object(Object object) {
        String objectStr = Utils.buildObject(object, "");
        d(objectStr);
    }


    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    @Override
    public void json(String json) {

        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                d(message);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                d(message);
            } else {
                d("Error json content");
            }
        } catch (JSONException e) {
            e(e, json);
        }
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    @Override
    public void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            d("Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(e.getCause().getMessage() + "\n" + xml);
        }
    }
    //==============================================================================================

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    private synchronized void log(int logType, String msg, Object... args) {
        String tag = getTag();
        int methodCount = getMethodCount();
        String message = createMessage(msg, args);

        logTopBorder(logType, tag);
        logHeaderContent(logType, tag, methodCount);

        if (methodCount > 0) {
            logDivider(logType, tag);
        }

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            logContent(logType, tag, message);
        } else {
            for (int i = 0; i < length; i += CHUNK_SIZE) {
                int count = Math.min(length - i, CHUNK_SIZE);
                //create a new String with system's default charset (which is UTF-8 for Android)
                logContent(logType, tag, new String(bytes, i, count));
            }
        }
        logBottomBorder(logType, tag);
    }

    private void logTopBorder(int logType, String tag) {
        logChunk(logType, tag, TOP_BORDER);
    }

    private void logHeaderContent(int logType, String tag, int methodCount) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (settings.isShowThreadInfo()) {
            logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " Thread: " + String.format(Locale.US, "[%d,%s]", Thread.currentThread().getId(), Thread.currentThread().getName()));
            logDivider(logType, tag);
        }
        String level = "";

        int stackOffset = getStackOffset(trace) + settings.getMethodOffset();

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("║ ")
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += Utils.BLANK_SPACE;
            logChunk(logType, tag, builder.toString());
        }
    }

    private void logBottomBorder(int logType, String tag) {
        logChunk(logType, tag, BOTTOM_BORDER);
    }

    private void logDivider(int logType, String tag) {
        logChunk(logType, tag, MIDDLE_BORDER);
    }

    private void logContent(int logType, String tag, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines) {
            logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " " + line);
        }
    }

    private void logChunk(int logType, String tag, String chunk) {
        String finalTag = formatTag(tag);
        switch (logType) {
            case Log.ERROR:
                Log.e(finalTag, chunk);
                break;
            case Log.INFO:
                Log.i(finalTag, chunk);
                break;
            case Log.VERBOSE:
                Log.v(finalTag, chunk);
                break;
            case Log.WARN:
                Log.w(finalTag, chunk);
                break;
            case Log.ASSERT:
                Log.wtf(finalTag, chunk);
                break;
            case Log.DEBUG:
                // Fall through, log debug by default
            default:
                Log.d(finalTag, chunk);
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }


    /**
     * @return the appropriate tag based on local or global
     */
    private String getTag() {
        String tag = LOCAL_TAG.get();
        if (tag != null) {
            LOCAL_TAG.remove();
            return tag;
        }
        return TAG_PREFIX;
    }

    private String formatTag(String tag) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.equals(TAG_PREFIX, tag)) {
            return TAG_PREFIX + "_" + tag;
        }
        return TAG_PREFIX + "_" + TAG_GENERATE;
    }


    private int getMethodCount() {
        Integer count = LOCAL_METHOD_COUNT.get();
        int result = settings.getMethodCount();
        if (count != null) {
            LOCAL_METHOD_COUNT.remove();
            result = count;
        }
        if (result < 0) {
            throw new IllegalStateException("methodCount cannot be negative");
        }
        return result;
    }

    private String createMessage(String message, Object... args) {
        return args.length == 0 ? message : String.format(message, args);
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private int getStackOffset(StackTraceElement[] trace) {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LoggerPrinter.class.getName()) && !name.equals(Logger.class.getName())) {
                return --i;
            }
        }
        return -1;
    }


    // -------------------------------------------------------------------------------------------

    //==============================================================================================

    /**
     * 将字符串写入SDcard文件中
     *
     * @param filename 文件名
     * @param message  要写入的字符串
     */
    @Override
    public void writeFile(String filename, String message) {
        if ((!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                || (!Environment.getExternalStorageDirectory().canWrite())) {
            return;
        }
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + filename;
        writeBytes(path, message);
    }

    /**
     * 将字符串写入SDcard文件夹中
     *
     * @param folder   文件夹名
     * @param filename 文件名
     * @param message  要写入的字符串
     */
    @Override
    public void writeFile(String folder, String filename, String message) {
        if ((!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                || (!Environment.getExternalStorageDirectory().canWrite())) {
            return;
        }
        String directory = Environment.getExternalStorageDirectory().getPath() + File.separator + folder;
        File directoryFile = new File(directory);
        if (!directoryFile.exists()) {
            directoryFile.mkdir();
        }
        String path = directory + File.separator + filename;
        writeBytes(path, message);
    }

    private void writeBytes(String path, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        byte[] cbs = str.getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(cbs);
        try {
            FileOutputStream out = new FileOutputStream(path, true);
            int read;
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}