package com.darkxell.client.resources.music;

import com.darkxell.client.resources.Res;

public class SoundsHolder {

	public static final Song[] soundtracks = getSoundtracks();
	public static final Song[] sfx = getSfx();
	private static String[] soundtrackNames, sfxNames;

	private static Song[] getSoundtracks() {
		String[] all = Res.getResourceFiles("/music/");
		soundtrackNames = new String[all.length];
		for (int i = 0; i < all.length; i++)
			soundtrackNames[i] = all[i];
		Song[] ts = new Song[all.length];
		for (int i = 0; i < all.length; i++)
			ts[i] = new Song("resources/music/" + all[i]);
		return ts;
	}

	private static Song[] getSfx() {
		String[] all = Res.getResourceFiles("/sound/");
		sfxNames = new String[all.length];
		for (int i = 0; i < all.length; i++)
			sfxNames[i] = all[i].substring(0, all[i].length() - 4);
		Song[] ts = new Song[all.length];
		for (int i = 0; i < all.length; i++)
			ts[i] = new Song("resources/sound/" + all[i]);
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
