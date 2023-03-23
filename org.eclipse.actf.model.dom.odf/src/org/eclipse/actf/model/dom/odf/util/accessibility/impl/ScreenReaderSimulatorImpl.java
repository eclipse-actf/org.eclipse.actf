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
package org.eclipse.actf.model.dom.odf.util.accessibility.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.ContentBaseElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.draw.PageElement;
import org.eclipse.actf.model.dom.odf.office.BodyElement;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.office.PresentationElement;
import org.eclipse.actf.model.dom.odf.office.SpreadSheetElement;
import org.eclipse.actf.model.dom.odf.office.TextElement;
import org.eclipse.actf.model.dom.odf.presentation.NotesElement;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.util.accessibility.ScreenReaderSimulator;
import org.eclipse.actf.model.dom.odf.util.converter.impl.TextExtractorImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ScreenReaderSimulatorImpl implements ScreenReaderSimulator {
	private static final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	private ODFElement curElem = null;

	private TextExtractorImpl textExtractor;

	public ScreenReaderSimulatorImpl() {
		textExtractor = new TextExtractorImpl();
	}

	public void setDocument(ODFDocument document) {
		this.curElem = (ODFElement) document.getDocumentElement();
		this.textExtractor.setDocument(document, this);
	}

	public void setElement(ODFElement element) {
		this.curElem = element;
		this.textExtractor.setDocument(
				(ODFDocument) element.getOwnerDocument(), this);
	}

	// for Notes8, Notes8 save document by ODF 1.1 schema,
	// but odf:version is set as 1.0
	// if specify odf:version by this function,
	// do not use odf:version defined in each ODF file
	public void setOdfVersion(double odfVersion) {
		this.textExtractor.setOdfVersion(odfVersion);
	}

	private void writePageElementContent(Writer writer, PageElement page) {
		/*
		 * NodeList children = page.getChildNodes(); for (int j=0;
		 * j<children.getLength(); j++) { Node child = children.item(j);
		 */
		List<ODFElement> children = page.getChildNodesInNavOrder();
		for (int j = 0; j < children.size(); j++) {
			Node child = children.get(j);
			if ((child instanceof ODFElement)
					&& (!(child instanceof NotesElement))) {
				String str = getElementContent((ODFElement) child);
				if (str != null) {
					try {
						writer.write(str);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public boolean extractContent(Writer writer, File dir, ODFElement elem,
			boolean enableStyle) {
		return textExtractor.extractContent(writer, dir, elem, enableStyle);
	}

	public String getElementContent(ODFElement elem) {
		setElement(elem);

		StringWriter writer = new StringWriter();

		if (elem instanceof PageElement) {
			writePageElementContent(writer, (PageElement) elem);
		} else {
			extractContent(writer, null, elem, false);
		}

		String str = ""; //$NON-NLS-1$
		try {
			writer.flush();
			writer.close();
			str = writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	public String getCurrentElementContent() {
		return getElementContent(curElem);
	}

	private String getTextDocumentContent(TextElement textElem) {
		StringWriter writer = new StringWriter();
		for (Iterator<ITextElementContainer> iter = textElem.getChildIterator(); iter
				.hasNext();) {
			Element child = iter.next();
			if (child instanceof ODFElement) {
				String str = getElementContent((ODFElement) child);
				if (str != null)
					writer.write(str);
			}
		}
		String str = ""; //$NON-NLS-1$
		try {
			writer.flush();
			writer.close();
			str = writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	private String getSpreadsheetDocumentContent(SpreadSheetElement spreadElem) {
		StringWriter writer = new StringWriter();
		for (int i = 0; i < spreadElem.getTableSize(); i++) {
			TableElement table = spreadElem.getTable(i);
			String str = getElementContent(table);
			if (str != null)
				writer.write(str + LINE_SEP);
		}
		String str = ""; //$NON-NLS-1$
		try {
			writer.flush();
			writer.close();
			str = writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	private String getPresentationDocumentContent(PresentationElement presenElem) {
		StringWriter writer = new StringWriter();
		for (int i = 0; i < presenElem.getPageSize(); i++) {
			PageElement page = presenElem.getPage(i);
			NotesElement notes = page.getPresentationNotesElement();
			writer.write("Page "); //$NON-NLS-1$
			writer.write(i + 1);
			writer.write(LINE_SEP);
			writer.write(getElementContent(page));
			writer.write(LINE_SEP);
			writer.write("--------------------------------------"); //$NON-NLS-1$
			writer.write(LINE_SEP);
			writer.write("Speaker Notes"); //$NON-NLS-1$
			writer.write(LINE_SEP);
			writer.write(getElementContent(notes));
		}
		String str = ""; //$NON-NLS-1$
		try {
			writer.flush();
			writer.close();
			str = writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	public String getDocumentContent() {
		if (curElem == null)
			return null;
		Element root = curElem.getOwnerDocument().getDocumentElement();
		if (root instanceof DocumentContentElement) {
			BodyElement body = ((DocumentContentElement) root).getBodyElement();
			ContentBaseElement content = body.getContent();
			ContentType type = content.getContentType();
			if ((type.equals(ContentType.WRITE))
					&& (content instanceof TextElement)) {
				return getTextDocumentContent((TextElement) content);
			} else if (type.equals(ContentType.SPREADSHEET)
					&& (content instanceof SpreadSheetElement)) {
				return getSpreadsheetDocumentContent((SpreadSheetElement) content);
			} else if (type.equals(ContentType.PRESENTATION)
					&& (content instanceof PresentationElement)) {
				return getPresentationDocumentContent((PresentationElement) content);
			} else {
				new ODFException("invalid content element").printStackTrace(); //$NON-NLS-1$
			}
		} else {
			new ODFException("invalid odf document").printStackTrace(); //$NON-NLS-1$
		}
		return null;
	}
}
