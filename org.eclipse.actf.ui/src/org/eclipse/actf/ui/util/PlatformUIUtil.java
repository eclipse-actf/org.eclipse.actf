/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util;

import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

/**
 * Utility class to provide frequently used methods related to
 * {@link PlatformUI}
 */
public class PlatformUIUtil {

	/**
	 * Get active workbench window
	 * 
	 * @return current active workbench window, or null if not available
	 */
	public static IWorkbenchWindow getActiveWindow() {
		try {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get active workbench page
	 * 
	 * @return current active workbench page, or null if not available
	 */
	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow window = getActiveWindow();
		if (null != window) {
			try {
				return window.getActivePage();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Get Active Workbench Window's shell
	 * 
	 * @see IWorkbenchWindow
	 * @return Shell, or null if the shell has not been created yet or if the
	 *         window has been closed
	 */
	public static Shell getShell() {
		IWorkbenchWindow window = getActiveWindow();
		if (null != window) {
			try {
				return window.getShell();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Get active perspective ID
	 * 
	 * @return ID of current active perspective, or null if not available
	 */
	public static String getActivePerspectiveId() {
		try {
			return PlatformUIUtil.getActivePage().getPerspective().getId();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Show the specified perspective
	 * 
	 * @see IWorkbench
	 * @param perspectiveId
	 *            target perspective ID to show
	 * @return {@link IWorkbenchPage}, or null if not available
	 */
	public static IWorkbenchPage showPerspective(String perspectiveId) {
		IWorkbenchWindow window = PlatformUIUtil.getActiveWindow();
		if (null != window) {
			try {
				if (null != perspectiveId) {
					return PlatformUI.getWorkbench().showPerspective(
							perspectiveId, window);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Get active Editor
	 * 
	 * @return current active Editor, or null if not available
	 */
	public static IEditorPart getActiveEditor() {
		IWorkbenchPage activePage = getActivePage();
		if (null != activePage) {
			try {
				return activePage.getActiveEditor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Check if view is visible or not
	 * 
	 * @param viewId
	 *            target view ID to check the visibility
	 * @return true if the target view is visible
	 */
	public static boolean isViewVisible(String viewId) {
		IWorkbenchPage page = PlatformUIUtil.getActivePage();
		if (null != page) {
			IViewReference reference = page.findViewReference(viewId);
			if (null != reference) {
				return page.isPartVisible(reference.getPart(false));
			}
		}
		return false;
	}

	/**
	 * Show specified View
	 * 
	 * @param viewId
	 *            target view ID to show
	 * @return IViewPart, or null if not available
	 */
	public static IViewPart showView(String viewId) {
		if (null != viewId) {
			IWorkbenchPage page = PlatformUIUtil.getActivePage();
			if (null != page) {
				try {
					return (page.showView(viewId));
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Get global action handler
	 * 
	 * @param viewId
	 *            target view ID
	 * @param actionId
	 *            target action ID
	 * @return the global action handler of target view for the action with the
	 *         given id, or null if not available
	 */
	public static IAction getGlobalActionHandler(String viewId, String actionId) {
		IWorkbenchPage page = PlatformUIUtil.getActivePage();
		if (null != page) {
			try {
				IViewPart viewPart = page.findView(viewId);
				if (null != viewPart) {
					IWorkbenchPartSite site = viewPart.getSite();
					if (site instanceof IViewSite) {
						return ((IViewSite) site).getActionBars()
								.getGlobalActionHandler(actionId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Get Help System
	 * 
	 * @return IWorkbenchHelpSystem, or null if not available
	 */
	public static IWorkbenchHelpSystem getHelpSystem() {
		try {
			return (PlatformUI.getWorkbench().getHelpSystem());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Resolve the help resource href
	 * 
	 * @see IWorkbenchHelpSystem
	 * @param href
	 *            target URL
	 * @param documentOnly
	 *            if true, the resulting URL must point at the document
	 *            referenced by href. Otherwise, it can be a URL that contains
	 *            additional elements like navigation that the help system adds
	 *            to the document.
	 * @return the resolved URL, or null if no help UI is available
	 */
	public static URL resolveHelpURL(String href, boolean documentOnly) {
		IWorkbenchHelpSystem helpSystem = getHelpSystem();
		if (helpSystem != null) {
			return (helpSystem.resolve(href, documentOnly));
		}
		return null;
	}

	/**
	 * Get SharedImages for the workbench
	 * 
	 * @return ISharedImages, or null if not available
	 */
	public static ISharedImages getSharedImages() {
		try {
			return (PlatformUI.getWorkbench().getSharedImages());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get ImageDescriptor from Shared Images
	 * 
	 * @param symbolicName
	 *            the symbolic name of the image; there are constants declared
	 *            in this interface for build-in images that come with the
	 *            workbench
	 * @see ISharedImages
	 * @return ImageDescriptor, or null if not found
	 */
	public static ImageDescriptor getSharedImageDescriptor(String symbolicName) {
		try {
			return (PlatformUI.getWorkbench().getSharedImages()
					.getImageDescriptor(symbolicName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
