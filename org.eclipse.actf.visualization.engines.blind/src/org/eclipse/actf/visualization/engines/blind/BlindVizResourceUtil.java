/*******************************************************************************
 * Copyright (c) 2007, 2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.blind;

import java.io.File;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.internal.engines.blind.BlindVizEnginePlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

/**
 * Utility class for managing blind usability visualization resources.
 */
public class BlindVizResourceUtil {

	/**
	 * Save images used in blind visualization into target path
	 * 
	 * @param path
	 *            target path
	 */
	@SuppressWarnings("nls")
	public static void saveImages(String path) {
		Bundle bundleChecker = Platform.getBundle(EvaluationUtil.PLUGIN_ID);

		FileUtils.copyFile(bundleChecker, new Path("icons/Err.png"), path + "Err.png", true);
		FileUtils.copyFile(bundleChecker, new Path("icons/Warn.png"), path + "Warn.png", true);
		FileUtils.copyFile(bundleChecker, new Path("icons/Conf.png"), path + "Conf.png", true);
		FileUtils.copyFile(bundleChecker, new Path("icons/Info.png"), path + "Info.png", true);
		FileUtils.copyFile(bundleChecker, new Path("icons/star.gif"), path + "star.gif", true);

		FileUtils.copyFile(bundleChecker, new Path("icons/rating/Bad.png"), path + "Bad.png", true);
		FileUtils.copyFile(bundleChecker, new Path("icons/rating/Good.png"), path + "Good.png", true);
		FileUtils.copyFile(bundleChecker, new Path("icons/rating/Poor.png"), path + "Poor.png", true);
		FileUtils.copyFile(bundleChecker, new Path("icons/rating/VeryGood.png"), path + "VeryGood.png", true);

		Bundle bundleBlind = Platform.getBundle(BlindVizEnginePlugin.PLUGIN_ID);

		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/clear.gif"), path + "clear.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/dest.gif"), path + "dest.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/draw.gif"), path + "draw.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/exclawhite21.gif"), path + "exclawhite21.gif",
				true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/face-sad.gif"), path + "face-sad.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/face-smile.gif"), path + "face-smile.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/face-usual.gif"), path + "face-usual.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/jump.gif"), path + "jump.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/line_filled.gif"), path + "line_filled.gif",
				true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/logo.gif"), path + "logo.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/move.gif"), path + "move.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/refresh.gif"), path + "refresh.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/images/stop.gif"), path + "stop.gif", true);
		
		//HTML5  (landmark) 
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/article.gif"), path + "article.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/aside.gif"), path + "aside.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/footer.gif"), path + "footer.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/header.gif"), path + "header.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/main.gif"), path + "main.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/nav.gif"), path + "nav.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/application.gif"), path + "application.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/banner.gif"), path + "banner.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/complementary.gif"), path + "complementary.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/contentinfo.gif"), path + "contentinfo.gif", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/search.gif"), path + "search.gif", true);
		//FileUtils.copyFile(bundleBlind, new Path("vizResources/landmark_icons/form.gif"), path + "form.gif", true);

		//ARIA
		FileUtils.copyFile(bundleBlind, new Path("vizResources/aria_icons/alert.png"), path + "alert.png", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/aria_icons/required.png"), path + "required.png", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/aria_icons/img.png"), path + "img.png", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/aria_icons/aria-label.png"), path + "aria-label.png", true);

	}

	/**
	 * Save script files used in blind visualization into target path
	 * 
	 * @param path
	 *            target path
	 */
	@SuppressWarnings("nls")
	public static void saveScripts(String path) {
		Bundle bundleBlind = Platform.getBundle(BlindVizEnginePlugin.PLUGIN_ID);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/scripts/highlight.js"), path + "highlight.js", true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/scripts/highlight_moz.js"), path + "highlight_moz.js",
				true);
		FileUtils.copyFile(bundleBlind, new Path("vizResources/scripts/highlight-dummy.js"),
				path + "highlight-dummy.js", true);

		// TODO create method for CSS
		FileUtils.copyFile(bundleBlind, new Path("vizResources/css/visualization.css"), path + "visualization.css",
				true);

	}

	/**
	 * Get {@link ImageDescriptor} from this plugin
	 * 
	 * @param imageFilePath
	 *            file path of target image
	 * @return {@link ImageDescriptor}
	 */
	public static ImageDescriptor getImageDescriptor(String imageFilePath) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(BlindVizEnginePlugin.PLUGIN_ID, imageFilePath);
	}

	/**
	 * Create temporary file under the state location of this plugin
	 * 
	 * @param prefix
	 *            prefix of temporary file
	 * @param suffix
	 *            suffix of temporary file
	 * @return temporary {@link File}
	 * @throws Exception
	 * @see AbstractUIPlugin#getStateLocation()
	 */
	public static File createTempFile(String prefix, String suffix) throws Exception {
		return (BlindVizEnginePlugin.createTempFile(prefix, suffix));
	}

	/**
	 * Get temporary directory under the state location of this plugin
	 * 
	 * @return temporary directory as {@link File}
	 * @see AbstractUIPlugin#getStateLocation()
	 */
	public static File getTempDirectory() {
		return BlindVizEnginePlugin.getTempDirectory();
	}

}
