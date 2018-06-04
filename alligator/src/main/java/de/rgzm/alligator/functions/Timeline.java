package de.rgzm.alligator.functions;

import de.rgzm.alligator.classes.AlligatorEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Timeline {

    public static void writeTimeline(String filename, Alligator alligator) {
        int id_t = 0;
        JSONArray timelinejson = new JSONArray();
        for (Object event : alligator.events) {
            id_t++;
            JSONObject t = new JSONObject();
            AlligatorEvent ae = (AlligatorEvent) event;
            t.put("id", ae.id);
            if (ae.nn_start_name != null && ae.nn_end_name != null) {
                t.put("content", ae.name + "-->" + ae.nn_start_name + "," + ae.nn_end_name);
            } else if (ae.nn_start_name == null && ae.nn_end_name != null) {
                t.put("content", ae.name + "-->" + "*" + "," + ae.nn_end_name);
            } else if (ae.nn_start_name != null && ae.nn_end_name == null) {
                t.put("content", ae.name + "-->" + ae.nn_start_name + "," + "*");
            } else {
                t.put("content", ae.name);
            }
            boolean error = false;
            if (ae.b < ae.a) {
                error = true;
                double atmp = ae.a;
                double btmp = ae.b;
                ae.a = btmp;
                ae.b = atmp;
            }
            if (!ae.startFixed && !ae.endFixed && !error) {
                t.put("className", "orange");
            } else if (!ae.startFixed && ae.endFixed && !error) {
                t.put("className", "orange");
            } else if (ae.startFixed && !ae.endFixed && !error) {
                t.put("className", "orange");
            } else if (error) {
                t.put("className", "red");
            } else if (!error) {
                t.put("className", "blue");
            }
            t.put("start", ae.a);
            t.put("end", ae.b);
            if (ae.a == ae.b) {
                t.put("type", "point");
            }
            t.put("nn_start", ae.nn_start_name);
            t.put("nn_end", ae.nn_end_name);
            timelinejson.add(t);
        }
        try {
            File fileDir2 = new File("../timeline/" + filename);
            Writer out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir2), "UTF8"));
            out2.append(timelinejson.toJSONString());
            out2.flush();
            out2.close();
        } catch (IOException e) {
            e.toString();
        }
    }

}
