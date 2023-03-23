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
 * Character (single fg color/multi bg color)
 */
public class CharacterSM extends PageComponent {
	private int foregroundColor = -1;

	private Topology topology = null;

	private int[][] image = null;

	public CharacterSM(CandidateCharacter _cChar, int[][] _pixel)
			throws ImageException {
		super(SM_CHARACTER_TYPE, _cChar.getPageImage());
		cc = _cChar.cc;
		container = _cChar.container;
		foregroundColor = _cChar.getForegroundColor();
		topology = new Topology(cc.thinning());
		if (_pixel != null) {
			int w = cc.getShape().getWidth();
			int h = cc.getShape().getHeight();
			int offsetX = cc.getLeft();
			int offsetY = cc.getTop();
			image = new int[h][w];
			for (int j = 0; j < h; j++) {
				for (int i = 0; i < w; i++) {
					image[j][i] = _pixel[j + offsetY][i + offsetX];
				}
			}
		}
	}

	public CharacterSM(CandidateCharacter _cChar, IInt2D _pixel)
			throws ImageException {
		this(_cChar, _pixel.getData());
	}

	public CharacterSM(IPageImage _pi, ConnectedComponent _cc, Container _cont,
			int _color, int[][] _pixel) throws ImageException {
		super(SM_CHARACTER_TYPE, _pi);
		cc = _cc;
		container = _cont;
		foregroundColor = _color;
		topology = new Topology(cc.thinning());
		if (_pixel != null) {
			int w = cc.getShape().getWidth();
			int h = cc.getShape().getHeight();
			int offsetX = cc.getLeft();
			int offsetY = cc.getTop();
			image = new int[h][w];
			for (int j = 0; j < h; j++) {
				for (int i = 0; i < w; i++) {
					image[j][i] = _pixel[j + offsetY][i + offsetX];
				}
			}
		}
	}

	public CharacterSM(IPageImage _pi, ConnectedComponent _cc, Container _cont,
			int _color, IInt2D _pixel) throws ImageException {
		this(_pi, _cc, _cont, _color, _pixel.getData());
	}

	public int getForegroundColor() {
		return (foregroundColor);
	}

	public int[][] getImage() {
		return (image);
	}

	public IInt2D getInt2D() throws ImageException {
		return (new Int2D(cc.getShape().getWidth(), cc.getShape().getHeight(), image));
	}

	public IInt2D makeMarginedImage(int _margin) throws ImageException {
		if (_margin == 0) {
			return (getInt2D());
		}
		if (_margin < 0) {
			throw new ImageException("The margin must be non-negative."); //$NON-NLS-1$
		}
		int w = cc.getShape().getWidth();
		int h = cc.getShape().getHeight();
		int largeW = w + 2 * _margin;
		int largeH = h + 2 * _margin;
		Int2D i2d = new Int2D(largeW, largeH);
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				i2d.getData()[j + _margin][i + _margin] = image[j][i];
			}
			for (int i = 0; i < _margin; i++) {
				i2d.getData()[j + _margin][i] = image[j][0];
				i2d.getData()[j + _margin][largeW - 1 - i] = image[j][w - 1];
			}
		}
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < _margin; j++) {
				i2d.getData()[j][i + _margin] = image[0][i];
				i2d.getData()[largeH - 1 - j][i + _margin] = image[h - 1][i];
			}
		}
		for (int j = 0; j < _margin; j++) {
			for (int i = 0; i < _margin; i++) {
				i2d.getData()[j][i] = image[0][0];
				i2d.getData()[j][largeW - 1 - i] = image[0][w - 1];
				i2d.getData()[largeH - 1 - j][i] = image[h - 1][0];
				i2d.getData()[largeH - 1 - j][largeW - 1 - i] = image[h - 1][w - 1];
			}
		}
		return (i2d);
	}

	public Topology getTopology() {
		return topology;
	}
}
