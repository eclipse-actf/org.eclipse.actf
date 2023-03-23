/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.mediator;

import org.eclipse.ui.IViewPart;

/**
 * The interface for report viewers in ACTF. The implementation of the
 * IACTFReportViewer will receive the status change in ACTF as a MediatorEvent.
 * 
 */
public interface IACTFReportViewer extends IViewPart, IMediatorEventListener {

}
