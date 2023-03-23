/*******************************************************************************
 * Copyright (c) 2007, 2019 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
#include "stdafx.h"
#include "org_eclipse_actf_util_win32_WindowUtil.h"

JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_WindowUtil_SetLayeredWindowAttributes
  (JNIEnv *env, jclass that, jintLong hwnd, jint crKey, jchar bAlpha, jint dwFlags)
{
	return SetLayeredWindowAttributes((HWND)hwnd, crKey, bAlpha, dwFlags);
}
