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
package org.eclipse.actf.model.dom.odf.base.impl;

import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFProcessingInstruction;
import org.w3c.dom.DOMException;
import org.w3c.dom.ProcessingInstruction;


public class ODFProcessingInstructionImpl extends ODFNodeImpl implements
		ODFProcessingInstruction {

	public ODFProcessingInstructionImpl(ODFDocument odfDoc,
			ProcessingInstruction innerProcessingInstruction) {
		super(odfDoc, innerProcessingInstruction);
	}

	public String getData() {
		if (iNode instanceof ProcessingInstruction) {
			return ((ProcessingInstruction) iNode).getData();
		}
		return null;
	}

	public String getTarget() {
		if (iNode instanceof ProcessingInstruction) {
			return ((ProcessingInstruction) iNode).getTarget();
		}
		return null;
	}

	public void setData(String s) throws DOMException {
		if (iNode instanceof ProcessingInstruction) {
			((ProcessingInstruction) iNode).setData(s);
		}
	}

}
