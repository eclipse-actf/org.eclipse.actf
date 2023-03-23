/*******************************************************************************
 * Copyright (c) 2007, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.model.ui.util;

import java.util.ArrayList;

import org.eclipse.actf.model.internal.ui.WebBrowserUtilForACTF;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.actf.model.ui.editor.DummyEditorInput;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Utility class to manage ACTF model services (implementation of
 * {@link IModelService})
 */
public class ModelServiceUtils {

	private static class EditorNotFoundException extends Exception {
		private static final long serialVersionUID = -5760127077107164112L;

	}

	/**
	 * Find and launch an Editor associated with the target URL
	 * 
	 * @param targetUrl
	 *            target URL
	 * @return {@link IEditorPart} implements {@link IModelServiceHolder}, or
	 *         null if not available
	 */
	public static IEditorPart launch(String targetUrl) {
		IWorkbenchPage activePage = PlatformUIUtil.getActivePage();
		if (activePage == null) {
			return null;
		}

		if (targetUrl != null && targetUrl.length() != 0) {
			targetUrl = targetUrl.trim();

			IEditorReference[] editorRefs = activePage.getEditorReferences();
			for (int i = 0; i < editorRefs.length; i++) {
				IEditorPart part = editorRefs[i].getEditor(false);
				if (part instanceof IModelServiceHolder) {
					IModelService modelService = ((IModelServiceHolder) part).getModelService();
					if (targetUrl.equals(modelService.getURL())) {
						activePage.activate(part);
						modelService.open(targetUrl);
						return part;
					}
				}
			}

			try {
				return (launch(targetUrl, getEditorId(targetUrl)));
			} catch (EditorNotFoundException e) {
				System.err.println("Editor not found: " + targetUrl); //$NON-NLS-1$
			}
		}
		return null;
	}

	/**
	 * Find and launch a new Editor associated with the target URL
	 * 
	 * @param targetUrl
	 *            target URL
	 * @return {@link IEditorPart} implements {@link IModelServiceHolder}, or
	 *         null if not available
	 */
	public static IEditorPart launchNew(String targetUrl) {
		IWorkbenchPage activePage = PlatformUIUtil.getActivePage();
		if (activePage == null) {
			return null;
		}

		if (targetUrl != null && targetUrl.length() != 0) {
			targetUrl = targetUrl.trim();

			try {
				return (launchNew(targetUrl, getEditorId(targetUrl)));
			} catch (EditorNotFoundException e) {
				System.err.println("Editor not found: " + targetUrl); //$NON-NLS-1$
			}
		}
		return null;
	}

	
	private static boolean isModelService(IEditorDescriptor desc) {
		if (desc == null) {
			return false;
		}
		return desc.getId().indexOf("actf.model.ui") >= 0; //$NON-NLS-1$
	}

	private static String getEditorId(String targetUrl) throws EditorNotFoundException {
		// TODO support multiple editor, dialog, preference
		IEditorRegistry editors = PlatformUI.getWorkbench().getEditorRegistry();
		IEditorDescriptor[] candidates;
		IEditorDescriptor editor;

		if (!targetUrl.startsWith("http://") //$NON-NLS-1$
				&& !targetUrl.startsWith("https://")) { //$NON-NLS-1$
			editor = editors.getDefaultEditor(targetUrl);
			if (isModelService(editor)) {
				return editor.getId();
			} else {
				candidates = editors.getEditors(targetUrl);
				for (IEditorDescriptor desc : candidates) {
					if (isModelService(desc)) {
						return desc.getId();
					}
				}
			}
		}
		editor = editors.getDefaultEditor("default.html"); //$NON-NLS-1$
		if (isModelService(editor)) {
			return editor.getId();
		} else {
			candidates = editors.getEditors("default.html"); //$NON-NLS-1$
			for (IEditorDescriptor desc : candidates) {
				if (isModelService(desc)) {
					return desc.getId();
				}
			}
		}
		throw new EditorNotFoundException();
	}

	/**
	 * Launch an Editor
	 * 
	 * @param targetUrl
	 *            target URL
	 * @param id
	 *            ID of target Editor
	 * @return {@link IEditorPart}, or null if not available
	 */
	public static IEditorPart launch(String targetUrl, String id) {

		IWorkbenchPage activePage = PlatformUIUtil.getActivePage();
		if (activePage == null) {
			return null;
		}
		if (targetUrl != null) {
			targetUrl = targetUrl.trim();
		} else {
			targetUrl = ""; //$NON-NLS-1$
		}

		IEditorPart blankEditorPart = getBlankBrowserEditorPart(id);
		if (blankEditorPart != null) {
			activePage.activate(blankEditorPart);
			if (targetUrl != null) {
				((IModelServiceHolder) blankEditorPart).getModelService().open(targetUrl);
				return blankEditorPart;
			}
		} else {
			IEditorReference[] editorRefs = activePage.getEditorReferences();
			for (int i = 0; i < editorRefs.length; i++) {
				if (editorRefs[i].getId().equals(id)) {
					IWorkbenchPart part = editorRefs[i].getPart(false);
					if (part instanceof IModelServiceHolder) {
						IModelService modelService = ((IModelServiceHolder) part).getModelService();
						if (targetUrl.equals(modelService.getURL())) {
							activePage.activate(part);
							return (IEditorPart) part;
						}
					}
				}
			}

			try {
				String editorName = ""; //$NON-NLS-1$
				IEditorRegistry editors = PlatformUI.getWorkbench().getEditorRegistry();
				IEditorDescriptor editorDesc = editors.findEditor(id);
				if (editorDesc != null) {
					editorName = editorDesc.getLabel();
					return activePage.openEditor(new DummyEditorInput(targetUrl, editorName), id);
				} else {
					System.err.println("Editor not found: " + id); //$NON-NLS-1$
				}
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * Launch a new Editor
	 * 
	 * @param targetUrl
	 *            target URL
	 * @param id
	 *            ID of target Editor
	 * @return {@link IEditorPart}, or null if not available
	 */
	public static IEditorPart launchNew(String targetUrl, String id) {

		IWorkbenchPage activePage = PlatformUIUtil.getActivePage();
		if (activePage == null) {
			return null;
		}
		if (targetUrl != null) {
			targetUrl = targetUrl.trim();
		} else {
			targetUrl = ""; //$NON-NLS-1$
		}

		try {
			String editorName = ""; //$NON-NLS-1$
			IEditorRegistry editors = PlatformUI.getWorkbench().getEditorRegistry();
			IEditorDescriptor editorDesc = editors.findEditor(id);
			if (editorDesc != null) {
				editorName = editorDesc.getLabel();
				return activePage.openEditor(new DummyEditorInput(targetUrl, editorName), id);
			} else {
				System.err.println("Editor not found: " + id); //$NON-NLS-1$
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static IEditorPart getBlankBrowserEditorPart(String id) {
		IEditorReference[] editors = PlatformUIUtil.getActivePage().getEditorReferences();
		for (int i = 0; i < editors.length; i++) {
			if (editors[i].getId().equals(id)) {
				IWorkbenchPart part = editors[i].getPart(false);
				if (part instanceof IModelServiceHolder) {
					IModelService modelService = ((IModelServiceHolder) part).getModelService();
					// System.out.println(modelService.getURL());
					if (modelService.getURL() == null)
						return (IEditorPart) part;
					if (modelService instanceof IWebBrowserACTF && ("about:blank".equals(modelService.getURL()) || ("" //$NON-NLS-1$ //$NON-NLS-2$
							.equals(modelService.getURL()))))
						return (IEditorPart) part;
				}
			}
		}
		return null;
	}

	/**
	 * Try to open the target URL in existing {@link IEditorPart}. If
	 * appropriate Editor is not available, launch new {@link IEditorPart}.
	 * 
	 * @param targetUrl
	 *            target URL
	 */
	public static void openInExistingEditor(String targetUrl) {
		if (targetUrl == null) {
			return;
		}

		targetUrl = targetUrl.trim();
		String editorId;

		IWorkbenchPage activePage = PlatformUIUtil.getActivePage();
		if (activePage == null) {
			return;
		}
		try {
			editorId = getEditorId(targetUrl);
		} catch (EditorNotFoundException e) {
			System.err.println("Editor not found: " + targetUrl); //$NON-NLS-1$
			return;
		}
		if (activateEditorPart(editorId)) {
			IEditorPart editor = activePage.getActiveEditor();
			if (editor instanceof IModelServiceHolder) {
				((IModelServiceHolder) editor).getModelService().open(targetUrl);
			} else {
				launch(targetUrl);
			}
		} else {
			launch(targetUrl);
		}

	}

	/**
	 * Activate {@link IEditorPart}
	 * 
	 * @param id
	 *            ID of target {@link IEditorPart}
	 * @return true if activation is successfully finished
	 */
	public static boolean activateEditorPart(String id) {
		if (id != null) {
			IWorkbenchPage activePage = PlatformUIUtil.getActivePage();
			if (activePage == null) {
				return false;
			}
			IEditorPart editor = activePage.getActiveEditor();
			if (editor != null && editor.getSite().getId().equals(id)) {
				return true;
			}

			IEditorReference[] editors = activePage.getEditorReferences();
			for (int i = 0; i < editors.length; i++) {
				DebugPrintUtil.devOrDebugPrintln(editors[i].getId());
				if (editors[i].getId().equals(id)) {
					editor = editors[i].getEditor(false);
					if (editor != null) {
						activePage.activate(editor);
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Get {@link IModelServiceHolder} from the current active
	 * {@link IEditorPart}
	 * 
	 * @return active {@link IModelServiceHolder}
	 */
	public static IModelServiceHolder getActiveModelServiceHolder() {
		IEditorPart editor = PlatformUIUtil.getActiveEditor();
		if (editor != null && editor instanceof IModelServiceHolder) {
			return (IModelServiceHolder) editor;
		}

		if (editor != null) {
			DebugPrintUtil.devOrDebugPrintln("ModelServiceUtils: " + editor //$NON-NLS-1$
					+ " isn't IModelServiceHolder"); //$NON-NLS-1$
		}

		return null;
	}

	public static IModelService[] getModelServices() {
		IEditorReference[] editorRefs = PlatformUIUtil.getActivePage().getEditorReferences();
		
		ArrayList<IModelService> results = new ArrayList<IModelService>();
		for(IEditorReference editorRef : editorRefs) {
			IEditorPart editor = editorRef.getEditor(false);
			if(editor != null && editor instanceof IModelServiceHolder) {
				results.add(((IModelServiceHolder)editor).getModelService());
			}
		}		
		return results.toArray(new IModelService[results.size()]);
	}
	
	
	/**
	 * Get {@link IModelService} from the current active {@link IEditorPart}
	 * 
	 * @return active {@link IModelService}
	 */
	public static IModelService getActiveModelService() {
		IModelServiceHolder holder = getActiveModelServiceHolder();
		if (holder != null) {
			return (holder.getModelService());
		}
		return null;
	}

	/**
	 * Try to open the same URL of the current active WebBrowserEditor by the
	 * ACTF Browser.
	 * 
	 * @return {@link IEditorPart} implements {@link IModelServiceHolder}, or
	 *         null if not available
	 */
	public static IEditorPart reopenInACTFBrowser() {
		String url = WebBrowserUtilForACTF.getUrl(PlatformUIUtil.getActiveEditor());
		if (url != null) {
			return ModelServiceUtils.launch(url);
		}
		return null;
	}

}
