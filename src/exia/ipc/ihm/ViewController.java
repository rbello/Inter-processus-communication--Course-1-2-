package exia.ipc.ihm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ViewController implements Runnable {

	public View view;

	private List<Package> moves;
	
	public ViewController(View v) {
		this.view = v;
		this.moves = new ArrayList<Package>();
	}

	@Override
	public void run() {
		
		// Redirection de System.out vers la console de la fenêtre
		MessageConsole mc = new MessageConsole(view.textArea);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		
		// Affichage de la fenêtre
		view.setVisible(true);
		
	}

	public List<Package> getMoves() {
		return moves;
	}

}
