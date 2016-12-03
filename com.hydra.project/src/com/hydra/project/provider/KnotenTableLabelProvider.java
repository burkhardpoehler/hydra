package com.hydra.project.provider;

import lifecycle.MyImageHandler;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hydra.project.model.MyTreeItem;

public class KnotenTableLabelProvider implements ITableLabelProvider {
	
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof MyTreeItem){
				return getImage(element,  columnIndex);
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		
		MyTreeItem myTreeItem =(MyTreeItem) element;
		switch (columnIndex){
			case 0: return myTreeItem.getAnzeigeText();
			case 1: return myTreeItem.getVariablenWert();
			case 2: return myTreeItem.getParameter();
//			case 3: return myTreeItem.getVariablenEinheit();
//			case 4: return String.valueOf(ds.getIndex());
//			case 5: return ds.getText();
//			case 6: return String.valueOf(ds.getFirstPos());
//			case 7: return String.valueOf(ds.getLastPos());
//			case 8: return ds.getVariablentyp();
			
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
		String dateiname = null;	// hole den Namen des Bildes
		if (element instanceof MyTreeItem){
			MyTreeItem myTreeItem =(MyTreeItem) element;

			switch (columnIndex){
			case 0: dateiname = myTreeItem.getIconDateiname();break;
//			case 1: dateiname = myTreeItem.getIconDateiname();break;
//			case 2:	dateiname = "001_001_001.gif";break;
//			case 3: dateiname = myTreeItem.getVariablentyp().toString()+".gif";break;
			}
		}		
		return MyImageHandler.getImage(dateiname);		
	}

}