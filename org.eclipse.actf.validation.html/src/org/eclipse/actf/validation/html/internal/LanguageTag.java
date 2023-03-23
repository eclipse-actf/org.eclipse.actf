/*******************************************************************************
 * Copyright (c) 2010,2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.validation.html.internal;

import java.util.Vector;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LanguageTag {
	// TODO to add more regular tags
	private static final String[] GRANDFATHERED_TAGS = { "en-GB-oed", "i-ami",
			"i-bnn", "i-default", "i-enochian", "i-hak", "i-klingon", "i-lux",
			"i-mingo" };

	private static final String PRIM_LANG_ISO639 = "[a-z]{2,3}";
	private static final String PRIM_LANG_RESERVED = "[a-z]{4}";
	private static final String PRIM_LANG_REGISTERED = "[a-z]{5,8}";

	private static final String EXT_LANG_PART = "-[a-z]{3}";
	private static final String EXT_LANG_PART_PAREN = "-([a-z]{3})";
	private static final String EXT_LANG = "(?:" + EXT_LANG_PART + "){0,3}";

	private static final String PRIM_LANG = PRIM_LANG_ISO639 + EXT_LANG + "|"
			+ PRIM_LANG_RESERVED + "|" + PRIM_LANG_REGISTERED;

	private static final String SCRIPT = "-[a-z]{4}";
	private static final String SCRIPT_PAREN = "-([a-z]{4})";

	private static final String REGION_ISO = "-[a-z]{2}";
	private static final String REGION_ISO_PAREN = "-([a-z]{2})";
	private static final String REGION_UN = "-\\d{3}";
	private static final String REGION_UN_PAREN = "-(\\d{3})";
	private static final String REGION = REGION_ISO + "|" + REGION_UN;
	private static final String REGION_PAREN = REGION_ISO_PAREN + "|"
			+ REGION_UN_PAREN;

	private static final String VARIANT_LONG = "-[a-z0-9]{5,8}";
	private static final String VARIANT_LONG_PAREN = "-([a-z0-9]{5,8})";
	private static final String VARIANT_SHORT = "-\\d[a-z0-9]{3}";
	private static final String VARIANT_SHORT_PAREN = "-(\\d[a-z0-9]{3})";
	private static final String VARIANT = VARIANT_LONG + "|" + VARIANT_SHORT;
	private static final String VARIANT_PAREN = VARIANT_LONG_PAREN + "|"
			+ VARIANT_SHORT_PAREN;

	private static final String EXTENSION = "-[a-wyz](?:-[a-z0-9]{2,8})+";
	private static final String EXTENSION_PAREN = "-([a-wyz](?:-[a-z0-9]{2,8})+)";

	private static final String PRIVATE_PART = "x(?:-[a-z0-9]{1,8})+";
	private static final String PRIVATE = "-" + PRIVATE_PART;
	private static final String PRIVATE_PAREN = "-x((?:-[a-z0-9]{1,8})+)";

	private static final Pattern syntax = Pattern.compile("^"
			+ addParen(PRIM_LANG) + addOptional(SCRIPT) + addOptional(REGION)
			+ addStar(VARIANT) + addStar(EXTENSION) + addOptional(PRIVATE)
			+ "$", Pattern.CASE_INSENSITIVE);

	private boolean wellFormed = false;
	private boolean valid = false;
	private boolean grandfathered = false;
	private boolean entirePrivate = false;

	private boolean isoPrimLan = false;
	private boolean resvPrimLan = false;
	private boolean regPrimLan = false;

	private boolean isoRegion = false;
	private boolean unRegion = false;

	private String tagString;
	private MatchResult result;
	private String primaryLanguage;
	private String extendedLanguage;
	private Vector<String> extendedLanguages;

	private String script;
	private String region;
	private String variantString;
	private Vector<String> variants;
	private String extensionString;
	private Vector<String> extensions;
	private String privateUse;

	private static String addParen(String exp) {
		return "(" + exp + ")";
	}

	private static String addOptional(String exp) {
		return "(" + exp + ")?";
	}

	private static String addStar(String exp) {
		return "((?:" + exp + ")*)";
	}

	public LanguageTag(String tagString, boolean parse) {
		this.tagString = tagString;
		for (String entry : GRANDFATHERED_TAGS) {
			if (tagString.matches(entry)) {
				wellFormed = true;
				grandfathered = true;
				return;
			}
		}

		if (tagString.matches(PRIVATE_PART)) {
			wellFormed = true;
			entirePrivate = true;
			return;
		}

		Matcher m = syntax.matcher(this.tagString);
		wellFormed = m.matches();
		if (!wellFormed)
			return;

		if (!parse)
			return;

		// when matched
		result = m.toMatchResult();
		primaryLanguage = result.group(1);
		script = result.group(2);
		region = result.group(3);
		variantString = result.group(4);
		extensionString = result.group(5);
		privateUse = result.group(6);

		processPrimLang();
		processScript();
		processRegion();
		processVariant();
		processExtension();
		processPrivateUse();
	}

	// post-processing
	private void processPrimLang() {
		Matcher m = Pattern.compile(
				addParen(addParen(PRIM_LANG_ISO639) + addParen(EXT_LANG)) + "|"
						+ addParen(PRIM_LANG_RESERVED) + "|"
						+ addParen(PRIM_LANG_REGISTERED),
				Pattern.CASE_INSENSITIVE).matcher(primaryLanguage);
		m.matches();
		if (m.group(1) != null) {
			isoPrimLan = true;
			primaryLanguage = m.group(2);
			if (m.group(3) != null && m.group(3).length() > 0) {
				extendedLanguage = m.group(3);
				processExtLang();
			}
		}
		if (m.group(4) != null)
			resvPrimLan = true;
		if (m.group(5) != null)
			regPrimLan = true;
	}

	private void processExtLang() {
		Matcher m = Pattern.compile(EXT_LANG_PART_PAREN,
				Pattern.CASE_INSENSITIVE).matcher(extendedLanguage);
		extendedLanguages = new Vector<String>();
		while (m.find()) {
			extendedLanguages.add(m.group(1));
		}
		extendedLanguage = extendedLanguages.elementAt(0);
	}

	private void processScript() {
		if (script != null && script.length() > 0) {
			Matcher m = Pattern.compile(SCRIPT_PAREN, Pattern.CASE_INSENSITIVE)
					.matcher(script);
			m.matches();
			script = m.group(1);
		} else {
			script = null;
		}
	}

	private void processRegion() {
		if (region != null && region.length() > 0) {
			Matcher m = Pattern.compile(REGION_PAREN, Pattern.CASE_INSENSITIVE)
					.matcher(region);
			m.matches();
			if (m.group(1) != null) {
				isoRegion = true;
				region = m.group(1);
			}
			if (m.group(2) != null) {
				unRegion = true;
				region = m.group(2);
			}
		} else {
			region = null;
		}
	}

	private void processVariant() {
		if (variantString != null && variantString.length() > 0) {
			variants = new Vector<String>();
			Matcher m = Pattern
					.compile(VARIANT_PAREN, Pattern.CASE_INSENSITIVE).matcher(
							variantString);
			while (m.find()) {
				if (m.group(1) != null)
					variants.add(m.group(1));
				if (m.group(2) != null)
					variants.add(m.group(2));
			}
		} else
			variantString = null;
	}

	private void processExtension() {
		if (extensionString != null && extensionString.length() > 0) {
			extensions = new Vector<String>();
			Matcher m = Pattern.compile(EXTENSION_PAREN,
					Pattern.CASE_INSENSITIVE).matcher(extensionString);
			while (m.find()) {
				extensions.add(m.group(1));
			}
		} else
			extensionString = null;
	}

	private void processPrivateUse() {
		if (privateUse != null && privateUse.length() > 0) {
			Matcher m = Pattern
					.compile(PRIVATE_PAREN, Pattern.CASE_INSENSITIVE).matcher(
							privateUse);
			m.matches();
			privateUse = m.group(1);
		} else
			privateUse = null;
	}

	//
	// getters and setters
	//

	public boolean isWellFormed() {
		return wellFormed;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isGrandfathered() {
		return grandfathered;
	}

	public boolean isEntirePrivate() {
		return entirePrivate;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public String getExtendedLanguage() {
		return extendedLanguage;
	}

	public Vector<String> getExtendedLanguages() {
		return extendedLanguages;
	}

	public String getScript() {
		return script;
	}

	public String getRegion() {
		return region;
	}

	public String getVariantString() {
		return variantString;
	}

	public Vector<String> getVariants() {
		return variants;
	}

	public String getExtensionString() {
		return extensionString;
	}

	public Vector<String> getExtensions() {
		return extensions;
	}

	public String getPrivateUse() {
		return privateUse;
	}

	public boolean isIsoPrimaryLang() {
		return isoPrimLan;
	}

	public boolean isReservedPrimaryLang() {
		return resvPrimLan;
	}

	public boolean isRegisteredPrimaryLang() {
		return regPrimLan;
	}

	public boolean isIsoRegion() {
		return isoRegion;
	}

	public boolean isUnRegion() {
		return unRegion;
	}

}
