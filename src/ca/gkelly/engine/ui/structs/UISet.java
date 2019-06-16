package ca.gkelly.engine.ui.structs;

/** A set of 4 values for use with UI */
public class UISet {
	/** A set with all 0s */
	public static final UISet NONE = new UISet(0);
	/** A set with all 5s */
	public static final UISet DEFAULT = new UISet(5, 5, 5, 5);

	/** The left component */
	public int left;
	/** The top component */
	public int top;
	/** The right component */
	public int right;
	/** The bottom component */
	public int bottom;

	/**
	 * Create the set with the specified values
	 * 
	 * @param l The left component
	 * @param t The top component
	 * @param r The right component
	 * @param b The bottom component
	 */
	public UISet(int l, int t, int r, int b) {
		left = l;
		top = t;
		right = r;
		bottom = b;
	}
	
	/**
	 * Create the set with the specified values
	 * 
	 * @param h The value for horizontal components
	 * @param v The value for vertical components
	 */
	public UISet(int h, int v) {
		this(h, v, h, v);
	}

	/**
	 * Create the set with the specified values
	 * 
	 * @param a The value for all components
	 */
	public UISet(int a) {
		this(a, a, a, a);
	}
}