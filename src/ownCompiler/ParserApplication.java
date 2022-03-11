package ownCompiler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;

public class ParserApplication {

	
	public static void main(String[] args) {
		try{
			List<String> lines = Files.lines(Paths.get("Code.sdk"),StandardCharsets.UTF_8).toList();
			new SDKParser().parseFile(lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
