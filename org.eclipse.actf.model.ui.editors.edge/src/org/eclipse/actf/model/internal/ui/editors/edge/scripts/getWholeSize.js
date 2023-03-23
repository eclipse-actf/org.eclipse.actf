(function() {

/*******************************************************************************
 * Copyright (c) 2022, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/ 

	const bodyWidth = document.body.scrollWidth + document.body.offsetLeft * 2;
	const bodyHeight = document.body.scrollHeight + document.body.offsetTop * 2;
	return [
		Math.max(document.documentElement.scrollWidth, bodyWidth),
		Math.max(document.documentElement.scrollHeight, bodyHeight)
	];
})();