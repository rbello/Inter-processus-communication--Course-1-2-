package exia.ipc.entities;

import java.awt.Point;

import exia.ipc.exceptions.CurrentAccessException;
import exia.ipc.exceptions.NoMoreProductsException;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public final class MachineX extends Machine {

	private int tempsTraitement = 4;
	
	InputDock q1;
	InputDock q2;
	
	public MachineX(int id, InputDock q1, InputDock q2) {
		super("X" + id, new Point(215, 30 + id * 31), new Point(160, 35 + id * 31), new Point(222, 37 + id * 31));
		this.q1 = q1;
		this.q2 = q2;
	}
	
	public final Product executeWork() {
		try {
			Thread.sleep(tempsTraitement * 500 + (int)(Math.random() * 800));
		}
		catch (InterruptedException e) {
			return null;
		}
		return new Product(Product.Type.M3);
	}

	public void run() {
		while (!Thread.interrupted()) {
			
			try {
				// Si les deux matières premières sont disponibles
//				if (q1.isProductAvailable() && q2.isProductAvailable()) {
					final Product p1 = PrositIPC.Step1.onMachineRequest(q1, this);
					final Product p2 = PrositIPC.Step1.onMachineRequest(q2, this);
					Thread t1 = PrositIPC.moveAsynch(p1, q1, this);
					PrositIPC.move(p2, q2, this);
					t1.join();
					notifyChange(1);
					final Product p3 = executeWork();
					p3.nextStep();
					
					final MachineY next = (MachineY) getOutputNode();
					PrositIPC.Step2.onMachineRequest(MachineX.this, next);
					
					// On envoie vers la machine suivante
					new Thread(new Runnable() {
						public void run() {
							try {
								notifyChange(0);
								PrositIPC.move(p3, MachineX.this, next);
								PrositIPC.Step2.onMachineExecute(MachineX.this, next);
							}
							catch (Throwable e) {
								PrositIPC.handleError(e);
							}
						}
					}, "X to Y").start();
					
//				}
//				else {
//					Thread.sleep(50);
//				}
			}
			catch (NoMoreProductsException | CurrentAccessException e) {
				try {
					PrositIPC.handleError(e);
					// Pénalité de 5 secondes
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					return;
				}
			}
			catch (Throwable e) {
				PrositIPC.handleError(e);
			}
			
		}
	}

}