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

import java.util.EventObject;

import org.eclipse.actf.model.ui.IModelServiceHolder;

/**
 * A <code>MediatorEvent</code> is sent by a {@link Mediator} to
 * {@link IMediatorEventListener}s when the <code>Mediator</code>
 * detects a status change in ACTF components. This notification typically 
 * occurs when the user activates an ACTF Model Service (usually Editor that
 * implements <code>IModelService</code>) or an ACTF Reporter Views 
 * (implements <code>IACTFReporterView</code> that generates an ACTF Report).
 * This event is also notified when the report 
 * (implements <code>IACTFReport</code>) was changed.
 * 
 */
public class MediatorEvent extends EventObject {

	private static final long serialVersionUID = -6308860434213716554L;

	private IModelServiceHolder modelServiceHolder;
	private IACTFReportGenerator view;
	private IACTFReport report;
		
	MediatorEvent(Mediator source, IModelServiceHolder modelServiceHolder, IACTFReportGenerator view, IACTFReport report) {
		super(source);
		this.modelServiceHolder = modelServiceHolder;
		this.view = view;
		this.report = report;
	}

	/**
	 * Returns the ModelServiceHolder that is related to this event.
	 * 
	 * @return the <code>IModelServiceHolder</code> related to this event
	 */
	public IModelServiceHolder getModelServiceHolder() {
		return modelServiceHolder;
	}

	/**
	 * Returns the ACTFReportGenerator that is related to this event.
	 * 
	 * @return the <code>IACTFReportGenerator</code> related to this event
	 */
	public IACTFReportGenerator getView() {
		return view;
	}

	/**
	 * Returns the ACTFReport that is related to this event.
	 * 
	 * @return the <code>IACTFReport</code> related to this event
	 */
	public IACTFReport getReport() {
		return report;
	}	

}
