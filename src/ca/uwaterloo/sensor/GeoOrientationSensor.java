package ca.uwaterloo.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class GeoOrientationSensor extends SoftSensor {
	private Sensor acclSensor;
	private Sensor magnSensor;
	
	//private float[] acclValues;
	//private float[] magnValues;
	
	private SensorFilter acclFilter;
	private SensorFilter magnFilter;
	
	private float[] orientations;
	
	public GeoOrientationSensor(Context context) {
		super(context, Type_GeoOrientation);
		//acclValues = new float[3];
		//magnValues = new float[3];
		orientations = new float[3];
		acclFilter = new SensorFilter(3.5f, 3);
		magnFilter = new SensorFilter(3.0f, 3);

		acclSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		register();
	}
	
	public float[] getOrientations() {
		return orientations;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			//acclValues = event.values;
			acclFilter.filter(event.values);
		}else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			//magnValues = event.values;
			magnFilter.filter(event.values);
		}
		float[] r = new float[9];
		float[] values = new float[3];
		//SensorManager.getRotationMatrix(r, null, acclValues, magnValues);
		SensorManager.getRotationMatrix(r, null, 
				acclFilter.getFilteredValues(), magnFilter.getFilteredValues());
		SensorManager.getOrientation(r, values);
		orientations = values;
		for (SoftSensorListener l : listeners) {
			l.onSensorChanged(this, values);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register() {
		sensorManager.registerListener(this, acclSensor, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, magnSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void unregister() {
		sensorManager.unregisterListener(this);
	}
	
}
