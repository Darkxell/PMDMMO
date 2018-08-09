package com.darkxell.client.resources.music;

import com.darkxell.client.resources.images.Res;
import com.darkxell.common.util.Logger;

public class SoundsHolder
{
	private static String externalResourcesPath;

	public static Song[] soundtracks;
	public static Song[] sfx;
	public static String[] soundtrackNames;
	public static String[] sfxNames;

	private static Song[] getSoundtracks()
	{
		String[] all = Res.getResourceFiles("/music/");
		soundtrackNames = new String[all.length];
		for (int i = 0; i < all.length; i++)
			soundtrackNames[i] = all[i];
		Song[] ts = new Song[all.length];
		for (int i = 0; i < all.length; i++)
			ts[i] = new Song(externalResourcesPath + "/resources/music/" + all[i]);
		return ts;
	}

	private static Song[] getSfx()
	{
		String[] all = Res.getResourceFiles("/sound/");
		sfxNames = new String[all.length];
		for (int i = 0; i < all.length; i++)
			sfxNames[i] = all[i].substring(0, all[i].length() - 4);
		Song[] ts = new Song[all.length];
		for (int i = 0; i < all.length; i++)
			ts[i] = new Song(externalResourcesPath + "/resources/sound/" + all[i]);
		return ts;
	}

	public static Song getSong(String name)
	{
		for (int i = 0; i < soundtrackNames.length; i++)
			if (soundtrackNames[i].equals(name)) return soundtracks[i];
		Logger.e("Unknown song: " + name);
		return null;
	}

	public static Song getSfx(String name)
	{
		for (int i = 0; i < sfxNames.length; i++)
			if (sfxNames[i].equals(name)) return sfx[i];
		Logger.e("Unknown Sfx: " + name);
		return null;
	}

	public static void reloadSoundtracks()
	{
		soundtracks = getSoundtracks();
	}

	public static void reloadSfx()
	{
		sfx = getSfx();
	}

	public static void load(String externalResourcesPath)
	{
		Logger.i("Loading sounds...");
		SoundsHolder.externalResourcesPath = externalResourcesPath;
		soundtracks = getSoundtracks();
		sfx = getSfx();
	}

}
