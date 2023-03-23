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
package org.eclipse.actf.visualization.internal.ui.report;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ReportDisplay {

    private Browser browser;

    public ReportDisplay(Composite _compositeParent) {

        Composite compositeRecommendationHalf = new Composite(_compositeParent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        compositeRecommendationHalf.setLayout(gridLayout);

        browser = new Browser(compositeRecommendationHalf, SWT.EDGE);
        browser.setLayoutData(new GridData(GridData.FILL_BOTH));
        displayReportFile(""); //$NON-NLS-1$
    }    
    
    public void displayReportFile(String url) {
        browser.setUrl(new File(url).exists() ? "file://" + url : "about:blank");
    }

}
