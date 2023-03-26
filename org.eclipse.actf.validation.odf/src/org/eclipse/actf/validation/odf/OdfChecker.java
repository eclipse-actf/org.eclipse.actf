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

import java.util.HashSet;
import java.util.List;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.validation.odf.preferences.OdfCheckerPreferenceConstants;
import org.eclipse.actf.visualization.eval.ICheckTarget;
import org.eclipse.actf.visualization.eval.IChecker;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.jface.preference.IPreferenceStore;

public class OdfChecker implements IChecker {

	private IPreferenceStore store = OdfCheckerPlugin.getDefault()
			.getPreferenceStore();

	private HashSet<String> mimeSet = new HashSet<String>();

	public OdfChecker() {
		for (String tmpS : IModelService.MIMETYPES_ODF) {
			mimeSet.add(tmpS);
		}
	}

	public List<IProblemItem> check(ICheckTarget checkTarget) {
		OdfCheckerEngine checker = new OdfCheckerEngineImpl(checkTarget
				.getTargetDocument(), checkTarget.getAdditionalDocument("html")); //$NON-NLS-1$
		checker
				.setDetectOdfVersion(store
						.getBoolean(OdfCheckerPreferenceConstants.ODFPLUGIN_DETECT_VERSION));
		return checker.check();
	}

	public boolean isTargetFormat(String mimeType) {
		if (mimeType == null) {
			return false;
		}
		return (mimeSet.contains(mimeType));
	}

	public boolean isEnabled() {
		// TODO
		IEvaluationItem tmpItem = GuidelineHolder.getInstance()
				.getEvaluationItem("O_999000001"); //$NON-NLS-1$
		return (tmpItem != null && GuidelineHolder.getInstance()
				.isMatchedCheckItem(tmpItem));
	}
}