package ca.uwaterloo.sensor;

public interface SoftSensorListener {
	
	public void onSensorChanged(SoftSensor sensor, float[] values);
}
