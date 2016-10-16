package exia.ipc.exceptions;

import exia.ipc.entities.InputDock;

public class NoMoreProductsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NoMoreProductsException(InputDock dock) {
		super("The dock " + dock.getName() + " is empty !");
	}

}
