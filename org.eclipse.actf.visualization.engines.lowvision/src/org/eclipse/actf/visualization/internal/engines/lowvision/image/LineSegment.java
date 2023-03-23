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

public class LineSegment {
	public static final short POINT = 0;
	public static final short HORIZONTAL_LINE = 1;
	public static final short VERTICAL_LINE = 2;
	public static final short DIAGONAL_LINE = 3;

	int x0; //upper/left
	int y0; //upper/left
	int x1;
	int y1;

	public LineSegment( int _x0, int _y0, int _x1, int _y1 ){
		if( _x0 < _x1 ){
			x0 = _x0;
			y0 = _y0;
			x1 = _x1;
			y1 = _y1;
		}
		else if( _x0 > _x1 ){
			x0 = _x1;
			y0 = _y1;
			x1 = _x0;
			y1 = _y0;
		}
		else if( _y0 <= _y1 ){
			x0 = _x0;
			y0 = _y0;
			x1 = _x1;
			y1 = _y1;
		}
		else{
			x0 = _x1;
			y0 = _y1;
			x1 = _x0;
			y1 = _y0;
		}
	}
	public LineSegment( Coord _co0, Coord _co1 ){
		this( _co0.x, _co0.y, _co1.x, _co1.y );
	}

	public Coord getLeftPoint(){
		return( new Coord( x0, y0 ));
	}
	public Coord getRightPoint(){
		return( new Coord( x1, y1 ));
	}
	
	public Coord getUpperPoint(){
		if( y0 <= y1 ){
			return( new Coord( x0, y0 ) );
		}
		else{
			return( new Coord( x1, y1 ));
		}
	}
	public Coord getLowerPoint(){
		if( y0 > y1 ){
			return( new Coord( x0, y0 ) );
		}
		else{
			return( new Coord( x1, y1 ));
		}
	}
	
	public short getType(){
		if( x0==x1 ){
			if( y0==y1 ){
				return( POINT );
			}
			else{
				return( VERTICAL_LINE );
			}
		}
		else{ // x0!=x1
			if( y0==y1 ){
				return( HORIZONTAL_LINE );
			}
			else{
				return( DIAGONAL_LINE );
			}
		}
	}
	public boolean isPoint(){
		return( (getType()==POINT) );
	}
	public boolean isVertical(){
		return( (getType()==VERTICAL_LINE) );
	}
	public boolean isHorizontal(){
		return( (getType()==HORIZONTAL_LINE) );
	}
	public boolean isDiagonal(){
		return( (getType()==DIAGONAL_LINE) );
	}
	
	// return number of pixels
	public double getLength(){
		int xDiff = x1-x0+1;
		int yDiff = y1-y0+1;
		if( x0 == x1 ){
			return( (Math.abs(yDiff)) );
		}
		else if( y0 == y1 ){
			return( (Math.abs(xDiff)) );
		}
		else{
			return( Math.sqrt((xDiff*xDiff + yDiff*yDiff)) );
		}
	}
}
