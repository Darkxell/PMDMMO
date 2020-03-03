package com.darkxell.client.mechanics.chat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.Sprites.HudSprites;

public class ChatResources {

    public static final BufferedImage HEADER = createHeader(true, true, true);

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
        buffergraphics.drawImage(HudSprites.chatFooter.left(), 0, 0, null);
        for (int i = HudSprites.chatFooter.left().getWidth(); i < width; i += HudSprites.chatFooter.center().getWidth())
            buffergraphics.drawImage(HudSprites.chatFooter.center(), i, 0, null);
        buffergraphics.drawImage(HudSprites.chatFooter.right(), width - HudSprites.chatFooter.right().getWidth(), 0, null);
        buffergraphics.drawImage(dest, 0, 3, null);
        buffergraphics.drawImage(HudSprites.chatIcons.send(), width - HudSprites.chatIcons.send().getWidth() - 3, 3, null);
        return buffer;
    }

    public static Color getColorFromChat(byte selectedcategory) {
        return selectedcategory == ChatBox.CHAT_GENERAL ? new Color(97, 255, 58)
                : selectedcategory == ChatBox.CHAT_GUILD ? new Color(255, 170, 0)
                        : selectedcategory == ChatBox.CHAT_WHISPER ? new Color(197, 73, 255) : new Color(255, 255, 255);
    }

}
