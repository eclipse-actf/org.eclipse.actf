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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Vector;

import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;

/*
 * represent single connected component
 * 
 * shape: rectangle of the connected component
 * shape.data[0][0]->originalImage.data[shape.top][shape.left]
 * 
 * Connected component derived by using thinning has same shape size of the
 * original component (before the thinning)
 */
public class ConnectedComponent {

	public static final short CONNECTIVITY_UNSET = 0;

	public static final short CONNECTIVITY_4 = 1;

	public static final short CONNECTIVITY_8 = 2;

	public static final short THINNING_HILDITCH = 10;

	public static final short THINNING_DEFAULT = THINNING_HILDITCH;

	int left;

	int top;

	BinaryImage shape;

	private int count; // number of pixelin the connected component

	short connectivity = CONNECTIVITY_UNSET; // 4 or 8

	public ConnectedComponent(int _left, int _top, int _width, int _height,
			int _count) {
		left = _left;
		top = _top;
		count = _count;
		shape = new BinaryImage(_width, _height);
	}

	public ConnectedComponent(int _left, int _top, BinaryImage _bi, short _conn) {
		left = _left;
		top = _top;
		shape = _bi;
		connectivity = _conn;
		if (_bi.getArea() == BinaryImage.NOT_MEASURED)
			_bi.measureArea();
		count = _bi.getArea();
	}

	public ConnectedComponent deepCopy() {
		ConnectedComponent cc = new ConnectedComponent(left, top, shape.width,
				shape.height, count);
		cc.shape = this.shape.deepCopy();
		cc.connectivity = this.connectivity;
		return (cc);
	}

	public int getLeft() {
		return (left);
	}

	void setLeft(int _l) {
		left = _l;
	}

	public int getTop() {
		return (top);
	}

	void setTop(int _t) {
		top = _t;
	}

	public int getWidth() {
		return (shape.width );
	}

	public int getHeight() {
		return (shape.height );
	}

	public BinaryImage getShape() {
		return (shape);
	}

	public int getCount() {
		return (count);
	}

	public boolean equals(ConnectedComponent _cc) {
		if (this.left != _cc.left) {
			return (false);
		}
		if (this.top != _cc.top) {
			return (false);
		}
		return (this.shape.equals(_cc.shape));
	}

	public static boolean includes(ConnectedComponent _a, ConnectedComponent _b) {
		if (_a.left > _b.left) {
			return (false);
		}
		if (_a.top > _b.top) {
			return (false);
		}
		if (_a.left + _a.shape.width < _b.left + _b.shape.width) {
			return (false);
		}
		if (_a.top + _a.shape.height < _b.top + _b.shape.height) {
			return (false);
		}
		int w = _b.shape.width;
		int h = _b.shape.height;
		int offsetX = _b.left - _a.left;
		int offsetY = _b.top - _a.top;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				if (_b.shape.data[j][i] != 0
						&& _a.shape.data[j + offsetY][i + offsetX] == 0) {
					return (false);
				}
			}
		}
		return (true);
	}

	public boolean includes(ConnectedComponent _cc) {
		return (includes(this, _cc));
	}

	public boolean isIncludedBy(ConnectedComponent _cc) {
		return (includes(_cc, this));
	}

	public double getDensity() {
		return (count / ((double) (shape.width) * (double) (shape.height)));
	}

	// adjust shape size (for removal of underline of link, etc.)
	public void adjustShape() {
		LabeledImage li = new LabeledImage(this.shape);
		ConnectedComponent cc = li.components[0];
		this.left += cc.left;
		this.top += cc.top;
		this.shape = cc.shape;
	}

	public ConnectedComponent calcContour() throws ImageException {
		if (connectivity == CONNECTIVITY_UNSET) {
			throw new ImageException(
					"Information on connectivity is needed to calculate genus"); //$NON-NLS-1$
		}

		int width = shape.width;
		int height = shape.height;
		if (width <= 2 || height <= 2)
			return (this.deepCopy());

		BinaryImage bi = new BinaryImage(width, height);
		for (int j = 0; j < height; j++) {
			if (shape.data[j][0] != 0)
				bi.data[j][0] = 1;
			if (shape.data[j][width - 1] != 0)
				bi.data[j][width - 1] = 1;
		}
		for (int i = 0; i < width; i++) {
			if (shape.data[0][i] != 0)
				bi.data[0][i] = 1;
			if (shape.data[height - 1][i] != 0)
				bi.data[height - 1][i] = 1;
		}

		if (connectivity == CONNECTIVITY_4) {
			for (int j = 1; j < height - 1; j++) {
				for (int i = 1; i < width - 1; i++) {
					if (shape.data[j][i] != 0
							&& (shape.data[j - 1][i - 1] == 0
									|| shape.data[j - 1][i] == 0
									|| shape.data[j - 1][i + 1] == 0
									|| shape.data[j][i - 1] == 0
									|| shape.data[j][i + 1] == 0
									|| shape.data[j + 1][i - 1] == 0
									|| shape.data[j + 1][i] == 0 || shape.data[j + 1][i + 1] == 0))
						bi.data[j][i] = 1;
				}
			}
		} else if (connectivity == CONNECTIVITY_8) {
			for (int j = 1; j < height - 1; j++) {
				for (int i = 1; i < width - 1; i++) {
					if (shape.data[j][i] != 0
							&& (shape.data[j - 1][i] == 0
									|| shape.data[j + 1][i] == 0
									|| shape.data[j][i - 1] == 0 || shape.data[j][i + 1] == 0))
						bi.data[j][i] = 1;
				}
			}
		} else {
			throw new ImageException("Unknown connectivity"); //$NON-NLS-1$
		}

		ConnectedComponent cc = new ConnectedComponent(left, top, bi,
				connectivity);
		return (cc);
	}

	public ConnectedComponent thinning() throws ImageException {
		return (thinning(THINNING_DEFAULT));
	}

	public ConnectedComponent thinning(short _method) throws ImageException {
		if (_method == THINNING_HILDITCH) {
			return (thinningHilditch());
		} else {
			// TBD
			throw new ImageException("Unknown thinning method: " + _method); //$NON-NLS-1$
		}
	}

	// Hilditch
	public ConnectedComponent thinningHilditch() {

		int width = shape.width;
		int height = shape.height;

		// add 1px to rim
		BinaryImage biBefore = new BinaryImage(width + 2, height + 2);
		BinaryImage biAfter = new BinaryImage(width + 2, height + 2);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				biBefore.data[j + 1][i + 1] = shape.data[j][i];
			}
		}

		while (scanForThinningHilditch(biBefore, biAfter) > 0) {

			biBefore = biAfter;
			biAfter = new BinaryImage(width + 2, height + 2);
		}

		BinaryImage bi = new BinaryImage(width, height);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				bi.data[j][i] = biBefore.data[j + 1][i + 1];
			}
		}

		return (new ConnectedComponent(left, top, bi, CONNECTIVITY_8));
	}

	private int scanForThinningHilditch(BinaryImage _before, BinaryImage _after) {
		int width = _before.width;
		int height = _before.height;
		int numChanged = 0;
		for (int j = 1; j < height - 1; j++) {
			for (int i = 1; i < width - 1; i++) {
				if (_before.data[j][i] == 0)
					continue;

				Neighbor nei = new Neighbor(_before, i, j);
				if (nei.numBackground4() == 0) { // (1)
					_after.data[j][i] = 1;
					continue;
				}
				if (nei.connectivityNumber8() != 1) { // (2)
					_after.data[j][i] = 1;
					continue;
				}
				if (nei.numForeground8() < 2) { // (3)
					_after.data[j][i] = 1;
					continue;
				}
				Neighbor nei2 = nei.deepCopy();
				nei2.x[2] = _after.data[j - 1][i + 1]; // right upper
				nei2.x[3] = _after.data[j - 1][i]; // upper
				nei2.x[4] = _after.data[j - 1][i - 1]; // left upper
				nei2.x[5] = _after.data[j][i - 1]; // left
				if (nei2.numForeground8() == 0) { // (4)
					_after.data[j][i] = 1;
					continue;
				}
				// need to check (5)
				Neighbor nei3 = nei.deepCopy();
				nei3.x[3] = _after.data[j - 1][i];
				if (nei3.connectivityNumber8() != 1) { // (5-A) upper
					_after.data[j][i] = 1;
					continue;
				}
				Neighbor nei4 = nei.deepCopy();
				nei4.x[5] = _after.data[j][i - 1];
				if (nei4.connectivityNumber8() != 1) { // (5-B) left
					_after.data[j][i] = 1;
					continue;
				}

				// remove the pixel(do nothing)
				numChanged++;
			}
		}
		return (numChanged);
	}

	public Topology calcTopology() throws ImageException {
		return (new Topology(this));
	}

	public ConnectedComponent calcConvexHull() throws ImageException {
		ConnectedComponent cont = calcContour();
		Coord[] contPoints = cont.shape.extractPoints();
		if (contPoints == null)
			return (null);
		int len = contPoints.length;
		if (len == 0)
			return (null);
		if (len < 5)
			return (this.deepCopy());

		Vector<Coord> vertexVector = new Vector<Coord>();
		Coord p0 = shape.topLeftPoint();
		vertexVector.addElement(p0);
		Coord curPoint = p0;
		Coord curVector = new Coord(1, 0);
		Coord nextPoint = nextConvexHullVertex(contPoints, curPoint, curVector);
		while (!nextPoint.equals(p0)) {
			vertexVector.addElement(nextPoint);
			curVector.set(nextPoint.x - curPoint.x, nextPoint.y - curPoint.y);
			curPoint = nextPoint;
			nextPoint = nextConvexHullVertex(contPoints, curPoint, curVector);
		}
		int numVertex = vertexVector.size();
		Coord[] vertexArray = new Coord[numVertex];
		for (int i = 0; i < numVertex; i++) {
			vertexArray[i] = vertexVector.elementAt(i);
		}

		// // debug(from here)
		// Coord.dump(System.out, vertexArray);
		// // debug(to here)

		BinaryImage newShape = new BinaryImage(shape.width, shape.height,
				vertexArray);
		return (new ConnectedComponent(left, top, newShape, connectivity));
	}

	/*
	 * find the point from _all that angle made by vector and (_origin to
	 * target) become the smallest degree->clockwise
	 */
	private Coord nextConvexHullVertex(Coord[] _all, Coord _origin,
			Coord _vector) throws ImageException {
		if (_all == null) {
			throw new ImageException("Empty vertex set."); //$NON-NLS-1$
		}
		int len = _all.length;
		if (len == 0) {
			throw new ImageException("No vertex"); //$NON-NLS-1$
		}

		Coord answer = new Coord(-1, -1);
		Coord curVector = new Coord(-1, -1);
		double maxCos = -2.0;
		double maxDistance = 0.0;
		for (int i = 0; i < len; i++) {
			Coord curPoint = _all[i];
			if (curPoint.equals(_origin))
				continue;
			curVector.set(curPoint.x - _origin.x, curPoint.y - _origin.y);
			double curCos = Coord.cosine(_vector, curVector);
			if (maxCos < curCos) {
				maxCos = curCos;
				maxDistance = Coord.distance(_origin, curPoint);
				answer.copy(curPoint);
			} else if (maxCos == curCos) {
				double curDistance = Coord.distance(_origin, curPoint);
				if (curDistance > maxDistance) {
					maxDistance = curDistance;
					answer.copy(curPoint);
				}
			}
		}

		return (answer);
	}

	/*
	 * calc genus (Euler number)
	 *  4: G = V - E + F
	 *  8: G = V - E - D + T - F
	 * V: number of fg pix
	 * E: number of connected fg pixel(2pix, vertically, horizontally)
	 * D: number of connected fg pixel(2pix, diagonal)
	 * T: number of connected fg pixel(3pix, vertically, horizontally)
	 * F: number of connected fg pixel(2x2 pixels)
	 */
	public int calcGenus() throws ImageException {
		if (connectivity == CONNECTIVITY_UNSET) {
			throw new ImageException(
					"Information on connectivity is needed to calculate genus"); //$NON-NLS-1$
		}

		int width = shape.width;
		int height = shape.height;
		if (width <= 2 || height <= 2)
			return (1);

		int v = shape.measureArea();
		int e = 0;
		int d = 0;
		int t = 0;
		int f = 0;

		if (connectivity == CONNECTIVITY_4) {
			for (int j = 0; j < height - 1; j++) {
				for (int i = 0; i < width - 1; i++) {
					if (shape.data[j][i] != 0) {
						if (shape.data[j + 1][i] != 0)
							e++;
						if (shape.data[j][i + 1] != 0)
							e++;
						if (shape.data[j + 1][i] != 0
								&& shape.data[j][i + 1] != 0
								&& shape.data[j + 1][i + 1] != 0)
							f++;
					}
				}
				if (shape.data[j][width - 1] != 0
						&& shape.data[j + 1][width - 1] != 0)
					e++;
			}
			for (int i = 0; i < width - 1; i++)
				if (shape.data[height - 1][i] != 0
						&& shape.data[height - 1][i + 1] != 0)
					e++;
			return (v - e + f);
		} else if (connectivity == CONNECTIVITY_8) {
			for (int j = 0; j < height - 1; j++) {
				for (int i = 0; i < width - 1; i++) {
					if (shape.data[j][i] != 0) {
						boolean ne = false;
						boolean sw = false;
						boolean se = false;
						if (shape.data[j + 1][i] != 0) {
							sw = true;
							e++;
						}
						if (shape.data[j][i + 1] != 0) {
							ne = true;
							e++;
						}
						if (shape.data[j + 1][i + 1] != 0) {
							se = true;
							d++;
						}
						if (ne && sw)
							t++;
						if (ne && se)
							t++;
						if (sw && se)
							t++;
						if (ne && sw && se)
							f++;
					}
					if (shape.data[j][i + 1] != 0) {
						if (shape.data[j + 1][i] != 0) {
							d++;
							if (shape.data[j + 1][i + 1] != 0)
								t++;
						}
					}
				}
				if (shape.data[j][width - 1] != 0
						&& shape.data[j + 1][width - 1] != 0)
					e++;
			}
			for (int i = 0; i < width - 1; i++)
				if (shape.data[height - 1][i] != 0
						&& shape.data[height - 1][i + 1] != 0)
					e++;
			return (v - e - d + t - f);
		} else {
			throw new ImageException("Unknown connectivity"); //$NON-NLS-1$
		}
	}
	
	public void dump(PrintStream _ps) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw);
	}

	@SuppressWarnings("nls")
	public void dump(PrintWriter _pw) {
		_pw.println("-------------------------------");
		_pw.println("Dumping a ConnectedComponent");
		_pw.println("Left = " + left + ", Top = " + top + ", Width = "
				+ shape.width + ", Height = " + shape.height);
		for (int j = 0; j < shape.height; j++) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < shape.width; i++) {
				if (shape.data[j][i] == 0)
					sb.append(".");
				else
					sb.append("#");
			}
			_pw.println(sb.toString());
		}
		_pw.println("-------------------------------");
	}

	public void dump(PrintStream _ps, int _left, int _top, int _width,
			int _height) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw, _left, _top, _width, _height);
	}

	@SuppressWarnings("nls")
	public void dump(PrintWriter _pw, int _left, int _top, int _width,
			int _height) {
		_pw.println("-------------------------------");
		_pw.println("Dumping a part of a ConnectedComponent");
		_pw.println("Left = " + left + ", Top = " + top + ", Width = "
				+ shape.width + ", Height = " + shape.height);
		_pw.println("Start point = ( " + _left + ", " + _top + ")");
		_pw.println("Printed width = " + _width + ", Printed height = "
				+ _height);
		for (int j = 0; j < _height; j++) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < _width; i++) {
				if (shape.data[j + _top][i + _left] == 0)
					sb.append(".");
				else
					sb.append("#");
			}
			_pw.println(sb.toString());
		}
		_pw.println("-------------------------------");
	}

	public Int2D drawShape(Int2D _i2d, int _color) {
		for (int j = 0; j < shape.height; j++) {
			for (int i = 0; i < shape.width; i++) {
				if (shape.data[j][i] != 0) {
					_i2d.getData()[j + top][i + left] = _color;
				}
			}
		}
		return (_i2d);
	}
}
