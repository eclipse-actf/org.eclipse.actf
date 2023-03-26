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
package org.eclipse.actf.validation.odf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.model.dom.odf.ODFConstants.ContentType;
import org.eclipse.actf.model.dom.odf.base.ContentBaseElement;
import org.eclipse.actf.model.dom.odf.base.DrawingObjectElement;
import org.eclipse.actf.model.dom.odf.base.EmbedDrawingObjectElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.base.ODFElement;
import org.eclipse.actf.model.dom.odf.dr3d.Dr3dConstants;
import org.eclipse.actf.model.dom.odf.dr3d.SceneElement;
import org.eclipse.actf.model.dom.odf.draw.CaptionElement;
import org.eclipse.actf.model.dom.odf.draw.CircleElement;
import org.eclipse.actf.model.dom.odf.draw.ConnectorElement;
import org.eclipse.actf.model.dom.odf.draw.ControlElement;
import org.eclipse.actf.model.dom.odf.draw.CustomShapeElement;
import org.eclipse.actf.model.dom.odf.draw.DrawConstants;
import org.eclipse.actf.model.dom.odf.draw.EllipseElement;
import org.eclipse.actf.model.dom.odf.draw.FrameElement;
import org.eclipse.actf.model.dom.odf.draw.GElement;
import org.eclipse.actf.model.dom.odf.draw.ImageElement;
import org.eclipse.actf.model.dom.odf.draw.ImageMapAreaElement;
import org.eclipse.actf.model.dom.odf.draw.ImageMapElement;
import org.eclipse.actf.model.dom.odf.draw.LineElement;
import org.eclipse.actf.model.dom.odf.draw.MeasureElement;
import org.eclipse.actf.model.dom.odf.draw.ObjectElement;
import org.eclipse.actf.model.dom.odf.draw.PageElement;
import org.eclipse.actf.model.dom.odf.draw.PageThumbnailElement;
import org.eclipse.actf.model.dom.odf.draw.PathElement;
import org.eclipse.actf.model.dom.odf.draw.PolygonElement;
import org.eclipse.actf.model.dom.odf.draw.PolylineElement;
import org.eclipse.actf.model.dom.odf.draw.RectElement;
import org.eclipse.actf.model.dom.odf.draw.RegularPolygonElement;
import org.eclipse.actf.model.dom.odf.draw.TextBoxElement;
import org.eclipse.actf.model.dom.odf.form.FormConstants;
import org.eclipse.actf.model.dom.odf.form.FormControlElement;
import org.eclipse.actf.model.dom.odf.office.BodyElement;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.office.DrawingElement;
import org.eclipse.actf.model.dom.odf.office.OfficeConstants;
import org.eclipse.actf.model.dom.odf.office.PresentationElement;
import org.eclipse.actf.model.dom.odf.office.SpreadSheetElement;
import org.eclipse.actf.model.dom.odf.office.TextElement;
import org.eclipse.actf.model.dom.odf.svg.SVGConstants;
import org.eclipse.actf.model.dom.odf.table.TableConstants;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.text.AElement;
import org.eclipse.actf.model.dom.odf.text.TextConstants;
import org.eclipse.actf.model.dom.odf.text.TrackedChangesElement;
import org.eclipse.actf.util.xpath.XPathService;
import org.eclipse.actf.util.xpath.XPathServiceFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OdfCheckerUtil {
	private static OdfCheckerUtil instance = null;

	private double odfVersion = -1.0;

	private XPathService xpathService = XPathServiceFactory.newService();

	@SuppressWarnings("nls")
	private Object exp1 = xpathService.compile(".//*[namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_FRAME + "']" + "/*[1][(namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_OBJECT + "') or (namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_OBJECT_OLE + "')]");

	@SuppressWarnings("nls")
	private Object exp2 = xpathService.compile(".//*[namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_FRAME + "']" + "/*[1][(namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_OBJECT + "') or (namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_OBJECT_OLE + "')]");

	@SuppressWarnings("nls")
	private Object exp3 = xpathService.compile(".//*[namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_FRAME + "']" + "/*[1][namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_IMAGE + "']");

	@SuppressWarnings("nls")
	private Object exp4 = xpathService.compile(".//*[namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_FRAME + "']" + "/*[1][namespace-uri()='"
			+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
			+ DrawConstants.ELEMENT_IMAGE + "']");

	@SuppressWarnings("nls")
	private Object exp5 = xpathService
			.compile("./descendant::*[(namespace-uri()='"
					+ TableConstants.TABLE_NAMESPACE_URI
					+ "' and local-name()='"
					+ TableConstants.ELEMENT_TABLE_CELL + "')]"
					+ "/*[(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_RECT + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_LINE
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_POLYLINE + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_POLYGON
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_REGULAR_POLYGON + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_PATH
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_CIRCLE + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_ELLIPSE
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_G + "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_PAGE_THUMBNAIL + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_MEASURE
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_CAPTION + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_CONNECTOR
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_CUSTOM_SHAPE + "') or"
					+ "(namespace-uri()='" + Dr3dConstants.DR3D_NAMESPACE_URI
					+ "' and local-name()='" + Dr3dConstants.ELEMENT_SCENE
					+ "')]" + "[parent::*[namespace-uri()!='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' or local-name()!='"
					+ DrawConstants.ELEMENT_G + "']]");

	@SuppressWarnings("nls")
	private Object exp6 = xpathService.compile(".//*[(namespace-uri()!='"
			+ TableConstants.TABLE_NAMESPACE_URI + "' or local-name()!='"
			+ TableConstants.ELEMENT_TABLE_CELL + "')"
			+ " and (namespace-uri()!='" + TableConstants.TABLE_NAMESPACE_URI
			+ "' or local-name()!='"
			+ TableConstants.ELEMENT_COVERED_TABLE_CELL + "')]"
			+ "/*[namespace-uri()='" + TableConstants.TABLE_NAMESPACE_URI
			+ "' and local-name()='" + TableConstants.ELEMENT_TABLE + "']");

	@SuppressWarnings("nls")
	private Object exp7 = xpathService.compile(".//*[(namespace-uri()!='"
			+ TableConstants.TABLE_NAMESPACE_URI + "' or local-name()!='"
			+ TableConstants.ELEMENT_TABLE_CELL + "')"
			+ " and (namespace-uri()!='" + TableConstants.TABLE_NAMESPACE_URI
			+ "' or local-name()!='"
			+ TableConstants.ELEMENT_COVERED_TABLE_CELL + "')]"
			+ "/*[namespace-uri()='" + TableConstants.TABLE_NAMESPACE_URI
			+ "' and local-name()='" + TableConstants.ELEMENT_TABLE + "']");


	@SuppressWarnings("nls")
	private Object exp8 = xpathService
			.compile("./descendant::*[(namespace-uri()='"
					+ TextConstants.TEXT_NAMESPACE_URI + "' and local-name()='"
					+ TextConstants.ELEMENT_P + "')]" + "/*[(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_RECT + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_LINE
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_POLYLINE + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_POLYGON
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_REGULAR_POLYGON + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_PATH
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_CIRCLE + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_ELLIPSE
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_G + "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_PAGE_THUMBNAIL + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_MEASURE
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_CAPTION + "') or"
					+ "(namespace-uri()='" + DrawConstants.DRAW_NAMESPACE_URI
					+ "' and local-name()='" + DrawConstants.ELEMENT_CONNECTOR
					+ "') or" + "(namespace-uri()='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' and local-name()='"
					+ DrawConstants.ELEMENT_CUSTOM_SHAPE + "') or"
					+ "(namespace-uri()='" + Dr3dConstants.DR3D_NAMESPACE_URI
					+ "' and local-name()='" + Dr3dConstants.ELEMENT_SCENE
					+ "')]" + "[parent::*[namespace-uri()!='"
					+ DrawConstants.DRAW_NAMESPACE_URI + "' or local-name()!='"
					+ DrawConstants.ELEMENT_G + "']]");

	private OdfCheckerUtil() {
	}

	public static OdfCheckerUtil getInstance() {
		if (instance == null) {
			instance = new OdfCheckerUtil();
		}
		return instance;
	}

	// for Notes8, Notes8 save document by ODF 1.1 schema,
	// but odf:version is set as 1.0
	// if specify odf:version by this function,
	// do not use odf:version defined in each ODF file
	public void setOdfVersion(double odfVersion) {
		this.odfVersion = odfVersion;
	}

	private double getOdfVersion(Document targetODF) {
		double odfVersion = -1.0;

		if (targetODF instanceof ODFDocument) {
			odfVersion = ((ODFDocument) targetODF).getODFVersion();
		}

		return odfVersion;
	}

	private boolean isGroupedShape(DrawingObjectElement obj) {
		Node parent = obj.getParentNode();
		while ((parent != null) && !(parent instanceof BodyElement)) {
			if (parent instanceof GElement) {
				return true;
			}
			parent = parent.getParentNode();
		}
		return false;
	}

	private boolean isTrackedChanges(ODFElement elem) {
		Node parent = elem.getParentNode();
		while ((parent != null) && !(parent instanceof BodyElement)) {
			if (parent instanceof TrackedChangesElement) {
				return true;
			}
			parent = parent.getParentNode();
		}
		return false;
	}

	private boolean checkImageShortDescError(ImageElement image) {
		FrameElement frame = image.getFrameElement();
		if (!isGroupedShape(frame)) {
			ODFElement shortDesc = null;
			if (this.odfVersion != -1.0) {
				shortDesc = image.getShortDescElement(this.odfVersion);
			} else {
				shortDesc = image.getShortDescElement();
			}

			if (shortDesc == null) {
				TextBoxElement captionElem = null;
				if (this.odfVersion != -1.0) {
					captionElem = frame
							.getBoundCaptionTextBoxElement(this.odfVersion);
				} else {
					captionElem = frame.getBoundCaptionTextBoxElement();
				}

				if (captionElem == null) {
					if (!isTrackedChanges(image))
						return false;
				}
			}
		}
		return true;
	}

	private List<ImageElement> getImageShortDescError(Element root) {
		List<ImageElement> errorElemList = new ArrayList<ImageElement>();

		NodeList nl = xpathService.evalForNodeList(exp4, root);
		for (int i = 0; i < nl.getLength(); i++) {
			ImageElement image = (ImageElement) nl.item(i);
			if (!checkImageShortDescError(image)) {
				errorElemList.add(image);
			}
		}

		return errorElemList;
	}

	public List<ImageElement> getImageShortDescError(Document targetODF) {
		BodyElement body = null;
		Element root = targetODF.getDocumentElement();
		if (root instanceof DocumentContentElement) {
			DocumentContentElement docContent = (DocumentContentElement) root;
			body = docContent.getBodyElement();
		}
		if (body == null)
			return new ArrayList<ImageElement>();

		return getImageShortDescError(body);
	}

	public List<ImageElement> getImageShortDescError(Document targetODF,
			long pageIndex) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			return getImageShortDescError(((DrawingElement) contentBase)
					.getPage(pageIndex));
		}
		return new ArrayList<ImageElement>();
	}

	private boolean checkImageLongDescError(ImageElement image) {
		FrameElement frame = image.getFrameElement();
		if (!isGroupedShape(frame)) {
			ODFElement longDesc = null;
			if (this.odfVersion != -1.0) {
				longDesc = image.getLongDescElement(this.odfVersion);
			} else {
				longDesc = image.getLongDescElement();
			}

			if (longDesc == null) {
				if (!isTrackedChanges(image))
					return false;
			}
		}
		return true;
	}

	private List<ImageElement> getImageLongDescError(Element root) {
		List<ImageElement> errorElemList = new ArrayList<ImageElement>();

		NodeList nl = xpathService.evalForNodeList(exp3, root);
		for (int i = 0; i < nl.getLength(); i++) {
			ImageElement image = (ImageElement) nl.item(i);
			if (!checkImageLongDescError(image)) {
				errorElemList.add(image);
			}
		}

		return errorElemList;
	}

	public List<ImageElement> getImageLongDescError(Document targetODF) {
		if (this.odfVersion != -1.0) {
			if (this.odfVersion < 1.1)
				return new ArrayList<ImageElement>();
		} else {
			if (getOdfVersion(targetODF) < 1.1)
				return new ArrayList<ImageElement>();
		}

		BodyElement body = null;
		Element root = targetODF.getDocumentElement();
		if (root instanceof DocumentContentElement) {
			DocumentContentElement docContent = (DocumentContentElement) root;
			body = docContent.getBodyElement();
		}
		if (body == null)
			return new ArrayList<ImageElement>();
		;

		return getImageLongDescError(body);
	}

	public List<ImageElement> geImageLongDescError(Document targetODF,
			long pageIndex) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			return getImageLongDescError(((DrawingElement) contentBase)
					.getPage(pageIndex));
		}
		return new ArrayList<ImageElement>();
	}

	private boolean checkTableHeaderError(TableElement table) {
		NodeList rowHeaders = table.getTableHeaderRows();
		NodeList colHeaders = table.getTableHeaderColumns();
		if (((rowHeaders == null) || (rowHeaders.getLength() == 0))
				&& ((colHeaders == null) || (colHeaders.getLength() == 0))
				&& (table.getTableRowSize() > 1)
				&& (table.getTableColumnSize() > 1)) {
			if (!isTrackedChanges(table))
				return false;
		}
		return true;

	}

	private List<TableElement> getTableHeaderError(Element root) {
		List<TableElement> errorElemList = new ArrayList<TableElement>();

		NodeList nl = xpathService.evalForNodeList(exp7, root);
		for (int i = 0; i < nl.getLength(); i++) {
			TableElement table = (TableElement) nl.item(i);
			if (!checkTableHeaderError(table)) {
				errorElemList.add(table);
			}
		}

		return errorElemList;
	}

	public List<TableElement> getTableHeaderError(Document targetODF) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentType type = content.getBodyElement().getContent()
				.getContentType();
		if (type != ContentType.WRITE)
			return new ArrayList<TableElement>();

		BodyElement body = content.getBodyElement();
		return getTableHeaderError(body);
	}

	private boolean checkTableCaptionError(TableElement table) {
		if (table.getTextSequenceElement() == null) {
			if (!isTrackedChanges(table))
				return false;
		}
		return true;
	}

	private List<TableElement> getTableCaptionError(Element root) {
		List<TableElement> errorElemList = new ArrayList<TableElement>();

		NodeList nl = xpathService.evalForNodeList(exp6, root);
		for (int i = 0; i < nl.getLength(); i++) {
			TableElement table = (TableElement) nl.item(i);
			if (!checkTableCaptionError(table)) {
				errorElemList.add(table);
			}
		}

		return errorElemList;
	}

	public List<TableElement> getTableCaptionError(Document targetODF) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentType type = content.getBodyElement().getContent()
				.getContentType();
		if (type != ContentType.WRITE)
			return new ArrayList<TableElement>();

		BodyElement body = content.getBodyElement();
		return getTableCaptionError(body);
	}

	private boolean checkImageMapShortDescError(ImageMapAreaElement area) {
		ODFElement shortDesc = null;
		if (this.odfVersion != -1.0) {
			shortDesc = area.getShortDescElement(this.odfVersion);
		} else {
			shortDesc = area.getShortDescElement();
		}

		if (shortDesc == null) {
			if (!isTrackedChanges(area))
				return false;
		}
		return true;
	}

	private List<ImageMapAreaElement> getImageMapShortDescError(Element root) {
		List<ImageMapAreaElement> errorElemList = new ArrayList<ImageMapAreaElement>();

		NodeList nl = root.getElementsByTagNameNS(
				DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ELEMENT_IMAGE_MAP);
		for (int i = 0; i < nl.getLength(); i++) {
			ImageMapElement imagemap = (ImageMapElement) nl.item(i);
			FrameElement frame = imagemap.getFrameElement();
			if (!isGroupedShape(frame)) {
				NodeList areas = imagemap.getAreaElements();
				for (int j = 0; j < areas.getLength(); j++) {
					ImageMapAreaElement area = (ImageMapAreaElement) areas
							.item(j);
					if (!checkImageMapShortDescError(area)) {
						errorElemList.add(area);
					}
				}
			}
		}

		return errorElemList;
	}

	public List<ImageMapAreaElement> getImageMapShortDescError(
			Document targetODF) {
		return getImageMapShortDescError(targetODF.getDocumentElement());
	}

	public List<ImageMapAreaElement> getImageMapShortDescError(
			Document targetODF, long pageIndex) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			return getImageMapShortDescError(((DrawingElement) contentBase)
					.getPage(pageIndex));
		}
		return new ArrayList<ImageMapAreaElement>();
	}

	private boolean checkImageMapLongDescError(ImageMapAreaElement area) {
		ODFElement longDesc = null;
		if (this.odfVersion != -1.0) {
			longDesc = area.getLongDescElement(this.odfVersion);
		} else {
			longDesc = area.getLongDescElement();
		}

		if (longDesc == null) {
			if (!isTrackedChanges(area))
				return false;
		}
		return true;
	}

	private List<ImageMapAreaElement> getImageMapLongDescError(Element root) {
		List<ImageMapAreaElement> errorElemList = new ArrayList<ImageMapAreaElement>();

		NodeList nl = root.getElementsByTagNameNS(
				DrawConstants.DRAW_NAMESPACE_URI,
				DrawConstants.ELEMENT_IMAGE_MAP);
		for (int i = 0; i < nl.getLength(); i++) {
			ImageMapElement imagemap = (ImageMapElement) nl.item(i);
			FrameElement frame = imagemap.getFrameElement();
			if (!isGroupedShape(frame)) {
				NodeList areas = imagemap.getAreaElements();
				for (int j = 0; j < areas.getLength(); j++) {
					ImageMapAreaElement area = (ImageMapAreaElement) areas
							.item(j);
					if (!checkImageMapLongDescError(area)) {
						errorElemList.add(area);
					}
				}
			}
		}

		return errorElemList;
	}

	public List<ImageMapAreaElement> getImageMapLongDescError(Document targetODF) {
		if (this.odfVersion != -1.0) {
			if (this.odfVersion < 1.1)
				return new ArrayList<ImageMapAreaElement>();
		} else {
			if (getOdfVersion(targetODF) < 1.1)
				return new ArrayList<ImageMapAreaElement>();
		}

		return getImageMapLongDescError(targetODF.getDocumentElement());
	}

	public List<ImageMapAreaElement> getImageMapLongDescError(
			Document targetODF, long pageIndex) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			return getImageMapLongDescError(((DrawingElement) contentBase)
					.getPage(pageIndex));
		}
		return new ArrayList<ImageMapAreaElement>();
	}

	/*
	 * check if shape is draw:text-box or contain text:p as child
	 */
	private boolean isShapeContainText(DrawingObjectElement drawShape) {
		if (drawShape instanceof TextBoxElement)
			return true;
		if (drawShape instanceof GElement)
			return false;

		NodeList nl = drawShape.getElementsByTagNameNS(
				TextConstants.TEXT_NAMESPACE_URI, TextConstants.ELEMENT_P);
		if ((null != nl) && (nl.getLength() > 0)) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				String text = node.getTextContent();
				if ((null != text) && (text.length() > 0))
					return true;
			}
		}

		return false;
	}

	private NodeList getWriteDrawingObject(TextElement textElem) {
		return xpathService.evalForNodeList(exp8, textElem);
	}

	private NodeList getSpreadsheetDrawingObject(SpreadSheetElement spreadElem) {
		return xpathService.evalForNodeList(exp5, spreadElem);
	}

	private boolean checkDrawingObjectShortDesc(DrawingObjectElement object) {
		if (!(object instanceof PageThumbnailElement)
				&& !(object instanceof FrameElement)
				&& (!isShapeContainText(object))) {
			ODFElement shortDesc = null;
			if (this.odfVersion != -1.0) {
				shortDesc = object.getShortDescElement(this.odfVersion);
			} else {
				shortDesc = object.getShortDescElement();
			}
			if (shortDesc != null)
				return true;

			TextBoxElement captionElem = null;
			if (this.odfVersion != -1.0) {
				captionElem = object
						.getBoundCaptionTextBoxElement(this.odfVersion);
			} else {
				captionElem = object.getBoundCaptionTextBoxElement();
			}
			if (captionElem == null) {
				if (!isTrackedChanges(object))
					return false;
			}
		}
		return true;
	}

	private List<DrawingObjectElement> getDrawingObjectShortDescError(
			Document targetODF, boolean checkGroupObject) {
		List<DrawingObjectElement> errorElemList = new ArrayList<DrawingObjectElement>();

		if (this.odfVersion != -1.0) {
			if (this.odfVersion < 1.1)
				return errorElemList;
		} else {
			if (getOdfVersion(targetODF) < 1.1)
				return errorElemList;
		}

		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentType type = content.getBodyElement().getContent()
				.getContentType();

		if ((type == ContentType.PRESENTATION) || (type == ContentType.DRAW)) {
			PresentationElement presentation = (PresentationElement) content
					.getBodyElement().getContent();
			for (int i = 0; i < presentation.getPageSize(); i++) {
				PageElement page = presentation.getPage(i);
				NodeList objects = page.getDrawingObjectElements();
				for (int j = 0; j < objects.getLength(); j++) {
					DrawingObjectElement object = (DrawingObjectElement) objects
							.item(j);
					if (((checkGroupObject) && (object instanceof GElement))
							|| ((!checkGroupObject) && !(object instanceof GElement))) {
						if (!checkDrawingObjectShortDesc(object)) {
							errorElemList.add(object);
						}
					}
				}
			}
		} else if ((type == ContentType.WRITE)
				|| (type == ContentType.SPREADSHEET)) {
			NodeList objects = null;
			if (type == ContentType.WRITE) {
				TextElement text = (TextElement) content.getBodyElement()
						.getContent();
				objects = getWriteDrawingObject(text);
			}
			if (type == ContentType.SPREADSHEET) {
				SpreadSheetElement spread = (SpreadSheetElement) content
						.getBodyElement().getContent();
				objects = getSpreadsheetDrawingObject(spread);
			}

			if (objects != null) {
				for (int i = 0; i < objects.getLength(); i++) {
					if (objects.item(i) instanceof DrawingObjectElement) {
						DrawingObjectElement object = (DrawingObjectElement) objects
								.item(i);
						if (((checkGroupObject) && (object instanceof GElement))
								|| ((!checkGroupObject) && !(object instanceof GElement))) {
							if (!checkDrawingObjectShortDesc(object)) {
								errorElemList.add(object);
							}
						}
					}
				}
			}
		}

		return errorElemList;
	}

	public List<DrawingObjectElement> getNotGroupDrawingObjectShortDescError(
			Document targetODF) {
		return getDrawingObjectShortDescError(targetODF, false);
	}

	public List<DrawingObjectElement> getGroupObjectShortDescError(
			Document targetODF) {
		return getDrawingObjectShortDescError(targetODF, true);
	}

	private List<DrawingObjectElement> getDrawingObjectShortDescError(
			Document targetODF, long pageIndex, boolean checkGroupObject) {
		List<DrawingObjectElement> errorElemList = new ArrayList<DrawingObjectElement>();

		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			PageElement page = ((DrawingElement) contentBase)
					.getPage(pageIndex);
			NodeList objects = page.getDrawingObjectElements();
			for (int j = 0; j < objects.getLength(); j++) {
				DrawingObjectElement object = (DrawingObjectElement) objects
						.item(j);
				if (((checkGroupObject) && (object instanceof GElement))
						|| ((!checkGroupObject) && !(object instanceof GElement))) {
					if (!checkDrawingObjectShortDesc(object)) {
						errorElemList.add(object);
					}
				}
			}
		}

		return errorElemList;
	}

	public List<DrawingObjectElement> getNotGroupDrawingObjectShortDescError(
			Document targetODF, long pageIndex) {
		return getDrawingObjectShortDescError(targetODF, pageIndex, false);
	}

	public List<DrawingObjectElement> getGroupObjectShortDescError(
			Document targetODF, long pageIndex) {
		return getDrawingObjectShortDescError(targetODF, pageIndex, true);
	}

	private boolean checkDrawingObjectLongDesc(DrawingObjectElement object) {
		if (!(object instanceof PageThumbnailElement)
				&& !(object instanceof FrameElement)
				&& (!isShapeContainText(object))) {
			ODFElement longDesc = null;
			if (this.odfVersion != -1.0) {
				longDesc = object.getLongDescElement(this.odfVersion);
			} else {
				longDesc = object.getLongDescElement();
			}

			if (longDesc == null) {
				if (!isTrackedChanges(object))
					return false;
			}
		}
		return true;
	}

	public List<DrawingObjectElement> getDrawingObjectLongDescError(
			Document targetODF) {
		List<DrawingObjectElement> errorElemList = new ArrayList<DrawingObjectElement>();

		if (this.odfVersion != -1.0) {
			if (this.odfVersion < 1.1)
				return errorElemList;
		} else {
			if (getOdfVersion(targetODF) < 1.1)
				return errorElemList;
		}

		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentType type = content.getBodyElement().getContent()
				.getContentType();
		if ((type == ContentType.PRESENTATION) || (type == ContentType.DRAW)) {
			PresentationElement presentation = (PresentationElement) content
					.getBodyElement().getContent();
			for (int i = 0; i < presentation.getPageSize(); i++) {
				PageElement page = presentation.getPage(i);
				NodeList objects = page.getDrawingObjectElements();
				for (int j = 0; j < objects.getLength(); j++) {
					DrawingObjectElement object = (DrawingObjectElement) objects
							.item(j);
					if (!checkDrawingObjectLongDesc(object)) {
						errorElemList.add(object);
					}
				}
			}
		} else if ((type == ContentType.WRITE)
				|| (type == ContentType.SPREADSHEET)) {
			NodeList objects = null;
			if (type == ContentType.WRITE) {
				TextElement text = (TextElement) content.getBodyElement()
						.getContent();
				objects = getWriteDrawingObject(text);
			}
			if (type == ContentType.SPREADSHEET) {
				SpreadSheetElement spread = (SpreadSheetElement) content
						.getBodyElement().getContent();
				objects = getSpreadsheetDrawingObject(spread);
			}

			if (objects != null) {
				for (int i = 0; i < objects.getLength(); i++) {
					if (objects.item(i) instanceof DrawingObjectElement) {
						DrawingObjectElement object = (DrawingObjectElement) objects
								.item(i);
						if (!checkDrawingObjectLongDesc(object)) {
							errorElemList.add(object);
						}
					}
				}
			}
		}

		return errorElemList;
	}

	public List<DrawingObjectElement> getDrawingObjectLongDescError(
			Document targetODF, long pageIndex) {
		List<DrawingObjectElement> errorElemList = new ArrayList<DrawingObjectElement>();

		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			PageElement page = ((DrawingElement) contentBase)
					.getPage(pageIndex);
			NodeList objects = page.getDrawingObjectElements();
			for (int j = 0; j < objects.getLength(); j++) {
				DrawingObjectElement object = (DrawingObjectElement) objects
						.item(j);
				if (!checkDrawingObjectLongDesc(object)) {
					errorElemList.add(object);
				}
			}
		}

		return errorElemList;
	}

	private boolean checkLinkHintError(AElement a) {
		if (a.getAttrOfficeTitle() == null) {
			if (!isTrackedChanges(a))
				return false;
		}
		return true;
	}

	private List<AElement> getLinkHintError(Element root) {
		List<AElement> errorElemList = new ArrayList<AElement>();

		NodeList nl = root.getElementsByTagNameNS(
				TextConstants.TEXT_NAMESPACE_URI, TextConstants.ELEMENT_A);
		for (int i = 0; i < nl.getLength(); i++) {
			AElement a = (AElement) nl.item(i);
			if (!checkLinkHintError(a)) {
				errorElemList.add(a);
			}
		}

		return errorElemList;
	}

	/*
	 * check item for <text:a>
	 */
	public List<AElement> getLinkHintError(Document targetODF) {
		if (this.odfVersion != -1.0) {
			if (this.odfVersion < 1.1)
				new ArrayList<AElement>();
		} else {
			if (getOdfVersion(targetODF) < 1.1)
				new ArrayList<AElement>();
		}

		return getLinkHintError(targetODF.getDocumentElement());
	}

	public List<AElement> getLinkHintError(Document targetODF, long pageIndex) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			return getLinkHintError(((DrawingElement) contentBase)
					.getPage(pageIndex));
		}
		return new ArrayList<AElement>();
	}

	private boolean checkEmbedObjectShortDescError(
			EmbedDrawingObjectElement object) {
		if ((object instanceof ObjectElement)
				&& (((ObjectElement) object).isPresentationTable()))
			return true;

		FrameElement frame = object.getFrameElement();
		if (!isGroupedShape(frame)) {
			ODFElement shortDesc = null;
			if (this.odfVersion != -1.0) {
				shortDesc = object.getShortDescElement(this.odfVersion);
			} else {
				shortDesc = object.getShortDescElement();
			}

			if (shortDesc == null) {
				TextBoxElement captionElem = null;
				if (this.odfVersion != -1.0) {
					captionElem = frame
							.getBoundCaptionTextBoxElement(this.odfVersion);
				} else {
					captionElem = frame.getBoundCaptionTextBoxElement();
				}

				if (captionElem == null) {
					if (!isTrackedChanges(object))
						return false;
				}
			}
		}
		return true;
	}

	private List<EmbedDrawingObjectElement> getEmbedObjectShortDescError(
			Element root) {
		List<EmbedDrawingObjectElement> errorElemList = new ArrayList<EmbedDrawingObjectElement>();

		NodeList nl = xpathService.evalForNodeList(exp2, root);
		for (int i = 0; i < nl.getLength(); i++) {
			EmbedDrawingObjectElement object = (EmbedDrawingObjectElement) nl
					.item(i);
			if (!checkEmbedObjectShortDescError(object)) {
				errorElemList.add(object);
			}
		}

		return errorElemList;
	}

	public List<EmbedDrawingObjectElement> getEmbedObjectShortDescError(
			Document targetODF) {
		BodyElement body = null;
		Element root = targetODF.getDocumentElement();
		if (root instanceof DocumentContentElement) {
			DocumentContentElement docContent = (DocumentContentElement) root;
			body = docContent.getBodyElement();
		}
		if (body == null)
			return new ArrayList<EmbedDrawingObjectElement>();

		return getEmbedObjectShortDescError(body);
	}

	public List<EmbedDrawingObjectElement> getEmbedObjectShortDescError(
			Document targetODF, long pageIndex) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			return getEmbedObjectShortDescError(((DrawingElement) contentBase)
					.getPage(pageIndex));
		}
		return new ArrayList<EmbedDrawingObjectElement>();
	}

	private boolean checkEmbedObjectLongDescError(
			EmbedDrawingObjectElement object) {
		if ((object instanceof ObjectElement)
				&& (((ObjectElement) object).isPresentationTable()))
			return true;

		FrameElement frame = object.getFrameElement();
		if (!isGroupedShape(frame)) {
			ODFElement longDesc = null;
			if (this.odfVersion != -1.0) {
				longDesc = object.getLongDescElement(this.odfVersion);
			} else {
				longDesc = object.getLongDescElement();
			}

			if (longDesc == null) {
				if (!isTrackedChanges(object))
					return false;
			}
		}
		return true;
	}

	private List<EmbedDrawingObjectElement> getEmbedObjectLongDescError(
			Element root) {
		List<EmbedDrawingObjectElement> errorElemList = new ArrayList<EmbedDrawingObjectElement>();

		NodeList nl = xpathService.evalForNodeList(exp1, root);
		for (int i = 0; i < nl.getLength(); i++) {
			EmbedDrawingObjectElement object = (EmbedDrawingObjectElement) nl
					.item(i);
			if (!checkEmbedObjectLongDescError(object)) {
				errorElemList.add(object);
			}
		}

		return errorElemList;
	}

	public List<EmbedDrawingObjectElement> getEmbedObjectLongDescError(
			Document targetODF) {
		if (this.odfVersion != -1.0) {
			if (this.odfVersion < 1.1)
				return new ArrayList<EmbedDrawingObjectElement>();
		} else {
			if (getOdfVersion(targetODF) < 1.1)
				return new ArrayList<EmbedDrawingObjectElement>();
		}

		BodyElement body = null;
		Element root = targetODF.getDocumentElement();
		if (root instanceof DocumentContentElement) {
			DocumentContentElement docContent = (DocumentContentElement) root;
			body = docContent.getBodyElement();
		}
		if (body == null)
			return new ArrayList<EmbedDrawingObjectElement>();

		return getEmbedObjectLongDescError(body);
	}

	public List<EmbedDrawingObjectElement> getEmbedObjectLongDescError(
			Document targetODF, long pageIndex) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			return getEmbedObjectLongDescError(((DrawingElement) contentBase)
					.getPage(pageIndex));
		}
		return new ArrayList<EmbedDrawingObjectElement>();
	}

	private boolean checkFormLabelError(ControlElement control) {
		FormControlElement fcontrol = control.getFormControlElement();
		if ((fcontrol != null) && (fcontrol.getAttrFormLabel() == null)
				&& (control.getFormLabelFixedTextElement() == null)) {
			if (!isTrackedChanges(control))
				return false;
		}
		return true;
	}

	private List<ControlElement> getFormLabelError(Element root) {
		List<ControlElement> errorElemList = new ArrayList<ControlElement>();

		NodeList nl = root
				.getElementsByTagNameNS(DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ELEMENT_CONTROL);
		for (int i = 0; i < nl.getLength(); i++) {
			ControlElement control = (ControlElement) nl.item(i);
			if (!checkFormLabelError(control)) {
				errorElemList.add(control);
			}
		}

		return errorElemList;
	}

	public List<ControlElement> getFormLabelError(Document targetODF) {
		return getFormLabelError(targetODF.getDocumentElement());
	}

	public List<ControlElement> getFormLabelError(Document targetODF,
			long pageIndex) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			return getFormLabelError(((DrawingElement) contentBase)
					.getPage(pageIndex));
		}
		return new ArrayList<ControlElement>();
	}

	private boolean checkDrawNavOrderError(PageElement page) {
		if (page.getAttrDrawNavOrder() == null) {
			return false;
		}
		return true;
	}

	public List<PageElement> getDrawNavOrderError(Document targetODF) {
		List<PageElement> errorElemList = new ArrayList<PageElement>();

		if (this.odfVersion != -1.0) {
			if (this.odfVersion < 1.1)
				return errorElemList;
		} else {
			if (getOdfVersion(targetODF) < 1.1)
				return errorElemList;
		}

		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBaseElem = content.getBodyElement()
				.getContent();
		ContentType type = contentBaseElem.getContentType();
		if ((type != ContentType.PRESENTATION)
				|| (!(contentBaseElem instanceof PresentationElement))) {
			return errorElemList;
		}

		PresentationElement presenElem = (PresentationElement) contentBaseElem;
		for (int i = 0; i < presenElem.getPageSize(); i++) {
			PageElement page = presenElem.getPage(i);
			if (!checkDrawNavOrderError(page)) {
				errorElemList.add(page);
			}
		}

		return errorElemList;
	}

	private List<DocumentContentElement> getFormTabIndexError(Element root) {
		List<DocumentContentElement> errorElemList = new ArrayList<DocumentContentElement>();

		// get draw:control
		NodeList nl = root
				.getElementsByTagNameNS(DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ELEMENT_CONTROL);
		if ((nl == null) || (nl.getLength() == 0)) {
			return errorElemList;
		}

		// get form control
		List<FormControlElement> formControlList = new ArrayList<FormControlElement>();
		for (int i = 0; i < nl.getLength(); i++) {
			ControlElement control = (ControlElement) nl.item(i);
			FormControlElement fcontrol = control.getFormControlElement();
			if (fcontrol != null) {
				formControlList.add(fcontrol);
			}
		}
		if (formControlList.size() == 0) {
			return errorElemList;
		}

		// check at least one form control has tab index attribute
		boolean bHasTabIndex = false;
		for (int i = 0; i < formControlList.size(); i++) {
			FormControlElement fcontrol = formControlList.get(i);
			if (fcontrol.getAttrFormTabIndex() != null) {
				bHasTabIndex = true;
			}
		}

		if (!bHasTabIndex) {
			errorElemList.add((DocumentContentElement) root);
		}

		return errorElemList;
	}

	public List<DocumentContentElement> getFormTabIndexError(Document targetODF) {
		Element root = targetODF.getDocumentElement();
		if (!(root instanceof DocumentContentElement)) {
			return new ArrayList<DocumentContentElement>();
		}

		return getFormTabIndexError(root);
	}

	private boolean checkFormTabStopError(ControlElement control) {
		FormControlElement fcontrol = control.getFormControlElement();
		if ((fcontrol != null)
				&& (fcontrol.hasAttributeNS(FormConstants.FORM_NAMESPACE_URI,
						FormConstants.ATTR_TAB_STOP))
				&& (!fcontrol.getAttrFormTabStop())) {
			if (!isTrackedChanges(control))
				return false;
		}
		return true;
	}

	private List<ControlElement> getFormTabStopError(Element root) {
		List<ControlElement> errorElemList = new ArrayList<ControlElement>();

		NodeList nl = root
				.getElementsByTagNameNS(DrawConstants.DRAW_NAMESPACE_URI,
						DrawConstants.ELEMENT_CONTROL);
		for (int i = 0; i < nl.getLength(); i++) {
			ControlElement control = (ControlElement) nl.item(i);
			if (!checkFormTabStopError(control)) {
				errorElemList.add(control);
			}
		}

		return errorElemList;
	}

	public List<ControlElement> getFormTabStopError(Document targetODF) {
		return getFormTabStopError(targetODF.getDocumentElement());
	}

	public List<ControlElement> getFormTabStopError(Document targetODF,
			long pageIndex) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			return getFormTabStopError(((DrawingElement) contentBase)
					.getPage(pageIndex));
		}
		return new ArrayList<ControlElement>();
	}

	public boolean hasTooManyShape(Document targetODF, long pageIndex,
			int threshold) {
		DocumentContentElement content = (DocumentContentElement) targetODF
				.getDocumentElement();
		ContentBaseElement contentBase = content.getBodyElement().getContent();
		ContentType type = contentBase.getContentType();
		if (((type == ContentType.PRESENTATION) || (type == ContentType.DRAW))
				&& (contentBase instanceof DrawingElement)) {
			PageElement page = ((DrawingElement) contentBase)
					.getPage(pageIndex);

			int count = 0;
			NodeList nl = page.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node instanceof DrawingObjectElement) {
					count++;
				}
				if (count > threshold)
					return true;
			}
		}
		return false;
	}

	public List<DocumentContentElement> getODF10CompativilityError(
			Document targetODF) {
		List<DocumentContentElement> errorElemList = new ArrayList<DocumentContentElement>();
		Element root = targetODF.getDocumentElement();
		if (!(root instanceof DocumentContentElement)) {
			return errorElemList;
		}

		// check if ODF version is 1.0
		if (this.odfVersion != -1.0) {
			if (this.odfVersion != 1.0)
				return errorElemList;
		} else {
			if (getOdfVersion(targetODF) != 1.0)
				return errorElemList;
		}

		boolean hasInvalidElem = false;

		// check if there is svg:title
		NodeList nl = targetODF.getDocumentElement().getElementsByTagNameNS(
				SVGConstants.SVG_NAMESPACE_URI, SVGConstants.ELEMENT_TITLE);
		if ((nl != null) && (nl.getLength() > 0)) {
			hasInvalidElem = true;
		}

		// check is graphic elements have svg:desc
		nl = targetODF.getDocumentElement().getElementsByTagNameNS(
				SVGConstants.SVG_NAMESPACE_URI, SVGConstants.ELEMENT_DESC);
		if ((nl != null) && (nl.getLength() > 0)) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node descNode = nl.item(i);
				Node descParentNode = descNode.getParentNode();
				/*
				 * "draw:rect", "draw:line", "draw:polyline", "draw:polygon",
				 * "draw:regular-polygon", "draw:path", "draw:circle",
				 * "draw:ellipse", "draw:g", "draw:page-thumbnail",
				 * "draw:measure", "draw:caption", "draw:connector",
				 * "draw:control", "dr3d:scene", "draw:custom-shape"
				 */
				if ((descParentNode instanceof RectElement)
						|| (descParentNode instanceof LineElement)
						|| (descParentNode instanceof PolylineElement)
						|| (descParentNode instanceof PolygonElement)
						|| (descParentNode instanceof RegularPolygonElement)
						|| (descParentNode instanceof PathElement)
						|| (descParentNode instanceof CircleElement)
						|| (descParentNode instanceof EllipseElement)
						|| (descParentNode instanceof GElement)
						|| (descParentNode instanceof PageThumbnailElement)
						|| (descParentNode instanceof MeasureElement)
						|| (descParentNode instanceof CaptionElement)
						|| (descParentNode instanceof ConnectorElement)
						|| (descParentNode instanceof ControlElement)
						|| (descParentNode instanceof SceneElement)
						|| (descParentNode instanceof CustomShapeElement)) {
					hasInvalidElem = true;
				}
			}
		}

		// check is draw:frame elements have draw:caption-id attribute
		nl = targetODF.getDocumentElement().getElementsByTagNameNS(
				DrawConstants.DRAW_NAMESPACE_URI, DrawConstants.ELEMENT_FRAME);
		if ((nl != null) && (nl.getLength() > 0)) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node instanceof FrameElement) {
					FrameElement frameElem = (FrameElement) node;
					if (frameElem.hasAttributeNS(
							DrawConstants.DRAW_NAMESPACE_URI,
							DrawConstants.ATTR_CAPTION_ID)) {
						hasInvalidElem = true;
					}
				}
			}
		}

		// check if text:a elements have office:title attribute
		nl = targetODF.getDocumentElement().getElementsByTagNameNS(
				TextConstants.TEXT_NAMESPACE_URI, TextConstants.ELEMENT_A);
		if ((nl != null) && (nl.getLength() > 0)) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node instanceof AElement) {
					AElement aElem = (AElement) node;
					if (aElem.hasAttributeNS(
							OfficeConstants.OFFICE_NAMESPACE_URI,
							OfficeConstants.ATTR_TITLE)) {
						hasInvalidElem = true;
					}
				}
			}
		}

		// check is draw:page elements have draw:nav-order attribute
		nl = targetODF.getDocumentElement().getElementsByTagNameNS(
				DrawConstants.DRAW_NAMESPACE_URI, DrawConstants.ELEMENT_PAGE);
		if ((nl != null) && (nl.getLength() > 0)) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node instanceof PageElement) {
					PageElement pageElem = (PageElement) node;
					if (pageElem.hasAttributeNS(
							DrawConstants.DRAW_NAMESPACE_URI,
							DrawConstants.ATTR_NAV_ORDER)) {
						hasInvalidElem = true;
					}
				}
			}
		}

		if (hasInvalidElem) {
			errorElemList.add((DocumentContentElement) root);
		}

		return errorElemList;
	}
}
