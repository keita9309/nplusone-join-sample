package com.example.nplusone_join_sample.application;

/**
 * リクエストスコープでSQLの発行本数を計測するカウンター。
 * ThreadLocalを使用することで、並行リクエスト間でカウントが混在しない。
 */
public class SqlQueryCounter {

    private static final ThreadLocal<Long> counter = ThreadLocal.withInitial(() -> 0L);

    /** カウントをリセットする（リクエスト開始時に呼ぶ） */
    public static void reset() {
        counter.set(0L);
    }

    /** カウントを1増やす（MyBatis Interceptorから呼ばれる） */
    public static void increment() {
        counter.set(counter.get() + 1);
    }

    /** 現在のカウントを返す */
    public static long get() {
        return counter.get();
    }
}
