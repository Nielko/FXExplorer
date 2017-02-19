package Graph;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Database.SerializableKnot;

public class Knot{
	public String name;
	public List<Knot> subKnots;
	public Point position;	// Ist-Position
	public Point positionGui; // Soll-Position
	public int circleRadius;
	public boolean VisibleSubKnots = true;
	public String ImgFile = "folder.png";
	public String WeblinkImg = "weblink.png";
	public String GraphImg = "icon.png";
	public String File;
	
	public Knot(SerializableKnot knot)
	{
		name = knot.name;
		subKnots = new ArrayList<Knot>();
		for(SerializableKnot k: knot.subKnots )
		{
			subKnots.add(new Knot(k));
		}
		position = knot.position;
		positionGui = position;
		circleRadius = knot.circleRadius;
		VisibleSubKnots = knot.VisibleSubKnots;
		ImgFile = knot.ImgFile;
		WeblinkImg = knot.WeblinkImg;
		GraphImg = knot.GraphImg;
		File = knot.File;
	}
	
	public Knot(String name)
	{
		subKnots = new ArrayList<Knot>();
		this.name = name;
		this.position = new Point((int) (50+Math.random()*200),(int) (50+Math.random()*200));
		circleRadius = 20;
		this.positionGui = (Point) position.clone();
	}
	
	public Knot(String name, Point positon)
	{
		this(name);
		this.positionGui = positon;
		this.position = positon;
	}
	
	public Knot(String name, String file)
	{
		this(name);
		this.File = file;
	}
	
	public boolean isWebLink()
	{
		if(File != null)
			return File.contains("http") || File.contains(".com") || File.contains(".de");
		return false;
	}
	
	public boolean isGraph()
	{
		if(File != null)
			return File.contains(".ser");
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
		// Konzept zum Anordnen der Subknots nach dem Bewegen
		
//		int smallRadius = 80;
//		//SubKnots ohne Subs zählen
//		int subs = 0;
//		for(int i = 0; i < this.subKnots.size(); i++)
//			if(this.subKnots.get(i).subKnots.size() == 0)
//				subs++;
//		for(int i = 0; i < subs; i++)
//		{
//			if(i < 4)
//				this.subKnots.get(i).position = new Point(this.position.x-smallRadius+i*80, this.position.y-100); //  Kreis: (x-xm)²+(y-ym)²=r²
//			else
//				System.out.println("Knot.posSubs: für i > 3 nicht definiert!! - "+subs);
//				
//		}
	}
}
