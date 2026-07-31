package com.minecraftplay;

public enum TimeOfDay {
    SUNRISE(0L),
    DAY(1000L),
    NOON(6000L),
    SUNSET(12000L),
    NIGHT(13000L),
    MIDNIGHT(18000L);

    private final long ticks;

    TimeOfDay(long ticks) {
        this.ticks = ticks;
    }

    public long getTicks() {
        return ticks;
    }

    /**
     * Safely parses a string into a TimeOfDay enum constant.
     */
    public static TimeOfDay fromString(String text) {
        for (TimeOfDay time : TimeOfDay.values()) {
            if (time.name().equalsIgnoreCase(text)) {
                return time;
            }
        }
        return null;
    }
}