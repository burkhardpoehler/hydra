/*
 * Created on Oct 29, 2010
 *
 * PLACE_YOUR_DISTRIBUTION_STATEMENT_RIGHT_HERE
 */
package com.hydra.project.myplugin_nebula.xviewer;

import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public interface IAltLeftClickProvider {

   boolean handleAltLeftClick(TreeColumn treeColumn, TreeItem treeItem);

}
