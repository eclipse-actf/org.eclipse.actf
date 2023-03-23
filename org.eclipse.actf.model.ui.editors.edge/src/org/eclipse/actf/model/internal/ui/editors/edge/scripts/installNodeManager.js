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

	window.__ACTF_NodeManager__ = function() {
		const array = [];

		function nodeToHandle(node) {
			const handle = array.indexOf(node);
			return handle != -1 ? handle : array.push(node) - 1;
		}

		function handleToNode(handle) {
			const node = array[handle];
			return document.contains(node) ? node : null;
		}

		function size() {
			return array.length;
		}

		function clear() {
			array.length = 0;
		}

		return {
			'nodeToHandle': nodeToHandle,
			'handleToNode': handleToNode,
			'size': size,
			'clear': clear
		};
	}();
})();