package com.pidev.minesweeperapi.util;

/**
 * Timer for games time tracking.
 */
public class Timer {
    /** The start, it's never null. */
    private final Long start;

    private Long stop;

    /** Creates a new instance of the Timer.*/
    public Timer() {
        this.start = System.currentTimeMillis();
    }

    public Timer(Long millis) {
        this.start = System.currentTimeMillis() + millis;
    }

    public void stop() {
        this.stop = System.currentTimeMillis() - this.start;
    }

    /**
     * Retrieve the time (in seconds) that the timer worked.
     * @return the time in seconds.
     */
    public Long total() {
        return System.currentTimeMillis() - start / 1000 ;
    }
}
