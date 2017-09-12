package com.darkxell.client.mechanics.chat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.resources.images.ChatResources;
import com.darkxell.common.util.Logger;

public class ChatBox {

	private Thread thread;
	public CustomTextfield textfield;
	public ArrayList<String> messages = new ArrayList<>();

	/**
	 * Creates a new chatBox instance. Note that this instance will create it's
	 * own thread and connection to the server when created.
	 */
	public ChatBox() {
		this.thread = new Thread(new Runnable() {

			@Override
			public void run() {
				long timePerUpdate = 1000 / 60;
				Logger.instance().debug("Started chat updater thread!");
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
		this.textfield = new CustomTextfield();
	}

	public void render(Graphics2D g, int width, int height) {
		g.setColor(new Color(32, 72, 104));
		g.fillRect(0, 0, width, height);
		int headerheight = ChatResources.HEADER.getHeight() * width / ChatResources.HEADER.getWidth();
		g.drawImage(ChatResources.HEADER, 0, 0, width, headerheight, null);
		// Draw the footer
		int footerheight = ChatResources.FOOTER.getHeight() * width / ChatResources.FOOTER.getWidth();
		g.drawImage(ChatResources.getFooter(ChatResources.ICON_CHANNEL_GLOBAL), 0, height - footerheight, width,
				footerheight, null);
		g.setColor(Color.WHITE);
		g.translate(width / 6, (height - footerheight) + (footerheight / 4));
		this.textfield.render(g, width / 3 * 2, footerheight / 2);
		g.translate(-width / 6, -(height - footerheight) - (footerheight / 4));
		// Displays the messages
		int iterator = 0;
		g.setColor(Color.WHITE);
		for (int i = height - footerheight - 20; i > headerheight + 20 && iterator < messages.size(); i -= 20) {
			g.drawString("User : " + messages.get(messages.size() - 1 - iterator), 10, i);
			++iterator;
		}
	}

	private void update() {
		if (this.textfield != null)
			this.textfield.update();
	}

	public void send() {
		this.messages.add(this.textfield.getContent());
		this.textfield.clear();
	}

}
