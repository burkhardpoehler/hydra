/*
 * Created on Nov 5, 2010
 *
 * PLACE_YOUR_DISTRIBUTION_STATEMENT_RIGHT_HERE
 */
package com.hydra.project.myplugin_nebula.xviewer;

import java.util.Collection;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public interface IMultiColumnEditProvider {

   void handleColumnMultiEdit(TreeColumn treeColumn, Collection<TreeItem> treeItems);

}
