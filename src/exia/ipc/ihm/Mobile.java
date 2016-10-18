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

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public class Mobile extends JLabel {

	private static final long serialVersionUID = 1L;

	/**
	 * Nombre de pixels de déplacement
	 * @deprecated
	 */
	private static final int STEP = 14;
	
	/**
	 * Indique si la destination finale du déplacement a été atteinte.
	 * @deprecated
	 */
	private boolean reached = false;
	
	/**
	 * Liste des points du chemin.
	 * @deprecated
	 */
	private List<Point> route;
	
	/**
	 * Prochain point à atteindre.
	 * @deprecated
	 */
	private Point next;

	/**
	 * Scénario de déplacement.
	 */
	private Sequence scenario;
	
	/**
	 * Construction d'un camion de ramassage en sortie d'usine (Truck)
	 */
	public Mobile(GamePanel panel) {
		
		// Emplacement initial
		setSize(45, 34);
		setLocation(panel.applyRatio(new Point(472, 185)));
		
		// Icône
		setIcon(new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/Truck.png")));
		
		// La route que va faire le camion
		route = new ArrayList<Point>();
		route.add(new Point(new Point(472, 185)));
		route.add(new Point(new Point(472, 230)));
		route.add(new Point(new Point(-80, 230)));
		route = panel.applyRatio(route);
		next = route.remove(0);

		// Fabrication de l'animation
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
	 * Constructeur pour les produits qui transitent dans l'usine (Packages)
	 */
	public Mobile(GamePanel panel, Product p, Node from, Node to) {
		
		// Emplacement initial
		setSize(20, 20);
		setLocation(panel.applyRatio(from.getOutputLocation()));
		
		// Icône
		setIcon(new ImageIcon(GamePanel.class.getResource("/exia/ipc/ihm/res/Package.png")));
		
		// Liste des points de déplacement de l'animation
		route = new ArrayList<Point>(Arrays.asList(from.getRoute(to)));
		route.add(to.getInputLocation());
		route = panel.applyRatio(route);

		// Fabrication de l'animation
		this.scenario = new TimelineScenario.Sequence();
		Point last = panel.applyRatio(from.getOutputLocation());
		for (Point pt : route) {
			Timeline timeline = new Timeline(this);
			timeline.addPropertyToInterpolate("location", last.getLocation(), pt);
			last = pt;
			scenario.addScenarioActor(timeline);
		}
		if (route.size() < 1) throw new NullPointerException("Invalid route");
		next = route.remove(0);
	}
	
	@Deprecated
	public void waitForDestinationReached() {
		while (!reached);
	}

	@Deprecated
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
