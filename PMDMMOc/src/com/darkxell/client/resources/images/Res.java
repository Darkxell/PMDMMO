package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.darkxell.common.util.Logger;

/** Class that holds commonly used resources utility. */
public class Res
{

	/** @param path - Path to a resource.
	 * @return True if the resource exists. */
	public static boolean exists(String path)
	{
		return Res.class.getResourceAsStream(path) != null;
	}

	/** Gets an image from the res folder as a BufferedImage. */
	static BufferedImage getBase(String path)
	{
		BufferedImage img = null;

		try
		{
			InputStream is = Res.class.getResourceAsStream(path);
			img = ImageIO.read(is);
		} catch (Exception e)
		{
			try
			{
				img = ImageIO.read(new File(path));
			} catch (Exception e1)
			{
				Logger.e("Could not read the following image file : " + path + "\nerrors : " + e + " / " + e1);
			}
		}
		Logger.instance().debug("Loaded resource from hard memory : " + path);
		return img;
	}

	/** Gets a part of a buffered Image. This method is safe: if the dimensions specified are out of the image's bound, this will throw no exception but may return unexpected results. */
	static BufferedImage createimage(BufferedImage image, int x, int y, int width, int height)
	{
		if (width > image.getWidth() || height > image.getHeight()) return image;
		if (x + width > image.getWidth()) x = image.getWidth() - width;
		if (y + height > image.getHeight()) y = image.getHeight() - height;
		return image.getSubimage(x, y, width, height);
	}

	/** Gets multiple player sprites next to each other in the specified BufferImage sheet file and returns them inside an array.
	 * 
	 * @param x the X position of the sprite in the player.png file.
	 * @param y the Y position of the sprite in the player.png file.
	 * @param size the size of the sprite. The sprite is a square, and the x/y coordinates are its top left corner.
	 * @param spritesAmmount the amount of sprites in the array. Also the length of the array, obviously. */
	static BufferedImage[] getSpriteRow(BufferedImage sheet, int x, int y, int size, int spritesAmmount)
	{
		return getSpriteRectRow(sheet, x, y, size, size, spritesAmmount);
	}

	/** Basically the same a s a getspriterow command, but the sprite doesn't have to be a square.
	 * 
	 * @see <code>getSpriteRow()</code> */
	static BufferedImage[] getSpriteRectRow(BufferedImage sheet, int x, int y, int width, int height, int spritesAmmount)
	{
		BufferedImage[] toreturn = new BufferedImage[spritesAmmount];
		for (int i = 0; i < toreturn.length; i++)
			toreturn[i] = Res.createimage(sheet, x + (i * width), y, width, height);
		return toreturn;
	}

	public static String[] getResourceFiles(String path)
	{
		List<String> filenames = new ArrayList<>();

		try
		{
			InputStream in = Res.class.getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String resource;

			while ((resource = br.readLine()) != null)
			{
				filenames.add(resource);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return filenames.toArray(new String[filenames.size()]);
	}

	public static InputStream get(String path)
	{
		return Res.class.getResourceAsStream(path);
	}

}
