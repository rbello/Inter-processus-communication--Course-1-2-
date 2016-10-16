package exia.ipc.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Node {

	private List<IndicatorListener> listeners = new ArrayList<IndicatorListener>();
	protected Map<Node, Point[]> routes = new HashMap<Node, Point[]>();
	private String name;
	private Point indicatorLocation;
	private Point inputLocation;
	private Point outputLocation;

	Node(String name, Point indicatorLocation, Point inputLocation, Point outputLocation) {
		this.name = name;
		this.indicatorLocation = indicatorLocation;
		this.inputLocation = inputLocation;
		this.outputLocation = outputLocation;
		PrositIPC.register(this);
	}
	
	void addIndicatorListener(IndicatorListener l) {
		listeners.add(l);
	}

	void notifyChange(int value) {
		for (IndicatorListener i : listeners) {
			i.notifyChange(value);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Point getIndicatorLocation() {
		return indicatorLocation;
	}
	
	public Point getInputLocation() {
		return inputLocation;
	}
	
	public Point getOutputLocation() {
		return outputLocation;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + name + "]";
	}
	
	void addRoute(Node target, Point... points) {
		routes.put(target, points);
	}
	
	public Point[] getRoute(Node target) {
		return routes.get(target);
	}
	
	public Node getRoute(int index) {
		return new ArrayList<Node>(routes.keySet()).get(index);
	}
	
	int counter = 0;

	public void incrementCounter() {
		counter++;
		notifyChange(counter);
	}
	
	public void decrementCounter() {
		counter--;
		notifyChange(counter);
	}
	
	public void resetCounter() {
		counter = 0;
		notifyChange(0);
	}
	
}
