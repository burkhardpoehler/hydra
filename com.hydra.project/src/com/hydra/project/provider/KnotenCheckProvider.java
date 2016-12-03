/**
 * 
 */
package com.hydra.project.provider;

import org.eclipse.jface.viewers.ICheckStateProvider;

import com.hydra.project.model.MyTreeItem;

/**
 * @author Poehler
 *
 */
public class KnotenCheckProvider implements ICheckStateProvider{

	@Override
	public boolean isChecked(Object element) {
		boolean schwitch = false; 
		if (element instanceof MyTreeItem){
			MyTreeItem myTreeItem =(MyTreeItem) element;
			schwitch= myTreeItem.isChecked();
		}
		return schwitch;
	}

	@Override
	public boolean isGrayed(Object element) {
		boolean schwitch = false; 
		if (element instanceof MyTreeItem){
			MyTreeItem myTreeItem =(MyTreeItem) element;
			schwitch= myTreeItem.isGrayed();
		}
		return schwitch;
	}

}
