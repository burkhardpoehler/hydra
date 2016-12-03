/**
 * 
 */
package com.hydra.project.database;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import com.hydra.project.model.MyTreeItem;

/**
 * @author Poehler
 *
 */
public class DBHandler {

	@Inject
	public DBHandler() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		createViewer(parent);
	}
	
	private void createViewer(Composite parent) {
		final Tree MeinBaum = new Tree(parent,SWT.NONE | SWT.CHECK | SWT.MULTI | SWT.H_SCROLL
	            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);	
		StructuredViewer treeViewer = new CheckboxTreeViewer(MeinBaum);	
	  
	    treeViewer.setInput(null);
		
	}

	@Inject @Optional
	void myEventReceiver(@UIEventTopic("MyTreeItemEvent") MyTreeItem myTreeItem) {
	    // empfängt geänderte Objekte mit dem Topic 'MyTreeItemEvent'
		System.out.println("Nachricht in DBHandler empfangen " + myTreeItem.getVariablenWert());

	}
	
	
}
