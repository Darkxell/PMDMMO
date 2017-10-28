package com.darkxell.client.resources;

import java.awt.Color;

/** Class that holds various color objects and color related methods. */
public class Palette {

	public static final Color CHAT_GLOBAL = new Color(97, 255, 58);
	public static final Color CHAT_GUILD = new Color(255, 170, 0);
	public static final Color CHAT_PRIVATE = new Color(197, 73, 55);
	
	public static final Color TEAM = new Color(0, 155, 155);
	public static final Color TEAM_HP_GREEN = new Color(41, 255, 49);
	public static final Color TEAM_HP_RED = new Color(255, 132, 90);
	public static final Color TEAM_XP_BLUE = new Color(100, 200, 255);
	public static final Color TEAM_XP_PURPLE = new Color(132, 90, 255);

	public static final Color TRANSPARENT_GRAY = new Color(0, 0, 0, 120);

	public static final Color FONT_BLUE = new Color(0, 255, 255);
	public static final Color FONT_GREEN = new Color(90, 255, 90);
	public static final Color FONT_RED = new Color(255, 132, 90);
	public static final Color FONT_YELLOW = new Color(255, 255, 0);


	public static Color getColorFromHexa(String hexa) {
		return Color.decode(hexa);
	}

	public static String getHexaFromClor(Color c) {
		return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
	}

}
