/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.actf.model.internal.ui.preferences.IPreferenceConstants;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferenceStore;

public class FavoritesUtil {
	private static FavoritesUtil INSTANCE = new FavoritesUtil();

	Properties favoritesProp = null;

	File targetFile;

	ModelUIPlugin plugin;

	private FavoritesUtil() {
		plugin = ModelUIPlugin.getDefault();
		IPath favoritesPath = plugin.getStateLocation().append(
				"favorites.properties"); //$NON-NLS-1$
		favoritesProp = new Properties();
		String targetFileName = favoritesPath.toOSString();
		try {
			targetFile = new File(targetFileName);
			if (targetFile.canRead()) {
				favoritesProp.load(new FileInputStream(targetFile));
			}
		} catch (Exception e) {

		}

		if (!plugin.getPreferenceStore().getBoolean(
				IPreferenceConstants.NOT_FIRST_TIME)) {

			URL[] prefFiles = BrowserFavoritesExtension
					.getBrowserFavoritesPrefFileURLs();

			if (prefFiles.length > 0) {
				for(URL target : prefFiles){
					try {
						InputStream prefIS = target.openStream();
						if (prefIS != null) {
							PreferenceStore tmpStore = new PreferenceStore();
							tmpStore.load(prefIS);
							for (String key : tmpStore.preferenceNames()) {
								favoritesProp.put(key, tmpStore.getString(key));
							}
						}					
					} catch (Exception e) {
						e.printStackTrace();
					}
				}				
			} else {

				try {
					InputStream prefIS = FileLocator.openStream(Platform
							.getBundle(ModelUIPlugin.PLUGIN_ID), new Path(
							"config/favorites.pref"), false); //$NON-NLS-1$
					if (prefIS != null) {
						PreferenceStore tmpStore = new PreferenceStore();
						tmpStore.load(prefIS);
						for (String key : tmpStore.preferenceNames()) {
							favoritesProp.put(key, tmpStore.getString(key));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			favoritesProp.store(new FileOutputStream(targetFile), ""); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> getFavoritesMap() {
		Map<String, String> favoritesMap = new TreeMap<String, String>();

		if (INSTANCE.favoritesProp != null) {
			Set<Object> keySet = INSTANCE.favoritesProp.keySet();
			for (Object key : keySet) {
				favoritesMap.put((String) key, INSTANCE.favoritesProp
						.getProperty((String) key));
			}
		}

		return favoritesMap;
	}

	public static void saveFavoritesMap(Map<String, String> favoritesMap) {

		if (INSTANCE.favoritesProp != null) {
			INSTANCE.favoritesProp.clear();

			Iterator<String> favoritesIt = favoritesMap.keySet().iterator();
			String key;
			while (favoritesIt.hasNext()) {
				key = favoritesIt.next();
				INSTANCE.favoritesProp.put(key, favoritesMap.get(key));
			}
			try {
				INSTANCE.favoritesProp.store(new FileOutputStream(
						INSTANCE.targetFile), ""); //$NON-NLS-1$
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
