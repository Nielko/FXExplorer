package Start;
import Gui.Gui;
import Settings.KnotGui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FXExplorer extends Application {
	public static final int SIZE_X = 800;
	public static final int SIZE_Y = 600;
	private static String file;

    public static void main(String[] args) {
    	if(args.length > 0)
    		file = args[0];
        launch(args);
    }
    
    

    @Override
    public void start(Stage primaryStage) {
    	
    	Gui gui = new Gui(primaryStage);
    	Logic logic = new Logic(gui, file);
    	gui.setListener(logic.getGuiListener());
    	
    }
    
    
    
    
    
    
}