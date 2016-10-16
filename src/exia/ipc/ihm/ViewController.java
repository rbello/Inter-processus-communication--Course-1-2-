package exia.ipc.ihm;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import exia.ipc.entities.PrositIPC;

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
		
		view.startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrositIPC.STARTED = true;
				view.startButton.setVisible(false);
			}
		});
		
		view.setVisible(true);
		
		while (!Thread.interrupted()) {
			
			for (Package p : new ArrayList<>(getMoves())) {
				if (p != null) p.updatePosition();
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					view.repaint();
				}
			});
			
			try {
				Thread.sleep(400);
			}
			catch (InterruptedException e) {
				return;
			}
			
		}
		
	}

	public List<Package> getMoves() {
		return moves;
	}

}
