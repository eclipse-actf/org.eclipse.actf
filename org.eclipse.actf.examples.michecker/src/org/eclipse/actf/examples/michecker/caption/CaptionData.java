/*******************************************************************************
 * Copyright (c) 2010, 2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.examples.michecker.caption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * General intermediate captions data. One CaptionData instance for each caption
 * lines.
 * 
 */
public class CaptionData {
	/**
	 * Constructor with time and source
	 * 
	 * @param time
	 * @param src
	 */
	public CaptionData(double time, URL src) {
		setTime(time);
		setCaptionSrc(src);
	}

	/**
	 * Constructor with time and text. Used in parsing RealText files.
	 * 
	 * @param time
	 * @param text
	 */
	public CaptionData(double time, String text) {
		setTime(time);
		captionText = text;
	}

	/**
	 * Constructor with no arguments
	 * 
	 * @param time
	 * @param src
	 */
	public CaptionData() {
	}

	/**
	 * Global time when this caption begins.
	 */
	private double time;
	/**
	 * The value of the source attribute (URL) for caption file.
	 */
	private URL captionSrc;
	/**
	 * Caption string itself.
	 */
	private String captionText;

	public double getTime() {
		return time;
	}

	/**
	 * Returns its time value in String format (e.g. 12:34:56.789)
	 * 
	 * @return
	 */
	public String getTimeString() {
		int hr = (int) (time / 3600);
		int min = (int) (time / 60);
		int sec = (int) (time % 60);
		int msec = (int) ((time * 1000) % 1000);
		// TODO format refinement
		return formatTime(hr, 2) + ":" + formatTime(min, 2) + ":"
				+ formatTime(sec, 2) + "." + formatTime(msec, 3);
	}

	/**
	 * Utility method which converts "3" to "03"
	 * 
	 * @param value
	 * @param numDigit
	 * @return
	 */
	private String formatTime(int value, int numDigit) {
		String str = "" + value;
		for (int i = 0; i < numDigit; i++) {
			str = "0" + str;
		}
		return str.substring(str.length() - numDigit);
	}

	public void setTime(double time) {
		this.time = time;
	}

	public String getCaptionText() {
		return captionText;
	}

	/**
	 * read a plain text caption file and set the content as it's caption data.
	 * 
	 * @param captionSrc
	 *            <see>URL</see>
	 */
	public void setCaptionSrc(URL captionSrc) {
		this.captionSrc = captionSrc;
		try {
			// DataInputStream is = new
			// DataInputStream(captionSrc.openStream());
			// captionText = is.readUTF();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					this.captionSrc.openStream()));
			String line;
			captionText = new String();
			while ((line = br.readLine()) != null) {
				captionText += (captionText.length() > 0 ? "\n" : "") + line;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
