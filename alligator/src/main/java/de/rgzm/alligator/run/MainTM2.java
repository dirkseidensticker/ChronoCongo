package de.rgzm.alligator.run;

import de.rgzm.alligator.classes.AlligatorEvent;
import de.rgzm.alligator.functions.Alligator;
import de.rgzm.alligator.log.Logging;
import de.rgzm.alligator.allen.AllenIA;
import de.rgzm.alligator.amt.AMT;
import de.rgzm.alligator.functions.Graph;
import de.rgzm.alligator.functions.Timeline;
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
import java.util.List;

public class MainTM2 {

    public static void main(String[] args) throws IOException, SQLException {
        try {
            // init Alligator
            Alligator alligator = new Alligator();
            // read file
            File fileDir = new File("../data/roman2.tsv");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
            String str;
            List inputfile = new ArrayList();
            while ((str = in.readLine()) != null) {
                inputfile.add(str);
            }
            in.close();
            // create alligator events
            alligator.writeToAlligatorEventList(inputfile, null, null);
            // calculate distances
            alligator.calculateDistances();
            // log metadata
            System.out.println("eventIDs:" + alligator.eventIDs.size());
            System.out.print("events_fixed_beginn:" + alligator.events_fixed_beginn.size());
            System.out.println(" events_fuzzy_beginn:" + alligator.events_fuzzy_beginn.size());
            System.out.print("events_fixed_end:" + alligator.events_fixed_end.size());
            System.out.println(" events_fuzzy_end:" + alligator.events_fuzzy_end.size());
            System.out.print("minDist:" + alligator.minDistance);
            System.out.println(" maxDist:" + alligator.maxDistance);
            // calculate next fixed neighbours
            alligator.getNextFixedNeighbours();
            // output virtual years
            System.out.println("\r\n===== virtual years =====");
            for (Object event : alligator.events) {
                AlligatorEvent ae = (AlligatorEvent) event;
                System.out.println(ae.name + "\t" + String.valueOf(ae.a) + "\t" + String.valueOf(ae.b) + " " + ae.startFixed + " " + ae.endFixed);
            }
            // write timeline json
            Timeline.writeTimeline("output_TM2.json", alligator);
            // allen
            alligator.calculateAllenSigns();
            Graph.writeGraph("nodesedges_TM2.json", alligator);

            // write distance matrix
            File file = new File("mainTM2_distanceMatrix.txt");
            String path = file.getCanonicalPath();
            File filePath = new File(path);
            filePath.delete();
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
            // list with ids and names
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t").append(alligator.getEventById(id).name).append("\r\n");
            }
            out.append("\r\n").append("-- distances [" + alligator.minDistance + ";" + alligator.maxDistance + "]").append("\r\n").append("\r\n");
            out.append("      ").append("\t");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
            }
            out.append("\r\n");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
                AlligatorEvent thisEvent = alligator.getEventById(id);
                HashMap dm = thisEvent.distances;
                for (String id2 : alligator.eventIDs) {
                    DecimalFormat df = new DecimalFormat("0.0000");
                    out.append(String.valueOf(df.format(dm.get(id2)))).append("\t");
                }
                out.append("\r\n");
            }
            out.append("\r\n");
            out.flush();
            out.close();

            // write allen matrix
            file = new File("mainTM2_allenMatrix.txt");
            path = file.getCanonicalPath();
            filePath = new File(path);
            filePath.delete();
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
            // list with ids and names
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t").append(alligator.getEventById(id).name).append("\r\n");
            }
            out.append("\r\n").append("-- allen relations").append("\r\n").append("\r\n");
            out.append("      ").append("\t");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
            }
            out.append("\r\n");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
                AlligatorEvent thisEvent = alligator.getEventById(id);
                HashMap dm = thisEvent.allenRelations;
                for (String id2 : alligator.eventIDs) {
                    out.append(String.valueOf(dm.get(id2)));
                    if (String.valueOf(dm.get(id2)) == "null") {
                        out.append("\t");
                    } else {
                        out.append("\t").append("\t");
                    }
                }
                out.append("\r\n");
            }
            out.append("\r\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(Logging.getMessageJSON(e, "de.rgzm.alligator.run.Main"));
        }
    }

}
