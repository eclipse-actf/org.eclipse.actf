/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Abstract implementation of {@link AbstractUIPlugin}. This class enables to
 * create temporary directory and files under the plugin state area.
 * 
 * @see Plugin
 */
public abstract class AbstractUIPluginACTF extends AbstractUIPlugin {

	private File tmpDir = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		createTempDirectory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		deleteFiles(tmpDir);
	}

	protected void createTempDirectory() {
		if (tmpDir == null) {
			String tmpS = getStateLocation().toOSString() + File.separator
					+ "tmp"; //$NON-NLS-1$
			if (isAvailableDirectory(tmpS)) {
				tmpDir = new File(tmpS);
			} else {
//				System.err.println(toString() + " : can't open tmp Directory ("
//						+ tmpDir + ")");
				tmpDir = new File(System.getProperty("java.io.tmpdir") //$NON-NLS-1$
						+ File.separator + "ACTF"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Create temporary file under the plugin state directory.
	 * 
	 * @param prefix
	 *            the prefix string to be used in generating the file's name;
	 *            must be at least three characters long
	 * @param suffix
	 *            the suffix string to be used in generating the file's name;
	 *            may be null, in which case the suffix ".tmp" will be used
	 * @return the generated temporary {@link File}
	 * @throws IOException
	 */
	public File createTempFile(String prefix, String suffix) throws IOException {
		if (tmpDir == null) {
			createTempDirectory();
		}
		return (File.createTempFile(prefix, suffix, tmpDir));
	}

	/**
	 * Return temporary directory for this plugin. The directory is generated
	 * under the plugin state directory.
	 * 
	 * @return the temporary directory as {@link File}
	 */
	public File getTempDirectory() {
		if (tmpDir == null) {
			createTempDirectory();
		}
		return tmpDir;
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

	private boolean isAvailableDirectory(String path) {
		File testDir = new File(path);
		if ((!testDir.isDirectory() || !testDir.canWrite())
				&& !testDir.mkdirs()) {
			//System.err.println(path + " is not available.");
			return false;
		}
		return true;
	}

}
