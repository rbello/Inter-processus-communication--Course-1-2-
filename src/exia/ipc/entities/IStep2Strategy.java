package exia.ipc.entities;

import exia.ipc.exceptions.MachineAllreadyUsedException;

public interface IStep2Strategy {

	void onMachineRequest(MachineX applicant, MachineY executor) throws MachineAllreadyUsedException;
	
}
