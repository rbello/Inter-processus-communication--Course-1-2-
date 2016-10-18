package exia.ipc.ihm;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public class GamePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private Dimension dim = new Dimension(587, 324);
	private Image imgA = new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/BackgroundA.png")).getImage();
	//private Image imgB = new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/BackgroundB.png")).getImage();
	
	private boolean peer = true;
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(imgA, 0, 0, getWidth(), getHeight(), null);
		peer = !peer;
		super.paint(g);
	}
	
	public double getRatioX() {
		return getWidth() / dim.getWidth();
	}
	
	public double getRatioY() {
		return getHeight() / dim.getHeight();
	}
	
//	@Override
//	public Dimension getMaximumSize() {
//		return dim;
//	}
	
	@Override
	public Dimension getMinimumSize() {
		return dim;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return dim;
	}

	public Point applyRatio(Point location) {
		return new Point((int)(location.x * getRatioX()), (int)(location.y * getRatioY()));
	}

	public List<Point> applyRatio(List<Point> route) {
		List<Point> out = new ArrayList<Point>();
		for (Point p : route) {
			out.add(applyRatio(p));
		}
		return out;
	}

}
