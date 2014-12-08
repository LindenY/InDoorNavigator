package ca.uwaterloo.pathFinding.AStar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.PointF;
import ca.uwaterloo.mapper.InterceptPoint;
import ca.uwaterloo.mapper.NavigationalMap;
import ca.uwaterloo.pathFinding.PathFinding;
import ca.uwaterloo.pathFinding.PathFindingDelegate;

public class AStarPathFinding extends PathFinding{
	
	private List<Grid> openList;
	private List<Grid> closeList;
	
	private Grid startingGrid;
	private Grid currentGird;
	private Grid endingGrid;
	
	public AStarPathFinding(PathFindingDelegate delegate) {
		super(delegate);
		
		openList = new ArrayList<>();
		closeList = new ArrayList<>();
		
		this.setOriginPoint(org);
		this.setDestinationPoint(des);
	}
	
	@Override
	public void setOriginPoint(PointF org) {
		this.org = org;
		if (org != null) {
			startingGrid = Grid.findGridWithMapAndLocation(org);
		}
	}
	
	@Override
	public void setDestinationPoint(PointF des) {
		this.des = des;
		if (des != null) {
			endingGrid = Grid.findGridWithMapAndLocation(des);
		}
	}

	@Override
	protected List<PointF> calculatePath() {
		openList.clear();
		closeList.clear();
		openList.add(startingGrid);
		if (AStarPathFinding()) {
			List<PointF> path = getPathFromGrid(currentGird);
			path.add(0, des);
			path.add(path.size(), org);
			return smoothPath(path);
		}
		return null;
	}
	
	private boolean AStarPathFinding() {
		while (openList.size() != 0) {
			currentGird = openList.get(0);
			if (currentGird.equals(endingGrid)) {
				return true;
			}
			openList.remove(currentGird);
			closeList.add(currentGird);
			List<Grid> surrounding = getSurroundingGrids(currentGird);
			for (Grid g : surrounding) {
				Grid existGrid = getEqualGridInList(openList, g);
				if (existGrid != null) {
					if (g.getG() < existGrid.getG()) {
						existGrid.setParent(currentGird);
						Collections.sort(openList);
					}
				} else {
					addGridToOpenList(g);
				}
			}
		}
		return false;
	}
	
	private void addGridToOpenList(Grid newGrid) {
		int size = openList.size();
		if (size == 0) {
			openList.add(newGrid);
			return;
		}
		for (int i=size-1; i>=0; i--) {
			if (openList.get(i).getF() < newGrid.getF()) {
				openList.add(i+1, newGrid);
				return;
			}else if (i == 0) {
				openList.add(0, newGrid);
			}
		}
	}
	
	private List<Grid> getSurroundingGrids(Grid grid) {
		List<Grid> surrounding = grid.getSurroundingGrids();
		List<Grid> walkableSurronding = new ArrayList<>();
		for (Grid g : surrounding) {
			List<InterceptPoint> interPoints = map.calculateIntersections(grid.center, g.center);
			if (interPoints.size() == 0
					&& (getEqualGridInList(closeList, g) == null)) {
				g.setHValueWithEndingGrid(endingGrid);
				g.setParent(grid);
				walkableSurronding.add(g);
			}
		}
		return walkableSurronding;
	}
	
	private Grid getEqualGridInList(List<Grid> list, Grid grid) {
		for (Grid g : list) {
			if (g.equals(grid)) {
				return g;
			}
		}
		return null;
	}
	
	private List<PointF> getPathFromGrid(Grid grid) {
		Grid g = grid;
		List<PointF> paths = new ArrayList<>();
		paths.add(g.center);
		while (g.getParent() != null) {
			g = g.getParent();
			paths.add(g.center);
		}
		return paths;
	}

	private List<PointF> smoothPath(List<PointF> path) {
		List<PointF> smoothedPath = new ArrayList<>();
		PointF startingPoint = path.get(0);
		smoothedPath.add(startingPoint);
		for (int i=1; i<path.size(); i++) {
			if (map.calculateIntersections(startingPoint, path.get(i)).size() != 0) {
				startingPoint = path.get(i - 1);
				smoothedPath.add(startingPoint);
			}
		}
		smoothedPath.add(path.get(path.size() - 1));
		
		if (smoothedPath.size() < path.size()) {
			return smoothPath(smoothedPath);
		} else {
			return smoothedPath;
		}
	}
}
