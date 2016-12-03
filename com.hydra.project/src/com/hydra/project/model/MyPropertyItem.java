/**
 * 
 */
package com.hydra.project.model;

import org.eclipse.swt.graphics.Image;

/**
 * @author Poehler
 *
 */
public class MyPropertyItem {

	String property;
	boolean button;
	boolean editable;
	String value;
	String oldValue;
	String editor;
	Image image;
	
	public MyPropertyItem() {
	  }

	
	
	/**
	 * @author Poehler
	 * @param property the property to show
	 * @param button true when Button needed
	 * @param editable true when editable
	 * @param value the value
	 * @param editor the editor to show
	 * @param image the image to show
	 */
	public MyPropertyItem(String property, boolean button, boolean editable,
			String value, String editor, Image image) {
	    super();
	    
	    this.property = property;
	    this.button = button;
	    this.editable = editable;
	    this.value = value;
	    this.editor = editor;
	    this.image = image;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the button
	 */
	public boolean isButton() {
		return button;
	}

	/**
	 * @param button the button to set
	 */
	public void setButton(boolean button) {
		this.button = button;
	}

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyPropertyItem [property=" + property + "]";
	}


	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}



	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	
	
}
