/*******************************************************************************
 * Copyright (c) 2005, 2013 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.ui.report.table;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.util.comparator.ChainComparator;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;

/**
 * Abstract implementation of viewer sorter. It provides useful
 * {@link Comparator} to implement viewer sorter for general accessibility
 * issues
 * 
 * @see ChainComparator
 * @see IResultTableSorter
 * @see IProblemItem
 */
public abstract class ResultTableSorterBase extends ViewerSorter implements
		IResultTableSorter {

	private Pattern pattern = Pattern.compile("\\d+");

	/**
	 * Sort {@link IProblemItem} based on it's score
	 */
	public class ScoreCompator extends ChainComparator {

		private int pos;

		public ScoreCompator(int pos) {
			this.pos = pos;
		}

		protected int resolve(Object o1, Object o2) {
			return (compareScore((IProblemItem) o1, (IProblemItem) o2, this.pos));
		}

	}

	protected int compareScore(IProblemItem item1, IProblemItem item2, int pos) {
		int[] score1 = item1.getMetricsScores();
		int[] score2 = item2.getMetricsScores();
		try {
			int result = score2[pos] - score1[pos];
			if (result == 0) {
				Image[] icon1 = item1.getMetricsIcons();
				Image[] icon2 = item2.getMetricsIcons();
				if (icon1[pos] == null && icon2[pos] != null) {
					return (1);
				} else if (icon1[pos] != null && icon2[pos] == null) {
					return (-1);
				}
			}
			return (result);
		} catch (Exception e) {
			return (0);
		}
	}

	/**
	 * Sort {@link IProblemItem} based on it's severity
	 */
	public class SeverityComparator extends ChainComparator {
		protected int resolve(Object o1, Object o2) {
			return (compareSeverity((IProblemItem) o1, (IProblemItem) o2));
		}
	}

	protected int compareSeverity(IProblemItem item1, IProblemItem item2) {
		return (item1.getSeverity() - item2.getSeverity());
	}

	/**
	 * Sort {@link IProblemItem} based on it's corresponding guideline
	 */
	public class GuidelineComparator extends ChainComparator {
		private int pos;

		public GuidelineComparator(int pos) {
			this.pos = pos;
		}

		protected int resolve(Object o1, Object o2) {
			return (compareGuideline((IProblemItem) o1, (IProblemItem) o2,
					this.pos));
		}
	}

	// TODO levels -> itemName
	protected int compareGuideline(IProblemItem item1, IProblemItem item2,
			int pos) {
		String guide1 = item1.getTableDataGuideline()[pos];
		String guide2 = item2.getTableDataGuideline()[pos];

		return (compareString(guide1, guide2));
	}

	/**
	 * Sort {@link IProblemItem} based on it's corresponding line number
	 */
	public class LinesComparator extends ChainComparator {
		protected int resolve(Object o1, Object o2) {
			return (compareLine((IProblemItem) o1, (IProblemItem) o2));
		}
	}

	protected int compareLine(IProblemItem tmp1, IProblemItem tmp2) {
		int line1 = tmp1.getLine();
		int line2 = tmp2.getLine();

		if (line1 < 0 && line2 > -1) {
			return (1);
		} else if (line1 > -1 && line2 < 0) {
			return (-1);
		} else if (line1 == 0 && line2 == 0) {
			return (0);
		}

		int result = tmp1.getLine() - tmp2.getLine();
		if (result == 0) {
			return (tmp1.getLineStrMulti().compareTo(tmp2.getLineStrMulti()));
		}
		return (result);
	}

	/**
	 * Sort items as String order
	 */
	public class StringComparator extends ChainComparator {
		protected int resolve(Object o1, Object o2) {
			String target1 = (String) o1;
			String target2 = (String) o2;

			if (target1.length() == 0 && target2.length() != 0) {
				return (1);
			} else if (target1.length() != 0 && target2.length() == 0) {
				return (-1);
			}
			return (target1.compareTo(target2));
		}
	}

	/**
	 * Sort {@link IProblemItem} based on it's description
	 */
	public class DescriptionComparator extends StringComparator {
		protected int resolve(Object o1, Object o2) {
			IProblemItem item1 = (IProblemItem) o1;
			IProblemItem item2 = (IProblemItem) o2;
			return super
					.resolve(item1.getDescription(), item2.getDescription());
		}
	}

	/**
	 * Sort {@link IProblemItem} based on it's error ID
	 */
	public class ErrorIDComparator extends StringComparator {
		protected int resolve(Object o1, Object o2) {
			IProblemItem item1 = (IProblemItem) o1;
			IProblemItem item2 = (IProblemItem) o2;
			String id1 = item1.getId();
			String id2 = item2.getId();
			String[] id1tokens = id1.split("_"); //$NON-NLS-1$
			String[] id2tokens = id2.split("_"); //$NON-NLS-1$
			if (id1tokens.length < 2 || id2tokens.length < 2)
				return super.resolve(item1.getId(), item2.getId());
			else {
				int ret = super.resolve(id1tokens[0], id2tokens[0]);
				if (ret != 0)
					return ret;
				else {
					Double d1 = new Double(id1tokens[1]);
					Double d2 = new Double(id2tokens[1]);
					return d1.compareTo(d2);
				}
			}
		}
	}

	protected int compareString(String target1, String target2) {
		if (target1.length() == 0 && target2.length() != 0) {
			return (1);
		} else if (target1.length() != 0 && target2.length() == 0) {
			return (-1);
		}
		return (target1.compareTo(target2));
	}

	private int compareEvalItem(String arg1, String arg2) {
		if (arg1.length() == 0) {
			if (arg2.length() > 0) {
				return 1;
			}
			return 0;
		} else if (arg2.length() == 0) {
			return -1;
		}

		int result;
		String str1, str2;
		int num1, num2;

		Matcher matcher1 = pattern.matcher(arg1);
		Matcher matcher2 = pattern.matcher(arg2);

		if (matcher1.find()) {
			str1 = arg1.substring(0, matcher1.start());
			num1 = Integer.parseInt(matcher1.group());
		} else {
			str1 = arg1;
			num1 = Integer.MIN_VALUE;
		}

		if (matcher2.find()) {
			str2 = arg2.substring(0, matcher2.start());
			num2 = Integer.parseInt(matcher2.group());
		} else {
			str2 = arg2;
			num2 = Integer.MIN_VALUE;
		}

		result = str1.compareTo(str2);
		if (result != 0)
			return result;
		//TODO Java 1.7 or later
		//result = Integer.compare(num1, num2);
		result = Integer.valueOf(num1).compareTo(num2);
		if (result != 0)
			return result;
		return compareEvalItem(arg1.substring(matcher1.end()),
				arg2.substring(matcher2.end()));

	}

	protected int compareEvalItem(IEvaluationItem target1,
			IEvaluationItem target2) {
		return compareEvalItem(target1.getTableDataTechniques(),
				target2.getTableDataTechniques());
	}

}
