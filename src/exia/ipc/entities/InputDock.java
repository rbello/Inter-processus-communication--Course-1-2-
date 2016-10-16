package exia.ipc.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import exia.ipc.entities.Product.Type;
import exia.ipc.exceptions.CurrentAccessException;
import exia.ipc.exceptions.NoMoreProductsException;

public class InputDock extends Node implements Runnable {

	private List<Product> stock = new ArrayList<Product>();
	private boolean working = false;
	private Type product;
	
	public InputDock(Product.Type type, Point indicatorLocation, Point outputLocation) {
		super("Q" + type, indicatorLocation, null, outputLocation);
		product = type;
	}

	public int getAvailableProductsCount() {
		return stock.size();
	}
	
	public Product accept() throws NoMoreProductsException, CurrentAccessException {
		
		// Picking en cours
		if (working) {
			throw new CurrentAccessException(this);
		}
		
		// Plus de stock
		if (stock.size() < 1) {
			throw new NoMoreProductsException(this);
		}
		
		// Temps de sortie de la marchandise
		working = true;
		try {
			Thread.sleep(200);
		}
		catch (InterruptedException e) {
			return null;
		}
		
		// On sort la marchandise du stock
		Product p = stock.remove(0);
		
		// C'est sorti, on peut servir d'autres demandes
		working = false;
		
		// On informe l'IHM du stock restant
		notifyChange(stock.size());
		
		return p;
	}
	
	boolean isCurrentlyPickingUp() {
		return working;
	}
	
	public void run() {
		while (!Thread.interrupted()) {
			
			// Nombre de produits arrivés
			int prod = 1 + (int)(Math.random() * 3);
			if (!PrositIPC.STARTED) prod = 0;
			
			//System.out.println(prod + " produits sont arrivés au quai " + name);
			
			// Arrivés en stock
			while (prod-- > 0) stock.add(new Product(product));
			
			notifyChange(stock.size());
			
			try {
				Thread.sleep((2 + (int)(Math.random() * 4)) * 1000);
			}
			catch (InterruptedException e) {
				return;
			}
			
		}
	}

	public boolean isProductAvailable() {
		return !stock.isEmpty();
	}

	void addProduct(Product p) {
		stock.add(p);
	}
	
	@Override
	void addIndicatorListener(IndicatorListener l) {
		super.addIndicatorListener(l);
		l.notifyChange(stock.size());
	}

	public void addProducts(int i) {
		while (i-- > 0) addProduct(new Product(product));
	}

}
