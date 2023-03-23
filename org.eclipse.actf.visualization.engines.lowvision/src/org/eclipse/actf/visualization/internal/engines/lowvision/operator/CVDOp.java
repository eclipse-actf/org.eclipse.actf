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
package org.eclipse.actf.visualization.internal.engines.lowvision.operator;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.util.HashMap;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorException;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorIRGB;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorSRGB;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorYXY;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;

/*
 * simulate color vision deficiency
 */
public class CVDOp implements ILowVisionOperator{
	// keep Y(0.299R+0.587G+0.114B) in sRGB (I in HSI) or not
	// false is better
    private static final boolean PRESERVE_SRGB_Y = false;

    // see ColorValue.java
    // these values are for (Y,x,y) (not for (x,y,z))
    // (Y,x,y)space : (x,y,z)space
    // Y(YY) : y
    // x : x/(x+y+z)
    // y : y/(x+y+z)

    // sRGB->CIEXYZ->Yxy
    @SuppressWarnings("unused")
    private static final float R_YY = 0.21347046f, 
    R_X  = 0.6524872f,  
    R_Y  = 0.32519758f, 
    G_YY = 0.71173096f, 
    G_X  = 0.3305722f,  
    G_Y  = 0.5944182f,  
    B_YY = 0.07168579f, 
    B_X  = 0.1482494f,  
    B_Y  = 0.07736899f, 
    W_YY = 0.9963379f,  
    W_X  = 0.34579438f, 
    W_Y  = 0.35854465f; 

    private static final float C1_X = 0.747f;  // Protan line
    private static final float C1_Y = 0.253f;  // 
    private static final float C2_X = 1.080f;  // Deutan line
    private static final float C2_Y = -0.080f; // 
    private static final float C3_X = 0.171f;  // Tritan line
    private static final float C3_Y = 0.000f;  // 

	private static float BW_A = (B_Y-W_Y)/(B_X-W_X);// blue-white line in (x,y)
	private static float BW_B = B_Y-BW_A*B_X;
	// private static float BR_A = (B_Y-R_Y)/(B_X-R_X);//blue-red line in (x,y)
	// private static float BR_B = B_Y-BR_A*B_X;
	private static float BR_A = -0.5f;  // blue-red line (cross white)
	private static float BR_B = 0.525f; //

    private int type = 0; 

    public CVDOp( int _type ){
		type = _type;
    }

    public BufferedImage filter( BufferedImage _src, BufferedImage _dest ) throws LowVisionException{
		if( type != 1 && type != 2 && type != 3 ){
			throw new LowVisionException( "Invalid type: " + type ); //$NON-NLS-1$
		}
	
		WritableRaster srcRaster = _src.copyData( null );
		DataBufferInt srcBufInt = (DataBufferInt)(srcRaster.getDataBuffer());
		int[] srcArray = srcBufInt.getData();
		int srcSize = srcArray.length;
	
		BufferedImage destImage = _dest;
		if( _dest == null ){
		    destImage = new BufferedImage( _src.getWidth(), _src.getHeight(), BufferedImage.TYPE_INT_RGB );
		}
		WritableRaster destRaster = destImage.copyData( null );
		DataBufferInt destBufInt = (DataBufferInt)(destRaster.getDataBuffer());
		int[] destArray = destBufInt.getData();
		int destSize = destArray.length;
		if( srcSize != destSize ){
		    throw new LowVisionException( "Sizes of src and dest images differ." ); //$NON-NLS-1$
		}
	
		HashMap<Integer, Integer> pixelMap = new HashMap<Integer, Integer>();
		for( int i=0; i<srcSize; i++ ){
		    Integer srcPixel = new Integer( srcArray[i] );
		    Integer destPixel = null;
		    if( (destPixel=pixelMap.get(srcPixel)) == null ){
		    	destPixel = new Integer( convertColor(srcArray[i],type) );
				pixelMap.put( srcPixel, destPixel );
		    }
		    destArray[i] = destPixel.intValue();
		}
	
		destImage.setData( destRaster );
		return( destImage );
    } // filter( BufferedImage, BufferedImage )

	public IInt2D filter( Int2D _src, Int2D _dest ) throws LowVisionException{
		if( type != 1 && type != 2 && type != 3 ){
			throw new LowVisionException( "Invalid type: " + type ); //$NON-NLS-1$
		}
		
		Int2D destImage = _dest;
		if( _dest == null ){
			destImage = new Int2D( _src.getWidth(), _src.getHeight() );
		}

		HashMap<Integer, Integer> pixelMap = new HashMap<Integer, Integer>();
		for( int j=0; j<_src.getHeight(); j++ ){
			for( int i=0; i<_src.getWidth(); i++ ){
				int srcColor =_src.getData()[j][i];
				Integer srcPixel = new Integer( srcColor );
				Integer destPixel  = pixelMap.get(srcPixel);
				if( destPixel == null ){ 
					int destColor = convertColor( srcColor, type );
					destImage.getData()[j][i] = destColor;
					pixelMap.put( srcPixel, new Integer(destColor) );
				}
				else{ 
					destImage.getData()[j][i] = destPixel.intValue();
				}
			}
		}
		return( destImage );
	} 

	public static int convertColor( int _src, int _type ) throws LowVisionException{
		try{
			if( PRESERVE_SRGB_Y ){
				ColorSRGB srgb = (new ColorIRGB( _src )).toSRGB();
				// float preservedY = 0.299f*srgb.getR() + 0.587f*srgb.getG() +
				// 0.114f*srgb.getB();
				float preservedY = Math.max( srgb.getR(), Math.max( srgb.getG(), srgb.getB() ) );
				ColorYXY yxy = srgb.toYXY();
				// move (x,y) while keeping brightness (yy)
				ColorYXY newYXY = moveXY( yxy, _type );
				ColorSRGB newSRGB = newYXY.toSRGB();
				float newR = newSRGB.getR();
				float newG = newSRGB.getG();
				float newB = newSRGB.getB();
				// float newY = 0.299f*newR + 0.587f*newG + 0.114f*newB;
				float newY = Math.max( newR, Math.max( newG, newB ) );
				float ratio = 0.0f;
				if( newY != 0.0f )
					ratio = preservedY/newY;
				else
					ratio = 1.0f; // black
				newSRGB.setR( newR*ratio );
				newSRGB.setG( newG*ratio );
				newSRGB.setB( newB*ratio );
				return( newSRGB.toIRGB().toInt() );
			}
			else{
				ColorIRGB irgb = new ColorIRGB( _src );
				ColorYXY yxy = irgb.toYXY();
				// move (x,y) while keeping brightness (yy)
				ColorYXY newYXY = moveXY( yxy, _type );
				ColorIRGB newIRGB = newYXY.toIRGB();				
				return( newIRGB.toInt() );
			}
		}catch( ColorException ce ){
			ce.printStackTrace();
			throw new LowVisionException( "ColorException occurred while converting color."); //$NON-NLS-1$
		}
	}

	// move (x,y) while keeping brightness (yy)
    private static ColorYXY moveXY( ColorYXY _src, int _type ) throws LowVisionException, ColorException{
		float srcYY = _src.getYY();
		float srcX = _src.getX();
		float srcY = _src.getY();
		float confuseX = 0.0f;
		float confuseY = 0.0f;
		float locusA = 0.0f;
		float locusB = 0.0f;
		if( _type == 1 ){      
		    confuseX = C1_X;
		    confuseY = C1_Y;
		    locusA = BW_A;
		    locusB = BW_B;
		}
		else if( _type == 2 ){ 
		    confuseX = C2_X;
		    confuseY = C2_Y;
		    locusA = BW_A;
		    locusB = BW_B;
		}
		else if( _type == 3 ){ 
		    confuseX = C3_X;
		    confuseY = C3_Y;
		    locusA = BR_A;
		    locusB = BR_B;
		}
		else{
		    throw new LowVisionException( "Invalid type : " + _type ); //$NON-NLS-1$
		}
	
		float destYY = srcYY;
		float destX = 0.0f;
		float destY = 0.0f;
		if( confuseX != srcX ){ 
		    float confuseA = (confuseY-srcY)/(confuseX-srcX); // slope
		    float confuseB = confuseY - confuseA*confuseX;    // y-intercept

		    if( confuseA != locusA ){ 
			destX = (confuseB-locusB)/(locusA-confuseA);
			destY = (locusA*confuseB-confuseA*locusB)/(locusA-confuseA);
		    }
		    else{                     
		    	// TBD use polar coordinates, etc.
   			    /*
				 * destX = srcX; destY = srcY; debugUtil.errMsg( this, "(x,y) is
				 * out of sRGB's range. (x,y) = " + srcX + ", " + srcY );
				 */
			throw new LowVisionException( "(x,y) is out of sRGB's range. (x,y) = " + srcX + ", " + srcY ); //$NON-NLS-1$ //$NON-NLS-2$
		    }
		} else{
		    destX = confuseX;
		    destY = confuseX*locusA + locusB;
		}
	
		return( new ColorYXY( destYY, destX, destY ) );
   } 
}
