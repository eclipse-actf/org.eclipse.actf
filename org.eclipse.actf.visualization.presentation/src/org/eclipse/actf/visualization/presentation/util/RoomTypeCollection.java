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

import java.awt.Toolkit;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;


public class RoomTypeCollection {
    private static LowVisionType smallMeetingRoom = null;

    private static LowVisionType largeMeetingRoom = null;

    private static LowVisionType auditorium = null;

    public static final int DEFAULT_RESOLUTION_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width / 2;

    public static final int DEFAULT_RESOLUTION_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height / 2;

    public static LowVisionType getSmallType() {
        smallMeetingRoom = new LowVisionType();
        smallMeetingRoom.setEyesight(true);
        smallMeetingRoom.setDisplayHeight(100);
        smallMeetingRoom.setEyeDisplayDistance(300);
        smallMeetingRoom.setDisplayResolution(DEFAULT_RESOLUTION_HEIGHT);
        smallMeetingRoom.setCVD(false);
        smallMeetingRoom.setColorFilter(false);
        smallMeetingRoom.setGlare(false);
        try {
            smallMeetingRoom.setEyesightDegree(1.0f);
        } catch (Exception e) {

        }
        return smallMeetingRoom;
    }

    public static LowVisionType getMiddleType() {
        largeMeetingRoom = new LowVisionType();
        largeMeetingRoom.setEyesight(true);
        largeMeetingRoom.setDisplayHeight(150);
        largeMeetingRoom.setEyeDisplayDistance(1200);
        largeMeetingRoom.setDisplayResolution(DEFAULT_RESOLUTION_HEIGHT);
        largeMeetingRoom.setCVD(false);
        largeMeetingRoom.setColorFilter(false);
        largeMeetingRoom.setGlare(false);
        try {
            largeMeetingRoom.setEyesightDegree(1.0f);
        } catch (Exception e) {

        }

        return largeMeetingRoom;
    }

    public static LowVisionType getLargeType() {
        auditorium = new LowVisionType();
        auditorium.setEyesight(true);
        auditorium.setDisplayHeight(300);
        auditorium.setEyeDisplayDistance(3000);
        auditorium.setDisplayResolution(DEFAULT_RESOLUTION_HEIGHT);
        auditorium.setCVD(false);
        auditorium.setColorFilter(false);
        auditorium.setGlare(false);
        try {
            auditorium.setEyesightDegree(1.0f);
        } catch (Exception e) {

        }

        return auditorium;
    }
}
