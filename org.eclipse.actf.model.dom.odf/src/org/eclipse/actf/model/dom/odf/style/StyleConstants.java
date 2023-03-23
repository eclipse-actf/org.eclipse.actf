/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.model.dom.odf.style;

/**
 * StyleConstants defines the constant values used by style namespace of ODF DOM API
 */
@SuppressWarnings("nls")
public final class StyleConstants {
	static public final String STYLE_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:style:1.0";

	static public final String ELEMENT_DEFAULT_STYLE = "default-style";

	static public final String ELEMENT_STYLE = "style";

	static public final String ELEMENT_TABLE_PROPERTIES = "table-properties";

	static public final String ELEMENT_TABLE_COLUMN_PROPERTIES = "table-column-properties";

	static public final String ELEMENT_TABLE_ROW_PROPERTIES = "table-row-properties";

	static public final String ELEMENT_TABLE_CELL_PROPERTIES = "table-cell-properties";

	static public final String ELEMENT_FONT_FACE = "font-face";

	static public final String ELEMENT_TEXT_PROPERTIES = "text-properties";

	static public final String ELEMENT_CHART_PROPERTIES = "chart-properties";

	static public final String ELEMENT_GRAPHIC_PROPERTIES = "graphic-properties";

	static public final String ELEMENT_PARAGRAPH_PROPERTIES = "paragraph-properties";

	static public final String ELEMENT_PAGE_LAYOUT = "page-layout";

	static public final String ELEMENT_PAGE_LAYOUT_PROPERTIES = "page-layout-properties";

	static public final String ELEMENT_DRAWING_PAGE_PROPERTIES = "drawing-page-properties";

	static public final String ELEMENT_MASTER_PAGE = "master-page";

	static public final String ELEMENT_MAP = "map";

	static public final String ATTR_NAME = "name";

	static public final String ATTR_DIRECTION = "direction";

	static public final String ATTR_FONT_PITCH = "font-pitch";

	static public final String ATTR_FAMILY = "family";

	static public final String ATTR_MASTER_PAGE_NAME = "master-page-name";

	static public final String ATTR_PARENT_STYLE_NAME = "parent-style-name";

	static public final String ATTR_DATA_STYLE_NAME = "data-style-name";

	static public final String ATTR_PAGE_LAYOUT_NAME = "page-layout-name";

	static public final String ATTR_COLUMN_WIDTH = "column-width";

	static public final String ATTR_USE_OPTIMAL_COLUMN_WIDTH = "use-optimal-column-width";

	static public final String ATTR_WRITING_MODE = "writing-mode";

	static public final String ATTR_ROW_HEIGHT = "row-height";

	static public final String ATTR_USE_OPTIMAL_ROW_HEIGHT = "use-optimal-row-height";

	static public final String ATTR_FONT_NAME = "font-name";

	static public final String ATTR_FONT_FAMILY_GENERIC = "font-family-generic";

	static public final String ATTR_FONT_FAMILY_ASIAN = "font-family-asian";

	static public final String ATTR_FONT_PITCH_ASIAN = "font-pitch-asian";

	static public final String ATTR_FONT_SIZE_ASIAN = "font-size-asian";

	static public final String ATTR_FONT_FAMILY_COMPLEX = "font-family-complex";

	static public final String ATTR_FONT_PITCH_COMPLEX = "font-pitch-complex";

	static public final String ATTR_FONT_SIZE_COMPLEX = "font-size-complex";

	static public final String ATTR_REPEAT = "repeat";

	static public final String ATTR_PRINT_ORIENTATION = "print-orientation";

	static public final String ATTR_SHADOW = "shadow";

	static public final String ATTR_CONDITION = "condition";

	static public final String ATTR_APPLY_STYLE_NAME = "apply-style-name";

	static public final String ATTR_BASE_CELL_ADDRESS = "base-cell-address";

	static public final String ATTR_TEXT_UNDERLINE_STYLE = "text-underline-style";

	static public final String ATTR_TEXT_POSITION = "text-position";

	static public final String ATTR_NUM_FORMAT = "num-format";

	static public final String ATTR_NUM_PREFIX = "num-prefix";

	static public final String ATTR_NUM_SUFFIX = "num-suffix";

	static public final String ATTR_WIDTH = "width";

	static public final String ATTR_VERTICAL_ALIGN = "vertical-align";

	static public final String ATTR_HORIZONTAL_POS = "horizontal-pos";

	static public final String ATTR_CELL_PROTECT = "cell-protect";

	static public final int CELL_PROTECT_VALUE_NOT_DEFINED = 0;

	static public final int CELL_PROTECT_VALUE_NONE = 1;

	static public final int CELL_PROTECT_VALUE_PROTECTED = 2;

	static public final int CELL_PROTECT_VALUE_HIDDEN = 3;

	static public final int CELL_PROTECT_VALUE_HIDDEN_AND_PROTECTED = 4;
}