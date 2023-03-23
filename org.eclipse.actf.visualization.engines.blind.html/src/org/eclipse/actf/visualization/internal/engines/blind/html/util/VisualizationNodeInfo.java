/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind.html.util;

import org.eclipse.actf.visualization.engines.voicebrowser.IPacket;
import org.w3c.dom.Node;


/**
 * @author kentarou
 *
 */
public class VisualizationNodeInfo {

	//separated from VisualizeEngine
	
    private int orgTotalWords;

    private int orgTotalLines;

    private int orgTime = 0;

    private int totalWords;

    private int totalLines;

    private int words;

    private int lines;

    private int id;

    private int packetId;

    private IPacket packet = null;

    private Node node = null; // only when packet is null

    private boolean isHeading = false;
    
    private boolean isLandmark = false; //for html5
    
    private boolean isCaption = false; //caption/figcaption

	private boolean tableHeader = false;

    private boolean isLabel = false;

    private boolean isIdRequiredInput = false;

    private boolean isSequence = false;

    private boolean isBlockElement = false;

    private boolean isInvisible = false;

    private int time = 0;

    private String comment = ""; //$NON-NLS-1$

    VisualizationNodeInfo() {
        //node = null;
        totalWords = 0;
        totalLines = 0;
        words = 0;
        lines = 0;

        //
        id = 0;
        packetId = 0;
        packet = null;
        node = null;
    }

    VisualizationNodeInfo(VisualizationNodeInfo info) {
        if (info != null) {
            totalWords = info.getTotalWords();
            totalLines = info.getTotalLines();
            words = info.getWords();
            lines = info.getLines();

            packet = info.getPacket();
            packetId = info.getPacketId();

            if (packet == null) {
                node = info.getNode();
            }

            //TODO if parent, use comment
            //comment = info.comment;
            comment = ""; //$NON-NLS-1$

            //
            id = 0;

            if ((packet == null) && (node != null)) {
            	//TODO
                //System.out.println("115: to be fixed");
            }
        } else {
            //node = null;
            totalWords = 0;
            totalLines = 0;
            words = 0;
            lines = 0;

            //
            id = 0;
            packetId = 0;
            packet = null;
            node = null;
            comment = ""; //$NON-NLS-1$
        }
    }

    /**
     * Returns the node.
     * 
     * @return Node
     */
    public Node getNode() {
        //return node;
        if (packet != null) {
            return packet.getNode();
        } else {
            if (node == null) {
            	//TODO
                //System.err.println("VisualizationNodeInfo.getNode: to be fixed");
            }
            return node;
        }
    }

    /**
     * Returns the totalWords.
     * 
     * @return int
     */
    public int getTotalWords() {
        return totalWords;
    }

    /**
     * Sets the node.
     * 
     * @param node
     *            The node to set
     */
    public void setNode(Node node) {

        /*
         * original code
         * 1. NodeInfoMap.get(targetNode)  (get nodeinfo)
         * 2-a. nodeinfo != null -> setPacket(nodeinfo.getPacket()) 
         * 2-b. nodeinfo == null -? setPacket(Null) & setNode(targetNode)
         *  
         */

        packet = null;//important!  //TODO modify getNode?
        this.node = node;
    }

    /**
     * Sets the totalWords.
     * 
     * @param totalWords
     *            The totalWords to set
     */
    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    /**
     * Returns the lines.
     * 
     * @return int
     */
    public int getLines() {
        return lines;
    }

    /**
     * Returns the totalLines.
     * 
     * @return int
     */
    public int getTotalLines() {
        return totalLines;
    }

    /**
     * Returns the words.
     * 
     * @return int
     */
    public int getWords() {
        return words;
    }

    /**
     * Sets the lines.
     * 
     * @param lines
     *            The lines to set
     */
    public void setLines(int lines) {
        this.lines = lines;
    }

    /**
     * Sets the totalLines.
     * 
     * @param totalLines
     *            The totalLines to set
     */
    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }

    /**
     * Sets the words.
     * 
     * @param words
     *            The words to set
     */
    public void setWords(int words) {
        this.words = words;
    }

    /**
     * Returns the id.
     * 
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            The id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the packetId.
     * 
     * @return int
     */
    public int getPacketId() {
        return packetId;
    }

    /**
     * Sets the packetId.
     * 
     * @param packetId
     *            The packetId to set
     */
    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
	public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(" Info=");
        sb.append(this.getTime() + " : ");
        sb.append(this.getTotalWords());
        sb.append(",");
        sb.append(this.getWords());
        sb.append(",");
        sb.append(this.getTotalLines());
        sb.append(",");
        sb.append(this.getLines());
        sb.append(",");
        sb.append(this.getNode());
        return sb.toString();
    }

    /**
     * Returns the packet.
     * 
     * @return Packet
     */
    public IPacket getPacket() {
        return packet;
    }

    /**
     * Sets the packet.
     * 
     * @param packet
     *            The packet to set
     */
    public void setPacket(IPacket packet) {
        this.packet = packet;
    }

    /**
     * Returns the heading.
     * 
     * @return boolean
     */
    public boolean isHeading() {
        return isHeading;
    }

    /**
     * Sets the heading.
     * 
     * @param heading
     *            The heading to set
     */
    public void setHeading(boolean heading) {
        this.isHeading = heading;
    }

    /**
     * Returns true if the node is landmark
     * @return boolean
     */
    public boolean isLandmark() {
		return isLandmark;
	}

	public boolean isCaption() {
		return isCaption;
	}

	public void setCaption(boolean isCaption) {
		this.isCaption = isCaption;
	}

	/**
	 * @param isLandmark
	 */
	public void setLandmark(boolean isLandmark) {
		this.isLandmark = isLandmark;
	}

    
    /**
     * Returns the time.
     * 
     * @return int
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets the time.
     * 
     * @param time
     *            The time to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param string
     */
    public void appendComment(String string) {

        //TODO duplicate check
        if (comment.length() != 0) {
            //			System.out.println("appendComment: " +comment+" + "+string);
            comment = comment + " " + string; //$NON-NLS-1$
        } else {
            comment = string;
        }
    }

    /**
     * @return
     */
    public boolean isTableHeader() {
        return tableHeader;
    }

    /**
     * @param b
     */
    public void setTableHeader(boolean b) {
        tableHeader = b;
    }

    /**
     * @return
     */
    public boolean isLabel() {
        return isLabel;
    }

    /**
     * @param b
     */
    public void setLabel(boolean b) {
        isLabel = b;
    }

    /**
     * @return
     */
    public boolean isIdRequiredInput() {
        return isIdRequiredInput;
    }

    /**
     * @param b
     */
    public void setIdRequiredInput(boolean b) {
        isIdRequiredInput = b;
    }

    /**
     * @param hasAccesskey
     */
    public void setAccesskey(boolean hasAccesskey) {
    }

    /**
     * @return
     */
    public boolean isSequence() {
        return isSequence;
    }

    /**
     * @param b
     */
    public void setSequence(boolean b) {
        isSequence = b;
    }

    /**
     * @return
     */
    public boolean isBlockElement() {
        return isBlockElement;
    }

    /**
     * @param b
     */
    public void setBlockElement(boolean b) {
        isBlockElement = b;
    }

    /**
     * @return Returns the isInvisible.
     */
    public boolean isInvisible() {
        return isInvisible;
    }

    /**
     * @param isInvisible
     *            The isInvisible to set.
     */
    public void setInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    public int getOrgTime() {
        return orgTime;
    }

    public int getOrgTotalLines() {
        return orgTotalLines;
    }

    public int getOrgTotalWords() {
        return orgTotalWords;
    }

    public void setOrgTime(int orgTime) {
        this.orgTime = orgTime;
    }

    public void setOrgTotalLines(int orgTotalLines) {
        this.orgTotalLines = orgTotalLines;
    }

    public void setOrgTotalWords(int orgTotalWords) {
        this.orgTotalWords = orgTotalWords;
    }

}
