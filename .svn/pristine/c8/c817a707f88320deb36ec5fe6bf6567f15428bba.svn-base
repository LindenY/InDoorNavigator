package ca.uwaterloo.navigationView;

import ca.uwaterloo.sensor.SoftSensor;
import ca.uwaterloo.sensor.SoftSensorManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationalView extends LinearLayout{
	
	private CompassView compassView;
	private GuidelineView guidelineView;
	private TextView instructionTextView;

	public NavigationalView(Context context) {
		super(context);
		
		compassView = new CompassView(context, 200);
		guidelineView = new GuidelineView(context);
		instructionTextView = new TextView(context);
		
		try {
			SoftSensor oriSensor = SoftSensorManager.getSoftSensor(SoftSensor.Type_GeoOrientation);
			oriSensor.addListener(compassView);
			oriSensor.addListener(guidelineView);
		} catch (Exception e) {
			Log.e("NavigationalView", "Fail to get geoOrientation sensor");
		}
		
		this.addView(compassView);
		this.addView(guidelineView);
		this.addView(instructionTextView);
	}
	
	public void setPathAngle(float a) {
		guidelineView.setPathAngle(a);
	}
	
	public void setInstructionString(String str) {
		instructionTextView.setText(str);
	}
	
}
