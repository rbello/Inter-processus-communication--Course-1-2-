package exia.ipc.entities;

import exia.ipc.exceptions.CurrentAccessException;
import exia.ipc.exceptions.NoMoreProductsException;

public interface IStep1Strategy {

	Product onMachineRequest(InputDock dock, Machine machine) throws NoMoreProductsException, CurrentAccessException;
	
}