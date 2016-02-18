package com.bestgood.commons.util.log;

/**
 * Created by negwiki on 15/9/10.
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Logger is a wrapper of {@link Log}
 * But more pretty, simple and powerful
 *
 * @author Orhan Obut
 */
final class LoggerPrinterEmpty implements Printer {

    @Override
    public LoggerSettings init() {
        return null;
    }

    @Override
    public LoggerSettings getSettings() {
        return null;
    }

    @Override
    public Printer t(String tag, int methodCount) {
        return null;
    }

    @Override
    public void generateTag(String tag) {

    }

    @Override
    public void v(String message, Object... args) {

    }

    @Override
    public void d(String message, Object... args) {

    }

    @Override
    public void i(String message, Object... args) {

    }

    @Override
    public void w(String message, Object... args) {

    }

    @Override
    public void e(String message, Object... args) {

    }

    @Override
    public void e(Throwable throwable, String message, Object... args) {

    }

    @Override
    public void wtf(String message, Object... args) {

    }

    @Override
    public void d(Intent intent, String message, Object... args) {

    }

    @Override
    public void d(Bundle bundle, String message, Object... args) {

    }

    @Override
    public void object(Object object) {

    }

    @Override
    public void json(String json) {

    }

    @Override
    public void xml(String xml) {

    }

    @Override
    public void writeFile(String filename, String message) {

    }

    @Override
    public void writeFile(String folder, String filename, String message) {

    }
}