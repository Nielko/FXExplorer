package Gui;

import javafx.animation.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class FrostyBackground {
	private Stage primaryStage;
	private Pane pane;
	private ImageView background;
	private Gui gui;
	
	
	public FrostyBackground(Pane pane, Stage primaryStage, Gui gui)
	{
		this.primaryStage = primaryStage;
		this.pane = pane;
		this.gui = gui;
		
		// Frosty-Background-Effekt durch kopieren
	   	double BLUR_AMOUNT = 5;
	   	Effect frostEffect = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 3);
	   	background = new ImageView();
	   	background.setImage(copyBackground(primaryStage));
	    background.setEffect(frostEffect);
	    pane.getChildren().add(0,background);
	}
	
    private Image copyBackground(Stage stage) {
        final int X = (int) stage.getX();
        final int Y = (int) stage.getY();
        final int W = (int) stage.getWidth();
        final int H = (int) stage.getHeight();

        try {
        	
            java.awt.Robot robot = new java.awt.Robot();
            java.awt.image.BufferedImage image = robot.createScreenCapture(
            		new java.awt.Rectangle(X, Y, W, H));
            return SwingFXUtils.toFXImage(image, null);
            
        } catch (java.awt.AWTException e) {
            e.printStackTrace();
            return null;
        }
    }

    private javafx.scene.shape.Rectangle makeGrayRectangle(Stage stage) {
        return new javafx.scene.shape.Rectangle(
                stage.getWidth(),
                stage.getHeight(),
                Color.WHITESMOKE.deriveColor(
                        0, 1, 1, 0.01
                )
        );
    }
    
    public void mousePressed(MouseEvent mouseEvent)
    {
        pane.getChildren().set(0, makeGrayRectangle(primaryStage));
        gui.removeAllButtons();
    }
    
    public void mouseReleased(MouseEvent mouseEvent)
    {
    	updateBackground();
    }
    
    public void updateBackground()
    {
    	
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	background.setImage(copyBackground(primaryStage));
   	 	pane.getChildren().set(0,background);
   	 	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	 	gui.redraw();
    }
}
