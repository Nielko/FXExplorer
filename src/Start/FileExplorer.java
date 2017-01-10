package Start;
import java.io.File;

public class FileExplorer {
	private File file;
	private File[] filesInFolder;
	
	public FileExplorer(String startPath)
	{
		file = new File(startPath);
	}
	
	public String[] listFiles()
    {
        File[] paths;
        
        // returns pathnames for files and directory
        paths = file.listFiles();
        filesInFolder = paths;
        
        String ret[] = new String[paths.length];
        for(int i = 0; i < paths.length; i++)
        {
        	ret[i] = paths[i].toString();
        }
        return ret;
    }
	
	/**
	 * Gibt an, ob die Files in listFiles Ordner waren oder nicht
	 */
	public boolean[] isFolders()
	{
		boolean ret[] = new boolean[filesInFolder.length];
        for(int i = 0; i < filesInFolder.length; i++)
        {
        	ret[i] = filesInFolder[i].isDirectory();
        }
        return ret;
	}
	
	public void goUp()
	{
		file = file.getParentFile();
	}
	
	public void goTo(String path)
	{
		file = new File(path);
	}
}
