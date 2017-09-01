package com.darkxell.client.resources.music;

import java.io.File;

public class SoundsHolder {

	public static Song[] sounds = getSounds();
	private static String[] names;

	private static Song[] getSounds() {
		File[] all = new File("resources/music/").listFiles();
		names = new String[all.length];
		for (int i = 0; i < all.length; i++)
			names[i] = all[i].getName();
		Song[] ts = new Song[all.length];
		for (int i = 0; i < all.length; i++)
			ts[i] = new Song(all[i].getAbsolutePath());
		return ts;
	}

	public static Song getSong(String name) {
		for (int i = 0; i < names.length; i++)
			if (names[i].equals(name))
				return sounds[i];
		return null;
	}

}
