package Gui;

import Graph.Knot;
import javafx.animation.Transition;
import javafx.util.Duration;

public class MyAnimation  extends Transition{
	private Knot knot;
	private Gui gui;
	private float deltaX;
	private float deltaY;

	public MyAnimation(Knot knot, Gui gui)
	{
		System.out.println("MyAnimation: Neue Animation "+knot.position +" -> " +knot.positionGui);
		this.setCycleDuration(new Duration(500));
		this.knot = knot;
		this.gui = gui;
		deltaX = (knot.positionGui.x-knot.position.x);
		deltaY = (knot.positionGui.y-knot.position.y);
	}
	@Override
	protected void interpolate(double frac) {
		float cycle = (float) (this.getCurrentTime().toMillis()/this.getTotalDuration().toMillis());
		
		if(knot != null && knot.position != null && knot.positionGui != null)
		{
			knot.position.x = (int) (knot.positionGui.x-deltaX*(1-cycle));
			knot.position.y = (int) (knot.positionGui.y-deltaY*(1-cycle));
			gui.drawKnot(knot);
		}
		if(Math.abs(knot.position.x - knot.positionGui.x) < 3 && Math.abs(knot.position.y - knot.positionGui.y) < 3)
		{
				knot.position.x = knot.positionGui.x;
				knot.position.y = knot.positionGui.y;
				gui.redraw();
		}
			
	}
}
