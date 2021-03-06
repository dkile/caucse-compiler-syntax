import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class InputOptimizer {
    private JSONObject table;
    private JSONParser parser = new JSONParser();

    InputOptimizer() {
        try {
            InputStream in = getClass().getResourceAsStream("/InputOptimize_table.json");
            table = (JSONObject) parser.parse(new InputStreamReader(in));
        } catch (Exception e) {
            System.out.println("변환에 실패");
            e.printStackTrace();
        }
    }

    public String readInput(String fileName) {
        String temp_line = "";
        String line = "";
        try{
            File file = new File("./"+fileName+"_output.txt");
            //입력 스트림 생성
            FileReader filereader = new FileReader(file);
            //입력 버퍼 생성
            BufferedReader bufReader = new BufferedReader(filereader);
            for(int i = 0; (temp_line = bufReader.readLine()) != null; i++) {
                line = temp_line;
            }
            //.readLine()은 끝에 개행문자를 읽지 않는다.
            bufReader.close();
            return line;
        }catch (FileNotFoundException e) {
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        return null;
    }

    public String[] inputOptimize(String fileName) {    //Output을 Syntax Input 형식에 맞게 가공
        String[] tokens;
        String[] token;
        String terminal;
        int flag = 0;

        String input = readInput(fileName);
        
        if (input.contains("Error"))
            return null;
        tokens = input.split(" ");

        for (int i = 0; i < tokens.length; i++) {
            //System.out.println("Tokens"+i+" : "+tokens[i]);
            
            token = tokens[i].split(",");   //“,”를 기준으로 token name과 value 인식
            if (token[0].equals("OPERATOR")) {  //operator의 경우 value를 따로 구별해준다.
                JSONObject temp = (JSONObject)this.table.get(token[0]);
                terminal = String.valueOf(temp.get(token[1]));
            }
            else {
                terminal = String.valueOf(this.table.get(token[0]));
                if (terminal.equals("null")) {
                    continue;
                }
            }
            tokens[flag] = terminal;    //인식한 토큰을 Tokens에 하나씩 저장
            flag++;
        }
        tokens = Arrays.copyOf(tokens, flag + 1);
        tokens[flag] = "$";
        return tokens;
    }
}