package exia.ipc.ihm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.TimelineScenario;
import org.pushingpixels.trident.TimelineScenario.Sequence;

import exia.ipc.entities.Node;
import exia.ipc.entities.Product;
import exia.ipc.entities.PrositIPC;

public class Mobile extends JLabel {

	private static final long serialVersionUID = 1L;

	private static final int STEP = 14;
	
	private boolean reached = false;
	private List<Point> route;
	private Point next;

	private Sequence scenario;
	
	/**
	 * Truck
	 */
	public Mobile() {
		setSize(45, 34);
		setIcon(new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/Truck.png")));
		setLocation(new Point(472, 185));
		
		route = new ArrayList<Point>();
		route.add(new Point(new Point(472, 185)));
		route.add(new Point(new Point(472, 230)));
		route.add(new Point(new Point(-80, 230)));
		
		next = route.remove(0);
		
		this.scenario = new TimelineScenario.Sequence();
		
		int i = 0;
		Point last = next;
		for (Point pt : route) {
			Timeline timeline = new Timeline(this);
			timeline.addPropertyToInterpolate("location", last.getLocation(), pt);
			timeline.setDuration(i++ > 0 ? 4000 : 800);
			last = pt;
			scenario.addScenarioActor(timeline);
		}
		
		
	}
	
	/**
	 * Packages
	 */
	public Mobile(Product p, Node from, Node to) {
		
		route = new ArrayList<Point>(Arrays.asList(from.getRoute(to)));
		route.add(to.getInputLocation());
		
		this.scenario = new TimelineScenario.Sequence();
		
		Point last = from.getOutputLocation();
		for (Point pt : route) {
			Timeline timeline = new Timeline(this);
			timeline.addPropertyToInterpolate("location", last.getLocation(), pt);
			last = pt;
			scenario.addScenarioActor(timeline);
		}
		
		setIcon(new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/Package.png")));
		//setText(p.getType().toString());
		//setForeground(Color.YELLOW);
		setLocation(from.getOutputLocation());
		setSize(20, 20);
		
		
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

	public TimelineScenario getMoveScenario() {
		return scenario;
	}
	
}