import java.io.*;

public class SyntaxAnalyzer {
    public void syntaxAnalyze(String fileName) {
        String[] tokens;
        boolean result;
        FileParser fileParser = new FileParser();

        InputOptimizer io = new InputOptimizer();
        tokens = io.inputOptimize(fileName);        //Output을 Syntax Input 형식에 맞게 가공
        
        if (tokens == null) {
            System.out.println("Lexical Error!!!");
            return ;
        }
        SlrParse sp = new SlrParse();
        result = sp.parsing(tokens);                //가공한 토큰의 SLR 파싱
        if (result == true) {
            System.out.println("Result: Accepted");
            String inputText = fileParser.parseInput(fileName);
            String outputText = inputText + "\nResult : Accept";
            fileParser.parseOutput(fileName, outputText);
        }
        else {
            System.out.println("Result: Not accepted");
            fileParser.parseOutput(fileName, fileParser.parseInput(fileName)+"\nResult : Reject");
        }
        return;
    }
}
