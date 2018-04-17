package de.rgzm.alligator.run;

import de.rgzm.alligator.classes.AlligatorEvent;
import de.rgzm.alligator.functions.Alligator;
import de.rgzm.alligator.log.Logging;
import de.rgzm.alligator.allen.AllenIA;
import de.rgzm.alligator.amt.AMT;
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

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        try {
            // init Alligator
            Alligator alligator = new Alligator();
            // read
            File fileDir = new File("../data/roman.tsv");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
            String str;
            List inputfile = new ArrayList();
            while ((str = in.readLine()) != null) {
                inputfile.add(str);
            }
            in.close();
            alligator.writeToAlligatorEventList(inputfile);
            alligator.calculateDistancesAndAngles();
            System.out.println("eventIDs:" + alligator.eventIDs.size());
            System.out.println("events_fixed_beginn:" + alligator.events_fixed_beginn.size());
            System.out.println("events_fuzzy_beginn:" + alligator.events_fuzzy_beginn.size());
            System.out.println("events_fixed_end:" + alligator.events_fixed_end.size());
            System.out.println("events_fuzzy_end:" + alligator.events_fuzzy_end.size());
            System.out.println("minDist:" + alligator.minDistance);
            System.out.println("maxDist:" + alligator.maxDistance);
            System.out.println("minDistNorm:" + alligator.minDistanceNorm);
            System.out.println("maxDistNorm:" + alligator.maxDistanceNorm);
            /*System.out.println("minAlpha:" + alligator.minAlpha);
            System.out.println("maxAlpha:" + alligator.maxAlpha);
            System.out.println("minAlphaNorm:" + alligator.minAlphaNorm);
            System.out.println("maxAlphaNorm:" + alligator.maxAlphaNorm);*/
            alligator.getNextFixedNeighbours();
            // validate
            AlligatorEvent a = alligator.getEventByName("Nijmegen-Kops Plateau");
            AlligatorEvent b = alligator.getEventByName("Pompeii-Hoard");
            //System.out.println("Nijmegen-Kops Plateau - Pompeii-Hoard distance:" + a.distances.get(b.id) + " normDist: " + a.distancesNormalised.get(b.id) + "[/100] angle:" + a.angels.get(b.id) + "°");
            System.out.println("Nijmegen-Kops Plateau - Pompeii-Hoard distance:" + a.distances.get(b.id) + " normDist: " + a.distancesNormalised.get(b.id) + "[/100]");
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
            /*out.append("\r\n").append("-- angles [" + alligator.minAlpha + ";" + alligator.maxAlpha + "]").append("\r\n").append("\r\n");
            out.append("            ").append("\t");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
            }
            out.append("\r\n");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
                AlligatorEvent thisEvent = alligator.getEventById(id);
                HashMap dm = thisEvent.angels;
                for (String id2 : alligator.eventIDs) {
                    DecimalFormat df = new DecimalFormat("#00.000000000");
                    out.append(String.valueOf(df.format(dm.get(id2)))).append("\t");
                }
                out.append("\r\n");
            }
            out.append("\r\n").append("-- normalised angles [" + alligator.minAlphaNorm + ";" + alligator.maxAlphaNorm + "]").append("\r\n").append("\r\n");
            out.append("            ").append("\t");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
            }
            out.append("\r\n");
            for (String id : alligator.eventIDs) {
                out.append(id).append("\t");
                AlligatorEvent thisEvent = alligator.getEventById(id);
                HashMap dm = thisEvent.angelsNormalised;
                for (String id2 : alligator.eventIDs) {
                    DecimalFormat df = new DecimalFormat("#00.000000000");
                    out.append(String.valueOf(df.format(dm.get(id2)))).append("\t");
                }
                out.append("\r\n");
            }*/
            // Allen Tests
            AlligatorEvent t1 = alligator.getEventByName("Nijmegen-Kops Plateau");
            AlligatorEvent t2 = alligator.getEventByName("Pompeii-Hoard");
            System.out.println(t1.name + " [" + t1.a + ";" + t1.b + "]");
            System.out.println(t2.name + " [" + t2.a + ";" + t2.b + "]");
            System.out.println(t1.name + " " + AllenIA.getAllenRelationShortDescriptions(t1.a, t1.b, t2.a, t2.b) + " " + t2.name);
            System.out.println(t1.name + " " + AllenIA.getAllenRelationSigns(t1.a, t1.b, t2.a, t2.b) + " " + t2.name);
            System.out.println(t2.name + " " + AllenIA.getAllenRelationShortDescriptions(t2.a, t2.b, t1.a, t1.b) + " " + t1.name);
            System.out.println(t2.name + " " + AllenIA.getAllenRelationSigns(t2.a, t2.b, t1.a, t1.b) + " " + t1.name);
            // output events
            out.append("\r\n");
            for (Object event : alligator.events) {
                AlligatorEvent ae = (AlligatorEvent) event;
                out.append(ae.name).append("\t").append(String.valueOf(ae.a)).append("\t").append(String.valueOf(ae.b)).append("\r\n");
            }
            // NEO4J tests
            String nodes = alligator.getEventsAsCypherNodes();
            List<String> properties = AllenIA.getAllenRelationCypherProperties(t1.a, t1.b, t2.a, t2.b, t1, t2);
            String listString = "";
            for (String s : properties) {
                listString += s + "\r\n";
            }
            String ret = alligator.getEventsAsCypherReturn();
            //out.append("\r\n").append("-- cypher").append("\r\n\r\n").append(nodes + listString + ret).append("\r\n");
            // more Allen
            alligator.calculateAllenSigns();
            // write output
            out.flush();

            // AMT test
            AMT amt = new AMT("http://ls-dev.i3mainz.hs-mainz.de/rdf4j-server/repositories/amtcaa2018");
            System.out.println(amt.GRAPH.toJSONString());
            amt.loadGraph();
        } catch (Exception e) {
            System.out.println(Logging.getMessageJSON(e, "de.rgzm.alligator.run.Main"));
        }
    }

}
