package ca.gkelly.engine.collision;

import java.util.ArrayList;

import ca.gkelly.engine.util.Vertex;

public class Hull {
	PolyCollider poly;
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	Vertex extra = null;
	
	public Hull(ArrayList<Vertex> vertices, Vertex extra) {
		this.vertices = vertices;
		this.extra = extra;
		poly = new PolyCollider(vertices);
	}

}
