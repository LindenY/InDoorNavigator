package ca.uwaterloo.sensor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class SoftSensor implements SensorEventListener {
	
	public static final int Type_GeoOrientation = 0;
	public static final int Type_StepCounter = 1;
	public static final int Type_StepCounterWithOrientation = 2;
	
	protected final SensorManager sensorManager;
	protected final List<SoftSensorListener> listeners;
	private final int type;

	public SoftSensor(Context context, int type) {
		this.type = type;
		listeners = new ArrayList<>();
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	public void addListener(SoftSensorListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(SoftSensorListener listener) {
		listeners.remove(listener);
	}
	
	public int getType() {
		return type;
	}
	
	public abstract void register();
	public abstract void unregister();
}
