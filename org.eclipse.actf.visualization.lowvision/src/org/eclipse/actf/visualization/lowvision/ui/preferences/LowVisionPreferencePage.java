/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.ui.preferences;

import java.io.File;

import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.PageImageFactory;
import org.eclipse.actf.visualization.lowvision.LowVisionVizPlugin;
import org.eclipse.actf.visualization.lowvision.ui.internal.Messages;
import org.eclipse.actf.visualization.lowvision.ui.internal.PartControlLowVision;
import org.eclipse.actf.visualization.lowvision.util.ParamLowVision;
import org.eclipse.actf.visualization.lowvision.util.SimulateLowVision;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.Bundle;

public class LowVisionPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public static final String ID = LowVisionPreferencePage.class.getName();

	private static final String EYESIGHT_1_0_JP = "1.0"; //$NON-NLS-1$

	private static final String EYESIGHT_0_5_JP = "0.5"; //$NON-NLS-1$

	private static final String EYESIGHT_0_1_JP = "0.1"; //$NON-NLS-1$

	private static final String EYESIGHT_1_0 = "20/20"; //$NON-NLS-1$

	private static final String EYESIGHT_0_5 = "20/40"; //$NON-NLS-1$

	private static final String EYESIGHT_0_1 = "20/200"; //$NON-NLS-1$

	private static final String COLOR_VISION_1 = Messages.DialogSettingLowVision_Protan;

	private static final String COLOR_VISION_2 = Messages.DialogSettingLowVision_Deutan;

	private static final String COLOR_VISION_3 = Messages.DialogSettingLowVision_Tritan;

	private Button _eyesightCheckButton;

	private Label _eyeSightLabel;

	private Slider _eyeSightSlider;

	private Group _colorvisionGroup;

	private Button _colorvisionCheckButton;

	private Button _colorvision1RadioButton;

	private Button _colorvision2RadioButton;

	private Button _colorvision3RadioButton;

	private Button _colorfilterCheckButton;

	private Label _colorfilterNumLabel;

	private Slider _colorFilterSlider;

	private IPageImage _samplePageImage;

	private ImageData[] _sampleImageData;

	private Image _beforeSimulateImage;

	private ImageData _beforeSimulateImageData;

	private Canvas _beforeSimulateImageCanvas;

	private Image _afterSimulateImage;

	private ImageData _afterSimulateImageData;

	private ImageData[] _afterSimulateImageDataArray;

	private Canvas _afterSimulateImageCanvas;

	private ParamLowVision _currentSetting = null;

	private ParamLowVision _appSetting = null;

	private PartControlLowVision _partRightLowVision;

	private File simulationImageFile;

	public LowVisionPreferencePage() {
		super();
	}

	public void init(IWorkbench workbench) {

		this._appSetting = ParamLowVision.getDefaultInstance();
		this._currentSetting = (ParamLowVision) this._appSetting.clone();

		initSampleImage();

	}

	protected Control createContents(Composite parent) {

		Composite composite = new Composite(parent, SWT.NULL);
		composite
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		createSettingControlsPart(composite);

		createSampleImagePart(composite);

		return parent;
	}

	public boolean performOk() {
		setNewValue(this._appSetting);
		return true;
	}

	protected void performApply() {
		performOk();
	}

	private void initSampleImage() {

		// IInt2D int2D = null;

		Bundle lvBundle = LowVisionVizPlugin.getDefault().getBundle();

		try {
			// int2D = Int2DFactory
			// .createInt2D(FileLocator.openStream(lvBundle, new Path(
			// "vizResources/images/LowVisionSample.bmp"), false));
			this._samplePageImage = PageImageFactory
					.createPageImage(FileLocator.openStream(lvBundle, new Path(
							"vizResources/images/LowVisionSample.bmp"), false)); //$NON-NLS-1$
		} catch (Exception e) {
			// e.printStackTrace();
			this._samplePageImage = PageImageFactory.createPageImage();
		}

		// if (null != int2D) {
		// this._samplePageImage = PageImageFactory.createPageImage(int2D,
		// false);
		// } else {
		// this._samplePageImage = PageImageFactory.createPageImage();
		// }

		_sampleImageData = new ImageData[0];

		try {
			ImageLoader imageLoader = new ImageLoader();
			_sampleImageData = imageLoader.load(FileLocator.openStream(
					lvBundle, new Path(
							"vizResources/images/LowVisionSample.bmp"), false)); //$NON-NLS-1$
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (simulationImageFile == null) {
			try {
				simulationImageFile = LowVisionVizPlugin.createTempFile(
						"preference", IVisualizationConst.SUFFIX_BMP); //$NON-NLS-1$
			} catch (Exception e) {
			}
		}

	}

	private void createSettingControlsPart(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		createEyeSightControl(composite);

		new Label(composite, SWT.NONE);

		createColorVisionControl(composite);

		new Label(composite, SWT.NONE);

		createColorFilterControl(composite);

	}

	private void createEyeSightControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		this._eyesightCheckButton = new Button(composite, SWT.CHECK);
		this._eyesightCheckButton.setLayoutData(new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 2, 1));
		this._eyesightCheckButton
				.setText(Messages.DialogSettingLowVision_Eyesight_9);
		this._eyesightCheckButton.setSelection(this._appSetting.useEyeSight());
		this._eyesightCheckButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (_partRightLowVision != null
						&& _partRightLowVision.isChildThread()) {
					_eyeSightSlider.setVisible(((Button) event.widget)
							.getSelection());
				} else {
					_eyeSightSlider.setEnabled(((Button) event.widget)
							.getSelection());
				}
				doSimulate();
			}
		});

		Composite sliderComposite = new Composite(composite, SWT.NONE);
		sliderComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		layout = new GridLayout();
		layout.numColumns = 3;
		sliderComposite.setLayout(layout);

		// Create the labels of EyeSight
		Label eyeSightLable11 = new Label(sliderComposite, SWT.NONE);
		eyeSightLable11.setText(EYESIGHT_0_1);
		eyeSightLable11.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false));

		Label eyeSightLable12 = new Label(sliderComposite, SWT.NONE);
		eyeSightLable12.setText(EYESIGHT_0_5);
		GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		eyeSightLable12.setLayoutData(gridData);

		Label eyeSightLable13 = new Label(sliderComposite, SWT.NONE);
		eyeSightLable13.setText(EYESIGHT_1_0);
		eyeSightLable13.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false));

		Label eyeSightLable21 = new Label(sliderComposite, SWT.NONE);
		eyeSightLable21.setText(EYESIGHT_0_1_JP);
		eyeSightLable21.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false));

		Label eyeSightLable22 = new Label(sliderComposite, SWT.NONE);
		eyeSightLable22.setText(EYESIGHT_0_5_JP);
		gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		eyeSightLable22.setLayoutData(gridData);

		Label eyeSightLable23 = new Label(sliderComposite, SWT.NONE);
		eyeSightLable23.setText(EYESIGHT_1_0_JP);
		eyeSightLable23.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false));

		// create the slider of EyeSight
		this._eyeSightSlider = new Slider(sliderComposite, SWT.NONE);
		this._eyeSightSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 3, 1));

		this._eyeSightSlider.setMinimum(10);
		this._eyeSightSlider.setMaximum(101);
		this._eyeSightSlider.setIncrement(10);
		this._eyeSightSlider.setPageIncrement(10);
		this._eyeSightSlider.setThumb(1);
		this._eyeSightSlider.setSelection(this._appSetting.getEyeSightValue());

		if (this._partRightLowVision != null
				&& this._partRightLowVision.isChildThread()) {
			this._eyeSightSlider
					.setVisible(_eyesightCheckButton.getSelection());
		} else {
			this._eyeSightSlider
					.setEnabled(_eyesightCheckButton.getSelection());
		}

		this._eyeSightSlider.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				doSimulate();
			}
		});

		this._eyeSightLabel = new Label(composite, SWT.NONE);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.horizontalIndent = 10;
		gridData.widthHint = 25;
		this._eyeSightLabel.setLayoutData(gridData);
	}

	private void createColorVisionControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NULL);
		composite
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		composite.setLayout(new GridLayout());

		this._colorvisionCheckButton = new Button(composite, SWT.CHECK);
		this._colorvisionCheckButton.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, true, false));
		this._colorvisionCheckButton
				.setText(Messages.DialogSettingLowVision_Color_Vision_Deficiency_10);
		this._colorvisionCheckButton.setSelection(this._appSetting
				.useColorVision());
		this._colorvisionCheckButton
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						_colorvisionGroup.setEnabled(((Button) event.widget)
								.getSelection());
						_colorvision1RadioButton
								.setEnabled(((Button) event.widget)
										.getSelection());
						_colorvision2RadioButton
								.setEnabled(((Button) event.widget)
										.getSelection());
						_colorvision3RadioButton
								.setEnabled(((Button) event.widget)
										.getSelection());
						doSimulate();
					}
				});

		// create the group of 1,2,3 radio buttons
		this._colorvisionGroup = new Group(composite, SWT.NONE); // controlGroup
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		this._colorvisionGroup.setLayoutData(gridData);
		this._colorvisionGroup.setLayout(new GridLayout());

		// Create the 1,2,3 radio buttons
		this._colorvision1RadioButton = new Button(_colorvisionGroup, SWT.RADIO);
		this._colorvision1RadioButton.setText(COLOR_VISION_1);

		this._colorvision2RadioButton = new Button(_colorvisionGroup, SWT.RADIO);
		this._colorvision2RadioButton.setText(COLOR_VISION_2);

		this._colorvision3RadioButton = new Button(_colorvisionGroup, SWT.RADIO);
		this._colorvision3RadioButton.setText(COLOR_VISION_3);

		int colorVisionValue = this._appSetting.getColorVisionValue();
		switch (colorVisionValue) {
		case 1:
			this._colorvision1RadioButton.setSelection(true);
			break;
		case 2:
			this._colorvision2RadioButton.setSelection(true);
			break;
		case 3:
			this._colorvision3RadioButton.setSelection(true);
			break;
		default:
			this._colorvision1RadioButton.setSelection(true);
			break;
		}

		// Add the listeners of 1,2,3 radio buttons
		SelectionListener selectionListener2 = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				doSimulate();
				if (!((Button) event.widget).getSelection())
					return;
			};
		};
		this._colorvision1RadioButton.addSelectionListener(selectionListener2);
		this._colorvision2RadioButton.addSelectionListener(selectionListener2);
		this._colorvision3RadioButton.addSelectionListener(selectionListener2);

		this._colorvisionGroup.setEnabled(_colorvisionCheckButton
				.getSelection());
		this._colorvision1RadioButton.setEnabled(_colorvisionCheckButton
				.getSelection());
		this._colorvision2RadioButton.setEnabled(_colorvisionCheckButton
				.getSelection());
		this._colorvision3RadioButton.setEnabled(_colorvisionCheckButton
				.getSelection());
	}

	private void createColorFilterControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		this._colorfilterCheckButton = new Button(composite, SWT.CHECK);
		this._colorfilterCheckButton.setLayoutData(new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 2, 1));
		this._colorfilterCheckButton
				.setText(Messages.DialogSettingLowVision_Crystalline_lens);
		this._colorfilterCheckButton.setSelection(this._appSetting
				.useEyeSight());
		this._colorfilterCheckButton
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						if (_partRightLowVision != null
								&& _partRightLowVision.isChildThread()) {
							_colorFilterSlider
									.setVisible(((Button) event.widget)
											.getSelection());
						} else {
							_colorFilterSlider
									.setEnabled(((Button) event.widget)
											.getSelection());
						}
						doSimulate();
					}
				});

		Composite colorFilterComposite = new Composite(composite, SWT.NONE);
		colorFilterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false));
		layout = new GridLayout();
		layout.numColumns = 3;
		colorFilterComposite.setLayout(layout);

		// Create the labels of Color Filter
		Label colorfilterLable1 = new Label(colorFilterComposite, SWT.NONE);
		colorfilterLable1.setText(Messages.DialogSettingLowVision_60);
		colorfilterLable1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				true, false));

		Label colorfilterLable2 = new Label(colorFilterComposite, SWT.NONE);
		colorfilterLable2.setText(Messages.DialogSettingLowVision_40);
		colorfilterLable2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				true, false));

		Label colorfilterLable3 = new Label(colorFilterComposite, SWT.NONE);
		colorfilterLable3.setText(Messages.DialogSettingLowVision_20);
		colorfilterLable3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				true, false));

		this._colorFilterSlider = new Slider(colorFilterComposite, SWT.NONE);
		this._colorFilterSlider.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, false, false, 3, 1));

		this._colorFilterSlider.setMinimum(60);
		this._colorFilterSlider.setMaximum(101);

		this._colorFilterSlider.setIncrement(5);
		this._colorFilterSlider.setPageIncrement(10);
		this._colorFilterSlider.setThumb(1);
		this._colorFilterSlider.setSelection(this._appSetting
				.getColorFilterValue());

		if (this._partRightLowVision != null
				&& this._partRightLowVision.isChildThread()) {
			this._colorFilterSlider.setVisible(_colorfilterCheckButton
					.getSelection());
		} else {
			this._colorFilterSlider.setEnabled(_colorfilterCheckButton
					.getSelection());
		}

		this._colorFilterSlider.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				doSimulate();
			}
		});

		this._colorfilterNumLabel = new Label(composite, SWT.NONE);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.horizontalIndent = 10;
		gridData.widthHint = 25;
		this._colorfilterNumLabel.setLayoutData(gridData);
	}

	private void createSampleImagePart(Composite parent) {
		GridData gridData;

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		// Create the Label "Original:"
		Label labelOriginal = new Label(composite, SWT.NONE);
		labelOriginal.setText(Messages.DialogSettingLowVision_Original__24);

		_beforeSimulateImageCanvas = new Canvas(composite, SWT.NO_REDRAW_RESIZE);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.widthHint = 320;
		gridData.heightHint = 130;
		_beforeSimulateImageCanvas.setLayoutData(gridData);
		_beforeSimulateImageCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				if (null != _beforeSimulateImage) {
					paintBeforeSimulateImage(event);
				}
			}
		});

		// Create the blank label for adjust the location
		Label labelBlank = new Label(composite, SWT.NONE);
		labelBlank.setText(" "); //$NON-NLS-1$
		gridData = new GridData();
		gridData.heightHint = 20;
		labelBlank.setLayoutData(gridData);

		// Create the Label "After simulation:"
		Label labelAfterSimulation = new Label(composite, SWT.NONE);
		labelAfterSimulation
				.setText(Messages.DialogSettingLowVision_After_simulation);

		_afterSimulateImageCanvas = new Canvas(composite, SWT.NO_REDRAW_RESIZE);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.widthHint = 320;
		gridData.heightHint = 130;
		_afterSimulateImageCanvas.setLayoutData(gridData);
		_afterSimulateImageCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				if (null != _afterSimulateImage) {
					paintAfterSimulateImage(event);
				}
			}
		});

		initImageBeforeSimulate(parent);
		doSimulate();
	}

	private void initImageBeforeSimulate(Composite parent) {
		try {
			for (int i = 0; i < this._sampleImageData.length; i++) {
				displayImageBeforeSimulate(this._sampleImageData[i]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void displayImageBeforeSimulate(ImageData newImageData) {
		// Dispose of the old image, if there was one.
		if (null != this._beforeSimulateImage) {
			this._beforeSimulateImage.dispose();
		}

		try {
			// Cache the new image and imageData.
			this._beforeSimulateImage = new Image(getShell().getDisplay(),
					newImageData);
			this._beforeSimulateImageData = newImageData;
		} catch (SWTException swte) {
			swte.printStackTrace();
			this._beforeSimulateImage = null;

			return;
		}

		// Redraw the canvases.
		this._beforeSimulateImageCanvas.redraw();
	}

	private void displayImageAfterSimulate(ImageData newImageData) {
		// Dispose of the old image, if there was one.
		if (null != this._afterSimulateImage) {
			this._afterSimulateImage.dispose();
		}

		try {
			// Cache the new image and imageData.
			this._afterSimulateImage = new Image(getShell().getDisplay(),
					newImageData);
			this._afterSimulateImageData = newImageData;
		} catch (SWTException e) {
			e.printStackTrace();
			this._afterSimulateImage = null;

			return;
		}

		// Redraw the canvases.
		this._afterSimulateImageCanvas.redraw();
	}

	private void doSimulate() {

		try {
			setNewValue(this._currentSetting);

			this._afterSimulateImageDataArray = SimulateLowVision.doSimulate(
					this._samplePageImage, this._currentSetting,
					simulationImageFile.getAbsolutePath());
			for (int i = 0; i < this._afterSimulateImageDataArray.length; i++) {
				displayImageAfterSimulate(this._afterSimulateImageDataArray[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void paintBeforeSimulateImage(PaintEvent event) {
		event.gc.drawImage(this._beforeSimulateImage, 0, 0,
				this._beforeSimulateImageData.width,
				this._beforeSimulateImageData.height,
				this._beforeSimulateImageData.x,
				this._beforeSimulateImageData.y, Math
						.round(this._beforeSimulateImageData.width), Math
						.round(this._beforeSimulateImageData.height));
	}

	private void paintAfterSimulateImage(PaintEvent event) {
		event.gc.drawImage(this._afterSimulateImage, 0, 0,
				this._afterSimulateImageData.width,
				this._afterSimulateImageData.height,
				this._afterSimulateImageData.x, this._afterSimulateImageData.y,
				Math.round(this._afterSimulateImageData.width), Math
						.round(this._afterSimulateImageData.height));
	}

	private void setNewValue(ParamLowVision currParam) {
		String str;
		currParam.setEyeSight(_eyesightCheckButton.getSelection());
		if (currParam.useEyeSight()) {
			currParam.setEyeSightValue(_eyeSightSlider.getSelection());

			int sliderValue = _eyeSightSlider.getSelection();
			if (sliderValue < 100) {
				str = "0." + String.valueOf(sliderValue); //$NON-NLS-1$
			} else {
				str = "1.00"; //$NON-NLS-1$
			}
			_eyeSightLabel.setText(str);
		} else {
			_eyeSightLabel.setText(""); //$NON-NLS-1$
		}

		currParam.setColorVision(_colorvisionCheckButton.getSelection());
		if (currParam.useColorVision()) {
			if (_colorvision1RadioButton.getSelection()) {
				currParam.setColorVisionValue(1);
			} else if (_colorvision2RadioButton.getSelection()) {
				currParam.setColorVisionValue(2);
			} else {
				currParam.setColorVisionValue(3);
			}
		}

		currParam.setColorFilter(_colorfilterCheckButton.getSelection());
		if (currParam.useColorFilter()) {
			currParam.setColorFilterValue(_colorFilterSlider.getSelection());
			int iAge = 120 - _colorFilterSlider.getSelection();
			str = String.valueOf(iAge);
			_colorfilterNumLabel.setText(str);
		} else {
			_colorfilterNumLabel.setText(""); //$NON-NLS-1$
		}
	}

}
