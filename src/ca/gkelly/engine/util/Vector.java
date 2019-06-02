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
	 * To be passed to {@link #getString(int) getString()}, indicates that the
	 * output will be normalized components
	 */
	public static final int STRING_NORMALIZED = 0;
	/**
	 * To be passed to {@link #getString(int) getString()}, indicates that the
	 * output will be full components
	 */
	public static final int STRING_RECTANGULAR = 1;
	/**
	 * To be passed to {@link #getString(int) getString()}, indicates that the
	 * output will be polar coordinates
	 */
	public static final int STRING_POLAR = 2;

	public static final Vector UP = new Vector(0, 1);
	public static final Vector RIGHT = new Vector(1, 0);
	public static final Vector DOWN = new Vector(0, -1);
	public static final Vector LEFT = new Vector(-1, 0);

	/**
	 * Create the vector, using the x and y components
	 * 
	 * @param x The x component
	 * @param y The y component
	 */
	public Vector(double x, double y) {
		magnitude = Math.sqrt(x * x + y * y);
		if (magnitude == 0) {
			this.x = 0;
			this.y = 0;
		} else {
			this.x = x / magnitude;
			this.y = y / magnitude;
		}
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
		if (ccw)
			angle = Math.PI * 2 - angle;
		if (angle < Math.PI * 0.5) { // All quadrant
			x = Math.cos(angle);
			y = Math.sin(angle);
		} else if (angle > Math.PI * 0.5 && angle < Math.PI) { // Cosine quadrant
			x = Math.cos(angle);
			y = -Math.sin(angle);
		} else if (angle > Math.PI && angle < Math.PI * 0.75) { // Tan quadrant
			x = -Math.cos(angle);
			y = -Math.sin(angle);
		} else if (angle > Math.PI * 0.75 && angle < Math.PI * 2) { // Sine quadrant
			x = -Math.cos(angle);
			y = Math.sin(angle);
		}
	}

	/** Get the full x component */
	public double getX() {
		return (x * magnitude);
	}

	/** Get the full y component */
	public double getY() {
		return (y * magnitude);
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
		if (ccw)
			return Math.PI * 2 - Math.atan2(y, x);
		else
			return Math.atan2(y, x);
	}

	/**
	 * Get a normalized version of the vector
	 * 
	 * @return The normalized vector
	 */
	public Vector normalized() {
		return (new Vector(x, y));
	}

	/** Get a vector with just the horizontal component */
	public Vector getHorizontal() {
		return new Vector(getX(), 0);
	}

	/** Get a vector with just the horizontal component */
	public Vector getVertical() {
		return new Vector(0, getY());
	}

	public Vector getAtAngle(double angle) {
		double offset = getAngle(false) - angle;
		double newMag = magnitude * Math.cos(offset);
		return new Vector(newMag * Math.cos(angle), newMag * Math.sin(angle));
	}

	/**
	 * Get a string representation of vector
	 * 
	 * @param mode The info to output<br/>
	 *             - {@link #STRING_NORMALIZED} Contains normalized X and Y<br/>
	 *             - {@link #STRING_RECTANGULAR} Contains full X and Y<br/>
	 *             - {@link #STRING_POLAR} Contains angle from up and length
	 */
	public String getString(int mode) {
		switch (mode) {
		case STRING_NORMALIZED:
			return "(" + x + "," + y + ")";
		case STRING_RECTANGULAR:
			return "(" + getX() + "," + getY() + ")";
		case STRING_POLAR:
			return "(" + getAngle(false) + "," + magnitude + ")";
		}

		return "Invalid mode";
	}

	/**
	 * Get the angle to another vector, using dot and cross
	 * 
	 * @param v   The other vector
	 * @param ccw Flag to indicate counter-clockwise
	 * @return Radian angle from this vector to v
	 */
	public double getAngle(Vector v, boolean ccw) {
		double dot = -dot(v, this);
		double cross = cross(v, this);
		if (ccw)
			return 2 * Math.PI - (Math.atan2(cross, dot));
		return (Math.atan2(cross, dot));
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

	/**
	 * Perform dot multiplication on two vectors
	 * 
	 * @param v1 The first vector
	 * @param v2 The second vector
	 * @return The resultant scalar
	 */
	public static double dot(Vector v1, Vector v2) {
		return v1.getX() * v2.getX() + v1.getY() * v2.getY();
	}

	/**
	 * Perform cross multiplication on two vectors
	 * 
	 * @param v1 The first vector
	 * @param v2 The second vector
	 * @return The magnitude resultant vector
	 */
	public static double cross(Vector v1, Vector v2) {
		return v1.getX() * v2.getY() - v1.getY() * v2.getX();
	}

}
