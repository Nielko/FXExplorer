package Database;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Graph.Graph;

public class GraphDatabase {
	private static String datName ="myDatabase.ser";  // Standard-Graph, kann in load überschrieben werden
	public ArrayList<Graph> graphs;
	
	public GraphDatabase()
	{
	     load(datName);
	}
	
	public GraphDatabase(String file)
	{
		if(file == null)
			file = datName;
	     load(file);
	}
	
	public void load(String file)
	{
		ObjectInputStream in = null; 
	     try { 
	    	 datName = file;
	    	 FileInputStream fin = new FileInputStream(file);
	         in = new ObjectInputStream(fin); 
	         graphs = (ArrayList<Graph>) in.readObject(); 
	     } catch (FileNotFoundException ex) { 
	         System.out.println("Speicherdatei (noch) nicht vorhanden!"); 
	     } catch (Exception ex) { 
	         System.out.println(ex); 
	     } finally { 
	         try { 
	             if(in != null) in.close(); 
	         } catch(IOException e) {} 
	     } 
	     // Keine gespeicherte Instanz gefunden -> Neue wird erzeugt mit einem Graph
	     if (graphs == null) 
	     {
	    	 System.out.println("Keine Datenbank-Instanz gefunden -> neue wird angelegt.");
	    	 graphs = new ArrayList<Graph>();
	    	 graphs.add(new Graph()); 
	     }
	}
	
	public void saveAs(String file)
	{
		datName = file;
		save();
	}
	
	public void save()
	{
		ObjectOutputStream aus = null; 
        try { 
            aus = new ObjectOutputStream(new FileOutputStream(datName)); 
            aus.writeObject(graphs); 
        } catch (Exception ex) { 
            System.out.println(ex); 
        } finally { 
            try { 
                if(aus != null) { 
                    aus.flush(); 
                    aus.close(); 
                } 
            } catch(IOException e) {} 
        } 
	}
}
