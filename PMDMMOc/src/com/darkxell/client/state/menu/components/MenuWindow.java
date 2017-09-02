package com.darkxell.client.state.menu.components;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.common.util.GameUtil;
import com.darkxell.common.util.Message;

public class MenuWindow
{
	/** Window colors. Fill = inside the window. out, middle, int describe the outline, from inside to outside. */
	static final Color fillT = new Color(31, 72, 104, 147), fillO = new Color(31, 72, 104, 255);
	static Color in, middle, out;
	public static final int MARGIN_X = 30, MARGIN_Y = 10;

	final Rectangle dimensions;
	protected Rectangle inside;
	public boolean isOpaque = false;

	public MenuWindow(Rectangle dimensions)
	{
		this.dimensions = dimensions;
	}

	private Color fill()
	{
		return this.isOpaque ? fillO : fillT;
	}

	private void initColors()
	{
		BufferedImage corner = MenuHudSpriteset.instance.windowCorner(GameUtil.NORTHWEST);
		out = new Color(corner.getRGB(corner.getWidth() - 1, 0), true);
		middle = new Color(corner.getRGB(corner.getWidth() - 1, 2), true);
		in = new Color(corner.getRGB(corner.getWidth() - 1, corner.getHeight() - 1), true);
	}

	public void render(Graphics2D g, Message name, int width, int height)
	{
		boolean hasName = name != null;
		int font = TextRenderer.CHAR_HEIGHT;
		Dimension corner = MenuHudSpriteset.instance.cornerSize;
		Dimension cornerName = MenuHudSpriteset.instance.cornerNameSize;
		this.inside = new Rectangle(this.dimensions.x + corner.width, this.dimensions.y + corner.height, this.dimensions.width - corner.width * 2,
				this.dimensions.height - corner.height * 2);
		Rectangle nameInside = new Rectangle(inside.x + cornerName.width, this.dimensions.y - font, Math.max(inside.width - corner.width * 2, hasName ? g
				.getFontMetrics().stringWidth(name.toString()) : 10), font);

		// Inside
		g.setColor(fill());
		g.fillRect(inside.x - 2, inside.y - 2, inside.width + 4, inside.height + 4);

		// Corners
		g.drawImage(MenuHudSpriteset.instance.windowCorner(GameUtil.NORTHEAST), (int) inside.getMaxX(), this.dimensions.y, null);
		g.drawImage(MenuHudSpriteset.instance.windowCorner(GameUtil.SOUTHEAST), (int) inside.getMaxX(), (int) inside.getMaxY(), null);
		g.drawImage(MenuHudSpriteset.instance.windowCorner(GameUtil.SOUTHWEST), this.dimensions.x, (int) inside.getMaxY(), null);
		g.drawImage(MenuHudSpriteset.instance.windowCorner(GameUtil.NORTHWEST), this.dimensions.x, this.dimensions.y, null);

		// Sides
		g.setColor(out);
		g.fillRect((hasName ? (int) nameInside.getMaxX() : inside.x), this.dimensions.y, (hasName ? inside.width - corner.width - nameInside.width + 2
				: inside.width), 2);
		g.fillRect(inside.x, (int) this.dimensions.getMaxY() - 2, inside.width, 2);
		g.fillRect(this.dimensions.x, inside.y, 2, inside.height);
		g.fillRect((int) this.dimensions.getMaxX() - 2, inside.y, 2, inside.height);

		g.setColor(middle);
		g.fillRect((hasName ? (int) nameInside.getMaxX() : inside.x), this.dimensions.y + 2, (hasName ? inside.width - corner.width - nameInside.width + 2
				: inside.width), (hasName ? 6 : 4));
		if (hasName) g.fillRect(inside.x, this.dimensions.y + 6, inside.width, 2);
		g.fillRect(inside.x, (int) this.dimensions.getMaxY() - 6, inside.width, 4);
		g.fillRect(this.dimensions.x + 2, inside.y, 10, inside.height);
		g.fillRect((int) this.dimensions.getMaxX() - 12, inside.y, 10, inside.height);

		g.setColor(in);
		g.fillRect(inside.x, this.dimensions.y + (hasName ? 8 : 6), inside.width, 2);
		g.fillRect(inside.x, (int) this.dimensions.getMaxY() - 8, inside.width, 2);
		g.fillRect(this.dimensions.x + 12, inside.y, 2, inside.height);
		g.fillRect((int) this.dimensions.getMaxX() - 14, inside.y, 2, inside.height);

		if (hasName)
		{
			// Inside
			g.setColor(fill());
			g.fillRect(nameInside.x - 2, nameInside.y - 2, nameInside.width + 4, nameInside.height + 4);
			g.fillRect(nameInside.x, (int) nameInside.getMaxY() + 2, nameInside.width, 4);

			// Corners
			g.drawImage(MenuHudSpriteset.instance.windowNameCorner(GameUtil.NORTHEAST), (int) nameInside.getMaxX(), nameInside.y - cornerName.height, null);
			g.drawImage(MenuHudSpriteset.instance.windowNameCorner(GameUtil.SOUTHEAST), (int) nameInside.getMaxX(), this.dimensions.y, null);
			g.drawImage(MenuHudSpriteset.instance.windowNameCorner(GameUtil.SOUTHWEST), inside.x, this.dimensions.y, null);
			g.drawImage(MenuHudSpriteset.instance.windowNameCorner(GameUtil.NORTHWEST), inside.x, nameInside.y - cornerName.height, null);

			// Sides
			g.setColor(out);
			g.fillRect(nameInside.x, nameInside.y - cornerName.height, nameInside.width, 2);
			g.fillRect(nameInside.x - cornerName.width, nameInside.y, 2, nameInside.height);
			g.fillRect((int) nameInside.getMaxX() + cornerName.width - 2, nameInside.y, 2, nameInside.height);

			g.setColor(middle);
			g.fillRect(nameInside.x, nameInside.y - cornerName.height + 2, nameInside.width, 4);
			g.fillRect(nameInside.x - cornerName.width + 2, nameInside.y, 8, nameInside.height);
			g.fillRect((int) nameInside.getMaxX() + cornerName.width - 10, nameInside.y, 8, nameInside.height);

			g.setColor(in);
			g.fillRect(nameInside.x, nameInside.y - 4, nameInside.width, 2);
			g.fillRect(nameInside.x - cornerName.width + 10, nameInside.y, 2, nameInside.height);
			g.fillRect((int) nameInside.getMaxX() + cornerName.width - 12, nameInside.y, 2, nameInside.height);

			TextRenderer.instance.render(g, name, nameInside.x + 2, nameInside.y + font * 1 / 5);
		}

	}

	public void update()
	{
		if (in == null) this.initColors();
	}

}
