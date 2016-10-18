package exia.ipc.entities;

import java.awt.Point;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public abstract class Machine extends Node implements Runnable {

	Machine(String name, Point indicatorLocation, Point inputLocation, Point outputLocation) {
		super(name, indicatorLocation, inputLocation, outputLocation);
	}

	@Override
	public void run() {
	}
	
	Node getOutputNode() {
		return this.routes.entrySet().iterator().next().getKey();
	}
	
}
