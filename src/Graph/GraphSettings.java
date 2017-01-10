package Graph;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class GraphSettings implements Serializable{
	// Speichert r,g,b,a ab, Color ist nicht serialisierbar
	private double r,g,b,a;
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
	
	public Color getColor()
	{
		return new Color(r,g,b,a);
	}
	
	public void setColor(Color color)
	{
		r = color.getRed();
		g = color.getGreen();
		b = color.getBlue();
		a = color.getOpacity();
	}
}
