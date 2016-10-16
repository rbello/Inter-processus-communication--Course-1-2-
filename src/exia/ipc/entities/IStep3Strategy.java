package exia.ipc.entities;

import exia.ipc.exceptions.AllreadyFinishedProductException;
import exia.ipc.exceptions.MachineAllreadyUsedException;

public interface IStep3Strategy {

	public MachineZ chooseMachine(MachineZ target1, MachineZ target2) throws InterruptedException;
	
	public void onMachineRequest(Product product, MachineZ m1, MachineZ m2, MachineZ m3)
			throws MachineAllreadyUsedException, AllreadyFinishedProductException;
	
}
