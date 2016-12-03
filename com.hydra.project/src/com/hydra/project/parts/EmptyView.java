package com.hydra.project.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import swing2swt.layout.BorderLayout;

public class EmptyView {

	public EmptyView() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new BorderLayout(0, 0));
		
		Label lblEsLiegenKeine = new Label(parent, SWT.NONE);
		lblEsLiegenKeine.setLayoutData(BorderLayout.NORTH);
		lblEsLiegenKeine.setText("Es liegen keine Daten zur Anzeige vor");
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
