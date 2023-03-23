/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.presentation.internal;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class RoomPlugin extends AbstractUIPlugin {

	public static String PLUGIN_ID = "org.eclipse.actf.visualization.presentation"; //$NON-NLS-1$

	// The shared instance.
	private static RoomPlugin _plugin;

	private static BundleContext _context;

	private static File tmpDir;

	/**
	 * The constructor.
	 */
	public RoomPlugin() {
		_plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@SuppressWarnings("nls")
	public void start(BundleContext context) throws Exception {
		super.start(context);
		_context = context;

		createTempDirectory();

		if (tmpDir != null) {

			String tmpS = tmpDir.getAbsolutePath() + File.separator + "html";
			if (FileUtils.isAvailableDirectory(tmpS)) {
				String en = tmpS + File.separator + "en";
				String ja = tmpS + File.separator + "ja";
				String images = tmpS + File.separator + "images";
				Bundle bundle = getBundle();
				if (FileUtils.isAvailableDirectory(en)
						&& FileUtils.isAvailableDirectory(ja)
						&& FileUtils.isAvailableDirectory(images)) {
					en = en + File.separator;
					ja = ja + File.separator;
					images = images + File.separator;

					FileUtils.copyFile(bundle, new Path("html/en/large.html"),
							en + "large.html", true);
					FileUtils.copyFile(bundle, new Path("html/en/middle.html"),
							en + "middle.html", true);
					FileUtils.copyFile(bundle, new Path("html/en/small.html"),
							en + "small.html", true);
					FileUtils.copyFile(bundle, new Path("html/ja/large.html"),
							ja + "large.html", true);
					FileUtils.copyFile(bundle, new Path("html/ja/middle.html"),
							ja + "middle.html", true);
					FileUtils.copyFile(bundle, new Path("html/ja/small.html"),
							ja + "small.html", true);
					FileUtils.copyFile(bundle, new Path(
							"html/images/auditorium.png"), images
							+ "auditorium.png", true);
					FileUtils.copyFile(bundle, new Path(
							"html/images/largeMeetingRoom.png"), images
							+ "largeMeetingRoom.png", true);
					FileUtils.copyFile(bundle, new Path(
							"html/images/smallMeetingRoom.png"), images
							+ "smallMeetingRoom.png", true);
				}

			}
		}

	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		deleteFiles(tmpDir);

		_plugin = null;
		_context = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static RoomPlugin getDefault() {
		return _plugin;
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
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static String getDirectory(String dir) {

		try {
			URL url = _context.getBundle().getEntry(dir);
			url = FileLocator.resolve(url);
			return new Path(url.getPath()).makeAbsolute().toOSString();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return ""; //$NON-NLS-1$
		}
	}

	public static String getTempDirectory() {
		if (tmpDir != null) {
			return (tmpDir.getAbsolutePath() + File.separator);
		}
		return ""; //$NON-NLS-1$
	}

	private static void createTempDirectory() {
		if (tmpDir == null) {
			String tmpS = _plugin.getStateLocation().toOSString()
					+ File.separator + "tmp"; //$NON-NLS-1$
			if (FileUtils.isAvailableDirectory(tmpS)) {
				tmpDir = new File(tmpS);
			}
		}
	}

	public static File createTempFile(String prefix, String suffix)
			throws Exception {
		createTempDirectory();
		return (File.createTempFile(prefix, suffix, tmpDir));
	}

	private void deleteFiles(File rootDir) {
		if (rootDir != null) {
			File[] fileList = rootDir.listFiles();

			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					deleteFiles(fileList[i]);
				}
				fileList[i].delete();
			}
		}
	}
}
