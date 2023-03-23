/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.util.accessibility;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.util.converter.TextExtractor;

/**
 * Screen reader simulator class should implement this interface
 */
public interface ScreenReaderSimulator extends TextExtractor {
	void setDocument(ODFDocument document);

	void setElement(ODFElement element);

	String getCurrentElementContent();

	String getElementContent(ODFElement elem);

	String getDocumentContent();
}
