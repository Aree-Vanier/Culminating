package ca.gkelly.engine.ui.structs;

public class UIDimensions {
	
	public static final UIDimensions DEFAULT = new UIDimensions();
	
	public UISet padding;
	
	private int width =  0;
	private int height = 0;
	
	private boolean fixedWidth = false;
	private boolean fixedHeight = false;
	
	public UIDimensions() {
		padding = UISet.DEFAULT;
	}
	
	public UIDimensions(UISet padding) {
		this.padding = padding;
	}
	
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
	
	public void setWidth(int w) {
		if(!fixedWidth)
			width = w;
	}
	
	public void setFixedWidth(int w) {
		fixedWidth = true;
		width = w;
	}
	
	public void unfixWidth() {
		fixedWidth = false;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int h) {
		if(!fixedHeight)
			height = h;
	}
	
	public void setFixedHeight(int h) {
		fixedHeight = true;
		height = h;
	}
	
	public void unfixHeight() {
		fixedWidth = false;
	}
	
	public int getHeight() {
		return height;
	}
	
	
	public int getTotalWidth() {
		return width + padding.left + padding.right;
	}
	
	public int getTotalHeight() {
		return height + padding.top + padding.bottom;
		
	}
}

