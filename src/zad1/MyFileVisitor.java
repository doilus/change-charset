package zad1;

//import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.*; //do continue


public class MyFileVisitor implements FileVisitor<Path> {
	
	//String resultFile;
	private Path startingDir;
	
	FileChannel outputFile;
	
	private ByteBuffer buff;
	
	//wyczyścimy plik zapisu
	
	//kodowanie pliku
	Charset decode = Charset.forName("Cp1250");
	Charset code = Charset.forName("UTF-8");
	
	MyFileVisitor(){}
	
	MyFileVisitor(Path startingDir, Path resultFile ) {
		//utworzenie pliku do zapisu
		this.startingDir = startingDir;
	
		
	
		try {
			this.outputFile = FileChannel.open(resultFile, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		// uruchamia się zawsze gdy pojawia się nowy podkatalog
		// decyduje że przeszukiwane są wszystkie podkatalogi
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		//wywoływana gdy jest znaleziony plik
		//sprawdzamy czy file jest txt
		

			writeToFile(file);
			
		
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		// gdy występuje błąd w odczytaniu pliku np. jest zablokowany
		System.out.println("Błąd w odczytaniu pliku" + file.toString());
		
		return CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException{
		// gdy zwiedzona została całość katalogu
		
		boolean finishedSearch = Files.isSameFile(dir, startingDir);
		if(finishedSearch) {
			System.out.println("Wyszukiwanie zakończone");
			return TERMINATE;
		}
		return CONTINUE;
	}
	
	public void writeToFile(Path file) throws IOException {
		FileChannel readFile = FileChannel.open(file, StandardOpenOption.READ);
		buff = ByteBuffer.allocate((int)readFile.size());
		readFile.read(buff);
		
		buff.flip();
		//kodowanie pliku
		CharBuffer cBuff = decode.decode(buff);
		buff = code.encode(cBuff);
		
		outputFile.write(buff);
		readFile.close();
	}
	

}
