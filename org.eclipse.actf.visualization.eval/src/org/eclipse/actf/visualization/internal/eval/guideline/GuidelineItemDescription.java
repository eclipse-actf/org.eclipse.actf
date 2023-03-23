/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.eval.guideline;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.eval.ICheckerInfoProvider;
import org.eclipse.actf.visualization.internal.eval.CheckerExtension;

public class GuidelineItemDescription {

	private static final String BUNDLE_NAME = "resources/description"; //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private static GuidelineItemDescription instance;

	private static ICheckerInfoProvider[] checkerInfos = CheckerExtension.getCheckerInfoProviders();

	private GuidelineItemDescription() {
	}

	public static String getDescription(String key) {
		if (instance == null) {
			instance = new GuidelineItemDescription();
		}

		String result = getString(RESOURCE_BUNDLE, key);

		if (EMPTY_STRING.equals(result)) {
			for (ICheckerInfoProvider checkerInfo : checkerInfos) {
				result = getString(checkerInfo.getDescriptionRB(), key);
				if(!EMPTY_STRING.equals(result)){
					return result;
				}
			}
			DebugPrintUtil.devOrDebugPrintln("Can't find description: " + '!' + key + '!'); //$NON-NLS-1$
		}
		return (result);
	}

	private static String getString(ResourceBundle bundle, String key) {
		if (null != bundle) {
			try {
				return bundle.getString(key);
			} catch (MissingResourceException e) {
				// DebugPrintUtil.devOrDebugPrintln('!' + key + '!');
			}
		}
		return EMPTY_STRING;
	}
}
