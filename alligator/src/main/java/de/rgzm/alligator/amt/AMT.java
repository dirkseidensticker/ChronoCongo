package de.rgzm.alligator.amt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    public void queryStore(String query, String callback) throws MalformedURLException, IOException {
        URL obj = new URL(STORE + "?queryLn=SPARQL&query=PREFIX amt: <" + PREFIX + "> " + query);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Accept-Encoding", "*");
        if (con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
             BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        }
    }

    /*
    var queryStore = function(query,callback) {
		$.ajax({
			url: STORE,
			dataType: 'jsonp',
			type: 'GET',
			data: {
				queryLn: 'SPARQL',
				query: "PREFIX amt: <"+PREFIX+"> " + query,
				Accept: 'application/json'
			},
			success: function(data) {
				var bindings = data.results.bindings;
				for (var i in bindings) {
					for (var j in bindings[i]) {
						if (bindings[i][j].value)
							bindings[i][j] = bindings[i][j].value;
					}
				}
				callback(bindings);
			},
			error: function(data) {
				console.log("Es ist ein Fehler aufgetreten: "+data);
				callback([]);
			}
		});
	};
     */
}
