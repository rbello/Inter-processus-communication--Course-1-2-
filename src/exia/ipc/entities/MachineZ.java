package exia.ipc.entities;

import java.awt.Point;

import exia.ipc.exceptions.MachineAllreadyUsedException;

public class MachineZ extends Machine {

	private Product job = null;
	private MachineZ next;
	private int type;

	MachineZ(int id) {
		this(id, null);
	}

	public MachineZ(int id, MachineZ next) {
		super("Z" + id, 
				new Point(405, 12 + id * 25 + (id > 2 ? 30 : 0)),
				new Point(368, 28 + id * 23 + (id > 2 ? 30 : 0)),
				new Point(415, 28 + id * 23 + (id > 2 ? 30 : 0)));
		this.type = id < 3 ? id : id - 3;
		this.next = next;
	}

	public void executeJob(Product p) throws MachineAllreadyUsedException {
		if (job != null) throw new MachineAllreadyUsedException(this);
		job = p;
		notifyChange(1);
		try {
			if (type == 0) Thread.sleep(300);
			else if (type == 1) Thread.sleep(2100);
			else if (type == 2) Thread.sleep(1500);
		} catch (InterruptedException e) {
			return;
		}
		p.nextStep();
		notifyChange(0);
		job = null;
	}

	public boolean isAvailable() {
		boolean ready = job == null;
		if (next != null) ready = ready && next.isAvailable();
		return ready;
	}

	public MachineZ getNext() {
		return next;
	}

}
