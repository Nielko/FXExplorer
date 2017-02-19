package Gui;
import java.awt.Desktop;
import java.awt.Point;
import java.io.File;
import java.net.URI;
import java.util.Optional;

import Graph.Graph;
import Graph.Knot;
import GuiElements.GraphGui;
import GuiElements.SearchOverlay;
import GuiElements.WindowBar;
import Settings.SettingsGui;
import Start.FXExplorer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//import com.apple.eawt.Application; - Icons beim Mac?

public class Gui {
	public Stage primaryStage;
	private GraphGui graphGui;
	private MyCanvas myCanvasStatic;
	private MyCanvas myCanvasCircles;
	private Text mouseOverText;
	
	public static final int MAX_BUTTONS = 50;

	private long clickTimeDifference;
	public GuiListener listener;
	public Pane pane;
	public boolean isEditMode = true; // Zeichnet kleine Kreuze in den Mittelpunkten
	private MenuItem newSubKnot;
	private MenuItem deleteSubKnot;
	private MenuItem changeKnotPaths;
	private Button btnLoad;
	private Graph graph;
	private Scene scene;
	private SystemFileIcon sysIcon;
	private SearchOverlay searchOverlay;
	private FrostyBackground frostyBack;

	public Gui(Stage primaryStage)
	{
		primaryStage.getIcons().clear();
		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon.png")));
		primaryStage.initStyle(StageStyle.UNDECORATED);
    	primaryStage.initStyle(StageStyle.TRANSPARENT);
    	
    	
    	
		this.primaryStage = primaryStage;
		init();
	}
	
	public void setListener(GuiListener listener)
	{
		this.listener = listener;
	}
	
	public void removeAllButtons()
	{
		this.graphGui.clearAll();
		myCanvasStatic.clearAll();
		myCanvasCircles.clearAll();
	}
	
	public void init()
	{
		pane = new Pane();
		scene = new Scene(pane, 600,700);
		primaryStage.setScene(scene);
        primaryStage.show();
		sysIcon = new  SystemFileIcon();
		
		// Frosty-Background-Effekt
		frostyBack = new FrostyBackground(pane, primaryStage, this);
    	
		// Canvas für statische und flexible Elemente hinzufügen
		graphGui = new GraphGui(pane, FXExplorer.SIZE_X, FXExplorer.SIZE_Y);
        myCanvasStatic = new MyCanvas(FXExplorer.SIZE_X, FXExplorer.SIZE_Y);
        myCanvasCircles = new MyCanvas(FXExplorer.SIZE_X, FXExplorer.SIZE_Y);
        pane.getChildren().add(2, myCanvasStatic);
        pane.getChildren().add(3, myCanvasCircles);
        
       
        
        btnLoad = new Button(">");
        btnLoad.setTooltip(new Tooltip(Database.TextGui.ButtonLoadTooltip));
        pane.getChildren().add(btnLoad);
        btnLoad.relocate(10,50);
        btnLoad.getStyleClass().add("buttonO");
        
        Button btnSettings = new Button("...");
        btnSettings.setTooltip(new Tooltip(Database.TextGui.ButtonSettingsTooltip));
        pane.getChildren().add(btnSettings);
        btnSettings.relocate(10,100);
        btnSettings.getStyleClass().add("buttonO");

        setActionListener(btnLoad, btnSettings);
        
        mouseOverText = new Text();
        mouseOverText.relocate(-100, -100);
        pane.getChildren().add(mouseOverText);
        
        
        scene.getStylesheets().add(FXExplorer.class.getResource("/Login.css").toExternalForm());
        scene.setFill(null);

        // Je ein EventHandler für Mausklick, -drag und -release setzen und an GuiListener weitergeben
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            	if(listener != null)
                {
            		// Einzelklick mit Zeitmessung und Doppelklick (nur wenn Zeitdifferenz klein genug)
            		// Das Event zum Einzelklick wird auch vor dem Doppelklick ausgelöst!
            		if(mouseEvent.getClickCount() < 2)
            		{
            			listener.mouseClicked(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getButton() == MouseButton.SECONDARY);
            			clickTimeDifference = System.currentTimeMillis();
            		}
            		else
            		{
            			if(System.currentTimeMillis() - clickTimeDifference < 400)
            				listener.mouseDoubleClicked(mouseEvent.getX(), mouseEvent.getY());
            		}
                }
            }
        });
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            	 if(listener != null)
                 {
                 	listener.mouseDragged(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getButton() == MouseButton.SECONDARY, true);
                 }
            }
        });
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            	 if(listener != null)
                 {
                 	listener.mouseDragged(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getButton() == MouseButton.SECONDARY, false);
                 }
            }
        });
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            	 if(listener != null)
                 {
                 	listener.mouseReleased(mouseEvent.getX(), mouseEvent.getY());
                 }
            	 
            }
        });
        scene.addEventFilter(DragEvent.DRAG_DONE, new EventHandler<DragEvent>() {
            @Override
			public void handle(DragEvent event) { // Geht nicht!!
				System.out.println(event.getDragboard().getString());
			}
        });
        
        // Rechtsklickmenü, Fensterleiste, Suche, DragAndDrop initialisieren
        setContentMenu();
        
        new WindowBar(this, this.frostyBack);
        this.searchOverlay = new SearchOverlay(this);
        this.searchOverlay.changeOpacity();
	}
	
	public void setMouseOver(String text, Point p)
	{
		mouseOverText.setText(text);
		if(!text.equals(""))
			mouseOverText.relocate(p.getX(), p.getY());
	}
	
	public void enableNewSubKnot(boolean enable)
	{
		newSubKnot.setDisable(!enable);
		deleteSubKnot.setDisable(!enable);
		changeKnotPaths.setDisable(!enable);
	}
	
	private void setContentMenu()
	{
		final ContextMenu contextMenu = new ContextMenu();
		newSubKnot = new MenuItem("Neuer Unterpunkt");
		//newSubKnot.setDisable(true);
		deleteSubKnot = new MenuItem("Unterpunkt löschen");
		deleteSubKnot.setDisable(true);
		changeKnotPaths = new MenuItem("Bild-/Pfad ändern");
		changeKnotPaths.setDisable(true);
		MenuItem changeModus = new MenuItem("Bearbeitungsmodus ändern");
		MenuItem paste = new MenuItem("...");
		contextMenu.getItems().addAll(newSubKnot, deleteSubKnot, changeKnotPaths, changeModus, paste);
		
		// Event-Handler für Buttons im Rechts-Klick Menü setzen 
		//		(Button wird als Enum-Wert dem Listener übergeben)
		newSubKnot.setOnAction((ActionEvent event)->{ if(listener != null) listener.contextMenuClicked(GuiListener.RightClicked.NEW);});
		deleteSubKnot.setOnAction((ActionEvent event)->{ if(listener != null) listener.contextMenuClicked(GuiListener.RightClicked.DELETE);});
		changeModus.setOnAction((ActionEvent event)->{ if(listener != null) listener.contextMenuClicked(GuiListener.RightClicked.CHANGING);});
		changeKnotPaths.setOnAction((ActionEvent event)->{ if(listener != null) listener.contextMenuClicked(GuiListener.RightClicked.CHANGE_IMG);});
		pane.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
	        contextMenu.show(pane, event.getScreenX(), event.getScreenY());
	        event.consume();
	    });
	    pane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
	        contextMenu.hide();
	    });
	}
	
	private void setActionListener(Button btnLoad, Button btnSettings)
	{
        
        // FileChooser aufrufen, nur wenn eine Datei gewählt wurde den Listener aufrufen
        btnLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(listener != null)
                {
                	searchOverlay.changeOpacity();
                	if(btnLoad.getText().equals("<"))
                		btnLoad.setText(">");
                	else
                		btnLoad.setText("<");
                }
            }
        });
        
        btnSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                new SettingsGui(Gui.this, graph.graphSettings);
            }
        });
	}
	
	public void closeSearchOverlay()
	{
		btnLoad.setText("<");
		searchOverlay.hide();
	}
	
	public void redraw()
	{
		this.removeAllButtons();
		this.initGraph(this.graph);
		//this.frostyBack.updateBackground();
	}
	
	/**
	 * Graphen initialiseren mit String-Array
	 */
	public void initGraph(Graph graph)
	{
		this.graph = graph;
		this.myCanvasCircles.setGraphSettings(graph.graphSettings);
		this.myCanvasStatic.setGraphSettings(graph.graphSettings);
		this.graphGui.setGraphSettings(graph.graphSettings);
		try
		{	
			// Alle abhängigen Knoten zeichnen
			expandDrawKnot(null, graph.mainKnot);
			// Alle freien Knoten zeichnen
			for( Knot knot: graph.freeKnots )
				this.drawKnot(knot, 30);
			
			if(graph.graphSettings.blur)
        	{
				myCanvasStatic.addShadow();
				myCanvasCircles.addShadow();
        	}
			
		}
		catch(Exception e)
		{
			System.out.println("Gui.initGraph: Fehler ");
			e.printStackTrace();
		}
		//initGraph(list, null, layer);
	}

	public void expandDrawKnot(Knot knotBig, Knot knot)
	{
		int sizeMainKnot = 60;
		int sizeSubKnot = 30;
		//drawKnot(knot);
		
		if(knotBig != null) // Knoten und Verbindung zum übergeordneten Knoten malen
		{
			graphGui.drawConnection(knot.positionGui.x,knot.positionGui.y, 
					knotBig.positionGui.x, knotBig.positionGui.y, sizeSubKnot, sizeSubKnot);
			drawKnot(knot, sizeSubKnot);
		}
		else // Nur den MainKnot malen
		{
			knot.position = knot.positionGui;
			myCanvasStatic.drawCircle(knot.positionGui.x, knot.positionGui.y, sizeMainKnot);
			myCanvasCircles.drawImage(knot.ImgFile, knot.positionGui.x, knot.positionGui.y, sizeMainKnot*2-10);
		}
		
		// Falls die Unterknoten sichtbar sind, die Struktur weiter malen
		if(knot.VisibleSubKnots == true)
			for(int i = 0; i < knot.subKnots.size(); i++)
				expandDrawKnot(knot, knot.subKnots.get(i));
		else
		{
			myCanvasStatic.drawThinCircle(knot.position.x,knot.position.y, 35);
			myCanvasStatic.drawThinCircle(knot.position.x,knot.position.y, 40);
		}
	}
	
	public void drawKnot(Knot knot, int sizeSubKnot)
	{
		if(knot.position.distance(knot.positionGui) > 5)
			new AnimationGui().makeAnimations(knot, this);
		myCanvasStatic.drawCircle(knot.position.x, knot.position.y, sizeSubKnot);
		// Großes Bild zeichnen
		if(knot.isGraph())
			myCanvasStatic.drawImage(knot.GraphImg, knot.position.x, knot.position.y, sizeSubKnot*2-10);
		else
			myCanvasStatic.drawImage(knot.ImgFile, knot.position.x, knot.position.y, sizeSubKnot*2-10);
		
		// Kleines Bild
		if(knot.isWebLink())
			myCanvasCircles.drawImage(knot.WeblinkImg, knot.position.x-20, knot.position.y+20, sizeSubKnot);
		else if(knot.isGraph())
			;// nichts -- myCanvasCircles.drawImage(knot.GraphImg, knot.position.x-20, knot.position.y+20, sizeSubKnot);
		else if(knot.File != null && !knot.File.equals(""))
			myCanvasCircles.drawImage(SystemFileIcon.getFileIcon(knot.File), knot.position.x-20, knot.position.y+20, sizeSubKnot);
	}
	
	public void drawTempKnot(Knot knot, Knot parentKnot)
	{
		myCanvasCircles.clearAll();
		if(parentKnot != null)
		{
			double dist = parentKnot.positionGui.distance(knot.positionGui);
			if(dist < 300)
				myCanvasCircles.drawThinConnection(parentKnot.positionGui.x, parentKnot.positionGui.y,
						knot.positionGui.x, knot.positionGui.y, graph.graphSettings.getGrayColor());
			else if(dist < 400)
				myCanvasCircles.drawThinConnection(parentKnot.positionGui.x, parentKnot.positionGui.y,
						knot.positionGui.x, knot.positionGui.y, new Color(1,0.5,0.1,1));
			else
				myCanvasCircles.drawThinConnection(parentKnot.positionGui.x, parentKnot.positionGui.y,
						knot.positionGui.x, knot.positionGui.y, new Color(1,0.1,0.1,1));
		}
		//clear(knot.positionGui.x,knot.positionGui.y, 50);
		myCanvasCircles.drawCircle(knot.positionGui.x,knot.positionGui.y, 30);
		if(isEditMode)
			myCanvasCircles.drawCross(knot.positionGui.x,knot.positionGui.y, 8);
	}
	
	public void drawKnot(Knot knot)
	{
		myCanvasCircles.clear(knot.position.x,knot.position.y, 50);
		myCanvasCircles.drawCircle(knot.position.x,knot.position.y, 30);
		if(isEditMode)
			myCanvasCircles.drawCross(knot.position.x,knot.position.y, 8);
	}
	
	public String getFileFromFileChooser(boolean saveDialog)
	{
		try
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Bild auswählen");
			 /*FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "JPG & GIF Images", "jpg", "gif");
				    chooser.setFileFilter(filter);*/
			if(saveDialog == false)
				return fileChooser.showOpenDialog(primaryStage).getAbsolutePath();
			else
				return fileChooser.showSaveDialog(primaryStage).getAbsolutePath();
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public void startFile(String file)
	{
		try {
			if(file.contains("http") || file.contains(".com") || file.contains(".de"))
			{
				Desktop.getDesktop().browse(new URI(file));
			}
			else if(file.contains(".ser"))
			{
				this.listener.loadFile(file);
			}
			else
			{
				File myFile = new File(file);
				Desktop.getDesktop().open(myFile);
			}
			// siehe auch: http://stackoverflow.com/questions/526037/how-to-open-user-system-preferred-editor-for-given-file
		} catch (Exception e) {
			System.out.println("Gui.startFile: Datei kann nicht gefunden/geladen werden!");
		}
	}
	
	public void miniWindow()
	{
		primaryStage.setAlwaysOnTop(true);
		this.primaryStage.setWidth(140);
		this.primaryStage.setHeight(140);
	}
	
	public void maxWindow()
	{
		primaryStage.setAlwaysOnTop(false);
		this.primaryStage.setWidth(FXExplorer.SIZE_X);
		this.primaryStage.setHeight(FXExplorer.SIZE_Y);
	}
	
	public void saveSnapshot(String file)
	{
		//myCanvasStatic.exportSnapshot(file);
	}
	
	public static boolean showMessagebox(String text1, String text2)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(text1);
		alert.setContentText(text2);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    return true;
		} else {
		    return false;
		}
	}
}
