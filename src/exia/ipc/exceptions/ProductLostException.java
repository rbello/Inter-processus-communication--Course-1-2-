package exia.ipc.exceptions;

import exia.ipc.entities.Product;

public class ProductLostException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ProductLostException(Product p, String msg, Throwable cause) {
		super("A product of type " + p.getType().toString() + " was lost ! (" + msg + ")", cause);
	}
	
	public ProductLostException(Product p, String msg) {
		this(p, msg, null);
	}
	
}
