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
import java.util.ArrayList;
import java.util.List;

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
            // write
            File file = new File("main.txt");
            String path = file.getCanonicalPath();
            File filePath = new File(path);
            filePath.delete();
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
            out.append("").append("\r\n");
            out.flush();
        } catch (Exception e) {
            System.out.println(Logging.getMessageJSON(e, "de.rgzm.alligator.run.Main"));
        }
    }

}
