package exia.ipc.entities;

import exia.ipc.exceptions.MachineAllreadyUsedException;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public interface IStep2Strategy {

	void onMachineRequest(MachineX applicant, MachineY executor) throws InterruptedException;

	void onMachineExecute(MachineX applicant, MachineY executor) throws MachineAllreadyUsedException;
	
}
