package com.darkxell.client.resources;

import java.awt.Color;

/** Class that holds various color objects and color related methods. */
public class Palette {

	public static final Color CHAT_GLOBAL = new Color(97, 255, 58);
	public static final Color CHAT_GUILD = new Color(255, 170, 0);
	public static final Color CHAT_PRIVATE = new Color(197, 73, 55);

	public static Color getColorFromHexa(String hexa) {
		return Color.decode(hexa);
	}

	public static String getHexaFromClor(Color c) {
		return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
	}

}
