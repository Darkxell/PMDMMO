package com.darkxell.client.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.darkxell.client.launchable.Launcher;

public class Mouse implements MouseListener, MouseMotionListener {

	private static boolean isClicking = false;
	private static boolean isRightClicking = false;
	private static int x = 0, y = 0;

	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}

	public static boolean isClicking() {
		return isClicking;
	}

	public static boolean isRightClicking() {
		return isRightClicking;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		Launcher.stateManager.onMouseMove(x, y);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		isClicking = false;
		isRightClicking = false;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		if (Launcher.stateManager != null)
			Launcher.stateManager.onMouseMove(x, y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			isClicking = true;
		if (e.getButton() == MouseEvent.BUTTON3)
			isRightClicking = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isClicking && e.getButton() == MouseEvent.BUTTON1) {
			Launcher.stateManager.onMouseClick(x, y);
			isClicking = true;
		}
		if (isRightClicking && e.getButton() == MouseEvent.BUTTON3) {
			Launcher.stateManager.onMouseRightClick(x, y);
			isRightClicking = false;
		}
	}

}
