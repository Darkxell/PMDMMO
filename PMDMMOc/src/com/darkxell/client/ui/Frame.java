package com.darkxell.client.ui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.images.Sprite;

/** Main frame for the client. */
public class Frame extends JFrame implements WindowListener
{
	public static class FrameIconSprite extends Sprite
	{

		public FrameIconSprite(String path)
		{
			super(path);
		}

		@Override
		protected void loaded(BufferedImage img)
		{
			super.loaded(img);
			if (Persistance.frame != null) Persistance.frame.setIconImage(this.image());
		}

	}

	private static final long serialVersionUID = -6197661374941529294L;

	public Canvas canvas;

	public Frame()
	{
		super();
		this.setTitle("Pokemon Mystery Dungeon: Online Rescue Team");
		this.setSize(1000, 800);
		this.setMinimumSize(new Dimension(650, 400));
		// this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.addWindowListener(this);

		this.canvas = new Canvas();
		this.add(this.canvas);
		this.canvas.createBufferStrategy(2);

		this.canvas.addKeyListener(new Keys());
		Mouse m = new Mouse();
		this.canvas.addMouseListener(m);
		this.canvas.addMouseMotionListener(m);
	}

	@Override
	public void windowActivated(WindowEvent e)
	{}

	@Override
	public void windowClosed(WindowEvent e)
	{}

	@Override
	public void windowClosing(WindowEvent e)
	{
		Launcher.stopGame();
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{}

	@Override
	public void windowDeiconified(WindowEvent e)
	{}

	@Override
	public void windowIconified(WindowEvent e)
	{}

	@Override
	public void windowOpened(WindowEvent e)
	{}

}
