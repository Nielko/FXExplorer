package Settings;

import Database.TextGui;
import Graph.GraphSettings;
import Graph.Knot;
import Gui.Gui;
import Gui.MyCanvas;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SettingsGui{
	private Stage stage;
	private TextField pathTextField;
	private TextField imgTextField;
	

	public SettingsGui(Gui gui, GraphSettings settings)
	{
		Parent root;
		try {
			//root = FXMLLoader.load(getClass().getResource("KnotSettings.fxml"));
			
			GridPane grid = new GridPane();
	        grid.setAlignment(Pos.CENTER);
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(25, 25, 25, 25));

	        /*Text scenetitle = new Text("Welcome");
	        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        grid.add(scenetitle, 0, 0, 2, 1);*/
	        
	        // Checkbox für dicke Verbindungen
	        CheckBox cbConnections = new CheckBox(TextGui.KnotSettingsBigConnections);
	        grid.add(cbConnections, 0, 1, 2, 1);
	        cbConnections.setSelected(settings.thickPaths);
	        
	        // Checkbox für Blur
	        CheckBox cbBlur = new CheckBox(TextGui.KnotSettingsBlur);
	        grid.add(cbBlur, 0, 6);
	        cbBlur.setSelected(settings.blur);
	        
	     // Ok-Button und Listener zum Übernehmen der Daten
	        Button btnColor = new Button("Choose Color");
	        //btnOk.set
	        grid.add(btnColor, 2, 12);
	        
	        //btnColor.setOnAction((ActionEvent event)->{
	        	ColorPicker picker = new ColorPicker(settings.getColor());
	        	
	        	 grid.add(picker, 2, 13);
	       // });
	        
	        // Ok-Button und Listener zum Übernehmen der Daten
	        Button btnOk = new Button("Ok");
	        //btnOk.set
	        HBox hbBtnOk = new HBox(10);
	        hbBtnOk.setAlignment(Pos.BOTTOM_RIGHT);
	        hbBtnOk.getChildren().add(btnOk);
	        grid.add(hbBtnOk, 2, 11);
	        
	        // Bei OK Farben übernehmen und schließen
	        btnOk.setOnAction((ActionEvent event)->{
	        	settings.setColor(picker.getValue());
	        	settings.thickPaths = cbConnections.isSelected();
	        	settings.blur = cbBlur.isSelected();
	        	gui.redraw();
	        	
	        	stage.close();
	        });
	        
	     // Abbrechen-Button und Listener zum Schließen des Fensters
	        Button btnAbort = new Button("Abbrechen");
	        btnAbort.setCancelButton(true);
	        HBox hbBtnAbort = new HBox(10);
	        hbBtnAbort.setAlignment(Pos.BOTTOM_RIGHT);
	        hbBtnAbort.getChildren().add(btnAbort);
	        grid.add(hbBtnAbort, 3, 11);
	        
	        btnAbort.setOnAction((ActionEvent event)->{
	        	stage.close();
	        });

	        
			stage = new Stage();
		    stage.setTitle(TextGui.KnotSettingsTitle);
		    stage.setScene(new Scene(grid, 450, 450));
		    stage.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
	}
	
	public String getFileFromFileChooser()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Datei auswählen");
		return fileChooser.showOpenDialog(stage).getAbsolutePath();
	}

}
