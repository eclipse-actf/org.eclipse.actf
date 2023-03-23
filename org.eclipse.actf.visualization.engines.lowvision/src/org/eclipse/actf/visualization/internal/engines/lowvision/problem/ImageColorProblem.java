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

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.internal.engines.lowvision.Messages;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.BinaryImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.ConnectedComponent;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.InteriorImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.InteriorImageComponent;


public class ImageColorProblem extends LowVisionProblem{
	// InteriorImage containerImage = null;
	InteriorImageComponent imageComponent1 = null;
	InteriorImageComponent imageComponent2 = null;

	public ImageColorProblem( InteriorImage _ii, LowVisionType _lvType, double _prob, InteriorImageComponent _lc1, InteriorImageComponent _lc2 ) throws LowVisionProblemException{
		super( LOWVISION_IMAGE_COLOR_PROBLEM, _lvType, Messages.ImageColorProblem_This_image_has_two_or_more_components_whose_colors_are_too_close__1, _ii, _prob );
		left = _ii.getLeft();
		top = _ii.getTop();
		width = _ii.getWidth();
		height = _ii.getHeight();
		imageComponent1 = _lc1;
		imageComponent2 = _lc2;
		if( _lc1.getContainerImage() != _lc2.getContainerImage() ){
			throw new LowVisionProblemException( "The two component come from different images." ); //$NON-NLS-1$
		}
		if( _lc1.getContainerImage() != _ii ){
			throw new LowVisionProblemException( "The container of the components and the InteriorImage do not match." ); //$NON-NLS-1$
		}
		setRecommendations();
	}

	public InteriorImageComponent[] getInteriorImageComponentArray(){
		InteriorImageComponent[] array = {imageComponent1, imageComponent2};
		return( array );
	}
	public InteriorImage getContainerImage(){
		return( imageComponent1.getContainerImage() );
	}

	protected void setRecommendations() throws LowVisionProblemException{
		recommendations = new LowVisionRecommendation[1];
		recommendations[0] = new DontRelyOnColorRecommendation( this );
	}

	//for debug
	public IInt2D showProblemImage(){
		Int2D whiteI2d = null;
		try{
			InteriorImage ii = (InteriorImage)(this.pageComponent);
			Int2D simI2d = ii.simulate(this.lowVisionType);
			whiteI2d = new Int2D( simI2d.getWidth(), simI2d.getHeight() );
			int numCC = imageComponent1.getNumConnectedComponents();
			ConnectedComponent[] ccArray = imageComponent1.getConnectedComponents();
			for( int k=0; k<numCC; k++ ){
				ConnectedComponent cc = ccArray[k];
				int ccLeft = cc.getLeft();
				int ccTop = cc.getTop();
				BinaryImage shape = cc.getShape();
				int ccWidth = shape.getWidth();
				int ccHeight = shape.getHeight();
				byte[][] ccData = shape.getData();
				for( int j=0; j<ccHeight; j++ ){
					for( int i=0; i<ccWidth; i++ ){
						if( ccData[j][i] != 0 ){
							whiteI2d.getData()[j+ccTop][i+ccLeft] = simI2d.getData()[j+ccTop][i+ccLeft];
						}
					}
				}
			}
			numCC = imageComponent2.getNumConnectedComponents();
			ccArray = null;
			ccArray = imageComponent2.getConnectedComponents();
			for( int k=0; k<numCC; k++ ){
				ConnectedComponent cc = ccArray[k];
				int ccLeft = cc.getLeft();
				int ccTop = cc.getTop();
				BinaryImage shape = cc.getShape();
				int ccWidth = shape.getWidth();
				int ccHeight = shape.getHeight();
				byte[][] ccData = shape.getData();
				for( int j=0; j<ccHeight; j++ ){
					for( int i=0; i<ccWidth; i++ ){
						if( ccData[j][i] != 0 ){
							whiteI2d.getData()[j+ccTop][i+ccLeft] = simI2d.getData()[j+ccTop][i+ccLeft];
						}
					}
				}
			}
		}catch( Exception e ){
			e.printStackTrace();
		}
		return( whiteI2d );
	}
}
