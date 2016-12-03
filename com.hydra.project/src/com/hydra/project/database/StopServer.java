/**
 * 
 */
package com.hydra.project.database;

import com.db4o.ObjectContainer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.messaging.MessageSender;

/**
 * @author Poehler
 * stops the db4o Server started with {@link StartServer}. <br>
 * <br>
 * This is done by opening a client connection to the server and by sending a
 * StopServer object as a message. {@link StartServer} will react in it's
 * processMessage method.
 */

public class StopServer implements ServerInfo{
	/**	www.db4o.com
	* stops a db4o Server started with StartServer.
	*
	* @throws Exception
	*/
	public static void main(String[] args) {
		ObjectContainer objectContainer = null;
		try {
			// connect to the server
			objectContainer = Db4oClientServer.openClient(Db4oClientServer.newClientConfiguration(), HOST, PORT, USER, PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	if (objectContainer != null) {
		// get the messageSender for the ObjectContainer
		MessageSender messageSender = objectContainer.ext().configure().clientServer().getMessageSender();
		// send an instance of a StopServer object
		messageSender.send(new StopServer());
		// close the ObjectContainer
		objectContainer.close();
		}
	}
}
