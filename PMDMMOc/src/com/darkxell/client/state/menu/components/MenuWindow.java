package com.darkxell.client.state.menu.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.common.util.GameUtil;

public class MenuWindow
{
	/** Window colors. Fill = inside the window. out, middle, int describe the outline, from inside to outside. */
	static Color fill, in, middle, out;
	public static final int MARGIN_X = 30, MARGIN_Y = 10;

	final Rectangle dimensions;

	public MenuWindow(Rectangle dimensions)
	{
		this.dimensions = dimensions;
	}

	private void initColors()
	{
		BufferedImage corner = MenuHudSpriteset.instance.windowCorner(GameUtil.NORTHWEST);
		fill = new Color(corner.getRGB(MenuHudSpriteset.instance.cornerSize.width - 1, MenuHudSpriteset.instance.cornerSize.height - 1), true);
		in = new Color(corner.getRGB(MenuHudSpriteset.instance.cornerSize.width - 1, MenuHudSpriteset.instance.cornerSize.height - 3), true);
		middle = new Color(corner.getRGB(MenuHudSpriteset.instance.cornerSize.width - 1, MenuHudSpriteset.instance.cornerSize.height - 5), true);
		out = new Color(corner.getRGB(MenuHudSpriteset.instance.cornerSize.width - 1, MenuHudSpriteset.instance.cornerSize.height - 7), true);
	}

	public void render(Graphics2D g, int width, int height)
	{
		Rectangle inside = new Rectangle(this.dimensions.x + MenuHudSpriteset.instance.cornerSize.width, this.dimensions.y
				+ MenuHudSpriteset.instance.cornerSize.height, this.dimensions.width - MenuHudSpriteset.instance.cornerSize.width * 2, this.dimensions.height
				- MenuHudSpriteset.instance.cornerSize.height * 2);

		// Corners
		g.drawImage(MenuHudSpriteset.instance.windowCorner(GameUtil.NORTHEAST), (int) inside.getMaxX(), this.dimensions.y, null);
		g.drawImage(MenuHudSpriteset.instance.windowCorner(GameUtil.SOUTHWEST), (int) inside.getMaxX(), (int) inside.getMaxY(), null);
		g.drawImage(MenuHudSpriteset.instance.windowCorner(GameUtil.SOUTHEAST), this.dimensions.x, (int) inside.getMaxY(), null);
		g.drawImage(MenuHudSpriteset.instance.windowCorner(GameUtil.NORTHWEST), this.dimensions.x, this.dimensions.y, null);

		// inside
		g.setColor(fill);
		g.fill(inside);

		// Sides
		Color[] colors = new Color[]
		{ out, middle, in, fill };
		int x = inside.x, y = this.dimensions.y, w = inside.width, h = 2;
		for (Color color : colors)
		{
			g.setColor(color);
			g.fillRect(x, y, w, h);
			y += 2;
		}

		y = (int) this.dimensions.getMaxY() - 2;
		for (Color color : colors)
		{
			g.setColor(color);
			g.fillRect(x, y, w, h);
			y -= 2;
		}

		x = this.dimensions.x;
		w = 2;
		y = inside.y;
		h = inside.height;
		for (Color color : colors)
		{
			g.setColor(color);
			g.fillRect(x, y, color == middle ? 8 : color == fill ? 4 : w, h);
			x += color == middle ? 8 : w;
		}

		x = (int) this.dimensions.getMaxX() - 2;
		for (Color color : colors)
		{
			g.setColor(color);
			g.fillRect(color == middle ? x - 6 : color == fill ? x - 2 : x, y, color == middle ? 8 : color == fill ? 4 : w, h);
			x -= color == middle ? 8 : w;
		}

	}

	public void update()
	{
		if (fill == null) this.initColors();
	}

}
