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

package org.eclipse.actf.visualization.internal.engines.lowvision;


// convert length in stylesheet
// in: 2.54cm
// cm: 
// mm: 
// pt: (1/72in)
// pc: (12pt = 1/6in)
// px: (1px = 0.21mm) experimental value (device dependent...)
public class LengthUtil {
	public static float in2cm( float _in ){
		return( _in*2.54f );
	}
	public static float in2mm( float _in ){
		return( _in*25.4f );
	}
	public static float in2pt( float _in ){
		return( _in*72.0f );
	}
	public static float in2pc( float _in ){
		return( _in*6.0f );
	}
	public static float in2px( float _in ){
		return( mm2px(in2mm(_in)) );
	}

	public static float cm2in( float _cm ){
		return( _cm/2.54f );
	}
	public static float cm2mm( float _cm ){
		return( _cm*10.0f );
	}
	public static float cm2pt( float _cm ){
		return( in2pt(cm2in(_cm)) );
	}
	public static float cm2pc( float _cm ){
		return( in2pc(cm2in(_cm)) );
	}
	public static float cm2px( float _cm ){
		return( _cm/0.021f );
	}

	public static float mm2in( float _mm ){
		return( _mm/25.4f );
	}
	public static float mm2cm( float _mm ){
		return( _mm/10.0f );
	}
	public static float mm2pt( float _mm ){
		return( in2pt(mm2in(_mm)) );
	}
	public static float mm2pc( float _mm ){
		return( in2pc(mm2in(_mm)) );
	}
	public static float mm2px( float _mm ){
		return( _mm/0.21f );
	}

	public static float pt2in( float _pt ){
		return( _pt/72.0f );
	}
	public static float pt2cm( float _pt ){
		return( in2cm(pt2in(_pt)) );
	}
	public static float pt2mm( float _pt ){
		return( in2mm(pt2in(_pt)) );
	}
	public static float pt2pc( float _pt ){
		return( _pt/12.0f );
	}
	public static float pt2px( float _pt ){
		return( mm2px(pt2mm(_pt)) );
	}

	public static float pc2in( float _pc ){
		return( _pc/6.0f );
	}
	public static float pc2cm( float _pc ){
		return( in2cm(pc2in(_pc)) );
	}
	public static float pc2mm( float _pc ){
		return( in2mm(pc2in(_pc)) );
	}
	public static float pc2pt( float _pc ){
		return( _pc*12.0f );
	}
	public static float pc2px( float _pc ){
		return( mm2px(pc2mm(_pc)) );
	}

	public static float px2in( float _px ){
		return( mm2in(px2mm(_px)) );
	}
	public static float px2cm( float _px ){
		return( _px*0.021f );
	}
	public static float px2mm( float _px ){
		return( _px*0.21f );
	}
	public static float px2pt( float _px ){
		return( mm2pt(px2mm(_px)) );
	}
	public static float px2pc( float _px ){
		return( mm2pc(px2mm(_px)) );
	}
}
