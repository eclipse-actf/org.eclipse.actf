/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.eval.guideline;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.ITechniquesItem;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineData;
import org.eclipse.swt.graphics.Image;

public class GuidelineData implements IGuidelineData {

	private String guidelineName;

	private int id; // define sort order

	// TODO nest
	private String levelStr = null;

	private String[] levels;

	private String[] mimetypes;

	private String description;

	private String category;

	private boolean isEnabled = false;

	private GuidelineData[] subLevelDataArray = new GuidelineData[0];

	private boolean[] hasMetrics;

	private HashSet<IEvaluationItem> checkItemSet = new HashSet<IEvaluationItem>();

	private Map<String, IGuidelineItem> guidelineItemMap = new HashMap<String, IGuidelineItem>();

	private Map<String, ITechniquesItem> techniquesItemMap = new HashMap<String, ITechniquesItem>();
	
	private String currentMIMEtype = "text/html"; //$NON-NLS-1$

	/**
	 * @param guidelineName
	 * @param levels
	 * @param guidelineItemMap
	 * 
	 * @deprecated 
	 */
	public GuidelineData(String guidelineName, int id, String category,
			String description, String[] levels, String[] categories,
			String[] descriptions, String[] mimetypes,
			Map<String, IGuidelineItem> guidelineItemMap) {
		this.guidelineName = guidelineName;
		this.guidelineItemMap = guidelineItemMap;
		this.levels = levels;
		this.mimetypes = mimetypes;
		this.id = id;
		this.category = category;
		this.description = description;

		subLevelDataArray = new GuidelineData[levels.length];
		for (int i = 0; i < levels.length; i++) {
			subLevelDataArray[i] = new GuidelineData(guidelineName, id,
					levels[i], categories[i], descriptions[i], mimetypes,
					guidelineItemMap);
		}

		// for(int i=0;i<levels.length;i++){
		// System.out.println(guidelineName+"("+levels[i]+"):"+guidelineItems[i].size());
		// }
	}
	
	/**
	 * @param guidelineName
	 * @param levels
	 * @param guidelineItemMap
	 */
	public GuidelineData(String guidelineName, int id, String category,
			String description, String[] levels, String[] categories,
			String[] descriptions, String[] mimetypes,
			Map<String, IGuidelineItem> guidelineItemMap, Map<String, ITechniquesItem> techniquesItemMap) {
		this.guidelineName = guidelineName;
		this.guidelineItemMap = guidelineItemMap;
		this.techniquesItemMap = techniquesItemMap;
		this.levels = levels;
		this.mimetypes = mimetypes;
		this.id = id;
		this.category = category;
		this.description = description;

		subLevelDataArray = new GuidelineData[levels.length];
		for (int i = 0; i < levels.length; i++) {
			subLevelDataArray[i] = new GuidelineData(guidelineName, id,
					levels[i], categories[i], descriptions[i], mimetypes,
					guidelineItemMap);
		}

		// for(int i=0;i<levels.length;i++){
		// System.out.println(guidelineName+"("+levels[i]+"):"+guidelineItems[i].size());
		// }
	}

	private GuidelineData(String guidelineName, int id, String levelStr,
			String categoryStr, String descriptionStr, String[] mimetypes,
			Map<String, IGuidelineItem> guidelineItemMap) {
		this.guidelineName = guidelineName;
		this.id = id;
		this.mimetypes = mimetypes;
		this.category = categoryStr;
		this.description = descriptionStr;

		// TODO
		if (levelStr == null) {
			this.levelStr = ""; //$NON-NLS-1$
		} else {
			this.levelStr = levelStr;
		}

		for (IGuidelineItem item : guidelineItemMap.values()) {
			if (item.getLevel().equals(levelStr)) {
				this.guidelineItemMap.put(item.getId(), item);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getGuidelineItem(java.lang.String)
	 */
	public IGuidelineItem getGuidelineItem(String id) {
		return guidelineItemMap.get(id);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getGuidelineItemMap()
	 */
	public Map<String, IGuidelineItem> getGuidelineItemMap() {
		return guidelineItemMap;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getGuidelineName()
	 */
	public String getGuidelineName() {
		return guidelineName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getId()
	 */
	public int getId() {
		return id;
	}

	// TODO
	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getLevels()
	 */
	public String[] getLevels() {
		return levels;
	}

	// TODO
	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#hasLevel()
	 */
	public boolean hasLevel() {
		return (0 != levels.length);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getMIMEtypes()
	 */
	public String[] getMIMEtypes() {
		return mimetypes;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#isEnabled()
	 */
	public boolean isEnabled() {
		if (subLevelDataArray.length > 0) {
			for (int i = 0; i < subLevelDataArray.length; i++) {
				if (subLevelDataArray[i].isEnabled()) {
					return true;
				}
			}
			return false;
		}

		return isEnabled;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#isMatched()
	 */
	public boolean isMatched() {
		return (isEnabled() && isTargetMIMEtype(currentMIMEtype));
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		for(IGuidelineItem i : guidelineItemMap.values()){
			i.setEnabled(isEnabled);
		}
		//TODO check sublevel
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getSubLevelData(java.lang.String)
	 */
	public GuidelineData getSubLevelData(String levelStr) {
		for (int i = 0; i < subLevelDataArray.length; i++) {
			if (subLevelDataArray[i].getLevelStr().equalsIgnoreCase(levelStr)) {
				return (subLevelDataArray[i]);
			}
		}
		return (null);
	}

	public void setEvaluationItems(
			Collection<IEvaluationItem> checkItemCollection, String[] metrics) {
		hasMetrics = new boolean[metrics.length];
		Arrays.fill(hasMetrics, false);

		for (IEvaluationItem item : checkItemCollection) {
			IGuidelineItem[] guidelineArray = item.getGuidelines();
			for (int j = 0; j < guidelineArray.length; j++) {
				if (this.guidelineName.equals(guidelineArray[j]
						.getGuidelineName())) {
					if (levelStr == null
							|| levelStr.equals(guidelineArray[j].getLevel())) {
						Image[] icons = item.getMetricsIcons();
						for (int l = 0; l < metrics.length; l++) {
							if (icons[l] != null) {
								hasMetrics[l] = true;
							}
						}
						checkItemSet.add(item);
					}
				}
			}
		}

		for (int i = 0; i < subLevelDataArray.length; i++) {
			subLevelDataArray[i].setEvaluationItems(checkItemCollection,
					metrics);
		}

		// System.out.println(guidelineName + " " + levelStr + ":" +
		// checkItemSet.size());
		// for (int i = 0; i < metrics.length; i++) {
		// System.out.println(guidelineName + " " + levelStr + ":" + metrics[i]
		// + "\t" + hasMetrics[i]);
		// }

	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getLevelStr()
	 */
	public String getLevelStr() {
		return levelStr;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getEvaluationItemSet()
	 */
	public HashSet<IEvaluationItem> getEvaluationItemSet() {
		return checkItemSet;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getCorrespondingMetrics()
	 */
	public boolean[] getCorrespondingMetrics() {
		return hasMetrics;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#isTargetMIMEtype(java.lang.String)
	 */
	public boolean isTargetMIMEtype(String mimetype) {
		for (int i = 0; i < mimetypes.length; i++) {
			if (mimetypes[i].equals(mimetype)) {
				return (true);
			}
		}
		return (false);
	}

	public void setCurrentMIMEtype(String currentMIMEtype) {
		this.currentMIMEtype = currentMIMEtype;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getCategory()
	 */
	public String getCategory() {
		return category;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.actf.visualization.eval.guideline.IGuidelineData#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	public ITechniquesItem getTequniquesItem(String id) {
		return techniquesItemMap.get(id);
	}
}
