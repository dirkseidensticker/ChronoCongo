package de.rgzm.alligator.functions;

import de.rgzm.alligator.classes.AlligatorEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class RDFEvents {

    public static void writeRDF(String filename, Alligator alligator) {
        StringBuilder rdfString = new StringBuilder();
        rdfString.append("@prefix alligator: <http://alligator.rgzm.de/> .\r\n");
        rdfString.append("@prefix time: <http://www.w3.org/2006/time#> .\r\n");
        rdfString.append("@prefix rdfs: http://www.w3.org/2000/01/rdf-schema#> .\r\n");
        rdfString.append("@prefix dc: <http://purl.org/dc/elements/1.1/> .\r\n\r\n");
        for (Object event : alligator.events) {
            AlligatorEvent ae = (AlligatorEvent) event;
            String aeSubject = "alligator:" + ae.id;
            rdfString.append(aeSubject + " a " + "time:Interval " + " .\r\n");
            rdfString.append(aeSubject + " dc:identifier " + "\"" + ae.id + "\"" + " .\r\n");
            rdfString.append(aeSubject + " rdfs:label " + "\"" + ae.name + "\"" + " .\r\n");
        }
        for (String item : alligator.allenRelationList) {
            rdfString.append(item.replace("http://www.w3.org/2006/time#", "time:"));
        }
        try {
            File fileDir2 = new File("../rdf/" + filename);
            try (Writer out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir2), "UTF8"))) {
                out2.append(rdfString.toString());
                out2.flush();
            }
        } catch (IOException e) {
            e.toString();
        }
    }

}
