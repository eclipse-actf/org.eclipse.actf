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

import java.util.EventListener;

/**
 * This listener interface may be implemented in order to receive a
 * {@link MediatorEvent} notification when a {@link Mediator} detects a status
 * change in ACTF components.
 * 
 * @see Mediator#addMediatorEventListener(IMediatorEventListener)
 * @see Mediator#removeMediatorEventListener(IMediatorEventListener)
 * 
 */
public interface IMediatorEventListener extends EventListener {

	/**
	 * This method is called when the report ({@link IACTFReport}) was
	 * submitted to the {@link Mediator}
	 * 
	 * @param event
	 *            The {@link MediatorEvent} contains information about the
	 *            event.
	 */
	public abstract void reportChanged(MediatorEvent event);

	/**
	 * This method is called when a report generator ({@link IACTFReportGenerator})
	 * was selected by user.
	 * 
	 * @param event
	 *            The {@link MediatorEvent} contains information about the
	 *            event.
	 */
	public abstract void reportGeneratorChanged(MediatorEvent event);

	/**
	 * This method is called when a model service was selected by user.
	 * 
	 * @param event
	 *            The {@link MediatorEvent} contains information about the
	 *            event.
	 */
	public abstract void modelserviceChanged(MediatorEvent event);

	/**
	 * This method is called when an input change event was occured in the
	 * active model service.
	 * 
	 * @param event
	 *            The {@link MediatorEvent} contains information about the
	 *            event.
	 */
	public abstract void modelserviceInputChanged(MediatorEvent event);

}