package ca.gkelly.engine.ui.structs;

import ca.gkelly.engine.ui.UIElement;

/** Used to manage dimensions for {@link UIElement}s */
public class UIDimensions {

	/** The padding around the element */
	public UISet padding;

	/** The width of the element */
	private int width = 0;
	/** The height of the element */
	private int height = 0;
	/**
	 * Value to pass to {@link #UIDimensions(UISet, int, int)} to prevent fixing
	 * certain dimension
	 */
	public static final int UNFIXED = -1;

	/** If true, the width cannot be changed by {@link #setWidth(int)} */
	private boolean fixedWidth = false;
	/** If true, the width cannot be changed by {@link #setHeight(int)} */
	private boolean fixedHeight = false;

	/**
	 * Create the dimension, with specified width and height
	 * 
	 * @param padding The padding to use
	 * @param width   The fixed width to use, pass {@link #UNFIXED} to leave unfixed
	 * @param height  The fixed height to use, pass {@link #UNFIXED} to leave
	 *                unfixed
	 */
	public UIDimensions(UISet padding, int width, int height) {
		this.padding = padding;
		if(width != -1) {
			this.width = width;
			fixedWidth = true;
		}
		if(height != -1) {
			this.height = height;
			fixedHeight = true;
		}
	}

	/**
	 * Create the dimension with specified padding
	 * 
	 * @param padding The padding to use
	 */
	public UIDimensions(UISet padding) {
		this.padding = padding;
	}

	/**
	 * Create the dimension with {@link UISet#UISet(int) UISet.UISet(5)} padding
	 */
	public UIDimensions() {
		this(new UISet(5));
	}

	/**
	 * Set the width of the dimension, will not work if {@link #fixedWidth} is true
	 * 
	 * @param w The width to use
	 * @return false if the width could not be set
	 */
	public boolean setWidth(int w) {
		if(fixedWidth) return false;
		width = w;
		return true;
	}

	/**
	 * Set the fixed width<br/>
	 * This will set {@link #fixedWidth} to be true
	 * 
	 * @param w The width to use
	 */
	public void setFixedWidth(int w) {
		fixedWidth = true;
		width = w;
	}

	/** Free the width, this will set {@link #fixedWidth} to false */
	public void unfixWidth() {
		fixedWidth = false;
	}

	/** Get the width of the dimension */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the height of the dimension, will not work if {@link #fixedHeight} is
	 * true
	 * 
	 * @param h The height to use
	 * @return false if the height could not be set
	 */
	public boolean setHeight(int h) {
		if(fixedHeight) return false;
		height = h;
		return true;
	}

	/**
	 * Set the fixed height<br/>
	 * This will set {@link #fixedHeight} to be true
	 * 
	 * @param h The height to use
	 */
	public void setFixedHeight(int h) {
		fixedHeight = true;
		height = h;
	}

	/** Free the height, this will set {@link #fixedHeight} to false */
	public void unfixHeight() {
		fixedWidth = false;
	}

	/** Get the height of the dimension */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the total width of the dimension
	 * 
	 * @return The left-padding + the width + right-padding
	 */
	public int getTotalWidth() {
		return getWidth() + padding.left + padding.right;
	}

	/**
	 * Get the total height of the dimension
	 * 
	 * @return The top-padding + the height + bottom-padding
	 */
	public int getTotalHeight() {
		return getHeight() + padding.top + padding.bottom;

	}
}
