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
package org.eclipse.actf.model.dom.odf.util.converter;

import java.io.File;
import java.io.Writer;

import org.eclipse.actf.model.dom.odf.base.ODFElement;

/**
 * Class that extract text contents should implements this interface
 */
public interface TextExtractor {
	boolean extractContent(Writer writer, File dir, ODFElement elem,
			boolean enableStyle);
}
