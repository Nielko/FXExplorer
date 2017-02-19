package Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Graph.Graph;
import Graph.Knot;

// Zentrale Klasse für die Serialisierung
// IDEE: Diese Klasse bleibt konstant, die anderen Klassen dürfen dadurch geändet werden
public class SerializableGraph implements Serializable{

	// Eigenschaften für den Graph
	public double r,g,b,a;
	public boolean thickPaths;
	public boolean blur;
	public Map<String, String> settingsMap; // unbenutzt
	
	// Knoten im Graph
	public List<SerializableKnot> freeKnots;
	public SerializableKnot mainKnot;
	
	public SerializableGraph(Graph graph)
	{
		r = graph.graphSettings.r;
		g = graph.graphSettings.g;
		b = graph.graphSettings.b;
		a = graph.graphSettings.a;
		
		thickPaths = graph.graphSettings.thickPaths;
		blur = graph.graphSettings.blur;
		
		freeKnots = new ArrayList<SerializableKnot>();
		for(Knot k: graph.freeKnots)
		{
			freeKnots.add(new SerializableKnot(k));
		}
		mainKnot = new SerializableKnot(graph.mainKnot); 
	}
}
