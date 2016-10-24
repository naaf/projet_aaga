package core;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

public class PointMIS extends java.awt.Point implements Cloneable {

	private Color c;
	private PointMIS dom;
	private List<PointMIS> neighbors;
	private boolean active;
	private int id;

	public PointMIS(Point p) {
		super(p);
	}

	public PointMIS(Point p, Color c, boolean active) {
		super(p);
		this.c = c;
		this.active = active;
		this.id = 0;
	}

	public PointMIS(PointMIS p) {
		super(p);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}

	public PointMIS getDom() {
		return dom;
	}

	public void setDom(PointMIS dom) {
		this.dom = dom;
	}

	public List<PointMIS> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<PointMIS> neighbors) {
		this.neighbors = neighbors;
	}

	public long degree() {
		return neighbors.stream().filter(n -> n.getC() == Color.WHITE).count();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

}
