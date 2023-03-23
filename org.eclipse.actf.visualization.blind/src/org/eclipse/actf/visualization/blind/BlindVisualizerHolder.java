/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.blind;

import org.eclipse.actf.visualization.blind.internal.BlindVisualizerExtension;

/**
 * Provide access to the blind visualizers that registered via an extension point
 * "org.eclipse.actf.visualization.blind.blindVisualizer".
 */
public class BlindVisualizerHolder {
	
	/**
	 * Get registered blind visualizers.
	 * 
	 * @return registered blind visualizers
	 * @see IBlindVisualizer
	 */
	public static IBlindVisualizer[] getVisualizers(){
		return BlindVisualizerExtension.getVisualizers();
	}

}
