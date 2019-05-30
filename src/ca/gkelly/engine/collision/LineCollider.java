package ca.gkelly.engine.collision;

import ca.gkelly.engine.util.Vector;

public class LineCollider extends Collider {

	public LineCollider(double x1, double y1, double x2, double y2, int width) {
		
	}

	/**Does not work with line colliders*/
	@Override
	public boolean cointains(Collider c) {
		return false;
	}

	/**Does not work with line colliders*/
	@Override
	public boolean contains(double x, double y) {
		return false;
	}

	@Override
	public boolean intersects(Collider c) {
		return false;
	}

	/**Does not work with line colliders*/
	@Override
	public Vector getPushback(Collider c) {
		return null;
	}

}
