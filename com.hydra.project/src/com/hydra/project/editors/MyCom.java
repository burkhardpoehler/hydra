package com.hydra.project.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.SashForm;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Table;

public class MyCom extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MyCom(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));
		
		Composite topComposite = new Composite(composite, SWT.NONE);
		topComposite.setLayoutData(BorderLayout.NORTH);
		
		Composite bottomComposite = new Composite(composite, SWT.NONE);
		bottomComposite.setLayoutData(BorderLayout.SOUTH);
		
		ScrolledComposite middleScrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		middleScrolledComposite.setLayoutData(BorderLayout.CENTER);
		middleScrolledComposite.setExpandHorizontal(true);
		middleScrolledComposite.setExpandVertical(true);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
