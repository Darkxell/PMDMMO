package com.darkxell.client.state.dungeon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import com.darkxell.client.graphics.TextRenderer;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.common.util.language.Message;

public class DungeonLogger {

    public static final int MESSAGE_TIME = 60 * 6;

    private int arrowtick = 0;
    /** True if the logger is being shown full-screen. */
    private boolean isFullscreen = false;
    /** The last width the messages window were calculated for. Set to -1 to force reloading. */
    private int lastWidth = -1;
    /** Lists the last 40 messages. */
    private LinkedList<Message> log;
    private int maxDisplayedMessages = 3;
    /** Maximum offset when fullscreen. */
    private int maxOffset = 0;
    private int messageOffset = 0;
    /** The currently displayed messages. */
    private LinkedList<Message> messages;
    /** The window to draw messages in. */
    MenuWindow messagesWindow;
    private int messageTime = 0;
    private boolean needsUpdate = true;
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

    public boolean isVisible() {
        return this.isFullscreen || (this.messagesWindow != null && this.messageTime > 0);
    }

    /** @return The last 40 messages that were displayed to the Player. */
    public Message[] log() {
        return this.log.toArray(new Message[this.log.size()]);
    }

    private void reloadMessages(int width, int height) {
        int w = width - 40;
        int h = this.isFullscreen ? height - 40 : w * 5 / 28;
        int y = this.isFullscreen ? 20 : height - h - 5;
        this.messagesWindow = new MenuWindow(new Rectangle((width - w) / 2, y, w, h));

        ArrayList<String> toReturn = new ArrayList<String>();
        for (int i = 0; i < this.messages.size(); ++i)
            if (this.messages.get(i) != null) {
                toReturn.addAll(TextRenderer.splitLines(this.messages.get(i).toString(), w - 40));
                toReturn.add(null);
            }

        this.messages.clear();
        for (String m : toReturn)
            if (m == null)
                this.messages.add(null);
            else
                this.messages.add(new Message(m, false));

        this.maxDisplayedMessages = this.isFullscreen
                ? this.messagesWindow.inside().height / (TextRenderer.height() + 5)
                : 3;
        if (this.isFullscreen) {
            this.maxOffset = this.messageOffset = -(TextRenderer.height() + 5)
                    * (this.displayedMessages() - this.maxDisplayedMessages);
            if (this.messageOffset > 0)
                this.messageOffset = 0;
        }
        this.lastWidth = width;
        this.needsUpdate = false;
    }

    public void render(Graphics2D g, int width, int height) {
        if (this.lastWidth != width)
            this.needsUpdate = true;
        if (this.needsUpdate)
            this.reloadMessages(width, height);
        if (!this.isVisible())
            return;

        this.messagesWindow.render(g, null, width, height);
        Shape clip = g.getClip();
        g.setClip(this.messagesWindow.inside());

        int y = this.messagesWindow.dimensions.y + MenuWindow.MARGIN_Y + this.messageOffset;
        for (int i = 0; i < this.messages.size(); ++i) {
            Message s = this.messages.get(i);
            if (s == null) {
                g.setColor(new Color(255, 255, 255, 128));
                g.drawLine(0, y - 4, width, y - 4);
                g.setColor(new Color(0, 0, 0, 128));
                g.drawLine(0, y - 3, width, y - 3);
            } else {
                TextRenderer.render(g, s, this.messagesWindow.dimensions.x + 20, y);
                y += TextRenderer.height() + 5;
            }
        }
        g.setClip(clip);

        if (this.isFullscreen && this.arrowtick < DialogScreen.ARROW_TICK_LENGTH / 2) {
            BufferedImage arrow = DialogScreen.arrow;
            int x = this.messagesWindow.dimensions.x + this.messagesWindow.dimensions.width / 2 - arrow.getWidth() / 2;
            if (this.maxOffset - this.messageOffset <= -2) {
                y = (int) this.messagesWindow.inside().getMaxY();
                g.drawImage(arrow, x, y, null);
            }

            if (this.messageOffset <= -2) {
                y = (int) this.messagesWindow.inside().getMinY();
                g.drawImage(arrow, x, y, arrow.getWidth(), -arrow.getHeight(), null);
            }
        }
    }

    public void scrollDown() {
        if (this.isFullscreen && this.messageOffset >= this.maxOffset + 2)
            this.messageOffset -= 2;
    }

    public void scrollUp() {
        if (this.isFullscreen && this.messageOffset <= -2)
            this.messageOffset += 2;
    }

    public void setFullscreen(boolean fullscreen) {
        this.isFullscreen = fullscreen;
        this.messages.clear();
        if (this.isFullscreen)
            this.messages.addAll(this.log);
        else {
            this.messageTime = 0;
            this.messageOffset = 0;
        }
        this.needsUpdate = true;
    }

    /** Shows a message to the player. */
    public void showMessage(Message message) {
        message.addReplacement("<player>", Persistence.player.getTeamLeader().getNickname());
        this.log.add(message);
        this.messages.add(message);
        if (this.log.size() > 40)
            this.log.poll();
        this.messageTime = MESSAGE_TIME;
        this.needsUpdate = true;
    }

    public void showMessages(Message... messages) {
        for (Message message : messages)
            this.showMessage(message);
    }

    public void update() {
        if (this.messageTime > 0 && !this.isFullscreen) {
            if (this.messageTime == 1) {
                this.messages.clear();
                this.messageOffset = 0;
            }
            --this.messageTime;
        }

        ++this.arrowtick;
        if (this.arrowtick >= DialogScreen.ARROW_TICK_LENGTH)
            this.arrowtick = 0;

        if (!this.isFullscreen && this.messageOffset > -(this.displayedMessages() - this.maxDisplayedMessages)
                * (TextRenderer.height() + 5))
            this.messageOffset -= 2;
    }

}
