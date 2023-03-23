/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.validation.html;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.validation.html.internal.CheckEngine;
import org.eclipse.actf.visualization.eval.ICheckTarget;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IHtmlCheckTarget;
import org.eclipse.actf.visualization.eval.IHtmlChecker;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.guideline.GuidelineSelectionChangedEvent;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineSlectionChangedListener;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;

public class Checker implements IHtmlChecker, IGuidelineSlectionChangedListener {

	private static final String CHECK_ITEM_PATTERN = "C_\\p{Digit}+(\\.\\p{Digit}+)?"; //$NON-NLS-1$

	private HashSet<String> mimeSet = new HashSet<String>();

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private boolean checkItems[];

	private boolean enabled = false;

	/**
	 * 
	 */
	public Checker() {

		checkItems = new boolean[CheckEngine.ITEM_COUNT];
		updateCheckItems();

		guidelineHolder.addGuidelineSelectionChangedListener(this);

		for (int i = 0; i < IModelService.MIMETYPES_HTML.length; i++) {
			mimeSet.add(IModelService.MIMETYPES_HTML[i]);
		}

	}

	private void updateCheckItems() {
		Arrays.fill(checkItems, false);
		enabled = false;

		for (IEvaluationItem cItem : guidelineHolder.getMatchedCheckitemSet()) {
			// System.out.println(cItem.getId());
			String id = cItem.getId();
			if (id.matches(CHECK_ITEM_PATTERN)) {
				id = id.substring(2);
				int index = id.indexOf(".");  //$NON-NLS-1$
				if (index > -1) {
					id = id.substring(0, index);
				}
				try {
					int item = Integer.parseInt(id);
					if (item > -1 && item < CheckEngine.ITEM_COUNT) {
						checkItems[item] = true;
						enabled = true;
					}
				} catch (Exception e) {
				}

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineSlectionChangedListener#selectionChanged(org.eclipse.actf.visualization.eval.guideline.GuidelineSelectionChangedEvent)
	 */
	public void selectionChanged(GuidelineSelectionChangedEvent e) {
		updateCheckItems();
	}

	public List<IProblemItem> check(ICheckTarget checkTarget) {
		// TODO Exception
		return null;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isTargetFormat(String mimeType) {
		if (mimeType == null) {
			return false;
		}
		return mimeSet.contains(mimeType);
	}

	public List<IProblemItem> checkHtml(IHtmlCheckTarget checkTarget) {
		CheckEngine engine = new CheckEngine(checkTarget.getHtmlEvalUtil(),
				checkItems);
		return (engine.check());

	}

}
