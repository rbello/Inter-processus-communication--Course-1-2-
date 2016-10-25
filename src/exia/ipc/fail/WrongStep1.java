package exia.ipc.fail;

import exia.ipc.entities.IStep1Strategy;
import exia.ipc.entities.InputDock;
import exia.ipc.entities.MachineX;
import exia.ipc.entities.Product;

public class WrongStep1 implements IStep1Strategy {

	@Override
	public Product onMachineRequest(InputDock dock, MachineX machine) throws Exception {
		// Erreur : tenter de prendre directement sans v�rifier
		return dock.accept();
	}

}
