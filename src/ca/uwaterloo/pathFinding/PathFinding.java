package ca.uwaterloo.pathFinding;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Path;
import android.graphics.PointF;
import android.os.AsyncTask;
import ca.uwaterloo.mapper.NavigationalMap;
import ca.uwaterloo.pathFinding.AStar.Grid;

public abstract class PathFinding{
	protected NavigationalMap map;
	protected PointF org;
	protected PointF des;
	private PathFindingDelegate delegate;
	
	public PathFinding(PathFindingDelegate delegate) {
		this.delegate = delegate;
	}
	
	public List<PointF> getPath() {
		if (map == null || org == null || des == null) {
			return null;
		}
		if (map.calculateIntersections(org, des).size() == 0) {
			List<PointF> path = new ArrayList<>();
			path.add(des);
			path.add(org);
			return path;
		}
		return calculatePath();
	}
	
	public void setMap(NavigationalMap map) {
		this.map = map;
	}
	
	public void setOriginPoint(PointF org) {
		this.org = org;
	}
	
	public void setDestinationPoint(PointF des) {
		this.des = des;
	}
	
	public NavigationalMap getMap() {
		return map;
	}
	
	public PointF getOriginPoint() {
		return org;
	}
	
	public PointF getDestinationPoint() {
		return des;
	}
	
	protected abstract List<PointF> calculatePath();
	
	private class PathFindingCalculation extends AsyncTask<PathFinding, Boolean, List<PointF>> {

		@Override
		protected List<PointF> doInBackground(PathFinding... params) {
			PathFinding pf = params[0];
			return pf.calculatePath();
		}
		
		@Override
		protected void onPostExecute(List<PointF> result) {
			delegate.pathFindingResult(result);
		}
		
	}
}
