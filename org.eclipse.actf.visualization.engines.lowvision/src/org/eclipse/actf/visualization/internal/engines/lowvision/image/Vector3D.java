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

public class Vector3D {
	double x;

	double y;

	double z;

	public Vector3D(double _x, double _y, double _z) {
		x = _x;
		y = _y;
		z = _z;
	}

	public static boolean isZeroVector(Vector3D _a) {
		if (_a.x == 0.0 && _a.y == 0.0 && _a.z == 0.0) {
			return (true);
		} else {
			return (false);
		}
	}

	public boolean isZeroVector() {
		return (isZeroVector(this));
	}

	public static double magnitude2(Vector3D _a) {
		return (_a.x * _a.x + _a.y * _a.y + _a.z * _a.z);
	}

	public double magnitude2() {
		return (magnitude2(this));
	}

	public static double magnitude(Vector3D _a) {
		return (Math.sqrt(magnitude2(_a)));
	}

	public double magnitude() {
		return (magnitude(this));
	}

	// a+b
	public static Vector3D add(Vector3D _a, Vector3D _b) {
		return (new Vector3D(_a.x + _b.x, _a.y + _b.y, _a.z + _b.z));
	}

	public Vector3D add(Vector3D _a) {
		return (add(this, _a));
	}

	// a-b
	public static Vector3D subtract(Vector3D _a, Vector3D _b) {
		return (new Vector3D(_a.x - _b.x, _a.y - _b.y, _a.z - _b.z));
	}

	public Vector3D subtractFrom(Vector3D _a) {
		return (subtract(_a, this));
	}

	public Vector3D subtractedBy(Vector3D _b) {
		return (subtract(this, _b));
	}

	public static double innerProduct(Vector3D _a, Vector3D _b) {
		return (_a.x * _b.x + _a.y * _b.y + _a.z * _b.z);
	}

	public static double cosine(Vector3D _a, Vector3D _b) throws ImageException {
		if (isZeroVector(_a) || isZeroVector(_b)) {
			throw new ImageException("Cannot calculate cosine of zero-vectors"); //$NON-NLS-1$
		}
		double answer = innerProduct(_a, _b) / (magnitude(_a) * magnitude(_b));
		if (answer < -1.0) {
			answer = -1.0;
		} else if (1.0 < answer) {
			answer = 1.0;
		}
		return (answer);
	}

	public double cosine(Vector3D _a) throws ImageException {
		return (cosine(this, _a));
	}

	public static double angle(Vector3D _a, Vector3D _b) throws ImageException {
		return (Math.acos(cosine(_a, _b)));
	}

	public double angle(Vector3D _a) throws ImageException {
		return (angle(this, _a));
	}

	public static double sine(Vector3D _a, Vector3D _b) throws ImageException {
		return (Math.sin(angle(_a, _b)));
	}

	public double sine(Vector3D _a) throws ImageException {
		return (sine(this, _a));
	}

	@SuppressWarnings("nls")
	public String toString() {
		return ("(" + x + ", " + y + ", " + z + ")");
	}

	public void dump(PrintStream _ps) {
		PrintWriter pw = new PrintWriter(_ps, true);
		dump(pw);
	}

	@SuppressWarnings("nls")
	public void dump(PrintWriter _pw) {
		_pw.println("-----");
		_pw.println("dumping Vector3D");
		_pw.println("(x,y,z) = ( " + x + ", " + y + ", " + z + ")");
		_pw.println("-----");
	}
}
