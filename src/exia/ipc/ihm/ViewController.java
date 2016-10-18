package exia.ipc.ihm;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public class ViewController {

	public View view;

	private List<Mobile> moves;
	
	public ViewController(View v) {
		this.view = v;
		this.moves = new ArrayList<Mobile>();
	}

	public void run() {
		
		// Redirection de System.out vers la console de la fenêtre
		MessageConsole mc = new MessageConsole(view.textArea);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		
		view.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				for (Component c : view.gamePanel.getComponents()) {
					if (c instanceof Indicator) {
						c.setLocation(view.gamePanel.applyRatio(((Indicator) c).getDefaultLocation()));
					}
				}
			}
		});
		
		// Affichage de la fenêtre
		view.setVisible(true);
		
	}

	public List<Mobile> getMoves() {
		return moves;
	}

}
