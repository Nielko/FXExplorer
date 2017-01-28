package GuiElements;

import Graph.GraphSettings;
import Gui.MyCanvas;
import javafx.scene.layout.Pane;

public class GraphGui {
	private MyCanvas myCanvasConnections;
	
	public GraphGui(Pane pane, int sizeX, int SizeY)
	{
		myCanvasConnections = new MyCanvas(sizeX, SizeY);
		pane.getChildren().add(1, myCanvasConnections);
	}
	
	// Pflicht! - Stellt die Eigenschaften bereit
	public void setGraphSettings(GraphSettings settings)
	{
		myCanvasConnections.setGraphSettings(settings);
	}
	
	public void clearAll()
	{
		myCanvasConnections.clearAll();
	}
	
	public void drawConnection(int x1, int y1, int x2, int y2, int r1, int r2)
	{
		// Notwendigkeit??
		myCanvasConnections.drawShapes(x1, y1, x2, y2, r1, r2);
	}
}
