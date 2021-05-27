import java.io.*;
import java.util.Stack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SlrParse {
    private JSONObject parseTable;
    private JSONObject ruleTable;
    private JSONParser parser = new JSONParser();
    private Stack<String> stack;

    SlrParse() {
        try {
            InputStream in_slr = getClass().getResourceAsStream("/Slr_table.json");
            parseTable = (JSONObject) parser.parse(new InputStreamReader(in_slr));
            InputStream in_rule = getClass().getResourceAsStream("/rule.json");
            ruleTable = (JSONObject) parser.parse(new InputStreamReader(in_rule));
            stack = new Stack<String>();
            stack.push("0");
        } catch (Exception e) {
            System.out.println("변환에 실패");
            e.printStackTrace();
        }
    }

    public boolean parsing(String[] tokens) {
        String rule = "";
        String reduce = "";
        String rule_num = "";
        String goto_state = "";
        int pop_count = 0;
        JSONObject rules;
        for (int i = 0; i < tokens.length;) {
            rules = (JSONObject)parseTable.get(stack.peek());
            //System.out.println("stack top: " + stack.peek());
            rule = (String)rules.get(tokens[i]);
            //System.out.println("token: " + tokens[i]);
            if (rule == null) {
                System.out.println("No rule for "+i+"th "+"token - "+tokens[i]);
                return false;
            }
            else if (rule.equals("acc"))
                return true;
            else if (rule.charAt(0) == 's') {
                rule_num = rule.substring(1);
                //System.out.println("Shift to " + rule_num);
                stack.push(tokens[i]);
                stack.push(rule_num);
                i++;
            }
            else if (rule.charAt(0) == 'r') {
                reduce = (String)ruleTable.get(rule);
                //System.out.println("reduce rule: " + reduce);
                String[] temp = reduce.split(" ");
                pop_count = Integer.parseInt(temp[1]);
                for (int j= 0; j < pop_count; j++) {
                    //System.out.println("popping: " + stack.peek());
                    stack.pop();
                }
                //System.out.println("after pop stack top: " + stack.peek());
                rules = (JSONObject)parseTable.get(stack.peek());
                //System.out.println("after pop token: " + temp[0]);
                goto_state = (String)rules.get(temp[0]);
                stack.push(temp[0]);
                stack.push(goto_state);
               // System.out.println("pushed goto state: " + goto_state);
            }
        }
        return false;
    }
}