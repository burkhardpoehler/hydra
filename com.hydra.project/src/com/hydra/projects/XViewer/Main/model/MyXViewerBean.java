package com.hydra.projects.XViewer.Main.model;

import com.hydra.project.model.MyTreeItem;

/**
 * @author Burkhard Pöhler
 * Bean enthält das MyTreeItem Objekt und die Spaltenüberschrift
 */
public class MyXViewerBean {


	private MyTreeItem myTreeItem;
	private String columnname;
	
	/**
	 * Konstruktor
	 */
	public MyXViewerBean( MyTreeItem myTreeItem, String columnname ) {
		this.myTreeItem = myTreeItem;
		this.columnname = columnname;
	}
	
	
	/**
	 * @return the myTreeItem
	 */
	public MyTreeItem getMyTreeItem() {
		return myTreeItem;
	}

	/**
	 * @param myTreeItem the myTreeItem to set
	 */
	public void setMyTreeItem(MyTreeItem myTreeItem) {
		this.myTreeItem = myTreeItem;
	}

	/**
	 * @return the columnname
	 */
	public String getColumnname() {
		return columnname;
	}

	/**
	 * @param columnname the columnname to set
	 */
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

}
