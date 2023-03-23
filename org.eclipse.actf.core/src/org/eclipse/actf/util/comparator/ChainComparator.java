/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shin SAITO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.util.comparator;

import java.util.Comparator;

/**
 * Abstract class for implementing Comparator chain.
 */
public abstract class ChainComparator implements Comparator<Object> {
	private static final int DEFVAL = 0;

	private ChainComparator next;

	/**
	 * Set next Comparator.
	 * 
	 * @param next
	 *            the next Comparator
	 * @return the next Comparator
	 */
	public ChainComparator setNext(ChainComparator next) {
		this.next = next;
		return next;
	}

	/**
	 * Return comparison result
	 * 
	 * @param i1
	 *            the target integer value 1
	 * @param i2
	 *            the target integer value 2
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 * 
	 */
	public final int intResolve(int i1, int i2) {
		Integer int1 = new Integer(i1);
		Integer int2 = new Integer(i2);
		return int1.compareTo(int2);
	}

	/**
	 * Implementations have to implement this method.
	 * 
	 * @param o1
	 *            the target Object 1
	 * @param o2
	 *            the target Object 2
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 */
	protected abstract int resolve(Object o1, Object o2);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		int res = resolve(o1, o2);
		if (res != 0)
			return res;
		else if (next == null)
			return DEFVAL;
		else
			return next.compare(o1, o2);
	}
}
