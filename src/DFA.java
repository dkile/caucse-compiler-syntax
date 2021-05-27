import org.json.simple.JSONObject;

public class DFA {
    private String name;
    private JSONObject table = null; //토큰 별 Transition Table 저장용 변수
    private JSONObject c_state = null; //현재 state를 담고있는 변수

    DFA(JSONObject table) {
        this.table = table;
        this.name = (String)this.table.get("name");
    }
    public void resetDFA() { //DFA의 시작 state로 이동
        this.c_state = (JSONObject)this.table.get("T0");
    }
    public String getName() {
        return this.name;
    }
    public JSONObject getC_state() {
        return this.c_state;
    }
    public boolean getNextState(String ch_input) {
        String n_input = "";

        if ("fin".equals(ch_input)) {
            n_input = (String)this.c_state.get(ch_input);
            return ("true".equals(n_input) ? true : false);
        }
        for (Object k : this.c_state.keySet()) {
            String key = k.toString();
            if (key.indexOf(ch_input) > -1) {
                n_input = (String)this.c_state.get(key);
                break;
            }
        }
        JSONObject n_state = (JSONObject)this.table.get(n_input);
        if(n_state == null) {
            return false;
        }
        else {
            this.c_state = n_state;
            return true;
        }
    }
}