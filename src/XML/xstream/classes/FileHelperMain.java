package XML.xstream.classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import XML.xstream.interfaces.XMLFileHelper;

public class FileHelperMain implements XMLFileHelper{
	
	public FileHelperMain(){

	}

	@Override
	public boolean directoryExists(String filepathToDir) {
		File f = new File(filepathToDir);
		return f.exists() && f.isDirectory();
	}

	@Override
	public boolean addXMLToDirectory(String filepathToDir, String XMLString, String filename) {
		if(!directoryExists(filepathToDir)) return false;
		File file = new File(filename);
		try {
			FileWriter fileWriter = new FileWriter(file, false);
			fileWriter.write(XMLString);
			fileWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getFileContent(String filepathToDir, String filename) {
		Path p = Paths.get(filepathToDir + filename + ".xml");
		return p.getFileName().toString();
	}

}
