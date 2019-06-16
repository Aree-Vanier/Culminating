package ca.gkelly.engine.ui.structs;


public class UISet{
	
	public static final UISet NONE = new UISet(0,0,0,0);
	public static final UISet DEFAULT = new UISet(5,5,5,5);
	
	public int left;
	public int top;
	public int right;
	public int bottom;
	
	public UISet(int l, int t, int r, int b) {
		left = l;
		top = t;
		right = r;
		bottom = b;
	}
}