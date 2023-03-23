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

/*
 * x4 x3 x2 x5 x0 x1 x6 x7 x8
 * 
 * x9 = x1
 */
public class Neighbor {
	int[] x = new int[10];

	public Neighbor() {
	}

	public Neighbor(BinaryImage _bi, int _i, int _j) {
		int width = _bi.width;
		int height = _bi.height;
		if (_bi.data[_j][_i] != 0) {
			x[0] = 1;
		}
		if (_i > 0) {
			if (_j > 0 && _bi.data[_j - 1][_i - 1] != 0)
				x[4] = 1;
			if (_bi.data[_j][_i - 1] != 0)
				x[5] = 1;
			if (_j < height - 1 && _bi.data[_j + 1][_i - 1] != 0)
				x[6] = 1;
		}
		if (_j > 0 && _bi.data[_j - 1][_i] != 0)
			x[3] = 1;
		if (_j < height - 1 && _bi.data[_j + 1][_i] != 0)
			x[7] = 1;
		if (_i < width - 1) {
			if (_j > 0 && _bi.data[_j - 1][_i + 1] != 0)
				x[2] = 1;
			if (_bi.data[_j][_i + 1] != 0) {
				x[1] = 1;
				x[9] = 1;
			}
			if (_j < height - 1 && _bi.data[_j + 1][_i + 1] != 0)
				x[8] = 1;
		}
	}

	public Neighbor deepCopy() {
		Neighbor nei = new Neighbor();
		for (int i = 0; i < 10; i++) {
			nei.x[i] = this.x[i];
		}
		return (nei);
	}

	public int numForeground4() {
		return (x[1] + x[3] + x[5] + x[7]);
	}

	public int numForeground8() {
		return (x[1] + x[2] + x[3] + x[4] + x[5] + x[6] + x[7] + x[8]);
	}

	public int numBackground4() {
		return (4 - (x[1] + x[3] + x[5] + x[7]));
	}

	public int numBackground8() {
		return (8 - (x[1] + x[2] + x[3] + x[4] + x[5] + x[6] + x[7] + x[8]));
	}

	// -1 -> target pixel = bg
	public int connectivityNumber4() {
		if (x[0] == 0)
			return (-1);
		int num = 0;
		for (int i = 1; i <= 7; i += 2) { // i = 1,3,5,7
			num += (x[i] - x[i] * x[i + 1] * x[i + 2]);
		}
		return (num);
	}

	// -1 -> target pixel = bg
	public int connectivityNumber8() {
		if (x[0] == 0)
			return (-1);
		int num = 0;
		for (int i = 1; i <= 7; i += 2) { // i = 1,3,5,7
			num += ((1 - x[i]) - (1 - x[i]) * (1 - x[i + 1]) * (1 - x[i + 2]));
		}
		return (num);
	}

	// -1 -> target pixel = bg
	public int crossingNumber() {
		if (x[0] == 0)
			return (-1);
		int num = 0;
		for (int i = 1; i < 9; i++) {
			num += x[i] * (1 - x[i + 1]);
		}
		return (num);
	}
}
