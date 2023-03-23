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
package org.eclipse.actf.examples.htmlchecker.eval;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.eclipse.actf.visualization.blind.BlindVisualizerHolder;
import org.eclipse.actf.visualization.blind.IBlindVisualizer;
import org.eclipse.actf.visualization.engines.blind.eval.EvaluationResultBlind;
import org.eclipse.actf.visualization.engines.blind.eval.PageEvaluation;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.ReportUtil;

public class VisualizeAndCheck {

	private int[] evaluationScores = null;
	private String[] evaluationMetrics = null;
	private String overallRating = null;
	private DummyBrowser browser = new DummyBrowser();
	private IBlindVisualizer visualizer = null;
	
	public VisualizeAndCheck() {
		IBlindVisualizer[] ibv = BlindVisualizerHolder.getVisualizers();
		visualizer = ibv[0]; // TODO check
	}

	public boolean doEvaluate(File target, File result) {
		browser.open(target);
		return doEvaluate(result);
	}

	private boolean doEvaluate(File resultFile) {
		// set target page
		visualizer.setModelService(browser);

		// evaluate & visualize
		int result = visualizer.visualize();

		if (result == IBlindVisualizer.ERROR) {
			return false;
		}

		IEvaluationResult report = visualizer.getEvaluationResult();
		PageData _pageData = visualizer.getPageData();

		// Evaluate target page by using statistics data (PageData) and
		// evaluation results (list of IProblemItem).
		PageEvaluation _pageEval = new PageEvaluation(report.getProblemList(),
				_pageData);

		if (report instanceof EvaluationResultBlind) {
			EvaluationResultBlind erb = (EvaluationResultBlind) report;
			erb.setPageEvaluation(_pageEval);
			PageEvaluation pageEval = erb.getPageEvaluation();

			/*
			 * PageData includes several statistics data, such as number of
			 * images in the page, etc. Please see
			 * org.eclipse.actf.visualization.eval.html.statistics.PageData for
			 * more details.
			 */
			@SuppressWarnings("unused")
			PageData pageData = null;
			if (pageEval != null) {
				/*
				 * If you need to store evaluation scores, please use these
				 * data.
				 */
				overallRating = pageEval.getOverallRating();
				evaluationMetrics = pageEval.getMetrics();
				evaluationScores = pageEval.getScores();

				System.out.println(overallRating);
				for (int i = 0; i < evaluationMetrics.length; i++) {
					System.out.println(evaluationMetrics[i] + " : "
							+ evaluationScores[i]);
				}

				// statistics data
				pageData = pageEval.getPageData();
			}

			try {
				OutputStreamWriter osw = new OutputStreamWriter(
						new BufferedOutputStream(new FileOutputStream(
								resultFile)),
						// "UTF-8");
						"Shift_JIS");
				// TODO Excel can't open UTF-8 encoded CSV file that
				// includes Japanese characters.

				PrintWriter reportPW = new PrintWriter(osw);

				/*
				 * ReportUtil is a utility class to export evaluation results
				 * (list of IProblemItem). You can create your own report
				 * generator by referring to it.
				 */
				ReportUtil reportUtil = new ReportUtil();
				reportUtil.setPrintWriter(reportPW);
				reportUtil.writeFirstLine();
				erb.accept(reportUtil);
				reportPW.flush();
				reportPW.close();

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
