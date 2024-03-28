/*******************************************************************************
 * Copyright (c) 2007,2024 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.eval;

import org.eclipse.actf.visualization.eval.preferences.IPreferenceConstants;
import org.eclipse.actf.visualization.internal.eval.CheckerExtension;
import org.eclipse.actf.visualization.internal.eval.EvaluationPlugin;
import org.eclipse.actf.visualization.internal.eval.ui.actions.TargetDocumentChangeAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Utility class for org.eclipse.actf.visualization.eval plugin
 */
public class EvaluationUtil {

	/**
	 * The plug-in ID
	 */
	public static final String PLUGIN_ID = "org.eclipse.actf.visualization.eval"; //$NON-NLS-1$

	/**
	 * Set target DOM type. If true, uses original source for evaluation. If false,
	 * uses live DOM.
	 * 
	 */
	public static void setOriginalDOM(boolean isOriginal) {
		IPreferenceStore ps = EvaluationPlugin.getDefault().getPreferenceStore();

		if (isOriginal) {
			ps.setValue(IPreferenceConstants.CHECKER_TARGET, IPreferenceConstants.CHECKER_ORG_DOM);
		} else {
			ps.setValue(IPreferenceConstants.CHECKER_TARGET, IPreferenceConstants.CHECKER_LIVE_DOM);
		}
	}

	/**
	 * Check user selection of target DOM (original source or live)
	 * 
	 * @return true, if user selected original DOM in preference page
	 */
	public static boolean isOriginalDOM() {
		return IPreferenceConstants.CHECKER_ORG_DOM.equals(
				EvaluationPlugin.getDefault().getPreferenceStore().getString(IPreferenceConstants.CHECKER_TARGET));
	}

	/**
	 * Update image and text of IAction to sync target DOM selection
	 * 
	 * @param action
	 */
	public static void updateAction(IAction action) {
		TargetDocumentChangeAction.updateAction(action);
	}

	/**
	 * Create and add IPropertyChangeListener to sync target DOM selection with IAction
	 * 
	 * @param action
	 * @return IPropertyChangeListner for the action
	 */
	public static IPropertyChangeListener addTargetDocumentChangeActionListner(IAction action) {
		IPropertyChangeListener listener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (IPreferenceConstants.CHECKER_TARGET.equals(event.getProperty())) {
					if (null != action) {
						TargetDocumentChangeAction.updateAction(action);
					}
				}
			};
		};

		EvaluationPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(listener);
		return (listener);
	}

	/**
	 * Remove IPropertyChangeListener from PreferenceStore of the EvaluationPlugin
	 * 
	 * @param listener
	 */
	public static void removeTargetDocumentChangeActionListner(IPropertyChangeListener listener) {
		if (null != listener) {
			EvaluationPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(listener);
		}
	}

	/**
	 * Get all registered {@link IChecker}
	 * 
	 * @return array of {@link IChecker}
	 */
	public static IChecker[] getCheckers() {
		return CheckerExtension.getCheckers();
	}

	/**
	 * Get all registered {@link ICheckerInfoProvider}
	 * 
	 * @return array of {@link ICheckerInfoProvider}
	 */
	public static ICheckerInfoProvider[] getCheckerInfoProviders() {
		return CheckerExtension.getCheckerInfoProviders();
	}

}
