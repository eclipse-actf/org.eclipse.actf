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
 * Character (multi fg color/single bg color)
 */
public class CharacterMS extends PageComponent {
	private int backgroundColor = -1; // container.color

	private int foregroundColor = -1; // average color

	private Topology topology = null;

	private int[][] image = null;

	public CharacterMS(IPageImage _pi, ConnectedComponent _cc, Container _cont,
			int[][] _pixel) throws ImageException {
		super(MS_CHARACTER_TYPE, _pi);
		cc = _cc;
		container = _cont;
		backgroundColor = _cont.getColor();
		topology = new Topology(cc.thinning());
		if (_pixel != null) {
			int w = cc.getShape().getWidth();
			int h = cc.getShape().getHeight();
			int offsetX = cc.getLeft();
			int offsetY = cc.getTop();
			image = new int[h][w];
			int sumR = 0;
			int sumG = 0;
			int sumB = 0;
			byte[][] data = cc.getShape().getData();
			for (int j = 0; j < h; j++) {
				for (int i = 0; i < w; i++) {
					image[j][i] = _pixel[j + offsetY][i + offsetX];
					if (data[j][i] != 0) {
						int color = image[j][i];
						sumR += (color >> 16) & 0xff;
						sumG += (color >> 8) & 0xff;
						sumB += color & 0xff;
					}
				}
			}
			sumR /= cc.getCount();
			sumG /= cc.getCount();
			sumB /= cc.getCount();
			foregroundColor = (sumR << 16) | (sumG << 8) | (sumB);
		}
	}

	public CharacterMS(IPageImage _pi, ConnectedComponent _cc, Container _cont,
			IInt2D _pixel) throws ImageException {
		this(_pi, _cc, _cont, _pixel.getData());
	}

	public int getBackgroundColor() {
		return (backgroundColor);
	}

	public int getForegroundColor() {
		return (foregroundColor);
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
		Int2D i2d = new Int2D(cc.getShape().getWidth() + 2 * _margin, cc.getShape().getHeight() + 2
				* _margin);
		i2d.fill(backgroundColor);
		for (int j = 0; j < cc.getShape().getHeight(); j++) {
			for (int i = 0; i < cc.getShape().getWidth(); i++) {
				i2d.getData()[j + _margin][i + _margin] = image[j][i];
			}
		}
		return (i2d);
	}

	public Topology getTopology() {
		return topology;
	}
}
