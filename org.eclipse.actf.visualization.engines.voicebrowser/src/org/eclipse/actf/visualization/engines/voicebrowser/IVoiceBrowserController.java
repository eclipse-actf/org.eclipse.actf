/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.voicebrowser;

import org.eclipse.actf.ai.tts.ITTSEngine;
import org.w3c.dom.Document;

/**
 * Interface of voice browser controller. The instance is available from
 * {@link VoiceBrowserControllerFactory}.
 * 
 * @see VoiceBrowserControllerFactory
 */
public interface IVoiceBrowserController {
	/**
	 * Command: move to previous line
	 */
	public static final int DO_PREV_LINE = 1;
	/**
	 * Command: revisit current line
	 */
	public static final int DO_CURR_LINE = 2;
	/**
	 * Command: move to next line
	 */
	public static final int DO_NEXT_LINE = 3;
	/**
	 * Command: move to previous link
	 */
	public static final int DO_PREV_LINK = 4;
	/**
	 * Command: revisit current link
	 */
	public static final int DO_CURR_LINK = 5;
	/**
	 * Command: move to next link
	 */
	public static final int DO_NEXT_LINK = 6;
	/**
	 * Command: move 10 lines backward
	 */
	public static final int DO_PREV_10LINE = 7;
	/**
	 * Command: move 10 lines forward
	 */
	public static final int DO_NEXT_10LINE = 8;
	/**
	 * Command: revisit current element
	 */
	public static final int DO_CURR_ELEMENT = 9;
	/**
	 * Command: move to top of page
	 */
	public static final int DO_TOP_OF_PAGE = 10;
	/**
	 * Command: move to bottom of page
	 */
	public static final int DO_BOTTOM_OF_PAGE = 11;
	/**
	 * Command: start continuous reading
	 */
	public static final int DO_PLAY = 12;
	/**
	 * Command: stop reading
	 */
	public static final int DO_STOP = 13;
	/**
	 * Command: invoke link jump
	 */
	public static final int DO_LINK_JUMP = 14;

	/**
	 * HomePage Reader simulation mode
	 */
	public static final int HPR_MODE = 1;
	/**
	 * Screen reader simulation mode
	 */
	public static final int SCREEN_READER_MODE = 2;

	/**
	 * Set voice browser simulation mode
	 * 
	 * @param mode
	 *            HPR_MODE or SCREEN_READER_MODE
	 */
	void setMode(int mode);

	/**
	 * Set target Document to voice browser controller
	 * 
	 * @param document
	 *            target Document
	 */
	void setDocument(Document document);

	/**
	 * Do command.
	 * 
	 * @param command
	 *            ID of commands
	 * @return String result string for DO_LINK_JUMP command, null for others
	 */
	String doCommand(int command);

	/**
	 * Get packet collection generated from the target Document
	 * 
	 * @return IPacketCollection
	 */
	IPacketCollection getPacketCollection();

	/**
	 * Add voice browser text view
	 * 
	 * @param view
	 *            target view
	 * @see IVoiceBrowserView
	 */
	void addView(IVoiceBrowserView view);

	/**
	 * Remove voice browser text view
	 * 
	 * @param view
	 *            target view
	 */
	void removeView(IVoiceBrowserView view);

	/**
	 * Add SelectionObserver
	 * 
	 * @param observer
	 *            target SelectionObserver
	 * @see SelectionObserver
	 */
	void addSelectionObserver(SelectionObserver observer);

	/**
	 * Remove SelectionObserver.
	 * 
	 * @param observer
	 *            target SelectionObserver
	 */
	void removeSelectionObserver(SelectionObserver observer);

	/**
	 * Add CursorListener
	 * 
	 * @param listener
	 *            target CursorListener
	 * @see CursorListener
	 */
	public void addCursorListener(CursorListener listener);

	/**
	 * Remove CursorListener.
	 * 
	 * @param listener
	 *            target CursorListener
	 */
	public void removeCursorListener(CursorListener listener);

	/**
	 * Set Text To Speech (TTS) Engine to enable speech output.
	 * 
	 * @param ttsEngine
	 *            target TTS engine
	 */
	void setSpeechControl(ITTSEngine ttsEngine);

}
