package de.rgzm.alligator.amt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

    public JSONObject loadGraph() throws IOException, MalformedURLException, ParseException {
        // load data
        CONCEPTS = queryStore("SELECT ?concept ?label ?placeholder WHERE { ?concept rdf:type amt:Concept . ?concept rdfs:label ?label . ?concept amt:placeholder ?placeholder . }");
        System.out.println(CONCEPTS);
        ROLES = queryStore("SELECT ?role ?label ?domain ?range WHERE { ?role rdf:type amt:Role . ?role rdfs:label ?label . ?role rdfs:domain ?domain . ?role rdfs:range ?range . }");
        System.out.println(ROLES);
        return GRAPH;
    }
    
    private JSONArray queryStore(String query) throws MalformedURLException, IOException, ParseException {
        String q = "?query=" + URLEncoder.encode("PREFIX amt: <" + PREFIX + "> " + query, "UTF-8");
        URL obj = new URL(STORE + q);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        if (con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));
            String inputLine;
            String data = "";
            while ((inputLine = in.readLine()) != null) {
                data += inputLine;
            }
            in.close();
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(data.toString());
            JSONObject results = (JSONObject) jsonObject.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");
            for (Object i : bindings) {
                JSONObject tmp1 = (JSONObject) i;
                for (Iterator iterator = tmp1.keySet().iterator(); iterator.hasNext();) {
                    String key = (String) iterator.next();
                    JSONObject value = (JSONObject) tmp1.get(key);
                    tmp1.put(key, value.get("value"));
                }
            }
            return bindings;
        } else {
            return new JSONArray();
        }
    }

}
