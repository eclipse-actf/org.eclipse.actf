/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.blind.ui.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


public class BlindVisualizationBrowser {

    private Browser browser;

    public BlindVisualizationBrowser(Composite parent) {

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
        parent.setLayout(gridLayout);

        // Web browser
        browser = new Browser(parent, SWT.EDGE);
        browser.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    @Deprecated
    protected void setBrowserSilent() {
    }

    // Display the result HTML in blind IE
    // Checked:
    protected void navigate(String url) {
    	if (!url.matches("[a-z]+:.+")) {
    		url = "file://" + url;
    	}
    	browser.setUrl(url);
    }

    protected void execScript(String str) {
    	browser.execute(str);
    }

    protected void clearHighlight() {
    	execScript("clearHighlight();");
    }

}
