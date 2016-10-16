package exia.ipc.entities;

import java.awt.Point;

public abstract class Machine extends Node implements Runnable {

	Machine(String name, Point indicatorLocation, Point inputLocation, Point outputLocation) {
		super(name, indicatorLocation, inputLocation, outputLocation);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
	
	Node getOutputNode() {
		return this.routes.entrySet().iterator().next().getKey();
	}
	
}
