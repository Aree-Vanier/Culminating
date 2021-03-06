package ca.gkelly.engine.util;

/** An assortment of tools and utility functions */
public class Tools {
	/**
	 * Fit a value to min/max boundary
	 * 
	 * @param val The value to fit
	 * @param min The minimum value
	 * @param max The maximum value
	 * @return The fit value
	 */
	public static int minmax(int val, int min, int max) {
		return (Math.max(min, Math.min(val, max)));
	}

	/**
	 * Fit a value to min/max boundary
	 * 
	 * @param val The value to fit
	 * @param min The minimum value
	 * @param max The maximum value
	 * @return The fit value
	 */
	public static double minmax(double val, double min, double max) {
		return (Math.max(min, Math.min(val, max)));
	}
}
