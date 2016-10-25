package exia.ipc.exceptions;

import exia.ipc.entities.Product;

public class NotFinishedProductException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NotFinishedProductException(Product product) {
		super("Le produit " + product.getType() + " est marqu� comme termin�, mais il n'est pas pass� par toutes les �tapes.");
	}

}
