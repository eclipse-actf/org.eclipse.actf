/*******************************************************************************
 * Copyright (c) 2010, 2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.examples.michecker;

import org.eclipse.actf.visualization.ui.IVisualizationPerspective;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return IVisualizationPerspective.ID_HTML_PERSPECTIVE;
	}
}
