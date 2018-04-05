package com.darkxell.client.resources.images;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Direction;

public class MenuHudSpriteset
{

	private static final BufferedImage source = Res.getBase("/hud/menu_hud.png");

	public static final BufferedImage SELECTION_ARROW = Res.createimage(source, 0, 0, 10, 16);
	public static final BufferedImage SELECTED_ARROW = Res.createimage(source, 12, 0, 10, 16);
	public static final BufferedImage TAB_ARROW_LEFT = Res.createimage(source, 24, 0, 8, 8);
	public static final BufferedImage TAB_ARROW_RIGHT = Res.createimage(source, 24, 8, 8, 8);
	public static final BufferedImage WINDOW_CORNER_NW = Res.createimage(source, 0, 16, 16, 10);
	public static final BufferedImage WINDOW_CORNER_NE = Res.createimage(source, 16, 16, 16, 10);
	public static final BufferedImage WINDOW_CORNER_SW = Res.createimage(source, 0, 26, 16, 10);
	public static final BufferedImage WINDOW_CORNER_SE = Res.createimage(source, 16, 26, 16, 10);
	public static final BufferedImage WINDOW_NAME_CORNER_NW = Res.createimage(source, 0, 36, 14, 10);
	public static final BufferedImage WINDOW_NAME_CORNER_NE = Res.createimage(source, 18, 36, 14, 10);
	public static final BufferedImage WINDOW_NAME_CORNER_SW = Res.createimage(source, 0, 46, 14, 10);
	public static final BufferedImage WINDOW_NAME_CORNER_SE = Res.createimage(source, 18, 46, 14, 10);
	public static final BufferedImage NEXT_WINDOW_ARROW = Res.createimage(source, 0, 56, 11, 7);

	public static final Dimension cornerSize = new Dimension(WINDOW_CORNER_NW.getWidth(), WINDOW_CORNER_NW.getHeight());
	public static final Dimension cornerNameSize = new Dimension(WINDOW_NAME_CORNER_NW.getWidth(), WINDOW_NAME_CORNER_NW.getHeight());

	public static BufferedImage windowCorner(Direction direction)
	{
		switch (direction)
		{
			case NORTHEAST:
				return WINDOW_CORNER_NE;
			case SOUTHEAST:
				return WINDOW_CORNER_SE;
			case SOUTHWEST:
				return WINDOW_CORNER_SW;
			default:
				return WINDOW_CORNER_NW;
		}
	}

	public static BufferedImage windowNameCorner(Direction direction)
	{
		switch (direction)
		{
			case NORTHEAST:
				return WINDOW_NAME_CORNER_NE;
			case SOUTHEAST:
				return WINDOW_NAME_CORNER_SE;
			case SOUTHWEST:
				return WINDOW_NAME_CORNER_SW;
			default:
				return WINDOW_NAME_CORNER_NW;
		}
	}

}
