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

import java.util.List;

import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.DrawingObjectBaseElement;
import org.eclipse.actf.model.dom.odf.base.DrawingObjectElement;
import org.eclipse.actf.model.dom.odf.base.EmbedDrawingObjectElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.content.IStylable;
import org.eclipse.actf.model.dom.odf.draw.ControlElement;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.FrameElement;
import org.eclipse.actf.model.dom.odf.draw.TextBoxElement;
import org.eclipse.actf.model.dom.odf.form.FormConstants;
import org.eclipse.actf.model.dom.odf.form.FormControlElement;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.office.OfficeConstants;
import org.eclipse.actf.model.dom.odf.office.SpreadSheetElement;
import org.eclipse.actf.model.dom.odf.presentation.NotesElement;
import org.eclipse.actf.model.dom.odf.presentation.PresentationConstants;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.model.dom.odf.style.StylePropertiesBase;
import org.eclipse.actf.model.dom.odf.style.TableCellPropertiesElement;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.eclipse.actf.model.dom.odf.table.CoveredTableCellElement;
import org.eclipse.actf.model.dom.odf.table.TableCellElement;
import org.eclipse.actf.model.dom.odf.table.TableColumnElement;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.table.TableRowElement;
import org.eclipse.actf.model.dom.odf.text.PElement;
import org.eclipse.actf.model.dom.odf.text.SequenceElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Utility class to fix ODF accessibility problems
 */
public class AccessibilityFixEngine {

	private static final String COLON = ":"; //$NON-NLS-1$

	private static final XPathService xpathService = XPathServiceFactory
			.newService();

	@SuppressWarnings("nls")
	private static final Object EXP1 = xpathService
			.compile("./ancestor::*[namespace-uri()='"
					+ TextConstants.TEXT_NAMESPACE_URI + "' and local-name()='"
					+ TextConstants.ELEMENT_SECTION + "']"
					+ "[attribute::*[namespace-uri()='"
					+ TextConstants.TEXT_NAMESPACE_URI + "' and local-name()='"
					+ TextConstants.ATTR_DISPLAY + "']='"
					+ TextConstants.ATTR_DISPLAY_VALUE_NONE + "']");

	@SuppressWarnings("nls")
	private static final Object EXP2 = xpathService
			.compile("./ancestor::*[(namespace-uri()='"
					+ TableConstants.TABLE_NAMESPACE_URI
					+ "' and local-name()='"
					+ TableConstants.ELEMENT_TABLE_CELL + "')"
					+ " or (namespace-uri()='"
					+ TableConstants.TABLE_NAMESPACE_URI
					+ "' and local-name()='"
					+ TableConstants.ELEMENT_COVERED_TABLE_CELL + "')]");

	@SuppressWarnings("nls")
	private static final Object EXP3 = xpathService
			.compile("./ancestor::*[namespace-uri()='"
					+ TableConstants.TABLE_NAMESPACE_URI
					+ "' and local-name()='" + TableConstants.ELEMENT_TABLE
					+ "']" + "[parent::*[namespace-uri()='"
					+ OfficeConstants.OFFICE_NAMESPACE_URI
					+ "' and local-name()='"
					+ OfficeConstants.ELEMENT_SPREADSHEET + "']]");

	private double odfVersion = -1.0;

	public AccessibilityFixEngine() {
	}

	public AccessibilityFixEngine(double odfVersion) {
		this.odfVersion = odfVersion;
	}

	public boolean fixShortDesc(ODFElement target, String shortDesc) {
		if ((shortDesc == null) || (shortDesc.length() == 0))
			return false;

		if (target instanceof DrawingObjectBaseElement) {
			ODFElement shortDescElem = null;
			if (odfVersion != -1.0) {
				shortDescElem = ((DrawingObjectBaseElement) target)
						.getShortDescElement(odfVersion);
			} else {
				shortDescElem = ((DrawingObjectBaseElement) target)
						.getShortDescElement();
			}

			if (shortDescElem == null) {
				Document doc = target.getOwnerDocument();
				if (doc instanceof ODFDocument) {
					Element newElem = null;
					ODFDocument odfDoc = ((ODFDocument) doc);
					String svgPrefix = target
							.lookupPrefix(SVGConstants.SVG_NAMESPACE_URI);
					if (svgPrefix == null)
						return false;

					if ((odfVersion == 1.0)
							|| ((odfVersion == -1.0) && (odfDoc.getODFVersion() == 1.0))) {
						newElem = doc.createElement(svgPrefix + COLON
								+ SVGConstants.ELEMENT_DESC);
						newElem.setTextContent(shortDesc);
					} else if ((odfVersion > 1.0)
							|| ((odfVersion == -1.0) && (odfDoc.getODFVersion() > 1.0))) {
						newElem = doc.createElement(svgPrefix + COLON
								+ SVGConstants.ELEMENT_TITLE);
						newElem.setTextContent(shortDesc);
					}

					if (newElem != null) {
						if (target instanceof EmbedDrawingObjectElement) {
							Element parent = (Element) target.getParentNode();
							parent.appendChild(newElem);
							return true;
						} else if (target instanceof DrawingObjectElement) {
							target.appendChild(newElem);
							return true;
						}
					}
				}
			} else {
				shortDescElem.setTextContent(shortDesc);
				return true;
			}
		}
		return false;
	}

	public boolean fixLongDesc(ODFElement target, String longDesc) {
		if ((longDesc == null) || (longDesc.length() == 0))
			return false;

		if (target instanceof DrawingObjectBaseElement) {
			ODFElement longDescElem = null;
			if (odfVersion != -1.0) {
				longDescElem = ((DrawingObjectBaseElement) target)
						.getLongDescElement(odfVersion);
			} else {
				longDescElem = ((DrawingObjectBaseElement) target)
						.getLongDescElement();
			}

			if (longDescElem == null) {
				Document doc = target.getOwnerDocument();
				if (doc instanceof ODFDocument) {
					Element newElem = null;
					ODFDocument odfDoc = ((ODFDocument) doc);
					Element parent = (Element) target.getParentNode();

					if ((odfVersion > 1.0)
							|| ((odfVersion == -1.0) && (odfDoc.getODFVersion() > 1.0))) {
						String svgPrefix = target
								.lookupPrefix(SVGConstants.SVG_NAMESPACE_URI);
						if (svgPrefix == null)
							return false;

						newElem = doc.createElement(svgPrefix + COLON
								+ SVGConstants.ELEMENT_DESC);
						newElem.setTextContent(longDesc);
					}

					if (newElem != null) {
						if (target instanceof EmbedDrawingObjectElement) {
							parent.appendChild(newElem);
							return true;
						} else if (target instanceof DrawingObjectElement) {
							target.appendChild(newElem);
							return true;
						}
					}
				}
			} else {
				longDescElem.setTextContent(longDesc);
				return true;
			}
		}
		return false;
	}

	private void expandRepeatedTableRow(TableRowElement row) {
		int repeat = row.getAttrTableNumberRowsRepeated();
		if (repeat != -1) {
			Node rowParent = row.getParentNode();
			row.removeAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_NUMBER_ROWS_REPEATED);
			for (int i = 0; i < repeat - 1; i++) {
				TableRowElement cloneRow = (TableRowElement) row
						.cloneNode(false);
				rowParent.insertBefore(cloneRow, row);
			}
		}
	}

	public boolean fixTableRowHeader(ODFElement target, int headerNum) {
		if (target instanceof TableElement) {
			NodeList testnl = ((TableElement) target).getTableHeaderRows();
			if ((testnl != null) && (testnl.getLength() > 0))
				return false;

			Document doc = target.getOwnerDocument();
			if (doc instanceof ODFDocument) {
				String tablePrefix = target
						.lookupPrefix(TableConstants.TABLE_NAMESPACE_URI);
				if (tablePrefix == null)
					return false;

				Element headerRow = doc.createElement(tablePrefix + COLON
						+ TableConstants.ELEMENT_TABLE_HEADER_ROWS);

				List<TableRowElement> rowList = ((TableElement) target)
						.getTableRowChildren();
				for (int i = 0; i < rowList.size(); i++) {
					expandRepeatedTableRow(rowList.get(i));
				}

				rowList = ((TableElement) target).getTableRowChildren();
				if ((rowList.size() == 0) || (headerNum > rowList.size()))
					return false;

				Node newHeaderRow = target.insertBefore(headerRow, rowList
						.get(0));
				for (int i = 0; i < headerNum; i++) {
					TableRowElement row = rowList.get(i);
					newHeaderRow.appendChild(row);
				}
				return true;
			}
		}
		return false;
	}

	private void expandRepeatedTableColumn(TableColumnElement col) {
		int repeat = col.getAttrTableNumberColumnsRepeated();
		if (repeat != -1) {
			Node colParent = col.getParentNode();
			col.removeAttributeNS(TableConstants.TABLE_NAMESPACE_URI,
					TableConstants.ATTR_NUMBER_COLUMNS_REPEATED);
			for (int i = 0; i < repeat - 1; i++) {
				TableColumnElement cloneCol = (TableColumnElement) col
						.cloneNode(false);
				colParent.insertBefore(cloneCol, col);
			}
		}
	}

	public boolean fixTableColumnHeader(ODFElement target, int headerNum) {
		if (target instanceof TableElement) {
			NodeList testnl = ((TableElement) target).getTableHeaderColumns();
			if ((testnl != null) && (testnl.getLength() > 0))
				return false;

			Document doc = target.getOwnerDocument();
			if (doc instanceof ODFDocument) {
				String tablePrefix = target
						.lookupPrefix(TableConstants.TABLE_NAMESPACE_URI);
				if (tablePrefix == null)
					return false;

				Element headerColumn = doc.createElement(tablePrefix + COLON
						+ TableConstants.ELEMENT_TABLE_HEADER_COLUMNS);

				List<TableColumnElement> colList = ((TableElement) target)
						.getTableColumnChildren();
				for (int i = 0; i < colList.size(); i++) {
					expandRepeatedTableColumn(colList.get(i));
				}

				colList = ((TableElement) target).getTableColumnChildren();
				if ((colList.size() == 0) || (headerNum > colList.size()))
					return false;

				Node newHeaderColumn = target.insertBefore(headerColumn,
						colList.get(0));
				for (int i = 0; i < headerNum; i++) {
					TableColumnElement col = colList.get(i);
					newHeaderColumn.appendChild(col);
				}
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("nls")
	public boolean fixTableCaption(ODFElement target, String caption) {
		if ((caption == null) || (caption.length() == 0))
			return false;

		if (target instanceof TableElement) {
			SequenceElement test = ((TableElement) target)
					.getTextSequenceElement();
			if (test != null)
				return false;

			Document doc = target.getOwnerDocument();
			if (doc instanceof ODFDocument) {
				// add text:p element next to table:table element
				Element newPElem = null;
				String textPrefix = target
						.lookupPrefix(TextConstants.TEXT_NAMESPACE_URI);
				if (textPrefix == null)
					return false;
				String stylePrefix = target
						.lookupPrefix(StyleConstants.STYLE_NAMESPACE_URI);
				if (stylePrefix == null)
					return false;

				Element pElem = doc.createElement(textPrefix + COLON
						+ TextConstants.ELEMENT_P);
				pElem.setAttribute(textPrefix + COLON
						+ TextConstants.ATTR_STYLE_NAME, "Table");
				Node parent = target.getParentNode();
				Node nextNode = target.getNextSibling();
				if (nextNode == null) {
					newPElem = (Element) parent.appendChild(pElem);
				} else {
					newPElem = (Element) parent.insertBefore(pElem, nextNode);
				}

				if (newPElem != null) {
					newPElem.appendChild(doc.createTextNode("Table "));

					// set content of text:p element
					Element seqElem = doc.createElement(textPrefix + COLON
							+ TextConstants.ELEMENT_SEQUENCE);
					seqElem.setAttribute(textPrefix + COLON
							+ TextConstants.ATTR_NAME, "Table");
					seqElem.setAttribute(textPrefix + COLON
							+ TextConstants.ATTR_FORMULA, "ooow:Table+1");
					seqElem.setAttribute(stylePrefix + COLON
							+ StyleConstants.ATTR_NUM_FORMAT, "1");
					newPElem.appendChild(seqElem);
					Text captionText = doc.createTextNode(" : " + caption);
					newPElem.appendChild(captionText);

					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("nls")
	public boolean isFormLabelFixCapable(ODFElement target) {
		final String CONTROL_IMPL_CHECKBOX = "com.sun.star.form.component.CheckBox";
		final String CONTROL_IMPL_OPTIONBUTTON = "com.sun.star.form.component.OptionButton";
		final String CONTROL_IMPL_PUSHBUTTON = "com.sun.star.form.component.PushButton";
		final String CONTROL_IMPL_RADIOBUTTON = "com.sun.star.form.component.RadioButton";
		final String CONTROL_IMPL_GROUPBOX = "com.sun.star.form.component.GroupBox";

		if (target instanceof ControlElement) {
			FormControlElement formControl = ((ControlElement) target)
					.getFormControlElement();
			String controlImpl = formControl.getAttrFormControlImplementation();
			if ((controlImpl.endsWith(CONTROL_IMPL_CHECKBOX))
					|| (controlImpl.endsWith(CONTROL_IMPL_OPTIONBUTTON))
					|| (controlImpl.endsWith(CONTROL_IMPL_PUSHBUTTON))
					|| (controlImpl.endsWith(CONTROL_IMPL_RADIOBUTTON))
					|| (controlImpl.endsWith(CONTROL_IMPL_GROUPBOX)))
				return true;
		}
		return false;
	}

	public boolean fixFormLabel(ODFElement target, String label) {
		if ((label == null) || (label.length() == 0))
			return false;

		if (target instanceof ControlElement) {
			FormControlElement formControl = ((ControlElement) target)
					.getFormControlElement();
			if (formControl.hasAttributeNS(FormConstants.FORM_NAMESPACE_URI,
					FormConstants.ATTR_LABEL)) {
				formControl.removeAttributeNS(FormConstants.FORM_NAMESPACE_URI,
						FormConstants.ATTR_LABEL);
			}
			String formPrefix = formControl.getPrefix();
			formControl.setAttribute(formPrefix + COLON
					+ FormConstants.ATTR_LABEL, label);
			return true;
		}
		return false;
	}

	public boolean fixFormTabStop(ODFElement target, boolean tabStop) {
		if (target instanceof ControlElement) {
			FormControlElement formControl = ((ControlElement) target)
					.getFormControlElement();
			if (formControl.hasAttributeNS(FormConstants.FORM_NAMESPACE_URI,
					FormConstants.ATTR_TAB_STOP)) {
				formControl.removeAttributeNS(FormConstants.FORM_NAMESPACE_URI,
						FormConstants.ATTR_TAB_STOP);
			}
			String formPrefix = formControl.getPrefix();
			formControl.setAttribute(formPrefix + COLON
					+ FormConstants.ATTR_TAB_STOP, new Boolean(tabStop)
					.toString());
			return true;
		}
		return false;
	}

	public boolean isInvisibleElement(ODFElement elem) {
		NodeList nl = xpathService.evalForNodeList(EXP1, elem);
		if ((nl != null) && (nl.getLength() > 0))
			return true;
		return false;
	}

	public String getInvisibleSectionName(ODFElement elem) {
		NodeList nl = xpathService.evalForNodeList(EXP1, elem);
		if ((nl != null) && (nl.getLength() > 0)) {
			if (nl.item(0) instanceof Element) {
				Element section = (Element) nl.item(0);
				if (section.hasAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ATTR_NAME)) {
					return section.getAttributeNS(
							TextConstants.TEXT_NAMESPACE_URI,
							TextConstants.ATTR_NAME);
				}
			}
		}
		return null;
	}

	public boolean isProtectedElement(ODFElement target) {
		ContentType type = ContentType.NONE;
		Element content = target.getOwnerDocument().getDocumentElement();
		if (content instanceof DocumentContentElement) {
			type = ((DocumentContentElement) content).getBodyElement()
					.getContent().getContentType();
		}
		if (type != ContentType.SPREADSHEET)
			return false;

		if (target instanceof SpreadSheetElement) {
			SpreadSheetElement spreadsheet = (SpreadSheetElement) target;
			return spreadsheet.getAttrTableStructureProtected();
		} else if (target instanceof TableElement) {
			TableElement table = (TableElement) target;
			return table.getAttrTableProtected();
		} else {
			SpreadSheetElement spreadsheet = null;
			TableElement table = null;
			IStylable cell = null;

			if ((target instanceof TableCellElement)
					|| (target instanceof CoveredTableCellElement)) {
				cell = (IStylable) target;
			} else {
				NodeList cellList = xpathService.evalForNodeList(EXP2, target);
				if ((cellList != null) && (cellList.getLength() == 1)) {
					Node node = cellList.item(0);
					if ((node instanceof TableCellElement)
							|| (node instanceof CoveredTableCellElement)) {
						cell = (IStylable) node;
					}
				}
			}
			if (cell == null)
				return false;

			NodeList tableList = xpathService.evalForNodeList(EXP3, target);
			if ((tableList != null) && (tableList.getLength() == 1)) {
				Node node = tableList.item(0);
				if (node instanceof TableElement) {
					table = (TableElement) node;
				}
			}
			if (table == null)
				return false;

			Node tableParent = table.getParentNode();
			if (tableParent instanceof SpreadSheetElement) {
				spreadsheet = (SpreadSheetElement) tableParent;
			}
			if (spreadsheet == null)
				return false;

			if (spreadsheet.getAttrTableStructureProtected()
					|| table.getAttrTableProtected()) {
				int cellProtect = StyleConstants.CELL_PROTECT_VALUE_NOT_DEFINED;

				StyleElement styleElem = cell.getStyle();
				if (styleElem != null) {
					for (int i = 0; i < styleElem.getPropertySize(); i++) {
						StylePropertiesBase prop = styleElem
								.getPropertyElement(i);
						if (prop instanceof TableCellPropertiesElement) {
							TableCellPropertiesElement cellProp = (TableCellPropertiesElement) prop;
							cellProtect = cellProp.getAttrStyleCellProtect();
						}
					}
				}

				if ((cellProtect == StyleConstants.CELL_PROTECT_VALUE_PROTECTED)
						|| (cellProtect == StyleConstants.CELL_PROTECT_VALUE_HIDDEN_AND_PROTECTED)) {
					return true;
				} else if (cellProtect == StyleConstants.CELL_PROTECT_VALUE_NOT_DEFINED) {
					if (table.hasAttributeNS(
							TableConstants.TABLE_NAMESPACE_URI,
							TableConstants.ATTR_PROTECTED))
						return table.getAttrTableProtected();
					if (spreadsheet.hasAttributeNS(
							TableConstants.TABLE_NAMESPACE_URI,
							TableConstants.ATTR_STRUCTURE_PROTECTED))
						return spreadsheet.getAttrTableStructureProtected();
				}
			}
		}
		return false;
	}

	public void setSpeakerNotesContent(NotesElement notes, String content) {
		String[] contentList = content.split("\n"); //$NON-NLS-1$

		NodeList frameList = notes.getElementsByTagNameNS(
				DrawConstants.DRAW_NAMESPACE_URI, DrawConstants.ELEMENT_FRAME);
		if ((frameList == null) || (frameList.getLength() <= 0))
			return;

		FrameElement notesFrame = null;
		for (int i = 0; i < frameList.getLength(); i++) {
			Node node = frameList.item(i);
			if (node instanceof FrameElement) {
				FrameElement frame = (FrameElement) node;
				String presentationClass = frame.getAttributeNS(
						PresentationConstants.PRESENTATION_NAMESPACE_URI,
						PresentationConstants.ATTR_CLASS);
				if ((presentationClass != null)
						&& (presentationClass
								.equals(PresentationConstants.ATTR_CLASS_VALUE_NOTES))) {
					notesFrame = frame;
				}
			}
		}
		if (notesFrame == null)
			return;

		NodeList textBoxList = notesFrame.getElementsByTagNameNS(
				DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ELEMENT_TEXT_BOX);
		if ((textBoxList == null) || (textBoxList.getLength() != 1))
			return;

		Document doc = notes.getOwnerDocument();
		TextBoxElement textBox = (TextBoxElement) textBoxList.item(0);
		NodeList pList = textBox.getElementsByTagNameNS(
				TextConstants.TEXT_NAMESPACE_URI, TextConstants.ELEMENT_P);

		for (int i = 0; i < contentList.length; i++) {
			PElement pElem = null;
			if ((i == 0) && (pList != null) && (pList.getLength() == 1)) {
				pElem = (PElement) pList.item(0);
			} else {
				pElem = (PElement) doc.createElementNS(
						TextConstants.TEXT_NAMESPACE_URI,
						TextConstants.ELEMENT_P);
			}
			textBox.appendChild(pElem);

			Text text = doc.createTextNode(contentList[i]);
			pElem.appendChild(text);
		}
	}
}
