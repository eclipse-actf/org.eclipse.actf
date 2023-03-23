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
package org.eclipse.actf.ai.tts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * AbstractPreferenceUIPlugin is used in plug-ins which provides ITTSEngine
 */
public abstract class AbstractUIPluginForTTS extends AbstractUIPlugin
		implements IPropertyChangeListener {

	private List<IPropertyChangeListener> listeners;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).propertyChange(event);
		}
	}

	/**
	 * Add property change listener to the preference store.
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		if (null == listeners) {
			listeners = new ArrayList<IPropertyChangeListener>();
			getPreferenceStore().addPropertyChangeListener(this);
		}
		listeners.add(listener);
	}

	/**
	 * Remove property change listener from the preference store.
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		listeners = null;
		getPreferenceStore().removePropertyChangeListener(this);
	}
}
