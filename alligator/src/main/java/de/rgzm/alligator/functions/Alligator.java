package de.rgzm.alligator.functions;

import de.rgzm.alligator.classes.AlligatorEvent;
import de.rgzm.alligator.log.Logging;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hashids.Hashids;

public class Alligator {
    
    private List<AlligatorEvent> events_fixed = new ArrayList();
    private List<AlligatorEvent> events_fuzzy = new ArrayList();
    
    public Alligator() {
        
    }
    
    public List<AlligatorEvent> getFixedEvents() {
        return events_fixed;
    }
    
    public List<AlligatorEvent> getFuzzyEvents() {
        return events_fuzzy;
    }
    
    public boolean writeToAlligatorEventList(List inputLines) {
        try {
            String header = (String) inputLines.get(0);
            String[] headersplit = header.split("\t");
            if (headersplit.length != 7) {
                return false;
            }
            inputLines.remove(0);
            for (Object line : inputLines) {
                String tmp = (String) line;
                String[] linesplit = tmp.split("\t");
                // populate AlligatorEvent
                AlligatorEvent ae = new AlligatorEvent();
                ae.id = getHASHIDParams(12);
                ae.name = linesplit[0];
                ae.x = Double.parseDouble(linesplit[1]);
                ae.b = Double.parseDouble(linesplit[2]);
                ae.z = Double.parseDouble(linesplit[3]);
                ae.a = Double.parseDouble(linesplit[4]);
                ae.b = Double.parseDouble(linesplit[5]);
                if (ae.a == 20.0 && ae.b == 130.0) {
                    ae.fixed = false;
                } else {
                    ae.fixed = true;
                }
                if (ae.a == 20.0) {
                    ae.startFixed = false;
                } else {
                    ae.startFixed = true;
                }
                if (ae.b == 130.0) {
                    ae.endFixed = false;
                } else {
                    ae.endFixed = true;
                }
                if (ae.fixed) {
                    events_fixed.add(ae);
                } else {
                    events_fuzzy.add(ae);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(Logging.getMessageJSON(e, "de.rgzm.alligator.run.Main"));
            return false;
        }
    }
    
    private static String getHASHIDParams(int length) {
		UUID newUUID = UUID.randomUUID();
        Hashids hashids = new Hashids(newUUID.toString(), length);
		String hash = hashids.encode(1234567L);
		return hash;
	}
    
}
