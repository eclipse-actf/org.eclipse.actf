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

import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;

public class Coord {
	int x; // holizontal

	int y; // vertical

	public Coord(int _x, int _y) {
		x = _x;
		y = _y;
	}

	public int getX() {
		return (x);
	}

	public int getY() {
		return (y);
	}

	public void set(int _x, int _y) {
		x = _x;
		y = _y;
	}

	public void setX(int _x) {
		x = _x;
	}

	public void setY(int _y) {
		y = _y;
	}

	public void copy(Coord _co) {
		x = _co.x;
		y = _co.y;
	}

	public boolean isOrigin() {
		if (x == 0 && y == 0)
			return (true);
		else
			return (false);
	}

	public boolean equals(Coord _co) {
		if (x == _co.x && y == _co.y)
			return (true);
		else
			return (false);
	}

	// a+b
	public static Coord add(Coord _a, Coord _b) {
		return (new Coord(_a.x + _b.x, _a.y + _b.y));
	} // add( Coord, Coord )

	// this+b
	public Coord add(Coord _b) {
		return (add(this, _b));
	} // add( Coord )

	// a-b
	public static Coord subtract(Coord _a, Coord _b) {
		return (new Coord(_a.x - _b.x, _a.y - _b.y));
	} // subtract( Coord, Coord )

	// this-b
	public Coord subtract(Coord _b) {
		return (subtract(this, _b));
	} // subtract( Coord )

	public static int distancePow2(Coord _a, Coord _b) {
		int xDif = _a.x - _b.x;
		int yDif = _a.y - _b.y;
		return (xDif * xDif + yDif * yDif);
	}

	public static int distancePow2(Coord _a) {
		return (_a.x * _a.x + _a.y * _a.y);
	}

	public static double distance(Coord _a, Coord _b) {
		return (Math.sqrt(distancePow2(_a, _b)));
	}

	public static double distance(Coord _a) {
		return (Math.sqrt(distancePow2(_a)));
	}

	public static boolean isOnLine(Coord _start, Coord _end, Coord _point) {
		Coord vector1 = Coord.subtract(_end, _start);
		Coord vector2 = Coord.subtract(_point, _start);
		if (outerProduct(vector1, vector2) == 0)
			return (true);
		else
			return (false);
	}

	public static boolean isLeftToVector(Coord _start, Coord _end, Coord _point) {
		Coord vector1 = Coord.subtract(_end, _start);
		Coord vector2 = Coord.subtract(_point, _start);
		if (outerProduct(vector2, vector1) > 0)
			return (true);
		else
			return (false);
	}

	public static boolean isRightToVector(Coord _start, Coord _end, Coord _point) {
		Coord vector1 = Coord.subtract(_end, _start);
		Coord vector2 = Coord.subtract(_point, _start);
		if (outerProduct(vector1, vector2) > 0)
			return (true);
		else
			return (false);
	}

	public static int innerProduct(Coord _a, Coord _b) {
		return (_a.x * _b.x + _a.y * _b.y);
	}

	public static double cosine(Coord _a, Coord _b) throws ImageException {
		if (_a.isOrigin() || _b.isOrigin()) {
			throw new ImageException("Cannot calculate cosine of zero-vectors"); //$NON-NLS-1$
		}
		return (innerProduct(_a, _b) / (distance(_a) * distance(_b)));
	}

	public static int outerProduct(Coord _a, Coord _b) {
		return (_a.x * _b.y - _a.y * _b.x);
	}

	public void dump() {
		dump(System.out);
	}

	public void dump(PrintStream _ps) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw);
	}

	@SuppressWarnings("nls")
	public void dump(PrintWriter _pw) {
		_pw.println("-------------------------------");
		_pw.println("Dumping a point");
		_pw.println("(" + x + "," + y + ")");
		_pw.println("-------------------------------");
	}

	public static void dump(PrintStream _ps, Coord[] _points) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw, _points);
	}

	@SuppressWarnings("nls")
	public static void dump(PrintWriter _pw, Coord[] _points) {
		int numPoints = _points.length;
		_pw.println("-------------------------------");
		_pw.println("Dumping points");
		_pw.println("# of points = " + numPoints);
		for (int i = 0; i < numPoints; i++) {
			_pw.println("(" + _points[i].x + "," + _points[i].y + ")");
			_pw.println("-----");
		}
	}
}
