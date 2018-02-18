package com.darkxell.client.resources.music;

import java.io.File;

public class SoundsHolder {

	public static final Song[] soundtracks = getSoundtracks();
	public static final Song[] sfx = getSfx();
	private static String[] soundtrackNames, sfxNames;

	private static Song[] getSoundtracks() {
		File[] all = new File("resources/music/").listFiles();
		soundtrackNames = new String[all.length];
		for (int i = 0; i < all.length; i++)
			soundtrackNames[i] = all[i].getName();
		Song[] ts = new Song[all.length];
		for (int i = 0; i < all.length; i++)
			ts[i] = new Song(all[i].getAbsolutePath());
		return ts;
	}

	private static Song[] getSfx() {
		File[] all = new File("resources/sound/").listFiles();
		sfxNames = new String[all.length];
		for (int i = 0; i < all.length; i++)
			sfxNames[i] = all[i].getName().substring(0, all[i].getName().length() - 4);
		Song[] ts = new Song[all.length];
		for (int i = 0; i < all.length; i++)
			ts[i] = new Song(all[i].getAbsolutePath());
		return ts;
	}

	public static Song getSong(String name) {
		for (int i = 0; i < soundtrackNames.length; i++)
			if (soundtrackNames[i].equals(name))
				return soundtracks[i];
		return null;
	}

	public static Song getSfx(String name) {
		for (int i = 0; i < sfxNames.length; i++)
			if (sfxNames[i].equals(name))
				return sfx[i];
		return null;
	}

}
