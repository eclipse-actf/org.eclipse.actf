/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import org.eclipse.actf.visualization.engines.voicebrowser.IContext;

/**
 * Context in the voice browser engine.
 */
public class Context implements IContext {
	private static final String COMMA_SPACE = ", "; //$NON-NLS-1$

	private boolean insideForm = false;
	private boolean linkTag = false;
	private boolean insideAnchor = false;
	private boolean startSelect = false;
	private boolean stringOutput = true;
	private boolean lineDelimiter = false;
	private boolean goChild = true;

	private String href = null;

	/**
	 * Constructor for Context.
	 */
	public Context() {
	}

	/**
	 * Constructor for Context.
	 * 
	 * @param ctx source context instance to be copied.
	 */
	public Context(IContext ctx) {
		insideForm = ctx.isInsideForm();
		insideAnchor = ctx.isInsideAnchor();
		lineDelimiter = ctx.isLineDelimiter();
		startSelect = ctx.isStartSelect();
		stringOutput = ctx.isStringOutput();
		linkTag = ctx.isLinkTag();
		goChild = ctx.isGoChild();

		href = ctx.getHref();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.actf.visualization.engines.voicebrowser.IContext#isInsideForm()
	 */
	public boolean isInsideForm() {
		return insideForm;
	}

	/**
	 * Sets the insideForm.
	 * 
	 * @param i The insideForm to set
	 */
	public void setInsideForm(boolean i) {
		insideForm = i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IContext#isGoChild()
	 */
	public boolean isGoChild() {
		return goChild;
	}

	/**
	 * Sets the goChild.
	 * 
	 * @param goChild The goChild to set
	 */
	public void setGoChild(boolean goChild) {
		this.goChild = goChild;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.actf.visualization.engines.voicebrowser.IContext#isLineDelimiter(
	 * )
	 */
	public boolean isLineDelimiter() {
		return lineDelimiter;
	}

	/**
	 * Sets the lineDelimiter.
	 * 
	 * @param lineDelimiter The lineDelimiter to set
	 */
	public void setLineDelimiter(boolean lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IContext#isLinkTag()
	 */
	public boolean isLinkTag() {
		return linkTag;
	}

	/**
	 * Sets the linkTag.
	 * 
	 * @param linkTag The linkTag to set
	 */
	public void setLinkTag(boolean linkTag) {
		this.linkTag = linkTag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.actf.visualization.engines.voicebrowser.IContext#isInsideAnchor()
	 */
	public boolean isInsideAnchor() {
		return insideAnchor;
	}

	/**
	 * Sets the insideAnchor.
	 * 
	 * @param insideAnchor The insideAnchor to set
	 */
	public void setInsideAnchor(boolean insideAnchor) {
		this.insideAnchor = insideAnchor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.actf.visualization.engines.voicebrowser.IContext#isStartSelect()
	 */
	public boolean isStartSelect() {
		return startSelect;
	}

	/**
	 * Sets the startSelect.
	 * 
	 * @param startSelect The startSelect to set
	 */
	public void setStartSelect(boolean startSelect) {
		this.startSelect = startSelect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.actf.visualization.engines.voicebrowser.IContext#isStringOutput()
	 */
	public boolean isStringOutput() {
		return stringOutput;
	}

	/**
	 * Sets the stringOutput.
	 * 
	 * @param stringOutput The stringOutput to set
	 */
	public void setStringOutput(boolean stringOutput) {
		this.stringOutput = stringOutput;
	}

	/**
	 * Convert object into a string
	 * 
	 * @return converted string
	 */
	@SuppressWarnings("nls")
	public String toString() {
		StringBuffer sbuf = new StringBuffer();

		sbuf.append("goChild=");
		sbuf.append(goChild);
		sbuf.append(COMMA_SPACE);

		sbuf.append("insideForm=");
		sbuf.append(insideForm);
		sbuf.append(COMMA_SPACE);

		sbuf.append("insideAnchor=");
		sbuf.append(insideAnchor);
		sbuf.append(COMMA_SPACE);

		sbuf.append("startSelect=");
		sbuf.append(startSelect);
		sbuf.append(COMMA_SPACE);

		sbuf.append("stringOutput=");
		sbuf.append(stringOutput);
		sbuf.append(COMMA_SPACE);

		sbuf.append("linkTag=");
		sbuf.append(linkTag);
		sbuf.append(COMMA_SPACE);

		sbuf.append("lineDelimiter=");
		sbuf.append(lineDelimiter);

		return sbuf.toString();
	}

	public String getHref() {
		return href;
	}

	/**
	 * Sets the value of href attribute of the context.
	 * 
	 * @param href The value of href attribute.
	 */
	public void setHref(String href) {
		this.href = href;
	}
}
