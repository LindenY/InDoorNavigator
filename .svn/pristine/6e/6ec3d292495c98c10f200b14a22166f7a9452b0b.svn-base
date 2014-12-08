package ca.uwaterloo.navigation;

import java.util.List;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import ca.uwaterloo.mapper.MapView;
import ca.uwaterloo.mapper.NavigationalMap;
import ca.uwaterloo.mapper.PositionListener;
import ca.uwaterloo.mapper.VectorUtils;
import ca.uwaterloo.navigationView.GuidelineView;
import ca.uwaterloo.navigationView.NavigationalView;
import ca.uwaterloo.pathFinding.PathFinding;
import ca.uwaterloo.pathFinding.AStar.AStarPathFinding;
import ca.uwaterloo.sensor.SoftSensor;
import ca.uwaterloo.sensor.SoftSensorListener;
import ca.uwaterloo.sensor.SoftSensorManager;

public class Navigation implements PositionListener, SoftSensorListener{
	
	private final float TOLERANCE = 1.2f;
	private final float STEP_LENGTH = 0.75f;
	
	private MapView mapView;
	private NavigationalView navigationView;
	
	private PathFinding pathFinding;
	
	private List<PointF> path;
	
	public Navigation (Context context, MapView mapView) {
		this.mapView = mapView;
		
		mapView.addListener(this);
		pathFinding = new AStarPathFinding(null);
		navigationView = new NavigationalView(context);
		
		try {
			SoftSensor stepCounter = SoftSensorManager.getSoftSensor(SoftSensor.Type_StepCounterWithOrientation);
			stepCounter.addListener(this);
		} catch (Exception e) {
			Log.e("Navigation", "Fail to get StepCounter sensor");
		}
	}
	
	public NavigationalView getView() {
		return navigationView;
	}
	
	private void calculatePath(NavigationalMap map, PointF org, PointF des) {
		pathFinding.setMap(mapView.getMap());
		pathFinding.setOriginPoint(org);
		pathFinding.setDestinationPoint(des);
		mapView.removeAllLabeledPoints();
		mapView.setUserPath(null);
		navigationView.setInstructionString("Calculating the path...");
		path = pathFinding.getPath();
		if (path != null) {
			mapView.setUserPath(path);
			onUserLocationChanged(org);
		} else {
			navigationView.setInstructionString("No path found");
		}
	}
	
	private void onUserLocationChanged(PointF loc) {
		PointF guidePoint = getGuidelinePoint(loc);
		if (!ifPointCloseToPath(loc) || guidePoint == null) {
			mapView.setOriginPoint(loc);
			calculatePath(mapView.getMap(), loc, mapView.getDestinationPoint());
		}
		if (ifTwoPointsAreClose(loc, mapView.getDestinationPoint())) {
			navigationView.setInstructionString("You have reached your destination.");
			return;
		}
		navigationView.setInstructionString(getInstructionStringFromTwoPoints(loc, guidePoint));
		navigationView.setPathAngle(getAngleBetweenPathAndNorth(loc, guidePoint));
	}
	
	private PointF getGuidelinePoint(PointF loc) {
		if (path == null) {
			return null;
		}
		for (int i=0; i<path.size(); i++) {
			PointF guidePoint = path.get(i);
			if (mapView.getMap().calculateIntersections(loc, guidePoint).size() == 0) {
				return guidePoint;
			}
		}
		return null;
	}
	
	private float getAngleBetweenPathAndNorth(PointF p1, PointF p2) {
		float angle = VectorUtils.angleBetween(p1, new PointF(p1.x, p1.y - 1), p2);
		return angle;
	}
	
	private String getInstructionStringFromTwoPoints(PointF p1, PointF p2) {
		float angle = Math.round(getAngleBetweenPathAndNorth(p1, p2) / Math.PI * 180);
		float distance = VectorUtils.distance(p1, p2);
		String str = "please walk " + Math.round(distance / STEP_LENGTH) + " step toward ";
		if (angle <= 0) {
			if (angle >= -90) {
				str += "N" + -angle + "W";
			} else {
				str += "S" + (angle + 180) + "W";
			}
		} else {
			if (angle <= 90) {
				str += "N" + angle + "E";
			} else {
				str += "S" + (180 - angle) + "E";
			}
		}
		return str;
	}
	
	private boolean ifTwoPointsAreClose(PointF p1, PointF p2) {
		return (VectorUtils.distance(p1, p2) <= TOLERANCE);
	}
	
	private boolean ifPointCloseToPath(PointF point) {
		if (path == null) {
			return false;
		}
		if (ifTwoPointsAreClose(path.get(0), point)) {
			return true;
		}
		for (int i=1; i<path.size(); i++) {
			PointF p1 = path.get(i);
			PointF p2 = path.get(i - 1);
			if (ifTwoPointsAreClose(p1, point)) {
				return true;
			}
			if (point.x >= Math.min(p1.x, p2.x) && point.x <= Math.max(p1.x, p2.x)
					&& point.y >= Math.min(p1.y, p2.y) && point.y < Math.max(p1.y, p2.y)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void originChanged(MapView source, PointF loc) {
		mapView.setUserPoint(loc);
		calculatePath(source.getMap(), loc, mapView.getDestinationPoint());
	}

	@Override
	public void destinationChanged(MapView source, PointF dest) {
		calculatePath(source.getMap(), source.getOriginPoint(), dest);
	}

	@Override
	public void onSensorChanged(SoftSensor sensor, float[] values) {
		PointF org = mapView.getUserPoint();
		PointF des = new PointF();
		des.x = org.x + STEP_LENGTH * (float) Math.sin(values[0]);
		des.y = org.y - STEP_LENGTH * (float) Math.cos(values[0]);
		if (mapView.getMap().calculateIntersections(org, des).size() == 0) {
			mapView.setUserPoint(des);
			onUserLocationChanged(des);
		}
	}
	
}
