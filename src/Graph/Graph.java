package Graph;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Database.SerializableGraph;
import Database.SerializableKnot;
import Start.FXExplorer;
import javafx.scene.paint.Color;

public class Graph{
	public Knot mainKnot;
	public GraphSettings graphSettings;
	public List<Knot> freeKnots;
	private Color initColors[] = new Color[]{Color.ALICEBLUE, Color.BLANCHEDALMOND, Color.BURLYWOOD, Color.DARKCYAN, Color.DARKGREEN, Color.FUCHSIA, Color.FORESTGREEN, Color.LIMEGREEN, Color.WHEAT, Color.TOMATO,
				Color.ORANGE, Color.BEIGE, Color.DARKCYAN, Color.CRIMSON, Color.DARKSEAGREEN, Color.DODGERBLUE, Color.TURQUOISE, Color.LAVENDERBLUSH, Color.LIGHTCORAL};
	
	/**
	 * Hardgecodeder Test-Graph
	 */
	public Graph()
	{
		mainKnot = new Knot("Main", new Point(FXExplorer.SIZE_X/2, (int) (FXExplorer.SIZE_Y*0.3f)));
		graphSettings = new GraphSettings();
		graphSettings.setColor(initColors[(int) (Math.random()*initColors.length)]);
		freeKnots = new ArrayList<Knot>();
	}
	
	public Graph(SerializableGraph graph)
	{
		mainKnot = new Knot(graph.mainKnot);
		graphSettings = new GraphSettings(graph);
		freeKnots = new ArrayList<Knot>();
		for(SerializableKnot k: graph.freeKnots)
		{
			freeKnots.add(new Knot(k));
		}
	}
	
	public Knot getKnot(Point position, int radius)
	{
		ArrayList<Knot> tempKnots = getAllKnots();
		for(int i = 0; i < tempKnots.size(); i++)
		{
			if(tempKnots.get(i).position.distance(position) < radius)
				return tempKnots.get(i);
		}
		for(Knot knot: this.freeKnots )
		{
			if(knot.position.distance(position) < radius)
				return knot;
		}
		return null;
		
	}
	
	public Knot getParentKnot(Knot subKnot)
	{
		// Rekursive Suche aufrufen und beim Main-Knoten beginnen
		return extendKnotForParentSearch(subKnot, mainKnot);
	}
	
	public ArrayList<Knot> getAllKnots()
	{
		ArrayList<Knot> tempKnots = new ArrayList<Knot>();
		tempKnots.add(mainKnot);
		extendKnot(mainKnot, tempKnots);
		tempKnots.addAll(freeKnots);
		return tempKnots;
	}
	
	public void extendKnot(Knot knot, List<Knot> tempKnots)
	{
		tempKnots.addAll(knot.subKnots);
		for(int i = 0; i < knot.subKnots.size(); i++)
			extendKnot(knot.subKnots.get(i), tempKnots);
	}
	
	// Rekursive Funktion, die den Parent zurückgibt
	private Knot extendKnotForParentSearch(Knot searchedSubKnot, Knot currentKnot)
	{
		Knot result = null;
		// Überprüfen, ob der gesuchte SubKnot ist dabei
		for(Knot k: currentKnot.subKnots)
			if(k == searchedSubKnot)
				return currentKnot;
		// Sonst tiefer untersuchen
		for(Knot k: currentKnot.subKnots)
		{
			result = extendKnotForParentSearch(searchedSubKnot, k);
			if(result != null)
				return result;
		}
		return null;
	}
	
	// Löscht den gewünschten Knoten im Baum (mit allen Unterknoten)
	public void removeKnot(Knot knot)
	{
		// Falls freie Knoten den entfernen, sonst weiteren Knoten suchen
		for(int i = 0; i < freeKnots.size(); i++)
		{
			if(freeKnots.get(i) == knot)
			{
				freeKnots.remove(i);
				return;
			}
		}
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
