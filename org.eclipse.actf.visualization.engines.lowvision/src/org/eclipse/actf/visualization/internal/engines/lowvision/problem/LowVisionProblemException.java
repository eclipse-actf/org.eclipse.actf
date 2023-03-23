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

package org.eclipse.actf.visualization.internal.engines.lowvision.problem;


public class LowVisionProblemException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7337672152876700847L;
	public LowVisionProblemException(){
		super();
	}
	public LowVisionProblemException( String _s ){
		super( _s );
	}
}
