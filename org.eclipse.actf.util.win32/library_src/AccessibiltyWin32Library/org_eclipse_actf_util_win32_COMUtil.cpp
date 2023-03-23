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
#include "org_eclipse_actf_util_win32_COMUtil.h"

#ifndef _WIN64
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__III
#else
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJI
#endif
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2)
{
	jint rc = 0;
	try {
		rc = (jint)((jint(STDMETHODCALLTYPE *)(jintLong, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2);
	}
	catch(...) {
		rc = 1;
	}
	return rc;
}

#ifndef _WIN64
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IIII
#else
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJII
#endif
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	try {
		rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
	}
	catch(...) {
		rc = 1;
	}
	return rc;
}

#ifndef _WIN64
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IIIII
#else
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIII
#endif
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	try {
		rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	}
	catch(...) {
		rc = 1;
	}
	return rc;
}

#ifndef _WIN64
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IIIIII
#else
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIIII
#endif
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	try {
		rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	}
	catch(...) {
		rc = 1;
	}
	return rc;
}

#ifndef _WIN64
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IIIIIII
#else
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIIIII
#endif
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	try {
		rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	}
	catch(...) {
		rc = 1;
	}
	return rc;
}

#ifndef _WIN64
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IIIIIIII
#else
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIIIIII
#endif
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	try {
		rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	catch(...) {
		rc = 1;
	}
	return rc;
}

#ifndef _WIN64
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IIIIIIIII
#else
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIIIIIII
#endif
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc = 0;
	try {
		rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	catch(...) {
		rc = 1;
	}
	return rc;
}

#ifndef _WIN64
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IIIIIIIIII
#else
JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIIIIIIII
#endif
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jint rc = 0;
	try {
		rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	catch(...) {
		rc = 1;
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIIIJ
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jlong arg5)
{
	jint rc = 0;
	try {
		rc = (jint)((jint(STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	}
	catch (...) {
		rc = 1;
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIIJJ
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlong arg4, jlong arg5)
{
	jint rc = 0;
	try {
		rc = (jint)((jint(STDMETHODCALLTYPE *)(jintLong, jint, jint, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	}
	catch (...) {
		rc = 1;
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIJJJ
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jlong arg4, jlong arg5)
{
	jint rc = 0;
	try {
		rc = (jint)((jint(STDMETHODCALLTYPE *)(jintLong, jint, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	}
	catch (...) {
		rc = 1;
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIIJJJ
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6)
{
	jint rc = 0;
	try {
		rc = (jint)((jint(STDMETHODCALLTYPE *)(jintLong, jint, jint, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	}
	catch (...) {
		rc = 1;
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIIJJJJ
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7)
{
	jint rc = 0;
	try {
		rc = (jint)((jint(STDMETHODCALLTYPE *)(jintLong, jint, jint, jlong, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	catch (...) {
		rc = 1;
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_actf_util_win32_COMUtil_VtblCall__IJIJJJJJ
  (JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7)
{
	jint rc = 0;
	try {
		rc = (jint)((jint(STDMETHODCALLTYPE *)(jintLong, jint, jlong, jlong, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	catch (...) {
		rc = 1;
	}
	return rc;
}

