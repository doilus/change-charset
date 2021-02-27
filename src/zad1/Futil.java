package zad1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Futil {
	
	
	
	Futil(){}

	public static void processDir(String dirName, String resultFileName) {
		
		//utworzenie ścieżki katalogu
		Path startingDir = Paths.get(dirName);
		
		Path resultFile = Paths.get(resultFileName);
		
		
		try {
		//przekazanie do konstruktora nazwy pliku
		MyFileVisitor visitor = new MyFileVisitor(startingDir, resultFile); 
		Files.walkFileTree(startingDir, visitor);
		} catch (IOException e) {
			System.out.println("Zakonczone niepowodzeniem");
		}
	}

}
