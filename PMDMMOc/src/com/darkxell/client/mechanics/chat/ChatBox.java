package com.darkxell.client.mechanics.chat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.images.others.ChatResources;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public class ChatBox {

	private static final int INTERLINE_SMALL = 15;
	private static final int INTERLINE_BIG = 20;

	private Thread thread;
	public CustomTextfield textfield;
	public ArrayList<ChatMessage> messages = new ArrayList<>();
	public byte selectedcategory = CHAT_GENERAL;
	public static final byte CHAT_GENERAL = 1;
	public static final byte CHAT_GUILD = 2;
	public static final byte CHAT_WHISPER = 3;

	private ChatClientEndpoint endpoint;

	/**
	 * Creates a new chatBox instance. Note that this instance will create it's
	 * own thread and connection to the server when created.
	 */
	public ChatBox() {
		this.textfield = new CustomTextfield();
		try {
			String loc = "ws://" + ClientSettings.getSetting(ClientSettings.SERVER_ADDRESS) + "chat";
			Logger.i("Started chat endpoint creation at address : " + loc);
			URI servwslocation = new URI(loc);
			endpoint = new ChatClientEndpoint(servwslocation, this);
		} catch (URISyntaxException e) {
			Logger.e("Could not connect to the chat socket : " + e.toString());
		}
		this.thread = new Thread(new Runnable() {
			@Override
			public void run() {
				long timePerUpdate = 1000 / 60;
				Logger.instance().debug("Started chat updater thread!");
				endpoint.connect();
				while (Launcher.isRunning) {
					update();
					try {
						Thread.sleep(timePerUpdate);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		this.thread.start();
	}

	private int boxwidth = 0;
	private int boxheight = 0;
	private int footerheight = 0;
	private int headerheight = 0;

	public void render(Graphics2D g, int width, int height, boolean chatFocus) {
		this.boxwidth = width;
		this.boxheight = height;
		g.setColor(new Color(32, 72, 104));
		g.fillRect(0, 0, width, height);
		headerheight = ChatResources.HEADER.getHeight() * width / ChatResources.HEADER.getWidth();
		g.drawImage(ChatResources.HEADER, 0, 0, width, headerheight, null);
		// Draw the footer
		footerheight = ChatResources.FOOTER.getHeight() * width / ChatResources.FOOTER.getWidth();
		g.drawImage(
				ChatResources.getFooter(selectedcategory == CHAT_GENERAL ? ChatResources.ICON_CHANNEL_GLOBAL
						: selectedcategory == CHAT_GUILD ? ChatResources.ICON_CHANNEL_GUILD
								: selectedcategory == CHAT_WHISPER ? ChatResources.ICON_CHANNEL_PRIVATE : null),
				0, height - footerheight, width, footerheight, null);
		g.setColor(Color.WHITE);
		g.translate(width / 6, (height - footerheight) + (footerheight / 4));
		this.textfield.render(g, width / 3 * 2, footerheight / 2);
		g.translate(-width / 6, -(height - footerheight) - (footerheight / 4));
		if (!chatFocus) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, height - footerheight, width, footerheight);
		}
		// Displays the messages
		int iterator = 0;
		g.setColor(Color.WHITE);
		for (int i = height - footerheight - 20; i > headerheight + 20
				&& iterator < messages.size(); i -= INTERLINE_BIG) {
			ChatMessage m = messages.get(messages.size() - 1 - iterator);
			++iterator;
			int taglength = g.getFontMetrics().stringWidth("[" + m.tag + "]"),
					messagelength = g.getFontMetrics().stringWidth(m.sender + " : " + m.message);
			if (taglength + messagelength + 3 < width - 10) {
				// if fits in one line
				if (!m.tag.equals("")) {
					g.setColor(m.tagColor);
					g.drawString("[" + m.tag + "]", 10, i);
				}
				g.setColor(m.senderColor);
				g.drawString(m.sender + " : ", 13 + taglength, i);
				g.setColor(m.messageColor);
				g.drawString(m.message, 13 + taglength + g.getFontMetrics().stringWidth(m.sender + " : "), i);
			} else {
				// if takes multiple lines
				int letterx = taglength + 13, linesammount = (taglength + messagelength + 3) / (width - 10);
				char[] completemessage = (m.sender + " : " + m.message).toCharArray();
				i -= INTERLINE_SMALL * linesammount;
				if (!m.tag.equals("")) {
					g.setColor(m.tagColor);
					g.drawString("[" + m.tag + "]", 10, i);
				}
				g.setColor(m.senderColor);
				for (int j = 0; j < completemessage.length; j++) {
					if (j == m.sender.length() + 3)
						g.setColor(m.messageColor);
					g.drawString(completemessage[j] + "", letterx, i);
					letterx += g.getFontMetrics().stringWidth(completemessage[j] + "");
					if (letterx > width - 10) {
						letterx = 10;
						i += INTERLINE_SMALL;
					}
				}
				i -= INTERLINE_SMALL * linesammount;
			}
		}

		if (this.endpoint.connectionStatus() != ChatClientEndpoint.CONNECTED) {
			g.setColor(Palette.TRANSPARENT_GRAY);
			g.fillRect(0, headerheight, width, 30);
			g.setColor(Color.RED);
			if (this.endpoint.connectionStatus() == ChatClientEndpoint.CONNECTING)
				g.drawString("Connecting to chat...", 10, headerheight + 20);
			else if (this.endpoint.connectionStatus() == ChatClientEndpoint.FAILED)
				g.drawString("Connection failed.", 10, headerheight + 20);
		}
	}

	private void update() {
		if (this.textfield != null)
			this.textfield.update();
	}

	public void send() {
		if (this.endpoint != null && this.endpoint.connectionStatus() == ChatClientEndpoint.CONNECTED) {
			JsonObject mess = new JsonObject().add("action", "message").add("tag", "DEV")
					.add("sender", ClientSettings.getSetting(ClientSettings.LOGIN))
					.add("message", this.textfield.getContent()).add("tagcolor", Palette.getHexaFromClor(Color.RED))
					.add("messagecolor", Palette.getHexaFromClor(Color.WHITE))
					.add("sendercolor", Palette.getHexaFromClor(Palette.CHAT_GLOBAL));
			this.endpoint.sendMessage(mess.toString());
			this.textfield.clear();
		} else {
			messages.add(new ChatMessage("Error", "Message not sent: no active connection to the chat server.",
					Color.RED, Color.RED));
			Logger.e("Could not send message, endpoint does not exist.");
			this.textfield.clear();
		}
	}

	public void onClick(int x, int y) {
		if (y > boxheight - footerheight && x < boxwidth / 6) {
			if (selectedcategory == CHAT_WHISPER)
				selectedcategory = CHAT_GENERAL;
			else
				++selectedcategory;
		}
	}

}
