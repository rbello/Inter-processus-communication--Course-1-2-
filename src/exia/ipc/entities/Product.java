package exia.ipc.entities;

import exia.ipc.exceptions.AllreadyFinishedProductException;
import exia.ipc.exceptions.NotFinishedProductException;
import exia.ipc.exceptions.OperationAllreadyDoneException;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public final class Product {

	private int step = 0;
	private Type type;
	private boolean finished;
	
	public static final int X = 1;
	public static final int Y = 2;
	public static final int Z1 = 4;
	public static final int Z2 = 8;
	public static final int Z3 = 16;
	
	public static enum Type {
		M1,
		M2,
		M3,
		M4
	}

	public Product(Type type) {
		this.type = type;
	}

	void addOperation(int value) throws OperationAllreadyDoneException {
		if ((step & value) == value) {
			throw new OperationAllreadyDoneException(this, value);
		}
		step = (step | value);
//		System.out.println("Add value " + value + " = " + step);
	}

	public Type getType() {
		return type;
	}

	public boolean isFinished() {
		return finished && step >= 31;
	}

	public void makeFinished() throws AllreadyFinishedProductException, NotFinishedProductException {
		if (finished) throw new AllreadyFinishedProductException(this);
		if (step < 31) throw new NotFinishedProductException(this);
		finished = true;
	}

	public static String machineName(int value) {
		if (value == 1) return "X";
		if (value == 2) return "Y";
		if (value == 4) return "Za";
		if (value == 8) return "Zb";
		if (value == 16) return "Zc";
		return "?";
	}
	
}
