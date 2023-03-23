/*******************************************************************************
 * Copyright (c) 2010, 2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.examples.michecker.smil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing Timecount values in SMIL 2.x
 * 
 */
public class ClockValueParser {
	private static final String TIME_COUNT_STRING = "(\\d+(\\.\\d+)?)(h|min|s|ms)?";
	private static final Pattern TIME_COUNT = Pattern
			.compile(TIME_COUNT_STRING);
	private static final Pattern OFFSET_PREFIX = Pattern
			.compile("(\\+|-)?(.*)");
	private static final String FULL_CLOCK_STRING = "(\\d+):(\\d+):(\\d+)(\\.(\\d+))?";
	private static final Pattern FULL_CLOCK = Pattern
			.compile(FULL_CLOCK_STRING);

	public static double parseOffset(String timeCount) {
		Matcher m = OFFSET_PREFIX.matcher(timeCount);
		boolean b = m.matches();
		// System.err.println(b);
		if (!b)
			return -9999.0;
		if (m.group(1) == null) { // no sign symbol
			return parseDuration(m.group(2));
		} else if (m.group(1).equals("+")) { // plus sign
			return parseDuration(m.group(2));
		} else { // minus sign
			return parseDuration(m.group(2)) * -1.0;
		}
	}

	public static double parseDuration(String duration) {
		if (duration == null)
			return 0.0;
		duration = duration.trim();
		if (duration.length() == 0)
			return 0.0;
		Matcher m = TIME_COUNT.matcher(duration);
		if (m.matches())
			return parseTimeCount(m);

		m = FULL_CLOCK.matcher(duration);
		if (m.matches())
			return parseFullClockValue(m);
		// parsing failed
		return 0.0;
	}

	private static double parseTimeCount(Matcher m) {
		if (m.group(3) == null) {
			// assume "s"
			return Double.parseDouble(m.group(1));
		} else if (m.group(3).equals("h")) {
			return Double.parseDouble(m.group(1)) * 3600.0;
		} else if (m.group(3).equals("min")) {
			return Double.parseDouble(m.group(1)) * 60.0;
		} else if (m.group(3).equals("s")) {
			return Double.parseDouble(m.group(1));
		} else if (m.group(3).equals("ms")) {
			return Double.parseDouble(m.group(1)) / 1000.0;
		} else
			return 0;
	}

	private static double parseFullClockValue(Matcher m) {
		double time = 0.0;
		time += Integer.parseInt(m.group(1)) * 3600.0;
		time += Integer.parseInt(m.group(2)) * 60.0;
		time += Integer.parseInt(m.group(3));
		if (m.group(5) != null) {
			time += Double.parseDouble("0." + m.group(5));
		}
		return time;
	}

	public static void main(String[] args) {
		System.out.println(parseOffset("1.2h"));
		System.out.println(parseOffset("-12min"));
		System.out.println(parseOffset("-102.4s"));
		System.out.println(parseOffset("+320ms"));
		System.out.println(parseOffset("25"));

		System.out.println("-- timecount --");
		System.out.println(parseDuration("1.2h"));
		System.out.println(parseDuration("12min"));
		System.out.println(parseDuration("102.4s"));
		System.out.println(parseDuration("320ms"));
		System.out.println(parseDuration("25"));

		System.out.println("-- full-clock --");
		System.out.println(parseDuration("0:01:45"));
		System.out.println(parseDuration("0:01:45.1"));
		System.out.println(parseDuration("0:01:45.12"));
		System.out.println(parseDuration("0:01:45.123"));
		System.out.println(parseDuration("0:01:45.1234"));
	}
}
