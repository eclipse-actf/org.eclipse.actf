/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.ui.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemConst;
import org.eclipse.actf.visualization.internal.ui.report.table.ResultTableFilter;
import org.eclipse.actf.visualization.internal.ui.report.table.ResultTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ProblemTree {

	private static final Image ICON_INFO = AbstractUIPlugin
			.imageDescriptorFromPlugin(EvaluationUtil.PLUGIN_ID, "icons/Info.png").createImage();

	private static final Image ICON_WARN = AbstractUIPlugin
			.imageDescriptorFromPlugin(EvaluationUtil.PLUGIN_ID, "icons/Warn.png").createImage();

	private static final Image ICON_ERR = AbstractUIPlugin
			.imageDescriptorFromPlugin(EvaluationUtil.PLUGIN_ID, "icons/Err.png").createImage();

	private static final Image ICON_FOLDER = PlatformUIUtil.getSharedImageDescriptor(ISharedImages.IMG_OBJ_FOLDER)
			.createImage();

	private static final Image ICON_CONF = AbstractUIPlugin
			.imageDescriptorFromPlugin(EvaluationUtil.PLUGIN_ID, "icons/Conf.png").createImage();

	private Tree tree;

	private TreeItem nodeRoot;

	private TreeItem node1_1;

	private TreeItem node1_2;

	private TreeItem node1_3;

	private TreeItem node1_4;

	private ResultTableViewer _resultTableViewer = null;

	public ProblemTree(Composite parent) {

		tree = new Tree(parent, SWT.MULTI | SWT.BORDER);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		tree.setLayoutData(gridData);

		createTreeNodes();

		tree.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				if (null != _resultTableViewer) {
					TreeItem[] targets = tree.getSelection();
					if (targets != null) {
						int result = 0;
						for (int i = 0; i < targets.length; i++) {
							TreeItem item = targets[i];
							if (item == nodeRoot) {
								_resultTableViewer.changeFilterType(ResultTableFilter.ALL);
								return;
							} else if (item == node1_1) {
								result = result | IEvaluationItem.SEV_ERROR;
							} else if (item == node1_2) {
								result = result | IEvaluationItem.SEV_WARNING;
							} else if (item == node1_3) {
								result = result | IEvaluationItem.SEV_USER;
							} else if (item == node1_4) {
								result = result | IEvaluationItem.SEV_INFO;
							}
						}
						_resultTableViewer.changeFilterType(result);
						return;
					}
					_resultTableViewer.changeFilterType(ResultTableFilter.ALL);
				}
			}
		});
	}

	public void createTreeNodes() {
		tree.removeAll();

		nodeRoot = new TreeItem(tree, SWT.NULL);
		nodeRoot.setImage(ICON_FOLDER);

		node1_1 = new TreeItem(nodeRoot, SWT.NULL);
		node1_1.setImage(ICON_ERR); // $NON-NLS-1$

		node1_2 = new TreeItem(nodeRoot, SWT.NULL);
		node1_2.setImage(ICON_WARN); // $NON-NLS-1$

		node1_3 = new TreeItem(nodeRoot, SWT.NULL);
		node1_3.setImage(ICON_CONF); // $NON-NLS-1$

		node1_4 = new TreeItem(nodeRoot, SWT.NULL);
		node1_4.setImage(ICON_INFO); // $NON-NLS-1$

		nodeRoot.setExpanded(true);

		clearTreeNodeAmount();
	}

	public Tree getTree() {
		return tree;
	}

	public void setRootSel() {
		tree.setSelection(new TreeItem[] { nodeRoot });
	}

	public void clearTreeNodeAmount() {
		changeTreeNodeAmount(new ArrayList<IProblemItem>());
		tree.getDisplay().update();
	}

	public void changeTreeNodeAmount(List<IProblemItem> targetList) {

		int error = 0;
		int warn = 0;
		int user = 0;
		int info = 0;

		for (Iterator<IProblemItem> i = targetList.iterator(); i.hasNext();) {
			try {
				switch (i.next().getSeverity()) {
				case IProblemItem.SEV_ERROR:
					error++;
					break;
				case IProblemItem.SEV_WARNING:
					warn++;
					break;
				case IProblemItem.SEV_USER:
					user++;
					break;
				case IProblemItem.SEV_INFO:
					info++;
					break;
				}
			} catch (Exception e) {

			}
		}

		int all = error + user + info;

		nodeRoot.setText(IProblemConst.ALL_ERRORS + "  (" + all + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		node1_1.setText(IProblemConst.ESSENTIAL + "  (" + error + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		node1_2.setText(IProblemConst.WARNING + "  (" + warn + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		node1_3.setText(IProblemConst.USER_CHECK + " (" + user + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		node1_4.setText(IProblemConst.INFO + " (" + info + ")"); //$NON-NLS-1$ //$NON-NLS-2$ ;

	}

	public void setResultTableViewer(ResultTableViewer resultTableViewer) {
		this._resultTableViewer = resultTableViewer;
	}

	public ResultTableViewer getResultTableViewer() {
		return this._resultTableViewer;
	}

	public void setResult(IEvaluationResult result) {
		changeTreeNodeAmount(result.getProblemList());
	}
}
