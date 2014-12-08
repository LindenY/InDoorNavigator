package ca.uwaterloo.sensor;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class SoftSensorManager {

	private static SoftSensorManager sharedManager;
	
	public static SoftSensorManager initSensorManager (Context context) {
		if (sharedManager == null) {
			sharedManager = new SoftSensorManager(context);
		}
		return sharedManager;
	}
	
	public static SoftSensor getSoftSensor(int type) throws Exception {
		if (sharedManager == null) {
			return null;
		}
		return sharedManager.getSensor(type);
	}
	
	private Context context;
	private Map<Integer, SoftSensor> sensors;
	private SoftSensorManager(Context context) {
		this.context = context;
		sensors = new HashMap<>();
	}
	
	private SoftSensor getSensor(int type) throws Exception {
		SoftSensor sensor = sensors.get(type);
		if (sensor == null) {
			sensor = createSensor(type);
			sensors.put(type, sensor);
		}
		return sensor;
	}
	
	private SoftSensor createSensor(int type) throws Exception {
		SoftSensor sensor = null;
		Log.i("SoftSensor", "type: " + type);
		switch (type) {
		case SoftSensor.Type_GeoOrientation:
			sensor = new GeoOrientationSensor(context);
			break;
		case SoftSensor.Type_StepCounter:
			sensor = new StepCounter(context);
			break;
		case SoftSensor.Type_StepCounterWithOrientation:
			sensor = new StepCounterWithOrientation(context);
			break;
		default:
			throw new Exception("Unsupport sensor type: " + type);	
		}
		return sensor;
	}
}
