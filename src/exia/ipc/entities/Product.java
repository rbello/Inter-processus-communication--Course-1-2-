package exia.ipc.entities;

import exia.ipc.exceptions.AllreadyFinishedProductException;

public final class Product {

	private int step = 0;
	private Type type;
	private boolean finished;
	
	public static enum Type {
		M1, M2, M3, M4
	}

	public Product(Type type) {
		this.type = type;
	}

	void nextStep() {
		step++;
		//System.out.println("Passage au step " + step);
	}

	public Type getType() {
		return type;
	}

	public boolean isFinished() {
		return finished && step >= 5;
	}

	public void makeFinished() throws AllreadyFinishedProductException {
		if (finished) throw new AllreadyFinishedProductException(this);
		finished = true;
	}
	
}
