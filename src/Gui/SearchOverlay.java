package Gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SearchOverlay {
	private Pane pane;
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
        
        Button btnSave = new Button(Database.TextGui.ButtonLoadText);
        btnSave.setTooltip(new Tooltip(Database.TextGui.ButtonLoadTooltip));
        pane.getChildren().add(btnSave);
        btnSave.relocate(20,50);
        btnSave.getStyleClass().add("buttonLarge");
        
        Button btnLoad = new Button(Database.TextGui.ButtonLoadText);
        btnLoad.setTooltip(new Tooltip(Database.TextGui.ButtonLoadTooltip));
        pane.getChildren().add(btnLoad);
        btnLoad.relocate(20,100);
        btnLoad.getStyleClass().add("buttonLarge");
        
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
			pane.setOpacity(0);
		else
			pane.setOpacity(1);
	}
}
