package Start;
import java.awt.Point;
import java.util.ArrayList;

import Database.GraphDatabase;
import Graph.Graph;
import Graph.Knot;
import Gui.Gui;
import Gui.GuiListener;
import Settings.KnotGui;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

public class Logic {
	public boolean FlowLayout = true;
	private Gui gui;
	private FileExplorer fileExplorer;
	public GraphDatabase database;
	
	private Graph graph;
	private Knot selectedKnot;
	private ArrayList<Knot> allKnots;
	private Point tempPositionMainKnot;
	
	private int oldX, oldY;
	
	/*	Erstellt die Logik:
	 * Die Gui mit einer Liste von Dateien initialisieren
	 */
	public Logic(Gui gui, String file)
	{
		System.out.println("Start");
		this.gui = gui;
		this.fileExplorer = new FileExplorer("/Users/nielskowala/Documents/Programmieren/workspace");
		//gui.initGraph(fileExplorer.listFiles(), fileExplorer.isFolders(),new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1});
		database = new GraphDatabase();
		graph = database.getStartGraph();//database.load(file);
		graph.mainKnot.VisibleSubKnots = true;
		gui.initGraph(graph);
		allKnots = graph.getAllKnots();
	}
	
	/**
	 * Erstellt den Listener für die Gui für die Eingaben durch die Buttons
	 */
	public GuiListener getGuiListener()
	{
		GuiListener listener = new GuiListener()
		{

			@Override
			public void mouseClicked(double x, double y, boolean rightClick) {
				// Linksklick: Selektierten Knoten speichern
				if(!rightClick)
				{
					selectedKnot = graph.getKnot(new Point((int) x,(int) y), 30);
					oldX = (int) x;
					oldY = (int) y;
				}
				else {
					// Bei Rechtsklick Knoten speichern, falls danach ein Unterknoten erzeugt werden soll
					selectedKnot = graph.getKnot(new Point((int) x,(int) y), 30);
					if(selectedKnot != null)
						gui.enableNewSubKnot(true);
					System.out.println("ToDo: Knoten speichern und den Unterknoten dazu im Contentmenu erzeugen");
				}
			}

			@Override
			public void mouseDragged(double x, double y, boolean rightClick, boolean click) {
				Point mousePoint = new Point((int) x,(int) y);
				boolean isKnot = false;
				if(click && selectedKnot != null && gui.isEditMode && !rightClick)
				{
					selectedKnot.positionGui = mousePoint;
					Knot parentKnot = graph.getParentKnot(selectedKnot);
					gui.drawTempKnot(selectedKnot, parentKnot);
					
					// Verbundung lösen
					if(parentKnot != null && parentKnot.positionGui.distance(selectedKnot.positionGui) > 440)
					{
						parentKnot.subKnots.remove(selectedKnot);
						graph.freeKnots.add(selectedKnot);
					}
					else if(parentKnot == null) // Verbindung hinzufügen
					{
						for(Knot k: allKnots)
						{
							if(k != selectedKnot && k.positionGui.distance(selectedKnot.positionGui) < 40)
							{
								k.subKnots.add(selectedKnot);
								graph.freeKnots.remove(selectedKnot);
								System.out.println("Logic.mouseDragged: Verbindung wurde geschaffen mit "+k.name);
							}
						}
					}
				}
				
				for(int i = 0; i < allKnots.size(); i++)
				{
					if(allKnots.get(i).isInside(mousePoint))
					{
						mousePoint.translate(10, 20);
						gui.setMouseOver(allKnots.get(i).name, mousePoint);
						isKnot = true;
					}
				}
				if(isKnot == false)
					gui.setMouseOver("", mousePoint);
			}

			@Override
			public boolean buttonExitClicked() {
				// Main-Knoten wieder zurücksetzen und anschließend speichern
				if(graph.mainKnot.VisibleSubKnots == false)
				{
					graph.mainKnot.positionGui = tempPositionMainKnot;
					graph.mainKnot.position = graph.mainKnot.positionGui;
				}
				if(database != null)
				{
					if(database.save(graph) == false)
						if(Gui.showMessagebox("Graph ist nicht gespeichert!", "Trotzdem fortfahren?") == false)
							return false;
					if(database.datName != null && !database.datName.equals(""))
						gui.saveSnapshot(database.datName+"x");
				}
				return true;
			}

			// Logik für die Buttons im Rechtklick-Menü
			@Override
			public void contextMenuClicked(RightClicked clicked) {
				switch(clicked)
				{
				case NEW:
					System.out.println("Logic.contextMenuClicked: New Knot");
					if(selectedKnot != null)
					{
						Point knotP = (Point) selectedKnot.positionGui.clone();
						knotP.translate(-80, -80);
						selectedKnot.subKnots.add(new Knot("Knoten", knotP));
					}
					else
						graph.freeKnots.add(new Knot("Knoten"));
					gui.removeAllButtons();
					gui.initGraph(graph);
					break;
				case DELETE:
					System.out.println("Logic.contextMenuClicked: Delete Knot");
					graph.removeKnot(selectedKnot);
					gui.removeAllButtons();
					gui.initGraph(graph);
					break;
				case CHANGE_IMG:
					new KnotGui(selectedKnot, gui);
					gui.removeAllButtons();
					gui.initGraph(graph);
					break;
				case CHANGING:
					gui.isEditMode = !gui.isEditMode;
					gui.removeAllButtons();
					gui.initGraph(graph);
					break;
				}
				allKnots = graph.getAllKnots();
			}

			@Override
			public void mouseReleased(double x, double y) {	// Alle Elemente erneut zeichnen am endgültigen Ort
				if(selectedKnot != null && oldX == x && oldY == y)
				{
					selectedKnot = graph.getKnot(new Point((int) x,(int) y), 20);
					// Sichtbarkeit des gewählten Knoten ändern und neu zeichnen
					if(selectedKnot != null && selectedKnot.subKnots.size() > 0)
						selectedKnot.VisibleSubKnots = !selectedKnot.VisibleSubKnots;
					if(selectedKnot == graph.mainKnot)
					{
						if(graph.mainKnot.VisibleSubKnots == false)
						{
							gui.miniWindow();
							tempPositionMainKnot = selectedKnot.positionGui;
							selectedKnot.positionGui = new Point(70,70);
							selectedKnot.position = selectedKnot.positionGui;
						}
						else
						{
							gui.maxWindow();
							selectedKnot.positionGui = tempPositionMainKnot;
							selectedKnot.position = selectedKnot.positionGui;
						}
					}
					gui.removeAllButtons();
					gui.initGraph(graph);
					
				}
				else if(selectedKnot != null)
				{
					// Knoten wurde verschoben -> alles neu zeichnen
					
					makeLayout(selectedKnot);
					gui.removeAllButtons();
					gui.initGraph(graph);
				}
			}

			@Override
			public void loadFile(String file) // Speichert unter der gespeicherten Adresse und öffnet die neue Datei
			{
				if(database.save(graph) == false)
					if(Gui.showMessagebox("Graph ist nicht gespeichert!", "Trotzdem fortfahren?") == false)
						return;
				if(database.datName != null && !database.datName.equals(""))
					gui.saveSnapshot(database.datName.substring(0, database.datName.length()-3)+"png");
				
				graph = database.load(file);
				gui.removeAllButtons();
				gui.initGraph(graph);
				allKnots = graph.getAllKnots();
			}

			@Override
			public void saveFile(String file) {
				database.saveAs(graph, file);
				gui.saveSnapshot(file.substring(0, file.length()-3)+"png");
				
			}
			
			@Override
			public void newGraph() {
				if(database.save(graph) == false)
					if(Gui.showMessagebox("Graph ist nicht gespeichert!", "Trotzdem fortfahren?") == false)
						return;
				if(database.datName != null && !database.datName.equals(""))
					gui.saveSnapshot(database.datName.substring(0, database.datName.length()-3)+"png");
				
				graph = new Graph();
				gui.removeAllButtons();
				gui.initGraph(graph);
				allKnots = graph.getAllKnots();
				database.datName = null;
			}

			@Override
			public void mouseDoubleClicked(double x, double y) {
				if(selectedKnot != null)
					gui.startFile(selectedKnot.File);
				
			}
	
		};
		return listener;
		
	}
	
	// Bei FlowLayout alle neu anordnen
	private void makeLayout(Knot knot)
	{
		if(this.FlowLayout)
			knot.positionSubKnots();
			
	}
}
