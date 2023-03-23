/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.preferences;

import java.util.List;



public interface IGuidelineTreeItem {
    
    public void add(IGuidelineTreeItem guidelineTreeItem);

    public IGuidelineTreeItem getParent();

    public List<IGuidelineTreeItem> getChildren();
}
