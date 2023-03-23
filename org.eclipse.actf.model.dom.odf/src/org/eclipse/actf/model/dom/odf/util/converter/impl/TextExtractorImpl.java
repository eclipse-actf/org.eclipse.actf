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
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.eclipse.actf.model.dom.odf.ODFParser;
import org.eclipse.actf.model.dom.odf.base.DrawingObjectBaseElement;
import org.eclipse.actf.model.dom.odf.base.EmbedDrawingObjectElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.content.IEditable;
import org.eclipse.actf.model.dom.odf.dr3d.SceneElement;
import org.eclipse.actf.model.dom.odf.draw.CaptionElement;
import org.eclipse.actf.model.dom.odf.draw.CircleElement;
import org.eclipse.actf.model.dom.odf.draw.ConnectorElement;
import org.eclipse.actf.model.dom.odf.draw.ControlElement;
import org.eclipse.actf.model.dom.odf.draw.CustomShapeElement;
import org.eclipse.actf.model.dom.odf.draw.EllipseElement;
import org.eclipse.actf.model.dom.odf.draw.FrameElement;
import org.eclipse.actf.model.dom.odf.draw.GElement;
import org.eclipse.actf.model.dom.odf.draw.ImageElement;
import org.eclipse.actf.model.dom.odf.draw.ImageMapAreaElement;
import org.eclipse.actf.model.dom.odf.draw.ImageMapElement;
import org.eclipse.actf.model.dom.odf.draw.LineElement;
import org.eclipse.actf.model.dom.odf.draw.ObjectElement;
import org.eclipse.actf.model.dom.odf.draw.PageThumbnailElement;
import org.eclipse.actf.model.dom.odf.draw.PolygonElement;
import org.eclipse.actf.model.dom.odf.draw.PolylineElement;
import org.eclipse.actf.model.dom.odf.draw.RectElement;
import org.eclipse.actf.model.dom.odf.draw.RegularPolygonElement;
import org.eclipse.actf.model.dom.odf.draw.TextBoxElement;
import org.eclipse.actf.model.dom.odf.form.FixedTextElement;
import org.eclipse.actf.model.dom.odf.form.FormControlElement;
import org.eclipse.actf.model.dom.odf.table.TableCellElement;
import org.eclipse.actf.model.dom.odf.text.PElement;
import org.eclipse.actf.model.dom.odf.text.PageNumberElement;
import org.eclipse.actf.model.dom.odf.text.SElement;
import org.eclipse.actf.model.dom.odf.text.TabElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.eclipse.actf.model.dom.odf.text.TrackedChangesElement;
import org.eclipse.actf.model.dom.odf.util.converter.ODFConverterUtils;
import org.eclipse.actf.model.dom.odf.util.converter.TextExtractor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

@SuppressWarnings("nls")
public class TextExtractorImpl implements TextExtractor {
	public static final String IMAGE_NO_ALT = "[image]";

	public static final String IMAGEMAP_NO_ALT = "[imagemap]";

	public static final String OBJECT_NO_ALT = "[object]";

	public static final String OBJECT_TABLE = "[table]";

	public static final String TEXTBOX_NO_ALT = "[text box]";

	public static final String CAPTION_NO_ALT = "[caption]";

	public static final String CIRCLE_NO_ALT = "[circle]";

	public static final String CONNECTOR_NO_ALT = "[connector]";

	public static final String CUSTOMSHAPE_NO_ALT = "[custom shape]";

	public static final String ELLIPSE_NO_ALT = "[ellipse]";

	public static final String FRAME_NO_ALT = "[frame]";

	public static final String G_NO_ALT = "[group]";

	public static final String IMAGEMAPAREA_NO_ALT = "[imagemap area]";

	public static final String LINE_NO_ALT = "[line]";

	public static final String POLYGON_NO_ALT = "[polygon]";

	public static final String POLYLINE_NO_ALT = "[polygon line]";

	public static final String RECT_NO_ALT = "[rectangle]";

	public static final String REGULARPOLYGON_NO_ALT = "[polygon]";

	public static final String SCENE_NO_ALT = "[3D shape]";

	public static final String UNKNOWNSHAPE_NO_ALT = "[graphic shape]";

	private TextExtractor converter;

	private double odfVersion = -1.0;

	private ODFElement curElem = null;

	public void setDocument(ODFDocument document, TextExtractor converter) {
		this.curElem = (ODFElement) document.getDocumentElement();
		this.converter = converter;
	}

	// for Notes8, Notes8 save document by ODF 1.1 schema,
	// but odf:version is set as 1.0
	// if specify odf:version by this function,
	// do not use odf:version defined in each ODF file
	public void setOdfVersion(double odfVersion) {
		this.odfVersion = odfVersion;
	}

	private boolean writeControlElementContent(Writer writer,
			ControlElement elem) {
		FormControlElement form = elem.getFormControlElement();
		if (form != null) {
			try {
				if (!(form instanceof FixedTextElement)) {
					writer.write("[form control]");
				}

				String formLabel = form.getAttrFormLabel();
				if (formLabel != null) {
					if (!(form instanceof FixedTextElement)) {
						writer.write(" ");
					}
					writer.write(formLabel);
				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean writeFrameElementContent(Writer writer, File dir,
			FrameElement elem, boolean enableStyle) {
		boolean addedTextContent = false;
		Iterator<ODFElement> iter = elem.getChildIterator();
		if (iter.hasNext()) {
			ODFElement firstContent = iter.next();
			if (firstContent != null) {
				addedTextContent |= converter.extractContent(writer, dir,
						firstContent, enableStyle);
				if (iter.hasNext()) {
					// if this frame has image map
					ODFElement secondContent = iter.next();
					if ((secondContent != null)
							&& (secondContent instanceof ImageMapElement)) {
						addedTextContent |= converter.extractContent(writer,
								dir, secondContent, enableStyle);
					}
				}
			}
		}
		return addedTextContent;
	}

	private boolean writeImageMapElementContent(Writer writer, File dir,
			ImageMapElement elem, boolean enableStyle) {
		boolean addedTextContent = false;
		NodeList nl = elem.getAreaElements();
		for (int i = 0; i < nl.getLength(); i++) {
			addedTextContent |= converter.extractContent(writer, dir,
					(ImageMapAreaElement) nl.item(i), enableStyle);
		}
		return addedTextContent;
	}

	private boolean writeTextBoxElementContent(Writer writer, File dir,
			TextBoxElement elem, boolean enableStyle) {
		boolean addedTextContent = false;
		NodeList children = elem.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Text) {
				String str = ((Text) child).getData().trim();
				if (str.length() != 0) {
					str = ODFConverterUtils.convertXMLCharacter(str);
					try {
						writer.write(str);
					} catch (IOException e) {
						e.printStackTrace();
					}
					addedTextContent = true;
				}
			} else if (child instanceof ODFElement) {
				addedTextContent |= converter.extractContent(writer, dir,
						(ODFElement) child, enableStyle);
			}
		}
		return addedTextContent;
	}

	private boolean writeTabElementContent(Writer writer, TabElement elem) {
		try {
			writer.write("\t");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private boolean writeSElementContent(Writer writer, SElement elem) {
		if (elem.hasAttributeNS(TextConstants.TEXT_NAMESPACE_URI,
				TextConstants.ATTR_C)) {
			int c = (elem).getAttrTextC();
			if (c > 0) {
				String value = "";
				for (int i = 0; i < c; i++)
					value += " ";
				try {
					writer.write(value);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				writer.write(" ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private boolean writeCustomShapeElementContent(Writer writer, File dir,
			CustomShapeElement elem, boolean enableStyle) {
		boolean addedTextContent = false;
		NodeList pList = elem.getElementsByTagNameNS(
				TextConstants.TEXT_NAMESPACE_URI, TextConstants.ELEMENT_P);
		if ((pList != null) && (pList.getLength() != 0)) { // if this custom
			// shape is caption
			for (int i = 0; i < pList.getLength(); i++) {
				Node pElem = pList.item(i);
				String content = pElem.getTextContent();
				if ((content != null) && (content.length() > 0)) {
					addedTextContent |= converter.extractContent(writer, dir,
							(ODFElement) pList.item(i), enableStyle);
				}
			}
		}

		if (!addedTextContent) {
			IEditable shortDesc = null;
			if (odfVersion != -1.0) {
				shortDesc = (IEditable) ((DrawingObjectBaseElement) elem)
						.getShortDescElement(odfVersion);
			} else {
				shortDesc = (IEditable) ((DrawingObjectBaseElement) elem)
						.getShortDescElement();
			}

			if (shortDesc == null) {
				ODFDocument doc = (ODFDocument) elem.getOwnerDocument();
				if ((doc.getODFVersion() > 1.0)
						|| ((odfVersion != -1.0) && (odfVersion > 1.0))) {
					try {
						writer.write(CUSTOMSHAPE_NO_ALT);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				addedTextContent = true;
			} else {
				String str = (String) shortDesc.getValue();
				if (str != null) {
					try {
						writer.write(str);
					} catch (IOException e) {
						e.printStackTrace();
					}
					addedTextContent = true;
				}
			}
		}
		return addedTextContent;
	}

	private boolean writeDrawingObjectBaseElementContent(Writer writer,
			File dir, DrawingObjectBaseElement elem, boolean enableStyle) {
		boolean addedTextContent = false;
		if (elem instanceof ImageElement)
			return false;
		if (elem instanceof PageThumbnailElement)
			return false;

		NodeList pList = elem.getElementsByTagNameNS(
				TextConstants.TEXT_NAMESPACE_URI, TextConstants.ELEMENT_P);
		if ((pList != null) && (pList.getLength() != 0)) { // if this custom
			// shape is caption
			for (int i = 0; i < pList.getLength(); i++) {
				Node pElem = pList.item(i);
				String content = pElem.getTextContent();
				if ((content != null) && (content.length() > 0)) {
					addedTextContent |= converter.extractContent(writer, dir,
							(ODFElement) pList.item(i), enableStyle);
				}
			}
		}

		if (!addedTextContent) {
			IEditable shortDesc = null;
			if (odfVersion != -1.0) {
				shortDesc = (IEditable) (elem).getShortDescElement(odfVersion);
			} else {
				shortDesc = (IEditable) (elem).getShortDescElement();
			}

			if (shortDesc == null) {
				ODFDocument doc = (ODFDocument) elem.getOwnerDocument();
				if ((elem instanceof EmbedDrawingObjectElement)
						|| ((doc.getODFVersion() > 1.0) || ((odfVersion != -1.0) && (odfVersion > 1.0)))) {
					try {
						if (elem instanceof ImageElement)
							writer.write(IMAGE_NO_ALT);
						else if (elem instanceof ImageMapElement)
							writer.write(IMAGEMAP_NO_ALT);
						else if (elem instanceof ObjectElement) {
							if (((ObjectElement) elem).isPresentationTable())
								writer.write(OBJECT_TABLE);
							else
								writer.write(OBJECT_NO_ALT);
						} else if (elem instanceof CaptionElement)
							writer.write(CAPTION_NO_ALT);
						else if (elem instanceof TextBoxElement)
							writer.write(TEXTBOX_NO_ALT);
						else if (elem instanceof CircleElement)
							writer.write(CIRCLE_NO_ALT);
						else if (elem instanceof ConnectorElement)
							writer.write(CONNECTOR_NO_ALT);
						else if (elem instanceof CustomShapeElement)
							writer.write(CUSTOMSHAPE_NO_ALT);
						else if (elem instanceof EllipseElement)
							writer.write(ELLIPSE_NO_ALT);
						else if (elem instanceof FrameElement)
							writer.write(FRAME_NO_ALT);
						else if (elem instanceof GElement)
							writer.write(G_NO_ALT);
						else if (elem instanceof ImageMapAreaElement)
							writer.write(IMAGEMAPAREA_NO_ALT);
						else if (elem instanceof LineElement)
							writer.write(LINE_NO_ALT);
						else if (elem instanceof PolygonElement)
							writer.write(POLYGON_NO_ALT);
						else if (elem instanceof PolylineElement)
							writer.write(POLYLINE_NO_ALT);
						else if (elem instanceof RectElement)
							writer.write(RECT_NO_ALT);
						else if (elem instanceof RegularPolygonElement)
							writer.write(REGULARPOLYGON_NO_ALT);
						else if (elem instanceof SceneElement)
							writer.write(SCENE_NO_ALT);
						else
							writer.write(UNKNOWNSHAPE_NO_ALT);

						writer.write(System.getProperty("line.separator"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					addedTextContent = true;
				}
			} else {
				String str = (String) shortDesc.getValue();
				if (str != null) {
					try {
						writer.write(str);
					} catch (IOException e) {
						e.printStackTrace();
					}
					addedTextContent = true;
				}
			}
		}
		return addedTextContent;
	}

	private boolean writeTableCellElementContent(Writer writer, File dir,
			TableCellElement elem, boolean enableStyle) {
		boolean addedToCell = writeODFElementContent(writer, dir, elem,
				enableStyle);
		if (!addedToCell) {
			try {
				writer.write("&nbsp;");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private boolean writeODFElementContent(Writer writer, File dir,
			ODFElement elem, boolean enableStyle) {
		boolean addedTextContent = false;
		NodeList children = elem.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Text) {
				String str = ((Text) child).getData().trim();
				if (str.length() != 0) {
					str = ODFConverterUtils.convertXMLCharacter(str);
					try {
						writer.write(str);
					} catch (IOException e) {
						e.printStackTrace();
					}
					addedTextContent = true;
				}
			} else if (child instanceof ODFElement) {
				addedTextContent |= converter.extractContent(writer, dir,
						(ODFElement) child, enableStyle);
			}
		}
		return addedTextContent;
	}

	public void extractEmbedFile(ODFElement elem, File dir, String href) {
		if (dir == null)
			return;

		String outputFileName = dir.getAbsolutePath()
				+ System.getProperty("file.separator") + href;
		File outputFile = new File(outputFileName);

		String outputDirName = outputFile.getParent();
		File outputDir = new File(outputDirName);
		if (!outputDir.exists())
			outputDir.mkdirs();

		ODFParser parser = new ODFParser();
		Document doc = curElem.getOwnerDocument();
		if (doc instanceof ODFDocument) {
			parser.copyFile(((ODFDocument) doc).getURL(), href, outputFileName);
		}
	}

	public boolean extractContent(Writer writer, File dir, ODFElement elem,
			boolean enableStyle) {
		// write odf content
		boolean addedTextContent = false;
		if (elem instanceof TabElement) {
			addedTextContent |= writeTabElementContent(writer,
					(TabElement) elem);
		} else if (elem instanceof SElement) {
			addedTextContent |= writeSElementContent(writer, (SElement) elem);
		} else if (elem instanceof TableCellElement) {
			addedTextContent |= writeTableCellElementContent(writer, dir,
					(TableCellElement) elem, enableStyle);
		} else if ((elem instanceof TrackedChangesElement)
				|| (elem instanceof PageThumbnailElement)
				|| (elem instanceof ImageElement)
				|| (elem instanceof PageNumberElement)) {
			// nothing to write
		} else if (elem instanceof DrawingObjectBaseElement) {
			if (elem instanceof ControlElement) {
				addedTextContent |= writeControlElementContent(writer,
						(ControlElement) elem);
			} else if (elem instanceof FrameElement) {
				addedTextContent |= writeFrameElementContent(writer, dir,
						(FrameElement) elem, enableStyle);
			} else if (elem instanceof ImageMapElement) {
				addedTextContent |= writeImageMapElementContent(writer, dir,
						(ImageMapElement) elem, enableStyle);
			} else if (elem instanceof TextBoxElement) {
				addedTextContent |= writeTextBoxElementContent(writer, dir,
						(TextBoxElement) elem, enableStyle);
			} else if (elem instanceof CustomShapeElement) {
				addedTextContent |= writeCustomShapeElementContent(writer, dir,
						(CustomShapeElement) elem, enableStyle);
			} else if (elem instanceof GElement) {
				addedTextContent |= writeODFElementContent(writer, dir, elem,
						enableStyle);
			} else {
				addedTextContent |= writeDrawingObjectBaseElementContent(
						writer, dir, (DrawingObjectBaseElement) elem,
						enableStyle);
			}
		} else if (elem instanceof PElement) {
			addedTextContent |= writeODFElementContent(writer, dir, elem,
					enableStyle);
			if (addedTextContent) {
				try {
					writer.write(System.getProperty("line.separator"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			addedTextContent |= writeODFElementContent(writer, dir, elem,
					enableStyle);
		}

		return addedTextContent;
	}
}
