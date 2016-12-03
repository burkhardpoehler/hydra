package com.hydra.project.provider;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import com.hydra.project.model.MyTreeItem;

public class KnotenContentProvider implements ITreeContentProvider{

	protected TreeViewer viewer;

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof MyTreeItem){
				MyTreeItem myTreeItem = (MyTreeItem)parentElement;
				
				Object[] o = null;
				if (myTreeItem.getChildren() != null) {
					if (!myTreeItem.isWurzel()) {

//						for (int i = 0; i < myTreeItem.getChildren().size(); i++) { //Liste der Väter ergänzen
//							myTreeItem
//									.getChildren()
//									.get(i)
//									.setDatenbankName(
//											myTreeItem.getDatenbankName());
//							myTreeItem.getChildren().get(i)
//									.setDbIndex(myTreeItem.getDbIndex());
//						}

					}
					o = myTreeItem.getChildren().toArray();
				}
				
				return o;
			}
			return new Object[0];
		}
		
		@Override
		public Object getParent(Object element) {
			if (element instanceof MyTreeItem){
			 	return ((MyTreeItem)element).getParent();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof MyTreeItem){
				MyTreeItem myTreeItem =(MyTreeItem) element;
				return myTreeItem.isHasChildren();
			}
			return false;
		}
		
		@Override
		public Object[] getElements(Object Liste) {
			return ((List)Liste).toArray();
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			System.out.println("inputChanged" );
			this.viewer = (TreeViewer)viewer;
			if (oldInput != null) {
				for (Iterator iterator = ((List) oldInput).iterator(); iterator
						.hasNext();) {
					MyTreeItem myTreeItem = (MyTreeItem) iterator.next();
				}
			}
			if (newInput != null) {
				for (Iterator iterator = ((List) newInput).iterator(); iterator
						.hasNext();) {					
					MyTreeItem myTreeItem = (MyTreeItem) iterator.next();
				}
			}
		}
	}

