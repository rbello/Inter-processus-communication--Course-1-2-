package exia.ipc.entities;

import java.awt.Point;

import exia.ipc.exceptions.MachineAllreadyUsedException;
import exia.ipc.exceptions.ProductLostException;

public class MachineY extends Machine {

	private boolean job = false;

	MachineY() {
		super("Y", new Point(328, 72), new Point(260, 95), new Point(335, 95));
	}

	public void executeJob() throws MachineAllreadyUsedException {
		if (job) throw new MachineAllreadyUsedException(this);
		job = true;
		while (job) { }
	}
	
	@Override
	public void run() {
		
		while (!Thread.interrupted()) {
			
			try {
				if (job) {
					
					// On commence le travail
					//notifyChange(1);
					Thread.sleep(500);

					// On fabrique le nouveau produit, et on lui donne 2 points d'avancement
					final Product product = new Product(Product.Type.M4);
					product.nextStep();
					product.nextStep();
					
					// On demande � l'�tudiant de choisir la bonne machine
					final MachineZ machine = PrositIPC.Step3.chooseMachine((MachineZ) getRoute(0), (MachineZ) getRoute(1));
					
					// On v�rifie qu'une machine a �t� choisie
					if (machine == null) throw new ProductLostException(product, "no machine choosen");
					
					new Thread(new Runnable() {
						public void run() {
							
							// On d�place le produit
							PrositIPC.move(product, MachineY.this, machine);
							
							// On fait executer le travail
							try {
								PrositIPC.Step3.onMachineRequest(
										product,
										machine,
										machine.getNext(),
										machine.getNext().getNext()
								);
							} catch (Throwable t) {
								PrositIPC.handleError(t);
							}
							
							// On v�rifie que le produit soit termin�
							int i = 0;
							while (!product.isFinished()) {
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									return;
								}
								if (i++ >= 10) {
									PrositIPC.handleError(new ProductLostException(product, "never finished"));
									return;
								}
							}
							
							// Quand c'est termin�, on envoie vers le d�p�t
							PrositIPC.move(product, machine, machine.getOutputNode());
							
							// Et on incr�mente le compteur de la plateforme de stockage
							machine.getOutputNode().incrementCounter();
							
							// Et on incr�mente le score
							PrositIPC.score();
							
						}
					}, "Y to Z").start();
					
					// Le produit sort de cette machine
					decrementCounter();
					job = false;
					
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

}
