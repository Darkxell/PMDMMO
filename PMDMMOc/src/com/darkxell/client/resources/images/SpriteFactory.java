package com.darkxell.client.resources.images;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Logger;

/** Calls that loads Images in a separate Thread for fluidity, and handles missing sprites without errors. */
public class SpriteFactory implements Runnable
{

	private static class SubSprite
	{
		private Sprite sprite;
		private int x, y, w, h;

		SubSprite(Sprite sprite, int x, int y, int w, int h)
		{
			this.sprite = sprite;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}

	private static SpriteFactory instance;

	/** @return The factory's instance. */
	public static SpriteFactory instance()
	{
		return instance;
	}

	/** Loads the SpriteFactory. Should be called when launching the game, before creating any object inheriting from the {@link Sprite} class.
	 * 
	 * @return <code>true</code> if it loaded properly. If <code>false</code>, game should close immediately as it won't be playable. */
	public static boolean load()
	{
		instance = new SpriteFactory();
		instance.defaultImg = Res.getBase("/missing.png");
		if (instance.defaultImg == null)
		{
			Logger.e("Fatal error: No default image found!");
			Launcher.stopGame();
			return false;
		}
		new Thread(instance).start();
		return true;
	}

	private BufferedImage defaultImg;
	/** Maps paths -> images. If not loaded, this maps to default images that can be displayed without errors. */
	private HashMap<String, BufferedImage> loaded = new HashMap<>();
	/** Images awaiting for loading. */
	private LinkedList<String> requested = new LinkedList<>();
	/** Maps Images -> Sprites that need that Image. */
	private HashMap<String, ArrayList<Sprite>> requesters = new HashMap<>();
	/** Maps Images -> SubSprites that need that Image. */
	private HashMap<String, ArrayList<SubSprite>> subsprites = new HashMap<>();

	private SpriteFactory()
	{}

	/** Removes references to the input image. May be called when that Image isn't necessary anymore and may be unloaded.
	 * 
	 * @param image - The image to dispose of. */
	public void dispose(String image)
	{
		this.loaded.remove(image);
	}

	/** @param image - A path to an image.
	 * @return The image if it's loaded, a default image else. */
	BufferedImage get(String image)
	{
		return this.loaded.get(image);
	}

	/** @return A default image with the input dimensions. */
	private BufferedImage getDefault(int width, int height)
	{
		if (width == -1 || height == -1) return this.defaultImg;

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();

		int cols = width / this.defaultImg.getWidth();
		int lines = height / this.defaultImg.getHeight();

		for (int x = 0; x <= cols; ++x)
			for (int y = 0; y <= lines; ++y)
				g.drawImage(this.defaultImg, x, y, null);

		return img;
	}

	/** @return <code>true</code> if the factory is currently loading sprites. */
	public boolean hasLoadingSprites()
	{
		return !this.requested.isEmpty();
	}

	/** @return <code>true</code> If the input Image has been loaded and is ready for use. */
	boolean isLoaded(String image)
	{
		return this.loaded.containsKey(image) && !this.requested.contains(image);
	}

	/** Registers the input Image to be loaded.
	 * 
	 * @param requester - The Sprite that needs this Image. This Sprite will be called when the Image is loaded and will have the new loaded Image set.
	 * @param image - The path to the Image to load.
	 * @param width - The width of the Image.
	 * @param height - The height of the Image. May be -1 if the dimensions are unknown or don't matter. These dimensions are used only to create a default Image with dimensions matching the desired Image, <b>the loaded Image will not have its dimensions set to these input dimensions.</b>
	 * @return The image if it was already loaded, or a default image that can already be used else. */
	BufferedImage load(Sprite requester, String image, int width, int height)
	{
		if (!this.isLoaded(image))
		{
			if (!this.requested.contains(image))
			{
				this.requested.add(image);
				this.requesters.put(image, new ArrayList<>());
				this.loaded.put(image, this.getDefault(width, height));
			}
			this.requesters.get(image).add(requester);
		}

		return this.get(image);
	}

	@Override
	public void run()
	{
		// /!\ WARNING: All methods called here must be thread-safe. /!\

		final int loaded = 1, noload = 100;
		int sleepTime;
		while (Launcher.isRunning)
		{
			if (this.requested.size() == 0) sleepTime = noload;
			else
			{
				String path = this.requested.getFirst();

				BufferedImage img = Res.getBase(path);
				if (img != null)
				{
					this.loaded.put(path, img);
					this.requested.removeFirst();
					ArrayList<Sprite> requesters = new ArrayList<>(this.requesters.remove(path)); // Put into new ArrayList to be thread-safe.
					for (int s = 0; s < requesters.size(); ++s)
						requesters.get(s).loaded(img);
					if (this.subsprites.containsKey(path))
					{
						ArrayList<SubSprite> subsprites = new ArrayList<>(this.subsprites.remove(path)); // Put into new ArrayList to be thread-safe.
						for (int s = 0; s < subsprites.size(); ++s)
						{
							SubSprite sub = subsprites.get(s);
							sub.sprite.loaded(Res.createimage(this.get(path), sub.x, sub.y, sub.w, sub.h));
						}
					}
				}

				sleepTime = loaded;
			}

			try
			{
				Thread.sleep(sleepTime);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/** Creates a Sprite that's a subimage of the Image of another Sprite. The created Sprite will have a default image until the target Sprite is loaded.
	 * 
	 * @param source - The source Sprite to get a subimage of.
	 * @param x <b>y width height</b> - The part of the Image to get.
	 * @return The created subsprite. */
	public Sprite subSprite(Sprite source, int x, int y, int width, int height)
	{
		Sprite sub = new Sprite(source.path, width, height, false);
		if (this.isLoaded(source.path)) sub.loaded(Res.createimage(this.get(source.path), x, y, width, height));
		else
		{
			sub.loaded(this.getDefault(width, height));
			if (!this.subsprites.containsKey(source.path)) this.subsprites.put(source.path, new ArrayList<>());
			this.subsprites.get(source.path).add(new SubSprite(sub, x, y, width, height));
		}
		return sub;
	}

}
