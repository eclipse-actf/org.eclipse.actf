/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.ui.report.srcviewer;

import java.io.File;

import org.eclipse.actf.visualization.eval.problem.HighlightTargetSourceInfo;
import org.eclipse.actf.visualization.internal.ui.report.Messages;
import org.eclipse.actf.visualization.internal.ui.report.ReportPlugin;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SrcViewerForPT {

	// TODO viewpart, SourceViewer
	private static final Image WINDOW_ICON = ReportPlugin
			.imageDescriptorFromPlugin(ReportPlugin.PLUGIN_ID, "icons/excla_squ.png").createImage(); //$NON-NLS-1$

	private static SrcViewerForPT INSTANCE = null;

	private static SrcViewer _srcViewer;

	private static Shell _srcViewerShell;

	private Shell _shell = null;

	private boolean srcChanged;

	private File curTarget;

	/**
	 * @param pt
	 * @param display
	 */
	private SrcViewerForPT(Shell shell) {
		super();
		this._shell = shell;
		initSrcViewer();
	}

	public static SrcViewerForPT initSrcViewerForPT(Shell parent) {
		if (INSTANCE != null) {
			// TODO close
			// INSTANCE.PARENT.removeShellListener();
		}

		INSTANCE = new SrcViewerForPT(parent);
		return (INSTANCE);
	}

	public static SrcViewerForPT getInstance() {
		return (INSTANCE);
	}

	private void initSrcViewer() {
		srcChanged = true;

		if (_shell != null) {
			_shell.addShellListener(new ShellAdapter() {
				public void shellClosed(ShellEvent arg0) {
					if (_srcViewerShell != null && !_srcViewerShell.isDisposed()) {
						_srcViewerShell.dispose();
					}
				}
			});
		}
	}

	public void openSrcViewer() {
		if (null == _srcViewerShell || _srcViewerShell.isDisposed()) {
			Display display = _shell.getDisplay();
			if (null == display) {
				display = Display.getDefault();
			}
			_srcViewerShell = new Shell(display);
			_srcViewerShell.setLayout(new FillLayout());
			_srcViewerShell.setImage(WINDOW_ICON);
			_srcViewerShell.setText(Messages.SrcViewerForPT_0);
			_srcViewer = new SrcViewer(_srcViewerShell);
			_srcViewerShell.setSize(600, 750);
			_srcViewerShell.setFocus();
			_srcViewerShell.open();

			_srcViewerShell.addShellListener(new ShellAdapter() {
				public void shellClosed(ShellEvent arg0) {
					_srcViewer.closeSearchDlgShell();
				}
			});

			srcChanged = true;

		} else if (!_srcViewerShell.isDisposed()) {
			if (_srcViewerShell.getMinimized()) {
				_srcViewerShell.setMinimized(false);
			}
			_srcViewerShell.forceActive();
		}
	}

	public void highlightSrcViewer(HighlightTargetSourceInfo[] sourceInfos, File target) {
		if (_srcViewerShell != null && !_srcViewerShell.isDisposed()) {

			if (curTarget != target || srcChanged) {
				srcChanged = true;
				updateSrcViewer(target);
			}

			// updateSrcViewer(currentLayout);
			_srcViewer.clearHighlight();

			for (int i = 0; i < sourceInfos.length; i++) {
				HighlightTargetSourceInfo curInfo = sourceInfos[i];

				if (curInfo.getStartColumn() < 0 || curInfo.getEndColumn() < 0) {
					_srcViewer.highlightLines(curInfo.getStartLine(), curInfo.getEndLine());
				} else {
					_srcViewer.highlight(curInfo.getStartLine(), curInfo.getStartColumn(), curInfo.getEndLine(),
							curInfo.getEndColumn());
				}

			}
		}

	}

	public void updateSrcViewer(File target) {
		if (_srcViewerShell != null && !_srcViewerShell.isDisposed() && srcChanged) {
			try {
				_srcViewer.openFile(target);
				curTarget = target;
			} catch (Exception e) {
				// e.printStackTrace();
				_srcViewer.setText(""); //$NON-NLS-1$
			}
			srcChanged = false;
		}
	}

	/**
	 * @param srcChanged The srcChanged to set.
	 */
	public void setSrcChanged(boolean srcChanged) {
		this.srcChanged = srcChanged;
	}
}
