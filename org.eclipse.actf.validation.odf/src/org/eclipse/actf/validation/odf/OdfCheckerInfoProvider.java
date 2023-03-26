/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.validation.odf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.eclipse.actf.visualization.eval.ICheckerInfoProvider;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class OdfCheckerInfoProvider implements ICheckerInfoProvider {

	private static final String BUNDLE_NAME = "resources/description"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private static final Bundle bundle = Platform.getBundle("org.eclipse.validation.odf"); //$NON-NLS-1$

	public InputStream[] getCheckItemInputStreams() {

		InputStream is;
		try {
			is = bundle.getEntry("resources/ODFcheckitem.xml").openStream(); //$NON-NLS-1$
			return new InputStream[] { is };
		} catch (IOException e) {
		}
		return new InputStream[] {};
	}

	public InputStream[] getGuidelineInputStreams() {
		InputStream is;
		try {
			is = bundle.getEntry("resources/ODFGuide.xml").openStream(); //$NON-NLS-1$
			return new InputStream[] { is };
		} catch (IOException e) {
		}
		return new InputStream[] {};
	}

	public ResourceBundle getDescriptionRB() {
		return RESOURCE_BUNDLE;
	}

}
