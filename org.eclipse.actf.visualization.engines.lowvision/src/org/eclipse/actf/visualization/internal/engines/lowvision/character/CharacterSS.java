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
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.ConnectedComponent;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Container;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageComponent;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Topology;

/*
 * Character (single fg color/single bg color)
 */
public class CharacterSS extends PageComponent {
	private int foregroundColor = -1;

	private int backgroundColor = -1; // container.color

	private Topology topology = null;

	public CharacterSS(CandidateCharacter _cChar) throws ImageException {
		super(SS_CHARACTER_TYPE, _cChar.getPageImage());
		cc = _cChar.cc;
		container = _cChar.getContainer();
		foregroundColor = _cChar.getForegroundColor();
		backgroundColor = container.getColor();
		topology = new Topology(cc.thinning());
	}

	public CharacterSS(IPageImage _pi, ConnectedComponent _cc, Container _cont,
			int _fg) throws ImageException {
		super(SS_CHARACTER_TYPE, _pi);
		cc = _cc;
		container = _cont;
		foregroundColor = _fg;
		backgroundColor = container.getColor();
		topology = new Topology(cc.thinning());
	}

	public int getForegroundColor() {
		return (foregroundColor);
	}

	public int getBackgroundColor() {
		return (backgroundColor);
	}

	public IInt2D makeMarginedImage(int _margin) {
		Int2D i2d = new Int2D(cc.getShape().getWidth() + 2 * _margin, cc.getShape().getHeight() + 2
				* _margin);
		i2d.fill(backgroundColor);
		byte[][] data = cc.getShape().getData(); 
		for (int j = 0; j < cc.getShape().getHeight(); j++) {
			for (int i = 0; i < cc.getShape().getWidth(); i++) {
				if (data[j][i] != 0) {
					i2d.getData()[j + _margin][i + _margin] = foregroundColor;
				}
			}
		}
		return (i2d);
	}

	public Topology getTopology() {
		return topology;
	}
}
