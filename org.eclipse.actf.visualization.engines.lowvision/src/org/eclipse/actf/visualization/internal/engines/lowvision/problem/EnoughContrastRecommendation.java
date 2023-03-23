/*******************************************************************************
 * Copyright (c) 2003, 2020 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision.problem;

import org.eclipse.actf.visualization.internal.engines.lowvision.DecisionMaker;
import org.eclipse.actf.visualization.internal.engines.lowvision.Messages;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorException;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorIRGB;
import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorLAB;


public class EnoughContrastRecommendation extends LowVisionRecommendation{

	public static final float RECOMMENDED_DELTA_L_FOR_TEXT = DecisionMaker.MAX_ENOUGH_DELTA_L_FOR_TEXT + 5.0f;
	public static final float MID_L = 50.0f;

	private int originalForegroundColor = -1;
	private int originalBackgroundColor = -1;
	private int recommendedForegroundColor = -1; 
	private int recommendedBackgroundColor = -1;

	public EnoughContrastRecommendation( ILowVisionProblem _prob, int _fg, int _bg ) throws LowVisionProblemException{
		super( ENOUGH_CONTRAST_RECOMMENDATION, _prob, Messages.EnoughContrastRecommendation_Provide_enough_contrast_between_foreground_and_background_colors__1 );
		originalForegroundColor = _fg;
		originalBackgroundColor = _bg;
		calcRecommendedColors();
	}

	// only fo SS Character, use original color
	private void calcRecommendedColors() throws LowVisionProblemException{
		if( originalForegroundColor == -1 || originalBackgroundColor == -1 ){
			return;
		}
		try{
			ColorLAB foreLAB = (new ColorIRGB(originalForegroundColor)).toXYZ().toLAB();
			ColorLAB backLAB = (new ColorIRGB(originalBackgroundColor)).toXYZ().toLAB();
			if( ColorLAB.deltaL(foreLAB, backLAB) >= EnoughContrastRecommendation.RECOMMENDED_DELTA_L_FOR_TEXT ){
				recommendedForegroundColor = originalForegroundColor;
				recommendedBackgroundColor = originalBackgroundColor;
				return;
			}
			float foreL = foreLAB.getL();
			float foreA = foreLAB.getA();
			float foreB = foreLAB.getB();
			float backL = backLAB.getL();
			float backA = backLAB.getA();
			float backB = backLAB.getB();
			if( foreL > backL ){
				if( backL >= EnoughContrastRecommendation.MID_L ){
					backL = foreL - EnoughContrastRecommendation.RECOMMENDED_DELTA_L_FOR_TEXT;
				}
				else if( foreL <= EnoughContrastRecommendation.MID_L ){
					foreL = backL + EnoughContrastRecommendation.RECOMMENDED_DELTA_L_FOR_TEXT;
				}
				else{
					foreL = (foreL+backL+EnoughContrastRecommendation.RECOMMENDED_DELTA_L_FOR_TEXT)/2.0f;
					backL = foreL - EnoughContrastRecommendation.RECOMMENDED_DELTA_L_FOR_TEXT;
				}
			}
			else{ // (foreL <= backL)
				if( foreL >= EnoughContrastRecommendation.MID_L ){
					foreL = backL - EnoughContrastRecommendation.RECOMMENDED_DELTA_L_FOR_TEXT;
				}
				else if( backL <= EnoughContrastRecommendation.MID_L ){
					backL = foreL + EnoughContrastRecommendation.RECOMMENDED_DELTA_L_FOR_TEXT;
				}
				else{
					backL = (foreL+backL+EnoughContrastRecommendation.RECOMMENDED_DELTA_L_FOR_TEXT)/2.0f;
					foreL = backL - EnoughContrastRecommendation.RECOMMENDED_DELTA_L_FOR_TEXT;
				}
			}
			recommendedForegroundColor = (new ColorLAB(foreL, foreA, foreB)).toXYZ().toIRGB().toInt();
			recommendedBackgroundColor = (new ColorLAB(backL, backA, backB)).toXYZ().toIRGB().toInt();
		}catch( ColorException ce ){
			ce.printStackTrace();
			throw new LowVisionProblemException( "Error occurred while calculating recommended colors." ); //$NON-NLS-1$
		}
	}
	
	public int getOriginalForegroundColor(){
		return( originalForegroundColor );
	}
	public int getOriginalBackgroundColor(){
		return( originalBackgroundColor );
	}
	public int getRecommendedForegroundColor(){
		return( recommendedForegroundColor );
	}
	public int getRecommendedBackgroundColor(){
		return( recommendedBackgroundColor );
	}

}
