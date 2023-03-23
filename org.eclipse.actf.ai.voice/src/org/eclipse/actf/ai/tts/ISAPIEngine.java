/*******************************************************************************
 * Copyright (c) 2008, 2012 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ai.tts;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ISAPIEngine interface defines text synthesis interface to be implemented by
 * SAPI5 and MSP text-to-speech engine
 * 
 * @see ITTSEngine
 */
public interface ISAPIEngine extends ITTSEngine {

	public static final int SVSFDefault = 0, SVSFlagsAsync = 1,
			SVSFPurgeBeforeSpeak = 2, SVSFIsFilename = 4, SVSFIsXML = 8,
			SVSFIsNotXML = 16, SVSFPersistXML = 32;

	/**
	 * Map to get LangId from "Language"-"Country" code (e.g., en-US).
	 */
	public static final Map<String, String> LANGID_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = 8393339647554273101L;
		{
			put("ar-SA", "401");
			put("bg-BG", "402");
			put("ca-ES", "403");
			put("zh-TW", "404");
			put("cs-CZ", "405");
			put("da-DK", "406");
			put("de-DE", "407");
			put("el-GR", "408");
			put("en-US", "409");
			put("fi-FI", "40B");
			put("fr-FR", "40C");
			put("he-IL", "40D");
			put("hu-HU", "40E");
			put("it-IT", "410");
			put("ja-JP", "411");
			put("ko-KR", "412");
			put("nl-NL", "413");
			put("nb-NO", "414");
			put("pl-PL", "415");
			put("pt-BR", "416");
			put("ro-RO", "418");
			put("ru-RU", "419");
			put("hr-HR", "41A");
			put("sk-SK", "41B");
			put("sv-SE", "41D");
			put("th-TH", "41E");
			put("tr-TR", "41F");
			put("uk-UA", "422");
			put("sl-SI", "424");
			put("et-EE", "425");
			put("lv-LV", "426");
			put("lt-LT", "427");
			put("vi-VN", "42A");
			put("eu-ES", "42D");
			put("zh-CN", "804");
			put("pt-PT", "816");
			put("sr-CS", "81A");
			put("es-ES", "C0A");
			put("en-AU", "C09");
			put("en-CA", "1009");
			put("en-GB", "809");
			put("en-IN", "4009");
			put("fr-CA", "C0C");
			put("zh-HK", "C04");
		}
	};

	/**
	 * Map to get "Language"-"Country" code (e.g., en-US) from LangId.
	 */
	public static final Map<String, String> LANGID_REVERSE_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = -4065510530588377900L;
		{
			put("401", "ar-SA");
			put("402", "bg-BG");
			put("403", "ca-ES");
			put("404", "zh-TW");
			put("405", "cs-CZ");
			put("406", "da-DK");
			put("407", "de-DE");
			put("408", "el-GR");
			put("409", "en-US");
			put("40B", "fi-FI");
			put("40C", "fr-FR");
			put("40D", "he-IL");
			put("40E", "hu-HU");
			put("410", "it-IT");
			put("411", "ja-JP");
			put("412", "ko-KR");
			put("413", "nl-NL");
			put("414", "nb-NO");
			put("415", "pl-PL");
			put("416", "pt-BR");
			put("418", "ro-RO");
			put("419", "ru-RU");
			put("41A", "hr-HR");
			put("41B", "sk-SK");
			put("41D", "sv-SE");
			put("41E", "th-TH");
			put("41F", "tr-TR");
			put("422", "uk-UA");
			put("424", "sl-SI");
			put("425", "et-EE");
			put("426", "lv-LV");
			put("427", "lt-LT");
			put("42A", "vi-VN");
			put("42D", "eu-ES");
			put("804", "zh-CN");
			put("816", "pt-PT");
			put("81A", "sr-CS");
			put("C0A", "es-ES");
			put("C09", "en-AU");
			put("1009", "en-CA");
			put("809", "en-GB");
			put("4009", "en-IN");
			put("C0C", "fr-CA");
			put("C04", "zh-HK");
		}
	};

	/**
	 * @param rate
	 *            The rate property to be set.
	 * @return The invocation is succeeded then it returns true.
	 */
	public boolean setRate(int rate);

	/**
	 * @return The rate property of the voice engine.
	 */
	public int getRate();

	/**
	 * Speak text by using specified SAPI flag
	 * 
	 * @param text
	 *            text string to be spoken
	 * @param sapiFlags
	 *            SAPI flags
	 */
	public void speak(String text, int sapiFlags);

	
	/**
	 * @return set of TTS engine information that supported in the environment.
	 */
	public Set<ITTSEngineInfo> getTTSEngineInfoSet();
}
