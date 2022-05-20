package ownCompiler;


class TestScanner {
    static public void main(String args[]) {
        SDKScanner scanner;
        scanner = new SDKScanner();
        if (scanner.readInput("testdatei_arithmetik.txt")) {
            scanner.printInputStream();
            if (scanner.lexicalAnalysis())
                scanner.printTokenStream();
        } else
            System.out.println("professorstuff.Scanner beendet");
    }
}