package com.darkxell.client.resources.images;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.resources.Res;

/**
 * Sprite loader that runs in the background and gracefully handles resource errors.
 */
public class SpriteFactory implements Runnable {
    private static class SubSprite {
        private Sprite sprite;
        private int x, y, w, h;

        SubSprite(Sprite sprite, int x, int y, int w, int h) {
            this.sprite = sprite;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    private static SpriteFactory instance;

    /**
     * @return The factory's instance.
     */
    public static SpriteFactory instance() {
        return instance;
    }

    /**
     * Loads a singleton. Should be called when launching the game, before creating any object inheriting from the
     * {@link Sprite} class.
     *
     * @throws AssertionError Could not load the default resource. In this case,
     */
    public static void load() {
        instance = new SpriteFactory();

        instance.defaultImg = Res.getBase("/missing.png");
        if (instance.defaultImg == null) {
            throw new AssertionError("No default image found!");
        }

        new Thread(instance).start();
    }

    public static void waitQueueDone() {
        try {
            instance.getLoadQueueLatch().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Field for notifying threads on load completion.
     */
    private CountDownLatch loadQueueDone;

    /**
     * Default image to render if a path is not in {@link #loaded}.
     */
    private BufferedImage defaultImg;

    /**
     * Loaded images.
     */
    private final HashMap<String, BufferedImage> loaded = new HashMap<>();

    /**
     * Queue of paths to load.
     */
    private final LinkedList<String> requested = new LinkedList<>();

    /**
     * Queues of sprites waiting for an image keyed on resource path.
     */
    private final HashMap<String, ArrayList<Sprite>> requesters = new HashMap<>();

    /**
     * Queues of sub-sprites waiting for an image keyed on resource path.
     */
    private final HashMap<String, ArrayList<SubSprite>> subsprites = new HashMap<>();

    private SpriteFactory() {
    }

    /**
     * Signals waiting threads (if any) that all sprites have been loaded.
     *
     * @see CountDownLatch
     */
    private synchronized void signalLoadComplete() {
        if (this.loadQueueDone != null) {
            this.loadQueueDone.countDown();
            this.loadQueueDone = null;
        }
    }

    /**
     * Removes references to the input image.
     *
     * @param image - The image to dispose of.
     */
    public void dispose(String image) {
        this.loaded.remove(image);
    }

    /**
     * @param image - A path to an image.
     * @return The image if it's loaded, a default image else.
     */
    public BufferedImage get(String image) {
        return this.loaded.get(image);
    }

    /**
     * @return A default image with the input dimensions.
     */
    public BufferedImage getDefault(int width, int height) {
        if (width <= 0 || height <= 0) {
            return this.defaultImg;
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g = img.createGraphics();

        int cols = width / this.defaultImg.getWidth();
        int lines = height / this.defaultImg.getHeight();

        for (int x = 0; x <= cols; ++x) {
            for (int y = 0; y <= lines; ++y) {
                g.drawImage(this.defaultImg, x, y, null);
            }
        }

        return img;
    }

    /**
     * @return The latch that signals if all sprites are loaded. The returned latch will always eventually be
     * signaled before discarding.
     * @see CountDownLatch
     */
    private synchronized CountDownLatch getLoadQueueLatch() {
        if (this.loadQueueDone == null) {
            this.loadQueueDone = new CountDownLatch(1);
        }
        return this.loadQueueDone;
    }

    /**
     * @return Has this image been loaded?
     */
    public boolean isResourceLoaded(String image) {
        return this.loaded.containsKey(image) && !this.requested.contains(image);
    }

    /**
     * Attempt to load an image through the resource cache. Queue path on miss.
     *
     * @return Was there was a cache hit?
     */
    private boolean attemptLoad(Sprite requester, String path) {
        if (!Res.exists(path)) {
            return false;
        }

        if (requester != null) {
            if (!this.requesters.containsKey(path)) {
                this.requesters.put(path, new ArrayList<>());
            }
            this.requesters.get(path).add(requester);
        }

        if (!this.requested.contains(path)) {
            this.requested.add(path);
        }

        return this.loaded.containsKey(path);
    }

    /**
     * Queues an path to be loaded.
     *
     * @param requester Sprite that needs this path. This sprite will be notified when the load finishes.
     * @param path      The path to the path, relative to current working directory.
     * @param width     Image width. May be -1 if dimensions are unknown.
     * @param height    Image height. May be -1 if dimensions are unknown.
     * @return Cached or default path, to be replaced upon callback.
     */
    public BufferedImage load(Sprite requester, String path, int width, int height) {
        if (!this.isResourceLoaded(path) && !this.attemptLoad(requester, path)) {
            // place interim image while it's loading
            this.loaded.put(path, this.getDefault(width, height));
        }

        return this.get(path);
    }

    /**
     * Notify sub-sprites waiting on {@code path} with resulting {@code img}.
     *
     * @see #notifySubSprites(String, BufferedImage)
     */
    private void notifyImage(String path, BufferedImage img) {
        synchronized (this.requesters) {
            ArrayList<Sprite> requesters = this.requesters.remove(path);
            if (requesters == null) {
                return;
            }
            for (Sprite s : requesters) {
                s.loaded(img);
            }
        }
    }

    /**
     * Notify sub-sprites waiting on {@code path} with resulting {@code img}.
     *
     * @see #notifyImage(String, BufferedImage)
     */
    private void notifySubSprites(String path, BufferedImage img) {
        synchronized (this.subsprites) {
            ArrayList<SubSprite> subSprites = this.subsprites.remove(path);
            if (subSprites == null) {
                return;
            }
            for (SubSprite s : subSprites) {
                s.sprite.loaded(Res.createimage(img, s.x, s.y, s.w, s.h));
            }
        }
    }

    /**
     * Load next image in path.
     */
    private void loadNext() {
        String path = this.requested.getFirst();

        BufferedImage img = Res.getBase(path);
        if (img != null) {
            this.loaded.put(path, img);
            this.notifyImage(path, img);
            this.notifySubSprites(path, img);
        }

        this.requested.removeFirst();
    }

    @Override
    public void run() {
        while (Launcher.isRunning) {
            try {
                if (this.requested.isEmpty()) {
                    this.signalLoadComplete();
                    Thread.sleep(100);
                } else {
                    this.loadNext();
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a Sprite as a sub-image of the Image of another Sprite. The created Sprite will have a default image
     * until the target Sprite is loaded.
     *
     * @param source The source Sprite.
     * @param x      Tile x-coordinate.
     * @param y      Tile y-coordinate.
     * @param width  Tile width.
     * @param height Tile height.
     * @return A placeholder sub-sprite or cached sub-sprite.
     */
    public Sprite subSprite(Sprite source, int x, int y, int width, int height) {
        Sprite sub = new Sprite(source.path, width, height, false);
        if (this.isResourceLoaded(source.path)) {
            sub.loaded(Res.createimage(this.get(source.path), x, y, width, height));
        } else {
            sub.loaded(this.getDefault(width, height));
            if (!this.subsprites.containsKey(source.path)) {
                this.subsprites.put(source.path, new ArrayList<>());
            }
            this.subsprites.get(source.path).add(new SubSprite(sub, x, y, width, height));
        }
        return sub;
    }
}
