package ca.gkelly.engine.util;

/**
 * Class used to do vector math<br/>
 * Supports rectangular, normalized and polar vectors
 */
public class Vector {
	/** The magnitude of the vector */
	double magnitude;
	/** The normalized x component */
	double x;
	/** The normalized y component */
	double y;

	/**
	 * Create the vector, using the x and y components
	 * 
	 * @param x The x component
	 * @param y The y component
	 */
	public Vector(double x, double y) {
		magnitude = Math.sqrt(x * x + y * y);
		this.x = x / magnitude;
		this.y = y / magnitude;
	}

	/**
	 * Create the vector, using the normalized components and magnitude
	 * 
	 * @param x         The normalized x component
	 * @param y         The normalized y component
	 * @param magnitude The magnitude of the vector
	 */
	public Vector(double x, double y, double magnitude) {
		this.x = x;
		this.y = y;
		this.magnitude = magnitude;
	}

	/**
	 * Create the vector using a length and direction
	 * 
	 * @param angle     The angle away from up, in radians
	 * @param magnitude The length of the vector
	 * @param ccw       Flag to indecate couter-clockwise angle
	 */
	public Vector(double angle, double magnitude, boolean ccw) {
		this.magnitude = magnitude;
		if(ccw)
			angle = Math.PI * 2 - angle;
		if(angle < Math.PI * 0.5) { // All quadrant
			x = Math.cos(angle);
			y = Math.sin(angle);
		} else if(angle > Math.PI * 0.5 && angle < Math.PI) { // Cosine quadrant
			x = Math.cos(angle);
			y = -Math.sin(angle);
		} else if(angle > Math.PI && angle < Math.PI * 0.75) { // Tan quadrant
			x = -Math.cos(angle);
			y = -Math.sin(angle);
		} else if(angle > Math.PI * 0.75 && angle < Math.PI * 2) { // Sine quadrant
			x = -Math.cos(angle);
			y = Math.sin(angle);
		}
	}

	/** Get the full x component */
	public double getX() {
		return x * magnitude;
	}

	/** Get the full y component */
	public double getY() {
		return y * magnitude;
	}

	/** Get the magnitude of the vector */
	public double getMag() {
		return magnitude;
	}

	/**
	 * Set the magnitude of the vector
	 * 
	 * @param magnitude The new magnitude
	 */
	public void setMag(double magnitude) {
		this.magnitude = magnitude;
	}

	/** Get the normalized x component */
	public double getNormalX() {
		return x;
	}

	/** Get the normalized y component */
	public double getNormalY() {
		return y;
	}

	/**
	 * Get the angle from up
	 * 
	 * @param ccw Flag to indicate whether result is counter-clockwise
	 * @return The angle, in radians
	 */
	public double getAngle(boolean ccw) {
		if(ccw)
			return Math.PI * 2 - Math.atan2(y, x);
		else
			return Math.atan2(y, x);
	}

	/**
	 * Get a normalized version of passed vector
	 * 
	 * @param v The vector to normalize
	 * @return The normalized vector
	 */
	public static Vector getNormal(Vector v) {
		return (new Vector(v.getNormalX(), v.getNormalY()));
	}

	/**
	 * Get the sum of the passed vectors
	 * 
	 * @param v1 The first vector to add
	 * @param v2 The second vector to add
	 * @return The sum vector
	 */
	public static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
	}

	/**
	 * Get the sum of the passed vectors
	 * 
	 * @param v1 The vector to subtract from
	 * @param v2 The vector to be subtracted
	 * @return The difference vector
	 */
	public static Vector subtract(Vector v1, Vector v2) {
		return new Vector(v1.getX() - v2.getX(), v1.getY() - v2.getY());
	}

	/**
	 * Multiply a vector by a scalar
	 * 
	 * @param v1 The vector to multiply
	 * @param s  The scalar to multiply
	 * @return The product vector
	 */
	public static Vector multiply(Vector v, int s) {
		return (new Vector(v.getX() * s, v.getY() * s));
	}

	/**
	 * Divide a vector by a scalar
	 * 
	 * @param v1 The vector divisor
	 * @param s  The scalar dividend
	 * @return The quotient vector
	 */
	public static Vector divide(Vector v, int s) {
		return (new Vector(v.getX() / s, v.getY() / s));
	}
}
