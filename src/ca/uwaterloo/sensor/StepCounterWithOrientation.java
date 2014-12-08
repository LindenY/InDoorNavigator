package ca.uwaterloo.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

public class StepCounterWithOrientation extends SoftSensor implements SoftSensorListener {

	private SoftSensor stepCounter;
	private SoftSensor orientation;
	
	private float azimuth;
	
	public StepCounterWithOrientation(Context context) {
		super(context, Type_StepCounterWithOrientation);
		
		try {
			stepCounter = SoftSensorManager.getSoftSensor(Type_StepCounter);
			orientation = SoftSensorManager.getSoftSensor(Type_GeoOrientation);
			stepCounter.addListener(this);
			orientation.addListener(this);
		} catch (Exception e) {
			Log.e("StepCounterWithOri", "Failed to get soft sensors");
		}		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SoftSensor sensor, float[] values) {
		if (sensor.getType() == Type_GeoOrientation) {
			azimuth = values[0];
		} else if (sensor.getType() == Type_StepCounter) {
			float[] v = new float[]{azimuth};
			for (SoftSensorListener l : listeners) {
				l.onSensorChanged(this, v);
			}
		}
	}

	@Override
	public void register() {
		if (stepCounter != null) {
			stepCounter.register();
		}
		if (orientation != null) {
			orientation.register();
		}
	}

	@Override
	public void unregister() {
		if (stepCounter != null) {
			stepCounter.unregister();
		}
		if (orientation != null) {
			orientation.unregister();
		}
	}

}
