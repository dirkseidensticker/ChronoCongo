package de.rgzm.alligator.functions;

import de.rgzm.alligator.classes.AllenObject;
import de.rgzm.alligator.classes.AlligatorEvent;
import de.rgzm.alligator.log.Logging;
import de.rgzm.alligator.allen.AllenIA;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.hashids.Hashids;
import org.json.simple.JSONObject;

public class Alligator {

    public List<AlligatorEvent> events = new ArrayList();
    public List<String> events_fixed = new ArrayList();
    public List<String> events_fixed_beginn = new ArrayList();
    public List<String> events_fixed_end = new ArrayList();
    public List<String> events_fuzzy_beginn = new ArrayList();
    public List<String> events_fuzzy_end = new ArrayList();
    public double minDistance = 1000000.0;
    public double maxDistance = -1000000.0;
    public double minDistanceNorm = 1000000.0;
    public double maxDistanceNorm = -1000000.0;
    //public double minAlpha = 361.0;
    //public double maxAlpha = -1.0;
    //public double minAlphaNorm = 361.0;
    //public double maxAlphaNorm = -1.0;
    public List<String> eventIDs = new ArrayList();

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
                // check if beginn or end is fixed and populate id lists
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
                if (!ae.startFixed) {
                    events_fuzzy_beginn.add(ae.id);
                } else {
                    events_fixed_beginn.add(ae.id);
                }
                if (!ae.endFixed) {
                    events_fuzzy_end.add(ae.id);
                } else {
                    events_fixed_end.add(ae.id);
                }
                // populate lists
                events.add(ae);
                eventIDs.add(ae.id);
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
                //angels.put(loopEvent.id, angle3D(thisEvent.x, thisEvent.y, thisEvent.z, loopEvent.x, loopEvent.y, loopEvent.z));
            }
            // set distances (origin) and angels in degree
            thisEvent.distances = distances;
            //thisEvent.angels = angels;
        }
        // calculate normed distance
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
                if (norm < minDistanceNorm) {
                    minDistanceNorm = norm;
                }
                if (norm > maxDistanceNorm) {
                    maxDistanceNorm = norm;
                }
            }
            thisEvent.distancesNormalised = distancesNormalised;
        }
        /*// calculate normed angle
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            HashMap anglesNormalised = new HashMap();
            HashMap angles = thisEvent.angels;
            Iterator iter = angles.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry mEntry = (Map.Entry) iter.next();
                String key = (String) mEntry.getKey();
                double value = (double) mEntry.getValue();
                double abstand = (maxAlpha - minAlpha);
                double norm = Math.abs((value - minAlpha) / abstand);
                norm = norm * 100;
                anglesNormalised.put(key, norm);
                if (norm < minAlphaNorm) {
                    minAlphaNorm = norm;
                }
                if (norm > maxAlphaNorm) {
                    maxAlphaNorm = norm;
                }
            }
            thisEvent.angelsNormalised = anglesNormalised;
        }*/
    }

    public void getNextFixedNeighbours() {
        // begin
        System.out.println("beginn: ===================================");
        int i = 1;
        for (Object event : events_fuzzy_beginn) {
            String NFBN_ID = null;
            double NFBN_DIST = 200.0;
            String fuzzyBeginnEventID = (String) event;
            for (Object event2 : events_fixed_beginn) {
                String fixedBeginnEventID = (String) event2;
                AlligatorEvent ae = getEventById(fuzzyBeginnEventID);
                HashMap dn = ae.distancesNormalised;
                String NFBN_ID_THIS = fixedBeginnEventID;
                double NFBN_DIST_THIS = (double) dn.get(fixedBeginnEventID);
                if (NFBN_DIST_THIS < NFBN_DIST) {
                    NFBN_DIST = NFBN_DIST_THIS;
                    NFBN_ID = NFBN_ID_THIS;
                }
            }
            JSONObject NFBN_JSON_KV = new JSONObject();
            NFBN_JSON_KV.put(NFBN_ID, NFBN_DIST);
            System.out.println(i++ + ": " + getEventById(fuzzyBeginnEventID).name + " --> " + getEventById(NFBN_ID).name + " " + NFBN_JSON_KV.toJSONString() + " a: " + getEventById(NFBN_ID).a);
            getEventById(fuzzyBeginnEventID).nextFixedEndNeighbour = NFBN_JSON_KV;
        }
        // end
        System.out.println("end: ===================================");
        int j = 1;
        for (Object event : events_fuzzy_end) {
            String NFEN_ID = null;
            double NFEN_DIST = 200.0;
            String fuzzyEndEventID = (String) event;
            for (Object event2 : events_fixed_end) {
                String fixedEndEventID = (String) event2;
                AlligatorEvent ae = getEventById(fuzzyEndEventID);
                HashMap dn = ae.distancesNormalised;
                String NFEN_ID_THIS = fixedEndEventID;
                double NFEN_DIST_THIS = (double) dn.get(fixedEndEventID);
                if (NFEN_DIST_THIS < NFEN_DIST) {
                    NFEN_DIST = NFEN_DIST_THIS;
                    NFEN_ID = NFEN_ID_THIS;
                }
            }
            JSONObject NFEN_JSON_KV = new JSONObject();
            NFEN_JSON_KV.put(NFEN_ID, NFEN_DIST);
            System.out.println(j++ + ": " + getEventById(fuzzyEndEventID).name + " --> " + getEventById(NFEN_ID).name + " " + NFEN_JSON_KV.toJSONString() + " b: " + getEventById(NFEN_ID).b);
            getEventById(fuzzyEndEventID).nextFixedEndNeighbour = NFEN_JSON_KV;
        }
    }

    public void calculateAllenSigns() {
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            List rel = new ArrayList();
            for (Object event2 : events) {
                AlligatorEvent loopEvent = (AlligatorEvent) event2;
                List realRealtions = AllenIA.getAllenRelationSigns(thisEvent.a, thisEvent.b, loopEvent.a, loopEvent.b);
                for (Object item : realRealtions) {
                    String tmp = (String) item;
                    rel.add(new AllenObject(thisEvent.id, loopEvent.id, tmp));
                }
            }
            // set allen relations
            thisEvent.allenRelations = rel;
        }
    }

    public List<AlligatorEvent> getEvents() {
        return events;
    }

    public String getEventsAsCypherNodes() {
        String nodes = "";
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            nodes += "CREATE (" + thisEvent.id + ":Event{label: '" + thisEvent.name + "'})" + "\r\n";
        }
        return nodes;
    }

    public String getEventsAsCypherReturn() {
        String ret = "RETURN ";
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            ret += thisEvent.id + ",";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
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
        if (dist < minDistance) {
            minDistance = dist;
        }
        if (dist > maxDistance) {
            maxDistance = dist;
        }
        return dist;
    }

    /*private double angle3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        double alpha = 0.0;
        // Skalarprodukt a * b
        double zaehler = (x1 * x2) + (y1 * y2) + (z1 * z2);
        // |a|*|b|
        double nenner = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2) + Math.pow(z1, 2)) * Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2) + Math.pow(z2, 2));
        // cosinus(alpha)
        alpha = zaehler / nenner;
        // alpha[rad]
        alpha = Math.acos(alpha);
        // alpha [degree]
        alpha = Math.toDegrees(alpha);
        if (alpha < 0.001 || Double.isNaN(alpha)) {
            alpha = 0.0;
        }
        if (alpha < minAlpha) {
            minAlpha = alpha;
        }
        if (alpha > maxAlpha) {
            maxAlpha = alpha;
        }
        return alpha;
    }*/
    private static String getHASHIDParams(int length) {
        UUID newUUID = UUID.randomUUID();
        Hashids hashids = new Hashids(newUUID.toString(), length);
        String hash = hashids.encode(1234567L);
        char ch = hash.charAt(0);
        if (Character.isDigit(ch)) {
            return getHASHIDParams(12);
        } else {
            return hash;
        }
    }

}
