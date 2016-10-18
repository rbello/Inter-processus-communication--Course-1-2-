package exia.ipc.entities;

import java.awt.Dimension;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.swing.SwingUtilities;

import org.pushingpixels.trident.TimelineScenario;
import org.pushingpixels.trident.TimelineScenario.TimelineScenarioState;
import org.pushingpixels.trident.callback.TimelineScenarioCallback;

import exia.ipc.fail.WrongStep1;
import exia.ipc.fail.WrongStep2;
import exia.ipc.fail.WrongStep3;
import exia.ipc.ihm.Indicator;
import exia.ipc.ihm.Mobile;
import exia.ipc.ihm.PlaceHolder;
import exia.ipc.ihm.View;
import exia.ipc.ihm.ViewController;

/**
 * Communication Inter-Processus (IPC)
 * 
 * @author remi.bello.pro@gmail.com
 * @link https://github.com/rbello
 */
public class PrositIPC {
	
	private static ArrayList<Node> jobs;
	
	public static IStep1Strategy Step1;
	public static IStep2Strategy Step2;
	public static IStep3Strategy Step3;

	private static ViewController ctrl;

	private static int score = 0;

	private static OutputDock outputDock;

	private static AudioInputStream audioStream;
	private static AudioFormat audioFormat;

	private static int audioSize;

	private static byte[] audioData;

	private static javax.sound.sampled.DataLine.Info audioInfo;

	static {
		
		Step1 = new WrongStep1();
		Step2 = new WrongStep2();
		Step3 = new WrongStep3();
		
		try {
			URL yourFile = PrositIPC.class.getResource("/exia/ipc/ihm/res/smw_coin.wav");
			audioStream = AudioSystem.getAudioInputStream(yourFile);
			audioFormat = audioStream.getFormat();
			audioSize = (int) (audioFormat.getFrameSize() * audioStream.getFrameLength());
			audioData = new byte[audioSize];
			audioInfo = new DataLine.Info(Clip.class, audioFormat, audioSize);
			audioStream.read(audioData, 0, audioSize);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		jobs = new ArrayList<Node>();

		// Quais d'arrivé
		final InputDock q1 = new InputDock(Product.Type.M1, new Point(50, 38), new Point(78, 63));
		final InputDock q2 = new InputDock(Product.Type.M2, new Point(50, 160), new Point(78, 130));
		q1.addProducts(3);
		q2.addProducts(2);
		
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
		
		// Suit une machine unique acceptant 2 produits à la fois
		final MachineY m4 = new MachineY();
		
		// P to S
		m1.addRoute(m4, new Point(238, 67), new Point(238, 95));
		m2.addRoute(m4, new Point(230, 95), new Point(238, 95));
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
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				View v = new View();
				ctrl = new ViewController(v);
				ctrl.run();
				beginStart();
			}
		});
		
	}
	
	private static void beginStart() {
		
		System.out.println("Go !");
		
		for (Node r : jobs) {
			if (r instanceof Runnable) {
				new Thread((Runnable)r, "Thread " + r.getClass().getSimpleName()).start();				
			}
			Indicator indicator = new Indicator(r.getIndicatorLocation());
			r.addIndicatorListener(indicator);
			indicator.setSize(new Dimension(16, 14));
			indicator.setLocation(r.getIndicatorLocation());
			ctrl.view.gamePanel.add(indicator);
			
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
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						ctrl.view.labelCoins.setText("" + score);
						System.out.println("Vous avez gagné " + score + " pièces d'or en 30 secondes");
						score = 0;
						outputDock.resetCounter();
					}
				});
				truck();
			}
		}, 30000L, 30000L);
		
	}
	
	static void playSound() {
		try {
			final Clip clip = (Clip) AudioSystem.getLine(audioInfo);
            clip.open(audioFormat, audioData, 0, audioSize);
            clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == Type.STOP) {
						clip.close();
					}
				}
			});
            clip.start();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static void register(Node job) {
		jobs.add(job);
	}

	static void move(Product p, Node from, Node to) {
		move(new Mobile(ctrl.view.gamePanel, p, from, to));
	}
	
	private static void move(final Mobile mobile) {
		
		// On demande le scénario de mouvement au mobile
		final TimelineScenario scenario = mobile.getMoveScenario();
		
		// On s'inscrit comme listener de la fin de l'animation
		final PlaceHolder<Boolean> done = new PlaceHolder<Boolean>(false);
		scenario.addCallback(new TimelineScenarioCallback() {
			public void onTimelineScenarioDone() {
				done.setValue(true);
			}
		});
		
		// On lance le scénario
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ctrl.view.gamePanel.add(mobile);
				scenario.play();
			}
		});
		
		// On bloque jusqu'à la fin de l'animation
		while (done.getValue() == false && scenario.getState() != TimelineScenarioState.DONE);
		
		// On retire le mobile et on repaint la fenêtre
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ctrl.view.gamePanel.remove(mobile);
				ctrl.view.gamePanel.repaint();
			}
		});
		
	}

	static void truck() {
		move(new Mobile(ctrl.view.gamePanel));
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
			score -= 5;
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
