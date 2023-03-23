/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl;

import java.util.Enumeration;
import java.util.HashSet;

import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerInfoProvider;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.VisualizationAttributeInfo;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ElementLabelAndColorProvider extends LabelProvider implements
		ITableLabelProvider, IColorProvider {

	private static String CATEGORY[];

	// TODO more sets
	private static int[][] BG_COLORS = { { 153, 255, 0 }, { 51, 204, 255 },
			{ 255, 153, 0 }, { 255, 128, 255 }, { 153, 51, 255 },
			{ 0, 255, 153 } };

	public ElementLabelAndColorProvider() {
		// if (ParamSystem.isUseInternal()) {
		IElementViewerInfoProvider[] providers = ElementInfoProviderExtension
				.getProviders();
		HashSet<String> tmpS = new HashSet<String>();
		for (int i = 0; i < providers.length; i++) {
			Enumeration<String> keys = providers[i].getCategories();
			while (keys.hasMoreElements()) {
				tmpS.add(keys.nextElement());
			}
		}
		CATEGORY = new String[tmpS.size()];
		tmpS.toArray(CATEGORY);
	}

	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	public String getColumnText(Object arg0, int arg1) {
		VisualizationAttributeInfo eleInfo = (VisualizationAttributeInfo) arg0;
		switch (arg1) {
		case 0:
			return eleInfo.getCategory();
		case 1:
			return eleInfo.getAttributeValue();
		case 2:
			return eleInfo.getTagName();
		case 3:
			return eleInfo.getDescription();
		}
		return null;
	}

	public Color getForeground(Object arg0) {
		return null;
	}

	public Color getBackground(Object arg0) {
		VisualizationAttributeInfo eleInfo = (VisualizationAttributeInfo) arg0;

		for (int i = 0; i < CATEGORY.length; i++) {
			if (eleInfo.getCategory().equals(CATEGORY[i])) {
				// return Display.getCurrent().getSystemColor(
				// backColor[i % backColor.length]);
				// System.out.println(i);
				return new Color(Display.getCurrent(), new RGB(BG_COLORS[i
						% BG_COLORS.length][0],
						BG_COLORS[i % BG_COLORS.length][1], BG_COLORS[i
								% BG_COLORS.length][2]));
			}
		}
		return null;
	}

}
