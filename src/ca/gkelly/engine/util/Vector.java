package ca.gkelly.engine.util;

public class Vector {
	double magnitude;
	double x;
	double y;

	public Vector(double x, double y) {
		magnitude = Math.sqrt(x * x + y * y);
		this.x = x / magnitude;
		this.y = y / magnitude;
	}

	public Vector(double x, double y, double magnitude) {
		this.x = x;
		this.y = y;
		this.magnitude = magnitude;
	}

	public Vector(double angle, double magnitude, boolean ccw) {
		this.magnitude = magnitude;
		if (ccw)
			angle = Math.PI * 2 - angle;
		if (angle < Math.PI * 0.5) {
			x = Math.cos(angle);
			y = Math.sin(angle);
		} else if (angle > Math.PI * 0.5 && angle < Math.PI) {
			x = Math.cos(angle);
			y = -Math.sin(angle);
		} else if (angle > Math.PI && angle < Math.PI * 0.75) {
			x = -Math.cos(angle);
			y = -Math.sin(angle);
		} else if (angle > Math.PI * 0.75 && angle < Math.PI * 2) {
			x = -Math.cos(angle);
			y = Math.sin(angle);
		}
	}

	public double getX() {
		return x * magnitude;
	}

	public double getY() {
		return y * magnitude;
	}

	public double getMag() {
		return magnitude;
	}

	public double getNormalX() {
		return x;
	}

	public double getNormalY() {
		return y;
	}

	public double getAngle() {
		return Math.atan2(y, x);
	}
}
