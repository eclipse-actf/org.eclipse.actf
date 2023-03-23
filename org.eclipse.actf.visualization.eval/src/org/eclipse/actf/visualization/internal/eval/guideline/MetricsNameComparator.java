/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.eval.guideline;

import java.util.Comparator;



public class MetricsNameComparator implements Comparator<String> {

    private static final String NAVIGABILITY = "navigability"; //$NON-NLS-1$
	private static final String LISTENABILITY = "listenability"; //$NON-NLS-1$
	private static final String COMPLIANCE = "compliance"; //$NON-NLS-1$

	private static final String P = "Perceivable"; //$NON-NLS-1$
	private static final String O = "Operable"; //$NON-NLS-1$
	private static final String U = "Understandable"; //$NON-NLS-1$
	private static final String R = "Robust"; //$NON-NLS-1$
		
	
	public int compare(String s1, String s2) {
        
        if(s1.equalsIgnoreCase(s2)){
            return(0);
        }

        if(s1.equalsIgnoreCase(P)){
            return(-1);
        }
        if(s2.equalsIgnoreCase(P)){
            return(1);
        }

        if(s1.equalsIgnoreCase(O)){
            return(-1);
        }
        if(s2.equalsIgnoreCase(O)){
            return(1);
        }
        
        if(s1.equalsIgnoreCase(U)){
            return(-1);
        }
        if(s2.equalsIgnoreCase(U)){
            return(1);
        }

        if(s1.equalsIgnoreCase(R)){
            return(-1);
        }
        if(s2.equalsIgnoreCase(R)){
            return(1);
        }
        
        if(s1.equalsIgnoreCase(COMPLIANCE)){
            return(-1);
        }
        if(s2.equalsIgnoreCase(COMPLIANCE)){
            return(1);
        }
        
        if(s1.equalsIgnoreCase(LISTENABILITY)){
            return(-1);
        }
        if(s2.equalsIgnoreCase(LISTENABILITY)){
            return(1);
        }
        
        if(s1.equalsIgnoreCase(NAVIGABILITY)){
            return(-1);
        }
        if(s2.equalsIgnoreCase(NAVIGABILITY)){
            return(1);
        }
        
        return(s1.compareTo(s2));
        
    }
    
}
