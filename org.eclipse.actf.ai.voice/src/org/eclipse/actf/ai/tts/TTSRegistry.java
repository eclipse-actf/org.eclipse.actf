/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ai.tts;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * TTSRegistry manages the TTS engine plug-ins.
 */
public class TTSRegistry {

	private static final String PRIORITY = "priority"; //$NON-NLS-1$

	private static final String TTS_EXTENSION = "org.eclipse.actf.ai.voice.TTSEngine"; //$NON-NLS-1$

	private static final String DEFAULT_TTS = "org.eclipse.actf.ai.tts.sapi.engine.SapiVoice"; //$NON-NLS-1$

	private static IConfigurationElement[] ttsElements;

	private static boolean[] availables;

	private static ITTSEngine[] INSTANCES;

	static {
		initialize();
	}

	private static void initialize() {
		ttsElements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(TTS_EXTENSION);
		Arrays.sort(ttsElements, new Comparator<IConfigurationElement>() {
			public int compare(IConfigurationElement c1,
					IConfigurationElement c2) {
				String s1 = c1.getAttribute(PRIORITY);
				String s2 = c2.getAttribute(PRIORITY);
				int i1 = 0;
				int i2 = 0;
				try {
					i1 = Integer.parseInt(s1);
				} catch (NumberFormatException e) {
				}
				try {
					i2 = Integer.parseInt(s2);
				} catch (NumberFormatException e) {
				}
				return i2 - i1;
			}
		});
		availables = new boolean[ttsElements.length];
		INSTANCES = new ITTSEngine[ttsElements.length];
		for (int i = 0; i < ttsElements.length; i++) {
			try {
				ITTSEngine test = (ITTSEngine) ttsElements[i]
						.createExecutableExtension("class"); //$NON-NLS-1$
				if (test.isAvailable()) {
					availables[i] = true;
					INSTANCES[i] = test;
				}
			} catch (Exception e) {
				availables[i] = false;
				INSTANCES[i] = null;
			}
		}
	}

	/**
	 * @param id
	 *            the ID of TTS engine plug-in.
	 * @return whether the TTS engine specified the ID is available or not.
	 */
	public static boolean isAvailable(String id) {
		try {
			for (int i = 0; i < ttsElements.length; i++) {
				if (id.equals(ttsElements[i].getAttribute("id"))) { //$NON-NLS-1$
					return availables[i];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This returns the ID of TTS engine which has the highest priority in the
	 * available engines.
	 * 
	 * @return the ID of the default TTS engine.
	 */
	public static String getDefaultEngine() {
		for (int i = 0; i < ttsElements.length; i++) {
			if (availables[i]) {
				return ttsElements[i].getAttribute("id"); //$NON-NLS-1$
			}
		}
		return DEFAULT_TTS;
	}

	/**
	 * This returns {"name", "id"} pairs of TTS engine plug-ins.
	 * 
	 * @return the string array of {"name", "id"} pairs.
	 */
	public static String[][] getLabelAndIds() {
		String[][] labelAndIds = new String[ttsElements.length][2];
		for (int i = 0; i < ttsElements.length; i++) {
			labelAndIds[i][0] = ttsElements[i].getAttribute("name"); //$NON-NLS-1$
			if (availables[i])
				labelAndIds[i][1] = ttsElements[i].getAttribute("id"); //$NON-NLS-1$
			else
				labelAndIds[i][1] = ""; //$NON-NLS-1$
		}
		return labelAndIds;
	}

	/**
	 * @param id
	 *            the ID of TTS engine plug-in.
	 * @return the instance of ITTSEngine specified by the ID.
	 */
	public static ITTSEngine createTTSEngine(String id) {
		try {
			for (int i = 0; i < ttsElements.length; i++) {
				if (id.equals(ttsElements[i].getAttribute("id"))) { //$NON-NLS-1$
				 if (INSTANCES[i] == null || INSTANCES[i].isDisposed()) {
						INSTANCES[i] = (ITTSEngine) ttsElements[i]
								.createExecutableExtension("class"); //$NON-NLS-1$
					}
					return INSTANCES[i];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
