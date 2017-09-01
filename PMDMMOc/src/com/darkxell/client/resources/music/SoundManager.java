package com.darkxell.client.resources.music;

import java.io.FileInputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

import javazoom.jl.player.Player;

/**
 * An object that manages all the sounds. Able to play a sound, and to
 * manipulate a background music.
 */
public class SoundManager implements Runnable {

	/**
	 * Creates a <code>SoundManager</code>. This constructor creates the needed
	 * thread to play the BGM, and is ready to use.
	 */
	public SoundManager() {
		this.runner = new Thread(this);
		this.runner.start();
	}

	/**
	 * The thread trying to play the song. Might be suspended, touching it might
	 * be deadlick phrone.
	 */
	private Thread runner;
	/** The current playing Player. */
	private Player currentplayer;
	/** The current song being played (even if paused). */
	private Song currentsong;
	/**
	 * is true if the Music is curently changing, preventing from playing or
	 * pausing.
	 */
	private boolean ischanging;
	/** Is true if the SM is killed. */
	private boolean iskilled;

	public void run() {
		while (!iskilled) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
			}
			try {
				if (this.currentplayer.isComplete() && !iskilled) {
					this.currentplayer.close();
					this.forceBackgroundMusic(currentsong);
				}
				if (!ischanging && !iskilled)
					this.currentplayer.play();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Sets the background music to the selected song. Setting the song to null
	 * will basically cancel the plays.
	 */
	public void setBackgroundMusic(Song song) {
		if (this.currentsong == song)
			return;
		else
			forceBackgroundMusic(song);
	}

	/**
	 * Does the same as setBackgroundMusic(), but ignores the "already playing
	 * this music" check. This is used to replay the same song in loops.
	 */
	private void forceBackgroundMusic(Song song) {
		if (!iskilled) {
			this.ischanging = true;
			this.currentsong = song;
			try {
				this.currentplayer.close();
			} catch (Exception e) {
			}
			try {
				this.currentplayer = new Player(new FileInputStream(song.getfilepath()));
			} catch (Exception e) {
				if (song != null) {
					System.err.println("Erreur lors de l'ouverture du fichier son.");
					e.printStackTrace();
				}
			} finally {
				this.ischanging = false;
			}
		} else
			System.err.println("Can't set the BGM of a killed sound manager.");
	}

	/**
	 * Pauses the background player. Note that changing the song will not resume
	 * it.
	 */
	@SuppressWarnings("deprecation")
	public void pauseBackgroundMusic() {
		if (!iskilled)
			runner.suspend();
	}

	/**
	 * Resumes the background player. If it is already running, does nothing.
	 */
	@SuppressWarnings("deprecation")
	public void resumeBackgroundMusic() {
		if (!iskilled)
			runner.resume();
	}

	/**
	 * Plays a sound in a separate thread. This sound can't be touched after
	 * launching it's play, so be careful.
	 */
	public void playSound(Song sound) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new Player(new FileInputStream(sound.getfilepath())).play();
				} catch (Exception e) {
					System.err.println("Sound couldn't be played.");
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	/**
	 * Closes the SoundManager and release the ressources. Also stop the thread.
	 */
	public void kill() {
		this.iskilled = true;
		currentplayer.close();
	}

	/**
	 * Sets the Volume of all the Lines of all the Mixers to the
	 * <code>volume</code> value.<br/>
	 * If you are unaware about audio, this basically sets your application
	 * volume to the desired value.
	 * 
	 * @param volume
	 *            The volume of your app, a float with 0 <= volume <= 1
	 */
	public static void setApplicationVolume(float volume) {
		javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixers.length; i++) {
			Mixer.Info mixerInfo = mixers[i];
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lineinfos = mixer.getTargetLineInfo();
			for (Line.Info lineinfo : lineinfos) {
				try {
					Line line = mixer.getLine(lineinfo);
					line.open();
					if (line.isControlSupported(FloatControl.Type.VOLUME)) {
						FloatControl control = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
						// Sets everything here.
						control.setValue((float) volume);
					}
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
	}
}