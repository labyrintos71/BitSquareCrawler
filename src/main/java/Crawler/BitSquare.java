package Crawler;

import GameObject.GameData;
import GameObject.PlayerData;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class BitSquare {
    static int gameprofit=0;
    static int count=0,errorcnt=0,plcnt=0;

    public void Parse(int start, int end) {
        String profit="";
        DecimalFormat Commas = new DecimalFormat("#,###");
        ArrayList<GameData> data=new ArrayList<>();
        int i;
        for(i=start;i<=end;i++) {
            GameData gd=bitsquare(i);
            if(gd==null) {
                gd=bitsquare(i);
                if(gd==null) {
                    gd=bitsquare(i);
                    if(gd==null) {
                        gd=bitsquare(i);
                    }
                }
            }
            if(gd!=null) data.add(gd);
        }

        gameprofit*=-1;
        plcnt/=4;
        errorcnt/=4;
        profit = (String)Commas.format(gameprofit);

        System.out.println("DATA PRINT COMPLET.");
        System.out.println("ALL COUNT / COMPLETE COUNT : "+i+" / "+count);
        System.out.println("ERROR COUNT / PLAYER NULL ERROR : "+errorcnt+"/"+plcnt);
        System.out.println("PROFIT : "+profit);
        System.out.println("COUNT : "+count);

    }


	public boolean checkNum(int num) {
		StringBuilder packet = new StringBuilder();
		String line;
		URL url;
		BufferedReader br;
		URLConnection openconn;
		try {
			System.out.println(num);
				url = new URL("http://bit365.es/game/"+num);
				openconn=url.openConnection();
				openconn.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
				br = new BufferedReader(new InputStreamReader(openconn.getInputStream(), "UTF-8"));
				while ((line = br.readLine()) !="null") {
		    		packet.append(line);
		    		if(line.equals("</html>")) break;
		    }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

    public GameData bitsquare(int num) {
        StringBuilder packet = new StringBuilder();
        String line;
        URL url;
        BufferedReader br;
        URLConnection openconn;
        try {
            url = new URL("http://bit365.es/game/"+num);
            openconn=url.openConnection();
            openconn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
            br = new BufferedReader(new InputStreamReader(openconn.getInputStream(), "UTF-8"));
            while ((line = br.readLine()) !="null") {
                packet.append(line);
                if(line.equals("</html>")) break;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
        return ParseData(packet.toString(),num);
    }

    public GameData ParseData(String data, int num) {
        String gamenum,gameper,gametime,temp;
        String player,batmoney,per,bonus,profit;
        ArrayList<PlayerData> ar=new ArrayList<>();
        GameData gd=null;

        try {
            data=data.substring(data.indexOf("���� #"));
            gamenum=data.substring(data.indexOf(">")+1,data.indexOf("</")).trim();

            data=data.substring(data.indexOf("</b>")+3);
            gameper=data.substring(data.indexOf(">")+1,data.indexOf("x")).trim();

            data=data.substring(data.indexOf("</b>")+3);
            gametime=data.substring(data.indexOf(">")+1,data.indexOf("<")).trim();

            data=data.substring(data.indexOf("<tbody>")+7)
                    .replaceAll("<tr class=\"lose\">", "<tr>")
                    .replaceAll("��","")
                    .replaceAll(",", "");


            while(data.indexOf("<tr>")!=-1) {
                temp=data.substring(0,data.indexOf("</tr>")+5);
                data=data.substring(data.indexOf("</tr>")+5);

                player=temp.substring(temp.indexOf("\">")+2,temp.indexOf("</"));
                temp=temp.substring(temp.indexOf("</td>")+5);

                batmoney=temp.substring(temp.indexOf(">")+1,temp.indexOf("</")).trim();
                temp=temp.substring(temp.indexOf("</td>")+5);

                per=temp.substring(temp.indexOf(">")+1,temp.indexOf("</")).replace("x","");
                temp=temp.substring(temp.indexOf("</td>")+5);

                bonus=temp.substring(temp.indexOf(">")+1,temp.indexOf("</"));
                temp=temp.substring(temp.indexOf("</td>")+5);

                profit=temp.substring(temp.indexOf(">")+1,temp.indexOf("."));
                temp=temp.substring(temp.indexOf("</td>")+5);
                ar.add(new PlayerData(player, batmoney, per, bonus, profit));/*
				System.out.println("�÷��̾� : "+player);
				System.out.println("���þ� : "+batmoney);
				System.out.println("��� : "+per);
				System.out.println("���ʽ� : "+bonus);
				System.out.println("���� : "+profit);
				System.out.println("======================================");*/
            }
            if(ar.size()!=0) {
                gd=new GameData(gamenum, gameper, gametime, ar);
                count++;
                System.out.println("Parsing Data Complete : "+gamenum);
            }
            else {
                System.out.println("Parsing Player ERROR : "+num);
                plcnt++;
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Parsing Data ERROR : "+num+"           "+data);
            errorcnt++;
        }

        return gd;
    }
}
