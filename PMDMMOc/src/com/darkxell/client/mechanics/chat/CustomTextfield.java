package com.darkxell.client.mechanics.chat;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;

public class CustomTextfield {

	@SuppressWarnings("unused")
	private int width = 0;
	@SuppressWarnings("unused")
	private int height = 0;
	private FontMetrics lastusedfontmetrics = null;
	private String charsbefore = "";
	private String charsafter = "";

	public CustomTextfield() {
	}

	public void render(Graphics2D g, int width, int height) {
		Shape lastclip = g.getClip();
		g.setClip(new Rectangle(0, 0, width, height));
		this.width = width;
		this.height = height;
		this.lastusedfontmetrics = g.getFontMetrics();
		// Starts to draw
		int beforelength = lastusedfontmetrics.stringWidth(charsbefore);
		int afterlength = lastusedfontmetrics.stringWidth(charsafter);
		g.setColor(Color.WHITE);
		if (beforelength + afterlength + 2 <= width || beforelength <= width / 2) {
			g.drawString(charsbefore, 0, height - 2);
			g.drawString(charsafter, beforelength + 2, height - 2);
			if (this.showCursor && !unselected) {
				g.setColor(Color.BLACK);
				g.drawLine(beforelength + 1, height, beforelength + 1, height - 10);
			}
		} else if (afterlength <= width / 2) {
			g.drawString(charsafter, width - afterlength, height - 2);
			g.drawString(charsbefore, width - afterlength - beforelength - 2, height - 2);
			if (this.showCursor && !unselected) {
				g.setColor(Color.BLACK);
				g.drawLine(width - afterlength - 1, height, width - afterlength - 1, height - 10);
			}
		} else {
			g.drawString(charsafter, width / 2 + 1, height - 2);
			g.drawString(charsbefore, width / 2 - 1 - beforelength, height - 2);
			if (this.showCursor && !unselected) {
				g.setColor(Color.BLACK);
				g.drawLine(width / 2, height, width / 2, height - 10);
			}
		}
		// Resets the graphics object
		g.setClip(lastclip);
	}

	private int counter = 0;
	private boolean showCursor = false;

	public void update() {
		++counter;
		this.showCursor = counter % 50 < 20;
	}

	/** Inserts a String in the textfield at the cursor location. */
	public void insertString(String s) {
		charsbefore = charsbefore + s;
	}

	public void clear() {
		this.charsbefore = "";
		this.charsafter = "";
	}

	public void pressLeft() {
		if (!charsbefore.equals("")) {
			char swich = charsbefore.charAt(charsbefore.length() - 1);
			charsbefore = charsbefore.substring(0, charsbefore.length() - 1);
			charsafter = swich + charsafter;
		}
	}

	public void pressRight() {
		if (!charsafter.equals("")) {
			char swich = charsafter.charAt(0);
			charsbefore = charsbefore + swich;
			charsafter = charsafter.substring(1, charsafter.length());
		}
	}

	public void pressDelete() {
		if (!charsbefore.equals(""))
			charsbefore = charsbefore.substring(0, charsbefore.length() - 1);
	}

	/** Returns the String contained in this textfield. */
	public String getContent() {
		return charsbefore + charsafter;
	}

	private boolean unselected = false;

	/**
	 * Sets this textfield as the selected one. This simply forces the display
	 * of the cursor so you don't have two textfields with blinking cursors on a
	 * state aty the same time.
	 */
	public void setSelection(boolean a) {
		this.unselected = !a;
	}

	/**
	 * Should be called by a state that uses this textfield upon recieving a
	 * KeyPressed event.
	 */
	public void onKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT && !unselected)
			this.pressLeft();
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && !unselected)
			this.pressRight();
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !unselected)
			this.pressDelete();
	}

	/**
	 * Should be called by a state that uses this textfield upon recieving a
	 * KeyTyped event.
	 */
	public void onKeyTyped(KeyEvent e) {
		if (e.getKeyChar() != '\b' && !unselected)
			this.insertString(e.getKeyChar() + "");
	}

}
