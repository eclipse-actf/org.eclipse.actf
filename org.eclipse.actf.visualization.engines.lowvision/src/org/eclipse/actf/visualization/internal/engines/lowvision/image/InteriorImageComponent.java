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

package org.eclipse.actf.visualization.internal.engines.lowvision.image;

import java.util.Vector;

import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;

public class InteriorImageComponent {
	InteriorImage containerImage = null;

	int color;

	int numConnectedComponents = 0;

	ConnectedComponent[] connectedComponents = null;

	int count = 0; // number of pixel in connected component

	double occupation = 0.0; // occupation ratio in original image

	InteriorImageComponent(InteriorImage _ii, int _color,
			Vector<ConnectedComponent> _vec) {
		containerImage = _ii;
		color = _color;
		numConnectedComponents = _vec.size();
		if (numConnectedComponents > 0) {
			connectedComponents = new ConnectedComponent[numConnectedComponents];
			for (int k = 0; k < numConnectedComponents; k++) {
				ConnectedComponent cc = _vec.elementAt(k);
				count += cc.getCount();
				connectedComponents[k] = cc;
			}
			occupation = (double) count
					/ (double) (_ii.getWidth() * _ii.getHeight());
		}
	}

	public InteriorImage getContainerImage() {
		return (containerImage);
	}

	public int getColor() {
		return (color);
	}

	public int getNumConnectedComponents() {
		return (numConnectedComponents);
	}

	public ConnectedComponent[] getConnectedComponents() {
		return (connectedComponents);
	}

	public ConnectedComponent getConnectedComponent(int _index)
			throws ImageException {
		if (_index < 0 || numConnectedComponents <= _index) {
			throw new ImageException("Out of range: specified index = " //$NON-NLS-1$
					+ _index + ", while #ConnectedComponent = " //$NON-NLS-1$
					+ numConnectedComponents);
		}
		return (connectedComponents[_index]);
	}

	public int getCount() {
		return (count);
	}

	public double getOccupation() {
		return (occupation);
	}
}
