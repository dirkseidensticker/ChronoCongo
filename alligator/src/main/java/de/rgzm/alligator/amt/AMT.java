package de.rgzm.alligator.amt;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AMT {
    
    public JSONArray CONCEPTS = new JSONArray();
	public JSONArray ROLES = new JSONArray();
    public JSONObject original = new JSONObject();
    public JSONArray original_nodes = new JSONArray();
    public JSONArray original_edges = new JSONArray();
    public JSONObject edited = new JSONObject();
    public JSONArray edited_nodes = new JSONArray();
    public JSONArray edited_edges = new JSONArray();
	public JSONObject GRAPH = new JSONObject();
    public JSONArray AXIOMS = new JSONArray();
    public String PREFIX = "http://academic-meta-tool.xyz/vocab#";
    public String STORE = "";
    
    public AMT(String store) {
        original.put("nodes", original_nodes);
        original.put("edges", original_edges);
        GRAPH.put("original", original);
        edited.put("nodes", edited_nodes);
        edited.put("edges", edited_edges);
        GRAPH.put("original", edited);
        STORE = store;
    }
    
}
