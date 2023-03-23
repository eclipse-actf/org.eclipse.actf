/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.ui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class to open dialog to enable users to specify target file name.
 */
@SuppressWarnings("nls")
public class DialogSave {

	/**
	 * The constant to target all files.
	 */
	public static final int ALL = 0;

	/**
	 * The constant to specify HTML files as a target.
	 */
	public static final int HTML = 1;

	/**
	 * The constant to specify Bitmap files as a target.
	 */
	public static final int BMP = 2;

	/**
	 * The constant to specify XML files as a target.
	 */
	public static final int XML = 3;

	/**
	 * The constant to specify CSV files as a target.
	 */
	public static final int CSV = 4;

	private static final int TYPE_MAX = 4;

	private static final int MAX_FILENAME_LENGTH = 100;

	private static final String[][] FILTER = { { "All Files (*.*)" },
			{ "HTML Files (*.htm, *.html)" }, { "BMP Files (*.bmp)" },
			{ "XML Files (*.xml)" }, {"CSV Files (*.csv)" } };

	private static final String[][] FILTER_EXT = { { "*.*" },
			{ "*.htm;*.html" }, { "*.bmp" }, { "*.xml" } , { "*.csv" }};

	private static final String[][] EXT_CHECK = { {}, { ".htm", ".html" },
			{ ".bmp" }, { ".xml" }, { ".csv" } };

	private static final String[] DEFAULT_EXT = { "", ".html", ".bmp", ".xml" , ".csv" };

	/**
	 * Open dialog to enable users to specify target file name to save.
	 * 
	 * @param shell
	 *            parent shell
	 * @param type
	 *            File Type (HTML or BMP)
	 * @param targetNameBase
	 *            file name to save
	 * @param ext
	 *            additional file name to be append to targetNameBase
	 * @return the resulting file name of the saved file
	 */
	public static String open(Shell shell, int type, String targetNameBase,
			String ext) {

		String saveFileName = null;
		if (type > -1 && type <= TYPE_MAX) {

			FileDialog fileD = new FileDialog(shell, SWT.SAVE);
			fileD.setFilterNames(FILTER[type]);
			fileD.setFilterExtensions(FILTER_EXT[type]);

			saveFileName = targetNameBase;
			if (saveFileName != null) {

				// TODO use System.property
				int iPos = saveFileName.indexOf("//");
				if (iPos != -1) {
					saveFileName = saveFileName.substring(iPos + 2);
				}
				// saveFileName = saveFileName.replace('/', '_');
				// saveFileName = saveFileName.replace('\\', '_');
				// saveFileName = saveFileName.replace(':', '_');
				// saveFileName = saveFileName.replace('?', '_');
				// saveFileName = saveFileName.replace('%', '_');
				saveFileName = saveFileName.replaceAll("\\p{Punct}", "_");

				if (saveFileName.indexOf(".") > 0) {
					saveFileName = saveFileName.substring(0, saveFileName
							.lastIndexOf("."));
				}

				if (saveFileName.length() > MAX_FILENAME_LENGTH) {
					saveFileName = saveFileName.substring(0,
							MAX_FILENAME_LENGTH);
				}
				if (null != ext) {
					saveFileName += ext;
				}
				fileD.setFileName(saveFileName);
				saveFileName = fileD.open();

				if (saveFileName != null) {
					// TODO check
					// if (saveFileName.endsWith(".htm")
					// || saveFileName.endsWith(".html")) {
					// saveFileName =
					// saveFileName.substring(
					// 0,
					// saveFileName.lastIndexOf("."));
					// }
					//
					// int pos =
					// saveFileName.lastIndexOf(ADesignerConst.DIR_SEP) + 1;
					// String strSub =
					// saveFileName.substring(pos, saveFileName.length());
					// if (strSub.length() > 100) {
					// saveFileName = saveFileName.substring(0, pos);
					// saveFileName += strSub.substring(0, 100);
					// }

					boolean flag = true;

					String tmpS = saveFileName.toLowerCase();
					for (int i = 0; i < EXT_CHECK[type].length; i++) {
						if (tmpS.endsWith(EXT_CHECK[type][i])) {
							flag = false;
						}
					}
					if (flag) {
						saveFileName += DEFAULT_EXT[type];
					}

				}

			}
		}
		return (saveFileName);
	}

}
