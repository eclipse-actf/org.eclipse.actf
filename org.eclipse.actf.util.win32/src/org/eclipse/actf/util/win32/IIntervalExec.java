/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.util.win32;


/**
 * IIntervalExec interface defines a method to be implemented by the object which will be
 * executed in certain interval. It has the same function of {@link Runnable}, but the
 * exec() method has a return value. 
 */
public interface IIntervalExec {
    /**
     * @return the next interval time in milliseconds.
     */
    public int exec();
}
