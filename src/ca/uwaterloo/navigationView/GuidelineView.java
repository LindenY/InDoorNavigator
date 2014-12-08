package ca.uwaterloo.navigationView;

import ca.uwaterloo.sensor.SoftSensor;
import ca.uwaterloo.sensor.SoftSensorListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class GuidelineView extends View implements SoftSensorListener{

	private float width = 200;
	private float northAngle = 0;
	private float pathAngle = 0;

	private Paint circlePaint;
	private Paint arcPaint;
	private Paint pointerPaint;
	private Paint textPaint;
	
	private RectF oval;
	
	public GuidelineView(Context context) {
		super(context);
		circlePaint = new Paint();
		circlePaint.setColor(0xffff0000);
		pointerPaint = new Paint();
		pointerPaint.setColor(0xff00ff00);
		pointerPaint.setStrokeWidth(5.0f);;
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		arcPaint = new Paint();
		arcPaint.setColor(Color.GRAY);
		
		oval = new RectF(0, 0, width, width);
	}
	
	public void setNorthAngle(float a) {
		this.northAngle = a;
		invalidate();
	}
	
	public float getNorthAngle() {
		return northAngle;
	}
	
	public void setPathAngle(float a) {
		this.pathAngle = a;
		invalidate();
	}
	
	public float getPathAngle() {
		return pathAngle;
	}
	
	public float getGuidelineAngle() {
		return pathAngle - northAngle;
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
		float angle = getGuidelineAngle();
		canvas.drawCircle(radius, radius, radius, circlePaint);
		canvas.drawArc(oval, -100, 20, true, arcPaint);
		float endingX = radius + radius * (float) Math.sin(angle);
		float endingY = radius - radius * (float) Math.cos(angle);
		canvas.drawLine(radius, radius, endingX, endingY, pointerPaint);
		int angleDrg = (int) Math.round(angle / Math.PI * 180);
		canvas.drawText("Angle:" + angleDrg, radius, radius, pointerPaint);
		String str = null;
		if (angle > 0) {
			str = "Turn right " + Math.round(angle/Math.PI*180) + " degree"; 
		}else {
			str = "Turn left " + Math.round(-angle/Math.PI*180) + " degree"; 
		}
		canvas.drawText("Walk this way", 0, width + 5, textPaint);
		canvas.drawText(str, 0, width + 20, textPaint);
		canvas.drawText("to place the pointer in the grey area and walk straight", 0, width + 35, textPaint);
	}

	@Override
	public void onSensorChanged(SoftSensor sensor, float[] values) {
		setNorthAngle(values[0]);
	}

}
