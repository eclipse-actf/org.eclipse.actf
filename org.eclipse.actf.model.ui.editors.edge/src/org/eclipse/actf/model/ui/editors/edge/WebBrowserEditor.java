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

package org.eclipse.actf.model.ui.editors.edge;

import java.net.URI;

import org.eclipse.actf.model.internal.ui.editors.edge.WebBrowserEdgeImpl;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.actf.model.ui.editor.DummyEditorInput;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.WebBrowserEventUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * The Editor implementation to open Web content by using Edge. It also provide
 * access to HTML DOM via {@link IModelService}. The model service also
 * implements {@link IWebBrowserACTF}.
 * 
 * @see IModelServiceHolder
 * @see IModelService
 * @see IWebBrowserACTF
 */
public class WebBrowserEditor extends EditorPart implements IModelServiceHolder {

	private static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$

	/**
	 * ID of this Editor
	 */
	public static final String ID = WebBrowserEditor.class.getName();

	private boolean hasIde = Platform.getBundle("org.eclipse.ui.ide") != null; //$NON-NLS-1$

	WebBrowserEdgeImpl webBrowser;

	IEditorInput input;

	/**
	 * Creates a new Internet Explorer Editor.
	 */
	public WebBrowserEditor() {
		super();
	}

	public void createPartControl(Composite parent) {
		String targetUrl = ABOUT_BLANK;
		if (input instanceof DummyEditorInput) {
			targetUrl = ((DummyEditorInput) input).getUrl();
			if ("".equals(targetUrl)) { //$NON-NLS-1$
				targetUrl = ABOUT_BLANK;
			}
		} else if (input instanceof IPathEditorInput) {
			targetUrl = ((IPathEditorInput) input).getPath().toFile().getAbsolutePath();
		} else {
			// to support RCP use
			if (hasIde) {
				if (input instanceof IURIEditorInput) {
					URI uri = ((IURIEditorInput) input).getURI();
					if (uri != null) {
						targetUrl = uri.toString();
//						if (targetUrl.startsWith("file:/")) {//$NON-NLS-1$
//							targetUrl = targetUrl.substring(6);
//						}
//						targetUrl = targetUrl.replaceAll("%20", " ");//$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				// if(input instanceof IFileEditorInput){
				// IFile file = ((IFileEditorInput)input).getFile();
				// targetUrl = file.getFullPath().toFile().getAbsolutePath();
				// }
			}
		}

		webBrowser = new WebBrowserEdgeImpl(this, parent, targetUrl);

//		webBrowser.setNewWindow2EventListener(new INewWiondow2EventListener() {
//			public void newWindow2(NewWindow2Parameters param) {
//				IEditorPart newEditor = ModelServiceUtils.launch(ABOUT_BLANK, ID);
//				if (newEditor instanceof WebBrowserEditor) {
//					IWebBrowserACTF browser = (IWebBrowserACTF) ((WebBrowserEditor) newEditor).getModelService();
//					param.setBrowserAddress(browser.getBrowserAddress());
//					WebBrowserEventUtil.newWindow(browser);
//				} else {
//					// TODO
//				}
//			}
//		});
//
//		webBrowser.setWindowClosedEventListener(new IWindowClosedEventListener() {
//			public void windowClosed() {
//				IWorkbenchPage page = PlatformUIUtil.getActivePage();
//				if (page != null) {
//					IEditorReference[] editorRefs = page.getEditorReferences();
//					for (IEditorReference i : editorRefs) {
//						if (WebBrowserEditor.this == i.getEditor(false)) {
//							PlatformUIUtil.getActivePage().closeEditor(WebBrowserEditor.this, false);
//						}
//					}
//				}
//			}
//		});
	}

	public void dispose() {
		WebBrowserEventUtil.browserDisposed(webBrowser, getPartName());
	}

	public void setFocus() {
		WebBrowserEventUtil.getFocus(webBrowser);
	}

	public void doSave(IProgressMonitor monitor) {
		// TODO
	}

	public void doSaveAs() {
		// TODO
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		this.input = input;
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public IModelService getModelService() {
		return (this.webBrowser);
	}

	public IEditorPart getEditorPart() {
		return this;
	}

	public void setEditorTitle(String title) {
		setPartName(title);
	}

}
