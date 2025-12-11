package com.github.lian2945.sonyflake.constants;

public final class SonyflakeConstants {
    public static final long SEQUENCE_BITS = 8L;
    public static final long MACHINE_BITS = 16L;

    public static final long SEQUENCE_MAX = (1L << SEQUENCE_BITS) - 1L;
    public static final long MACHINE_MAX = (1L << MACHINE_BITS) - 1L;

    public static final long TICK_MILLIS = 10L;

    // 2025-01-01T00:00:00 KST(UTC+9)
    public static final long LOCAL_DATE_MAX = 1735657200000L;
}
