package exia.ipc.fail;

import exia.ipc.entities.IStep2Strategy;
import exia.ipc.entities.MachineX;
import exia.ipc.entities.MachineY;

public class WrongStep2 implements IStep2Strategy {

	@Override
	public void onMachineRequest(MachineX applicant, MachineY executor) throws Exception {
		// Erreur : déplacer la marchandise vers la machine sans qu'elle soit prête
	}

	@Override
	public void onMachineExecute(MachineX applicant, MachineY executor) throws Exception {
		// Erreur : tenter de prendre directement sans vérifier que la machine soit prête
		executor.executeJob();
	}
	
}
