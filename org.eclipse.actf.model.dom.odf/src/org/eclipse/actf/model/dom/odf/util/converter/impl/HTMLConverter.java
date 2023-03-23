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
package org.eclipse.actf.model.dom.odf.util.converter.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.eclipse.actf.model.dom.odf.ODFException;
import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.ContentBaseElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.base.impl.ODFElementImpl;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.draw.FrameElement;
import org.eclipse.actf.model.dom.odf.draw.ImageElement;
import org.eclipse.actf.model.dom.odf.draw.PageElement;
import org.eclipse.actf.model.dom.odf.draw.TextBoxElement;
import org.eclipse.actf.model.dom.odf.office.BodyElement;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.office.PresentationElement;
import org.eclipse.actf.model.dom.odf.office.SpreadSheetElement;
import org.eclipse.actf.model.dom.odf.office.TextElement;
import org.eclipse.actf.model.dom.odf.presentation.NotesElement;
import org.eclipse.actf.model.dom.odf.range.ITextElementContainer;
import org.eclipse.actf.model.dom.odf.style.DefaultStyleElement;
import org.eclipse.actf.model.dom.odf.style.FontFaceElement;
import org.eclipse.actf.model.dom.odf.style.GraphicPropertiesElement;
import org.eclipse.actf.model.dom.odf.style.ParagraphPropertiesElement;
import org.eclipse.actf.model.dom.odf.style.StyleConstants;
import org.eclipse.actf.model.dom.odf.style.StyleElement;
import org.eclipse.actf.model.dom.odf.style.TableCellPropertiesElement;
import org.eclipse.actf.model.dom.odf.style.TableColumnPropertiesElement;
import org.eclipse.actf.model.dom.odf.style.TablePropertiesElement;
import org.eclipse.actf.model.dom.odf.style.TextPropertiesElement;
import org.eclipse.actf.model.dom.odf.table.TableCellElement;
import org.eclipse.actf.model.dom.odf.table.TableColumnElement;
import org.eclipse.actf.model.dom.odf.table.TableColumnsElement;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.table.TableHeaderRowsElement;
import org.eclipse.actf.model.dom.odf.table.TableRowElement;
import org.eclipse.actf.model.dom.odf.text.AElement;
import org.eclipse.actf.model.dom.odf.text.ChangeElement;
import org.eclipse.actf.model.dom.odf.text.ChangeEndElement;
import org.eclipse.actf.model.dom.odf.text.ChangeStartElement;
import org.eclipse.actf.model.dom.odf.text.HElement;
import org.eclipse.actf.model.dom.odf.text.ListElement;
import org.eclipse.actf.model.dom.odf.text.ListItemElement;
import org.eclipse.actf.model.dom.odf.text.ListLevelStyleBulletElement;
import org.eclipse.actf.model.dom.odf.text.ListLevelStyleNumberElement;
import org.eclipse.actf.model.dom.odf.text.ListStyleElement;
import org.eclipse.actf.model.dom.odf.text.PElement;
import org.eclipse.actf.model.dom.odf.text.SElement;
import org.eclipse.actf.model.dom.odf.text.SequenceElement;
import org.eclipse.actf.model.dom.odf.text.SpanElement;
import org.eclipse.actf.model.dom.odf.text.TabElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.eclipse.actf.model.dom.odf.util.converter.ODFConverter;
import org.eclipse.actf.model.dom.odf.xlink.XLinkConstants;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("nls")
public class HTMLConverter implements ODFConverter {
	private static final String HTML_ENCODING = "UTF8";

	private static final String ODF_CSS_FILE = "ODF.css";

	private static final String HTML_HEADER = "<html><head><META http-equiv='Content-Type' content='text/html; charset=UTF-8'/>"
			+ "<meta content='text/html; charset=UTF-8' http-equiv='Content-type'/><title></title>"
			+ "<link rel='stylesheet' type='text/css' href='"
			+ ODF_CSS_FILE
			+ "'/></head>";

	private double odfVersion = -1.0;

	private ODFElement curElem = null;

	private TextExtractorImpl textExtractor;

	public HTMLConverter() {
		textExtractor = new TextExtractorImpl();
	}

	public void setDocument(ODFDocument document) {
		this.curElem = (ODFElement) document.getDocumentElement();
		this.textExtractor.setDocument(document, this);
	}

	// for Notes8, Notes8 save document by ODF 1.1 schema,
	// but odf:version is set as 1.0
	// if specify odf:version by this function,
	// do not use odf:version defined in each ODF file
	public void setOdfVersion(double odfVersion) {
		this.odfVersion = odfVersion;
		this.textExtractor.setOdfVersion(odfVersion);
	}

	private String getHTMLTagName(ODFElement elem) {
		if (elem instanceof ImageElement) {
			return "img";
		} else if (elem instanceof HElement) {
			int level = ((HElement) elem).getAttrTextOutlineLevel();
			if (level > 6)
				level = 6;
			return "h" + level;
		} else if (elem instanceof ListElement) {
			StyleElement style = ((ListElement) elem)
					.getListLevelStyleElement();
			if (style != null) {
				if (style instanceof ListLevelStyleBulletElement) {
					return "ul";
				} else if (style instanceof ListLevelStyleNumberElement) {
					return "ol";
				}
			}
		} else if (elem instanceof TableCellElement) {
			Node parent = elem.getParentNode();
			Node gparent = null;
			if (parent != null)
				gparent = parent.getParentNode();
			if ((parent != null) && (parent instanceof TableRowElement)
					&& (gparent != null)
					&& (gparent instanceof TableHeaderRowsElement)) {
				return "th";
			} else {
				return "td";
			}
		}

		String tagName = null;
		if (elem instanceof PElement)
			tagName = "p";
		if (elem instanceof AElement)
			tagName = "a";
		if (elem instanceof SpanElement)
			tagName = "span";
		if (elem instanceof TabElement)
			tagName = "span";
		if (elem instanceof SequenceElement)
			tagName = "span";
		if (elem instanceof SElement)
			tagName = "span";
		if (elem instanceof ListItemElement)
			tagName = "li";
		if (elem instanceof TableElement)
			tagName = "table";
		if (elem instanceof TableHeaderRowsElement)
			tagName = "thead";
		if (elem instanceof TableRowElement)
			tagName = "tr";
		if (elem instanceof TableColumnsElement)
			tagName = "colgroup";
		if (elem instanceof TableColumnElement)
			tagName = "col";
		if (elem instanceof ChangeElement)
			tagName = "span";
		if (elem instanceof ChangeStartElement)
			tagName = "span";
		if (elem instanceof ChangeEndElement)
			tagName = "span";
		if (tagName == null)
			tagName = "div";
		return tagName;
	}

	private String getStyleClassName(ODFElement elem) {
		String styleClassName = null;
		if (elem instanceof ListElement) {
			ListElement topListElem = ((ListElement) elem)
					.getTopLevelListElement();
			long level = ((ListElement) elem).getListLevel();
			styleClassName = topListElem.getAttributeNS(
					TextConstants.TEXT_NAMESPACE_URI,
					TextConstants.ATTR_STYLE_NAME)
					+ level;
		} else {
			NamedNodeMap attrs = elem.getAttributes();
			for (int i = 0; i < attrs.getLength(); i++) {
				Node attr = attrs.item(i);
				if ("style-name".equals(attr.getLocalName())) {
					if (styleClassName == null)
						styleClassName = new String();
					else
						styleClassName += " ";
					styleClassName += attr.getNodeValue();
				}
			}
		}
		return styleClassName;
	}

	public boolean extractContent(Writer writer, File dir, ODFElement elem,
			boolean enableStyle) {
		String htmlTag = getHTMLTagName(elem);
		// write tag name
		if (htmlTag != null) {
			try {
				writer.write("<" + htmlTag + " " + OUTPUT_ATTR_ODF_TAGNAME
						+ "='" + elem.getTagName() + "' id='"
						+ elem.getAttribute(OUTPUT_ATTR_ODF_CONTENT_ID)
						+ "' name='"
						+ elem.getAttribute(OUTPUT_ATTR_ODF_CONTENT_ID) + "' "
						+ OUTPUT_ATTR_ODF_CONTENT_ID + "='"
						+ elem.getAttribute(OUTPUT_ATTR_ODF_CONTENT_ID) + "'");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (enableStyle) {
				String styleClassName = getStyleClassName(elem);
				if (styleClassName != null) {
					try {
						writer.write(" class='" + styleClassName + "'");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			if (elem instanceof ImageElement) {
				String href = ((ImageElement) elem).getAttrXlinkHref();
				if (!href.startsWith("http://") && !href.startsWith("https://")
						&& !href.startsWith("file://")
						&& !href.startsWith("chrome://")) {
					textExtractor.extractEmbedFile(elem, dir, href);
				}
				try {
					writer.write(" src='" + href + "'");
				} catch (IOException e) {
					e.printStackTrace();
				}

				ODFElement odfElem = null;
				if (this.odfVersion != -1.0) {
					odfElem = ((ImageElement) elem)
							.getShortDescElement(odfVersion);
				} else {
					odfElem = ((ImageElement) elem).getShortDescElement();
				}

				if (odfElem != null) {
					String altText = (String) ((IEditable) odfElem).getValue();
					if (altText != null) {
						try {
							writer.write(" alt='" + altText + "'");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					FrameElement frameElem = ((ImageElement) elem)
							.getFrameElement();
					if (frameElem != null) {
						TextBoxElement captionElem = null;
						if (odfVersion != -1.0) {
							captionElem = frameElem
									.getBoundCaptionTextBoxElement(odfVersion);
						} else {
							captionElem = frameElem
									.getBoundCaptionTextBoxElement();
						}

						if (captionElem != null) {
							String captionText = captionElem.getTextContent();
							if (captionText != null) {
								try {
									writer.write(" alt=''");
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}

						String width = frameElem.getAttrSvgWidth();
						if (width == null)
							width = "auto";
						String height = frameElem.getAttrSvgHeight();
						if (height == null)
							height = "auto";
						try {
							writer.write(" style='width:" + width + ";height:"
									+ height + ";'");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else if (elem instanceof AElement) {
				String type = ((AElement) elem).getType();
				if (XLinkConstants.LINK_TYPE_SIMPLE.equals(type)) {
					String href = ((AElement) elem).getHref();
					if (href.startsWith("#")) {
						ODFElement linkElem = ((AElement) elem)
								.getHrefElement();
						if (linkElem != null) {
							try {
								writer
										.write(" href='#"
												+ linkElem
														.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID)
												+ "'");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						try {
							writer.write(" href='" + href + "'");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else if (elem instanceof TableColumnElement) {
				int repeat = ((TableColumnElement) elem)
						.getAttrTableNumberColumnsRepeated();
				if (repeat > 1) {
					try {
						writer.write(">");
						writer.write("</" + htmlTag + ">\n");
						for (int i = 1; i < repeat; i++)
							writer.write("<" + htmlTag + "/>\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				}
			} else if (elem instanceof TableCellElement) {
				int span = ((TableCellElement) elem)
						.getAttrTableNumberColumnsSpanned();
				if (span > 1) {
					try {
						writer.write(" colspan='" + span + "'");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				int repeat = ((TableCellElement) elem)
						.getAttrTableNumberColumnsRepeated();
				if (repeat > 1) {
					try {
						writer.write(">");
						writer.write("</" + htmlTag + ">\n");
						for (int i = 1; i < repeat; i++)
							writer.write("<" + htmlTag + "/>\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				}
			}

			try {
				writer.write(">");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		boolean addedTextContent = textExtractor.extractContent(writer, dir,
				elem, enableStyle);

		// write close tag
		if (htmlTag != null) {
			try {
				writer.write("</" + htmlTag + ">\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return addedTextContent;
	}

	private void convertTextDoc(PrintWriter writer, File dir,
			TextElement textElem, boolean enableStyle) {
		for (Iterator<ITextElementContainer> iter = textElem.getChildIterator(); iter
				.hasNext();) {
			ODFElement child = iter.next();
			extractContent(writer, dir, child, enableStyle);
		}
	}

	private void convertSpreadsheetDoc(PrintWriter writer, File dir,
			SpreadSheetElement spreadElem, boolean enableStyle) {
		for (int i = 0; i < spreadElem.getTableSize(); i++) {
			TableElement table = spreadElem.getTable(i);
			writer.write("<h1>" + table.getAttrTableName() + "</h1>");
			extractContent(writer, dir, table, enableStyle);
		}
	}

	private void convertPresentationDoc(PrintWriter writer, File dir,
			PresentationElement presenElem, boolean enableStyle) {
		for (int i = 0; i < presenElem.getPageSize(); i++) {
			PageElement page = presenElem.getPage(i);
			writer.write("<div" + " " + OUTPUT_ATTR_ODF_TAGNAME + "='"
					+ page.getTagName() + "' id='"
					+ page.getAttribute(OUTPUT_ATTR_ODF_CONTENT_ID)
					+ "' name='"
					+ page.getAttribute(OUTPUT_ATTR_ODF_CONTENT_ID) + "' "
					+ OUTPUT_ATTR_ODF_CONTENT_ID + "='"
					+ page.getAttribute(OUTPUT_ATTR_ODF_CONTENT_ID)
					+ "'><h1>Page " + (i + 1) + "</h1>");
			/*
			 * NodeList children = page.getChildNodes(); for (int j = 0; j <
			 * children.getLength(); j++) { Node child = children.item(j);
			 */
			List<ODFElement> children = page.getChildNodesInNavOrder();
			for (int j = 0; j < children.size(); j++) {
				Node child = children.get(j);
				if ((child instanceof ODFElement)
						&& (!(child instanceof NotesElement))) {
					extractContent(writer, dir, (ODFElement) child, enableStyle);
				}
			}
			NotesElement notesElem = page.getPresentationNotesElement();
			if (notesElem != null) {
				writer.write("<h2>Notes of Page " + (i + 1) + "</h2>");
				extractContent(writer, dir, notesElem, enableStyle);
			}
			writer.write("</div>");
		}
	}

	private void convertDocument(PrintWriter writer, File dir,
			boolean enableStyle) {
		if (curElem == null)
			return;
		Element root = curElem.getOwnerDocument().getDocumentElement();
		if (root instanceof DocumentContentElement) {
			writer.write(HTML_HEADER);
			writer.write("<body>");

			BodyElement body = ((DocumentContentElement) root).getBodyElement();
			ContentBaseElement content = body.getContent();
			ContentType type = content.getContentType();
			if ((type.equals(ContentType.WRITE))
					&& (content instanceof TextElement)) {
				convertTextDoc(writer, dir, (TextElement) content, enableStyle);
			} else if (type.equals(ContentType.SPREADSHEET)
					&& (content instanceof SpreadSheetElement)) {
				convertSpreadsheetDoc(writer, dir,
						(SpreadSheetElement) content, enableStyle);
			} else if (type.equals(ContentType.PRESENTATION)
					&& (content instanceof PresentationElement)) {
				convertPresentationDoc(writer, dir,
						(PresentationElement) content, enableStyle);
			} else {
				new ODFException("invalid content element").printStackTrace();
			}

			writer.write("</body></html>");
		} else {
			new ODFException("invalid odf document").printStackTrace();
		}
		return;
	}

	private void writeCssValueByTextPropertiesElement(PrintWriter writer,
			StyleElement styleElem) {
		NodeList textProps = styleElem.getElementsByTagNameNS(
				StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ELEMENT_TEXT_PROPERTIES);
		for (int i = 0; i < textProps.getLength(); i++) {
			TextPropertiesElement prop = (TextPropertiesElement) textProps
					.item(i);
			String fontName = prop.getAttrStyleFontName();
			if (fontName != null) {
				ODFElement fontFaceElem = ((ODFElementImpl) styleElem)
						.findElementByAttrValue(
								StyleConstants.STYLE_NAMESPACE_URI,
								StyleConstants.ELEMENT_FONT_FACE,
								StyleConstants.STYLE_NAMESPACE_URI,
								StyleConstants.ATTR_NAME, fontName);
				if (fontFaceElem == null) {
					fontFaceElem = ((ODFElementImpl) styleElem)
							.findElementByAttrValueFromStyleDoc(
									StyleConstants.STYLE_NAMESPACE_URI,
									StyleConstants.ELEMENT_FONT_FACE,
									StyleConstants.STYLE_NAMESPACE_URI,
									StyleConstants.ATTR_NAME, fontName);
				}
				if (fontFaceElem != null) {
					String fontFamily = ((FontFaceElement) fontFaceElem)
							.getAttrSvgFontFamily();
					writer.write("font-family:" + fontFamily + ";");
				}
			}
			String fontSize = prop.getAttrFormatFontSize();
			if (fontSize != null) {
				writer.write("font-size:" + fontSize + ";");
			}
			String fontColor = prop.getAttrFormatColor();
			if (fontColor != null) {
				writer.write("color:" + fontColor + ";");
			}
			String backgroundColor = prop.getAttrFormatBackgroundColor();
			if (backgroundColor != null) {
				writer.write("background-color:" + backgroundColor + ";");
			}
			String fontWeight = prop.getAttrFormatFontWeight();
			if ((fontWeight != null) && (fontWeight.equals("bold"))) {
				writer.write("font-weight:bold;");
			}
			String fontStyle = prop.getAttrFormatFontStyle();
			if ((fontStyle != null) && (fontStyle.equals("italic"))) {
				writer.write("font-style:italic;");
			}
			String underLineStyle = prop.getAttrStyleTextUnderlineStyle();
			if ((underLineStyle != null) && (underLineStyle.equals("solid"))) {
				writer.write("text-decoration:underline;");
			}
			String textPosition = prop.getAttrStyleTextPosition();
			if (textPosition != null) {
				String[] textPositionVals = textPosition.split(" ");
				if (textPositionVals.length == 2) {
					if (textPositionVals[0].equals("super")) {
						writer.write("vertical-align:super;");
					} else if (textPositionVals[0].equals("sub")) {
						writer.write("vertical-align:sub;");
					}
					writer.write("font-size:" + textPositionVals[1] + ";");
				}
			}
		}
	}

	private void writeCssValueByParagraphPropertiesElement(PrintWriter writer,
			StyleElement styleElem) {
		NodeList paraProps = styleElem.getElementsByTagNameNS(
				StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ELEMENT_PARAGRAPH_PROPERTIES);
		for (int i = 0; i < paraProps.getLength(); i++) {
			ParagraphPropertiesElement prop = (ParagraphPropertiesElement) paraProps
					.item(i);
			String backgroundColor = prop.getAttrFormatBackgroundColor();
			if (backgroundColor != null) {
				writer.write("background-color:" + backgroundColor + ";");
			}
			String textAlign = prop.getAttrFormatTextAlign();
			if (textAlign != null) {
				writer.write("text-align:" + textAlign + ";");
			}
			String verticalAlign = prop.getAttrStyleVerticalAlign();
			if (verticalAlign != null) {
				writer.write("vertical-align:" + verticalAlign + ";");
			}
			String marginLeft = prop.getAttrFormatMarginLeft();
			if (marginLeft != null) {
				writer.write("margin-left:" + marginLeft + ";");
			}
			String marginRight = prop.getAttrFormatMarginRight();
			if (marginRight != null) {
				writer.write("margin-right:" + marginRight + ";");
			}
			String marginTop = prop.getAttrFormatMarginTop();
			if (marginTop != null) {
				writer.write("margin-top:" + marginTop + ";");
			}
			String marginBottom = prop.getAttrFormatMarginBottom();
			if (marginBottom != null) {
				writer.write("margin-bottom:" + marginBottom + ";");
			}
		}
	}

	private void writeCssValueByGraphicPropertiesElement(PrintWriter writer,
			StyleElement styleElem) {
		NodeList graphicProps = styleElem.getElementsByTagNameNS(
				StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ELEMENT_GRAPHIC_PROPERTIES);
		for (int i = 0; i < graphicProps.getLength(); i++) {
			GraphicPropertiesElement prop = (GraphicPropertiesElement) graphicProps
					.item(i);
			String horizontalPos = prop.getAttrStyleHorizontalPos();
			if (horizontalPos != null) {
				if (horizontalPos.equals("left")) {
					writer.write("text-align:left;");
				} else if (horizontalPos.equals("center")) {
					writer.write("text-align:center;");
				} else if (horizontalPos.equals("right")) {
					writer.write("text-align:right;");
				}
			}
		}
	}

	private void writeCssValueByTablePropertiesElement(PrintWriter writer,
			StyleElement styleElem) {
		NodeList tableProps = styleElem.getElementsByTagNameNS(
				StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ELEMENT_TABLE_PROPERTIES);
		for (int i = 0; i < tableProps.getLength(); i++) {
			TablePropertiesElement prop = (TablePropertiesElement) tableProps
					.item(i);
			String width = prop.getAttrStyleWidth();
			if (width != null) {
				writer.write("width:" + width + ";");
			}
			String borderModel = prop.getAttrTableBorderModel();
			if (borderModel != null) {
				if (borderModel.equals("collapsing")) {
					writer.write("border-collapse:collapse;");
				} else if (borderModel.equals("separating")) {
					writer.write("border-collapse:separate;");
				}
			}
		}
	}

	private void writeCssValueByTableColumnPropertiesElement(
			PrintWriter writer, StyleElement styleElem) {
		NodeList tableColProps = styleElem.getElementsByTagNameNS(
				StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ELEMENT_TABLE_COLUMN_PROPERTIES);
		for (int i = 0; i < tableColProps.getLength(); i++) {
			TableColumnPropertiesElement prop = (TableColumnPropertiesElement) tableColProps
					.item(i);
			String width = prop.getAttrStyleColumnWidth();
			if (width != null) {
				writer.write("width:" + width + ";");
			}
		}
	}

	private void writeCssValueByTableCellPropertiesElement(PrintWriter writer,
			StyleElement styleElem) {
		NodeList tableCellProps = styleElem.getElementsByTagNameNS(
				StyleConstants.STYLE_NAMESPACE_URI,
				StyleConstants.ELEMENT_TABLE_CELL_PROPERTIES);
		for (int i = 0; i < tableCellProps.getLength(); i++) {
			TableCellPropertiesElement prop = (TableCellPropertiesElement) tableCellProps
					.item(i);
			String border = prop.getAttrFormatBorder();
			if (border != null) {
				writer.write("border:" + border + ";");
			}
			String borderLeft = prop.getAttrFormatBorderLeft();
			if (borderLeft != null) {
				writer.write("border-left:" + borderLeft + ";");
			}
			String borderRight = prop.getAttrFormatBorderRight();
			if (borderRight != null) {
				writer.write("border-right:" + borderRight + ";");
			}
			String borderTop = prop.getAttrFormatBorderTop();
			if (borderTop != null) {
				writer.write("border-top:" + borderTop + ";");
			}
			String borderBottom = prop.getAttrFormatBorderBottom();
			if (borderBottom != null) {
				writer.write("border-bottom:" + borderBottom + ";");
			}
			String backgroundColor = prop.getAttrFormatBackgroundColor();
			if (backgroundColor != null) {
				writer.write("background-color:" + backgroundColor + ";");
			}
			String padding = prop.getAttrFormatPadding();
			if (padding != null) {
				writer.write("padding:" + padding + ";");
			}
		}
	}

	private void writeCssValueByStyleStyleElement(PrintWriter writer,
			StyleElement styleElem) {
		String parentStyleName = styleElem.getAttrStyleParentStyleName();
		if (parentStyleName != null) {
			ODFElement parentStyle = ((ODFElementImpl) styleElem)
					.findElementByAttrValue(StyleConstants.STYLE_NAMESPACE_URI,
							StyleConstants.ELEMENT_STYLE,
							StyleConstants.STYLE_NAMESPACE_URI,
							StyleConstants.ATTR_NAME, parentStyleName);
			if (parentStyle == null) {
				parentStyle = ((ODFElementImpl) styleElem)
						.findElementByAttrValueFromStyleDoc(
								StyleConstants.STYLE_NAMESPACE_URI,
								StyleConstants.ELEMENT_STYLE,
								StyleConstants.STYLE_NAMESPACE_URI,
								StyleConstants.ATTR_NAME, parentStyleName);
			}
			if (parentStyle != null) {
				writeCssValueByStyleStyleElement(writer,
						(StyleElement) parentStyle);
			}
		}

		writeCssValueByTextPropertiesElement(writer, styleElem);
		writeCssValueByParagraphPropertiesElement(writer, styleElem);
		writeCssValueByGraphicPropertiesElement(writer, styleElem);
		writeCssValueByTablePropertiesElement(writer, styleElem);
		writeCssValueByTableColumnPropertiesElement(writer, styleElem);
		writeCssValueByTableCellPropertiesElement(writer, styleElem);
	}

	private static final XPathService xpathService = XPathServiceFactory
			.newService();
	private static final Object EXP1 = xpathService
			.compile(".//*[namespace-uri()='"
					+ StyleConstants.STYLE_NAMESPACE_URI
					+ "' and local-name()='"
					+ StyleConstants.ELEMENT_DEFAULT_STYLE + "']");
	private static final Object EXP2 = xpathService
			.compile(".//*[namespace-uri()='"
					+ StyleConstants.STYLE_NAMESPACE_URI
					+ "' and local-name()='" + StyleConstants.ELEMENT_STYLE
					+ "']");
	private static final Object EXP3 = xpathService
			.compile(".//*[namespace-uri()='"
					+ TextConstants.TEXT_NAMESPACE_URI + "' and local-name()='"
					+ TextConstants.ELEMENT_LIST_STYLE + "']");

	private void writeCssByStyleDefaultStyleElement(PrintWriter writer,
			ODFDocument doc) {
		Element root = doc.getDocumentElement();
		NodeList styleDefaultStyleList = xpathService.evalForNodeList(EXP1,
				root);
		for (int i = 0; i < styleDefaultStyleList.getLength(); i++) {
			DefaultStyleElement styleElem = (DefaultStyleElement) styleDefaultStyleList
					.item(i);
			String styleFamily = styleElem.getAttrStyleFamily();
			if (styleFamily != null) {
				if (styleFamily.equals("paragraph")) {
					writer.write("p {");
				} else if (styleFamily.equals("table")) {
					writer.write("table {");
				} else if (styleFamily.equals("table-row")) {
					writer.write("tr {");
				} else {
					writer.write("." + styleFamily + " {");
				}
				writeCssValueByStyleStyleElement(writer, styleElem);
				writer.write("}\n");
			}
		}
	}

	private void writeCssByStyleStyleElement(PrintWriter writer, ODFDocument doc) {
		Element root = doc.getDocumentElement();
		NodeList styleStyleList = xpathService.evalForNodeList(EXP2, root);
		for (int i = 0; i < styleStyleList.getLength(); i++) {
			StyleElement styleElem = (StyleElement) styleStyleList.item(i);
			String styleName = styleElem.getName();
			styleName = styleName.replaceAll("\\.", "\\\\\\.");
			writer.write("." + styleName + " {");
			writeCssValueByStyleStyleElement(writer, styleElem);
			writer.write("}\n");
		}
	}

	private void writeCssByListStyleElement(PrintWriter writer, ODFDocument doc) {
		Element root = doc.getDocumentElement();
		NodeList listStyleList = xpathService.evalForNodeList(EXP3, root);
		for (int i = 0; i < listStyleList.getLength(); i++) {
			ListStyleElement styleElem = (ListStyleElement) listStyleList
					.item(i);
			String styleName = styleElem.getName();
			styleName = styleName.replaceAll("\\.", "\\\\\\.");
			NodeList nl = styleElem.getElementsByTagNameNS(
					TextConstants.TEXT_NAMESPACE_URI,
					TextConstants.ELEMENT_LIST_LEVEL_STYLE_NUMBER);
			for (int j = 0; j < nl.getLength(); j++) {
				ListLevelStyleNumberElement styleNumElem = (ListLevelStyleNumberElement) nl
						.item(j);
				writer.write("." + styleName + styleNumElem.getAttrLevel()
						+ " {");
				String format = styleNumElem.getAttrNumFormat();
				if (format.equals("1")) {
					writer.write("list-style-type:decimal;");
				} else if (format.equals("i")) {
					writer.write("list-style-type:lower-roman;");
				} else if (format.equals("I")) {
					writer.write("list-style-type:upper-roman;");
				} else if (format.equals("a")) {
					writer.write("list-style-type:lower-alpha;");
				} else if (format.equals("A")) {
					writer.write("list-style-type:upper-alpha;");
				}
				writer.write("}\n");
			}
		}
	}

	private void writeCssFile(String fileName) {
		File file = new File(fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, HTML_ENCODING);
			PrintWriter pw = new PrintWriter(osw);

			ODFDocument styleDoc = ((ODFDocument) curElem.getOwnerDocument())
					.getStyleDocument();
			// write default css style by using styles.xml
			writeCssByStyleDefaultStyleElement(pw, styleDoc);

			// write css style by using styles.xml
			writeCssByStyleStyleElement(pw, styleDoc);

			// write css style by using content.xml
			ODFDocument contentDoc = (ODFDocument) curElem.getOwnerDocument();
			writeCssByStyleStyleElement(pw, (ODFDocument) curElem
					.getOwnerDocument());

			// write additional css style for list structure
			writeCssByListStyleElement(pw, contentDoc);

			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void convertDocument(String fileName, boolean enableStyle) {
		File file = new File(fileName);
		String dirName = file.getParent();
		File dir = new File(dirName);
		if (!dir.exists())
			dir.mkdirs();

		if (enableStyle) {
			String cssFileName = dirName + System.getProperty("file.separator")
					+ ODF_CSS_FILE;
			writeCssFile(cssFileName);
		}

		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, HTML_ENCODING);
			PrintWriter pw = new PrintWriter(osw);
			convertDocument(pw, dir, enableStyle);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
