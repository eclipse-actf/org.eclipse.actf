/*******************************************************************************
 * Copyright (c) 2006, 2011 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.eval.preferences;

import java.util.Arrays;
import java.util.List;

import org.eclipse.actf.ui.preferences.GroupFieldEditorPreferencePage;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineData;
import org.eclipse.actf.visualization.internal.eval.EvaluationPlugin;
import org.eclipse.actf.visualization.internal.eval.Messages;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class GuidelinePreferencePage extends GroupFieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public static final String ID = GuidelinePreferencePage.class.getName();

	private GuidelineHolder _guidelineHolder;

	private String[] _checkerOptionNames;

	private String[] _criteriaNames;

	private String[] _criteriaNamesOrg;

	private Button[] _criteriaCheckButtons;

	private CheckboxTreeViewer _guidelineTreeViewer;

	private TreeItem[] _guidelineTreeItems = null;

	public GuidelinePreferencePage() {
		super();
		setPreferenceStore(EvaluationPlugin.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
		_guidelineHolder = GuidelineHolder.getInstance();
		_checkerOptionNames = _guidelineHolder.getGuidelineNamesWithLevels();
		_criteriaNamesOrg = _guidelineHolder.getMetricsNames();
		_criteriaNames = _guidelineHolder.getLocalizedMetricsNames();
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();

		createGuidelineTreePart(parent);

		// createPropertiesPart(parent);

		createCriteriaOptionPart(parent);

		createLineNumberInfoPart(parent);

		updateSelectableMetricsButton();

		Composite buttonComposite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		gridData.verticalIndent = 5;
		buttonComposite.setLayoutData(gridData);
		buttonComposite.setLayout(new GridLayout());

		Label spacer = new Label(parent, SWT.NONE);
		gridData = new GridData();
		gridData.verticalIndent = 10;
		spacer.setLayoutData(gridData);

	}

	private void createGuidelineTreePart(Composite parent) {

		final Group guidelineTreeGroup = new Group(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 280;
		guidelineTreeGroup.setLayoutData(gridData);
		guidelineTreeGroup.setLayout(new GridLayout());

		guidelineTreeGroup
				.setText(Messages.adesigner_preference_guideline_list_group_text);

		this._guidelineTreeViewer = new CheckboxTreeViewer(guidelineTreeGroup,
				SWT.BORDER);
		this._guidelineTreeViewer
				.setLabelProvider(new GuidelineTreeLabelProvider());
		this._guidelineTreeViewer
				.setContentProvider(new GuidelineTreeContentProvider());

		Tree guidelineTree = this._guidelineTreeViewer.getTree();
		guidelineTree.setLinesVisible(true);
		guidelineTree.setHeaderVisible(true);
		guidelineTree
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TreeColumn categoryColumn = new TreeColumn(guidelineTree, SWT.NONE);
		categoryColumn.setWidth(240);
		categoryColumn.setText(Messages.GuidelinePreferencePage_0);

		TreeColumn guidelineColumn = new TreeColumn(guidelineTree, SWT.NONE);
		guidelineColumn.setWidth(450);
		guidelineColumn.setText(Messages.GuidelinePreferencePage_1);

		GuidelineTreeItemData root = new GuidelineTreeItemData(null);
		GuidelineTreeItemType htmlType = new GuidelineTreeItemType(
				GuidelineTreeItemType.TYPE_HTML);
		GuidelineTreeItemType odfType = new GuidelineTreeItemType(
				GuidelineTreeItemType.TYPE_ODF);
		root.add(htmlType);
		boolean hasOdf = false;

		IGuidelineData[] guidelineDataArray = this._guidelineHolder
				.getLeafGuidelineData();
		this._checkerOptionNames = this._guidelineHolder
				.getGuidelineNamesWithLevels();

		int guidelineNum = this._checkerOptionNames.length;

		boolean[][] correspondingCriteria = this._guidelineHolder
				.getCorrespondingMetricsOfLeafGuideline();
		for (int i = 0; i < guidelineNum; i++) {
			boolean isHTMLType = false;
			String[] mimeTypes = guidelineDataArray[i].getMIMEtypes();
			for (int j = 0; j < mimeTypes.length; j++) {
				if (mimeTypes[j].equals("text/html") //$NON-NLS-1$
						|| mimeTypes[j].equals("application/xhtml+xml")) { //$NON-NLS-1$
					isHTMLType = true;
					break;
				}
			}
			GuidelineTreeItemData guidelineData = new GuidelineTreeItemData(
					this._checkerOptionNames[i]);
			guidelineData.setCorrespondingCriteria(correspondingCriteria[i]);
			if (isHTMLType) {
				htmlType.add(guidelineData);
			} else {
				if (!hasOdf) {
					root.add(odfType);
					hasOdf = true;
				}
				odfType.add(guidelineData);
			}

			guidelineData.setIndex(i);
			guidelineData.setEnabled(guidelineDataArray[i].isEnabled());
			guidelineData.setCategory(guidelineDataArray[i].getCategory());
			guidelineData
					.setDescription(guidelineDataArray[i].getDescription());
		}

		this._guidelineTreeViewer.setInput(root);
		this._guidelineTreeViewer.expandAll();

		this._guidelineTreeViewer
				.addCheckStateListener(new ICheckStateListener() {
					public void checkStateChanged(CheckStateChangedEvent event) {
						if (event.getElement() instanceof GuidelineTreeItemType) {
							GuidelineTreeItemType guidelineType = (GuidelineTreeItemType) event
									.getElement();
							List<IGuidelineTreeItem> children = guidelineType
									.getChildren();
							for (int i = 0; i < children.size(); i++) {
								GuidelineTreeItemData guidelineData = (GuidelineTreeItemData) children
										.get(i);
								boolean checkState = event.getChecked();
								guidelineData.setEnabled(checkState);
								_guidelineTreeItems[guidelineData.getIndex()]
										.setChecked(checkState);
							}
						}

						updateSelectableMetricsButton();
					}
				});

		this._guidelineTreeItems = new TreeItem[guidelineNum];
		TreeItem[] typeTreeItem = guidelineTree.getItems();
		for (int i = 0; i < typeTreeItem.length; i++) {
			boolean isAllEnabled = true;

			TreeItem[] guidelineTreeItem = typeTreeItem[i].getItems();
			for (int j = 0; j < guidelineTreeItem.length; j++) {
				GuidelineTreeItemData guidelineData = (GuidelineTreeItemData) guidelineTreeItem[j]
						.getData();

				guidelineTreeItem[j].setText(new String[] {
						guidelineData.getCategory(),
						guidelineData.getDescription() });
				guidelineTreeItem[j].setImage(new Image[] { null,
						guidelineData.getGuidelineImage() });

				boolean isGuidelineEnabled = guidelineData.isEnabled();
				guidelineTreeItem[j].setChecked(isGuidelineEnabled);
				if (!isGuidelineEnabled) {
					isAllEnabled = false;
				}

				this._guidelineTreeItems[guidelineData.getIndex()] = guidelineTreeItem[j];
			}

			typeTreeItem[i].setChecked(isAllEnabled);
		}
	}

	private void createCriteriaOptionPart(Composite parent) {

		int columnNum = 4;
		Group criteriaGroup = new Group(parent, SWT.NONE);
		criteriaGroup
				.setText(Messages.adesigner_preference_guideline_criteria_group_text);
		criteriaGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = columnNum;
		criteriaGroup.setLayout(gridLayout);

		int length = this._criteriaNames.length;
		this._criteriaCheckButtons = new Button[length];
		boolean[] isOptionEnabled = _guidelineHolder.getEnabledMetrics();

		GridData gridData;
		for (int i = 0; i < length; i++) {
			this._criteriaCheckButtons[i] = new Button(criteriaGroup, SWT.CHECK);
			this._criteriaCheckButtons[i].setText(this._criteriaNames[i]);
			this._criteriaCheckButtons[i].setSelection(isOptionEnabled[i]);
			if (_criteriaNamesOrg[i]
					.matches("(Perceivable|Operable|Understandable|Robust)")) {
				_criteriaCheckButtons[i].setEnabled(false);
			}

			if (i % columnNum != 0) {
				gridData = new GridData();
				gridData.horizontalIndent = 20;
				this._criteriaCheckButtons[i].setLayoutData(gridData);
			}
		}
	}

	private void createLineNumberInfoPart(Composite parent) {

		addField(new RadioGroupFieldEditor(IPreferenceConstants.CHECKER_TARGET,
				Messages.DialogCheckerOption_Line_Number_Information_19, 1,
				new String[][] {
						{ Messages.DialogCheckerOption_Add_line_number_20,
								IPreferenceConstants.CHECKER_ORG_DOM },
						{ Messages.DialogCheckerOption_LIVE_DOM,
								IPreferenceConstants.CHECKER_LIVE_DOM } },
				parent));

	}

	public boolean performOk() {

		boolean isOK = super.performOk();

		setParameters();

		/*
		 * //TODO WCAG 2.0 support if
		 * (this._guidelineHolder.isEnabledMetric("Navigability")) {
		 * //$NON-NLS-1$
		 * 
		 * IGuidelineData[] datas = this._guidelineHolder
		 * .getLeafGuidelineData(); boolean isWcagOn = false; boolean isWcagOff
		 * = false; boolean isOtherComp = false;
		 * 
		 * for (int i = 0; i < datas.length; i++) { if (datas[i].isEnabled()) {
		 * if (datas[i].getGuidelineName().matches(
		 * "Section508|JIS|IBMGuideline|WCAG 2.0")) { //$NON-NLS-1$ isOtherComp
		 * = true; } else if (datas[i].getGuidelineName().equals("WCAG")) {
		 * //$NON-NLS-1$ isWcagOn = true; } } else { if
		 * (datas[i].getGuidelineName().equals("WCAG")) { //$NON-NLS-1$
		 * isWcagOff = true; } } }
		 * 
		 * if (!isOtherComp && isWcagOn && isWcagOff) {
		 * NavigabilityWarningDialog nwd = new NavigabilityWarningDialog(
		 * getShell()); int result = nwd.open(); switch (result) { case
		 * NavigabilityWarningDialog.ENABLE_ALL: for (int i = 0; i <
		 * this._guidelineTreeItems.length; i++) { if
		 * (this._guidelineTreeItems[i].getText().indexOf( "WCAG") > -1) {
		 * //$NON-NLS-1$ this._guidelineTreeItems[i].setChecked(true); } }
		 * return true; case NavigabilityWarningDialog.DISABLE_NAVIGABILITY: for
		 * (int i = 0; i < this._criteriaCheckButtons.length; i++) { if
		 * (this._criteriaCheckButtons[i].getText().indexOf( "Navigability") >
		 * -1) { //$NON-NLS-1$
		 * this._criteriaCheckButtons[i].setSelection(false); } } return true;
		 * case NavigabilityWarningDialog.CONTINUE: default: // do nothing } } }
		 */

		return isOK;
	}

	private void setParameters() {

		if (null != _criteriaCheckButtons) {
			boolean[] result = new boolean[this._criteriaCheckButtons.length];
			Arrays.fill(result, false);
			for (int i = 0; i < _criteriaCheckButtons.length; i++) {
				if (_criteriaCheckButtons[i] != null
						&& (_criteriaNamesOrg[i]
								.matches("(Perceivable|Operable|Understandable|Robust)") || (_criteriaCheckButtons[i]
								.isEnabled() && _criteriaCheckButtons[i]
								.getSelection()))) {
					result[i] = true;
				}
			}
			this._guidelineHolder.setEnabledMetrics(result);
		}

		if (null != this._guidelineTreeItems) {
			boolean[] result = new boolean[this._guidelineTreeItems.length];
			Arrays.fill(result, false);
			for (int i = 0; i < this._guidelineTreeItems.length; i++) {
				if (null != this._guidelineTreeItems[i]
						&& this._guidelineTreeItems[i].getChecked()) {
					result[i] = true;
				}
			}
			this._guidelineHolder.setEnabledGuidelineWithLevels(result);
		}
	}

	private void updateSelectableMetricsButton() {

		boolean[] isSelectable = new boolean[this._criteriaCheckButtons.length];
		Arrays.fill(isSelectable, false);

		for (int i = 0; i < this._guidelineTreeItems.length; i++) {
			if (this._guidelineTreeItems[i].getChecked()) {
				GuidelineTreeItemData guidelineData = (GuidelineTreeItemData) this._guidelineTreeItems[i]
						.getData();
				for (int j = 0; j < this._criteriaCheckButtons.length; j++) {
					isSelectable[j] = (isSelectable[j] | guidelineData
							.getCorrespondingCriteria()[j]);
				}
			}
		}

		for (int i = 0; i < this._criteriaCheckButtons.length; i++) {
			if (!_criteriaNamesOrg[i]
					.matches("(Perceivable|Operable|Understandable|Robust)")) {
				_criteriaCheckButtons[i].setEnabled(isSelectable[i]);
			}
		}
	}

}
