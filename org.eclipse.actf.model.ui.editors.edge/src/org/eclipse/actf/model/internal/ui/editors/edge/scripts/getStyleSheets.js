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
 
	function parse(styleSheet) {
		try {
			const rules = [], imports = [];
			for (const cssRule of styleSheet.cssRules) {
				rules.push(cssRule.cssText);
				if ('styleSheet' in cssRule) {
					imports.push(parse(cssRule.styleSheet));
				}
			}
			return [styleSheet.title, styleSheet.href, rules, imports];
		} catch (e) {
			return [styleSheet.title, styleSheet.href, e.message];
		}
	}

	return Array.from(document.styleSheets).map(parse);
})();