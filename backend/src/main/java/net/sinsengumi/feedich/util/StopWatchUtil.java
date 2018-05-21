package net.sinsengumi.feedich.util;

import org.springframework.util.StopWatch;

public class StopWatchUtil {

    private final StopWatch stopWatch;

    public StopWatchUtil() {
        this.stopWatch = new StopWatch();
        this.stopWatch.start();
    }

    public long getTotalTimeMillis() {
        if (stopWatch.isRunning()) {
            stopWatch.stop();
        }

        return stopWatch.getTotalTimeMillis();
    }

    public double getTotalTimeSeconds() {
        if (stopWatch.isRunning()) {
            stopWatch.stop();
        }
        return stopWatch.getTotalTimeSeconds();
    }
}
