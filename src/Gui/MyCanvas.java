package Gui;
import java.awt.Point;
import java.io.File;

import Graph.GraphSettings;
import Start.FXExplorer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MyCanvas {
	private GraphicsContext gc;
	private GraphSettings settings;
	public boolean ThickConnections;
	
	public MyCanvas(Canvas canvas)
	{
		 gc = canvas.getGraphicsContext2D();
		 gc.setFill(new Color(0.1,1,0.1, 1));
	}
	
	public void setGraphSettings(GraphSettings settings)
	{
		this.settings = settings;
	}
	
	public void drawCircle(int x, int y, int r)
	{
		gc.setFill(settings.getColor());
		gc.fillOval(x-r, y-r, r*2, r*2);
	}
	
	public void drawThinCircle(int x, int y, int r)
	{
		gc.setStroke(settings.getColor());
		gc.setLineWidth(2);
		gc.strokeOval(x-r, y-r, r*2, r*2);
	}
	
	/**
	 * Zeichnet eine Verbindung von x1,y1 nach x2,y2 mit dem Radius r1 und r2
	 */
	public void drawShapes(int x1, int y1, int x2, int y2, int r1, int r2) 
    {
		this.drawShapes(x1, y1, x2, y2, r1, r2, x1+(x2-x1)/2, y1+(y2-y1)/2);
    }
	
	/**
	 * Zeichnet eine Verbindung von x1,y1 nach x2,y2 mit dem Radius r1 und r2
	 */
	public void drawShapes(int x1, int y1, int x2, int y2, int r1, int r2, int x12, int y12) 
    {
		float sin,cos;
			sin = (float) ((y2-y1)/Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1)));
			cos = (float) ((x2-x1)/Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1)));
    	
		if(settings.thickPaths == false)
		{
			sin = sin*0.1f;
			cos = cos*0.1f;
		}
    	gc.setStroke(settings.getColor());
    	gc.setFill(settings.getColor());
		gc.setLineWidth(20);
    	r1 = (int) (r1*0.8);
    	r2 = (int) (r2*0.8);
    	
    	// ToDo: Breitere Verbindungen
    	gc.beginPath();	
    	gc.moveTo(x1-r1*sin, y1+r1*cos);
    	gc.bezierCurveTo(x1-r1*sin, y1+r1*cos, x12-10, y12-10, x2-r2*sin, y2+r2*cos); 
    	gc.lineTo(x2+r1*sin, y2-r2*cos);  
    	gc.bezierCurveTo(x2+r1*sin, y2-r2*cos, x12+10, y12+10, x1+r1*sin, y1-r1*cos);    	//gc.bezierCurveTo(x2-r2, y2-r2, x1+(x2-x1)/2, y1-10, x1+r1, y1-r1); 
    	gc.closePath();
    	gc.fill();

    }
	
	public void clear(int x, int y, int r)
	{
		gc.clearRect(x-r, y-r, 2*r, 2*r);
	}
	
	public void clearAll()
	{
		gc.clearRect(0,0,FXExplorer.SIZE_X, FXExplorer.SIZE_Y);
	}
	
	/**
	 * Zeichnet eine Verbindung von x1,y1 nach x2,y2 mit den Mittelpunkten x12,y12 mit dem Radius r1 und r2
	 */
	public void drawShapes( Point p1, Point p2, Point p12, int r1, int r2) 
    {
		System.out.println("MyCanvas.drawShapes: Nicht implementiert!");
    }
	
	public void drawCross(int x1, int y1, int r)
	{
		gc.setStroke(new Color(1,1,1, 0.7));
		gc.setLineWidth(2);
		
		gc.beginPath();
		gc.moveTo(x1-r, y1);
		gc.lineTo(x1+r, y1);
		gc.moveTo(x1, y1-r);
		gc.lineTo(x1, y1+r);
		//gc.closePath();
		gc.stroke();
	}
	
	public void drawImage(String file, int x, int y, int w)
	{
		try
		{
			if(file != null)
			{
				Image img;
				File myfile = new File(file);
				if(myfile.isAbsolute())
					img = new Image(myfile.toURI().toURL().toExternalForm());
				else
					img = new Image(file);
				gc.drawImage(img, x-w/2, y-w/2, w, w);
			}
		}
		catch(Exception e)
		{
			System.out.println("MyCanvas.drawImage: Bild konnte nicht geladen werden "+file);
		}
	}
	
	public void drawImage(Image img, int x, int y, int w)
	{
		try
		{
			if(img != null)
			{
				gc.drawImage(img, x-w/2, y-w/2, w, w);
			}
		}
		catch(Exception e)
		{
			System.out.println("MyCanvas.drawImage: Bild konnte nicht geladen werden "+img);
		}
	}
	
	public void addShadow()
	{
		gc.applyEffect(new DropShadow(15, 0, 0, settings.getColor()));
		gc.applyEffect(new DropShadow(10, 0, 0, settings.getColor()));
	}
}
