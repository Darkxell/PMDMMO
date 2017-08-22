package com.darkxell.client.launchable;

import com.darkxell.client.ui.Frame;

/**Launching class of the client*/
public class Launcher {
	
	public static Frame frame;
	public static Game instance;
	public static Thread thread;

	public static void main(String[] args) {
		
		frame = new Frame();
		thread = new Thread(instance = new Game());
		thread.start();
		
	}

}
