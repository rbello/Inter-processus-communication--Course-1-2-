package exia.ipc.fail;

import exia.ipc.entities.IStep1Strategy;
import exia.ipc.entities.InputDock;
import exia.ipc.entities.Machine;
import exia.ipc.entities.Product;
import exia.ipc.exceptions.CurrentAccessException;
import exia.ipc.exceptions.NoMoreProductsException;

public class WrongStep1 implements IStep1Strategy {

	@Override
	public Product onMachineRequest(InputDock dock, Machine machine) throws NoMoreProductsException, CurrentAccessException {
		// Erreur : tenter de prendre directement sans vérifier
		return dock.accept();
	}

}
