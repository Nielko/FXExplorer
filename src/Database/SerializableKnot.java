package Database;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Graph.Knot;

public class SerializableKnot implements Serializable{
	
	// Eigenschaften
	public String name;
	public List<SerializableKnot> subKnots;
	public Point position;	// Ist-Position
	public int circleRadius;
	public boolean VisibleSubKnots = true;
	public String ImgFile = "folder.png";
	public String WeblinkImg = "weblink.png";
	public String GraphImg = "icon.png";
	public String File;
	public Map<String, String> settingsMap; // unbenutzt
	
	public SerializableKnot(Knot knot)
	{
		name = knot.name;
		position = knot.position;
		circleRadius = knot.circleRadius;
		VisibleSubKnots = knot.VisibleSubKnots;
		ImgFile = knot.ImgFile;
		WeblinkImg = knot.WeblinkImg;
		GraphImg = knot.GraphImg;
		File = knot.File;
		
		subKnots = new ArrayList<SerializableKnot>();
		for(Knot k: knot.subKnots )
		{
			subKnots.add(new SerializableKnot(k));
		}
		
	}
}
