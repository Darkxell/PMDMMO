package com.darkxell.client.resources.images;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Logger;

/** Sprite loader that runs in the background and gracefully handles resource errors. */
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

	/** @return The factory's instance. */
	public static SpriteFactory instance() {
		return instance;
	}

	/** Loads a singleton. Should be called when launching the game, before creating any object inheriting from the {@link Sprite} class.
	 *
	 * @throws AssertionError Could not load the default resource. In this case, the program should quit. */
	public static void load() {
		instance = new SpriteFactory();

		instance.defaultImg = Res.getBase("/missing.png");
		if (instance.defaultImg == null) { throw new AssertionError("No default image found!"); }

		new Thread(instance).start();
	}

	public static Sprite getDefaultSprite(int width, int height) {
		return new Sprite(instance.getDefault(width, height));
	}

	/** Wait for the loading queue to empty before proceeding. */
	public static void waitQueueDone() {
		while (!instance.requested.isEmpty()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private SpriteFactory() {}

	private BufferedImage defaultImg;

	/** Loaded images. */
	private final Map<String, BufferedImage> loaded = new HashMap<>();

	/** Queue of paths to load. */
	private LinkedList<String> requested = new LinkedList<>();

	/** Queues of sprites waiting for an image keyed on resource path. */
	private HashMap<String, ArrayList<Sprite>> requesters = new HashMap<>();

	/** Queues of sub-sprites waiting for an image keyed on resource path. */
	private HashMap<String, ArrayList<SubSprite>> subsprites = new HashMap<>();

	/** Removes references to the input image. May be called when that Image isn't necessary anymore and may be unloaded.
	 *
	 * @param image - The image to dispose of. */
	public void dispose(String image) {
		this.loaded.remove(image);
	}

	/** @param image - A path to an image.
	 * @return The image if it's loaded, a default image else. */
	BufferedImage get(String image) {
		return this.loaded.get(image);
	}

	/** @return A default image with the input dimensions. */
	BufferedImage getDefault(int width, int height) {
		if (width <= 0 || height <= 0) { return this.defaultImg; }

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = img.createGraphics();

		g.setPaint(new TexturePaint(this.defaultImg, new Rectangle()));
		g.fillRect(0, 0, this.defaultImg.getWidth(), this.defaultImg.getHeight());

		return img;
	}

	/** @return Has this image been loaded? */
	boolean isResourceLoaded(String image) {
		return this.loaded.containsKey(image) && !this.requested.contains(image);
	}

	/** Add requester to a queue in a thread-safe manner.
	 *
	 * @param requester Listener to add.
	 * @param path Path to listen to.
	 * @param listenerMap Listener queue to add in.
	 * @param <T> Listener type. */
	private <T> void addRequester(T requester, String path, Map<String, ArrayList<T>> listenerMap) {
		if (!listenerMap.containsKey(path)) {
			listenerMap.put(path, new ArrayList<>());
		}

		List<T> listeners = listenerMap.get(path);
		listeners.add(requester);
	}

	/** Attempt to load an image through the resource cache. Queue path on miss.
	 *
	 * @return Was there was a cache hit? */
	private boolean attemptLoad(Sprite requester, String path) {
		if (!Res.exists(path)) { return false; }

		if (requester != null) {
			this.addRequester(requester, path, this.requesters);
		}

		if (!this.requested.contains(path)) {
			this.requested.add(path);
		}

		return this.loaded.containsKey(path);
	}

	/** Queues an path to be loaded.
	 *
	 * @param requester Sprite that needs this path. This sprite will be notified when the load finishes.
	 * @param path The path to the path, relative to current working directory.
	 * @param width Image width. May be -1 if dimensions are unknown.
	 * @param height Image height. May be -1 if dimensions are unknown.
	 * @return Cached or default path, to be replaced upon callback. */
	public BufferedImage load(Sprite requester, String path, int width, int height) {
		if (!this.isResourceLoaded(path) && !this.attemptLoad(requester, path)) {
			// place interim image while it's loading
			this.loaded.put(path, this.getDefault(width, height));
		}

		return this.get(path);
	}

	/** Notify listeners to a specific request queue.
	 *
	 * @param path Path to notify
	 * @param img Retrieved image.
	 * @param requesterMap Queue map, keyed on resource path.
	 * @param callback What to do with image.
	 * @param <T> Type of sprite (see {@link Sprite} and {@link SubSprite}) */
	private <T> void notify(String path, BufferedImage img, Map<String, ArrayList<T>> requesterMap,
			BiConsumer<T, BufferedImage> callback) {
		if (!requesterMap.containsKey(path)) return;

		while (!requesterMap.get(path).isEmpty()) {
			T s = requesterMap.get(path).remove(0);
			if (s != null) callback.accept(s, img);
		}
		requesterMap.remove(path);
	}

	/** Load next image in path. */
	private void loadNext() {
		String path;

		try {
			path = this.requested.getFirst();
		} catch (NoSuchElementException e) {
			// rare race condition; in case it happens, just ignore it.
			Logger.w("SpriteFactory queue could not read first element.");
			return;
		}

		BufferedImage img = Res.getBase(path);
		if (img != null) {
			this.loaded.put(path, img);
		}

		this.requested.removeFirst();

		this.notify(path, img, this.requesters, (s, i) -> s.loaded(img));
		this.notify(path, img, this.subsprites, (s, i) -> s.sprite.loaded(Res.createimage(i, s.x, s.y, s.w, s.h)));
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

	/** Creates a Sprite as a sub-image of the Image of another Sprite. The created Sprite will have a default image until the target Sprite is loaded.
	 *
	 * @param source The source Sprite.
	 * @param x Tile x-coordinate.
	 * @param y Tile y-coordinate.
	 * @param width Tile width.
	 * @param height Tile height.
	 * @return A placeholder sub-sprite or cached sub-sprite. */
	public Sprite subSprite(Sprite source, int x, int y, int width, int height) {
		Sprite sub = new Sprite(source.path, width, height, false);
		if (this.isResourceLoaded(source.path)) {
			sub.loaded(Res.createimage(this.get(source.path), x, y, width, height));
		} else {
			sub.loaded(this.getDefault(width, height));
			this.addRequester(new SubSprite(sub, x, y, width, height), source.path, this.subsprites);
		}
		return sub;
	}
}
