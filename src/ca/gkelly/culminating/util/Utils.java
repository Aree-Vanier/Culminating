package ca.gkelly.culminating.util;

public class Utils {
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
	};
}
