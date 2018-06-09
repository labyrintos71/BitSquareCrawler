package GameObject;

import java.util.ArrayList;

public class GameData {
    String gamenum,gameper,gametime;
    ArrayList<PlayerData> pldata=new ArrayList<>();
    public GameData(String num, String per, String time, ArrayList<PlayerData> data) {
        gamenum=num;
        gameper=per;
        gametime=time;
        pldata=data;
    }
}