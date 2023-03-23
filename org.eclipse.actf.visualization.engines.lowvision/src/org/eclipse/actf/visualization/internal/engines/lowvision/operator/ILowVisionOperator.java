/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision.operator;

import java.awt.image.BufferedImage;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;

interface ILowVisionOperator {
	abstract BufferedImage filter( BufferedImage _src, BufferedImage _dest ) throws LowVisionException;
}
