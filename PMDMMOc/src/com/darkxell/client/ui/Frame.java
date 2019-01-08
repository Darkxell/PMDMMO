package com.darkxell.client.ui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.darkxell.client.launchable.Launcher;

/** Main frame for the client. */
public class Frame extends JFrame implements WindowListener, WindowFocusListener {

	private static final long serialVersionUID = -6197661374941529294L;

	public Canvas canvas;

	public Frame() {
		super();
		this.setTitle("Pokemon Mystery Dungeon: Online Rescue Team");
		this.setSize(1300, 950);
		this.setMinimumSize(new Dimension(650, 450));
		// this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.addWindowListener(this);
		this.addWindowFocusListener(this);

		this.canvas = new Canvas();
		this.add(this.canvas);
		this.canvas.createBufferStrategy(2);

		this.canvas.addKeyListener(new Keys());
		Mouse m = new Mouse();
		this.canvas.addMouseListener(m);
		this.canvas.addMouseMotionListener(m);
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		Launcher.stopGame();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowGainedFocus(WindowEvent e) {}

	@Override
	public void windowLostFocus(WindowEvent e) {
		Keys.onLostFocus();
	}

}
