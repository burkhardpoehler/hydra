package com.hydra.project.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.FillLayout;

import swing2swt.layout.BorderLayout;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

import com.hydra.project.model.MyTreeItem;

public class MyCopyNodeStructureEditor extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MyCopyNodeStructureEditor(Composite parent, int style, MyTreeItem sample) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));
		
		Composite topComposite = new Composite(composite, SWT.NONE);
		topComposite.setLayoutData(BorderLayout.NORTH);
		topComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button btnNewButton = new Button(topComposite, SWT.NONE);
		btnNewButton.setText("New Button");
		
		Button btnNewButton_1 = new Button(topComposite, SWT.NONE);
		btnNewButton_1.setText("New Button");
		
		Composite bottomComposite = new Composite(composite, SWT.NONE);
		bottomComposite.setLayoutData(BorderLayout.SOUTH);
		bottomComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button btnNewButton_2 = new Button(bottomComposite, SWT.NONE);
		btnNewButton_2.setText("New Button");
		
		Button btnNewButton_3 = new Button(bottomComposite, SWT.NONE);
		btnNewButton_3.setText("New Button");
		
		Composite middleComposite = new Composite(composite, SWT.NONE);
		middleComposite.setLayoutData(BorderLayout.CENTER);
		middleComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
