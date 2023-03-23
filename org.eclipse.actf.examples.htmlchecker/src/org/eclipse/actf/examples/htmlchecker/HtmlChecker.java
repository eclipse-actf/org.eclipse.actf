/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.examples.htmlchecker;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.actf.examples.htmlchecker.eval.VisualizeAndCheck;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.guideline.IGuidelineData;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.service.datalocation.Location;

public class HtmlChecker implements IApplication {

	private static final String NOT_FIRST = "notFirst";
	PrintWriter logPW = null;
	SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd_hhmm");
	String dateS = dateFormat.format(new Date());

	// execution dir
	File currentDir = new File(System.getProperties().getProperty("user.dir"));

	// htmlchecker's dir
	Location installLoc = Platform.getInstallLocation();

	public Object start(IApplicationContext context) throws Exception {

		// ---prep from here---
		File appDir = new File(installLoc.getURL().toURI());

		File logFile = new File(appDir, "log.txt");
		try {
			logPW = new PrintWriter(logFile);
		} catch (Exception e) {
		}

		File resultDir = new File(appDir, "result");
		if (!resultDir.exists()) {
			try {
				resultDir.mkdir();
			} catch (Exception e) {
			}
		}

		if (!resultDir.isDirectory() || !resultDir.canWrite()) {
			logPrintln("Can't use result folder ("
					+ resultDir.getAbsolutePath() + "). Please check it.");
			logClose();
			return EXIT_OK;
		}

		File htmllistFile = new File(appDir, "htmllist.txt");

		String[] argv = Platform.getApplicationArgs();
		boolean fileFlag = false;
		for (String s : argv) {
			if (s.matches("-f")) {
				fileFlag = true;
			} else if (fileFlag) {
				File tmpFile;
				if (s.contains(":")) {
					tmpFile = new File(s);
				} else {
					tmpFile = new File(currentDir, s);
				}
				if (tmpFile.isFile() && tmpFile.canRead()) {
					htmllistFile = tmpFile;
				} else {
					logPrintln("File does not exist \"" + s
							+ "\". Will use default file ("
							+ htmllistFile.getAbsolutePath() + ") instead.");
				}
				fileFlag = false;
			}
		}

		File[] targetFiles = readHtmlList(htmllistFile);

		// Enable JIS(A,AA) and WCAG 2.0 (A,AA) at first launch
		initGuidelines();

		// ---prep end here---

		// Utility for visualization and check
		VisualizeAndCheck vac = new VisualizeAndCheck();

		PrintWriter listPW;
		try {
			listPW = new PrintWriter(new OutputStreamWriter(
					new BufferedOutputStream(new FileOutputStream(new File(
							resultDir, dateS + "_list.csv"))),
					// "UTF-8");
					"Shift_JIS"));
			// TODO Excel can't open UTF-8 encoded CSV file that
			// includes Japanese characters.

		} catch (Exception e) {
			listPW = new PrintWriter(System.out);
		}
		listPW.println("Target HTML file,Result CSV file");

		int counter = 1;
		for (File f : targetFiles) {
			if (f.isFile() && f.canRead()) {
				File resultFile = new File(resultDir, dateS + "_" + counter
						+ ".csv");
				if (!resultFile.exists()
						|| (resultFile.isFile() && resultFile.canWrite())) {
					vac.doEvaluate(f, resultFile);
					listPW.println("\"" + f.getAbsolutePath() + "\",\""
							+ resultFile.getAbsolutePath() + "\"");
				} else {
					logPrintln("Can't write result file ("
							+ resultFile.getAbsolutePath() + ")");
					listPW.println("\"" + f.getAbsolutePath()
							+ "\",\"Can't create result file ("
							+ resultFile.getAbsolutePath() + ").\"");
				}
			} else {
				logPrintln("File can't read: " + f.getAbsolutePath());
				listPW.println("\"" + f.getAbsolutePath()
						+ "\",\"Can't read target HTML file.\"");
			}
			counter++;
		}

		listPW.flush();
		listPW.close();

		logClose();
		return EXIT_OK;
	}

	public void stop() {
	}

	private void logPrintln(String logS) {
		if (logPW != null) {
			logPW.println(logS);
		}
	}

	private void logClose() {
		if (logPW != null) {
			logPW.flush();
			logPW.close();
		}
	}

	/**
	 * @param HTMLlist
	 *            file
	 * @return target HTML file list
	 */
	private File[] readHtmlList(File target) {
		ArrayList<File> list = new ArrayList<File>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(target));
			String tmpS = br.readLine();
			while (tmpS != null) {
				File tmpF = new File(tmpS);
				list.add(tmpF);
				tmpS = br.readLine();
			}
		} catch (Exception e) {
			String msg = "File does not exist (" + target.getAbsolutePath()
					+ "). Please create HTML list file.";
			System.out.println(msg);
			logPrintln(msg);
		}

		return list.toArray(new File[list.size()]);
	}

	/**
	 * Initialize target guidelines. In this example, JIS(A,AA) and WCAG 2.0
	 * (A,AA) are enabled.
	 */
	private void initGuidelines() {
		IPreferenceStore prefStore = Activator.getDefault()
				.getPreferenceStore();

		if (!prefStore.getBoolean(NOT_FIRST)) {
			GuidelineHolder gh = GuidelineHolder.getInstance();
			IGuidelineData[] guidelines = gh.getLeafGuidelineData();
			boolean[] enabledItems = new boolean[guidelines.length];
			for (int i = 0; i < guidelines.length; i++) {
				IGuidelineData gData = guidelines[i];
				enabledItems[i] = false;
				if (("JIS".equalsIgnoreCase(gData.getGuidelineName()) || ("WCAG 2.0"
						.equalsIgnoreCase(gData.getGuidelineName())))
						&& !"AAA".equalsIgnoreCase(gData.getLevelStr())) {
					enabledItems[i] = true;
				}
			}
			gh.setEnabledGuidelineWithLevels(enabledItems);

			prefStore.setValue(NOT_FIRST, true);
		}
	}

}
