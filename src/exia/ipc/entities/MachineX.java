package exia.ipc.entities;

import java.awt.Point;

public class MachineX extends Machine {

	private int tempsTraitement = 4;
	
	InputDock q1;
	InputDock q2;
	
	public MachineX(int id, InputDock q1, InputDock q2) {
		super("X" + id, new Point(215, 30 + id * 31), new Point(160, 35 + id * 31), new Point(222, 37 + id * 31));
		this.q1 = q1;
		this.q2 = q2;
	}
	
	public final Product executeWork() {
//		System.out.println("La machine " + getName() + " vient de prendre en charge un produit " + in1.getName());
		
		try {
			Thread.sleep(tempsTraitement * 1000 + (int)(Math.random() * 1500));
		}
		catch (InterruptedException e) {
			System.err.println("La machine " + getName() + " a été interrompue");
			return null;
		}
//		System.out.println("La machine " + getName() + " a terminé");
		
		return new Product(Product.Type.M3);
	}

	public void run() {
		while (!Thread.interrupted()) {
			
			try {
				// Si les deux matières premières sont disponibles
				if (q1.isProductAvailable() && q2.isProductAvailable()) {
					final Product p1 = PrositIPC.Step1.onMachineRequest(q1, this);
					final Product p2 = PrositIPC.Step1.onMachineRequest(q2, this);
					Thread t1 = PrositIPC.moveAsynch(p1, q1, this);
					PrositIPC.move(p2, q2, this);
					t1.join();
					notifyChange(1);
					final Product p3 = executeWork();
					p3.nextStep();
					notifyChange(0);
					
					// On envoie vers la machine suivante
					new Thread(new Runnable() {
						public void run() {
							try {
								MachineY next = (MachineY) getOutputNode();
								PrositIPC.move(p3, MachineX.this, next);
								PrositIPC.Step2.onMachineRequest(MachineX.this, next);
							}
							catch (Throwable e) {
								PrositIPC.handleError(e);
							}
						}
					}).start();
					
				}
			}
			catch (Throwable e) {
				PrositIPC.handleError(e);
			}
			
		}
	}

}