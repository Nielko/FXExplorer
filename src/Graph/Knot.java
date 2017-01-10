package Graph;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Knot implements Serializable{
	public String name;
	public List<Knot> subKnots;
	public Point position;	// Ist-Position
	public Point positionGui; // Soll-Position
	public int circleRadius;
	public boolean VisibleSubKnots = true;
	public String ImgFile = "folder.png";
	public String WeblinkImg = "weblink.png";
	public String File;
	
	public Knot(String name)
	{
		subKnots = new ArrayList<Knot>();
		this.name = name;
		this.position = new Point(100,100);
		circleRadius = 20;
		this.positionGui = new Point(100,100);
	}
	
	public Knot(String name, Point positon)
	{
		this(name);
		this.positionGui = positon;
		this.position = positon;
	}
	
	public boolean isWebLink()
	{
		if(File != null)
			return File.contains("http") || File.contains(".com") || File.contains(".de");
		return false;
	}
	
	public boolean isInside(Point p)
	{
		if(positionGui.distance(p) < circleRadius*1.5)
			return true;
		else
			return false;
	}
	
	/**
	 * SubKnots im Kreis anordnen -- Test!
	 */
	public void positionSubKnots()
	{
		int smallRadius = 80;
		//SubKnots ohne Subs zählen
		int subs = 0;
		for(int i = 0; i < this.subKnots.size(); i++)
			if(this.subKnots.get(i).subKnots.size() == 0)
				subs++;
		for(int i = 0; i < subs; i++)
		{
			if(i < 4)
				this.subKnots.get(i).position = new Point(this.position.x-smallRadius+i*80, this.position.y-100); //  Kreis: (x-xm)²+(y-ym)²=r²
			else
				System.out.println("Knot.posSubs: für i > 3 nicht definiert!! - "+subs);
				
		}
	}
}
