/*******************************************************************************
 * Copyright (c) 2010,2011 Ministry of Internal Affairs and Communications (MIC).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yasuharu GOTOU (MIC) - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.examples.michecker.views;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.actf.examples.michecker.caption.CaptionData;
import org.eclipse.actf.examples.michecker.internal.Messages;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

public class CaptionView extends ViewPart {

	public static final String ID_SMILVIEW = "org.eclipse.actf.examples.michecker.views.CaptionView";

	private TableViewer tableViewer;
	private Table table;

	private class CaptionLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof CaptionData) {
				switch (columnIndex) {
				case 0:
					return ((CaptionData) element).getTimeString();
				case 1:
					return ((CaptionData) element).getCaptionText();
				default:
				}
			}
			return null;
		}
	}

	public CaptionView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.BORDER);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn tc = new TableColumn(table, SWT.RIGHT);
		tc.setText(Messages.CaptionView_time);
		tc.setWidth(100);

		tc = new TableColumn(table, SWT.LEFT);
		tc.setText(Messages.CaptionView_caption);
		tc.setWidth(500);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new CaptionLabelProvider());
	}

	@Override
	public void setFocus() {
	}
	
	public void setCaptionData(Collection<CaptionData> data){
		if(data == null){
			tableViewer.setInput(new ArrayList<CaptionData>());
		}else{
			tableViewer.setInput(data);
		}		
	}

}
