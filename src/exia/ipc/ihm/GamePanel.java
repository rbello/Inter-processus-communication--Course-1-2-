package exia.ipc.ihm;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private Dimension dim = new Dimension(587, 324);
	private Image imgA = new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/BackgroundA.png")).getImage();
	private Image imgB = new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/BackgroundB.png")).getImage();
	
	private boolean peer = true;
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(peer ? imgA : imgB, 0, 0, null);
		peer = !peer;
		super.paint(g);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return dim;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return dim;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return dim;
	}

}
