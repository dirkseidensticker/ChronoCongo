package de.rgzm.alligator.classes;

public class AllenObject {

    private String event1ID = null;
    private String event2ID = null;
    private String relation = null;

    public AllenObject(String e1, String e2, String rel) {
        event1ID = e1;
        event2ID = e2;
        relation = rel;
    }

    public String getEvent1ID() {
        return event1ID;
    }

    public String getEvent2ID() {
        return event2ID;
    }

    public String getRelation() {
        return relation;
    }

}
