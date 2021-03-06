package ca.gkelly.engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import ca.gkelly.engine.Mouse;
import ca.gkelly.engine.ui.structs.UIDimensions;
import ca.gkelly.engine.ui.structs.UIPosition;
import ca.gkelly.engine.util.Vertex;

/** Container to house other {@link UIElement}s */
public class UIContainer extends UIElement {

	/** List of children {@link UIElement}s */
	ArrayList<UIElement> children = new ArrayList<>();

	/**
	 * Create the container
	 * 
	 * @param p        The position data
	 * @param dimens   The dimensional data
	 * @param bgColour The background colour
	 */
	public UIContainer(UIPosition p, UIDimensions dimens, Color bgColour) {
		super(p, dimens, bgColour);
	}

	/**
	 * Add a child {@link UIElement}
	 * 
	 * @param e The element to add
	 */
	public void addChild(UIElement e) {
		children.add(e);
	}

	/**
	 * Remove a child {@link UIElement}
	 * 
	 * @param e The element to remove
	 */
	public void removeChild(UIElement e) {
		if(children.contains(e)) children.remove(e);
	}

	/** Get a list of children {@link UIElement}s */
	public ArrayList<UIElement> getChildren() {
		return children;
	}

	@Override
	public void update() {
		for(UIElement e : children) {
			e.update();
		}
	}
	
	@Override
	public void render(Graphics2D g, UIContainer c) {
		// Set width/height to fit parent, can be prevented by making them fixed
		dimens.setWidth(c.dimens.getWidth());
		dimens.setHeight(c.dimens.getHeight());
		// Render self
		super.render(g, c);
		// Render children
		for(UIElement e: children) {
			if(e.getVisible()) // Only render if visible
				e.render(g, this);
		}
	}

	/**
	 * Called upon mouse move, finds which element was clicked
	 * 
	 * @param v The {@link Vertex} mouse posititon
	 * @param top The current top-level hovered element
	 */
	public UIElement onMouseMove(Vertex v, UIElement top) {
		// Iterate backward to start at highest-rendered level
		for(int i = children.size() - 1;i >= 0;i--) {
			UIElement e = children.get(i);
			if(!e.getVisible())// Only check for visible elements
				continue;
			
			if(e instanceof UIContainer) {// Iterate through nested
				top = ((UIContainer) e).onMouseMove(v, top);
			}
			
			//If the top element has not yet been found
			if(top == null && e.isMouseOver(v)) {
				e.onHover();
				top = e;
			}else {
				e.onNotHover();
			}
		}
		if(isMouseOver(v) && top == null) {// If the mouse is over this, but not sub elements, prevent below items from being hovered
			onHover();
			top = this;
		} else {
			onNotHover();
		}
		return top;
	}

	/**
	 * Called upon mouse click, finds which element was clicked
	 * 
	 * @param m The {@link Mouse} object
	 * @return The element that was clicked
	 */
	public UIElement onMouseClick(Mouse m) {
		// Iterate backward to start at highest-rendered level
		for(int i = children.size() - 1;i >= 0;i--) {
			UIElement e = children.get(i);
			if(!e.getVisible())// Only check for visible elements
				continue;
			if(e instanceof UIContainer) {// Iterate through nested
				UIElement clicked = ((UIContainer) e).onMouseClick(m);
				if(clicked != null) return clicked;
			}
			if(e.isMouseOver(m.pos)) {
				return e;
			}
		}
		if(isMouseOver(m.pos)) // If the mouse is over this, but not sub elements, prevent below items from being clicked
			return this;
		return null;
	}

}
