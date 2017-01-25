package Gui;
import java.awt.Desktop;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import Graph.Graph;
import Graph.Knot;
import Gui.GuiListener.RightClicked;
import Settings.KnotGui;
import Settings.SettingsGui;
import Start.FXExplorer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
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
	private Canvas canvasStatic;
	private Canvas canvasCircles;
	private MyCanvas myCanvasStatic;
	private MyCanvas myCanvasCircles;
	private Text mouseOverText;
	
	public static final int MAX_BUTTONS = 50;

	private Button[] buttons;
	private Text[] text;
	private long clickTimeDifference;
	public GuiListener listener;
	public Pane pane;
	private EHandler eHandler;
	public boolean isEditMode = true; // Zeichnet kleine Kreuze in den Mittelpunkten
	private MenuItem newSubKnot;
	private MenuItem deleteSubKnot;
	private MenuItem changeKnotPaths;
	private Graph graph;
	private Scene scene;
	private boolean isDragging;
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
		//pane.getChildren().clear(); //-- Löscht wirklich alles
		if(buttons != null)
			for(int i = 0; i < buttons.length; i++)
	        {
				if(buttons[i] != null)
					pane.getChildren().remove(buttons[i]);
				if(text[i] != null)
					pane.getChildren().remove(text[i]);
	        }
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
    	
		canvasStatic = new Canvas(FXExplorer.SIZE_X, FXExplorer.SIZE_Y);
        pane.getChildren().add(1, canvasStatic);
        
        canvasCircles = new Canvas(FXExplorer.SIZE_X, FXExplorer.SIZE_Y);
        pane.getChildren().add(2, canvasCircles);
        myCanvasStatic = new MyCanvas(canvasStatic);
        myCanvasCircles = new MyCanvas(canvasCircles);
        
        
       
        
        Button btnLoad = new Button(">");
        btnLoad.setTooltip(new Tooltip(Database.TextGui.ButtonLoadTooltip));
        pane.getChildren().add(btnLoad);
        btnLoad.relocate(10,50);
        btnLoad.getStyleClass().add("buttonO");
        
        Button btnSettings = new Button("...");
        btnSettings.setTooltip(new Tooltip(Database.TextGui.ButtonSettingsTooltip));
        pane.getChildren().add(btnSettings);
        btnSettings.relocate(10,100);
        btnSettings.getStyleClass().add("buttonO");
        
        Button btnUp = new Button("");
        pane.getChildren().add(btnUp);
        btnUp.relocate(300,10);
        btnUp.getStyleClass().add("buttonO");
        btnUp.getStyleClass().add("buttonO_big");
        btnUp.getStyleClass().add("folder");
        setActionListener(btnUp, btnLoad, btnSettings);
        
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
		newSubKnot.setDisable(true);
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
	
	private void setActionListener(
			Button btnUp, Button btnLoad, Button btnSettings)
	{
		
        
        btnUp.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if(listener != null)
                {
                	// ToDo: Listener auf alle Buttons erweitern
                	listener.buttonUpClicked();
                }
            }
        });
        
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
        
        eHandler = new EHandler();
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
		try
		{	
			expandDrawKnot(null, graph.mainKnot);
			
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
			if(knot.position.distance(knot.positionGui) > 5)
				new AnimationGui().makeAnimations(knot, this);
			myCanvasStatic.drawShapes(knot.positionGui.x,knot.positionGui.y, 
				knotBig.positionGui.x, knotBig.positionGui.y, sizeSubKnot, sizeSubKnot);
			myCanvasStatic.drawCircle(knot.position.x, knot.position.y, sizeSubKnot);
			myCanvasStatic.drawImage(knot.ImgFile, knot.position.x, knot.position.y, sizeSubKnot*2-10);
			if(knot.isWebLink())
				myCanvasCircles.drawImage(knot.WeblinkImg, knot.position.x-20, knot.position.y+20, sizeSubKnot);
			else if(knot.isGraph())
				myCanvasCircles.drawImage(knot.GraphImg, knot.position.x-20, knot.position.y+20, sizeSubKnot);
			else if(knot.File != null && !knot.File.equals(""))
				myCanvasCircles.drawImage(sysIcon.getFileIcon(knot.File), knot.position.x-20, knot.position.y+20, sizeSubKnot);
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
	
	public void drawTempKnot(Knot knot)
	{
		myCanvasCircles.clear(knot.positionGui.x,knot.positionGui.y, 50);
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
	
	/**
	 * Graphen initialiseren mit String-Array und einzelnen Layer
	 */
	public void initGraph(String[] files, boolean[] isFolders, int[] layer)
	{
		int tempLayer = 0;
		buttons = new Button[MAX_BUTTONS];
    	text = new Text[MAX_BUTTONS];
    	
    	//canvas = new Canvas(1000, 2500);
        //pane.getChildren().add(0, canvas);
    	
        
        // Strukturen zeichnen
        drawStructure(files, layer);
        
        //Buttons und Texte erstellen
        for(int i = 0; i < files.length; i++)
        {
        	text[i] = new Text(files[i]);
        	text[i].getStyleClass().add("titleButton");
        	text[i].setOpacity(1);
        	text[i].relocate(100,i*80+40);
        	pane.getChildren().add(text[i]);
            buttons[i] = new Button("");
            pane.getChildren().add(buttons[i]);
            buttons[i].getStyleClass().add("buttonO");
            if(isFolders != null &&  isFolders[i])
            	 buttons[i].getStyleClass().add("folder");
            buttons[i].relocate(50,i*80+40);
            buttons[i].setOpacity(1);
            buttons[i].setOnAction(eHandler);
        }
	}
	
	private void drawStructure(String files[], int[] layer)
    {
		if(layer != null)
			for(int i = 0; i < files.length; i++)
			{
				myCanvasCircles.drawShapes(200-20*layer[i],i*80+40, 400, 250, 30, 60);
				if(isEditMode)
					myCanvasCircles.drawCross(200-20*layer[i],i*80+40, 8);
			}
		else
			for(int i = 0; i < files.length; i++)
			{
				myCanvasCircles.drawShapes(50,i*80+40, 400, 250, 30, 60);
				if(isEditMode)
					myCanvasCircles.drawCross(50,i*80+40, 8);
			}
		
		myCanvasCircles.addShadow();
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
	
	
	
	
	
	class EHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event) {
			for(int i = 0; i < buttons.length; i++)
	        {
				if(event.getSource() == buttons[i])
					listener.buttonClicked(i);
	        }
		}	
	}
	
	class EHandlerMouseEntry implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event) {
			for(int i = 0; i < buttons.length; i++)
	        {
				if(event.getSource() == buttons[i])
					listener.buttonClicked(i);
	        }
		}	
	}
}
