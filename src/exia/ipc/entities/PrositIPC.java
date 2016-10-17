package exia.ipc.entities;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.swing.SwingUtilities;

import org.pushingpixels.trident.TimelineScenario;
import org.pushingpixels.trident.TimelineScenario.TimelineScenarioState;

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

	private static int score = 0;

	private static OutputDock outputDock;

	private static Clip audioClip;
	private static AudioInputStream audioStream;

	static {
		
		try {
			File yourFile = new File(PrositIPC.class.getResource("/exia/ipc/ihm/res/smw_coin.wav").toURI());
			audioStream = AudioSystem.getAudioInputStream(yourFile);
		    AudioFormat format = audioStream.getFormat();
		    Info info = new DataLine.Info(Clip.class, format);
		    audioClip = (Clip) AudioSystem.getLine(info);
		    audioClip.open(audioStream);
		}
		catch (Exception ex) {
			audioClip = null;
		}
		
		jobs = new ArrayList<Node>();

		Step1 = new WrongStep1();
		Step2 = new WrongStep2();
		Step3 = new WrongStep3();
		
		// Sur le premier quai arrivent les chassis
		final InputDock q1 = new InputDock(Product.Type.M1, new Point(50, 38), new Point(78, 63));
		q1.addProducts(1);
		
		// Sur le second quai arrivent les pièces de carrosserie
		final InputDock q2 = new InputDock(Product.Type.M2, new Point(50, 160), new Point(78, 130));
		q2.addProducts(1);
		
		// Suivent 3 machines qui se partagent les produits sur les quais d'arrivées
		// Ils ont pour but d'assembler les deux pièces
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
		m4.addRoute(m5, new Point(363, 95)/*, new Point(363, 40)*/);
		m4.addRoute(m8, new Point(363, 95)/*, new Point(363, 125)*/);
		
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
				new Thread((Runnable)r, "Thread " + r.getClass().getSimpleName()).start();				
			}
			Indicator indicator = new Indicator();
			r.addIndicatorListener(indicator);
			indicator.setSize(new Dimension(16, 14));
			indicator.setLocation(r.getIndicatorLocation());
			v.gamePanel.add(indicator);
			
			// Debug : afficher les entrées/sorties des machines sur la vue
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
		
		new Thread(ctrl, "View Updater").start();
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						ctrl.view.labelCoins.setText("" + score);
						System.out.println("Vous avez gagné " + score + " € en 30 secondes");
						score = 0;
						outputDock.resetCounter();
					}
				});
				truck();
			}
		}, 30000L, 30000L);
		
	}
	
	static void playSound() {
		if (audioClip == null) return;
		try {
			audioClip.close();
			audioClip.open(audioStream);
		    audioClip.start();
		}
		catch (Exception ex) {}
	}

	static void register(Node job) {
		jobs.add(job);
	}

	static void move(Product p, Node from, Node to) {
		move(new Package(p, from, to));
	}
	
	private static void move(final Package pk) {
		final TimelineScenario scenario = pk.getMoveScenario();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ctrl.view.gamePanel.add(pk);
				scenario.play();
			}
		});
		while (scenario.getState() != TimelineScenarioState.DONE) { }
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ctrl.view.gamePanel.remove(pk);
				ctrl.view.gamePanel.repaint();
			}
		});
	}

	static void truck() {
		move(new Package());
	}
	
	static Thread moveAsynch(final Product p, final Node from, final Node to) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				move(p, from, to);
			}
		}, "Move asynch");
		t.start();
		return t;
	}

	public static View getView() {
		return ctrl.view;
	}

	public static void handleError(Throwable e) {
		if ("exia.ipc.exceptions".equals(e.getClass().getPackage().getName())) {
			System.err.println("Alerte : " + e.getMessage());
		}
		else {
			e.printStackTrace();
		}
	}

	static void score() {
		score  += 10;
		playSound();
	}
	
	public static int getScore() {
		return score;
	}

}
