package Database;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Graph.Graph;
import Graph.Knot;

public class GraphDatabase {
	public static String directoryHome = Graph.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	public static final String databaseHome ="myDatabase.ser";
	public String datName ="myDatabase.ser";  // Standard-Graph, kann in load überschrieben werden
	//public ArrayList<Graph> graphs;
	 
	
	public GraphDatabase()
	{
	     //load(datName);
	}
	
	public GraphDatabase(String file)
	{
		if(file == null)
			file = datName;
	     load(file);
	}
	
	public Graph load(String file)
	{
		ObjectInputStream in = null; 
		SerializableGraph graph = null;
	     try { 
	    	 if(file != null)
	    		 datName = file;
	    	 else
	    		 return this.getStartGraph();//datName = databaseHome;
	    	 FileInputStream fin = new FileInputStream(datName);
	         in = new ObjectInputStream(fin); 
	         graph = (SerializableGraph) in.readObject(); 
	     } catch (FileNotFoundException ex) { 
	         System.out.println("Speicherdatei (noch) nicht vorhanden!"); 
	     } catch (Exception ex) { 
	         System.out.println("GDatabase: "); 
	         ex.printStackTrace();
	     } finally { 
	         try { 
	             if(in != null) in.close(); 
	         } catch(IOException e) {} 
	     } 
	     // Keine gespeicherte Instanz gefunden -> Neue wird erzeugt mit einem Graph
	     if (graph == null) 
	     {
	    	 System.out.println("Keine Datenbank-Instanz gefunden -> neue wird angelegt.");
	    	 return new Graph();
	     }
	     return new Graph(graph);
	}
	
	public void saveAs(Graph graph, String file)
	{
		datName = file;
		save(graph);
	}
	
	
	
	public boolean save(Graph graph)
	{
		// Bei nicht gesetzten Dateinamen wird abgebrochen mit false
		/*if(this.datName == null || this.datName.equals(""))
		{
			System.out.println("GraphDatabase.save: Dateiname ist nicht gesetzt");
			return false;
		}*/
		if(this.datName == null || this.datName.equals("")) // Neuen zufälligen Name wählen
		{
			do
			{
				this.datName = graph.mainKnot.name+"-graph"+(int) (Math.random()*9999)+".ser";
			} while((new File(datName)).exists());
		}
		
		
		// Name des Mainknotens setzen 
		graph.mainKnot.name = datName;
				
		ObjectOutputStream aus = null; 
        try { 
            aus = new ObjectOutputStream(new FileOutputStream(datName)); 
            aus.writeObject(new SerializableGraph(graph)); 
        } catch (Exception ex) { 
            ex.printStackTrace();
            return false;
        } finally { 
            try { 
                if(aus != null) { 
                    aus.flush(); 
                    aus.close(); 
                } 
            } catch(IOException e) {} 
        } 
        return true;
	}
	
	public void saveStartGraph(Graph graph)
	{
		String temp = datName;
		datName = this.databaseHome;
		save(graph);
		datName = temp;
	}
	
	
	public Graph getStartGraph()
	{
		Graph ret = new Graph();
		File file = new File(directoryHome).getParentFile();
		File files[] = file.listFiles();
		for(int i = 0; i < files.length; i++)
		{
			if(files[i].getName().endsWith(".ser"))
			{
				if(files[i].getName().contains("-"))
					ret.freeKnots.add(new Knot(files[i].getName().split("-")[0], files[i].getName()));
				else	
					ret.freeKnots.add(new Knot(files[i].getName(), files[i].getName()));
			}
		}
		
		return ret;
	}
}
