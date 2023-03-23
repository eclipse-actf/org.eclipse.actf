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

	if (!window.__ACTF_NodeManager__) {
		return [];
	}
	const results = [];
	for (const img of document.getElementsByTagName('img')) {
		try {
			const rect = [0, 0, img.offsetWidth, img.offsetHeight];
			for (let n = img; n; n = n.offsetParent) {
				rect[0] += n.offsetLeft;
				rect[1] += n.offsetTop;
			}
			results.push([__ACTF_NodeManager__.nodeToHandle(img), rect, img.src]);
		} catch (e) {
			console.error(e);
		}
	}
	return results;
})();