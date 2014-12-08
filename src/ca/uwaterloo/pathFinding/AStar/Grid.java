package ca.uwaterloo.pathFinding.AStar;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.mapper.NavigationalMap;
import android.graphics.PointF;

public class Grid implements Comparable<Grid> {
	
	static final public float WIDTH = 0.5f;
	static final public float ADJACENT_COST = 0.5f;
	static final public float SLANTING_COST = 0.71f;
	
	static public Grid findGridWithMapAndLocation(PointF loc) {
		int col = (int) Math.floor(loc.x / WIDTH);
		int row = (int) Math.floor(loc.y / WIDTH);
		return new Grid((float)(col + 0.5) * WIDTH, (float)(row + 0.5) * WIDTH);
	}
	
	public PointF center;
	
	private Grid parent;
	private float h;
	private float g;
	
	public Grid(PointF center) {
		this.center = center;
	}
	
	public Grid(float x, float y) {
		this.center = new PointF(x, y);
	}
	
	public List<Grid> getSurroundingGrids() {
		List<Grid> sgs = new ArrayList<>();
		for (int i=-1; i<2; i++) {
			for (int j=-1; j<2; j++) {
				Grid g = new Grid(center.x + i*WIDTH, center.y + j*WIDTH);
				sgs.add(g);
			}
		}
		return sgs;
	}
	
	public void setParent(Grid parent) {
		this.parent = parent;
		g = parent.getG() + getCenterDistance(parent);
	}
	
	public Grid getParent() {
		return parent;
	}
	
	public void setHValueWithEndingGrid(Grid endingGrid) {
		h = Math.abs(endingGrid.center.x - center.x)
				+ Math.abs(endingGrid.center.y - center.y);
	}
	
	public float getH() {
		return h;
	}
	
	public float getG() {
		return g;
	}
	
	public float getF() {
		return h + g;
	}
	
	public float getCenterDistance(Grid grid) {
		return (float) Math.sqrt(Math.pow(Math.abs(grid.center.x - center.x), 2)
				+ Math.pow(Math.abs(grid.center.y - center.y), 2));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().isInstance(this)) {
			Grid pa = (Grid) obj;
			if (pa.center.equals(this.center)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(Grid another) {
		if (getF() > another.getF()) {
			return 1;
		}else if (getF() < another.getF()) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		String str = "Center:(" + center.toString() + "); ";
		str += String.format("(H:%f, G:%f, F:%f)", h, g, getF());
		return str;
	}
	
}
