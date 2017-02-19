package Gui;
import javafx.event.ActionEvent;

public interface GuiListener {
	public static enum RightClicked {NEW, DELETE, CHANGE_IMG, CHANGING};
	public boolean buttonExitClicked();
	public void loadFile(String file);
	public void saveFile(String file);
	public void newGraph();
	public void contextMenuClicked(RightClicked clicked);
	public void mouseClicked(double x, double y, boolean rightClick);
	public void mouseDoubleClicked(double x, double y);
	public void mouseDragged(double d, double e, boolean rightClick, boolean click);
	public void mouseReleased(double x, double y);
}
