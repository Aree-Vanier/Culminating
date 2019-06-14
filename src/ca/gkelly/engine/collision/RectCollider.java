package ca.gkelly.engine.collision;

import ca.gkelly.engine.util.Vector;
import java.awt.Rectangle;

public class RectCollider extends Collider {

	public RectCollider(double x, double y, double width, double height) {
		setVertices(new double[] { x, x + width, x + width, x }, new double[] { y, y, y + height, y + height }, 4);
	}

	public RectCollider(Rectangle r) {
		this(r.x, r.y, r.width, r.height);
	}

	public Rectangle getRect() {
		return new Rectangle((int) vertices[0].x, (int) vertices[0].y, (int) (width),
				(int) (height));
	}

}
