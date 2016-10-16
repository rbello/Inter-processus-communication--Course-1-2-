package exia.ipc.fail;

import exia.ipc.entities.IStep2Strategy;
import exia.ipc.entities.MachineX;
import exia.ipc.entities.MachineY;
import exia.ipc.exceptions.MachineAllreadyUsedException;

public class WrongStep2 implements IStep2Strategy {

	@Override
	public void onMachineRequest(MachineX applicant, MachineY executor) throws MachineAllreadyUsedException {
		// Erreur : tenter de prendre directement sans vérifier
		executor.executeJob();
	}

}
