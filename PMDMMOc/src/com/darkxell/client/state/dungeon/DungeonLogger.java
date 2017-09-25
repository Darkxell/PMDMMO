package com.darkxell.client.state.dungeon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.LinkedList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.common.util.Message;

public class DungeonLogger {

	public static final int MESSAGE_HEIGHT = TextRenderer.CHAR_HEIGHT + 5;
	public static final int MESSAGE_TIME = 60 * 6;

	/**
	 * The last width the messages window were calculated for. Set to -1 to
	 * force reloading.
	 */
	private int lastWidth = -1;
	/** Lists the last 40 messages. */
	private LinkedList<Message> log;
	private int messageOffset = 0;
	/** The currently displayed messages. */
	private LinkedList<Message> messages;
	/** The window to draw messages in. */
	private MenuWindow messagesWindow;
	private int messageTime = 0;
	public final DungeonState parent;

	public DungeonLogger(DungeonState parent) {
		this.parent = parent;
		this.log = new LinkedList<Message>();
		this.messages = new LinkedList<Message>();
	}

	public int displayedMessages() {
		int count = 0;
		for (Message m : this.messages)
			if (m != null)
				++count;
		return count;
	}

	public void hideMessages() {
		this.messageTime = 0;
		this.messageOffset = 0;
		this.messages.clear();
	}

	/** @return The last 40 messages that were displayed to the Player. */
	public Message[] log() {
		return this.log.toArray(new Message[this.log.size()]);
	}

	private void reloadMessages(int width, int height) {
		int w = width - 40;
		int h = w * 5 / 28;
		this.messagesWindow = new MenuWindow(new Rectangle((width - w) / 2, height - h - 20, w, h));

		ArrayList<String> toReturn = new ArrayList<String>();
		for (Message m : this.messages)
			if (m != null)
				toReturn.addAll(TextRenderer.instance.splitLines(m.toString(), w - 40));

		this.messages.clear();
		for (int i = 0; i < toReturn.size(); ++i) {
			this.messages.add(new Message(toReturn.get(i), false));
			if (i < toReturn.size() - 1)
				this.messages.add(null);
		}

		this.lastWidth = width;
	}

	public void render(Graphics2D g, int width, int height) {
		if (this.messageTime == 0)
			return;
		if (this.lastWidth != width)
			this.reloadMessages(width, height);

		this.messagesWindow.render(g, null, width, height);
		Shape clip = g.getClip();
		g.setClip(this.messagesWindow.inside());

		int y = this.messagesWindow.dimensions.y + MenuWindow.MARGIN_Y + this.messageOffset;
		for (int i = 0; i < this.messages.size(); ++i) {
			Message s = this.messages.get(i);
			if (s == null) {
				g.setColor(Color.WHITE);
				g.drawLine(0, y - 4, width, y - 4);
				g.setColor(Color.BLACK);
				g.drawLine(0, y - 3, width, y - 3);
			} else {
				TextRenderer.instance.render(g, s, this.messagesWindow.dimensions.x + 20, y);
				y += MESSAGE_HEIGHT;
			}
		}
		g.setClip(clip);
	}

	/** Shows a message to the player. */
	public void showMessage(Message message) {
		message.addReplacement("<player>", Persistance.player.getPokemon().getNickname());
		this.log.add(message);
		this.messages.add(message);
		if (this.log.size() > 40)
			this.log.poll();
		this.messageTime = MESSAGE_TIME;
		this.lastWidth = -1;
		if (!this.parent.isMain())
			this.hideMessages();
	}

	public void showMessages(Message... messages) {
		for (Message message : messages)
			this.showMessage(message);
	}

	public void update() {
		if (this.messageTime > 0) {
			if (this.messageTime == 1) {
				this.messages.clear();
				this.messageOffset = 0;
			}
			--this.messageTime;
		}

		if (this.messageOffset > -(this.displayedMessages() - 3) * MESSAGE_HEIGHT)
			--this.messageOffset;
	}

}
