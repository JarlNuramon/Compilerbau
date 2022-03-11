package ownCompiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParserApplication {

	List<String> lines = new ArrayList<>();
	public static void main(String[] args) {
		try(BufferedReader bR = Files.newBufferedReader(Paths.get("Code.sdk"))){
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
