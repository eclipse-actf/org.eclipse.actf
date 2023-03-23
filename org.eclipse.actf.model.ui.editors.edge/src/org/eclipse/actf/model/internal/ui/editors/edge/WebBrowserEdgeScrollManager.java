/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.internal.ui.editors.edge;

import org.eclipse.actf.model.ui.IModelServiceScrollManager;
import org.eclipse.actf.model.ui.ModelServiceSizeInfo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Composite;

public class WebBrowserEdgeScrollManager implements IModelServiceScrollManager {

	WebBrowserEdgeImpl browserEdge;
	Composite targetComposite;
	private static long SCROLL_DELAY_MS = 50;

	public WebBrowserEdgeScrollManager(WebBrowserEdgeImpl browserEdge) {
		this.browserEdge = browserEdge;
		targetComposite = browserEdge.getTargetComposite();
	}

	// do not have to wait rendering

	public void absoluteCoordinateScroll(int y, boolean waitRendering) {
		browserEdge.scrollY(y);
		if (waitRendering) {
			try {
				Thread.sleep(SCROLL_DELAY_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void absoluteCoordinateScroll(int x, int y, boolean waitRendering) {
		browserEdge.scrollTo(x, y);
		if (waitRendering) {
			try {
				Thread.sleep(SCROLL_DELAY_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int decrementScrollX(boolean waitRendering) {
		return -1;
	}

	public int decrementScrollY(boolean waitRendering) {
		return -1;
	}

	public int incrementScrollX(boolean waitRendering) {
		return -1;
	}

	public int incrementScrollY(boolean waitRendering) {
		return -1;
	}

	public int decrementLargeScrollX(boolean waitRendering) {
		return -1;
	}

	public int decrementLargeScrollY(boolean waitRendering) {
		return -1;
	}

	public int incrementLargeScrollX(boolean waitRendering) {
		return -1;
	}

	public int incrementLargeScrollY(boolean waitRendering) {
		return -1;
	}

	public int decrementPageScroll(boolean waitRendering) {
		return -1;
	}

	public int incrementPageScroll(boolean waitRendering) {
		return -1;
	}

	public int jumpToPage(int pageNumber, boolean waitRendering) {
		return -1;
	}

	public int getCurrentPageNumber() {
		return -1;
	}

	public int getLastPageNumber() {
		return -1;
	}

	public int getScrollType() {
		return ABSOLUTE_COORDINATE;
	}

	public ModelServiceSizeInfo getSize(boolean isWhole) {
		return browserEdge.getBrowserSize(isWhole);
	}

	public Point getRelativePositionToDisplay() {
//		return targetComposite.toDisplay(1, 0);
		return DPIUtil.autoScaleUp(targetComposite.toDisplay(1, 0));
	}

}
