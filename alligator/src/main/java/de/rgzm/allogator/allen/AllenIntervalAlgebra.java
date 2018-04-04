package de.rgzm.allogator.allen;

public class AllenIntervalAlgebra {

    /**
     * get Allen relation as short sign following Freksa's "Temporal Reasoning
     * Based on Semi-Intervals" '92
     *
     * @param a1 start event 1
     * @param b1 end event 1
     * @param a2 start event 2
     * @param b2 end event 2
     * @return short Allen relation sign
     */
    public static String getRelationSign(double a1, double b1, double a2, double b2) {
        if (b1 < a2 && b1 < b2) {
            return "<"; // 1 before 2
        } else if (a1 < b2 && a1 < a2) {
            return ">"; // 1 after 2
        } else if (b1 == a2 && b1 < b2) {
            return "m"; // 1 meets 2
        } else if (a1 == b2 && a1 < a2) {
            return "mi"; // 1 met-by 2
        } else if (b1 > a2 && b1 < b2) {
            return "o"; // 1 overlaps 2
        } else if (a1 < b2 && a1 > a2) {
            return "oi"; // 1 overlapped-by 2
        } else if (a1 == a2 && b1 < b2) {
            return "s"; // 1 starts 2
        } else if (a1 == a2 && b1 > b2) {
            return "si"; // 1 started-by 2
        } else if (b1 == b2 && a1 > a2) {
            return "f"; // 1 finishes 2
        } else if (b1 == b2 && a1 < a2) {
            return "fi"; // 1 finished-by 2
        } else if (a1 < b2 && b1 > a2) {
            return "d"; // 1 during 2
        } else if (b1 > b2 && a1 < a2) {
            return "di"; // 1 contains 2
        } else if (b1 == b2 && a1 == a2) {
            return "="; // 1 equals 2
        } else {
            return null;
        }
    }

    /**
     * get Allen relation as description following Freksa's "Temporal Reasoning
     * Based on Semi-Intervals" '92
     *
     * @param a1 start event 1
     * @param b1 end event 1
     * @param a2 start event 2
     * @param b2 end event 2
     * @return short Allen relation sign
     */
    public static String getRelationShortDescription(double a1, double b1, double a2, double b2) {
        if (b1 < a2 && b1 < b2) {
            return "before"; // 1 before 2
        } else if (a1 < b2 && a1 < a2) {
            return "after"; // 1 after 2
        } else if (b1 == a2 && b1 < b2) {
            return "meets"; // 1 meets 2
        } else if (a1 == b2 && a1 < a2) {
            return "met-by"; // 1 met-by 2
        } else if (b1 > a2 && b1 < b2) {
            return "overlaps"; // 1 overlaps 2
        } else if (a1 < b2 && a1 > a2) {
            return "overlapped-by"; // 1 overlapped-by 2
        } else if (a1 == a2 && b1 < b2) {
            return "starts"; // 1 starts 2
        } else if (a1 == a2 && b1 > b2) {
            return "started-by"; // 1 started-by 2
        } else if (b1 == b2 && a1 > a2) {
            return "finishes"; // 1 finishes 2
        } else if (b1 == b2 && a1 < a2) {
            return "finished-by"; // 1 finished-by 2
        } else if (a1 < b2 && b1 > a2) {
            return "during"; // 1 during 2
        } else if (b1 > b2 && a1 < a2) {
            return "contains"; // 1 contains 2
        } else if (b1 == b2 && a1 == a2) {
            return "equals"; // 1 equals 2
        } else {
            return null;
        }
    }

    /**
     * get Allen relation as OWL property following Time Ontology in OWL, see
     * https://www.w3.org/TR/2017/REC-owl-time-20171019/
     *
     * @param a1 start event 1
     * @param b1 end event 1
     * @param a2 start event 2
     * @param b2 end event 2
     * @return short Allen relation sign
     */
    public static String getRelationProperty(double a1, double b1, double a2, double b2) {
        // https://www.w3.org/TR/2017/REC-owl-time-20171019/
        if (b1 < a2 && b1 < b2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalBefore"; // 1 before 2
        } else if (a1 < b2 && a1 < a2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalAfter"; // 1 after 2
        } else if (b1 == a2 && b1 < b2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalMeets"; // 1 meets 2
        } else if (a1 == b2 && a1 < a2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalMetBy"; // 1 met-by 2
        } else if (b1 > a2 && b1 < b2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalOverlaps"; // 1 overlaps 2
        } else if (a1 < b2 && a1 > a2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalOverlappedBy"; // 1 overlapped-by 2
        } else if (a1 == a2 && b1 < b2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalStarts"; // 1 starts 2
        } else if (a1 == a2 && b1 > b2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalStartedBy"; // 1 started-by 2
        } else if (b1 == b2 && a1 > a2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalFinishes"; // 1 finishes 2
        } else if (b1 == b2 && a1 < a2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalFinishedBy"; // 1 finished-by 2
        } else if (a1 < b2 && b1 > a2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalDuring"; // 1 during 2
        } else if (b1 > b2 && a1 < a2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalContains"; // 1 contains 2
        } else if (b1 == b2 && a1 == a2) {
            return "https://www.w3.org/TR/2017/REC-owl-time-20171019/#time:intervalEquals"; // 1 equals 2
        } else {
            return null;
        }
    }

}
