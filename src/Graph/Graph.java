package Graph;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Start.FXExplorer;

public class Graph implements Serializable{
	public Knot mainKnot;
	public GraphSettings graphSettings;
	
	/**
	 * Hardgecodeder Test-Graph
	 */
	public Graph()
	{
		mainKnot = new Knot("Main", new Point(FXExplorer.SIZE_X/2, (int) (FXExplorer.SIZE_Y*0.3f)));
		mainKnot.subKnots.add(new Knot("Test1", new Point(50,50)));
		mainKnot.subKnots.add(new Knot("Test2", new Point(100,150)));
		mainKnot.subKnots.add(new Knot("Test3", new Point(50,260)));
		graphSettings = new GraphSettings();
	}
	
	public Knot getKnot(Point position, int radius)
	{
		ArrayList<Knot> tempKnots = getAllKnots();
		for(int i = 0; i < tempKnots.size(); i++)
		{
			if(tempKnots.get(i).position.distance(position) < radius)
				return tempKnots.get(i);
		}
		return null;
		
	}
	
	public ArrayList<Knot> getAllKnots()
	{
		ArrayList<Knot> tempKnots = new ArrayList<Knot>();
		tempKnots.add(mainKnot);
		extendKnot(mainKnot, tempKnots);
		return tempKnots;
	}
	
	public void extendKnot(Knot knot, List<Knot> tempKnots)
	{
		tempKnots.addAll(knot.subKnots);
		for(int i = 0; i < knot.subKnots.size(); i++)
			extendKnot(knot.subKnots.get(i), tempKnots);
	}
	
	// Löscht den gewünschten Knoten im Baum (mit allen Unterknoten)
	public void removeKnot(Knot knot)
	{
		extendRemoveKnot(mainKnot,knot);
	}
	
	private void extendRemoveKnot(Knot tempKnot, Knot goalKnot)
	{
		for(int i = 0; i < tempKnot.subKnots.size(); i++)
			if(tempKnot.subKnots.get(i) == goalKnot)
			{
				tempKnot.subKnots.remove(i);
				return;
			}
		// Falls der Knoten noch nicht gefunden und gelöscht wurde, tiefer suchen
		for(int i = 0; i < tempKnot.subKnots.size(); i++)
			extendRemoveKnot(tempKnot.subKnots.get(i), goalKnot);
	}
}
