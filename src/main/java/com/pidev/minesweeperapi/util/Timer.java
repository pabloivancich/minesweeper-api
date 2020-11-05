package com.pidev.minesweeperapi.util;

/**
 * Timer for games time tracking.
 */
public class Timer {
    /** The start, it's never null. */
    private Long start;

    /** Creates a new instance of the Timer.*/
    public Timer() {
        this.start = System.currentTimeMillis();
    }

    /**
     * Restart the timer.
     */
    public void restart() {
        this.start = System.currentTimeMillis();
    }

    /**
     * Retrieve the time (in seconds) that the timer worked since it started.
     * @return the time in seconds.
     */
    public Long total() {
        return (System.currentTimeMillis() - start) / 1000 ;
    }
}
