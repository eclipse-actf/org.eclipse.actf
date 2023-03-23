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

package org.eclipse.actf.visualization.lowvision.util;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.lowvision.LowVisionVizPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

@SuppressWarnings("nls")
public class LowVisionVizResourceUtil {

	public static void saveErrorIcons(String path) {
		Bundle bundleChecker = Platform.getBundle(EvaluationUtil.PLUGIN_ID);
		FileUtils.copyFile(bundleChecker, new Path(
				"icons/lowvision/HiIro21.gif"), path + "HiIro21.gif", true);
		FileUtils.copyFile(bundleChecker, new Path(
				"icons/lowvision/HiBoke21.gif"), path + "HiBoke21.gif", true);
		FileUtils.copyFile(bundleChecker, new Path(
				"icons/lowvision/ErrIro21.gif"), path + "ErrIro21.gif", true);
		FileUtils.copyFile(bundleChecker, new Path(
				"icons/lowvision/ErrBoke21.gif"), path + "ErrBoke21.gif", true);

	}

	public static void saveImages(String path) {
		Bundle bundleChecker = Platform.getBundle(EvaluationUtil.PLUGIN_ID);

		FileUtils.copyFile(bundleChecker, new Path("icons/rating/Bad.png"),
				path + "Bad.png", true);
		FileUtils.copyFile(bundleChecker, new Path("icons/rating/Good.png"),
				path + "Good.png", true);
		FileUtils.copyFile(bundleChecker, new Path("icons/rating/Poor.png"),
				path + "Poor.png", true);
		FileUtils.copyFile(bundleChecker,
				new Path("icons/rating/VeryGood.png"), path + "VeryGood.png",
				true);
	}

	public static void saveScripts(String path) {
		Bundle lvBundle = Platform.getBundle(LowVisionVizPlugin.PLUGIN_ID);

		FileUtils.copyFile(lvBundle, new Path(
				"vizResources/scripts/lvHighlight.js"),
				path + "lvHighlight.js", true);
		FileUtils.copyFile(lvBundle, new Path(
				"vizResources/scripts/lvHighlight_moz.js"), path
				+ "lvHighlight_moz.js", true);

	}

}
