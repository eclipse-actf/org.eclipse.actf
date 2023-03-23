/*******************************************************************************
 * Copyright (c) 2010, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.eval;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.visualization.eval.ITechniquesItem;

public class TechniquesItemImpl implements ITechniquesItem {

	/**
	 * parse ID such as "SCR24"
	 */
	private static final Pattern TECH_ID = Pattern
			.compile("(\\p{Alpha}+)(\\d+)");

	private static final List<String> prefixArray = Arrays.asList("G", "H",
			"C", "SCR", "SVR", "SM", "T", "ARIA", "FLASH", "F");
	private String guideline = "";
	private String id = "";
	private String url = "";

	public String getGuidelineName() {
		return guideline;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setGuidelineName(String guideline) {
		this.guideline = guideline;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int compareTo(ITechniquesItem o) {
		Matcher m1 = TECH_ID.matcher(this.getId());
		m1.matches();
		String prefix1 = m1.group(1);
		int number1 = Integer.parseInt(m1.group(2));
		int i1 = prefixArray.indexOf(prefix1);
		if (i1 < 0) {
			System.err.println("Unknown techniques prefix: " + prefix1);
			i1 = 999;
		}
		Matcher m2 = TECH_ID.matcher(o.getId());
		m2.matches();
		String prefix2 = m2.group(1);
		int number2 = Integer.parseInt(m2.group(2));
		int i2 = prefixArray.indexOf(prefix2);
		if (i2 < 0) {
			System.err.println("Unknown techniques prefix: " + prefix2);
			i2 = 999;
		}

		int flag = Integer.valueOf(i1).compareTo(Integer.valueOf(i2));
		if (flag == 0) {
			flag = Integer.valueOf(number1).compareTo(Integer.valueOf(number2));
		}
		return flag;
	}
}
