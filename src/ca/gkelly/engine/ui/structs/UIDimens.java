package ca.gkelly.engine.ui.structs;

public class UIDimens {
	
	public UISet padding;
	
	private int width =  0;
	private int height = 0;
	
	public boolean fixedWidth = false;
	public boolean fixedHeight = false;
	
	public UIDimens() {
		padding = new UISet(5,5,5,5);
	}
	
	public UIDimens(UISet padding) {
		this.padding = padding;
	}
	
	public UIDimens(UISet padding, int width, int height) {
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
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int h) {
		if(!fixedHeight)
			height = h;
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

