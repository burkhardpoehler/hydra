package com.hydra.project.exceptions;


/**
 * @author Burkhard P�hler
 * Eigne Exception erzeugen wenn ein Kind nicht gefunden wurde
 */
public class NoChildFoundException extends Exception{
	   //Konstructor
		public NoChildFoundException() {
	        super("Zu suchendes Kind nicht gefunden ");
	    }
}
