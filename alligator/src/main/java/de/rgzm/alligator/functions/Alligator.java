package de.rgzm.alligator.functions;

import de.rgzm.alligator.classes.AlligatorEvent;
import de.rgzm.alligator.log.Logging;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.hashids.Hashids;

public class Alligator {

    private List<AlligatorEvent> events = new ArrayList();
    private List<AlligatorEvent> events_fixed = new ArrayList();
    private List<AlligatorEvent> events_fuzzy = new ArrayList();
    public double minDistance = 1000000.0;
    public double maxDistance = -1000000.0;
    public double minDistanceNorm = 1000000.0;
    public double maxDistanceNorm = -1000000.0;
    public List<String> eventIDs = new ArrayList();

    public Alligator() {

    }

    public List<AlligatorEvent> getFixedEvents() {
        return events_fixed;
    }

    public List<AlligatorEvent> getFuzzyEvents() {
        return events_fuzzy;
    }

    public List<AlligatorEvent> getEvents() {
        return events;
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
                ae.y = Double.parseDouble(linesplit[2]);
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
                events.add(ae);
                eventIDs.add(ae.id);
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

    public void calculateDistancesAndAngles() {
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            HashMap distances = new HashMap();
            HashMap angels = new HashMap();
            for (Object event2 : events) {
                AlligatorEvent loopEvent = (AlligatorEvent) event2;
                distances.put(loopEvent.id, distance3D(thisEvent.x, thisEvent.y, thisEvent.z, loopEvent.x, loopEvent.y, loopEvent.z));
                angels.put(loopEvent.id, angle3D(thisEvent.x, thisEvent.y, thisEvent.z, loopEvent.x, loopEvent.y, loopEvent.z));
            }
            // set distances (origin) and angels in degree
            thisEvent.distances = distances;
            thisEvent.angels = angels;
        }
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            HashMap distancesNormalised = new HashMap();
            HashMap distances = thisEvent.distances;
            Iterator iter = distances.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry mEntry = (Map.Entry) iter.next();
                String key = (String) mEntry.getKey();
                double value = (double) mEntry.getValue();
                double abstand = (maxDistance - minDistance);
                double norm = Math.abs((value - minDistance) / abstand);
                norm = norm * 100;
                distancesNormalised.put(key, norm);
                if (norm < minDistanceNorm && norm > 0.0) {
                    minDistanceNorm = norm;
                }
                if (norm > maxDistanceNorm) {
                    maxDistanceNorm = norm;
                }
            }
            thisEvent.distancesNormalised = distancesNormalised;
        }
    }

    public AlligatorEvent getEventByName(String name) {
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            if (thisEvent.name.equals(name)) {
                return thisEvent;
            }
        }
        return null;
    }

    public AlligatorEvent getEventById(String id) {
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            if (thisEvent.id.equals(id)) {
                return thisEvent;
            }
        }
        return null;
    }

    private double distance3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        double a = Math.pow((x2 - x1), 2);
        double b = Math.pow((y2 - y1), 2);
        double c = Math.pow((z2 - z1), 2);
        double dist = Math.sqrt(a + b + c);
        if (dist < minDistance && dist > 0.0) {
            minDistance = dist;
        }
        if (dist > maxDistance) {
            maxDistance = dist;
        }
        return dist;
    }

    private double angle3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        double angle;
        // Skalarprodukt a * b
        double zaehler = (x1 * x2) + (y1 * y2) + (z1 * z2);
        // |a|*|b|
        double nenner = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2) + Math.pow(z1, 2)) * Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2) + Math.pow(z2, 2));
        // cosinus(alpha)
        angle = zaehler / nenner;
        // alpha[rad]
        angle = Math.acos(angle);
        // alpha [degree]
        angle = Math.toDegrees(angle);
        return angle;
    }

    private static String getHASHIDParams(int length) {
        UUID newUUID = UUID.randomUUID();
        Hashids hashids = new Hashids(newUUID.toString(), length);
        String hash = hashids.encode(1234567L);
        return hash;
    }

}
