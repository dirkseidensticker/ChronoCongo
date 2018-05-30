package de.rgzm.alligator.run;

import de.rgzm.alligator.log.Logging;
import de.rgzm.alligator.allen.AllenIA;
import java.io.IOException;
import java.sql.SQLException;

public class AllenTest {

    public static void main(String[] args) throws IOException, SQLException {
        try {
            System.out.println(AllenIA.getAllenRelationSigns(200, 400, 500, 600) + " <");
            System.out.println(AllenIA.getAllenRelationSigns(700, 800, 500, 600) + " >");
            System.out.println(AllenIA.getAllenRelationSigns(400, 500, 500, 600) + " m");
            System.out.println(AllenIA.getAllenRelationSigns(600, 700, 500, 600) + " mi");
            System.out.println(AllenIA.getAllenRelationSigns(500, 600, 500, 600) + " =");
            System.out.println(AllenIA.getAllenRelationSigns(450, 550, 500, 600) + " o");
            System.out.println(AllenIA.getAllenRelationSigns(550, 650, 500, 600) + " oi");
            System.out.println(AllenIA.getAllenRelationSigns(525, 575, 500, 600) + " d");
            System.out.println(AllenIA.getAllenRelationSigns(450, 650, 500, 600) + " di");
            System.out.println(AllenIA.getAllenRelationSigns(500, 550, 500, 600) + " s");
            System.out.println(AllenIA.getAllenRelationSigns(500, 650, 500, 600) + " si");
            System.out.println(AllenIA.getAllenRelationSigns(550, 600, 500, 600) + " f");
            System.out.println(AllenIA.getAllenRelationSigns(450, 600, 500, 600) + " fi");
        } catch (Exception e) {
            System.out.println(Logging.getMessageJSON(e, "de.rgzm.alligator.run.Main"));
        }
    }

}
