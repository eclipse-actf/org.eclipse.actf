/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.eclipse.actf.ai.tts.ITTSEngine;
import org.eclipse.actf.visualization.engines.voicebrowser.CursorListener;
import org.eclipse.actf.visualization.engines.voicebrowser.CursorMovedEvent;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacket;
import org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController;
import org.eclipse.actf.visualization.engines.voicebrowser.SelectionObserver;
import org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("nls")
//TODO
public class JWATControllerImpl implements IVoiceBrowserController {

	private static final String BACKSLASH_N = "\n"; //$NON-NLS-1$

	private static List<IVoiceBrowserView> views = new ArrayList<IVoiceBrowserView>();

	private static List<SelectionObserver> selObservers = new ArrayList<SelectionObserver>();

	@SuppressWarnings("unused")
	private static MessageCollection mc = null;

	private JWATCore core = new JWATCore();

	private CurrentCursor ccursor = new CurrentCursor();

	private ITTSEngine speech = new DummyTTSEngine();

	/**
	 * @see java.lang.Object#Object()
	 */
	public JWATControllerImpl() {
		setMode(HPR_MODE);
	}

	/**
	 * Method JWATControllerImpl.
	 * 
	 * @param mode
	 */
	public JWATControllerImpl(int mode) {
		setMode(mode);
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#setMode(int)
	 */
	public void setMode(int mode) {
		setMode(mode, ""); // xmlpath is not set by defalt //$NON-NLS-1$
	}

	public void setMode(int mode, String xmlpath) {
		// System.out.println ("setJwatMode [" + mode + "]");
		mc = core.setJwatMode(mode, xmlpath);
		doTopOfPage();
		// OutLoud.dumpMessages(mc);

		String s = OutLoud.name + " mode"; //$NON-NLS-1$
		speech.stop();
		speech.speak(s, ITTSEngine.TTSFLAG_FLUSH, -1);
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#setDocument(Document)
	 */
	public void setDocument(Document document) {
		setDocument(document, null, null);
	}

	public void setDocument(Document document, String uri, String anchorName) {
		Node node = document;
		JWATCore.setUriPrefix(uri);
		if (node != null) {
			NodeList children = document.getElementsByTagName("body"); //$NON-NLS-1$
			if (children != null && children.getLength() > 0) {
				node = children.item(0);
				DomUtil.setFormList(node);
				ccursor.setPc(core.getPacketCollection(node));
				ccursor.setOwnerDocumentNode(document);
				if (ccursor.getPc() != null && ccursor.getPc().size() > 0
						&& ccursor.getCurPos() == 0) {
					int pos = 0;
					if (anchorName != null && anchorName.length() > 0) {
						pos = core.getAnchorPosition(anchorName, ccursor
								.getTopNode(), ccursor.getPc());
						ccursor.setCurPos(pos);
						doCurLine();
					} else {
						pos = core.getNodePosition(node, ccursor.getPc());
						ccursor.setCurPos(pos);
					}
				}
				// DomUtil.recursiveWalk(node,0);
				// System.out.println("JWATControlImpl: 62:\n" +
				// ccursor.getPc().toString());
			}
		}
	}

	public void setNode(Node node) {
		setNode(node, null, null);
	}

	public void setNode(Node node, String uri, String anchorName) {
		JWATCore.setUriPrefix(uri);
		if (node != null) {
			Node ownerNode = node.getOwnerDocument();
			if (ownerNode != null) {
				if (ccursor.getPc() == null || ccursor.getPc().size() == 0
						|| ccursor.getOwnerDocumentNode() != ownerNode) {
					setDocument((Document) ownerNode);
				}
			}
			int pos = 0;
			if (anchorName != null && anchorName.length() > 0) {
				pos = core.getAnchorPosition(anchorName, ccursor.getTopNode(),
						ccursor.getPc());
				ccursor.setCurPos(pos);
				doCurLine();
			} else {
				pos = core.getNodePosition(node, ccursor.getPc());
				ccursor.setCurPos(pos);
			}
		}
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#doCommand(int)
	 */
	public String doCommand(int type) {
		int prevPos = ccursor.getCurPos();
		switch (type) {
		case DO_PREV_LINE:
			doPrevLine();
			break;
		case DO_CURR_LINE:
			doCurLine();
			break;
		case DO_NEXT_LINE:
			doNextLine();
			break;
		case DO_PREV_LINK:
			doPrevLink();
			break;
		case DO_CURR_LINK:
			doCurLink();
			break;
		case DO_NEXT_LINK:
			doNextLink();
			break;
		case DO_PREV_10LINE:
			for (int i = 0; i < 10; i++)
				doPrevLine();
			break;
		case DO_NEXT_10LINE:
			for (int i = 0; i < 10; i++)
				doNextLine();
			break;
		case DO_CURR_ELEMENT:
			doCurLine();
			break;
		case DO_TOP_OF_PAGE:
			doTopOfPage();
			break;
		case DO_BOTTOM_OF_PAGE:
			doBottomOfPage();
			break;
		case DO_LINK_JUMP:
			String s = doLinkJump();
			if (s != null && s.length() > 0)
				return s;
			break;
		case DO_PLAY:
			doPlay();
			break;
		case DO_STOP:
			doStop();
			break;
		}
		if (ccursor.getCurPos() != prevPos) {
			sendCursorEvent(new CursorMovedEventImpl(this, ccursor.getCurPos()));
		}

		return null;
	}

	/**
	 * Method doCurLine.
	 */
	private void doCurLine() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null && pc.size() > 0) {
				PacketCollection lpc = getCurLine(true);
				if (lpc != null && lpc.size() > 0)
					speakWithVisual(lpc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method doPrevLine.
	 */
	private void doPrevLine() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null && pc.size() > 0) {
				PacketCollection lpc = getPrevLine(true);
				if (lpc != null && lpc.size() > 0) {
					speakWithVisual(lpc);
				} else {
					speech.stop();
					if (OutLoud.topofpage != null
							&& OutLoud.topofpage.length() > 0)
						speech.speak(OutLoud.topofpage,
								ITTSEngine.TTSFLAG_FLUSH, -1);
					else
						speech.speak("Top of page.", ITTSEngine.TTSFLAG_FLUSH, 
								-1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method doNextLine.
	 */
	private void doNextLine() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null && pc.size() > 0) {
				PacketCollection lpc = getNextLine(true);
				if (lpc != null && lpc.size() > 0) {
					speakWithVisual(lpc);
				} else {
					speech.stop();
					if (OutLoud.endofpage != null
							&& OutLoud.endofpage.length() > 0)
						speech.speak(OutLoud.endofpage,
								ITTSEngine.TTSFLAG_FLUSH, -1);
					else
						speech.speak("End of page.", ITTSEngine.TTSFLAG_FLUSH,
								-1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method doCurLink.
	 */
	private void doCurLink() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null && pc.size() > 0) {
				PacketCollection lpc = getCurLink();
				if (lpc != null && lpc.size() > 0) {
					speakWithVisual(lpc);
				} else {
					speech.stop();
					if (OutLoud.nocurlink != null
							&& OutLoud.nocurlink.length() > 0)
						speech.speak(OutLoud.nocurlink,
								ITTSEngine.TTSFLAG_FLUSH, -1);
					else
						speech.speak("No Current Link.",
								ITTSEngine.TTSFLAG_FLUSH, -1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method doPrevLink.
	 */
	private void doPrevLink() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null && pc.size() > 0) {
				PacketCollection lpc = getPrevLink();
				if (lpc != null && lpc.size() > 0) {
					speakWithVisual(lpc);
				} else {
					speech.stop();
					if (OutLoud.noprevlink != null
							&& OutLoud.noprevlink.length() > 0)
						speech.speak(OutLoud.noprevlink,
								ITTSEngine.TTSFLAG_FLUSH, -1);
					else
						speech.speak("No Previous Link.",
								ITTSEngine.TTSFLAG_FLUSH, -1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method doNextLine.
	 */
	private void doNextLink() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null && pc.size() > 0) {
				PacketCollection lpc = getNextLink();
				if (lpc != null && lpc.size() > 0) {
					speakWithVisual(lpc);
				} else {
					speech.stop();
					if (OutLoud.nonextlink != null
							&& OutLoud.nonextlink.length() > 0)
						speech.speak(OutLoud.nonextlink,
								ITTSEngine.TTSFLAG_FLUSH, -1);
					else
						speech.speak("No Next Link.", ITTSEngine.TTSFLAG_FLUSH,
								-1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method doTopOfPage.
	 */
	private void doTopOfPage() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null && pc.size() > 0) {
				PacketCollection tpc = getTopLine();
				if (tpc != null && tpc.size() > 0) {
					speakWithVisual(tpc);
				} else {
					speech.stop();
					if (OutLoud.topofpage != null
							&& OutLoud.topofpage.length() > 0)
						speech.speak(OutLoud.topofpage,
								ITTSEngine.TTSFLAG_FLUSH, -1);
					else
						speech.speak("Top of page.", ITTSEngine.TTSFLAG_FLUSH,
								-1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method doBottomOfPage.
	 */
	private void doBottomOfPage() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null && pc.size() > 0) {
				PacketCollection bpc = getBottomLine();
				if (bpc != null && bpc.size() > 0) {
					speakWithVisual(bpc);
				} else {
					speech.stop();
					if (OutLoud.endofpage != null
							&& OutLoud.endofpage.length() > 0)
						speech.speak(OutLoud.endofpage,
								ITTSEngine.TTSFLAG_FLUSH, -1);
					else
						speech.speak("End of page.", ITTSEngine.TTSFLAG_FLUSH,
								-1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method doPlay.
	 */
	private void doPlay() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null && pc.size() > 0) {
				// speech
				String str = render(pc, ccursor.getCurPos());
				speech.stop();
				speech.speak(str, ITTSEngine.TTSFLAG_FLUSH, -1);
				// visual
				drawText(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method doStop.
	 */
	private void doStop() {
		// System.out.println("346: doStop()");
		speech.stop();
	}

	/**
	 * Method doLinkJump.
	 */
	private String doLinkJump() {
		try {
			PacketCollection pc = ccursor.getPc();
			if (pc != null) {

				int curpos = ccursor.getCurPos();
				Node node = null;
				// check if it's on a link tag
				node = (pc.get(curpos)).getNode();
				if (!pc.isLinkTag(curpos) && node != null) {
					Node pnode = node.getParentNode();
					boolean link = false;
					for (;;) {
						if (pnode == null)
							break;
						// String n = pnode.getNodeName();
						if (pnode.getNodeType() == Node.ELEMENT_NODE
								&& pnode.getNodeName().toLowerCase().equals(
										"body")) //$NON-NLS-1$
							break;
						if (pnode.getNodeType() == Node.ELEMENT_NODE
								&& pnode.getNodeName().toLowerCase()
										.equals("a")) { //$NON-NLS-1$
							node = pnode;
							link = true;
							break;
						}
						pnode = pnode.getParentNode();
					}
					if (link == false) {
						speech.stop();
						if (OutLoud.notonalink != null
								&& OutLoud.notonalink.length() > 0)
							speech.speak(OutLoud.notonalink,
									ITTSEngine.TTSFLAG_FLUSH, -1);
						else
							speech.speak("Not on a link.",
									ITTSEngine.TTSFLAG_FLUSH, -1);
						return null;
					}
				}

				// get target position
				if (ccursor.getTopNode() != null) {
					Node targetNode = null;
					String href = core.getHrefString(node);
					if (href != null && href.length() > 0) {
						if (href.charAt(0) == '#') {
							targetNode = core.getTargetNode(node, ccursor
									.getTopNode());

							int targetpos = curpos;
							if (targetNode != null)
								targetpos = core.getNodePosition(targetNode,
										ccursor.getPc());
							if (curpos != targetpos) {
								boolean hasstr = false;
								int pos;
								for (pos = targetpos; pos < pc.size(); pos++) {
									String str = (pc.get(pos)).getText();
									if (str != null && str.length() > 0) {
										hasstr = true;
										break;
									}
								}
								if (hasstr) {
									ccursor.setCurPos(pos);
									doCurLine();
								} else {
									ccursor.setCurPos(curpos);
									speech.stop();
									if (OutLoud.nostring != null
											&& OutLoud.nostring.length() > 0)
										speech.speak(OutLoud.nostring,
												ITTSEngine.TTSFLAG_FLUSH, -1);
									else
										speech.speak("Target has no strings.",
												ITTSEngine.TTSFLAG_FLUSH, -1);
								}
							}
							return null;
						} else if (!href.toLowerCase()
								.startsWith("javascript:")) { //$NON-NLS-1$
							return href;
						}
					}
				}
			}
			speech.stop();
			if (OutLoud.notonalink != null && OutLoud.notonalink.length() > 0)
				speech.speak(OutLoud.notonalink, ITTSEngine.TTSFLAG_FLUSH, -1);
			else
				speech.speak("Not on a link.", ITTSEngine.TTSFLAG_FLUSH, -1);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method getPrevLink.
	 * 
	 * @return PacketCollection
	 */
	private PacketCollection getPrevLink() {
		PacketCollection pc = ccursor.getPc();
		PacketCollection lpc = new PacketCollection();
		if (pc != null && pc.size() > 0) {
			int curpos = ccursor.getCurPos();
			int newpos = curpos;
			int endpos;
			boolean hasstr = false;

			if (curpos > 0) {
				for (int i = curpos - 1; i >= 0; i--) {
					if (i < pc.size()) {
						String str = (pc.get(i)).getText();
						if (str != null && str.length() > 0
								&& pc.isInsideAnchor(i)) {
							hasstr = true;
						}

						if (pc.isLinkTag(i) && hasstr) {
							newpos = i;
							break;
						} else if (pc.isLinkTag(i)) {
							hasstr = false;
						}
					}
				}
			}
			if (curpos != newpos) {
				ccursor.setCurPos(newpos);
				endpos = getEndPositionOfCurLine();
				if (newpos == endpos && endpos < (pc.size() - 1))
					endpos++;
				lpc.addAll(pc.subList(newpos, endpos));
				if (lpc.size() > 0) {
					int linkcnt = 0;
					for (int j = 0; j < lpc.size(); j++)
						if (lpc.isLinkTag(j))
							linkcnt++;
					if (linkcnt > 1)
						lpc = getCurLinkPC(true);
					else if (linkcnt == 0)
						lpc = null;
				}
			}
		}
		return lpc;
	}

	/**
	 * Method getCurLink.
	 * 
	 * @return PacketCollection
	 */
	private PacketCollection getCurLink() {
		PacketCollection pc = ccursor.getPc();
		PacketCollection lpc = null;
		if (pc != null && pc.size() > 0) {
			lpc = getCurLine(false);
			if (lpc != null && lpc.size() > 0) {
				int linkcnt = 0;
				for (int j = 0; j < lpc.size(); j++)
					if (lpc.isLinkTag(j))
						linkcnt++;
				if (linkcnt > 1)
					lpc = getCurLinkPC(true);
				else if (linkcnt == 0)
					lpc = null;
			}
		}
		return lpc;
	}

	/**
	 * Method getNextLink.
	 * 
	 * @return PacketCollection
	 */
	private PacketCollection getNextLink() {
		PacketCollection pc = ccursor.getPc();
		PacketCollection lpc = new PacketCollection();
		if (pc != null && pc.size() > 0) {
			int curpos = ccursor.getCurPos();
			int newpos = curpos;
			int endpos;
			boolean found = false;
			for (int i = curpos + 1; i < pc.size(); i++) {
				if (pc.isLinkTag(i)) {
					int k = i;
					if (pc.isInsideAnchor(i)) {
						for (k = i; k < pc.size(); k++)
							if (!pc.isInsideAnchor(k))
								break;
					}
					for (int j = i; j <= k; j++) {
						String str = (pc.get(j)).getText();
						if (((str != null) && (str.length() > 0))) {
							found = true;
							break;
						}
					}
					if (found) {
						newpos = i;
						break;
					}
				}
			}

			if (curpos != newpos) {
				ccursor.setCurPos(newpos);
				endpos = getEndPositionOfCurLine();
				if (newpos == endpos && endpos < (pc.size() - 1))
					endpos++;
				lpc.addAll(pc.subList(newpos, endpos));

				if (lpc.size() > 0) {
					int linkcnt = 0;
					for (int j = 0; j < lpc.size(); j++)
						if (lpc.isLinkTag(j))
							linkcnt++;
					if (linkcnt > 1) {
						lpc = getCurLinkPC(true);
						return lpc;
					} else if (linkcnt == 0)
						lpc = null;
				}
			}
		}
		return lpc;
	}

	/**
	 * Method getCurLine.
	 * 
	 * @param pc
	 * @return PacketCollection
	 */
	private PacketCollection getCurLine(boolean setpos) {
		PacketCollection newPc = new PacketCollection();
		PacketCollection pc = ccursor.getPc();
		if (pc != null && pc.size() > 0) {
			int startIndex = 0;
			int endIndex = 0;
			int pos = 0;
			int headPos = 0;
			int i = 0;
			int j = 0;

			// get position (current/head)
			pos = ccursor.getCurPos();
			headPos = pc.getTopNodePosition();

			// startIndex
			startIndex = pos;
			if (headPos >= pos) {
				startIndex = headPos;
			} else {
				if (pos > 0) {
					if (pc.isLinkTag(pos) && pc.isLinkTag(pos - 1))
						i = pos - 1;
					else {
						for (i = pos - 1; i >= 0; i--) {
							if (pc.isLineDelimiter(i))
								break;
						}
					}
				}

				// check its node has string
				for (j = i + 1; j < pc.size(); j++) {
					String str = (pc.get(j)).getText();
					if (((str != null) && (str.length() > 0))) {
						pos = j;
						break;
					}
				}
				if (pc.isInsideAnchor(j)) {
					for (int k = j; k >= 0; k--) {
						if (pc.isLinkTag(k)) {
							startIndex = k;
							break;
						}
					}
				} else
					startIndex = j;

				if (pos < j)
					startIndex = pos;
			}

			// endIndex
			endIndex = searchEndPosition(startIndex);
			if (startIndex == endIndex)
				endIndex += 1;

			// set start position to CurrentCursor
			if (setpos)
				ccursor.setCurPos(startIndex);

			// build line PacketCollection
			newPc.addAll(pc.subList(startIndex, endIndex));
		}
		return newPc;
	}

	private int getEndPositionOfCurLine() {
		int endIndex = 0;
		PacketCollection pc = ccursor.getPc();
		if (pc != null && pc.size() > 0) {
			int startIndex = 0;
			int pos = 0;
			int headPos = 0;
			int i = 0;
			int j = 0;

			// get position (current/head)
			pos = ccursor.getCurPos();
			headPos = pc.getTopNodePosition();

			// startIndex
			startIndex = pos;
			if (headPos >= pos) {
				startIndex = headPos;
			} else {
				if (pos > 0) {
					if (pc.isLinkTag(pos) && pc.isLinkTag(pos - 1))
						i = pos - 1;
					else {
						for (i = pos - 1; i >= 0; i--) {
							if (pc.isLineDelimiter(i))
								break;
						}
					}
				}

				// check its node has string
				for (j = i + 1; j < pc.size(); j++) {
					String str = (pc.get(j)).getText();
					if (((str != null) && (str.length() > 0))) {
						pos = j;
						break;
					}
				}
				if (pc.isInsideAnchor(j)) {
					for (int k = j; k >= 0; k--) {
						if (pc.isLinkTag(k)) {
							startIndex = k;
							break;
						}
					}
				} else
					startIndex = j;

				if (pos < j)
					startIndex = pos;
			}

			// endIndex
			endIndex = searchEndPosition(startIndex);
			if (startIndex == endIndex)
				endIndex += 1;
		}
		return endIndex;
	}

	/**
	 * Method searchEndPosition.
	 * 
	 * @param startPos
	 * @return int
	 */
	private int searchEndPosition(int startPos) {
		int endPos = startPos;
		PacketCollection pc = ccursor.getPc();
		if (pc != null && pc.size() > 0) {
			if (pc.isStartSelect(startPos)
					|| (pc.isLinkTag(startPos) && (startPos + 1 < pc.size() && pc
							.isLinkTag(startPos + 1)))) {
				endPos = startPos + 1;
			} else {
				boolean insideForm = pc.isInsideForm(startPos);
				while ((startPos < pc.size())
						&& (!pc.isLineDelimiter(startPos))) {
					if (insideForm && !pc.isInsideForm(startPos))
						break;
					startPos++;
				}
				endPos = startPos;
			}
		}
		return endPos;
	}

	/**
	 * Method getPrevLine.
	 * 
	 * @param pc
	 * @return PacketCollection
	 */
	private PacketCollection getPrevLine(boolean setpos) {
		PacketCollection newPc = new PacketCollection();
		PacketCollection pc = ccursor.getPc();
		if (pc != null && pc.size() > 0) {
			int startIndex = 0;
			int endIndex = 0;
			int pos = 0;
			int headPos = 0;

			// get position (current/head)
			pos = ccursor.getCurPos();
			headPos = pc.getTopNodePosition();

			if (headPos >= pos) {
				ccursor.setCurPos(headPos);
				return null;
			}

			// startIndex
			int curpos = ccursor.getCurPos();
			boolean strfound = false;
			if (curpos - 1 >= 0) {
				for (int i = curpos - 1; i >= 0; i--) {
					String str = (pc.get(i)).getText();
					if (!strfound && ((str != null) && (str.length() > 0))) {
						strfound = true;
						if (strfound) {
							if (pc.isLineDelimiter(i)) {
								pos = i;
								break;
							}
						}
					} else if (strfound && pc.isLineDelimiter(i)) {
						pos = i + 1;
						if (pc.isLinkTag(pos) && pc.isLinkTag(pos + 1))
							pos += 1;
						break;
					}
				}
			}
			if (pos == curpos)
				startIndex = pos - 1;
			else
				startIndex = pos;

			// endIndex
			endIndex = searchEndPosition(startIndex);
			if (startIndex == endIndex)
				endIndex += 1;

			// set start position to CurrentCursor
			if (setpos)
				ccursor.setCurPos(startIndex);

			// build line PacketCollection
			newPc.addAll(pc.subList(startIndex, endIndex));
		}
		return newPc;
	}

	/**
	 * Method getNextLine.
	 * 
	 * @param pc
	 * @return PacketCollection
	 */
	private PacketCollection getNextLine(boolean setpos) {
		PacketCollection newPc = new PacketCollection();
		PacketCollection pc = ccursor.getPc();
		if (pc != null && pc.size() > 0) {
			int startIndex = 0;
			int endIndex = 0;
			int pos = 0;

			// check last position
			int curpos = ccursor.getCurPos();
			int bottompos = pc.getBottomNodePosition();
			if (curpos >= bottompos)
				return null;

			// skip current line
			int i = curpos;
			boolean insideForm = pc.isInsideForm(i);
			if (pc.isStartSelect(i)
					|| (pc.isLinkTag(i) && (i + 1 < pc.size() && pc
							.isLinkTag(i + 1)))) {
				pos = i + 1;
			} else {
				while ((i < pc.size()) && (!pc.isLineDelimiter(i))) {
					if ((insideForm && !pc.isInsideForm(i))
							|| pc.isStartSelect(i))
						break;
					i++;
				}
				if ((insideForm && !pc.isInsideForm(i)) || pc.isStartSelect(i))
					pos = i;
				else
					pos = i + 1;
			}

			// check its node has string
			if (pos <= pc.size()) {
				for (i = pos; i < pc.size(); i++) {
					String str = (pc.get(i)).getText();
					if (((str != null) && (str.length() > 0))) {
						pos = i;
						break;
					}
				}
			}
			startIndex = pos;

			// endIndex
			endIndex = searchEndPosition(startIndex);
			if (startIndex == endIndex)
				endIndex += 1;

			// set start position to CurrentCursor
			if (setpos)
				ccursor.setCurPos(startIndex);

			// build line PacketCollection
			newPc.addAll(pc.subList(startIndex, endIndex));
		}
		return newPc;
	}

	/**
	 * Method getTopLine.
	 * 
	 * @param pc
	 * @return PacketCollection
	 */
	private PacketCollection getTopLine() {
		PacketCollection newPc = new PacketCollection();
		PacketCollection pc = ccursor.getPc();
		if (pc != null && pc.size() > 0) {
			int startIndex = 0;
			int endIndex = 0;

			// startIndex
			startIndex = pc.getTopNodePosition();

			// endIndex
			int i = startIndex;
			while ((i < pc.size()) && (!pc.isLineDelimiter(i))) {
				i++;
			}
			endIndex = i;
			if (startIndex == endIndex)
				endIndex += 1;
			ccursor.setCurPos(startIndex);
			// if (startIndex < pc.size() && endIndex < pc.size())
			newPc.addAll(pc.subList(startIndex, endIndex));
		}
		return newPc;
	}

	/**
	 * Method getBottomLine.
	 * 
	 * @param pc
	 * @return PacketCollection
	 */
	private PacketCollection getBottomLine() {
		PacketCollection newPc = new PacketCollection();
		PacketCollection pc = ccursor.getPc();
		if (pc != null && pc.size() > 0) {
			int startIndex = 0;
			int endIndex = 0;
			int pos = 0;

			// startIndex
			pos = pc.getBottomNodePosition();
			int i = pos;
			if (!pc.isLineDelimiter(i)) {
				while ((i < pc.size()) && (pc.isLineDelimiter(i)))
					i++;
			}
			startIndex = i;

			// endIndex
			i = startIndex;
			while ((i < pc.size() - 1) && (!pc.isLineDelimiter(i))) {
				i++;
			}
			endIndex = i;
			if (startIndex == endIndex)
				endIndex += 1;

			ccursor.setCurPos(startIndex);
			newPc.addAll(pc.subList(startIndex, endIndex));
		}
		return newPc;
	}

	private PacketCollection getCurLinkPC(boolean setpos) {
		PacketCollection newPc = new PacketCollection();
		PacketCollection pc = ccursor.getPc();
		if (pc != null && pc.size() > 0) {
			int startIndex = 0;
			int endIndex = 0;

			// startIndex
			startIndex = ccursor.getCurPos();
			for (int i = startIndex; i < pc.size(); i++) {
				if (pc.isLinkTag(i)) {
					startIndex = i;
					break;
				}
			}

			// endIndex
			endIndex = startIndex + 1;
			if (pc.isInsideAnchor(startIndex)) {
				for (int i = startIndex; i < pc.size(); i++) {
					if (!pc.isInsideAnchor(i)) {
						endIndex = i;
						break;
					}
				}
			}

			if (setpos)
				ccursor.setCurPos(startIndex);
			newPc.addAll(pc.subList(startIndex, endIndex));
		}
		return newPc;
	}

	/**
	 * Method setRange.
	 * 
	 * @param firstNode
	 * @param lastNode
	 */
	private void setRange(Node firstNode, Node lastNode) {
		if (firstNode != null && lastNode != null) {
			int numSelObservers = selObservers.size();
			for (int i = 0; i < numSelObservers; i++) {
				(selObservers.get(i)).setRange(firstNode, lastNode);
			}
		}
	}

	/**
	 * Method speakWithVisual.
	 * 
	 * @param pc
	 */
	private void speakWithVisual(PacketCollection pc) {
		String str;
		Node firstNode, lastNode;
		try {
			firstNode = pc.getFirstNode();
			lastNode = pc.getLastNode();
			str = render(pc);

		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return;
		} catch (NoSuchElementException nse) {
			speech.stop();
			if (OutLoud.endofpage != null && OutLoud.endofpage.length() > 0)
				speech.speak(OutLoud.endofpage, ITTSEngine.TTSFLAG_FLUSH, -1);
			else
				speech.speak("End of page.", ITTSEngine.TTSFLAG_FLUSH, -1);
			return;
		}

		if ((str != null) && (str.length() > 0)) {
			speech.stop();
			speech.speak(str, ITTSEngine.TTSFLAG_FLUSH, -1);
			setRange(firstNode, lastNode);
			drawText(str);
		}
	}

	/**
	 * Method render.
	 * 
	 * @param pc
	 * @return String
	 */
	public String render(PacketCollection pc) {
		int startPos = 0;
		try {
			int size = pc.size();
			StringBuffer result = new StringBuffer();
			for (int i = startPos; i < size; i++) {
				IPacket p = pc.get(i);
				String str = p.getText();

				if ((str != null) && (str.length() > 0)) {
					if (result.length() > 0) {
						char lastChar = result.charAt(result.length() - 1);
						if (lastChar != '\n')
							if (p.getContext().isStartSelect())
								result.append(BACKSLASH_N);
					}
					result.append(" "); //$NON-NLS-1$
					result.append(str);
				}

				if (result.length() > 0) {
					char lastChar = result.charAt(result.length() - 1);
					if (lastChar != '\n')
						if (p.getContext().isLineDelimiter()
								|| (pc.isLinkTag(i) && (i + 1 < pc.size() && pc
										.isLinkTag(i + 1))))
							result.append(BACKSLASH_N);
				}
			}
			return result.toString();
		} catch (NullPointerException npe) {
			return null;
		}
	}

	/**
	 * Method render.
	 * 
	 * @param pc
	 * @param startPos
	 * @return String
	 */
	public String render(PacketCollection pc, int startPos) {
		try {
			int size = pc.size();
			StringBuffer result = new StringBuffer();
			for (int i = startPos; i < size; i++) {
				IPacket p = pc.get(i);
				String str = p.getText();

				if ((str != null) && (str.length() > 0)
						&& p.getContext().isStringOutput()) {
					if (result.length() > 0) {
						char lastChar = result.charAt(result.length() - 1);
						if (lastChar != '\n')
							if (p.getContext().isStartSelect())
								result.append(BACKSLASH_N);
					}
					result.append(" "); //$NON-NLS-1$
					result.append(str);
				}

				if (result.length() > 0) {
					char lastChar = result.charAt(result.length() - 1);
					if (lastChar != '\n')
						if (p.getContext().isLineDelimiter()
								|| (pc.isLinkTag(i) && (i + 1 < pc.size() && pc
										.isLinkTag(i + 1))))
							result.append(BACKSLASH_N);
				}
			}
			return result.toString();
		} catch (NullPointerException npe) {
			return null;
		}
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#addView(IVoiceBrowserView)
	 */
	public void addView(IVoiceBrowserView view) {
		views.add(view);
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#removeView(IVoiceBrowserView)
	 */
	public void removeView(IVoiceBrowserView view) {
		views.remove(view);
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#addSelectionObserver(SelectionObserver)
	 */
	public void addSelectionObserver(SelectionObserver view) {
		selObservers.add(view);
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#removeSelectionObserver(SelectionObserver)
	 */
	public void removeSelectionObserver(SelectionObserver view) {
		selObservers.remove(view);
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#getPacketCollection()
	 */
	public PacketCollection getPacketCollection() {
		return ccursor.getPc();
	}

	/**
	 * Method drawText.
	 * 
	 * @param text
	 */
	void drawText(String text) {
		if (views == null)
			return;
		int numViews = views.size();
		for (int i = 0; i < numViews; i++) {
			IVoiceBrowserView tv = views.get(i);
			tv.drawText(text);
		}
	}

	/**
	 * Method drawAppendText.
	 * 
	 * @param text
	 */
	void drawAppendText(String text) {
		if (views == null)
			return;
		int numViews = views.size();
		for (int i = 0; i < numViews; i++) {
			IVoiceBrowserView tv = views.get(i);
			tv.drawAppendText(text);
		}
	}

	private Vector<CursorListener> cursorListeners = new Vector<CursorListener>();

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#addCursorListener(CursorListener)
	 */
	public void addCursorListener(CursorListener listener) {
		cursorListeners.addElement(listener);
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController#removeCursorListener(CursorListener)
	 */
	public void removeCursorListener(CursorListener listener) {
		cursorListeners.removeElement(listener);
	}

	@SuppressWarnings("unchecked")
	public void sendCursorEvent(CursorMovedEvent event) {
		Vector<CursorListener> listeners = (Vector<CursorListener>) cursorListeners
				.clone();
		Enumeration<CursorListener> enums = listeners.elements();
		while (enums.hasMoreElements()) {
			CursorListener listener = enums.nextElement();
			listener.doCursorMoved(event);
		}
	}

	public void setSpeechControl(ITTSEngine speech) {
		if (speech != null) {
			this.speech = speech;
		}
	}

}
