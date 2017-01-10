package Gui;

import java.time.Duration;

import Graph.Graph;
import Graph.Knot;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

/**
 * Repräsentiert den Graph mit den Ist-Positionen
 * @author nielskowala
 *
 */
public class AnimationGui{

	/**
	 * Überprüft die Ist-/Soll-Positionen und erzeugt Animationen, bei Abweichungen
	 * @param graph
	 */
	public void makeAnimations(Knot knot, Gui gui)
	{
		MyAnimation ani = new MyAnimation(knot, gui);
		ani.play();
		/*DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ofMillis(0),
                    new KeyValue(x, 0),
                    new KeyValue(y, 0)
            ),
            new KeyFrame(Duration.ofMillis(500),
                    new KeyValue(x, W - D),
                    new KeyValue(y, H - D)
            )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	knot.position.x = (int) (knot.position.x+(knot.position.x-knot.positionGui.x)*0.1f);
        		knot.position.y = (int) (knot.position.y+(knot.position.y-knot.positionGui.y)*0.1f);
        		gui.drawKnot(knot);
            }
        };
        timer.start();
        timeline.playFromStart();*/
	}
	
	
	
	public void translate()
	{
		
	}

	
}
