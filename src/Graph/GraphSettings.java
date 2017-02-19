package Graph;

import java.io.Serializable;

import Database.SerializableGraph;
import javafx.scene.paint.Color;

public class GraphSettings implements Serializable{
	// Speichert r,g,b,a ab, Color ist nicht serialisierbar
	public double r,g,b,a;
	public boolean thickPaths;
	public boolean blur;
	
	public GraphSettings()
	{
		r = 0.1f;
		g = 1;
		b = 0.1f;
		a = 1;
		thickPaths = true;
		blur = false;
	}
	
	public GraphSettings(SerializableGraph graph)
	{
		r = graph.r;
		g = graph.g;
		b = graph.b;
		a = graph.a;
		thickPaths = graph.thickPaths;
		blur = graph.blur;
	}
	
	public Color getColor()
	{
		return new Color(r,g,b,a);
	}
	
	public Color getGrayColor()
	{
		return new Color(0.8,0.8,0.8,0.6f);
	}
	
	public void setColor(Color color)
	{
		r = color.getRed();
		g = color.getGreen();
		b = color.getBlue();
		a = color.getOpacity();
	}
}
