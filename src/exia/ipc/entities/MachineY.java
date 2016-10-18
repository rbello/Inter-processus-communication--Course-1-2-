package exia.ipc.entities;

import java.awt.Point;

import exia.ipc.exceptions.MachineAllreadyUsedException;
import exia.ipc.exceptions.ProductLostException;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public class MachineY extends Machine {

	MachineY() {
		super("Y", new Point(328, 72), new Point(260, 95), new Point(335, 95));
	}

	public void executeJob() throws MachineAllreadyUsedException {
		if (counter >= 2) throw new MachineAllreadyUsedException(this);
		
		incrementCounter();

		try {
			
			// On commence le travail
			Thread.sleep(600);

			// On fabrique le nouveau produit, et on lui donne 2 points d'avancement
			final Product product = new Product(Product.Type.M4);
			product.nextStep();
			product.nextStep();
		
			// On demande � l'�tudiant de choisir la bonne machine
			final MachineZ machine = PrositIPC.Step3.chooseMachine((MachineZ) getRoute(0), (MachineZ) getRoute(1));
			
			// On v�rifie qu'une machine a �t� choisie
			if (machine == null) throw new ProductLostException(product, "pas de machine Z choisie");
				
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
						return;
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
							PrositIPC.handleError(new ProductLostException(product, "jamais termin�"));
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
			
		}
		catch (InterruptedException e) {
			return;
		}
		catch (Exception e) {
			decrementCounter();
			PrositIPC.handleError(e);
		}
	}
}
