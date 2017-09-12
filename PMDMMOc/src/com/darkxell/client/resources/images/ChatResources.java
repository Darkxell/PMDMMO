package com.darkxell.client.resources.images;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public class ChatResources {

	public static final BufferedImage HEADER = createHeader(true, true, true);
	public static final BufferedImage FOOTER = Res.getBase("resources/hud/chat/chatfooter.png");

	private static final BufferedImage ICONSBASE = Res.getBase("resources/hud/chat/icons.png");
	public static final BufferedImage ICON_CHANNEL_GLOBAL = Res.createimage(ICONSBASE, 0, 0, 32, 32);
	public static final BufferedImage ICON_CHANNEL_GUILD = Res.createimage(ICONSBASE, 32, 0, 32, 32);
	public static final BufferedImage ICON_CHANNEL_PRIVATE = Res.createimage(ICONSBASE, 64, 0, 32, 32);
	public static final BufferedImage ICON_SEND = Res.createimage(ICONSBASE, 96, 0, 32, 32);

	private static BufferedImage createHeader(boolean general, boolean guild, boolean whisper) {
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

	public static BufferedImage getFooter(BufferedImage dest) {
		if (buffer == null) {
			buffer = new BufferedImage(FOOTER.getWidth(), FOOTER.getHeight(), BufferedImage.TYPE_INT_RGB);
			buffergraphics = buffer.createGraphics();	
		}
		buffergraphics.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
		buffergraphics.drawImage(FOOTER, 0, 0, null);
		buffergraphics.drawImage(dest, 0, 3, null);
		buffergraphics.drawImage(ICON_SEND, 177, 3, null);
		return buffer;
	}

}
