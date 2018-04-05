package de.rgzm.alligator.allen;

import de.rgzm.alligator.classes.AlligatorEvent;
import java.util.ArrayList;
import java.util.List;

public class AllenIA {

    /**
     * get Allen relation as short sign following Freksa's "Temporal Reasoning
     * Based on Semi-Intervals" '92
     *
     * @param a1 start event 1
     * @param b1 end event 1
     * @param a2 start event 2
     * @param b2 end event 2
     * @return list of short Allen relation signs
     */
    public static List<String> getAllenRelationSigns(double a1, double b1, double a2, double b2) {
        List<String> relations = new ArrayList();
        if (b1 < a2 && b1 < b2) {
            relations.add("<"); // 1 before 2
        }
        if (a1 < b2 && a1 < a2) {
            relations.add(">"); // 1 after 2
        }
        if (b1 == a2 && b1 < b2) {
            relations.add("m"); // 1 meets 2
        }
        if (a1 == b2 && a1 < a2) {
            relations.add("mi"); // 1 met-by 2
        }
        if (b1 > a2 && b1 < b2) {
            relations.add("o"); // 1 overlaps 2
        }
        if (a1 < b2 && a1 > a2) {
            relations.add("oi"); // 1 overlapped-by 2
        }
        if (a1 == a2 && b1 < b2) {
            relations.add("s"); // 1 starts 2
        }
        if (a1 == a2 && b1 > b2) {
            relations.add("si"); // 1 started-by 2
        }
        if (b1 == b2 && a1 > a2) {
            relations.add("f"); // 1 finishes 2
        }
        if (b1 == b2 && a1 < a2) {
            relations.add("fi"); // 1 finished-by 2
        }
        if (a1 < b2 && b1 > a2) {
            relations.add("d"); // 1 during 2
        }
        if (b1 > b2 && a1 < a2) {
            relations.add("di"); // 1 contains 
        }
        if (b1 == b2 && a1 == a2) {
            relations.add("="); // 1 equals 2
        }
        // Freksa (1992) "Temporal Reasoning Based on Semi-Intervals" p.21
        // F1
        if (a1 < a2) {
            relations.add("ol"); // 1 older 2
        }
        if (a1 == a2) {
            relations.add("hh"); // 1 head to head with 2
        }
        if (a1 > a2) {
            relations.add("yo"); // 1 younger 2
        }
        // F2
        if (b1 < b2) {
            relations.add("sb"); // 1 survived by 2
        }
        if (b1 == b2) {
            relations.add("tt"); // 1 tail to tail with 2
        }
        if (b1 > b2) {
            relations.add("sv"); // 1 survives 2
        }
        // F3
        if (a1 < b2) {
            relations.add("bd"); // 1 born before death of 2
        }
        if (b1 > a2) {
            relations.add("db"); // 1 died after birth of 2
        }
        // F4
        if (b1 <= a2) {
            relations.add("pr"); // 1 precedes 2
        }
        if (a2 >= b2) {
            relations.add("sd"); // 1 succeeds 2
        }
        // F5
        if (a1 < b2 && b1 > a2) {
            relations.add("ct"); // 1 contemporary of 2
        }
        // F6
        if (a1 < a2 && b1 < b2) {
            relations.add("ob"); // 1 older and survived by 2
        }
        if (a1 > a2 && b1 > b2) {
            relations.add("ys"); // 1 younger and survives 2
        }
        if (a1 < a2 && b1 > a2) {
            relations.add("oc"); // 1 older contemporary of 2
        }
        if (a1 > a2 && a1 > b2) {
            relations.add("yc"); // 1 younger contemporary of 2
        }
        if (a1 < b2 && b1 > b2) {
            relations.add("sc"); // 1 surviving contemporary of 2
        }
        if (b1 > a2 && b1 < b2) {
            relations.add("bc"); // 1 survived by contemporary of 2
        }
        return relations;
    }

    /**
     * get Allen relation as description following Freksa's "Temporal Reasoning
     * Based on Semi-Intervals" '92
     *
     * @param a1 start event 1
     * @param b1 end event 1
     * @param a2 start event 2
     * @param b2 end event 2
     * @return list of short Allen relation descriptions
     */
    public static List<String> getAllenRelationShortDescriptions(double a1, double b1, double a2, double b2) {
        List<String> relations = new ArrayList();
        if (b1 < a2 && b1 < b2) {
            relations.add("before"); // 1 before 2
        }
        if (a1 < b2 && a1 < a2) {
            relations.add("after"); // 1 after 2
        }
        if (b1 == a2 && b1 < b2) {
            relations.add("meets"); // 1 meets 2
        }
        if (a1 == b2 && a1 < a2) {
            relations.add("met-by"); // 1 met-by 2
        }
        if (b1 > a2 && b1 < b2) {
            relations.add("overlaps"); // 1 overlaps 2
        }
        if (a1 < b2 && a1 > a2) {
            relations.add("overlapped-by"); // 1 overlapped-by 2
        }
        if (a1 == a2 && b1 < b2) {
            relations.add("starts"); // 1 starts 2
        }
        if (a1 == a2 && b1 > b2) {
            relations.add("started-by"); // 1 started-by 2
        }
        if (b1 == b2 && a1 > a2) {
            relations.add("finishes"); // 1 finishes 2
        }
        if (b1 == b2 && a1 < a2) {
            relations.add("finished-by"); // 1 finished-by 2
        }
        if (a1 < b2 && b1 > a2) {
            relations.add("during"); // 1 during 2
        }
        if (b1 > b2 && a1 < a2) {
            relations.add("contains"); // 1 contains 
        }
        if (b1 == b2 && a1 == a2) {
            relations.add("equals"); // 1 equals 2
        }
        // Freksa (1992) "Temporal Reasoning Based on Semi-Intervals" p.21
        // F1
        if (a1 < a2) {
            relations.add("older"); // 1 older 2
        }
        if (a1 == a2) {
            relations.add("head to head with"); // 1 head to head with 2
        }
        if (a1 > a2) {
            relations.add("younger"); // 1 younger 2
        }
        // F2
        if (b1 < b2) {
            relations.add("survived by"); // 1 survived by 2
        }
        if (b1 == b2) {
            relations.add("tail to tail with"); // 1 tail to tail with 2
        }
        if (b1 > b2) {
            relations.add("survives"); // 1 survives 2
        }
        // F3
        if (a1 < b2) {
            relations.add("born before death of"); // 1 born before death of 2
        }
        if (b1 > a2) {
            relations.add("died after birth of"); // 1 died after birth of 2
        }
        // F4
        if (b1 <= a2) {
            relations.add("precedes"); // 1 precedes 2
        }
        if (a2 >= b2) {
            relations.add("succeeds"); // 1 succeeds 2
        }
        // F5
        if (a1 < b2 && b1 > a2) {
            relations.add("contemporary of"); // 1 contemporary of 2
        }
        // F6
        if (a1 < a2 && b1 < b2) {
            relations.add("older and survived by"); // 1 older and survived by 2
        }
        if (a1 > a2 && b1 > b2) {
            relations.add("younger and survives"); // 1 younger and survives 2
        }
        if (a1 < a2 && b1 > a2) {
            relations.add(" older contemporary of"); // 1 older contemporary of 2
        }
        if (a1 > a2 && a1 > b2) {
            relations.add("younger contemporary of"); // 1 younger contemporary of 2
        }
        if (a1 < b2 && b1 > b2) {
            relations.add("surviving contemporary of"); // 1 surviving contemporary of 2
        }
        if (b1 > a2 && b1 < b2) {
            relations.add("survived by contemporary of"); // 1 survived by contemporary of 2
        }
        return relations;
    }

    /**
     * get Allen relation as OWL property following Time Ontology in OWL, see
     * https://www.w3.org/TR/2017/REC-owl-time-20171019/
     *
     * @param a1 start event 1
     * @param b1 end event 1
     * @param a2 start event 2
     * @param b2 end event 2
     * @return list of short Allen relation properties
     */
    public static List<String> getAllenRelationProperties(double a1, double b1, double a2, double b2) {
        // https://www.w3.org/TR/2017/REC-owl-time-20171019/
        List<String> relations = new ArrayList();
        if (b1 < a2 && b1 < b2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalBefore"); // 1 before 2
        }
        if (a1 < b2 && a1 < a2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalAfter"); // 1 after 2
        }
        if (b1 == a2 && b1 < b2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalMeets"); // 1 meets 2
        }
        if (a1 == b2 && a1 < a2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalMetBy"); // 1 met-by 2
        }
        if (b1 > a2 && b1 < b2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalOverlaps"); // 1 overlaps 2
        }
        if (a1 < b2 && a1 > a2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalOverlappedBy"); // 1 overlapped-by 2
        }
        if (a1 == a2 && b1 < b2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalStarts"); // 1 starts 2
        }
        if (a1 == a2 && b1 > b2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalStartedBy"); // 1 started-by 2
        }
        if (b1 == b2 && a1 > a2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalFinishes"); // 1 finishes 2
        }
        if (b1 == b2 && a1 < a2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalFinishedBy"); // 1 finished-by 2
        }
        if (a1 < b2 && b1 > a2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalDuring"); // 1 during 2
        }
        if (b1 > b2 && a1 < a2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalContains"); // 1 contains 
        }
        if (b1 == b2 && a1 == a2) {
            relations.add("https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalEquals"); // 1 equals 2
        }
        return relations;
    }

    /**
     * get Allen relation as description following Freksa's "Temporal Reasoning
     * Based on Semi-Intervals" '92
     *
     * @param a1 start event 1
     * @param b1 end event 1
     * @param a2 start event 2
     * @param b2 end event 2
     * @return list of short Allen relation descriptions
     */
    public static List<String> getAllenRelationCypherProperties(double a1, double b1, double a2, double b2, AlligatorEvent one, AlligatorEvent two) {
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
        return relations;
    }

}
