package com.darkxell.client.launchable;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.SpriteFactory;
import com.darkxell.common.util.Util;

/**
 * (EXPERIMENTAL) A combined updater and renderer to avoid synchronization issues between game ticks and render events.
 *
 * @see com.darkxell.client.launchable.Updater
 * @see com.darkxell.client.launchable.Renderer
 */
public class UpdaterAndRenderer implements Runnable {
    /**
     * Nanoseconds per second.
     */
    public static final long NS_PER_S = (long) 1e9;

    /**
     * Target updates per second.
     *
     * @see #UPDATE_NS
     */
    public static final int TARGET_UPS = 60;

    /**
     * Target nanoseconds per update.
     *
     * @see #TARGET_UPS
     */
    private static final double UPDATE_NS = NS_PER_S / TARGET_UPS;

    /**
     * Timestamp of the launch time of this thread.
     *
     * @see System#nanoTime()
     */
    private long startTime;

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

    /**
     * Get the current UPS to the nearest whole number.
     *
     * @return Updates per second in the last update cycle.
     */
    public int currentUPS() {
        return (int) this.currentUPS;
    }

    protected boolean keepRunning() {
        return Launcher.isRunning && Launcher.getProcessingProfile() == Launcher.PROFILE_SYNCHRONIZED;
    }

    @Override
    public void run() {
        this.startTime = System.nanoTime();
        this.lastTime = this.startTime;
        this.framesMissing = 0;
        this.intervalDuration = 0;
        this.nextUPSUpdates = 0;
        this.currentUPS = 0;

        try {
            while (SpriteFactory.instance().hasLoadingSprites()) {
                Thread.sleep(5);
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        while (this.keepRunning()) {
            this.update();

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void tickUpdate() {
        Persistance.stateManager.update();

        if (Persistance.frame == null || !Persistance.frame.isVisible()) {
            return;
        }

        BufferStrategy bf = Persistance.frame.canvas.getBufferStrategy();
        Graphics2D g = (Graphics2D) bf.getDrawGraphics();
        int width = Persistance.frame.canvas.getWidth(), height = Persistance.frame.canvas.getHeight();
        g.clearRect(0, 0, width, height);

        Persistance.stateManager.render(g, width, height);
        TextRenderer.render(g, "V " + Util.GAME_VERSION, 5, 5);

        g.dispose();
        bf.show();
    }

    private void update() {
        long now = System.nanoTime();
        long elapsedTime = now - this.lastTime;
        this.lastTime = now;

        this.intervalDuration += elapsedTime;
        this.framesMissing += elapsedTime / UPDATE_NS;

        int catchUpFrames = (int) this.framesMissing;

        // perform catchUpFrames ticks, but at least once per update.
        this.tickUpdate();
        for (int i = 0; i < catchUpFrames - 1; i++) {
            this.tickUpdate();
        }

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
