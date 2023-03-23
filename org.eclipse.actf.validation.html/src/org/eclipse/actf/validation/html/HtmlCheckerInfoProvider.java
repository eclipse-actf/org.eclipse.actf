/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.validation.html;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.eclipse.actf.visualization.eval.ICheckerInfoProvider;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class HtmlCheckerInfoProvider implements ICheckerInfoProvider {

	private static final String BUNDLE_NAME = "resources/description"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public InputStream[] getCheckItemInputStreams() {
		Bundle bundle = Platform
				.getBundle("org.eclipse.actf.validation.html");  //$NON-NLS-1$

		try {
			InputStream is = bundle.getEntry("resources/checkitem.xml")  //$NON-NLS-1$
					.openStream();
			return new InputStream[] { is };

		} catch (IOException e) {
			return new InputStream[0];
		}
	}

	public ResourceBundle getDescriptionRB() {
		return RESOURCE_BUNDLE;
	}

	public InputStream[] getGuidelineInputStreams() {
		return new InputStream[0];
	}

}
