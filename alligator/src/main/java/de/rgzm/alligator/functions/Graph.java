package de.rgzm.alligator.functions;

import de.rgzm.alligator.classes.AlligatorEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Graph {

    public static void writeGraph(String filename, Alligator alligator) {
        JSONArray nodejson = new JSONArray();
        for (Object event : alligator.events) {
            JSONObject t = new JSONObject();
            AlligatorEvent ae = (AlligatorEvent) event;
            t.put("id", ae.id);
            t.put("label", ae.name);
            nodejson.add(t);
        }
        JSONArray edgejson = new JSONArray();
        for (String id : alligator.eventIDs) {
            AlligatorEvent thisEvent = alligator.getEventById(id);
            HashMap dm = thisEvent.allenRelations;
            for (String id2 : alligator.eventIDs) {
                JSONObject t = new JSONObject();
                t.put("from", thisEvent.id);
                t.put("to", id2);
                t.put("label", String.valueOf(dm.get(id2)));
                if (String.valueOf(dm.get(id2)) != "null") {
                    edgejson.add(t);
                }
            }
        }
        JSONObject output = new JSONObject();
        output.put("nodes", nodejson);
        output.put("edges", edgejson);
        try {
            File fileDir2 = new File("../graph/" + filename);
            Writer out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir2), "UTF8"));
            out2.append(output.toJSONString());
            out2.flush();
            out2.close();
        } catch (IOException e) {
            e.toString();
        }
    }

}
