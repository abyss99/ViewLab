package com.naver.viewlabs.log;

/*
 * @(#)LnInterface.class $version 2014. 4. 15.
 *
 * Copyright 2014 Naver Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public interface LnInterface {
    int v(Throwable t);

    int v(Object s1, Object... args);

    int v(Throwable throwable, Object s1, Object[] args);

    int d(Throwable t);

    int d(Object s1, Object... args);

    int d(Throwable throwable, Object s1, Object... args);

    int i(Throwable t);

    int i(Throwable throwable, Object s1, Object... args);

    int i(Object s1, Object... args);

    int w(Throwable t);

    int w(Throwable throwable, Object s1, Object... args);

    int w(Object s1, Object... args);

    int e(Throwable t);

    int e(Throwable throwable, Object s1, Object... args);

    int e(Object s1, Object... args);

    boolean isDebugEnabled();

    boolean isVerboseEnabled();

    int getLoggingLevel();

    void setLoggingLevel(int level);

    String logLevelToString(int loglevel);

    void flush();
}
