package exia.ipc.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

import exia.ipc.entities.IndicatorListener;

public class Indicator extends JLabel implements IndicatorListener {

	private static final long serialVersionUID = 1L;
	
	private int value = 0;
	
	private Dimension dimMax = new Dimension(20, 35);
	private Dimension dimMin = new Dimension(25, 20);
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.drawString("" + value, value > 9 ? 1 : 5, 11);
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, getWidth(), 0);
		g.drawLine(0, 0, 0, getHeight());
		g.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
		g.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
	}
	
	public void reset() {
		value = 0;
		invalidate();
	}
	
	public void setValue(int value) {
		this.value = value;
		invalidate();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return value > 9 ? dimMax : dimMin;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return dimMin;
	}
	
	@Override
	public Dimension getMaximumSize() {
		return dimMax;
	}

	@Override
	public void notifyChange(int value) {
//		System.out.println("set " + value);
		setValue(value);
	}

}
