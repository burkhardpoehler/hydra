package com.hydra.project.editors;

import com.hydra.project.model.MyTreeItem;

public class MyTreeItemBean {

		public MyTreeItem left;
		public MyTreeItem right;
		public Integer posLeft;
		public Integer posRight;
		
		public MyTreeItemBean(MyTreeItem left, MyTreeItem right) {
			super();
			this.left = left;
			this.right = right;
		}

}
