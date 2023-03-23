/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.range;

import org.eclipse.actf.model.dom.odf.table.TableCellElement;

public interface CellRange extends IContentRange {
	public void setCellRange(long left, long top, long right, long bottom);

	public TableCellElement getCell(long column, long row);
}
