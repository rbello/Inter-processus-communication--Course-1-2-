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

	@Override
	public void run() {
		
		while (!Thread.interrupted()) {
			
			try {
				if (job != null) {
					//System.out.println("Debut du travail pour machine " + getName());
					notifyChange(1);
					Thread.sleep(1500 + type * 150);
					job.nextStep();
					//System.out.println("Fin du travail pour machine " + getName());
					notifyChange(0);
					job = null;
				}
				else {
					Thread.sleep(100);
				}
			}
			catch (InterruptedException e) {
				return;
			}
			catch (Exception e) {
				PrositIPC.handleError(e);
			}
			
		}
		
	}

	public void executeJob(Product p) throws MachineAllreadyUsedException {
		if (job != null) throw new MachineAllreadyUsedException(this);
		job = p;
		while (job != null) { }
	}

	public boolean isAvailable() {
		return job == null;
	}

	public MachineZ getNext() {
		return next;
	}

}
