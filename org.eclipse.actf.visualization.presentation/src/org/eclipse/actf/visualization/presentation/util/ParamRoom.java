/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.presentation.util;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;




public class ParamRoom {
    public static final int ROOM_SMALL = 0;

    public static final int ROOM_MIDDLE = 1;

    public static final int ROOM_LARGE = 2;

    private int type = ROOM_SMALL;

    private int height;
    
    public ParamRoom() {
    }

    public LowVisionType getLowVisionType() {
        LowVisionType result = null;
        switch (type) {
        case ROOM_SMALL:
            result = RoomTypeCollection.getSmallType();
            break;
        case ROOM_MIDDLE:
            result = RoomTypeCollection.getMiddleType();
            break;
        case ROOM_LARGE:
            result = RoomTypeCollection.getLargeType();
            break;
        }
        if(result != null)
            result.setDisplayResolution(height);
        try {
            result.setEyesightDegree(1.0f);
        } catch (LowVisionException e) {
            e.printStackTrace();
        }
        
        DebugPrintUtil.devOrDebugPrintln(result.getEyesightPixel());
        return result;
    }
    
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }

    public void setDisplayResolution(int height) {
        this.height = height;
    }

}
