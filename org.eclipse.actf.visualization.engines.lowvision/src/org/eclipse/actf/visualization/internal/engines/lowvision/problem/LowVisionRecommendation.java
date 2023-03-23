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

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class LowVisionRecommendation implements Recommendation{
	public static final short ENOUGH_CONTRAST_RECOMMENDATION = 1;
	public static final short ENLARGE_TEXT_RECOMMENDATION = 101;
	public static final short ENLARGE_LINE_RECOMMENDATION = 102;
	public static final short CHANGABLE_FONT_RECOMMENDATION = 103;
	public static final short DONT_RELY_ON_COLOR_RECOMMENDATION = 201;
	public static final short USE_ALLOWED_COLOR_RECOMMENDATION = 301;
	
	//
	
	short recommendationType;
	ILowVisionProblem problem;
	String description;
	int priority;
	
	public LowVisionRecommendation( short _type, ILowVisionProblem _prob, String _desc ) throws LowVisionProblemException{
		recommendationType = _type;
		problem = _prob;
		description = _desc;
		setPriority();
	}
	
	private void setPriority(){
		// TODO
		priority = 0;
	}
	
	public short getType(){
		return( recommendationType );
	}
	public String getDescription(){
		return( description );
	}
	public int getPriority(){
		return( priority );
	}
	
	
	public void dump( PrintStream _ps ){
		PrintWriter pw = new PrintWriter( _ps, true );
		dump( pw );
	}
	
	@SuppressWarnings("nls")
	public void dump( PrintWriter _pw ){
		_pw.println( "-----" );
		_pw.println( "dumping a recommendation" );
		_pw.println( "type = " + recommendationType );
		_pw.println( "description = " + getDescription() );
		_pw.println( "priority = " + getPriority() );
		_pw.println( "-----" );
	}
}
