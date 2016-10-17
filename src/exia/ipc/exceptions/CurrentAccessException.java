package exia.ipc.exceptions;

import exia.ipc.entities.InputDock;

public class CurrentAccessException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CurrentAccessException(InputDock dock) {
		super("Acc�s concurrent sur le dock " + dock.getName() + " : 5 secondes de p�nalit�");
	}
	
}
