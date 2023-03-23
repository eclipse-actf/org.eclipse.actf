/*******************************************************************************
 * Copyright (c) 2010, 2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.examples.michecker.internal;

import org.eclipse.osgi.util.NLS;



public class Messages extends NLS {
    private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$
    
    public static String DialogOpenURL_Open_URL;
    
    public static String Validation_confirmT;
    public static String Validation_confirm;
    public static String Validation_localfileM;
    public static String Validation_localfileT;
    
    public static String Error;
    public static String Not_supported;
    
    public static String CaptionView_time;
    public static String CaptionView_caption;
    
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
