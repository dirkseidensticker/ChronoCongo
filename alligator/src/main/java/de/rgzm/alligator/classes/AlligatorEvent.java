package de.rgzm.alligator.classes;

import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONObject;

public class AlligatorEvent {

    public String id = "";
    public String name = "";
    public double x = -42.0;
    public double y = -42.0;
    public double z = -42.0;
    public double a = -42.0;
    public double b = -42.0;
    public boolean fixed = false;
    public boolean startFixed = false;
    public boolean endFixed = false;
    public HashMap distances;
    public HashMap distancesNormalised;
    public JSONObject nextFixedEndNeighbour = new JSONObject();
    public String schwebend = null;
    //public HashMap angels;
    //public HashMap angelsNormalised;
    public List<AllenObject> allenRelations;
    
    public String toString() {
        return id + " " + name + " " + x + " " + y + " " + z + " " + a + " " + b + " " + fixed + " " + startFixed + " " + endFixed;
    }

}
