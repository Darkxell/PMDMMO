
package com.darkxell.client.launchable.render;

import static com.darkxell.client.launchable.render.RenderProfile.PROFILE_UNDEFINED;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.resources.image.spritefactory.PMDSpriteFactory;

public abstract class GameLoop implements Runnable {
    /**
     * Nanoseconds per second.
     */
    public static final long NS_PER_S = (long) 1e9;

    /**
     * Nanoseconds per millisecond.
     */
    public static final long NS_PER_MS = (long) (NS_PER_S / 1e3);

    /**
     * How much the sleep interval should change by, if necessary (ns).
     */
    public static final long SLEEP_DIFF_INTERVAL_NS = (long) 5e4;

    /**
     * Target updates per second (Hz).
     *
     * @see #UPDATE_NS
     */
    public static final int TARGET_UPS = 60;

    /**
     * Target nanoseconds per update (ns).
     *
     * @see #TARGET_UPS
     */
    private static final double UPDATE_NS = NS_PER_S / TARGET_UPS;

    /**
     * Timestamp of when the previous tick began.
     *
     * @see System#nanoTime()
     */
    private long lastTime;

    /**
     * Duration (in ns) of when the UPS measurement interval began.
     */
    private long intervalDuration;

    /**
     * How long the next sleep duration should be (in ns) to avoid unnecessary wakeups.
     */
    private long nextSleepDuration;

    /**
     * How many "frames behind" the thread is. In other words, in order to meet a target UPS measure of
     * {@link #TARGET_UPS}, we would have to immediately render this amount of frames.
     */
    private double framesMissing;

    /**
     * The last interval's average UPS. If the update thread is consistently slow for whatever reason, this may not be
     * reflective of the actual UPS.
     */
    private double currentUPS;

    /**
     * How many updates have been performed since the last UPS calculation. This measure will be divided by
     * {@link #intervalDuration} to yield {@link #currentUPS} after at least 1 second.
     */
    private int nextUPSUpdates;

    protected RenderProfile getProcessingProfile() {
        return PROFILE_UNDEFINED;
    }

    /**
     * Get the current UPS to the nearest whole number.
     *
     * @return Updates per second in the last update cycle.
     */
    public int ticksPerSecond() {
        return (int) this.currentUPS;
    }

    /**
     * Run while the game is running and the running profile matches this class's profile implementation.
     *
     * @return If the loop should continue.
     */
    protected boolean keepRunning() {
        return Launcher.isRunning && Launcher.getProcessingProfile() == getProcessingProfile();
    }

    @Override
    public void run() {
        this.lastTime = System.nanoTime();
        this.framesMissing = 0;
        this.intervalDuration = 0;
        this.nextSleepDuration = (long) 2e6; // 2 ms
        this.nextUPSUpdates = 0;
        this.currentUPS = 0;

        PMDSpriteFactory.waitQueueDone();

        while (this.keepRunning()) {
            this.update();

            try {
                long sleepMs = this.nextSleepDuration / NS_PER_MS;
                // in extreme cases, we may end up never explicitly sleeping, although this is not a problem as threads
                // that hog CPU time are preempted anyways.
                if (sleepMs >= 0)
                    Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * One tick of the game loop.
     */
    abstract protected void tick();

    /**
     * Adjust length of sleep, accounting for possible negative values.
     *
     * @param multiple How much time to adjust, in terms of {@link #SLEEP_DIFF_INTERVAL_NS}.
     */
    private void adjustSleep(int multiple) {
        this.nextSleepDuration += multiple * SLEEP_DIFF_INTERVAL_NS;

        // never sleep for negative time.
        this.nextSleepDuration = Math.max(this.nextSleepDuration, 0);
    }

    private void update() {
        long now = System.nanoTime();
        long elapsedTime = now - this.lastTime;
        this.lastTime = now;

        this.intervalDuration += elapsedTime;
        this.framesMissing += elapsedTime / UPDATE_NS;

        int catchUpFrames = (int) this.framesMissing;

        // perform catchUpFrames ticks
        for (int i = 0; i < catchUpFrames; i++)
            this.tick();

        // if we performed 0 ticks this update, we were too quick, so we lengthen our sleep time.
        // otherwise, always try to stay as close to 1 tick per update as possible.

        // this algorithm often yields update frequencies a tiny fraction of a hertz slower than the target rate, but
        // it is usually on the order of mHz to cHz. and in any case, it prevents us from having to wake up the update
        // thread for no reason.
        this.adjustSleep(catchUpFrames == 0 ? 1 : -1);

        this.nextUPSUpdates += catchUpFrames;
        this.framesMissing -= catchUpFrames; // leave decimal part

        // gather metrics over past second and reset
        if (this.intervalDuration >= NS_PER_S) {
            this.currentUPS = (double) this.nextUPSUpdates * NS_PER_S / this.intervalDuration;
            this.intervalDuration = 0;
            this.nextUPSUpdates = 0;
        }
    }
}
