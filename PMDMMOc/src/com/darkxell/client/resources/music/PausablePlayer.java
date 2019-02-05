package com.darkxell.client.resources.music;

import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;

/** @author Durandal from stackOverflow */
public class PausablePlayer {

    private final static int FINISHED = 3;
    private final static int NOTSTARTED = 0;
    private final static int PAUSED = 2;
    private final static int PLAYING = 1;

    // the player actually doing all the work
    private final Player player;

    // locking object used to communicate with player thread
    private final Object playerLock = new Object();

    // status variable what player thread is doing/supposed to do
    private int playerStatus = NOTSTARTED;

    public PausablePlayer(final InputStream inputStream) throws JavaLayerException {
        this.player = new Player(inputStream);
    }

    public PausablePlayer(final InputStream inputStream, final AudioDevice audioDevice) throws JavaLayerException {
        this.player = new Player(inputStream, audioDevice);
    }

    /** Closes the player, regardless of current state. */
    public void close() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
        }
        try {
            player.close();
        } catch (final Exception e) {
            // ignore, we are terminating anyway
        }
    }

    public boolean isComplete() {
        return this.player.isComplete();
    }

    /** Pauses playback. Returns true if new state is PAUSED. */
    public boolean pause() {
        synchronized (playerLock) {
            if (playerStatus == PLAYING)
                playerStatus = PAUSED;
            return playerStatus == PAUSED;
        }
    }

    /** Starts playback (resumes if paused) */
    public void play() throws JavaLayerException {
        synchronized (playerLock) {
            switch (playerStatus) {
            case NOTSTARTED:
                final Runnable r = () -> playInternal();
                final Thread t = new Thread(r);
                t.setDaemon(true);
                t.setPriority(Thread.MAX_PRIORITY);
                playerStatus = PLAYING;
                t.start();
                break;
            case PAUSED:
                resume();
                break;
            default:
                break;
            }
        }
    }

    private void playInternal() {
        while (playerStatus != FINISHED) {
            try {
                if (!player.play(1))
                    break;
            } catch (final JavaLayerException e) {
                break;
            }
            // check if paused or terminated
            synchronized (playerLock) {
                while (playerStatus == PAUSED)
                    try {
                        playerLock.wait();
                    } catch (final InterruptedException e) {
                        // terminate player
                        break;
                    }
            }
        }
        close();
    }

    /** Resumes playback. Returns true if the new state is PLAYING. */
    public boolean resume() {
        synchronized (playerLock) {
            if (playerStatus == PAUSED) {
                playerStatus = PLAYING;
                playerLock.notifyAll();
            }
            return playerStatus == PLAYING;
        }
    }

    /** Stops playback. If not playing, does nothing */
    public void stop() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
            playerLock.notifyAll();
        }
    }
}