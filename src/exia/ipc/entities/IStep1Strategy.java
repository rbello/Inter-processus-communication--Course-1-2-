package exia.ipc.entities;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public interface IStep1Strategy {

	Product onMachineRequest(InputDock dock, MachineX machine) throws Exception;
	
}
