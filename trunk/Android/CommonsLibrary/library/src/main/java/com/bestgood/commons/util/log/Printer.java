package com.bestgood.commons.util.log;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by negwiki on 15/9/10.
 */
public interface Printer {

    LoggerSettings init();

    LoggerSettings getSettings();

    Printer t(String tag, int methodCount);

    void generateTag(String tag);

    void v(String message, Object... args);

    void d(String message, Object... args);

    void i(String message, Object... args);

    void w(String message, Object... args);

    void e(String message, Object... args);

    void e(Throwable throwable, String message, Object... args);

    void wtf(String message, Object... args);

    void d(Intent intent, String message, Object... args);

    void d(Bundle bundle, String message, Object... args);

    void object(Object object);

    void json(String json);

    void xml(String xml);

    void writeFile(String filename, String message);

    void writeFile(String folder, String filename, String message);
}
