import java.io.*;
import java.util.Scanner;

public class LAnalyzer {
    public static final int NUM_OF_TOKEN = 23;

    public static void main(String[] args) {

        ParseTable pt = new ParseTable();
        DFA[] dfa = new DFA[NUM_OF_TOKEN];
        SyntaxAnalyzer sa = new SyntaxAnalyzer();

        FileParser fileParser = new FileParser();
        String inputText= "";
        String outputText = "";

        String input;
        Boolean isFinish;

        String finalOutput, finalToken = "temp";

        int startPos = 0;
        int[] checkToken = new int[NUM_OF_TOKEN+1];

        for (int i = 0; i < NUM_OF_TOKEN; i++) {
            dfa[i] = new DFA(pt.splitTable(i));
            dfa[i].resetDFA();
        }

        for (String fileName : args) {
            inputText = fileParser.parseInput(fileName);
            outputText += ("Input : " + inputText + "\n");
            startPos = 0;

            while(startPos < inputText.length()) {

                for(int T=0;T<NUM_OF_TOKEN;T++){
                    checkToken[T] = -1;                               //배열 초기화
                    for(int i=startPos;i<=inputText.length();i++) {

                        isFinish = dfa[T].getNextState("fin");
                        if(i == inputText.length())
                            input = "&";
                        else                     
                            input = inputText.substring(i,i+1);

                        if(input.equals("-") && T==10 && i==startPos && (finalToken.equals("IDENTIFIER") || finalToken.equals("INTEGER")))
                            break;                              //"-"의 바로 앞 토큰이 Integer나 Identifier라면 "-"는 무조건 Operator로 취급.
                        
                        if(input.equals("-") && T==11 && i==startPos) {
                        }
                        if(!dfa[T].getNextState(input)) {     //c_state에 차례대로 symbol을 삽입.
                            dfa[T].resetDFA();                    //해당 DFA가 끝났을때 (or 문장이 끝났을 때)
                            if(isFinish) {
                                // System.out.println(input+", "+i+", "+startPos);                        //Token으로 성립했다면
                                checkToken[T] = i-startPos;  //Token의 길이 저장
                            }
                            break;
                        }
                        else {
                        }
                    }
                    dfa[T].resetDFA();
                }
                int max = 0;
                int index = -1;
                for(int i=0;i<NUM_OF_TOKEN;i++) {      //제일 길게 파싱을 성공한 DFA 추출
                    if(max < checkToken[i]) {          //길이가 같다면 먼저 추출된 DFA(우선순위가 높은 DFA)가 추출됨.
                        max = checkToken[i];
                        index = i;
                    }
                }
                if( index == -1) {                      //어떤 DFA도 추출되지 않았다면 오류
                    outputText += ("Lexical Error Occured!!");
                    break;
                }
                if(!dfa[index].getName().equals("WHITESPACES")) {   //Whitespace 토큰은 저장/출력하지 않는다.
                    finalToken = dfa[index].getName();
                    finalOutput = inputText.substring(startPos,startPos+max);

                    if(finalToken.equals(dfa[8].getName()) || finalToken.equals(dfa[9].getName()) ||
                    finalToken.equals(dfa[10].getName()) || finalToken.equals(dfa[11].getName()) || 
                    finalToken.equals(dfa[13].getName()) || finalToken.equals(dfa[20].getName()) || finalToken.equals(dfa[21].getName()))
                        outputText += ("<"+finalToken+","+finalOutput+"> ");
                    else
                        outputText += ("<"+finalToken+"> ");    
                }
                startPos += max;
            }
            fileParser.parseOutput(fileName, outputText);

            System.out.println("Syntax Analyze about " + fileName);
            sa.syntaxAnalyze(fileName);
        }
    }
}