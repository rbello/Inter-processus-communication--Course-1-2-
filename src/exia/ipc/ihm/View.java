package exia.ipc.ihm;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;

public class View extends JFrame {

	private static final long serialVersionUID = 5113884946042341761L;

	public JPanel contentPane;
	public GamePanel gamePanel;
	public JLabel labelCoins;
	public JTextArea textArea;

	JButton startButton;

	/**
	 * Create the frame.
	 */
	public View() {
		setTitle("Exia - Prosit IPC");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 604, 483);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		gamePanel = new GamePanel();
		gamePanel.setOpaque(false);
		gamePanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(scrollPane, Alignment.LEADING)
						.addComponent(gamePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(1, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(gamePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
		);
		
		labelCoins = new JLabel("0");
		labelCoins.setIcon(new ImageIcon(View.class.getResource("/exia/ipc/ihm/res/coins.png")));
		labelCoins.setBounds(10, 11, 90, 14);
		gamePanel.add(labelCoins);
		
		startButton = new JButton("Go !");
		startButton.setBounds(251, 290, 71, 23);
		gamePanel.add(startButton);
		
		textArea = new JTextArea();
		textArea.setForeground(Color.RED);
		scrollPane.setViewportView(textArea);
		contentPane.setLayout(gl_contentPane);
	}
}
