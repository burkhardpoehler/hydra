/**
 * 
 */
package com.hydra.project.editors;

import org.eclipse.nebula.widgets.treemapper.ISemanticTreeMapperSupport;
import org.eclipse.swt.widgets.Composite;

import com.hydra.project.editors.MyTreeItemBean;
import com.hydra.project.model.MyTreeItem;

/**
 * @author Poehler
 *
 */
public class MySemanticTreeMapperSupport implements ISemanticTreeMapperSupport<MyTreeItemBean, MyTreeItem, MyTreeItem>{



	public MySemanticTreeMapperSupport(Composite parent, MyTreeItem myTreeItem,	MyTreeItem myTreeItem2) {
		// TODO Auto-generated constructor stub
	}

	public MyTreeItemBean createSemanticMappingObject(MyTreeItem leftItem,MyTreeItem rightItem) {
		MyTreeItemBean res = new MyTreeItemBean(rightItem, leftItem);
//		res.left = leftItem;
//		res.right = rightItem;
		return res;
	}

	public MyTreeItem resolveLeftItem(MyTreeItemBean semanticMappingObject) {
		return semanticMappingObject.left;
	}

	public MyTreeItem resolveRightItem(MyTreeItemBean semanticMappingObject) {
		return semanticMappingObject.right;
	}





}
