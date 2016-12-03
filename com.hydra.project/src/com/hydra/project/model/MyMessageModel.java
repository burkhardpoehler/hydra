package com.hydra.project.model;

import org.eclipse.swt.graphics.Image;

public class MyMessageModel {

	private int pos = 0;
	private String message="";
	private Image image;
	
	public MyMessageModel(){
	}
	
	/**
	 * @param myPosition the position in table
	 * @param myMessage the massage
	 * @param myImage the Icon to show
	 * @return the pos
	 */
	public MyMessageModel(int myPosition,  String myMessage, Image myImage) {
		    super();
		    this.pos = myPosition;
		    this.message = myMessage;
		    this.image = myImage;
	}

	/**
	 * @return the pos
	 */
	public String getPos() {
		return String.valueOf(pos);
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
