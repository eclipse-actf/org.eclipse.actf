/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ai.tts;

/**
 * ITTSEngineInfo enables to store information of text synthesis engines (name, language and gender).
 * 
 * @see ITTSEngine
 */
public interface ITTSEngineInfo {
	
	/**
	 * @return name of TTS engine
	 */
	String getName();
	
	/**
	 * @return language of TTS engine
	 */
	String getLanguage();
	
	/**
	 * @return gender of TTS engine
	 */
	String getGender();
}
