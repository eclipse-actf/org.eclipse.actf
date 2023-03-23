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

import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;


public abstract class PageComponent {
	public static final short UNDEFINED_TYPE = -1;
	
	public static final short CONTAINER_TYPE = 1;
	public static final short CANDIDATE_CHARACTER_TYPE = 2; 
	public static final short CANDIDATE_UNDERLINED_CHARACTER_TYPE = 3; 

	public static final short SS_CHARACTER_TYPE = 4; // single fg/single bg
	public static final short MS_CHARACTER_TYPE = 5; // multi fg /single bg
	public static final short SM_CHARACTER_TYPE = 6; // single fg/multi bg
	// public static final short MM_CHARACTER_TYPE = 7; // multi fg/multi bg (do not consider)

	public static final short INTERIOR_IMAGE_TYPE = 10;
	public static final short OTHER_TYPE = 100; // others

	private IPageImage pageImage = null;
	int componentID;
	short type;
	public ConnectedComponent cc = null;
	public Container container = null;
	
	public PageComponent( short _type, IPageImage _pi ){
		type = _type;
		pageImage = _pi;
	}

	public IPageImage getPageImage(){
		return( pageImage );
	}
	public int getID(){
		return( componentID );
	}
	public void setID( int _id ){
		componentID = _id;
	}
	public short getType(){
		return( type );
	}
	public void setType( short _type ){
		type = _type;
	}
	public ConnectedComponent getConnectedComponent(){
		return( cc );
	}
	public void setConnectedComponent( ConnectedComponent _cc ){
		cc = _cc;
	}
	public Container getContainer(){
		return( container );
	}
	public int getX(){
		return( cc.left );
	}
	public int getY(){
		return( cc.top );
	}
	public int getWidth(){
		return( cc.shape.width );
	}
	public int getHeight(){
		return( cc.shape.height );
	}
}
