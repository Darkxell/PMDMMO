package com.darkxell.client.mechanics.chat;

import java.awt.Graphics2D;
import java.util.LinkedList;

import com.darkxell.client.renderers.TextRenderer.PMDChar;

public class CustomTextfield {

	private int width;
	private int height;
	private LinkedList<PMDChar> chars;
	private int cursorposition;

	public CustomTextfield() {
	}

	public void render(Graphics2D g, int width, int height) {
		this.width = width;
		this.height = height;
	}

	private int counter = 0;

	public void update() {
		++counter;

	}

	/** Inserts a String in the textfield at the cursor location. */
	public void insertString(String s) {

	}

}
