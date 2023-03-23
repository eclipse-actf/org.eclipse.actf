/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ai.voice.internal;

import org.eclipse.actf.ai.tts.AbstractUIPluginForTTS;
import org.eclipse.actf.ai.voice.IVoice;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class VoicePlugin extends AbstractUIPluginForTTS {

	// The shared instance.
	private static VoicePlugin plugin;
	private static Voice voice;

	/**
	 * The constructor.
	 */
	public VoicePlugin() {
		plugin = this;
	}

	public static IVoice getVoice() {
		if (null == voice) {
			voice = new Voice();
		}
		return voice;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		if (null != voice) {
			voice.dispose();
		}
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static VoicePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.actf.ai.voice", path); //$NON-NLS-1$
	}

}
