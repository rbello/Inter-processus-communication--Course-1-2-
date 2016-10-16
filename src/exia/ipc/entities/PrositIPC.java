package exia.ipc.entities;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import exia.ipc.fail.WrongStep1;
import exia.ipc.fail.WrongStep2;
import exia.ipc.fail.WrongStep3;
import exia.ipc.ihm.Indicator;
import exia.ipc.ihm.Package;
import exia.ipc.ihm.View;
import exia.ipc.ihm.ViewController;

public class PrositIPC {
	
	private static ArrayList<Node> jobs;
	
	public static IStep1Strategy Step1;
	public static IStep2Strategy Step2;
	public static IStep3Strategy Step3;

	private static ViewController ctrl;

	public static boolean STARTED = false;

	private static int score = 0;

	private static OutputDock outputDock;

	static {
		
		jobs = new ArrayList<Node>();

		Step1 = new WrongStep1();
		Step2 = new WrongStep2();
		Step3 = new WrongStep3();
		
		// Sur le premier quai arrivent les chassis
		final InputDock q1 = new InputDock(Product.Type.M1, new Point(50, 38), new Point(78, 63));
		q1.addProducts(1);
		
		// Sur le second quai arrivent les pi�ces de carrosserie
		final InputDock q2 = new InputDock(Product.Type.M2, new Point(50, 160), new Point(78, 130));
		q2.addProducts(1);
		
		// Suivent 3 machines qui se partagent les produits sur les quais d'arriv�es
		// Ils ont pour but d'assembler les deux pi�ces
		final MachineX m1 = new MachineX(1, q1, q2);
		final MachineX m2 = new MachineX(2, q1, q2);
		final MachineX m3 = new MachineX(3, q1, q2);
		
		// P to P
		q1.addRoute(m1, new Point(78, 95), new Point(143, 95), new Point(143, 65));
		q2.addRoute(m1, new Point(78, 95), new Point(143, 95), new Point(143, 65));
		q1.addRoute(m2, new Point(78, 95), new Point(143, 95));
		q2.addRoute(m2, new Point(78, 95), new Point(143, 95));
		q1.addRoute(m3, new Point(78, 95), new Point(143, 95), new Point(143, 125));
		q2.addRoute(m3, new Point(78, 95), new Point(143, 95), new Point(143, 125));
		
		// Suit une machine unique qui introduit le moteur dans la voiture
		final MachineY m4 = new MachineY();
		
		// P to S
		m1.addRoute(m4, new Point(238, 67), new Point(238, 95));
		m2.addRoute(m4, new Point(238, 95));
		m3.addRoute(m4, new Point(238, 125), new Point(238, 95));

		// Suivent deux files de 3 machines Z
		final MachineZ m7 = new MachineZ(2);
		final MachineZ m6 = new MachineZ(1, m7);
		final MachineZ m5 = new MachineZ(0, m6);
		
		final MachineZ m10 = new MachineZ(5);
		final MachineZ m9  = new MachineZ(4, m10);
		final MachineZ m8  = new MachineZ(3, m9);
		
		// S to P
		m4.addRoute(m5, new Point(363, 95), new Point(363, 40));
		m4.addRoute(m8, new Point(363, 95), new Point(363, 125));
		
		// Suit la plateforme de stockage
		outputDock = new OutputDock();
		
		// P to S
		m5.addRoute(outputDock, new Point(420, 100));
		m8.addRoute(outputDock, new Point(420, 100));
		
	}

	public static void start() {
		
		View v = new View();
		ctrl = new ViewController(v);

		for (Node r : jobs) {
			if (r instanceof Runnable) {
//				System.out.println("Init " + r);
				new Thread((Runnable)r, "Thread " + r.getClass().getSimpleName()).start();				
			}
			Indicator indicator = new Indicator();
			r.addIndicatorListener(indicator);
			indicator.setSize(new Dimension(16, 14));
			indicator.setLocation(r.getIndicatorLocation());
			v.gamePanel.add(indicator);
			
			// Debug : afficher les entr�es/sorties des machines sur la vue
//			if (r.getInputLocation() != null) {
//				JPanel p = new JPanel();
//				p.setSize(3, 3);
//				p.setBackground(Color.GREEN);
//				p.setLocation(r.getInputLocation());
//				v.gamePanel.add(p);
//			}
//			if (r.getOutputLocation() != null) {
//				JPanel p = new JPanel();
//				p.setSize(3, 3);
//				p.setBackground(Color.BLUE);
//				p.setLocation(r.getOutputLocation());
//				v.gamePanel.add(p);
//			}
			
		}
		
		new Thread(ctrl).start();
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						ctrl.view.labelCoins.setText("" + score);
						score = 0;
						outputDock.notifyChange(0);
						truck();
					}
				});
			}
		}, 0L, 30000L);
		
	}

	static void register(Node job) {
		jobs.add(job);
	}

	static void move(Product p, Node from, Node to) {
		if (from.getRoute(to) == null) throw new NullPointerException("No route from " + from + " to " + to);
		Package pk = new Package(p, from, to);
		ctrl.getMoves().add(pk);
		ctrl.view.gamePanel.add(pk);
		pk.waitForDestinationReached();
	}
	
	static void truck() {
		Package pk = new Package();
		ctrl.getMoves().add(pk);
		ctrl.view.gamePanel.add(pk);
		pk.waitForDestinationReached();
	}
	
	static Thread moveAsynch(final Product p, final Node from, final Node to) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				move(p, from, to);
			}
		});
		t.start();
		return t;
	}

	public static View getView() {
		return ctrl.view;
	}

	public static void handleError(Throwable e) {
		System.err.println(e.getClass().getSimpleName() + " Alerte ! " + e.getMessage());
	}

	static void score() {
		score  += 10;
	}
	
	public static int getScore() {
		return score;
	}

}