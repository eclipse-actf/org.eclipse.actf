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

import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.actf.model.dom.odf.base.DrawingObjectElement;
import org.eclipse.actf.model.dom.odf.base.EmbedDrawingObjectElement;
import org.eclipse.actf.model.dom.odf.base.ODFDocument;
import org.eclipse.actf.model.dom.odf.draw.ControlElement;
import org.eclipse.actf.model.dom.odf.draw.ImageElement;
import org.eclipse.actf.model.dom.odf.draw.ImageMapAreaElement;
import org.eclipse.actf.model.dom.odf.draw.ObjectElement;
import org.eclipse.actf.model.dom.odf.draw.ObjectOleElement;
import org.eclipse.actf.model.dom.odf.draw.PageElement;
import org.eclipse.actf.model.dom.odf.office.DocumentContentElement;
import org.eclipse.actf.model.dom.odf.table.TableElement;
import org.eclipse.actf.model.dom.odf.text.AElement;
import org.eclipse.actf.model.dom.odf.util.converter.ODFConverter;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OdfCheckerEngineImpl extends OdfCheckerEngineBase {

	/**
	 * @param target
	 * @param result
	 * @param curUrlS
	 * @param document2IdMap
	 */
	public OdfCheckerEngineImpl(Document targetODF, Document target) {
		super(targetODF, target);
	}

	private Element findElementByODFContentID(Document doc, String odfContentID) {
		if (target != null) {
			NodeList bodyNl = target.getElementsByTagName("body"); //$NON-NLS-1$
			if (bodyNl.getLength() > 0) {
				Stack<Node> stack = new Stack<Node>();
				Node curNode = bodyNl.item(0);
				while (curNode != null) {
					if (curNode.getNodeType() == Node.ELEMENT_NODE) {
						Element el = (Element) curNode;
						String id = el
								.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID);
						if (odfContentID.equals(id)) {
							return (el);
						}
					}

					if (curNode.hasChildNodes()) {
						stack.push(curNode);
						curNode = curNode.getFirstChild();
					} else if (curNode.getNextSibling() != null) {
						curNode = curNode.getNextSibling();
					} else {
						curNode = null;
						while ((curNode == null) && (stack.size() > 0)) {
							curNode = stack.pop();
							curNode = curNode.getNextSibling();
						}
					}
				}
			}
		}
		return null;
	}

	protected void addProblems(int id, String midDesc, Vector<Node> targetV) {
		for (Iterator<Node> iter = targetV.iterator(); iter.hasNext();) {
			addProblem(id, iter.next(), midDesc);
		}
	}

	public Vector<IProblemItem> check() {
		if (detectVersion) {
			targetODF = checkOdfVersion(targetODF);
		}

		check101();
		check201();
		check301();
		check401();
		check50101();
		check601();
		check70101();
		check801();
		return this.resultV;
	}

	/*
	 * check if wrong ODF version is saved
	 */
	private Document checkOdfVersion(Document targetODF) {
		if (targetODF instanceof ODFDocument) {
			double version = ((ODFDocument) targetODF).getODFVersion();
			if (version == 1.0) {
				List<DocumentContentElement> errorElemList = OdfCheckerUtil
						.getInstance().getODF10CompativilityError(targetODF);
				if (errorElemList.size() != 0) {
					((ODFDocument) targetODF).setODFVersion(1.1);
					DocumentContentElement root = errorElemList.get(0);
					Node resultNode = findElementByODFContentID(
							target,
							root
									.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
					addProblem(90101, resultNode, null);
				}
			}
		}
		return targetODF;
	}

	/*
	 * check item for <draw:image>
	 */
	private void check101() {
		check10101();
		check10102();
	}

	/*
	 * check item for <table:table>
	 */
	private void check201() {
		check20101();
		check20102();
	}

	/*
	 * check item for <draw:image-map>
	 */
	private void check301() {
		check30101();
		check30102();
	}

	/*
	 * check item for drawing objects
	 */
	private void check401() {
		check40101();
		check40102();
	}

	/*
	 * check item for embed objects
	 */
	private void check601() {
		check60101();
		check60102();
	}

	/*
	 * check item for navigation order
	 */
	private void check801() {
		check80101();
		check80102();
		check80103();
	}

	private void check10101() {
		List<ImageElement> errorElemList = OdfCheckerUtil.getInstance()
				.getImageShortDescError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			ImageElement image = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, image
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(10101, resultNode, null);
		}
	}

	private void check10102() {
		List<ImageElement> errorElemList = OdfCheckerUtil.getInstance()
				.getImageLongDescError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			ImageElement image = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, image
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(10102, resultNode, null);
		}
	}

	private void check20101() {
		List<TableElement> errorElemList = OdfCheckerUtil.getInstance()
				.getTableHeaderError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			TableElement table = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, table
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(20101, resultNode, null);
		}
	}

	private void check20102() {
		List<TableElement> errorElemList = OdfCheckerUtil.getInstance()
				.getTableCaptionError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			TableElement table = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, table
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(20102, resultNode, null);
		}
	}

	private void check30101() {
		List<ImageMapAreaElement> errorElemList = OdfCheckerUtil.getInstance()
				.getImageMapShortDescError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			ImageMapAreaElement imageMapArea = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, imageMapArea
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(30101, resultNode, null);
		}
	}

	private void check30102() {
		List<ImageMapAreaElement> errorElemList = OdfCheckerUtil.getInstance()
				.getImageMapLongDescError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			ImageMapAreaElement imageMapArea = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, imageMapArea
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(30102, resultNode, null);
		}
	}

	private void check40101() {
		List<DrawingObjectElement> errorElemList = OdfCheckerUtil.getInstance()
				.getNotGroupDrawingObjectShortDescError(targetODF);
		errorElemList.addAll(OdfCheckerUtil.getInstance()
				.getGroupObjectShortDescError(targetODF));
		for (int i = 0; i < errorElemList.size(); i++) {
			Object errorElem = errorElemList.get(i);
			if (errorElem instanceof DrawingObjectElement) {
				DrawingObjectElement drawingObject = (DrawingObjectElement) errorElem;
				Node resultNode = findElementByODFContentID(
						target,
						drawingObject
								.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
				addProblem(40101, resultNode, null);
			}
		}
	}

	private void check40102() {
		List<DrawingObjectElement> errorElemList = OdfCheckerUtil.getInstance()
				.getDrawingObjectLongDescError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			DrawingObjectElement drawingObject = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, drawingObject
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(40102, resultNode, null);
		}
	}

	/*
	 * check item for <text:a>
	 */
	private void check50101() {
		List<AElement> errorElemList = OdfCheckerUtil.getInstance()
				.getLinkHintError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			AElement aElem = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, aElem
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(50101, resultNode, null);
		}
	}

	private void check60101() {
		List<EmbedDrawingObjectElement> errorElemList = OdfCheckerUtil
				.getInstance().getEmbedObjectShortDescError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			EmbedDrawingObjectElement errorElem = errorElemList.get(i);
			if ((errorElem instanceof ObjectElement)
					|| (errorElem instanceof ObjectOleElement)) {
				Node resultNode = findElementByODFContentID(target, errorElem
						.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
				addProblem(60101, resultNode, null);
			}
		}
	}

	private void check60102() {
		List<EmbedDrawingObjectElement> errorElemList = OdfCheckerUtil
				.getInstance().getEmbedObjectLongDescError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			EmbedDrawingObjectElement errorElem = errorElemList.get(i);
			if ((errorElem instanceof ObjectElement)
					|| (errorElem instanceof ObjectOleElement)) {
				Node resultNode = findElementByODFContentID(target, errorElem
						.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
				addProblem(60102, resultNode, null);
			}
		}
	}

	private void check70101() {
		List<ControlElement> errorElemList = OdfCheckerUtil.getInstance()
				.getFormLabelError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			ControlElement control = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, control
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(70101, resultNode, null);
		}
	}

	private void check80101() {
		List<PageElement> errorElemList = OdfCheckerUtil.getInstance()
				.getDrawNavOrderError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			PageElement page = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, page
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(80101, resultNode, null);
		}
	}

	private void check80102() {
		List<DocumentContentElement> errorElemList = OdfCheckerUtil
				.getInstance().getFormTabIndexError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			DocumentContentElement content = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, content
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(80102, resultNode, null);
		}
	}

	private void check80103() {
		List<ControlElement> errorElemList = OdfCheckerUtil.getInstance()
				.getFormTabStopError(targetODF);
		for (int i = 0; i < errorElemList.size(); i++) {
			ControlElement control = errorElemList.get(i);
			Node resultNode = findElementByODFContentID(target, control
					.getAttribute(ODFConverter.OUTPUT_ATTR_ODF_CONTENT_ID));
			addProblem(80103, resultNode, null);
		}
	}
}