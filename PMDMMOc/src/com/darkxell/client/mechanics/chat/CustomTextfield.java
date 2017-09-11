package com.darkxell.client.mechanics.chat;

import java.awt.Graphics2D;

public class CustomTextfield {

	private int width = 0;
	private int height = 0;
	private String charsbefore = "";
	private String charsafter = "";

	public CustomTextfield() {
	}

	public void render(Graphics2D g, int width, int height) {
		this.width = width;
		this.height = height;
		g.drawString(charsbefore + charsafter, 0, height);
	}

	private int counter = 0;

	public void update() {
		++counter;

	}

	/** Inserts a String in the textfield at the cursor location. */
	public void insertString(String s) {
		charsbefore = charsbefore + s;
	}

	public void clear() {
		this.charsbefore = "";
		this.charsafter = "";
	}

}
