package com.bestgood.commons.util.log;

/**
 * Created by negwiki on 15/9/10.
 */
final class LoggerSettings {
    private int methodCount = 2;
    private boolean showThreadInfo = true;
    private int methodOffset = 0;

    public LoggerSettings hideThreadInfo() {
        showThreadInfo = false;
        return this;
    }

    public LoggerSettings setMethodCount(int methodCount) {
        if (methodCount < 0) {
            methodCount = 0;
        }
        this.methodCount = methodCount;
        return this;
    }

    public LoggerSettings setMethodOffset(int offset) {
        this.methodOffset = offset;
        return this;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    public int getMethodOffset() {
        return methodOffset;
    }
}
