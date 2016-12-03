package com.hydra.project.provider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hydra.project.model.MyPropertyItem;

public class PropertyTableLabelProvider implements ITableLabelProvider{
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof MyPropertyItem){
				return getImage(element,  columnIndex);
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		
		MyPropertyItem myPropertyItem =(MyPropertyItem) element;
		switch (columnIndex){
			case 0: return myPropertyItem.getProperty();
			case 1: return myPropertyItem.getValue();
//			case 2: return myPropertyItem.getImage();
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}
	
	public  Image getImage(Object element, int columnIndex) {
		Image image = null;	// hole den Namen des Bildes
		if (element instanceof MyPropertyItem){
			MyPropertyItem myPropertyItem =(MyPropertyItem) element;

			switch (columnIndex){
			case 0: image = myPropertyItem.getImage();break;
			}
		}		
		return image;		
	}

}