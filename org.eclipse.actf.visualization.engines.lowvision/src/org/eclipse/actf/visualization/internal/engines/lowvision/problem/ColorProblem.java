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

import java.math.BigDecimal;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.internal.engines.lowvision.Messages;
import org.eclipse.actf.visualization.internal.engines.lowvision.PageElement;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterMS;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterSM;
import org.eclipse.actf.visualization.internal.engines.lowvision.character.CharacterSS;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageComponent;


public class ColorProblem extends LowVisionProblem{
	public static int LEVEL0 = 0;
	public static int LEVEL1 = 1;
	public static int LEVEL2 = 2;
	
	private int foregroundColor = -1;
	private int backgroundColor = -1;
	private boolean hasBackgroundImage = false;
	private double contrast = 0;
	private String[] targetStrings;

	public ColorProblem( PageComponent _pc, LowVisionType _lvType, double _proba ) throws LowVisionProblemException{
		super( LOWVISION_COLOR_PROBLEM, _lvType, Messages.ColorProblem_Foreground_and_background_colors_are_too_close__1, _pc, _proba );
		setComponentColors();
		setRecommendations();
	}
	
	public ColorProblem( PageElement _pe, LowVisionType _lvType, double _proba ) throws LowVisionProblemException{
		super( LOWVISION_COLOR_PROBLEM, _lvType, Messages.ColorProblem_Foreground_and_background_colors_are_too_close__1, _pe, _proba );
		foregroundColor = _pe.getForegroundColor();
		backgroundColor = _pe.getBackgroundColor();
		setRecommendations();
	}
	
	protected void setRecommendations() throws LowVisionProblemException{
		recommendations = new LowVisionRecommendation[1];		
		recommendations[0] = new EnoughContrastRecommendation( this, foregroundColor, backgroundColor );
	}
	
	private void setComponentColors() throws LowVisionProblemException{
		if( componentType == PageComponent.SS_CHARACTER_TYPE ){
			foregroundColor = ((CharacterSS)pageComponent).getForegroundColor();
			backgroundColor = ((CharacterSS)pageComponent).getBackgroundColor();
		}
		else if( componentType == PageComponent.MS_CHARACTER_TYPE ){
			backgroundColor = ((CharacterMS)pageComponent).getBackgroundColor();
			//use average color
			foregroundColor = ((CharacterMS)pageComponent).getForegroundColor();
		}
		else if( componentType == PageComponent.SM_CHARACTER_TYPE ){
			foregroundColor = ((CharacterSM)pageComponent).getForegroundColor();
		}
		else{
			throw new LowVisionProblemException( "Invalid component type." ); //$NON-NLS-1$
		}
	}
	
	public String getDescription() throws LowVisionProblemException{
		return(super.getDescription()+getAdditionalDescription());
	}

	public String getAdditionalDescription(){
		StringBuffer tmpSB = new StringBuffer();
		if(hasBackgroundImage&&problemType==LOWVISION_COLOR_PROBLEM){
			tmpSB.append(Messages.BackgroundImage);
		}
		if(targetStrings!=null && targetStrings.length>0){
			tmpSB.append("("+Messages.TargetString+" = ");
			for(String tmpS : targetStrings){
				tmpSB.append(tmpS+", ");
			}
			tmpSB.delete(tmpSB.length()-2, tmpSB.length());
			tmpSB.append(") ");
		}
		if(contrast>=1&&contrast<=21){
			BigDecimal bd = new BigDecimal(contrast);
			tmpSB.append("("+Messages.ContrastRatio+" = "+bd.setScale(2,BigDecimal.ROUND_HALF_UP)+")");
		}
		return(tmpSB.toString());
	}
	
	public int getForegroundColor(){
		return( foregroundColor );
	}

	public int getBackgroundColor(){
		return( backgroundColor );
	}

	public boolean hasBackgroundImage() {
		return hasBackgroundImage;
	}

	public void setHasBackgroundImage(boolean hasBackgroundImage) {
		this.hasBackgroundImage = hasBackgroundImage;
	}
	
	public void setIsWarning(boolean isWarning){
		if(isWarning){
			problemType = LOWVISION_BACKGROUND_IMAGE_WARNING;
		}else{
			problemType = LOWVISION_COLOR_PROBLEM;
		}		
	}

	public double getContrast() {
		return contrast;
	}

	public void setContrast(double contrast) {
		this.contrast = contrast;
	}

	public String[] getTargetStrings() {
		return targetStrings;
	}

	public void setTargetStrings(String[] targetStrings) {
		this.targetStrings = targetStrings;
	}
	
	public int getLevel(){
		if(contrast>=1 && contrast<3){
			return LEVEL0;			
		}else if (contrast<4.5){
			return LEVEL1;
		}
		return LEVEL2;
	}
	
}
