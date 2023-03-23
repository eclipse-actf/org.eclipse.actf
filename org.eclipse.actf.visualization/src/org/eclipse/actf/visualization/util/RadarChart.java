/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Utility class to generate radar chart
 */
public class RadarChart {

	// TODO move to common util

	// min and max values of items
	private static final int MIN_VALUE = 0;

	private static final int MAX_VALUE = 100;

	// image's attributes
	private static final int IMAGE_WIDTH = 260;

	private static final int IMAGE_HEIGHT = 150;

	private static final Color IMAGE_BGCOLOR = Color.WHITE;

	private static final double CIRCLE_CENTER_X = IMAGE_WIDTH / 2.0;

	private static final double CIRCLE_CENTER_Y = IMAGE_HEIGHT / 2.0;

	private static final Point2D CIRCLE_CENTER = new Point2D.Double(
			CIRCLE_CENTER_X, CIRCLE_CENTER_Y);

	private static final double CIRCLE_RADIUS = 40.0;

	private static final Color CIRCLE_COLOR = Color.BLACK;

	private static final Color BAR_COLOR = Color.BLACK;

	private static final Color POLYGON_COLOR = new Color(102, 102, 255);

	private static final Color NAMES_COLOR = Color.RED;

	private static final Font NAMES_FONT = new Font("Default", Font.PLAIN, 16); //$NON-NLS-1$

	private static final FontRenderContext NAMES_FONT_RENDER_CONTEXT = new FontRenderContext(
			null, true, true);

	private static final int NAMES_MARGIN = 5;

	private BufferedImage bufImage;

	private Graphics2D g2d;

	private int numItems;

	private String[] names;

	private int[] values;

	private Point2D[] valueFullPoints;

	private double[] valueTheta;

	private Point2D[] valuePoints;

	private static final int NUM_GRAD = 4; // grad = graduation

	private static final double GRAD_LENGTH = 5.0;

	private boolean smoothing = true; // hidden selection

	/**
	 * Constructor of RaderChart
	 * 
	 * @param _names
	 *            names of axis
	 * @param _values
	 *            values for each axis
	 * @throws Exception
	 */
	public RadarChart(String[] _names, int[] _values) throws Exception {
		names = _names;
		values = _values;
		numItems = values.length;

		if (numItems != names.length) {
			throw new Exception(
					"The numbers of names and values are different with each other."); //$NON-NLS-1$
		}
		if (numItems < 3) {
			throw new Exception("At least three items are needed."); //$NON-NLS-1$
		}
		for (int i = 0; i < numItems; i++) {
			if (isOutOfRange(values[i])) {
				throw new Exception("The " + i + "-th value is out of range: " //$NON-NLS-1$ //$NON-NLS-2$
						+ values[i]);
			}
		}

		createChart();
	}

	private boolean isOutOfRange(int _value) {
		if (_value < MIN_VALUE || MAX_VALUE < _value) {
			return (true);
		} else {
			return (false);
		}
	}

	// general purpose utility
	// compute new point located at (r, theta) position from the origin(x,y)
	// be aware that mathmatical coordinate and image coordinate are different
	private Point2D calcPointPolar(double _x, double _y, double _radius,
			double _theta) {
		double x = _x + _radius * Math.cos(_theta);
		double y = _y - _radius * Math.sin(_theta);
		return (new Point2D.Double(x, y));
	}

	private Point2D calcPointPolar(Point2D _p, double _radius, double _theta) {
		return (calcPointPolar(_p.getX(), _p.getY(), _radius, _theta));
	}

	private void createChart() {
		makePoints();

		bufImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		g2d = bufImage.createGraphics();
		g2d.setBackground(IMAGE_BGCOLOR);

		if (smoothing) {
			BufferedImage sandImage = new BufferedImage(
					bufImage.getWidth() * 2, bufImage.getHeight() * 2,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D sandG = sandImage.createGraphics();
			Point2D[] enlargedValuePoints = new Point2D[numItems];
			Point2D[] enlargedValueFullPoints = new Point2D[numItems];
			for (int i = 0; i < numItems; i++) {
				Point2D curPoint = valuePoints[i];
				enlargedValuePoints[i] = new Point2D.Double(
						curPoint.getX() * 2, curPoint.getY() * 2);
				curPoint = valueFullPoints[i];
				enlargedValueFullPoints[i] = new Point2D.Double(
						curPoint.getX() * 2, curPoint.getY() * 2);
			}
			Point2D enlargedCircleCenter = new Point2D.Double(
					CIRCLE_CENTER_X * 2, CIRCLE_CENTER_Y * 2);

			fillBackground(sandImage, sandG, IMAGE_BGCOLOR);
			drawEllipse(sandG, CIRCLE_COLOR, enlargedCircleCenter,
					CIRCLE_RADIUS * 2);
			fillPolygon(sandG, POLYGON_COLOR, BAR_COLOR, enlargedValuePoints);
			drawBars(sandG, BAR_COLOR, enlargedValueFullPoints,
					enlargedCircleCenter);

			WritableRaster srcRaster = sandImage.copyData(null);
			DataBufferInt srcBufInt = (DataBufferInt) (srcRaster
					.getDataBuffer());
			int[] srcArray = srcBufInt.getData();
			WritableRaster destRaster = bufImage.copyData(null);
			DataBufferInt destBufInt = (DataBufferInt) (destRaster
					.getDataBuffer());
			int[] destArray = destBufInt.getData();
			float[][] fImageR = new float[IMAGE_HEIGHT + 1][IMAGE_WIDTH + 1];
			float[][] fImageG = new float[IMAGE_HEIGHT + 1][IMAGE_WIDTH + 1];
			float[][] fImageB = new float[IMAGE_HEIGHT + 1][IMAGE_WIDTH + 1];
			for (int j = 0; j < IMAGE_HEIGHT; j++) {
				for (int i = 0; i < IMAGE_WIDTH; i++) {
					int nwIndex = 4 * j * IMAGE_WIDTH + i * 2;
					int nw = srcArray[nwIndex];
					int nwR = (nw >> 16) & 0xff;
					int nwG = (nw >> 8) & 0xff;
					int nwB = nw & 0xff;
					int ne = srcArray[nwIndex + 1];
					int neR = (ne >> 16) & 0xff;
					int neG = (ne >> 8) & 0xff;
					int neB = ne & 0xff;
					int sw = srcArray[nwIndex + IMAGE_WIDTH * 2];
					int swR = (sw >> 16) & 0xff;
					int swG = (sw >> 8) & 0xff;
					int swB = sw & 0xff;
					int se = srcArray[nwIndex + IMAGE_WIDTH * 2 + 1];
					int seR = (se >> 16) & 0xff;
					int seG = (se >> 8) & 0xff;
					int seB = se & 0xff;
					fImageR[j][i] = (nwR + neR + swR + seR) / 4.0f;
					fImageG[j][i] = (nwG + neG + swG + seG) / 4.0f;
					fImageB[j][i] = (nwB + neB + swB + seB) / 4.0f;
				}
			}
			int k = 0;
			for (int j = 0; j < IMAGE_HEIGHT; j++) {
				for (int i = 0; i < IMAGE_WIDTH; i++) {
					int newR = Math.round(fImageR[j][i]);
					if (newR < 0)
						newR = 0;
					else if (255 < newR)
						newR = 255;
					float errR = fImageR[j][i] - newR;
					fImageR[j][i + 1] += errR * 0.375f;
					fImageR[j + 1][i] += errR * 0.375f;
					fImageR[j + 1][i + 1] += errR * 0.25f;

					int newG = Math.round(fImageG[j][i]);
					if (newG < 0)
						newG = 0;
					else if (255 < newG)
						newG = 255;
					float errG = fImageG[j][i] - newG;
					fImageG[j][i + 1] += errG * 0.375f;
					fImageG[j + 1][i] += errG * 0.375f;
					fImageG[j + 1][i + 1] += errG * 0.25f;

					int newB = Math.round(fImageB[j][i]);
					if (newB < 0)
						newB = 0;
					else if (255 < newB)
						newB = 255;
					float errB = fImageB[j][i] - newB;
					fImageB[j][i + 1] += errB * 0.375f;
					fImageB[j + 1][i] += errB * 0.375f;
					fImageB[j + 1][i + 1] += errB * 0.25f;

					destArray[k] = newR << 16 | newG << 8 | newB;
					k++;
				}
			}
			bufImage.setData(destRaster);
		} else { // no smoothing
			fillBackground(bufImage, g2d, IMAGE_BGCOLOR);
			drawEllipse(g2d, CIRCLE_COLOR, CIRCLE_CENTER, CIRCLE_RADIUS);
			fillPolygon(g2d, POLYGON_COLOR, BAR_COLOR, valuePoints);
			drawBars(g2d, BAR_COLOR, valueFullPoints, CIRCLE_CENTER);
		}

		drawNames(g2d, NAMES_COLOR);
	}

	// preparing valueFullPoints, valueTheta, valuePoints
	private void makePoints() {
		valueFullPoints = new Point2D[numItems];
		valueTheta = new double[numItems];
		valuePoints = new Point2D[numItems];
		double theta = 2.0 * Math.PI / numItems;
		double startTheta = Math.PI / 2.0;
		for (int i = 0; i < numItems; i++) {
			valueTheta[i] = startTheta + i * theta;
			valueFullPoints[i] = calcPointPolar(CIRCLE_CENTER_X,
					CIRCLE_CENTER_Y, CIRCLE_RADIUS, valueTheta[i]);
			double ratio = ((double) values[i]) / MAX_VALUE;
			valuePoints[i] = new Point2D.Double(CIRCLE_CENTER_X * (1 - ratio)
					+ valueFullPoints[i].getX() * ratio, CIRCLE_CENTER_Y
					* (1 - ratio) + valueFullPoints[i].getY() * ratio);
		}
	}

	private void fillBackground(BufferedImage _bufIm, Graphics2D _g2d, Color _c) {
		Paint curPaint = _g2d.getPaint();
		_g2d.setPaint(_c);
		Rectangle2D r2d = new Rectangle2D.Double(0, 0, _bufIm.getWidth(),
				_bufIm.getHeight());
		_g2d.fill(r2d);
		_g2d.setPaint(curPaint);
	}

	private void drawEllipse(Graphics2D _g2d, Color _c, Point2D _center,
			double _r) {
		Paint curPaint = _g2d.getPaint();
		_g2d.setPaint(_c);
		double boundaryX = _center.getX() - _r;
		double boundaryY = _center.getY() - _r;
		double boundaryW = _r * 2.0f;
		Ellipse2D e2d = new Ellipse2D.Double(boundaryX, boundaryY, boundaryW,
				boundaryW);
		_g2d.draw(e2d);
		_g2d.setPaint(curPaint);
	}

	private void fillPolygon(Graphics2D _g2d, Color _fill, Color _contour,
			Point2D[] _points) {
		Paint curPaint = _g2d.getPaint();
		int[] xpoints = new int[numItems];
		int[] ypoints = new int[numItems];
		for (int i = 0; i < numItems; i++) {
			xpoints[i] = (int) (_points[i].getX());
			ypoints[i] = (int) (_points[i].getY());
		}
		// Polygon has int-precision only
		Polygon pol = new Polygon(xpoints, ypoints, numItems);
		_g2d.setPaint(_fill);
		_g2d.fill(pol);

		// the countour will be drawn in double-precision
		_g2d.setPaint(_contour);
		Line2D l2d = new Line2D.Double(_points[numItems - 1], _points[0]);
		_g2d.draw(l2d);
		for (int i = 0; i < numItems - 1; i++) {
			l2d = new Line2D.Double(_points[i], _points[i + 1]);
			_g2d.draw(l2d);
		}

		_g2d.setPaint(curPaint);
	}

	private void drawBars(Graphics2D _g2d, Color _c, Point2D[] _points,
			Point2D _circleCenter) {
		double circleCenterX = _circleCenter.getX();
		double circleCenterY = _circleCenter.getY();
		Paint curPaint = _g2d.getPaint();
		_g2d.setPaint(_c);
		for (int i = 0; i < numItems; i++) {
			double fullX = _points[i].getX();
			double fullY = _points[i].getY();
			Line2D l2d = new Line2D.Double(circleCenterX, circleCenterY, fullX,
					fullY);
			_g2d.draw(l2d);
			for (int j = 1; j < NUM_GRAD; j++) {
				double ratio = ((double) j) / NUM_GRAD;
				Point2D gradCenter = new Point2D.Double(circleCenterX
						* (1 - ratio) + fullX * ratio, circleCenterY
						* (1 - ratio) + fullY * ratio);
				Point2D grad0 = calcPointPolar(gradCenter, GRAD_LENGTH,
						valueTheta[i] + Math.PI / 2.0);
				Point2D grad1 = calcPointPolar(gradCenter, GRAD_LENGTH,
						valueTheta[i] - Math.PI / 2.0);
				l2d = new Line2D.Double(grad0, grad1);
				_g2d.draw(l2d);
			}
		}
		_g2d.setPaint(curPaint);
	}

	private void drawNames(Graphics2D _g2d, Color _c) {
		Paint curPaint = _g2d.getPaint();
		_g2d.setPaint(_c);
		_g2d.setFont(NAMES_FONT);

		for (int i = 0; i < numItems; i++) {
			Rectangle2D boundaryBox = NAMES_FONT.getStringBounds(names[i],
					NAMES_FONT_RENDER_CONTEXT);
			double stringWidth = boundaryBox.getWidth();
			double stringHeight = boundaryBox.getHeight();

			double fullX = valueFullPoints[i].getX();
			double fullY = valueFullPoints[i].getY();
			float x = 0.0f;
			float y = 0.0f;
			if (fullX < CIRCLE_CENTER_X) {
				x = (float) (fullX - stringWidth - NAMES_MARGIN);
				y = (float) (fullY + stringHeight / 2.0);
			} else if (CIRCLE_CENTER_X < fullX) {
				x = (float) (fullX + NAMES_MARGIN);
				y = (float) (fullY + stringHeight / 2.0);
			} else {
				x = (float) (fullX - stringWidth / 2.0);
				if (fullY < CIRCLE_CENTER_Y) {
					y = (float) (fullY - NAMES_MARGIN);
				} else {
					y = (float) (fullY + stringHeight + NAMES_MARGIN);
				}
			}

			_g2d.drawString(names[i], x, y);
		}

		_g2d.setPaint(curPaint);
	}

	/**
	 * Get radar chart as {@link BufferedImage}
	 * 
	 * @return radar chart as {@link BufferedImage}
	 */
	public BufferedImage getBufferedImage() {
		return (bufImage);
	}

	/**
	 * Write radar chart to file in PNG format
	 * 
	 * @param target
	 *            target file path
	 * @throws IOException
	 */
	public void writeToPNG(File target) throws IOException {
		ImageIO.write(bufImage, "PNG", target); //$NON-NLS-1$
	}
}
