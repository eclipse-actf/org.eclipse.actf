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
	const STYLE_NAMES = ['visibility', 'textDecoration', 'textAlign', 'position', 'lineHeight', 'letterSpacing', 'fontVariant',
		'fontStyle', 'fontSize', 'fontFamily', 'display', 'color', 'backgroundRepeat', 'backgroundColor', 'backgroundImage', 'opacity'];
	const REJECT_PAT = /^(HEAD|SCRIPT|STYLE|SVG)$/i;
	const TRANSPARENT_PAT = /^(transparent|rgba\(0, *0, *0, *0\))$/i;
	const NONE_PAT = /^none$/i;

	const results = [];
	const mainWalker = document.createTreeWalker(document.documentElement, NodeFilter.SHOW_ELEMENT, n => {
		return n.tagName.match(REJECT_PAT) ? NodeFilter.FILTER_REJECT : NodeFilter.FILTER_ACCEPT;
	});
	const dummy = document.createElement('div');
	dummy.style.display = 'none';
	dummy.style.backgroundColor = 'rgb(255,255,255)';
	document.documentElement.append(dummy);
	const whiteBgColor = getComputedStyle(dummy).backgroundColor;
	dummy.remove();

	for (let node; node = mainWalker.nextNode();) {
		results.push([
			__ACTF_NodeManager__.nodeToHandle(node),
			getXpath(node),
			node.tagName,
			getTarget(node),
			getRect(node),
			getStyles(node),
			getChildTexts(node),
			getDescendantTextsWithBGImage(node)
		]);
	}
	return results;

	function getStyles(node) {
		const css = getComputedStyle(node);
		let cascadeColor = css.color;
		let cascadeBackgroundColor = css.backgroundColor;
		let cascadeBackgroundImage = css.backgroundImage;
		let fgFlag = cascadeColor.match(TRANSPARENT_PAT);
		let bgFlag = cascadeBackgroundColor.match(TRANSPARENT_PAT);
		let bgImgFlag = bgFlag && cascadeBackgroundImage.match(NONE_PAT);
		for (let parent = node.parentNode; parent && parent != document && (fgFlag || bgFlag); parent = parent.parentNode) {
			const s = getComputedStyle(parent);
			if (fgFlag && !s.color.match(TRANSPARENT_PAT)) {
				cascadeColor = s.color;
				fgFlag = false;
			}
			if (bgImgFlag && !s.backgroundImage.match(NONE_PAT)) {
				cascadeBackgroundImage = s.backgroundImage;
				bgImgFlag = false;
			}
			if (bgFlag && !s.backgroundColor.match(TRANSPARENT_PAT)) {
				cascadeBackgroundColor = s.backgroundColor;
				bgFlag = bgImgFlag = false;
			}
		}

		if (bgFlag) {
			cascadeBackgroundColor = whiteBgColor;
		}

		return STYLE_NAMES.map(name => [name, css[name]]).concat([
			['cascadeColor', cascadeColor],
			['cascadeBackgroundColor', cascadeBackgroundColor],
			['cascadeBackgroundImage', cascadeBackgroundImage]
		]);
	}

	function getChildTexts(node) {
		const results = [];
		for (const child of node.childNodes) {
			child.nodeName == '#text' && child.nodeValue.trim() && results.push(child.nodeValue);
		}
		if (results.length == 0 && node.tagName.match(/^INPUT$/i) && node.type.match(/^(BUTTON|SUBMIT|RESET)$/i)) {
			node.value.trim() && results.push(node.value);
		}
		return results;
	}

	function getDescendantTextsWithBGImage(node) {
		const results = [];
		const css = getComputedStyle(node);
		if (!css.backgroundImage.match(NONE_PAT)) {
			const bgImageWalker = document.createTreeWalker(node, NodeFilter.SHOW_ALL, n => {
				if (n.nodeName == '#text') {
					return n.nodeValue.trim() ? NodeFilter.FILTER_ACCEPT : NodeFilter.FILTER_SKIP;
				} else if (n.nodeType == Node.ELEMENT_NODE) {
					const c = getComputedStyle(n);
					if (!c.backgroundImage.match(NONE_PAT) || !c.backgroundColor.match(TRANSPARENT_PAT)) {
						return NodeFilter.FILTER_REJECT;
					}
				}
				return NodeFilter.FILTER_SKIP;
			});
			for (let n; n = bgImageWalker.nextNode();) {
				results.push(n.nodeValue);
			}
		}
		return results;
	}

	function getTarget(node) {
		return node.tagName.match(/^A$/i) && node.href && new URL(node.href, location.href).href;
	}

	function getRect(node) {
		if (isNaN(node.offsetWidth)) {
			// console.error(node);
			return null;
		}
		const rect = [0, 0, node.offsetWidth, node.offsetHeight];
		for (let n = node; n; n = n.offsetParent) {
			rect[0] += n.offsetLeft;
			rect[1] += n.offsetTop;
		}
		return rect;
	}

	function getXpath(element) {
		if (element && element.parentNode) {
			let xpath = getXpath(element.parentNode) + '/' + element.tagName;
			let count = 0, index;
			for (const sibling of element.parentNode.childNodes) {
				if (sibling.tagName == element.tagName) {
					count++;
					if (sibling == element) {
						index = count;
					}
				}
			}
			if (count > 1) {
				xpath += '[' + (index) + ']';
			}
			return xpath.toLowerCase();
		} else {
			return '';
		}
	}
})();