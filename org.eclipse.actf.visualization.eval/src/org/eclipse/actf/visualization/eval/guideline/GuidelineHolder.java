/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.guideline;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.eval.ICheckerInfoProvider;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.ITechniquesItem;
import org.eclipse.actf.visualization.eval.preferences.ICheckerPreferenceConstants;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.eclipse.actf.visualization.eval.problem.ReportUtil;
import org.eclipse.actf.visualization.internal.eval.CheckerExtension;
import org.eclipse.actf.visualization.internal.eval.EvaluationItemImpl;
import org.eclipse.actf.visualization.internal.eval.EvaluationPlugin;
import org.eclipse.actf.visualization.internal.eval.Messages;
import org.eclipse.actf.visualization.internal.eval.guideline.CheckItemReader;
import org.eclipse.actf.visualization.internal.eval.guideline.GuidelineData;
import org.eclipse.actf.visualization.internal.eval.guideline.GuidelineDataComparator;
import org.eclipse.actf.visualization.internal.eval.guideline.GuidelineItemReader;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.Bundle;

/**
 * Utility class to manage guideline, metrics, and evaluation items.
 */
public class GuidelineHolder {

	private static final String UNDERSCORE = "_"; //$NON-NLS-1$

	private static GuidelineHolder INSTANCE = null;

	private IPreferenceStore preferenceStore = EvaluationPlugin.getDefault()
			.getPreferenceStore();

	private ICheckerInfoProvider[] checkerInfos = CheckerExtension
			.getCheckerInfoProviders();

	/**
	 * Get instance of {@link GuidelineHolder}
	 * 
	 * @return instance of {@link GuidelineHolder}
	 */
	public static GuidelineHolder getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GuidelineHolder();
		}
		return (INSTANCE);
	}

	private Map<String, GuidelineData> guidelineMaps = new HashMap<String, GuidelineData>();

	private Set<GuidelineData> guidelineSet = new TreeSet<GuidelineData>(
			new GuidelineDataComparator());

	private GuidelineData[] guidelineArray;

	private GuidelineData[] leafGuidelineArray;

	private String[] guidelineNamesWithLevels;

	private String[] guidelineNames;

	private Map<String, IEvaluationItem> checkitemMap = new HashMap<String, IEvaluationItem>();

	private String[] metricsNames = new String[0];

	private String[] localizedMetricsNames = new String[0];

	private boolean[][] correspondingMetricsOfLeafGuideline;

	private boolean[] enabledMetrics;

	private Set<IEvaluationItem> enabledCheckitemSet = new HashSet<IEvaluationItem>();

	private Set<IGuidelineItem> enabledGuidelineitemSet = new HashSet<IGuidelineItem>();

	private boolean[] matchedMetrics;

	private Set<IEvaluationItem> matchedCheckitemSet = new HashSet<IEvaluationItem>();

	private Set<IGuidelineItem> matchedGuidelineitemSet = new HashSet<IGuidelineItem>();

	private Set<IGuidelineSlectionChangedListener> guidelineSelectionChangedListenerSet = new HashSet<IGuidelineSlectionChangedListener>();

	private String currentMimeType = "text/html"; //$NON-NLS-1$

	/**
	 * for debug/print use. Set of TechniquesItems
	 */
	private SortedSet<ITechniquesItem> techniquesItemSet;
	/**
	 * for debug/print use. Map from a techniques ID to a Set of ProblemItems
	 */
	private Map<String, Set<IProblemItem>> techniquesProblemMap;

	// TODO guideline base -> check item base On/Off

	private GuidelineHolder() {
		Bundle bundle = EvaluationPlugin.getDefault().getBundle();
		Enumeration<URL> guidelines = bundle.findEntries(
				"resources/guidelines", "*.xml", false); //$NON-NLS-1$ //$NON-NLS-2$

		InputStream is;

		while (guidelines.hasMoreElements()) {
			try {
				readGuidelines(guidelines.nextElement().openStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (ICheckerInfoProvider checkerInfo : checkerInfos) {
			InputStream[] iss = checkerInfo.getGuidelineInputStreams();
			if (null != iss) {
				DebugPrintUtil.devOrDebugPrintln(checkerInfo.getClass()
						.getName() + ":" + iss.length); //$NON-NLS-1$
				for (InputStream tmpIs : iss) {
					readGuidelines(tmpIs);
				}
			}
		}

		guidelineNames = new String[guidelineSet.size()];
		guidelineArray = new GuidelineData[guidelineSet.size()];
		guidelineSet.toArray(guidelineArray);
		Vector<GuidelineData> tmpV = new Vector<GuidelineData>();
		Vector<String> tmpV2 = new Vector<String>();
		for (int i = 0; i < guidelineArray.length; i++) {
			GuidelineData data = guidelineArray[i];
			guidelineNames[i] = data.getGuidelineName();
			String[] levels = data.getLevels();
			if (levels.length > 0) {
				for (int j = 0; j < levels.length; j++) {
					GuidelineData subData = data.getSubLevelData(levels[j]);
					tmpV.add(subData);
					String name = subData.getGuidelineName() + " (" //$NON-NLS-1$
							+ subData.getLevelStr() + ")"; //$NON-NLS-1$
					tmpV2.add(name);
				}
			} else {
				tmpV.add(data);
				tmpV2.add(data.getGuidelineName());
			}
		}
		leafGuidelineArray = new GuidelineData[tmpV.size()];
		tmpV.toArray(leafGuidelineArray);
		guidelineNamesWithLevels = new String[tmpV2.size()];
		tmpV2.toArray(guidelineNamesWithLevels);

		// check item config

		try {
			is = FileLocator.openStream(bundle, new Path(
					"resources/checkitem.xml"), false); //$NON-NLS-1$
			CheckItemReader cir = CheckItemReader.parse(is, this);
			Set<String> metricsNameSet = new HashSet<String>();
			if (cir.isSucceed()) {
				checkitemMap = cir.getCheckItemMap();
				metricsNameSet = cir.getMetricsSet();

				for (ICheckerInfoProvider checkerInfo : checkerInfos) {
					InputStream[] iss = checkerInfo.getCheckItemInputStreams();
					if (null != iss) {
						DebugPrintUtil.devOrDebugPrintln(checkerInfo.getClass()
								.getName() + ":" + iss.length); //$NON-NLS-1$
						for (InputStream tmpIs : iss) {
							try {
								cir = CheckItemReader.parse(tmpIs, this);
							} catch (Exception e) {
								DebugPrintUtil
										.devOrDebugPrintln("can't parse: " //$NON-NLS-1$
												+ checkerInfo.getClass()
														.getName());
							}
							if (cir.isSucceed()) {
								checkitemMap.putAll(cir.getCheckItemMap());
								metricsNameSet.addAll(cir.getMetricsSet());
							}
						}
					}
				}

				metricsNames = new String[metricsNameSet.size()];
				localizedMetricsNames = new String[metricsNameSet.size()];
				metricsNameSet.toArray(metricsNames);
				metricsNameSet.toArray(localizedMetricsNames);

				for (int i = 0; i < localizedMetricsNames.length; i++) {
					if (localizedMetricsNames[i]
							.equalsIgnoreCase("perceivable")) {
						localizedMetricsNames[i] = Messages.Perceivable;
					} else if (localizedMetricsNames[i]
							.equalsIgnoreCase("operable")) {
						localizedMetricsNames[i] = Messages.Operable;
					} else if (localizedMetricsNames[i]
							.equalsIgnoreCase("understandable")) {
						localizedMetricsNames[i] = Messages.Understandable;
					} else if (localizedMetricsNames[i]
							.equalsIgnoreCase("robust")) {
						localizedMetricsNames[i] = Messages.Robust;
					}
				}

				enabledMetrics = new boolean[metricsNameSet.size()];
				Arrays.fill(enabledMetrics, true);
			} else {
				// TODO error report
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (IEvaluationItem tmpItem : checkitemMap.values()) {
			if (tmpItem instanceof EvaluationItemImpl) {
				((EvaluationItemImpl) tmpItem).initMetrics(metricsNames);
			}
			addGuidelineSelectionChangedListener(tmpItem);
		}

		initGuidelineNameLevel2checkItem();
		initDisabledGuideline();
		initDisabledMetrics();
		initCorrespondingMetrics();
		resetMatchedItems();
		notifyGuidelineSelectionChange();
	}

	/**
	 * Get registered guideline information. If guideline has levels, this
	 * method returns all guideline items of each level.
	 * 
	 * @return all leaf guideline information
	 */
	public IGuidelineData[] getLeafGuidelineData() {
		return (leafGuidelineArray);
	}

	/**
	 * Get registered guideline information. This method returns all guideline
	 * items (excludes leaf items).
	 * 
	 * @return all top guideline information
	 */
	public IGuidelineData[] getGuidelineData() {
		return (guidelineArray);
	}

	/**
	 * Get all guideline name with it's level information.
	 * 
	 * @return all guideline name with level
	 */
	public String[] getGuidelineNamesWithLevels() {
		return (guidelineNamesWithLevels);
	}

	/**
	 * Get guideline item information
	 * 
	 * @param guidelineName
	 *            target guideline name
	 * @param id
	 *            target guideline item ID
	 * @return guideline item information, or null if not available
	 */
	public IGuidelineItem getGuidelineItem(String guidelineName, String id) {
		if (guidelineMaps.containsKey(guidelineName)) {
			IGuidelineData data = guidelineMaps.get(guidelineName);
			return (data.getGuidelineItem(id));
		}
		return (null);
	}

	/**
	 * Get evaluation item information
	 * 
	 * @param id
	 *            target ID of evaluation item
	 * @return evaluation item information, or null if not available
	 */
	public IEvaluationItem getEvaluationItem(String id) {
		if (checkitemMap.containsKey(id)) {
			return (checkitemMap.get(id));
		}
		return (null);
	}

	/**
	 * Set enabled guideline items.
	 * 
	 * @param enabledItems
	 *            on/off parameters
	 * @return true (success), false (array size error)
	 */
	public boolean setEnabledGuidelineWithLevels(boolean[] enabledItems) {
		if (enabledItems.length != leafGuidelineArray.length) {
			return false;
		}

		enabledCheckitemSet.clear();
		enabledGuidelineitemSet.clear();

		for (int i = 0; i < enabledItems.length; i++) {
			GuidelineData data = leafGuidelineArray[i];
			data.setEnabled(enabledItems[i]);
			if (enabledItems[i]) {
				addEnabledItems(data);
			}
		}

		storeDisabledGuideline();
		resetMatchedItems();
		notifyGuidelineSelectionChange();
		return true;
	}

	/**
	 * Set enabled guideline items.
	 * 
	 * @param guidelineNameArray
	 *            array of name of enabled guideline item
	 * @param levelArray
	 *            array of level of enabled guideline item
	 */
	public void setEnabledGuidelines(String[] guidelineNameArray,
			String[] levelArray) {

		for (int i = 0; i < leafGuidelineArray.length; i++) {
			leafGuidelineArray[i].setEnabled(false);
		}

		if (guidelineNameArray != null && levelArray != null
				&& guidelineNameArray.length == levelArray.length) {
			enabledCheckitemSet.clear();
			enabledGuidelineitemSet.clear();

			for (int i = 0; i < guidelineNameArray.length; i++) {
				if (guidelineMaps.containsKey(guidelineNameArray[i])) {
					GuidelineData data = guidelineMaps
							.get(guidelineNameArray[i]);
					if (levelArray[i] == null) {
						data.setEnabled(true);
						addEnabledItems(data);
					} else {
						data = data.getSubLevelData(levelArray[i]);
						if (data != null) {
							data.setEnabled(true);
							addEnabledItems(data);
						}
					}

				}
			}

			storeDisabledGuideline();
			resetMatchedItems();

			notifyGuidelineSelectionChange();
		}

	}

	private void addEnabledItems(GuidelineData data) {
		if (data.isEnabled()) {
			enabledCheckitemSet.addAll(data.getEvaluationItemSet());
			enabledGuidelineitemSet.addAll(data.getGuidelineItemMap().values());
		}
	}

	private void resetMatchedItems() {
		matchedCheckitemSet.clear();
		matchedGuidelineitemSet.clear();
		matchedMetrics = new boolean[metricsNames.length];
		Arrays.fill(matchedMetrics, false);

		for (int i = 0; i < leafGuidelineArray.length; i++) {
			GuidelineData data = leafGuidelineArray[i];
			data.setCurrentMIMEtype(currentMimeType);
			if (data.isMatched()) {
				matchedCheckitemSet.addAll(data.getEvaluationItemSet());
				matchedGuidelineitemSet.addAll(data.getGuidelineItemMap()
						.values());
				for (int j = 0; j < metricsNames.length; j++) {
					matchedMetrics[j] = (enabledMetrics[j] && (matchedMetrics[j] | correspondingMetricsOfLeafGuideline[i][j]));
				}
			}
		}

		for (int i = 0; i < guidelineArray.length; i++) {
			guidelineArray[i].setCurrentMIMEtype(currentMimeType);
		}

		// System.out.println(matchedCheckitemSet.size() + " " +
		// matchedGuidelineitemSet.size());

	}

	/**
	 * Set enabled evaluation metrics
	 * 
	 * @param enabledMetrics
	 *            on/off parameters
	 * @return true (success), false (array size error)
	 */
	public boolean setEnabledMetrics(boolean[] enabledMetrics) {
		if (enabledMetrics == null
				|| enabledMetrics.length != this.enabledMetrics.length) {
			return false;
		}
		this.enabledMetrics = enabledMetrics;
		storeDisabledMetrics();
		resetMatchedItems();
		notifyGuidelineSelectionChange();
		return true;
	}

	/**
	 * Get enabled evaluation metrics.
	 * 
	 * @param enabledMetricsStringArray
	 *            array of enabled evaluation metrics
	 */
	public void setEnabledMetrics(String[] enabledMetricsStringArray) {
		if (enabledMetricsStringArray != null) {
			Arrays.fill(enabledMetrics, false);

			for (int i = 0; i < metricsNames.length; i++) {
				for (int j = 0; j < enabledMetricsStringArray.length; j++) {
					if (metricsNames[i]
							.equalsIgnoreCase(enabledMetricsStringArray[j])) {
						enabledMetrics[i] = true;
						break;
					}
				}
			}

			storeDisabledMetrics();
			resetMatchedItems();
			notifyGuidelineSelectionChange();
		}
	}

	/**
	 * Get set of {@link IEvaluationItem} matched to current active content and
	 * user selection of guidelines/metrics.
	 * 
	 * @return set of {@link IEvaluationItem}
	 */
	public Set<IEvaluationItem> getMatchedCheckitemSet() {
		// 061018 kf
		// return enabledCheckitemSet;
		return matchedCheckitemSet;
	}

	/**
	 * Get registered evaluation metrics names.
	 * 
	 * @return evaluation metrics
	 */
	public String[] getMetricsNames() {
		return metricsNames;
	}

	/**
	 * Get registered evaluation metrics names (localized).
	 * 
	 * @return evaluation metrics (localized)
	 */
	public String[] getLocalizedMetricsNames() {
		return localizedMetricsNames;
	}

	private void initGuidelineNameLevel2checkItem() {
		for (GuidelineData data : guidelineMaps.values()) {
			data.setEvaluationItems(checkitemMap.values(), metricsNames);
		}

	}

	private void initDisabledGuideline() {

		try {

			for (int i = 0; i < guidelineArray.length; i++) {
				GuidelineData data = guidelineArray[i];
				String[] subLevels = data.getLevels();
				if (subLevels.length == 0) {
					String tmpS = ICheckerPreferenceConstants.GUIDELINE_PREFIX
							+ data.getGuidelineName() + UNDERSCORE;
					if (preferenceStore.contains(tmpS)
							&& preferenceStore.getBoolean(tmpS)) {
						data.setEnabled(false);
					} else {
						data.setEnabled(true);
					}
				} else {
					for (int j = 0; j < subLevels.length; j++) {
						GuidelineData subData = data
								.getSubLevelData(subLevels[j]);
						String tmpS = ICheckerPreferenceConstants.GUIDELINE_PREFIX
								+ subData.getGuidelineName() + UNDERSCORE + j;
						if (preferenceStore.contains(tmpS)
								|| preferenceStore.getBoolean(tmpS)) {
							subData.setEnabled(false);
						} else {
							subData.setEnabled(true);
						}
					}
				}

			}
		} catch (Exception e) {
		}
	}

	private void storeDisabledGuideline() {
		try {
			for (int i = 0; i < guidelineArray.length; i++) {
				IGuidelineData data = guidelineArray[i];
				String[] subLevels = data.getLevels();
				if (subLevels.length == 0) {
					preferenceStore.setValue(
							ICheckerPreferenceConstants.GUIDELINE_PREFIX
									+ data.getGuidelineName() + UNDERSCORE,
							!data.isEnabled());
				} else {
					for (int j = 0; j < subLevels.length; j++) {
						IGuidelineData subData = data
								.getSubLevelData(subLevels[j]);
						preferenceStore.setValue(
								ICheckerPreferenceConstants.GUIDELINE_PREFIX
										+ subData.getGuidelineName()
										+ UNDERSCORE + j, !subData.isEnabled());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initDisabledMetrics() {
		try {
			for (int j = 0; j < metricsNames.length; j++) {
				String tmpS = ICheckerPreferenceConstants.METRICS_PREFIX
						+ metricsNames[j];
				if (preferenceStore.contains(tmpS)
						&& preferenceStore.getBoolean(tmpS)) {
					enabledMetrics[j] = false;
				}
			}
		} catch (Exception e) {
		}
	}

	private void initCorrespondingMetrics() {
		correspondingMetricsOfLeafGuideline = new boolean[leafGuidelineArray.length][metricsNames.length];

		for (int i = 0; i < leafGuidelineArray.length; i++) {
			correspondingMetricsOfLeafGuideline[i] = leafGuidelineArray[i]
					.getCorrespondingMetrics();
		}
	}

	private void storeDisabledMetrics() {
		try {
			for (int i = 0; i < enabledMetrics.length; i++) {
				if (!enabledMetrics[i]) {
					preferenceStore.setValue(
							ICheckerPreferenceConstants.METRICS_PREFIX
									+ metricsNames[i], true);
				} else {
					preferenceStore.setValue(
							ICheckerPreferenceConstants.METRICS_PREFIX
									+ metricsNames[i], false);
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Check if the target {@link IEvaluationItem} is enabled in current
	 * environment (active content and user selection)
	 * 
	 * @param target
	 *            target {@link IEvaluationItem}
	 * @return true if the target item is needed to evaluate
	 */
	public boolean isMatchedCheckItem(IEvaluationItem target) {
		return (matchedCheckitemSet.contains(target));
	}

	/**
	 * Check if the target {@link IGuidelineItem} is enabled in current
	 * environment (active content and user selection)
	 * 
	 * @param target
	 *            target {@link IGuidelineItem}
	 * @return true if the target item is enabled
	 * 
	 * @deprecated
	 */
	public boolean isMatchedGuidelineItem(IGuidelineItem target) {
		return (matchedGuidelineitemSet.contains(target));
	}

	/**
	 * Check if at least one of the child levels of target
	 * {@link IGuidelineItem} is enabled in current environment (active content
	 * and user selection)
	 * 
	 * @param target
	 *            target top level {@link IGuidelineItem}
	 * @return true if at least one of the child levels of the target is enabled
	 * 
	 * @deprecated
	 */
	public boolean isMatchedInTopLevel(IGuidelineItem target) {
		if (guidelineMaps.containsKey(target.getGuidelineName())) {
			IGuidelineData data = guidelineMaps.get(target.getGuidelineName());
			return (data.isEnabled() && data.isTargetMIMEtype(currentMimeType));
		}
		return (false);
	}

	/**
	 * Check if the target evaluation metric is enabled
	 * 
	 * @param metricName
	 *            target metric
	 * @return true if the target item is needed to evaluate
	 */
	public boolean isEnabledMetric(String metricName) {
		for (int i = 0; i < metricsNames.length; i++) {
			if (metricsNames[i].equalsIgnoreCase(metricName)) {
				return (enabledMetrics[i]);
			}
		}
		return (false);
	}

	/**
	 * Check if the target evaluation metric is enabled in current environment
	 * (active content and user selection)
	 * 
	 * @param metricName
	 *            target metric
	 * @return true if the target item is needed to evaluate
	 */
	public boolean isMatchedMetric(String metricName) {
		for (int i = 0; i < metricsNames.length; i++) {
			if (metricsNames[i].equalsIgnoreCase(metricName)) {
				return (matchedMetrics[i]);
			}
		}
		return (false);
	}

	private void readGuidelines(InputStream is) {
		GuidelineData data = GuidelineItemReader.getGuidelineData(is);
		if (data != null) {
			guidelineMaps.put(data.getGuidelineName(), data);
			if (!guidelineSet.add(data)) {
				guidelineSet.remove(data);
				guidelineSet.add(data);
			}
		}
	}

	/**
	 * Get enabled evaluation metrics
	 * 
	 * @return array of enabled evaluation metrics
	 */
	public boolean[] getEnabledMetrics() {
		return enabledMetrics;
	}

	/**
	 * Get evaluation metric matched to current environment (active content and
	 * user selection)
	 * 
	 * @return array of enabled evaluation metrics
	 */
	public boolean[] getMatchedMetrics() {
		return matchedMetrics;
	}

	/**
	 * Add {@link IGuidelineSlectionChangedListener} to listen user change of a
	 * selection of target guidelines and/or metrics.
	 * 
	 * @param listener
	 *            target {@link IGuidelineSlectionChangedListener}
	 */
	public void addGuidelineSelectionChangedListener(
			IGuidelineSlectionChangedListener listener) {
		guidelineSelectionChangedListenerSet.add(listener);
	}

	/**
	 * Remove {@link IGuidelineSlectionChangedListener}
	 * 
	 * @param listener
	 *            target {@link IGuidelineSlectionChangedListener}
	 */
	public void removeGuidelineSelectionChangedListener(
			IGuidelineSlectionChangedListener listener) {
		guidelineSelectionChangedListenerSet.remove(listener);
	}

	private void notifyGuidelineSelectionChange() {
		GuidelineSelectionChangedEvent event = new GuidelineSelectionChangedEvent(
				this);
		for (IGuidelineSlectionChangedListener listener : guidelineSelectionChangedListenerSet) {
			listener.selectionChanged(event);
		}
	}

	/**
	 * @return
	 */
	public String getTargetMimeType() {
		return currentMimeType;
	}

	/**
	 * @param currentMimeType
	 */
	public void setTargetMimeType(String currentMimeType) {
		if (currentMimeType != null
				&& !currentMimeType.equals(this.currentMimeType)) {
			this.currentMimeType = currentMimeType;
			resetMatchedItems();
			notifyGuidelineSelectionChange();
		}
	}

	/**
	 * @return
	 */
	public boolean[][] getCorrespondingMetricsOfLeafGuideline() {
		return correspondingMetricsOfLeafGuideline;
	}

	/**
	 * @return
	 */
	public String[] getGuidelineNames() {
		return guidelineNames;
	}

	private class ItemType {
		boolean error = false;
		boolean warning = false;
		boolean user = false;
		boolean info = false;

		public ItemType(int severity) {
			setSeverity(severity);
		}

		public void setSeverity(int severity) {
			switch (severity) {
			case IProblemItem.SEV_ERROR:
				error = true;
				break;
			case IProblemItem.SEV_WARNING:
				warning = true;
				break;
			case IProblemItem.SEV_USER:
				user = true;
				break;
			case IProblemItem.SEV_INFO:
				info = true;
				break;
			}
		}

		@Override
		public String toString() {
			StringBuffer tmpSB = new StringBuffer();
			if (error) {
				tmpSB.append(Messages.ProblemConst_Essential_2 + " / ");
			}
			if (warning) {
				tmpSB.append(Messages.ProblemConst_Warning + " / ");
			}
			if (user) {
				tmpSB.append(Messages.ProblemConst_User_Check_5 + " / ");
			}
			if (info) {
				tmpSB.append(Messages.ProblemConst_Info + " / ");
			}
			if (tmpSB.length() > 3) {
				tmpSB.setLength(tmpSB.length() - 3);
			}
			return tmpSB.toString();
		}
	}

	@Override
	public String toString() {
		Set<IEvaluationItem> eSet = getMatchedCheckitemSet();
		ArrayList<IProblemItem> ar = new ArrayList<IProblemItem>();
		for (IEvaluationItem i : eSet) {
			ar.add(new ProblemItemImpl(i.getId()));
		}
		Map<String, ItemType> itemMap = new HashMap<String, ItemType>();
		ReportUtil ru = new ReportUtil();
		ru.setMode(ReportUtil.TAB);
		StringBuffer tmpSB = new StringBuffer();
		tmpSB.append("ACTF id\t" + ru.getFirstLine() + FileUtils.LINE_SEP);
		for (IProblemItem i : ar) {
			tmpSB.append(i.getId() + "\t" + ru.toString(i) + FileUtils.LINE_SEP);
			String[] techS = i.getEvaluationItem().getTableDataTechniques()
					.split(",");
			for (String s : techS) {
				String tech = s.trim();
				ItemType itemType = itemMap.get(tech);
				if (itemType == null) {
					itemType = new ItemType(i.getSeverity());
					itemMap.put(tech, itemType);
				} else {
					itemType.setSeverity(i.getSeverity());
				}
			}
		}
		tmpSB.append(FileUtils.LINE_SEP);

		Set<String> keys = new TreeSet<String>(new Comparator<String>() {
			public int compare(String o1, String o2) {

				Pattern TECH_ID = Pattern.compile("(\\p{Alpha}+)(\\d+)");
				Matcher m1 = TECH_ID.matcher(o1);
				m1.matches();
				String prefix1 = m1.group(1);
				int number1 = Integer.parseInt(m1.group(2));
				Matcher m2 = TECH_ID.matcher(o2);
				m2.matches();
				String prefix2 = m2.group(1);
				int number2 = Integer.parseInt(m2.group(2));

				int flag = prefix1.compareTo(prefix2);
				if (flag == 0) {
					flag = Integer.valueOf(number1).compareTo(Integer.valueOf(number2));
				}
				return flag;
			}
		});

		keys.addAll(itemMap.keySet());

		for (String key : keys) {
			tmpSB.append(key + "\t" + itemMap.get(key) + FileUtils.LINE_SEP);
		}

		return tmpSB.toString();
	}

	public SortedSet<ITechniquesItem> getTechniquesItemSet() {
		if (techniquesItemSet == null)
			createTechSet();
		return techniquesItemSet;
	}

	private void createTechSet() {
		techniquesItemSet = new TreeSet<ITechniquesItem>();

		for (IEvaluationItem i : matchedCheckitemSet) {
			ProblemItemImpl pitem = new ProblemItemImpl(i.getId());
			ITechniquesItem[][] techs = pitem.getEvaluationItem()
					.getTechniques();
			for (int j = 0; j < techs.length; j++) {
				for (int j2 = 0; j2 < techs[j].length; j2++) {
					techniquesItemSet.add(techs[j][j2]);
				}
			}
		}
	}

	public Map<String, Set<IProblemItem>> getTechProbMap() {
		if (techniquesProblemMap == null)
			createTechProbMap();
		return techniquesProblemMap;
	}

	private void createTechProbMap() {
		techniquesProblemMap = new HashMap<String, Set<IProblemItem>>();

		for (IEvaluationItem i : matchedCheckitemSet) {
			ProblemItemImpl pitem = new ProblemItemImpl(i.getId());
			ITechniquesItem[][] techs = pitem.getEvaluationItem()
					.getTechniques();
			for (int j = 0; j < techs.length; j++) {
				for (int j2 = 0; j2 < techs[j].length; j2++) {
					ITechniquesItem tech = techs[j][j2];
					techniquesItemSet.add(tech);
					if (!techniquesProblemMap.containsKey(tech.getId())) {
						techniquesProblemMap.put(tech.getId(),
								new HashSet<IProblemItem>());
					}
					techniquesProblemMap.get(tech.getId()).add(pitem);
				}
			}
		}
	}
}
