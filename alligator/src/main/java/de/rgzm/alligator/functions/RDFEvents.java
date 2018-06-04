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
        // prefixes
        rdfString.append("@prefix alligator: <http://rgzm.github.io/alligator/ontology#> .\r\n");
        rdfString.append("@prefix ae: <http://example.net/event#> .\r\n");
        rdfString.append("@prefix time: <http://www.w3.org/2006/time#> .\r\n");
        rdfString.append("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\r\n");
        rdfString.append("@prefix dc: <http://purl.org/dc/elements/1.1/> .\r\n");
        rdfString.append("\r\n");
        // metadata
        for (Object event : alligator.events) {
            AlligatorEvent ae = (AlligatorEvent) event;
            String aeSubject = "ae:" + ae.id;
            rdfString.append(aeSubject).append(" a alligator:Event").append(" .\r\n");
            rdfString.append(aeSubject).append(" a time:Interval").append(" .\r\n");
            rdfString.append(aeSubject).append(" dc:identifier \"").append(ae.id).append("\" .\r\n");
            rdfString.append(aeSubject).append(" rdfs:label \"").append(ae.name).append("\" .\r\n");
            rdfString.append(aeSubject).append(" alligator:estimatedStart \"").append(ae.a).append("\" .\r\n");
            rdfString.append(aeSubject).append(" alligator:estimatedEnd \"").append(ae.b).append("\" .\r\n");
            rdfString.append(aeSubject).append(" alligator:CAx \"").append(ae.x).append("\" .\r\n");
            rdfString.append(aeSubject).append(" alligator:CAy \"").append(ae.y).append("\" .\r\n");
            rdfString.append(aeSubject).append(" alligator:CAz \"").append(ae.z).append("\" .\r\n");
            rdfString.append(aeSubject).append(" alligator:startFixed \"").append(ae.startFixed).append("\" .\r\n");
            rdfString.append(aeSubject).append(" alligator:endFixed \"").append(ae.endFixed).append("\" .\r\n");
            if (ae.nn_start_id != null) {
                rdfString.append(aeSubject).append(" alligator:nfsn ").append("ae:").append(ae.nn_start_id).append(" .\r\n");
            }
            if (ae.nn_end_id != null) {
                rdfString.append(aeSubject).append(" alligator:nfen ").append("ae:").append(ae.nn_end_id).append(" .\r\n");
            }
            rdfString.append("\r\n");
        }
        // allen relations
        for (String item : alligator.allenRelationList) {
            rdfString.append(item.replace("http://www.w3.org/2006/time#", "time:"));
        }
        // write output
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
