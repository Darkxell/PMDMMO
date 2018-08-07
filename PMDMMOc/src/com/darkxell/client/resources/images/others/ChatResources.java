package com.darkxell.client.resources.images.others;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.mechanics.chat.ChatBox;
import com.darkxell.client.resources.images.Sprite;
import com.darkxell.client.resources.images.SpriteFactory;

public class ChatResources
{

	public static final BufferedImage HEADER = createHeader(true, true, true);
	public static final Sprite FOOTER = new Sprite("/hud/chat/chatfooter.png", 210, 35);
	public static final Sprite ICON_CHANNEL_GLOBAL, ICON_CHANNEL_GUILD, ICON_CHANNEL_PRIVATE, ICON_SEND;

	static
	{
		Sprite ICONSBASE = new Sprite("/hud/chat/icons.png");
		ICON_CHANNEL_GLOBAL = SpriteFactory.instance().subSprite(ICONSBASE, 0, 0, 32, 32);
		ICON_CHANNEL_GUILD = SpriteFactory.instance().subSprite(ICONSBASE, 32, 0, 32, 32);
		ICON_CHANNEL_PRIVATE = SpriteFactory.instance().subSprite(ICONSBASE, 64, 0, 32, 32);
		ICON_SEND = SpriteFactory.instance().subSprite(ICONSBASE, 96, 0, 32, 32);
	}

	private static BufferedImage createHeader(boolean general, boolean guild, boolean whisper)
	{
		BufferedImage header = new BufferedImage(210, 35, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = header.createGraphics();
		g.setColor(new Color(32, 72, 104));
		g.fillRect(0, 0, 210, 35);
		g.setColor(new Color(72, 32, 248));
		g.fillRect(0, 32, 210, 35);
		g.setColor(new Color(69, 128, 248));
		g.fillRect(0, 33, 210, 1);
		// Draws categories
		g.setFont(g.getFont().deriveFont(12f));
		g.setColor(general ? new Color(97, 255, 58) : new Color(130, 130, 130));
		g.drawString("General", 60, 27);
		g.setColor(guild ? new Color(255, 170, 0) : new Color(130, 130, 130));
		g.drawString("Guild", 110, 27);
		g.setColor(whisper ? new Color(197, 73, 255) : new Color(130, 130, 130));
		g.drawString("Private", 160, 27);
		// Draws chat text
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(17f));
		g.drawString("Chat", 10, 20);
		g.setColor(Color.WHITE);
		g.drawString("Chat", 9, 19);
		g.dispose();
		return header;
	}

	private static BufferedImage buffer;
	private static Graphics2D buffergraphics;

	public static BufferedImage getFooter(BufferedImage dest)
	{
		if (buffer == null)
		{
			buffer = new BufferedImage(FOOTER.image().getWidth(), FOOTER.image().getHeight(), BufferedImage.TYPE_INT_RGB);
			buffergraphics = buffer.createGraphics();
		}
		buffergraphics.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
		buffergraphics.drawImage(FOOTER.image(), 0, 0, null);
		buffergraphics.drawImage(dest, 0, 3, null);
		buffergraphics.drawImage(ICON_SEND.image(), 177, 3, null);
		return buffer;
	}

	public static Color getColorFromChat(byte selectedcategory)
	{
		return selectedcategory == ChatBox.CHAT_GENERAL ? new Color(97, 255, 58)
				: selectedcategory == ChatBox.CHAT_GUILD ? new Color(255, 170, 0)
						: selectedcategory == ChatBox.CHAT_WHISPER ? new Color(197, 73, 255) : new Color(255, 255, 255);
	}

}
