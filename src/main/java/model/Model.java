package model;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.SaveData;

public class Model {

    public static void go(String url) throws IOException, InvalidFormatException {


        Document doc = Jsoup.connect(url).get();

        Elements tables = doc.select("table");
        for (Element table : tables) {
            Elements trs = table.select("tr");
            String[][] trtd = new String[trs.size()][];

            // thead
            Elements ths = trs.get(0).select("th");
            String[] trth = new String[ths.size()];
            for (int i = 0; i < ths.size(); i++) {
                trth[i] = ths.get(i).text();
            }
            // tbody
            for (int i = 0; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");

                trtd[i] = new String[tds.size()];
                for (int j = 0; j < tds.size(); j++) {
                    trtd[i][j] = tds.get(j).text();
                }
            }

            for (int i = 1; i < trtd.length; i++) {
                if (new File(trtd[i][0] + ".xlsx").exists()) {
                    SaveData.savedata(trtd[i], trtd[i][0] + ".xlsx");
                } else {
                    SaveData.savedata(trth, trtd[i][0] + ".xlsx");
                    SaveData.savedata(trtd[i], trtd[i][0] + ".xlsx");
                }

            }

        }
    }
}