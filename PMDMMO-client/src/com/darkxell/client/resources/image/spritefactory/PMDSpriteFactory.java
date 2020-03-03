package com.darkxell.client.resources.image.spritefactory;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Logger;

public class PMDSpriteFactory implements Runnable {

    private static PMDSpriteFactory instance;

    /**
     * Initializes the Sprite Factory.
     */
    public static void initialize() {
        if (instance != null)
            throw new AssertionError("Sprite Factory already initialized.");

        instance = new PMDSpriteFactory();
        if (instance.defaultImg == null)
            throw new AssertionError("Default image could not be loaded.");

        new Thread(instance).start();
    }

    public static PMDSpriteFactory instance() {
        return instance;
    }

    /**
     * Call this method to wait for all requested images to be loaded by the Sprite Factory.
     */
    public static void waitQueueDone() {
        while (!instance.requested.isEmpty())
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    /**
     * The image to use as default, when an invalid path is provided; or when an image is required before it's loaded.
     */
    private final BufferedImage defaultImg;
    /**
     * Associates an image's path to its current loaded value. In general, it will correspond to the actual loaded
     * image, however it may be a default image if the image isn't loaded yet.
     */
    private final HashMap<String, BufferedImage> loaded = new HashMap<>();
    /**
     * The list of all images that are requested to be loaded.
     */
    private final LinkedList<String> requested = new LinkedList<>();

    private PMDSpriteFactory() {
        this.defaultImg = Res.getBase("/missing.png");
    }

    /**
     * @return The Image with the input path.
     */
    BufferedImage get(String path) {
        if (!this.loaded.containsKey(path)) {
            Logger.w("Tried to use an unrequested image!");
            return this.getDefault();
        }
        return this.loaded.get(path);
    }

    /**
     * @return An image to use by default if an image is missing.
     */
    BufferedImage getDefault() {
        return this.defaultImg;
    }

    /**
     * @return Is the image with the input path loaded?
     */
    boolean isLoaded(String path) {
        return this.loaded.containsKey(path) && !this.requested.contains(path);
    }

    /**
     * Notifies the Sprite Factory to load the image with the input sprite's path.
     * 
     * @return An image to use for the Sprite.
     */
    BufferedImage load(PMDSprite sprite) {
        return this.load(sprite.path);
    }

    /**
     * Notifies the Sprite Factory to load the image with the input path.
     * 
     * @return An image to use for this path.
     */
    BufferedImage load(String path) {
        if (this.isLoaded(path))
            return this.get(path);
        else if (!this.requested.contains(path)) {
            this.requested.add(path);
            this.loaded.put(path, this.getDefault());
        }

        return this.get(path);
    }

    /**
     * Attempts to load the next image.
     * 
     * @return Was the image successfully loaded?
     */
    private boolean loadNext() {
        String path;

        try {
            path = this.requested.getFirst();
        } catch (NoSuchElementException e) {
            // rare race condition; in case it happens, just ignore it.
            Logger.w("SpriteFactory queue could not read first element.");
            return false;
        }

        BufferedImage img = Res.getBase(path);
        if (img != null)
            this.loaded.put(path, img);

        this.requested.removeFirst();

        return img != null;
    }

    @Override
    public void run() {
        while (Launcher.isRunning) {
            try {
                if (this.requested.isEmpty()) {
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

}
