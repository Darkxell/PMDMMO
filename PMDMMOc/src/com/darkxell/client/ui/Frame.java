package com.darkxell.client.ui;

import java.awt.Canvas;

import javax.swing.JFrame;

/** Main frame for the client. */
public class Frame extends JFrame
{
	private static final long serialVersionUID = -6197661374941529294L;

	public Canvas canvas;

	public Frame()
	{
		super();
		this.setTitle("Pokémon Mystery Dungeon: Online Rescue Team");
		this.setSize(1000, 800);
		//this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		this.canvas = new Canvas();
		this.add(this.canvas);
		this.canvas.createBufferStrategy(2);

		this.canvas.addKeyListener(new Keys());
		Mouse m = new Mouse();
		this.canvas.addMouseListener(m);
		this.canvas.addMouseMotionListener(m);
	}

}
