package de.rgzm.alligator.run;

import de.rgzm.alligator.classes.AlligatorEvent;
import de.rgzm.alligator.functions.Alligator;
import de.rgzm.alligator.log.Logging;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        try {
            // init Alligator
            Alligator alligator = new Alligator();
            // read
            File fileDir = new File("roman.tsv");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
            String str;
            List inputfile = new ArrayList();
            while ((str = in.readLine()) != null) {
                inputfile.add(str);
            }
            in.close();
            alligator.writeToAlligatorEventList(inputfile);
            System.out.println("--fixed events");
            for (Object a : alligator.getFixedEvents()) {
                AlligatorEvent tmp = (AlligatorEvent) a;
                System.out.println(tmp.toString());
            }
            System.out.println("--fuzzy events");
            for (Object a : alligator.getFuzzyEvents()) {
                AlligatorEvent tmp = (AlligatorEvent) a;
                System.out.println(tmp.toString());
            }
            alligator.calculateDistancesAndAngles();
            System.out.println("minDist:" + alligator.minDistance);
            System.out.println("maxDist:" + alligator.maxDistance);
            // validate
            AlligatorEvent a = alligator.getEventByName("Nijmegen-Kops Plateau");
            AlligatorEvent b = alligator.getEventByName("Pompeii-Hoard");
            System.out.println("Nijmegen-Kops Plateau - Pompeii-Hoard distance:" + a.distances.get(b.id) + " normDist: " + a.distancesNormalised.get(b.id) + "[/100] angle:" + a.angels.get(b.id) + "°");
            // write
            File file = new File("main.txt");
            String path = file.getCanonicalPath();
            File filePath = new File(path);
            filePath.delete();
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
            // list with ids and names
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t").append(alligator.getEventById(id).name).append("\r\n");
            }
            out.append("\r\n").append("-- distances [" + alligator.minDistance + ";" + alligator.maxDistance + "]").append("\r\n").append("\r\n");
            out.append("            ").append("\t");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
            }
            out.append("\r\n");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
                AlligatorEvent thisEvent = alligator.getEventById(id);
                HashMap dm = thisEvent.distances;
                for (String id2 : alligator.eventIDs) {
                    //out.append(id2).append(":").append(String.valueOf(dm.get(id2))).append("\t");
                    DecimalFormat df = new DecimalFormat("00.000000000");
                    out.append(String.valueOf(df.format(dm.get(id2)))).append("\t");
                }
                out.append("\r\n");
            }
            out.append("\r\n").append("-- normalised distances [" + alligator.minDistanceNorm + ";" + alligator.maxDistanceNorm + "]").append("\r\n").append("\r\n");
            out.append("            ").append("\t");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
            }
            out.append("\r\n");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
                AlligatorEvent thisEvent = alligator.getEventById(id);
                HashMap dm = thisEvent.distancesNormalised;
                for (String id2 : alligator.eventIDs) {
                    DecimalFormat df = new DecimalFormat("#00.000000000");
                    out.append(String.valueOf(df.format(dm.get(id2)))).append("\t");
                }
                out.append("\r\n");
            }

            out.flush();
        } catch (Exception e) {
            System.out.println(Logging.getMessageJSON(e, "de.rgzm.alligator.run.Main"));
        }
    }

}
