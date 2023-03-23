/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.blind.html;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Interface to provide mapping information between original {@link Document}
 * and visualization result {@link Document}
 */
public interface IVisualizeMapData {

	/**
	 * @return map between {@link Node} in original Document to corresponding
	 *         result {@link Node} ID
	 */
	public abstract Map<Node, Integer> getOrig2idMap();

	/**
	 * Add replaced {@link Node} information.
	 * 
	 * @param target
	 *            target Node to replace
	 * @param replacement
	 *            replacement Node
	 */
	public abstract void addReplacedNodeMapping(Node target, Node replacement);

	/**
	 * Get corresponding {@link Node} from original {@link Document}
	 * 
	 * @param result
	 *            target Node in result {@link Document}
	 * @return corresponding original Node
	 */
	public abstract Node getOrigNode(Node result);

	/**
	 * Get corresponding {@link Node} from result {@link Document}
	 * 
	 * @param orig
	 *            target Node in original {@link Document}
	 * @return corresponding result Node
	 */
	public abstract Node getResultNode(Node orig);

	/**
	 * Get replacement {@link Node} of target {@link Node}
	 * 
	 * @param target
	 *            target {@link Node}
	 * @return replacement Node
	 */
	public abstract Node getReplacement(Node target);

	/**
	 * Get ID of target {@link Node} in result {@link Document}
	 * 
	 * @param target
	 *            target {@link Node}
	 * @return ID of target Node
	 */
	public abstract Integer getIdOfNode(Node target);

	/**
	 * Get ID of target {@link Node} in original {@link Document}
	 * 
	 * @param target
	 *            target {@link Node}
	 * @return ID of target Node
	 */
	public abstract Integer getIdOfOrigNode(Node target);

}