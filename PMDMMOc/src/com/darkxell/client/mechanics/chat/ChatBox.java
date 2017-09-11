package com.darkxell.client.mechanics.chat;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.resources.images.ChatResources;

public class ChatBox {

	private Thread thread;
	public CustomTextfield textfield;

	/**
	 * Creates a new chatBox instance. Note that this instance will create it's
	 * own thread and connection to the server when created.
	 */
	public ChatBox() {
		this.thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Preparing FPS handling
				long startTime = System.nanoTime();
				long currentTime = startTime;
				long updateTime = 0;
				long timePerUpdate = 1000000000 / 60;

				while (Launcher.isRunning) {
					// Calculate elapsed time
					long elapsedTime = System.nanoTime() - currentTime;
					currentTime += elapsedTime;
					updateTime += elapsedTime / timePerUpdate;

					// If a tick has passed, update until there is no delayed
					// update
					while (updateTime >= 1) {
						update();
						--updateTime;
					}
					try {
						Thread.sleep(2);
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
		// TextRenderer.instance.render(g, "Chat", width - 50, 10);
		g.setColor(new Color(32, 72, 104));
		g.fillRect(0, 0, width, height);
		int headerheight = ChatResources.HEADER.getHeight() * width / ChatResources.HEADER.getWidth();
		g.drawImage(ChatResources.HEADER, 0, 0, width, headerheight, null);
		int footerheight = ChatResources.FOOTER.getHeight() * width / ChatResources.FOOTER.getWidth();
		g.drawImage(ChatResources.getFooter(ChatResources.ICON_CHANNEL_GLOBAL), 0, height - footerheight, width,
				footerheight, null);
		g.setColor(Color.WHITE);
		g.translate(width / 6, (height - footerheight) + (footerheight / 4));
		this.textfield.render(g, width / 3 * 2, footerheight / 2);
		g.translate(-width / 6,  -(height - footerheight) - (footerheight / 4));
	}

	private void update() {

	}
	
	public void send(){
		this.textfield.clear();
	}

}
