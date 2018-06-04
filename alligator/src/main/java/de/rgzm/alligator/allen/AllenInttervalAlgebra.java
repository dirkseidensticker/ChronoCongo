package de.rgzm.alligator.allen;

import java.util.ArrayList;
import java.util.List;

public class AllenInttervalAlgebra {

    /**
     * get Allen relation as short sign following Freksa's "Temporal Reasoning
     * Based on Semi-Intervals" '92
     *
     * @param anfang1 start event 1
     * @param ende1 end event 1
     * @param anfang2 start event 2
     * @param ende2 end event 2
     * @return list of short Allen relation signs
     */
    public static List<String> getAllenRelationSigns(double anfang1, double ende1, double anfang2, double ende2) {
        List<String> relations = new ArrayList();
        if (anfang1 < ende1 && ende1 < anfang2 && anfang2 < ende2) {
            relations.add("<"); // 1 before 2
        }
        if (anfang2 < ende2 && ende2 < anfang1 && anfang1 < ende1) {
            relations.add(">"); // 1 after 2
        }
        if (anfang1 < ende1 && ende1 == anfang2 && anfang2 < ende2) {
            relations.add("m"); // 1 meets 2
        }
        if (anfang2 < ende2 && ende2 == anfang1 && anfang1 < ende1) {
            relations.add("mi"); // 1 met-by 2
        }
        if (anfang1 < anfang2 && anfang2 < ende1 && ende1 < ende2) {
            relations.add("o"); // 1 overlaps 2
        }
        if (anfang2 < anfang1 && anfang1 < ende2 && ende2 < ende1) {
            relations.add("oi"); // 1 overlapped-by 2
        }
        if (anfang1 == anfang2 && anfang2 < ende1 && ende1 < ende2) {
            relations.add("s"); // 1 starts 2
        }
        if (anfang1 == anfang2 && anfang2 < ende2 && ende2 < ende1) {
            relations.add("si"); // 1 started-by 2
        }
        if (anfang2 < anfang1 && anfang1 < ende2 && ende2 == ende1) {
            relations.add("f"); // 1 finishes 2
        }
        if (anfang1 < anfang2 && anfang2 < ende2 && ende2 == ende1) {
            relations.add("fi"); // 1 finished-by 2
        }
        if (anfang2 < anfang1 && anfang1 < ende1 && ende1 < ende2) {
            relations.add("d"); // 1 during 2
        }
        if (anfang1 < anfang2 && anfang2 < ende2 && ende2 < ende1) {
            relations.add("di"); // 1 contains 2
        }
        if (ende1 == ende2 && anfang1 == anfang2) {
            relations.add("="); // 1 equals 2
        }
        // point data
        /*if (anfang1 == ende1 && anfang2 < anfang1) { 
            relations.add("D");
        }
        if (anfang2 == ende2 && anfang2 < anfang1) { 
            relations.add("DI");
        }
        if (relations.contains("=") && relations.contains("DI") && relations.contains("D")) {
            //relations.remove("DI");
            //relations.remove("D");
        }*/
        return relations;
    }

    /**
     * get Allen relation as description following Freksa's "Temporal Reasoning
     * Based on Semi-Intervals" '92
     *
     * @param allenSign allen sign
     * @return list of short Allen relation descriptions
     */
    public static String getAllenRelationShortDescriptions(String allenSign) {
        if (allenSign.equals("<")) {
            return "before";
        }
        if (allenSign.equals(">")) {
            return "after";
        }
        if (allenSign.equals("m")) {
            return "meets";
        }
        if (allenSign.equals("mi")) {
            return "met-by";
        }
        if (allenSign.equals("o")) {
            return "overlaps";
        }
        if (allenSign.equals("oi")) {
            return "overlapped-by";
        }
        if (allenSign.equals("s")) {
            return "starts";
        }
        if (allenSign.equals("si")) {
            return "started-by";
        }
        if (allenSign.equals("f")) {
            return "finishes";
        }
        if (allenSign.equals("fi")) {
            return "finished-by";
        }
        if (allenSign.equals("d")) {
            return "during";
        }
        if (allenSign.equals("di")) {
            return "contains";
        }
        if (allenSign.equals("=")) {
            return "equals";
        }
        // Freksa (1992) "Temporal Reasoning Based on Semi-Intervals" p.21
        if (allenSign.equals("ol")) {
            return "older";
        }
        if (allenSign.equals("hh")) {
            return "head to head with";
        }
        if (allenSign.equals("yo")) {
            return "younger";
        }
        if (allenSign.equals("sb")) {
            return "survived by";
        }
        if (allenSign.equals("tt")) {
            return "tail to tail with";
        }
        if (allenSign.equals("sv")) {
            return "survived by";
        }
        if (allenSign.equals("sb")) {
            return "survives";
        }
        if (allenSign.equals("pr")) {
            return "precedes";
        }
        if (allenSign.equals("bd")) {
            return "born before death of";
        }
        if (allenSign.equals("ct")) {
            return "contemporary of";
        }
        if (allenSign.equals("db")) {
            return "died after birth of";
        }
        if (allenSign.equals("sd")) {
            return "succeeds";
        }
        if (allenSign.equals("ob")) {
            return "older & survived by";
        }
        if (allenSign.equals("oc")) {
            return "older contemporary of";
        }
        if (allenSign.equals("sc")) {
            return "surviving contemporary of";
        }
        if (allenSign.equals("bc")) {
            return "survived by contemporary of";
        }
        if (allenSign.equals("yc")) {
            return "younger contemporary of";
        }
        if (allenSign.equals("ys")) {
            return "younger & survives";
        }
        return null;
    }

    /**
     * get Allen relation as OWL property following Time Ontology in OWL, see
     * https://www.w3.org/TR/2017/REC-owl-time-20171019/ and
     * https://www.w3.org/2006/time
     *
     * @param allenSign allen sign
     * @return list of short Allen relation properties
     */
    public static String getAllenRelationProperties(String allenSign) {
        // https://www.w3.org/TR/2017/REC-owl-time-20171019/
        if (allenSign.equals("<")) {
            return "http://www.w3.org/2006/time#intervalBefore";
        }
        if (allenSign.equals(">")) {
            return "http://www.w3.org/2006/time#intervalAfter";
        }
        if (allenSign.equals("m")) {
            return "http://www.w3.org/2006/time#intervalMeets";
        }
        if (allenSign.equals("mi")) {
            return "http://www.w3.org/2006/time#intervalMetBy";
        }
        if (allenSign.equals("o")) {
            return "http://www.w3.org/2006/time#intervalOverlaps";
        }
        if (allenSign.equals("oi")) {
            return "http://www.w3.org/2006/time#intervalOverlappedBy";
        }
        if (allenSign.equals("s")) {
            return "http://www.w3.org/2006/time#intervalStarts";
        }
        if (allenSign.equals("si")) {
            return "http://www.w3.org/2006/time#intervalStartedBy";
        }
        if (allenSign.equals("f")) {
            return "http://www.w3.org/2006/time#intervalFinishes";
        }
        if (allenSign.equals("fi")) {
            return "http://www.w3.org/2006/time#intervalFinishedBy";
        }
        if (allenSign.equals("d")) {
            return "http://www.w3.org/2006/time#intervalDuring";
        }
        if (allenSign.equals("di")) {
            return "http://www.w3.org/2006/time#intervalContains";
        }
        if (allenSign.equals("=")) {
            return "http://www.w3.org/2006/time#intervalEquals";
        }
        return null;
    }

    /**
     * get Allen relation as description following Freksa's "Temporal Reasoning
     * Based on Semi-Intervals" '92
     *
     * @param a1 start event 1
     * @param b1 end event 1
     * @param a2 start event 2
     * @param b2 end event 2
     * @param one AlligatorEvent 1
     * @param two AlligatorEvent 2
     * @return list of short Allen relation descriptions
     */
    /*public static List<String> getAllenRelationCypherProperties(double a1, double b1, double a2, double b2, AlligatorEvent one, AlligatorEvent two) {
        List<String> relations = new ArrayList();
        if (b1 < a2 && b1 < b2) {
            relations.add("MERGE (" + one.id + ")-[:BEFORE]->(" + two.id + ")"); // 1 before 2
        }
        if (a1 < b2 && a1 < a2) {
            relations.add("MERGE (" + one.id + ")-[:AFTER]->(" + two.id + ")"); // 1 after 2
        }
        if (b1 == a2 && b1 < b2) {
            relations.add("MERGE (" + one.id + ")-[:MEETS]->(" + two.id + ")"); // 1 meets 2
        }
        if (a1 == b2 && a1 < a2) {
            relations.add("MERGE (" + one.id + ")-[:MET_BY]->(" + two.id + ")"); // 1 met-by 2
        }
        if (b1 > a2 && b1 < b2) {
            relations.add("MERGE (" + one.id + ")-[:OVERLAPS]->(" + two.id + ")"); // 1 overlaps 2
        }
        if (a1 < b2 && a1 > a2) {
            relations.add("MERGE (" + one.id + ")-[:OVERLAPPED_BY]->(" + two.id + ")"); // 1 overlapped-by 2
        }
        if (a1 == a2 && b1 < b2) {
            relations.add("MERGE (" + one.id + ")-[:STARTS]->(" + two.id + ")"); // 1 starts 2
        }
        if (a1 == a2 && b1 > b2) {
            relations.add("MERGE (" + one.id + ")-[:STARTED_BY]->(" + two.id + ")"); // 1 started-by 2
        }
        if (b1 == b2 && a1 > a2) {
            relations.add("MERGE (" + one.id + ")-[:FINISHES]->(" + two.id + ")"); // 1 finishes 2
        }
        if (b1 == b2 && a1 < a2) {
            relations.add("MERGE (" + one.id + ")-[:FINISHED_BY]->(" + two.id + ")"); // 1 finished-by 2
        }
        if (a1 < b2 && b1 > a2) {
            relations.add("MERGE (" + one.id + ")-[:DURING]->(" + two.id + ")"); // 1 during 2
        }
        if (b1 > b2 && a1 < a2) {
            relations.add("MERGE (" + one.id + ")-[:CONTAINS]->(" + two.id + ")"); // 1 contains 
        }
        if (b1 == b2 && a1 == a2) {
            relations.add("MERGE (" + one.id + ")-[:EQUALS]->(" + two.id + ")"); // 1 equals 2
        }
        // Freksa (1992) "Temporal Reasoning Based on Semi-Intervals" p.21
        // F1
        if (a1 < a2) {
            relations.add("MERGE (" + one.id + ")-[:OLDER]->(" + two.id + ")"); // 1 older 2
        }
        if (a1 == a2) {
            relations.add("MERGE (" + one.id + ")-[:HEAD_TO_HEAD_WITH]->(" + two.id + ")"); // 1 head to head with 2
        }
        if (a1 > a2) {
            relations.add("MERGE (" + one.id + ")-[:YOUNGER]->(" + two.id + ")"); // 1 younger 2
        }
        // F2
        if (b1 < b2) {
            relations.add("MERGE (" + one.id + ")-[:SURVIVED_BY]->(" + two.id + ")"); // 1 survived by 2
        }
        if (b1 == b2) {
            relations.add("MERGE (" + one.id + ")-[:TAIL_TO_TAIL_WITH]->(" + two.id + ")"); // 1 tail to tail with 2
        }
        if (b1 > b2) {
            relations.add("MERGE (" + one.id + ")-[:SURVIVES]->(" + two.id + ")"); // 1 survives 2
        }
        // F3
        if (a1 < b2) {
            relations.add("MERGE (" + one.id + ")-[:BORN_BEFORE_DEATH_OF]->(" + two.id + ")"); // 1 born before death of 2
        }
        if (b1 > a2) {
            relations.add("MERGE (" + one.id + ")-[:DIED_AFTER_BIRTH_OF]->(" + two.id + ")"); // 1 died after birth of 2
        }
        // F4
        if (b1 <= a2) {
            relations.add("MERGE (" + one.id + ")-[:PRECEDES]->(" + two.id + ")"); // 1 precedes 2
        }
        if (a2 >= b2) {
            relations.add("MERGE (" + one.id + ")-[:SUCCEEDS]->(" + two.id + ")"); // 1 succeeds 2
        }
        // F5
        if (a1 < b2 && b1 > a2) {
            relations.add("MERGE (" + one.id + ")-[:CONTEMPORARY_OF]->(" + two.id + ")"); // 1 contemporary of 2
        }
        // F6
        if (a1 < a2 && b1 < b2) {
            relations.add("MERGE (" + one.id + ")-[:OLDER_AND_SURVIVED_BY]->(" + two.id + ")"); // 1 older and survived by 2
        }
        if (a1 > a2 && b1 > b2) {
            relations.add("MERGE (" + one.id + ")-[:YOUNGER_AND_SURVIVES]->(" + two.id + ")"); // 1 younger and survives 2
        }
        if (a1 < a2 && b1 > a2) {
            relations.add("MERGE (" + one.id + ")-[:OLDER_CONTEMPORARY_OF]->(" + two.id + ")"); // 1 older contemporary of 2
        }
        if (a1 > a2 && a1 > b2) {
            relations.add("MERGE (" + one.id + ")-[:YOUNGER_CONTEMPORARY_OF]->(" + two.id + ")"); // 1 younger contemporary of 2
        }
        if (a1 < b2 && b1 > b2) {
            relations.add("MERGE (" + one.id + ")-[:SURVIVING_CONTEMPORARY_OF]->(" + two.id + ")"); // 1 surviving contemporary of 2
        }
        if (b1 > a2 && b1 < b2) {
            relations.add("MERGE (" + one.id + ")-[:SURVIVED_BY_CONTEMPORARY_OF]->(" + two.id + ")"); // 1 survived by contemporary of 2
        }
        return relations;
    }*/
}
