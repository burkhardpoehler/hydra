package com.hydra.project.provider;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.hydra.project.model.MyPropertyItem;

public class PropertyContentProvider implements IContentProvider{

	protected TableViewer viewer;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		System.out.println("inputChanged" );
		this.viewer = (TableViewer)viewer;
		if (oldInput != null) {
			for (Iterator iterator = ((List) oldInput).iterator(); iterator
					.hasNext();) {
				MyPropertyItem myPropertyItem = (MyPropertyItem) iterator.next();
			}
		}
		if (newInput != null) {
			for (Iterator iterator = ((List) newInput).iterator(); iterator
					.hasNext();) {					
				MyPropertyItem myPropertyItem = (MyPropertyItem) iterator.next();
			}
		}
	}
}

