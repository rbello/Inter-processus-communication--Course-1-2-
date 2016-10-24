package exia.ipc.entities;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public interface IStep3Strategy {

	public MachineZ chooseMachine(MachineZ target1, MachineZ target2) throws Exception;
	
	public void onMachineRequest(Product product, MachineZ m1, MachineZ m2, MachineZ m3) throws Exception;
	
}
