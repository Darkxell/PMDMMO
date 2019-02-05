package com.darkxell.client.resources.images;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Logger;

/** Sprite loader that runs in the background and gracefully handles resource errors. */
public class SpriteFactory implements Runnable {

    private static SpriteFactory instance;

    /** @return The factory's instance. */
    public static SpriteFactory instance() {
        return instance;
    }

    /**
     * Loads a singleton. Should be called when launching the game, before creating any object inheriting from the
     * {@link Sprite} class.
     *
     * @throws AssertionError Could not load the default resource. In this case, the program should quit.
     */
    public static void load() {
        instance = new SpriteFactory();

        instance.defaultImg = Res.getBase("/missing.png");
        if (instance.defaultImg == null)
            throw new AssertionError("No default image found!");

        new Thread(instance).start();
    }

    /** Wait for the loading queue to empty before proceeding. */
    public static void waitQueueDone() {
        while (!instance.toLoad.isEmpty())
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    private BufferedImage defaultImg;

    /** Loaded images. */
    private final Map<String, BufferedImage> images = new HashMap<>();
    /** Split subimages. */
    private final Map<String, BufferedImage> subimages = new HashMap<>();
    private final LinkedList<String> toLoad = new LinkedList<>();

    private SpriteFactory() {
    }

    /**
     * Attempt to load an image through the resource cache. Queue path on miss.
     *
     * @return Was there was a cache hit?
     */
    private boolean attemptLoad(String path) {
        if (!Res.exists(path))
            return false;

        if (!this.toLoad.contains(path))
            this.toLoad.add(path);

        return this.images.containsKey(path);
    }

    /**
     * Removes references to the input image. May be called when that Image isn't necessary anymore and may be unloaded.
     *
     * @param image - The image to dispose of.
     */
    public void dispose(String image) {
        this.images.remove(image);
    }

    /**
     * @param  image - A path to an image.
     * @return       The image if it's loaded, a default image else.
     */
    BufferedImage get(String image) {
        return this.images.get(image);
    }

    /** @return A default image with the input dimensions. */
    BufferedImage getDefault(int width, int height) {
        if (width <= 0 || height <= 0)
            return this.defaultImg;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g = img.createGraphics();

        g.setPaint(new TexturePaint(this.defaultImg, new Rectangle()));
        g.fillRect(0, 0, this.defaultImg.getWidth(), this.defaultImg.getHeight());

        return img;
    }

    BufferedImage getSubimage(String image, int x, int y, int width, int height) {
        String id = image + "|" + x + "|" + y + "|" + width + "|" + height;
        if (!this.subimages.containsKey(id))
            if (!this.isResourceLoaded(image))
                return this.getDefault(width, height);
            else {
                BufferedImage i = this.get(image);
                if (i.getWidth() >= x + width && i.getHeight() >= y + height)
                    this.subimages.put(id, i.getSubimage(x, y, width, height));
                else
                    return this.getDefault(width, height);
            }

        return this.subimages.get(id);
    }

    /** @return Has this image been loaded? */
    boolean isResourceLoaded(String image) {
        return this.images.containsKey(image) && !this.toLoad.contains(image);
    }

    /**
     * Queues an path to be loaded.
     *
     * @param  requester Sprite that needs this path. This sprite will be notified when the load finishes.
     * @param  path      The path to the path, relative to current working directory.
     * @param  width     Image width. May be -1 if dimensions are unknown.
     * @param  height    Image height. May be -1 if dimensions are unknown.
     * @return           Cached or default path, to be replaced upon callback.
     */
    public BufferedImage load(String path, int width, int height) {
        if (!this.isResourceLoaded(path) && !this.attemptLoad(path))
            // place interim image while it's loading
            this.images.put(path, this.getDefault(width, height));

        return this.get(path);
    }

    /** Load next image in path. */
    private void loadNext() {
        String path;

        try {
            path = this.toLoad.getFirst();
        } catch (NoSuchElementException e) {
            // rare race condition; in case it happens, just ignore it.
            Logger.w("SpriteFactory queue could not read first element.");
            return;
        }

        BufferedImage img = Res.getBase(path);
        if (img != null)
            this.images.put(path, img);

        this.toLoad.removeFirst();
    }

    @Override
    public void run() {
        while (Launcher.isRunning)
            try {
                if (this.toLoad.isEmpty())
                    Thread.sleep(100);
                else {
                    this.loadNext();
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
