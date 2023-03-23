/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision.character;

import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.ConnectedComponent;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Container;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageComponent;

/*
 * Character candidate (SS, SM, part of MS)
 * 
 */
public class CandidateCharacter extends PageComponent{
	private int foregroundColor = -1;

	public CandidateCharacter( IPageImage _pi, ConnectedComponent _cc, int _color ){
		super( CANDIDATE_CHARACTER_TYPE, _pi );
		cc = _cc;
		foregroundColor = _color;
	}

	public void setContainer( Container _c ){
		container = _c;
	}

	public int getForegroundColor() {
		return foregroundColor;
	}
}
