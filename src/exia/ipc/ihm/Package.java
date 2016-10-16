package exia.ipc.ihm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import exia.ipc.entities.Node;
import exia.ipc.entities.Product;
import exia.ipc.entities.PrositIPC;

public class Package extends JLabel {

	private static final long serialVersionUID = 1L;

	private static final int STEP = 14;
	
	private boolean reached = false;
	private List<Point> route;
	private Point next;
	
	public Package() {
		setSize(45, 34);
		setIcon(new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/Truck.png")));
		setLocation(new Point(472, 185));
		route = new ArrayList<Point>();
		route.add(new Point(new Point(472, 185)));
		route.add(new Point(new Point(472, 230)));
		route.add(new Point(new Point(0, 230)));
		next = route.remove(0);
	}
	
	public Package(Product p, Node from, Node to) {
		setIcon(new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/Package.png")));
		//setText(p.getType().toString());
		//setForeground(Color.YELLOW);
		setLocation(from.getOutputLocation());
		setSize(20, 20);
		route = new ArrayList<Point>(Arrays.asList(from.getRoute(to)));
		route.add(to.getInputLocation());
		if (route.size() < 1) throw new NullPointerException("Invalid route");
		next = route.remove(0);
	}
	
	public void waitForDestinationReached() {
		while (!reached) { }
	}

	public void updatePosition() {
		boolean reached = true;
		Point loc = getLocation();
		if (Math.abs(loc.x - next.x) < STEP) {
			loc.x = next.x;
		}
		else {
			reached = false;
			if (loc.x > next.x) loc.x -= STEP;
			else if (loc.x < next.x) loc.x += STEP;
		}
		if (Math.abs(loc.y - next.y) < STEP) {
			loc.y = next.y;
		}
		else {
			reached = false;
			if (loc.y > next.y) loc.y -= STEP;
			else if (loc.y < next.y) loc.y += STEP;
		}
		setLocation(loc);
		if (reached) {
			if (!route.isEmpty()) {
				next = route.remove(0);
			}
			else {
				PrositIPC.getView().gamePanel.remove(this);
				this.reached = true;
			}
		}
	}
	
}
