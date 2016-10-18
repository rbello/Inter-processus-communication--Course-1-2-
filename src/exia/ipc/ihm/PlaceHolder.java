package exia.ipc.ihm;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public class PlaceHolder<N> {

	private N val;

	public PlaceHolder(N val) {
		setValue(val);
	}

	public void setValue(N val) {
		this.val = val;
	}
	
	public N getValue() {
		return this.val;
	}
	
}
