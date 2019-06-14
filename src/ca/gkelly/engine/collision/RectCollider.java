package ca.gkelly.engine.collision;

import java.awt.Rectangle;

/**{@link Collider} override used for rectangles*/
public class RectCollider extends Collider {

	/**Create the collider using the passed corners*/
	public RectCollider(double x, double y, double width, double height) {
		super(new double[] { x, x + width, x + width, x }, new double[] { y, y, y + height, y + height }, 4);
	}

	/**Create the collider from the passed rectangle*/
	public RectCollider(Rectangle r) {
		this(r.x, r.y, r.width, r.height);
	}

	/**Get the {@link Rectangle} with the same points*/
	public Rectangle getRect() {
		return new Rectangle((int) vertices[0].x, (int) vertices[0].y, (int) (width),
				(int) (height));
	}

}