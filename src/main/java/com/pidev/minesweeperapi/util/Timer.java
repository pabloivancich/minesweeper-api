package com.pidev.minesweeperapi.util;

/**
 * Timer for games time tracking.
 */
public class Timer {
    /** The start, it's never null. */
    private final Long start;

    /** Creates a new instance of the Timer.*/
    public Timer() {
        this.start = System.currentTimeMillis();
    }

    public Timer(Long millis) {
        this.start = System.currentTimeMillis() + millis;
    }

    public long end() {
        return System.currentTimeMillis() - this.start;
    }
}
