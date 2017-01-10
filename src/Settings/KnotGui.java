package Settings;

import java.io.IOException;

import Database.TextGui;
import Graph.Knot;
import Gui.Gui;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class KnotGui{
	private Gui gui;
	private Stage stage;
	private Knot selectedKnot;
	private TextField nameTextField;
	private TextField pathTextField;
	private TextField imgTextField;
	
	public KnotGui(Knot knot, Gui gui)
	{
		this(gui);
		selectedKnot = knot;
		nameTextField.setText(knot.name);
		pathTextField.setText(knot.File);
		imgTextField.setText(knot.ImgFile);
		
	}

	public KnotGui(Gui gui)
	{
		this.gui = gui;
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
	        
	        Label name = new Label(TextGui.KnotSettingsName);
	        grid.add(name, 0, 0, 2, 1);

	        nameTextField = new TextField();
	        grid.add(nameTextField, 2, 0);
	        

	        // Datei-Pfad und Button für FileChooser
	        Label userName = new Label(TextGui.KnotSettingsPath);
	        grid.add(userName, 0, 1);

	        pathTextField = new TextField();
	        grid.add(pathTextField, 2, 1);
	        
	        Button btn = new Button(TextGui.KnotSettingsChoosePath);
	        HBox hbBtn = new HBox(10);
	        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	        hbBtn.getChildren().add(btn);
	        grid.add(hbBtn, 2, 2);

	        // Bild-Pfad
	        Label knotImgPath = new Label(TextGui.KnotSettingsImg);
	        grid.add(knotImgPath, 0, 6);

	        imgTextField = new TextField();
	        grid.add(imgTextField, 2, 6);
	        
	        Button btnImg = new Button(TextGui.KnotSettingsChooseImg);
	        HBox hbBtnImg = new HBox(10);
	        hbBtnImg.setAlignment(Pos.BOTTOM_RIGHT);
	        hbBtnImg.getChildren().add(btnImg);
	        grid.add(hbBtnImg, 2, 7);

	        btn.setOnAction(new EventHandler<ActionEvent>() {

	            @Override
	            public void handle(ActionEvent e) {
	            	String path = getFileFromFileChooser();
	            	if(path != null)
	            		pathTextField.setText(path);
	            }
	        });
	        btnImg.setOnAction(new EventHandler<ActionEvent>() {

	            @Override
	            public void handle(ActionEvent e) {
	            	String path = getFileFromFileChooser();
	            	if(path != null)
	            		imgTextField.setText(path);
	            }
	        });
	        
	        // Ok-Button und Listener zum Übernehmen der Daten
	        Button btnOk = new Button("Ok");
	        HBox hbBtnOk = new HBox(10);
	        hbBtnOk.setAlignment(Pos.BOTTOM_RIGHT);
	        hbBtnOk.getChildren().add(btnOk);
	        grid.add(hbBtnOk, 2, 11);
	        
	        btnOk.setOnAction((ActionEvent event)->{
	        	selectedKnot.name = nameTextField.getText();
	        	selectedKnot.File = pathTextField.getText();
	        	selectedKnot.ImgFile = imgTextField.getText();
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
