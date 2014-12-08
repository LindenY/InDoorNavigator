package ca.uwaterloo.sensor;

public class SensorFilter {

	private final float FILTER_CONSTANT;
	private final int DIMENTION;
	private float[] filteredValues;
	
	public SensorFilter(float filterConstant, int dimention) {
		this.FILTER_CONSTANT = filterConstant;
		this.DIMENTION = dimention;
		filteredValues = new float[DIMENTION];
	}
	
	public float[] filter(float[] rawValues) {
		for (int i=0; i<DIMENTION; i++) {
			filteredValues[i] += (rawValues[i] - filteredValues[i]) / FILTER_CONSTANT;
		}
		return filteredValues;
	}
	
	public float[] getFilteredValues() {
		return filteredValues;
	}
	
}
