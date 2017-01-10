package Gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

public class WindowBar {
	private Gui gui;
	
	public WindowBar(Gui gui)
	{
		this.gui = gui;
		final FlowPane pane = new FlowPane();
		pane.setAlignment(Pos.TOP_LEFT);
		pane.setHgap(15);
        //fp.setBackground(new Background());
        gui.pane.getChildren().add(pane);
        
        // Schließen-Button
        Button btnX = new Button("x");
        pane.getChildren().add(btnX);
        btnX.getStyleClass().add("buttonX");
        
        // Minimize-Button
        Button btnMinimize = new Button("-");
        pane.getChildren().add(btnMinimize);
        btnMinimize.getStyleClass().add("buttonMinimize");
        
        // Move-Button zum Bewegen des Programms
        Button btnMove = new Button("o");
        pane.getChildren().add(btnMove);
        btnMove.getStyleClass().add("buttonMove");
        
        setActionListeners(btnX, btnMinimize, btnMove);
	}
	
	private void setActionListeners(Button btnX, Button btnMinimize, Button btnMove)
	{
		btnX.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
            	gui.listener.buttonExitClicked();
                System.exit(0);
            }
        });
		
		btnMinimize.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
            	gui.primaryStage.setIconified(true);
            }
        });
		
		btnMove.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				gui.primaryStage.setX(event.getScreenX()-95);
				gui.primaryStage.setY(event.getScreenY()-15);
			}
        });
	}
}
