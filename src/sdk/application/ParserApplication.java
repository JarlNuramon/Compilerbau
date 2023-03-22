package sdk.application;

import java.util.List;

import sdk.stack.KellerMaschineSDK;

public class ParserApplication {

	public static void main(String[] args) {
		List<String> code = CompilerRun
				.getPseudoCodeOrError(new String[]{"testdatei_arithmetik.txt"});
		printCode(code);
		if (code != null && !code.isEmpty())
			new KellerMaschineSDK(
					args.length > 1 && Boolean.parseBoolean(args[1]))
							.runPseudoCode(code);

	}
	private static void printCode(List<String> code) {
		for (String statement : code) {
			System.out.println(statement);
		}
	}

}
