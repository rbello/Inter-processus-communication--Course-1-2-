package exia.ipc.exceptions;

import exia.ipc.entities.Machine;

public class MachineAllreadyUsedException extends Exception {

	private static final long serialVersionUID = 1L;

	public MachineAllreadyUsedException(Machine subject, Machine user) {
		super("The machine " + subject.getName() + " is allready used, " + user.getName() + " cannot use it !");
	}
	
	public MachineAllreadyUsedException(Machine subject) {
		super("The machine " + subject.getName() + " is allready used !");
	}
	
}
