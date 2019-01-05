package com.darkxell.client.resources.images.hud;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.mechanics.chat.ChatBox;
import com.darkxell.client.resources.images.Sprite;
import com.darkxell.client.resources.images.SubSprite;

public class ChatResources {

	public static final BufferedImage HEADER = createHeader(true, true, true);
	public static final Sprite FOOTER = new Sprite("/hud/chat/chatfooter.png", 210, 35);
	public static final Sprite FOOTER_LEFT;
	public static final Sprite FOOTER_RIGHT;
	public static final Sprite FOOTER_CENTER;
	public static final Sprite ICON_CHANNEL_GLOBAL, ICON_CHANNEL_GUILD, ICON_CHANNEL_PRIVATE, ICON_SEND;

	static {
		ICON_CHANNEL_GLOBAL = new SubSprite("/hud/chat/icons.png", 0, 0, 32, 32);
		ICON_CHANNEL_GUILD = new SubSprite("/hud/chat/icons.png", 32, 0, 32, 32);
		ICON_CHANNEL_PRIVATE = new SubSprite("/hud/chat/icons.png", 64, 0, 32, 32);
		ICON_SEND = new SubSprite("/hud/chat/icons.png", 96, 0, 32, 32);
		FOOTER_LEFT = new SubSprite("/hud/chat/chatfooter.png", 0, 0, 43, 35);
		FOOTER_RIGHT = new SubSprite("/hud/chat/chatfooter.png", 171, 0, 39, 35);
		FOOTER_CENTER = new SubSprite("/hud/chat/chatfooter.png", 43, 0, 125, 35);
	}

	private static BufferedImage createHeader(boolean general, boolean guild, boolean whisper) {
		BufferedImage header = new BufferedImage(210, 35, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = header.createGraphics();
		g.setColor(new Color(32, 72, 104));
		g.fillRect(0, 0, 210, 35);
		g.setColor(new Color(72, 32, 248));
		g.fillRect(0, 32, 210, 35);
		g.setColor(new Color(69, 128, 248));
		g.fillRect(0, 33, 210, 1);
		// Draws chat text
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(18f));
		g.drawString("Chat", 10, 22);
		g.setColor(Color.WHITE);
		g.drawString("Chat", 9, 21);
		g.dispose();
		return header;
	}

	private static BufferedImage buffer;
	private static Graphics2D buffergraphics;
	private static int lastheight = 0, lastwidth = 0;

	public static BufferedImage getFooter(BufferedImage dest, int height, int width) {
		if (buffer == null || lastwidth != width || lastheight != height) {
			buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			buffergraphics = buffer.createGraphics();
		}
		buffergraphics.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
		buffergraphics.drawImage(FOOTER_LEFT.image(), 0, 0, null);
		for (int i = FOOTER_LEFT.image().getWidth(); i < width; i += FOOTER_CENTER.image().getWidth())
			buffergraphics.drawImage(FOOTER_CENTER.image(), i, 0, null);
		buffergraphics.drawImage(FOOTER_RIGHT.image(), width - FOOTER_RIGHT.image().getWidth(), 0, null);
		buffergraphics.drawImage(dest, 0, 3, null);
		buffergraphics.drawImage(ICON_SEND.image(), width - ICON_SEND.image().getWidth() - 3, 3, null);
		return buffer;
	}

	public static Color getColorFromChat(byte selectedcategory) {
		return selectedcategory == ChatBox.CHAT_GENERAL ? new Color(97, 255, 58)
				: selectedcategory == ChatBox.CHAT_GUILD ? new Color(255, 170, 0)
						: selectedcategory == ChatBox.CHAT_WHISPER ? new Color(197, 73, 255) : new Color(255, 255, 255);
	}

}
