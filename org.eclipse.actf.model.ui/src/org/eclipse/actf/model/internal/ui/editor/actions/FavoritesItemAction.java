/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.internal.ui.editor.actions;

import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.ui.IWorkbenchWindow;


public class FavoritesItemAction extends FavoritesAction {

    public static final String ID = FavoritesItemAction.class.getName();

    private IWorkbenchWindow _window;

    private String _url;
    
    boolean useExistingEditor;

    public FavoritesItemAction(IWorkbenchWindow window, String name, String url, boolean useExistingEditor) {
        this._window = window;
        this._url = url;
        this.useExistingEditor = useExistingEditor;
        
        setText(name);
        setId(ID + "_" + name); //$NON-NLS-1$
    }

    public void run() {
        if (null != this._window) {
            if(useExistingEditor){
                ModelServiceUtils.openInExistingEditor(_url);
            }else{
                ModelServiceUtils.launch(_url);
            }
        }
    }
}
