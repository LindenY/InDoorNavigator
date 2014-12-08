package ca.uwaterloo.sensor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class StepCounter extends SoftSensor{
	
	private final float Sensor_Filter_Constant = 3.5f;
	private final int Trand_Capacity = 50;
	
	private final float TopPeak_Altitude = 3;
	private final float TopPeakResting_Offset = 0.1f;
	private final float TopPeakRising_Offset = 0.55f;
	private final float TopPeakUpperBound_Offset = 2;
	private final float BotPeak_Altitude = -2;
	private final float BotPeakResting_Offset = 0.1f;
	private final float BotPeakFalling_Offset = 0.55f;
	private final float BotPeakLowerBound_Offset = 2;
	
	
	private Sensor linearAcclSensor;
	private SensorFilter linearAcclFilter;

	public enum STATE {
		Resting,
		Rising,
		TopPeak,
		Falling,
		BotPeak,
		Bouncing
	}
	
	private STATE currentState;
	private TrendCalculator trendCalculator;
	private ValueRecord topPeakRecord;
	private ValueRecord botPeakRecord;
	
	public StepCounter(Context context) {
		super(context, Type_StepCounter);
		linearAcclSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		register();
		
		currentState = STATE.Resting;
		
		linearAcclFilter = new SensorFilter(Sensor_Filter_Constant, 3);
		trendCalculator = new TrendCalculator(Trand_Capacity);
		topPeakRecord = new ValueRecord(1);
		botPeakRecord = new ValueRecord(-1);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		linearAcclFilter.filter(event.values);
		float value = linearAcclFilter.getFilteredValues()[2];
		switch (currentState) {
		case Resting:
			inResting(value);
			break;
			
		case Rising:
			inRising(value);
			break;
		
		case TopPeak:
			inTopPeak(value);
			break;
		
		case Falling:
			inFalling(value);
			break;
		
		case BotPeak:
			inBotPeak(value);
			break;
			
		case Bouncing:
			inBouncing(value);
			break;
		
		default:
			break;
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register() {
		sensorManager.registerListener(this, linearAcclSensor, SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	public void unregister() {
		sensorManager.unregisterListener(this);
	}
	
	private void nextState() {
		if (currentState == STATE.Bouncing) {
			currentState = STATE.Resting;
			transitionFinished();
		} else {
			currentState = STATE.values()[currentState.ordinal() + 1];
		}
	}
	
	private void rollback() {
		currentState = STATE.Resting;
	}
	
	private void transitionFinished() {
		if (topPeakRecord.getRecordValue() <= TopPeak_Altitude * TopPeakUpperBound_Offset &&
				botPeakRecord.getRecordValue() >= BotPeak_Altitude * BotPeakLowerBound_Offset) {
			for (SoftSensorListener l : listeners) {
				l.onSensorChanged(this, null);
			}
		}
	}
	
	private void inResting(float value) {
		float tpa = TopPeak_Altitude;
		if (value >= tpa * TopPeakResting_Offset) {
			if (trendCalculator != null) {
				trendCalculator.reset();
				trendCalculator.pushValue(value);
			}
			nextState();
		}
	}
	
	private void inRising(float value) {
		float tpa = TopPeak_Altitude;
		if (value < tpa * TopPeakRising_Offset) {
			if (trendCalculator != null)  {
				trendCalculator.pushValue(value);
				if (trendCalculator.getTrend() < 0) {
					rollback();
					return;
				}
			}
		} else {
			topPeakRecord.startRecording();
			nextState();
		}
	}
	
	private void inTopPeak(float value) {
		float tpa = TopPeak_Altitude;
		if (value <= tpa * TopPeakRising_Offset) {
			topPeakRecord.endRecording();
			if (trendCalculator != null) {
				trendCalculator.reset();
				trendCalculator.pushValue(value);
			}
			nextState();
		}else {
			topPeakRecord.pushValue(value);
		}
	}
	
	private void inFalling(float value) {
		float bpa = BotPeak_Altitude;
		if (value > bpa * BotPeakFalling_Offset) {
			if (trendCalculator != null)  {
				trendCalculator.pushValue(value);
				if (trendCalculator.getTrend() > 0) {
					rollback();
					return;
				}
			}
		} else {
			botPeakRecord.startRecording();
			nextState();
		}
	}
	
	private void inBotPeak(float value) {
		float bpa = BotPeak_Altitude;
		if (value >= bpa * BotPeakFalling_Offset) {
			botPeakRecord.endRecording();
			if (trendCalculator != null) {
				trendCalculator.reset();
				trendCalculator.pushValue(value);
			}
			nextState();
		}else {
			botPeakRecord.pushValue(value);
		}
	}
	
	private void inBouncing(float value) {
		float bpa = BotPeak_Altitude;
		if (value < bpa * BotPeakResting_Offset) {
			if (trendCalculator != null)  {
				trendCalculator.pushValue(value);
				if (trendCalculator.getTrend() < 0) {
					rollback();
					return;
				}
			}
		} else {
			nextState();
		}
	}
	
	/* 
	 * Data Processing Support Classes
	 */
		
	private class TrendCalculator {
			
			private int capacity;
			
			private Date startingTime;
			private FiniteList xValues;
			private FiniteList yValues;
			private FiniteList xxValues;
			private FiniteList xyValues;
			
			
			public TrendCalculator(int capacity) {
				this.capacity = capacity;
				xValues = new FiniteList(capacity);
				yValues = new FiniteList(capacity);
				xxValues = new FiniteList(capacity);
				xyValues = new FiniteList(capacity);
			}
			
			public void reset() {
				startingTime = new Date();
				xValues = new FiniteList(capacity);
				yValues = new FiniteList(capacity);
				xxValues = new FiniteList(capacity);
				xyValues = new FiniteList(capacity);
			}
			
			public float getTrend() {
				if (xValues.getSize() < capacity) {
					return 0;
				}
				float xSum = xValues.getSum();
				float ySum = yValues.getSum();
				float xxSum = xxValues.getSum();
				float xySum = xyValues.getSum();
				float m = (capacity * xySum - xSum * ySum) / (capacity * xxSum - xSum * xSum);
				return m;
			}

			public void pushValue(float value) {
				long x = new Date().getTime() - startingTime.getTime();
				xValues.pushValue(x);
				yValues.pushValue(value);
				xxValues.pushValue(x * x);
				xyValues.pushValue(x * value);
			}
		}

	private class FiniteList {

		private int capacity;
		private List<Float> values;
		private float sum;
			
		public FiniteList(int capacity) {
			this.capacity = capacity;
			values = new ArrayList<>();
			sum = 0;
		}
			
		public void pushValue(float value) {
			if (values.size() >= capacity) {
				sum -= values.get(0);
				values.remove(0);
				sum += value;
				values.add(value);
			} else {
				sum += value;
				values.add(value);
			}
		}
			
		public float getSum() {
			return sum;
		}
			
		public float getAverage() {
			return sum / values.size();
		}
			
		public int getSize() {
			return values.size();
		}
			
		public float get(int index) {
			return values.get(index);
		}
	}

	private class ValueRecord {
			
		private int compareFactor;
			
		private float recordValue;
			
		public ValueRecord(int compareFactor) {
			this.compareFactor = compareFactor;
		}
			
		public void startRecording() {
			recordValue = 0;
		}
			
		public void endRecording() {
		}
			
		public void pushValue(float value) {
			switch (compareFactor) {
			case 1:
				recordValue = Math.max(recordValue, value);
				break;
			
			case -1:
				recordValue = Math.min(recordValue, value);
				break;
					
			default:
				recordValue = value;
				break;
			}
		}
			
		public float getRecordValue() {
			return recordValue;
		}
	}

}
