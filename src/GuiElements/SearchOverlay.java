package GuiElements;

import Gui.Gui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SearchOverlay {
	private Pane pane;
	private Text textSearch;
	
	public SearchOverlay(Gui gui)
	{
		pane= new Pane();
		pane.relocate(50, 50);
        //fp.setBackground(new Background());
        gui.pane.getChildren().add(pane);
        
        TextField text = new TextField();
        text.setText("Suche");
        text.setBackground(Background.EMPTY);
        text.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        text.relocate(20, 0);
        pane.getChildren().add(text);
        
        textSearch = new Text();
        textSearch.setText(Database.TextGui.TextNotImplemented);
        textSearch.relocate(160, 40);
        textSearch.setOpacity(0);
        pane.getChildren().add(textSearch);
        
        Button btnSave = new Button(Database.TextGui.ButtonSaveText);
        btnSave.setTooltip(new Tooltip(Database.TextGui.ButtonLoadTooltip));
        pane.getChildren().add(btnSave);
        btnSave.relocate(20,50);
        btnSave.getStyleClass().add("buttonLarge");
        
        Button btnLoad = new Button(Database.TextGui.ButtonLoadText);
        btnLoad.setTooltip(new Tooltip(Database.TextGui.ButtonLoadTooltip));
        pane.getChildren().add(btnLoad);
        btnLoad.relocate(20,100);
        btnLoad.getStyleClass().add("buttonLarge");
        
        // KeyEvent für das Textfeld
        text.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER)) // Overlay mit Text anzeigen
                {
                	pane.setMinWidth(700);
                	pane.setMinHeight(600);
                	pane.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
                	textSearch.setOpacity(1);
                }
            }
        });
        
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	String file = gui.getFileFromFileChooser(true);
            	if(file != null)
            		gui.listener.saveFile(file);
            }
        });
        
        btnLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	String file = gui.getFileFromFileChooser(false);
            	if(file != null)
            		gui.listener.loadFile(file);
            }
        });
	}
	
	public void changeOpacity()
	{
		if(pane.getOpacity() > 0)
		{
			pane.setOpacity(0);
			resetSearch();
		}
		else
			pane.setOpacity(1);
	}
	
	private void resetSearch()
	{
		// Zurücksetzen des Overlays
		pane.setMinWidth(0);
    	pane.setMinHeight(0);
    	pane.setBackground(Background.EMPTY);
    	textSearch.setOpacity(0);
	}
}
