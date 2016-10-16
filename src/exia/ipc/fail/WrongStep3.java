package exia.ipc.fail;

import exia.ipc.entities.IStep3Strategy;
import exia.ipc.entities.MachineZ;
import exia.ipc.entities.Product;
import exia.ipc.exceptions.AllreadyFinishedProductException;
import exia.ipc.exceptions.MachineAllreadyUsedException;

public class WrongStep3 implements IStep3Strategy {

	@Override
	public MachineZ chooseMachine(MachineZ target1, MachineZ target2) {
		// Erreur : on ne répartit pas la charge sur les machines, et on n'attends pas qu'elles soient disponibles
		return target1;
	}

	@Override
	public void onMachineRequest(Product product, MachineZ m1, MachineZ m2, MachineZ m3)
			throws MachineAllreadyUsedException, AllreadyFinishedProductException {
		// Erreur : on execute toutes les tâches en séries, pas en parallèle
		m1.executeJob(product);
		m2.executeJob(product);
		m3.executeJob(product);
		product.makeFinished();
	}

}
