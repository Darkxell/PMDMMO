package com.darkxell.client.resources.music;

import java.io.File;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;

import com.darkxell.common.util.Logger;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

public class Song {

	/** The absolute filepath to the .mp3 file */
	private String filepath;
	private String displayname;
	private String name = null;
	private String author = null;
	/** the length of the .mp3 file */
	public int length;

	/** Constructs a song object using a <code>File </code> object. */
	public Song(File mp3) {
		this(mp3.getAbsolutePath());
	}

	/** Constructs a Song object from a .mp3 filepath. */
	public Song(String path) {
		this.filepath = path + "";
		AudioFileFormat baseFileFormat;
		try {
			baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(new File(path));
			@SuppressWarnings("rawtypes")
			Map properties = baseFileFormat.properties();
			long duration = (long) properties.get("duration");
			this.length = (int) duration / 1000;
			this.author = (String) properties.get("author");
			this.name = (String) properties.get("title");
		} catch (Exception e) {
			Logger.e("Could not read song properties properly : " + e);
			e.printStackTrace();
		}
		File tempfile = new File(path);
		this.displayname = tempfile.getName();
		if (this.author != null && !this.author.equals("") && this.name != null && !this.name.equals(""))
			this.displayname = this.author + " - " + this.name;
	}

	public String getfilepath() {
		return this.filepath;
	}

	/**
	 * Returns the displayname of the song. Usually Artist - Name , but just the
	 * name of the mp3 file if not specified.
	 */
	public String getdipsplayname() {
		return this.displayname;
	}

	/**
	 * Returns the duration of an mp3 file in miliseconds. Don't work with .wav
	 * files. Returns 0 in case of errors.<br/>
	 * Note : I'm aware that this is not hte best method to get it, but I just
	 * couldn't find something better. <br/>
	 * <strong>Deprecated :</strong> this method has been deprecated due to high
	 * and not necessary CPU usage. MP3 length can now be obtained by creating a
	 * Song Object with the filepath, and using the public Song.length
	 * attribute.
	 */
	@Deprecated
	public static int getmp3Length(File file) {
		AudioFileFormat baseFileFormat;
		try {
			baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
			@SuppressWarnings("rawtypes")
			Map properties = baseFileFormat.properties();
			long duration = (long) properties.get("duration");
			return (int) duration / 1000;
		} catch (Exception e) {
			Logger.e("Could not obtain song length : " + e);
			return 0;
		}
	}
}