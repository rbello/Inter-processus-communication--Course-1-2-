package exia.ipc.exceptions;

import exia.ipc.entities.Product;

public class ProductAllreadyAcceptedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ProductAllreadyAcceptedException(Product p) {
		super("A product of type " + p.getType().toString() + " was allready accepted !");
	}

	
}
