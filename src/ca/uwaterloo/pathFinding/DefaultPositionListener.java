package ca.uwaterloo.pathFinding;

import java.util.List;

import android.content.pm.PathPermission;
import android.graphics.PointF;
import ca.uwaterloo.mapper.MapView;
import ca.uwaterloo.mapper.NavigationalMap;
import ca.uwaterloo.mapper.PositionListener;
import ca.uwaterloo.pathFinding.AStar.AStarPathFinding;
import ca.uwaterloo.pathFinding.AStar.Grid;

public class DefaultPositionListener implements PositionListener{

	private PointF org;
	private PointF des;
	private PathFinding pathFinding;
	private List<PointF> path;
	
	public DefaultPositionListener() {
		pathFinding = new AStarPathFinding(null);
	}

	@Override
	public void originChanged(MapView source, PointF loc) {
		source.setUserPoint(loc);
		org = loc;
		onEndPointsChange(source);
	}

	@Override
	public void destinationChanged(MapView source, PointF dest) {
		des = dest;
		onEndPointsChange(source);
	}
	
	public List<PointF> getPath() {
		return path;
	}
	
	private void onEndPointsChange(MapView mapView) {
		if (org != null && des != null) {
			pathFinding.setMap(mapView.getMap());
			pathFinding.setOriginPoint(org);
			pathFinding.setDestinationPoint(des);
			mapView.removeAllLabeledPoints();
			mapView.setUserPath(null);
			path = pathFinding.getPath();
			if (path != null) {
				mapView.setUserPath(path);
			} else {
				mapView.addLabeledPoint(des, "No path found");
			}
		}
	}
}
