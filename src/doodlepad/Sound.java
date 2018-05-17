/*
 * Sound.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2018 Mark F. Russo
 * 
 * This file is part of DoodlePad
 * 
 * DoodlePad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DoodlePad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DoodlePad.  If not, see <http://www.gnu.org/licenses/>.
 */

package doodlepad;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;

/**
 * A class that loads a supported sound file and allows it to be played.
 * Multiple sound file formats are supported, including WAV.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Sound {

	private String path = null;
	private Clip clip = null;
	
	/**
	 * Nullary Sound constructor not initialized with sound file
	 */
	public Sound() {}
	
	/**
	 * Sound constructor initiated with a sound file
	 * @param path	Full path to a sound file
	 */
	public Sound(String path) {
		try {
			this.open(path);
		} catch (FileNotFoundException ex) {
			System.out.println("Sound file not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("IOException: " + ex.getMessage());
		} catch (UnsupportedAudioFileException ex) {
			System.out.println("Audio file format is not recognized: " + ex.getMessage());
		} catch (LineUnavailableException ex) {
			System.out.println("Sounds cannot be played: " + ex.getMessage());
		}
	}
	
	/**
	 * Open an audio file and create a sound clip ready to be played
	 * @param path	Full path to a sound file
	 * @throws IOException when a problem occurs while opening sound file
	 * @throws FileNotFoundException when sound file path does not exist
	 * @throws UnsupportedAudioFileException when a sound file of unknown format is attempted to be opened
	 * @throws LineUnavailableException when the sound is not able to be played
	 */
	public void open(String path) throws IOException, FileNotFoundException, UnsupportedAudioFileException, LineUnavailableException {
		this.path = path;
		File fil = new File(path);
		AudioInputStream ais = AudioSystem.getAudioInputStream(fil);              
		this.clip = AudioSystem.getClip();
		this.clip.open(ais);
	}
	
	/**
	 * Close a sound file
	 */
	public void close() {
		if (this.clip != null && this.clip.isOpen()) { this.clip.close(); }
		this.clip = null;
		this.path = null;
	}
	
	/**
	 * Check if a sound file is open
	 * @return A boolean indicating if a sound file is open
	 */
	public boolean isOpen() {
		if (this.clip != null && this.clip.isOpen()) {
			return true;
		} else {
			return false;
		}
	}
		
	/**
	 * Get the path to the Sound file
	 * @return Sound file path
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Get the internal managed javax.sound.sampled.Clip object 
	 * @return Clip object
	 */
	public Clip getClip() {
		return this.clip;
	}
	
	/**
	 * Play a sound clip of the clip has been loaded and opened
	 */
	public void play() {
		if (this.clip != null && this.clip.isOpen()) {
			this.clip.stop();
			this.clip.flush();
			this.clip.setFramePosition(0);
			this.clip.start();
		}
	}
	
	/**
	 * Stop a sound if currently being played
	 */
	public void stop() {
		if (this.clip != null && this.clip.isOpen()) {
			this.clip.stop();
		}
	}

}
