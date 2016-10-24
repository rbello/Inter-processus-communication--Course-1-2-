package exia.ipc.entities;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public interface IStep2Strategy {

	void onMachineRequest(MachineX applicant, MachineY executor) throws Exception;

	void onMachineExecute(MachineX applicant, MachineY executor) throws Exception;
	
}
