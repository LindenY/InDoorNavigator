package ca.uwaterloo.navigationView;

import ca.uwaterloo.sensor.SoftSensor;
import ca.uwaterloo.sensor.SoftSensorListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CompassView extends View implements SoftSensorListener{
	
	private float width = 200;
	private float angle = 0;

	private Paint circlePaint;
	private Paint pointerPaint;
	private Paint textPaint;
	
	public CompassView(Context context, float width) {
		super(context);
		circlePaint = new Paint();
		circlePaint.setColor(0xffff0000);
		pointerPaint = new Paint();
		pointerPaint.setColor(0xff00ff00);
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
	}
	
	public void setAngle(float a) {
		this.angle = a;
		invalidate();
	}
	
	public float getAngle() {
		return angle;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension((int) width, (int) width + 60); 
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float radius = width / 2;
		canvas.drawCircle(radius, radius, radius, circlePaint);
		float endingX = radius + radius * (float) Math.sin(angle);
		float endingY = radius - radius * (float) Math.cos(angle);
		canvas.drawLine(radius, radius, endingX, endingY, pointerPaint);
		
		int angleDrg = (int) Math.round(angle / Math.PI * 180);
		canvas.drawText("Angle:" + angleDrg, radius, radius, pointerPaint);
		canvas.drawText("Compass", 0, width + 10, textPaint);
	}

	@Override
	public void onSensorChanged(SoftSensor sensor, float[] values) {
		if (sensor.getType() == SoftSensor.Type_GeoOrientation) {
			this.setAngle(-values[0]);
		}
	}

}
