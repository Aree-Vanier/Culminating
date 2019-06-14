package ca.gkelly.engine.collision;

import java.util.ArrayList;

import ca.gkelly.engine.util.Vertex;

public class Hull {
	PolyCollider c;
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	Vertex extra = null;
	
	public Hull(ArrayList<Vertex> vertices, Vertex extra) {
		
	}
	
	public Hull(Collider c, Vertex extra) {
		
	}

}
