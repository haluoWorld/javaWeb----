package com.liule.log;

public abstract class Log {
    private final int logId;
    private static int logCount = 0;
    public Log() {
        logId = ++logCount;
    }
    public int getLogId() {
        return logId;
    }
    public static int getLogCount() {
        return logCount;
    }
    public String toString() {
        return "Log" + logId;
    }
}
