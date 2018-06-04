package de.rgzm.alligator.functions;

import de.rgzm.alligator.classes.AlligatorEvent;
import de.rgzm.alligator.log.Logging;
import de.rgzm.alligator.allen.AllenInttervalAlgebra;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public List<String> eventIDs = new ArrayList();
    double yearCoefficientBeginn = 1.0;
    double yearCoefficientEnd = 1.0;
    public List<String> allenRelationList = new ArrayList();

    public boolean writeToAlligatorEventList(List inputLines, Double startFixedValue, Double endFixedValue) {
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
                ae.id = getHASHIDParams(6);
                ae.name = linesplit[0];
                ae.x = Double.parseDouble(linesplit[1]);
                ae.y = Double.parseDouble(linesplit[2]);
                ae.z = Double.parseDouble(linesplit[3]);
                ae.a = Double.parseDouble(linesplit[4]);
                ae.b = Double.parseDouble(linesplit[5]);
                ae.schwebend = linesplit[6];
                // check if beginn or end is fixed and populate id lists
                if (startFixedValue == null && endFixedValue == null) {
                    if (ae.schwebend.equals("schwebend")) {
                        ae.startFixed = false;
                        ae.endFixed = false;
                    } else {
                        ae.startFixed = true;
                        ae.endFixed = true;
                    }
                } else {
                    if (ae.a == startFixedValue) {
                        ae.startFixed = false;
                    } else {
                        ae.startFixed = true;
                    }
                    if (ae.b == endFixedValue) {
                        ae.endFixed = false;
                    } else {
                        ae.endFixed = true;
                    }
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

    public void calculateDistances() {
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            HashMap distances = new HashMap();
            for (Object event2 : events) {
                AlligatorEvent loopEvent = (AlligatorEvent) event2;
                distances.put(loopEvent.id, distance3D(thisEvent.x, thisEvent.y, thisEvent.z, loopEvent.x, loopEvent.y, loopEvent.z));
            }
            thisEvent.distances = distances;
        }
    }

    public void getNextFixedNeighbours() {
        // begin
        System.out.println("\r\n===== Next Neighbours for fuzzy begin =====");
        int i = 1;
        for (Object event : events_fuzzy_beginn) {
            String NFBN_ID = null;
            double NFBN_DIST = 200.0;
            int NFBN_SIGN = 0;
            String fuzzyBeginnEventID = (String) event;
            for (Object event2 : events_fixed_beginn) {
                String fixedBeginnEventID = (String) event2;
                AlligatorEvent ae = getEventById(fuzzyBeginnEventID);
                HashMap dn = ae.distances;
                String NFBN_ID_THIS = fixedBeginnEventID;
                double NFBN_DIST_THIS = (double) dn.get(fixedBeginnEventID);
                if (NFBN_DIST_THIS < NFBN_DIST) {
                    NFBN_DIST = NFBN_DIST_THIS;
                    NFBN_ID = NFBN_ID_THIS;
                }
            }
            JSONObject NFBN_JSON_KV = new JSONObject();
            NFBN_JSON_KV.put(NFBN_ID, NFBN_DIST);
            double virtualAnno = getEventById(NFBN_ID).a;
            getEventById(fuzzyBeginnEventID).nextFixedStartNeighbour = NFBN_JSON_KV;
            getEventById(fuzzyBeginnEventID).a = virtualAnno;
            AlligatorEvent ae = getEventById(fuzzyBeginnEventID);
            ae.nn_start_name = getEventById(NFBN_ID).name;
            ae.nn_start_id = getEventById(NFBN_ID).id;
            System.out.println(getEventById(fuzzyBeginnEventID).name + " --> " + getEventById(NFBN_ID).name + " " + NFBN_DIST + " a: " + getEventById(NFBN_ID).a + " sign: " + NFBN_SIGN + " vAnno: " + virtualAnno);
        }
        // end
        System.out.println("\r\n===== Next Neighbours for fuzzy end =====");
        int j = 1;
        for (Object event : events_fuzzy_end) {
            String NFEN_ID = null;
            double NFEN_DIST = 200.0;
            int NFEN_SIGN = 0;
            String fuzzyEndEventID = (String) event;
            for (Object event2 : events_fixed_end) {
                String fixedEndEventID = (String) event2;
                AlligatorEvent ae = getEventById(fuzzyEndEventID);
                HashMap dn = ae.distances;
                String NFEN_ID_THIS = fixedEndEventID;
                double NFEN_DIST_THIS = (double) dn.get(fixedEndEventID);
                if (NFEN_DIST_THIS < NFEN_DIST) {
                    NFEN_DIST = NFEN_DIST_THIS;
                    NFEN_ID = NFEN_ID_THIS;
                }
            }
            JSONObject NFEN_JSON_KV = new JSONObject();
            NFEN_JSON_KV.put(NFEN_ID, NFEN_DIST);
            double virtualAnno = getEventById(NFEN_ID).b;
            getEventById(fuzzyEndEventID).nextFixedEndNeighbour = NFEN_JSON_KV;
            getEventById(fuzzyEndEventID).b = virtualAnno;
            AlligatorEvent ae = getEventById(fuzzyEndEventID);
            ae.nn_end_name = getEventById(NFEN_ID).name;
            ae.nn_end_id = getEventById(NFEN_ID).id;
            System.out.println(getEventById(fuzzyEndEventID).name + " --> " + getEventById(NFEN_ID).name + " " + NFEN_DIST + " b: " + getEventById(NFEN_ID).b + " sign: " + NFEN_SIGN + " vAnno: " + virtualAnno);
        }
    }

    public void calculateAllenSigns() {
        System.out.println("\r\n===== Allen Relations =====");
        for (Object event : events) {
            AlligatorEvent thisEvent = (AlligatorEvent) event;
            HashMap allenRelations = new HashMap();
            for (Object event2 : events) {
                AlligatorEvent loopEvent = (AlligatorEvent) event2;
                System.out.println(thisEvent.name + " " + AllenInttervalAlgebra.getAllenRelationSigns(thisEvent.a, thisEvent.b, loopEvent.a, loopEvent.b) + " " + loopEvent.name);
                if (AllenInttervalAlgebra.getAllenRelationSigns(thisEvent.a, thisEvent.b, loopEvent.a, loopEvent.b).size() > 0) {
                    allenRelations.put(loopEvent.id, AllenInttervalAlgebra.getAllenRelationSigns(thisEvent.a, thisEvent.b, loopEvent.a, loopEvent.b).get(0));
                    String p = AllenInttervalAlgebra.getAllenRelationProperties(AllenInttervalAlgebra.getAllenRelationSigns(thisEvent.a, thisEvent.b, loopEvent.a, loopEvent.b).get(0));
                    if (thisEvent.id != loopEvent.id) {
                        allenRelationList.add("ae:" + thisEvent.id + " " + p + " ae:" + loopEvent.id + " .\r\n");
                    }
                }
            }
            thisEvent.allenRelations = allenRelations;
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

    private static String getHASHIDParams(int length) {
        UUID newUUID = UUID.randomUUID();
        Hashids hashids = new Hashids(newUUID.toString(), length);
        String hash = hashids.encode(1234567L);
        char ch = hash.charAt(0);
        if (Character.isDigit(ch)) {
            return getHASHIDParams(length);
        } else {
            return hash;
        }
    }

}
