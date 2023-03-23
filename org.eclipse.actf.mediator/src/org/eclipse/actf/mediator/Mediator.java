/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.mediator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.actf.ui.util.AbstractPartListener;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * A <code>Mediator</code> manages components and dataflows in the ACTF by
 * collaborating with the Eclipse framework. If <code>Mediator</code> detects a
 * status change in ACTF components, it will send <code>MediatorEvent</code> to
 * ACTF components and other registered EventListeners.
 * 
 */
public class Mediator {

	private static Mediator instance = new Mediator();

	private IACTFReportGenerator curRepoter;

	private IModelServiceHolder currentModelServiceHolder;

	// TODO model/vizview combination to store the result
	private HashMap<IACTFReportGenerator, IACTFReport> reportMap = new HashMap<IACTFReportGenerator, IACTFReport>();

	private HashSet<IWorkbenchPage> pageSet = new HashSet<IWorkbenchPage>();

	private Set<IMediatorEventListener> mediatorEventLisnterSet = Collections
			.synchronizedSet(new HashSet<IMediatorEventListener>());

	/**
	 * Gets Mediator instance.
	 * 
	 * @return Mediator
	 */
	public static Mediator getInstance() {
		return instance;
	}

	private Mediator() {
		init();
	}

	/**
	 * Sets the report to Mediator. Other ACTF components will receive the
	 * <code>MediatorEvent</code> (report changed).
	 * 
	 * @param generator
	 *            generator of the report
	 * @param report
	 *            new report to set
	 */
	public void setReport(IACTFReportGenerator generator, IACTFReport report) {
		reportMap.put(generator, report);
		reportChanged(new MediatorEvent(this, currentModelServiceHolder,
				generator, report));
	}

	/**
	 * Gets current report submitted from the report generator.
	 * 
	 * @param generator
	 *            target report generator
	 * @return current report submitted from the report generator
	 */
	public IACTFReport getReport(IACTFReportGenerator generator) {
		return getEvaluationResult(null, generator);
	}

	private IACTFReport getEvaluationResult(IModelServiceHolder holder,
			IACTFReportGenerator reporter) {
		if (reportMap.containsKey(reporter)) {
			return reportMap.get(reporter);
		}
		return null;
		// return (new EvaluationResultImpl());
	}

	private void initPage(IWorkbenchPage page) {
		if (pageSet.add(page)) {
			IViewReference[] views = page.getViewReferences();
			IViewPart tmpViewPart;
			for (int i = 0; i < views.length; i++) {
				if ((tmpViewPart = views[i].getView(false)) != null) {
					if (tmpViewPart instanceof IMediatorEventListener) {
						addMediatorEventListener((IMediatorEventListener) tmpViewPart);
					}
				}
			}

			page.addPartListener(new AbstractPartListener() {

				public void partActivated(IWorkbenchPartReference partRef) {
					IWorkbenchPart part = partRef.getPart(false);
					if (part instanceof IACTFReportGenerator
							&& part != curRepoter) {
						curRepoter = (IACTFReportGenerator) part;
						reporterViewChanged(new MediatorEvent(Mediator.this,
								currentModelServiceHolder, curRepoter,
								getReport(curRepoter)));
					}
					if (part instanceof IModelServiceHolder
							&& part != currentModelServiceHolder) {
						currentModelServiceHolder = (IModelServiceHolder) part;
						modelserviceChanged(new MediatorEvent(Mediator.this,
								currentModelServiceHolder, curRepoter,
								getReport(curRepoter)));
					}
				}

				@Override
				public void partClosed(IWorkbenchPartReference partRef) {
					IWorkbenchPart part = partRef.getPart(false);
					if (part instanceof IMediatorEventListener) {
						removeMediatorEventListener((IMediatorEventListener) part);
					}
				}

				@Override
				public void partOpened(IWorkbenchPartReference partRef) {
					IWorkbenchPart part = partRef.getPart(false);
					if (part instanceof IACTFReportViewer) {
						IACTFReportViewer viewer = (IACTFReportViewer) part;
						if (currentModelServiceHolder != null) {
							viewer.reportChanged(new MediatorEvent(
									Mediator.this, currentModelServiceHolder,
									curRepoter, getReport(curRepoter)));
						}
						addMediatorEventListener(viewer);
					} else if (part instanceof IMediatorEventListener) {
						addMediatorEventListener((IMediatorEventListener) part);
					}
				}

				// TODO Call InputChanged when the target URL of the Editor
				// changes
				@Override
				public void partInputChanged(IWorkbenchPartReference partRef) {
					IWorkbenchPart part = partRef.getPart(false);
					if (part instanceof IModelServiceHolder) {
						currentModelServiceHolder = (IModelServiceHolder) part;
						modelserviceInputChanged(new MediatorEvent(
								Mediator.this, currentModelServiceHolder,
								curRepoter, getReport(curRepoter)));
					}
				}

			});

		}
	}

	private void init() {
		IWorkbenchWindow activeWindow = PlatformUIUtil.getActiveWindow();

		activeWindow.addPageListener(new IPageListener() {

			public void pageActivated(IWorkbenchPage page) {
			}

			public void pageClosed(IWorkbenchPage page) {
				pageSet.remove(page);
			}

			public void pageOpened(IWorkbenchPage page) {
				initPage(page);
			}

		});
		IWorkbenchPage activePage = PlatformUIUtil.getActivePage();
		if (activePage != null) {
			initPage(activePage);
		}

	}

	/**
	 * Registers the <code>IMediatorEventListener</code> to the Mediator.
	 * 
	 * @param listener
	 *            the listener to register
	 */
	public synchronized void addMediatorEventListener(
			IMediatorEventListener listener) {
		mediatorEventLisnterSet.add(listener);
	}

	/**
	 * Removes the <code>IMediatorEventListener</code> from the Mediator.
	 * 
	 * @param listener
	 *            the listener to remove
	 * @return true if the listener is removed from the Mediator
	 */
	public synchronized boolean removeMediatorEventListener(
			IMediatorEventListener listener) {
		return mediatorEventLisnterSet.remove(listener);
	}

	private synchronized void modelserviceChanged(MediatorEvent event) {
		for (IMediatorEventListener i : mediatorEventLisnterSet
				.toArray(new IMediatorEventListener[mediatorEventLisnterSet
						.size()])) {
			i.modelserviceChanged(event);
		}
	}

	private synchronized void modelserviceInputChanged(MediatorEvent event) {
		for (IMediatorEventListener i : mediatorEventLisnterSet
				.toArray(new IMediatorEventListener[mediatorEventLisnterSet
						.size()])) {
			i.modelserviceInputChanged(event);
		}
	}

	private synchronized void reportChanged(MediatorEvent event) {
		for (IMediatorEventListener i : mediatorEventLisnterSet
				.toArray(new IMediatorEventListener[mediatorEventLisnterSet
						.size()])) {
			i.reportChanged(event);
		}
	}

	private synchronized void reporterViewChanged(MediatorEvent event) {
		for (IMediatorEventListener i : mediatorEventLisnterSet
				.toArray(new IMediatorEventListener[mediatorEventLisnterSet
						.size()])) {
			i.reportGeneratorChanged(event);
		}
	}

}
