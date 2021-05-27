import java.io.*;

public class SyntaxAnalyzer {
    public void syntaxAnalyze(String fileName) {
        String[] tokens;
        boolean result;

        InputOptimizer io = new InputOptimizer();
        tokens = io.inputOptimize(fileName);
        if (tokens == null) {
            System.out.println("Lexical Error!!!");
            return ;
        }
        SlrParse sp = new SlrParse();
        result = sp.parsing(tokens);
        if (result == true)
            System.out.println("Result: Accepted");
        else
            System.out.println("Result: Not accepted");
        return;
    }
}
