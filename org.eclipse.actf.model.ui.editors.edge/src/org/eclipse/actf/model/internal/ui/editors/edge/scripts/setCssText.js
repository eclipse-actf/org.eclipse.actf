(function(handle, text, add) {

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
		return;
	}
	const node = __ACTF_NodeManager__.handleToNode(handle);
	if (node) {
		const style = node.style;
		const current = style.cssText.trim();
		const exists = current.indexOf(text) != -1;
		if (add && !exists) {
			if (current.length > 0 && !current.endsWith(';')) {
				style.cssText += ';';
			}
			style.cssText += text;
		} else if (!add && exists) {
			style.cssText = style.cssText.replace(text, '');
		}
	}
})(__ARGS__);